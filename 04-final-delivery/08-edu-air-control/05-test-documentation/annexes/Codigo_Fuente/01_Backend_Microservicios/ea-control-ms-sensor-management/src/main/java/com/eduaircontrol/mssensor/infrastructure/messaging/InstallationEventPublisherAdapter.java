package com.eduaircontrol.mssensor.infrastructure.messaging;

import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import com.eduaircontrol.mssensor.domain.port.out.InstallationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Publica el ciclo de vida de instalaciones en el outbox transaccional.
 *
 * <p>Solo traduce del modelo de dominio al contrato del evento y lo encola: la
 * publicacion real la hace {@link OutboxRelay} fuera de la transaccion de negocio.
 */
@Component
@RequiredArgsConstructor
public class InstallationEventPublisherAdapter implements InstallationEventPublisher {

    private final OutboxWriter outboxWriter;

    @Override
    public void installed(SensorInstallation installation) {
        enqueue(SensorInstallationEvent.installed(
                installation.getId(),
                installation.getSensorId(),
                installation.getEducationalEnvironmentId(),
                installation.getInstalledAt()));
    }

    @Override
    public void removed(SensorInstallation installation) {
        enqueue(SensorInstallationEvent.removed(
                installation.getId(),
                installation.getSensorId(),
                installation.getEducationalEnvironmentId(),
                installation.getInstalledAt(),
                installation.getRemovedAt()));
    }

    private void enqueue(SensorInstallationEvent event) {
        outboxWriter.append(
                event.eventType(),
                event.aggregateType(),
                event.aggregateId().toString(),
                event.routingKey(),
                event);
    }
}
