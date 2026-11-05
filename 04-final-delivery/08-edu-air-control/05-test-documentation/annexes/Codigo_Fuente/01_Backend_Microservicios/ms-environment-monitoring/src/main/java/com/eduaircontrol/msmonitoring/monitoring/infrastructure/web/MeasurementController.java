package com.eduaircontrol.msmonitoring.monitoring.infrastructure.web;

import com.eduaircontrol.msmonitoring.monitoring.application.MeasurementService;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.in.MeasurementUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API de mediciones (HU-MON-001 … HU-MON-004).
 *
 * <p>La ingesta principal llega por RabbitMQ desde ms-sensor-management. Este
 * endpoint HTTP es la alternativa para sensores con capacidad HTTPS (ADR-004).
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementUseCase measurementUseCase;

    /** Ingesta HTTP alternativa a MQTT (HU-MON-001). Idempotente por reintento. */
    @PostMapping("/measurements")
    @ResponseStatus(HttpStatus.CREATED)
    public MeasurementIdResponse ingest(@Valid @RequestBody IngestRequest request) {
        UUID id = measurementUseCase.record(new MeasurementUseCase.RecordCommand(
                request.sensorInstallationId(),
                request.variableId(),
                request.value(),
                request.measuredAt()));
        return new MeasurementIdResponse(id);
    }

    /** Ultimos valores por variable de un ambiente (HU-MON-003). */
    @GetMapping("/environments/{id}/current")
    public List<MeasurementUseCase.CurrentValue> current(@PathVariable UUID id) {
        return measurementUseCase.currentValues(id);
    }

    /** Historial paginado por variable y ventana (HU-MON-004). */
    @GetMapping("/environments/{id}/measurements")
    public List<MeasurementUseCase.MeasurementView> history(
            @PathVariable UUID id,
            @RequestParam(name = "variable", required = false) UUID variableId,
            @RequestParam(name = "from", required = false) Instant from,
            @RequestParam(name = "to", required = false) Instant to,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return measurementUseCase.history(new MeasurementUseCase.HistoryQuery(
                id, variableId,
                from != null ? from : Instant.EPOCH,
                to != null ? to : Instant.now(),
                page, limit));
    }

    public record IngestRequest(
            @NotNull UUID sensorInstallationId,
            @NotNull UUID variableId,
            @NotNull BigDecimal value,
            Instant measuredAt) {
    }

    public record MeasurementIdResponse(UUID id) {
    }
}
