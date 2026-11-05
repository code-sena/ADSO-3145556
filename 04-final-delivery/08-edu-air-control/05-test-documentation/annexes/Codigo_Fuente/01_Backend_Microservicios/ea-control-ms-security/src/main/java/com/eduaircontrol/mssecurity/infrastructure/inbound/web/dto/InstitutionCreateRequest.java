package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InstitutionCreateRequest(
        // Mismo formato que el companyCode del registro: ABC-0000.
        @NotBlank @Size(max = 20)
        @Pattern(regexp = "(?i)^[A-Z]{3}-\\d{4}$",
                message = "code must match the format ABC-0000") String code,
        @NotBlank @Size(max = 150) String name,
        @Size(max = 50) String type) {
}
