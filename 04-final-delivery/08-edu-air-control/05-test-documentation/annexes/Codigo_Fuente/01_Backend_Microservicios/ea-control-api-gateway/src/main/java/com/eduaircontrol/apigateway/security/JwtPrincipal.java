package com.eduaircontrol.apigateway.security;

import java.time.Instant;
import java.util.List;

/**
 * Identidad extraída de un access token válido. El gateway la propaga a los
 * servicios internos mediante los headers X-User-Id, X-User-Role,
 * X-Institution-Id y X-Campus-Id.
 */
public record JwtPrincipal(String userId, String email, String role, List<String> roles, String jti,
        Instant expiresAt, String institutionId, String campusId) {
}
