# Equipo 6 · Energy Monitor — evaluación extremo a extremo

Corte 2026-10-20 · **Puntaje 76/100 · Juicio A**

## Veredicto

Energy Monitor es un sistema que funciona de punta a punta en lo que se pudo leer: el registro, la vinculación del medidor por Bluetooth, la telemetría MQTT hasta la alerta, el consumo y los umbrales coinciden entre web, backend, base y firmware, y el backend tiene 1124 pruebas, CI con despliegue y cero secretos en el historial. Lo que falta no es el núcleo sino el cierre: la rama main de la documentación está vacía y la de la app móvil solo trae un README, la app móvil no tiene pruebas, CI ni instrucciones, y el inicio de sesión con Google que describe el manual móvil no está implementado. También quedan una tabla de mediciones sin índices, la configuración Bluetooth del medidor sin emparejamiento y documentos que describen un diseño anterior (api_key en claro, 29 tablas). Con dos semanas de trabajo enfocado en integrar a main, probar el móvil y actualizar la documentación el equipo queda en condiciones de cerrar.

## Plan para cerrar las brechas

1. **Fusionar dev en main por PR en docs-final y en Energy-Monitor-Mobile y proteger main** (esfuerzo S) — se cierra cuando: git ls-tree origin/main en ambos repos lista el mismo contenido que dev y main exige PR con CI verde.
2. **Implementar o retirar el acceso con Google y los términos de uso en la app móvil** (esfuerzo M) — se cierra cuando: El botón de Google completa el login contra /auth/google (o desaparece), los términos abren su pantalla, y el manual móvil dice lo mismo que la app.
3. **Crear pruebas y un workflow de CI para la app móvil** (esfuerzo M) — se cierra cuando: npm test pasa en local y en GitHub Actions en cada PR, con pruebas del cliente HTTP, la sesión y las validaciones.
4. **Escribir el README de la app móvil** (esfuerzo S) — se cierra cuando: Una persona nueva levanta la app contra el backend siguiendo solo el README.
5. **Agregar índice compuesto (device_id, date_time) a measurement** (esfuerzo S) — se cierra cuando: Un changeSet de Liquibase crea el índice y EXPLAIN de aggregateHourly lo usa.
6. **Cerrar las exposiciones de seguridad: Swagger en prod, google-services.json, contraseña pública del AP** (esfuerzo S) — se cierra cuando: GET /swagger-ui.html en prod devuelve 401 o 404, la clave de Firebase está restringida y fuera del repo, y config.h no trae contraseña por defecto.
7. **Proteger la sesión y la configuración Bluetooth del medidor** (esfuerzo M) — se cierra cuando: El refresh token ya no está en localStorage ni AsyncStorage y vincular un medidor exige un PIN.
8. **Actualizar la documentación al sistema construido** (esfuerzo S) — se cierra cuando: 05.2 muestra 30 tablas y api_key_hash, los README no mencionan módulos inexistentes y todos los enlaces a archivos resuelven.
9. **Poblar 06-base-datos, 13-articulo-proyecto y 14-anexos o retirarlos** (esfuerzo M) — se cierra cuando: Ninguna carpeta del esqueleto contiene solo .gitkeep.
10. **Healthchecks, imagen web sin root y paginación** (esfuerzo M) — se cierra cuando: docker compose ps muestra backend healthy y los endpoints de listas aceptan page y size.
11. **Limpieza de repositorios: ramas fusionadas, typedefs, lint en CI y rutas del compose** (esfuerzo S) — se cierra cuando: Sin ramas ya fusionadas, npm run lint en la CI web, y el compose usa los nombres reales de los repos.

## Flujos extremo a extremo

| Estado | Flujo | Dónde se rompe |
|---|---|---|
| Completo | Registro, verificación de correo y login | — |
| Completo | Vincular un medidor al hogar (web, Bluetooth y firmware) | — |
| Completo | Telemetría del medidor hasta la alerta y la notificación | — |
| Completo | Consultar el consumo del hogar | — |
| Completo | Definir el límite de consumo y recibir la alerta | — |

## Hallazgos

