# Equipo 8 · LavaRápido Vehicular — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 85/100 · Juicio A**

## Veredicto

LavaRápido es un sistema completo y bien construido: siete servicios con arquitectura hexagonal, web, app móvil, gateway y despliegue en producción, y los cinco flujos principales (sesión, reserva, pago, ejecución del operario y avisos) están conectados de pantalla a tabla sin desajustes de contrato que encontremos. Lo que lo separa de cerrar es el proceso y la confianza: main está vacío o desactualizado en los 10 repos, no hay Pull Requests ni issues, las pruebas del web son plantillas y el móvil no tiene ninguna, y hay contraseñas de desarrollo publicadas. Además, la matriz de permisos por rol se guarda pero no se aplica, y la aplicación en línea no respondió el día de la revisión. Con unas dos semanas de trabajo dirigido a esos puntos queda en condiciones de entrega.

## Plan para cerrar las brechas

1. **Fusionar develop en main en los 10 repos con PR revisado y etiquetar la versión entregada** (esfuerzo S) — se cierra cuando: git rev-list --count origin/main..origin/develop devuelve 0 en cada repo y existe un tag v1.0 en cada uno; main tiene protección con PR obligatorio.
2. **Quitar las contraseñas de desarrollo del código, las guías y los compose, y volver prod el perfil por defecto** (esfuerzo S) — se cierra cuando: git grep de las cuentas demo y de la clave de SQL Server no devuelve resultados en develop; arrancar sin variable de perfil no crea cuentas; la clave de Firebase está restringida por paquete.
3. **Implementar o retirar la matriz de permisos por rol** (esfuerzo L) — se cierra cuando: Quitar un permiso a un rol en la pantalla provoca 403 en el endpoint correspondiente (prueba automática), o la pantalla ya no existe y la guía lo dice.
4. **Escribir pruebas de comportamiento para web y móvil y ejecutarlas en el CI** (esfuerzo M) — se cierra cuando: El web tiene al menos 15 casos con asserts sobre auth, guards, reserva y pago; el móvil tiene jest en el CI con pruebas de servicios y login; ambos reportan cobertura.
5. **Ejecutar las pruebas e2e de infra y las de integración de payment en el CI con SQL Server y RabbitMQ** (esfuerzo M) — se cierra cuando: Un workflow levanta los servicios, corre node --test e2e y las pruebas de payment sin omitirse, y queda en success.
6. **Garantizar entrega de eventos con outbox y reintentos, y limpiar los enlaces huérfanos** (esfuerzo M) — se cierra cuando: Apagar RabbitMQ durante una reserva y encenderlo después entrega los avisos y la asignación; booking.created se publica o se retira.
7. **Un usuario de base de datos por servicio con permisos solo sobre su esquema** (esfuerzo M) — se cierra cuando: Cada servicio se conecta con su usuario; intentar leer el esquema de otro servicio falla con permiso denegado.
8. **Tener la aplicación en línea disponible y vigilada durante la entrega** (esfuerzo S) — se cierra cuando: GET a la URL pública devuelve 200 y 200 en /api/v1/catalog/services; hay un monitor externo y un video de respaldo.
9. **Crear los reportes del admin en el backend** (esfuerzo M) — se cierra cuando: Los reportes del admin leen endpoints de agregación; con 10.000 reservas de prueba cargan en menos de 3 s.
10. **Abrir tablero de seguimiento y usar PR enlazados a issues** (esfuerzo S) — se cierra cuando: Cada historia de usuario tiene un issue y cada PR cierra uno; el tablero muestra el estado.
11. **Alinear documentación y contrato con el código (READMEs, OpenAPI exportado, motivo de cancelación, URL de producción del móvil)** (esfuerzo S) — se cierra cuando: La tabla de rutas del gateway coincide con application.yml; existe openapi.json publicado por servicio; el perfil production del móvil define la URL https.
12. **Endurecer y aligerar: nginx sin root, token fuera de localStorage, imágenes optimizadas y clases grandes divididas** (esfuerzo M) — se cierra cuando: La imagen web corre como usuario no root con HEALTHCHECK; las imágenes pesan menos de 300 KB cada una; OperationsService queda por debajo de 300 líneas por clase.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Registrarse e iniciar sesión (cliente, operario, admin) | — |
| Completo | Reservar un lavado (elegir vehículo, servicios, hora y operario) | — |
| Completo | Pagar una reserva con comprobante y ganar puntos de fidelidad | — |
| Completo | El operario inicia y termina el servicio; informe de inspección compartido | — |
| Completo | Avisos al cliente: eventos por RabbitMQ a la bandeja y push | — |

