package com.eduaircontrol.userexperience.shared.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Decodificador RS256 via JWKS de ms-security (ADR-006).
 *
 * <p>Solo se crea si {@code jwt.jwks-uri} esta configurado. En local y en tests,
 * donde no hay ms-security, el servicio sigue validando HS256 con el secreto
 * compartido.
 */
@Slf4j
@Configuration
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder rs256JwtDecoder(
            @Value("${jwt.jwks-uri:}") String jwksUri) {
        if (jwksUri == null || jwksUri.isBlank()) {
            log.debug("Sin jwt.jwks-uri: validacion RS256 desactivada, solo HS256");
            return null;
        }
        log.info("Validacion RS256 activada con JWKS: {}", jwksUri);
        return NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
    }
}
