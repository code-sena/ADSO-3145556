package com.eduaircontrol.msclassroom.application;

import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.port.out.EducationalEnvironmentRepository;
import com.eduaircontrol.msclassroom.infrastructure.messaging.OutboxWriter;
import com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentCreatedEvent;
import com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentUpdatedEvent;
import com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentRemovedEvent;
import com.eduaircontrol.msclassroom.shared.exception.ConflictException;
import com.eduaircontrol.msclassroom.shared.exception.NotFoundException;
import com.eduaircontrol.msclassroom.shared.exception.ValidationException;
import com.eduaircontrol.msclassroom.domain.model.EducationalEnvironment;
import com.eduaircontrol.msclassroom.domain.model.RecordStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EducationalEnvironmentService {

    private final EducationalEnvironmentRepository environmentRepository;
    private final OutboxWriter outboxWriter;

    @Transactional(readOnly = true)
    public PageResult<EducationalEnvironment> list(String query, RecordStatus status,
            UUID campusId, UUID environmentTypeId, int page, int limit) {
        return environmentRepository.search(query, status, campusId, environmentTypeId,
                TenantContext.institutionId(), page, limit);
    }

    @Transactional(readOnly = true)
    public EducationalEnvironment get(UUID id) {
        return environmentRepository.findById(id)
                .filter(environment -> !environment.isDeleted())
                .orElseThrow(() -> new NotFoundException("Educational environment not found: " + id));
    }

    @Transactional
    public EducationalEnvironment create(UUID campusId, String code, String name,
            UUID environmentTypeId, Integer floor, BigDecimal areaM2,
            Integer occupancyCapacity, RecordStatus status) {
        String normalizedCode = CampusService.requireText(code, "code").toUpperCase();
        requireReferences(campusId, environmentTypeId);
        if (environmentRepository.existsByCampusAndCode(campusId, normalizedCode)) {
            throw new ConflictException("Environment code already exists in this campus: " + normalizedCode);
        }
        EducationalEnvironment environment = EducationalEnvironment.builder()
                .campusId(campusId)
                .institutionId(environmentRepository.findCampusInstitutionId(campusId)
                        .orElse(TenantContext.institutionId()))
                .code(normalizedCode)
                .name(name)
                .environmentTypeId(environmentTypeId)
                .floor(floor)
                .areaM2(areaM2)
                .occupancyCapacity(occupancyCapacity)
                .status(status != null ? status : RecordStatus.ACTIVE)
                .build();
        EducationalEnvironment saved = environmentRepository.save(environment);

        // Emitir evento de creación
        outboxWriter.append(
                "EducationalEnvironmentCreated",
                "EducationalEnvironment",
                environment.getId().toString(),
                "classroom.educational_environment.created",
                com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentCreatedEvent
                        .of(environment.getId(), environment.getCode(), environment.getName(),
                            environment.getCampusId(), environment.getEnvironmentTypeId(),
                            environment.getFloor(), environment.getStatus().name(), Instant.now()),
                Instant.now()
        );

        return environment;
    }

    @Transactional
    public EducationalEnvironment update(UUID id, UUID campusId, String code, String name,
            UUID environmentTypeId, Integer floor, BigDecimal areaM2,
            Integer occupancyCapacity, RecordStatus status) {
        EducationalEnvironment environment = get(id);
        UUID targetCampus = campusId != null ? campusId : environment.getCampusId();
        if (campusId != null || code != null) {
            requireCampus(targetCampus);
        }
        if (campusId != null) {
            environment.setCampusId(campusId);
            environment.setInstitutionId(environmentRepository.findCampusInstitutionId(campusId)
                    .orElse(environment.getInstitutionId()));
        }
        if (code != null) {
            String normalizedCode = CampusService.requireText(code, "code").toUpperCase();
            if (!environment.getCode().equals(normalizedCode)
                    && environmentRepository.existsByCampusAndCode(targetCampus, normalizedCode)) {
                throw new ConflictException("Environment code already exists in this campus: " + normalizedCode);
            }
            environment.setCode(normalizedCode);
        }
        if (name != null) {
            environment.setName(name);
        }
        if (environmentTypeId != null) {
            environment.setEnvironmentTypeId(environmentTypeId);
        }
        if (floor != null) {
            environment.setFloor(floor);
        }
        if (areaM2 != null) {
            environment.setAreaM2(areaM2);
        }
        if (occupancyCapacity != null) {
            environment.setOccupancyCapacity(occupancyCapacity);
        }
        if (status != null) {
            environment.setStatus(status);
        }
        EducationalEnvironment saved = environmentRepository.save(environment);

        // Emitir evento de actualización
        outboxWriter.append(
                "EducationalEnvironmentUpdated",
                "EducationalEnvironment",
                environment.getId().toString(),
                "classroom.educational_environment.updated",
                com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentUpdatedEvent
                        .builder()
                        .eventId(java.util.UUID.randomUUID())
                        .aggregateId(saved.getId())
                        .payload(com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentUpdatedEvent.Payload.builder()
                                .environmentId(environment.getId())
                                .code(environment.getCode())
                                .name(environment.getName())
                                .campusId(environment.getCampusId())
                                .environmentTypeId(environment.getEnvironmentTypeId())
                                .floor(environment.getFloor())
                                .status(environment.getStatus().name())
                                .build())
                        .occurredAt(java.time.Instant.now())
                        .build(),
                java.time.Instant.now()
        );

        return saved;
    }

    @Transactional
    public void delete(UUID id) {
        EducationalEnvironment environment = get(id);
        environment.softDelete();
        environmentRepository.save(environment);

        // Emitir evento de eliminación (soft delete)
        outboxWriter.append(
                "EducationalEnvironmentRemoved",
                "EducationalEnvironment",
                environment.getId().toString(),
                "classroom.educational_environment.removed",
                com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentRemovedEvent
                        .builder()
                        .eventId(java.util.UUID.randomUUID())
                        .aggregateId(environment.getId())
                        .payload(com.eduaircontrol.msclassroom.infrastructure.messaging.events.EducationalEnvironmentRemovedEvent.Payload.builder()
                                .environmentId(environment.getId())
                                .code(environment.getCode())
                                .name(environment.getName())
                                .campusId(environment.getCampusId())
                                .environmentTypeId(environment.getEnvironmentTypeId())
                                .floor(environment.getFloor())
                                .status(environment.getStatus().name())
                                .build())
                        .occurredAt(java.time.Instant.now())
                        .build(),
                java.time.Instant.now()
        );
    }

    private void requireReferences(UUID campusId, UUID environmentTypeId) {
        if (campusId == null) {
            throw new ValidationException("campusId is required");
        }
        if (environmentTypeId == null) {
            throw new ValidationException("environmentTypeId is required");
        }
        requireCampus(campusId);
        requireEnvironmentType(environmentTypeId);
    }

    private void requireCampus(UUID campusId) {
        if (!environmentRepository.existsActiveCampus(campusId)) {
            throw new ValidationException("campusId does not reference an existing campus: " + campusId);
        }
    }

    private void requireEnvironmentType(UUID environmentTypeId) {
        if (!environmentRepository.existsActiveEnvironmentType(environmentTypeId)) {
            throw new ValidationException(
                    "environmentTypeId does not reference an existing environment type: " + environmentTypeId);
        }
    }
}
