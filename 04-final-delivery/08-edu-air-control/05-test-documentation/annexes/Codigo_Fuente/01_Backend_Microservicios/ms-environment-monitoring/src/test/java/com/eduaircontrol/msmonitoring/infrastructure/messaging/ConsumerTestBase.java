package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import com.eduaircontrol.msmonitoring.PostgresTestBase;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;

/**
 * Base de los tests del consumidor: PostgreSQL y RabbitMQ reales, con el listener
 * montado por la propia aplicacion.
 *
 * <p>Se prueba contra el listener de verdad y no invocando el metodo a mano, porque
 * lo que importa es el comportamiento del contenedor: acknowledgement, routing y
 * destino de los fallos.
 */
public abstract class ConsumerTestBase extends PostgresTestBase {

    protected static final RabbitMQContainer RABBIT =
            new RabbitMQContainer("rabbitmq:3.13-alpine");

    protected static final String EXCHANGE = "eduaircontrol.environmental-data.test";
    protected static final String QUEUE = EXCHANGE + ".ms-environment-monitoring";
    protected static final String DLQ = QUEUE + EnvironmentMonitoringMessagingConfig.DLQ_SUFFIX;

    protected static final UUID ENVIRONMENT_ID =
            UUID.fromString("aaaaaaaa-0000-4000-8000-000000000001");
    protected static final UUID ENVIRONMENT_TYPE_ID =
            UUID.fromString("00000000-0000-4000-8000-000000000081");
    protected static final UUID INSTALLATION_ID =
            UUID.fromString("bbbbbbbb-0000-4000-8000-000000000001");
    protected static final UUID TEMPERATURE = UUID.fromString("00000000-0000-4000-8000-000000000071");
    protected static final UUID CO2 = UUID.fromString("00000000-0000-4000-8000-000000000073");
    protected static final UUID NOISE = UUID.fromString("00000000-0000-4000-8000-000000000074");
    protected static final UUID HUMIDITY = UUID.fromString("00000000-0000-4000-8000-000000000072");

    @Autowired
    protected JdbcTemplate jdbc;

    @Autowired
    protected InboxRepository inbox;

    @Autowired
    protected ConnectionFactory connectionFactory;

    @Autowired
    protected org.springframework.amqp.rabbit.core.RabbitTemplate rabbit;

    @BeforeAll
    static void startRabbit() {
        if (!RABBIT.isRunning()) {
            RABBIT.start();
        }
    }

    @DynamicPropertySource
    static void messaging(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBIT::getHost);
        registry.add("spring.rabbitmq.port", RABBIT::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBIT::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBIT::getAdminPassword);
        registry.add("app.messaging.measurements-exchange", () -> EXCHANGE);
        registry.add("app.messaging.measurements-queue", () -> QUEUE);
    }

    @BeforeEach
    void cleanState() {
        jdbc.update("DELETE FROM environment_monitoring.environment_measurement");
        jdbc.update("DELETE FROM environment_monitoring.inbox_message");
        jdbc.update("DELETE FROM environment_monitoring.installation_projection");
        jdbc.update("DELETE FROM environment_monitoring.educational_environment");

        // Ambiente y proyeccion: el listener los necesita para atribuir lecturas.
        jdbc.update("""
                insert into environment_monitoring.educational_environment
                    (educational_environment_id, code, name, environment_type_id, floor)
                values (?, 'TEST-ENV', 'Ambiente de prueba', ?, 1)
                ON CONFLICT (educational_environment_id) DO NOTHING
                """, ENVIRONMENT_ID, ENVIRONMENT_TYPE_ID);
        jdbc.update("""
                insert into environment_monitoring.installation_projection
                    (sensor_installation_id, educational_environment_id,
                     environment_type_id, sensor_id)
                values (?, ?, ?, ?)
                ON CONFLICT (sensor_installation_id) DO NOTHING
                """, INSTALLATION_ID, ENVIRONMENT_ID, ENVIRONMENT_TYPE_ID, UUID.randomUUID());

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.purgeQueue(QUEUE, false);
        admin.purgeQueue(DLQ, false);
    }

    /** Envoltura de estandar de dominio con un solo valor informado. */
    protected Message event(UUID eventId, Instant occurredAt, String field, String value) {
        String body = "{\"eventId\":\"" + eventId + "\""
                + ",\"eventType\":\"" + EnvironmentalDataRecorded.TYPE + "\""
                + ",\"aggregateId\":\"" + ENVIRONMENT_ID + "\""
                + ",\"aggregateType\":\"" + EnvironmentalDataRecorded.AGGREGATE_TYPE + "\""
                + ",\"occurredAt\":\"" + occurredAt + "\""
                + ",\"version\":" + EnvironmentalDataRecorded.SCHEMA_VERSION
                + ",\"payload\":{\"environmentId\":\"" + ENVIRONMENT_ID + "\""
                + ",\"" + field + "\":" + value + "}"
                + ",\"metadata\":{\"correlationId\":\"" + UUID.randomUUID() + "\"}}";
        return message(body, eventId.toString());
    }

    protected Message message(String body, String messageId) {
        var builder = MessageBuilder
                .withBody(body.getBytes(java.nio.charset.StandardCharsets.UTF_8))
                .setContentType(MessageProperties.CONTENT_TYPE_JSON);
        if (messageId != null) {
            builder.setMessageId(messageId);
        }
        return builder.build();
    }

    protected void publish(Message message) {
        rabbit.send(EXCHANGE, EnvironmentalDataRecorded.ROUTING_KEY, message);
    }

    protected boolean awaitRows(long expected, long timeoutMillis) {
        return await(() -> countRows() == expected, timeoutMillis);
    }

    protected boolean awaitInbox(long expected, long timeoutMillis) {
        return await(() -> inboxCount() == expected, timeoutMillis);
    }

    protected boolean awaitQueue(String queue, long timeoutMillis) {
        return await(() -> queueDepth(queue) > 0, timeoutMillis);
    }

    private boolean await(java.util.function.BooleanSupplier condition, long timeoutMillis) {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < deadline) {
            if (condition.getAsBoolean()) {
                return true;
            }
            sleep();
        }
        return condition.getAsBoolean();
    }

    protected long countRows() {
        return jdbc.queryForObject(
                "select count(*) from environment_monitoring.environment_measurement",
                Long.class);
    }

    protected long inboxCount() {
        return jdbc.queryForObject(
                "select count(*) from environment_monitoring.inbox_message", Long.class);
    }

    protected long queueDepth(String queue) {
        return rabbit.execute(channel -> {
            try {
                var ok = channel.queueDeclarePassive(queue);
                return (long) ok.getMessageCount();
            } catch (Exception e) {
                return 0L;
            }
        });
    }

    protected BigDecimal valueOf(String variableCode) {
        return jdbc.queryForObject("""
                select measured_value
                from environment_monitoring.environment_measurement m
                join environment_monitoring.variable v on v.variable_id = m.variable_id
                where v.code = ?
                """, BigDecimal.class, variableCode);
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}