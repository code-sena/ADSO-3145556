package com.eduaircontrol.mssensor.infrastructure.web;

import com.eduaircontrol.mssensor.application.SensorInstallationService;
import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import com.eduaircontrol.mssensor.infrastructure.web.dto.PageResponse;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorInstallationCreateRequest;
import com.eduaircontrol.mssensor.infrastructure.web.dto.SensorInstallationResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/sensor-installations")
@RequiredArgsConstructor
public class SensorInstallationController {

    private final SensorInstallationService installationService;

    @GetMapping
    public PageResponse<SensorInstallationResponse> list(
            @RequestParam(name = "sensorId", required = false) UUID sensorId,
            @RequestParam(name = "educationalEnvironmentId", required = false) UUID educationalEnvironmentId,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<SensorInstallation> result = installationService.list(
                sensorId, educationalEnvironmentId, active, page, limit);
        return PageResponse.of(result, SensorInstallationResponse::from);
    }

    @GetMapping("/{id}")
    public SensorInstallationResponse get(@PathVariable UUID id) {
        return SensorInstallationResponse.from(installationService.get(id));
    }

    @PostMapping
    public ResponseEntity<SensorInstallationResponse> create(
            @Valid @RequestBody SensorInstallationCreateRequest request) {
        SensorInstallation installation = installationService.create(
                request.sensorId(), request.educationalEnvironmentId(), request.installedAt());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(installation.getId())
                .toUri();
        return ResponseEntity.created(location).body(SensorInstallationResponse.from(installation));
    }

    @PostMapping("/{id}/remove")
    public SensorInstallationResponse close(@PathVariable UUID id) {
        return SensorInstallationResponse.from(installationService.close(id));
    }
}
