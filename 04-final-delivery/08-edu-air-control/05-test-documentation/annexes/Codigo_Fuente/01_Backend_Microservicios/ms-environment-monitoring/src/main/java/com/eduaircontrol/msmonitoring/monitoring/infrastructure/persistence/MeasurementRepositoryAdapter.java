package com.eduaircontrol.msmonitoring.monitoring.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentMeasurement;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.out.MeasurementRepository;
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
 * Adaptador JPA de mediciones.
 */
@Repository
@RequiredArgsConstructor
public class MeasurementRepositoryAdapter implements MeasurementRepository {

    private final EnvironmentMeasurementJpaRepository measurements;

    @Override
    @Transactional
    public EnvironmentMeasurement save(EnvironmentMeasurement measurement) {
        return measurements.save(measurement);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByInstallationAndVariableAndInstant(UUID installationId, UUID variableId,
                                                             Instant measuredAt) {
        return measurements.existsBySensorInstallationIdAndVariableIdAndMeasuredAt(
                installationId, variableId, measuredAt);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnvironmentMeasurement> findById(UUID id) {
        return measurements.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvironmentMeasurement> latestByEnvironment(UUID environmentId) {
        return measurements.latestByEnvironment(environmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnvironmentMeasurement> history(UUID environmentId, UUID variableId,
                                                Instant from, Instant to, Pageable pageable) {
        return measurements.history(environmentId, variableId, from, to, pageable);
    }
}
