# Equipo 2 · Cuida Tu Agua — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 76/100 · Juicio A**

## Veredicto

Cuida Tu Agua es el sistema mas completo de la ficha: seis microservicios .NET/Java, gateway, base por Liquibase, infraestructura de produccion y una app con cuatro idiomas, y cuatro de sus cinco flujos principales funcionan de pantalla a tabla con contratos que coinciden. Lo que lo separa de un cierre es seguridad y proceso: una contrasena de base y una llave JWT privada en el repositorio de identidad, MQTT sin cifrar por Internet, un limite de login que en produccion es global, y un seed con credenciales publicas que corre en produccion. Ademas ninguno de los 16 repos tiene CI, el trabajo de nueve historias del frontend y las metas HU-023/024 sigue en ramas sin integrar, la alerta de fuga no existe, y 74 commits del frontend tienen como autor a una herramienta de IA. Con las acciones 1 a 5 del plan el equipo queda listo para una entrega defendible.

## Plan para cerrar las brechas

1. **Rotar la contrasena de security_app, sacar el valor de ms-iam-documentation.md, quitar el archivo del indice y purgar del historial el documento y keys/private.pem** (esfuerzo S) — se cierra cuando: git log --all -S con el valor viejo no devuelve commits, git ls-files no lista el documento con secretos y la nueva contrasena solo vive en .env del servidor.
2. **Corregir el limitador por IP del gateway para leer X-Forwarded-For de Caddy** (esfuerzo S) — se cierra cuando: Una prueba envia dos X-Forwarded-For distintos tras el proxy y cada uno tiene su cubeta; 11 logins desde IPs distintas en un minuto no devuelven 429.
3. **Poner TLS en Mosquitto, cerrar 1883 en produccion y usar ssl en el firmware, con un usuario de broker por dispositivo** (esfuerzo M) — se cierra cuando: nmap al servidor solo ve 8883; mosquitto_sub sin TLS falla y un ESP32 con credencial propia no lee las ordenes de otro.
4. **Retirar el dispositivo del seed dev, fijar --contexts por entorno y reemplazar los valores de secrets.example.py** (esfuerzo S) — se cierra cuando: Una base nueva con compose.prod.yml no contiene el serial de prueba y secrets.example.py solo tiene placeholders.
5. **Integrar a dev las ramas de HU (017, 022, 025/029/033 con repositorios HTTP, 038, 057, 058 y metas 023/024 en db, servicio y web) y definir main como rama de produccion** (esfuerzo L) — se cierra cuando: git branch -r --no-merged origin/dev no lista ramas feature y main contiene el mismo codigo que dev.
6. **Implementar la deteccion de fuga y el cierre automatico (HU-029, HU-033)** (esfuerzo L) — se cierra cuando: Un flujo continuo simulado genera una notificacion CRITICAL y una orden AUTO_LEAK visible en el historial.
7. **Crear CI por repositorio (dotnet test, mvn test, npm test/tsc/lint, liquibase validate) y una prueba contra SQL Server real** (esfuerzo M) — se cierra cuando: Cada PR muestra checks verdes en GitHub Actions y el job de integracion aplica las migraciones y corre un caso por servicio.
8. **Arreglar scripts/clone-all.sh y agregar guia de despliegue con generacion de llaves** (esfuerzo S) — se cierra cuando: En una carpeta vacia, clone-all.sh y los tres comandos del README levantan el sistema y un login responde 200.
9. **Alinear la documentacion: READMEs de ms-places-db, ms-iam-db, ms-devices, ms-places, gateway y firmware, y publicar un diagrama de arquitectura** (esfuerzo M) — se cierra cuando: Cada README se probo siguiendo sus pasos y el firmware tiene README con pines y flasheo.
10. **Exigir revision cruzada en los PR, declarar el uso de IA y no aplastar historia ni subir servicios completos en un solo commit** (esfuerzo S) — se cierra cuando: Cada PR muestra la aprobacion de un integrante distinto del autor, el README declara el uso de IA y los cambios nuevos entran en commits por HU.
11. **Mejoras de seguridad menores: tokens web, OpenAPI restringido, correos de admin fuera de git, ruta valve-commands bloqueada** (esfuerzo M) — se cierra cuando: El refresh no esta en localStorage, /places/openapi/v1.json exige ADMIN y el seed de admins sale de una variable.
12. **Higiene: colapsar baseline de ms-iam-db, borrar codigo muerto, versionar la API y paginar el historial de valvula** (esfuerzo M) — se cierra cuando: Una base nueva ejecuta un unico baseline, /api/v1 responde y el ADR sobre base compartida esta fechado.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Registro e inicio de sesion | — |
| Completo | Crear y gestionar lugares | — |
| Completo | Vincular medidor y ver consumo | — |
| Completo | Cerrar y abrir la valvula con codigo al correo | — |
| No implementado | Alerta de fuga y cierre automatico | — |

