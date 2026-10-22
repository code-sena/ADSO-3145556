# Equipo 1 · Alerta Mujer — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 37/100 · Juicio D**

## Veredicto

Alerta Mujer tiene mucho trabajo hecho y bien encaminado: movil y web conectan con un backend real con base de datos versionada, autenticacion y subida segura de evidencias. En el SHA evaluado (develop del 14 de octubre) el nucleo del producto falla: al activar el SOS no se avisa a ningun contacto, la recuperacion de contrasena es simulada y el panel de administracion no puede bloquear usuarias ni atender alertas. Ademas hay riesgos graves de seguridad: la clave privada de Firebase esta en el historial, la clave JWT por defecto es publica y cualquier usuaria puede leer contactos de otra. Gran parte de lo que falta (microservicios, push, chat, grabacion, Jenkins, documentacion) ya existe en ramas sin integrar, asi que el cierre depende sobre todo de integrar, probar y corregir contratos; estan a varias semanas de poder entregarlo.

## Plan para cerrar las brechas

1. **Rotar y limpiar secretos: nueva cuenta de servicio de Firebase, nueva clave JWT sin valor por defecto, rotar contrasena del admin de produccion y de la base.** (esfuerzo M) — se cierra cuando: El backend falla al arrancar sin JWT_SECRET; git log -S sobre la clave vieja ya no devuelve commits alcanzables o la clave esta revocada en la consola de Firebase.
2. **Cerrar los dos accesos indebidos de datos: ownership en GET /api/contacts/user/{id} y quitar el id del cuerpo antes de save en alertas, contactos y dispositivos.** (esfuerzo S) — se cierra cuando: Pruebas automatizadas donde una usuaria recibe 403 al pedir contactos ajenos y su POST con id ajeno crea una fila nueva sin tocar la existente.
3. **Hacer que el SOS avise: registrar token FCM desde el movil, notificar a contactos al crear la alerta (push y SMS o correo) e integrar la rama alertas-recibidas.** (esfuerzo L) — se cierra cuando: En una demostracion, al pulsar SOS el telefono de un contacto recibe la notificacion con la ubicacion en menos de 30 segundos.
4. **Integrar las ramas de trabajo en develop por PR con revision y promover develop a main.** (esfuerzo M) — se cierra cuando: main contiene la ultima version funcional; git rev-list --count main..develop devuelve 0 y no quedan ramas con trabajo sin integrar.
5. **Corregir los desajustes de contrato del panel admin y contactos (PATCH, estado de usuaria, estado de alerta, user-reports, descarga de evidencias).** (esfuerzo M) — se cierra cuando: El admin puede bloquear una usuaria, marcar una alerta como atendida y descargar una evidencia real desde la web, con prueba de contrato por cada endpoint.
6. **Implementar la recuperacion de contrasena real en backend, web y movil.** (esfuerzo M) — se cierra cuando: Una usuaria solicita el codigo por correo, lo ingresa y entra con la nueva clave; la ruta reset-password existe.
7. **Endurecer CORS, actuator y la imagen Docker.** (esfuerzo S) — se cierra cuando: Una peticion con Origin no listado no recibe cabeceras CORS y /actuator/metrics responde 401 sin token.
8. **Agregar CI con compilacion y pruebas en los tres repositorios y subir la cobertura de pruebas reales (contrato, guards, servicios).** (esfuerzo M) — se cierra cuando: Cada PR muestra una ejecucion verde de GitHub Actions y la web no tiene pruebas should create.
9. **Decidir la arquitectura y alinear documentacion: monolito modular o microservicios integrados; eliminar stubs; actualizar README y mover SRS y MER a docs/.** (esfuerzo M) — se cierra cuando: README, compose y carpetas describen lo mismo; docker compose up levanta el sistema desde cero siguiendo solo el README.
10. **Limpiar repositorios: quitar target/, .deb, notas de agentes y documentos de trabajo; apagar console.log sensibles y guardar el JWT con SecureStore.** (esfuerzo S) — se cierra cuando: git ls-files no lista target/, .deb ni .agents y el build de produccion del movil no imprime cuerpos de peticiones.
11. **Repartir y registrar el trabajo: tablero con issues por HU, revisiones cruzadas y colaboradores o una organizacion como duena de los repos.** (esfuerzo M) — se cierra cuando: Ninguna persona supera el 60 % de los commits de las ultimas 4 semanas y los tres repos tienen al menos dos mantenedores.
12. **Publicar contrato OpenAPI, validar DTOs, versionar la API y corregir configuracion de Firebase web y manejo de 401 en el movil.** (esfuerzo M) — se cierra cuando: /v3/api-docs es consultable, los endpoints rechazan cuerpos invalidos con 400 y el movil renueva o cierra sesion al recibir 401.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Registro e inicio de sesion (movil y web) | — |
| Roto | Activar SOS y avisar a los contactos de emergencia | Notificacion a los contactos: crear la alerta solo inserta la fila y registra ubicaciones; no se envia push, SMS ni correo a los contactos, el movil no registra token FCM y FirebaseConfig.sendToDevice siempre lanza excepcion. |
| Roto | Gestionar contactos de emergencia (movil y web) | Edicion desde la web: emergency-contact.service.ts:53 usa PATCH y el backend solo declara PUT (EmergencyContactController.java:45). El movil si funciona (PUT). Ademas GET /api/contacts/user/{id} no valida dueno. |
| Roto | Capturar y consultar evidencias de una alerta | Captura en el movil: no existe grabacion ni llamada a /api/evidences/upload en develop (solo en la rama feat/alert-recording). La web de usuaria si lista y sube; el panel admin descarga archivos vacios (simulado). |
| Roto | Administracion de usuarias y alertas (panel admin web) | Acciones de escritura: PATCH /api/admin/users/{id} no existe; marcar alerta atendida envia estado en vez de status y el backend exige ser la dueña de la alerta. Las lecturas GET admin si funcionan. |

