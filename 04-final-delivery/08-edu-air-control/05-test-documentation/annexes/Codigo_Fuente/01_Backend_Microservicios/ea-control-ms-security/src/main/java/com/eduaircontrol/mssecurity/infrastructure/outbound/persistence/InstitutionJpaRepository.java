package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.domain.model.Institution;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionJpaRepository extends JpaRepository<Institution, UUID> {

    Optional<Institution> findByCode(String code);

    boolean existsByCode(String code);
}
