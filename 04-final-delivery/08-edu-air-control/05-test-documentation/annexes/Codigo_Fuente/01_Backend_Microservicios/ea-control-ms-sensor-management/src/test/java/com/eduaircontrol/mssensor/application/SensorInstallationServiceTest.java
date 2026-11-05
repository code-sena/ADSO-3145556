package com.eduaircontrol.mssensor.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eduaircontrol.mssensor.domain.port.out.InstallationEventPublisher;
import com.eduaircontrol.mssensor.domain.port.out.SensorInstallationRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import com.eduaircontrol.mssensor.shared.exception.ValidationException;
import com.eduaircontrol.mssensor.domain.model.Sensor;
import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SensorInstallationServiceTest {

    private SensorInstallationRepository installationRepository;
    private SensorRepository sensorRepository;
    private InstallationEventPublisher eventPublisher;
    private SensorInstallationService installationService;

    @BeforeEach
    void setUp() {
        installationRepository = mock(SensorInstallationRepository.class);
        sensorRepository = mock(SensorRepository.class);
        eventPublisher = mock(InstallationEventPublisher.class);
        installationService = new SensorInstallationService(
                installationRepository, sensorRepository, eventPublisher);
    }

    @Test
    void createRejectsWhenSensorDoesNotExist() {
        UUID sensorId = UUID.randomUUID();
        when(sensorRepository.findById(sensorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> installationService.create(
                sensorId, UUID.randomUUID(), Instant.now()))
                .isInstanceOf(NotFoundException.class);

        verify(installationRepository, never()).save(any());
    }

    @Test
    void createRejectsOverlappingInstallation() {
        UUID sensorId = UUID.randomUUID();
        when(sensorRepository.findById(sensorId)).thenReturn(
                Optional.of(Sensor.builder().id(sensorId).serialNumber("SN-1")
                        .sensorModelId(UUID.randomUUID()).sensorStatusId(UUID.randomUUID()).build()));
        when(installationRepository.existsOverlapForSensor(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> installationService.create(
                sensorId, UUID.randomUUID(), Instant.now()))
                .isInstanceOf(ConflictException.class);

        verify(installationRepository, never()).save(any());
    }

    @Test
    void createPersistsOpenInstallation() {
        UUID sensorId = UUID.randomUUID();
        Instant installedAt = Instant.now().minus(1, ChronoUnit.DAYS);
        when(sensorRepository.findById(sensorId)).thenReturn(
                Optional.of(Sensor.builder().id(sensorId).serialNumber("SN-1")
                        .sensorModelId(UUID.randomUUID()).sensorStatusId(UUID.randomUUID()).build()));
        when(installationRepository.existsOverlapForSensor(sensorId, installedAt)).thenReturn(false);
        when(installationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SensorInstallation created = installationService.create(
                sensorId, UUID.randomUUID(), installedAt);

        assertThat(created.getInstalledAt()).isEqualTo(installedAt);
        assertThat(created.isActive()).isTrue();
    }

    @Test
    void closeRejectsAlreadyClosedInstallation() {
        UUID id = UUID.randomUUID();
        SensorInstallation closed = SensorInstallation.builder()
                .id(id)
                .sensorId(UUID.randomUUID())
                .educationalEnvironmentId(UUID.randomUUID())
                .installedAt(Instant.now().minus(2, ChronoUnit.DAYS))
                .removedAt(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();
        when(installationRepository.findById(id)).thenReturn(Optional.of(closed));

        assertThatThrownBy(() -> installationService.close(id))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void closeRejectsInstallationWithFutureInstalledAt() {
        UUID id = UUID.randomUUID();
        SensorInstallation future = SensorInstallation.builder()
                .id(id)
                .sensorId(UUID.randomUUID())
                .educationalEnvironmentId(UUID.randomUUID())
                .installedAt(Instant.now().plus(2, ChronoUnit.DAYS))
                .build();
        when(installationRepository.findById(id)).thenReturn(Optional.of(future));

        assertThatThrownBy(() -> installationService.close(id))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void closeSetsRemovedAt() {
        UUID id = UUID.randomUUID();
        SensorInstallation installation = SensorInstallation.builder()
                .id(id)
                .sensorId(UUID.randomUUID())
                .educationalEnvironmentId(UUID.randomUUID())
                .installedAt(Instant.now().minus(2, ChronoUnit.DAYS))
                .build();
        when(installationRepository.findById(id)).thenReturn(Optional.of(installation));
        when(installationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SensorInstallation closed = installationService.close(id);

        assertThat(closed.getRemovedAt()).isNotNull();
        assertThat(closed.isActive()).isFalse();
    }

    @Test
    void createPublishesInstalledEvent() {
        UUID sensorId = UUID.randomUUID();
        UUID environmentId = UUID.randomUUID();
        when(sensorRepository.findById(sensorId)).thenReturn(Optional.of(sensor()));
        when(installationRepository.existsOverlapForSensor(any(), any())).thenReturn(false);
        when(installationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        installationService.create(sensorId, environmentId, Instant.now());

        // ms-environment-monitoring mantiene con este evento su installation_projection,
        // que es la que permite atribuir las mediciones a un ambiente.
        verify(eventPublisher).installed(any(SensorInstallation.class));
    }

    @Test
    void closePublishesRemovedEvent() {
        UUID id = UUID.randomUUID();
        SensorInstallation installation = SensorInstallation.builder()
                .id(id)
                .sensorId(UUID.randomUUID())
                .educationalEnvironmentId(UUID.randomUUID())
                .installedAt(Instant.now().minus(2, ChronoUnit.DAYS))
                .build();
        when(installationRepository.findById(id)).thenReturn(Optional.of(installation));
        when(installationRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        installationService.close(id);

        verify(eventPublisher).removed(any(SensorInstallation.class));
    }

    @Test
    void rejectedCreatePublishesNothing() {
        when(sensorRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> installationService.create(
                UUID.randomUUID(), UUID.randomUUID(), Instant.now()))
                .isInstanceOf(NotFoundException.class);

        // El evento se escribe dentro de la transaccion de negocio: si la operacion
        // falla, no debe quedar en el outbox un evento de algo que no ocurrio.
        verify(eventPublisher, never()).installed(any());
    }

    private Sensor sensor() {
        return Sensor.builder().id(UUID.randomUUID()).build();
    }
}
