-- Historial de busquedas del usuario con filtros aplicados.
-- user_id: referencia logica al servicio IAM.
CREATE TABLE user_experience.searches (
    search_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    search_text VARCHAR(500),
    applied_filter VARCHAR(500),
    searched_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
