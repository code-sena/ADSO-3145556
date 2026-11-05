package com.eduaircontrol.mssecurity.application.port;

import com.eduaircontrol.mssecurity.domain.model.UserIdentity;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistencia de vinculos usuario-red social.
 */
public interface UserIdentityRepository {

    UserIdentity save(UserIdentity identity);

    Optional<UserIdentity> findByProviderAndProviderUserId(String provider, String providerUserId);

    Optional<UserIdentity> findByUserIdAndProvider(UUID userId, String provider);
}