## Hallazgos

### [Bloqueante] H-01 · Al activar el SOS nadie avisa a los contactos de emergencia
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/java/com/alertamujer/alerts/application/service/AlertService.java:92 createAlert solo guarda la fila. La unica notificacion (AlertService.java:126, FcmServiceImpl.java:42) se dispara al resolver o cancelar y va al primer dispositivo de la propia usuaria. FirebaseConfig.java:69 hace que sendToDevice siempre lance excepcion y FirebaseConfig.java:86 la traga. El movil no registra ningun token FCM (sin expo-notifications en Frontend-movil/package.json) y FcmServiceImpl.java:76 deja la ubicacion como TODO.`
- **Por qué importa:** El requisito RF4.2 y el proposito del producto (que alguien reciba la alerta) no existe en develop: la usuaria cree que pidio ayuda y nadie se entera.
- **Cómo cerrarlo (L):** Implementar el envio a contactos (push a contactos que tengan la app, y SMS o correo) al crear la alerta, registrar el token FCM desde el movil y probar de extremo a extremo. El codigo de alertas recibidas y push web ya existe en ramas sin integrar.

### [Bloqueante] H-02 · Clave privada de Firebase en el historial git de develop
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/resources/firebase-service-account.json.example y .gitignore:35 (credenciales ignoradas). La clave privada (1 708 caracteres, valor ««redactado»») de la cuenta de servicio del proyecto alerta-mujer esta en el commit bdcebab (2026-10-11, archivo src/main/resources/firebase-service-account.json), ancestro de develop y de decenas de ramas; f9dfdd2 la quito del arbol el 2026-10-14 pero no del historial.`
- **Por qué importa:** Cualquiera con acceso de lectura al repositorio puede enviar notificaciones y usar los servicios de Firebase del proyecto. Se clasifica bloqueante porque expone un secreto (rubrica); el impacto real baja si la clave ya fue revocada, algo que no se pudo comprobar.
- **Cómo cerrarlo (M):** Revocar y regenerar la cuenta de servicio en Firebase, guardar la nueva solo como secreto de Render, y limpiar el historial (git filter-repo) o declarar la clave perdida.

