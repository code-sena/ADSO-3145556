# Equipo 10 · FaceAttend EDU — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 66/100 · Juicio D**

## Veredicto

FaceAttend EDU es de lo más completo de la ficha: nueve microservicios con Kong, web y móvil, migraciones, CI en verde y un despliegue real con HTTPS que responde. Cuatro de los cinco flujos revisados (login, quiosco de asistencia, registro facial y alta de sedes) funcionan de punta a punta; el de justificaciones pierde el archivo de soporte porque nunca se sube. Lo que más urge cerrar es de seguridad: las contraseñas del administrador y de los usuarios demo están escritas en las migraciones y se aplican a producción, la guía de despliegue deja abiertos puertos y bases con credenciales por defecto, y los datos biométricos se tratan sin consentimiento ni cifrado. Además, el paquete RAPS aún describe JWT que no existe, su README está vacío y falta el artículo. Con esas correcciones y revisiones de PR el proyecto queda cerca de entregable en pocas semanas.

## Plan para cerrar las brechas

1. **Sacar las contraseñas del administrador y demo de los changesets, forzar cambio en el primer ingreso, filtrar semillas por contexto y rotar las cuentas ya creadas en Neon.** (esfuerzo S) — se cierra cuando: git grep no encuentra contraseñas en claro en database/*/02-dml; liquibase se ejecuta con --contexts; el login con las credenciales antiguas devuelve 401 en producción y la cuenta admin nueva obliga a cambiar la clave.
2. **Cerrar el security group a 80/443, BIND_IP=127.0.0.1, exigir con :? todas las contraseñas del compose, rotar la de MongoDB y limpiar DESPLIEGUE.md de credenciales y host de Neon.** (esfuerzo S) — se cierra cuando: Un escaneo desde fuera de la VPC solo ve 80/443; docker compose config falla si falta alguna contraseña; git grep no halla valores por defecto en back-end/docker-compose.yml.
3. **Restringir CORS a los dominios reales, añadir límite estricto por IP a /auth/login y tomar reviewedBy de la sesión.** (esfuerzo S) — se cierra cuando: Una petición con Origin ajeno no recibe cabeceras CORS; el undécimo intento de login en un minuto responde 429; una prueba demuestra que reviewedBy ignora el cuerpo.
4. **Implementar la subida real del soporte de justificaciones (multipart o MinIO) con prueba e2e.** (esfuerzo M) — se cierra cuando: Un aprendiz adjunta un PDF, el revisor lo descarga con el mismo contenido y el registro supporting_document apunta a un objeto existente.
5. **Mover el alta compuesta de usuarios al backend con compensación en servidor y eliminar los catch vacíos del rollback.** (esfuerzo M) — se cierra cuando: Si falla un paso del alta no quedan filas huérfanas en identity ni en academic (prueba de integración) y no existe ninguna llamada DELETE /persons desde la web.
6. **Consentimiento de tratamiento de datos (Ley 1581), aviso de privacidad, revocación y cifrado de columnas y embeddings sensibles.** (esfuerzo L) — se cierra cuando: El alta registra aceptación con fecha y versión; existe endpoint de borrado de biometría; los embeddings y el documento de identidad no son legibles en una consulta directa a la base.
7. **Completar el repositorio RAPS: README, artículo del proyecto, .gitignore y corregir las secciones de seguridad (sesión opaca en lugar de JWT).** (esfuerzo M) — se cierra cuando: README con índice y autores; carpetas 10 con el artículo ES/EN; git grep -i 'jjwt\|RS256' solo aparece en secciones marcadas como evolución futura.
8. **Proteger main con revisión obligatoria, usar issues/Project para las HU pendientes y limpiar o fusionar las ramas residuales.** (esfuerzo S) — se cierra cuando: La configuración de rama exige 1 aprobación; hay al menos un issue por HU pendiente de la matriz de trazabilidad; git branch -r solo lista ramas activas.
9. **Transferir el repositorio principal y RAPS a la organización code-sena y mover los secretos de Actions.** (esfuerzo S) — se cierra cuando: Ambos repositorios aparecen en code-sena con los tres integrantes y el instructor como admin; el despliegue de producción corre desde la nueva ubicación.
10. **Aislar datos por servicio (rol y GRANT por schema), activar el alta de sedes documentándola y retirar o implementar la pantalla de Sanciones.** (esfuerzo M) — se cierra cuando: Cada servicio usa su propio rol que no puede leer otros schemas (prueba SELECT denegado); el README explica ONBOARDING_SERVICE_USER_ID; la sección de Sanciones consume un endpoint o ya no existe.
11. **Reducir duplicación y deuda de calidad: librería de autenticación compartida, PersonController con consulta paginada, cobertura en CI y regenerar OpenAPI desde el código.** (esfuerzo L) — se cierra cuando: Un solo módulo de autenticación consumido por todos los servicios; CI publica cobertura con umbral; el OpenAPI del repositorio coincide con el generado por los servicios en CI.
12. **Endurecimiento menor: nginx sin root, /health/quality interno, token de sesión de la web fuera de localStorage y quitar artefactos de IA del índice.** (esfuerzo S) — se cierra cuando: El contenedor web corre con usuario no root; /health/quality devuelve 401/404 desde Internet; git ls-files no lista .claude ni docs/superpowers.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Inicio de sesión (web/móvil) | — |
| Completo | Registro de asistencia por quiosco (abrir clase, identificar, registrar) | — |
| Completo | Registro facial con prueba de vida (enrolamiento biométrico) | — |
| Roto | Justificar una inasistencia con documento de soporte | El archivo de soporte nunca sale del navegador: el cliente fabrica un storageUri y solo envía metadatos; el backend no tiene endpoint de subida de bytes ni MinIO/multipart, así que el documento se pierde y el revisor no puede abrirlo. |
| Completo | Alta pública de sede y administrador (onboarding) | — |

## Hallazgos

### [Crítico] H-01 · Cuentas semilla con contraseñas conocidas se aplican también a la base de producción
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `database/01-ms-identity-db/02-dml/001-bootstrap-admin-user.yaml:15 (admin.faceattend como Super Administrador con contraseña ««redactado»» en claro), database/01-ms-identity-db/02-dml/002-bootstrap-demo-users.yaml:15-16, database/docker-compose.neon.yml:18-25 (liquibase update sin --contexts), database/SEEDS.md:26 (documenta la contraseña)`
- **Por qué importa:** Los changesets llevan context: seed pero ningún comando de Liquibase (neon, local ni deploy-database.yml) filtra por contexto, así que corren en Neon; la cuenta tiene must_change_password en FALSE (009-add-must-change-password-to-app-user.yaml:9). Cualquiera que lea el repositorio puede entrar como administrador global de producción.
- **Cómo cerrarlo (S):** Quitar las contraseñas de los changesets: crear el admin con un valor generado fuera del repositorio (variable de despliegue) y must_change_password en TRUE; limitar las semillas demo con --contexts=dev; rotar o borrar en Neon las cuentas ya creadas.

### [Crítico] H-02 · La guía de despliegue documenta puertos de datos y administración abiertos y credenciales por defecto de MongoDB en producción
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `DESPLIEGUE.md:83 (usuario mongoadmin con contraseña por defecto ««redactado»»), DESPLIEGUE.md:117 (security groups abiertos en 8000, 8001, 8081-8089, 27017, 6379, 9092), DESPLIEGUE.md:143 (BIND_IP=0.0.0.0), back-end/docker-compose.yml:24 (respaldo mongopass en el compose)`
- **Por qué importa:** Según la propia guía el MongoDB con los embeddings faciales/dactilares y la consola de administración de Kong (8001, sin autenticación) quedan expuestos a Internet con credenciales triviales. El estado vivo no se sondeó (fuera del alcance de solo lectura) y el host EC2 ya no responde, pero la configuración versionada y el documento lo normalizan.
- **Cómo cerrarlo (S):** Cerrar en el security group todo salvo 80/443; dejar BIND_IP=127.0.0.1 y quitar el valor por defecto de MONGO_PASSWORD, POSTGRES_PASSWORD y NEON_PASSWORD del compose (exigirlos con :?); borrar las credenciales de DESPLIEGUE.md y rotar las de Mongo.

### [Alto] H-03 · Datos personales y biométricos sin consentimiento ni cifrado en reposo (Ley 1581 de 2012)
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/ProyectoFaceAttendEDU:back-end/06-ms-biometric/infrastructure/persistence/mongo_embedding_repository.py:58 (embeddings en claro); code-sena/fae-docs:05-architecture/decisions/records/ADR-016-encryption-at-rest.md:5 (nota: 'not implemented'); grep de consentimiento, habeas data y 1581 en back-end y front-end: 0 archivos`
- **Por qué importa:** El sistema captura rostro y huella (dato sensible) de aprendices, algunos menores, sin registrar autorización del titular ni política de tratamiento, y los embeddings, fotos y documentos de soporte no se cifran; el equipo mismo lo reconoce como no implementado.
- **Cómo cerrarlo (L):** Agregar al alta de usuario una aceptación de tratamiento con fecha y versión de la política, endpoint de revocación/borrado, cifrado de columnas sensibles (pgcrypto) y de la colección de embeddings; documentar el aviso de privacidad.

### [Alto] H-04 · El documento de soporte de una justificación nunca se sube; se guarda una ruta inventada
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `front-end/Web/src/viewmodels/useJustificationsViewModel.js:411-427 (comentario 'aún no existe endpoint de subida de bytes' y storageUri fabricado en el cliente)`
- **Por qué importa:** HU-JUS exige adjuntar soporte para justificar; hoy el revisor ve un registro con un enlace a un objeto que no existe y el archivo se pierde al cerrar la pestaña. Contradice ADR-009 (MinIO) declarado como decisión.
- **Cómo cerrarlo (M):** Implementar en ms-attendance un endpoint multipart (o URL prefirmada de MinIO), guardar el objeto y devolver el storageUri real; agregar al compose el servicio MinIO y una prueba e2e de justificar con adjunto.

### [Alto] H-05 · El rollback del alta de usuarios llama DELETE /persons/{id}, ruta que no existe, y el error se traga
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `front-end/Web/src/services/api/staffProvisioning.js:306 y front-end/Web/src/services/api/staffProvisioning.js:430 (authApi.deletePerson dentro de catch vacío); back-end/01-ms-identity/src/main/java/com/faceattend_edu/identity_service/adapter/in/web/controller/PersonController.java:30 (el controlador no declara DeleteMapping)`
- **Por qué importa:** Si falla un paso del alta de una persona, el navegador intenta compensar con tres llamadas independientes; la de persona devuelve 404/405 en silencio y quedan personas huérfanas que luego bloquean el reintento por documento o correo duplicado. Además la transacción distribuida vive en el cliente.
- **Cómo cerrarlo (M):** Mover el alta compuesta a un caso de uso del backend (como ya hace el onboarding) con compensación en servidor, o exponer el borrado lógico de personas; no usar catch vacíos.

### [Alto] H-06 · Los RAP documentan JWT RS256 con jjwt, pero el sistema usa sesiones opacas
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/FaceAttendEDU-RAPS:09-Manuales/es/Manual_Tecnico.md:107 y :519 (jjwt), propietario/FaceAttendEDU-RAPS:04-Documentación de diseño del software/es/Documentacion-diseno-software.md:63 (autenticación JWT RS256 en Kong); propietario/ProyectoFaceAttendEDU:back-end/99-api-gateway/kong/kong.yml:169-172 (sin plugin jwt ni consumidores); code-sena/fae-docs:07-api/authentication.md:10-11 (lo reconoce)`
- **Por qué importa:** El paquete que se entrega como RAP (23 archivos mencionan JWT) describe una seguridad distinta de la construida; un evaluador o un nuevo desarrollador configuraría llaves que no existen.
- **Cómo cerrarlo (S):** Regenerar las secciones de seguridad de los manuales a partir de fae-docs (sesión opaca validada en Kong y en cada servicio) y eliminar las menciones a jjwt/RS256 o marcarlas como evolución futura (ADR-007).

### [Alto] H-07 · El artículo del proyecto (RAP 10) no existe y el README del repositorio está vacío
- **Repositorio:** `propietario/FaceAttendEDU-RAPS`
- **Evidencia:** `README.md:1 (20 bytes, solo el título) y README_EN.md:1; las carpetas 10-Articulo del proyecto/es y /en solo contienen .gitkeep`
- **Por qué importa:** Es un entregable del proyecto formativo y su carpeta está vacía; el README no orienta a quien abre el repositorio. Todo el historial (94 commits) está comprimido entre el 9 y el 14 de octubre.
- **Cómo cerrarlo (M):** Redactar el artículo (ES/EN) en las carpetas 10, escribir un README con el índice de RAP, autores y enlaces a fae-docs y al proyecto, y agregar un .gitignore.

### [Medio] H-08 · CORS abierto a cualquier origen con credenciales y límite de intentos de desarrollo en el login
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `back-end/99-api-gateway/kong/kong.yml:289-292 (origins '*' y credentials true), back-end/99-api-gateway/kong/kong.yml:296 (6000 peticiones por minuto, comentado como límite de desarrollo, aplica también a /auth/login)`
- **Por qué importa:** Cualquier sitio puede invocar la API desde el navegador de un usuario autenticado y el login admite fuerza bruta distribuida (solo lo frena el bloqueo de cuenta de identity).
- **Cómo cerrarlo (S):** Restringir origins a los dominios reales de la web (Vercel/duckdns), y definir un límite por IP para /auth/login (por ejemplo 10 por minuto) en una ruta propia, como ya se hizo con el onboarding.

### [Medio] H-09 · La revisión de justificaciones acepta reviewedBy desde el cuerpo de la petición
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `back-end/05-ms-attendance/src/main/java/com/faceattend_edu/attendance_service/infrastructure/web/controller/JustificationController.java:64 y back-end/05-ms-attendance/src/main/java/com/faceattend_edu/attendance_service/infrastructure/web/dto/ReviewJustificationRequest.java:10`
- **Por qué importa:** Quien tenga permiso de revisar puede registrar la aprobación a nombre de otro usuario; la trazabilidad de la decisión no es confiable.
- **Cómo cerrarlo (S):** Ignorar el campo del cuerpo y tomar el usuario del atributo X-User-Id que ya pone AuthTokenFilter.

### [Medio] H-10 · Todas las bases comparten una instancia y un superusuario
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `back-end/docker-compose.yml:247-249 (los servicios usan el mismo usuario postgres), database/01-ms-identity-db/03-dcl/changelog.yaml (vacío)`
- **Por qué importa:** Un servicio comprometido lee o altera los schemas de los demás; el principio de base por contexto solo se cumple lógicamente.
- **Cómo cerrarlo (M):** Crear un rol por schema con GRANT mínimo en los changelogs 03-dcl y usar credenciales distintas por servicio; en Neon, un rol por servicio.

### [Medio] H-11 · La pantalla de Sanciones guarda sus datos solo en localStorage
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `front-end/Web/src/models/data/sanctionsData.js:20 (clave faceattend_sanctions), front-end/Web/src/view/authorized/reports/sections/SanctionsSection.js:25`
- **Por qué importa:** Aparece en reportes como si fuese información oficial pero no existe servicio ni tabla; los datos desaparecen al limpiar el navegador y no se comparten entre administradores.
- **Cómo cerrarlo (M):** Retirar la sección o crear el servicio/tabla de sanciones con su contrato; mientras tanto, rotularla como demostración.

### [Medio] H-12 · El alta pública de sedes queda apagada por defecto y no se explica en el arranque
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `back-end/docker-compose.yml:136 (ONBOARDING_SERVICE_USER_ID vacío deshabilita el endpoint con 503), README.md:23-26`
- **Por qué importa:** Siguiendo el README, el flujo de registro de sede de la landing responde 503; hace falta crear una cuenta de servicio y poner su id, algo que ni README ni DESPLIEGUE.md mencionan.
- **Cómo cerrarlo (S):** Documentar el paso (crear la cuenta de servicio Super Administrador y fijar la variable) y agregarlo a la semilla de desarrollo; mostrar en la web un mensaje claro si responde 503.

### [Medio] H-13 · Lógica de autenticación y métricas duplicada por servicio
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `back-end/01-ms-identity/src/main/java/com/faceattend_edu/identity_service/adapter/in/web/security/AuthTokenFilter.java:42-45 ('Duplicado intencional por MS'), back-end/03-ms-academic/src/infrastructure/http/authGuard.ts:1, back-end/03-ms-academic/src/infrastructure/http/qualityMiddleware.ts:1`
- **Por qué importa:** La seguridad más delicada está copiada en 4 servicios Java y 4 Node; cualquier corrección debe repetirse ocho veces y ya hay divergencias entre copias.
- **Cómo cerrarlo (L):** Extraer un módulo/librería compartida (starter Spring y paquete npm) o mover la validación solo a Kong y propagar X-User-Id firmado.

### [Medio] H-14 · Controlador con lógica de alcance, paginación en memoria y consultas N+1
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `back-end/01-ms-identity/src/main/java/com/faceattend_edu/identity_service/adapter/in/web/controller/PersonController.java:58-87, back-end/03-ms-academic/src/infrastructure/http/routes.ts:190`
- **Por qué importa:** Listar personas de un instructor hace una consulta por id y pagina en memoria; con cientos de aprendices degrada el servicio, y la lógica de alcance en el controlador rompe la arquitectura hexagonal declarada.
- **Cómo cerrarlo (M):** Mover el alcance a un caso de uso con consulta paginada (WHERE person_id IN ...) y dividir routes.ts de academic por recurso.

### [Medio] H-15 · Cobertura sin medir y servicios con pruebas escasas
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `back-end/07-ms-configuration/src/infrastructure/http/__tests__/permissions.test.ts:1 (07-ms-configuration: 3 archivos de prueba para 2008 líneas; 03-ms-academic: 4 para 3063), .github/workflows/ci.yml líneas 60-62 sin informe de cobertura`
- **Por qué importa:** No hay cifra de cobertura ni umbral en CI, y las pruebas no están ligadas a las HU (todas 'proposed' en la matriz); el riesgo se concentra donde hay más reglas de negocio.
- **Cómo cerrarlo (M):** Activar jacoco/jest --coverage/pytest-cov con un umbral mínimo en CI y escribir pruebas e2e de los 5 flujos principales sobre el compose.

### [Medio] H-16 · Los PR casi no se revisan y no se usan issues
- **Repositorio:** `EQUIPO`
- **Evidencia:** `gh api: 6 de 36 PR humanos de propietario/ProyectoFaceAttendEDU con revisión y 0 de 22 en code-sena/fae-docs; 0 issues en ambos repositorios`
- **Por qué importa:** Los merges se hacen por el propio autor y no hay planeación visible, por lo que errores como H-01 o H-09 pasan sin segunda mirada.
- **Cómo cerrarlo (S):** Activar protección de rama con 1 aprobación obligatoria, plantilla de PR con lista de seguridad y llevar el backlog (HU pendientes) como issues o Project.

### [Medio] H-17 · El código de la rama principal nace de un commit raíz de 2097 archivos
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/ProyectoFaceAttendEDU: git show --stat 4a09de3 (2097 archivos, 242450 inserciones, sin padres, 2026-10-19 09:05) y merges 70c9231, 3c6dcbb, 83b1b96 del mismo día`
- **Por qué importa:** El estado v1.0.0 no tiene autoría por línea ni historial propio; la evaluación de aportes depende de ramas que se enlazaron después.
- **Cómo cerrarlo (S):** No volver a crear raíces nuevas: publicar releases como etiquetas sobre el historial existente y documentar la razón del commit v1.0.0.

### [Medio] H-18 · Los dos repositorios principales viven en cuentas personales
- **Repositorio:** `EQUIPO`
- **Evidencia (inferida):** `REPOSITORIOS-EQUIPOS.md (proyecto principal en propietario/ProyectoFaceAttendEDU y RAPS en propietario/FaceAttendEDU-RAPS); solo fae-docs está en code-sena`
- **Por qué importa:** Si la cuenta de un integrante se borra o se cierra, el equipo pierde el repositorio y el despliegue (los secretos de Actions viven allí). No se pudo comprobar la lista de colaboradores.
- **Cómo cerrarlo (S):** Transferir ambos repositorios a la organización code-sena (o agregar a los tres integrantes y al instructor como admins) y mover los secretos de Actions.

### [Medio] H-19 · El contrato OpenAPI conserva rutas y URLs de una versión anterior
- **Repositorio:** `code-sena/fae-docs`
- **Evidencia:** `07-api/contracts/openapi/01-identity-service.yaml:30 y :54 (Kong en localhost:8080, real 8000), 07-api/contracts/openapi/01-identity-service.yaml:13 (CityController, tabla eliminada en database/01-ms-identity-db/01-ddl/03-tables/008-drop-city-table.yaml del repo principal)`
- **Por qué importa:** Quien genere clientes desde el contrato obtendrá un servicio de ciudades que no existe y una URL base equivocada; mina la confianza en el resto de la documentación.
- **Cómo cerrarlo (M):** Regenerar los OpenAPI desde springdoc/fastify-swagger en CI y validarlos contra kong.yml.

### [Bajo] H-20 · README y comentarios con referencias a archivos y ramas inexistentes
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `README.md:19 (AGENTS.md), README.md:60 (rama chore/production-readiness), README.md:5 (10 microservicios, hay 9), front-end/Web/src/services/api/authApi.js:104-105 (dice que la recuperación responde 404 pero PasswordRecoveryController.java:49 la implementa), DESPLIEGUE.md:98 (rama fix/frontend-mocked-requests)`
- **Por qué importa:** El primer contacto con el repositorio muestra instrucciones que no se pueden seguir.
- **Cómo cerrarlo (S):** Revisar README y comentarios contra el estado real y quitar las referencias al proyecto padre FULL/ y a AGENTS.md.

### [Bajo] H-21 · El token de sesión de la web vive en localStorage
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `front-end/Web/src/api/apiClient.js:57-70 y front-end/Web/src/context/AuthContext.js:45`
- **Por qué importa:** Cualquier XSS lo roba; el móvil sí usa SecureStore (front-end/Mobile/src/storage/TokenStorage.js:10).
- **Cómo cerrarlo (M):** Mantener la expiración corta ya existente y pasar a cookie HttpOnly SameSite si el gateway se sirve bajo el mismo dominio, o endurecer CSP en nginx.conf.

### [Bajo] H-22 · Imagen de la web sin usuario no root, /health/quality público y endpoint de eventos con pérdida silenciosa
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `front-end/Web/Dockerfile:1 (sin USER), back-end/99-api-gateway/kong/kong.yml:139-151 (ruta pública), back-end/01-ms-identity/src/main/java/com/faceattend_edu/identity_service/adapter/out/messaging/DomainEventPublisher.java:19`
- **Por qué importa:** Defectos menores de endurecimiento: nginx como root, métricas agregadas visibles sin sesión y eventos de dominio que se pierden si Kafka cae.
- **Cómo cerrarlo (S):** Usar nginx-unprivileged, proteger /health/quality o dejarlo solo en red interna y adoptar outbox para los eventos.

### [Bajo] H-23 · El nombre del host y el usuario dueño de la base Neon están versionados
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `DESPLIEGUE.md:58 (cadena de conexión con la contraseña como marcador) y DESPLIEGUE.md:141 (usuario faceattend_db_owner)`
- **Por qué importa:** Aunque la contraseña no aparece, el endpoint y el nombre del rol facilitan ataques dirigidos al proveedor si esa contraseña llegó a filtrarse o es débil.
- **Cómo cerrarlo (S):** Sustituir host y usuario por marcadores y mover los datos reales al secreto ENV_PROD_FILE.

### [Bajo] H-24 · Artefactos de herramientas de IA versionados pese al .gitignore
- **Repositorio:** `propietario/ProyectoFaceAttendEDU`
- **Evidencia:** `docs/superpowers/plans/2026-10-12-attendance-kiosk-websocket.md:1 y .claude/skills/faceattend-commit-workflow/SKILL.md:1`
- **Por qué importa:** El patrón .claude está ignorado pero 8 archivos de .claude se versionaron a la fuerza; añaden ruido y exponen flujos internos.
- **Cómo cerrarlo (S):** Quitar docs/superpowers y .claude del índice (git rm --cached) o moverlos a fae-docs 00-governance.

### [Bajo] H-25 · fae-docs sin .gitignore y con README de plantilla
- **Repositorio:** `code-sena/fae-docs`
- **Evidencia:** `README.md:25-35 (pasos 'Fork / Pick your stack / Do not delete instructions' del framework) y SRS FaceAttendEdu.docx en la raíz`
- **Por qué importa:** El README no dice cómo se consulta la documentación de FaceAttend y se versiona un binario sin carpeta propia.
- **Cómo cerrarlo (S):** Reemplazar el bloque de plantilla por una guía de lectura, mover el .docx a 99-archive y agregar .gitignore.

### [Bajo] H-26 · Ramas sin integrar o residuales
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/ProyectoFaceAttendEDU: origin/feature/frontend-settingsWeb (+36 commits, 2026-09-23), feat/mobile-hide-nav-bar y pr/* (contenido ya en main); code-sena/fae-docs: origin/docs/update-rbac-permission-map (+2)`
- **Por qué importa:** Ramas con commits que no están en main confunden qué está terminado; el mapa RBAC por ruta de la rama de docs no se refleja igual en main.
- **Cómo cerrarlo (S):** Fusionar o borrar cada rama; para docs/update-rbac-permission-map, cotejar con 07-api/endpoint-matrix.md de main y cerrar.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 66.7 |
| D2 | Reproducibilidad | 70.0 |
| D3 | Arquitectura y congruencia | 60.7 |
| D4 | Calidad de código | 54.5 |
| D5 | Seguridad | 64.9 |
| D6 | Pruebas | 85.0 |
| D7 | DevOps y despliegue | 86.4 |
| D8 | Datos | 90.0 |
| D9 | API y contratos | 65.0 |
| D10 | Frontend y móvil | 62.5 |
| D11 | IoT y firmware | — |
| D12 | Documentación y trazabilidad | 57.4 |
| D13 | Proceso y colaboración | 68.4 |
| D14 | Extremo a extremo | 50.0 |

## Lo que está bien

- Arquitectura hexagonal y 9 microservicios realmente separados con CI completo en verde (`back-end/SERVICES.md:5-18; .github/workflows/ci.yml:15-157 (matriz Java/Node/Python + web + móvil); API de Actions: CI en success en main y develop el 2026-10-19 y 2026-10-20`)
- Autenticación y autorización aplicadas en el servidor en dos capas, con bcrypt costo 12 y bloqueo de cuenta (`back-end/99-api-gateway/kong/kong.yml:183-280; back-end/01-ms-identity/src/main/java/com/faceattend_edu/identity_service/adapter/in/web/security/AuthTokenFilter.java:147-200; back-end/01-ms-identity/src/main/java/com/faceattend_edu/identity_service/config/SecurityConfig.java:18-22; GET /api/v1/roles sin bearer en el despliegue responde 401`)
- Pipeline de despliegue serio: CI -> migraciones Neon -> EC2 por SSH -> Vercel, con entorno protegido y TLS por Caddy (`back-end/99-api-gateway/docker-compose.yml:31-33; docker-compose.prod.yml:31-52; .github/workflows/deploy-production.yml:26-58; https://faceattend-edu.duckdns.org responde (200 en /health/quality)`)
- Migraciones Liquibase por schema con restricciones, índices y rollbacks, y modelo documentado en DBML (`database/05-ms-attendance-db/01-ddl/03-tables/001-create-attendance-record-table.yaml:9-29; database/faceattend_edu_mr_v4.dbml:1`)
- Biometría con prueba de vida firmada, limitación de tasa, canal WebSocket idempotente y pruebas propias (`back-end/06-ms-biometric/infrastructure/web/routers/facial_router.py:367-420; back-end/06-ms-biometric/infrastructure/web/routers/ws_router.py:379; back-end/06-ms-biometric/tests/unit/web/test_liveness_hardening.py:1`)
- Pruebas con afirmaciones reales (173 casos solo en identity) y manejador de errores uniforme (`back-end/01-ms-identity/src/test/java/com/faceattend_edu/identity_service/application/usecase/AuthenticateUserUseCaseImplTest.java:77-93; back-end/01-ms-identity/src/main/java/com/faceattend_edu/identity_service/adapter/in/web/controller/GlobalExceptionHandler.java:44-52`)
- Documentación honesta: reconoce lo no implementado y trae matriz de trazabilidad verificada contra el código (`04-requirements/traceability-matrix.md:20-30; 05-architecture/decisions/records/ADR-016-encryption-at-rest.md:5; 07-api/authentication.md:10-11`)
- Aportes equilibrados entre los tres integrantes y actividad en las 9 semanas (`git shortlog -sne --all --no-merges: Integrante 8 97, Integrante 2 79, Integrante 1 76 commits en el repo principal; commits en cada semana desde el 25 de agosto`)

## Lo que no se pudo verificar

- Estado vivo de MongoDB, Redis, Kafka y Kong Admin (puertos 27017, 6379, 9092, 8001) en el host del despliegue — Solo se permite un GET de solo lectura contra la URL declarada; sondear puertos fuera de ese alcance no se hizo. H-02 se apoya en la configuración y la guía versionadas.
- Que las cuentas semilla (H-01) existan hoy en la base Neon de producción — Exige un POST /auth/login o acceso a la base; la evidencia es que el pipeline ejecuta liquibase update sin filtrar contextos.
- Flujo autenticado extremo a extremo contra el despliegue (login, asistencia) — Requiere POST con credenciales; solo se hicieron GET sin sesión (200 en /health/quality, 401 en /api/v1/roles).
- Compilación, pruebas y contenedores de los aprendices — Prohibido ejecutar el código; se confió en el estado de CI por la API de Actions.
- Calidad de código D4 por muestreo — Se leyeron con profundidad ms-identity (6389 líneas, el mayor) y ms-biometric (4581, segundo) más el filtro, controladores y rutas de academic; los otros 7 servicios y la web de 74 mil líneas se revisaron por búsquedas.
- Lista de colaboradores y permisos de los repositorios, y GitHub Project externo — La API exige permisos de escritura; solo se contaron issues y PR.
- Si Integrante 8 corresponde a la misma persona que los autores Integrante@... con otros alias — La asociación se hizo por correo y nombre de usuario; no se tienen datos de matrícula.
- Servicio Notification y Quality en profundidad (Fastify) — Fuera del muestreo de dos servicios; se validó su existencia, rutas en Kong, pruebas y Dockerfile.
