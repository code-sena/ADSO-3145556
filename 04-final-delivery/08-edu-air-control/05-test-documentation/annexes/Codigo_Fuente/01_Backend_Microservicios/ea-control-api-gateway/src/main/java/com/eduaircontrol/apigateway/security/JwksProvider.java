package com.eduaircontrol.apigateway.security;

import java.security.interfaces.RSAPublicKey;
import reactor.core.publisher.Mono;

/**
 * Fuente de claves públicas RSA para validar access tokens RS256. La
 * implementación de producción consulta el JWKS de ms-security y cachea el
 * resultado (ADR-006).
 */
public interface JwksProvider {

    Mono<RSAPublicKey> key(String kid);
}
