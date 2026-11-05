package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.domain.model.Sensor;
import com.eduaircontrol.mssensor.domain.model.SensorVariable;
import com.eduaircontrol.mssensor.domain.model.Variable;
import com.eduaircontrol.mssensor.infrastructure.persistence.SensorInstallationJpaRepository;
import com.eduaircontrol.mssensor.infrastructure.persistence.SensorJpaRepository;
import com.eduaircontrol.mssensor.infrastructure.persistence.SensorVariableJpaRepository;
import com.eduaircontrol.mssensor.infrastructure.persistence.VariableJpaRepository;
import com.eduaircontrol.mssensor.shared.security.TestTokenMint;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import tools.jackson.databind.JsonNode;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SensorFlowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorJpaRepository sensorRepository;

    @Autowired
    private VariableJpaRepository variableRepository;

    @Autowired
    private SensorInstallationJpaRepository installationRepository;

    @Autowired
    private SensorVariableJpaRepository sensorVariableRepository;

    @Autowired
    private com.eduaircontrol.mssensor.infrastructure.persistence.SensorModelJpaRepository modelRepository;

    @Autowired
    private com.eduaircontrol.mssensor.infrastructure.persistence.SensorStatusJpaRepository statusRepository;

    @Autowired
    private com.eduaircontrol.mssensor.infrastructure.persistence.MeasurementUnitJpaRepository unitRepository;

    @Autowired
    private TestTokenMint jwtService;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    private String adminToken;
    private UUID modelId;
    private UUID statusId;
    private UUID unitId;

    @BeforeEach
    void setUp() {
        modelRepository.deleteAll();
        statusRepository.deleteAll();
        unitRepository.deleteAll();
        sensorVariableRepository.deleteAll();
        installationRepository.deleteAll();
        variableRepository.deleteAll();
        sensorRepository.deleteAll();
        modelId = modelRepository.save(
                com.eduaircontrol.mssensor.domain.model.SensorModel.builder()
                        .code("ESP32_DHT11").name("ESP32 con DHT11").build()).getId();
        statusId = statusRepository.save(
                com.eduaircontrol.mssensor.domain.model.SensorStatus.builder()
                        .code("ACTIVE").name("Activo").build()).getId();
        unitId = unitRepository.save(
                com.eduaircontrol.mssensor.domain.model.MeasurementUnit.builder()
                        .code("PERCENT").symbol("%").name("Porcentaje").build()).getId();
        adminToken = jwtService.generateToken("admin@test.com", "ADMIN");
    }

    private Sensor saveSensor(String serialNumber) {
        return sensorRepository.save(Sensor.builder()
                .serialNumber(serialNumber)
                .sensorModelId(modelId)
                .sensorStatusId(statusId)
                .build());
    }

    private Variable saveVariable(String code) {
        return variableRepository.save(Variable.builder()
                .code(code)
                .name("Variable " + code)
                .measurementUnitId(unitId)
                .build());
    }

    @Test
    void variableCreateIsUppercasedAndDuplicateReturns409() throws Exception {
        mockMvc.perform(post("/api/v1/variables")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"temp\",\"name\":\"Temperature\","
                                + "\"measurementUnitId\":\"" + unitId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("TEMP"));

        mockMvc.perform(post("/api/v1/variables")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"TEMP\",\"name\":\"Temperature\","
                                + "\"measurementUnitId\":\"" + unitId + "\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void installSensorReturns201AndOverlappingInstallReturns409() throws Exception {
        Sensor sensor = saveSensor("SN-INST-1");
        UUID environmentId = UUID.randomUUID();
        String body = "{\"sensorId\":\"" + sensor.getId() + "\","
                + "\"educationalEnvironmentId\":\"" + environmentId + "\","
                + "\"installedAt\":\"" + Instant.now().minus(1, ChronoUnit.DAYS) + "\"}";

        mockMvc.perform(post("/api/v1/sensor-installations")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorId").value(sensor.getId().toString()))
                .andExpect(jsonPath("$.removedAt").doesNotExist());

        mockMvc.perform(post("/api/v1/sensor-installations")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void installForUnknownSensorReturns404() throws Exception {
        mockMvc.perform(post("/api/v1/sensor-installations")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"" + UUID.randomUUID() + "\","
                                + "\"educationalEnvironmentId\":\"" + UUID.randomUUID() + "\","
                                + "\"installedAt\":\"" + Instant.now() + "\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void removeInstallationClosesItAndSecondRemoveReturns409() throws Exception {
        Sensor sensor = saveSensor("SN-INST-2");
        MvcResult created = mockMvc.perform(post("/api/v1/sensor-installations")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"" + sensor.getId() + "\","
                                + "\"educationalEnvironmentId\":\"" + UUID.randomUUID() + "\","
                                + "\"installedAt\":\""
                                + Instant.now().minus(1, ChronoUnit.DAYS) + "\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode json = objectMapper.readTree(created.getResponse().getContentAsString());
        String installationId = json.get("sensorInstallationId").asText();

        mockMvc.perform(post("/api/v1/sensor-installations/{id}/remove", installationId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.removedAt").exists());

        mockMvc.perform(post("/api/v1/sensor-installations/{id}/remove", installationId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));

        mockMvc.perform(get("/api/v1/sensor-installations")
                        .param("sensorId", sensor.getId().toString())
                        .param("active", "false")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void associateDissociateAndDuplicateAssociationReturns409() throws Exception {
        Sensor sensor = saveSensor("SN-VAR-1");
        Variable variable = saveVariable("TEMP");

        mockMvc.perform(post("/api/v1/sensor-variables")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"" + sensor.getId() + "\","
                                + "\"variableId\":\"" + variable.getId() + "\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/sensor-variables")
                        .param("sensorId", sensor.getId().toString())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sensorId").value(sensor.getId().toString()));

        mockMvc.perform(post("/api/v1/sensor-variables")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sensorId\":\"" + sensor.getId() + "\","
                                + "\"variableId\":\"" + variable.getId() + "\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));

        mockMvc.perform(delete("/api/v1/sensor-variables/{sensorId}/{variableId}",
                        sensor.getId(), variable.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/sensor-variables/{sensorId}/{variableId}",
                        sensor.getId(), variable.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVariableReferencedBySensorReturns409() throws Exception {
        Sensor sensor = saveSensor("SN-VAR-2");
        Variable variable = saveVariable("HUM");
        sensorVariableRepository.save(new SensorVariable(sensor.getId(), variable.getId()));

        mockMvc.perform(delete("/api/v1/variables/{id}", variable.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }
}
