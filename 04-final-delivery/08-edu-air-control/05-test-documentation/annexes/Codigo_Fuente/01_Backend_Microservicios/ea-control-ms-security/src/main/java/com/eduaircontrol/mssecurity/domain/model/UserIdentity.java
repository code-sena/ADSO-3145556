package com.eduaircontrol.mssecurity.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Vinculo entre un usuario local y una cuenta social (Google, Facebook).
 * Permite login social sin duplicar usuarios.
 */
@Entity
@Table(name = "user_identity", schema = "seguridad", uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_identity_provider",
                columnNames = {"provider", "provider_user_id"}),
        @UniqueConstraint(name = "uq_user_identity_user_provider",
                columnNames = {"user_id", "provider"})
})
@Getter
@Setter
@NoArgsConstructor
public class UserIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "provider", nullable = false, length = 30)
    private String provider;

    @Column(name = "provider_user_id", nullable = false, length = 255)
    private String providerUserId;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "display_name", length = 255)
    private String displayName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public UserIdentity(UUID userId, String provider, String providerUserId,
                        String email, String displayName, String avatarUrl) {
        this.userId = userId;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.email = email;
        this.displayName = displayName;
        this.avatarUrl = avatarUrl;
        this.createdAt = Instant.now();
    }
}
