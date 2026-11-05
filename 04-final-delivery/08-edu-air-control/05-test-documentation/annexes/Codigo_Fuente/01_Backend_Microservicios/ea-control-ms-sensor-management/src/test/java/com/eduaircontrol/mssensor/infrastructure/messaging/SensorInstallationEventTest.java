package com.eduaircontrol.mssensor.infrastructure.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

/**
 * Contrato del evento de instalacion.
 *
 * <p>La comprobacion de nombres de campo no es cosmética: ms-environment-monitoring
 * deserializa contra su propio {@code SensorInstallationEvent}, un record con estos
 * mismos nombres. Si aqui cambia una propiedad, el consumidor la ignora
 * silenciosamente y la proyeccion {@code installation_projection} se queda vacia.
 *
 * <p>El payload a proposito NO lleva {@code environmentTypeId}: este servicio no
 * posee el catalogo de tipos de ambiente (DEC-007).
 */
class SensorInstallationEventTest {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();

    @Test
    void installedEventCarriesTheInstallationData() {
        UUID installationId = UUID.randomUUID();
        UUID sensorId = UUID.randomUUID();
        UUID environmentId = UUID.randomUUID();
        Instant installedAt = Instant.parse("2026-01-01T10:00:00Z");

        SensorInstallationEvent event = SensorInstallationEvent.installed(
                installationId, sensorId, environmentId, installedAt);

        assertThat(event.eventType()).isEqualTo(SensorInstallationEvent.INSTALLED_TYPE);
        assertThat(event.aggregateId()).isEqualTo(installationId);
        assertThat(event.aggregateType()).isEqualTo(SensorInstallationEvent.AGGREGATE_TYPE);
        assertThat(event.payload().sensorInstallationId()).isEqualTo(installationId);
        assertThat(event.payload().sensorId()).isEqualTo(sensorId);
        assertThat(event.payload().educationalEnvironmentId()).isEqualTo(environmentId);
        assertThat(event.payload().installedAt()).isEqualTo(installedAt);
        assertThat(event.payload().removedAt()).isNull();
    }

    @Test
    void removedEventCarriesTheRemovalTimestamp() {
        Instant removedAt = Instant.parse("2026-02-01T10:00:00Z");

        SensorInstallationEvent event = SensorInstallationEvent.removed(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                Instant.parse("2026-01-01T10:00:00Z"), removedAt);

        assertThat(event.eventType()).isEqualTo(SensorInstallationEvent.REMOVED_TYPE);
        assertThat(event.payload().removedAt()).isEqualTo(removedAt);
    }

    @Test
    void routingKeysMatchTheConsumerBinding() {
        // ms-environment-monitoring se suscribe a sensor.installation.*
        assertThat(SensorInstallationEvent.installed(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now())
                .routingKey()).isEqualTo("sensor.installation.created");
        assertThat(SensorInstallationEvent.removed(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                Instant.now(), Instant.now())
                .routingKey()).isEqualTo("sensor.installation.closed");
    }

    @Test
    void serializedEnvelopeMatchesWhatTheConsumerReads() throws Exception {
        UUID installationId = UUID.randomUUID();
        UUID sensorId = UUID.randomUUID();
        UUID environmentId = UUID.randomUUID();

        SensorInstallationEvent event = SensorInstallationEvent.installed(
                installationId, sensorId, environmentId,
                Instant.parse("2026-01-01T10:00:00Z"));

        String json = MAPPER.writeValueAsString(event);

        assertThat(json)
                .contains("\"eventType\":\"" + SensorInstallationEvent.INSTALLED_TYPE + "\"")
                .contains("\"aggregateType\":\"" + SensorInstallationEvent.AGGREGATE_TYPE + "\"")
                .contains("\"aggregateId\":\"" + installationId + "\"")
                .contains("\"sensorInstallationId\":\"" + installationId + "\"")
                .contains("\"sensorId\":\"" + sensorId + "\"")
                .contains("\"educationalEnvironmentId\":\"" + environmentId + "\"")
                .contains("\"installedAt\":\"2026-01-01T10:00:00Z\"")
                .contains("\"version\":" + SensorInstallationEvent.SCHEMA_VERSION);
    }

    @Test
    void eachEventGetsItsOwnEventId() {
        // El consumidor deduplica por eventId: dos altas distintas deben poder
        // procesarse las dos.
        SensorInstallationEvent first = SensorInstallationEvent.installed(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now());
        SensorInstallationEvent second = SensorInstallationEvent.installed(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now());

        assertThat(first.eventId()).isNotEqualTo(second.eventId());
    }
}
