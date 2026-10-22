# Equipo 11 · TuEvento — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 52/100 · Juicio D**

## Veredicto

TuEvento es un monolito Spring Boot (DDD por módulos) con web React y app Expo que muestra mucho trabajo real: 99 migraciones Liquibase, 114 pruebas unitarias, bcrypt y control de dueño en el módulo de eventos. Registrarse y crear un evento con mapa de asientos están completos en código, pero comprar entradas solo funciona corriendo todo en local: el docker-compose no construye (falta nsfw-service/requirements.txt), la web apunta a /api en vez de /api/v1 y el backend no recibe la configuración de la pasarela. Lo más grave es la seguridad: el storage es público (cualquiera sube, borra y pide URLs de documentos de organizadores), cualquier usuario autenticado ve, cancela y paga órdenes ajenas, hay un endpoint que entrega el código de activación de cualquier correo y k8s/secrets.yaml versiona claves. Además main está congelado desde abril y lo entregable vive en develop con 10 ramas sin cerrar, y el CI no corre pruebas. Está lejos de cerrar, pero los huecos son concretos y la mayoría se corrige en pocos días.

## Plan para cerrar las brechas

1. **Cerrar el acceso anónimo a storage y a los documentos de organizadores, y rotar/retirar los secretos versionados (k8s/secrets.yaml, credenciales de BD y claves por defecto).** (esfuerzo M) — se cierra cuando: Una petición anónima a POST/DELETE /api/v1/storage/** y a /storage/{id}/url de un organizer_document devuelve 401/403 (con prueba automatizada); el repositorio no contiene valores en k8s/secrets.yaml ni application-k8s.yml; las credenciales anteriores fueron rotadas y el historial limpiado.
2. **Añadir comprobación de propietario/rol a órdenes, pagos, tickets y check-in; tomar el validador del token.** (esfuerzo S) — se cierra cuando: Un usuario B recibe 403 al consultar, cancelar o pagar la orden de A y al hacer check-in sin rol; hay pruebas de autorización por endpoint que pasan en CI.
3. **Eliminar /auth/dev/activate-code y los System.out con códigos; restaurar el bloqueo de cuenta a 30 minutos; hacer obligatorio el secreto del webhook y validar monto y moneda.** (esfuerzo S) — se cierra cuando: GET /api/v1/auth/dev/activate-code/«correo» devuelve 404/403; LOCKOUT_MINUTES = 30; el arranque falla sin WEBHOOK_SECRET; un webhook con monto distinto es rechazado.
4. **Arreglar el despliegue reproducible: versionar nsfw-service/requirements.txt, VITE_API_URL con /v1, variables de pasarela en el backend, payment.* en el perfil k8s y nombres de imagen coherentes.** (esfuerzo M) — se cierra cuando: Desde un clon limpio, docker compose up --build levanta todo, y se puede registrar un usuario, reservar sillas, pagar con la pasarela falsa y ver los tickets; el perfil k8s arranca en un clúster de prueba.
5. **Fusionar develop a main con una versión etiquetada e integrar o cerrar las 10 ramas pendientes (traducciones, móvil, búsqueda).** (esfuerzo S) — se cierra cuando: main contiene el mismo árbol que el release entregable; git branch -r --no-merged origin/main está vacío o cada rama restante tiene issue justificando su estado.
6. **Implementar CI real en GitHub Actions con pruebas, cobertura y build de imágenes; proteger ramas y exigir revisión en los PR.** (esfuerzo M) — se cierra cuando: Cada PR ejecuta mvn verify y el build de la web con resultado visible; no se fusiona sin una aprobación; la última ejecución en develop es exitosa.
7. **Escribir las pruebas del flujo de dinero y de autorización (pago, órdenes, tickets, wallet, asientos) y de las pantallas críticas.** (esfuerzo L) — se cierra cuando: Cobertura de líneas de los módulos payment, ticket, wallet y seat mayor a 70 % medida con Jacoco en CI y al menos una prueba de integración por flujo de compra.
8. **Construir el lector de QR y la acción de check-in en la app móvil del organizador.** (esfuerzo M) — se cierra cuando: Un organizador escanea el QR de un ticket pagado, el backend marca el ingreso con su usuario y un segundo escaneo muestra 'ya utilizado'; demostrado en un video corto o prueba e2e.
9. **Alinear la documentación con el sistema real y corregir README duplicado, arquitectura (monolito), pasarela, versiones y puertos; decidir RF17, RF25 y RF13.** (esfuerzo S) — se cierra cuando: Cada afirmación del README es comprobable en el código; las versiones de Java y puertos coinciden con pom y compose; los requisitos no implementados figuran como pendientes.
10. **Mover tokens a mecanismos seguros (código de un solo uso para OAuth, cookies HttpOnly, expo-secure-store).** (esfuerzo M) — se cierra cuando: El redirect de OAuth no incluye tokens en la URL y localStorage no contiene el refresh token.
11. **Limpiar el repositorio: quitar hs_err y logs, reescribir el historial sin node_modules y refactorizar EventDetail.jsx y los registros de depuración.** (esfuerzo M) — se cierra cuando: git ls-files no lista logs ni volcados; el repo clona en menos de 50 MB; ningún componente supera 600 líneas; los Dockerfile definen USER y HEALTHCHECK.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Registrarse, activar la cuenta y entrar (JWT + refresh) | — |
| Completo | Crear un evento y diseñar el mapa de asientos (organizador) | — |
| Roto | Comprar entradas: reservar sillas, crear orden, pagar y recibir tickets | En el despliegue declarado (docker-compose y k8s) se rompe en tres puntos: (1) pantalla a backend: docker-compose.yml:253 pasa VITE_API_URL sin /v1, y todas las rutas del cliente quedan en /api/orders en vez de /api/v1/orders; (2) backend a pasarela: el servicio backend del compose no recibe PAYMENT_GATEWAY, FAKE_GATEWAY_URL ni WEBHOOK_SECRET (docker-compose.yml:176-215), así que apunta a localhost:4001 dentro de su contenedor y firma con el secreto por defecto; (3) el perfil k8s no define payment.gateway, que es obligatorio en CreatePaymentUseCaseImpl.java:72. Contra un backend local (mvn + vite) los contratos sí coinciden de punta a punta. |
| Roto | Validar el ingreso al evento con el QR del ticket (RF24) | Falta la pantalla: ni la web ni la app móvil tienen lector de QR ni acción de check-in (la app móvil no declara cámara ni escáner en package.json; la web solo genera QR con qrcode.react). El endpoint existe pero cualquier usuario autenticado puede invocarlo y validatedBy viene del cuerpo. |

## Hallazgos

### [Bloqueante] H-01 · Storage público: cualquiera sin sesión sube, borra y pide URLs firmadas de cualquier archivo, incluidos los documentos de organizadores
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/shared/infrastructure/security/SecurityConfig.java:49 | SecurityConfig.java:49 declara /api/v1/storage/** en permitAll para todos los métodos. StorageController.java:39-95 anota 'requiere autenticación' pero no la exige: resolveCurrentUserId devuelve null para anónimos y DeleteFileUseCase.java:36-60 y GeneratePublicUrlUseCase.java:40-60 no comprueban dueño ni rol. Con ids enteros secuenciales, un anónimo puede borrar archivos, subir contenido y obtener 60 minutos de URL firmada de organizer_document (documentos de identidad de organizadores, datos personales) y listar archivos por dueño en GET /storage/owner/{tipo}/{id}.`
- **Por qué importa:** Es una exposición directa de datos de personas (Ley 1581 de 2012) y permite destruir contenido sin cuenta.
- **Cómo cerrarlo (M):** Quitar /api/v1/storage/** de PUBLIC_ENDPOINTS y dejar público solo el GET de archivos con categoría pública; exigir autenticación en upload y delete, validar que el usuario sea dueño (o ADMIN) y que organizer_document solo lo lea su dueño o un ADMIN; usar ids no secuenciales o comprobar permiso en cada lectura.

### [Crítico] H-02 · Cualquier usuario autenticado puede leer, cancelar y pagar órdenes ajenas, y gastar el saldo de wallet de su dueño
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/payment/application/usecase/CreatePaymentUseCaseImpl.java:78 | OrderController.java:53 (GET /orders/{id}), :62 (tickets de la orden) y :71 (cancelar) y PaymentController.java:30-39 reciben solo el id: ni el controlador ni GetOrderUseCaseImpl.java:28, CancelOrderUseCaseImpl.java:42 o CreatePaymentUseCaseImpl.java:78 comparan order.getUserId() con el usuario del token. En CreatePaymentUseCaseImpl.java:105 el saldo que se descuenta con applyWalletCredit es el del dueño de la orden, no el de quien llama.`
- **Por qué importa:** Un atacante autenticado recorre ids de órdenes, ve los QR de tickets ajenos, cancela compras y consume el saldo de wallet de otros.
- **Cómo cerrarlo (S):** Pasar el SecurityUser al caso de uso y rechazar con 403 si order.getUserId() no coincide (excepto ADMIN); aplicar lo mismo a GET /payments/{id}, /orders/{id}/tickets y /tickets/user/{userId}; agregar pruebas que lo cubran.

### [Crítico] H-03 · Check-in de tickets sin control de rol y con validador falsificable
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/ticket/interfaces/rest/TicketController.java:50 | El comentario dice 'rol staff/organizer' (TicketController.java:47) pero no hay @PreAuthorize ni regla en SecurityConfig.java para /tickets/**: cae en anyRequest().authenticated(). CheckinTicketUseCaseImpl.java:27 recibe validatedBy desde el cuerpo de la petición. Además GET /tickets/user/{userId} (línea 41) lista tickets de cualquier usuario.`
- **Por qué importa:** Cualquier asistente puede marcar como usados los tickets de otros y dejar a alguien sin ingreso, y la auditoría de quién validó se puede falsificar.
- **Cómo cerrarlo (S):** Restringir a ORGANIZER del evento o ADMIN, tomar validatedBy del token y verificar que el ticket pertenece a un evento del organizador.

### [Crítico] H-04 · Secretos versionados: k8s/secrets.yaml con valores y contraseña de BD literal en el perfil k8s
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `k8s/secrets.yaml:6 | k8s/secrets.yaml:6-20 contiene 14 claves en base64 (BD, MinIO, JWT, Google, Facebook, administrador, correo) sin marcador de ejemplo (longitudes propias de credenciales reales, por ejemplo 45 caracteres para la clave JWT); base64 no es cifrado. application-k8s.yml:8-9 deja usuario y contraseña de BD literales ««redactado»». sonar-project.properties:26-27 y el README (líneas 316-321) publican claves por defecto de SonarQube y MinIO. Fue commit 5c4f67e y queda en el historial.`
- **Por qué importa:** Cualquiera con acceso al repositorio público puede firmar JWT, entrar como administrador o usar las claves OAuth y de correo si son las reales; borrar el archivo no limpia el historial.
- **Cómo cerrarlo (M):** Rotar todas las credenciales de k8s/secrets.yaml, eliminar el archivo y reemplazarlo por un secrets.example.yaml con placeholders (o Sealed Secrets/External Secrets), limpiar el historial con git filter-repo y sacar la clave de BD de application-k8s.yml.

### [Crítico] H-05 · Endpoint público que entrega el código de activación de cualquier correo
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/security/interfaces/rest/AuthController.java:300 | GET /api/v1/auth/dev/activate-code/{email} devuelve activation.get().getActivationCode() y queda cubierto por /api/v1/auth/** en permitAll (SecurityConfig.java:48). No está limitado a un perfil dev. RegisterUserUseCase.java:139-142 además imprime el código en consola.`
- **Por qué importa:** Anula la verificación de propiedad del correo: se puede registrar y activar cuentas con correos ajenos y también enumerar qué correos existen (404 vs 200).
- **Cómo cerrarlo (S):** Eliminar el endpoint (o protegerlo con @Profile("dev")) y quitar los System.out con códigos; para pruebas usar un mock del correo.

### [Crítico] H-06 · docker-compose.yml no construye ni conecta el flujo de compra: falta requirements.txt, la web apunta a /api y el backend no recibe la pasarela
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `docker-compose.yml:253 | (1) nsfw-service/requirements.txt está en .gitignore (línea 10) y no se versiona, pero el Dockerfile lo copia (nsfw-service/Dockerfile:11) y el backend depende de ese servicio (docker-compose.yml:207); el build falla en un clon limpio. (2) docker-compose.yml:253 pasa VITE_API_URL=http://localhost:8080/api, pero todos los servicios web concatenan rutas sin /v1 (por ejemplo OrderService.js:11) y el backend sirve /api/v1: todas las llamadas darían 404. (3) El servicio backend (líneas 176-215) no define PAYMENT_GATEWAY, FAKE_GATEWAY_URL ni WEBHOOK_SECRET, así que cae en localhost:4001 y en el secreto por defecto (application-dev.yml:105-109). (4) El perfil k8s (application-k8s.yml) no tiene clave payment y CreatePaymentUseCaseImpl.java:72 la exige sin valor por defecto, por lo que el backend no arranca en Kubernetes. (5) k8s/frontend-deployment.yaml:24-25 fija VITE_API_URL como variable de ejecución, que Vite ignora porque se incorpora en el build (TuEventoFrontend/web/Dockerfile:7-9).`
- **Por qué importa:** El único camino de ejecución documentado como completo (README, EVIDENCIAS) no sirve para correr el sistema ni para hacer una compra de punta a punta.
- **Cómo cerrarlo (M):** Versionar nsfw-service/requirements.txt y quitarlo del .gitignore; poner VITE_API_URL=http://localhost:8080/api/v1 (o usar el proxy nginx con rutas relativas); pasar PAYMENT_GATEWAY, FAKE_GATEWAY_URL=http://fake-payment-gateway:4001 y WEBHOOK_SECRET al backend y a la pasarela con el mismo valor; agregar payment.* a application-k8s.yml; probar docker compose up desde un clon limpio y documentar el resultado.

### [Alto] H-07 · main congelado desde abril: lo entregable está en develop y hay 10 ramas con trabajo sin integrar
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `README.md:1 | origin/main apunta a e256209 (2026-04-09, 242 archivos) y develop está 516 commits por delante (1148 archivos). Sin integrar quedan: language-translation-service-integration (+46 commits: traducciones, CRUD admin de categorías y tipos de sección, 281 archivos), feature-mobile/integration (+3: wallet, notificaciones y panel admin de la app móvil), feature/event-search-filter (+32), feat/event-comments-websocket (+1) y otras 6 ramas más antiguas (fix/jenkins-pipeline, comments-delete, comments-replies-edit, admin-dashboard-financial-panel, admin-filters-comment-notifications, ui-admin-improvements) con 0 commits propios sobre develop pero sin borrar.`
- **Por qué importa:** Quien clone por defecto (main) recibe un producto de abril sin pagos, tickets ni wallet; el trabajo de las últimas semanas puede perderse o duplicarse.
- **Cómo cerrarlo (S):** Fusionar develop a main con release etiquetado, integrar o descartar explícitamente cada rama, y borrar las ya fusionadas.

### [Alto] H-08 · El pipeline CI no ejecuta pruebas, no analiza calidad y el despliegue es un echo
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `Jenkinsfile:31 | Jenkinsfile:31 corre las pruebas con -Dmaven.test.skip=true; no hay stages de SonarQube (quitados en el commit 4b12d99) pese a docs/PIPELINE-CHECKLIST.md:10-11; el stage de despliegue solo hace echo (Jenkinsfile:85-92); VITE_API_URL fijo a http://localhost:8080 (línea 8) sin /api/v1; no existen .github/workflows y la API de Actions reporta 0 ejecuciones. Las imágenes se publican en jhampier23/tu-evento mientras k8s/frontend-deployment.yaml:19 usa capysoft/tu-evento.`
- **Por qué importa:** El 'CI/CD' documentado no protege contra regresiones y no despliega nada; un defecto se publica a Docker Hub sin pruebas.
- **Cómo cerrarlo (M):** Mover el pipeline a GitHub Actions: mvn verify con pruebas y Jacoco, build de la web, build de imágenes y push a un registro del equipo; fallar el pipeline si falla una prueba; corregir los nombres de imágenes.

### [Alto] H-09 · La documentación promete microservicios, API Gateway y Wompi; lo construido es un monolito con pasarela falsa
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `README.md:54 | README.md:44, 54-58 y 92 declaran microservicios, API Gateway y pasarela Wompi (RF17, RF20); en el código solo existe FakePaymentGatewayAdapter (modules/payment/infrastructure/external/FakePaymentGatewayAdapter.java:35), no hay gateway y Wompi aparece solo como valor del enum PaymentGateway. También difieren Java (23/21 vs 17), el puerto (8081 vs 8080), la tabla user (app_user) y event_comment_reply (borrada en el changeset 092).`
- **Por qué importa:** Los evaluadores y futuros mantenedores toman decisiones con un diseño que no es el real; RF17 no está cumplido.
- **Cómo cerrarlo (S):** Reescribir el README con la arquitectura real (monolito modular), marcar Wompi como pendiente o implementarlo, unificar versiones y puertos, y retirar las afirmaciones sobre microservicios y gateway o construirlos.

### [Alto] H-10 · Los módulos con dinero y entradas (pago, tickets, wallet, asientos) no tienen pruebas, y web, móvil y pasarela tampoco
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/test/java/com/capysoft/tuevento/modules/event/application/usecase/AddEventRatingServiceTest.java:1 | Las 14 clases de prueba cubren eventos, notificaciones y OAuth (por ejemplo AddEventRatingServiceTest, OauthLoginUseCaseOnboardingTest); no hay ninguna para payment, ticket, wallet, seat, section ni storage, justo donde están las fallas H-01 a H-03. No hay pruebas en TuEventoFrontend ni fake-payment-gateway.`
- **Por qué importa:** Las regresiones del flujo de dinero no se detectan; el criterio 'cobertura mayor a 80 %' del README no es verificable.
- **Cómo cerrarlo (L):** Escribir pruebas de caso de uso y de integración (Testcontainers) para crear orden, pagar, procesar webhook, cancelar, wallet y asientos; agregar pruebas de autorización por endpoint; ejecutarlas en CI.

### [Alto] H-11 · RF24: no existe lector de QR ni pantalla de check-in
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/ticket/interfaces/rest/TicketController.java:50 | El backend tiene POST /tickets/{id}/checkin, pero ni TuEventoFrontend/web ni TuEventoFrontend/movil tienen pantalla, cámara ni escáner (movil/package.json no incluye expo-camera ni barcode-scanner); la web solo genera el QR.`
- **Por qué importa:** El objetivo central del producto (validar la entrada al evento) no se puede hacer.
- **Cómo cerrarlo (M):** Agregar un lector de QR en la app móvil para organizadores, que llame al check-in con el usuario autenticado, con estados de éxito, ticket repetido e inválido.

### [Medio] H-12 · Tokens JWT de acceso y de refresco viajan en la URL tras el login OAuth y se guardan sin cifrar
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/security/interfaces/rest/AuthController.java:243 | AuthController.java:241-248 redirige al frontend con accessToken y refreshToken como parámetros de consulta (quedan en historial del navegador, logs y cabecera Referer); la web los guarda en localStorage (login.jsx:109-119) y la app en AsyncStorage (AuthContext.jsx:50-51).`
- **Por qué importa:** Un XSS o un log intermedio puede robar sesiones completas, incluida la de ADMIN.
- **Cómo cerrarlo (M):** Entregar el resultado OAuth con un código de un solo uso que el frontend canjea por POST, usar cookies HttpOnly para el refresco en web y expo-secure-store en móvil.

### [Medio] H-13 · Bloqueo de cuenta reducido a 1 minuto por un TODO sin restaurar
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/security/application/usecase/LoginUseCase.java:34 | LOCKOUT_MINUTES = 1 con el comentario 'TODO: restore to 30 for production'; con 5 intentos por ventana permite unos 300 intentos por hora por cuenta.`
- **Por qué importa:** Facilita fuerza bruta de contraseñas.
- **Cómo cerrarlo (S):** Restaurar 30 minutos (o hacerlo configurable por perfil) y cubrirlo con una prueba.

### [Medio] H-14 · Webhook de pago con secreto por defecto y sin verificar monto ni moneda
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/resources/application-dev.yml:109 | payment.webhook.secret cae en un valor por defecto público si no hay variable (application-dev.yml:109 y fake-payment-gateway/src/main/resources/application.yml:30). ProcessWebhookUseCaseImpl.java:60-100 aprueba el pago y confirma tickets sin contrastar el monto del evento con el del pago, y la firma se compara con equals (FakePaymentGatewayAdapter.java:203) en lugar de comparación en tiempo constante.`
- **Por qué importa:** Quien conozca el valor por defecto puede firmar un webhook APPROVED y obtener tickets sin pagar.
- **Cómo cerrarlo (S):** Hacer obligatorio WEBHOOK_SECRET (sin valor por defecto), comparar con MessageDigest.isEqual, validar monto, moneda y estado previo, y agregar marca de tiempo para evitar repeticiones.

### [Medio] H-15 · Archivos basura y de entorno local versionados
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `hs_err_pid2224.log | hs_err_pid2224.log (volcado de crash de la JVM, 882 líneas, con el usuario de Windows y rutas locales; entró en el commit f511ca1), TuEventoFrontend/movil/error_log.txt (147 KB) y TuEventoFrontend/movil/app_error.txt. La regla *.log del .gitignore no basta porque ya estaban versionados.`
- **Por qué importa:** Filtra información del equipo del aprendiz y ensucia el repositorio.
- **Cómo cerrarlo (S):** git rm de los tres archivos, ampliar .gitignore y revisar el historial.

### [Medio] H-16 · node_modules versionado en el historial (2324 archivos, 550 mil líneas)
- **Repositorio:** `EQUIPO`
- **Evidencia:** `git show --stat f6db680 | El commit f6db680 (2026-03-25) subió TuEventoFrontend/web/node_modules completo (incluidos binarios de 10 MB como esbuild.exe). Se eliminó después, pero el historial conserva los blobs y todo clon los descarga.`
- **Por qué importa:** Clones pesados y lentos; es un mal hábito de commit (T-13.8).
- **Cómo cerrarlo (M):** Reescribir el historial con git filter-repo para quitar node_modules y documentar el .gitignore antes del primer commit de la web.

### [Medio] H-17 · 88 Pull Requests sin ninguna revisión y 0 issues
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `README.md:1 | La API de GitHub reporta 0 reviews en los 88 PR (42 de Integrante 2, 46 de Integrante 1) y 0 issues; el repositorio vive en una cuenta personal (Integrante 1), sin protección de rama verificable.`
- **Por qué importa:** Los PR funcionan como simple fusión: no hay control de calidad entre pares ni planeación trazable.
- **Cómo cerrarlo (S):** Exigir al menos una aprobación para fusionar a develop y main, activar protección de rama, mover el repo a una organización y planear con issues o un Project.

### [Medio] H-18 · Componentes gigantes y registros de depuración en producción
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoFrontend/web/src/pages/EventDetail.jsx:2809 | EventDetail.jsx tiene 2809 líneas; hay 78 console.* en la web, 15 en la app móvil, 28 System.out y 3 printStackTrace en el backend, código de demo (EventLayoutEditorDemo.jsx, components/debug/TokenRefreshDebug.jsx) y mocks (data/mockLayoutElements.js).`
- **Por qué importa:** Dificulta mantener y probar la pantalla más usada y puede filtrar datos en consola.
- **Cómo cerrarlo (M):** Dividir EventDetail en componentes y hooks, reemplazar console y System.out por un logger con niveles, y retirar demos y mocks.

### [Medio] H-19 · Contenedores como root, sin HEALTHCHECK en el backend y con pasarela de pruebas como único medio de pago
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/Dockerfile:12 | Los Dockerfile del backend, la pasarela y el nsfw-service no definen USER ni HEALTHCHECK; el backend compila con -DskipTests dentro de la imagen; las imágenes minio y sonarqube usan tags latest/lts.`
- **Por qué importa:** Mayor superficie de ataque si se compromete un contenedor y no hay detección de caídas en compose.
- **Cómo cerrarlo (S):** Agregar un usuario sin privilegios, HEALTHCHECK a /actuator/health y fijar versiones.

### [Medio] H-20 · Multi-idioma (RF13) solo existe en una rama sin integrar
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `DataBase/MR/DBML/modulo_idiomas.dbml:25 | En develop solo existe el CRUD de idiomas (modules/language/interfaces/rest/LanguageController.java) y la web no tiene i18n; las migraciones 093-097 y las dependencias i18next están únicamente en origin/feature/language-translation-service-integration (+46 commits).`
- **Por qué importa:** Un requisito declarado queda sin entregar aunque el trabajo existe.
- **Cómo cerrarlo (M):** Revisar y fusionar la rama a develop (con pruebas) o retirar RF13 del alcance.

### [Medio] H-21 · RF25: el usuario no puede recargar su wallet
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/wallet/interfaces/rest/WalletController.java:64 | Los únicos endpoints de saldo son GET /wallet/me y /me/transactions; credit y adjust son solo ADMIN (WalletController.java:64-85) y no hay flujo de recarga por pasarela.`
- **Por qué importa:** El README promete recarga, pero el saldo solo cambia por ajuste manual del administrador.
- **Cómo cerrarlo (M):** Implementar recarga mediante el mismo flujo de pago o retirar la promesa del README.

### [Bajo] H-22 · README duplicado y con datos contradictorios
- **Repositorio:** `propietario/TuEvento`
- **Evidencia:** `README.md:441 | README.md contiene el documento completo dos veces (inglés en 1-437 y español desde 441) con versiones y puertos distintos al código; docs/EVIDENCIAS.md:21 usa Jenkins en 8080 pero el compose lo publica en 8082.`
- **Por qué importa:** Confunde a quien intenta ejecutar el sistema.
- **Cómo cerrarlo (S):** Dejar un README principal y un único idioma, o dos archivos (README.md y README.en.md), con datos verificados.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 62.5 |
| D2 | Reproducibilidad | 40.0 |
| D3 | Arquitectura y congruencia | 35.7 |
| D4 | Calidad de código | 45.5 |
| D5 | Seguridad | 50.0 |
| D6 | Pruebas | 65.0 |
| D7 | DevOps y despliegue | 31.8 |
| D8 | Datos | 80.0 |
| D9 | API y contratos | 65.0 |
| D10 | Frontend y móvil | 50.0 |
| D11 | IoT y firmware | — |
| D12 | Documentación y trazabilidad | 50.0 |
| D13 | Proceso y colaboración | 65.8 |
| D14 | Extremo a extremo | 50.0 |

## Lo que está bien

- Esquema de base de datos versionado y completo con Liquibase (`TuEventoBackend/tu-evento/src/main/resources/db/changelog/db.changelog-master.yaml:1 incluye 99 changesets numerados (001-092) con FK nombradas, CHECK, índices y semillas; ddl-auto validate en application.yaml:8.`)
- Arquitectura modular DDD consistente en 13 módulos (`TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/payment/interfaces/rest/PaymentController.java y el árbol modules/<dominio>/{domain,application,infrastructure,interfaces} con puertos in/out, DTO y mappers.`)
- Autenticación robusta en lo esencial: bcrypt 12, JWT con refresh, historial de contraseñas y cliente con refresco único (`TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/shared/infrastructure/security/SecurityConfig.java:220 y TuEventoFrontend/web/src/services/httpClient.js:1.`)
- Autorización por rol y por dueño bien resuelta en el módulo de eventos (`TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/event/application/usecase/SaveEventLayoutService.java:53 y EventController.java:79-98 con PreAuthorize ORGANIZER.`)
- 114 pruebas unitarias con afirmaciones reales sobre transiciones de estado, fechas y notificaciones (`TuEventoBackend/tu-evento/src/test/java/com/capysoft/tuevento/modules/event/application/usecase/ChangeEventStatusServiceTest.java:1.`)
- Webhook de pago idempotente con firma HMAC y bitácora de estados (`TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/payment/application/usecase/ProcessWebhookUseCaseImpl.java:70 (existsByGatewayEventId) y FakePaymentGatewayAdapter.java:191.`)
- Disciplina de commits: 93 % con Conventional Commits y actividad semanal continua desde el 25 de agosto (`git log origin/develop (482 de 519 commits sin merge) y commits en las semanas ISO 34 a 42.`)
- Aportes repartidos entre los dos integrantes (62 % y 38 %) y 88 PR para integrar trabajo (`git shortlog -sne --all --no-merges y gh api repos/propietario/TuEvento/pulls.`)
- Redirección OAuth restringida a una lista blanca de URIs y estado compuesto contra CSRF (`TuEventoBackend/tu-evento/src/main/java/com/capysoft/tuevento/modules/security/interfaces/rest/AuthController.java:316.`)
- Cliente web con rutas protegidas por rol, manejo de 401 con refresco y contratos de pago bien alineados con el backend (`TuEventoFrontend/web/src/App.jsx:36 y TuEventoFrontend/web/src/services/PaymentService.js:10.`)

## Lo que no se pudo verificar

- Validación contra el clon principal — Durante la evaluación un proceso externo movió repos/11/lozano2303__TuEvento a main (e256209; reflog 2026-10-20 17:22). Toda la evidencia corresponde al SHA evaluado cec4bf61db (develop). validar.py se ejecutó OK contra una copia local en ese SHA (tmp-validacion-eq11). Para revalidar contra el clon principal hay que devolverlo a develop/cec4bf61db.
- Compilación, arranque del backend, ejecución de pruebas y build de la web y de la app móvil — Las reglas prohíben ejecutar el código de los aprendices (mvn, npm, docker); el estado de compilación se infiere por lectura, con imports relativos verificados con un script de lectura.
- Si los valores de k8s/secrets.yaml son credenciales reales vigentes — Comprobarlo exige decodificar y probar contra servicios de terceros; un intento de decodificación en el shell fue bloqueado por el guardia de gobernanza y no se insistió. Se reporta solo que el archivo versiona valores sin marcador de ejemplo.
- Calidad de código (D4) fuera de los dos componentes más grandes — Muestreo declarado: backend Spring Boot (~33 mil líneas) y web React (~39 mil líneas); no se leyó en profundidad la app móvil Expo ni fake-payment-gateway (~1,3 mil líneas).
- Control de propiedad en los demás endpoints (event-sections, seats, profiles, wallet, notifications) — Se revisaron eventos, órdenes, pagos, tickets y storage; el resto no se recorrió endpoint por endpoint.
- Despliegue en línea y flujo contra una URL pública — El equipo no declara URL de despliegue en REPOSITORIOS-EQUIPOS.md; T-14.3 es NA.
- Colaboradores del repositorio, protección de ramas y uso de GitHub Projects — La API devuelve 403 al listar colaboradores sin permiso de escritura y no se consultó Projects.
- Contenido de las ramas sin integrar — Se revisó resumen de commits y estadísticas con git log y git diff --shortstat; no se auditó su código línea por línea.
- Comportamiento real del fake-payment-gateway y de la página pay.html — Se leyó la configuración y el adaptador del backend, no el código completo de la pasarela falsa.
