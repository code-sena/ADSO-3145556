package com.eduaircontrol.msmonitoring.monitoring.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Verifica el ciclo de vida de la alerta: se genera activa y solo se reconoce una vez.
 */
class EnvironmentAlertTest {

    private EnvironmentAlert active() {
        return EnvironmentAlert.raise(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                Instant.now());
    }

    @Test
    void newAlertIsActive() {
        EnvironmentAlert alert = active();

        assertThat(alert.isActive()).isTrue();
        assertThat(alert.getAcknowledgedAt()).isNull();
        assertThat(alert.getAcknowledgedBy()).isNull();
    }

    @Test
    void acknowledgeMarksAlertAsInactive() {
        EnvironmentAlert alert = active();
        UUID userId = UUID.randomUUID();
        Instant at = Instant.now();

        alert.acknowledge(userId, at);

        assertThat(alert.isActive()).isFalse();
        assertThat(alert.getAcknowledgedAt()).isEqualTo(at);
        assertThat(alert.getAcknowledgedBy()).isEqualTo(userId);
    }

    @Test
    void cannotAcknowledgeTwice() {
        EnvironmentAlert alert = active();
        alert.acknowledge(UUID.randomUUID(), Instant.now());

        assertThatThrownBy(() -> alert.acknowledge(UUID.randomUUID(), Instant.now()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya fue reconocida");
    }
}
