package com.eduaircontrol.msclassroom.infrastructure.messaging.events;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Evento publicado cuando se crea un nuevo ambiente educativo.
 *
 * <p>Routing key: {@code classroom.educational_environment.created}
 */
public class EducationalEnvironmentCreatedEvent {

    public static final String TYPE = "EducationalEnvironmentCreated";
    public static final String ROUTING_KEY = "classroom.educational_environment.created";

    private UUID eventId;
    private String eventType = TYPE;
    private UUID aggregateId;
    private String aggregateType = "EducationalEnvironment";
    private java.time.Instant occurredAt;
    private Integer version = 1;
    private Payload payload;

    public EducationalEnvironmentCreatedEvent() {}

    @lombok.Builder
    public EducationalEnvironmentCreatedEvent(UUID eventId, UUID aggregateId, Payload payload, Instant occurredAt) {
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
    public Instant getOccurredAt() { return occurredAt; }
    public Integer getVersion() { return version; }
    public Payload getPayload() { return payload; }

    public static EducationalEnvironmentCreatedEvent of(UUID environmentId, String code, String name,
                                                        UUID campusId, UUID environmentTypeId, Integer floor,
                                                        String status, Instant occurredAt) {
        UUID eventId = java.util.UUID.randomUUID();
        Payload payload = Payload.builder()
                .environmentId(environmentId)
                .code(code)
                .name(name)
                .campusId(campusId)
                .environmentTypeId(environmentTypeId)
                .floor(floor)
                .status(status)
                .build();
        return EducationalEnvironmentCreatedEvent.builder()
                .eventId(eventId)
                .aggregateId(environmentId)
                .payload(payload)
                .occurredAt(occurredAt != null ? occurredAt : java.time.Instant.now())
                .build();
    }
}