## Hallazgos

### [Crítico] H-01 · Contrasena de la base de datos en un archivo versionado
- **Repositorio:** `cuida-tu-agua/iam-backend`
- **Evidencia:** `ms-iam-documentation.md:5 fija la variable de la contrasena del usuario de aplicacion con un valor literal ««redactado»»; el archivo esta en .gitignore:40 pero sigue rastreado en el SHA evaluado.`
- **Por qué importa:** Es la credencial de runtime de security_app; infra/.env.example dice que en local se usen las mismas contrasenas del servidor, asi que podria coincidir con la de produccion. El repositorio es publico.
- **Cómo cerrarlo (S):** Rotar esa contrasena en todos los entornos, quitar el valor del documento (usar un placeholder), hacer git rm --cached del archivo y reescribir el historial con git filter-repo si el repo es publico.

### [Crítico] H-03 · Limitador de intentos de login es global detras de Caddy
- **Repositorio:** `cuida-tu-agua/api-gateway`
- **Evidencia:** `src/main/java/com/sywater/gateway/config/RateLimitConfig.java:43 toma la IP de getRemoteAddress() y no hay ninguna configuracion de cabeceras reenviadas; infra/Caddyfile:18 hace reverse_proxy a api-gateway:8080.`
- **Por qué importa:** En produccion todas las peticiones llegan con la IP de Caddy, asi que el limite de autenticacion (1 token/s, 6 por peticion, ~10/min) lo comparten todos los usuarios: con 10 inicios de sesion por minuto el resto recibe 429 y un atacante bloquea el login de todos.
- **Cómo cerrarlo (S):** Resolver la IP del cliente desde X-Forwarded-For de un proxy de confianza (XForwardedRemoteAddressResolver con trusted-proxies) y agregar una prueba con la cabecera.

### [Crítico] H-05 · MQTT en claro por Internet con credenciales y token de dispositivo
- **Repositorio:** `cuida-tu-agua/infra`
- **Evidencia:** `compose.prod.yml:16 publica 1883:1883; mosquitto/config/mosquitto.conf:10 listener 1883 sin TLS; cuida-tu-agua-device-ESP:link.py:36 conecta sin ssl y protocol.py:39,43 envia el token de fabrica en cada mensaje.`
- **Por qué importa:** Cualquiera en la ruta de red puede capturar usuario y clave del broker y el token de cada medidor, y con ellos publicar lecturas o estados falsos.
- **Cómo cerrarlo (M):** Habilitar listener TLS 8883 con certificado (Caddy/ACME o CA propia), cerrar 1883 en produccion y usar ssl=True en umqtt; idealmente una credencial por dispositivo.