### [Bloqueante] H-03 · Clave de firma JWT y contrasena de base por defecto escritas en codigo, compose y README
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/java/com/alertamujer/identity/application/service/AuthService.java:42 El mismo valor por defecto (««redactado»») esta en JwtAuthenticationFilter.java:30, backend-monolith/src/main/resources/application.properties:22, docker-compose.yml:35, .env.example y README.md:372; la contrasena de base ««redactado»» en docker-compose.yml:8 y README.md:370.`
- **Por qué importa:** Si el entorno no define la variable, el servidor firma con una clave publica en GitHub y cualquiera puede fabricar un token de administrador. Se clasifica bloqueante porque expone un secreto; en Render la variable la genera la plataforma, lo que reduce la explotabilidad en ese despliegue.
- **Cómo cerrarlo (S):** Quitar los valores por defecto (fallar al arrancar si falta JWT_SECRET), rotar la clave en produccion, dejar placeholders vacios en README y .env.example.

### [Bloqueante] H-04 · Cualquier usuaria autenticada puede leer los contactos de emergencia de otra
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/java/com/alertamujer/alerts/infrastructure/controller/EmergencyContactController.java:35 GET /api/contacts/user/{userProfileId} no recibe la identidad ni verifica dueno; EmergencyContactService.java:43 devuelve los contactos de cualquier id (nombre, telefono, correo, parentesco).`
- **Por qué importa:** Datos personales sensibles de mujeres en riesgo, enumerables por id secuencial, con base en la Ley 1581 de 2012.
- **Cómo cerrarlo (S):** Resolver el perfil desde el JWT y permitir id ajeno solo a ADMIN (@PreAuthorize como en LocationLogController.java:50); agregar prueba de acceso denegado.

### [Crítico] H-05 · Guardado por id permite sobrescribir y apropiarse de alertas, contactos y dispositivos ajenos
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/java/com/alertamujer/alerts/application/service/AlertService.java:92 createAlert recibe la entidad completa (AlertController.java:60), fija userProfileId y llama save(); si el cuerpo trae un id existente, Spring Data ejecuta merge y reescribe la fila de otra usuaria asignandola a quien llama. Igual en EmergencyContactService.java:47 y DeviceService.java:47 Hallazgo por lectura de codigo; no se ejecuto.`
- **Por qué importa:** Rompe la propiedad de los datos que los controles de dueno de update y delete pretenden garantizar.
- **Cómo cerrarlo (S):** Usar DTOs de entrada sin id, o poner id en null antes de save, y cubrirlo con pruebas.

### [Alto] H-06 · CORS abierto con credenciales y endpoints de actuator publicos
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/java/com/alertamujer/identity/infrastructure/config/SecurityConfig.java:48 (origen * con credenciales), :36 (/actuator/** permitAll) y backend-monolith/src/main/resources/application.properties:29-30 (health con detalles siempre y metrics).`
- **Por qué importa:** Expone detalles internos sin autenticacion y deja que cualquier sitio use la sesion de una usuaria; la propiedad cors.allowed-origins existe pero el codigo la ignora.
- **Cómo cerrarlo (S):** Leer los origenes permitidos de CORS_ORIGIN, exponer solo health sin detalles y proteger el resto de actuator.

