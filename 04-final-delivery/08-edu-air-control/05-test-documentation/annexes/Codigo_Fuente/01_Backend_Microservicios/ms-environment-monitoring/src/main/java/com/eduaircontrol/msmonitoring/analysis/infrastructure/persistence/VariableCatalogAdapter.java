package com.eduaircontrol.msmonitoring.analysis.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.shared.contract.VariableCatalogPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Catalogo de variables contra la replica local.
 *
 * <p>El servicio siembra las cuatro variables ambientales al arrancar (co2,
 * humidity, noise, temperature), asi que no necesita recibir el catalogo por el
 * broker: su forma y contenido son estables y forma parte del dominio.
 */
@Repository
@Transactional(readOnly = true)
public class VariableCatalogAdapter implements VariableCatalogPort {

    private final JdbcTemplate jdbc;

    public VariableCatalogAdapter(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<VariableRef> findByCode(String code) {
        return jdbc.query("""
                        select variable_id, code, name, unit_symbol
                        from environment_monitoring.variable where code = ?
                        """,
                        (rs, rowNum) -> new VariableRef(
                                rs.getObject("variable_id", UUID.class),
                                rs.getString("code"),
                                rs.getString("name"),
                                rs.getString("unit_symbol")),
                        code)
                .stream().findFirst();
    }

    @Override
    public List<VariableRef> findAll() {
        return jdbc.query("""
                select variable_id, code, name, unit_symbol
                from environment_monitoring.variable order by code
                """, (rs, rowNum) -> new VariableRef(
                rs.getObject("variable_id", UUID.class),
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("unit_symbol")));
    }

    @Override
    public Optional<VariableRef> findById(UUID variableId) {
        return jdbc.query("""
                        select variable_id, code, name, unit_symbol
                        from environment_monitoring.variable where variable_id = ?
                        """,
                        (rs, rowNum) -> new VariableRef(
                                rs.getObject("variable_id", UUID.class),
                                rs.getString("code"),
                                rs.getString("name"),
                                rs.getString("unit_symbol")),
                        variableId)
                .stream().findFirst();
    }
}