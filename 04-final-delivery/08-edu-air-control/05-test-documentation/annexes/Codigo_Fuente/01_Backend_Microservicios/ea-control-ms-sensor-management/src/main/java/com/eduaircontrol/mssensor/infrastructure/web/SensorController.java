package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.application.SensorService;
import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.Sensor;
import com.eduaircontrol.mssensor.infrastructure.web.dto.PageResponse;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorCreateRequest;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorResponse;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorUpdateRequest;
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
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @GetMapping
    public PageResponse<SensorResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "sensorModelId", required = false) UUID sensorModelId,
            @RequestParam(name = "sensorStatusId", required = false) UUID sensorStatusId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<Sensor> result = sensorService.list(query, sensorModelId, sensorStatusId, page, limit);
        return PageResponse.of(result, SensorResponse::from);
    }

    @GetMapping("/{id}")
    public SensorResponse get(@PathVariable UUID id) {
        return SensorResponse.from(sensorService.get(id));
    }

    @PostMapping
    public ResponseEntity<SensorResponse> create(@Valid @RequestBody SensorCreateRequest request) {
        Sensor sensor = sensorService.create(
                request.serialNumber(), request.sensorModelId(), request.sensorStatusId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(sensor.getId())
                .toUri();
        return ResponseEntity.created(location).body(SensorResponse.from(sensor));
    }

    @PatchMapping("/{id}")
    public SensorResponse update(@PathVariable UUID id, @Valid @RequestBody SensorUpdateRequest request) {
        return SensorResponse.from(sensorService.update(
                id, request.serialNumber(), request.sensorModelId(),
                request.sensorStatusId(), request.lastSeenAt()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        sensorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
