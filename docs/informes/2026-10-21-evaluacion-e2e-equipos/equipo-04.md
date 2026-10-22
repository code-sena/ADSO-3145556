# Equipo 4 · Guardián Escolar — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 44/100 · Juicio D**

## Veredicto

El sistema tiene una base técnica seria: arquitectura hexagonal, JWT RS256 con JWKS validado en Kong, migraciones Liquibase por esquema, pruebas unitarias con afirmaciones reales y un flujo de escaneo QR con alertas bien resuelto. Pero hoy no se puede entregar: el panel de administración de personas llama a rutas /user/api/* que el servicio de usuarios no expone (usa /api/v1/*), ms-fleet no compila en develop, el seguimiento GPS depende de una URL y un IMEI incrustados y de un servicio sin autenticación, y ningún endpoint verifica el rol del usuario. Hay además secretos expuestos (un .env con credenciales de RDS en el historial público, el Admin de Kong abierto, claves de demostración sembradas siempre) y una base de datos compartida que contradice el diseño de microservicios. Casi todas las GitHub Actions de despliegue fallan, el 78 % de los commits son de una sola persona, y las ramas con lo necesario (database en sg-db) no están en main. Está lejos de cerrar en seguridad, contratos y despliegue, pero cerca en diseño y pruebas: los cambios son concretos y acotados.

## Plan para cerrar las brechas

1. **Rotar y retirar los secretos expuestos: credenciales de RDS/Liquibase del .env histórico, clave de demo sembrada, clave de Firebase sin restricción; limpiar el historial de GuardianEscolar.** (esfuerzo M) — se cierra cuando: Las credenciales antiguas ya no funcionan; un escaneo de secretos (gitleaks) sobre todo el historial de GuardianEscolar y sg-db no devuelve hallazgos; las semillas de prueba solo corren con el contexto dev.
2. **Cerrar la exposición: no publicar el Admin de Kong ni los puertos de los servicios; todo tráfico entra por Kong.** (esfuerzo M) — se cierra cuando: Desde fuera de la red del compose, curl a los puertos 8001, 8002, 8081, 8084, 8091 y 8080 de cada servicio no conecta; solo responde Kong.
3. **Autorizar por permiso en todas las acciones de escritura (fleet, route, school-management, notification) y endurecer Kong para rechazar refresh tokens.** (esfuerzo M) — se cierra cuando: Pruebas automáticas muestran 403 al llamar DELETE de bus, ruta y colegio con un token de estudiante o acudiente, y 401 en Kong con un refresh token.
4. **Alinear el prefijo de API de personas (/api/v1) entre web, móvil, gateway y sg-ms-user-management, y añadir un smoke test de contrato.** (esfuerzo S) — se cierra cuando: Con el sistema levantado, crear un estudiante, una familia y un conductor desde la web devuelve 2xx y se ve en la lista; el smoke test falla si una ruta del cliente no existe en el servicio.
5. **Rehacer el seguimiento GPS: pasar por Kong con JWT, resolver IMEI desde el bus de la ruta, persistir ubicaciones y quitar el endpoint de depuración y la URL fija.** (esfuerzo M) — se cierra cuando: Un acudiente ve el bus de la ruta de su hijo en el mapa con su propio token; llamar a /api/gps sin token da 401 y POST /api/gps/test no existe; gps.js no contiene URL ni IMEI.
6. **Fusionar la rama database en main de sg-db, cerrar las ramas huérfanas y arreglar develop de fleet.** (esfuerzo S) — se cierra cuando: main de sg-db crea Iam.TermsAcceptance, Phone BIGINT y los tipos de incidente en una base nueva; develop de fleet compila; no quedan ramas con trabajo sin integrar.
7. **Poner en verde la CI: workflows de build y pruebas por PR en todos los repos (incluidos web, móvil, gps y gateway) y protección de ramas.** (esfuerzo M) — se cierra cuando: Cada PR a develop/main ejecuta compilación y pruebas y no se puede fusionar si fallan; las últimas 5 ejecuciones de cada repo son success.
8. **Escribir los README y un documento de arranque del sistema con variables, red, orden de servicios y claves JWT.** (esfuerzo M) — se cierra cuando: Una persona ajena al equipo levanta Kong, IAM, un servicio y el web siguiendo solo los README y completa el inicio de sesión en menos de 60 minutos.
9. **Eliminar las lecturas y FK entre esquemas de distintos servicios, usando gRPC y solo identificadores.** (esfuerzo L) — se cierra cuando: Ningún DbContext declara tablas de otro esquema y sg-db no tiene FK entre esquemas de servicios distintos; la tabla de forgot-information está en sg-db.
10. **Añadir healthchecks y usuario no root, y corregir los defectos de IAM (logs, refresh en el cuerpo, revocación persistente).** (esfuerzo S) — se cierra cuando: Cada servicio responde /health 200 y su imagen define HEALTHCHECK; docker inspect de IAM y GPS muestra usuario no root; el log de login no contiene correos completos.
11. **Repartir el trabajo y la revisión entre los tres integrantes y limpiar el repositorio principal (video, copias, andamiaje MAUI, gps-backend duplicado).** (esfuerzo M) — se cierra cuando: Ningún integrante supera el 60 % de los commits del último mes, todo PR tiene una revisión de otro integrante, y el .git de GuardianEscolar pesa menos de 30 MB.
12. **Declarar los topics faltantes y documentar el contrato de eventos y las dependencias entre repos (incluyendo sg-ms-user-management y sg-documentation en la tabla oficial).** (esfuerzo S) — se cierra cuando: topics.conf contiene todos los topics que consumen o publican los servicios y el README del broker lista productor, consumidor y esquema de cada uno; la tabla de repositorios del equipo incluye los 13 repos reales.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Inicio de sesión y renovación de sesión (web y móvil) | — |
| Roto | Administrar personas: estudiantes, familias, conductores y acudientes | Gateway a servicio: la web y el móvil llaman /user/api/students (y families, drivers, parents, admins); Kong quita /user y reenvía /api/students, pero los controladores de sg-ms-user-management están en api/v1/students|families|parents|drivers. Resultado: 404 en todo el CRUD de personas. Además ese repositorio no figura en la lista declarada por el equipo. |
| Roto | Flota y rutas: registrar bus, asignar conductor, crear ruta y paradas, asignar estudiante | Construcción del servicio de flota en develop: BusDtos.cs declara dos veces el record BusAssignedDto (error de compilación CS0101), por lo que ni la imagen ni las pruebas de ms-fleet se construyen en este SHA; el arreglo existe en main (PR #28) pero develop no lo trae. Los contratos web-ruta y web-flota sí coinciden. |
| Completo | Escaneo de QR del estudiante y alerta push al acudiente | — |
| Roto | Seguimiento en vivo del bus (ubicación GPS en el mapa) | Móvil a servicio: la app consulta una URL fija de una EC2 (sslip.io) con un IMEI fijo y sin cabecera Authorization; por el gateway Kong respondería 401 (/api/gps no es ruta pública) y directo al servicio queda sin autenticación. El servicio guarda las ubicaciones en un ConcurrentHashMap, no en las tablas Gps.* de sg-db, así que la flota (Fleet.Bus.GpsDeviceId) y la ubicación nunca se enlazan y un reinicio pierde todo. |

## Hallazgos

### [Bloqueante] H-01 · Credenciales de RDS/Liquibase en el historial público de GuardianEscolar
- **Repositorio:** `school-guardian-project/GuardianEscolar`
- **Evidencia:** `.gitignore:2 (hoy ignora .env); commit 31a0450e (2026-04-07, Integrante, 'chore: environment variables') agregó un .env raíz con host RDS, nombre de BD, usuario y clave de Liquibase (valores ««redactado»»); alcanzable desde main y develop; repositorio público.`
- **Por qué importa:** Cualquiera puede recuperar el host de la base en AWS y las credenciales de migración; borrar el archivo en un commit posterior no lo elimina del historial.
- **Cómo cerrarlo (M):** Rotar ya las credenciales de la base y del usuario Liquibase, confirmar que RDS no es accesible desde Internet, reescribir el historial (git filter-repo) o declarar el repositorio comprometido y crear uno nuevo; activar secret scanning y push protection.

### [Bloqueante] H-06 · La gestión de personas llama rutas que el servicio de usuarios no tiene
- **Repositorio:** `EQUIPO`
- **Evidencia:** `GuardianEscolar:dvlp-front/dvlp-web/src/app/core/services/students.service.ts:11 (/user/api/students) y dvlp-front/dvlp-movil/guardian-escolar/src/core/services/userApi.js:3; sg-ms-api-gateway:kong/kong.yml:61-66 (strip_path /user); school-guardian-project/sg-ms-user-management (main, leído por API): StudentController api/v1/students línea 13, FamilyController api/v1/families línea 11, ParentController api/v1/parents línea 13, DriverController api/v1/drivers línea 13`
- **Por qué importa:** Sin esas rutas no se pueden crear estudiantes, familias, conductores ni acudientes desde la web: el alta de usuarios, base de todos los demás flujos, devuelve 404.
- **Cómo cerrarlo (S):** Elegir un único prefijo (recomendado /api/v1) y alinearlo en web, móvil, gateway y servicios; añadir una prueba de contrato (o un smoke test con Kong levantado) que llame cada ruta del cliente.

### [Crítico] H-02 · Admin API de Kong publicado en todas las interfaces
- **Repositorio:** `school-guardian-project/sg-ms-api-gateway`
- **Evidencia:** `docker-compose.yml:15 (KONG_ADMIN_LISTEN en 0.0.0.0:8001) y :23 (puerto 8001 publicado; también 8002 de la GUI)`
- **Por qué importa:** El Admin API de Kong permite cambiar rutas y plugins (en modo declarativo, cargar una configuración nueva): quien lo alcance puede desactivar la validación JWT de todo el sistema.
- **Cómo cerrarlo (S):** No publicar 8001/8002; escucharlos solo en 127.0.0.1 o en la red interna, y cerrar el grupo de seguridad de la EC2 a esos puertos.

### [Crítico] H-03 · Ningún endpoint de los servicios .NET autoriza por rol o permiso
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-ms-fleet:src/ms-fleet.Api/Infrastructure/Controller/BusController.cs:127 (DELETE) y src/ms-fleet.Api/Infrastructure/Security/GatewayAuthentication.cs:103 (RequirePermissionAttribute definido y sin un solo uso); sg-ms-route:src/ms-route.Api/Program.cs:53 ('no hay autorización'); sg-ms-school-management:src/ms-school-management.Api/School/Infrastructure/Controller/SchoolController.cs:125; búsqueda de usos de RequirePermission en fleet, route, school-management y notification: 0`
- **Por qué importa:** Un estudiante o acudiente con sesión válida puede borrar buses, rutas, paradas y colegios o cambiar su estado llamando directo a la API; los permisos del catálogo solo ocultan menús en la web.
- **Cómo cerrarlo (M):** Aplicar [RequirePermission("buses.manage")] y equivalentes (el catálogo ya existe en el JWT) a cada acción de escritura, añadir pruebas de 403 por rol y exigir [Authorize] por defecto.

### [Crítico] H-04 · Los servicios confían en cabeceras X-User-* y están publicados sin pasar por Kong
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-ms-fleet:docker-compose.yml:9 ('8080' sin host fijo); sg-ms-notification:docker-compose.yml:9 (8081:8080); sg-ms-forgot-information:docker-compose.yml:10 (8091:8080); sg-ms-gps:docker-compose.yml:9 (8084:8080); sg-ms-fleet:src/ms-fleet.Api/Infrastructure/Security/GatewayAuthentication.cs:37`
- **Por qué importa:** Quien llegue al puerto del servicio puede enviar X-User-Id y X-User-Role arbitrarios y suplantar a cualquier usuario o al superadministrador; la seguridad completa depende de que nadie rodee a Kong.
- **Cómo cerrarlo (M):** Quitar las publicaciones de puertos de los servicios (solo red interna), o firmar/validar la identidad en cada servicio (JWKS) en vez de confiar en cabeceras.

### [Crítico] H-05 · Servicio GPS sin autenticación y con endpoint de depuración que escribe ubicaciones
- **Repositorio:** `school-guardian-project/sg-ms-gps`
- **Evidencia:** `src/main/java/gps_backend/adapters/in/rest/GpsLocationController.java:23 (GET ubicación por IMEI sin token); src/main/java/gps_backend/adapters/in/rest/GpsTestController.java:27 (POST /api/gps/test/{imei} fabrica una ubicación); docker-compose.yml:9-10 (8084 y 8842 publicados)`
- **Por qué importa:** La ubicación en tiempo real de buses escolares con menores a bordo es accesible a quien conozca un IMEI, y cualquiera puede inyectar posiciones falsas.
- **Cómo cerrarlo (M):** Poner el servicio solo detrás de Kong con JWT, eliminar GpsTestController del código desplegable (o limitarlo al perfil de pruebas) y validar que el IMEI pertenece a un bus del tenant del usuario.

### [Alto] H-07 · El seguimiento GPS del móvil usa URL e IMEI incrustados y no envía token
- **Repositorio:** `school-guardian-project/GuardianEscolar`
- **Evidencia:** `dvlp-front/dvlp-movil/guardian-escolar/src/core/config/gps.js:2 (URL sslip.io de EC2) y :3 (IMEI fijo); dvlp-front/dvlp-movil/guardian-escolar/src/core/services/GpsService.js:32 (fetch sin Authorization)`
- **Por qué importa:** El mapa solo funciona contra un servidor y un bus concretos, no para cada familia; cambia de dirección si la EC2 se recrea y la llamada no pasa por el gateway.
- **Cómo cerrarlo (M):** Resolver el IMEI desde el bus de la ruta del estudiante (Fleet) y la base desde EXPO_PUBLIC_API_URL, consumir /api/gps por Kong con el Bearer de la sesión.

### [Alto] H-08 · El gateway acepta refresh tokens como si fueran access tokens
- **Repositorio:** `school-guardian-project/sg-ms-api-gateway`
- **Evidencia:** `kong/plugins/sg-jwt/handler.lua:135 (solo comprueba alg), :168-183 (exp, nbf, iss, aud, sub) sin verificar el claim type; sg-ms-iam:src/main/java/com/school_guardian/ms_iam/infrastructure/security/JwtTokenProvider.java:113 sí lo exige en IAM`
- **Por qué importa:** Un refresh token (7 días, devuelto además en el cuerpo del login) sirve como credencial en todas las rutas protegidas; una filtración da acceso mucho más largo que el previsto de 1 hora.
- **Cómo cerrarlo (S):** Rechazar en el plugin los tokens con type distinto de access y evaluar consultar la lista de revocación (hoy en memoria, InMemoryTokenDenyList.java:16).

### [Alto] H-09 · Base de datos compartida entre servicios, con llaves foráneas y lecturas cruzadas
- **Repositorio:** `EQUIPO`
- **Evidencia:** `code-sena/sg-db:ms-fleet-db/01-ddl/04-alter/001-add-fk-bus.sql:5 y :8; sg-ms-fleet:src/ms-fleet.Api/Infrastructure/Persistence/Context/FleetContext.cs:35; sg-ms-notification:src/ms-notification.Api/Infrastructure/Persistence/Context/NotificationContext.cs:32 y :36; code-sena/sg-db:docker-compose.yaml:30`
- **Por qué importa:** Contradice la independencia de microservicios que documenta el diseño: un cambio de esquema en IAM o user-management rompe notificación, flota y rutas, y no se pueden desplegar por separado.
- **Cómo cerrarlo (L):** Cortar las lecturas cruzadas (pedir los datos por gRPC, que ya existe entre servicios), guardar solo identificadores sin FK entre esquemas y documentar la propiedad de cada tabla.

### [Alto] H-10 · La rama evaluada (main) de sg-db está atrasada respecto de lo que el código necesita
- **Repositorio:** `code-sena/sg-db`
- **Evidencia:** `ms-user-management-db/01-ddl/03-tables/001-create-table-person.sql:8 (Phone INT; en la rama database es BIGINT); la tabla Iam.TermsAcceptance (006-create-table-terms-acceptance.sql) y los tipos de alerta de incidentes (006-insert-incident-alert-types.sql) solo existen en origin/database (+24 commits, 15-oct)`
- **Por qué importa:** Una base levantada desde main no tiene la tabla que IAM mapea para aceptar términos ni los tipos de incidente de FR-8, y el teléfono de un móvil colombiano no cabe en INT.
- **Cómo cerrarlo (S):** Fusionar 'database' en main mediante PR con revisión, borrar la rama y decidir si los esquemas Audit, Exceptional y Settings que ahí se eliminan deben seguir.

### [Alto] H-11 · ms-fleet no compila en develop: record duplicado
- **Repositorio:** `school-guardian-project/sg-ms-fleet`
- **Evidencia:** `src/ms-fleet.Api/Application/Dtos/BusDtos.cs:13 y :18 (BusAssignedDto declarado dos veces); corregido en main (PR #28) pero no en develop (rama hotfix/duplicate-bus-assigned-dto +1)`
- **Por qué importa:** develop, la rama de integración, no se puede construir ni probar; el workflow de despliegue de fleet falla en 5 de 5 ejecuciones del 20-oct.
- **Cómo cerrarlo (S):** Traer el arreglo de main a develop y proteger ambas ramas exigiendo que el workflow de build y pruebas pase antes de fusionar.

### [Alto] H-12 · La integración continua casi no corre y cuando corre falla
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-ms-iam:.github/workflows/java-iam-deploy.yml:15 (6 de 6 ejecuciones failure el 20-oct); Deploy School Management y Deploy Forgot Information: failure y startup_failure; Deploy Route y Deploy Notification: un éxito y fallos antes y después; GuardianEscolar, sg-db, broker-kafka, sg-ms-api-gateway y sg-ms-gps sin workflows`
- **Por qué importa:** No hay garantía automática de que lo que se fusiona compile, pase pruebas o se despliegue; las pruebas solo corren dentro del paso previo al despliegue (school-guardian-project/.github, deploy-dotnet.yml).
- **Cómo cerrarlo (M):** Separar un workflow de CI (build + test + lint) por repositorio que corra en cada PR, dejar el despliegue para main y arreglar las causas de los fallos recientes; añadir CI a web, móvil, gps y gateway.

### [Alto] H-13 · Clave de demostración compartida sembrada en todos los entornos
- **Repositorio:** `code-sena/sg-db`
- **Evidencia:** `.env.example:5 (variable de clave de demo con valor ««redactado»»); ms-iam-db/02-dml/00-inserts/010-insert-test-profiles.sql:2 (comentario con la misma clave para 57 perfiles); ms-iam-db/02-dml/00-inserts/changelog.yaml:1 (sin contextos de entorno)`
- **Por qué importa:** Las cuentas de administrador, superadministrador y 57 perfiles de prueba existirían en producción con una clave conocida públicamente.
- **Cómo cerrarlo (S):** Mover las semillas de prueba a un contexto Liquibase 'dev'/'test', no publicar la clave en .env.example ni comentarios, y crear el superadministrador de producción con una clave generada fuera del repositorio.

### [Medio] H-14 · google-services.json de Firebase versionado con su clave de API
- **Repositorio:** `school-guardian-project/GuardianEscolar`
- **Evidencia:** `dvlp-front/dvlp-movil/guardian-escolar/google-services.json:18 (clave de API ««redactado»»)`
- **Por qué importa:** Las claves de Firebase no son secretas por diseño, pero sin restricciones de paquete/firma pueden usarse abusivamente y exponen el identificador del proyecto.
- **Cómo cerrarlo (S):** Restringir la clave en la consola de Google a la app (paquete y huella SHA-1) y entregar el archivo por EAS secrets en lugar del repositorio.

### [Medio] H-15 · Video de entrevistas y encuestas con datos de personas en repositorio público
- **Repositorio:** `school-guardian-project/GuardianEscolar`
- **Evidencia (inferida):** `docs/SRS/gestor de proyectos - MER.png (misma carpeta que docs/Entrevistas - Guardian Escolar.mp4, 88 MB, y docs/Guardian Escolar (respuestas).xlsx)`
- **Por qué importa:** Si el video o las respuestas identifican a entrevistados, hay tratamiento de datos personales sin autorización visible (Ley 1581 de 2012). No se abrió el contenido: se infiere por el nombre.
- **Cómo cerrarlo (S):** Confirmar con el equipo qué contiene, retirarlos del repositorio y del historial si hay datos identificables, y guardar evidencia de campo en un almacenamiento privado.

### [Medio] H-16 · Seis de once repositorios sin README útil y documentación desfasada
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-db:README.md:1, GuardianEscolar:README.md:1, sg-ms-api-gateway:README.md:1, sg-ms-gps:README.md:1, sg-ms-notification:README.md:1 y sg-ms-route:README.md:1 solo traen el título; sg-ms-fleet:README.md:20 describe Driver/ y Vehicle/ y Kafka que no existen; GuardianEscolar:dvlp-front/dvlp-web/ARCHITECTURE.md:35 cita archivos inexistentes`
- **Por qué importa:** Un integrante nuevo no puede levantar ni entender el sistema; la documentación que existe contradice el código.
- **Cómo cerrarlo (M):** Un README por repositorio con propósito, dependencias, variables, comandos de arranque y de pruebas, y un README raíz con el orden de arranque de todo el sistema (red, sg-db, Kafka, IAM, servicios, Kong).

### [Medio] H-17 · Solo pruebas unitarias con dobles en memoria; los desajustes entre capas pasaron sin detectar
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-ms-route:tests/ms-route.Tests/Fakes/InMemoryRouteRepository.cs:1 y equivalentes en fleet, notification, school-management; ninguna prueba de integración ni de contrato; gateway, sg-db y broker-kafka sin pruebas`
- **Por qué importa:** El 404 de /user/api/* y el record duplicado de fleet son errores que una prueba de contrato o un build en CI habrían atrapado.
- **Cómo cerrarlo (L):** Añadir pruebas de integración con Testcontainers (SQL Server) en un servicio piloto y un smoke test e2e con Kong que recorra los 5 flujos principales.

### [Medio] H-18 · Concentración de aportes: 78 % de los commits en una sola persona
- **Repositorio:** `EQUIPO`
- **Evidencia:** `git shortlog -sne --all --no-merges en los 11 clones, agrupado por correo: Integrante 1 1.035 de 1.319; Integrante 2 161; Integrante 3 123`
- **Por qué importa:** Un solo integrante concentra casi todo el conocimiento y el equipo de tres personas no demuestra reparto de trabajo; en repos como sg-ms-notification el 100 % es de la misma persona.
- **Cómo cerrarlo (M):** Repartir la propiedad de servicios (revisión cruzada obligatoria en PR) y registrar quién implementa cada historia; documentar los aportes de quienes trabajan sin commit (diseño, documentación).

### [Medio] H-19 · Repositorio de 161 MB por binarios y andamiaje abandonado
- **Repositorio:** `school-guardian-project/GuardianEscolar`
- **Evidencia:** `.gitignore:1; commit 50b9d25d (2026-04-22, Integrante 2) con 2.382 archivos, 2.338 de ellos bin/obj de un proyecto .NET MAUI reemplazado por Expo; docs/ con video de 88 MB, .tmp y copias`
- **Por qué importa:** Clonar el repositorio es lento y el historial conserva basura y material sensible; dificulta revisar y mantener.
- **Cómo cerrarlo (M):** Mover docs pesados a un almacenamiento aparte, limpiar el historial junto con H-01 y borrar el andamiaje MAUI y gps-backend duplicado (existe sg-ms-gps).

### [Medio] H-20 · IAM registra correos en logs, devuelve el refresh token en el cuerpo y revoca en memoria
- **Repositorio:** `school-guardian-project/sg-ms-iam`
- **Evidencia:** `src/main/java/com/school_guardian/ms_iam/application/usecase/LoginService.java:40 (log del correo en cada intento); src/main/java/com/school_guardian/ms_iam/infrastructure/web/AuthController.java:76-78 (refreshToken también en el JSON); src/main/java/com/school_guardian/ms_iam/infrastructure/security/InMemoryTokenDenyList.java:16 (ConcurrentHashMap)`
- **Por qué importa:** Datos personales en logs, un refresh token expuesto a JavaScript pese a la cookie HttpOnly y un cierre de sesión que se pierde al reiniciar o escalar el servicio.
- **Cómo cerrarlo (S):** Enmascarar el correo en logs, no devolver el refresh token a clientes web y guardar la lista de revocación en BD o Redis.

### [Medio] H-21 · Cambio de correo y de teléfono salen del gateway por un túnel directo
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-ms-api-gateway:kong/kong.yml:241-246 (solo /api/v1/password hacia ms-forgot-information); GuardianEscolar:docker-compose.yml:23-26 (cloudflared-forgot a ms-forgot-information:8080); GuardianEscolar:dvlp-front/dvlp-web/src/app/core/services/change-email.service.ts:17`
- **Por qué importa:** Esos flujos no pasan por el CORS, el rate limit ni el JWT de Kong y exigen una segunda URL pública (API_FORGOT_INFORMATION_URL) en cada cliente.
- **Cómo cerrarlo (S):** Añadir en Kong las rutas /api/v1/email/change y /api/v1/phone/change (autenticadas) y retirar el túnel directo.

### [Medio] H-22 · Trabajo y ramas sin integrar
- **Repositorio:** `EQUIPO`
- **Evidencia:** `GuardianEscolar: ramas feature/mobile-gps-tracking (+14), feature/vercel-deploy (+3), feature/web-bus-list-status (+2), feature/web-bus-status-toggle (+1) y docs (+34 commits, 538 archivos de diferencia con main); sg-ms-notification: feat/incident-reporting, feat/report-incident y test/notification-scan-process (12 commits cada una, mismo contenido); sg-db: database (+24)`
- **Por qué importa:** Hay funcionalidad sin integrar (por ejemplo, despliegue en Vercel y seguimiento GPS móvil) y ramas idénticas duplicadas que ocultan cuál es la vigente.
- **Cómo cerrarlo (S):** Fusionar o cerrar cada rama con PR, borrar las duplicadas y dejar en main/develop lo que se entrega.

### [Medio] H-23 · Sin healthchecks ni endpoints de salud; contenedores Java como root
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-ms-iam:Dockerfile:7-11 y sg-ms-gps:Dockerfile:9-17 (sin USER); ninguna imagen define HEALTHCHECK y ningún servicio expone /health`
- **Por qué importa:** El orquestador no distingue un servicio vivo de uno caído; un fallo de dependencia pasa inadvertido hasta que un usuario lo nota.
- **Cómo cerrarlo (S):** Añadir /health (ASP.NET HealthChecks y Spring Actuator), HEALTHCHECK en los Dockerfile y USER no root en las imágenes Java.

### [Medio] H-24 · Errores por texto de excepción y rutas de API sin versión
- **Repositorio:** `school-guardian-project/sg-ms-route`
- **Evidencia:** `src/ms-route.Api/Program.cs:80 (404 si el mensaje contiene 'not found'); src/ms-route.Api/Infrastructure/Controller/RouteAssignmentController.cs:65 (BadRequest(ex.Message)); src/ms-route.Api/Infrastructure/Controller/RouteController.cs:9 (/api/routes sin versión)`
- **Por qué importa:** Cambiar un mensaje cambia el código HTTP; la inconsistencia de versionado entre servicios es la raíz del desajuste de H-06.
- **Cómo cerrarlo (M):** Excepciones de dominio tipadas con un único manejador ProblemDetails y una política de versionado (/api/v1) para todos los servicios.

### [Medio] H-25 · Levantar el sistema completo no es reproducible desde los repositorios
- **Repositorio:** `EQUIPO`
- **Evidencia:** `sg-ms-iam:src/main/resources/application.properties.example:1 (no existe application.properties); GuardianEscolar:dvlp-front/dvlp-web/src/environments/environment.ts:2 (API_URL sin .env.example); GuardianEscolar:dvlp-front/dvlp-movil/guardian-escolar/.env.example:4 (rutas de notificación vacías); code-sena/sg-db:docker-compose.yaml:166 (red externa); claves RSA de JWT sin documentar`
- **Por qué importa:** Cada repositorio exige adivinar variables, red y orden; sin eso, IAM arranca con claves efímeras y los tokens se invalidan en cada reinicio.
- **Cómo cerrarlo (M):** Un docker-compose de sistema (o script) y documento de arranque con todas las variables en .env.example, incluyendo JWT_PRIVATE_KEY/JWT_PUBLIC_KEY y las rutas EXPO_PUBLIC_NOTIFICATION_*.

### [Medio] H-28 · La tabla de verificaciones se crea con SQL al arrancar, fuera de sg-db
- **Repositorio:** `school-guardian-project/sg-ms-forgot-information`
- **Evidencia:** `src/ms-forgot-information.Api/Shared/Infrastructure/Persistence/VerificationRequestSchemaInitializer.cs:70 (ExecuteSqlRawAsync)`
- **Por qué importa:** Esquema no versionado ni revisado en Liquibase; el usuario de la aplicación necesita permisos DDL en producción.
- **Cómo cerrarlo (S):** Mover la tabla a un changelog de sg-db y quitar el inicializador.

### [Bajo] H-26 · Topics de Kafka usados que el broker no declara
- **Repositorio:** `EQUIPO`
- **Evidencia:** `broker-kafka:init/topics.conf:1 (6 topics: student/driver/admin/parent.created, profile.created, student.scanned); sg-ms-iam:src/main/java/com/school_guardian/ms_iam/infrastructure/messaging/PersonStatusChangedConsumer.java:23 (user.status.changed) y PersonCreatedConsumer.java:28 (admin.school.updated)`
- **Por qué importa:** Dependen de la creación automática de topics del broker; el contrato de eventos no está documentado ni versionado en un solo lugar.
- **Cómo cerrarlo (S):** Declarar todos los topics (incluido el de incidentes) y documentar productor, consumidor y esquema de cada evento.

### [Bajo] H-27 · Código duplicado y de desarrollo dentro de la app
- **Repositorio:** `school-guardian-project/GuardianEscolar`
- **Evidencia:** `dvlp-front/dvlp-web/src/app/core/services/auth.interceptor.ts:1 y dvlp-front/dvlp-web/src/app/core/interceptors/auth.interceptor.ts:10 (dos interceptores); dvlp-front/dvlp-movil/guardian-escolar/App.js:3 (RoleSwitcherProvider de desarrollo) y 24 console.log/error en el móvil; gps-backend/gps-backend duplica sg-ms-gps`
- **Por qué importa:** Mantenimiento doble y riesgo de que utilidades de desarrollo lleguen a producción.
- **Cómo cerrarlo (S):** Dejar un interceptor, condicionar el RoleSwitcher a __DEV__, usar un logger con niveles y borrar gps-backend.

### [Bajo] H-29 · Erratas persistentes en nombres de tablas y columnas
- **Repositorio:** `code-sena/sg-db`
- **Evidencia:** `ms-fleet-db/01-ddl/03-tables/004-create-table-driver-assignments.sql:1 (DriverAssignemts); ms-fleet-db/01-ddl/03-tables/003-create-table-bus.sql:3 (CampuseId, repetido en Iam.Profile y Route.Route)`
- **Por qué importa:** Los nombres erróneos ya se propagaron a entidades, FK y consultas; corregirlos más tarde cuesta más.
- **Cómo cerrarlo (M):** Renombrar con un changelog de migración y actualizar entidades en una sola versión coordinada.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 72.6 |
| D2 | Reproducibilidad | 50.1 |
| D3 | Arquitectura y congruencia | 45.2 |
| D4 | Calidad de código | 65.2 |
| D5 | Seguridad | 57.1 |
| D6 | Pruebas | 48.6 |
| D7 | DevOps y despliegue | 26.8 |
| D8 | Datos | 50.0 |
| D9 | API y contratos | 75.0 |
| D10 | Frontend y móvil | 45.8 |
| D11 | IoT y firmware | — |
| D12 | Documentación y trazabilidad | 66.7 |
| D13 | Proceso y colaboración | 46.9 |
| D14 | Extremo a extremo | 0.0 |

## Lo que está bien

- IAM con cripto y sesión bien resueltas (`sg-ms-iam:src/main/java/com/school_guardian/ms_iam/infrastructure/security/JwtTokenProvider.java:47 (RS256 + kid), src/main/java/com/school_guardian/ms_iam/infrastructure/config/PasswordEncoderConfig.java:12 (BCrypt), src/main/java/com/school_guardian/ms_iam/infrastructure/web/AuthController.java:69-71 (cookie HttpOnly, Secure, SameSite=Strict en la ruta de refresh)`)
- Plugin Lua propio en Kong que valida firma, iss, aud, exp y descarta X-User-* entrantes (`sg-ms-api-gateway:kong/plugins/sg-jwt/handler.lua:104-183`)
- Flujo de escaneo QR con credencial firmada, validación de ruta del conductor y tabla de estados idempotente (`sg-ms-notification:src/ms-notification.Api/Application/UseCase/ProcessScanService.cs:60-110 y src/ms-notification.Api/Application/Security/QrTokenSigner.cs (HMAC + comparación en tiempo constante)`)
- Esquema de datos versionado con Liquibase por servicio, con FK, CHECK, únicos y rollbacks previstos (`code-sena/sg-db:ms-route-db/changelog/changelog-master.yaml y ms-route-db/01-ddl/04-alter/*.sql`)
- Pruebas unitarias con afirmaciones reales y en volumen razonable (web 146 casos y 313 expect; servicios .NET y Java con decenas de Assert) (`sg-ms-route:tests/ms-route.Tests/Route/CreateRouteServiceTests.cs; sg-ms-notification:tests/ms-notification.Tests/ProcessScanServiceTests.cs; GuardianEscolar:dvlp-front/dvlp-web/src/app/core/services/auth.service.spec.ts`)
- Recuperación de contraseña y cambios de contacto con OTP hasheado (HMAC + pepper), límite de intentos y README/TESTING completos (`sg-ms-forgot-information:src/ms-forgot-information.Api/Shared/Application/Otp/SecretHasher.cs:15, Program.cs:92 (política otp-public), README.md:66`)
- Arquitectura hexagonal consistente en los servicios .NET (puertos In/Out, casos de uso, repositorios) y límite de escritura con rate limiting (`sg-ms-route:src/ms-route.Api/Domain/Ports/Out/IRouteRepository.cs; sg-ms-fleet:src/ms-fleet.Api/Infrastructure/Controller/BusController.cs:49 ([EnableRateLimiting("writes")])`)
- Contratos web-servicio correctos en flota, rutas y colegios (campos, verbos y rutas coinciden) (`GuardianEscolar:dvlp-front/dvlp-web/src/app/core/services/routes.service.ts:38-60 contra sg-ms-route:src/ms-route.Api/Infrastructure/Controller/RouteAssignmentController.cs:119-134`)
- Sesión en el cliente: token de acceso en memoria, refresh con single-flight en web y SecureStore en móvil (`GuardianEscolar:dvlp-front/dvlp-web/src/app/core/services/auth.service.ts:104; dvlp-front/dvlp-movil/guardian-escolar/src/core/services/authService.js:45`)
- Documentación de producto abundante y honesta en sg-documentation (SRS, vistas dinámicas, diseño, pruebas, manuales en ES/EN), que reconoce sus brechas (`school-guardian-project/sg-documentation:04-Diseno-Software/es/04-Diseno-Software.md (sección 'Punto por resolver' sobre límites de esquema)`)
- Imágenes .NET multi-etapa y como usuario no root; GPS con ARCHITECTURE.md detallado y sincero (`sg-ms-route:src/ms-route.Api/Dockerfile:2; sg-ms-gps:ARCHITECTURE.md:59`)
- Trabajo por PR con plantilla y convención de commits, actividad semanal continua desde la semana 35 (`sg-ms-fleet:.github/pull_request_template.md; sg-ms-fleet:.github/git-conventions.md`)

## Lo que no se pudo verificar

- Compilación, ejecución de pruebas y arranque del sistema — Está prohibido ejecutar el código de los aprendices (npm, mvn, docker, dotnet); los veredictos de build salen de lectura (BusDtos.cs duplicado) y de las ejecuciones de Actions.
- sg-ms-user-management y sg-documentation (existen en la organización, no se declararon ni estaban entre los 11 clones) — No están clonados; solo se leyeron por la API de GitHub (solo lectura) los controladores de ruta, la lista de archivos y el documento de diseño. No se evaluó su calidad, seguridad ni pruebas.
- Estado de CI de sg-ms-api-gateway, sg-ms-gps, GuardianEscolar, broker-kafka y sg-db; y de sg-ms-forgot-information más allá de startup_failure — La API de GitHub agotó el tiempo varias veces; se asumió 'sin workflows' porque el árbol no tiene .github/workflows. Resultados de 7 servicios tomados el 20-oct.
- Miembros de la organización, colaboradores, issues y tableros (T-13.6, T-13.7) — La API no respondió en los reintentos.
- Inicio de sesión de GitHub de cada integrante — No se resolvieron logins; los aportes se agrupan por correo de git (único criterio de fusión) y los tres correos se mantienen separados.
- Respuesta de las URL sslip.io y trycloudflare halladas en el código (T-14.3) — El equipo no declara una URL de despliegue; la regla solo autoriza un GET a la declarada.
- Muestreo de calidad de código (D4) — Lectura profunda de sg-ms-route (4.581 líneas) y sg-ms-iam (3.294), los dos servicios más grandes, más muestras de fleet y notification; los demás servicios, el web y el móvil solo por patrones (grep).
- Contenido de binarios (mp4, docx, xlsx, pdf) y datos personales reales en ellos — No se abrieron; H-15 se infiere por el nombre.
- Vulnerabilidades de dependencias — No se audita sin instalar herramientas o ejecutar código.
- Exposición real del Admin de Kong y de los puertos de servicios — Depende del grupo de seguridad de la EC2 y del firewall, que no están en los repositorios; la configuración es verificable, la accesibilidad desde Internet no.
- Búsqueda de secretos en el historial — Heurística de patrones con -G en cada repositorio; no sustituye a un escáner como gitleaks.
- Ramas remotas creadas después del clonado (p. ej. hotfix/fix-refresh-token y hotfix/fix-errors en sg-ms-iam; rama de user-management develop) — Aparecen en las ejecuciones de Actions pero no en los clones evaluados (3 ramas en IAM).
