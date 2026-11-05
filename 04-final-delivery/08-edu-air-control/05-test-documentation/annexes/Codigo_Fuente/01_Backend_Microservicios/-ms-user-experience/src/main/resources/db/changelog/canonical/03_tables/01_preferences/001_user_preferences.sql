-- Preferencias de interfaz y notificaciones por usuario.
-- user_id: referencia logica al servicio IAM. Un registro por usuario.
CREATE TABLE user_experience.user_preferences (
    preference_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
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