### [Alto] H-07 · Recuperacion de contrasena simulada de punta a punta
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/java/com/alertamujer/identity/application/service/AuthService.java:199 forgotPassword solo imprime el correo y resetPassword responde exito sin cambiar nada; en la web forgot-password.ts:58 solo hace console.log y verify-code.ts:140 navega a /auth/reset-password, ruta que no existe; en el movil useRecuperarContrasenaViewModel.ts:95 y :161 usan setTimeout.`
- **Por qué importa:** La usuaria que olvida su clave no puede recuperar la cuenta y recibe un mensaje de exito falso (RF2.3).
- **Cómo cerrarlo (M):** Implementar token de recuperacion con vencimiento, envio de correo y el cambio de clave real, y conectar las dos apps.

### [Alto] H-08 · develop esta muy por detras del trabajo real y main esta vacio
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/Backend-ALERTA-MUJER:README.md:1 Backend: feature/audit-cleanup-google-signin esta 160 commits delante de develop (887 archivos, 44 157 inserciones) con tres microservicios, Jenkins, chat, alertas recibidas, push web, retencion de evidencias y arquitectura hexagonal. Movil: feature/free-maps-maplibre +115 y feat/alert-recording. Web: feature/audit-cleanup-google-signin +139. main: backend Initial commit, web first commit, movil solo README.`
- **Por qué importa:** Lo que se entrega (main o develop) no es lo que se desarrollo; decenas de ramas del backend, el movil y la web llevan trabajo sin integrar y el avance se pierde si alguien borra una rama.
- **Cómo cerrarlo (M):** Integrar por PR las ramas terminadas en develop, probar y promover develop a main; borrar las ramas fusionadas.

### [Alto] H-09 · Los microservicios declarados no existen en develop; el gateway no enruta
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `README.md:15 declara identity, alert, resource, admin y gateway. services/am-alert-service/src/main/resources/application.properties:4 apunta a la misma base que el monolito; gateway/src/main/java/com/alertamujer/gateway/GatewayApplication.java:16 solo expone /health.`
- **Por qué importa:** La arquitectura documentada y la construida no coinciden; los Dockerfile de services/ y gateway/ no estan en el compose.
- **Cómo cerrarlo (L):** Decidir una arquitectura: o monolito modular documentado, o integrar los servicios de la rama feature/microservicios con su gateway; borrar los stubs.

### [Alto] H-10 · El panel de administracion llama rutas y metodos que el backend no tiene
- **Repositorio:** `propietario/Frond-end-Web`
- **Evidencia:** `alerta-mujer-web/src/app/core/services/users.services.ts:20 (PATCH /api/admin/users/{id} inexistente: no se puede bloquear ni editar usuarias); core/services/alerts.services.ts:40 (PUT /api/alerts/{id} con campo estado; AlertService.java:101 del backend lee status y exige ser la dueña, el admin recibe acceso denegado); core/services/moderator.service.ts:60 (POST y DELETE sobre /api/admin/user-reports inexistentes); core/services/emergency-contact.service.ts:53 (PATCH contra PUT).`
- **Por qué importa:** El administrador solo puede mirar: no puede marcar alertas como atendidas, bloquear usuarias ni crear reportes de moderacion; editar contactos desde la web da 405.
- **Cómo cerrarlo (M):** Alinear contrato: agregar los endpoints admin faltantes (estado de usuaria, estado de alerta con rol ADMIN) y cambiar PATCH a PUT en contactos; probar con un test de contrato.

### [Alto] H-11 · Descarga y estado de evidencias en el panel admin son simulados
- **Repositorio:** `propietario/Frond-end-Web`
- **Evidencia:** `alerta-mujer-web/src/app/core/services/evidence.service.ts:117 descargar devuelve un Blob vacio tras 1 s y solo incrementa un contador; los estados Verificada, Pendiente y Rechazada (evidence-management.ts:242) no existen en el backend, que ademas solo entrega el archivo a su duena (EvidenceController.java del backend, linea 55).`
- **Por qué importa:** Moderacion y revision de evidencias no funcionan y la descarga entrega archivos vacios.
- **Cómo cerrarlo (M):** Definir si el admin puede ver evidencias, agregar el endpoint con auditoria, y eliminar el servicio simulado.

