package com.eduaircontrol.mssecurity.application;

import java.util.List;
import java.util.UUID;

public record AuthResult(
        String accessToken,
        String refreshToken,
        long expiresIn,
        UUID userId,
        String email,
        String username,
        List<String> roles,
        UUID institutionId,
        UUID campusId) {
}
