package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.domain.model.UserRole;
import com.eduaircontrol.mssecurity.domain.model.UserRoleKey;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleJpaRepository extends JpaRepository<UserRole, UserRoleKey> {

    List<UserRole> findByUserId(UUID userId);
}
