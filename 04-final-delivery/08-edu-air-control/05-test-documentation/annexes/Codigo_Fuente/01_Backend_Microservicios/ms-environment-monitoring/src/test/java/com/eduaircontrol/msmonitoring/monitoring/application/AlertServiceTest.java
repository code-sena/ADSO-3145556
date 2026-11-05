package com.eduaircontrol.msmonitoring.monitoring.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.eduaircontrol.msmonitoring.PostgresTestBase;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.in.AlertUseCase;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Verifica el flujo completo de alertas: evaluacion de umbral, generacion y
 * reconocimiento.
 *
 * <p>Usa los umbrales sembrados de CLASSROOM: CO2 normal 0-800, warning 0-1000.
 */
class AlertServiceTest extends PostgresTestBase {

    private static final UUID ENVIRONMENT_ID =
            UUID.fromString("aaaaaaaa-0000-4000-8000-000000000001");
    private static final UUID ENVIRONMENT_TYPE_ID =
            UUID.fromString("00000000-0000-4000-8000-000000000081");
    private static final UUID CO2 =
            UUID.fromString("00000000-0000-4000-8000-000000000073");

    @Autowired
    private AlertService alertService;

    @Autowired
    private ThresholdService thresholdService;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void seedEnvironment() {
        jdbc.update("""
                insert into environment_monitoring.educational_environment
                    (educational_environment_id, code, name, environment_type_id, floor)
                values (?, 'ALERT-TEST', 'Ambiente alertas', ?, 1)
                ON CONFLICT (educational_environment_id) DO NOTHING
                """, ENVIRONMENT_ID, ENVIRONMENT_TYPE_ID);
        jdbc.update("DELETE FROM environment_monitoring.environment_alert");
    }

    @Test
    void raisesAlertWhenThresholdIsExceeded() {
        // CO2 en 1200 supera el warning (0-1000) -> CRITICAL.
        var evaluation = thresholdService.evaluate(
                ENVIRONMENT_TYPE_ID, CO2, new BigDecimal("1200"),
                java.time.LocalDate.now());
        assertThat(evaluation).isPresent();

        var alert = alertService.raiseIfAbsent(
                ENVIRONMENT_ID, CO2,
                evaluation.get().thresholdId(), UUID.randomUUID(),
                evaluation.get().severityCode(), Instant.now());

        assertThat(alert.getId()).isNotNull();
        assertThat(alert.isActive()).isTrue();
    }

    @Test
    void doesNotDuplicateAlertForSameMeasurement() {
        UUID measurementId = UUID.randomUUID();
        var evaluation = thresholdService.evaluate(
                ENVIRONMENT_TYPE_ID, CO2, new BigDecimal("1200"),
                java.time.LocalDate.now()).orElseThrow();

        var first = alertService.raiseIfAbsent(
                ENVIRONMENT_ID, CO2,
                evaluation.thresholdId(), measurementId,
                evaluation.severityCode(), Instant.now());
        var second = alertService.raiseIfAbsent(
                ENVIRONMENT_ID, CO2,
                evaluation.thresholdId(), measurementId,
                evaluation.severityCode(), Instant.now());

        assertThat(second.getId()).isEqualTo(first.getId());
    }

    @Test
    void listsActiveAlerts() {
        var evaluation = thresholdService.evaluate(
                ENVIRONMENT_TYPE_ID, CO2, new BigDecimal("1200"),
                java.time.LocalDate.now()).orElseThrow();
        alertService.raiseIfAbsent(
                ENVIRONMENT_ID, CO2,
                evaluation.thresholdId(), UUID.randomUUID(),
                evaluation.severityCode(), Instant.now());

        var alerts = alertService.list(new AlertUseCase.AlertQuery(
                ENVIRONMENT_ID, "active", null, null, 1, 20));

        assertThat(alerts).hasSize(1);
        assertThat(alerts.get(0).environmentId()).isEqualTo(ENVIRONMENT_ID);
    }

    @Test
    void acknowledgesAnActiveAlert() {
        var evaluation = thresholdService.evaluate(
                ENVIRONMENT_TYPE_ID, CO2, new BigDecimal("1200"),
                java.time.LocalDate.now()).orElseThrow();
        var alert = alertService.raiseIfAbsent(
                ENVIRONMENT_ID, CO2,
                evaluation.thresholdId(), UUID.randomUUID(),
                evaluation.severityCode(), Instant.now());

        UUID userId = UUID.randomUUID();
        var acknowledged = alertService.acknowledge(alert.getId(), userId, Instant.now());

        assertThat(acknowledged.acknowledgedAt()).isNotNull();
        assertThat(acknowledged.acknowledgedBy()).isEqualTo(userId);
    }
}
