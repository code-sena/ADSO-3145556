package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.port.out.SensorVariableRepository;
import com.eduaircontrol.mssensor.domain.model.SensorVariable;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SensorVariableRepositoryAdapter implements SensorVariableRepository {

    private final SensorVariableJpaRepository jpaRepository;

    @Override
    public SensorVariable save(SensorVariable sensorVariable) {
        return jpaRepository.save(sensorVariable);
    }

    @Override
    public boolean exists(UUID sensorId, UUID variableId) {
        return jpaRepository.existsBySensorIdAndVariableId(sensorId, variableId);
    }

    @Override
    public boolean existsBySensorId(UUID sensorId) {
        return jpaRepository.existsBySensorId(sensorId);
    }

    @Override
    public boolean existsByVariableId(UUID variableId) {
        return jpaRepository.existsByVariableId(variableId);
    }

    @Override
    public void deleteBySensorIdAndVariableId(UUID sensorId, UUID variableId) {
        jpaRepository.deleteBySensorIdAndVariableId(sensorId, variableId);
    }

    @Override
    public void deleteBySensorId(UUID sensorId) {
        jpaRepository.deleteBySensorId(sensorId);
    }

    @Override
    public List<SensorVariable> listBySensorId(UUID sensorId) {
        return jpaRepository.findBySensorId(sensorId);
    }

    @Override
    public List<SensorVariable> listAll() {
        return jpaRepository.findAll();
    }
}
