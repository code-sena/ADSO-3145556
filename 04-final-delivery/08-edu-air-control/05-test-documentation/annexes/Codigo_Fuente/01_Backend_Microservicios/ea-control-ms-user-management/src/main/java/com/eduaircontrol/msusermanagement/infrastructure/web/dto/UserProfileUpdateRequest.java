package com.eduaircontrol.msusermanagement.infrastructure.web.dto;

import com.eduaircontrol.msusermanagement.domain.model.RecordStatus;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @Size(max = 150) String fullName,
        @Size(max = 30) String phone,
        @Size(max = 100) String department,
        @Size(max = 100) String position,
        @Size(max = 10) String locale,
        @Size(max = 500) String avatarUrl,
        RecordStatus status) {
}
