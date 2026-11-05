package com.eduaircontrol.mssensor.infrastructure.web.dto;

import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import java.time.Instant;
import java.util.UUID;

public record SensorInstallationResponse(
        UUID sensorInstallationId,
        UUID sensorId,
        UUID educationalEnvironmentId,
        Instant installedAt,
        Instant removedAt) {

    public static SensorInstallationResponse from(SensorInstallation installation) {
        return new SensorInstallationResponse(
                installation.getId(),
                installation.getSensorId(),
                installation.getEducationalEnvironmentId(),
                installation.getInstalledAt(),
                installation.getRemovedAt());
    }
}
