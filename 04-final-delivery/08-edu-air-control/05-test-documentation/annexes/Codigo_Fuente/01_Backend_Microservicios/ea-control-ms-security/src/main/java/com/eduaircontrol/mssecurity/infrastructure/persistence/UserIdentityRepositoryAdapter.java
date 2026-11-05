package com.eduaircontrol.mssecurity.infrastructure.persistence;

import com.eduaircontrol.mssecurity.domain.model.UserIdentity;
import com.eduaircontrol.mssecurity.application.port.UserIdentityRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserIdentityRepositoryAdapter implements UserIdentityRepository {

    private final UserIdentityJpaRepository jpaRepository;

    @Override
    public UserIdentity save(UserIdentity identity) {
        return jpaRepository.save(identity);
    }

    @Override
    public Optional<UserIdentity> findByProviderAndProviderUserId(String provider, String providerUserId) {
        return jpaRepository.findByProviderAndProviderUserId(provider, providerUserId);
    }

    @Override
    public Optional<UserIdentity> findByUserIdAndProvider(UUID userId, String provider) {
        return jpaRepository.findByUserIdAndProvider(userId, provider);
    }
}
