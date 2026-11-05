package com.eduaircontrol.msclassroom.infrastructure.web;

import com.eduaircontrol.msclassroom.infrastructure.persistence.CampusJpaRepository;
import com.eduaircontrol.msclassroom.domain.model.Campus;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CampusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CampusJpaRepository campusRepository;

    @Autowired
    private TestTokenMint jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        campusRepository.deleteAll();
        adminToken = jwtService.generateToken("admin@test.com", "ADMIN");
        userToken = jwtService.generateToken("user@test.com", "USER");
    }

    private Campus saveCampus(String code, String name) {
        return campusRepository.save(Campus.builder()
                .code(code)
                .name(name)
                .status(RecordStatus.ACTIVE)
                .build());
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
        mockMvc.perform(get("/api/v1/campuses"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    void createAsUserReturns403WithSharedErrorFormat() throws Exception {
        mockMvc.perform(post("/api/v1/campuses")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"C-1\",\"name\":\"Campus 1\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }

    @Test
    void createAsAdminReturns201AndUppercasesCode() throws Exception {
        mockMvc.perform(post("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"camp-norte\",\"name\":\"Campus Norte\",\"city\":\"Bogotá\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/api/v1/campuses/")))
                .andExpect(jsonPath("$.code").value("CAMP-NORTE"))
                .andExpect(jsonPath("$.name").value("Campus Norte"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createDuplicateCodeReturns409() throws Exception {
        saveCampus("CAMP-1", "Existente");

        mockMvc.perform(post("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"camp-1\",\"name\":\"Otro\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("CONFLICT"));
    }

    @Test
    void createWithoutRequiredFieldsReturns400WithDetails() throws Exception {
        mockMvc.perform(post("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[?(@.field == 'code')]").exists())
                .andExpect(jsonPath("$.details[?(@.field == 'name')]").exists());
    }

    @Test
    void listReturnsPaginatedDataAndMeta() throws Exception {
        saveCampus("CAMP-1", "Campus Uno");
        saveCampus("CAMP-2", "Campus Dos");

        mockMvc.perform(get("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.meta.page").value(1))
                .andExpect(jsonPath("$.meta.limit").value(20))
                .andExpect(jsonPath("$.meta.total").value(2))
                .andExpect(jsonPath("$.meta.totalPages").value(1));
    }

    @Test
    void listFiltersByQuery() throws Exception {
        saveCampus("CAMP-NORTE", "Campus Norte");
        saveCampus("CAMP-SUR", "Campus Sur");

        mockMvc.perform(get("/api/v1/campuses")
                        .param("q", "norte")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].code").value("CAMP-NORTE"));
    }

    @Test
    void listWithInvalidLimitReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/campuses")
                        .param("limit", "500")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void getByIdReturns404ForUnknownId() throws Exception {
        mockMvc.perform(get("/api/v1/campuses/{id}", UUID.randomUUID())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void patchUpdatesOnlySubmittedFields() throws Exception {
        Campus campus = saveCampus("CAMP-1", "Campus Original");

        mockMvc.perform(patch("/api/v1/campuses/{id}", campus.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Campus Original"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void deleteIsSoftAndRemovesFromGetAndList() throws Exception {
        Campus campus = saveCampus("CAMP-1", "Campus Borrable");

        mockMvc.perform(delete("/api/v1/campuses/{id}", campus.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/campuses/{id}", campus.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void listIsScopedByInstitutionHeader() throws Exception {
        UUID institutionA = UUID.randomUUID();
        UUID institutionB = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionA.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"CAMP-A\",\"name\":\"Campus A\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionB.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total").value(0));

        mockMvc.perform(get("/api/v1/campuses")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionA.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total").value(1))
                .andExpect(jsonPath("$.data[0].code").value("CAMP-A"));
    }

    @Test
    void acceptsGatewayIdentityHeaders() throws Exception {
        mockMvc.perform(get("/api/v1/campuses")
                        .header("X-User-Id", "user-1")
                        .header("X-User-Role", "USER"))
                .andExpect(status().isOk());
    }

    @Test
    void adminViaGatewayHeaderCanCreate() throws Exception {
        mockMvc.perform(post("/api/v1/campuses")
                        .header("X-User-Id", "admin-1")
                        .header("X-User-Role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"HDR-1\",\"name\":\"Header Campus\"}"))
                .andExpect(status().isCreated());
    }
}
