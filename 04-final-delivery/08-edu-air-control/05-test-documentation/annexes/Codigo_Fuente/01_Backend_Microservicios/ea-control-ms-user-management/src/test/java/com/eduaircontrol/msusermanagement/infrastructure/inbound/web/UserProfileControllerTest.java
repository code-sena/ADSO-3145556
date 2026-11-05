package com.eduaircontrol.msusermanagement.infrastructure.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eduaircontrol.msusermanagement.domain.model.RecordStatus;
import com.eduaircontrol.msusermanagement.domain.model.UserProfile;
import com.eduaircontrol.msusermanagement.infrastructure.persistence.UserProfileJpaRepository;
import com.eduaircontrol.msusermanagement.shared.security.TestTokenMint;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserProfileJpaRepository repository;

    @Autowired
    private TestTokenMint jwtService;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        adminToken = jwtService.generateToken("admin@test.com", "ADMIN");
        userToken = jwtService.generateToken("user@test.com", "USER");
    }

    private UserProfile saveProfile(UUID userId, String fullName) {
        return repository.save(UserProfile.builder()
                .userId(userId)
                .fullName(fullName)
                .status(RecordStatus.ACTIVE)
                .build());
    }

    @Test
    void healthIsPublicAndOk() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void listWithoutTokenReturns401() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"));
    }

    @Test
    void createAsUserReturns403() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + UUID.randomUUID() + "\",\"fullName\":\"Ada\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"));
    }

    @Test
    void createAsAdminReturns201() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + userId + "\",\"fullName\":\"Ada Lovelace\",\"department\":\"Systems\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void createWithMissingFullNameReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + UUID.randomUUID() + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void createDuplicateUserIdReturns409() throws Exception {
        UUID userId = UUID.randomUUID();
        saveProfile(userId, "Existing");

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + userId + "\",\"fullName\":\"Duplicated\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void getUnknownReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    @Test
    void getByUserIdReturnsProfile() throws Exception {
        UUID userId = UUID.randomUUID();
        saveProfile(userId, "Ada Lovelace");

        mockMvc.perform(get("/api/v1/users/by-user/" + userId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Ada Lovelace"));
    }

    @Test
    void listReturnsPaginatedProfiles() throws Exception {
        saveProfile(UUID.randomUUID(), "Ada Lovelace");
        saveProfile(UUID.randomUUID(), "Grace Hopper");

        mockMvc.perform(get("/api/v1/users?page=1&limit=10")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total").value(2))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void updateAsAdminChangesFields() throws Exception {
        UserProfile saved = saveProfile(UUID.randomUUID(), "Ada Lovelace");

        mockMvc.perform(put("/api/v1/users/" + saved.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Grace Hopper\",\"status\":\"INACTIVE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Grace Hopper"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void deleteAsAdminSoftDeletes() throws Exception {
        UserProfile saved = saveProfile(UUID.randomUUID(), "Ada Lovelace");

        mockMvc.perform(delete("/api/v1/users/" + saved.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/" + saved.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void listIsScopedByInstitutionHeader() throws Exception {
        UUID institutionA = UUID.randomUUID();
        UUID institutionB = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionA.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + UUID.randomUUID() + "\",\"fullName\":\"Ada Lovelace\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionB.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total").value(0));

        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .header("X-Institution-Id", institutionA.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.total").value(1));
    }
}
