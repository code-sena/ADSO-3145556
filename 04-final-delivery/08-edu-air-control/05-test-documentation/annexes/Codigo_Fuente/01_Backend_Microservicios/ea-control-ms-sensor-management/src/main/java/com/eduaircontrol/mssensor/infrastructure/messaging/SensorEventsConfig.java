package com.eduaircontrol.mssensor.infrastructure.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declara el exchange de eventos de sensores. Es <b>topic</b> y no direct: así
 * ms-environment-monitoring puede suscribirse a {@code sensor.installation.*} y,
 * más adelante, otro servicio a otra ruta, sin que el productor cambie.
 *
 * <p>El binding y la cola los declara el consumidor, que es quien sabe que los
 * necesita (ADR-004).
 */
@Configuration
public class SensorEventsConfig {

    @Value("${app.sensor.events.exchange:eduaircontrol.sensor}")
    private String exchangeName;

    @Bean
    public TopicExchange sensorEventsExchange() {
        return new TopicExchange(exchangeName, true, false);
    }
}
