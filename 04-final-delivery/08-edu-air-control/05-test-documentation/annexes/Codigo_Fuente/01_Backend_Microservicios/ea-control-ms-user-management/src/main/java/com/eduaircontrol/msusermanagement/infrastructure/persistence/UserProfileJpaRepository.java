package com.eduaircontrol.msusermanagement.infrastructure.persistence;

import com.eduaircontrol.msusermanagement.domain.model.UserProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserProfileJpaRepository
        extends JpaRepository<UserProfile, UUID>, JpaSpecificationExecutor<UserProfile> {

    boolean existsByUserId(UUID userId);

    Optional<UserProfile> findByUserId(UUID userId);
}
