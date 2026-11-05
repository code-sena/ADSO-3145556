package com.eduaircontrol.msclassroom.infrastructure.persistence;

import com.eduaircontrol.msclassroom.domain.model.Campus;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CampusJpaRepository extends JpaRepository<Campus, UUID>, JpaSpecificationExecutor<Campus> {

    boolean existsByIdAndDeletedAtIsNull(UUID id);
}
