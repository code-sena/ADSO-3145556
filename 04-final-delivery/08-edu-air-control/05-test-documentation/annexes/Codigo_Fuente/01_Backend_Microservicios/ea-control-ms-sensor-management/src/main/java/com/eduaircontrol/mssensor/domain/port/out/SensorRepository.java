package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.Sensor;
import java.util.Optional;
import java.util.UUID;

public interface SensorRepository {

    Sensor save(Sensor sensor);

    Optional<Sensor> findById(UUID id);

    boolean existsBySerialNumber(String serialNumber);

    /** Evita borrar un modelo que ya esta en uso. */
    boolean existsBySensorModelId(UUID sensorModelId);

    /** Evita borrar un estado que ya esta en uso. */
    boolean existsBySensorStatusId(UUID sensorStatusId);

    void delete(Sensor sensor);

    PageResult<Sensor> search(String query, UUID sensorModelId, UUID sensorStatusId, UUID institutionId, int page, int limit);
}
