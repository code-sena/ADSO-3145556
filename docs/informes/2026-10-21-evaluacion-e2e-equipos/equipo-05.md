# Equipo 5 · TraduceSeñas — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 65/100 · Juicio D**

## Veredicto

TraduceSenas es de lo mejor construido que se reviso: gateway y cinco microservicios hexagonales con una base por servicio, unas 650 pruebas, seguridad solida (bcrypt, JWT verificado, rate limit, CORS cerrado) y un despliegue real en traducesenas.tech que responde. Cuatro de los cinco flujos principales funcionan de punta a punta con contratos que coinciden; el quinto, el entrenamiento del modelo por el administrador, nunca llega al backend. Los huecos son de proceso mas que de codigo: no hay CI en ningun repo, main esta detras de develop (el arreglo del cierre en Android no esta en main ni en el APK publicado), el consentimiento de la Ley 1581 no se guarda y la documentacion de evidencias describe un diseno distinto del construido. Con dos o tres semanas de trabajo ordenado el proyecto queda cerrado.

## Plan para cerrar las brechas

1. **Integrar develop en main en frontend y backend, compilar y publicar el APK 1.0.2 con el arreglo de Android** (esfuerzo S) — se cierra cuando: main y develop sin commits de diferencia; el Release de traducesenas-download es 1.0.2 y la app abre en un Android real.
2. **Crear workflows de CI (build + test) en backend y frontend y marcarlos como obligatorios** (esfuerzo S) — se cierra cuando: Cada PR muestra un check verde de Actions; la API de actions/runs lista ejecuciones exitosas.
3. **Persistir el consentimiento Ley 1581 con fecha y version de terminos** (esfuerzo S) — se cierra cuando: Registrar un usuario deja terms_accepted_at no nulo y un registro sin la casilla recibe 400.
4. **Unificar las migraciones en una sola herramienta ejecutable desde cero, incluido profile** (esfuerzo M) — se cierra cuando: docker compose up en un volumen vacio y la migracion automatica producen el mismo esquema que produccion.
5. **Actualizar la documentacion de evidencias desde el codigo real (catalogo, 06-data, OpenAPI, ADR)** (esfuerzo M) — se cierra cuando: service-catalog sin 'Pending' para servicios existentes y cada ruta del OpenAPI existe en el gateway.
6. **Resolver el entrenamiento del administrador: conectar /api/samples o retirarlo** (esfuerzo M) — se cierra cuando: Capturar una muestra en AdminTrainingScreen crea un documento en gesture_samples, o el endpoint y Mongo desaparecen del repo.
7. **Endurecer sesion y errores: JWT corto con refresh y errores 500 genericos** (esfuerzo M) — se cierra cuando: Un usuario bloqueado pierde acceso en menos de 15 minutos y ningun controlador devuelve error.message crudo.
8. **Endurecer imagenes Docker y publicar healthchecks** (esfuerzo S) — se cierra cuando: docker inspect muestra usuario no root y estado healthy en los 6 servicios.
9. **Publicar OpenAPI desde cada servicio y validar en CI** (esfuerzo M) — se cierra cuando: GET /docs responde en el gateway y la CI falla si una ruta no esta en el contrato.
10. **Corregir README, historial vacio por error y archivos basura del frontend** (esfuerzo S) — se cierra cuando: README sin contradicciones, historial muestra error con reintento y estructura.txt eliminado.
11. **Pruebas de AuthContext, traducciones e historial** (esfuerzo M) — se cierra cuando: npm test cubre AuthContext y translationsService con casos de exito y fallo.
12. **Limpiar el repo de evidencias del esqueleto del framework, ordenar el historial de releases y mejoras menores de seguridad y logging** (esfuerzo S) — se cierra cuando: docs/README.md describe el proyecto; las releases se etiquetan sin squash; nginx.conf con CSP.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Registro, verificacion por codigo e inicio de sesion | — |
| Completo | Traducir una sena y guardarla en el historial | — |
| Completo | Aprender el abecedario LSC | — |
| Completo | Logros y progreso del perfil | — |
| Roto | Entrenamiento del modelo por el administrador (muestras a modelo) | La app guarda las muestras solo en el dispositivo y nunca llama a /api/samples (endpoint y coleccion Mongo huerfanos); el entrenamiento /api/models/train que documenta el OpenAPI no existe. Solo funciona sincronizar plantillas de movimiento (sign-templates). |

