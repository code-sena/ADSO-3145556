package com.eduaircontrol.userexperience.ratings.infrastructure.web;

import com.eduaircontrol.userexperience.ratings.application.RatingsService;
import com.eduaircontrol.userexperience.ratings.domain.model.ClassroomRating;
import com.eduaircontrol.userexperience.shared.contract.UserIdentityPort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Calificaciones de ambientes.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RatingsController {

    private final RatingsService ratingsService;
    private final UserIdentityPort userIdentityPort;

    @PostMapping("/classrooms/{classroomId}/ratings")
    public ClassroomRating rate(@PathVariable UUID classroomId,
                                @Valid @RequestBody RateRequest request) {
        return ratingsService.rate(currentUserId(), classroomId,
                request.score(), request.comment());
    }

    @GetMapping("/ratings")
    public List<ClassroomRating> mine() {
        return ratingsService.byUser(currentUserId());
    }

    private UUID currentUserId() {
        return userIdentityPort.currentUserId()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Sesion requerida"));
    }

    public record RateRequest(
            @NotNull @Min(1) @Max(5) Integer score,
            String comment) {
    }
}
