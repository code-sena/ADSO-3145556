package com.eduaircontrol.apigateway.filter;

import com.eduaircontrol.apigateway.config.AppProperties;
import com.eduaircontrol.apigateway.ratelimit.RedisStateStore;
import com.eduaircontrol.apigateway.security.JwtPrincipal;
import com.eduaircontrol.apigateway.support.ErrorWriter;
import java.net.InetSocketAddress;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Rate limiting global con contadores en Redis (ventana fija). Limita por
 * IP en endpoints públicos y por user-id en endpoints autenticados
 * (decisions.md del gateway). Si Redis no está disponible, deja pasar la
 * petición (fail-open) para no tumbar el tráfico.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitFilter implements GlobalFilter, Ordered {

    private final RedisStateStore stateStore;
    private final AppProperties properties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (isExempt(path)) {
            return chain.filter(exchange);
        }
        Duration window = Duration.ofSeconds(properties.rateLimit().windowSeconds());
        String key = "rate:" + subject(exchange) + ":" + path;
        return stateStore.increment(key, window)
                .flatMap(count -> count > properties.rateLimit().maxRequests()
                        ? tooMany(exchange, window)
                        : chain.filter(exchange))
                .onErrorResume(error -> {
                    log.warn("Rate limit no disponible ({}); se permite la petición", error.getMessage());
                    return chain.filter(exchange);
                });
    }

    private boolean isExempt(String path) {
        return properties.rateLimit().exemptPaths().contains(path);
    }

    private String subject(ServerWebExchange exchange) {
        JwtPrincipal principal = exchange.getAttribute(JwtAuthenticationFilter.PRINCIPAL_ATTRIBUTE);
        if (principal != null && principal.userId() != null && !principal.userId().isBlank()) {
            return "user:" + principal.userId();
        }
        return "ip:" + clientIp(exchange);
    }

    private String clientIp(ServerWebExchange exchange) {
        String forwarded = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        InetSocketAddress remote = exchange.getRequest().getRemoteAddress();
        return remote == null || remote.getAddress() == null
                ? "unknown"
                : remote.getAddress().getHostAddress();
    }

    private Mono<Void> tooMany(ServerWebExchange exchange, Duration window) {
        exchange.getResponse().getHeaders().set("Retry-After", String.valueOf(window.toSeconds()));
        return ErrorWriter.write(exchange.getResponse(), HttpStatus.TOO_MANY_REQUESTS,
                "RATE_LIMIT_EXCEEDED", "Too many requests, retry later");
    }

    @Override
    public int getOrder() {
        return -80;
    }
}
