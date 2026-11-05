package com.eduaircontrol.apigateway.security;

import java.util.List;
import org.springframework.http.HttpMethod;

/**
 * Endpoints que no requieren JWT. El resto del tráfico pasa por el filtro de
 * autenticación del gateway. Incluye el flujo de login del BFF (OAuth2/OIDC).
 */
public final class PublicPaths {

    private static final List<String> EXACT = List.of(
            "/health",
            "/api/v1/auth/health",
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/auth/forgot-password",
            "/api/v1/auth/resend-code",
            "/api/v1/auth/verify-code",
            "/api/v1/auth/reset-password",
            "/api/v1/auth/jwks");

    private PublicPaths() {
    }

    public static boolean isPublic(HttpMethod method, String path) {
        if (HttpMethod.OPTIONS.equals(method)) {
            return true;
        }
        if (path.startsWith("/oauth2/") || path.startsWith("/login")) {
            return true;
        }
        if (path.startsWith("/api/v1/auth/oauth2/")) {
            return true;
        }
        return EXACT.contains(path);
    }
}
