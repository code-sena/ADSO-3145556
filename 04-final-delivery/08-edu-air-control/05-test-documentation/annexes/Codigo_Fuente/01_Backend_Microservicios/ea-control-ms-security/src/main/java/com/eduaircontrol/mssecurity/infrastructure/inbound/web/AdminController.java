package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import com.eduaircontrol.mssecurity.application.AdminService;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.UserSummary;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Panel de administracion global (solo SUPER_ADMIN). Permite gestionar
 * usuarios, roles e instituciones.
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public List<UserSummary> listUsers() {
        return adminService.listUsers();
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<Void> assignRoles(@PathVariable UUID id,
            @Valid @RequestBody AssignRolesRequest request) {
        adminService.assignRoles(id, request.roleNames());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public List<RoleResponse> listRoles() {
        return adminService.listRoles().stream()
                .map(r -> new RoleResponse(r.getName(), r.getDescription()))
                .toList();
    }

    public record AssignRolesRequest(@NotNull List<String> roleNames) {
    }

    public record RoleResponse(String name, String description) {
    }
}
