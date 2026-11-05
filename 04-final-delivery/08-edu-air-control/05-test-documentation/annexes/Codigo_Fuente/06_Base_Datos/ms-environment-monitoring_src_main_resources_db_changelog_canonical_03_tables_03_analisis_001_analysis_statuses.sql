-- Catalogo del ciclo de vida de un analisis. Los UUID coinciden con los del
-- monolito para que la migracion de analisis existentes no requiera traduccion.
CREATE TABLE environment_monitoring.analysis_statuses (
    analysis_status_id UUID PRIMARY KEY,
    code               VARCHAR(20) NOT NULL UNIQUE,
    name               VARCHAR(60) NOT NULL
);
