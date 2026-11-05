package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

public record AuthResponse(String accessToken, String refreshToken, long expiresIn, UserSummary user) {
}
