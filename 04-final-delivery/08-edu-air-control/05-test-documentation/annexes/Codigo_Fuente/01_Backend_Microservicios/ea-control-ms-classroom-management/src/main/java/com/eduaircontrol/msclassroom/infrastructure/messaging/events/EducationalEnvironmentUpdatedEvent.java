package com.eduaircontrol.msclassroom.infrastructure.messaging.events;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Evento publicado cuando se actualiza un ambiente educativo.
 *
 * <p>Routing key: {@code classroom.educational_environment.updated}
 */
public class EducationalEnvironmentUpdatedEvent {

    public static final String TYPE = "EducationalEnvironmentUpdated";
    public static final String ROUTING_KEY = "classroom.educational_environment.updated";

    private UUID eventId;
    private String eventType = TYPE;
    private UUID aggregateId;
    private String aggregateType = "EducationalEnvironment";
    private java.time.Instant occurredAt;
    private Integer version = 1;
    private Payload payload;

    public EducationalEnvironmentUpdatedEvent() {}

    @lombok.Builder
    public EducationalEnvironmentUpdatedEvent(UUID eventId, UUID aggregateId, Payload payload, java.time.Instant occurredAt) {
        this.eventId = eventId != null ? eventId : java.util.UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.occurredAt = occurredAt != null ? occurredAt : java.time.Instant.now();
    }

    @lombok.Getter @lombok.Setter
    @lombok.NoArgsConstructor @lombok.AllArgsConstructor @lombok.Builder
    public static class Payload {
        private UUID environmentId;
        private String code;
        private String name;
        private UUID campusId;
        private UUID environmentTypeId;
        private Integer floor;
        private String status;
    }

    public UUID getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public UUID getAggregateId() { return aggregateId; }
    public String getAggregateType() { return aggregateType; }
    public java.time.Instant getOccurredAt() { return occurredAt; }
    public Integer getVersion() { return version; }
    public Payload getPayload() { return payload; }
}
