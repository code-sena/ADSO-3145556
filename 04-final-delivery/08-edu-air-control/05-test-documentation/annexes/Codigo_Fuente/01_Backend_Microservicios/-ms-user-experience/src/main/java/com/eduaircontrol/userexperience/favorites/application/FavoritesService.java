package com.eduaircontrol.userexperience.favorites.application;

import com.eduaircontrol.userexperience.favorites.domain.model.Favorite;
import com.eduaircontrol.userexperience.favorites.infrastructure.persistence.FavoriteJpaRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Favoritos del usuario: ambientes y/o variables.
 */
@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoriteJpaRepository favoriteRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public Set<UUID> favoriteIds(UUID userId) {
        if (userId == null) {
            return Set.of();
        }
        return favoriteRepository.findByUserId(userId).stream()
                .map(Favorite::getClassroomId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Transactional
    public boolean toggle(UUID userId, UUID environmentId) {
        if (userId == null || environmentId == null) {
            throw new IllegalArgumentException("userId y environmentId son obligatorios");
        }
        boolean favorite = !isFavorite(userId, environmentId);
        setFavorite(userId, environmentId, favorite);
        return favorite;
    }

    @Transactional
    public void setFavorite(UUID userId, UUID environmentId, boolean favorite) {
        if (userId == null || environmentId == null) {
            return;
        }
        if (favorite) {
            favoriteRepository.findByUserIdAndClassroomId(userId, environmentId)
                    .orElseGet(() -> favoriteRepository.save(Favorite.builder()
                            .id(UUID.randomUUID())
                            .userId(userId)
                            .classroomId(environmentId)
                            .addedAt(clock.instant())
                            .build()));
            return;
        }
        favoriteRepository.deleteByUserIdAndClassroomId(userId, environmentId);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(UUID userId, UUID environmentId) {
        return userId != null && environmentId != null
                && favoriteRepository.findByUserIdAndClassroomId(userId, environmentId).isPresent();
    }
}
