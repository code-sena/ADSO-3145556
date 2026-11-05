package com.eduaircontrol.msclassroom.infrastructure.persistence;

import com.eduaircontrol.msclassroom.domain.model.EnvironmentType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnvironmentTypeJpaRepository
        extends JpaRepository<EnvironmentType, UUID>, JpaSpecificationExecutor<EnvironmentType> {

    boolean existsByIdAndDeletedAtIsNull(UUID id);
}
