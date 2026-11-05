package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.application.MeasurementUnitService;
import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.MeasurementUnit;
import com.eduaircontrol.mssensor.infrastructure.web.dto.MeasurementUnitCreateRequest;
import com.eduaircontrol.mssensor.infrastructure.web.dto.MeasurementUnitResponse;
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
@RequestMapping("/api/v1/measurement-units")
@RequiredArgsConstructor
public class MeasurementUnitController {

    private final MeasurementUnitService service;

    @GetMapping
    public PageResponse<MeasurementUnitResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<MeasurementUnit> result = service.list(query, page, limit);
        return PageResponse.of(result, MeasurementUnitResponse::from);
    }

    @GetMapping("/{id}")
    public MeasurementUnitResponse get(@PathVariable UUID id) {
        return MeasurementUnitResponse.from(service.get(id));
    }

    @PostMapping
    public ResponseEntity<MeasurementUnitResponse> create(@Valid @RequestBody MeasurementUnitCreateRequest request) {
        MeasurementUnit item = service.create(request.code(), request.symbol(), request.name());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.getId())
                .toUri();
        return ResponseEntity.created(location).body(MeasurementUnitResponse.from(item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
