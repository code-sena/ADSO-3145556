package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank @Email String email,
        @NotBlank String code,
        @NotBlank @Size(min = 8, max = 100) String newPassword) {
}
