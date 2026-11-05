# ms-user-management

Servicio dueño de la **información de perfil** de los usuarios (contexto IAM de
EduAirControl). La identidad y las credenciales pertenecen a `ms-security`; aquí solo se
administra el perfil, referenciado por `userId`.

- Spring Boot 4.0.5 / Java 17 — arquitectura hexagonal (ADR-009)
- PostgreSQL 15 con esquema propio `user_management` (ADR-003), migraciones Liquibase (ADR-008)
- JWT interino HS256 (mismo `jwt.secret` que el resto de servicios de negocio; ADR-006 pendiente)
- Puerto **3007**, Swagger UI en `/swagger-ui.html`, health en `/health`

## Endpoints

| Método | Ruta | Auth | Notas |
|--------|------|------|-------|
| GET | `/api/v1/users` | autenticado | filtros `q` (nombre/departamento), `status`, paginado `page`/`limit` |
| GET | `/api/v1/users/{id}` | autenticado | 404 si no existe o está eliminado |
| GET | `/api/v1/users/by-user/{userId}` | autenticado | perfil por la identidad de ms-security |
| POST | `/api/v1/users` | ADMIN | 409 si el `userId` ya tiene perfil |
| PUT | `/api/v1/users/{id}` | ADMIN | actualización parcial de campos |
| DELETE | `/api/v1/users/{id}` | ADMIN | soft delete (`deleted_at`) |

`userId` referencia por valor a la identidad de `ms-security` (sin FK entre esquemas).
Ciclo de vida `ACTIVE`/`INACTIVE` + soft delete independiente (§6/§7 del dominio).

## Ejecutar

```bash
# local (necesita PostgreSQL con la BD eduaircontrol_user_management)
POSTGRES_USER=user_management_user POSTGRES_PASSWORD=user_management_pass ./mvnw spring-boot:run

# o stack del servicio
docker compose up --build
```

## Pruebas

```bash
./mvnw verify        # unit + controllers sobre H2
```
