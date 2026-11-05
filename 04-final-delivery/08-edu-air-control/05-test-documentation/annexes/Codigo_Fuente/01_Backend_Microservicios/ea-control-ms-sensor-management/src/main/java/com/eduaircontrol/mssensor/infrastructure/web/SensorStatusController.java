package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.application.SensorStatusService;
import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorStatus;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorStatusCreateRequest;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorStatusResponse;
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
@RequestMapping("/api/v1/sensor-statuses")
@RequiredArgsConstructor
public class SensorStatusController {

    private final SensorStatusService service;

    @GetMapping
    public PageResponse<SensorStatusResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<SensorStatus> result = service.list(query, page, limit);
        return PageResponse.of(result, SensorStatusResponse::from);
    }

    @GetMapping("/{id}")
    public SensorStatusResponse get(@PathVariable UUID id) {
        return SensorStatusResponse.from(service.get(id));
    }

    @PostMapping
    public ResponseEntity<SensorStatusResponse> create(@Valid @RequestBody SensorStatusCreateRequest request) {
        SensorStatus item = service.create(request.code(), request.name());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.getId())
                .toUri();
        return ResponseEntity.created(location).body(SensorStatusResponse.from(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
