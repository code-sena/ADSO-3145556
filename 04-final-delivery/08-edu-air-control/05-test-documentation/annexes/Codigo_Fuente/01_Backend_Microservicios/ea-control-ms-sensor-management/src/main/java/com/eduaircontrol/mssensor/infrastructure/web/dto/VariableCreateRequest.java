package com.eduaircontrol.mssensor.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record VariableCreateRequest(
        @NotBlank(message = "code is required")
        @Size(max = 30, message = "code must be at most 30 characters") String code,
        @NotBlank(message = "name is required")
        @Size(max = 80, message = "name must be at most 80 characters") String name,
        @NotNull(message = "measurementUnitId is required") UUID measurementUnitId,
        @Size(max = 255, message = "description must be at most 255 characters") String description) {
}
