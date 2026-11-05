package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import com.eduaircontrol.msmonitoring.monitoring.application.AlertService;
import com.eduaircontrol.msmonitoring.monitoring.application.ThresholdService;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

/**
 * Consume {@code EnvironmentalDataRecorded} y lo proyecta en el read model.
 *
 * <p><b>Idempotencia.</b> La garantiza el patron documentado para el dominio: el
 * consumidor guarda el {@code eventId} en la misma transaccion en que aplica el
 * efecto, y descarta el mensaje si ya lo vio. La entrega es at-least-once, asi que
 * un reintento del broker llega aqui otra vez.
 *
 * <p>Hay un segundo nivel de deduplicacion en la tabla, por la clave natural
 * {@code (environment_id, variable_id, measured_at)}. Hace falta porque el backfill
 * historico puede traer la misma lectura por un camino distinto, con otro
 * {@code eventId}.
 *
 * <p>Un mensaje invalido revienta con excepcion. Con la politica de reintentos del
 * contenedor, tras agotarlos pasa a la cola de letras muertas: un payload corrupto
 * no se arregla reintentandolo.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MeasurementEventListener {

    private static final String CONSUMER = "environmental-measurement-projection";
    private static final int CHUNK = 500;

    private static final String INSERT_SQL = """
            INSERT INTO environment_monitoring.environment_measurement
                (environment_measurement_id, sensor_installation_id, variable_id,
                 measured_value, measured_at, quality_flag_id)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (sensor_installation_id, variable_id, measured_at) DO NOTHING
            """;

    private final InboxRepository inbox;
    private final JdbcTemplate jdbc;
    private final JsonMapper jsonMapper;
    private final EnvironmentalReadingMapper readingMapper;
    private final ThresholdService thresholdService;
    private final AlertService alertService;

    @RabbitListener(
            queues = "${app.messaging.measurements-queue}",
            containerFactory = "measurementListenerContainerFactory")
    @Transactional
    public void onEnvironmentalDataRecorded(Message message) {
        EnvironmentalDataRecorded event = parse(message);
        UUID eventId = resolveEventId(message, event);

        if (inbox.claim(eventId, CONSUMER, EnvironmentalDataRecorded.TYPE) == 0) {
            log.debug("Evento {} ya procesado; se descarta el reintento", eventId);
            return;
        }

        List<EnvironmentalReadingMapper.Reading> readings = readingMapper.toReadings(event);
        List<ProjectedReading> projected = project(readings);
        log.debug("Evento {} procesado: {} lectura(s) nuevas", eventId, projected.size());

        // Evaluacion de umbrales y generacion de alertas por cada lectura nueva.
        for (ProjectedReading item : projected) {
            evaluateAndAlert(item);
        }
    }

    private EnvironmentalDataRecorded parse(Message message) {
        String body = message.getBody() == null ? null
                : new String(message.getBody(), java.nio.charset.StandardCharsets.UTF_8);
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Evento de datos ambientales vacio");
        }
        EnvironmentalDataRecorded event =
                jsonMapper.readValue(body, EnvironmentalDataRecorded.class);
        if (event == null || event.payload() == null) {
            throw new IllegalArgumentException("Evento sin payload: " + body);
        }
        return event;
    }

    /**
     * El {@code eventId} del cuerpo es el que vale, porque es el que el contrato
     * declara unico e idempotente. El metadato del broker se usa de respaldo para no
     * perder el mensaje si un productor se olvida del campo.
     */
    private UUID resolveEventId(Message message, EnvironmentalDataRecorded event) {
        if (event.eventId() != null) {
            return event.eventId();
        }
        String messageId = message.getMessageProperties().getMessageId();
        if (messageId != null && !messageId.isBlank()) {
            try {
                return UUID.fromString(messageId);
            } catch (IllegalArgumentException e) {
                log.warn("messageId no es un UUID ({}); no se puede deduplicar", messageId);
            }
        }
        throw new IllegalArgumentException(
                "Evento sin eventId ni messageId: no se puede deduplicar");
    }

    private List<ProjectedReading> project(List<EnvironmentalReadingMapper.Reading> readings) {
        List<ProjectedReading> projected = new ArrayList<>();
        if (readings.isEmpty()) {
            return projected;
        }

        UUID qualityFlagId = qualityFlagId("VALID");
        for (int start = 0; start < readings.size(); start += CHUNK) {
            List<Object[]> batch = new ArrayList<>();
            List<ProjectedReading> chunk = new ArrayList<>();
            for (EnvironmentalReadingMapper.Reading reading
                    : readings.subList(start, Math.min(start + CHUNK, readings.size()))) {
                // La instalacion se resuelve por la proyeccion local. Si no hay
                // proyeccion para el ambiente, la lectura no se puede atribuir y se
                // descarta (limitacion documentada en DEC-006).
                UUID installationId = installationIdFor(reading.environmentId());
                if (installationId == null) {
                    log.warn("Sin proyeccion para el ambiente {}; lectura descartada",
                            reading.environmentId());
                    continue;
                }
                UUID measurementId = UUID.randomUUID();
                batch.add(new Object[]{
                        measurementId,
                        installationId,
                        reading.variableId(),
                        reading.value(),
                        Timestamp.from(reading.measuredAt()),
                        qualityFlagId});
                chunk.add(new ProjectedReading(
                        measurementId, reading.environmentId(), reading.variableId(),
                        reading.value(), reading.measuredAt()));
            }
            if (!batch.isEmpty()) {
                jdbc.batchUpdate(INSERT_SQL, batch);
                projected.addAll(chunk);
            }
        }
        return projected;
    }

    /**
     * Evalua la lectura contra el umbral activo y genera la alerta si hay excedencia.
     * Idempotente: si la medicion ya genero una alerta activa, no se duplica.
     */
    private void evaluateAndAlert(ProjectedReading item) {
        UUID environmentTypeId = environmentTypeOf(item.environmentId());
        if (environmentTypeId == null) {
            return;
        }
        thresholdService.evaluate(environmentTypeId, item.variableId(), item.value(),
                        item.measuredAt().atZone(ZoneOffset.UTC).toLocalDate())
                .ifPresent(evaluation -> alertService.raiseIfAbsent(
                        item.environmentId(), item.variableId(),
                        evaluation.thresholdId(), item.measurementId(),
                        evaluation.severityCode(), item.measuredAt()));
    }

    private UUID environmentTypeOf(UUID environmentId) {
        return jdbc.query("""
                        select environment_type_id
                        from environment_monitoring.educational_environment
                        where educational_environment_id = ?
                        """,
                        (rs, rowNum) -> rs.getObject("environment_type_id", UUID.class),
                        environmentId)
                .stream().findFirst().orElse(null);
    }

    private record ProjectedReading(UUID measurementId, UUID environmentId, UUID variableId,
                                    java.math.BigDecimal value, java.time.Instant measuredAt) {
    }

    /**
     * Primera instalacion activa del ambiente segun la proyeccion local.
     *
     * <p>Limitacion conocida: si hay varias instalaciones activas, se usa la primera.
     * La agregacion por ambiente no se ve afectada porque agrupa por ambiente, no por
     * instalacion.
     */
    private UUID installationIdFor(UUID environmentId) {
        return jdbc.query("""
                        select sensor_installation_id
                        from environment_monitoring.installation_projection
                        where educational_environment_id = ?
                          and removed_at is null
                        order by sensor_installation_id
                        limit 1
                        """,
                        (rs, rowNum) -> rs.getObject("sensor_installation_id", UUID.class),
                        environmentId)
                .stream().findFirst().orElse(null);
    }

    private UUID qualityFlagId(String code) {
        return jdbc.query("""
                        select quality_flag_id from environment_monitoring.quality_flag
                        where code = ?
                        """,
                        (rs, rowNum) -> rs.getObject("quality_flag_id", UUID.class),
                        code)
                .stream().findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "quality_flag no sembrado: " + code));
    }
}