## Hallazgos

### [Alto] H-01 · main no contiene el trabajo entregable en ninguno de los 10 repositorios
- **Repositorio:** `EQUIPO`
- **Evidencia:** `git rev-list --count origin/main..origin/develop: gateway 16 (main sin archivos), web 448, booking 53, móvil 219, infra 63, security 59, payment 45, operations 41, notification 38, customer 10`
- **Por qué importa:** Quien clone la rama por defecto o revise main encuentra un andamio vacío o de marzo. El despliegue a Azure sale de develop a propósito, pero cualquier revisión, entrega o respaldo basado en main pierde el sistema real.
- **Cómo cerrarlo (S):** Fusionar develop en main en cada repo (con PR revisado), etiquetar la versión entregada y fijar main como rama de producción; dejar develop como integración.

### [Alto] H-03 · Las pruebas del web son plantillas: 36 de 37 specs solo comprueban que el componente se crea
- **Repositorio:** `propietario/Front-end-proyecto-web`
- **Evidencia:** `src/app/core/services/auth.spec.ts:1`
- **Por qué importa:** El CI en verde da falsa seguridad: login, reserva, pago, guards y cálculo de reportes no tienen ninguna verificación automática y son lo que más cambia (448 commits).
- **Cómo cerrarlo (M):** Reemplazar los 'should create' por pruebas de comportamiento al menos de AuthService, authGuard, interceptor, reserva y pago; medir cobertura en el CI.

### [Alto] H-04 · La app móvil (unas 35 mil líneas) no tiene ninguna prueba y su CI solo revisa tipos
- **Repositorio:** `propietario/LavadoVehicular-Movil`
- **Evidencia:** `README.md y el archivo .github/workflows/ci.yml:35 (la ruta empieza con punto)`
- **Por qué importa:** Es la pieza con más código y la que se instala en celulares. Las regresiones solo se ven al usarla; ya hay muchos fix de la misma pantalla en el historial.
- **Cómo cerrarlo (M):** Agregar jest con pruebas de los servicios HTTP y viewmodels críticos (login, reserva, pagos) y ejecutarlas en el CI.

### [Alto] H-05 · La matriz de permisos por rol se guarda pero no se aplica al autorizar
- **Repositorio:** `propietario/lavarapido-security-service`
- **Evidencia:** `src/main/java/com/lavarapido/security/infrastructure/config/SecurityConfig.java:85`
- **Por qué importa:** El admin puede editar permisos en la pantalla y el sistema sigue autorizando por roles fijos (hasRole). Una función declarada en el producto no tiene efecto, lo que engaña a quien la usa y deja el control de acceso fuera de lo que se ve.
- **Cómo cerrarlo (L):** O bien hacer que los servicios consulten las autorizaciones guardadas (claim de permisos en el JWT) o retirar la pantalla hasta implementarlo y dejarlo en la documentación.

### [Medio] H-02 · Sin Pull Requests ni revisión de pares: se fusiona a mano con --no-ff
- **Repositorio:** `EQUIPO`
- **Evidencia:** `gh api repos/{owner}/{repo}/pulls?state=all: 0 PR en 9 repos; payment-service tiene 2 (1 fusionado, 1 abierto 'Develop')`
- **Por qué importa:** 175 ramas en el web y más de 80 en los demás se integran sin que otra persona revise. Los defectos de contrato o seguridad no pasan por un segundo par de ojos.
- **Cómo cerrarlo (S):** Activar protección de develop/main que exija PR con al menos una aprobación de otro integrante y que el CI esté en verde.