### [Alto] H-12 · La app movil no graba ni sube evidencias y los metodos de activacion son interruptores sin efecto
- **Repositorio:** `propietario/AlertaMujer`
- **Evidencia:** `Frontend-movil/src/contexts/MetodosActivacionContext.tsx:13 agitar, bloqueo y widget solo guardan un booleano en AsyncStorage; no hay codigo de sensores. No existe ninguna llamada a /api/evidences/upload ni grabacion en Frontend-movil/src; en locales/en.json:742 la app promete que las grabaciones quedan 60 dias en la nube. Existe en la rama feat/alert-recording.`
- **Por qué importa:** RF5.1-5.3 y RF6 aparecen en pantalla pero no hacen nada; la usuaria puede confiar en una proteccion que no existe.
- **Cómo cerrarlo (M):** Integrar la rama de grabacion y desactivar o quitar en la interfaz lo que no este implementado hasta entonces.

### [Alto] H-14 · El backend no arranca con las instrucciones del README
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `docker-compose.yml:30 exige APP_DB_PASSWORD que .env.example deja vacia; docker-compose.yml:44 monta un archivo de Firebase inexistente; backend-monolith/src/main/java/com/alertamujer/config/FirebaseConfig.java:37 falla si falta. README.md:130 contradice a Liquibase y no menciona Firebase.`
- **Por qué importa:** Un companero nuevo o el docente no puede levantar el sistema siguiendo el README.
- **Cómo cerrarlo (S):** Reescribir README y .env.example con todas las variables, hacer opcional Firebase en desarrollo y probar el arranque limpio con docker compose up.

### [Alto] H-15 · No hay integracion continua en ningun repositorio
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/Backend-ALERTA-MUJER:render.yaml:1 Sin .github/workflows en ninguna rama de los tres repositorios; la API devolvio 0 ejecuciones en movil y backend; el Dockerfile compila con -DskipTests. El Jenkinsfile solo existe en ramas no integradas.`
- **Por qué importa:** Las pruebas no se ejecutan solas y no hay evidencia de que develop compile.
- **Cómo cerrarlo (S):** Agregar un workflow que compile y pruebe backend, web y movil en cada PR y exigirlo para fusionar.

### [Medio] H-13 · La app imprime en consola contrasenas y tokens y guarda el JWT sin cifrar
- **Repositorio:** `propietario/AlertaMujer`
- **Evidencia:** `Frontend-movil/src/services/api.ts:51 imprime el cuerpo de cada peticion (incluye la clave en login y registro); auth.service.ts:47 imprime la respuesta de login con el token y :66 los datos de registro; AuthContext.tsx:61 guarda el JWT en AsyncStorage; app.json:60 permite HTTP en claro.`
- **Por qué importa:** En un dispositivo depurado o con logcat se filtran credenciales; el token puede leerse de un telefono con acceso root.
- **Cómo cerrarlo (S):** Eliminar los console.log de datos sensibles (o envolverlos en __DEV__), usar expo-secure-store y desactivar cleartext en el build de produccion.

### [Medio] H-16 · Carpeta target/ y archivos de trabajo versionados
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/target/classes/application.properties. 63 archivos de target/ (incluido inputFiles.lst con rutas del equipo del autor) en develop pese a .gitignore:35, mas docker-compose.yml.backup, PROMPT_CONTINUACION.md y RESUMEN.md.`
- **Por qué importa:** Ensucia el repositorio y puede contradecir la fuente (target/classes trae un application.properties distinto).
- **Cómo cerrarlo (S):** git rm -r --cached backend-monolith/target y borrar los archivos de trabajo.

### [Medio] H-17 · Las 21 pruebas de la web solo verifican que el componente se crea
- **Repositorio:** `propietario/Frond-end-Web`
- **Evidencia:** `alerta-mujer-web/src/app/features/admin/zone-management/zone-management.spec.ts:16 21 de 28 expect son toBeTruthy sobre el componente; ningun servicio ni guard tiene prueba.`
- **Por qué importa:** Dan falsa sensacion de cobertura; no detectarian ninguno de los desajustes de contrato de H-10.
- **Cómo cerrarlo (M):** Probar servicios con HttpTestingController, guards y el interceptor; borrar los spec de plantilla.

