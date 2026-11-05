package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.application.SensorModelService;
import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorModel;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorModelCreateRequest;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorModelResponse;
import com.eduaircontrol.mssensor.infrastructure.web.dto.PageResponse;
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
@RequestMapping("/api/v1/sensor-models")
@RequiredArgsConstructor
public class SensorModelController {

    private final SensorModelService service;

    @GetMapping
    public PageResponse<SensorModelResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<SensorModel> result = service.list(query, page, limit);
        return PageResponse.of(result, SensorModelResponse::from);
    }

    @GetMapping("/{id}")
    public SensorModelResponse get(@PathVariable UUID id) {
        return SensorModelResponse.from(service.get(id));
    }

    @PostMapping
    public ResponseEntity<SensorModelResponse> create(@Valid @RequestBody SensorModelCreateRequest request) {
        SensorModel item = service.create(request.code(), request.name(), request.description());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.getId())
                .toUri();
        return ResponseEntity.created(location).body(SensorModelResponse.from(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
