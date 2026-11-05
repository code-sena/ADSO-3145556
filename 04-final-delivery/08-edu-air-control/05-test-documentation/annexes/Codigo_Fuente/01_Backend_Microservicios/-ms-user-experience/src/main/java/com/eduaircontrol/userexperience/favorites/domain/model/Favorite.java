package com.eduaircontrol.userexperience.favorites.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ambiente o variable marcada como favorita por un usuario.
 *
 * <p>Al menos un objetivo (ambiente o variable) debe estar presente.
 */
@Entity
@Table(name = "favorites", schema = "user_experience")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Id
    @Column(name = "favorite_id")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "classroom_id")
    private UUID classroomId;

    @Column(name = "variable_id")
    private UUID variableId;

    @Column(name = "added_at", nullable = false, updatable = false)
    private Instant addedAt;
}
