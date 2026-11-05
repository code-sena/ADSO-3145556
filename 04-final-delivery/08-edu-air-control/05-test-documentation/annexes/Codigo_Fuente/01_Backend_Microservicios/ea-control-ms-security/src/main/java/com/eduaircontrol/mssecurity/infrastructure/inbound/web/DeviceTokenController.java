package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Emision de credenciales para dispositivos IoT (ESP32).
 *
 * <p>Un dispositivo no puede hacer login ni refrescar su token: recibe una
 * credencial de larga duracion al ser provisionado y la guarda. Este endpoint la
 * emite, firmada con la misma clave que el resto de access tokens, de modo que
 * todos los servicios la validan contra el JWKS sin codigo nuevo.
 *
 * <p>Requiere rol ADMIN: dar de alta un dispositivo es una operacion de
 * mantenimiento, no de un usuario normal.
 */
@RestController
@RequestMapping("/api/v1/device-tokens")
@RequiredArgsConstructor
public class DeviceTokenController {

    private final com.eduaircontrol.mssecurity.infrastructure.security.JwtService jwtService;

    /** Duracion por defecto: un dispositivo no refresca su token. */
    @Value("${jwt.device-token-ttl-seconds:31536000}")
    private long defaultTtlSeconds;

    @PostMapping
    public ResponseEntity<DeviceTokenResponse> issue(@Valid @RequestBody DeviceTokenRequest request) {
        UUID subject = request.subject();
        long ttl = request.ttlSeconds() != null && request.ttlSeconds() > 0
                ? Math.min(request.ttlSeconds(), defaultTtlSeconds)
                : defaultTtlSeconds;

        String token = jwtService.generateDeviceToken(subject, ttl);
        Date expiresAt = new Date(System.currentTimeMillis() + ttl * 1000L);
        return ResponseEntity.ok(new DeviceTokenResponse(
                token,
                "Bearer",
                subject,
                com.eduaircontrol.mssecurity.infrastructure.security.JwtService.DEVICE_ROLE,
                expiresAt.toInstant()));
    }

    /** {@code subject} es la identidad del dispositivo: el {@code sensorId}. */
    public record DeviceTokenRequest(
            @NotNull(message = "subject is required") UUID subject,
            Long ttlSeconds) {
    }

    public record DeviceTokenResponse(
            String accessToken,
            String tokenType,
            UUID subject,
            String role,
            java.time.Instant expiresAt) {
    }
}
