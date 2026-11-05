package com.eduaircontrol.userexperience.infrastructure.backfill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Copia los datos de experiencia de usuario del monolito al esquema propio.
 *
 * <p>Las tablas son 1:1 ({@code ux.*} a {@code user_experience.*}) porque el
 * servicio nacio de extraer el modulo. La copia es idempotente por clave primaria:
 * repetirla no duplica filas.
 *
 * <p>El acoplamiento es temporal: una vez copiado el historico, la unica via son
 * los endpoints del servicio.
 */
@Slf4j
@Service
public class UxBackfillService {

    private final JdbcTemplate jdbc;

    public UxBackfillService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Copia todas las tablas y devuelve el total de filas escritas. */
    public int copyAll() {
        int total = 0;
        total += copyUserPreferences();
        total += copyFavorites();
        total += copyRatings();
        total += copySearches();
        total += copyNotificationState();
        log.info("Backfill UX completo: {} fila(s) copiadas", total);
        return total;
    }

    private int copyUserPreferences() {
        return jdbc.update("""
                INSERT INTO user_experience.user_preferences
                    (preference_id, user_id, language, color_theme,
                     notifications_enabled, time_zone, date_format, reminders,
                     dark_mode, auto_time_zone)
                SELECT preference_id, user_id, language, color_theme,
                       notifications_enabled, time_zone, date_format, reminders,
                       dark_mode, auto_time_zone
                FROM ux.user_preferences
                ON CONFLICT (preference_id) DO NOTHING
                """);
    }

    private int copyFavorites() {
        return jdbc.update("""
                INSERT INTO user_experience.favorites
                    (favorite_id, user_id, classroom_id, variable_id, added_at)
                SELECT favorite_id, user_id, classroom_id, variable_id, added_at
                FROM ux.favorites
                ON CONFLICT (favorite_id) DO NOTHING
                """);
    }

    private int copyRatings() {
        return jdbc.update("""
                INSERT INTO user_experience.classroom_ratings
                    (rating_id, user_id, classroom_id, score, comment, rated_at)
                SELECT rating_id, user_id, classroom_id, score, comment, rated_at
                FROM ux.classroom_ratings
                ON CONFLICT (rating_id) DO NOTHING
                """);
    }

    private int copySearches() {
        return jdbc.update("""
                INSERT INTO user_experience.searches
                    (search_id, user_id, search_text, applied_filter, searched_at)
                SELECT search_id, user_id, search_text, applied_filter, searched_at
                FROM ux.searches
                ON CONFLICT (search_id) DO NOTHING
                """);
    }

    private int copyNotificationState() {
        return jdbc.update("""
                INSERT INTO user_experience.notification_state
                    (user_id, alert_id, read_at)
                SELECT user_id, alert_id, read_at
                FROM ux.notification_state
                ON CONFLICT (user_id, alert_id) DO NOTHING
                """);
    }
}
