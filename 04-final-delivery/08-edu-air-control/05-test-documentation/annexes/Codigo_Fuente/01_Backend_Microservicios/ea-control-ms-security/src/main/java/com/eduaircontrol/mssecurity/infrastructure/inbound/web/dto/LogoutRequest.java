package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

public record LogoutRequest(String refreshToken, Boolean allDevices) {
}
