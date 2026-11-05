package com.eduaircontrol.mssecurity.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.eduaircontrol.mssecurity.domain.model.RefreshToken;
import com.eduaircontrol.mssecurity.domain.model.Role;
import com.eduaircontrol.mssecurity.domain.model.User;
import com.eduaircontrol.mssecurity.infrastructure.security.JwtService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

class AuthServiceTest {

    private static final Instant NOW = Instant.parse("2026-10-06T10:00:00Z");
    private static final String PASSWORD = "SecurePass123!";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private InstitutionRepository institutionRepository;
    private PasswordEncoder passwordEncoder;
    private TokenBlacklist tokenBlacklist;
    private AuthService authService;

    private static final UUID INSTITUTION_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        institutionRepository = mock(InstitutionRepository.class);
        passwordEncoder = new BCryptPasswordEncoder(4);
        tokenBlacklist = mock(TokenBlacklist.class);
        JwtService jwtService = new JwtService("keys/dev-private.pem", "keys/dev-public.pem", 3600);
        authService = new AuthService(userRepository, roleRepository, userRoleRepository,
                refreshTokenRepository, institutionRepository, jwtService, passwordEncoder,
                Clock.fixed(NOW, ZoneOffset.UTC), tokenBlacklist);
        ReflectionTestUtils.setField(authService, "lockoutMaxAttempts", 5);
        ReflectionTestUtils.setField(authService, "lockoutDurationMinutes", 15);
        ReflectionTestUtils.setField(authService, "refreshTtlDays", 7);

