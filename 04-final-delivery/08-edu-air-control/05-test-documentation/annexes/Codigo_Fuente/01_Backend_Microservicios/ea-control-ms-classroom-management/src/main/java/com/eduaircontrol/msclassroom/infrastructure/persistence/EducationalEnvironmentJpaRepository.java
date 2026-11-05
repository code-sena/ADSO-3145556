package com.eduaircontrol.msclassroom.infrastructure.persistence;

import com.eduaircontrol.msclassroom.domain.model.EducationalEnvironment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EducationalEnvironmentJpaRepository
        extends JpaRepository<EducationalEnvironment, UUID>, JpaSpecificationExecutor<EducationalEnvironment> {

    boolean existsByCampusIdAndCode(UUID campusId, String code);
}