### [Medio] H-18 · Concentracion del trabajo en una persona desde el 25 de agosto
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/Backend-ALERTA-MUJER:README.md:1 Integrante hizo 425 de 559 commits sin fusiones (76 %): 165 de 205 en backend y 140 de 172 en web. Integrante 69 (12 %), Jose 55 (10 %). En historial completo 507 de 919 (55 %).`
- **Por qué importa:** Si el proyecto depende de una persona, el equipo no puede sostenerlo ni todos pueden defenderlo.
- **Cómo cerrarlo (M):** Repartir tareas en el tablero, rotar revisiones de PR y documentar el aporte de cada integrante.

### [Medio] H-19 · La sesion movil no maneja 401 y asume que ids de usuario, perfil y cuenta coinciden
- **Repositorio:** `propietario/AlertaMujer`
- **Evidencia (inferida):** `Frontend-movil/src/services/devices.service.ts:31 getOrCreateDevice filtra por accountId igual a user.id y contactos y alertas se piden con /api/contacts/user/{user.id} y /api/alerts/user/{user.id}; el backend compara contra user_profile.id y account.id (AlertService.java:85). api.ts:60 no trata 401 ni usa el refreshToken. Inferido: depende de que las secuencias coincidan.`
- **Por qué importa:** Si los ids divergen se crean dispositivos duplicados o se niega el acceso; a las 24 h la app queda sin sesion valida sin avisar.
- **Cómo cerrarlo (S):** Usar los endpoints /me, interceptar 401 con refresh y cierre de sesion.

### [Medio] H-21 · Siembra de usuarios de prueba por defecto y credencial de administrador en mensajes de commit
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/src/main/resources/db/changelog/changesets/v1.0.0/05-seed-data.yaml:41 Con LIQUIBASE_CONTEXTS por defecto dev (application.properties:15) se crea «correo» con hash compartido y COMO_INICIAR.md:126 documenta la clave. El commit f473b74 (ramas no integradas) escribe en su mensaje la contrasena de un admin de produccion (««redactado»»).`
- **Por qué importa:** Cuenta administradora conocida si se despliega con el contexto por defecto; la contrasena de produccion quedo en el historial.
- **Cómo cerrarlo (S):** Contexto por defecto prod, retirar credenciales de la documentacion y rotar la del admin de produccion.

### [Medio] H-22 · Sin contrato OpenAPI, sin validacion de entrada ni version de API
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia:** `backend-monolith/pom.xml:92 springdoc sin configuracion y bloqueado por autenticacion; @Valid solo en AuthController.java:34; todas las rutas reales cuelgan de /api/ sin version (PingController.java:8).`
- **Por qué importa:** Web y movil se construyeron contra un contrato implicito y por eso aparecen los desajustes de H-10.
- **Cómo cerrarlo (M):** Publicar OpenAPI, validar DTOs y versionar bajo /api/v1.

### [Medio] H-23 · La documentacion de diseno vive en una rama aparte y no coincide con lo construido
- **Repositorio:** `EQUIPO`
- **Evidencia:** `propietario/AlertaMujer:Frontend-movil/README.md:1 SRS, casos de uso y modelo en la rama documentacion; README del backend:README.md:130 afirma que no hay Liquibase ni pruebas; el modelo documentado usa tablas en espanol distintas de las 28 implementadas.`
- **Por qué importa:** Quien evalua o hereda el proyecto no puede saber que esta hecho ni por que.
- **Cómo cerrarlo (M):** Llevar SRS, MER y matriz de trazabilidad RF a codigo a una carpeta docs/ de la rama principal y actualizarlos con el estado real.

### [Medio] H-24 · Inicio de sesion con Google deshabilitado por configuracion vacia y .env.example engañoso
- **Repositorio:** `propietario/Frond-end-Web`
- **Evidencia:** `alerta-mujer-web/src/environments/environment.prod.ts:10 deja apiKey, authDomain, projectId y appId vacios; alerta-mujer-web/.env.example:1 define NG_APP_API_URL que nadie lee.`
- **Por qué importa:** El boton de Google no puede funcionar en develop y la configuracion documentada no tiene efecto.
- **Cómo cerrarlo (S):** Documentar donde poner la configuracion publica de Firebase y borrar o corregir .env.example.

