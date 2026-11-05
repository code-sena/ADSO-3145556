package com.eduaircontrol.msclassroom.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

/**
 * Encola un evento para su publicación posterior.
 *
 * <p>Se invoca desde el servicio de dominio, dentro de la transacción ya abierta:
 * el append solo añade una fila, no publica nada. Si la transacción de negocio
 * falla, la fila tampoco se guarda.
 */
@Component
@RequiredArgsConstructor
public class OutboxWriter {

    private final OutboxRepository repository;
    private final tools.jackson.databind.json.JsonMapper jsonMapper;

    /**
     * @param payload objeto de dominio a serializar como JSON del evento
     * @return id del evento encolado, útil para trazas
     */
    public UUID append(String eventType, String aggregateType, String aggregateId,
                       String routingKey, Object payload) {
        return append(eventType, aggregateType, aggregateId, routingKey, payload, Instant.now());
    }

    public UUID append(String eventType, String aggregateType, String aggregateId,
                       String routingKey, Object payload, Instant occurredAt) {
        OutboxEvent event = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .eventType(eventType)
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .routingKey(routingKey)
                .payload(serialize(payload))
                .occurredAt(occurredAt)
                .attempts(0)
                .build();
        repository.save(event);
        return event.getId();
    }

    private String serialize(Object payload) {
        // Si el payload no se puede serializar, el evento no sirve de nada: conviene
        // fallar la transacción de negocio y no encolar basura a la espera.
        try {
            return com.fasterxml.jackson.databind.json.JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build()
                    .writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("No se pudo serializar el payload: " + e.getMessage(), e);
        }
    }
}
