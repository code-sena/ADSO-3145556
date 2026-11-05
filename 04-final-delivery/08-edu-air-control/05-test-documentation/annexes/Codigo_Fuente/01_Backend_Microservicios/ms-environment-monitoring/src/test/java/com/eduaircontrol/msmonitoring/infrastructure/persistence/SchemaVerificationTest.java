package com.eduaircontrol.msmonitoring.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.eduaircontrol.msmonitoring.PostgresTestBase;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Verifica que Liquibase dejo el esquema propio completo. El test de contexto
 * solo prueba que la app arranca; esto comprueba que el contrato de datos sobre
 * el que dependen el consumidor y la agregacion existe de verdad.
 */
class SchemaVerificationTest extends PostgresTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    void coreAnalysisTablesExist() {
        assertThat(tablesIn("environment_monitoring")).contains(
                "environmental_analysis",
                "analysis_result",
                "analysis_statuses");
    }

    @Test
    void readModelAndInboxExist() {
        assertThat(tablesIn("environment_monitoring")).contains(
                "environment_measurement",
                "inbox_message");
    }

    @Test
    void catalogReplicasExist() {
        assertThat(tablesIn("environment_monitoring")).contains(
                "variable",
                "educational_environment",
                "variable_threshold",
                "severity",
                "quality_flag");
    }

    @Test
    void alertAndProjectionTablesExist() {
        assertThat(tablesIn("environment_monitoring")).contains(
                "environment_alert",
                "installation_projection");
    }

    @Test
    void analysisStatusesAreSeededWithMonolithIdenticalIds() {
        Map<String, String> byCode = new LinkedHashMap<>();
        jdbc.query("select code, analysis_status_id::text as id from environment_monitoring.analysis_statuses",
                rs -> {
                    byCode.put(rs.getString("code"), rs.getString("id"));
                });

        // Los UUID deben coincidir con los del monolito; si divergen, la migracion
        // de analisis preexistentes deja de ser directa.
        assertThat(byCode).containsExactlyInAnyOrderEntriesOf(Map.of(
                "PENDING", "00000000-0000-4000-8000-000000000041",
                "RUNNING", "00000000-0000-4000-8000-000000000042",
                "COMPLETED", "00000000-0000-4000-8000-000000000043",
                "FAILED", "00000000-0000-4000-8000-000000000044"));
    }

    @Test
    void environmentalVariablesAreSeeded() {
        List<String> codes = jdbc.queryForList(
                "select code from environment_monitoring.variable order by code", String.class);

        assertThat(codes).containsExactly("co2", "humidity", "noise", "temperature");
    }

    @Test
    void severitiesAreSeeded() {
        List<String> codes = jdbc.queryForList(
                "select code from environment_monitoring.severity order by level", String.class);

        assertThat(codes).containsExactly("INFO", "WARNING", "CRITICAL");
    }

    @Test
    void qualityFlagsAreSeeded() {
        List<String> codes = jdbc.queryForList(
                "select code from environment_monitoring.quality_flag order by code", String.class);

        assertThat(codes).containsExactly("BAD", "SUSPECT", "VALID");
    }

    @Test
    void measurementIsIdempotentPerInstallationVariableAndInstant() {
        assertThat(constraintsOf("environment_monitoring", "environment_measurement"))
                .contains("uq_environment_measurement_inst_var_time");
    }

    private List<String> tablesIn(String schema) {
        return jdbc.queryForList(
                "select table_name from information_schema.tables where table_schema = ?",
                String.class, schema);
    }

    private List<String> constraintsOf(String schema, String table) {
        return jdbc.queryForList("""
                select c.conname
                from pg_constraint c
                join pg_class t on t.oid = c.conrelid
                join pg_namespace n on n.oid = t.relnamespace
                where n.nspname = ? and t.relname = ?
                """, String.class, schema, table);
    }
}
