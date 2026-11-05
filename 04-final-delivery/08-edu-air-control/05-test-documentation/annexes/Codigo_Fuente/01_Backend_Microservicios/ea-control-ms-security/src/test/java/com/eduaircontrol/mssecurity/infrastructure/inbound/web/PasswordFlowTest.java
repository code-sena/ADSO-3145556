package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eduaircontrol.mssecurity.domain.model.Institution;
import com.eduaircontrol.mssecurity.domain.model.InstitutionStatus;
import com.eduaircontrol.mssecurity.domain.model.PasswordResetToken;
import com.eduaircontrol.mssecurity.domain.model.User;
import com.eduaircontrol.mssecurity.infrastructure.outbound.persistence.InstitutionJpaRepository;
import com.eduaircontrol.mssecurity.infrastructure.outbound.persistence.PasswordResetTokenJpaRepository;
import com.eduaircontrol.mssecurity.infrastructure.outbound.persistence.UserJpaRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PasswordFlowTest {

    private static final String PASSWORD = "SecurePass123!";
    private static final String NEW_PASSWORD = "NewSecure456!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private PasswordResetTokenJpaRepository tokenRepository;

    @Autowired
    private InstitutionJpaRepository institutionRepository;

    @BeforeEach
    void seedInstitution() {
        if (institutionRepository.findByCode("SEN-4444").isEmpty()) {
            institutionRepository.save(Institution.builder()
                    .code("SEN-4444").name("SENA").status(InstitutionStatus.ACTIVE).build());
        }
    }

    private static String sha256(String value) {
        try {
            return HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String register(String email) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s","username":"%s","companyCode":"SEN-4444"}
                                """.formatted(email, PASSWORD, email.replace("@", "_").replace(".", "_"))))
                .andExpect(status().isCreated());
        return email;
    }

    private String login(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s"}
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn();
        return com.jayway.jsonpath.JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.accessToken");
    }

    private void issueKnownCode(String email, String code) {
        User user = userRepository.findByEmail(email).orElseThrow();
        tokenRepository.save(PasswordResetToken.builder()
                .userId(user.getId())
                .codeHash(sha256(code))
                .expiresAt(Instant.now().plusSeconds(600))
                .createdAt(Instant.now())
                .build());
    }

    @Test
    void forgotPasswordAlwaysReturns204() throws Exception {
        String email = register("fp-%s@example.com".formatted(UUID.randomUUID()));
        mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"%s\"}".formatted(email)))
                .andExpect(status().isNoContent());
        // también para correos inexistentes (no revela existencia)
        mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nobody-%s@example.com\"}".formatted(UUID.randomUUID())))
                .andExpect(status().isNoContent());
    }

    @Test
    void resetPasswordWithValidCodeChangesPassword() throws Exception {
        String email = register("reset-%s@example.com".formatted(UUID.randomUUID()));
        issueKnownCode(email, "123456");

        mockMvc.perform(post("/api/v1/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"%s\",\"code\":\"123456\"}".formatted(email)))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"%s\",\"code\":\"123456\",\"newPassword\":\"%s\"}"
                                .formatted(email, NEW_PASSWORD)))
                .andExpect(status().isNoContent());

        login(email, NEW_PASSWORD);
    }

    @Test
    void verifyCodeWithWrongCodeReturns400() throws Exception {
        String email = register("wrong-%s@example.com".formatted(UUID.randomUUID()));
        issueKnownCode(email, "123456");

        mockMvc.perform(post("/api/v1/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"%s\",\"code\":\"000000\"}".formatted(email)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changePasswordRequiresCurrentPassword() throws Exception {
        String email = register("change-%s@example.com".formatted(UUID.randomUUID()));
        String token = login(email, PASSWORD);

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"%s\",\"newPassword\":\"%s\"}"
                                .formatted(NEW_PASSWORD, NEW_PASSWORD)))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"%s\",\"newPassword\":\"%s\"}"
                                .formatted(PASSWORD, NEW_PASSWORD)))
                .andExpect(status().isNoContent());

        login(email, NEW_PASSWORD);
    }

    @Test
    void deleteAccountSoftDeletesUser() throws Exception {
        String email = register("del-%s@example.com".formatted(UUID.randomUUID()));
        String token = login(email, PASSWORD);

        mockMvc.perform(delete("/api/v1/auth/account")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"%s\"}".formatted(PASSWORD)))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"%s\",\"password\":\"%s\"}"
                                .formatted(email, PASSWORD)))
                .andExpect(status().isUnauthorized());
    }
}