## Hallazgos

### [Alto] H-01 · El arreglo del cierre inesperado en Android no esta en main y el APK publicado es anterior
- **Repositorio:** `propietario/frontendTS`
- **Evidencia:** `package.json:43 (en origin/main sigue expo-av; el commit 49cd3b3 lo retira el 2026-10-20 00:29). El Release v1.0.1 del repo de descarga se publico el 2026-10-19 22:08Z.`
- **Por qué importa:** El propio mensaje del commit dice que expo-av hace caer la app al abrirla. Quien descarga el APK desde la pagina publica obtiene la version anterior al arreglo y main (la rama de produccion) no lo contiene.
- **Cómo cerrarlo (S):** Integrar develop en main, compilar el APK 1.0.2 con EAS y publicar el Release en traducesenas-download.

### [Alto] H-02 · Los changelogs Liquibase no reproducen el esquema real y el esquema vivo depende de scripts de initdb
- **Repositorio:** `propietario/backendTS`
- **Evidencia:** `services/iam-service/db/changelogs/003_user_role.xml:12 (crea user_role) frente a services/iam-service/db/003_iam_roles_permissions.sql:8 (roles); services/iam-service/db/changelogs/015_account_deletion_outbox.xml:6 (precondicion HALT); docker-compose.yml:9`
- **Por qué importa:** Hay dos fuentes de verdad. Los scripts de initdb solo corren al crear el volumen y cada migracion nueva se aplica a mano con psql; profile no tiene Liquibase. Una base nueva con Liquibase no queda igual a la de produccion.
- **Cómo cerrarlo (M):** Elegir una sola herramienta (Liquibase o Flyway), convertir los .sql 000-012 en changesets ejecutables y correrla al arrancar cada servicio o con un contenedor de migracion.

### [Alto] H-03 · Ningun repositorio tiene integracion continua
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/backendTS:package.json:5 (script test con ~652 casos); propietario/frontendTS:package.json:7 (jest). Sin .github/workflows en ninguna rama; la API de Actions de backendTS devuelve 0 ejecuciones.`
- **Por qué importa:** Las pruebas, el typecheck y el build dependen de que alguien los corra a mano antes de fusionar; los PRs numerados no validan nada automaticamente y el arreglo de Android llego a develop sin una verificacion automatica.
- **Cómo cerrarlo (S):** Agregar un workflow por repo que ejecute build + test en cada PR hacia develop y main y exigirlo como check obligatorio.

### [Alto] H-04 · El consentimiento de tratamiento de datos (Ley 1581) no se guarda en el servidor
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/backendTS:services/iam-service/db/000_users.sql:12 (terms_accepted y terms_accepted_at) sin ninguna escritura en services/*/src; propietario/frontendTS:src/app/providers/AuthContext.tsx:437 (el registro solo envia name, email y password).`
- **Por qué importa:** El sistema trata datos de personas y solo puede probar la autorizacion con una casilla del cliente; no hay fecha ni version de los terminos aceptados.
- **Cómo cerrarlo (S):** Enviar el consentimiento en el registro, validarlo con zod, guardar terms_accepted_at y la version de la politica, y rechazar el registro sin consentimiento.

