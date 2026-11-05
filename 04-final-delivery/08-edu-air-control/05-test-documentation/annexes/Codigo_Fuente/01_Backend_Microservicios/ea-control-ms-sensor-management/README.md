# ms-sensor-management

Servicio de registro y gestión de sensores (dominio **sensors** de EduAirControl).

- Spring Boot 4.0.5 / Java 17 — arquitectura hexagonal (ADR-009)
- PostgreSQL 15 con esquema propio `sensors` (ADR-003), migraciones Liquibase (ADR-008)
- Seguridad JWT interina HS256 (mismo `jwt.secret` que el monolito; ADR-006 pendiente)
- Puerto **3004**, Swagger UI en `/swagger-ui.html`, health en `/health`

## Endpoints

| Método | Ruta | Auth | Notas |
|--------|------|------|-------|
| GET | `/api/v1/sensors` | GET autenticado | filtros `q`, `sensorModelId`, `sensorStatusId`, paginado |
| GET/POST/PATCH/DELETE | `/api/v1/sensors/{id}` | POST/PATCH/DELETE = ADMIN | `DELETE` físico (409 si tiene historial de instalaciones) |
| GET/POST/DELETE | `/api/v1/variables` | POST/DELETE = ADMIN | sin PATCH (la tabla de referencia no tiene `updated_at`) |
| GET/POST | `/api/v1/sensor-installations` | POST = ADMIN | 409 si hay instalación solapada |
| POST | `/api/v1/sensor-installations/{id}/remove` | ADMIN | cierra con `removedAt` (sin borrado físico) |
| GET/POST/DELETE | `/api/v1/sensor-variables` | POST/DELETE = ADMIN | `DELETE /{sensorId}/{variableId}` → 204 |

IDs lógicos entre dominios y a catálogos excluidos (`sensorModelId`, `sensorStatusId`,
`measurementUnitId`, `educationalEnvironmentId`) se validan **solo como UUID** — ver
`docs-ea-control/09-microservices/services/04-ms-sensor-management/decisions.md`.

## Ejecutar

```bash
# local (necesita PostgreSQL con la BD eduaircontrol_sensors)
./mvnw spring-boot:run

# o stack completo
docker compose up --build
```

## Pruebas

```bash
./mvnw verify        # 34 tests: unit + controllers sobre H2
```
