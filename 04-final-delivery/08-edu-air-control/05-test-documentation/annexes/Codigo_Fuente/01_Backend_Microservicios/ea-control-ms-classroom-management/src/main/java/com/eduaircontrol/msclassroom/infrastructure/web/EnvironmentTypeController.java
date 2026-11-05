package com.eduaircontrol.msclassroom.infrastructure.web;

import com.eduaircontrol.msclassroom.application.EnvironmentTypeService;
import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.model.EnvironmentType;
import com.eduaircontrol.msclassroom.infrastructure.web.dto.EnvironmentTypeCreateRequest;
import com.eduaircontrol.msclassroom.infrastructure.web.dto.EnvironmentTypeResponse;
import com.eduaircontrol.msclassroom.infrastructure.web.dto.EnvironmentTypeUpdateRequest;
import com.eduaircontrol.msclassroom.infrastructure.web.dto.PageResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/environment-types")
@RequiredArgsConstructor
public class EnvironmentTypeController {

    private final EnvironmentTypeService environmentTypeService;

    @GetMapping
    public PageResponse<EnvironmentTypeResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<EnvironmentType> result = environmentTypeService.list(query, page, limit);
        return PageResponse.of(result, EnvironmentTypeResponse::from);
    }

    @GetMapping("/{id}")
    public EnvironmentTypeResponse get(@PathVariable UUID id) {
        return EnvironmentTypeResponse.from(environmentTypeService.get(id));
    }

    @PostMapping
    public ResponseEntity<EnvironmentTypeResponse> create(
            @Valid @RequestBody EnvironmentTypeCreateRequest request) {
        EnvironmentType type = environmentTypeService.create(request.code(), request.name(), request.description());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(type.getId())
                .toUri();
        return ResponseEntity.created(location).body(EnvironmentTypeResponse.from(type));
    }

    @PatchMapping("/{id}")
    public EnvironmentTypeResponse update(
            @PathVariable UUID id, @Valid @RequestBody EnvironmentTypeUpdateRequest request) {
        return EnvironmentTypeResponse.from(environmentTypeService.update(
                id, request.code(), request.name(), request.description()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        environmentTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
