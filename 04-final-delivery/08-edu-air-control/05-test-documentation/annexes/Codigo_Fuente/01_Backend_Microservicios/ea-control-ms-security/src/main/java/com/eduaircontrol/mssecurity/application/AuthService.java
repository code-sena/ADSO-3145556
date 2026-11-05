package com.eduaircontrol.mssecurity.application;

import com.eduaircontrol.mssecurity.application.port.InstitutionRepository;
import com.eduaircontrol.mssecurity.application.port.RefreshTokenRepository;
import com.eduaircontrol.mssecurity.application.port.RoleRepository;
import com.eduaircontrol.mssecurity.application.port.TokenBlacklist;
import com.eduaircontrol.mssecurity.application.port.UserRepository;
import com.eduaircontrol.mssecurity.application.port.UserRoleRepository;
import com.eduaircontrol.mssecurity.domain.exception.AccountLockedException;
import com.eduaircontrol.mssecurity.domain.exception.ConflictException;
import com.eduaircontrol.mssecurity.domain.exception.UnauthorizedException;
import com.eduaircontrol.mssecurity.domain.exception.ValidationException;
import com.eduaircontrol.mssecurity.domain.model.Institution;
import com.eduaircontrol.mssecurity.domain.model.RefreshToken;
import com.eduaircontrol.mssecurity.domain.model.Role;
import com.eduaircontrol.mssecurity.domain.model.User;
import com.eduaircontrol.mssecurity.domain.model.UserRole;
import com.eduaircontrol.mssecurity.infrastructure.security.JwtService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Flujo de autenticación: registro, login con bloqueo, rotación de refresh
 * tokens y cierre de sesión. Reglas en 02-auth-service/decisions.md y data-model.md.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    static final String ROLE_USER = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final InstitutionRepository institutionRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;
    private final TokenBlacklist tokenBlacklist;

    @Value("${app.security.lockout-max-attempts:5}")
    private int lockoutMaxAttempts;

    @Value("${app.security.lockout-duration-minutes:15}")
    private long lockoutDurationMinutes;

    @Value("${app.security.refresh-ttl-days:7}")
    private long refreshTtlDays;

    @Transactional
    public AuthResult register(String email, String password, String username, String companyCode,
            UUID campusId, String userAgent) {
        validateEmail(email);
        validatePassword(password);
        if (username == null || username.isBlank() || username.length() > 100) {
            throw new ValidationException("username: must be between 1 and 100 characters");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("EMAIL_ALREADY_EXISTS", "Email is already registered");
        }
        Institution institution = resolveInstitution(companyCode);
        Instant now = clock.instant();
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setEmailVerified(false);
        user.setFailedAttempts(0);
        user.setInstitutionId(institution.getId());
        user.setCampusId(campusId);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user = userRepository.save(user);

        Role userRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Required role " + ROLE_USER + " is not seeded"));
        userRoleRepository.save(new UserRole(user.getId(), userRole.getId(), now));

        return issueTokens(user, List.of(ROLE_USER), userAgent);
    }

    public AuthResult login(String email, String password, String userAgent) {
        User user = userRepository.findByEmail(email)
                .filter(User::isActive)
                .orElseThrow(() -> new UnauthorizedException("Incorrect email or password"));
        Instant now = clock.instant();

        if (user.isLocked(now)) {
            throw new AccountLockedException("Account temporarily locked due to failed attempts");
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            user.setFailedAttempts(user.getFailedAttempts() + 1);
            if (user.getFailedAttempts() >= lockoutMaxAttempts) {
                user.setLockedUntil(now.plus(lockoutDurationMinutes, ChronoUnit.MINUTES));
            }
            user.setUpdatedAt(now);
            userRepository.save(user);
            throw new UnauthorizedException("Incorrect email or password");
        }
        if (user.getFailedAttempts() > 0 || user.getLockedUntil() != null) {
            user.setFailedAttempts(0);
            user.setLockedUntil(null);
            user.setUpdatedAt(now);
            userRepository.save(user);
        }
        return issueTokens(user, rolesOf(user.getId()), userAgent);
    }

    @Transactional
    public AuthResult refresh(String rawRefreshToken, String userAgent) {
        Instant now = clock.instant();
        RefreshToken token = refreshTokenRepository.findByTokenHash(sha256(rawRefreshToken))
                .filter(found -> found.isUsable(now))
                .orElseThrow(() -> new UnauthorizedException(
                        "REFRESH_TOKEN_INVALID", "Refresh token invalid, expired, or already used"));
        User user = userRepository.findById(token.getUserId())
                .filter(User::isActive)
                .orElseThrow(() -> new UnauthorizedException(
                        "REFRESH_TOKEN_INVALID", "Refresh token invalid, expired, or already used"));

        token.setRevokedAt(now);
        refreshTokenRepository.save(token);
        return issueTokens(user, rolesOf(user.getId()), userAgent);
    }

    @Transactional
    public void logout(UUID userId, String refreshToken, boolean allDevices,
            String accessTokenJti, Instant accessTokenExpiresAt) {
        Instant now = clock.instant();
        tokenBlacklist.blacklist(accessTokenJti, accessTokenExpiresAt);
        if (refreshToken != null) {
            refreshTokenRepository.findByTokenHash(sha256(refreshToken))
                    .filter(found -> found.getUserId().equals(userId) && found.getRevokedAt() == null)
                    .ifPresent(found -> {
                        found.setRevokedAt(now);
                        refreshTokenRepository.save(found);
                    });
        }
        if (allDevices) {
            for (RefreshToken active : refreshTokenRepository.findActiveByUserId(userId)) {
                active.setRevokedAt(now);
                refreshTokenRepository.save(active);
            }
        }
    }

    @Transactional
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .filter(User::isActive)
                .orElseThrow(() -> new UnauthorizedException("Session invalid"));
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new UnauthorizedException("Current password is incorrect");
        }
        validatePassword(newPassword);
        Instant now = clock.instant();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(now);
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(UUID userId, String password) {
        User user = userRepository.findById(userId)
                .filter(User::isActive)
                .orElseThrow(() -> new UnauthorizedException("Session invalid"));
        if (password == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnauthorizedException("Password is incorrect");
        }
        Instant now = clock.instant();
        user.setDeletedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);
    }

    private AuthResult issueTokens(User user, List<String> roles, String userAgent) {
        Instant now = clock.instant();
        String accessToken = jwtService.generateAccessToken(
                user.getId(), user.getEmail(), user.getUsername(), roles,
                user.getInstitutionId(), user.getCampusId());
        String rawRefresh = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setUserId(user.getId());
        token.setTokenHash(sha256(rawRefresh));
        token.setExpiresAt(now.plus(refreshTtlDays, ChronoUnit.DAYS));
        token.setCreatedAt(now);
        token.setUserAgent(userAgent);
        refreshTokenRepository.save(token);
        return new AuthResult(accessToken, rawRefresh, jwtService.expiresInSeconds(),
                user.getId(), user.getEmail(), user.getUsername(), roles,
                user.getInstitutionId(), user.getCampusId());
    }

    private Institution resolveInstitution(String companyCode) {
        if (companyCode == null || companyCode.isBlank()) {
            throw new ValidationException("companyCode must not be blank");
        }
        return institutionRepository.findByCode(companyCode.trim().toUpperCase())
                .orElseThrow(() -> new ValidationException("Unknown institution: " + companyCode));
    }

    private List<String> rolesOf(UUID userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(userRole -> roleRepository.findById(userRole.getRoleId()).map(Role::getName).orElse(null))
                .filter(java.util.Objects::nonNull)
                .sorted()
                .toList();
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || email.length() > 255) {
            throw new ValidationException("email: must be a valid email address");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 100
                || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*")) {
            throw new ValidationException(
                    "password: minimum 8 characters, one uppercase letter and one number");
        }
    }

    /**
     * Emite tokens para un usuario autenticado via red social (sin password).
     * No modifica el usuario, solo emite tokens.
     */
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED)
    public AuthResult loginWithUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new com.eduaircontrol.mssecurity.domain.exception.NotFoundException("User not found: " + userId));
        return issueTokens(user, rolesOf(user.getId()), "social-login");
    }

    static String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }
}
