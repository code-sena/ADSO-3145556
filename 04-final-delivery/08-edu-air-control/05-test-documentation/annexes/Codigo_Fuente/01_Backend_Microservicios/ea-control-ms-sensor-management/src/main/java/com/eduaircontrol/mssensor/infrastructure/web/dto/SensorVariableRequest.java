package com.eduaircontrol.mssensor.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SensorVariableRequest(
        @NotNull(message = "sensorId is required") UUID sensorId,
        @NotNull(message = "variableId is required") UUID variableId) {
}
