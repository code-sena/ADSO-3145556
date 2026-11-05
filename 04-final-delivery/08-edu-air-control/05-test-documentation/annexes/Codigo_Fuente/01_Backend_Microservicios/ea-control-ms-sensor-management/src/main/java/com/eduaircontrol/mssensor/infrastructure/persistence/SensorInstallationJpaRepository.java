package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SensorInstallationJpaRepository
        extends JpaRepository<SensorInstallation, UUID>, JpaSpecificationExecutor<SensorInstallation> {

    boolean existsBySensorId(UUID sensorId);
}
