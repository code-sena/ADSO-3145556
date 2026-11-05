package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(@NotBlank @Email String email) {
}
