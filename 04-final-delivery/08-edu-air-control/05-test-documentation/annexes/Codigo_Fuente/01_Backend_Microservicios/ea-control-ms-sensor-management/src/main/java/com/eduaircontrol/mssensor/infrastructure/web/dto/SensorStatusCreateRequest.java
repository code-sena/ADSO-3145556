package com.eduaircontrol.mssensor.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SensorStatusCreateRequest(
        @NotBlank(message = "code is required")
        @Size(max = 30, message = "code must be at most 30 characters") String code,
        @NotBlank(message = "name is required")
        @Size(max = 80, message = "name must be at most 80 characters") String name) {
}
