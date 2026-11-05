package com.eduaircontrol.msmonitoring.analysis.infrastructure.messaging;

import com.eduaircontrol.msmonitoring.analysis.domain.port.out.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publica los eventos de dominio en el bus interno de Spring.
 *
 * <p>Hoy no hay consumidor para {@code EnvironmentalDataAnalyzed}: el analisis se
 * consulta por HTTP y nada reacciona a el. El publicador se queda de todos modos
 * porque el agregado no debe saber si su evento se consume.
 *
 * <p>Cuando exista un consumidor (por ejemplo ms-alert), esta clase pasa a escribir
 * en una tabla outbox dentro de la misma transaccion y un relay la envia a RabbitMQ,
 * igual que hace el monolito. Los casos de uso no cambian.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Object event) {
        log.debug("Publishing domain event {}", event.getClass().getSimpleName());
        applicationEventPublisher.publishEvent(event);
    }
}