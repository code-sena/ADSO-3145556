package com.eduaircontrol.msmonitoring.shared.security;

import com.eduaircontrol.msmonitoring.shared.contract.UserIdentityPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Identidad del solicitante, resuelta por tres vias:
 *
 * <ol>
 *   <li>Headers del gateway ({@code X-User-Id}) guardados en los detalles.</li>
 *   <li>Token RS256 ya validado por el filtro.</li>
 *   <li>Token HS256 parseado con el secreto compartido.</li>
 * </ol>
 */
@Component
@RequiredArgsConstructor
public class UserIdentityAdapter implements UserIdentityPort {

    private final JwtService jwtService;

    @Override
    public Optional<UUID> currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }

        // 1. Detalles: el filtro guarda ahi el userId cuando viene del gateway o de RS256.
        Object details = authentication.getDetails();
        if (details instanceof UUID uuid) {
            return Optional.of(uuid);
        }
        if (details instanceof String text) {
            try {
                return Optional.of(UUID.fromString(text));
            } catch (IllegalArgumentException e) {
                // No es un UUID, se sigue con las demas vias.
            }
        }

        // 2. Principal Jwt (si algun filtro futuro lo usa).
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return claimUserId(jwt.getClaimAsString("userId"));
        }

        // 3. Token HS256 en las credenciales.
        if (authentication.getCredentials() instanceof String token) {
            try {
                return jwtService.optionalUserId(jwtService.parse(token));
            } catch (RuntimeException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> currentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication.getPrincipal() instanceof Jwt jwt && jwt.getSubject() != null) {
            return Optional.of(jwt.getSubject());
        }
        String name = authentication.getName();
        if (name != null && !name.isBlank() && name.contains("@")) {
            return Optional.of(name);
        }
        if (authentication.getCredentials() instanceof String token) {
            try {
                return Optional.ofNullable(jwtService.parse(token).getSubject())
                        .filter(email -> !email.isBlank());
            } catch (RuntimeException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Optional<UUID> claimUserId(String raw) {
        if (raw == null || raw.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(raw));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