### [Alto] H-01 · La rama main de la documentación está vacía
- **Repositorio:** `propietario/energy-monitor-docs-final`
- **Evidencia:** `15-enlaces-repositorios-github/enlaces.md`
- **Por qué importa:** El propio enlaces.md declara main como rama de producción de docs-final, pero origin/main tiene 0 archivos (commit initialize repository) y dev lleva 47 commits por delante; quien abra main no ve nada entregable.
- **Cómo cerrarlo (S):** Abrir un PR de dev a main (como ya hicieron en backend y web), fusionarlo y comprobar que main lista las 16 carpetas.

### [Alto] H-02 · La rama main de la app móvil solo contiene un README
- **Repositorio:** `propietario/Energy-Monitor-Mobile`
- **Evidencia:** `README.md`
- **Por qué importa:** origin/main tiene un único archivo y dev 121 commits adelante; la rama que el equipo declara como producción no contiene la aplicación.
- **Cómo cerrarlo (S):** Fusionar dev en main por PR, proteger main y comprobar que main compila con expo.

### [Alto] H-03 · La app móvil no tiene pruebas automatizadas ni CI
- **Repositorio:** `propietario/Energy-Monitor-Mobile`
- **Evidencia:** `package.json`
- **Por qué importa:** Con 327 archivos en src y flujos de sesión, push y Bluetooth, cualquier cambio puede romper el acceso sin que nadie se entere; web y backend sí tienen CI en verde.
- **Cómo cerrarlo (M):** Agregar jest-expo y pruebas del cliente HTTP, la sesión y los esquemas de validación, y un workflow que ejecute lint y pruebas en cada PR.

### [Alto] H-04 · El acceso y registro con Google se documenta y se ofrece en la app móvil, pero no está implementado
- **Repositorio:** `propietario/Energy-Monitor-Mobile`
- **Evidencia:** `src/features/auth/components/LoginForm/LoginForm.tsx:161`
- **Por qué importa:** El manual móvil (Manual_Usuario_EnergyMonitor_Movil.md:178) y RF-SEC-05 prometen Google; el botón solo ejecuta console.log con el texto pendiente de implementar. Lo mismo ocurre con los términos de uso en RegisterForm.tsx:209 y :267.
- **Cómo cerrarlo (M):** Implementar el flujo de Google en móvil contra POST /auth/google o quitar el botón y corregir manual y requisitos; implementar la pantalla de términos.

### [Alto] H-05 · El README de la app móvil no explica qué es ni cómo ejecutarla
- **Repositorio:** `propietario/Energy-Monitor-Mobile`
- **Evidencia:** `README.md`
- **Por qué importa:** 197 bytes con integrantes y ficha; no hay requisitos, variables (EXPO_PUBLIC_API_BASE_URL), emulador contra 10.0.2.2 ni dependencia del backend; solo los comentarios de src/config/env.ts lo cuentan.
- **Cómo cerrarlo (S):** Escribir un README con requisitos, .env, cómo apuntar al backend, cómo correr en emulador o teléfono y cómo generar el APK.

### [Alto] H-06 · La tabla measurement no tiene índices sobre device_id y date_time
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `src/main/resources/db/changelog/measurement/changes/measurement-001-create-measurement.xml`
- **Por qué importa:** Recibe una fila por minuto y dispositivo y todas sus consultas (history, summary, aggregateHourly en MeasurementRepository.java:48) filtran por device_id y rango de fecha; sin índice cada consulta de consumo recorre toda la tabla y degrada con el uso. Solo 5 changelogs crean índices y ninguno es de measurement.
- **Cómo cerrarlo (S):** Agregar un changeSet con índice compuesto (device_id, date_time) y, si se quiere integridad, la FK a device; medir con EXPLAIN.

### [Medio] H-07 · La configuración del medidor por Bluetooth no exige emparejamiento ni cifra el enlace
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `firmware/README.md:382`
- **Por qué importa:** Mientras el modo de configuración está activo (hasta 10 min tras pulsar BOOT) cualquiera cerca puede escribir red, servidor y clave en el módulo; el equipo lo reconoce en sus limitaciones.
- **Cómo cerrarlo (M):** Exigir emparejamiento con PIN (NimBLE security con passkey) o una clave de un solo uso mostrada por la web, y acortar la ventana.

