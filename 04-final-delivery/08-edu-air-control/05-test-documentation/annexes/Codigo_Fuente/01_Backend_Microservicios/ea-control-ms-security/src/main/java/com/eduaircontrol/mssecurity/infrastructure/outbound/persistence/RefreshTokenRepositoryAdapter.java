package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.application.port.RefreshTokenRepository;
import com.eduaircontrol.mssecurity.domain.model.RefreshToken;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash);
    }

    @Override
    public List<RefreshToken> findActiveByUserId(UUID userId) {
        return jpaRepository.findByUserIdAndRevokedAtIsNull(userId);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return jpaRepository.save(refreshToken);
    }
}
