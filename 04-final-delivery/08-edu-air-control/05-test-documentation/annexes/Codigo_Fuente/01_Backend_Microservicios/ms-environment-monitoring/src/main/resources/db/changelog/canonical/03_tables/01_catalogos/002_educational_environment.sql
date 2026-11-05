-- Replica minima de ambientes educativos (dominio classrooms).
-- El servicio no administra ambientes: solo necesita environment_type_id para
-- resolver la precedencia de umbrales y el nombre para etiquetar analisis.
CREATE TABLE environment_monitoring.educational_environment (
    educational_environment_id UUID PRIMARY KEY,
    code                       VARCHAR(30) NOT NULL,
    name                       VARCHAR(120) NOT NULL,
    campus_id                  UUID,
    environment_type_id        UUID NOT NULL,
    floor                      INTEGER
);
