package com.eduaircontrol.msmonitoring.infrastructure.backfill;

import static org.assertj.core.api.Assertions.assertThat;

import com.eduaircontrol.msmonitoring.PostgresTestBase;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Verifica la copia del historico al read model.
 *
 * <p>ADR-003 deja origen y destino en la misma instancia PostgreSQL con un esquema
 * por servicio, asi que aqui se siembra el esquema {@code monitoring} (servicios de
 * sensores) dentro del mismo contenedor y se verifica el destino en
 * {@code environment_monitoring}.
 *
 * <p>La propiedad que mas importa es la idempotencia: el backfill corre muchas
 * veces sobre las mismas filas y no puede duplicar ni degradar datos.
 */
class HistoricalBackfillServiceTest extends PostgresTestBase {

    private static final UUID ENVIRONMENT_ID =
            UUID.fromString("aaaaaaaa-0000-4000-8000-000000000001");
    private static final UUID TEMPERATURE =
            UUID.fromString("00000000-0000-4000-8000-000000000071");
    private static final UUID CO2 = UUID.fromString("00000000-0000-4000-8000-000000000073");
    private static final UUID INSTALLATION_ID =
            UUID.fromString("bbbbbbbb-0000-4000-8000-000000000001");
    private static final UUID SENSOR_ID = UUID.fromString("cccccccc-0000-4000-8000-000000000001");
    private static final UUID GOOD_FLAG = UUID.fromString("00000000-0000-4000-8000-000000000031");

    @Autowired
    private HistoricalBackfillService backfill;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void prepareSchemas() {
        jdbc.execute("""
                CREATE SCHEMA IF NOT EXISTS monitoring;
                CREATE SCHEMA IF NOT EXISTS sensors;
                CREATE SCHEMA IF NOT EXISTS classrooms;

                CREATE TABLE IF NOT EXISTS sensors.sensor_installation (
                    sensor_installation_id UUID PRIMARY KEY,
                    sensor_id UUID NOT NULL,
                    educational_environment_id UUID,
                    removed_at TIMESTAMPTZ
                );

                CREATE TABLE IF NOT EXISTS classrooms.educational_environment (
                    educational_environment_id UUID PRIMARY KEY,
                    environment_type_id UUID NOT NULL
                );

                CREATE TABLE IF NOT EXISTS monitoring.quality_flags (
                    quality_flag_id UUID PRIMARY KEY,
                    code VARCHAR(20) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS monitoring.environment_measurement (
                    environment_measurement_id UUID PRIMARY KEY,
                    sensor_installation_id UUID NOT NULL,
                    variable_id UUID NOT NULL,
                    measured_value NUMERIC(12,4) NOT NULL,
                    measured_at TIMESTAMPTZ NOT NULL,
                    quality_flag_id UUID
                );
                """);

        jdbc.update("DELETE FROM monitoring.environment_measurement");
        jdbc.update("DELETE FROM monitoring.quality_flags");
        jdbc.update("DELETE FROM sensors.sensor_installation");
        jdbc.update("DELETE FROM classrooms.educational_environment");
        jdbc.update("DELETE FROM environment_monitoring.environment_measurement");
        jdbc.update("DELETE FROM environment_monitoring.installation_projection");

        jdbc.update(
                "INSERT INTO monitoring.quality_flags (quality_flag_id, code) VALUES (?, 'GOOD')",
                GOOD_FLAG);
        jdbc.update("""
                INSERT INTO classrooms.educational_environment
                    (educational_environment_id, environment_type_id)
                VALUES (?, ?)
                """, ENVIRONMENT_ID,
                UUID.fromString("00000000-0000-4000-8000-000000000081"));
        jdbc.update("""
                INSERT INTO sensors.sensor_installation
                    (sensor_installation_id, sensor_id, educational_environment_id, removed_at)
                VALUES (?, ?, ?, NULL)
                """, INSTALLATION_ID, SENSOR_ID, ENVIRONMENT_ID);
    }

    private void insertMeasurement(Instant measuredAt, BigDecimal value, UUID variableId) {
        jdbc.update("""
                INSERT INTO monitoring.environment_measurement
                    (environment_measurement_id, sensor_installation_id, variable_id,
                     measured_value, measured_at, quality_flag_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """, UUID.randomUUID(), INSTALLATION_ID, variableId, value,
                Timestamp.from(measuredAt), GOOD_FLAG);
    }

    private Instant daysAgo(int days) {
        return Instant.now().minus(days, ChronoUnit.DAYS);
    }

