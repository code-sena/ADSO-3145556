**SERVICIO NACIONAL DE APRENDIZAJE – SENA**

**CENTRO DE LA INDUSTRIA, LA EMPRESA Y LOS SERVICIOS (CIES)**

**REGIONAL HUILA**

Programa de Formación: Tecnología en Análisis y Desarrollo de Software (ADSO)

Ficha: 3145556

**MANUAL DE ADMINISTRADOR**

**Proyecto: Monitor de Consumo Eléctrico**

*Administración de cuentas, operación, configuración, respaldos y gestión de seguridad de la plataforma*

| **Dato**             | **Información**                                                                 |
|----------------------|---------------------------------------------------------------------------------|
| Integrantes          | Juan Esteban Cortés Parra, Miguel Ángel García Artunduaga                       |
| Centro de Formación  | Centro de la Industria, la Empresa y los Servicios (CIES) – SENA Regional Huila |
| Programa             | Tecnología en Análisis y Desarrollo de Software (ADSO)                          |
| Ficha                | 3145556                                                                         |
| Tipo de documento    | Manual de administrador                                                         |
| Versión              | 1.5                                                                             |
| Fecha de elaboración | 07 de octubre de 2026 (actualizado el 30 de octubre de 2026)                    |

Año 2026

# 2. Control de versiones del documento

El historial de cambios del presente manual se registra en la siguiente tabla. Cualquier operación administrativa nueva o cambio de configuración soportado por el sistema debe ir acompañado de una actualización del manual.

| **Versión** | **Fecha**  | **Descripción del cambio**                                                                                                                                                                          | **Responsable**      |
|-------------|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------|
| 1.0         | 07/10/2026 | Creación inicial del manual de administrador: entorno, roles y permisos, política de contraseñas, registro de auditoría, respaldos, actualización e incidentes. Citas y referencias en formato APA. | Equipo desarrollador |
| 1.1         | 20/10/2026 | Administración desde la aplicación web (directorio de usuarios, estados, rol de administrador y avisos de administración), designación del primer administrador con `make-admin.sh --prod`, entorno de producción en `https://energy-monitor.tech`, respaldos y actualización en el servidor, incidentes de administración y corrección de consultas SQL. | Equipo desarrollador |
| 1.2         | 24/10/2026 | Operación con la arquitectura de servicios: contenedores del API gateway, de los servicios de seguridad, dispositivos y mediciones, del backend y de RabbitMQ; comprobación de salud dentro de los contenedores, logs por servicio, componentes de `deploy.sh`, secretos nuevos (`RABBITMQ_PASSWORD`, `INTERNAL_API_TOKEN`, `MQTT_DEVICE_SERVICE_PASS`, `MQTT_MEASUREMENT_SERVICE_PASS`) e incidentes nuevos. | Equipo desarrollador |
| 1.3         | 27/10/2026 | Backend retirado: operación de los siete microservicios (seguridad, dispositivos, mediciones, hogares, notificaciones, alertas y recomendaciones) y del gateway; avisos de administración servidos por el servicio de notificaciones; colas y outbox que debe vigilar el administrador; orden de despliegue; `EMAIL_NOT_VERIFIED`; tablas por servicio; comandos y checklist actualizados. | Equipo desarrollador |
| 1.4         | 28/10/2026 | Se documenta la alerta de consumo inusual de un aparato (qué la dispara, cuándo se resuelve y qué ve el usuario), el centro de ayuda de Ajustes con el soporte atendido por los administradores y las fotos de perfil; la versión final se unifica en 1.4. | Equipo desarrollador |
| 1.5         | 30/10/2026 | La introducción aclara que las pantallas de administración y el rol de administrador son exclusivos de la aplicación web. | Equipo desarrollador |

# 3. Tabla de contenido

