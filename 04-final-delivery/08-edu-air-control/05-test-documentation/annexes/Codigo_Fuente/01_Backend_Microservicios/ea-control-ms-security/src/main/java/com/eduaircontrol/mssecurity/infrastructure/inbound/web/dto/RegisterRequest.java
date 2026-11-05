package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RegisterRequest(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Size(min = 1, max = 100) String username,
        // Formato del codigo de empresa: tres letras mayusculas, guion y cuatro
        // digitos (ABC-0000). Lo validan tambien los frontends; aqui es donde se
        // fija, porque es el que da de alta de verdad.
        @NotBlank @Size(max = 20)
        @Pattern(regexp = "(?i)^[A-Z]{3}-\\d{4}$",
                message = "companyCode must match the format ABC-0000") String companyCode,
        UUID campusId) {
}