    private long countReadModel() {
        return jdbc.queryForObject(
                "select count(*) from environment_monitoring.environment_measurement",
                Long.class);
    }

    @Test
    void copiesMeasurementsIntoTheReadModel() {
        insertMeasurement(daysAgo(5), new BigDecimal("21.5"), TEMPERATURE);
        insertMeasurement(daysAgo(5).plusSeconds(3600), new BigDecimal("612"), CO2);

        int written = backfill.copyWindow(daysAgo(10), Instant.now().plusSeconds(60));

        assertThat(written).isEqualTo(2);
        assertThat(countReadModel()).isEqualTo(2);
    }

    @Test
    void keepsTheSourceInstallationId() {
        insertMeasurement(daysAgo(1), new BigDecimal("21.5"), TEMPERATURE);

        backfill.copyWindow(daysAgo(2), Instant.now());

        assertThat(jdbc.queryForList(
                "select sensor_installation_id from environment_monitoring.environment_measurement",
                UUID.class)).containsExactly(INSTALLATION_ID);
    }

    @Test
    void isIdempotentAndDoesNotDuplicate() {
        insertMeasurement(daysAgo(3), new BigDecimal("21.5"), TEMPERATURE);

        backfill.copyWindow(daysAgo(5), Instant.now());
        backfill.copyWindow(daysAgo(5), Instant.now());
        backfill.copyWindow(daysAgo(5), Instant.now());

        assertThat(countReadModel()).isEqualTo(1);
    }

    @Test
    void updatesTheValueWhenTheSourceIsCorrected() {
        Instant when = daysAgo(2);
        insertMeasurement(when, new BigDecimal("21.5"), TEMPERATURE);

        backfill.copyWindow(daysAgo(3), Instant.now());

        jdbc.update(
                "UPDATE monitoring.environment_measurement SET measured_value = ? WHERE measured_at = ?",
                new BigDecimal("25.0"), Timestamp.from(when));

        backfill.copyWindow(daysAgo(3), Instant.now());

        BigDecimal value = jdbc.queryForObject(
                "select measured_value from environment_monitoring.environment_measurement",
                BigDecimal.class);
        assertThat(value).isEqualByComparingTo("25.0");
        assertThat(countReadModel()).isEqualTo(1);
    }

    @Test
    void onlyCopiesMeasurementsInsideTheWindow() {
        insertMeasurement(daysAgo(40), new BigDecimal("21.5"), TEMPERATURE);
        insertMeasurement(daysAgo(3), new BigDecimal("22.5"), TEMPERATURE);

        backfill.copyWindow(daysAgo(10), Instant.now());

        assertThat(countReadModel()).isEqualTo(1);
        assertThat(jdbc.queryForObject(
                "select measured_value from environment_monitoring.environment_measurement",
                BigDecimal.class)).isEqualByComparingTo("22.5");
    }

    @Test
    void skipsMeasurementsOfRemovedInstallations() {
        insertMeasurement(daysAgo(1), new BigDecimal("21.5"), TEMPERATURE);
        jdbc.update(
                "UPDATE sensors.sensor_installation SET removed_at = now() WHERE sensor_installation_id = ?",
                INSTALLATION_ID);

        backfill.copyWindow(daysAgo(2), Instant.now());

        assertThat(countReadModel()).isZero();
    }

    @Test
    void mapsGoodQualityToValidFlag() {
        insertMeasurement(daysAgo(1), new BigDecimal("21.5"), TEMPERATURE);

        backfill.copyWindow(daysAgo(2), Instant.now());

        assertThat(jdbc.queryForObject("""
                select q.code
                from environment_monitoring.environment_measurement m
                join environment_monitoring.quality_flag q
                    on q.quality_flag_id = m.quality_flag_id
                """, String.class)).isEqualTo("VALID");
    }

    @Test
    void handlesAnEmptyWindowWithoutFailing() {
        assertThat(backfill.copyWindow(daysAgo(10), Instant.now())).isZero();
    }

    @Test
    void rejectsAnInvertedWindow() {
        Instant now = Instant.now();
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> backfill.copyWindow(now, now.minus(1, ChronoUnit.HOURS))))
                .hasMessageContaining("duracion positiva");
    }

    @Test
    void copiesMoreRowsThanASingleBatch() {
        for (int i = 0; i < 1200; i++) {
            insertMeasurement(daysAgo(4).plusSeconds(i), new BigDecimal("20"), CO2);
        }

        int written = backfill.copyWindow(daysAgo(5), Instant.now());

        assertThat(written).isEqualTo(1200);
        assertThat(countReadModel()).isEqualTo(1200);
    }
}