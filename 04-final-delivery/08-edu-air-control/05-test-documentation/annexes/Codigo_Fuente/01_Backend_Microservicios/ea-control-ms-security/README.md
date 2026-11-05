# ms-security

Servicio de autenticación y autorización de EduAirControl (dominio **seguridad**).

- Spring Boot 4.0.5 / Java 17 — arquitectura hexagonal (ADR-009)
- PostgreSQL 15 con esquema propio `seguridad` (ADR-003), migraciones Liquibase (ADR-008)
- JWT **RS256** con claves RSA y publicación **JWKS** (`/api/v1/auth/jwks`); el resto de
  servicios y el gateway validan sin llamar a este servicio (ADR-006)
- Refresh tokens **stateful** (UUID, hash SHA-256, rotación de un solo uso)
- **Lista negra en Redis** (`blacklist:{jti}`) al hacer logout: el gateway la consulta en
  solo lectura para invalidar el access token antes de su expiración
- Bloqueo de cuenta por intentos fallidos (5 intentos / 15 min) en columnas PostgreSQL
- **Multi-tenant**: instituciones (`institution`, `code` = `companyCode`, ej. `SEN-444`);
  cada usuario pertenece a **una institución y una sede** (`institution_id`, `campus_id`);
  el access token lleva los claims `institutionId`/`campusId` (ADR-016)
- Roles `SUPER_ADMIN`, `ADMIN`, `USER`, `VIEWER` (sembrados por `RoleBootstrap`)
- Puerto **8081**, Swagger UI en `/swagger-ui.html`

## Endpoints

| Método | Ruta | Auth | Notas |
|--------|------|------|-------|
| GET | `/api/v1/auth/health` | público | estado del servicio y de sus dependencias |
| POST | `/api/v1/auth/register` | público | crea cuenta, valida `companyCode`, asigna rol `USER` |
| POST | `/api/v1/auth/login` | público | 401 credenciales/institución inválidas, 423 cuenta bloqueada |
| POST | `/api/v1/auth/refresh` | público | rota el refresh token; el anterior queda revocado |
| POST | `/api/v1/auth/logout` | Bearer | revoca el refresh token y añade el `jti` a la lista negra Redis |
| GET | `/api/v1/auth/jwks` | público | clave pública RSA (JWKS) para validar access tokens |
| POST | `/api/v1/auth/forgot-password` | público | genera código de recuperación (204 siempre) |
| POST | `/api/v1/auth/resend-code` | público | reenvía el código |
| POST | `/api/v1/auth/verify-code` | público | valida el código (400 si inválido) |
| POST | `/api/v1/auth/reset-password` | público | consume el código y cambia la contraseña |
| POST | `/api/v1/auth/change-password` | Bearer | cambio con contraseña actual |
| DELETE | `/api/v1/auth/account` | Bearer | baja lógica de la cuenta (soft delete) |
| GET/POST/PUT | `/api/v1/institutions` | SUPER_ADMIN | gestión de instituciones (tenants) |

El access token es un JWT RS256 de 1 h con `sub`, `email`, `username`, `roles`,
`institutionId`, `campusId` y `permissions` (vacío hasta que exista autorización granular).

## OAuth2 / OIDC (ADR-017)

**Spring Authorization Server 7** embebido. Endpoints estándar:
`/.well-known/openid-configuration`, `/oauth2/jwks`, `/oauth2/authorize`, `/oauth2/token`,
`/userinfo`. Flujo **Authorization Code + PKCE** (obligatorio); el **api-gateway actúa como
BFF** y guarda los tokens en cookies httpOnly. Página de login en `/login` (con `companyCode`).

- Clientes sembrados: `ea-control-web` y `ea-control-mobile` (públicos, PKCE).
- Access token 15 min; refresh 7 días (rotación).
- Claims del access token: `sub`, `email`, `roles`, `institutionId`, `campusId`, `permissions`.

## Ejecutar

```bash
# local (necesita PostgreSQL con la BD eduaircontrol_security y Redis)
POSTGRES_USER=security_user POSTGRES_PASSWORD=security_pass ./mvnw spring-boot:run

# o stack completo
docker compose up --build
```

Las claves de desarrollo están en `src/main/resources/keys/dev-{private,public}.pem`; en
producción se sustituyen por variables de entorno (`SECURITY_RSA_*_KEY_PATH`).

## Pruebas

```bash
./mvnw verify        # 53 tests: unit + controllers + OAuth2 + password flows (H2)
```
