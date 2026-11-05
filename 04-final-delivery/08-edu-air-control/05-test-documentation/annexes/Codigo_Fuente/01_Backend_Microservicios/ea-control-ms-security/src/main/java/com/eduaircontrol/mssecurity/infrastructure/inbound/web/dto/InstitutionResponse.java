package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import com.eduaircontrol.mssecurity.domain.model.Institution;
import com.eduaircontrol.mssecurity.domain.model.InstitutionStatus;
import java.time.Instant;
import java.util.UUID;

public record InstitutionResponse(
        UUID id,
        String code,
        String name,
        String type,
        InstitutionStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public static InstitutionResponse from(Institution institution) {
        return new InstitutionResponse(
                institution.getId(),
                institution.getCode(),
                institution.getName(),
                institution.getType(),
                institution.getStatus(),
                institution.getCreatedAt(),
                institution.getUpdatedAt());
    }
}
