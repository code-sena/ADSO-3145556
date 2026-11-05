package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.application.SensorVariableService;
import com.eduaircontrol.mssensor.domain.model.SensorVariable;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorVariableRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
@RequestMapping("/api/v1/sensor-variables")
@RequiredArgsConstructor
public class SensorVariableController {

    private final SensorVariableService sensorVariableService;

    @GetMapping
    public List<SensorVariable> list(@RequestParam(name = "sensorId", required = false) UUID sensorId) {
        return sensorVariableService.list(sensorId);
    }

    @PostMapping
    public ResponseEntity<SensorVariable> create(
            @Valid @RequestBody SensorVariableRequest request) {
        SensorVariable association = sensorVariableService.associate(
                request.sensorId(), request.variableId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location).body(association);
    }

    @DeleteMapping("/{sensorId}/{variableId}")
    public ResponseEntity<Void> delete(@PathVariable UUID sensorId, @PathVariable UUID variableId) {
        sensorVariableService.dissociate(sensorId, variableId);
        return ResponseEntity.noContent().build();
    }
}