### [Alto] H-02 · Llave privada RSA de firma JWT estuvo versionada
- **Repositorio:** `cuida-tu-agua/iam-backend`
- **Evidencia:** `git log --all -- keys/private.pem: agregada en eb3fe67 (2026-09-23) y eliminada en 9055c87 (2026-09-29); keys/public.pem cambio en 9055c87 y 146b5c6 (2026-10-16). Hoy .gitignore:37 la excluye.`
- **Por qué importa:** Quien clone el historial conserva la llave privada de las primeras semanas. El HEAD usa otra llave (la publica actual difiere), por eso no es critico, pero queda en el historial publico de dev.
- **Cómo cerrarlo (M):** Confirmar que ninguna instalacion usa ya la llave de eb3fe67, purgar el historial (filter-repo) y documentar un procedimiento de generacion y rotacion de llaves en infra.

### [Alto] H-04 · El seed de desarrollo con credenciales publicas se aplica en produccion
- **Repositorio:** `cuida-tu-agua/ms-device-db`
- **Evidencia:** `liquibase.properties:9 fija contexts=dev; 02-dml/00-inserts/001-seed-dev-devices.sql:3 es context:dev y :10 inserta serial, token y codigo de emparejamiento en claro; infra/compose.yml:28 usa ese mismo defaultsFile tambien con compose.prod.yml; el mismo valor esta en cuida-tu-agua-device-ESP:secrets.example.py:13.`
- **Por qué importa:** Sin --contexts distinto, Liquibase ejecuta el seed dev en el servidor: queda un medidor vinculable por cualquiera que lea el repositorio publico, y su token de fabrica es conocido.
- **Cómo cerrarlo (S):** Quitar el dispositivo del seed (usar scripts/new-device.ps1), pasar --contexts explicito desde infra segun el entorno y rotar ese medidor si ya existe en produccion.

### [Alto] H-06 · Base de datos compartida con llaves foraneas entre servicios
- **Repositorio:** `cuida-tu-agua/infra`
- **Evidencia:** `compose.yml:21 todas las migraciones usan sy-water-db; cuida-tu-agua/ms-places-db:01-ddl/04-alter/003-fk-places-owner.sql:9 y ms-consumption-db 01-ddl/04-alter/001-fk-readings-device.sql:7 referencian tablas de otros schemas; migraciones encadenadas en compose.yml:308.`
- **Por qué importa:** Un cambio en security.users o devices.devices rompe a los demas servicios, no se pueden desplegar ni escalar de forma independiente y las FK impiden mover un schema a su propia base.
- **Cómo cerrarlo (L):** Decidir y documentar (ADR) si el aislamiento por schema es el objetivo final; si no, sustituir las FK cruzadas por validacion en servicio y base por servicio.

### [Alto] H-07 · El script de clonado esta roto
- **Repositorio:** `cuida-tu-agua/infra`
- **Evidencia:** `scripts/clone-all.sh:25 clona la rama feauture-one-proyect de cuida-tu-agua-frontNEW, que ya no existe en origin (se fusiono en el PR #14 y las ramas actuales son dev y feature/*); lineas 14 y 17 usan los nombres ms-device y ms-notification, distintos de los repos declarados ms-devices y ms-notifications.`
- **Por qué importa:** El flujo de arranque de cero documentado en el README de infra falla antes de construir la web. Lo de los nombres depende de la redireccion de GitHub (no verificable sin red).
- **Cómo cerrarlo (S):** Clonar dev del frontend, usar los nombres reales de los repos y probar el script en una carpeta limpia.

### [Alto] H-08 · Nueve ramas de funcionalidad del frontend sin integrar a dev
- **Repositorio:** `cuida-tu-agua/cuida-tu-agua-frontNEW`
- **Evidencia:** `git log origin/dev..origin/<rama>: feature/hu-017-chart-tooltip (+46), hu-022-valve-history-filter (+52), hu-025-notification-center (+23), hu-029-leak-alert (+34), hu-033-valve-closed-alert (+27), hu-038-export-csv (+42), hu-057-consumption-report (+58), hu-058-report-pdf (+66) y hu-023-024-goals (+2); ultimas del 07 al 08-oct, a 95 commits de dev; dev no contiene ExportConsumptionButton, bucketTooltip ni ConsumptionReport. Dev solo tiene src/presentation/screens/valve/ValveHistoryScreen.tsx sin filtro.`
- **Por qué importa:** Unos 350 commits de la cuenta Integrante 1 no estan en la rama entregable: HU-017, 022, 038, 057 y 058 no existen en el producto. Varias usan InMemoryAlertRepository y chocaran con la implementacion HTTP de notificaciones que ya esta en dev.
- **Cómo cerrarlo (L):** Rebasar o descartar cada rama contra dev, integrar las HU 017, 022, 038, 057, 058 con repositorios HTTP y cerrar la rama de metas (HU-023/024).

