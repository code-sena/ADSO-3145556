package com.eduaircontrol.msclassroom.infrastructure.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declara el exchange de eventos de aulas. Es <b>topic</b> y no direct: así
 * ms-environment-monitoring puede suscribirse a {@code classroom.educational_environment.*}
 * y, más adelante, otro servicio a {@code sensor.*}, sin que el productor cambie.
 */
@Configuration
public class ClassroomEventsConfig {

    @Value("${app.classroom.events.exchange:eduaircontrol.classroom}")
    private String exchangeName;

    @org.springframework.context.annotation.Bean
    public org.springframework.amqp.core.TopicExchange classroomEventsExchange() {
        return new org.springframework.amqp.core.TopicExchange(exchangeName, true, false);
    }
}
