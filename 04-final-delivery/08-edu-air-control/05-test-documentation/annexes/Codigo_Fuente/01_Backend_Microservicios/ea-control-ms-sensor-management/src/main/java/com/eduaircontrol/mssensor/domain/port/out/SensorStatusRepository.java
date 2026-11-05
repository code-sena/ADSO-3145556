package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorStatus;
import java.util.Optional;
import java.util.UUID;

public interface SensorStatusRepository {

    SensorStatus save(SensorStatus item);

    Optional<SensorStatus> findById(UUID id);

    boolean existsById(UUID id);

    boolean existsByCode(String code);

    void delete(SensorStatus item);

    PageResult<SensorStatus> search(String query, int page, int limit);
}