### [Medio] H-06 · El perfil por defecto es dev y crea cuentas demo cuyas contraseñas están publicadas
- **Repositorio:** `propietario/lavarapido-security-service`
- **Evidencia:** `src/main/java/com/lavarapido/security/infrastructure/config/DevDataSeeder.java:30`
- **Por qué importa:** application.yml:9-11 fija dev como perfil por defecto; si un despliegue olvida SPRING_PROFILES_ACTIVE=prod, quedan un admin, un operario y un cliente con claves que están en un repo público (valores ««redactado»»). El compose de producción sí fuerza prod.
- **Cómo cerrarlo (S):** Hacer prod el perfil por defecto o fallar al arrancar si dev corre fuera de localhost; quitar las claves del código y de GUIA-EQUIPO.md y generarlas por entorno.

### [Medio] H-07 · GUIA-EQUIPO.md publica credenciales de desarrollo de admin, operario y cliente
- **Repositorio:** `propietario/lavarapido-infra`
- **Evidencia:** `GUIA-EQUIPO.md:148`
- **Por qué importa:** Repo público con correos y contraseñas reales de cuentas de prueba (valores ««redactado»»); sirven en cualquier instalación que corra el perfil dev, incluida una expuesta por error.
- **Cómo cerrarlo (S):** Retirar las contraseñas del documento (indicar que se leen del seeder local) y rotar cualquier cuenta que exista fuera del entorno local.

### [Medio] H-08 · Contraseña de SQL Server de desarrollo escrita como valor por defecto en el compose y el README
- **Repositorio:** `propietario/lavarapido-payment-service`
- **Evidencia:** `docker-compose.yml:8`
- **Por qué importa:** Repetida en docker-compose.yml:8, :32 y :48 y en README.md:230 y :237 (valor ««redactado»»); queda en el historial público y es el patrón de clave de otros entornos.
- **Cómo cerrarlo (S):** Exigir la variable sin valor por defecto (${MSSQL_SA_PASSWORD:?}) como ya hace lavarapido-infra, y quitarla del README.

### [Medio] H-10 · Una sola base de datos y un solo usuario administrador de SQL Server para los seis servicios
- **Repositorio:** `propietario/lavarapido-infra`
- **Evidencia:** `docker-compose.yml:115`
- **Por qué importa:** Los esquemas separados y la ausencia de FK cruzadas (verificadas) son buen diseño, pero todos entran con el usuario de máxima autoridad: un fallo en un servicio expone los datos de los demás, y no pueden escalar o respaldarse por separado.
- **Cómo cerrarlo (M):** Crear un usuario de base por servicio con permisos solo sobre su esquema; documentar el plan para separar bases cuando haga falta.

### [Medio] H-11 · Si RabbitMQ no responde, el evento de reserva se pierde: solo se escribe en el log
- **Repositorio:** `propietario/lavarapido-booking-service`
- **Evidencia:** `src/main/java/com/lavarapido/booking/infrastructure/adapter/out/messaging/RabbitDomainEventPublisher.java:78`
- **Por qué importa:** Sin outbox ni reintento, una caída corta del broker deja reservas sin asignación automática ni aviso al cliente, y nadie se entera. Operations publica igual.
- **Cómo cerrarlo (M):** Guardar el evento en una tabla outbox en la misma transacción y publicarlo con reintentos; alertar cuando el broker falle.

### [Medio] H-13 · Las pruebas e2e de infra no se ejecutan en el CI; solo se revisa su sintaxis
- **Repositorio:** `propietario/lavarapido-infra`
- **Evidencia:** `README.md y el archivo .github/workflows/ci.yml:42 (la ruta empieza con punto)`
- **Por qué importa:** Los flujos entre servicios (reserva, asignación, avisos, inspección) solo se prueban a mano. Lo mismo pasa en payment-service: sus pruebas de integración se omiten sin SQL Server (ci.yml:2) y el CI no lo levanta.
- **Cómo cerrarlo (M):** Levantar sqlserver y rabbitmq como servicios del job (o docker compose) y correr node --test e2e en un workflow nocturno; dar SQL Server a las pruebas de payment.

### [Medio] H-14 · Un build production de la app apuntaría a 10.0.2.2; solo el perfil preview fija la URL del servidor
- **Repositorio:** `propietario/LavadoVehicular-Movil`
- **Evidencia:** `eas.json:12`
- **Por qué importa:** El APK publicado sale del perfil preview (funciona); el perfil production, que es el que se envía a una tienda, no define EXPO_PUBLIC_API_URL y cae en la dirección del emulador. app.json:41 además permite tráfico sin cifrar.
- **Cómo cerrarlo (S):** Definir la URL https en el perfil production, quitar usesCleartextTraffic para builds de producción y probar el build antes de publicar.

