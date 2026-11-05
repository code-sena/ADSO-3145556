package com.eduaircontrol.msmonitoring.monitoring.domain.port.out;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentAlert;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Persistencia de alertas.
 */
public interface AlertRepository {

    EnvironmentAlert save(EnvironmentAlert alert);

    Optional<EnvironmentAlert> findById(UUID id);

    Page<EnvironmentAlert> search(UUID environmentId, Boolean active,
                                  java.time.Instant from, java.time.Instant to,
                                  Pageable pageable);

    List<EnvironmentAlert> findActiveByMeasurement(UUID measurementId);
}
