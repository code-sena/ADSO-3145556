# Equipo 7 · RentaMóvil — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 68/100 · Juicio D**

## Veredicto

RentaMovil tiene un sistema real y desplegado en rentamovil.tech: cinco servicios hexagonales con JWT RS256, un gateway, migraciones Liquibase y una web que habla con el backend; cuatro de cinco flujos recorren todas las capas sin desajustes. Lo que falta para cerrar es de orden y de seguridad, no de funcionalidad: el registro de usuarios no funciona en las ramas entregadas porque el iam de develop no tiene la verificación de correo que ya espera la web (esa versión solo está en tags sueltos), una clave de SendGrid sigue en el historial público, los endpoints internos son anónimos con los puertos publicados y la rama main de iam contiene solo el commit inicial. Tampoco hay CI, OpenAPI ni pruebas en el frontend, y los README de pagos y del frontend no sirven. Está cerca de cerrar si en esta semana se integran las ramas, se revoca la clave y se cierran los puertos.

## Plan para cerrar las brechas

1. **Revocar la clave de SendGrid, borrar la rama que la contiene y rotar cualquier secreto usado en el mismo periodo** (esfuerzo S) — se cierra cuando: La clave anterior responde 401 en SendGrid, la rama origin/chore/sync-develop-with-main-credentials-fix no existe y git log --all -S sobre el prefijo de la clave no devuelve nada.
2. **Cerrar los puertos publicados de servicios y base de datos y proteger /internal/** con una clave de servicio** (esfuerzo S) — se cierra cuando: docker compose config no muestra 'ports' para servicios ni SQL Server; un POST a /internal/events sin la clave devuelve 401 y nmap desde fuera del VPS solo ve 80/443.
3. **Unificar iam: fusionar tags v1.0.2 y develop, avanzar main y verificar el registro de punta a punta** (esfuerzo M) — se cierra cuando: main de rtm-iam contiene /email/verify y /email/resend; registrarse en la web envía el código, lo verifica y entra; git rev-list origin/main..v1.0.2 da 0.
4. **Integrar o cerrar las ramas con trabajo pendiente y promover develop a main en todos los repos** (esfuerzo M) — se cierra cuando: git rev-list origin/main..origin/develop = 0 en los 14 repos y ninguna rama de feature con commits que main no tenga.
5. **Añadir CI de pruebas y build en servicios y frontend** (esfuerzo M) — se cierra cuando: Cada PR dispara un workflow en verde con mvn verify o npm run build + lint; la rama main exige ese check.
6. **Endurecer Portainer/nginx y fijar versiones de imágenes** (esfuerzo S) — se cierra cuando: admin.rentamovil.tech solo responde desde IP/VPN autorizada y docker-compose.yml usa nginx con versión explícita.
7. **Retirar el db.json del historial del frontend y confirmar que esas cuentas no existen en producción** (esfuerzo S) — se cierra cuando: git log --all -- web/Front-end/db.json vacío y las cuentas del mock ausentes o con claves nuevas.
8. **Publicar el contrato OpenAPI de cada servicio y comprobarlo en CI** (esfuerzo M) — se cierra cuando: GET /v3/api-docs (o un .yaml versionado) por servicio; el frontend consume los mismos campos.
9. **Escribir README del servicio de pagos y del frontend, y limpiar la documentación** (esfuerzo S) — se cierra cuando: Un integrante nuevo levanta el servicio solo con el README; la matriz de trazabilidad coincide con los repos.
10. **Quitar eventos perdidos: reintento u outbox entre payment, booking y fleet** (esfuerzo M) — se cierra cuando: Con booking apagado, aprobar un pago y encenderlo después deja la reserva en CONFIRMED sin intervención manual.
11. **Crear el procedimiento del primer SUPER_ADMIN, consentimiento de datos y clave por dispositivo GPS** (esfuerzo M) — se cierra cuando: Documento y script que crean el primer administrador; el registro guarda aceptación de la política; cada dispositivo tiene su clave.
12. **Documentar la base compartida como deuda y revisar el uso de FK entre esquemas** (esfuerzo L) — se cierra cuando: ADR actualizado con el plan de separación y fijadas las identidades de git del equipo.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Roto | Registro, verificación de correo e inicio de sesión | web -> iam: la web llama POST /auth/email/verify y /auth/email/resend y lee {email} del registro; el iam de develop (SHA evaluado) devuelve tokens en /register y no define /email/verify ni /email/resend ni EMAIL_NOT_VERIFIED. El código que sí los tiene está en los tags v1.0.1/v1.0.2, fuera de toda rama. |
| Completo | Consultar el catálogo y reservar un vehículo | — |
| Completo | Pagar por transferencia y aprobar el comprobante | — |
| Completo | Entrega (pickup), devolución y rastreo GPS | — |
| Completo | Administración de flota: registrar vehículo y mantenimiento | — |

## Hallazgos

### [Bloqueante] H-01 · El registro de usuarios no funciona con el iam y la web entregados
- **Repositorio:** `RentaMovil/rtm-iam`
- **Evidencia:** `src/main/java/com/rentamovil/iam/infrastructure/web/controller/AuthController.java:41`
- **Por qué importa:** La web (develop) espera que /register no inicie sesión y que existan /auth/email/verify y /auth/email/resend; el iam de develop devuelve tokens y no tiene esos endpoints. Un cliente nuevo no completa el alta; la funcionalidad solo existe en los tags v1.0.1/v1.0.2, que no están en ninguna rama.
- **Cómo cerrarlo (M):** Integrar a develop y a main el código de los tags v1.0.1/v1.0.2 de rtm-iam (EmailVerificationController, EMAIL_NOT_VERIFIED, PENDING_VERIFICATION) y volver a correr el flujo registro -> código -> login.

### [Crítico] H-02 · Los endpoints /internal/** no tienen autenticación y los puertos de los servicios están publicados en el host
- **Repositorio:** `RentaMovil/rtm-booking-reservation`
- **Evidencia:** `src/main/java/com/rentamovil/booking/infrastructure/web/controller/InternalEventController.java:40 y docker-compose.yml:15`
- **Por qué importa:** El código confía en que el gateway no enruta /internal/**, pero cada compose publica el puerto del servicio (3001-3005, 8080) en 0.0.0.0. Quien alcance el puerto 3003 puede enviar PaymentCompleted y confirmar reservas sin pagar; telemetry y fleet aceptan eventos igual. Depende de que el firewall del VPS no los bloquee, algo que no se pudo comprobar.
- **Cómo cerrarlo (S):** Quitar 'ports' de los servicios (solo el gateway y nginx publican), o atarlos a 127.0.0.1, y añadir una clave de servicio compartida o JWT de servicio a /internal/**.

### [Crítico] H-03 · Clave de SendGrid en el historial de un repositorio público
- **Repositorio:** `RentaMovil/rtm-iam`
- **Evidencia:** `src/main/resources/application.properties:36`
- **Por qué importa:** El commit dc1707f (8-oct, rtm-iam) subió .env.example con la clave API real de SendGrid; el 14-oct se reemplazó por un marcador, pero la clave sigue legible en la rama origin/chore/sync-develop-with-main-credentials-fix de un repositorio público. Permite enviar correo suplantando al dominio. Valor ««redactado»».
- **Cómo cerrarlo (S):** Revocar la clave en SendGrid hoy, emitir otra, borrar la rama y reescribir el historial (git filter-repo) o declarar la clave muerta; añadir escaneo de secretos.

### [Alto] H-04 · La rama main de rtm-iam es solo el commit inicial y las versiones están fuera de toda rama
- **Repositorio:** `RentaMovil/rtm-iam`
- **Evidencia:** `README.md`
- **Por qué importa:** origin/main apunta a ae5c52c (29-sep); los tags v1.0.0-v1.0.2 (34 commits que develop no tiene) no están en ninguna rama, y develop tiene 27 que los tags no tienen. No existe una fuente única de la verdad para el servicio de identidad.
- **Cómo cerrarlo (M):** Decidir la línea buena, fusionar tags y develop en una sola, avanzar main hasta ella y proteger la rama.

### [Alto] H-05 · Trabajo sin integrar en ramas del frontend, fleet, payment y documentación
- **Repositorio:** `RentaMovil/rtm-frontend`
- **Evidencia:** `web/Front-end/src/App.jsx`
- **Por qué importa:** fix/i18n-sweep-web (5 commits, 20-oct), fix/reservation-price-and-ux (1, 20-oct), feat/updatePassword (6, recuperación de contraseña móvil), feat/stylePremium (4), feat/tradutions (1) no están en develop; además develop está por delante de main en fleet-maintenance (+3), payment-billing (+2), rtm-documentation (+2) y fleet-maintenance-db (+2).
- **Cómo cerrarlo (M):** Revisar y fusionar o descartar cada rama, borrar las ramas viejas (hay 20 con meses de antigüedad) y promover develop a main antes de la entrega.

### [Alto] H-06 · SQL Server expuesto en el puerto 1433 con la cuenta sa
- **Repositorio:** `RentaMovil/rtm-iam-db`
- **Evidencia:** `docker-compose.yml:19`
- **Por qué importa:** El compose publica 1433 en todas las interfaces y es el mismo que levanta la base en el VPS (deploy/README.md). Si el firewall no lo bloquea, cualquiera puede intentar fuerza bruta contra sa, que es dueña de las cinco bases lógicas.
- **Cómo cerrarlo (S):** Quitar el mapeo de puerto o atarlo a 127.0.0.1; la red 'rentamovil' basta para los servicios.

### [Alto] H-07 · Portainer con el socket de Docker montado detrás de un subdominio público
- **Repositorio:** `RentaMovil/rtm-deploy`
- **Evidencia:** `docker-compose.portainer.yml:38`
- **Por qué importa:** admin.rentamovil.tech enruta al panel (nginx.conf:67-103) y el contenedor controla Docker del host; una cuenta de Portainer débil equivale a root en el VPS. Además nginx:latest sin versión fija (docker-compose.yml:20).
- **Cómo cerrarlo (S):** Restringir admin.* por IP o VPN, activar 2FA/contraseña larga, fijar versiones de imágenes y valorar quitar Portainer en producción.

### [Alto] H-08 · Datos de usuarios y hashes bcrypt de un mock quedaron en el historial público
- **Repositorio:** `RentaMovil/rtm-frontend`
- **Evidencia:** `web/Front-end/package.json`
- **Por qué importa:** El archivo web/Front-end/db.json (commits 3134b10 a 4baa340, borrado el 16-oct en eb145e1) contiene cuatro cuentas con correo, teléfono y hash bcrypt, dos con rol ADMIN y SUPER_ADMIN. Son datos personales (Ley 1581) y hashes atacables sin conexión.
- **Cómo cerrarlo (S):** Confirmar que esas cuentas no existen en producción (o cambiar sus claves), y purgar el archivo del historial.

### [Alto] H-09 · Ningún repositorio tiene CI; las pruebas no se ejecutan automáticamente
- **Repositorio:** `EQUIPO`
- **Evidencia:** `No hay .github/workflows en los 14 repos (git log --all -- .github vacío)`
- **Por qué importa:** Hay 279 pruebas Java en los servicios y ninguna se corre al abrir un PR; el frontend no tiene pruebas. Los despliegues v1.0.x se hacen sin un resultado verde que los respalde.
- **Cómo cerrarlo (M):** Añadir un workflow por servicio con 'mvn -B verify' y uno en el frontend con lint y build, y exigirlos en las ramas protegidas.

### [Alto] H-10 · No hay contrato OpenAPI en ningún repositorio
- **Repositorio:** `RentaMovil/rtm-fleet-maintenance`
- **Evidencia:** `README.md:5`
- **Por qué importa:** Los README remiten a rtm-docs/07-api/contracts/openapi/*.yaml, carpeta que no está en rtm-documentation ni en los otros 13 repos. El contrato real solo vive en el código; el frontend lo replica a mano en los mappers.
- **Cómo cerrarlo (M):** Generar OpenAPI desde el código (springdoc) o versionar los .yaml en rtm-documentation y comprobarlos en CI.

### [Alto] H-11 · Base de datos compartida con claves foráneas entre esquemas
- **Repositorio:** `RentaMovil/rtm-payment-billing-db`
- **Evidencia:** `01_ddl/04_alter/001_add_fks_payment.sql:14`
- **Por qué importa:** payment.payment y payment.invoice referencian booking.reservation e iam.[user]; las cinco migraciones deben correr en orden y un servicio no puede evolucionar su esquema sin tocar al vecino. Es una decisión documentada (ADR-009) pero contradice la independencia de microservicios.
- **Cómo cerrarlo (L):** Para esta entrega documentarlo como deuda; a futuro quitar las FK entre esquemas y validar por API.

### [Alto] H-12 · Los eventos entre servicios se pierden si el destino está caído
- **Repositorio:** `RentaMovil/rtm-payment-billing`
- **Evidencia:** `src/main/java/com/rentamovil/payment/infrastructure/booking/BookingEventForwarderAdapter.java:61`
- **Por qué importa:** Si booking no responde al aprobar un pago, el catch solo escribe en el log: el pago queda APPROVED y la reserva sigue PENDING_REVIEW, sin reintento. El dinero está recibido y el cliente no ve su reserva confirmada.
- **Cómo cerrarlo (M):** Reintentos con backoff y una tabla outbox, o una tarea que concilie pagos aprobados con reservas.

### [Medio] H-13 · El servicio de pagos no tiene README
- **Repositorio:** `RentaMovil/rtm-payment-billing`
- **Evidencia:** `README.md:1`
- **Por qué importa:** El README tiene una línea; tampoco hay documentación de facturas ni cuentas bancarias (sin pruebas, lo admite la matriz). Nadie nuevo puede levantar ni entender el servicio.
- **Cómo cerrarlo (S):** Documentar qué hace, variables, dependencias (booking), almacenamiento local de facturas y cómo correr las pruebas.

### [Medio] H-14 · README de plantilla y sin documentación del frontend
- **Repositorio:** `RentaMovil/rtm-frontend`
- **Evidencia:** `web/Front-end/README.md:1`
- **Por qué importa:** El único README es el texto de Vite; no explica la web, la app Expo ni las variables (VITE_API_URL, Cloudinary, EXPO_PUBLIC_API_URL). El .env de la web habla de un mock que ya no existe.
- **Cómo cerrarlo (S):** Escribir un README raíz con estructura, cómo correr web y móvil, variables y cómo se despliega.

### [Medio] H-15 · El Dockerfile de la web ejecuta 'vite preview' como root en una sola etapa
- **Repositorio:** `RentaMovil/rtm-frontend`
- **Evidencia:** `web/Front-end/Dockerfile:15`
- **Por qué importa:** Se sirve producción con la herramienta de desarrollo, con node y todas las dependencias dentro de la imagen y sin HEALTHCHECK.
- **Cómo cerrarlo (S):** Construir con node y servir /dist con nginx sin privilegios, con healthcheck.

### [Medio] H-16 · El refresh token se guarda en localStorage
- **Repositorio:** `RentaMovil/rtm-frontend`
- **Evidencia:** `web/Front-end/src/features/auth/services/sessionStorage.js:7`
- **Por qué importa:** Cualquier XSS en la web puede robar la sesión de 7 días; el gateway ya deja CORS con credenciales para una cookie httpOnly (ADR-005) que no se usó.
- **Cómo cerrarlo (M):** Mover el refresh token a una cookie httpOnly/secure o aceptar el riesgo por escrito.

### [Medio] H-17 · No hay forma reproducible de crear el primer SUPER_ADMIN
- **Repositorio:** `RentaMovil/rtm-iam-db`
- **Evidencia:** `02_dml/00_inserts/001_insert_roles.sql:5`
- **Por qué importa:** Solo se siembran roles y permisos; ni los README ni iam describen cómo nace la primera cuenta con roles:grant. En producción se hizo a mano, sin dejar rastro.
- **Cómo cerrarlo (S):** Script o variable de arranque documentada (con clave por entorno) y nota en el README de despliegue.

### [Medio] H-18 · Una sola clave compartida para todos los dispositivos GPS y sin firmware en el sistema
- **Repositorio:** `RentaMovil/rtm-telemetry-gps`
- **Evidencia:** `src/main/java/com/rentamovil/telemetry/infrastructure/security/DeviceApiKeyValidator.java:29`
- **Por qué importa:** Un dispositivo comprometido permite falsear la ubicación de toda la flota; la comparación usa equals y no tiempo constante. No hay repositorio de firmware ni simulador, así que no se pudo verificar la ingesta real.
- **Cómo cerrarlo (M):** Clave por dispositivo (columna en telemetry.gps), comparación en tiempo constante y un simulador de dispositivo en el repo.

### [Medio] H-19 · La matriz de trazabilidad y el MER no describen lo construido
- **Repositorio:** `RentaMovil/rtm-documentation`
- **Evidencia:** `12-trazabilidad/matriz-trazabilidad.md:38`
- **Por qué importa:** Declara telemetry-gps sin repositorio ni servicio y pantallas con mock-api, ambos ya resueltos; el MER (Diagramas/Base_de _datos) define Driver, Contract y Payment_Method que no existen. El lector no sabe cuál versión creer.
- **Cómo cerrarlo (S):** Actualizar la matriz y el MER contra los repos *-db y marcar lo viejo como histórico.

### [Medio] H-20 · Sin consentimiento ni política de tratamiento de datos (Ley 1581)
- **Repositorio:** `RentaMovil/rtm-iam`
- **Evidencia:** `README.md`
- **Por qué importa:** Se guardan nombre, correo, teléfono y foto; no hay aceptación de política de datos en el registro ni mención en la documentación. Hay términos y condiciones de alquiler, no de tratamiento de datos.
- **Cómo cerrarlo (M):** Añadir casilla y registro de consentimiento en el alta, política publicada y procedimiento de supresión.

### [Medio] H-21 · Pruebas insuficientes en pagos y facturación
- **Repositorio:** `RentaMovil/rtm-payment-billing`
- **Evidencia:** `src/test/java/com/rentamovil/payment/application/usecase/PaymentUseCaseImplTest.java`
- **Por qué importa:** 2 archivos y 14 pruebas frente a 2 567 líneas; facturas (PDF), cuentas bancarias y el reenvío de eventos sin cubrir en el servicio que mueve el dinero.
- **Cómo cerrarlo (M):** Pruebas de InvoiceUseCaseImpl, BankAccountUseCaseImpl y de BookingEventForwarderAdapter (fallo de red).

### [Bajo] H-22 · Archivos duplicados y un submódulo roto en la documentación
- **Repositorio:** `RentaMovil/rtm-documentation`
- **Evidencia:** `07-procedimiento-implementacion-software/Procedimiento_Implementacion_Software(1).pdf`
- **Por qué importa:** Copias '(1)', un .respaldo2.md junto al documento de diseño y 'informe tecnico/rtm-documentation' registrado como gitlink sin .gitmodules; el README tiene una línea y 55,6 MB de binarios.
- **Cómo cerrarlo (S):** Borrar copias, eliminar el gitlink y escribir un README índice.

### [Bajo] H-23 · Restos del mock y rutas duplicadas
- **Repositorio:** `RentaMovil/rtm-frontend`
- **Evidencia:** `web/Front-end/src/App.jsx:83`
- **Por qué importa:** /register y /Register apuntan al mismo componente, quedan devDependencies del mock (json-server, bcryptjs, jsonwebtoken) y el .env habla de un mock-api inexistente.
- **Cómo cerrarlo (S):** Normalizar rutas a minúsculas, quitar dependencias y variables huérfanas.

### [Bajo] H-24 · Identidad de commits ambigua y node_modules en el historial
- **Repositorio:** `EQUIPO`
- **Evidencia:** `git shortlog -sne --all --no-merges (rtm-booking-db, rtm-booking-reservation)`
- **Por qué importa:** Cuatro commits aparecen como 'Integrante 3' con el correo de Integrante 2 (Integrante 2); no se pudo confirmar a quién pertenecen. El frontend subió node_modules tres veces (~680 000 líneas por commit).
- **Cómo cerrarlo (S):** Fijar user.name y user.email por integrante y purgar node_modules del historial si el tamaño del clon molesta.

### [Bajo] H-25 · El README del gateway omite rutas que el gateway sí enruta
- **Repositorio:** `RentaMovil/rtm-api-gateway`
- **Evidencia:** `README.md`
- **Por qué importa:** La tabla de enrutamiento no incluye /locations (ingesta GPS con clave de dispositivo) ni /gps (catálogo), declaradas en application.properties:54-61.
- **Cómo cerrarlo (S):** Completar la tabla del README.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 73.7 |
| D2 | Reproducibilidad | 93.3 |
| D3 | Arquitectura y congruencia | 77.1 |
| D4 | Calidad de código | 90.0 |
| D5 | Seguridad | 77.8 |
| D6 | Pruebas | 59.5 |
| D7 | DevOps y despliegue | 62.3 |
| D8 | Datos | 98.3 |
| D9 | API y contratos | 45.0 |
| D10 | Frontend y móvil | 50.0 |
| D11 | IoT y firmware | — |
| D12 | Documentación y trazabilidad | 61.1 |
| D13 | Proceso y colaboración | 65.6 |
| D14 | Extremo a extremo | 50.0 |

## Lo que está bien

- Arquitectura hexagonal consistente y cinco servicios con dominios claros (`src/main/java/com/rentamovil/booking/domain/port/in/ReservationUseCases.java y src/main/java/com/rentamovil/fleetmaintenance/application/usecase/ManageMaintenanceUseCaseImpl.java: puertos, casos de uso y adaptadores en todos los servicios, mismo esqueleto.`)
- Cuatro de cinco flujos recorren web, gateway, servicio y tabla sin desajustes de contrato (`reservationMapper.js:44 = CreateReservationRequest.java; paymentMapper.js:1 = UploadPaymentRequest.java; rentalMapper.js:19 = PickupRequest.java.`)
- Autenticación RS256 con JWKS, rotación de refresh y bloqueo por intentos (`RentaMovil/rtm-iam:src/main/java/com/rentamovil/iam/infrastructure/security/JwtTokenProviderAdapter.java:59 y LoginUseCaseImpl.java:24; el gateway valida y borra X-User-* (JwtAuthenticationFilter.java).`)
- Despliegue real y vivo con HTTPS (`https://rentamovil.tech/ y /api/health responden 200 (2026-10-20); RentaMovil/rtm-deploy:nginx/nginx.conf:45-55 y README.md con orden de arranque y renovación de certificados.`)
- Migraciones Liquibase ordenadas con rollback por changeset (`RentaMovil/rtm-iam-db:changelog/changelog-master.yaml y 05_rollbacks/; mismo patrón en los cinco repos *-db.`)
- Configuración externalizada y Docker de servicios bien hecho (`Dockerfile de cada servicio: multi-etapa, usuario no root y HEALTHCHECK; application.properties solo con variables de entorno y .env.example con marcadores.`)
- Pruebas serias en fleet-maintenance y telemetry-gps (`RentaMovil/rtm-fleet-maintenance:src/test (24 archivos, 111 pruebas, controlador, persistencia y verificador JWKS); RentaMovil/rtm-telemetry-gps:src/test (16 archivos, 57 pruebas).`)
- Trabajo repartido (35,9 / 35,7 / 28,5 %), PR y actividad continua (`git shortlog -sne --all --no-merges en los 14 repos; commits en las semanas 36-43; fusiones por Pull Request (#7, #12, #13 en el gateway).`)
- Documentación académica completa y bilingüe (`RentaMovil/rtm-documentation:09-manuales/ES/MD/Manual_Tecnico_ES.md, 12-trazabilidad/matriz-trazabilidad.md con RF -> HU -> endpoint -> prueba.`)

## Lo que no se pudo verificar

- Compilación del frontend web y móvil, ejecución de pruebas y arranque de los servicios — Prohibido ejecutar npm, mvn, docker o expo; todo se leyó estáticamente.
- T-13.6 propiedad de los repositorios y T-13.7 issues/tableros — La API de GitHub expiró por TLS en la mayoría de llamadas; no se pudo leer miembros de la organización ni issues. El conteo de ejecuciones de Actions (0) se obtuvo en 10 de 14 repos; en booking-db, booking-reservation (segunda consulta sí: 0), payment-billing-db, telemetry-gps y telemetry-gps-db varias consultas expiraron, pero ninguno tiene .github en el historial.
- Si el firewall del VPS bloquea los puertos 1433, 3001-3005 y 8080 del host — Solo se leyó el compose; no se hizo ninguna conexión a esos puertos. H-02 y H-06 son riesgo condicionado a eso.
- Calidad de código (D4) solo muestreada en fleet-maintenance y booking-reservation (los dos mayores por líneas); frontend, iam, payment y telemetry no se leyeron a fondo — Muestreo declarado por el briefing.
- Fecha y alcance de lo desplegado en rentamovil.tech (qué versión de iam corre) — Solo GET de lectura: 200 en /, /api/health y /api/vehicles; no se puede inferir el commit desplegado. La evidencia de H-01 es sobre las ramas evaluadas.
- Identidad del alias 'Integrante 3' (4 commits con el correo de Integrante 2) — Se fusionó solo por correo idéntico (Integrante 2) con la cuenta de Integrante 2, pero el nombre sugiere a otra persona; se informa por separado en aportes y no se pudo resolver el login de GitHub.
- Escaneo de secretos con patrones propios — En una consulta el barrido de historial incluyó patrones de otras plataformas (AWS, SendGrid, Google) fuera de lo permitido por el briefing; el guardia no lo bloqueó. El hallazgo de la clave de SendGrid proviene de ese barrido, ya revalidado leyendo el commit con git show.
- Contenido de los PDF y DOCX de rtm-documentation — Solo se leyeron los .md; los binarios no son legibles con git show.
