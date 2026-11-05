package com.eduaircontrol.msclassroom.domain.port.out;

import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.model.Campus;
import com.eduaircontrol.msclassroom.domain.model.RecordStatus;
import java.util.Optional;
import java.util.UUID;

public interface CampusRepository {

    Campus save(Campus campus);

    Optional<Campus> findById(UUID id);

    boolean existsByCode(String code);

    PageResult<Campus> search(String query, RecordStatus status, UUID institutionId, int page, int limit);
}
