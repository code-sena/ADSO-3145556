package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.application.port.PasswordResetTokenRepository;
import com.eduaircontrol.mssecurity.domain.model.PasswordResetToken;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

    private final PasswordResetTokenJpaRepository jpaRepository;

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        return jpaRepository.save(token);
    }

    @Override
    public Optional<PasswordResetToken> findLatestUnused(UUID userId) {
        return jpaRepository.findTopByUserIdAndUsedAtIsNullOrderByCreatedAtDesc(userId);
    }
}
