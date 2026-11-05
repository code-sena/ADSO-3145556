package com.eduaircontrol.mssensor.infrastructure.web.dto;

import com.eduaircontrol.mssensor.domain.model.MeasurementUnit;
import java.time.Instant;
import java.util.UUID;

public record MeasurementUnitResponse(
        UUID measurementUnitId,
        String code,
        String symbol,
        String name,
        Instant createdAt) {

    public static MeasurementUnitResponse from(MeasurementUnit item) {
        return new MeasurementUnitResponse(
                item.getId(),
                item.getCode(),
                item.getSymbol(),
                item.getName(),
                item.getCreatedAt());
    }
}
