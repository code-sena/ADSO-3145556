package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.SensorVariable;
import java.util.List;
import java.util.UUID;

public interface SensorVariableRepository {

    SensorVariable save(SensorVariable sensorVariable);

    boolean exists(UUID sensorId, UUID variableId);

    boolean existsBySensorId(UUID sensorId);

    boolean existsByVariableId(UUID variableId);

    void deleteBySensorIdAndVariableId(UUID sensorId, UUID variableId);

    void deleteBySensorId(UUID sensorId);

    List<SensorVariable> listBySensorId(UUID sensorId);

    List<SensorVariable> listAll();
}