### [Medio] H-08 · El repositorio público trae una contraseña por defecto del punto de acceso de configuración
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `firmware/esp32-pzem-monitor/config.h:69`
- **Por qué importa:** El AP EnergyMonitor-Setup se protege con un valor ««redactado»» público si no se crea secrets.h; cualquier lector del repo conoce la contraseña de todos los módulos que no la cambien.
- **Cómo cerrarlo (S):** Quitar el valor por defecto y generar la contraseña por dispositivo (por ejemplo desde la MAC) o fallar el build si falta secrets.h.

### [Medio] H-09 · Access y refresh token en localStorage (web) y AsyncStorage (móvil)
- **Repositorio:** `propietario/Energy-Monitor-application`
- **Evidencia:** `src/services/auth/session.js:21`
- **Por qué importa:** Cualquier XSS lee un refresh token de 7 días; el backend lo documenta en LoginResponse.java pero no lo mitiga. En móvil, src/services/auth/session.ts guarda lo mismo en AsyncStorage sin cifrar.
- **Cómo cerrarlo (M):** Mover el refresh token a cookie HttpOnly con SameSite y CSRF en el endpoint de refresh (web) y a expo-secure-store (móvil).

### [Medio] H-10 · google-services.json de Firebase versionado con la clave de API
- **Repositorio:** `propietario/Energy-Monitor-Mobile`
- **Evidencia:** `google-services.json`
- **Por qué importa:** El archivo está en el árbol desde el commit c6edbf4 y no figura en .gitignore; la clave (valor ««redactado»») identifica el proyecto Firebase y, sin restricciones de paquete y huella, permite uso abusivo.
- **Cómo cerrarlo (S):** Restringir la clave en Google Cloud por paquete y SHA-1, regenerarla y proveer el archivo por EAS secrets en lugar del repositorio.

### [Medio] H-11 · Swagger UI y /v3/api-docs públicos también en producción
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `src/main/java/com/energymonitor/security/infrastructure/SecurityFilterChainConfiguration.java`
- **Por qué importa:** Están en la lista de rutas sin autenticación y application.yaml no las desactiva por perfil; el despliegue en energy-monitor.tech expondría el contrato completo de la API.
- **Cómo cerrarlo (S):** Poner springdoc.api-docs.enabled y springdoc.swagger-ui.enabled en false para el perfil prod, o protegerlos con rol.

### [Medio] H-12 · Documentos que describen un diseño anterior al código
- **Repositorio:** `propietario/energy-monitor-docs-final`
- **Evidencia:** `05-diseno-software/ES/05.2-modelo-de-datos.md:219`
- **Por qué importa:** 05.2 cuenta 29 tablas y device.api_key VARCHAR(100) (el código tiene 30 y api_key se eliminó por api_key_hash en device-008); firmware/README.md:465 dice api key en claro; el README de la web dice que el módulo recommendation no existe y remite a un devices/README.md ausente (README.md:24 y :51 de la web).
- **Cómo cerrarlo (S):** Revisar 05.2, 06-datos, los dos README y el manual móvil contra las migraciones y los controladores y corregirlos en un solo PR.

### [Medio] H-13 · La web integra funcionalidades por merges locales, sin PR ni revisión; docs y móvil no usan PR
- **Repositorio:** `propietario/Energy-Monitor-application`
- **Evidencia:** `package.json y .github/workflows/ci-cd.yml`
- **Por qué importa:** La web tiene 1 PR en toda su historia (gh api pulls) frente a 35 merges locales; docs y móvil tienen 0 PR; backend sí tiene 53. Sin PR no hay revisión ni check obligatorio de CI.
- **Cómo cerrarlo (S):** Proteger dev y main en todos los repos para exigir PR con un aprobador y el check de CI.

### [Medio] H-14 · Carpetas del esqueleto vacías: base de datos, artículo y anexos
- **Repositorio:** `propietario/energy-monitor-docs-final`
- **Evidencia:** `06-base-datos/script-DB/.gitkeep`
- **Por qué importa:** 06-base-datos (diccionario, modelo ER, scripts), 13-articulo-proyecto y 14-anexos solo contienen .gitkeep; el modelo de datos real está en 05.2 y las migraciones en el backend.
- **Cómo cerrarlo (M):** Poblar 06-base-datos con el diccionario generado desde Liquibase y el MER renderizado, o enlazar a 05.2 y retirar las carpetas vacías.

