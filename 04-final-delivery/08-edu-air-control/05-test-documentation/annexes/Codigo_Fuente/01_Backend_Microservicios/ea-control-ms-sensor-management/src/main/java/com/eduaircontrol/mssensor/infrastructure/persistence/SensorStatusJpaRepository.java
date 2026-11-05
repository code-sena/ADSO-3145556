package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.SensorStatus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SensorStatusJpaRepository extends JpaRepository<SensorStatus, UUID>, JpaSpecificationExecutor<SensorStatus> {
}
