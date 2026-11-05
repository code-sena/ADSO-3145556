package com.eduaircontrol.msclassroom.domain.port.out;

import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.model.EnvironmentType;
import java.util.Optional;
import java.util.UUID;

public interface EnvironmentTypeRepository {

    EnvironmentType save(EnvironmentType environmentType);

    Optional<EnvironmentType> findById(UUID id);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    PageResult<EnvironmentType> search(String query, int page, int limit);
}