### [Bajo] H-15 · node_modules versionado en el historial de la web
- **Repositorio:** `propietario/Energy-Monitor-application`
- **Evidencia:** `package.json y .gitignore`
- **Por qué importa:** Los commits b94a0b7, d5d9419 y 68f85ef agregaron hasta 9 599 archivos y 1,6 M de líneas; el SHA actual está limpio pero cada clon descarga ese peso.
- **Cómo cerrarlo (S):** Aceptar la deuda o reescribir el historial con git filter-repo en un repo nuevo; mantener node_modules en .gitignore.

### [Bajo] H-16 · AuthController concentra 14 endpoints, 16 dependencias y 969 líneas
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `src/main/java/com/energymonitor/security/adapter/in/web/AuthController.java:111`
- **Por qué importa:** Mezcla controlador con orquestación asíncrona y límite de intentos; cuesta leerlo y probarlo.
- **Cómo cerrarlo (M):** Partir en AuthController, PasswordController, AccountController y EmailVerificationController, y mover las anotaciones de OpenAPI a interfaces.

### [Bajo] H-17 · El backend no declara HEALTHCHECK y la imagen web corre como root
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `Dockerfile:17`
- **Por qué importa:** Actuator ya expone /actuator/health, pero Docker no lo usa, así que depends_on solo espera el arranque; el Dockerfile de la web (Dockerfile:24) termina en nginx sin USER.
- **Cómo cerrarlo (S):** Agregar HEALTHCHECK al backend (curl a /actuator/health) y healthcheck en infra; usar nginxinc/nginx-unprivileged en la web.

