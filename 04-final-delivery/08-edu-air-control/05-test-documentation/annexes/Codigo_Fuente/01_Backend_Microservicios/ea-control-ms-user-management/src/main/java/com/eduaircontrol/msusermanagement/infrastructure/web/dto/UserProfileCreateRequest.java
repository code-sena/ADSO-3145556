package com.eduaircontrol.msusermanagement.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UserProfileCreateRequest(
        @NotNull(message = "userId is required") UUID userId,
        @NotBlank(message = "fullName is required") @Size(max = 150) String fullName,
        @Size(max = 30) String phone,
        @Size(max = 100) String department,
        @Size(max = 100) String position,
        @Size(max = 10) String locale,
        @Size(max = 500) String avatarUrl) {
}
