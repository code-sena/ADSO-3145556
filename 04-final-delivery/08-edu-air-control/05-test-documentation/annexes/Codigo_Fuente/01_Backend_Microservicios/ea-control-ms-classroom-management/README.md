# ms-classroom-management

> Servicio dueño del dominio **Classrooms** (gestión de espacios educativos):
> `campuses`, `environment_types` y `educational_environment` en el esquema `classrooms`.

Catálogo: [service-catalog](https://github.com/code-sena/ea-control-docs/blob/main/09-microservices/service-catalog.md) — #03, puerto **3002**, PostgreSQL.

## Responsabilidad

- CRUD de campus, tipos de ambiente y ambientes educativos (espacios físicos).
- Reglas del dominio: unicidad de códigos, transiciones ACTIVE/INACTIVE (§6), soft delete vía `deleted_at` (§7), CHECKs de piso/área/capacidad (§5).
- Dueño exclusivo del esquema `classrooms`; otros dominios lo referencian solo por API/eventos (ADR-003).

## Fuera de alcance

- No gestiona usuarios/roles (ms-security) ni preferencias/favoritos (ms-user-experience).
- No mide ni analiza variables ambientales (ms-sensor-management / ms-environment-monitoring).

## Arquitectura

Hexagonal (ADR-009): `domain` → `application` (puertos + casos de uso) → `infrastructure` (adapters inbound/outbound).

```text
src/main/java/com/eduaircontrol/msclassroom/
├── domain/           modelo y excepciones de negocio
├── application/      casos de uso y puertos (PageResult + repositorios)
└── infrastructure/
    ├── inbound/web/  controllers REST, DTOs, manejo de errores
    ├── outbound/     adaptadores JPA (Specifications + paginación)
    └── security/     JWT interino HS256 (ver decisions.md)
```

## Migraciones

Liquibase: `src/main/resources/db/changelog/` (master + changes por dominio, patrón §12 de
`06-data/domain/ms-classroom-management.md`). Se ejecutan automáticamente al arrancar.

## Cómo correrlo localmente

```bash
# Con Docker (incluye su propio PostgreSQL en el puerto 5433)
docker compose up -d

# Verificar
curl http://localhost:3002/health

# Desarrollo sin Docker (necesita un PostgreSQL con la variable POSTGRES_URL)
source <(grep -E '^POSTGRES_(URL|USER|PASSWORD)=' .env)   # o exportarlas
./mvnw spring-boot:run
```

Swagger: `http://localhost:3002/swagger-ui.html`

## Tests

```bash
./mvnw verify
```

Tests de controller contra H2 (Liquibase desactivado, `ddl-auto=create-drop`, esquema `classrooms`).

## API

Contrato OpenAPI: `07-api/contracts/openapi/ms-classroom-management.yaml` en
[ea-control-docs](https://github.com/code-sena/ea-control-docs).

- `GET /health` — público
- `GET|POST /api/v1/campuses` · `GET|PATCH|DELETE /api/v1/campuses/{id}`
- `GET|POST /api/v1/environment-types` · `GET|PATCH|DELETE /api/v1/environment-types/{id}`
- `GET|POST /api/v1/educational-environments` · `GET|PATCH|DELETE /api/v1/educational-environments/{id}`

Lecturas: cualquier token válido. Escrituras: rol `ADMIN` (interino, ver decisions.md).

## Documentación relacionada

- `docs-ea-control/09-microservices/services/03-ms-classroom-management/` — README de servicio, data-model, events, decisions, runbook
- `docs-ea-control/06-data/domain/ms-classroom-management.md` — modelo canónico del dominio
- `EduAirControl/database/negocio/` — implementación de referencia del modelo global
