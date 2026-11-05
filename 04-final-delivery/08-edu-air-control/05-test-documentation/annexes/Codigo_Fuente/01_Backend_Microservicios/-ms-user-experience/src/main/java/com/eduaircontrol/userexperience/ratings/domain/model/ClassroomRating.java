package com.eduaircontrol.userexperience.ratings.domain.model;

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
 * Calificacion de un ambiente por un usuario (escala 1-5).
 */
@Entity
@Table(name = "classroom_ratings", schema = "user_experience")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRating {

    @Id
    @Column(name = "rating_id")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "classroom_id", nullable = false)
    private UUID classroomId;

    @Column(nullable = false)
    private Integer score;

    @Column(length = 1000)
    private String comment;

    @Column(name = "rated_at", nullable = false, updatable = false)
    private Instant ratedAt;

    public static ClassroomRating rate(UUID userId, UUID classroomId, int score,
                                       String comment, Instant at) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("score debe estar entre 1 y 5");
        }
        return ClassroomRating.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .classroomId(classroomId)
                .score(score)
                .comment(comment)
                .ratedAt(at)
                .build();
    }
}
