package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.Variable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VariableJpaRepository extends JpaRepository<Variable, UUID>, JpaSpecificationExecutor<Variable> {
}
