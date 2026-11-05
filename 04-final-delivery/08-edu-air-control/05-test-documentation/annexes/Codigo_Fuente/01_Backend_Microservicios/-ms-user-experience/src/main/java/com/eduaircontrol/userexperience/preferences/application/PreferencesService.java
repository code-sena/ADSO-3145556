package com.eduaircontrol.userexperience.preferences.application;

import com.eduaircontrol.userexperience.preferences.domain.model.UserPreference;
import com.eduaircontrol.userexperience.preferences.infrastructure.persistence.UserPreferenceJpaRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Preferencias del usuario. Si no existen, se crean con valores por defecto al
 * primer acceso.
 */
@Service
@RequiredArgsConstructor
public class PreferencesService {

    private final UserPreferenceJpaRepository preferenceRepository;

    @Transactional(readOnly = true)
    public UserPreference get(UUID userId) {
        requireUser(userId);
        return requirePreference(userId);
    }

    @Transactional
    public UserPreference save(UUID userId, PreferencesUpdate update) {
        requireUser(userId);
        UserPreference preference = requirePreference(userId);
        if (update.language() != null) {
            preference.setLanguage(update.language());
        }
        if (update.dateFormat() != null) {
            preference.setDateFormat(update.dateFormat());
        }
        if (update.manualTimezone() != null) {
            preference.setTimeZone(update.manualTimezone());
        }
        if (update.reminders() != null) {
            preference.setReminders(update.reminders());
        }
        if (update.colorTheme() != null) {
            preference.setColorTheme(update.colorTheme());
        }
        if (update.darkMode() != null) {
            preference.setDarkMode(update.darkMode());
        }
        if (update.autoTimezone() != null) {
            preference.setAutoTimeZone(update.autoTimezone());
        }
        if (update.notificationsEnabled() != null) {
            preference.setNotificationsEnabled(update.notificationsEnabled());
        }
        return preferenceRepository.save(preference);
    }

    private void requireUser(UUID userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion requerida");
        }
    }

    private UserPreference requirePreference(UUID userId) {
        return preferenceRepository.findByUserId(userId).orElseGet(() -> {
            UserPreference preference = new UserPreference();
            preference.setId(UUID.randomUUID());
            preference.setUserId(userId);
            preference.setLanguage("es");
            preference.setDateFormat("DD-MM-YYYY");
            preference.setNotificationsEnabled(true);
            preference.setAutoTimeZone(true);
            preference.setDarkMode(false);
            preference.setColorTheme("");
            preference.setTimeZone("America/Lima");
            preference.setReminders(new LinkedHashMap<>(Map.of(
                    "alertas", true,
                    "advertencias", true,
                    "resumenDiario", false,
                    "sonido", true)));
            return preferenceRepository.save(preference);
        });
    }

    public record PreferencesUpdate(
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
