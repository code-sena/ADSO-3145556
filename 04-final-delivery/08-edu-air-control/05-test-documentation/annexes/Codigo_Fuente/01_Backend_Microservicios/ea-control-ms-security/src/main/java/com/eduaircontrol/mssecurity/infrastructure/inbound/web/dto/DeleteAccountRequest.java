package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteAccountRequest(@NotBlank String password) {
}
