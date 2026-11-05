package com.eduaircontrol.mssensor.infrastructure.web.dto;

import com.eduaircontrol.mssensor.domain.model.Variable;
import java.time.Instant;
import java.util.UUID;

public record VariableResponse(
        UUID variableId,
        String code,
        String name,
        UUID measurementUnitId,
        String description,
        Instant createdAt) {

    public static VariableResponse from(Variable variable) {
        return new VariableResponse(
                variable.getId(),
                variable.getCode(),
                variable.getName(),
                variable.getMeasurementUnitId(),
                variable.getDescription(),
                variable.getCreatedAt());
    }
}
