# Equipo 9 · EduAirControl — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 68/100 · Juicio D**

## Veredicto

EduAirControl es un sistema grande y bien pensado: seis microservicios hexagonales con Liquibase, autenticación RS256 en gateway y servicios, firmware ESP32 que envía mediciones que llegan hasta el dashboard, y documentación con 19 ADR. Tres de cinco flujos principales están completos por lectura de código, pero el perfil de usuario falla con 403 para el rol USER y eliminar notificaciones llama a un endpoint que no existe. Lo más urgente: la rama main de 8 repos solo tiene andamiaje (todo está en develop), hay una clave privada RSA en el historial de ms-security y monitoring no separa los datos por institución. Faltan además CI visible, Pull Requests con revisión y pruebas del frontend; cerrando esos puntos el equipo queda cerca de poder entregar.

## Plan para cerrar las brechas

1. **Rotar las claves RSA de ms-security en todos los entornos, invalidar tokens y depurar src/main/resources/keys/ del historial.** (esfuerzo S) — se cierra cuando: git log --all -- src/main/resources/keys no devuelve commits y un token firmado con la clave antigua recibe 401 en el despliegue.
2. **Fusionar develop en main con Pull Request revisado en los 8 repos de código, etiquetar y proteger ramas.** (esfuerzo S) — se cierra cuando: main contiene src/ completo en cada repo, la protección exige 1 revisión y el deploy de Jenkins sale de main.
3. **Corregir el CORS del gateway (orígenes por configuración) y la IP del rate limit.** (esfuerzo S) — se cierra cuando: Una petición con Origin no listado y credenciales no recibe Access-Control-Allow-Origin; prueba automática en GatewayIntegrationTest.
4. **Aplicar aislamiento por institución en monitoring y user-experience.** (esfuerzo M) — se cierra cuando: Con dos instituciones, un token de la A no ve mediciones, alertas ni dashboard de la B (prueba de integración).
5. **Arreglar el perfil de usuario con endpoint de autoservicio y el borrado de alertas.** (esfuerzo S) — se cierra cuando: Un usuario USER guarda su perfil con 200 y el botón eliminar alerta devuelve 204 y la alerta no reaparece al recargar.
6. **Endurecer el login social (state, email verificado, timeouts) y el mensaje de errores.** (esfuerzo M) — se cierra cuando: El callback sin state válido redirige con error; un email no verificado no se enlaza; prueba que lo cubre.
7. **Restablecer CI visible con ./mvnw verify y npm run build como requisito de merge.** (esfuerzo M) — se cierra cuando: Cada PR muestra un check verde en GitHub y el último run de cada repo está en success.
8. **Añadir pruebas al frontend y ampliar el smoke e2e a ambientes, medición y dashboard.** (esfuerzo M) — se cierra cuando: npm test corre en el pipeline con pruebas de apiClient, authService y servicios; smoke.sh termina verificando un dato leído de /dashboard/summary.
9. **Alinear el almacenamiento de tokens con el diseño (BFF con cookie) y el dominio de la app móvil.** (esfuerzo M) — se cierra cuando: localStorage no contiene tokens tras iniciar sesión y el build de release de la app llega a la URL del backend desplegado.
10. **Corregir rutas de build de infra y añadir .env.example, USER y HEALTHCHECK a los servicios.** (esfuerzo S) — se cierra cuando: Clonando los 9 repos con sus nombres reales, ./up.sh levanta el stack; docker inspect muestra usuario no root.
11. **Actualizar la documentación al sistema real y publicar el historial del monolito.** (esfuerzo M) — se cierra cuando: ms-iam.md y overview.md coinciden con las migraciones y el compose; el README enlaza el repo del monolito.
12. **Extraer una librería compartida de seguridad JWT y tenant.** (esfuerzo M) — se cierra cuando: Los 6 servicios importan la misma dependencia y JwksKeyResolver no existe duplicado.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Registro e inicio de sesión con empresa (companyCode) | — |
| Completo | Medición del ESP32 a dashboard ambiental | — |
| Completo | Alta y gestión de ambientes educativos (admin) | — |
| Roto | Alertas y notificaciones (ver, reconocer, eliminar) | Listar y reconocer funcionan (GET /api/v1/alerts y POST /api/v1/alerts/{id}/acknowledge). Eliminar llama DELETE /api/v1/alerts/{id} y AlertController no tiene ese endpoint, así que la UI borra la alerta localmente y la restaura al fallar. |
| Roto | Editar perfil de usuario | La escritura PUT/POST /api/v1/users exige ADMIN o SUPER_ADMIN en ms-user-management y el registro asigna el rol USER; un usuario normal recibe 403 al guardar su propio perfil. |

