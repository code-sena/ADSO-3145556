package com.eduaircontrol.msclassroom.infrastructure.messaging.events;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Evento base para eventos de ambiente educativo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationalEnvironmentEvent {

    private UUID eventId;
    private String eventType;
    private UUID aggregateId;
    private String aggregateType;
    private Instant occurredAt;
    private Integer version;
    private Payload payload;

    public static final int SCHEMA_VERSION = 1;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Payload {
        private UUID environmentId;
        private String code;
        private String name;
        private UUID campusId;
        private UUID environmentTypeId;
        private Integer floor;
        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Metadata {
        private UUID correlationId;
        private UUID causationId;
        private UUID userId;
    }
}
