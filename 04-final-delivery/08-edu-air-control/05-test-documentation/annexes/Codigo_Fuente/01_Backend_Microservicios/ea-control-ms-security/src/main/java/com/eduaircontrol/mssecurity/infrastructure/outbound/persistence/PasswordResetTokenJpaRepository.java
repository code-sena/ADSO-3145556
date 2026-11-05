package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.domain.model.PasswordResetToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findTopByUserIdAndUsedAtIsNullOrderByCreatedAtDesc(UUID userId);
}