## Hallazgos

### [Crítico] H-01 · Clave privada RSA de firma de JWT versionada en el historial
- **Repositorio:** `EduAirControl/ea-control-ms-security`
- **Evidencia:** `scripts/generate-dev-keys.sh`
- **Por qué importa:** Quien clone el repo recupera la clave con git show 0ae81ca y puede forjar tokens válidos con rol ADMIN o SUPER_ADMIN en cualquier despliegue que la haya usado. La retirada en edb5830 no la borra del historial, y deploy/k8s/apps.yaml:93 de infra monta un secreto llamado dev-private.pem.
- **Cómo cerrarlo (S):** Rotar el par de claves en todos los entornos (cloud, k8s), invalidar tokens emitidos, y si el repo se va a mantener, reescribir el historial para quitar src/main/resources/keys/. Confirmar que producción usa el par ./secrets/test-private.pem y no el de desarrollo.

### [Crítico] H-02 · main contiene solo andamiaje en 8 repos; todo el trabajo está en develop
- **Repositorio:** `EQUIPO`
- **Evidencia:** `EduAirControl/ea-control-api-gateway:pom.xml`
- **Por qué importa:** Quien entregue, clone o despliegue desde la rama por defecto obtiene un pom.xml y un mvnw. El pipeline de Jenkins solo despliega desde main (Jenkinsfile.test:51), así que o no se despliega el código real o se despliega a mano.
- **Cómo cerrarlo (S):** Fusionar develop en main con un Pull Request revisado en gateway, frontend, classroom, security, sensor, user-experience, user-management y monitoring; etiquetar la versión desplegada; dejar main protegida.

### [Alto] H-03 · Los datos de monitoreo no se aíslan por institución (ADR-016 incumplido)
- **Repositorio:** `EduAirControl/ms-environment-monitoring`
- **Evidencia:** `src/main/java/com/eduaircontrol/msmonitoring/analysis/infrastructure/web/DashboardController.java:42`
- **Por qué importa:** El resumen y las series del dashboard promedian todos los ambientes de todas las instituciones (environmentLookupPort.findAll()) y alertas y mediciones se listan sin filtro; un usuario de una institución ve datos de otras. ADR-016 exige que cada servicio filtre por institución y classroom, sensor y user-management sí lo hacen con TenantContext.
- **Cómo cerrarlo (M):** Llevar institution_id a la proyección de ambientes y filtrar con el claim institutionId del JWT en dashboard, alertas, mediciones y análisis; lo mismo en ms-user-experience.

### [Alto] H-04 · Un usuario con rol USER no puede guardar su perfil
- **Repositorio:** `EduAirControl/ea-control-ms-user-management`
- **Evidencia:** `src/main/java/com/eduaircontrol/msusermanagement/shared/security/SecurityConfig.java:56`
- **Por qué importa:** El front llama PUT/POST /api/v1/users para el perfil propio, el servicio exige ADMIN o SUPER_ADMIN y todo usuario registrado nace con rol USER (AuthService.java:41): el flujo de perfil falla con 403 para casi todos.
- **Cómo cerrarlo (S):** Añadir endpoints de autoservicio (PUT /api/v1/users/me, derivado del sub del JWT) abiertos al propio usuario y dejar la gestión por id solo para administradores; probarlo con un token USER.

