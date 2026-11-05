package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.MeasurementUnit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MeasurementUnitJpaRepository extends JpaRepository<MeasurementUnit, UUID>, JpaSpecificationExecutor<MeasurementUnit> {
}
