package com.eduaircontrol.mssensor.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Vacia el outbox hacia RabbitMQ.
 *
 * <p>Lee solo pendientes, en orden de antigüedad, y delega cada publicación. Un
 * evento que falla no bloquea a los siguientes: se acumula {@code attempts} y se
 * reintenta en la siguiente pasada.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelay {

    private final OutboxRepository repository;
    private final OutboxPublisher publisher;

    @Value("${app.outbox.batch-size:100}")
    private int batchSize;

    @Value("${app.outbox.stale-minutes:5}")
    private long staleMinutes;

    @Scheduled(fixedDelayString = "${app.outbox.relay-interval-ms:1000}")
    @Transactional(readOnly = true)
    public void publishPending() {
        var pending = repository.findPending(
                        org.springframework.data.domain.PageRequest.of(0, batchSize))
                .stream()
                .map(OutboxEvent::getId)
                .toList();

        if (pending.isEmpty()) {
            return;
        }
        log.debug("Outbox: {} evento(s) pendiente(s) por publicar", pending.size());
        pending.forEach(publisher::publish);
    }

    /**
     * Deuda de eventos que no se han podido publicar. Sirve para alertar: un
     * numero alto sostenido significa que el broker está caído o que los intentos
     * están fallando.
     */
    @Scheduled(fixedDelayString = "${app.outbox.health-interval-ms:60000}")
    public void checkStaleEvents() {
        long stale = repository.countByPublishedAtIsNullAndOccurredAtBefore(
                java.time.Instant.now().minusSeconds(staleMinutes * 60));
        if (stale > 0) {
            log.warn("Outbox: {} evento(s) sin publicar hace más de {} minuto(s)",
                    stale, staleMinutes);
        }
    }
}
