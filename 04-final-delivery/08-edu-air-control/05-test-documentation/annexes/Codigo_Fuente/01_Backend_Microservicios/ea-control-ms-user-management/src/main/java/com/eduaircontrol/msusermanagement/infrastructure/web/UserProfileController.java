package com.eduaircontrol.msusermanagement.infrastructure.web;

import com.eduaircontrol.msusermanagement.application.UserProfileService;
import com.eduaircontrol.msusermanagement.domain.model.RecordStatus;
import com.eduaircontrol.msusermanagement.infrastructure.web.dto.PageResponse;
import com.eduaircontrol.msusermanagement.infrastructure.web.dto.UserProfileCreateRequest;
import com.eduaircontrol.msusermanagement.infrastructure.web.dto.UserProfileResponse;
import com.eduaircontrol.msusermanagement.infrastructure.web.dto.UserProfileUpdateRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de información de usuario bajo /api/v1/users (HU-USER-001).
 * Lectura para usuarios autenticados; escritura solo ADMIN (SecurityConfig).
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public PageResponse<UserProfileResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "status", required = false) RecordStatus status,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        return PageResponse.of(userProfileService.list(query, status, page, limit),
                UserProfileResponse::from);
    }

    @GetMapping("/{id}")
    public UserProfileResponse get(@PathVariable UUID id) {
        return UserProfileResponse.from(userProfileService.get(id));
    }

    @GetMapping("/by-user/{userId}")
    public UserProfileResponse getByUserId(@PathVariable UUID userId) {
        return UserProfileResponse.from(userProfileService.getByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<UserProfileResponse> create(@Valid @RequestBody UserProfileCreateRequest request) {
        var created = userProfileService.create(request.userId(), request.fullName(), request.phone(),
                request.department(), request.position(), request.locale(), request.avatarUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserProfileResponse.from(created));
    }

    @PutMapping("/{id}")
    public UserProfileResponse update(@PathVariable UUID id,
            @Valid @RequestBody UserProfileUpdateRequest request) {
        var updated = userProfileService.update(id, request.fullName(), request.phone(),
                request.department(), request.position(), request.locale(), request.avatarUrl(), request.status());
        return UserProfileResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
