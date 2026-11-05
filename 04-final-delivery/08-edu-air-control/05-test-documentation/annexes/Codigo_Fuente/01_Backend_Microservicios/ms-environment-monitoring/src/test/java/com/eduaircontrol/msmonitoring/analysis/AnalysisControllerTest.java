package com.eduaircontrol.msmonitoring.analysis;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eduaircontrol.msmonitoring.PostgresTestBase;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.time.temporal.ChronoUnit;
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
 * API de analisis contra el servicio aislado.
 *
 * <p>Siembra el read model y los catalogos directamente por SQL, que es como
 * llegarian en produccion: por el broker y el backfill, no por el controller.
 */
class AnalysisControllerTest extends PostgresTestBase {

    private static final UUID ENVIRONMENT_TYPE_ID =
            UUID.fromString("00000000-0000-4000-8000-000000000081");
    private static final UUID TEMPERATURE = UUID.fromString("00000000-0000-4000-8000-000000000071");
    private static final UUID CO2 = UUID.fromString("00000000-0000-4000-8000-000000000073");
    private static final UUID WARNING_SEVERITY =
            UUID.fromString("00000000-0000-4000-8000-000000000062");
    private static final UUID VALID_FLAG =
            UUID.fromString("00000000-0000-4000-8000-000000000065");

    /** La base se comparte entre tests, asi que cada uno crea su propio ambiente. */
    private UUID environmentId;
    private UUID installationId;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JdbcTemplate jdbc;

    private MockMvc mockMvc;
    private String token;

    @BeforeEach
    void setUp() {
        // springSecurity() engancha la cadena real de filtros. Sin esto, MockMvc no
        // pasa por JwtAuthFilter ni por authorizeHttpRequests y los tests de
        // autenticacion darian verde sin comprobar nada.
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(org.springframework.security.test.web.servlet.setup
                        .SecurityMockMvcConfigurers.springSecurity())
                .build();
        environmentId = UUID.randomUUID();
        installationId = UUID.randomUUID();

        String suffix = environmentId.toString().substring(0, 8);
        jdbc.update("""
                insert into environment_monitoring.educational_environment
                    (educational_environment_id, code, name, environment_type_id, floor)
                values (?, ?, ?, ?, 1)
                """, environmentId, "AULA-" + suffix, "Aula " + suffix, ENVIRONMENT_TYPE_ID);

        // Proyeccion local: la agregacion resuelve el ambiente por aqui.
        jdbc.update("""
                insert into environment_monitoring.installation_projection
                    (sensor_installation_id, educational_environment_id,
                     environment_type_id, sensor_id)
                values (?, ?, ?, ?)
                """, installationId, environmentId, ENVIRONMENT_TYPE_ID, UUID.randomUUID());

        // Umbral de advertencia de CO2 por tipo de ambiente (ADR-014): por encima
        // de 1000 ppm hay excedencia.
        jdbc.update("""
                insert into environment_monitoring.variable_threshold
                    (variable_threshold_id, variable_id,
                     environment_type_id, min_value, max_value, severity_id, valid_from)
                values (?, ?, ?, null, 1000, ?, current_date)
                """, UUID.randomUUID(), CO2, ENVIRONMENT_TYPE_ID, WARNING_SEVERITY);

        // Las 24 horas del dia UTC natural. El periodo DAY es el dia completo
        // [00:00, 00:00+1), no las ultimas 24 horas, asi que sembrar el dia entero
        // hace que el conteo de excedencias sea exacto y no dependa de la hora en
        // que corra la suite.
        Instant dayStart = Instant.now().truncatedTo(ChronoUnit.DAYS);
        for (int h = 0; h < 24; h++) {
            Instant at = dayStart.plus(h, ChronoUnit.HOURS);
            insertMeasurement(at, TEMPERATURE, "21.5");
            // La mitad de las horas supera el umbral de 1000 ppm.
            insertMeasurement(at, CO2, h % 2 == 0 ? "900" : "1200");
        }

        token = tokenFor(UUID.randomUUID(), "tester-" + suffix + "@test.com");
    }

    private void insertMeasurement(Instant measuredAt, UUID variableId, String value) {
        jdbc.update("""
                insert into environment_monitoring.environment_measurement
                    (environment_measurement_id, sensor_installation_id, variable_id,
                     measured_value, measured_at, quality_flag_id)
                values (?, ?, ?, ?, ?, ?)
                """, UUID.randomUUID(), installationId, variableId,
                new java.math.BigDecimal(value), Timestamp.from(measuredAt), VALID_FLAG);
    }

    /** Firma un token con el mismo secreto que usa el servicio. */
    private String tokenFor(UUID userId, String email) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(email)
                .claim("role", "USER")
                .claim("userId", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(key)
                .compact();
    }

    private String json(String environment, String period) {
        return "{\"environmentId\":\"" + environment + "\",\"period\":\"" + period + "\"}";
    }

    @Test
    void createsCompletedAnalysis() throws Exception {
        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(environmentId.toString(), "DAY")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.results.length()").value(2))
                .andExpect(jsonPath("$.results[0].variableCode").value("co2"));
    }

    @Test
    void countsExceedancesUsingTheWarningThreshold() throws Exception {
        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(environmentId.toString(), "DAY")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.results[?(@.variableCode=='co2')].exceedanceCount")
                        .value(12));
    }

    @Test
    void recordsWhoRequestedTheAnalysis() throws Exception {
        UUID userId = UUID.fromString("cccccccc-0000-4000-8000-000000000001");
        String userToken = tokenFor(userId, "solicitante@test.com");

        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(environmentId.toString(), "DAY")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestedBy").value(userId.toString()));
    }

    @Test
    void completesWithoutResultsWhenNoData() throws Exception {
        UUID empty = UUID.randomUUID();
        jdbc.update("""
                insert into environment_monitoring.educational_environment
                    (educational_environment_id, code, name, environment_type_id, floor)
                values (?, ?, 'Ambiente sin datos', ?, 2)
                """, empty, "VACIO-" + empty.toString().substring(0, 8), ENVIRONMENT_TYPE_ID);

        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(empty.toString(), "DAY")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.results.length()").value(0));
    }

    @Test
    void rejectsDuplicateWindow() throws Exception {
        String body = json(environmentId.toString(), "DAY");

        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsUnknownEnvironment() throws Exception {
        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(UUID.randomUUID().toString(), "DAY")))
                .andExpect(status().isNotFound());
    }

    @Test
    void requiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/analyses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(environmentId.toString(), "DAY")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listsAnalyses() throws Exception {
        createDaily();

        mockMvc.perform(get("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .param("environmentId", environmentId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.meta.page").value(1));
    }

    @Test
    void latestReturnsTheMostRecentCompleted() throws Exception {
        createDaily();

        mockMvc.perform(get("/api/v1/analyses/latest")
                        .header("Authorization", "Bearer " + token)
                        .param("environmentId", environmentId.toString())
                        .param("period", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void latestReturnsNoContentWhenThereIsNone() throws Exception {
        mockMvc.perform(get("/api/v1/analyses/latest")
                        .header("Authorization", "Bearer " + token)
                        .param("environmentId", environmentId.toString())
                        .param("period", "YEAR"))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/api/v1/analyses/{id}", UUID.randomUUID())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void capsPageSize() throws Exception {
        mockMvc.perform(get("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .param("limit", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.limit").value(100));
    }

    private void createDaily() throws Exception {
        mockMvc.perform(post("/api/v1/analyses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(environmentId.toString(), "DAY")))
                .andExpect(status().isCreated());
    }
}