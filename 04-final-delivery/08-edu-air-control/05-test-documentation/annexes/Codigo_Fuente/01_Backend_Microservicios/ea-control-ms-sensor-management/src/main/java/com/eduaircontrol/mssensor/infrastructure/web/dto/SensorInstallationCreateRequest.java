package com.eduaircontrol.mssensor.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record SensorInstallationCreateRequest(
        @NotNull(message = "sensorId is required") UUID sensorId,
        @NotNull(message = "educationalEnvironmentId is required") UUID educationalEnvironmentId,
        @NotNull(message = "installedAt is required") Instant installedAt) {
}
