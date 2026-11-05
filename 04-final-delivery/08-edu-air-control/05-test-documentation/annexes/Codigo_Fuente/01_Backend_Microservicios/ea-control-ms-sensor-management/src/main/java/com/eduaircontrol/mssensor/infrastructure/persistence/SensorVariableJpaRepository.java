package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.SensorVariable;
import com.eduaircontrol.mssensor.domain.model.SensorVariableId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorVariableJpaRepository extends JpaRepository<SensorVariable, SensorVariableId> {

    List<SensorVariable> findBySensorId(UUID sensorId);

    boolean existsBySensorIdAndVariableId(UUID sensorId, UUID variableId);

    boolean existsBySensorId(UUID sensorId);

    boolean existsByVariableId(UUID variableId);

    void deleteBySensorIdAndVariableId(UUID sensorId, UUID variableId);

    void deleteBySensorId(UUID sensorId);
}
