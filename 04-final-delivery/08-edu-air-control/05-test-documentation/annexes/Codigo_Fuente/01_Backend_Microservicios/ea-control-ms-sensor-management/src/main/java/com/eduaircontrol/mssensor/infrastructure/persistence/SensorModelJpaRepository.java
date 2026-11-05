package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.SensorModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SensorModelJpaRepository extends JpaRepository<SensorModel, UUID>, JpaSpecificationExecutor<SensorModel> {
}
