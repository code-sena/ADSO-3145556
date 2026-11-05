package com.eduaircontrol.mssensor.infrastructure.web.dto;

import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

public record SensorUpdateRequest(
        @Size(max = 60, message = "serialNumber must be at most 60 characters") String serialNumber,
        UUID sensorModelId,
        UUID sensorStatusId,
        Instant lastSeenAt) {
}
