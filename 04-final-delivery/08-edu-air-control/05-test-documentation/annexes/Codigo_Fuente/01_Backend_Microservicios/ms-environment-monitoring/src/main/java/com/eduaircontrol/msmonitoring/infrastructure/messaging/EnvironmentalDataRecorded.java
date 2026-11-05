package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Contrato del evento {@code EnvironmentalDataRecorded}, segun
 * {@code 02-domain/domain-events.md}.
 *
 * <p>Usa el sobre estandar del dominio (eventId, eventType, aggregateId,
 * aggregateType, occurredAt, version, payload, metadata). El {@code eventId} es lo
 * que garantiza la idempotencia del consumidor, asi que viaja en el cuerpo y no
 * solo como metadato del broker.
 *
 * <p>El payload lleva un campo por variable ambiental en lugar de una lista. Es lo
 * que fija el contrato, y encaja con el grano de la lectura: el dispositivo
 * reporta sus cuatro variables en una sola peticion.
 *
 * <p><b>Ojo:</b> el payload es plano pero el read model es atomico, una fila por
 * variable (regla 4.1 de {@code 06-data/domain/ms-environment-monitoring.md}). El
 * consumidor aplana el sobre a filas; nunca se guarda una fila con varias
 * variables.
 */
public record EnvironmentalDataRecorded(
        UUID eventId,
        String eventType,
        UUID aggregateId,
        String aggregateType,
        Instant occurredAt,
        Integer version,
        Payload payload,
        Metadata metadata) {

    public static final String TYPE = "EnvironmentalDataRecorded";

    public static final String AGGREGATE_TYPE = "EnvironmentalData";

    public static final int SCHEMA_VERSION = 1;

    public static final String ROUTING_KEY = "environmental.data.recorded";

    public record Payload(
            UUID environmentId,
            BigDecimal temperature,
            BigDecimal humidity,
            BigDecimal co2,
            BigDecimal noiseLevel) {
    }

    public record Metadata(
            UUID correlationId,
            UUID causationId,
            UUID userId) {

        public static Metadata empty() {
            return new Metadata(null, null, null);
        }
    }

    /**
 * Normaliza la version del esquema.
 *
 * <p>Se declara como {@code Integer} y no {@code int} a proposito: con un primitivo,
 * un sobre sin {@code version} falla al deserializar antes de llegar aqui. Declararla
 * obligatoria mandaria a la cola de letras muertas los eventos de un productor
 * antiguo que no conoce el campo.
 */
    public EnvironmentalDataRecorded {
        if (version == null || version <= 0) {
            version = SCHEMA_VERSION;
        }
    }
}