### [Bajo] H-18 · Listas sin paginación y verbos en las rutas
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `src/main/java/com/energymonitor/measurement/adapter/in/web/MeasurementController.java:51`
- **Por qué importa:** GET /measurements y GET /alerts devuelven colecciones completas que crecen con el tiempo; /auth/account/delete y /push/*/remove usan verbos.
- **Cómo cerrarlo (M):** Agregar page y size con tope, y usar DELETE sobre los recursos.

### [Bajo] H-19 · Detalles de mantenibilidad en la web
- **Repositorio:** `propietario/Energy-Monitor-application`
- **Evidencia:** `src/services/home/types.js:47`
- **Por qué importa:** El typedef UpdateThresholdsRequest ya no coincide con la petición real, el cliente HTTP cae a localhost:8080 sin variable (httpClient.js:11), hay mezcla de español e inglés y la CI no ejecuta eslint.
- **Cómo cerrarlo (S):** Corregir el typedef, quitar el fallback o fallar el build sin variable, y agregar npm run lint al workflow.

### [Bajo] H-20 · El compose asume carpetas con nombres distintos a los repositorios
- **Repositorio:** `propietario/energy-monitor-infra`
- **Evidencia:** `docker-compose.yml:56`
- **Por qué importa:** El contexto de build por defecto es ../frontend/energy-monitor-web y ../backend/energy-monitor-backend; el repo real se llama Energy-Monitor-application, así que quien clone con el nombre del repo falla hasta leer README.md:37.
- **Cómo cerrarlo (S):** Usar los nombres reales como valor por defecto o documentar en la primera línea del compose el clonado con destino.

### [Bajo] H-21 · Ramas de funcionalidad ya fusionadas por squash siguen vivas
- **Repositorio:** `propietario/energy-monitor-backend`
- **Evidencia:** `README.md`
- **Por qué importa:** El backend conserva 7 ramas feat/test y la web conserva backend, chore/mock-api-integration y feature/auth-jwt-endpoints (un backend viejo de abril dentro del repo web); parecen trabajo pendiente en las comparaciones.
- **Cómo cerrarlo (S):** Borrar las ramas fusionadas y archivar la rama backend del repo web con una etiqueta.

## Puntaje por dimensión

| Dim. | Nombre | Puntaje |
|---|---|---|
| D1 | Entrega y estructura | 76.2 |
| D2 | Reproducibilidad | 82.9 |
| D3 | Arquitectura y congruencia | 90.9 |
| D4 | Calidad de código | 80.3 |
| D5 | Seguridad | 87.1 |
| D6 | Pruebas | 60.0 |
| D7 | DevOps y despliegue | 58.9 |
| D8 | Datos | 90.0 |
| D9 | API y contratos | 80.0 |
| D10 | Frontend y móvil | 83.3 |
| D11 | IoT y firmware | 65.0 |
| D12 | Documentación y trazabilidad | 55.6 |
| D13 | Proceso y colaboración | 69.4 |
| D14 | Extremo a extremo | 75.0 |

## Lo que está bien

- Autenticación y autorización rigurosas en el servidor (`energy-monitor-backend:src/main/java/com/energymonitor/security/infrastructure/SecurityFilterChainConfiguration.java (JWT RS256, anyRequest().authenticated(), admin con permisos y denyAll); AuthController.java:532 evita la enumeración de cuentas respondiendo 202 siempre.`)
- Cero secretos en árbol e historial de backend, web, infra y docs (`Búsqueda con git log --all -G sobre contraseñas, tokens, JWT, claves privadas y .env/.pem/secrets.h sin hallazgos reales; application.yaml:31,49,69,129,155 solo usa variables de entorno y firmware/esp32-pzem-monitor/secrets.h.example no se versiona.`)
- Arquitectura modular verificada con pruebas (`energy-monitor-backend:src/test/java/com/energymonitor/ModularityTests.java ejecuta ApplicationModules.verify; 7 módulos con package-info.java y los eventos de 05.1 implementados.`)
- Esquema versionado con Liquibase y cambios reversibles (`energy-monitor-backend:src/main/resources/db/changelog/device/changes/device-008-drop-clear-api-key.xml con rollback documentado; la clave del dispositivo pasó a hash SHA-256 con comparación en tiempo constante.`)
- Firmware sin credenciales incrustadas, con TLS y reconexión (`energy-monitor-backend:firmware/esp32-pzem-monitor/config.h:4-13 (identidad y red escritas por Bluetooth en NVS), esp32-pzem-monitor.ino:725 (setCACert) y :1119-1177 (reintentos y backoff).`)
- CI/CD con pruebas, validación de compose, aprobación de producción y smoke test (`energy-monitor-infra:.github/workflows/ci-cd.yml:55-86; backend con MySQL real en el job Tests; web con 5 corridas en éxito el 2026-10-20.`)
- Contratos web, móvil, backend y firmware coherentes en los cinco flujos (`energy-monitor-application:src/services/devices/deviceApi.js:63 contra HomeConsumptionController.java:34; LinkDeviceModal.jsx:217-221 contra esp32-pzem-monitor.ino:920-925.`)
- Contribución repartida y actividad sostenida (`git shortlog --all --no-merges: 297 commits de «correo» (60,5 %) y 194 de Integrante 2; commits en las 9 semanas entre W35 y W43.`)

## Lo que no se pudo verificar

- Respuesta de https://energy-monitor.tech (T-7.4, T-14.3) — El briefing no lista una URL de despliegue autorizada para el GET; la URL aparece solo en el workflow y en enlaces.md del equipo.
- Que los cinco flujos funcionen al ejecutarse — Prohibido ejecutar código de aprendices; los flujos se trazaron por lectura estática de rutas, DTO, migraciones y firmware.
- Compilación de la app móvil y de la web (R-10.1 móvil) — Exige npm install y expo; no se ejecutó. En web solo se apoya en que la CI compila.
- Revisiones reales en los PR del backend y estado de issues y tablero (T-13.2, T-13.7) — Solo se leyó el conteo de PR; no se consultaron reviews, issues ni proyectos.
- Colaboradores y permisos de los repositorios (T-13.6) — Requiere permisos de administración en GitHub.
- Muestreo de calidad de código (D4) — Solo se leyeron a fondo los módulos security y home del backend y los archivos Consumption.jsx y LinkDeviceModal.jsx de la web; el resto se muestreó por búsquedas de patrones.
- Identidad de Integrante 2 — Dos correos distintos («correo» y el noreply de GitHub Integrante 2) aparecen en el backend; se reportan separados porque no comparten correo. Si fueran la misma persona sumaría 194.
- Dependencias vulnerables (R-5.7) — No se ejecutó ningún escáner; se juzgó solo por versiones.
- Historial de commits de la rama main de los repos con red intermitente — Algunas consultas a la API de GitHub fallaron por tiempo de espera y se repitieron; las de docs y móvil devolvieron cero ejecuciones.