### [Bajo] H-20 · Historial con node_modules y 152 commits duplicados; binario de Discord en assets
- **Repositorio:** `propietario/AlertaMujer`
- **Evidencia:** `Frontend-movil/assets/imagesAlertaMujer/ScMapa/discord-1.0.137.deb. El commit 8772bc00 subio 37 927 archivos; el paquete git pesa 185 MB; 152 pares de commits repetidos por cherry-pick.`
- **Por qué importa:** Clones lentos y estadisticas de aporte infladas.
- **Cómo cerrarlo (S):** Quitar el .deb; si se limpia el historial, hacerlo coordinado.

### [Bajo] H-25 · Imagen Docker como root y limite de intentos que depende de la IP del proxy
- **Repositorio:** `propietario/Backend-ALERTA-MUJER`
- **Evidencia (inferida):** `backend-monolith/Dockerfile:1. Sin USER ni HEALTHCHECK; backend-monolith/src/main/java/com/alertamujer/identity/infrastructure/config/RateLimitFilter.java:31 usa getRemoteAddr, que en Render sera la IP del proxy (inferido: no se configuro forward-headers).`
- **Por qué importa:** Un contenedor comprometido tiene permisos de root y un solo cliente podria bloquear el login de todos.
- **Cómo cerrarlo (S):** Agregar USER no root, HEALTHCHECK y server.forward-headers-strategy.

### [Bajo] H-26 · Mucho console.log y componentes enormes en la web y el movil
- **Repositorio:** `propietario/Frond-end-Web`
- **Evidencia:** `alerta-mujer-web/src/app/features/admin/report-management/report-management.ts:677 97 console en la web, 120 en el movil; MapaView.tsx del movil tiene 1 088 lineas (Frontend-movil/src/features/mapa/view/MapaView.tsx:1088).`
- **Por qué importa:** Dificulta mantener y revisar el codigo.
- **Cómo cerrarlo (M):** Dividir en componentes y usar un logger con nivel.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 60.4 |
| D2 | Reproducibilidad | 51.7 |
| D3 | Arquitectura y congruencia | 21.4 |
| D4 | Calidad de código | 45.5 |
| D5 | Seguridad | 51.8 |
| D6 | Pruebas | 31.7 |
| D7 | DevOps y despliegue | 36.4 |
| D8 | Datos | 75.0 |
| D9 | API y contratos | 45.0 |
| D10 | Frontend y móvil | 62.5 |
| D11 | IoT y firmware | — |
| D12 | Documentación y trazabilidad | 27.8 |
| D13 | Proceso y colaboración | 41.7 |
| D14 | Extremo a extremo | 0.0 |

## Lo que está bien

