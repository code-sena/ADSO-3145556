package com.eduaircontrol.apigateway.support;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * Escribe respuestas de error JSON sin depender de la versión de Jackson.
 */
public final class ErrorWriter {

    private ErrorWriter() {
    }

    public static Mono<Void> write(ServerHttpResponse response, HttpStatus status, String code, String message) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"error\":\"" + escape(code) + "\",\"message\":\"" + escape(message)
                + "\",\"timestamp\":\"" + Instant.now() + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
