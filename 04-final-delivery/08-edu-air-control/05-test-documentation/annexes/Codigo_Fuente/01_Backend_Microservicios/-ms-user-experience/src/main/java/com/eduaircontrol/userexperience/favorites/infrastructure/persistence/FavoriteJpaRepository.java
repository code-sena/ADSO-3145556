package com.eduaircontrol.userexperience.favorites.infrastructure.persistence;

import com.eduaircontrol.userexperience.favorites.domain.model.Favorite;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, UUID> {

    List<Favorite> findByUserId(UUID userId);

    Optional<Favorite> findByUserIdAndClassroomId(UUID userId, UUID classroomId);

    void deleteByUserIdAndClassroomId(UUID userId, UUID classroomId);
}
