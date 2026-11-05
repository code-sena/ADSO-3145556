package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.domain.model.Sensor;
import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import com.eduaircontrol.mssensor.infrastructure.persistence.SensorInstallationJpaRepository;
import com.eduaircontrol.mssensor.infrastructure.persistence.SensorJpaRepository;
import com.eduaircontrol.mssensor.infrastructure.persistence.SensorModelJpaRepository;
import com.eduaircontrol.mssensor.infrastructure.persistence.SensorStatusJpaRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorJpaRepository sensorRepository;

    @Autowired
    private SensorInstallationJpaRepository installationRepository;

    @Autowired
    private SensorModelJpaRepository modelRepository;

    @Autowired
    private SensorStatusJpaRepository statusRepository;

    @Autowired
    private TestTokenMint jwtService;

    private String adminToken;
    private String userToken;
    private UUID modelId;
    private UUID statusId;

    @BeforeEach
    void setUp() {
        modelRepository.deleteAll();
        statusRepository.deleteAll();
        installationRepository.deleteAll();
        sensorRepository.deleteAll();
        // El alta exige modelo y estado reales: sin catalogo no hay sensor.
        modelId = modelRepository.save(
                com.eduaircontrol.mssensor.domain.model.SensorModel.builder()
                        .code("ESP32_DHT11").name("ESP32 con DHT11").build())
                .getId();
        statusId = statusRepository.save(
                com.eduaircontrol.mssensor.domain.model.SensorStatus.builder()
                        .code("ACTIVE").name("Activo").build())
                .getId();
        adminToken = jwtService.generateToken("admin@test.com", "ADMIN");
        userToken = jwtService.generateToken("user@test.com", "USER");
    }

    private Sensor saveSensor(String serialNumber) {
        return sensorRepository.save(Sensor.builder()
                .serialNumber(serialNumber)
                .sensorModelId(modelId)
                .sensorStatusId(statusId)
                .build());
    }

    private String createBody(String serialNumber) {
        return "{\"serialNumber\":\"" + serialNumber + "\","
                + "\"sensorModelId\":\"" + modelId + "\","
                + "\"sensorStatusId\":\"" + statusId + "\"}";
    }

    @Test
    void healthIsPublicAndOk() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.dependencies.database").value("ok"));
    }

    @Test
    void listWithoutTokenReturns401WithSharedErrorFormat() throws Exception {
        mockMvc.perform(get("/api/v1/sensors"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    void createAsUserReturns403WithSharedErrorFormat() throws Exception {
        mockMvc.perform(post("/api/v1/sensors")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("SN-403")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }

    @Test
    void createAsAdminReturns201WithLocation() throws Exception {
        mockMvc.perform(post("/api/v1/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("SN-NEW-1")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        org.hamcrest.Matchers.containsString("/api/v1/sensors/")))
                .andExpect(jsonPath("$.serialNumber").value("SN-NEW-1"));
    }

    @Test
    void createDuplicateSerialReturns409() throws Exception {
        saveSensor("SN-DUP");

        mockMvc.perform(post("/api/v1/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("SN-DUP")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void createWithoutRequiredFieldsReturns400WithDetails() throws Exception {
        mockMvc.perform(post("/api/v1/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[?(@.field == 'serialNumber')]").exists())
                .andExpect(jsonPath("$.details[?(@.field == 'sensorModelId')]").exists());
    }

    @Test
    void listFiltersBySerialQueryAndModelId() throws Exception {
        UUID modelId = UUID.randomUUID();
        Sensor one = saveSensor("SN-AAA");
        one.setSensorModelId(modelId);
        sensorRepository.save(one);
        saveSensor("SN-BBB");

        mockMvc.perform(get("/api/v1/sensors")
                        .param("q", "aaa")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].serialNumber").value("SN-AAA"));

        mockMvc.perform(get("/api/v1/sensors")
                        .param("sensorModelId", modelId.toString())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].serialNumber").value("SN-AAA"));
    }

    @Test
    void listWithInvalidLimitReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/sensors")
                        .param("limit", "500")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void getByIdReturns404ForUnknownId() throws Exception {
        mockMvc.perform(get("/api/v1/sensors/{id}", UUID.randomUUID())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void patchUpdatesSerialAndLastSeenAt() throws Exception {
        Sensor sensor = saveSensor("SN-OLD");

        mockMvc.perform(patch("/api/v1/sensors/{id}", sensor.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serialNumber\":\"SN-RENAMED\",\"lastSeenAt\":\""
                                + Instant.now().truncatedTo(ChronoUnit.SECONDS) + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("SN-RENAMED"))
                .andExpect(jsonPath("$.lastSeenAt").exists());
    }

    @Test
    void deleteIsPhysicalAndRemovesSensor() throws Exception {
        Sensor sensor = saveSensor("SN-DEL");

        mockMvc.perform(delete("/api/v1/sensors/{id}", sensor.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/sensors/{id}", sensor.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWithInstallationHistoryReturns409() throws Exception {
        Sensor sensor = saveSensor("SN-INSTALLED");
        installationRepository.save(SensorInstallation.builder()
                .sensorId(sensor.getId())
                .educationalEnvironmentId(UUID.randomUUID())
                .installedAt(Instant.now().minus(1, ChronoUnit.DAYS))
                .build());

        mockMvc.perform(delete("/api/v1/sensors/{id}", sensor.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void listIsScopedByInstitutionHeader() throws Exception {
        UUID institutionA = UUID.randomUUID();
        UUID institutionB = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionA.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody("SN-SCOPED-A")))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionB.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total").value(0));

        mockMvc.perform(get("/api/v1/sensors")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionA.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total").value(1));
    }
}
