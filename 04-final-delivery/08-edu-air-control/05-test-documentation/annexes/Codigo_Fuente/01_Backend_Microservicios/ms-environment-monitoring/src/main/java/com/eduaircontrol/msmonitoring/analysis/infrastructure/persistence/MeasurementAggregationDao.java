package com.eduaircontrol.msmonitoring.analysis.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.shared.contract.MeasurementAggregationPort;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Agregacion de mediciones por variable sobre el read model local.
 *
 * <p>Es una proyeccion, no una entidad, asi que se resuelve con JDBC crudo.
 *
 * <p>El SQL queda mas simple que el que usaba el modulo analysis dentro del
 * monolito. Alli cada medicion tenia que recorrer
 * {@code environment_measurement -> sensor_installation} solo para descubrir el ambiente, y el
 * codigo de variable exigia otro JOIN. Aqui el read model ya guarda
 * {@code environment_id} directo y {@code environment_monitoring.variable} tiene cuatro filas,
 * asi que un JOIN ahi no cuesta nada.
 *
 * <p>Los umbrales de advertencia llegan como arrays paralelos cruzados con
 * {@code unnest}, de modo que una sola consulta resuelve minimo, maximo, promedio,
 * cantidad de muestras y excedencias por variable.
 */
@Repository
public class MeasurementAggregationDao {

    /**
     * Una muestra es excedencia cuando supera un limite que este definido.
     *
     * <p>Importante: la comprobacion es {@code max IS NOT NULL AND valor > max}, no
     * {@code max IS NULL OR valor > max}. Con la segunda forma, un umbral sin
     * maximo (tipico de un minimo, como CO2) hace que la condicion sea siempre
     * verdadera y todas las muestras se contabilicen como excedencia.
     */
    private static final String SQL = """
            SELECT m.variable_id,
                   v.code AS variable_code,
                   min(m.measured_value) AS min_value,
                   max(m.measured_value) AS max_value,
                   avg(m.measured_value) AS avg_value,
                   count(*) AS sample_count,
                   count(*) FILTER (
                       WHERE (t.max_value IS NOT NULL AND m.measured_value > t.max_value)
                          OR (t.min_value IS NOT NULL AND m.measured_value < t.min_value)
                   ) AS exceedance_count
            FROM environment_monitoring.environment_measurement m
            JOIN environment_monitoring.variable v ON v.variable_id = m.variable_id
            JOIN environment_monitoring.installation_projection p
                ON p.sensor_installation_id = m.sensor_installation_id
            LEFT JOIN unnest(CAST(? AS uuid[]), CAST(? AS numeric[]), CAST(? AS numeric[]))
                       AS t(variable_id, min_value, max_value)
                   ON t.variable_id = m.variable_id
            WHERE p.educational_environment_id = ?
              AND p.removed_at IS NULL
              AND m.measured_at >= ?
              AND m.measured_at < ?
            GROUP BY m.variable_id, v.code
            ORDER BY v.code
            """;

    private final JdbcTemplate jdbcTemplate;

    public MeasurementAggregationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @param ranges umbral de advertencia vigente por variable; puede estar vacio, en
     *               cuyo caso ninguna muestra se considera excedencia
     */
    public List<MeasurementAggregationPort.Aggregate> aggregate(
            UUID environmentId,
            Instant start,
            Instant end,
            Map<UUID, ThresholdRange> ranges) {
        UUID[] variableIds = ranges.keySet().toArray(new UUID[0]);
        BigDecimal[] mins = new BigDecimal[ranges.size()];
        BigDecimal[] maxs = new BigDecimal[ranges.size()];
        int index = 0;
        for (Map.Entry<UUID, ThresholdRange> entry : ranges.entrySet()) {
            ThresholdRange range = entry.getValue();
            mins[index] = range == null ? null : range.min();
            maxs[index] = range == null ? null : range.max();
            index++;
        }

        List<Row> rows = jdbcTemplate.query(con -> {
            PreparedStatement statement = con.prepareStatement(SQL);
            statement.setArray(1, con.createArrayOf("uuid", variableIds));
            statement.setArray(2, con.createArrayOf("numeric", toObjects(mins)));
            statement.setArray(3, con.createArrayOf("numeric", toObjects(maxs)));
            statement.setObject(4, environmentId);
            // PostgreSQL no puede inferir el tipo de un Instant en setObject.
            statement.setObject(5, java.sql.Timestamp.from(start));
            statement.setObject(6, java.sql.Timestamp.from(end));
            return statement;
        }, (rs, rowNum) -> new Row(
                rs.getObject("variable_id", UUID.class),
                rs.getString("variable_code"),
                rs.getBigDecimal("min_value"),
                rs.getBigDecimal("max_value"),
                rs.getBigDecimal("avg_value"),
                rs.getLong("sample_count"),
                rs.getLong("exceedance_count")));

        return rows.stream()
                .map(row -> new MeasurementAggregationPort.Aggregate(
                        row.variableId(),
                        row.variableCode(),
                        scale(row.minValue()),
                        scale(row.maxValue()),
                        scale(row.avgValue()),
                        row.sampleCount(),
                        row.exceedanceCount()))
                .toList();
    }

    private static Object[] toObjects(BigDecimal[] values) {
        Object[] boxed = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            boxed[i] = values[i];
        }
        return boxed;
    }

    private static BigDecimal scale(BigDecimal value) {
        return value == null ? null : value.setScale(4, RoundingMode.HALF_UP);
    }

    private record Row(UUID variableId,
                       String variableCode,
                       BigDecimal minValue,
                       BigDecimal maxValue,
                       BigDecimal avgValue,
                       long sampleCount,
                       long exceedanceCount) {
    }

    /** Rango de advertencia vigente de una variable en el ambiente analizado. */
    public record ThresholdRange(BigDecimal min, BigDecimal max) {
    }
}