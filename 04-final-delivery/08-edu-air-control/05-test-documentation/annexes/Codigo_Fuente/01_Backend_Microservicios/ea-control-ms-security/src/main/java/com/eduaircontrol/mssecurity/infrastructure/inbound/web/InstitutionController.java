package com.eduaircontrol.mssecurity.infrastructure.inbound.web;

import com.eduaircontrol.mssecurity.application.InstitutionService;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.InstitutionCreateRequest;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.InstitutionResponse;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.InstitutionUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gestión de instituciones (tenants). Restringido a SUPER_ADMIN (ADR-016).
 */
@RestController
@RequestMapping("/api/v1/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @GetMapping
    public List<InstitutionResponse> list() {
        return institutionService.list().stream().map(InstitutionResponse::from).toList();
    }

    @GetMapping("/{id}")
    public InstitutionResponse get(@PathVariable UUID id) {
        return InstitutionResponse.from(institutionService.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstitutionResponse create(@Valid @RequestBody InstitutionCreateRequest request) {
        return InstitutionResponse.from(
                institutionService.create(request.code(), request.name(), request.type()));
    }

    @PutMapping("/{id}")
    public InstitutionResponse update(@PathVariable UUID id,
            @Valid @RequestBody InstitutionUpdateRequest request) {
        return InstitutionResponse.from(
                institutionService.update(id, request.name(), request.type(), request.status()));
    }
}