### [Alto] H-06 · Login social sin state y con vinculación por email no verificado
- **Repositorio:** `EduAirControl/ea-control-ms-security`
- **Evidencia:** `src/main/java/com/eduaircontrol/mssecurity/infrastructure/web/SocialAuthController.java:85`
- **Por qué importa:** La URL de Google se arma sin state (líneas 85-92) y el callback no lo valida; SocialAuthService.java:54-62 enlaza la identidad social a cualquier cuenta con el mismo email sin comprobar email_verified del proveedor, lo que permite tomar una cuenta existente con un proveedor que no verifique correos y deja abierto el login CSRF.
- **Cómo cerrarlo (M):** Generar y validar un state (y nonce) por sesión; vincular solo si el proveedor declara el email verificado y pedir confirmación en el primer enlace; mover el cliente HTTP a un servicio con timeouts.

### [Alto] H-07 · Sin CI verificable en los repos de código
- **Repositorio:** `EQUIPO`
- **Evidencia:** `EduAirControl/ea-control-infra:jenkins/Jenkinsfile.test:46`
- **Por qué importa:** GitHub Actions se retiró (PR #2 de infra) y las últimas ejecuciones consultadas estaban en failure; los repos no tienen workflows y las pruebas solo corren en un Jenkins externo cuyo estado no es público. Nada garantiza que develop pase pruebas antes de fusionarse.
- **Cómo cerrarlo (M):** Restablecer un check de pruebas visible en cada repo (workflow que ejecute ./mvnw verify y npm run build) o publicar el estado del Jenkins; exigirlo como requisito del merge.

### [Alto] H-08 · Historial reconstruido: los servicios nacen completos en el mismo segundo
- **Repositorio:** `EQUIPO`
- **Evidencia:** `EduAirControl/ea-control-ms-user-management:pom.xml`
- **Por qué importa:** user-management arranca con scaffold, feature y tests en 18:52:46-47 del 6-oct; gateway con cinco commits en 18:37:37; sensor, security y classroom igual. El trabajo real previo (desde 25-ago) no se puede auditar ni atribuir a cada integrante.
- **Cómo cerrarlo (S):** Enlazar o publicar el repositorio del monolito EduAirControl/ con su historial y documentar la migración; desde ahora no reescribir ni aplastar historia.

### [Alto] H-09 · CORS refleja cualquier origen con credenciales en el gateway
- **Repositorio:** `EduAirControl/ea-control-api-gateway`
- **Evidencia:** `src/main/java/com/eduaircontrol/apigateway/config/BffSecurityConfig.java:59`
- **Por qué importa:** setAllowedOriginPatterns('*') junto con setAllowCredentials(true) permite que cualquier sitio lea respuestas del gateway con la cookie de sesión BFF (por ejemplo /api/v1/me). La variable CORS_ALLOWED_ORIGINS que infra define para el gateway no se usa. El rate limit además toma la IP del primer X-Forwarded-For (RateLimitFilter.java:62) sin proxy de confianza, evitable con una cabecera falsa en rutas públicas.
- **Cómo cerrarlo (S):** Leer los orígenes permitidos de configuración (CORS_ALLOWED_ORIGINS), sin comodín; tomar la IP solo del proxy de confianza (Caddy).

### [Alto] H-10 · El frontend (web y móvil) no tiene ninguna prueba
- **Repositorio:** `EduAirControl/ea-control-frontend`
- **Evidencia:** `apps/web/package.json:10`
- **Por qué importa:** 317 archivos entre web y móvil sin pruebas; los dos fallos de integración encontrados (perfil, alertas) y las regresiones de contrato que el equipo corrigió en ramas fix/* habrían saltado en pruebas de servicios del front.
- **Cómo cerrarlo (M):** Añadir Vitest + Testing Library a apps/web con pruebas de apiClient, authService y los servicios de ambientes/alertas/perfil, y ejecutarlas en el pipeline.

### [Medio] H-05 · La UI elimina notificaciones con un endpoint que no existe
- **Repositorio:** `EduAirControl/ms-environment-monitoring`
- **Evidencia:** `src/main/java/com/eduaircontrol/msmonitoring/monitoring/infrastructure/web/AlertController.java:45`
- **Por qué importa:** alertService.delete llama DELETE /api/v1/alerts/{id} (alertService.js:29), el controlador solo tiene GET y acknowledge; la acción 'eliminar' falla y se revierte, y el comentario de useNotifications.js:93 promete un soft delete que no existe.
- **Cómo cerrarlo (S):** Implementar DELETE /api/v1/alerts/{id} (soft delete tras reconocer) con prueba, o quitar el botón y el método del front.

### [Medio] H-11 · Tokens en localStorage mientras el README promete cookies httpOnly
- **Repositorio:** `EduAirControl/ea-control-frontend`
- **Evidencia:** `apps/web/src/shared/services/apiClient.js:16`
- **Por qué importa:** Cualquier XSS roba el access y el refresh token; además conviven dos mecanismos (sesión BFF y token) y la documentación describe el seguro, no el real.
- **Cómo cerrarlo (M):** Elegir el BFF con cookie httpOnly (SameSite) y eliminar localStorage, o documentar y mitigar el riesgo (CSP, vida corta del token).

### [Medio] H-12 · up.sh no construye con los nombres reales de los repos
- **Repositorio:** `EduAirControl/ea-control-infra`
- **Evidencia:** `docker-compose.yml:27`
- **Por qué importa:** El compose usa build: ../api-gateway, ../ms-security... pero los repos se llaman ea-control-api-gateway, ea-control-ms-security...; solo el frontend usa el nombre real (../ea-control-frontend). Un integrante nuevo no puede levantar el stack con el README.
- **Cómo cerrarlo (S):** Unificar los nombres (build: ../ea-control-...) y documentar el script de clonación de los 9 repos; o usar las imágenes de GHCR.

### [Medio] H-13 · La documentación de datos y arquitectura describe un sistema distinto en tres puntos
- **Repositorio:** `code-sena/ea-control-docs`
- **Evidencia:** `06-data/README.md:32`
- **Por qué importa:** Dice que IAM tiene 13 tablas de ms-iam.md (permissions, user_sessions, audit_logs...) y el servicio tiene 7 de negocio más OAuth2; overview.md:38 dice que monitoring y UX comparten BD y la infra levanta 6; el overview dice ingesta con X-API-Key y el código usa JWT de dispositivo.
- **Cómo cerrarlo (M):** Actualizar ms-iam.md al modelo real (o implementar lo que falta) y corregir overview.md; añadir un chequeo de congruencia al DoD.

### [Medio] H-14 · Cambios fusionados sin Pull Request ni revisión
- **Repositorio:** `EQUIPO`
- **Evidencia:** `EduAirControl/ea-control-ms-security:README.md`
- **Por qué importa:** 0 PR en 7 de 10 repos; en infra 4 PR fusionados por su autor sin revisores. Los merges a develop se hacen en local ('merge: ... into develop'), así que ningún cambio de seguridad (RSA, superadmin) tuvo una segunda mirada.
- **Cómo cerrarlo (S):** Proteger develop y main, exigir PR con un revisor y asignar al otro integrante como revisor por defecto.

### [Medio] H-15 · La app móvil en producción apunta a un dominio que no es el desplegado
- **Repositorio:** `EduAirControl/ea-control-frontend`
- **Evidencia:** `apps/mobile/EduAirControlMobile/Front-End/src/shared/config/index.js:18`
- **Por qué importa:** El valor por defecto de producción es https://api.eduaircontrol.com mientras infra y el firmware usan api.eduaircontrol.tech: un build de release de la app no llegaría al backend.
- **Cómo cerrarlo (S):** Parametrizar por EXPO_PUBLIC_API_URL en el build de release y alinear el dominio.

### [Medio] H-16 · Código de seguridad copiado en los 6 servicios
- **Repositorio:** `EQUIPO`
- **Evidencia:** `EduAirControl/ms-environment-monitoring:src/main/java/com/eduaircontrol/msmonitoring/shared/security/JwksKeyResolver.java:92`
- **Por qué importa:** JwksKeyResolver, JwtAuthFilter, SecurityConfig y TenantContext están repetidos con pequeñas variaciones; una corrección de seguridad hay que aplicarla seis veces (monitoring y user-experience ya divergieron: no filtran por institución).
- **Cómo cerrarlo (M):** Extraer una librería starter compartida (Maven) para autenticación JWT y contexto de tenant.

### [Medio] H-17 · Mensajes internos de error expuestos al cliente
- **Repositorio:** `EduAirControl/ea-control-ms-security`
- **Evidencia:** `src/main/resources/application.properties:45`
- **Por qué importa:** server.error.include-message=always devuelve el texto de la excepción en errores no controlados.
- **Cómo cerrarlo (S):** Poner never o on-param y confiar en el GlobalExceptionHandler.

### [Medio] H-18 · Sin .env.example en 7 repos; el README de classroom pide un .env inexistente
- **Repositorio:** `EQUIPO`
- **Evidencia:** `EduAirControl/ea-control-ms-classroom-management:README.md:48`
- **Por qué importa:** Quien trabaja un servicio aislado no sabe qué variables definir (POSTGRES_*, JWKS, RabbitMQ, claves RSA) salvo que lea el compose.
- **Cómo cerrarlo (S):** Añadir .env.example a gateway y los 6 servicios con las variables reales y mencionarlo en el README.

### [Medio] H-19 · Dockerfiles sin usuario no-root ni HEALTHCHECK
- **Repositorio:** `EduAirControl/ea-control-infra`
- **Evidencia:** `EduAirControl/ea-control-ms-security:Dockerfile:17`
- **Por qué importa:** Las imágenes finales de los 7 servicios y del web corren como root; si un servicio se compromete, el contenedor tiene privilegios de más.
- **Cómo cerrarlo (S):** Añadir USER no privilegiado (y HEALTHCHECK) a la etapa final de cada Dockerfile.

### [Bajo] H-20 · Smoke de infra solo cubre autenticación; ambientes, medición y dashboard no tienen e2e
- **Repositorio:** `EQUIPO`
- **Evidencia:** `EduAirControl/ea-control-infra:scripts/smoke.sh`
- **Por qué importa:** scripts/smoke.sh:19-55 verifica health, registro, login y logout; no hay prueba automática de los flujos de ambientes, ingesta o alertas, justo donde aparecieron los desajustes de contrato.
- **Cómo cerrarlo (M):** Ampliar el smoke con crear ambiente, instalar sensor, enviar una medición con token DEVICE y leer /dashboard/summary.

### [Bajo] H-21 · Carpetas de documentación duplicadas para el mismo servicio
- **Repositorio:** `code-sena/ea-control-docs`
- **Evidencia:** `09-microservices/services/07-user-experience/README.md`
- **Por qué importa:** Existen 07-ms-user-experience y 07-user-experience; el catálogo apunta a un nombre (02-auth-service) distinto del repo (ms-security).
- **Cómo cerrarlo (S):** Dejar una sola carpeta por servicio con el nombre del repo.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 95.0 |
| D2 | Reproducibilidad | 62.7 |
| D3 | Arquitectura y congruencia | 84.5 |
| D4 | Calidad de código | 63.6 |
| D5 | Seguridad | 83.5 |
| D6 | Pruebas | 67.0 |
| D7 | DevOps y despliegue | 46.0 |
| D8 | Datos | 96.7 |
| D9 | API y contratos | 78.8 |
| D10 | Frontend y móvil | 50.0 |
| D11 | IoT y firmware | 90.0 |
| D12 | Documentación y trazabilidad | 50.0 |
| D13 | Proceso y colaboración | 50.0 |
| D14 | Extremo a extremo | 50.0 |

## Lo que está bien

- Autenticación RS256 con JWKS validada en gateway y en cada servicio (`EduAirControl/ea-control-api-gateway:src/main/java/com/eduaircontrol/apigateway/filter/JwtAuthenticationFilter.java; EduAirControl/ea-control-ms-security:src/main/java/com/eduaircontrol/mssecurity/infrastructure/security/PasswordEncoderConfig.java:5`)
- Sin secretos en el árbol actual: superadmin y claves RSA obligatorios por variable, sin default (`EduAirControl/ea-control-ms-security:src/main/resources/application.properties:30,43`)
- Autorización por rol en las escrituras de classroom, sensor, monitoring y security (`EduAirControl/ea-control-ms-classroom-management:src/main/java/com/eduaircontrol/msclassroom/shared/security/SecurityConfig.java:56`)
- Una base de datos PostgreSQL por servicio con Liquibase y healthchecks (`EduAirControl/ea-control-infra:docker-compose.yml:245`)
- Suite de pruebas con afirmaciones reales en los 7 servicios Java (más de 480 @Test) (`EduAirControl/ms-environment-monitoring:src/test/java/com/eduaircontrol/msmonitoring/monitoring/infrastructure/web/MonitoringControllerTest.java`)
- Firmware sin credenciales incrustadas: aprovisionamiento BLE, TLS con CA y reconexión (`EduAirControl/ea-control-infra:iot/esp32-node/include/config.h:43; iot/esp32-node/src/main.cpp:269`)
- Contrato de ingesta ESP32 a backend y UUID de variables coherentes (`EduAirControl/ea-control-infra:iot/esp32-node/src/main.cpp:259; EduAirControl/ms-environment-monitoring:src/main/resources/db/changelog/changes/010-seed-reference.yaml:19`)
- Documentación rica: 19 ADR, OpenAPI por servicio y matriz de trazabilidad (`code-sena/ea-control-docs:05-architecture/decisions/records/ADR-016-multi-tenancy-by-institution.md`)
- Infra completa con observabilidad (Prometheus, OTel), Jenkins y k8s (`EduAirControl/ea-control-infra:docker-compose.observability.yml; EduAirControl/ea-control-infra:deploy/k8s/apps.yaml`)
- El equipo corrigió activamente desajustes de contrato con ramas fix/* ya integradas en develop (`EduAirControl/ea-control-frontend:apps/web/src/shared/services/apiClient.js:31`)

## Lo que no se pudo verificar

- Calidad de código (D4) — Se muestreó con profundidad solo ms-security y ms-environment-monitoring (los dos servicios con más líneas de Java); los otros servicios y el frontend no recibieron ítems D4.
- Compilación del frontend y de los servicios, y ejecución de las pruebas — No se permite ejecutar npm, mvn ni docker; todo es lectura estática. Los flujos COMPLETO se dedujeron por lectura de contratos.
- Ejecuciones de CI en Jenkins y estado de GitHub Actions de gateway, classroom y user-experience — Jenkins es externo; las consultas a la API de GitHub fallaron por tiempo de espera en esos repos.
- Despliegue en línea (T-7.4 y T-14.3) — REPOSITORIOS-EQUIPOS no declara una URL de despliegue; no se hizo ningún GET.
- Colaboradores y propiedad de los repos de la organización EduAirControl (T-13.6) — La API respondió 403 al listar colaboradores (sin permiso de push) y solo expuso a un miembro de la organización.
- Pull Requests de gateway y user-experience — La consulta a la API falló por tiempo de espera; se infiere por los demás repos.
- Si la clave RSA del historial llegó a usarse en un entorno desplegado — Requiere acceso a los secretos del VPS; solo se ve que k8s referencia un secreto con el nombre dev-private.pem y que el compose cloud usa test-private.pem.
- Si el BLE del firmware exige emparejamiento seguro, y la política de cookies SameSite del BFF — No se leyó el detalle de la pila BLE ni BffSessionConfig a fondo.
- Si «correo», Integrante 2 y Integrante 3 son la misma persona — Comparten nombre de autor pero no correo ni login noreply; no se fusionaron. Si fueran la misma persona (Integrante 1) tendría 387 commits (56,8 %), igualmente por debajo del 70 %.
- Tablero de GitHub Projects — Solo se consultaron issues (0), no los proyectos.
