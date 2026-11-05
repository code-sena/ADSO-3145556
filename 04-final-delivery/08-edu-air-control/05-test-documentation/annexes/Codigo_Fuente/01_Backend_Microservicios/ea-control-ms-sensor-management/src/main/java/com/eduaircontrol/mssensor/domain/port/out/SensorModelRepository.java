package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorModel;
import java.util.Optional;
import java.util.UUID;

public interface SensorModelRepository {

    SensorModel save(SensorModel item);

    Optional<SensorModel> findById(UUID id);

    boolean existsById(UUID id);

    boolean existsByCode(String code);

    void delete(SensorModel item);

    PageResult<SensorModel> search(String query, int page, int limit);
}
