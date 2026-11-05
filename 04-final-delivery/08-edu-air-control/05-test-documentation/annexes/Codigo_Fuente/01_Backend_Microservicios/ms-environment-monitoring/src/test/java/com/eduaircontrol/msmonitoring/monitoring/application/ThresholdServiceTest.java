package com.eduaircontrol.msmonitoring.monitoring.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.eduaircontrol.msmonitoring.PostgresTestBase;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Verifica la evaluacion de umbrales en los bordes exactos.
 *
 * <p>Usa los umbrales sembrados de CLASSROOM (081): TEMP normal 20-26, warning
 * 18-28; CO2 normal 0-800, warning 0-1000.
 */
class ThresholdServiceTest extends PostgresTestBase {

    private static final UUID CLASSROOM =
            UUID.fromString("00000000-0000-4000-8000-000000000081");
    private static final UUID TEMPERATURE =
            UUID.fromString("00000000-0000-4000-8000-000000000071");
    private static final UUID CO2 =
            UUID.fromString("00000000-0000-4000-8000-000000000073");

    private static final LocalDate TODAY = LocalDate.now();

    @Autowired
    private ThresholdService thresholds;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void valueInsideNormalRangeGeneratesNoAlert() {
        assertThat(thresholds.evaluate(CLASSROOM, TEMPERATURE, new BigDecimal("22.0"), TODAY))
                .isEmpty();
        assertThat(thresholds.evaluate(CLASSROOM, CO2, new BigDecimal("600"), TODAY))
                .isEmpty();
    }

    @Test
    void valueOnNormalBoundaryGeneratesNoAlert() {
        // Los bordes son inclusivos: 20 y 26 estan dentro del rango normal.
        assertThat(thresholds.evaluate(CLASSROOM, TEMPERATURE, new BigDecimal("20.0"), TODAY))
                .isEmpty();
        assertThat(thresholds.evaluate(CLASSROOM, TEMPERATURE, new BigDecimal("26.0"), TODAY))
                .isEmpty();
    }

    @Test
    void valueOutsideNormalButInsideWarningGeneratesWarning() {
        var evaluation = thresholds.evaluate(CLASSROOM, TEMPERATURE, new BigDecimal("19.0"), TODAY);

        assertThat(evaluation).isPresent();
        assertThat(evaluation.get().severityCode()).isEqualTo("WARNING");
    }

    @Test
    void valueOnWarningBoundaryGeneratesWarning() {
        // 18 esta dentro del rango WARNING (18-28) aunque fuera del normal.
        var evaluation = thresholds.evaluate(CLASSROOM, TEMPERATURE, new BigDecimal("18.0"), TODAY);

        assertThat(evaluation).isPresent();
        assertThat(evaluation.get().severityCode()).isEqualTo("WARNING");
    }

    @Test
    void valueOutsideWarningGeneratesCritical() {
        var low = thresholds.evaluate(CLASSROOM, TEMPERATURE, new BigDecimal("15.0"), TODAY);
        assertThat(low).isPresent();
        assertThat(low.get().severityCode()).isEqualTo("CRITICAL");

        var high = thresholds.evaluate(CLASSROOM, CO2, new BigDecimal("1200"), TODAY);
        assertThat(high).isPresent();
        assertThat(high.get().severityCode()).isEqualTo("CRITICAL");
    }

    @Test
    void nullValueGeneratesNoAlert() {
        assertThat(thresholds.evaluate(CLASSROOM, TEMPERATURE, null, TODAY)).isEmpty();
    }

    @Test
    void unknownVariableGeneratesNoAlert() {
        assertThat(thresholds.evaluate(CLASSROOM, UUID.randomUUID(), new BigDecimal("99"), TODAY))
                .isEmpty();
    }
}
