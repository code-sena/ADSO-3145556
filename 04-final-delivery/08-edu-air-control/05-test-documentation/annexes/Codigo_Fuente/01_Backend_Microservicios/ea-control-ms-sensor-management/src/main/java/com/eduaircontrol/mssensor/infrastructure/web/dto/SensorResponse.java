package com.eduaircontrol.mssensor.infrastructure.web.dto;

import com.eduaircontrol.mssensor.domain.model.Sensor;
import java.time.Instant;
import java.util.UUID;

public record SensorResponse(
        UUID sensorId,
        String serialNumber,
        UUID sensorModelId,
        UUID sensorStatusId,
        Instant lastSeenAt,
        Instant createdAt,
        Instant updatedAt) {

    public static SensorResponse from(Sensor sensor) {
        return new SensorResponse(
                sensor.getId(),
                sensor.getSerialNumber(),
                sensor.getSensorModelId(),
                sensor.getSensorStatusId(),
                sensor.getLastSeenAt(),
                sensor.getCreatedAt(),
                sensor.getUpdatedAt());
    }
}
