package com.eduaircontrol.userexperience;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * API de experiencia de usuario contra el servicio aislado.
 *
 * <p>El usuario siempre sale del token; ningun endpoint acepta userId en el cuerpo.
 */
class UserExperienceControllerTest extends PostgresTestBase {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JdbcTemplate jdbc;

    private MockMvc mockMvc;
    private String token;
    private UUID userId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(org.springframework.security.test.web.servlet.setup
                        .SecurityMockMvcConfigurers.springSecurity())
                .build();
        userId = UUID.randomUUID();
        token = tokenFor(userId, "ux-" + userId.toString().substring(0, 8) + "@test.com");
        jdbc.update("DELETE FROM user_experience.searches WHERE user_id = ?", userId);
        jdbc.update("DELETE FROM user_experience.favorites WHERE user_id = ?", userId);
        jdbc.update("DELETE FROM user_experience.classroom_ratings WHERE user_id = ?", userId);
        jdbc.update("DELETE FROM user_experience.user_preferences WHERE user_id = ?", userId);
    }

    private String tokenFor(UUID id, String email) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(email)
                .claim("role", "USER")
                .claim("userId", id.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(key)
                .compact();
    }

    @Test
    void createsDefaultPreferencesOnFirstRead() throws Exception {
        mockMvc.perform(get("/api/v1/preferences")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.language").value("es"));
    }

    @Test
    void updatesPreferences() throws Exception {
        mockMvc.perform(put("/api/v1/preferences")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"language\":\"en\",\"darkMode\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.language").value("en"));
    }

    @Test
    void togglesFavorite() throws Exception {
        UUID environmentId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/favorites/{id}/toggle", environmentId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorite").value(true));

        mockMvc.perform(get("/api/v1/favorites")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(post("/api/v1/favorites/{id}/toggle", environmentId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favorite").value(false));
    }

    @Test
    void ratesClassroom() throws Exception {
        UUID classroomId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/classrooms/{id}/ratings", classroomId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"score\":5,\"comment\":\"Excelente aula\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(5));

        mockMvc.perform(get("/api/v1/ratings")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void rejectsInvalidScore() throws Exception {
        mockMvc.perform(post("/api/v1/classrooms/{id}/ratings", UUID.randomUUID())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"score\":9}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recordsAndListsSearches() throws Exception {
        mockMvc.perform(post("/api/v1/searches")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"searchText\":\"aula norte\",\"appliedFilter\":\"floor:2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchText").value("aula norte"));

        mockMvc.perform(get("/api/v1/searches")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/preferences"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/favorites"))
                .andExpect(status().isUnauthorized());
    }
}
