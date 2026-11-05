package com.eduaircontrol.msmonitoring.monitoring.domain.port.out;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentMeasurement;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistencia de mediciones.
 */
public interface MeasurementRepository {

    EnvironmentMeasurement save(EnvironmentMeasurement measurement);

    boolean existsByInstallationAndVariableAndInstant(UUID installationId, UUID variableId,
                                                     Instant measuredAt);

    Optional<EnvironmentMeasurement> findById(UUID id);

    List<EnvironmentMeasurement> latestByEnvironment(UUID environmentId);

    Page<EnvironmentMeasurement> history(UUID environmentId, UUID variableId,
                                         Instant from, Instant to, Pageable pageable);
}