### [Alto] H-09 · Deteccion de fuga y alerta de fuga no existen en ningun servicio
- **Repositorio:** `cuida-tu-agua/ms-notifications`
- **Evidencia:** `src/SyWater.Notifications.Domain/Notifications/NotificationEnums.cs:7 no define tipo de fuga; en ms-consumption, ms-devices, ms-notifications y ms-places el grep de leak/fuga en codigo no devuelve nada; solo ms-valve tiene el origen AUTO_LEAK.`
- **Por qué importa:** HU-029 y HU-033 (alerta de fuga y cierre automatico) estan declaradas en README, base de datos y frontend (rama), pero el flujo no se cumple de punta a punta.
- **Cómo cerrarlo (L):** Implementar la regla de fuga (consumo continuo) en ms-consumption, emitir el evento a ms-notifications y a ms-valve (AUTO_LEAK) y conectar la pantalla de la rama HU-029.

### [Alto] H-10 · Ningun repositorio tiene integracion continua
- **Repositorio:** `EQUIPO`
- **Evidencia:** `Cero archivos bajo .github/workflows en las 16 ramas por defecto y en el historial de todas las ramas (git log --all --name-only -- .github).`
- **Por qué importa:** Las 700+ pruebas (jest, xUnit, JUnit) nunca se ejecutan automaticamente; nada impide fusionar a dev codigo que rompe el build ni detecta contratos rotos entre servicios.
- **Cómo cerrarlo (M):** Agregar un workflow por tecnologia (dotnet test, mvn test, npm test + tsc + lint) como requisito de PR y, en los *-db, liquibase validate.

### [Medio] H-12 · README vacio en ms-places-db
- **Repositorio:** `cuida-tu-agua/ms-places-db`
- **Evidencia:** `README.md tiene 0 bytes.`
- **Por qué importa:** Es el repo de la base que arranca la cadena de datos de lugares y no documenta nada.
- **Cómo cerrarlo (S):** Escribir el README siguiendo el de ms-device-db (schema, requisitos, comandos).

### [Medio] H-13 · README de ms-iam-db describe otro proyecto
- **Repositorio:** `cuida-tu-agua/ms-iam-db`
- **Evidencia:** `README.md:1 titulo design-software-iam-db, :5 ms-security, :11 00_bootstrap.sql y :42 liquibase.properties.example; ninguno existe.`
- **Por qué importa:** Quien siga el README no puede levantar la base; el arranque correcto solo esta en infra.
- **Cómo cerrarlo (S):** Reescribir el README con la estructura y comandos reales (01-ddl, 06-releases v1.1 a v1.4).

### [Medio] H-14 · Firmware sin README, sin pruebas y con archivo vacio
- **Repositorio:** `cuida-tu-agua/cuida-tu-agua-device-ESP`
- **Evidencia:** `commits.md es un archivo de 0 bytes y no hay README ni carpeta de pruebas; config.py:22 cita Arquitectura_Fisica_Dispositivo_IoT.md que no esta en el repo.`
- **Por qué importa:** No hay forma de reproducir el dispositivo (cableado, flasheo, secrets.py, broker) ni de probar protocol.py sin la placa.
- **Cómo cerrarlo (M):** Agregar README con hardware, pines, flasheo y secrets; pruebas pytest para protocol.py; borrar commits.md.

