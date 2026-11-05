package com.eduaircontrol.mssensor.domain.port.out;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SensorInstallationRepository {

    SensorInstallation save(SensorInstallation installation);

    Optional<SensorInstallation> findById(UUID id);

    PageResult<SensorInstallation> search(UUID sensorId, UUID educationalEnvironmentId,
            Boolean active, int page, int limit);

    boolean existsOverlapForSensor(UUID sensorId, Instant installedAt);

    boolean hasAnyForSensor(UUID sensorId);
}
