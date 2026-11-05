package com.eduaircontrol.msclassroom.infrastructure.web;

import com.eduaircontrol.msclassroom.application.CampusService;
import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.model.Campus;
import com.eduaircontrol.msclassroom.domain.model.RecordStatus;
import com.eduaircontrol.msclassroom.infrastructure.web.dto.CampusCreateRequest;
import com.eduaircontrol.msclassroom.infrastructure.web.dto.CampusResponse;
import com.eduaircontrol.msclassroom.infrastructure.web.dto.CampusUpdateRequest;
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
@RequestMapping("/api/v1/campuses")
@RequiredArgsConstructor
public class CampusController {

    private final CampusService campusService;

    @GetMapping
    public PageResponse<CampusResponse> list(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "status", required = false) RecordStatus status,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        PageParams.validate(page, limit);
        PageResult<Campus> result = campusService.list(query, status, page, limit);
        return PageResponse.of(result, CampusResponse::from);
    }

    @GetMapping("/{id}")
    public CampusResponse get(@PathVariable UUID id) {
        return CampusResponse.from(campusService.get(id));
    }

    @PostMapping
    public ResponseEntity<CampusResponse> create(@Valid @RequestBody CampusCreateRequest request) {
        Campus campus = campusService.create(request.code(), request.name(), request.city(), request.status());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(campus.getId())
                .toUri();
        return ResponseEntity.created(location).body(CampusResponse.from(campus));
    }

    @PatchMapping("/{id}")
    public CampusResponse update(@PathVariable UUID id, @Valid @RequestBody CampusUpdateRequest request) {
        return CampusResponse.from(campusService.update(
                id, request.code(), request.name(), request.city(), request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        campusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