- Esquema versionado con Liquibase, restricciones, indices, auditoria y usuario de aplicacion sin DDL (`propietario/Backend-ALERTA-MUJER:backend-monolith/src/main/resources/db/changelog/db.changelog-master.yaml:24 y changesets/v1.1.0/01-indexes.sql, 02-check-constraints.sql, 04-audit.sql, 05-app-role.sql`)
- Contrasenas con BCrypt, manejador global de errores, limite de intentos de login y correccion de propiedad en alertas, ubicacion y evidencias (`backend-monolith/src/main/java/com/alertamujer/identity/application/service/AuthService.java:71; backend-monolith/src/main/java/com/alertamujer/shared/exception/GlobalExceptionHandler.java:17; backend-monolith/src/main/java/com/alertamujer/identity/infrastructure/config/RateLimitFilter.java:44; backend-monolith/src/main/java/com/alertamujer/alerts/application/service/AlertService.java:101-112`)
- Subida de evidencias con validacion, huella SHA-256, almacenamiento fuera de carpeta publica y entrega solo a la duena (`backend-monolith/src/main/java/com/alertamujer/evidence/infrastructure/controller/EvidenceController.java:47-71 y backend-monolith/src/main/resources/db/changelog/changesets/v1.3.0/01-evidence-files.sql:1`)
- App movil con README preciso, URL por entorno en EAS, cola de ubicaciones sin conexion, cuatro idiomas y pruebas unitarias reales (`Frontend-movil/README.md:34; Frontend-movil/eas.json:11; Frontend-movil/src/services/location-reporter.service.ts:43-72; Frontend-movil/src/services/__tests__/location-reporter.service.test.ts:1`)
- Web Angular con guards por rol, interceptor JWT con cierre de sesion al 401 y vencimiento de token (`alerta-mujer-web/src/app/core/guards/role.guard.ts:14-30; alerta-mujer-web/src/app/core/interceptors/jwt.interceptor.ts:19-30; alerta-mujer-web/src/app/core/auth/auth.service.ts:109-128`)
- Render Blueprint con secretos generados por la plataforma y rol de base de datos de menor privilegio (`render.yaml:41-49 y DEPLOY_RENDER.md:15`)
- Trabajo reciente muy activo, con ramas por funcionalidad, PR numerados y commits convencionales en el backend (193 de 206) (`propietario/Backend-ALERTA-MUJER:README.md:1; git log --all en los tres clones`)
- Las ramas sin integrar contienen mucho trabajo de calidad (hexagonal, microservicios, Jenkins, chat de alertas) listo para revisar e integrar (`propietario/Backend-ALERTA-MUJER: ramas feature/microservicios, feature/hexagonal-alerts, ci/jenkins-microservicios`)

## Lo que no se pudo verificar

- Despliegue en linea (T-7.4, T-14.3) — El briefing no declara URL de despliegue; la que aparece en eas.json y environment.prod.ts no se consulto para respetar la regla de red.
- Compilacion y ejecucion de pruebas de los tres proyectos — Prohibido ejecutar npm, mvn o docker; la evaluacion fue por lectura del SHA.
- Ejecuciones de CI de AlertaMujer y Frond-end-Web y existencia de issues, proyectos y colaboradores — La API de GitHub agoto el tiempo por red en varios intentos; colaboradores devolvio 403 (sin permiso de escritura). La ausencia de workflows se comprobo con git ls-tree.
- Revisiones de los PR (T-13.2) — Solo se obtuvo una revision del PR #5 del backend; el resto de consultas fallo por red.
- Bloqueos del guardia de gobernanza — Dos comandos fueron bloqueados por [BOUNDARY-WRITE] como falsos positivos sobre el texto del comando (una sustitucion sed con el SHA y un heredoc con una expresion regular); no se insistio ni se rodeo el guardia: se reescribieron las operaciones (lectura con git show y scripts escritos con la herramienta de archivos en el scratchpad).
- Calidad de codigo (D4) — Muestreo: se leyeron AuthService, AlertService, EvidenceService, GlobalExceptionHandler, controladores, AlertaActiva, api.ts, servicios web y las clases mas grandes por lineas; no se leyo todo el codigo.
- Contenido de las ramas sin integrar — Se midio por git diff --shortstat, git log y listados de archivos; no se evaluo su calidad ni se verifico que funcionen. Las rutas citadas en items y hallazgos son del SHA de develop.
- Si la clave de Firebase del historial sigue vigente y si Render define JWT_SECRET — No se pueden consultar consoles de terceros; se asume lo peor para la clave de Firebase. La clave JWT por defecto solo es explotable si el entorno no define la variable.
- Los ids de user, user_profile y account coinciden — Depende de datos en ejecucion; H-19 queda como inferido.
- Fusion de identidades en aportes — Se fusionaron solo alias con el mismo correo (comparado sin distinguir mayusculas: Integrante 1 y Integrante 1) o el mismo nombre y correo («correo»). Los correos de Integrante 6, Integrante 5, Integrante 7, Integrante 4, Integrante 8, Integrante 9 y Integrante 10 se reportan aparte y no se asociaron a nadie.
- Cumplimiento real de la Ley 1581 de 2012 — Requiere revision legal y de procesos; solo se evaluo lo visible en el codigo.
