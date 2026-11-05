package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.application.VariableService;
import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.Variable;
import com.eduaircontrol.mssensor.infrastructure.web.dto.PageResponse;
import com.eduaircontrol.mssensor.infrastructure.web.dto.VariableCreateRequest;
import com.eduaircontrol.mssensor.infrastructure.web.dto.VariableResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/variables")
@RequiredArgsConstructor
public class VariableController {

    private final VariableService variableService;

    @GetMapping
    public PageResponse<VariableResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<Variable> result = variableService.list(query, page, limit);
        return PageResponse.of(result, VariableResponse::from);
    }

    @GetMapping("/{id}")
    public VariableResponse get(@PathVariable UUID id) {
        return VariableResponse.from(variableService.get(id));
    }

    @PostMapping
    public ResponseEntity<VariableResponse> create(@Valid @RequestBody VariableCreateRequest request) {
        Variable variable = variableService.create(
                request.code(), request.name(), request.measurementUnitId(), request.description());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(variable.getId())
                .toUri();
        return ResponseEntity.created(location).body(VariableResponse.from(variable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        variableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
