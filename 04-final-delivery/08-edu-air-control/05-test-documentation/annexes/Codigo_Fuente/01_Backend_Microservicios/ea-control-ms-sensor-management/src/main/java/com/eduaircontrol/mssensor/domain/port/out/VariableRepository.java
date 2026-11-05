package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.Variable;
import java.util.Optional;
import java.util.UUID;

public interface VariableRepository {

    Variable save(Variable variable);

    Optional<Variable> findById(UUID id);

    boolean existsByCode(String code);

    /** Evita borrar una unidad que ya usan variables. */
    boolean existsByMeasurementUnitId(UUID measurementUnitId);

    void delete(Variable variable);

    PageResult<Variable> search(String query, int page, int limit);
}