### [Medio] H-15 · El README del gateway contradice al codigo sobre los documentos OpenAPI
- **Repositorio:** `cuida-tu-agua/api-gateway`
- **Evidencia:** `README.md:12 dice que /*/openapi/** responde 404; src/main/java/com/sywater/gateway/security/SecurityConfig.java:49 y :74 los permiten sin token y GatewaySecurityTest.java prueba que se enrutan.`
- **Por qué importa:** Los contratos de toda la API quedan publicos en produccion (infra/compose.yml:166 y siguientes habilitan OpenApi).
- **Cómo cerrarlo (S):** Decidir: exigir rol ADMIN o IP restringida para la documentacion y alinear README y prueba.

### [Medio] H-16 · El ejemplo de secretos trae valores reales
- **Repositorio:** `cuida-tu-agua/cuida-tu-agua-device-ESP`
- **Evidencia:** `secrets.example.py:13 y :17 incluyen un token de fabrica y una clave del portal que coinciden con el seed de ms-device-db.`
- **Por qué importa:** Quien flashee con el ejemplo obtiene un dispositivo valido con credenciales publicas.
- **Cómo cerrarlo (S):** Usar placeholders obvios en el ejemplo y retirar ese dispositivo del seed.

### [Medio] H-17 · Tokens de sesion web en localStorage
- **Repositorio:** `cuida-tu-agua/cuida-tu-agua-frontNEW`
- **Evidencia:** `src/core/storage/secureStorage.web.ts:1 guarda access y refresh en localStorage; el refresh dura 7 dias (iam.jwt.refresh-ttl en el application.yml de iam-backend).`
- **Por qué importa:** Un XSS en la web exfiltra la sesion completa por una semana; el codigo reconoce el riesgo.
- **Cómo cerrarlo (M):** Mover el refresh a cookie httpOnly con SameSite o acortar su vida; sumar cabeceras CSP en nginx.conf.

### [Medio] H-18 · Correos personales de administradores en el repositorio
- **Repositorio:** `cuida-tu-agua/ms-iam-db`
- **Evidencia:** `06-releases/v1.4/02-dml/00-inserts/001-official-admins.sql define los administradores por correo (dos cuentas gmail) en el changeset y en su rollback.`
- **Por qué importa:** Son datos personales (Ley 1581) en un repo publico y el esquema da el rol ADMIN a quien registre ese correo antes.
- **Cómo cerrarlo (S):** Leer los administradores de una variable del entorno de despliegue o de un script manual fuera de git.

### [Medio] H-19 · La ACL de Mosquitto promete aislar a cada dispositivo pero no lo logra
- **Repositorio:** `cuida-tu-agua/infra`
- **Evidencia:** `mosquitto/config/acl:18 dice que cada ESP32 solo recibe sus ordenes porque %c es su serial; el client id lo elige el cliente, y todos comparten el usuario sywater-device con permiso de escribir en cualquier +/telemetry.`
- **Por qué importa:** Quien tenga la credencial compartida puede escuchar las ordenes de otras valvulas o suplantar un id; solo el token en el payload frena la suplantacion.
- **Cómo cerrarlo (M):** Un usuario de broker por dispositivo (con %u en la ACL) y rotacion al dar de baja.

### [Medio] H-20 · Las pruebas de API corren sobre SQLite, no sobre el esquema de Liquibase
- **Repositorio:** `cuida-tu-agua/ms-valve`
- **Evidencia:** `tests/SyWater.Valve.Api.Tests/ApiFactory.cs:21 define SqliteValveDbContext; lo mismo en los otros servicios .NET.`
- **Por qué importa:** Si una columna o restriccion de SQL Server cambia, ninguna prueba lo detecta (hoy coinciden, verificado a mano); el unico contrato real es la lectura manual del DDL.
- **Cómo cerrarlo (M):** Agregar un job de CI con Testcontainers/SQL Server y las migraciones de Liquibase.

