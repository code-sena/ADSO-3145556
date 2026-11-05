package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Verifica el consumidor de punta a punta: se publica en el exchange y se comprueba
 * el efecto en el read model.
 */
class MeasurementEventListenerTest extends ConsumerTestBase {

    @Test
    void projectsAnEnvironmentalDataRecordedEvent() {
        UUID eventId = UUID.randomUUID();
        publish(event(eventId, Instant.now().minusSeconds(3600), "temperature", "21.5"));

        assertThat(awaitRows(1, 10_000)).isTrue();
        assertThat(valueOf("temperature")).isEqualByComparingTo("21.5");
    }

    @Test
    void flattensThePayloadIntoAtomicReadings() {
        // Un solo evento con las cuatro variables debe producir cuatro filas: el
        // read model es atomico, una fila por variable (regla 4.1).
        Instant at = Instant.now().minusSeconds(7200);
        UUID eventId = UUID.randomUUID();
        String body = "{\"eventId\":\"" + eventId + "\""
                + ",\"eventType\":\"" + EnvironmentalDataRecorded.TYPE + "\""
                + ",\"aggregateId\":\"" + ENVIRONMENT_ID + "\""
                + ",\"aggregateType\":\"" + EnvironmentalDataRecorded.AGGREGATE_TYPE + "\""
                + ",\"occurredAt\":\"" + at + "\""
                + ",\"version\":1"
                + ",\"payload\":{\"environmentId\":\"" + ENVIRONMENT_ID + "\""
                + ",\"temperature\":22.0,\"humidity\":55.0,\"co2\":612,\"noiseLevel\":48.0}"
                + ",\"metadata\":{}}";
        publish(message(body, eventId.toString()));

        assertThat(awaitRows(4, 10_000)).isTrue();
        assertThat(valueOf("temperature")).isEqualByComparingTo("22.0");
        assertThat(valueOf("humidity")).isEqualByComparingTo("55.0");
        assertThat(valueOf("co2")).isEqualByComparingTo("612");
        assertThat(valueOf("noise")).isEqualByComparingTo("48.0");
    }

    @Test
    void resolvesNoiseLevelToTheNoiseVariableCode() {
        // El evento llama a la variable noiseLevel y el catalogo la llama noise.
        publish(event(UUID.randomUUID(), Instant.now(), "noiseLevel", "48.0"));

        assertThat(awaitRows(1, 10_000)).isTrue();
        assertThat(valueOf("noise")).isEqualByComparingTo("48.0");
    }

    @Test
    void registersTheEventIdInTheInbox() {
        UUID eventId = UUID.randomUUID();
        publish(event(eventId, Instant.now(), "temperature", "20.0"));

        assertThat(awaitInbox(1, 10_000)).isTrue();
        assertThat(inbox.findById(eventId)).isPresent();
    }

    @Test
    void ignoresRedeliveryOfAnAlreadyProcessedEvent() {
        UUID eventId = UUID.randomUUID();
        Instant at = Instant.now().minusSeconds(1800);
        var message = event(eventId, at, "temperature", "20.0");

        publish(message);
        assertThat(awaitInbox(1, 10_000)).isTrue();

        // La entrega es at-least-once: el broker puede reenviar. Sin inbox esto
        // duplicaria la lectura.
        publish(message);
        awaitInbox(1, 5_000);

        assertThat(countRows()).isEqualTo(1);
        assertThat(inboxCount()).isEqualTo(1);
    }

    @Test
    void alsoDeduplicatesWhenTheSameReadingArrivesUnderDifferentEventIds() {
        Instant at = Instant.now().minusSeconds(900);
        publish(event(UUID.randomUUID(), at, "co2", "612"));
        assertThat(awaitRows(1, 10_000)).isTrue();

        publish(event(UUID.randomUUID(), at, "co2", "612"));
        assertThat(awaitInbox(2, 10_000)).isTrue();

        // El backfill puede traer la misma lectura por otro camino y con otro
        // eventId: la clave natural del read model tambien la descarta.
        assertThat(countRows()).isEqualTo(1);
        assertThat(inboxCount()).isEqualTo(2);
    }

    @Test
    void ignoresFieldsThatWereNotReported() {
        // El dispositivo puede reportar solo algunas variables. No se inventa un cero.
        publish(event(UUID.randomUUID(), Instant.now(), "temperature", "20.0"));

        assertThat(awaitRows(1, 10_000)).isTrue();
        assertThat(jdbc.queryForObject("""
                select count(*) from environment_monitoring.environment_measurement
                where variable_id = ?
                """, Long.class, CO2)).isZero();
    }

    @Test
    void routesAMalformedMessageToTheDeadLetterQueue() {
        publish(message("{ esto no es json", UUID.randomUUID().toString()));

        assertThat(awaitQueue(DLQ, 20_000)).isTrue();
        assertThat(countRows()).isZero();
        assertThat(inboxCount()).isZero();
    }

    @Test
    void routesAnEventWithoutIdentifiersToTheDeadLetterQueue() {
        String body = "{\"eventType\":\"EnvironmentalDataRecorded\""
                + ",\"payload\":{\"environmentId\":\"" + ENVIRONMENT_ID + "\",\"co2\":600}}";
        publish(message(body, null));

        assertThat(awaitQueue(DLQ, 20_000)).isTrue();
    }

    @Test
    void routesAnEventWithoutPayloadToTheDeadLetterQueue() {
        String body = "{\"eventId\":\"" + UUID.randomUUID() + "\""
                + ",\"eventType\":\"EnvironmentalDataRecorded\"}";
        publish(message(body, null));

        assertThat(awaitQueue(DLQ, 20_000)).isTrue();
    }

    @Test
    void acceptsAnEventWithoutReadings() {
        String body = "{\"eventId\":\"" + UUID.randomUUID() + "\""
                + ",\"eventType\":\"" + EnvironmentalDataRecorded.TYPE + "\""
                + ",\"occurredAt\":\"" + Instant.now() + "\",\"version\":1"
                + ",\"payload\":{\"environmentId\":\"" + ENVIRONMENT_ID + "\"}}";
        publish(message(body, null));

        // Sin lecturas es valido: queda registrado y no va a la DLQ.
        assertThat(awaitInbox(1, 10_000)).isTrue();
        assertThat(countRows()).isZero();
        assertThat(queueDepth(DLQ)).isZero();
    }
}