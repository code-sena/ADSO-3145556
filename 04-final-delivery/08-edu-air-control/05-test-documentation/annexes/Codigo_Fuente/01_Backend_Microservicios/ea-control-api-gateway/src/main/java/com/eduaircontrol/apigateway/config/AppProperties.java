package com.eduaircontrol.apigateway.config;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuración del gateway. Las rutas viven en {@code application.yml}
 * (spring.cloud.gateway.routes); aquí se centralizan los parámetros de
 * seguridad, rate limiting y el mapa de upstreams para el health check.
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, RateLimit rateLimit, Map<String, String> upstreams) {

    public record Jwt(String jwksUri) {
    }

    public record RateLimit(int maxRequests, int windowSeconds, List<String> exemptPaths) {
    }
}
