package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.MeasurementUnit;
import java.util.Optional;
import java.util.UUID;

public interface MeasurementUnitRepository {

    MeasurementUnit save(MeasurementUnit item);

    Optional<MeasurementUnit> findById(UUID id);

    boolean existsById(UUID id);

    boolean existsByCode(String code);

    void delete(MeasurementUnit item);

    PageResult<MeasurementUnit> search(String query, int page, int limit);
}
