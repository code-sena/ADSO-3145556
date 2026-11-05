package com.eduaircontrol.mssecurity.infrastructure.persistence;

import com.eduaircontrol.mssecurity.domain.model.UserIdentity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentityJpaRepository extends JpaRepository<UserIdentity, UUID> {

    Optional<UserIdentity> findByProviderAndProviderUserId(String provider, String providerUserId);

    Optional<UserIdentity> findByUserIdAndProvider(UUID userId, String provider);
}
