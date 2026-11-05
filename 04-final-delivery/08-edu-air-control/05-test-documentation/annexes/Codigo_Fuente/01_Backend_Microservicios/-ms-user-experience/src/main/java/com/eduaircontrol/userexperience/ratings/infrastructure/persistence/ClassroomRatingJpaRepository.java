package com.eduaircontrol.userexperience.ratings.infrastructure.persistence;

import com.eduaircontrol.userexperience.ratings.domain.model.ClassroomRating;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRatingJpaRepository extends JpaRepository<ClassroomRating, UUID> {

    List<ClassroomRating> findByUserIdOrderByRatedAtDesc(UUID userId);

    List<ClassroomRating> findByClassroomIdOrderByRatedAtDesc(UUID classroomId);
}