### [Medio] H-15 · El despliegue depende de encender a mano la VM de Azure y no pudo comprobarse en línea
- **Repositorio:** `propietario/lavarapido-infra`
- **Evidencia:** `README.md y el archivo .github/workflows/deploy.yml:34 (la ruta empieza con punto)`
- **Por qué importa:** El 2026-10-20 la URL pública no respondió (curl código 28). Una demo o evaluación sin la VM encendida muestra un sistema caído; no hay evidencia reciente de que responda.
- **Cómo cerrarlo (S):** Documentar el horario de encendido, añadir un monitor externo (por ejemplo UptimeRobot) y dejar capturas o un video del sistema funcionando para la entrega.

### [Medio] H-16 · Los reportes del admin se calculan en el navegador, no existen en el backend
- **Repositorio:** `propietario/lavarapido-infra`
- **Evidencia:** `GUIA-EQUIPO.md:191`
- **Por qué importa:** La guía lo admite: cada gráfica descarga todas las reservas y pagos y las suma en el cliente; con volumen real se vuelve lenta y las cifras dependen de lo que el navegador alcance a cargar.
- **Cómo cerrarlo (M):** Crear endpoints de agregación (por periodo, servicio, operario) en booking y payment y consumirlos desde web y móvil.

### [Medio] H-22 · Sin issues ni tablero de seguimiento en los repositorios
- **Repositorio:** `EQUIPO`
- **Evidencia:** `GET /repos/propietario/lavarapido-booking-service/issues?state=all devuelve 0; open_issues_count 0 en web, infra y payment`
- **Por qué importa:** No hay trazabilidad entre el requisito, la tarea y el commit; con tres personas y 10 repos coordinar el avance depende de la memoria.
- **Cómo cerrarlo (S):** Abrir un tablero de GitHub Projects con las historias de usuario y enlazar los PR con 'Closes #n'.

### [Bajo] H-09 · google-services.json de Firebase está versionado con la clave de API
- **Repositorio:** `propietario/LavadoVehicular-Movil`
- **Evidencia:** `google-services.json:18`
- **Por qué importa:** No es un secreto de servidor, pero la clave sin restricción de paquete permite abusar de la cuota del proyecto Firebase.
- **Cómo cerrarlo (S):** Restringir la clave por paquete y huella en Google Cloud; si no se necesita en el repo, moverlo a un secreto de EAS.

### [Bajo] H-12 · Enlaces de mensajería sin contraparte: booking.created se escucha pero no se publica
- **Repositorio:** `propietario/lavarapido-notification-service`
- **Evidencia:** `src/main/java/com/lavarapido/notification/infrastructure/adapter/in/messaging/RabbitMessagingConfig.java:39`
- **Por qué importa:** notification procesa BookingCreated, pero booking-service solo publica confirmed y cancelled (BookingEvent.java:24-25); además security publica security.user.deactivated sin consumidor. Código y documentación divergen.
- **Cómo cerrarlo (S):** Publicar el evento que falta o quitar el enlace y el caso; decidir quién consume la desactivación de cuenta.

### [Bajo] H-17 · El README del gateway contradice el código: lista 4 servicios y dice que no hace CORS
- **Repositorio:** `propietario/lavarapido-api-gateway`
- **Evidencia:** `README.md:12`
- **Por qué importa:** application.yml enruta 6 servicios (operations y payment incluidos) y CorsConfig.java:28 implementa CORS; quien configure un servicio nuevo por el README se equivoca. El README de customer tampoco documenta /admin/vehicles.
- **Cómo cerrarlo (S):** Actualizar la tabla de rutas y la sección 'Qué hace y qué no', y generar la tabla desde application.yml.

### [Bajo] H-18 · El token de sesión se guarda en localStorage
- **Repositorio:** `propietario/Front-end-proyecto-web`
- **Evidencia:** `src/app/core/services/auth.ts:190`
- **Por qué importa:** Cualquier XSS puede leer el token (dura 1 hora). Hay cabeceras de seguridad en nginx, lo que reduce el riesgo.
- **Cómo cerrarlo (M):** Evaluar cookie httpOnly con refresh o, como mínimo, mantener la política CSP estricta y revisar la entrada de todo HTML dinámico.

