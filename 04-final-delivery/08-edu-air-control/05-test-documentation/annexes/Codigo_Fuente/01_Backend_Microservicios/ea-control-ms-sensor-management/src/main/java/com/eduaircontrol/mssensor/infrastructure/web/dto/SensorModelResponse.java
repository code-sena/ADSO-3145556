package com.eduaircontrol.mssensor.infrastructure.web.dto;

import com.eduaircontrol.mssensor.domain.model.SensorModel;
import java.time.Instant;
import java.util.UUID;

public record SensorModelResponse(
        UUID sensorModelId,
        String code,
        String name,
        String description,
        Instant createdAt) {

    public static SensorModelResponse from(SensorModel item) {
        return new SensorModelResponse(
                item.getId(),
                item.getCode(),
                item.getName(),
                item.getDescription(),
                item.getCreatedAt());
    }
}
