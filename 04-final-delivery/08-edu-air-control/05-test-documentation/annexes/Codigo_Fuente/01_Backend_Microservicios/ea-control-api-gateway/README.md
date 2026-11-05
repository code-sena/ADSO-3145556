# api-gateway

Punto de entrada único al backend de EduAirControl (ADR-005). Enruta las peticiones
del frontend hacia los microservicios, valida el JWT en el borde y aplica controles
comunes de borde. **No contiene lógica de negocio ni datos de dominio.**

- Spring Cloud Gateway 5.0 (reactivo) sobre Spring Boot 4.0.5 / Java 17
- Validación de JWT **RS256** con las claves públicas del JWKS de `ms-security` (ADR-006)
- Lista negra de tokens en Redis (`blacklist:{jti}`, solo lectura — la escribe `ms-security`)
- Rate limiting global con contadores en Redis (por IP en público, por usuario autenticado)
- Propaga `X-User-Id`, `X-User-Role` y `X-Correlation-Id` a los servicios internos
- Puerto **8080**

## Rutas

| Ruta externa | Upstream | Variable |
|--------------|----------|----------|
| `/api/v1/auth/**` | `ms-security` (:8081) | `AUTH_URI` |
| `/api/v1/educational-environments/**`, `/api/v1/classrooms/**` | `ms-classroom-management` (:3002) | `CLASSROOM_URI` |
| `/api/v1/sensors/**`, `/api/v1/variables/**`, `/api/v1/sensor-installations/**`, `/api/v1/sensor-variables/**` | `ms-sensor-management` (:3004) | `SENSOR_URI` |
| `/api/v1/environmental-data/**`, `/api/v1/measurements/**`, `/api/v1/alerts/**` | `ms-environment-monitoring` (:3003) | `MONITORING_URI` |
| `/api/v1/users/**`, `/api/v1/user-profiles/**` | `ms-user-management` (:3007) | `USER_MANAGEMENT_URI` |
| `/api/v1/preferences/**`, `/api/v1/favorites/**` | `ms-user-experience` (:3006) | `USER_EXPERIENCE_URI` |

Rutas configuradas en `src/main/resources/application.yml`
(`spring.cloud.gateway.server.webflux.routes`).

## Autenticación

Los endpoints públicos son `/health`, `/api/v1/auth/health`, `/api/v1/auth/register`,
`/api/v1/auth/login`, `/api/v1/auth/refresh` y `/api/v1/auth/jwks`. El resto exige
`Authorization: Bearer <access_token>`; el gateway valida firma, expiración y lista negra,
y reenvía la identidad como headers internos. La autorización fina la aplica cada servicio.

## BFF (Backend-for-Frontend, ADR-017)

El gateway también actúa como **cliente OAuth2** del navegador:

- Inicia sesión en `/oauth2/authorization/web` (Authorization Code + **PKCE** contra
  `ms-security`), con callback en `/login/oauth2/code/web`.
- Guarda los tokens **en el servidor** (sesión en **Redis**) y expone solo una cookie
  `SESSION` **httpOnly**; el token nunca llega a JavaScript.
- Para las llamadas de API toma el access token de la sesión (o de la cabecera Bearer) y
  propaga `X-User-Id`, `X-User-Role`, `X-Institution-Id`, `X-Campus-Id`.

## Ejecutar

```bash
# stack del gateway + Redis
docker compose up --build

# verificación
curl http://localhost:8080/health
```

Variables: `AUTH_ISSUER` (issuer OAuth2 de ms-security), `OAUTH2_CLIENT_ID`,
`SESSION_TIMEOUT`, `REDIS_HOST`/`REDIS_PORT`.

## Pruebas

```bash
./mvnw verify        # unit + integración (upstream stub, Redis y JWKS mockeados)
```
