package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.Sensor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SensorJpaRepository extends JpaRepository<Sensor, UUID>, JpaSpecificationExecutor<Sensor> {
}
