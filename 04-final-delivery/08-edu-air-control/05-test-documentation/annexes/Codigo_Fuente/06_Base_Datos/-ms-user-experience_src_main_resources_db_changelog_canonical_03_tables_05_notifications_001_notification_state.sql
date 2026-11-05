-- Estado de lectura de notificaciones por usuario y alerta.
-- Permite marcar alertas como leidas sin duplicar. La alerta en si vive en
-- ms-environment-monitoring; aqui solo se guarda la interaccion del usuario.
CREATE TABLE user_experience.notification_state (
    user_id UUID NOT NULL,
    alert_id UUID NOT NULL,
    read_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, alert_id)
);
