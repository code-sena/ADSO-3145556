package com.eduaircontrol.msusermanagement.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Información de perfil de un usuario. ms-user-management es el dueño de los
 * datos de perfil; {@code userId} referencia por valor a la identidad de
 * ms-security (sin FK entre esquemas, ADR-003).
 */
@Entity
@Table(name = "user_profiles", schema = "user_management", uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_profiles_user_id", columnNames = "user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "profile_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "institution_id")
    private UUID institutionId;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(length = 30)
    private String phone;

    @Column(length = 100)
    private String department;

    @Column(name = "position", length = 100)
    private String position;

    @Column(length = 10)
    private String locale;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecordStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    void onCreate() {
        if (status == null) {
            status = RecordStatus.ACTIVE;
        }
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }
}