### [Medio] H-21 · Metas de consumo (HU-023/024) terminadas el dia del corte en ramas sin integrar
- **Repositorio:** `cuida-tu-agua/ms-consumption`
- **Evidencia:** `ms-consumption origin/feature/hu-023-024-goals (3d0acb7), ms-consumption-db (fec0290) y frontNEW (81e827a) del 2026-10-20; src/SyWater.Consumption.Api/Controllers/ConsumptionController.cs en dev no tiene /goals.`
- **Por qué importa:** La funcionalidad existe, es coherente entre capas y tiene pruebas, pero no esta en dev: hasta fusionar las tres ramas y aplicar la migracion v1.1 de goals el flujo no funciona.
- **Cómo cerrarlo (S):** Fusionar en orden db, servicio, web y volver a correr la cadena migrate.

### [Medio] H-22 · Pruebas unitarias y cobertura de ms-iam solo en una rama
- **Repositorio:** `cuida-tu-agua/iam-backend`
- **Evidencia:** `origin/test/pruebas-unitarias-ms-iam (5 commits, 1960 lineas con JaCoCo y TESTING.md) no esta en dev; pom.xml en dev no tiene JaCoCo.`
- **Por qué importa:** No hay medicion de cobertura en la rama entregable.
- **Cómo cerrarlo (S):** Fusionar la rama y publicar la cobertura en CI.

### [Medio] H-23 · Ninguna rama main entregable
- **Repositorio:** `cuida-tu-agua/api-gateway`
- **Evidencia:** `origin/main de api-gateway contiene solo README.md (1 commit) mientras todo el codigo esta en dev; 12 de 16 repos solo tienen dev.`
- **Por qué importa:** No existe una rama de produccion a la que apuntar un despliegue; cualquier clone por defecto del gateway trae un repo vacio.
- **Cómo cerrarlo (S):** Definir main como rama de produccion en todos los repos y promover dev a main por PR.

### [Medio] H-24 · Esquema heredado v1.0 se crea y se borra
- **Repositorio:** `cuida-tu-agua/ms-iam-db`
- **Evidencia:** `06-releases/v1.1/01-ddl/00-drop-legacy/001-drop-legacy-v1.0.yaml elimina las tablas creadas por 01-ddl/03-tables/001-users.sql ... 010-password-policies.sql.`
- **Por qué importa:** Toda base nueva crea 10 tablas para borrarlas y recrearlas; confunde y aumenta el riesgo.
- **Cómo cerrarlo (M):** Colapsar a un baseline unico antes del primer despliegue real.

### [Medio] H-25 · Servicios completos entran en un solo commit
- **Repositorio:** `EQUIPO`
- **Evidencia:** `ms-valve 60b3ee2 (50 archivos), ms-consumption 47967fe (47), ms-valve-db 29b486d (65, unico commit) y ms-consumption-db 6c480b0 (59); ms-notifications README conserva una ruta local C:\Users\User\Desktop\proyect\.`
- **Por qué importa:** El historial no demuestra el proceso de construccion ni quien hizo cada parte; indica desarrollo local subido despues.
- **Cómo cerrarlo (S):** Trabajar en ramas por HU en el propio repo y no reescribir ni aplastar historia.

### [Medio] H-26 · No hay procedimiento para generar las llaves JWT ni guia de despliegue
- **Repositorio:** `cuida-tu-agua/infra`
- **Evidencia:** `README.md:18 remite a 'the deployment guide' inexistente; .env.example:3 exige KEYS_DIR con public.pem y private.pem (ignoradas) y ningun repo trae el comando que las genera.`
- **Por qué importa:** Nadie puede levantar el sistema desde cero sin ayuda de quien ya tiene las llaves.
- **Cómo cerrarlo (S):** Agregar scripts/gen-keys.sh y una guia de despliegue (llaves, .env, migrate, backup).

