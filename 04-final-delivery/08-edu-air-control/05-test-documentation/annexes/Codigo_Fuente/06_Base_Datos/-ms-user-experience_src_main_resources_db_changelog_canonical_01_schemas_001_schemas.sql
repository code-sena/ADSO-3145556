-- Esquema propio del microservicio (ADR-003: cada servicio tiene su namespace).
CREATE SCHEMA IF NOT EXISTS user_experience;

CREATE EXTENSION IF NOT EXISTS pgcrypto;
