package com.eduaircontrol.msclassroom.domain.port.out;

import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.model.EducationalEnvironment;
import com.eduaircontrol.msclassroom.domain.model.RecordStatus;
import java.util.Optional;
import java.util.UUID;

public interface EducationalEnvironmentRepository {

    EducationalEnvironment save(EducationalEnvironment environment);

    Optional<EducationalEnvironment> findById(UUID id);

    boolean existsByCampusAndCode(UUID campusId, String code);

    boolean existsActiveCampus(UUID campusId);

    boolean existsActiveEnvironmentType(UUID environmentTypeId);

    Optional<UUID> findCampusInstitutionId(UUID campusId);

    PageResult<EducationalEnvironment> search(String query, RecordStatus status,
            UUID campusId, UUID environmentTypeId, UUID institutionId, int page, int limit);
}
