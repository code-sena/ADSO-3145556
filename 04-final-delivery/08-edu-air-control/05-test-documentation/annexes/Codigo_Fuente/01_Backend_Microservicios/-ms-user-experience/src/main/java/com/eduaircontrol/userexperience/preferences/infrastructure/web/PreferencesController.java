package com.eduaircontrol.userexperience.preferences.infrastructure.web;

import com.eduaircontrol.userexperience.preferences.application.PreferencesService;
import com.eduaircontrol.userexperience.preferences.domain.model.UserPreference;
import com.eduaircontrol.userexperience.shared.contract.UserIdentityPort;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Preferencias del usuario autenticado. El usuario siempre sale del token.
 */
@RestController
@RequestMapping("/api/v1/preferences")
@RequiredArgsConstructor
public class PreferencesController {

    private final PreferencesService preferencesService;
    private final UserIdentityPort userIdentityPort;

    @GetMapping
    public UserPreference get() {
        return preferencesService.get(currentUserId());
    }

    @PutMapping
    public UserPreference save(@Valid @RequestBody PreferencesUpdateRequest request) {
        return preferencesService.save(currentUserId(), new PreferencesService.PreferencesUpdate(
                request.language(), request.dateFormat(), request.manualTimezone(),
                request.reminders(), request.colorTheme(), request.darkMode(),
                request.autoTimezone(), request.notificationsEnabled()));
    }

    private UUID currentUserId() {
        return userIdentityPort.currentUserId()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Sesion requerida"));
    }

    public record PreferencesUpdateRequest(
            String language,
            String dateFormat,
            String manualTimezone,
            Map<String, Boolean> reminders,
            String colorTheme,
            Boolean darkMode,
            Boolean autoTimezone,
            Boolean notificationsEnabled) {
    }
}
