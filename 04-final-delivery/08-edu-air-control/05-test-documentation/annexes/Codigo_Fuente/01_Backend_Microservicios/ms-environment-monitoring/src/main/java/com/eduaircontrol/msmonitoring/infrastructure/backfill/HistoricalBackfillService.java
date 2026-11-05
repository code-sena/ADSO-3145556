package com.eduaircontrol.msmonitoring.infrastructure.backfill;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Copia mediciones historicas al read model de ms-environment-monitoring.
 *
 * <p>Nace de un hueco concreto: el seed de desarrollo inserta ~93k filas
 * directamente en {@code monitoring.environment_measurement}, saltandose la ingesta
 * y por tanto el outbox. Sin esta copia el read model arrancaria vacio y el
 * historial del dashboard mostraria cero datos sin fallar.
 *
 * <p>ADR-003 permite una sola instancia PostgreSQL con un esquema por servicio, asi
 * que el origen ({@code monitoring}) y el destino ({@code environment_monitoring})
 * estan en la misma base. Por eso hace falta un unico datasource y no una conexion
 * aparte.
 *
 * <p>El acoplamiento es temporal y con fecha de caducidad: en cuanto el historico
 * este copiado, la unica via es el broker y este componente puede retirarse.
 *
 * <p>La copia es <b>idempotente</b>: resuelve el conflicto por
 * {@code (environment_id, variable_id, measured_at)} y actualiza el valor, asi que
 * repetirla no duplica filas y una correccion del origen se propaga.
 */
@Slf4j
@Service
public class HistoricalBackfillService {

    private static final int CHUNK = 500;

    /**
     * Lectura en el esquema del monolito. Se copia el installation_id tal cual
     * porque es una referencia logica, no una FK fisica.
     */
    private static final String SOURCE_SQL = """
            SELECT m.environment_measurement_id AS source_id,
                   m.sensor_installation_id AS installation_id,
                   m.variable_id AS variable_id,
                   m.measured_value AS measured_value,
                   m.measured_at AS measured_at,
                   qf.code AS quality_flag
            FROM monitoring.environment_measurement m
            JOIN sensors.sensor_installation si
                ON si.sensor_installation_id = m.sensor_installation_id
            LEFT JOIN monitoring.quality_flags qf
                ON qf.quality_flag_id = m.quality_flag_id
            WHERE si.removed_at IS NULL
              AND m.measured_at >= ?
              AND m.measured_at < ?
            ORDER BY m.measured_at
            """;

    /**
     * Resolucion de conflicto por la clave natural del read model. Actualiza el
     * valor en vez de ignorarlo, para que una correccion en el origen llegue aqui.
     */
    private static final String TARGET_SQL = """
            INSERT INTO environment_monitoring.environment_measurement
                (environment_measurement_id, sensor_installation_id, variable_id,
                 measured_value, measured_at, quality_flag_id)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT (sensor_installation_id, variable_id, measured_at)
            DO UPDATE SET measured_value = EXCLUDED.measured_value,
                          quality_flag_id = EXCLUDED.quality_flag_id
            """;

    /**
     * La proyeccion tambien se puebla desde el backfill: sin ella, las mediciones
     * copiadas no se pueden atribuir a ningun ambiente.
     */
    private static final String PROJECTION_SQL = """
            INSERT INTO environment_monitoring.installation_projection
                (sensor_installation_id, educational_environment_id,
                 environment_type_id, sensor_id)
            SELECT si.sensor_installation_id, si.educational_environment_id,
                   e.environment_type_id, si.sensor_id
            FROM sensors.sensor_installation si
            JOIN classrooms.educational_environment e
                ON e.educational_environment_id = si.educational_environment_id
            WHERE si.removed_at IS NULL
            ON CONFLICT (sensor_installation_id) DO UPDATE SET
                educational_environment_id = EXCLUDED.educational_environment_id,
                environment_type_id = EXCLUDED.environment_type_id,
                sensor_id = EXCLUDED.sensor_id,
                removed_at = NULL,
                synced_at = now()
            """;

    private final JdbcTemplate jdbc;

    public HistoricalBackfillService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Copia la ventana [from, to) y devuelve cuantas filas se escribieron. */
    public int copyWindow(Instant from, Instant to) {
        if (!to.isAfter(from)) {
            throw new IllegalArgumentException(
                    "La ventana del backfill debe tener duracion positiva");
        }

        List<MeasurementRow> rows = jdbc.query(SOURCE_SQL, (rs, rowNum) -> new MeasurementRow(
                        rs.getObject("source_id", UUID.class),
                        rs.getObject("installation_id", UUID.class),
                        rs.getObject("variable_id", UUID.class),
                        rs.getBigDecimal("measured_value"),
                        rs.getTimestamp("measured_at").toInstant(),
                        rs.getString("quality_flag")),
                Timestamp.from(from), Timestamp.from(to));

        // La proyeccion se sincroniza antes que las mediciones: sin ella no se
        // pueden atribuir a ningun ambiente.
        jdbc.update(PROJECTION_SQL);

        if (rows.isEmpty()) {
            log.debug("Backfill: no hay mediciones en [{} a {})", from, to);
            return 0;
        }

        UUID validFlagId = qualityFlagId("VALID");
        UUID suspectFlagId = qualityFlagId("SUSPECT");
        UUID badFlagId = qualityFlagId("BAD");

        int written = 0;
        for (int start = 0; start < rows.size(); start += CHUNK) {
            List<Object[]> batch = new ArrayList<>();
            for (MeasurementRow row : rows.subList(start, Math.min(start + CHUNK, rows.size()))) {
                batch.add(new Object[]{row.sourceId(), row.installationId(), row.variableId(),
                        row.measuredValue(), Timestamp.from(row.measuredAt()),
                        qualityFlagIdFor(row.qualityFlag(), validFlagId, suspectFlagId, badFlagId)});
            }
            written += jdbc.batchUpdate(TARGET_SQL, batch).length;
        }

        log.info("Backfill copio {} medicion(es) de [{} a {})", written, from, to);
        return written;
    }

    /**
     * Mapea el codigo de calidad del monolito al catalogo local. GOOD del monolito
     * equivale a VALID aqui; lo desconocido se marca SUSPECT para revision.
     */
    private UUID qualityFlagIdFor(String sourceCode, UUID valid, UUID suspect, UUID bad) {
        if (sourceCode == null) {
            return valid;
        }
        return switch (sourceCode.toUpperCase()) {
            case "GOOD", "VALID" -> valid;
            case "BAD" -> bad;
            default -> suspect;
        };
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

    private record MeasurementRow(UUID sourceId, UUID installationId, UUID variableId,
                                  java.math.BigDecimal measuredValue, Instant measuredAt,
                                  String qualityFlag) {
    }
}