package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.domain.model.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);
}
