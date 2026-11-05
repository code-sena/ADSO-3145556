package com.eduaircontrol.msclassroom.infrastructure.web;

import com.eduaircontrol.msclassroom.infrastructure.persistence.CampusJpaRepository;
import com.eduaircontrol.msclassroom.infrastructure.persistence.EducationalEnvironmentJpaRepository;
import com.eduaircontrol.msclassroom.infrastructure.persistence.EnvironmentTypeJpaRepository;
import com.eduaircontrol.msclassroom.domain.model.Campus;
import com.eduaircontrol.msclassroom.domain.model.EducationalEnvironment;
import com.eduaircontrol.msclassroom.domain.model.EnvironmentType;
import com.eduaircontrol.msclassroom.domain.model.RecordStatus;
import com.eduaircontrol.msclassroom.shared.security.TestTokenMint;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EducationalEnvironmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampusJpaRepository campusRepository;

    @Autowired
    private EnvironmentTypeJpaRepository environmentTypeRepository;

    @Autowired
    private EducationalEnvironmentJpaRepository environmentRepository;

    @Autowired
    private TestTokenMint jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private Campus campus;
    private EnvironmentType environmentType;

    @BeforeEach
    void setUp() {
        environmentRepository.deleteAll();
        campusRepository.deleteAll();
        environmentTypeRepository.deleteAll();

        adminToken = jwtService.generateToken("admin@test.com", "ADMIN");
        campus = campusRepository.save(Campus.builder()
                .code("CAMP-1")
                .name("Campus Uno")
                .status(RecordStatus.ACTIVE)
                .build());
        environmentType = environmentTypeRepository.save(EnvironmentType.builder()
                .code("CLASSROOM")
                .name("Aula")
                .build());
    }

    private String validBody() {
        return "{\"campusId\":\"" + campus.getId()
                + "\",\"code\":\"a-101\",\"name\":\"Aula 101\","
                + "\"environmentTypeId\":\"" + environmentType.getId()
                + "\",\"floor\":1,\"areaM2\":65.5,\"occupancyCapacity\":40}";
    }

    @Test
    void createReturns201WithUppercaseCode() throws Exception {
        mockMvc.perform(post("/api/v1/educational-environments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("A-101"))
                .andExpect(jsonPath("$.campusId").value(campus.getId().toString()))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createWithUnknownCampusReturns400() throws Exception {
        String body = "{\"campusId\":\"" + UUID.randomUUID()
                + "\",\"code\":\"A-101\",\"name\":\"Aula 101\","
                + "\"environmentTypeId\":\"" + environmentType.getId() + "\"}";

        mockMvc.perform(post("/api/v1/educational-environments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void createDuplicateCodePerCampusReturns409() throws Exception {
        environmentRepository.save(EducationalEnvironment.builder()
                .campusId(campus.getId())
                .code("A-101")
                .name("Existente")
                .environmentTypeId(environmentType.getId())
                .status(RecordStatus.ACTIVE)
                .build());

        mockMvc.perform(post("/api/v1/educational-environments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validBody()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void createWithNegativeFloorReturns400() throws Exception {
        String body = validBody().replace("\"floor\":1", "\"floor\":-1");

        mockMvc.perform(post("/api/v1/educational-environments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void createWithZeroCapacityReturns400() throws Exception {
        String body = validBody().replace("\"occupancyCapacity\":40", "\"occupancyCapacity\":0");

        mockMvc.perform(post("/api/v1/educational-environments")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void listFiltersByCampusAndStatus() throws Exception {
        environmentRepository.save(EducationalEnvironment.builder()
                .campusId(campus.getId())
                .code("A-101")
                .name("Aula 101")
                .environmentTypeId(environmentType.getId())
                .status(RecordStatus.ACTIVE)
                .build());
        environmentRepository.save(EducationalEnvironment.builder()
                .campusId(campus.getId())
                .code("B-201")
                .name("Aula 201")
                .environmentTypeId(environmentType.getId())
                .status(RecordStatus.INACTIVE)
                .build());

        mockMvc.perform(get("/api/v1/educational-environments")
                        .param("campusId", campus.getId().toString())
                        .param("status", "ACTIVE")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].code").value("A-101"));
    }

    @Test
    void patchChangesStatusToInactive() throws Exception {
        EducationalEnvironment environment = environmentRepository.save(EducationalEnvironment.builder()
                .campusId(campus.getId())
                .code("A-101")
                .name("Aula 101")
                .environmentTypeId(environmentType.getId())
                .status(RecordStatus.ACTIVE)
                .build());

        mockMvc.perform(patch("/api/v1/educational-environments/{id}", environment.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INACTIVE\",\"occupancyCapacity\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.occupancyCapacity").value(50))
                .andExpect(jsonPath("$.name").value("Aula 101"));
    }

    @Test
    void deleteIsSoftAndReturns404Afterwards() throws Exception {
        EducationalEnvironment environment = environmentRepository.save(EducationalEnvironment.builder()
                .campusId(campus.getId())
                .code("A-101")
                .name("Aula 101")
                .environmentTypeId(environmentType.getId())
                .status(RecordStatus.ACTIVE)
                .build());

        mockMvc.perform(delete("/api/v1/educational-environments/{id}", environment.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/educational-environments/{id}", environment.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