### [Alto] H-05 · La documentacion de evidencias describe un sistema distinto del construido
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/traduce-senas-evidencias:docs/09-microservices/service-catalog.md:64 (gateway, analytics y profile 'Pending (no code yet)'); docs/06-data/models.md:59 (29 tablas, UUID, refresh_tokens, audit_logs); docs/07-api/contracts/openapi/recognition-service.yaml:303 (/api/models/train inexistente).`
- **Por qué importa:** Quien evalua o mantiene el sistema no puede confiar en 04-design ni en 06-data: documentan servicios que ya existen como pendientes y funciones (refresh token, auditoria, modelos) que el codigo no tiene.
- **Cómo cerrarlo (M):** Actualizar service-catalog, 06-data, ADR-002/003 y los OpenAPI desde el codigo real, o marcar explicitamente lo que queda como diseno futuro.

### [Medio] H-06 · La captura de muestras del administrador no llega al backend y el entrenamiento del modelo no existe
- **Repositorio:** `propietario/backendTS`
- **Evidencia:** `services/recognition-service/src/samples/adapters/inbound/http/routes.ts:15 (POST y GET /api/samples sin cliente); frontend src/feature/Admin/pages/AdminTrainingScreen.tsx:119 guarda solo en el dispositivo.`
- **Por qué importa:** El README del frontend promete 'gestion y entrenamiento del modelo'; hoy solo se sincronizan plantillas de movimiento. El servicio recognition y la coleccion Mongo gesture_samples no se usan desde la app.
- **Cómo cerrarlo (M):** Conectar AdminTrainingScreen a /api/samples o retirar el endpoint y Mongo del compose; documentar que el 'modelo' es KNN/DTW con plantillas.

### [Medio] H-07 · Los controladores de translations y sign-templates devuelven 400 con el mensaje crudo del error
- **Repositorio:** `propietario/backendTS`
- **Evidencia:** `services/recognition-service/src/translations/adapters/inbound/http/translation_controller.ts:21`
- **Por qué importa:** Un fallo de base de datos se informa como error del cliente y filtra el texto interno; no sigue el formato {success, code, message} que usan iam y lexicon.
- **Cómo cerrarlo (S):** Mapear errores de dominio a codigos y dejar que el errorHandler comun responda 500 generico para el resto.

### [Medio] H-08 · El token dura 1 dia con roles incrustados y no hay revocacion ni refresh
- **Repositorio:** `propietario/backendTS`
- **Evidencia:** `packages/shared/src/config/load_config.ts:28; packages/shared/src/http/auth_middleware.ts:25; la documentacion (propietario/traduce-senas-evidencias:docs/06-data/models.md:40) promete refresh_tokens que no existen.`
- **Por qué importa:** Cerrar sesion solo borra el token del cliente; bloquear una cuenta o quitar un rol no surte efecto hasta que caduque el JWT.
- **Cómo cerrarlo (M):** Acortar el access token (15 min) con refresh token rotativo y tabla de sesiones, o consultar el estado del usuario en cada peticion sensible.

### [Medio] H-09 · Trabajo entregable sin integrar en main
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/backendTS:docker-compose.yml:9 (migraciones 005 y 006 de profile solo en develop); rama feat/profile-admin-notifications con 1 commit y 26 archivos; propietario/frontendTS develop 8 commits por delante de main.`
- **Por qué importa:** La rama de produccion no coincide con lo que se desarrolla; el reporte semanal y mensual a administradores existe solo en una rama.
- **Cómo cerrarlo (S):** Abrir PR de develop a main en ambos repos, decidir si se integra o se descarta la rama de reportes y cerrar las ramas fix ya fusionadas.