### [Bajo] H-11 · 74 commits del frontend tienen como autor a una herramienta de IA
- **Repositorio:** `EQUIPO`
- **Evidencia:** `git shortlog -sne --all --no-merges en cuida-tu-agua-frontNEW: Claude <«correo»> 74 commits en la rama traduccion (ya integrada a dev en fe03959), con Co-Authored-By y Claude-Session (HU-081/082, i18n frances y portugues, README y docs).`
- **Por qué importa:** El 18% de los commits del equipo no tiene un aprendiz como autor; no hay evidencia de quien reviso y entendio ese codigo, lo que importa para la sustentacion y la propiedad del trabajo.
- **Cómo cerrarlo (S):** Declarar en el README el uso de IA, hacer que un integrante firme y revise esos cambios en PR y mantener autoria humana en los commits.

### [Bajo] H-27 · README de ms-devices y ms-places desactualizados
- **Repositorio:** `cuida-tu-agua/ms-devices`
- **Evidencia:** `README.md de ms-devices dice ms-iam en 8081 y solo documenta heartbeat; ms-places promete data-model.md, decisions.md, runbook.md y place_members que no existen.`
- **Por qué importa:** Documentacion que contradice lo construido (T-12.4).
- **Cómo cerrarlo (S):** Actualizar al estado real o quitar las promesas.

### [Bajo] H-28 · Codigo muerto y plantilla de Expo
- **Repositorio:** `cuida-tu-agua/cuida-tu-agua-frontNEW`
- **Evidencia:** `src/config/env.ts no se importa en ningun modulo y scripts/reset-project.js es de la plantilla.`
- **Por qué importa:** Ruido y una URL por defecto distinta (localhost:8080/api) que confunde.
- **Cómo cerrarlo (S):** Borrarlos.

### [Bajo] H-29 · La API no tiene version y el historial de valvula no se pagina
- **Repositorio:** `cuida-tu-agua/ms-valve`
- **Evidencia:** `src/SyWater.Valve.Api/Controllers/ValveController.cs:49 lista ordenes por rango de fechas; todas las rutas son /api/... sin version.`
- **Por qué importa:** Un cambio de contrato rompe a las apps instaladas y el historial puede crecer sin limite.
- **Cómo cerrarlo (M):** Prefijar /api/v1 en el gateway y agregar limit/before al historial.

### [Bajo] H-30 · Endpoint interno de ordenes expuesto detras del gateway y clave interna unica
- **Repositorio:** `cuida-tu-agua/ms-devices`
- **Evidencia:** `src/SyWater.Devices.Api/Controllers/PlaceDevicesController.cs:39 POST valve-commands usa X-Internal-Key pero su ruta /devices/api/places/{id}/device/valve-commands no esta en la lista bloqueada del gateway; una sola INTERNAL_API_KEY sirve a los seis servicios.`
- **Por qué importa:** Quien obtenga esa clave compartida puede mandar ordenes de valvula por la puerta publica con un token valido.
- **Cómo cerrarlo (S):** Bloquear la ruta en el gateway y usar claves distintas por pareja de servicios.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 94.1 |
| D2 | Reproducibilidad | 78.7 |
| D3 | Arquitectura y congruencia | 76.4 |
| D4 | Calidad de código | 100.0 |
| D5 | Seguridad | 91.6 |
| D6 | Pruebas | 74.8 |
| D7 | DevOps y despliegue | 54.7 |
| D8 | Datos | 91.7 |
| D9 | API y contratos | 88.6 |
| D10 | Frontend y móvil | 88.9 |
| D11 | IoT y firmware | 45.0 |
| D12 | Documentación y trazabilidad | 63.9 |
| D13 | Proceso y colaboración | 59.4 |
| D14 | Extremo a extremo | 65.0 |

## Lo que está bien