### [Bajo] H-19 · La imagen del web corre nginx como root y no tiene chequeo de salud
- **Repositorio:** `propietario/Front-end-proyecto-web`
- **Evidencia:** `Dockerfile:16`
- **Por qué importa:** Una vulnerabilidad en nginx daría privilegios de root dentro del contenedor.
- **Cómo cerrarlo (S):** Usar nginxinc/nginx-unprivileged (puerto 8080) y añadir HEALTHCHECK.

### [Bajo] H-20 · OperationsService concentra 617 líneas y cinco responsabilidades
- **Repositorio:** `propietario/lavarapido-operation-service`
- **Evidencia:** `src/main/java/com/lavarapido/operations/application/usecase/OperationsService.java:55`
- **Por qué importa:** Operarios, ausencias, asignación, ejecución y calificaciones en una sola clase transaccional dificulta probar y cambiar sin romper otra parte. BookingService (24 KB) va en el mismo camino.
- **Cómo cerrarlo (M):** Dividir por caso de uso (asignación, ejecución, calificaciones, disponibilidad).

### [Bajo] H-21 · El web versiona 8,2 MB de imágenes PNG sin optimizar
- **Repositorio:** `propietario/Front-end-proyecto-web`
- **Evidencia:** `public/images/hero.png`
- **Por qué importa:** Cuatro PNG de 1,7 a 2,5 MB aumentan la descarga inicial y el tamaño del repo.
- **Cómo cerrarlo (S):** Convertirlos a WebP/AVIF con el tamaño real que se muestra.

### [Bajo] H-23 · El contrato OpenAPI solo existe en desarrollo; en producción Swagger está apagado y casi sin anotar
- **Repositorio:** `propietario/lavarapido-booking-service`
- **Evidencia:** `pom.xml`
- **Por qué importa:** Quien integre otro cliente no tiene un contrato publicado ni versionado; solo security anota sus controladores con @Operation.
- **Cómo cerrarlo (S):** Exportar openapi.json en el CI y publicarlo (en el repo o en Pages) por cada servicio.

### [Bajo] H-24 · POST /bookings/{id}/cancel acepta un motivo y lo ignora
- **Repositorio:** `propietario/lavarapido-booking-service`
- **Evidencia:** `src/main/java/com/lavarapido/booking/infrastructure/adapter/in/web/BookingController.java:92`
- **Por qué importa:** CancelRequest declara reasonCode pero el controlador pasa null al caso de uso; el cliente no puede indicar por qué cancela.
- **Cómo cerrarlo (S):** Pasar request.reasonCode() validado, o quitar el campo del contrato.

### [Bajo] H-25 · Los servicios nacen como commits únicos de 63 a 142 archivos
- **Repositorio:** `EQUIPO`
- **Evidencia:** `git log --shortstat: security e59d3bb (142 archivos), booking 6dcba98 (142), notification b38b61e (81), operations 0bad7a8 (63)`
- **Por qué importa:** No es copia de plantilla (se revisó), pero el historial previo del diseño no está en estos repos y no se puede ver quién escribió qué en esos 4 servicios.
- **Cómo cerrarlo (S):** Mantener el historial de trabajo antes de integrar y documentar de dónde vino el código inicial.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 98.1 |
| D2 | Reproducibilidad | 92.3 |
| D3 | Arquitectura y congruencia | 80.0 |
| D4 | Calidad de código | 86.4 |
| D5 | Seguridad | 90.4 |
| D6 | Pruebas | 78.8 |
| D7 | DevOps y despliegue | 96.3 |
| D8 | Datos | 99.2 |
| D9 | API y contratos | 76.7 |
| D10 | Frontend y móvil | 85.4 |
| D11 | IoT y firmware | — |
| D12 | Documentación y trazabilidad | 59.0 |
| D13 | Proceso y colaboración | 68.4 |
| D14 | Extremo a extremo | 90.0 |

## Lo que está bien

