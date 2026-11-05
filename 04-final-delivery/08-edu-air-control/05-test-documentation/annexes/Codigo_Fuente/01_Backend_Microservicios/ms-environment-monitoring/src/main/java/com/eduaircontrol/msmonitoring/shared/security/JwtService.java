package com.eduaircontrol.msmonitoring.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Lectura del JWT compartido con el monolito.
 *
 * <p>El servicio <b>no firma</b> tokens: solo valida el que trae la peticion. El
 * emisor sigue siendo el monolito y la clave es la misma, de modo que el gateway
 * y este servicio ven exactamente la misma identidad.
 *
 * <p>El claim {@code userId} es opcional a proposito: los tokens emitidos antes de
 * que existiera no lo llevan. Un token sin el no es invalido, simplemente deja
 * {@code requested_by} en null.
 */
@Slf4j
@Service
public class JwtService {

    private static final String CLAIM_USER_ID = "userId";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    private Key signingKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /** Correo del titular (claim {@code sub}). */
    public String extractEmail(String token) {
        return claims(token).getSubject();
    }

    public String extractRole(String token) {
        return claims(token).get("role", String.class);
    }

    public Optional<UUID> extractUserId(String token) {
        return optionalUserId(claims(token));
    }

    public Optional<UUID> optionalUserId(Claims claims) {
        if (claims == null) {
            return Optional.empty();
        }
        Object raw = claims.get(CLAIM_USER_ID);
        if (raw == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(raw.toString()));
        } catch (IllegalArgumentException e) {
            log.warn("El claim userId del token no es un UUID: {}", raw);
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            claims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Claims completos del token, o excepcion si es invalido. */
    public Claims parse(String token) {
        return claims(token);
    }

    private Claims claims(String token) {
        // jjwt 0.11.5: el conversor a Claims sigue siendo parseClaimsJws/getBody.
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}