- Contratos coherentes de punta a punta (`frontNEW:src/config/api.ts:45 contra los controladores de iam-backend, ms-places, ms-devices, ms-consumption, ms-valve y ms-notifications; enums SNAKE_CASE_UPPER = CHECK de Liquibase; un script de columnas EF/JPA contra DDL no encontro diferencias reales en los 5 servicios .NET.`)
- Seguridad por capas bien resuelta (`api-gateway:src/main/java/com/sywater/gateway/security/JwtConfig.java (RS256, issuer, revocacion Redis); iam-backend:src/main/java/com/sywater/ms_iam/infrastructure/security/BCryptPasswordHasher.java:10; ms-devices:src/SyWater.Devices.Api/Security/InternalKeyAttribute.cs (comparacion en tiempo constante); fallback RequireAuthenticatedUser en cada Program.cs.`)
- Arquitectura hexagonal consistente y limpia (`ms-valve, ms-places, ms-devices, ms-consumption y ms-notifications con Domain/Application/Infrastructure/Api; cero TODO, console.log, printStackTrace ni catch vacios en los 9 repos de codigo.`)
- Mensajeria robusta (`ms-consumption:src/SyWater.Consumption.Api/Messaging/RabbitMqEventConsumer.cs (cola muerta, reintentos, recuperacion automatica, idempotencia por UNIQUE device_id,recorded_at); ms-notifications con outbox de correo y reintentos.`)
- Pruebas abundantes y con aserciones reales (`ms-notifications 118 pruebas / 353 aserciones, ms-places 90 / 227, ms-devices 91 / 207, iam-backend ~130 @Test, frontNEW ~280 casos jest; ms-valve:tests/SyWater.Valve.Api.Tests/ValveApiTests.cs:47 prueba el flujo completo de cierre.`)
- Migraciones Liquibase por categoria y release (`ms-notification-db:changelog/db.changelog-master.yaml con releases v1.1 a v1.5, rollbacks inline y permisos minimos por servicio (03-dcl); infra:compose.yml:305 encadena toda la migracion con un comando.`)
- Infraestructura reproducible y documentada en el propio compose (`infra:compose.yml (healthchecks, depends_on service_healthy, limites de memoria en compose.prod.yml, Caddy con TLS, backup.sh, Portainer solo por tunel SSH).`)
- Frontend con i18n, accesibilidad y manejo de sesion cuidados (`frontNEW:src/core/http/ApiClient.ts:28 (refresh single-flight), src/i18n con 4 idiomas y prueba noHardcodedSpanish.test.ts, 25 de 27 pantallas con estado de carga.`)

## Lo que no se pudo verificar

- Estado de las ejecuciones de CI, PR con sus revisiones, issues/tablero, colaboradores y permisos (T-13.2 revisiones, T-13.6, T-13.7) — La API de GitHub no respondio (TLS handshake timeout / unexpected EOF); ademas no existen workflows en ningun repo.
- T-3.6 y T-3.1 contra 05-architecture/06-data — El equipo no declara repo de documentacion entre sus 16 repos; se uso lo que dicen los README y los comentarios de changesets (ADR-002, data-model v5).
- T-14.3: flujo contra despliegue en linea — El equipo no declara URL de despliegue.
- R-10.1 compilacion de la app y R-5.7 vulnerabilidades de dependencias — Exigen ejecutar npm/dotnet/mvn o herramientas de auditoria; no se ejecuto codigo.
- Existencia en GitHub de los repos ms-device y ms-notification que usa scripts/clone-all.sh — Sin red; GitHub redirige repos renombrados, asi que el efecto real no se pudo confirmar.
- Muestreo de D4: solo iam-backend (5864 lineas) y ms-devices (2898) se leyeron con profundidad — Instruccion de priorizar D14 y D5; los demas repos solo se revisaron por busquedas (TODO, console.log, catch vacios, tamanos).
- Comportamiento en ejecucion de MQTT, RabbitMQ y Liquibase (p.ej. que el contexto dev corra en produccion) — No se ejecuto nada; los hallazgos se basan en lectura de configuracion y semantica documentada de Liquibase.
- Alias de autor: Integrante 2 y Integrante se tratan como una persona solo porque comparten correo; Integrante 1 y Integrante se reportan por separado — Regla: fusionar alias unicamente por correo o login noreply compartido.
- Las 9 ramas feature del frontend se evaluaron por git log, diff y grep, no por revision completa de su codigo — Presupuesto de contexto.
