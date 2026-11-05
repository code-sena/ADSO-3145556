package com.eduaircontrol.mssensor.infrastructure.messaging;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Publica un evento del outbox y lo marca como enviado.
 *
 * <p>Es una unidad transaccional propia (y por eso vive en su propia clase, no
 * dentro del relay): el relay recorre el lote y delega cada publicación, de modo
 * que un fallo individual no arrastra al resto.
 *
 * <p>La garantía que ofrece es <b>at-least-once</b>: si el broker cae entre
 * publicar y marcar, el evento se reintentará y llegará dos veces. Por eso el
 * consumidor de ms-environment-monitoring deduplica por el {@code eventId} del
 * cuerpo. El {@code message_id} del broker queda como respaldo. La alternativa
 * (confirmar antes de publicar) dejaría huecos silenciosos, que es peor.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private static final int MAX_ERROR_LENGTH = 1000;

    private final OutboxRepository repository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.outbox.exchange:eduaircontrol.sensor}")
    private String exchange;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publish(UUID eventId) {
        OutboxEvent event = repository.findById(eventId).orElse(null);
        if (event == null || !event.isPending()) {
            // Ya publicado por otra pasada concurrente del relay.
            return;
        }

        event.setAttempts(event.getAttempts() + 1);
        try {
            rabbitTemplate.convertAndSend(exchange, event.getRoutingKey(), event.getPayload(),
                    message -> {
                        // El messageId permite al consumidor deduplicar sin parsear el cuerpo.
                        message.getMessageProperties().setMessageId(event.getId().toString());
                        message.getMessageProperties().setHeader("eventType", event.getEventType());
                        message.getMessageProperties().setContentType("application/json");
                        return message;
                    });
            event.setPublishedAt(java.time.Instant.now());
            event.setLastError(null);
        } catch (RuntimeException e) {
            log.warn("No se pudo publicar el evento {} (intento {}): {}",
                    eventId, event.getAttempts(), e.getMessage());
            event.setLastError(truncate(e.getMessage()));
        }
        repository.save(event);
    }

    private String truncate(String message) {
        if (message == null) {
            return "error desconocido";
        }
        return message.length() <= MAX_ERROR_LENGTH
                ? message
                : message.substring(0, MAX_ERROR_LENGTH);
    }
}
