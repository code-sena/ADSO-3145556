package com.eduaircontrol.mssensor.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eduaircontrol.mssensor.domain.port.out.SensorInstallationRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorModelRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorStatusRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorVariableRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import com.eduaircontrol.mssensor.domain.model.Sensor;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SensorServiceTest {

    private SensorRepository sensorRepository;
    private SensorInstallationRepository installationRepository;
    private SensorVariableRepository sensorVariableRepository;
    private SensorModelRepository sensorModelRepository;
    private SensorStatusRepository sensorStatusRepository;
    private SensorService sensorService;

    @BeforeEach
    void setUp() {
        sensorRepository = mock(SensorRepository.class);
        installationRepository = mock(SensorInstallationRepository.class);
        sensorVariableRepository = mock(SensorVariableRepository.class);
        sensorModelRepository = mock(SensorModelRepository.class);
        sensorStatusRepository = mock(SensorStatusRepository.class);
        when(sensorModelRepository.existsById(any())).thenReturn(true);
        when(sensorStatusRepository.existsById(any())).thenReturn(true);
        sensorService = new SensorService(sensorRepository, installationRepository,
                sensorVariableRepository, sensorModelRepository, sensorStatusRepository);
    }

    private Sensor sensor(UUID id) {
        return Sensor.builder()
                .id(id)
                .serialNumber("SN-001")
                .sensorModelId(UUID.randomUUID())
                .sensorStatusId(UUID.randomUUID())
                .build();
    }

    @Test
    void getReturnsNotFoundForUnknownId() {
        UUID id = UUID.randomUUID();
        when(sensorRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sensorService.get(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createRejectsDuplicatedSerialNumber() {
        when(sensorRepository.existsBySerialNumber("SN-001")).thenReturn(true);

        assertThatThrownBy(() -> sensorService.create("SN-001", UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOf(ConflictException.class);

        verify(sensorRepository, never()).save(any());
    }

    @Test
    void createTrimsSerialNumber() {
        when(sensorRepository.existsBySerialNumber("SN-9")).thenReturn(false);
        when(sensorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Sensor created = sensorService.create("  SN-9  ", UUID.randomUUID(), UUID.randomUUID());

        assertThat(created.getSerialNumber()).isEqualTo("SN-9");
        assertThat(created.getLastSeenAt()).isNull();
    }

    @Test
    void deleteRejectsSensorWithInstallationHistory() {
        UUID id = UUID.randomUUID();
        when(sensorRepository.findById(id)).thenReturn(Optional.of(sensor(id)));
        when(installationRepository.hasAnyForSensor(id)).thenReturn(true);

        assertThatThrownBy(() -> sensorService.delete(id))
                .isInstanceOf(ConflictException.class);

        verify(sensorRepository, never()).delete(any());
    }

    @Test
    void deleteRemovesAssociationsAndSensor() {
        UUID id = UUID.randomUUID();
        when(sensorRepository.findById(id)).thenReturn(Optional.of(sensor(id)));
        when(installationRepository.hasAnyForSensor(id)).thenReturn(false);

        sensorService.delete(id);

        verify(sensorVariableRepository).deleteBySensorId(id);
        verify(sensorRepository).delete(any());
    }

    @Test
    void updateRejectsDuplicatedSerialOnAnotherSensor() {
        UUID id = UUID.randomUUID();
        Sensor existing = sensor(id);
        when(sensorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(sensorRepository.existsBySerialNumber("SN-002")).thenReturn(true);

        assertThatThrownBy(() -> sensorService.update(id, "SN-002", null, null, null))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void createRejectsUnknownModel() {
        when(sensorRepository.existsBySerialNumber("SN-001")).thenReturn(false);
        when(sensorModelRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> sensorService.create("SN-001", UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOf(NotFoundException.class);

        verify(sensorRepository, never()).save(any());
    }

    @Test
    void createRejectsUnknownStatus() {
        when(sensorRepository.existsBySerialNumber("SN-001")).thenReturn(false);
        when(sensorModelRepository.existsById(any())).thenReturn(true);
        when(sensorStatusRepository.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> sensorService.create("SN-001", UUID.randomUUID(), UUID.randomUUID()))
                .isInstanceOf(NotFoundException.class);

        verify(sensorRepository, never()).save(any());
    }
}
