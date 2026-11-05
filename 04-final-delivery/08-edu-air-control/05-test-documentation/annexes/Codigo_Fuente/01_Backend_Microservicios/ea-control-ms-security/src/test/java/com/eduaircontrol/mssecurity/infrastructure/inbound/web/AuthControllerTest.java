package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eduaircontrol.mssecurity.application.AuthResult;
import com.eduaircontrol.mssecurity.application.AuthService;
import com.eduaircontrol.mssecurity.domain.model.User;
import com.eduaircontrol.mssecurity.domain.model.Institution;
import com.eduaircontrol.mssecurity.domain.model.InstitutionStatus;
import com.eduaircontrol.mssecurity.infrastructure.outbound.persistence.InstitutionJpaRepository;
import com.eduaircontrol.mssecurity.infrastructure.outbound.persistence.UserJpaRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final String PASSWORD = "SecurePass123!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InstitutionJpaRepository institutionRepository;

    @Autowired
    private com.eduaircontrol.mssecurity.infrastructure.security.JwtService jwtService;

    @BeforeEach
    void seedInstitution() {
        if (institutionRepository.findByCode("SEN-4444").isEmpty()) {
            institutionRepository.save(Institution.builder()
                    .code("SEN-4444")
                    .name("SENA")
                    .status(InstitutionStatus.ACTIVE)
                    .build());
        }
    }

    @MockitoBean
    private com.eduaircontrol.mssecurity.application.port.TokenBlacklist tokenBlacklist;

    @Test
    void healthReportsDatabaseConnected() throws Exception {
        mockMvc.perform(get("/api/v1/auth/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.db").value("connected"))
                .andExpect(jsonPath("$.redis").isString());
    }

    @Test
    void registerReturnsTokensAndUser() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"register-%s@example.com","password":"%s","username":"new.user","companyCode":"SEN-4444"}
                                """.formatted(UUID.randomUUID().toString().substring(0, 8), PASSWORD)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken", not(blankOrNullString())))
                .andExpect(jsonPath("$.refreshToken", not(blankOrNullString())))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.user.email").isString())
                .andExpect(jsonPath("$.user.roles[0]").value("USER"))
                .andExpect(jsonPath("$.user.permissions").isArray());
    }

    @Test
    void registerDuplicateEmailReturns409() throws Exception {
        String email = "dup-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));
        register(email);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s","username":"other","companyCode":"SEN-4444"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("EMAIL_ALREADY_EXISTS"));
    }

    @Test
    void registerWeakPasswordReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"weak-%s@example.com","password":"weak","username":"w","companyCode":"SEN-4444"}
                                """.formatted(UUID.randomUUID().toString().substring(0, 8))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void loginReturnsTokens() throws Exception {
        String email = "login-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));
        register(email);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", not(blankOrNullString())))
                .andExpect(jsonPath("$.user.roles[0]").value("USER"));
    }

    @Test
    void loginWithWrongPasswordReturns401() throws Exception {
        String email = "bad-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));
        register(email);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"Nope12345"}
                                """.formatted(email)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("INVALID_CREDENTIALS"));
    }

    @Test
    void accountLocksAfterFiveFailedAttempts() throws Exception {
        String email = "lock-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));
        register(email);

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"email":"%s","password":"Nope12345"}
                                    """.formatted(email)))
                    .andExpect(status().isUnauthorized());
        }
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isLocked())
                .andExpect(jsonPath("$.error").value("ACCOUNT_LOCKED"));
    }

    @Test
    void refreshRotatesTokensAndOldTokenStopsWorking() throws Exception {
        String email = "refresh-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));
        register(email);
        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();
        String refreshToken = com.jayway.jsonpath.JsonPath.parse(login.getResponse().getContentAsString())
                .read("$.refreshToken");

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"%s"}
                                """.formatted(refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken", not(blankOrNullString())));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"%s"}
                                """.formatted(refreshToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("REFRESH_TOKEN_INVALID"));
    }

    @Test
    void logoutRevokesRefreshToken() throws Exception {
        String email = "logout-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));
        register(email);
        MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();
        String body = login.getResponse().getContentAsString();
        String accessToken = com.jayway.jsonpath.JsonPath.parse(body).read("$.accessToken");
        String refreshToken = com.jayway.jsonpath.JsonPath.parse(body).read("$.refreshToken");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"%s","allDevices":false}
                                """.formatted(refreshToken)))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"%s"}
                                """.formatted(refreshToken)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutWithoutTokenReturns401() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwksExposesRsaPublicKey() throws Exception {
        mockMvc.perform(get("/api/v1/auth/jwks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys[0].kty").value("RSA"))
                .andExpect(jsonPath("$.keys[0].alg").value("RS256"))
                .andExpect(jsonPath("$.keys[0].n").isString())
                .andExpect(jsonPath("$.keys[0].e").isString())
                .andExpect(jsonPath("$.keys[0].kid").isString());
    }

    @Test
    void loginForUnknownUserReturns401() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"nobody-%s@example.com","password":"Whatever1"}
                                """.formatted(UUID.randomUUID().toString().substring(0, 8))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerInvalidEmailReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"not-an-email","password":"%s","username":"nobody","companyCode":"SEN-4444"}
                                """.formatted(PASSWORD)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    /**
     * El codigo de empresa tiene formato fijo: tres letras, guion y cuatro
     * digitos (ABC-0000). El backend es donde se fija; los frontends solo
     * ayudan.
     */
    @Test
    void registerRejectsCompanyCodeWithWrongFormat() throws Exception {
        for (String bad : new String[] {"SEN-444", "SEN_4444", "SE-4444", "SEN-44444", "123-4444"}) {
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"email":"fmt-%s@example.com","password":"%s","username":"nobody","companyCode":"%s"}
                                    """.formatted(
                                    UUID.randomUUID().toString().substring(0, 8), PASSWORD, bad)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
        }
    }

    /** El codigo se guarda en mayusculas, asi que las minusculas tambien valen. */
    @Test
    void registerAcceptsLowercaseCompanyCode() throws Exception {
        String email = "lower-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s","username":"lower.user","companyCode":"sen-4444"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isCreated());
    }

    /**
     * GET sobre un endpoint autenticado que solo acepta POST es 405, no 500.
     * Sin el manejador especifico caia en el generico de Exception.
     */
    @Test
    void unsupportedHttpMethodReturns405() throws Exception {
        String superToken = jwtService.generateAccessToken(UUID.randomUUID(), "sa@test.com", "sa",
                java.util.List.of("SUPER_ADMIN"), null, null);

        mockMvc.perform(get("/api/v1/device-tokens")
                        .header("Authorization", "Bearer " + superToken))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.error").value("METHOD_NOT_ALLOWED"));
    }

    @Test
    void refreshUnknownTokenReturns401() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"%s"}
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("REFRESH_TOKEN_INVALID"));
    }

    @Test
    void logoutAllDevicesRevokesEveryToken() throws Exception {
        String email = "alldev-%s@example.com".formatted(UUID.randomUUID().toString().substring(0, 8));
        register(email);
        String accessToken = login(email).read("$.accessToken");
        String firstRefresh = login(email).read("$.refreshToken");
        String secondRefresh = login(email).read("$.refreshToken");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"refreshToken":"%s","allDevices":true}
                                """.formatted(firstRefresh)))
                .andExpect(status().isNoContent());

        for (String token : new String[] {firstRefresh, secondRefresh}) {
            mockMvc.perform(post("/api/v1/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"refreshToken":"%s"}
                                    """.formatted(token)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void institutionsRequireSuperAdmin() throws Exception {
        String userToken = jwtService.generateAccessToken(UUID.randomUUID(), "user@test.com", "user",
                java.util.List.of("USER"), null, null);

        mockMvc.perform(get("/api/v1/institutions")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void superAdminCanCreateInstitution() throws Exception {
        String superToken = jwtService.generateAccessToken(UUID.randomUUID(), "sa@test.com", "sa",
                java.util.List.of("SUPER_ADMIN"), null, null);

        mockMvc.perform(post("/api/v1/institutions")
                        .header("Authorization", "Bearer " + superToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"code":"UNI-1000","name":"Universidad"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("UNI-1000"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    private com.jayway.jsonpath.DocumentContext login(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s"}
                                """.formatted(email, PASSWORD)))
                .andExpect(status().isOk())
                .andReturn();
        return com.jayway.jsonpath.JsonPath.parse(result.getResponse().getContentAsString());
    }

    private void register(String email) throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"%s","password":"%s","username":"%s","companyCode":"SEN-4444"}
                                """.formatted(email, PASSWORD, email.replace("@", "_").replace(".", "_"))))
                .andExpect(status().isCreated());
    }
}
