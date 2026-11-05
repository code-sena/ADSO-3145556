package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import com.eduaircontrol.mssecurity.application.AuthResult;
import com.eduaircontrol.mssecurity.application.AuthService;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.AuthResponse;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.LoginRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.LogoutRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.RegisterRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.RefreshRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.ServiceHealthResponse;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.UserSummary;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.sql.Connection;
import java.util.UUID;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints del contrato auth-service.yaml bajo /api/v1/auth:
 * register, login, refresh, logout y health.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final DataSource dataSource;
    private final StringRedisTemplate redis;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        AuthResult result = authService.register(
                request.email(), request.password(), request.username(), request.companyCode(),
                request.campusId(), userAgent(httpRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(result));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        return toResponse(authService.login(request.email(), request.password(), userAgent(httpRequest)));
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request, HttpServletRequest httpRequest) {
        return toResponse(authService.refresh(request.refreshToken(), userAgent(httpRequest)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody(required = false) LogoutRequest request,
            Authentication authentication, HttpServletRequest httpRequest) {
        UUID userId = UUID.fromString(authentication.getName());
        String refreshToken = request == null ? null : request.refreshToken();
        boolean allDevices = request != null && Boolean.TRUE.equals(request.allDevices());
        String jti = (String) httpRequest.getAttribute("jwtJti");
        java.time.Instant expiresAt = (java.time.Instant) httpRequest.getAttribute("jwtExp");
        authService.logout(userId, refreshToken, allDevices, jti, expiresAt);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<ServiceHealthResponse> health() {
        String redisStatus = redisStatus();
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return ResponseEntity.ok(ServiceHealthResponse.healthy(redisStatus));
            }
        } catch (Exception e) {
            // database down — handled below
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ServiceHealthResponse.databaseDown(redisStatus));
    }

    private String redisStatus() {
        try {
            redis.execute((RedisCallback<Void>) connection -> {
                connection.ping();
                return null;
            });
            return "connected";
        } catch (Exception e) {
            return "disconnected";
        }
    }

    private String userAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    private AuthResponse toResponse(AuthResult result) {
        return new AuthResponse(
                result.accessToken(),
                result.refreshToken(),
                result.expiresIn(),
                new UserSummary(result.userId(), result.email(), result.username(),
                        result.roles(), java.util.List.of(), result.institutionId(), result.campusId()));
    }
}
