package com.eduaircontrol.msusermanagement.infrastructure.web.dto;

import com.eduaircontrol.msusermanagement.domain.model.RecordStatus;
import com.eduaircontrol.msusermanagement.domain.model.UserProfile;
import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        UUID userId,
        String fullName,
        String phone,
        String department,
        String position,
        String locale,
        String avatarUrl,
        RecordStatus status,
        Instant createdAt,
        Instant updatedAt) {

    public static UserProfileResponse from(UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getFullName(),
                profile.getPhone(),
                profile.getDepartment(),
                profile.getPosition(),
                profile.getLocale(),
                profile.getAvatarUrl(),
                profile.getStatus(),
                profile.getCreatedAt(),
                profile.getUpdatedAt());
    }
}
