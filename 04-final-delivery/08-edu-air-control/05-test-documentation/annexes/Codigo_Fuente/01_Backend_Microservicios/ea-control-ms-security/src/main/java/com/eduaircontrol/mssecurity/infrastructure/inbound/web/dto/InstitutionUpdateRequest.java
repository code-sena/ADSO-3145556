package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import com.eduaircontrol.mssecurity.domain.model.InstitutionStatus;
import jakarta.validation.constraints.Size;

public record InstitutionUpdateRequest(
        @Size(max = 150) String name,
        @Size(max = 50) String type,
        InstitutionStatus status) {
}
