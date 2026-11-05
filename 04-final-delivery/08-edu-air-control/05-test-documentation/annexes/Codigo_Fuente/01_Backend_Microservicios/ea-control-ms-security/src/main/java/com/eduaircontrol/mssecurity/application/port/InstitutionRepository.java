package com.eduaircontrol.mssecurity.application.port;

import com.eduaircontrol.mssecurity.domain.model.Institution;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstitutionRepository {

    Optional<Institution> findById(UUID id);

    Optional<Institution> findByCode(String code);

    boolean existsByCode(String code);

    List<Institution> findAll();

    Institution save(Institution institution);
}
