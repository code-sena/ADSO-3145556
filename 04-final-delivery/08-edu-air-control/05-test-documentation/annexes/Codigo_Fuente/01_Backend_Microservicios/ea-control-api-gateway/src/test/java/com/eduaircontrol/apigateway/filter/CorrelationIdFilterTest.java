package com.eduaircontrol.apigateway.filter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

class CorrelationIdFilterTest {

    private final CorrelationIdFilter filter = new CorrelationIdFilter();

    @Test
    void generatesCorrelationIdWhenAbsent() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/sensors").build());
        AtomicReference<ServerWebExchange> forwarded = new AtomicReference<>();

        filter.filter(exchange, mutated -> {
            forwarded.set(mutated);
            return Mono.empty();
        }).block();

        String generated = forwarded.get().getRequest().getHeaders().getFirst(CorrelationIdFilter.HEADER);
        assertThat(generated).isNotBlank();
        assertThat(exchange.getResponse().getHeaders().getFirst(CorrelationIdFilter.HEADER)).isEqualTo(generated);
    }

    @Test
    void preservesExistingCorrelationId() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/v1/sensors")
                        .header(CorrelationIdFilter.HEADER, "corr-123")
                        .build());
        AtomicReference<ServerWebExchange> forwarded = new AtomicReference<>();

        filter.filter(exchange, mutated -> {
            forwarded.set(mutated);
            return Mono.empty();
        }).block();

        assertThat(forwarded.get().getRequest().getHeaders().getFirst(CorrelationIdFilter.HEADER))
                .isEqualTo("corr-123");
        assertThat(exchange.getResponse().getHeaders().getFirst(CorrelationIdFilter.HEADER))
                .isEqualTo("corr-123");
    }
}
