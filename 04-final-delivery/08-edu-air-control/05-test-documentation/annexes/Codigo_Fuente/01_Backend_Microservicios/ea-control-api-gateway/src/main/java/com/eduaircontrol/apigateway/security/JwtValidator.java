package com.eduaircontrol.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import reactor.core.publisher.Mono;

/**
 * Valida access tokens RS256 contra la clave pública publicada en JWKS y
 * extrae la identidad para propagarla como headers internos (ADR-006).
 */
public class JwtValidator {

    private static final Pattern KID = Pattern.compile("\"kid\"\\s*:\\s*\"([^\"]+)\"");

    private final JwksProvider jwksProvider;

    public JwtValidator(JwksProvider jwksProvider) {
        this.jwksProvider = jwksProvider;
    }

    public Mono<JwtPrincipal> validate(String token) {
        String kid = readKid(token);
        return jwksProvider.key(kid).map(publicKey -> {
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(publicKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                String subject = claims.getSubject();
                String userId = claimAsString(claims, "userId");
                String email = claimAsString(claims, "email");
                List<String> roles = rolesOf(claims);
                return new JwtPrincipal(
                        userId != null ? userId : subject,
                        email != null ? email : subject,
                        roles.isEmpty() ? null : roles.get(0),
                        roles,
                        claims.getId(),
                        claims.getExpiration() == null ? null : claims.getExpiration().toInstant(),
                        claimAsString(claims, "institutionId"),
                        claimAsString(claims, "campusId"));
            } catch (JwtException | IllegalArgumentException e) {
                throw new UnauthorizedException("INVALID_TOKEN", "Invalid or expired token");
            }
        });
    }

    static String readKid(String token) {
        try {
            String header = new String(Base64.getUrlDecoder().decode(token.split("\\.")[0]),
                    StandardCharsets.UTF_8);
            Matcher matcher = KID.matcher(header);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (RuntimeException ignored) {
            // header malformado — se rechaza más abajo al no encontrar la clave
        }
        throw new UnauthorizedException("INVALID_TOKEN", "Malformed token header");
    }

    static List<String> rolesOf(Claims claims) {
        Object roles = claims.get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    static String claimAsString(Claims claims, String name) {
        Object value = claims.get(name);
        return value == null ? null : String.valueOf(value);
    }
}
