package com.eduaircontrol.mssensor.application;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.port.out.SensorInstallationRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorModelRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorStatusRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorVariableRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import com.eduaircontrol.mssensor.shared.exception.ValidationException;
import com.eduaircontrol.mssensor.domain.model.Sensor;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorInstallationRepository sensorInstallationRepository;
    private final SensorVariableRepository sensorVariableRepository;
    private final SensorModelRepository sensorModelRepository;
    private final SensorStatusRepository sensorStatusRepository;

    @Transactional(readOnly = true)
    public PageResult<Sensor> list(String query, UUID sensorModelId, UUID sensorStatusId,
            int page, int limit) {
        return sensorRepository.search(query, sensorModelId, sensorStatusId, TenantContext.institutionId(), page, limit);
    }

    @Transactional(readOnly = true)
    public Sensor get(UUID id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sensor not found: " + id));
    }

    public Sensor create(String serialNumber, UUID sensorModelId, UUID sensorStatusId) {
        String normalizedSerial = requireText(serialNumber, "serialNumber");
        if (sensorRepository.existsBySerialNumber(normalizedSerial)) {
            throw new ConflictException("Sensor serial number already exists: " + normalizedSerial);
        }
        // Antes solo se comprobaba que no fueran null: un UUID inventado pasaba el
        // alta y el sensor quedaba sin modelo ni estado reales.
        requireExistingModel(sensorModelId);
        requireExistingStatus(sensorStatusId);
        Sensor sensor = Sensor.builder()
                .serialNumber(normalizedSerial)
                .institutionId(TenantContext.institutionId())
                .sensorModelId(sensorModelId)
                .sensorStatusId(sensorStatusId)
                .build();
        return sensorRepository.save(sensor);
    }

    public Sensor update(UUID id, String serialNumber, UUID sensorModelId,
            UUID sensorStatusId, Instant lastSeenAt) {
        Sensor sensor = get(id);
        if (serialNumber != null) {
            String normalizedSerial = requireText(serialNumber, "serialNumber");
            if (!sensor.getSerialNumber().equals(normalizedSerial)
                    && sensorRepository.existsBySerialNumber(normalizedSerial)) {
                throw new ConflictException("Sensor serial number already exists: " + normalizedSerial);
            }
            sensor.setSerialNumber(normalizedSerial);
        }
        if (sensorModelId != null) {
            requireExistingModel(sensorModelId);
            sensor.setSensorModelId(sensorModelId);
        }
        if (sensorStatusId != null) {
            requireExistingStatus(sensorStatusId);
            sensor.setSensorStatusId(sensorStatusId);
        }
        if (lastSeenAt != null) {
            sensor.setLastSeenAt(lastSeenAt);
        }
        return sensorRepository.save(sensor);
    }

    public void delete(UUID id) {
        Sensor sensor = get(id);
        if (sensorInstallationRepository.hasAnyForSensor(id)) {
            throw new ConflictException("Sensor has installation history and cannot be deleted: " + id);
        }
        sensorVariableRepository.deleteBySensorId(id);
        sensorRepository.delete(sensor);
    }

    static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(field + " must not be blank");
        }
        return value.trim();
    }

    static UUID requireId(UUID value, String field) {
        if (value == null) {
            throw new ValidationException(field + " is required");
        }
        return value;
    }

    private void requireExistingModel(UUID sensorModelId) {
        UUID id = requireId(sensorModelId, "sensorModelId");
        if (!sensorModelRepository.existsById(id)) {
            throw new NotFoundException("Sensor model not found: " + id);
        }
    }

    private void requireExistingStatus(UUID sensorStatusId) {
        UUID id = requireId(sensorStatusId, "sensorStatusId");
        if (!sensorStatusRepository.existsById(id)) {
            throw new NotFoundException("Sensor status not found: " + id);
        }
    }

    static String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
