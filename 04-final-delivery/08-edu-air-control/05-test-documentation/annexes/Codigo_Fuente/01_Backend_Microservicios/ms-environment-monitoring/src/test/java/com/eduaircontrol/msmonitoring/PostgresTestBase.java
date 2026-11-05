package com.eduaircontrol.msmonitoring;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Base de los tests de integracion: PostgreSQL real en Testcontainers.
 * Liquibase aplica el esquema propio de ms-environment-monitoring sobre una base limpia por suite.
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class PostgresTestBase {

    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("eduaircontrol_env_monitoring_test")
                    .withUsername("test")
                    .withPassword("test");

    @BeforeAll
    static void startContainer() {
        if (!POSTGRES.isRunning()) {
            POSTGRES.start();
        }
    }

    @DynamicPropertySource
    static void datasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
