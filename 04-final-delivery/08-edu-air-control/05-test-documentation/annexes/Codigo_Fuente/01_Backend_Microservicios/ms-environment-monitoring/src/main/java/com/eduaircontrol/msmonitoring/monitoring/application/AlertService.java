package com.eduaircontrol.msmonitoring.monitoring.application;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentAlert;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.in.AlertUseCase;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.out.AlertRepository;
import com.eduaircontrol.msmonitoring.shared.contract.VariableCatalogPort;
import com.eduaircontrol.msmonitoring.shared.exception.ConflictException;
import com.eduaircontrol.msmonitoring.shared.exception.NotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generacion y gestion de alertas.
 *
 * <p>Las alertas se generan cuando una medicion excede el umbral activo. Son
 * trazables: referencian el umbral y la medicion que las disparo. Una vez
 * reconocidas no se pueden reabrir; si la condicion persiste se genera una alerta
 * nueva.
 */
@Service
@RequiredArgsConstructor
public class AlertService implements AlertUseCase {

    private static final int MAX_LIMIT = 100;

    private final AlertRepository alertRepository;
    private final VariableCatalogPort variableCatalogPort;
    private final Clock clock;

    /**
     * Genera una alerta si no existe ya una activa para la misma medicion.
     * Idempotente: reprocesar el mismo evento no duplica la alerta.
     */
    @Transactional
    public EnvironmentAlert raiseIfAbsent(UUID environmentId, UUID variableId,
                                          UUID thresholdId, UUID measurementId,
                                          String severityCode, Instant at) {
        List<EnvironmentAlert> existing =
                alertRepository.findActiveByMeasurement(measurementId);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        EnvironmentAlert alert = EnvironmentAlert.raise(
                environmentId, variableId, thresholdId, measurementId, at);
        return alertRepository.save(alert);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertView> list(AlertQuery query) {
        Pageable pageable = PageRequest.of(
                Math.max(query.page(), 1) - 1,
                Math.min(Math.max(query.limit(), 1), MAX_LIMIT),
                Sort.by(Sort.Direction.DESC, "raisedAt"));
        Boolean active = query.status() == null ? null
                : !"acknowledged".equalsIgnoreCase(query.status());
        return alertRepository.search(
                        query.environmentId(), active, query.from(), query.to(), pageable)
                .getContent().stream().map(this::toView).toList();
    }

    @Override
    @Transactional
    public AlertView acknowledge(UUID alertId, UUID acknowledgedBy, Instant at) {
        EnvironmentAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new NotFoundException("Alerta no encontrada: " + alertId));
        if (!alert.isActive()) {
            throw new ConflictException("La alerta ya fue reconocida");
        }
        alert.acknowledge(acknowledgedBy, at != null ? at : clock.instant());
        return toView(alertRepository.save(alert));
    }

    private AlertView toView(EnvironmentAlert alert) {
        String variableCode = variableCatalogPort.findById(alert.getVariableId())
                .map(VariableCatalogPort.VariableRef::code)
                .orElse(alert.getVariableId().toString());
        // La severidad se resuelve por el umbral; por ahora se deriva del estado.
        // El detalle completo (valor disparador) requiere JOIN con la medicion.
        return new AlertView(
                alert.getId(),
                alert.getEducationalEnvironmentId(),
                alert.getVariableId(),
                variableCode,
                alert.isActive() ? "ACTIVE" : "ACKNOWLEDGED",
                null,
                alert.getRaisedAt(),
                alert.getAcknowledgedAt(),
                alert.getAcknowledgedBy());
    }
}
