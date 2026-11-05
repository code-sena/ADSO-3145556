package com.eduaircontrol.mssensor.infrastructure.web.dto;

import com.eduaircontrol.mssensor.domain.model.SensorStatus;
import java.time.Instant;
import java.util.UUID;

public record SensorStatusResponse(
        UUID sensorStatusId,
        String code,
        String name,
        Instant createdAt) {

    public static SensorStatusResponse from(SensorStatus item) {
        return new SensorStatusResponse(
                item.getId(),
                item.getCode(),
                item.getName(),
                item.getCreatedAt());
    }
}
