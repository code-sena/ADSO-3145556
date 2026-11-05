package com.eduaircontrol.mssensor.application;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.port.out.InstallationEventPublisher;
import com.eduaircontrol.mssensor.domain.port.out.SensorInstallationRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import com.eduaircontrol.mssensor.shared.exception.ValidationException;
import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorInstallationService {

    private final SensorInstallationRepository installationRepository;
    private final SensorRepository sensorRepository;
    private final InstallationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public PageResult<SensorInstallation> list(UUID sensorId, UUID educationalEnvironmentId,
            Boolean active, int page, int limit) {
        return installationRepository.search(sensorId, educationalEnvironmentId, active, page, limit);
    }

    @Transactional(readOnly = true)
    public SensorInstallation get(UUID id) {
        return installationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sensor installation not found: " + id));
    }

    public SensorInstallation create(UUID sensorId, UUID educationalEnvironmentId, Instant installedAt) {
        if (sensorRepository.findById(sensorId).isEmpty()) {
            throw new NotFoundException("Sensor not found: " + sensorId);
        }
        Instant installed = requireInstant(installedAt, "installedAt");
        if (installationRepository.existsOverlapForSensor(sensorId, installed)) {
            throw new ConflictException(
                    "Sensor already has an overlapping installation: " + sensorId);
        }
        SensorInstallation installation = SensorInstallation.builder()
                .sensorId(sensorId)
                .educationalEnvironmentId(SensorService.requireId(
                        educationalEnvironmentId, "educationalEnvironmentId"))
                .installedAt(installed)
                .build();
        SensorInstallation saved = installationRepository.save(installation);
        // Se emite dentro de esta misma transaccion (outbox, ADR-007): si el cambio
        // no se confirma, tampoco se publica el evento.
        eventPublisher.installed(saved);
        return saved;
    }

    public SensorInstallation close(UUID id) {
        SensorInstallation installation = get(id);
        if (!installation.isActive()) {
            throw new ConflictException("Sensor installation already closed: " + id);
        }
        if (installation.getInstalledAt().isAfter(Instant.now())) {
            throw new ValidationException("installedAt is in the future; installation cannot be closed yet");
        }
        installation.setRemovedAt(Instant.now());
        SensorInstallation saved = installationRepository.save(installation);
        eventPublisher.removed(saved);
        return saved;
    }

    private static Instant requireInstant(Instant value, String field) {
        if (value == null) {
            throw new ValidationException(field + " is required");
        }
        return value;
    }
}
