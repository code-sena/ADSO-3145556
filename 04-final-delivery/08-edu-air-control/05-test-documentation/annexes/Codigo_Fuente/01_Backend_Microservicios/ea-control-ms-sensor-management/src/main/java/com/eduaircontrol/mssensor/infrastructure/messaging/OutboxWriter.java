package com.eduaircontrol.mssensor.infrastructure.messaging;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    private final JsonMapper jsonMapper;

    /**
     * @param payload objeto de dominio a serializar como JSON del evento
     * @return id del evento encolado, útil para trazas
     */
    @Transactional
    public UUID append(String eventType, String aggregateType, String aggregateId,
                       String routingKey, Object payload) {
        OutboxEvent event = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .eventType(eventType)
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .routingKey(routingKey)
                .payload(serialize(payload))
                .occurredAt(Instant.now())
                .attempts(0)
                .build();
        repository.save(event);
        return event.getId();
    }

    private String serialize(Object payload) {
        // Si el payload no se puede serializar, el evento no sirve de nada: conviene
        // fallar la transacción de negocio y no encolar basura a la espera.
        try {
            return jsonMapper.writeValueAsString(payload);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("No se pudo serializar el payload: " + e.getMessage(), e);
        }
    }
}
