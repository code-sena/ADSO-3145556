package com.eduaircontrol.msmonitoring.monitoring.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentAlert;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.out.AlertRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador JPA de alertas.
 */
@Repository
@RequiredArgsConstructor
public class AlertRepositoryAdapter implements AlertRepository {

    private final EnvironmentAlertJpaRepository alerts;

    @Override
    @Transactional
    public EnvironmentAlert save(EnvironmentAlert alert) {
        return alerts.save(alert);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnvironmentAlert> findById(UUID id) {
        return alerts.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnvironmentAlert> search(UUID environmentId, Boolean active,
                                         Instant from, Instant to, Pageable pageable) {
        return alerts.search(environmentId, active, from, to, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvironmentAlert> findActiveByMeasurement(UUID measurementId) {
        return alerts.findByTriggeringMeasurementIdAndAcknowledgedAtIsNull(measurementId);
    }
}