### [Medio] H-10 · Historial aplastado y duplicado en main del backend
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/backendTS:README.md (main = 3 commits: Initial, 'Develop (#22)' 422 archivos, 'chore(release): v1.0.0 (#44)' 285 archivos); commits 7281679 y 7dfc237 repiten el mismo cambio en ingles y espanol.`
- **Por qué importa:** Quien revise main no ve quien hizo que; el aporte individual solo se reconstruye desde develop.
- **Cómo cerrarlo (S):** Fusionar con merge commit (no squash) para releases o etiquetar cada release sobre develop; evitar commits duplicados bilingues.

### [Medio] H-11 · El repositorio de evidencias importa el esqueleto del framework de gobernanza en dos commits grandes
- **Repositorio:** `propietario/traduce-senas-evidencias`
- **Evidencia:** `docs/README.md:3 (README generico 'contains no project-specific information'); commits 654c656 y 65d7ce5 de 175 y 240 archivos.`
- **Por qué importa:** Mezcla trabajo propio y plantillas del framework con un mensaje que no lo dice; el indice docs/README.md no describe el proyecto Traduce Senas y quedan plantillas sin usar.
- **Cómo cerrarlo (S):** Reemplazar docs/README.md por un indice del proyecto, borrar plantillas sin uso y declarar en el README el origen del framework.

### [Medio] H-12 · El README del frontend contradice el estado real del repositorio
- **Repositorio:** `propietario/frontendTS`
- **Evidencia:** `README.md:136 ('main: Unicamente esta documentacion'); README.md:26 (Expo ~54 y RN 0.81 frente a package.json); README.md:145 (autor unico).`
- **Por qué importa:** Un evaluador o nuevo integrante clonaria main esperando solo documentacion y usaria versiones equivocadas; la autoria queda atribuida a una persona.
- **Cómo cerrarlo (S):** Actualizar tabla de ramas, stack y autores, y enlazar el repo backendTS con las variables EXPO_PUBLIC_*.

### [Medio] H-13 · Un error de red en el historial se muestra como historial vacio
- **Repositorio:** `propietario/frontendTS`
- **Evidencia:** `src/feature/History/pages/HistoryScreen.tsx:74`
- **Por qué importa:** El usuario cree haber perdido sus traducciones cuando solo fallo la conexion; no hay boton de reintentar.
- **Cómo cerrarlo (S):** Separar estado vacio de error con mensaje y reintento, igual que se hizo al borrar (HistoryScreen.tsx:102).

### [Medio] H-14 · Imagenes Docker como root, sin HEALTHCHECK y con dependencias de arranque incompletas
- **Repositorio:** `propietario/backendTS`
- **Evidencia:** `services/iam-service/Dockerfile:1; docker-compose.yml:36 (gateway depende solo de iam y lexicon).`
- **Por qué importa:** El orquestador no sabe cuando un servicio esta sano y un contenedor comprometido tendria privilegios de root.
- **Cómo cerrarlo (S):** Agregar USER node, HEALTHCHECK a /health, npm ci y depends_on con condition service_healthy para los 5 servicios.

### [Medio] H-15 · Cobertura de pruebas del frontend limitada a utilidades y logros
- **Repositorio:** `propietario/frontendTS`
- **Evidencia:** `src/shared/services/__tests__/api.client.test.ts; sin pruebas para src/app/providers/AuthContext.tsx (490 lineas) ni pantallas de traduccion e historial.`
- **Por qué importa:** El flujo principal (traducir y guardar) y la sesion no tienen red de seguridad ante cambios.
- **Cómo cerrarlo (M):** Agregar pruebas de AuthContext, translationsService y HistoryScreen con el cliente HTTP simulado, y un e2e minimo.

### [Medio] H-16 · No hay contrato de API servido y el OpenAPI vive en otro repo desincronizado
- **Repositorio:** `propietario/backendTS`
- **Evidencia:** `services/api-gateway/src/route_table.ts:5; propietario/traduce-senas-evidencias:docs/07-api/contracts/openapi/recognition-service.yaml:303`
- **Por qué importa:** Los consumidores dependen de leer el codigo; las rutas documentadas y las reales ya difieren y las respuestas mezclan snake_case y camelCase.
- **Cómo cerrarlo (M):** Generar el OpenAPI desde el codigo en cada servicio, publicarlo en /docs y validarlo en CI.

### [Bajo] H-17 · Archivos basura y binarios duplicados
- **Repositorio:** `propietario/frontendTS`
- **Evidencia:** `estructura.txt (3 MB); src/assets/images/traducion-real.png y src/assets/images/traduccion-real.png son identicos (2101846 bytes).`
- **Por qué importa:** Engordan el clon y confunden; el listado se desactualiza.
- **Cómo cerrarlo (S):** Borrar estructura.txt, deduplicar imagenes y mover los modelos 3D a un solo repositorio.

### [Bajo] H-18 · Token en localStorage en la version web y sin Content-Security-Policy
- **Repositorio:** `propietario/frontendTS`
- **Evidencia:** `src/shared/services/api.client.ts:14; nginx.conf:13 (cabeceras sin CSP)`
- **Por qué importa:** Cualquier XSS podria leer el JWT de 1 dia.
- **Cómo cerrarlo (M):** Agregar CSP en nginx.conf y plantear cookie httpOnly para la web.

### [Bajo] H-19 · Pantallas muy largas y nombres de dominio mezclados espanol e ingles
- **Repositorio:** `propietario/frontendTS`
- **Evidencia:** `src/feature/Profile/pages/ProfileScreen.tsx:1 (965 lineas); src/feature/History/pages/HistoryScreen.tsx:64 (texto_entrada)`
- **Por qué importa:** Dificulta pruebas y mantenimiento.
- **Cómo cerrarlo (M):** Extraer secciones a componentes y unificar los nombres con el contrato de la API.

### [Bajo] H-20 · Registro por console.error en lugar de un logger estructurado
- **Repositorio:** `propietario/backendTS`
- **Evidencia:** `services/lexicon-service/src/adapters/inbound/http/lexicon_controller.ts:56`
- **Por qué importa:** 13 llamadas a console.* en servicios impiden filtrar por nivel o correlacionar peticiones.
- **Cómo cerrarlo (S):** Usar pino con id de correlacion propagado por el gateway.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 81.2 |
| D2 | Reproducibilidad | 62.5 |
| D3 | Arquitectura y congruencia | 67.9 |
| D4 | Calidad de código | 75.0 |
| D5 | Seguridad | 93.4 |
| D6 | Pruebas | 60.0 |
| D7 | DevOps y despliegue | 61.4 |
| D8 | Datos | 65.0 |
| D9 | API y contratos | 40.0 |
| D10 | Frontend y móvil | 75.0 |
| D11 | IoT y firmware | — |
| D12 | Documentación y trazabilidad | 38.9 |
| D13 | Proceso y colaboración | 69.4 |
| D14 | Extremo a extremo | 50.0 |

## Lo que está bien

- Arquitectura de microservicios coherente con el catalogo (`services/api-gateway/src/route_table.ts:5 y docker-compose.yml:173: gateway, 5 servicios y 5 Postgres separados sin FK entre servicios.`)
- Seguridad del backend por encima de lo esperado (`packages/shared/src/security/jwt_token_provider.ts:41 (HS256 con issuer y audience), services/iam-service/src/auth/application/login.ts:22 (bcrypt con hash falso), services/api-gateway/src/cors_options.ts:16 (lista blanca) y rate limit estricto en login.`)
- Suite de pruebas amplia con integracion real (`tests/postgres_schema.test.ts y services/analytics-service/tests/atomic_erasure_postgres.test.ts: 76 archivos, ~652 casos, Postgres aislado y RabbitMQ.`)
- Despliegue real y verificado (`deploy/Caddyfile y docker-compose.prod.yml: HTTPS con Caddy; GET https://traducesenas.tech/api/lexicon/alphabet devolvio 200 y existe Release v1.0.1 del APK.`)
- Derecho al olvido implementado de punta a punta (`services/iam-service/src/users/adapters/inbound/http/routes.ts:12 y services/recognition-service/src/accounts/application/erase_account_data.ts:5: borrado de cuenta propagado por eventos (outbox) a recognition, analytics y profile.`)
- Contratos front-back cuidados y defensivos (`src/feature/Profile/data/persistedAchievements.ts:48 valida la respuesta; src/shared/services/api.client.ts:35 solo adjunta el token a la propia API (frontendTS).`)
- Aportes repartidos y proceso con PRs (`416 commits no merge: 53% / 31% / 15%; PRs numerados y Conventional Commits en los repos.`)

