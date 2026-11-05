package com.eduaircontrol.userexperience.searches.infrastructure.web;

import com.eduaircontrol.userexperience.searches.application.SearchesService;
import com.eduaircontrol.userexperience.searches.domain.model.Search;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Historial de busquedas del usuario autenticado.
 */
@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
public class SearchesController {

    private final SearchesService searchesService;
    private final UserIdentityPort userIdentityPort;

    @GetMapping
    public List<Search> list(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return searchesService.recent(currentUserId(), page, limit);
    }

    @PostMapping
    public Search record(@RequestBody SearchRequest request) {
        return searchesService.record(currentUserId(),
                request == null ? null : request.searchText(),
                request == null ? null : request.appliedFilter());
    }

    @DeleteMapping("/{searchId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID searchId) {
        searchesService.delete(currentUserId(), searchId);
        return ResponseEntity.ok(Map.of("message", "Busqueda eliminada"));
    }

    private UUID currentUserId() {
        return userIdentityPort.currentUserId()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Sesion requerida"));
    }

    public record SearchRequest(String searchText, String appliedFilter) {
    }
}
