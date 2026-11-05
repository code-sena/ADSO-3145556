package com.eduaircontrol.msusermanagement.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Validación de access tokens RS256 (ADR-006).
 *
 * <p>Este servicio <b>no emite</b> tokens: los emite ms-security y los valida el
 * api-gateway. Aquí solo se comprueba la firma contra la clave pública publicada
 * en su JWKS, de modo que un token real interopera entre servicios (antes cada uno
 * validaba HS256 con un secreto compartido y ningún token de ms-security era
 * válido aquí).
 *
 * <p>Los roles viajan como lista en el claim {@code roles}. Se conserva también la
 * lectura de {@code role} (string) por si llegara un token del monolito.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwksKeyResolver keyResolver;

    /**
     * @throws JwtException si el token está vencido, mal firmado o malformado
     */
    public AuthenticatedUser authenticate(String token) {
        Claims claims = parse(token);

        return new AuthenticatedUser(
                optionalUserId(claims).orElse(null),
                claims.get("email", String.class),
                claims.get("username", String.class),
                rolesOf(claims),
                parseUuid(claimAsString(claims, "institutionId")),
                parseUuid(claimAsString(claims, "campusId")));
    }

    /**
     * Claims de un token válido. Se expone porque hay componentes
     * ({@code UserIdentityAdapter}) que resuelven la identidad a partir del token
     * guardado como credencial de la autenticación.
     *
     * @throws JwtException si el token está vencido, mal firmado o malformado
     */
    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyResolver.resolve(kidOf(token)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Id de usuario de un token válido.
     *
     * <p>Mismo criterio que el api-gateway: manda el claim {@code userId} si existe
     * y, si no, el {@code sub}. ms-security pone el id en {@code sub}; los tokens de
     * prueba y los del monolito lo llevan en {@code userId}.
     */
    public java.util.Optional<UUID> optionalUserId(Claims claims) {
        UUID fromClaim = parseUuid(claimAsString(claims, "userId"));
        return fromClaim != null
                ? java.util.Optional.of(fromClaim)
                : java.util.Optional.ofNullable(parseUuid(claims.getSubject()));
    }

    /** {@code kid} del header JWT; si no hay header legible, null y se rechaza abajo. */
    private String kidOf(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new JwtException("Token malformado");
        }
        try {
            String header = new String(
                    java.util.Base64.getUrlDecoder().decode(parts[0]),
                    java.nio.charset.StandardCharsets.UTF_8);
            int at = header.indexOf("\"kid\"");
            if (at < 0) {
                return null;
            }
            int start = header.indexOf('"', header.indexOf(':', at) + 1);
            int end = header.indexOf('"', start + 1);
            return start >= 0 && end > start ? header.substring(start + 1, end) : null;
        } catch (RuntimeException e) {
            throw new JwtException("Header del token ilegible", e);
        }
    }

    /**
     * Roles del token. La forma canónica es {@code roles} (lista, la que emite
     * ms-security); se acepta {@code role} (string) como residuo del monolito.
     */
    static List<String> rolesOf(Claims claims) {
        Object roles = claims.get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        String single = claims.get("role", String.class);
        return single == null || single.isBlank() ? List.of() : List.of(single);
    }

    private static String claimAsString(Claims claims, String name) {
        Object value = claims.get(name);
        return value == null ? null : String.valueOf(value);
    }

    private static UUID parseUuid(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Identidad extraída de un token válido. */
    public record AuthenticatedUser(
            UUID userId,
            String email,
            String username,
            List<String> roles,
            UUID institutionId,
            UUID campusId) {
    }
}
