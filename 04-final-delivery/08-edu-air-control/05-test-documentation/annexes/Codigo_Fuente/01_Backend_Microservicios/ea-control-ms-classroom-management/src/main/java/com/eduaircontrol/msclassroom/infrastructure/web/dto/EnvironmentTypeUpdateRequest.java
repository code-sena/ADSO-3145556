package com.eduaircontrol.msclassroom.infrastructure.web.dto;

public record EnvironmentTypeUpdateRequest(
        String code,
        String name,
        String description) {
}
