package com.eduaircontrol.msmonitoring.analysis.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.shared.contract.EnvironmentLookupPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ambientes contra la replica local.
 *
 * <p>El analisis solo valida que el ambiente exista y consulta su tipo para
 * resolver umbrales; no administra ambientes ni consulta sus capacidades.
 */
@Repository
@Transactional(readOnly = true)
public class EnvironmentLookupAdapter implements EnvironmentLookupPort {

    private static final String SELECT = """
            select educational_environment_id, code, name, campus_id,
                   environment_type_id, floor
            from environment_monitoring.educational_environment
            """;

    private final JdbcTemplate jdbc;

    public EnvironmentLookupAdapter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<EnvironmentInfo> findById(UUID environmentId) {
        return jdbc.query(SELECT + " where educational_environment_id = ?",
                        this::mapRow, environmentId)
                .stream().findFirst();
    }

    @Override
    public List<EnvironmentInfo> findAll() {
        return jdbc.query(SELECT + " order by name", this::mapRow);
    }

    @Override
    public Optional<EnvironmentInfo> findByCode(String code) {
        return jdbc.query(SELECT + " where code = ?", this::mapRow, code)
                .stream().findFirst();
    }

    private EnvironmentInfo mapRow(java.sql.ResultSet rs, int rowNum)
            throws java.sql.SQLException {
        return new EnvironmentInfo(
                rs.getObject("educational_environment_id", UUID.class),
                rs.getString("code"),
                rs.getString("name"),
                rs.getObject("campus_id", UUID.class),
                rs.getObject("environment_type_id", UUID.class),
                rs.getObject("floor", Integer.class));
    }
}