- Arquitectura hexagonal consistente en los 6 servicios, con límites claros (`propietario/lavarapido-security-service:src/main/java/com/lavarapido/security/domain/port (puertos in/out) y propietario/lavarapido-payment-service:src/PaymentService.Application/Ports; ninguna FK cruza esquemas en las migraciones`)
- Seguridad de servidor bien resuelta: JWT con emisor y audiencia, roles por ruta, llave para /internal y bcrypt 12 (`propietario/lavarapido-security-service:src/main/java/com/lavarapido/security/infrastructure/config/SecurityConfig.java:37-109 y JwtProperties.java:22`)
- Contratos entre web, móvil, gateway y servicios coinciden: más de 100 llamadas rastreadas hasta un controlador (`propietario/Front-end-proyecto-web:src/app/core/services/booking-api.ts frente a propietario/lavarapido-booking-service BookingDtos.java:32-39 y gateway application.yml:25-77`)
- Gateway que no expone /internal y lo prueba (`propietario/lavarapido-api-gateway:src/test/java/com/lavarapido/gateway/GatewayRoutesTest.java`)
- Infraestructura de producción pensada: Caddy con HTTPS, solo 80/443 públicos, llaves obligatorias y despliegue por SSH de comando único (`propietario/lavarapido-infra:docker-compose.prod.yml:1-136, deploy/Caddyfile y deploy/ci-deploy.sh:10-34`)
- Migraciones Liquibase por servicio, con rollback, índices únicos filtrados y semillas sin datos personales (`propietario/lavarapido-payment-service:db/changelog/changes/011-payment-payment.sql:5-33`)
- Pruebas de servicio con asserts reales (unos 1.150 en los 6 servicios) y CI verde en los 10 repos (`propietario/lavarapido-security-service:src/test/java/com/lavarapido/security/infrastructure/config/ApiSecurityWebTest.java; gh api actions/runs: success en los 10`)
- Aportes equilibrados (38 %, 38 %, 23 %), commits convencionales y todas las ramas de trabajo integradas en develop (`git shortlog -sne --all --no-merges en los 10 repos; git rev-list origin/develop..rama = 0 salvo 3 ramas del web`)
- Documentación honesta del estado: la guía de infra dice qué está conectado y qué no (`propietario/lavarapido-infra:GUIA-EQUIPO.md:186-205`)

## Lo que no se pudo verificar

- Respuesta de la aplicación en línea (T-7.4 GET y T-14.3) — GET de solo lectura a https://lavarapidovehicular.northcentralus.cloudapp.azure.com sin respuesta (curl código 28, 2026-10-20); el briefing avisa que la VM de Azure puede estar apagada. El APK no se comprobó por la misma razón.
- Documentación del equipo (lavarapido-docs: 05-architecture, ADR, 06-data) para T-3.6, T-12.3 y T-12.4 — El repo no estaba entre los clones; se contrastó contra READMEs, la guía de infra y los comentarios de código (RF-xxx, ADR-xxx).
- Calidad de código (D4) solo se muestreó — Se leyó con profundidad operations-service y booking-service (los dos servicios backend más grandes por líneas tras payment y security); web (16,6 mil líneas) y móvil (34,9 mil) no se revisaron componente por componente.
- Compilación, pruebas y comportamiento en ejecución — El briefing prohíbe ejecutar código, npm, mvn o docker; se infiere por las ejecuciones de CI (success) y la lectura del código.
- Revisión de pares en los 2 PR de payment-service y colaboradores/permisos de los repos — La API respondió con tiempos agotados intermitentes; solo se contó PR e issues. Tampoco se consultó un tablero de GitHub Projects.
- Vulnerabilidades conocidas de dependencias — No se ejecutó un escáner (audit, dependency-check); R-5.7 se juzgó por las versiones vigentes.
- Contenido exacto de los secretos del historial de ramas sin integrar del web — Solo se confirmó que existe una clave JWT fija de mock; no se copió su valor.
- Fusión de alias de git en aportes — 'Integrante' y 'Integrante 2' se trataron como la misma persona solo porque comparten el mismo correo («correo») en git shortlog -sne --all --no-merges; no se asumió que los logins de GitHub de Integrante y Integrante sean los dueños de los repos más allá del nombre de la cuenta propietaria.
