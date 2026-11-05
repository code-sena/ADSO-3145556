package com.eduaircontrol.mssecurity.application;

import com.eduaircontrol.mssecurity.application.port.PasswordResetTokenRepository;
import com.eduaircontrol.mssecurity.application.port.UserRepository;
import com.eduaircontrol.mssecurity.domain.exception.ValidationException;
import com.eduaircontrol.mssecurity.domain.model.PasswordResetToken;
import com.eduaircontrol.mssecurity.domain.model.User;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Recuperación de contraseña por código de un solo uso (forgot / verify / reset /
 * resend). En producción el código se envía por correo; aquí se registra en el log
 * (no hay SMTP configurado en el servicio).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long CODE_TTL_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Transactional
    public void requestCode(String email) {
        userRepository.findByEmail(email)
                .filter(User::isActive)
                .ifPresent(this::issueCode);
        // No se revela si el correo existe o no.
    }

    @Transactional
    public void verifyCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .filter(User::isActive)
                .orElseThrow(() -> new ValidationException("Invalid or expired code"));
        PasswordResetToken token = tokenRepository.findLatestUnused(user.getId())
                .filter(candidate -> candidate.isUsable(clock.instant()))
                .filter(candidate -> candidate.getCodeHash().equals(AuthService.sha256(code)))
                .orElseThrow(() -> new ValidationException("Invalid or expired code"));
        // válido — no se consume hasta el reset
        token.getId();
    }

    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        validatePassword(newPassword);
        User user = userRepository.findByEmail(email)
                .filter(User::isActive)
                .orElseThrow(() -> new ValidationException("Invalid or expired code"));
        Instant now = clock.instant();
        PasswordResetToken token = tokenRepository.findLatestUnused(user.getId())
                .filter(candidate -> candidate.isUsable(now))
                .filter(candidate -> candidate.getCodeHash().equals(AuthService.sha256(code)))
                .orElseThrow(() -> new ValidationException("Invalid or expired code"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        user.setUpdatedAt(now);
        userRepository.save(user);

        token.setUsedAt(now);
        tokenRepository.save(token);
    }

    private void issueCode(User user) {
        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        Instant now = clock.instant();
        PasswordResetToken token = PasswordResetToken.builder()
                .userId(user.getId())
                .codeHash(AuthService.sha256(code))
                .expiresAt(now.plus(CODE_TTL_MINUTES, ChronoUnit.MINUTES))
                .createdAt(now)
                .build();
        tokenRepository.save(token);
        log.info("Password reset code for {}: {} (dev only — send by email in production)",
                user.getEmail(), code);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 100
                || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*")) {
            throw new ValidationException(
                    "password: minimum 8 characters, one uppercase letter and one number");
        }
    }
}
