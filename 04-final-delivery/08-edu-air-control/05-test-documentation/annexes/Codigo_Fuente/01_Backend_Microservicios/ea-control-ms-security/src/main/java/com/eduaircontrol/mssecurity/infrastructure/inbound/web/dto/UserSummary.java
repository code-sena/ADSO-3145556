package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

import java.util.List;
import java.util.UUID;

public record UserSummary(UUID id, String email, String username, List<String> roles,
        List<String> permissions, UUID institutionId, UUID campusId) {
}
