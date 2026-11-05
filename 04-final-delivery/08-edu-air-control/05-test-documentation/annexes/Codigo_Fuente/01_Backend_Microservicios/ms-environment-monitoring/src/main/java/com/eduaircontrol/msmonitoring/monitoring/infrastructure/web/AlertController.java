package com.eduaircontrol.msmonitoring.monitoring.infrastructure.web;

import com.eduaircontrol.msmonitoring.monitoring.domain.port.in.AlertUseCase;
import com.eduaircontrol.msmonitoring.shared.contract.UserIdentityPort;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API de alertas.
 */
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertUseCase alertUseCase;
    private final UserIdentityPort userIdentityPort;

    /** Lista alertas con filtros y paginacion. */
    @GetMapping
    public List<AlertUseCase.AlertView> list(
            @RequestParam(name = "environment", required = false) UUID environmentId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "from", required = false) Instant from,
            @RequestParam(name = "to", required = false) Instant to,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return alertUseCase.list(new AlertUseCase.AlertQuery(
                environmentId, status, from, to, page, limit));
    }

    /** Reconoce una alerta activa. El usuario sale del token. */
    @PostMapping("/{id}/acknowledge")
    public AlertUseCase.AlertView acknowledge(@PathVariable UUID id) {
        UUID userId = userIdentityPort.currentUserId().orElse(null);
        return alertUseCase.acknowledge(id, userId, Instant.now());
    }
}
