package com.eduaircontrol.mssecurity.application.port;

import com.eduaircontrol.mssecurity.domain.model.PasswordResetToken;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository {

    PasswordResetToken save(PasswordResetToken token);

    Optional<PasswordResetToken> findLatestUnused(UUID userId);
}
