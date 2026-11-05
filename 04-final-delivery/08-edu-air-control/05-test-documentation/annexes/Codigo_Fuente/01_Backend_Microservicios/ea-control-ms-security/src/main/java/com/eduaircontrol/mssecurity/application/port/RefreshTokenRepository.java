package com.eduaircontrol.mssecurity.application.port;

import com.eduaircontrol.mssecurity.domain.model.RefreshToken;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findActiveByUserId(UUID userId);

    RefreshToken save(RefreshToken refreshToken);
}
