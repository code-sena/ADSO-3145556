package com.eduaircontrol.apigateway.filter;

import java.util.UUID;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Garantiza que toda petición tenga un X-Correlation-Id. Si el cliente no lo
 * envía se genera uno; siempre se reenvía al upstream y se devuelve en la
 * respuesta para poder correlacionar logs (ADR-005).
 */
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    public static final String HEADER = "X-Correlation-Id";
    public static final String ATTRIBUTE = "correlationId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        exchange.getAttributes().put(ATTRIBUTE, correlationId);
        exchange.getResponse().getHeaders().set(HEADER, correlationId);
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(HEADER, correlationId)
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
