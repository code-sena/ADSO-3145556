package com.eduaircontrol.mssensor.application;

import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorVariableRepository;
import com.eduaircontrol.mssensor.domain.port.out.VariableRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import com.eduaircontrol.mssensor.domain.model.SensorVariable;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorVariableService {

    private final SensorVariableRepository sensorVariableRepository;
    private final SensorRepository sensorRepository;
    private final VariableRepository variableRepository;

    @Transactional(readOnly = true)
    public List<SensorVariable> list(UUID sensorId) {
        if (sensorId != null) {
            return sensorVariableRepository.listBySensorId(sensorId);
        }
        return sensorVariableRepository.listAll();
    }

    public SensorVariable associate(UUID sensorId, UUID variableId) {
        if (sensorRepository.findById(sensorId).isEmpty()) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        if (variableRepository.findById(variableId).isEmpty()) {
            throw new NotFoundException("Variable not found: " + variableId);
        }
        if (sensorVariableRepository.exists(sensorId, variableId)) {
            throw new ConflictException(
                    "Sensor already measures this variable: " + sensorId + " / " + variableId);
        }
        return sensorVariableRepository.save(new SensorVariable(sensorId, variableId));
    }

    public void dissociate(UUID sensorId, UUID variableId) {
        if (!sensorVariableRepository.exists(sensorId, variableId)) {
            throw new NotFoundException(
                    "Sensor-variable association not found: " + sensorId + " / " + variableId);
        }
        sensorVariableRepository.deleteBySensorIdAndVariableId(sensorId, variableId);
    }
}
