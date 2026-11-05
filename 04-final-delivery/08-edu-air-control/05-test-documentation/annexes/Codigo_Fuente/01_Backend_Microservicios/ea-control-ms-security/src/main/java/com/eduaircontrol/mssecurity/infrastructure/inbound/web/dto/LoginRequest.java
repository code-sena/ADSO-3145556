package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        String companyCode) {
}
