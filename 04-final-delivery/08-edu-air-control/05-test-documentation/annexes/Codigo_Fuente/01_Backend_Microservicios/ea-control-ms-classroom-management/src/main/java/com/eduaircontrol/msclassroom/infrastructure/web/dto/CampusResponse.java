package com.eduaircontrol.msclassroom.infrastructure.web.dto;

import com.eduaircontrol.msclassroom.domain.model.Campus;
import java.time.Instant;
import java.util.UUID;

public record CampusResponse(
        UUID campusId,
        String code,
        String name,
        String city,
        String status,
        Instant createdAt,
        Instant updatedAt) {

    public static CampusResponse from(Campus campus) {
        return new CampusResponse(
                campus.getId(),
                campus.getCode(),
                campus.getName(),
                campus.getCity(),
                campus.getStatus().name(),
                campus.getCreatedAt(),
                campus.getUpdatedAt());
    }
}
