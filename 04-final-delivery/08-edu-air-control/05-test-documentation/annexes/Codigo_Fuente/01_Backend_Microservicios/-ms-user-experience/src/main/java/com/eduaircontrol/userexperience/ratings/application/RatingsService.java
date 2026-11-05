package com.eduaircontrol.userexperience.ratings.application;

import com.eduaircontrol.userexperience.ratings.domain.model.ClassroomRating;
import com.eduaircontrol.userexperience.ratings.infrastructure.persistence.ClassroomRatingJpaRepository;
import java.time.Clock;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Calificaciones de ambientes (escala 1-5).
 */
@Service
@RequiredArgsConstructor
public class RatingsService {

    private final ClassroomRatingJpaRepository ratingRepository;
    private final Clock clock;

    @Transactional
    public ClassroomRating rate(UUID userId, UUID classroomId, int score, String comment) {
        requireUser(userId);
        if (classroomId == null) {
            throw new IllegalArgumentException("classroomId es obligatorio");
        }
        return ratingRepository.save(
                ClassroomRating.rate(userId, classroomId, score, comment, clock.instant()));
    }

    @Transactional(readOnly = true)
    public List<ClassroomRating> byUser(UUID userId) {
        requireUser(userId);
        return ratingRepository.findByUserIdOrderByRatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<ClassroomRating> byClassroom(UUID classroomId) {
        if (classroomId == null) {
            throw new IllegalArgumentException("classroomId es obligatorio");
        }
        return ratingRepository.findByClassroomIdOrderByRatedAtDesc(classroomId);
    }

    private void requireUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId es obligatorio");
        }
    }
}
