package com.eduaircontrol.userexperience.favorites.infrastructure.web;

import com.eduaircontrol.userexperience.favorites.application.FavoritesService;
import com.eduaircontrol.userexperience.shared.contract.UserIdentityPort;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Favoritos del usuario autenticado.
 */
@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final UserIdentityPort userIdentityPort;

    @GetMapping
    public List<UUID> list() {
        return favoritesService.favoriteIds(currentUserId()).stream().sorted().toList();
    }

    @PostMapping("/{environmentId}/toggle")
    public Map<String, Object> toggle(@PathVariable UUID environmentId) {
        UUID userId = currentUserId();
        boolean favorite = favoritesService.toggle(userId, environmentId);
        return Map.of("environmentId", environmentId, "favorite", favorite);
    }

    @DeleteMapping("/{environmentId}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable UUID environmentId) {
        favoritesService.setFavorite(currentUserId(), environmentId, false);
        return ResponseEntity.ok(Map.of("message", "Favorito eliminado"));
    }

    private UUID currentUserId() {
        return userIdentityPort.currentUserId()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Sesion requerida"));
    }
}
