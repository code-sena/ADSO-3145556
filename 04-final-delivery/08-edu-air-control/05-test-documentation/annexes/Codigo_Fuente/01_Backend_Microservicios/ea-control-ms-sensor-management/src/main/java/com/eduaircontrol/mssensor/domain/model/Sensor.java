package com.eduaircontrol.mssensor.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sensor", schema = "sensors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sensor_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "serial_number", nullable = false, unique = true, length = 60)
    private String serialNumber;

    @Column(name = "sensor_model_id", nullable = false)
    private UUID sensorModelId;

    @Column(name = "sensor_status_id", nullable = false)
    private UUID sensorStatusId;

    @Column(name = "institution_id")
    private UUID institutionId;

    @Column(name = "last_seen_at")
    private Instant lastSeenAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