- [4. Introducción](#4-introducción)
- [5. Objetivo](#5-objetivo)
- [6. Alcance técnico](#6-alcance-técnico)
- [7. Entorno y credenciales de administración](#7-entorno-y-credenciales-de-administración)
- [8. Modelo de seguridad de la plataforma](#8-modelo-de-seguridad-de-la-plataforma)
- [9. Roles y permisos (RBAC)](#9-roles-y-permisos-rbac)
- [10. Administración desde la aplicación web](#10-administración-desde-la-aplicación-web)
- [11. Política de contraseñas y configuración de seguridad](#11-política-de-contraseñas-y-configuración-de-seguridad)
- [12. Sesiones, tokens y revocación](#12-sesiones-tokens-y-revocación)
- [13. Copias de seguridad y restauración](#13-copias-de-seguridad-y-restauración)
- [14. Actualización del software](#14-actualización-del-software)
- [15. Verificación del correo](#15-verificación-del-correo)
- [16. Incidentes frecuentes y cómo diagnosticarlos](#16-incidentes-frecuentes-y-cómo-diagnosticarlos)
- [17. Comandos rápidos de administración](#17-comandos-rápidos-de-administración)
- [18. Checklist diario / semanal del administrador](#18-checklist-diario--semanal-del-administrador)
- [19. Referencias (formato APA, 7.ª edición)](#19-referencias-formato-apa-7ª-edición)

# 4. Introducción

El presente documento es el manual de administrador del software «Monitor de Consumo Eléctrico» (EnergyMonitor). Está orientado a quien tendrá a su cargo la administración de la plataforma: la gestión de las cuentas de usuario y de los administradores, la vigilancia del comportamiento del sistema, los respaldos, la actualización del software y la atención de incidentes (Cortés Parra y García Artunduaga, 2026a).

El sistema tiene dos niveles de administración:

- **Administración funcional, desde la aplicación web.** Una cuenta con el rol global de administrador (`ADMIN`) dispone de una pantalla propia en `https://energy-monitor.tech` para consultar las cuentas, cambiar su estado, conceder o revocar el rol de administrador y recibir avisos de administración (sección 10).
- **Administración técnica, desde el servidor.** La designación del primer administrador, la política de contraseñas, los parámetros de seguridad, los respaldos y la actualización se hacen en el servidor, con los scripts del repositorio de infraestructura y consultas a la base de datos MySQL 8.4 (Spring, s. f.; Docker Inc., s. f.).

La administración funcional y el rol de administrador son exclusivos de la aplicación web: la aplicación móvil no incluye pantallas de administración ni comprueba este rol.

Las referencias académicas y técnicas citadas en el cuerpo del documento se formatearon según la 7.ª edición del manual APA (American Psychological Association \[APA\], 2020).

# 5. Objetivo

Dotar al equipo que administra el sistema de una guía clara, operativa y verificable para realizar, sin riesgos, las tareas propias de la administración de la plataforma.

- Explicar cómo se designa el primer administrador y cómo se administran las cuentas desde la aplicación web.
- Describir el entorno de administración y dónde se encuentra cada credencial o archivo de configuración.
- Documentar el modelo de roles y permisos y sus restricciones.
- Explicar cómo revisar y rotar las claves de cifrado (pepper de recuperación y par RSA).
- Registrar el procedimiento para ejecutar copias de seguridad y restaurarlas.
- Definir el flujo para actualizar el software sin perder datos y para diagnosticar incidentes frecuentes.

# 6. Alcance técnico

Este manual cubre la administración en dos entornos:

- **Producción:** VPS con Ubuntu 26.04 LTS, publicado en `https://energy-monitor.tech`. Toda la plataforma (MySQL 8.4, RabbitMQ 4.1, API gateway, los siete microservicios, broker MQTT y frontend) corre con Docker Compose desde `/opt/energy-monitor/infra` (`docker-compose.prod.yml`), detrás de Nginx con certificados Let's Encrypt.
- **Desarrollo local:** la pila completa del repositorio `energy-monitor-infra` (`docker-compose.yml`), o MySQL, RabbitMQ y el broker en Docker con los servicios ejecutados desde el IDE o con `./mvnw spring-boot:run`.

Componentes administrados:

- API gateway (`en-monitor-api-gateway`, Spring Cloud Gateway, puerto 8080): única entrada de la API.
- Servicio de seguridad (`en-monitor-security-service`, puerto 8082): cuentas, sesiones, JWT RS256, RBAC y administración de usuarios.
- Servicio de dispositivos (`en-monitor-device-service`, puerto 8084) y servicio de mediciones (`en-monitor-measurement-service`, puerto 8085).
- Servicio de hogares (`en-monitor-home-service`, 8086), de notificaciones (`en-monitor-notification-service`, 8087; correo, push, actividad y avisos de administración), de alertas (`en-monitor-alert-service`, 8088) y de recomendaciones (`en-monitor-recommendation-service`, 8089).
- El antiguo backend (`energy-monitor-backend`) está retirado: ya no corre y `deploy.sh` no lo acepta. Solo conserva el firmware del dispositivo.
- La base de datos `energy_monitor`, compartida por todos los servicios (cada uno con sus tablas), y la cola RabbitMQ con la que se avisan entre ellos.
- Aplicación web, incluida su pantalla de administración.
- Correo de verificación, recuperación, borrado de cuenta, alertas, recomendaciones y actividad vía SMTP (Gmail en producción, Mailpit en local), enviado solo por el servicio de notificaciones.

No describe la instalación del servidor desde cero ni la configuración del despliegue continuo: ambos están en el *Manual de implementación y despliegue* (Cortés Parra y García Artunduaga, 2026b). La puesta a punto del proyecto desde el código está en el *Manual técnico*.

# 7. Entorno y credenciales de administración

## 7.1 Componentes que el administrador debe vigilar

En el servidor de producción se usa el alias `emc` (definido en el manual de implementación y despliegue), equivalente a `docker compose -f /opt/energy-monitor/infra/docker-compose.prod.yml --env-file /opt/energy-monitor/infra/.env.prod --project-directory /opt/energy-monitor/infra`.

| **Componente**        | **Producción**                                                | **Evidencia/verificación**                                              |
|-----------------------|---------------------------------------------------------------|-------------------------------------------------------------------------|
| MySQL 8.4             | Servicio `mysql` (`emc ps`)                                   | Estado `Up (healthy)`                                                   |
| RabbitMQ 4.1          | Servicio `rabbitmq`; consola en `127.0.0.1:15672` (túnel SSH) | Estado `Up (healthy)`; colas `.dlq` vacías                              |
| API gateway           | Servicio `gateway` (puerto 8080 solo en la red Docker)        | `emc exec gateway curl -fsS http://localhost:8080/actuator/health` → `{"status":"UP"...}` |
| Servicio de seguridad | Servicio `security` (8082, red Docker)                        | `emc ps` → `Up (healthy)`                                               |
| Servicio de dispositivos | Servicio `device` (8084, red Docker)                       | `emc ps` → `Up (healthy)`                                               |
| Servicio de mediciones | Servicio `measurement` (8085, red Docker)                    | `emc ps` → `Up (healthy)`                                               |
| Servicio de hogares   | Servicio `home` (8086, red Docker)                            | `emc ps` → `Up (healthy)`                                               |
| Servicio de notificaciones | Servicio `notification` (8087, red Docker)               | `emc ps` → `Up (healthy)`; colas `notification-service.*` sin acumular |
| Servicio de alertas   | Servicio `alert` (8088, red Docker)                           | `emc ps` → `Up (healthy)`; colas `alert-service.*` sin acumular         |
| Servicio de recomendaciones | Servicio `recommendation` (8089, red Docker)            | `emc ps` → `Up (healthy)`                                               |
| Broker MQTT           | Servicio `mosquitto`, puerto público 8883 (TLS)               | `emc logs --tail 50 mosquitto` sin reinicios; conectados `em-device-service` y `em-measurement-service` |
| Web                   | Servicio `frontend` detrás de Nginx                           | `https://energy-monitor.tech` abre la página de inicio                  |
| Correo                | Cuenta SMTP configurada en `.env.prod`                        | Tabla `notification` sin envíos `FAILED` (sección 15)                   |

Ningún servicio Java publica puertos en el host: su salud la mide el *healthcheck* de cada contenedor (`/actuator/health`), que `emc ps` muestra como `(healthy)`. En desarrollo local, los mismos servicios se consultan con `docker compose ps` desde `energy-monitor-infra`; Mailpit muestra los correos en `http://localhost:8025`.

## 7.2 Dónde están los secretos de la plataforma

Ningún secreto se guarda en el código ni en Git. El administrador debe ubicarlos únicamente en estos lugares:

- **Producción:**
  - `/opt/energy-monitor/infra/.env.prod` (permisos 600): `MYSQL_PASSWORD`, `MYSQL_ROOT_PASSWORD`, `SECURITY_RESET_PEPPER`, `RABBITMQ_PASSWORD`, `INTERNAL_API_TOKEN`, `MQTT_DEVICE_SERVICE_PASS`, `MQTT_MEASUREMENT_SERVICE_PASS`, credenciales `MAIL_*`, `GOOGLE_CLIENT_SECRET`, claves VAPID y demás secretos. `MQTT_BACKEND_USER/PASS`, `BACKEND_URL` y `BACKEND_INTERNAL_URL` ya no se usan: el backend está retirado.
  - `/opt/energy-monitor/keys/`: par `private.pem` / `public.pem` de los JWT (dueño uid 10001). Solo el servicio de seguridad lee `private.pem`; el gateway y los demás servicios reciben solo `public.pem`.
  - `/opt/energy-monitor/mqtt-certs/`: certificado del broker, copiado desde Let's Encrypt.
- **Desarrollo local:** `energy-monitor-infra/.env` y `energy-monitor-infra/keys/`; si los servicios se ejecutan desde el IDE, también el `.env` y el `src/main/resources/application-local.yaml` de cada uno (`backend/en-monitor-security-service`, `backend/en-monitor-device-service`, `backend/en-monitor-measurement-service`, `backend/en-monitor-home-service`, `backend/en-monitor-notification-service`, `backend/en-monitor-alert-service`, `backend/en-monitor-recommendation-service` y el `.env` de `backend/en-monitor-api-gateway`).

> *Regla de oro: ningún secreto va a commits, a anexos del manual, a chats ni a directorios compartidos. Los archivos `.env`, `.env.prod`, `application-local.yaml`, `*.pem` y los certificados están en `.gitignore`.*

# 8. Modelo de seguridad de la plataforma

## 8.1 Tablas que gobiernan la seguridad

Todas las tablas están en la misma base `energy_monitor`. Las de seguridad pertenecen al servicio de seguridad (`en-monitor-security-service`); `notification` pertenece al servicio de notificaciones. El detalle de todas las tablas está en el [diccionario de datos](../../04-software-design/es/data-dictionary.md).

| **Tabla**                                                         | **Propósito**                                                                                     |
|-------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| user, person, user_configuration, user_session                    | Identidad, datos básicos, configuración propia y sesiones de cada cuenta. `user.status` vale `ACTIVE`, `INACTIVE` o `BLOCKED`. |
| password_policy                                                   | Política activa de contraseñas (por defecto min 8, max 64, 90 días, mayúsculas/números/símbolos). |
| password_reset_token                                              | Tokens (hash con pepper) de recuperación de contraseña.                                           |
| security_configuration                                            | Pares nombre/valor de parámetros de seguridad que administra el servicio de seguridad.            |
| login_error_log                                                   | Intentos fallidos de acceso con tipo, descripción, IP y fecha.                                    |
| system_role, permission, user_system_role, system_role_permission | Modelo RBAC de los roles globales (sección 9).                                                   |
| audit_log                                                         | Auditoría de CREATE/UPDATE/DELETE/LOGIN/LOGIN_FAILED/LOGOUT con IP y aplicación, incluidos los cambios de estado y de rol hechos por los administradores. |
| notification                                                      | Bitácora de entregas por correo y push (estado SENT/FAILED y motivo), notificaciones de actividad y avisos de administración (canal `IN_APP`, fuente `ADMINISTRATION`). |
| pending_registration                                              | Registros que esperan la verificación del correo; vencen a las 24 horas y un proceso horario los borra. |
| security_outbox                                                   | Eventos y pedidos de correo pendientes de publicar en RabbitMQ (`last_error` explica un atasco). |

## 8.2 Auditoría (`audit_log`)

Toda operación relevante sobre cuentas, sesiones, credenciales y roles deja rastro en `audit_log`. El administrador puede consultar las últimas acciones así:

```sql
SELECT id_audit_log, user_id, action, description, ip_address, created_at
FROM audit_log
ORDER BY created_at DESC
LIMIT 50;
```

El campo `action` admite CREATE, UPDATE, DELETE, LOGIN, LOGIN_FAILED y LOGOUT (MySQL, 2026).

## 8.3 Intentos de acceso fallidos (`login_error_log`)

Los inicios de sesión denegados quedan registrados con motivo y dirección de origen. Sirve para detectar fuerza bruta:

```sql
SELECT error_type, ip_address, COUNT(*) AS intentos, MAX(created_at) AS ultimo
FROM login_error_log
GROUP BY error_type, ip_address
ORDER BY intentos DESC
LIMIT 20;
```

`error_type` puede ser `INVALID_PASSWORD`, `USER_NOT_FOUND`, `ACCOUNT_BLOCKED`, `ACCOUNT_INACTIVE` o `EMAIL_NOT_VERIFIED` (contraseña correcta, pero el correo aún no se verificó). Los dos últimos aparecen cuando una cuenta bloqueada o desactivada por un administrador intenta iniciar sesión.

# 9. Roles y permisos (RBAC)

## 9.1 Modelo

El sistema distingue dos clases de rol que no se mezclan:

- **Roles de hogar** (`OWNER` / `MEMBER`): valen dentro de un solo hogar y los gestiona el propietario desde la aplicación.
- **Roles globales** (RBAC): valen para la plataforma completa. Hoy existe uno, `ADMIN`.

```
system_role (id_system_role, name, description, enabled)
permission (id_permission, code, name, description)
system_role_permission (system_role_id, permission_id)
user_system_role (user_id, system_role_id, assigned_at, deleted_at)
```

El changeset de Liquibase `security-025` crea el rol `ADMIN` con sus tres permisos:

| **Permiso**   | **Qué permite**                                                       |
|---------------|-----------------------------------------------------------------------|
| `USER.READ`   | Consultar el directorio de cuentas, los roles y los avisos de administración propios |
| `USER.MANAGE` | Cambiar el estado de una cuenta a activa, inactiva o bloqueada        |
| `ROLE.MANAGE` | Conceder o revocar roles globales                                     |

Los permisos se leen de la base de datos **en cada solicitud** y no viajan dentro del token de sesión. Por eso conceder o revocar el rol, o bloquear una cuenta, surte efecto en la siguiente solicitud al servicio de seguridad. Los avisos de administración los sirve el servicio de notificaciones, que pregunta el permiso `USER.READ` al servicio de seguridad por su API interna. Revocar el rol desde la web no borra la fila de `user_system_role`: le pone fecha en `deleted_at`.

> *El rol `ADMIN` no da acceso a ningún hogar: un administrador que no es miembro de un hogar no puede ver sus datos.*

## 9.2 Designar el primer administrador

Ninguna cuenta recibe el rol al instalar el sistema. El primer administrador se designa una sola vez desde el servidor; los siguientes se designan desde la aplicación web (sección 10.4).

1. Registrar la cuenta en `https://energy-monitor.tech` (registro con correo o «Iniciar sesión con Google»).
2. En el servidor, ejecutar:

   ```bash
   cd /opt/energy-monitor/infra
   ./make-admin.sh --prod correo-del-admin@ejemplo.com
   ```

3. Cerrar sesión en la web y volver a entrar: el menú muestra **Administración**.

El script normaliza el correo (sin espacios y en minúsculas), rechaza direcciones no válidas, falla si la cuenta no existe y es idempotente: si la cuenta ya es administradora no duplica nada, y si el rol se le había revocado lo reactiva. Al terminar muestra los roles activos de la cuenta. En la pila local se usa sin `--prod` (`./make-admin.sh correo@ejemplo.com`); con `--prod` lee `.env.prod` y `docker-compose.prod.yml`.

# 10. Administración desde la aplicación web

## 10.1 Acceso y menú

Una cuenta con el rol `ADMIN` que inicia sesión en `https://energy-monitor.tech` entra directamente a **Administración** (`/admin/users`). Su menú lateral solo muestra tres opciones:

- **Administración:** directorio de usuarios.
- **Notificaciones:** avisos de administración.
- **Ajustes:** idioma, tema y centro de ayuda. Las preferencias de notificación de los hogares no aparecen, porque una cuenta administradora no tiene hogares.

Las pantallas de hogares (panel, detalle del hogar y favoritos) no están disponibles para una cuenta administradora: si intenta abrirlas, la aplicación la devuelve a la administración. Una cuenta sin el permiso `USER.READ` que intenta abrir la administración vuelve a su panel de hogares, y la API rechaza sus solicitudes de administración con `403`. La app móvil no tiene pantallas de administración: la administración solo se hace desde la web.

## 10.2 Directorio de usuarios

La pantalla **Usuarios de la plataforma** muestra las cuentas activas, de la más reciente a la más antigua, en páginas de 20.

| **Columna**       | **Contenido**                                                    |
|-------------------|------------------------------------------------------------------|
| Usuario           | Nombre, apellido y correo. La cuenta propia se marca con «Tú».  |
| Estado            | Activa, Inactiva o Bloqueada                                     |
| Roles globales    | Roles que tiene la cuenta, o «Sin roles»                         |
| Registro          | Fecha de creación de la cuenta                                   |
| Último acceso     | Último inicio de sesión, o «Nunca»                               |

- **Buscar:** escribir parte del nombre, del apellido o del correo y pulsar **Buscar**.
- **Paginar:** con más de 20 resultados aparecen los botones de página anterior y siguiente.

## 10.3 Cambiar el estado de una cuenta

Requiere el permiso `USER.MANAGE`.

1. En la fila de la cuenta, elegir el nuevo estado en el selector **Estado**.
2. Si el estado nuevo no es **Activa**, la aplicación pide confirmación: la cuenta no podrá iniciar sesión y sus sesiones abiertas se cerrarán.
3. Pulsar **Confirmar**.

| **Estado** | **Efecto**                                                                 |
|------------|----------------------------------------------------------------------------|
| Activa     | La cuenta puede iniciar sesión con normalidad.                             |
| Inactiva   | La cuenta no puede iniciar sesión (`ACCOUNT_INACTIVE`); se cierran sus sesiones. |
| Bloqueada  | La cuenta no puede iniciar sesión (`ACCOUNT_BLOCKED`); se cierran sus sesiones. |

Las sesiones se revocan con el motivo `ADMINISTRATIVE`. El token de acceso que la cuenta ya tenía puede seguir vigente hasta 15 minutos, pero no puede renovarse.

## 10.4 Conceder o revocar el rol de administrador

Requiere el permiso `ROLE.MANAGE`.

- **Hacer administrador:** pulsar el botón del escudo en la fila de la cuenta. El rol se aplica de inmediato; la cuenta debe cerrar sesión y volver a entrar para ver la administración.
- **Quitar el rol:** pulsar el mismo botón en una cuenta administradora y confirmar. La cuenta deja de tener acceso a la administración.

Repetir una concesión o una revocación no cambia nada. Cada cambio queda en `audit_log` con la dirección de origen.

## 10.5 Avisos de administración

La opción **Notificaciones** muestra los avisos de administración de la cuenta: las 50 más recientes. El menú y el título de la pestaña indican cuántos avisos están sin leer.

| **Aviso**                 | **Cuándo se genera**                                                    |
|---------------------------|-------------------------------------------------------------------------|
| Nueva cuenta registrada   | Alguien crea una cuenta, con correo o con su primer acceso con Google  |
| Cuenta activada / desactivada / bloqueada | Otro administrador cambia el estado de una cuenta         |
| Nuevo administrador / Administrador retirado | Otro administrador concede o revoca el rol `ADMIN`     |

- Quien hace un cambio no recibe aviso de su propio cambio, y una acción que no cambia nada no genera avisos.
- Cada aviso se puede **marcar como leído**; también se pueden marcar todos a la vez y **eliminar** los que ya no se necesiten.
- Cada administrador solo ve y modifica sus propios avisos.
- Los avisos de administración solo se muestran dentro de la aplicación web: no se envían por correo ni por notificación push.

## 10.6 Restricciones

| **Restricción**                                              | **Comportamiento**                                                       |
|--------------------------------------------------------------|--------------------------------------------------------------------------|
| Un administrador no puede administrar su propia cuenta       | En la web no aparecen las acciones en la fila propia; el servicio de seguridad responde `409` si se intenta |
| Una acción sin el permiso correspondiente                    | La web oculta la acción y la API responde `403`                          |
| Sin sesión iniciada                                          | El API gateway responde `401`                                            |
| Cualquier ruta `/api/v1/admin/**` sin permiso asignado       | Se rechaza siempre (`403`)                                               |
| El rol `ADMIN` y los hogares                                  | El rol no da acceso a ningún hogar                                       |

## 10.7 Funciones pendientes

Las siguientes funciones no existen en esta versión y quedan planificadas:

- Gestión de las solicitudes de soporte desde **Ajustes**.
- Edición de la documentación de ayuda desde **Ajustes**.
- Entrega de los avisos de administración por correo o por notificación push.
- Otros roles globales además de `ADMIN`.

## 10.8 Soporte y alerta de consumo inusual

**Soporte técnico.** En Ajustes \> Ayuda, el usuario encuentra la tarjeta **Soporte técnico**: elige a uno de los administradores disponibles y **Crear ticket** abre su correo con el asunto ya escrito. Por eso el soporte lo atienden los administradores, que responden por correo, y la tarjeta no aparece para ellos. Mantener actualizados los correos de las cuentas administradoras (sección 10.2) es lo que garantiza que haya a quién escribir.

**Alerta de consumo inusual.** El servicio de alertas avisa a los miembros del hogar cuando un aparato consume al menos 200 W y al menos 2,5 veces su promedio por hora de los últimos 7 días, siempre que tenga 24 horas de historial y un promedio de al menos 5 W. La alerta se resuelve sola cuando el consumo se normaliza y llega por correo y push según las preferencias de cada persona. Si un usuario reporta que no recibe el aviso, revisar en este orden: que el módulo tenga historial suficiente, que el hogar tenga miembros con las notificaciones activas y que el servicio de alertas esté operando (sección 16). Los umbrales son fijos en el código y no se pueden cambiar desde la aplicación.

# 11. Política de contraseñas y configuración de seguridad

## 11.1 Política activa (`password_policy`)

La aplicación toma la política de la **primera fila** sin `deleted_at`. Por seguridad operativa, el esquema siembra una única fila por defecto (id `pol0000001`): mín. 8, máx. 64, expiración 90 días, mayúsculas, números y símbolos obligatorios. Para cambiarla:

```sql
UPDATE password_policy SET min_length = 10, expiration_days = 60 WHERE id_password_policy = 'pol0000001';
```

No insertes una segunda fila activa: la selección no tiene `ORDER BY` y el comportamiento sería no determinista.

## 11.2 Parámetros dinámicos (`security_configuration`)

La tabla guarda pares `config_name / config_value` que el administrador puede ajustar sin recompilar. Edita valores existentes:

```sql
SELECT id_security_configuration, config_name, config_value, description FROM security_configuration;
UPDATE security_configuration SET config_value = 'valor' WHERE config_name = 'nombre_del_parametro';
```

> *Cambiar valores que el código usa para firmar/validar tokens puede invalidar sesiones: coordina con el equipo desarrollador antes de tocar esos parámetros.*

# 12. Sesiones, tokens y revocación

Toda revocación de sesión o de refresh token registra su motivo: `ADMINISTRATIVE`, `PASSWORD_CHANGED` o `REFRESH_TOKEN_REUSE` (MySQL, 2026). La forma recomendada de cerrar las sesiones de una cuenta es cambiar su estado desde la web (sección 10.3), que las revoca con el motivo `ADMINISTRATIVE` y deja el cambio auditado. Si hace falta hacerlo por SQL durante un incidente:

```sql
UPDATE user_session SET revoked_at = NOW(), revoked_reason = 'ADMINISTRATIVE' WHERE user_id = 'usr0000001' AND revoked_at IS NULL;
```

Tras una revocación, el siguiente `POST /api/v1/auth/refresh` del cliente responde `401` y obliga a autenticarse de nuevo.

# 13. Copias de seguridad y restauración

## 13.1 Respaldo en producción

El script `infra/deploy/backup-mysql.sh` genera `energy_monitor-AAAAMMDD-HHMMSS.sql.gz` en `/opt/energy-monitor/backups` (permisos 600) y borra los de más de 14 días. Se ejecuta automáticamente antes de cada despliegue y debe programarse a diario:

```bash
sudo /opt/energy-monitor/infra/deploy/backup-mysql.sh                 # a mano
# cron (sudo crontab -e):
0 3 * * * /opt/energy-monitor/infra/deploy/backup-mysql.sh >> /var/log/energy-monitor-backup.log 2>&1
```

## 13.2 Restaurar

Restaurar **sobrescribe** la base de datos, que comparten todos los servicios. Detener antes los servicios que escriben en ella (`emc stop gateway security device measurement home notification alert recommendation`) y, desde `/opt/energy-monitor/infra`:

```bash
gunzip -c /opt/energy-monitor/backups/<archivo>.sql.gz | \
  docker compose -f docker-compose.prod.yml --env-file .env.prod \
  exec -T -e MYSQL_PWD="<contraseña de root>" mysql mysql -uroot energy_monitor
emc start security device measurement home notification alert recommendation gateway
```

## 13.3 Volumen de datos

Los datos persistentes de MySQL viven en el volumen Docker `mysql-data`, y los mensajes pendientes de RabbitMQ en `rabbitmq-data`. Ejecutar `docker compose down -v` borra los volúmenes y, con ellos, toda la base de datos; no lo hagas nunca en producción (Docker Inc., s. f.).

> *Los respaldos quedan en el mismo servidor que la base de datos: copia periódicamente los archivos a un almacenamiento externo con acceso restringido.*

# 14. Actualización del software

En producción, el software se actualiza con GitHub Actions: cada cambio fusionado en `main` se despliega tras la aprobación del entorno `production`, con respaldo previo de la base de datos, prueba de salud y reversión automática si algo falla. Cada repositorio despliega su propio componente, y la prueba de salud espera a que todos los servicios Java estén `healthy`, la web responda y `/api/v1/homes` responda 401 por el gateway. Cuando un cambio agrega un evento nuevo, se despliega primero el servicio que lo consume y después el que lo produce (por ejemplo, `notification` antes que `security` y `home`): un outbox se detiene ante un mensaje que ninguna cola recibe. Liquibase aplica los changesets nuevos de cada servicio al arrancarlo. El procedimiento completo está en el manual de implementación y despliegue (Cortés Parra y García Artunduaga, 2026b).

Si GitHub no está disponible, el mismo procedimiento se ejecuta a mano en el servidor:

```bash
sudo /opt/energy-monitor/infra/deploy/deploy.sh security     # o device, measurement, home, notification, alert, recommendation, gateway, frontend, infra
```

> *Cambiar `SECURITY_RESET_PEPPER` invalida todos los códigos de recuperación vigentes. Rotarlo es una decisión de seguridad, no de costumbre.*

# 15. Verificación del correo

Solo el servicio de notificaciones envía correo: los de verificación, recuperación y borrado de cuenta los pide el servicio de seguridad por RabbitMQ (`mail.email-verification`, `mail.password-reset`, `mail.account-deletion`), y los de alertas, recomendaciones y actividad salen de los eventos `alert.raised`, `recommendation.created` y de actividad. Cada intento de envío por correo o push queda en la tabla `notification` con estado (`delivery_status`) `SENT` o `FAILED` y, si falla, el motivo (`failure_reason`). Para auditar si el correo falla:

```sql
SELECT id_notification, channel, delivery_status, failure_reason, created_at
FROM notification
WHERE delivery_status = 'FAILED'
ORDER BY created_at DESC
LIMIT 20;
```

Si casi todos fallan, suele deberse a credenciales SMTP incorrectas, a que Gmail exige una «contraseña de aplicación» en lugar de la contraseña de la cuenta, o a que `MAIL_FROM` no coincide con `MAIL_USERNAME` (Google, s. f.). Si un correo de verificación o recuperación ni siquiera aparece en `notification`, revisa en la consola de RabbitMQ que la cola `notification-service.notification.mail` no acumule mensajes ni los desvíe a su `.dlq`, y la columna `last_error` de `security_outbox`. En desarrollo, revisa que Mailpit esté activo (`http://localhost:8025`).

# 16. Incidentes frecuentes y cómo diagnosticarlos

| **Síntoma**                                                                        | **Posible causa**                                               | **Acción**                                                                                                    |
|------------------------------------------------------------------------------------|-----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| Tras `make-admin.sh` el menú no muestra **Administración**                        | La sesión se inició antes de conceder el rol                    | Cerrar sesión y volver a entrar.                                                                              |
| `make-admin.sh` responde «No encontré MYSQL_ROOT_PASSWORD»                         | Se ejecutó en el servidor sin `--prod`                          | Ejecutar `./make-admin.sh --prod correo`.                                                                     |
| `make-admin.sh` responde «No existe una cuenta activa con el correo»               | La cuenta aún no se registró o el correo tiene otra escritura   | Registrar la cuenta en la web y repetir.                                                                      |
| La web rechaza un cambio en la propia cuenta (`409`)                               | Un administrador no puede administrarse a sí mismo              | Pedir el cambio a otro administrador.                                                                         |
| La web indica falta de permiso (`403`) en la administración                        | La cuenta no tiene el permiso que exige la acción               | Revisar sus roles (sección 17) o conceder el rol desde otra cuenta administradora.                            |
| Un usuario no puede iniciar sesión y `login_error_log` muestra `ACCOUNT_BLOCKED` o `ACCOUNT_INACTIVE` | Un administrador cambió su estado            | Revisar `audit_log`; reactivar la cuenta desde la web si corresponde.                                        |
| El servicio de seguridad no arranca y nombra claves o pepper                       | Faltan las claves JWT o `SECURITY_RESET_PEPPER`.                | Revisar `/opt/energy-monitor/keys` y `.env.prod` (o `.env` en local).                                         |
| La web muestra errores y la API responde `503`                                     | El gateway no alcanza al servicio de esa ruta                   | `emc ps` y `emc logs --tail 100 security device measurement home notification alert recommendation`.          |
| La API responde `504`                                                              | El servicio de esa ruta tardó más de 30 s                       | Revisar los logs de ese servicio y de MySQL.                                                                  |
| Un servicio queda `unhealthy` con RabbitMQ arriba                                  | `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` no coinciden con el usuario que RabbitMQ creó la primera vez | Cambiar la clave dentro de RabbitMQ (`rabbitmqctl change_password`) o corregir `.env.prod`.                  |
| Un medidor o un servicio es rechazado por el broker (`rc=5`)                       | El servicio de dispositivos no reconoce sus credenciales        | Comprobar que `MQTT_DEVICE_SERVICE_*` y `MQTT_MEASUREMENT_SERVICE_*` coinciden en `.env.prod`; para un medidor, volver a vincularlo. |
| No llegan correos de recuperación                                                  | SMTP mal configurado o Mailpit apagado.                         | Revisar `notification` (sección 15) y `MAIL_*`.                                                               |
| No se crean alertas o recomendaciones, o no llega la actividad                     | Servicio detenido, interruptor en `false` o outbox atascado     | `emc ps`; revisar que `.env.prod` no tenga `ALERT_CONSUMERS_ENABLED=false` ni `RECOMMENDATION_SCHEDULER_ENABLED=false`; consultar `last_error` de los `*_outbox`. |
| Colas `energy-monitor-backend.*` con mensajes en RabbitMQ                          | Restos del backend retirado, sin consumidor                     | Borrarlas con su `.dlq` (`emc exec rabbitmq rabbitmqctl delete_queue <cola>`).                                 |
| Errores frecuentes `INVALID_PASSWORD`, `USER_NOT_FOUND` en `login_error_log`       | Intentos de acceso inválidos o fuerza bruta.                    | Revisar IPs repetidas y bloquear en el firewall si hace falta.                                                |
| `docker compose ps` no muestra los servicios en el servidor                        | El comando se ejecutó sin el archivo de producción              | Usar el alias `emc ps`.                                                                                       |

# 17. Comandos rápidos de administración

| **Tarea**                                   | **Comando**                                                                                                                                                  |
|---------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Designar el primer administrador            | `./make-admin.sh --prod correo@ejemplo.com` (desde `/opt/energy-monitor/infra`)                                                                              |
| Ver estado de los servicios                 | `emc ps` (producción) o `docker compose ps` (local, desde `energy-monitor-infra`)                                                                           |
| Estado de salud de los servicios            | `emc ps` (el gateway y los siete servicios en `(healthy)`) o `emc exec gateway curl -fsS http://localhost:8080/actuator/health`                                      |
| Logs de un servicio                         | `emc logs --tail 100 gateway` (o `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation`, `mosquitto`, `rabbitmq`)                                                    |
| Colas de RabbitMQ                           | `emc exec rabbitmq rabbitmqctl list_queues name messages` (las `.dlq` deben estar en 0)                                                                      |
| Mensajes pendientes en los outbox           | `SELECT message_type, attempts, last_error FROM home_outbox WHERE published_at IS NULL AND discarded_at IS NULL;` (igual en `security_outbox`, `device_outbox`, `measurement_outbox`, `alert_outbox` y `recommendation_outbox`) |
| Ver últimas 10 acciones auditadas           | `SELECT * FROM audit_log ORDER BY created_at DESC LIMIT 10;`                                                                                                 |
| Ver últimos errores de login                | `SELECT * FROM login_error_log ORDER BY created_at DESC LIMIT 20;`                                                                                           |
| Listar los administradores activos          | `SELECT u.email, r.name FROM user u JOIN user_system_role usr ON usr.user_id = u.id_user JOIN system_role r ON r.id_system_role = usr.system_role_id WHERE usr.deleted_at IS NULL AND u.deleted_at IS NULL;` |
| Forzar cierre de sesiones de un usuario     | Cambiar su estado desde la web, o `UPDATE user_session …` (sección 12)                                                                                       |
| Respaldo de la base de datos                | `sudo /opt/energy-monitor/infra/deploy/backup-mysql.sh` (sección 13.1)                                                                                       |
| Desplegar a mano                            | `sudo /opt/energy-monitor/infra/deploy/deploy.sh <componente>` con `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation`, `gateway`, `frontend` o `infra` (sección 14) |

# 18. Checklist diario / semanal del administrador

| **Revisión**                                             | **Frecuencia** | **Estado esperado**                                   |
|----------------------------------------------------------|----------------|-------------------------------------------------------|
| Avisos de administración sin leer                        | Diaria         | Cuentas nuevas y cambios de otros administradores revisados |
| Servicios sanos (`emc ps`) y `actuator/health`           | Diaria         | Todos `Up`; gateway, security, device, measurement, home, notification, alert y recommendation `(healthy)` |
| Logs de los servicios y del broker                       | Diaria         | Sin errores de contexto ni reinicios                  |
| Colas de RabbitMQ y outbox                               | Semanal        | Colas `.dlq` vacías; ningún `*_outbox` con mensajes pendientes antiguos |
| `audit_log` y `login_error_log`                          | Diaria         | Sin ráfagas de `LOGIN_FAILED` ni picos por IP         |
| Notificaciones `FAILED`                                  | Diaria         | Cola `FAILED` en cero o con motivo conocido           |
| Lista de administradores activos                         | Semanal        | Solo las cuentas que deben serlo                      |
| Respaldo reciente en `/opt/energy-monitor/backups`       | Diaria         | Archivo del día; copia externa semanal                |
| Revisar `.env` y claves sin commitear                    | Semanal        | `pem`, `.env` y `.env.prod` ignorados                 |

# 19. Referencias (formato APA, 7.ª edición)

Las citas en el texto y la lista de referencias siguen el estilo de la séptima edición del manual de publicaciones de la APA (American Psychological Association \[APA\], 2020).

American Psychological Association. (2020). *Publication manual of the American Psychological Association* (7.ª ed.). https://www.apa.org/

Cortés Parra, J. E. y García Artunduaga, M. A. (2026a). *Especificación de requisitos de software de EnergyMonitor* (versión 2.1). SENA – CIES Regional Huila.

Cortés Parra, J. E. y García Artunduaga, M. A. (2026b). *Manual de implementación y despliegue de EnergyMonitor*. SENA – CIES Regional Huila.

Docker Inc. (s. f.). *Docker documentation*. https://docs.docker.com/

Google. (s. f.). *Cómo crear una contraseña de aplicación*. https://myaccount.google.com/apppasswords

MySQL. (2026). *MySQL 8.4 reference manual*. https://dev.mysql.com/doc/refman/8.4/en/

Spring. (s. f.). *Spring Boot reference documentation*. https://docs.spring.io/spring-boot/index.html
