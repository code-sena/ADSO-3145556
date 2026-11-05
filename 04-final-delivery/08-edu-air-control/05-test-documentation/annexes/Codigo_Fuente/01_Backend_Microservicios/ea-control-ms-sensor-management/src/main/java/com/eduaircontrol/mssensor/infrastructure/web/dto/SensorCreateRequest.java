package com.eduaircontrol.mssensor.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record SensorCreateRequest(
        @NotBlank(message = "serialNumber is required")
        @Size(max = 60, message = "serialNumber must be at most 60 characters") String serialNumber,
        @NotNull(message = "sensorModelId is required") UUID sensorModelId,
        @NotNull(message = "sensorStatusId is required") UUID sensorStatusId) {
}
