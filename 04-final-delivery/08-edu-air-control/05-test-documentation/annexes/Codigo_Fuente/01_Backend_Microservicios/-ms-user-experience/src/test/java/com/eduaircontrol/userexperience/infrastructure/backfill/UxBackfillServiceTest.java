package com.eduaircontrol.userexperience.infrastructure.backfill;

import static org.assertj.core.api.Assertions.assertThat;

import com.eduaircontrol.userexperience.PostgresTestBase;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Verifica la copia del historico UX del esquema del monolito al propio.
 */
class UxBackfillServiceTest extends PostgresTestBase {

    @Autowired
    private UxBackfillService backfill;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void prepareSourceSchema() {
        jdbc.execute("""
                CREATE SCHEMA IF NOT EXISTS ux;

                CREATE TABLE IF NOT EXISTS ux.user_preferences (
                    preference_id UUID PRIMARY KEY,
                    user_id UUID NOT NULL UNIQUE,
                    language VARCHAR(10),
                    color_theme VARCHAR(20),
                    notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
                    time_zone VARCHAR(100),
                    date_format VARCHAR(20),
                    reminders JSONB NOT NULL DEFAULT '{}'::jsonb,
                    dark_mode BOOLEAN NOT NULL DEFAULT FALSE,
                    auto_time_zone BOOLEAN NOT NULL DEFAULT TRUE
                );

                CREATE TABLE IF NOT EXISTS ux.favorites (
                    favorite_id UUID PRIMARY KEY,
                    user_id UUID NOT NULL,
                    classroom_id UUID,
                    variable_id UUID,
                    added_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT chk_favorite_target
                        CHECK (classroom_id IS NOT NULL OR variable_id IS NOT NULL)
                );

                CREATE TABLE IF NOT EXISTS ux.classroom_ratings (
                    rating_id UUID PRIMARY KEY,
                    user_id UUID NOT NULL,
                    classroom_id UUID NOT NULL,
                    score INTEGER NOT NULL,
                    comment VARCHAR(1000),
                    rated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT chk_classroom_rating_score
                        CHECK (score BETWEEN 1 AND 5)
                );

                CREATE TABLE IF NOT EXISTS ux.searches (
                    search_id UUID PRIMARY KEY,
                    user_id UUID NOT NULL,
                    search_text VARCHAR(500),
                    applied_filter VARCHAR(500),
                    searched_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
                );

                CREATE TABLE IF NOT EXISTS ux.notification_state (
                    user_id UUID NOT NULL,
                    alert_id UUID NOT NULL,
                    read_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                    PRIMARY KEY (user_id, alert_id)
                );
                """);

        jdbc.update("DELETE FROM ux.notification_state");
        jdbc.update("DELETE FROM ux.searches");
        jdbc.update("DELETE FROM ux.classroom_ratings");
        jdbc.update("DELETE FROM ux.favorites");
        jdbc.update("DELETE FROM ux.user_preferences");
        jdbc.update("DELETE FROM user_experience.notification_state");
        jdbc.update("DELETE FROM user_experience.searches");
        jdbc.update("DELETE FROM user_experience.classroom_ratings");
        jdbc.update("DELETE FROM user_experience.favorites");
        jdbc.update("DELETE FROM user_experience.user_preferences");
    }

    @Test
    void copiesAllTables() {
        UUID userId = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO ux.user_preferences (preference_id, user_id, language)
                VALUES (?, ?, 'es')
                """, UUID.randomUUID(), userId);
        jdbc.update("""
                INSERT INTO ux.favorites (favorite_id, user_id, classroom_id)
                VALUES (?, ?, ?)
                """, UUID.randomUUID(), userId, UUID.randomUUID());
        jdbc.update("""
                INSERT INTO ux.classroom_ratings
                    (rating_id, user_id, classroom_id, score)
                VALUES (?, ?, ?, 5)
                """, UUID.randomUUID(), userId, UUID.randomUUID());
        jdbc.update("""
                INSERT INTO ux.searches (search_id, user_id, search_text)
                VALUES (?, ?, 'aula')
                """, UUID.randomUUID(), userId);
        jdbc.update("""
                INSERT INTO ux.notification_state (user_id, alert_id)
                VALUES (?, ?)
                """, userId, UUID.randomUUID());

        int total = backfill.copyAll();

        assertThat(total).isEqualTo(5);
        assertThat(count("user_experience.user_preferences")).isEqualTo(1);
        assertThat(count("user_experience.favorites")).isEqualTo(1);
        assertThat(count("user_experience.classroom_ratings")).isEqualTo(1);
        assertThat(count("user_experience.searches")).isEqualTo(1);
        assertThat(count("user_experience.notification_state")).isEqualTo(1);
    }

    @Test
    void isIdempotent() {
        UUID userId = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO ux.user_preferences (preference_id, user_id)
                VALUES (?, ?)
                """, UUID.randomUUID(), userId);

        backfill.copyAll();
        backfill.copyAll();

        assertThat(count("user_experience.user_preferences")).isEqualTo(1);
    }

    private long count(String table) {
        return jdbc.queryForObject("select count(*) from " + table, Long.class);
    }
}
