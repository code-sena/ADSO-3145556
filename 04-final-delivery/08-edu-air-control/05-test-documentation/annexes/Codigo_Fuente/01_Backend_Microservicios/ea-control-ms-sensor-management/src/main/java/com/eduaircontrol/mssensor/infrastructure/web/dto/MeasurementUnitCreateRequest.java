package com.eduaircontrol.mssensor.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MeasurementUnitCreateRequest(
        @NotBlank(message = "code is required")
        @Size(max = 30, message = "code must be at most 30 characters") String code,
        @NotBlank(message = "symbol is required")
        @Size(max = 10, message = "symbol must be at most 10 characters") String symbol,
        @NotBlank(message = "name is required")
        @Size(max = 80, message = "name must be at most 80 characters") String name) {
}
