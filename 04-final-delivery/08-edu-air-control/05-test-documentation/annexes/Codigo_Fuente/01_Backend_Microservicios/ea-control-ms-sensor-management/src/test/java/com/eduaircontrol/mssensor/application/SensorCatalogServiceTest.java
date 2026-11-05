package com.eduaircontrol.mssensor.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eduaircontrol.mssensor.domain.model.MeasurementUnit;
import com.eduaircontrol.mssensor.domain.model.SensorModel;
import com.eduaircontrol.mssensor.domain.model.SensorStatus;
import com.eduaircontrol.mssensor.domain.port.out.MeasurementUnitRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorModelRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorStatusRepository;
import com.eduaircontrol.mssensor.domain.port.out.VariableRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import com.eduaircontrol.mssensor.shared.exception.ValidationException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Catalogos de hardware y de unidades.
 *
 * <p>Sin ellos {@code Sensor.sensorModelId} y {@code Sensor.sensorStatusId} eran
 * UUIDs sueltos: el alta aceptaba cualquier valor y ningun cliente tenia de donde
 * obtenerlos.
 */
class SensorCatalogServiceTest {

    private SensorRepository sensorRepository;
    private VariableRepository variableRepository;

    private SensorModelRepository modelRepository;
    private SensorStatusRepository statusRepository;
    private MeasurementUnitRepository unitRepository;

    private SensorModelService modelService;
    private SensorStatusService statusService;
    private MeasurementUnitService unitService;

    @BeforeEach
    void setUp() {
        sensorRepository = mock(SensorRepository.class);
        variableRepository = mock(VariableRepository.class);
        modelRepository = mock(SensorModelRepository.class);
        statusRepository = mock(SensorStatusRepository.class);
        unitRepository = mock(MeasurementUnitRepository.class);

        modelService = new SensorModelService(modelRepository, sensorRepository);
        statusService = new SensorStatusService(statusRepository, sensorRepository);
        unitService = new MeasurementUnitService(unitRepository, variableRepository);
    }

    // --- modelos ---

    @Test
    void createModelNormalizesCodeAndKeepsDescription() {
        when(modelRepository.existsByCode("ESP32_MQ135")).thenReturn(false);
        when(modelRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SensorModel created = modelService.create("esp32_mq135", "ESP32 con MQ-135", "  ");

        assertThat(created.getCode()).isEqualTo("ESP32_MQ135");
        assertThat(created.getName()).isEqualTo("ESP32 con MQ-135");
        assertThat(created.getDescription()).isNull();
    }

    @Test
    void createModelRejectsDuplicateCode() {
        when(modelRepository.existsByCode("ESP32_DHT11")).thenReturn(true);

        assertThatThrownBy(() -> modelService.create("esp32_dht11", "DHT11", null))
                .isInstanceOf(ConflictException.class);

        verify(modelRepository, never()).save(any());
    }

    @Test
    void createModelRequiresCodeAndName() {
        assertThatThrownBy(() -> modelService.create("  ", "DHT11", null))
                .isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> modelService.create("ESP32_DHT11", "  ", null))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void deleteModelIsBlockedWhenSensorsUseIt() {
        UUID id = UUID.randomUUID();
        when(modelRepository.findById(id)).thenReturn(Optional.of(
                SensorModel.builder().id(id).code("ESP32_DHT11").name("DHT11").build()));
        when(sensorRepository.existsBySensorModelId(id)).thenReturn(true);

        assertThatThrownBy(() -> modelService.delete(id)).isInstanceOf(ConflictException.class);

        verify(modelRepository, never()).delete(any());
    }

    @Test
    void deleteModelRemovesItWhenUnused() {
        UUID id = UUID.randomUUID();
        SensorModel model = SensorModel.builder().id(id).code("ESP32_DHT11").name("DHT11").build();
        when(modelRepository.findById(id)).thenReturn(Optional.of(model));
        when(sensorRepository.existsBySensorModelId(id)).thenReturn(false);

        modelService.delete(id);

        verify(modelRepository).delete(model);
    }

    // --- estados ---

    @Test
    void createStatusRejectsDuplicateCode() {
        when(statusRepository.existsByCode("ACTIVE")).thenReturn(true);

        assertThatThrownBy(() -> statusService.create("active", "Activo"))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void deleteStatusIsBlockedWhenSensorsUseIt() {
        UUID id = UUID.randomUUID();
        when(statusRepository.findById(id)).thenReturn(Optional.of(
                SensorStatus.builder().id(id).code("ACTIVE").name("Activo").build()));
        when(sensorRepository.existsBySensorStatusId(id)).thenReturn(true);

        assertThatThrownBy(() -> statusService.delete(id)).isInstanceOf(ConflictException.class);
    }

    // --- unidades ---

    @Test
    void createUnitKeepsSymbolAndRequiresIt() {
        when(unitRepository.existsByCode("PPM")).thenReturn(false);
        when(unitRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        MeasurementUnit created = unitService.create("ppm", "ppm", "Partes por millon");

        assertThat(created.getCode()).isEqualTo("PPM");
        assertThat(created.getSymbol()).isEqualTo("ppm");

        assertThatThrownBy(() -> unitService.create("PPM", "  ", "Partes por millon"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void deleteUnitIsBlockedWhenVariablesUseIt() {
        UUID id = UUID.randomUUID();
        when(unitRepository.findById(id)).thenReturn(Optional.of(
                MeasurementUnit.builder().id(id).code("PPM").symbol("ppm").name("ppm").build()));
        when(variableRepository.existsByMeasurementUnitId(id)).thenReturn(true);

        assertThatThrownBy(() -> unitService.delete(id)).isInstanceOf(ConflictException.class);

        verify(unitRepository, never()).delete(any());
    }
}
