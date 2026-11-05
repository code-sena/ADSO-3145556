package com.eduaircontrol.msmonitoring.shared.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Catalogo de variables ambientales tal como lo necesita el analisis.
 *
 * <p>En el servicio se resuelve contra la replica local {@code environment_monitoring.variable},
 * no contra el dominio sensors: el servicio no administra sensores y no debe
 * couplejar su dominio a otro que no posee.
 */
public interface VariableCatalogPort {

    Optional<VariableRef> findByCode(String code);

    List<VariableRef> findAll();

    Optional<VariableRef> findById(UUID variableId);

    record VariableRef(UUID id, String code, String name, String unitSymbol) {
    }
}