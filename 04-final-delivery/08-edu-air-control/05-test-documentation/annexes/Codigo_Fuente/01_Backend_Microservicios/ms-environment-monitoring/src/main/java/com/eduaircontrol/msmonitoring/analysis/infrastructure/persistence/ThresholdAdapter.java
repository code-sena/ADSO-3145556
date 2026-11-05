package com.eduaircontrol.msmonitoring.analysis.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.shared.contract.ThresholdPort;
import com.eduaircontrol.msmonitoring.shared.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Umbrales vigentes contra la replica local (ADR-014 aceptado).
 *
 * <p>El umbral es por (variable_id, environment_type_id, severity_id). NO existe
 * precedencia ambiente > tipo: la especificacion rechaza explicitamente la columna
 * educational_environment_id del monolito. Solo se consideran los vigentes
 * ({@code valid_to is null}).
 *
 * <p>La severidad se resuelve por codigo contra la tabla local {@code severity},
 * que el servicio siembra con INFO, WARNING y CRITICAL.
 */
@Repository
@Transactional(readOnly = true)
public class ThresholdAdapter implements ThresholdPort {

    private static final String SEVERITY_WARNING = "WARNING";

    private final JdbcTemplate jdbc;

    public ThresholdAdapter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Range> warningRange(UUID environmentId, UUID variableId) {
        UUID environmentTypeId = environmentTypeOf(environmentId);
        return jdbc.query("""
                select t.variable_id, t.min_value, t.max_value
                from environment_monitoring.variable_threshold t
                join environment_monitoring.severity s on s.severity_id = t.severity_id
                where t.valid_to is null
                  and s.code = ?
                  and t.variable_id = ?
                  and t.environment_type_id = ?
                """, (rs, rowNum) -> new Range(
                environmentId,
                rs.getObject("variable_id", UUID.class),
                rs.getBigDecimal("min_value"),
                rs.getBigDecimal("max_value")),
                SEVERITY_WARNING, variableId, environmentTypeId)
                .stream().findFirst();
    }

    @Override
    public List<Range> warningRanges(UUID environmentId) {
        UUID environmentTypeId = environmentTypeOf(environmentId);
        return jdbc.query("""
                select t.variable_id, t.min_value, t.max_value
                from environment_monitoring.variable_threshold t
                join environment_monitoring.severity s on s.severity_id = t.severity_id
                where t.valid_to is null
                  and s.code = ?
                  and t.environment_type_id = ?
                order by t.variable_id
                """, (rs, rowNum) -> new Range(
                environmentId,
                rs.getObject("variable_id", UUID.class),
                rs.getBigDecimal("min_value"),
                rs.getBigDecimal("max_value")),
                SEVERITY_WARNING, environmentTypeId);
    }

    private UUID environmentTypeOf(UUID environmentId) {
        return jdbc.query("""
                        select environment_type_id from environment_monitoring.educational_environment
                        where educational_environment_id = ?
                        """,
                        (rs, rowNum) -> rs.getObject("environment_type_id", UUID.class),
                        environmentId)
                .stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Ambiente no encontrado"));
    }
}
