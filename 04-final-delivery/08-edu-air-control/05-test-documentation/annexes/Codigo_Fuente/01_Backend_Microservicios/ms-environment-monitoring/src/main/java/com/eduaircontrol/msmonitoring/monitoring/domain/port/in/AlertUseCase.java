package com.eduaircontrol.msmonitoring.monitoring.domain.port.in;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Casos de uso de alertas.
 */
public interface AlertUseCase {

    /** Lista alertas con filtros y paginacion. */
    List<AlertView> list(AlertQuery query);

    /** Reconoce una alerta activa. */
    AlertView acknowledge(UUID alertId, UUID acknowledgedBy, Instant at);

    record AlertQuery(
            UUID environmentId,
            String status,
            Instant from,
            Instant to,
            int page,
            int limit) {
    }

    record AlertView(
            UUID id,
            UUID environmentId,
            UUID variableId,
            String variableCode,
            String severityCode,
            java.math.BigDecimal triggeringValue,
            Instant raisedAt,
            Instant acknowledgedAt,
            UUID acknowledgedBy) {
    }
}