        Role userRole = new Role();
        userRole.setId(UUID.randomUUID());
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        com.eduaircontrol.mssecurity.domain.model.Institution institution =
                com.eduaircontrol.mssecurity.domain.model.Institution.builder()
                        .id(INSTITUTION_ID)
                        .code("SEN-4444")
                        .name("SENA")
                        .status(com.eduaircontrol.mssecurity.domain.model.InstitutionStatus.ACTIVE)
                        .build();
        when(institutionRepository.findByCode("SEN-4444")).thenReturn(Optional.of(institution));
        when(institutionRepository.findById(INSTITUTION_ID)).thenReturn(Optional.of(institution));
    }

    @Test
    void registerCreatesUserWithUserRoleAndTokens() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        AuthResult result = authService.register("a@b.com", PASSWORD, "alice", "SEN-4444", null, "junit");

        assertThat(result.roles()).containsExactly("USER");
        assertThat(result.accessToken()).isNotBlank();
        assertThat(result.refreshToken()).isNotBlank();
        assertThat(result.expiresIn()).isEqualTo(3600);
        verify(userRoleRepository).save(any());
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void registerRejectsDuplicateEmail() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register("a@b.com", PASSWORD, "alice", "SEN-4444", null, null))
                .isInstanceOf(ConflictException.class)
                .hasFieldOrPropertyWithValue("code", "EMAIL_ALREADY_EXISTS");
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerRejectsWeakPassword() {
        assertThatThrownBy(() -> authService.register("a@b.com", "weak", "alice", "SEN-4444", null, null))
                .isInstanceOf(ValidationException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginWithWrongPasswordIncrementsFailedAttempts() {
        User user = activeUser(passwordEncoder.encode(PASSWORD));
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login("a@b.com", "WrongPass1", null))
                .isInstanceOf(UnauthorizedException.class)
                .hasFieldOrPropertyWithValue("code", "INVALID_CREDENTIALS");
        assertThat(user.getFailedAttempts()).isEqualTo(1);
        assertThat(user.getLockedUntil()).isNull();
        verify(userRepository).save(user);
    }

    @Test
    void fifthFailedLoginLocksTheAccount() {
        User user = activeUser(passwordEncoder.encode(PASSWORD));
        user.setFailedAttempts(4);
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login("a@b.com", "WrongPass1", null))
                .isInstanceOf(UnauthorizedException.class);
        assertThat(user.getFailedAttempts()).isEqualTo(5);
        assertThat(user.getLockedUntil()).isAfter(NOW);
    }

    @Test
    void lockedAccountIsRejectedWith423() {
        User user = activeUser(passwordEncoder.encode(PASSWORD));
        user.setLockedUntil(NOW.plusSeconds(60));
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login("a@b.com", PASSWORD, null))
                .isInstanceOf(AccountLockedException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void successfulLoginResetsCountersAndIssuesTokens() {
        User user = activeUser(passwordEncoder.encode(PASSWORD));
        user.setFailedAttempts(3);
        when(userRepository.findByEmail("a@b.com")).thenReturn(Optional.of(user));
        when(userRoleRepository.findByUserId(user.getId()))
                .thenReturn(List.of(new com.eduaircontrol.mssecurity.domain.model.UserRole(
                        user.getId(), UUID.randomUUID(), NOW)));

        AuthResult result = authService.login("a@b.com", PASSWORD, null);

        assertThat(user.getFailedAttempts()).isZero();
        assertThat(user.getLockedUntil()).isNull();
        assertThat(result.accessToken()).isNotBlank();
    }

    @Test
    void refreshRotatesTokenAndRevokesOldOne() {
        User user = activeUser(passwordEncoder.encode(PASSWORD));
        String raw = "raw-refresh-token";
        RefreshToken stored = new RefreshToken();
        stored.setUserId(user.getId());
        stored.setTokenHash(AuthService.sha256(raw));
        stored.setExpiresAt(NOW.plusSeconds(3600));
        when(refreshTokenRepository.findByTokenHash(AuthService.sha256(raw)))
                .thenReturn(Optional.of(stored));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRoleRepository.findByUserId(user.getId())).thenReturn(List.of());

        AuthResult result = authService.refresh(raw, "junit");

        assertThat(stored.getRevokedAt()).isEqualTo(NOW);
        assertThat(result.refreshToken()).isNotEqualTo(raw);
    }

    @Test
    void refreshRejectsRevokedToken() {
        RefreshToken stored = new RefreshToken();
        stored.setUserId(UUID.randomUUID());
        stored.setTokenHash(AuthService.sha256("raw"));
        stored.setExpiresAt(NOW.plusSeconds(3600));
        stored.setRevokedAt(NOW.minusSeconds(10));
        when(refreshTokenRepository.findByTokenHash(AuthService.sha256("raw")))
                .thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> authService.refresh("raw", null))
                .isInstanceOf(UnauthorizedException.class)
                .hasFieldOrPropertyWithValue("code", "REFRESH_TOKEN_INVALID");
    }

    @Test
    void logoutWithAllDevicesRevokesEveryActiveToken() {
        UUID userId = UUID.randomUUID();
        RefreshToken active = new RefreshToken();
        active.setUserId(userId);
        active.setTokenHash("h1");
        active.setExpiresAt(NOW.plusSeconds(3600));
        when(refreshTokenRepository.findActiveByUserId(userId)).thenReturn(List.of(active));

        authService.logout(userId, null, true, "jti-1", NOW.plusSeconds(3600));

        assertThat(active.getRevokedAt()).isEqualTo(NOW);
        verify(refreshTokenRepository).save(active);
        verify(tokenBlacklist).blacklist("jti-1", NOW.plusSeconds(3600));
    }

    @Test
    void registerRejectsUnknownInstitution() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);

        assertThatThrownBy(() -> authService.register("a@b.com", PASSWORD, "alice", "UNKNOWN-1", null, null))
                .isInstanceOf(ValidationException.class);
        verify(userRepository, never()).save(any());
    }


    private User activeUser(String passwordHash) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("a@b.com");
        user.setUsername("alice");
        user.setInstitutionId(INSTITUTION_ID);
        user.setPasswordHash(passwordHash);
        user.setCreatedAt(NOW);
        user.setUpdatedAt(NOW);
        return user;
    }
}