## Lo que no se pudo verificar

- Que compilen el frontend y el backend, y que las pruebas pasen — La regla prohibe ejecutar npm, mvn y docker; se evaluo por lectura.
- Funcionamiento autenticado del flujo de traducir y guardar en el despliegue — Exige POST con credenciales; solo se permitio GET de solo lectura.
- Ejecucion de la cadena de eventos RabbitMQ (recognition a profile y analytics) — Requiere levantar la plataforma; se verifico por codigo y pruebas.
- Vulnerabilidades de dependencias (npm audit) — No se pueden ejecutar comandos de npm.
- D4 se muestreo: iam-service (2722 lineas), recognition-service (1626) y lexicon-service (1629); analytics y profile se leyeron parcialmente y el frontend por pantallas clave — Muestreo declarado segun el briefing.
- Ejecuciones de CI del frontend y de evidencias via la API de Actions — La consulta fallo por tiempo de espera; se concluye ausencia de CI por la inexistencia de .github/workflows en todas las ramas.
- Issues y tableros de GitHub Projects (T-13.7) y colaboradores de los repos (T-13.6) — Fuera del alcance de lectura permitido.
- Si el APK v1.0.1 publicado es la compilacion anterior al arreglo de expo-av — No se descargo el APK; se infiere por fechas (Release 2026-10-19 22:08Z, arreglo 2026-10-20 00:29).
- Si 'Integrante 2' (correo Integrante) y 'Integrante' (noreply de Integrante 4) son la misma persona — Los correos difieren, asi que no se fusionaron; el README del frontend los asocia ('Integrante 2, @tadeo77789'). Tampoco se fusiono el alias 'Integrante 5 <Integrante 5>' (2 commits, sin correo valido). Si se fusionaran, el reparto no cambia de forma material.
