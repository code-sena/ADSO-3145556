# Diccionario de datos — EnergyMonitor

**Proyecto:** Monitor de Consumo Eléctrico (EnergyMonitor) · SENA ADSO, ficha 3145556
**Esquema:** `energy_monitor` (MySQL 8.4, InnoDB, utf8mb4) · **Actualizado:** 27 de octubre de 2026

## 1. Alcance y fuente

Este diccionario describe **todas las tablas y columnas** de la base de datos del sistema tal como quedan después de aplicar todos los changesets de Liquibase de los siete servicios que tienen base de datos:

| **Servicio (puerto)** | **Repositorio** | **Changesets** | **Tablas** |
|---|---|---|---|
| security (8082) | en-monitor-security-service | security-001 a security-030 | 16 |
| device (8084) | en-monitor-device-service | device-001 a device-009 | 5 |
| measurement (8085) | en-monitor-measurement-service | measurement-001 a measurement-004 | 3 |
| home (8086) | en-monitor-home-service | home-001 a home-007 | 5 |
| notification (8087) | en-monitor-notification-service | notification-001 a notification-007 | 4 |
| alert (8088) | en-monitor-alert-service | alert-001 a alert-004 | 2 |
| recommendation (8089) | en-monitor-recommendation-service | recommendation-001 a recommendation-002 | 2 |
| **Total** | | **63 changesets** | **37** |

La fuente de verdad son los archivos `src/main/resources/db/changelog/**` de cada servicio; este documento y el script [`../../script-DB/energy_monitor.sql`](../database/energy_monitor.sql) se generaron a partir de ellos. El API gateway (8080) no tiene base de datos. El backend retirado `energy-monitor-backend` ya no crea tablas: las que eran suyas pasaron, con los mismos changesets y la misma ruta, a los servicios de hogares, notificaciones, alertas y recomendaciones, y en una base migrada puede quedar su tabla `backend_outbox`, sin uso. Las tablas de control de Liquibase (`DATABASECHANGELOG` y `DATABASECHANGELOGLOCK`) no se describen.

## 2. Reglas del modelo

- **Un esquema compartido, un dueño por tabla.** Cada tabla la crea, la migra y la escribe un solo servicio. Los demás obtienen esos datos por la API interna (`/internal/**` con la cabecera `X-Internal-Token`) o por eventos de RabbitMQ.
- **Llaves foráneas solo dentro de un servicio.** Las referencias a tablas de otro servicio (`user_id`, `home_id`, `device_id`, `measurement_id`, `consumption_level_id`) son columnas `VARCHAR(10)` sin `FOREIGN KEY` y se validan en la aplicación; en la columna *Restricción* aparecen como «referencia de aplicación, sin FK».
- **Identificadores** `VARCHAR(10)` generados por la aplicación (por ejemplo `user000001`).
- **Auditoría y borrado lógico:** las tablas de negocio tienen `created_at`, `updated_at` y `deleted_at`; un registro con `deleted_at` se considera eliminado.
- **Outbox transaccional:** cada servicio que publica eventos tiene su tabla `*_outbox` (seguridad, dispositivos, mediciones, hogares, alertas y recomendaciones). El servicio de notificaciones solo consume eventos y no tiene outbox.
- **Fechas:** `TIMESTAMP` en UTC; las tablas nuevas (outbox y `pending_registration`) usan `DATETIME(6)` con microsegundos.

## 3. Resumen de tablas

| **#** | **Tabla** | **Servicio dueño** | **Descripción** | **Llave primaria** | **Llaves foráneas (dentro del servicio)** |
|---|---|---|---|---|---|
| 1 | person | security (8082) | Registra los datos personales asociados a un usuario. | id_person | — |
| 2 | user | security (8082) | Gestiona las credenciales, estado y datos de acceso del usuario. | id_user | person_id → person |
| 3 | user_configuration | security (8082) | Almacena las preferencias de configuración de cada usuario. | id_configuration | user_id → user |
| 4 | password_reset_token | security (8082) | Almacena tokens para recuperación de contraseña. | id_reset_token | user_id → user |
| 5 | user_session | security (8082) | Registra las sesiones activas y renovables del usuario. | id_user_session | user_id → user |
| 6 | security_configuration | security (8082) | Define parámetros configurables de seguridad del sistema. | id_security_configuration | — |
| 7 | password_policy | security (8082) | Define las reglas de validación y expiración de contraseñas. | id_password_policy | — |
| 8 | login_error_log | security (8082) | Registra los errores producidos durante el inicio de sesión. | id_login_error | user_id → user |
| 9 | system_role | security (8082) | Define los roles disponibles en el sistema. | id_system_role | — |
| 10 | permission | security (8082) | Define los permisos funcionales del sistema. | id_permission | — |
| 11 | user_system_role | security (8082) | Relaciona usuarios con roles del sistema. | user_id, system_role_id | user_id → user; system_role_id → system_role |
| 12 | system_role_permission | security (8082) | Relaciona roles con permisos. | system_role_id, permission_id | system_role_id → system_role; permission_id → permission |
| 13 | audit_log | security (8082) | Registra las acciones relevantes ejecutadas por los usuarios. | id_audit_log | user_id → user |
| 14 | refresh_token | security (8082) | Cada fila es una generación de refresh token. | id_refresh_token | id_user_session → user_session; parent_id → refresh_token |
| 15 | security_outbox | security (8082) | Outbox transaccional del servicio: eventos que publica en RabbitMQ. | id_message | — |
| 16 | pending_registration | security (8082) | Registros que esperan la verificación del correo. | id_user | — |
| 17 | appliance_type | device (8084) | Catálogo de tipos de electrodomésticos. | id_appliance_type | — |
| 18 | device | device (8084) | Registra los dispositivos de medición instalados. | id_device | appliance_type_id → appliance_type |
| 19 | device_status_log | device (8084) | Registra el estado y conectividad de los dispositivos. | id_device_status | device_id → device |
| 20 | device_home | device (8084) | Relaciona dispositivos con hogares. | device_id, home_id | device_id → device |
| 21 | device_outbox | device (8084) | Outbox transaccional del servicio: eventos que publica en RabbitMQ. | id_message | — |
| 22 | measurement | measurement (8085) | Almacena las mediciones eléctricas registradas. | id_measurement | — |
| 23 | consumption_level | measurement (8085) | Define niveles de riesgo y sus rangos de consumo. | id_consumption_level | — |
| 24 | measurement_outbox | measurement (8085) | Outbox transaccional del servicio: eventos que publica en RabbitMQ. | id_message | — |
| 25 | home_type | home (8086) | Catálogo de tipos de hogar. | id_home_type | — |
| 26 | home | home (8086) | Registra los hogares administrados por el sistema. | id_home | home_type_id → home_type |
| 27 | home_thresholds | home (8086) | Configura los límites de consumo de un hogar. | id_threshold | home_id → home |
| 28 | user_home | home (8086) | Relaciona usuarios con hogares y define su rol. | user_id, home_id | home_id → home |
| 29 | home_outbox | home (8086) | Outbox transaccional del servicio: eventos que publica en RabbitMQ. | id_message | — |
| 30 | notification | notification (8087) | Registro de entrega de un mensaje al usuario. | id_notification | — |
| 31 | notification_preference | notification (8087) | Preferencias de canal de notificación de cada usuario (correo y push). | user_id | — |
| 32 | push_subscription | notification (8087) | Suscripciones de notificaciones push de los navegadores del usuario. | id_push_subscription | — |
| 33 | push_device | notification (8087) | Instalaciones de la app móvil que reciben notificaciones push por el relay de Expo (una fila por instalación). | id_push_device | — |
| 34 | alert | alert (8088) | Alertas de un hogar: lectura sobre el umbral de potencia (THRESHOLD, nivel HIGH o CRITICAL), límite diario o mensual alcanzado (LIMIT), dispositivo sin conexión (CONNECTIVITY) y primera lectura de un dispositivo recién vinculado (DEVICE). | id_alert | — |
| 35 | alert_outbox | alert (8088) | Outbox transaccional del servicio: eventos que publica en RabbitMQ. | id_message | — |
| 36 | recommendation | recommendation (8089) | Recomendaciones de ahorro que genera cada hora el servicio de recomendaciones a partir de la energía por hora del hogar; device_id vacío si aplica a todo el hogar. | id_recommendation | — |
| 37 | recommendation_outbox | recommendation (8089) | Outbox transaccional del servicio: eventos que publica en RabbitMQ. | id_message | — |

## 4. Diccionario por tabla

**Leyenda:** PK = llave primaria; FK = llave foránea dentro del mismo servicio; UNIQUE = restricción de unicidad (con su nombre); DEFAULT = valor por defecto; *Nulo* = Sí si la columna admite NULL. En los ENUM la columna *Tamaño / valores* lista los valores permitidos; en `TIMESTAMP(6)` y `DATETIME(6)`, el 6 es la precisión de microsegundos.

### person

*Registra los datos personales asociados a un usuario.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-001-create-person, security-018-drop-person-contact-data, security-020-drop-person-cellphone

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_person | VARCHAR | 10 | No | PK | Identificador de la persona |
| name | VARCHAR | 100 | No | — | Nombres de la persona |
| last_name | VARCHAR | 100 | Sí | — | Apellidos de la persona (opcional) |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### user

*Gestiona las credenciales, estado y datos de acceso del usuario.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-002-create-user, security-017-add-user-profile-image, security-022-widen-user-profile-image, security-024-add-user-google-subject, security-029-add-user-terms-accepted-at

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_user | VARCHAR | 10 | No | PK | Identificador del usuario |
| person_id | VARCHAR | 10 | No | UNIQUE (uk_user_person_id); FK → person.id_person | Persona asociada |
| password_hash | VARCHAR | 255 | No | — | Hash de la contraseña |
| email | VARCHAR | 255 | No | UNIQUE (uk_user_email) | Correo electrónico |
| email_verified | BOOLEAN | — | No | — | Indica si el correo fue verificado |
| registration_date | TIMESTAMP | — | No | — | Fecha y hora de registro |
| status | ENUM | ACTIVE, INACTIVE, BLOCKED | No | — | Estado de la cuenta |
| failed_login_attempts | INT | — | No | — | Cantidad de intentos fallidos |
| last_login_at | TIMESTAMP | — | Sí | — | Último inicio de sesión |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |
| profile_image | MEDIUMTEXT | — | Sí | — | Imagen de perfil de la cuenta (URL de datos en base64) |
| google_subject | VARCHAR | 255 | Sí | UNIQUE (uq_user_google_subject) | Identificador estable (sub) de la cuenta de Google con la que se creó la cuenta; vacío si se registró con contraseña |
| terms_accepted_at | TIMESTAMP | — | Sí | — | Momento en que la cuenta aceptó los términos y condiciones; vacío obliga a aceptarlos en el próximo inicio de sesión (respuesta 428) |

**Índices:** Ninguno

### user_configuration

*Almacena las preferencias de configuración de cada usuario.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-003-create-user-configuration

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_configuration | VARCHAR | 10 | No | PK | Identificador de la configuración |
| user_id | VARCHAR | 10 | No | UNIQUE (uk_user_configuration_user_id); FK → user.id_user | Usuario configurado |
| notify_by_email | BOOLEAN | — | No | — | Activa notificaciones por correo |
| notify_by_push | BOOLEAN | — | No | — | Activa notificaciones push |
| color_theme | VARCHAR | 50 | Sí | — | Tema de color |
| language | VARCHAR | 50 | Sí | — | Idioma preferido |
| social_provider | VARCHAR | 50 | Sí | — | Proveedor de autenticación social |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### password_reset_token

*Almacena tokens para recuperación de contraseña.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-004-create-password-reset-token, security-021-add-password-reset-token-attempts

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_reset_token | VARCHAR | 10 | No | PK | Identificador del token |
| user_id | VARCHAR | 10 | No | FK → user.id_user | Usuario propietario |
| reset_token_hash | VARCHAR | 64 | No | UNIQUE (uk_password_reset_token_reset_token_hash) | Hash HMAC-SHA256 (con pepper) del código de recuperación |
| used | BOOLEAN | — | No | — | Indica si el token fue utilizado |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| expiration_at | TIMESTAMP | — | No | — | Fecha de expiración |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |
| attempts | INT | — | No | DEFAULT 0 | Intentos de canje del código |

**Índices:** Ninguno

### user_session

*Registra las sesiones activas y renovables del usuario.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-005-create-user-session, security-014-add-session-revocation, security-016-drop-session-refresh-token

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_user_session | VARCHAR | 10 | No | PK | Identificador de sesión |
| user_id | VARCHAR | 10 | No | FK → user.id_user | Usuario de la sesión |
| revoked | BOOLEAN | — | No | — | Indica si la sesión fue revocada |
| ip_address | VARCHAR | 45 | Sí | — | Dirección IP |
| user_agent | VARCHAR | 150 | Sí | — | Agente del cliente |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| expiration_at | TIMESTAMP | — | No | — | Fecha de expiración |
| closed_at | TIMESTAMP | — | Sí | — | Fecha de cierre |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |
| revoked_at | TIMESTAMP | — | Sí | — | Fecha y hora de revocación de la sesión |
| revoked_reason | ENUM | REFRESH_TOKEN_REUSE, PASSWORD_CHANGED, ADMINISTRATIVE | Sí | — | Motivo de la revocación |

**Índices:** Ninguno

### security_configuration

*Define parámetros configurables de seguridad del sistema.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-006-create-security-configuration

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_security_configuration | VARCHAR | 10 | No | PK | Identificador de configuración |
| config_name | VARCHAR | 100 | No | UNIQUE (uk_security_configuration_config_name) | Nombre de la configuración |
| config_value | VARCHAR | 100 | No | — | Valor de la configuración |
| description | VARCHAR | 255 | Sí | — | Descripción |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### password_policy

*Define las reglas de validación y expiración de contraseñas.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-007-create-password-policy

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_password_policy | VARCHAR | 10 | No | PK | Identificador de política |
| min_length | INT | — | No | — | Longitud mínima |
| max_length | INT | — | No | — | Longitud máxima |
| require_uppercase | BOOLEAN | — | No | — | Exige mayúsculas |
| require_numbers | BOOLEAN | — | No | — | Exige números |
| require_symbols | BOOLEAN | — | No | — | Exige símbolos |
| expiration_days | INT | — | No | — | Días de vigencia |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### login_error_log

*Registra los errores producidos durante el inicio de sesión.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-008-create-login-error-log, security-027-login-error-email-not-verified

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_login_error | VARCHAR | 10 | No | PK | Identificador del error |
| user_id | VARCHAR | 10 | Sí | FK → user.id_user | Usuario relacionado, si existe |
| error_type | ENUM | INVALID_PASSWORD, USER_NOT_FOUND, ACCOUNT_BLOCKED, ACCOUNT_INACTIVE, EMAIL_NOT_VERIFIED | No | — | Tipo de error |
| description | VARCHAR | 500 | Sí | — | Descripción del error |
| ip_address | VARCHAR | 45 | Sí | — | Dirección IP |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### system_role

*Define los roles disponibles en el sistema.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-009-create-system-role

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_system_role | VARCHAR | 10 | No | PK | Identificador del rol |
| name | VARCHAR | 50 | No | UNIQUE (uk_system_role_name) | Nombre del rol |
| description | VARCHAR | 200 | Sí | — | Descripción |
| enabled | BOOLEAN | — | No | — | Indica si está habilitado |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### permission

*Define los permisos funcionales del sistema.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-010-create-permission

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_permission | VARCHAR | 10 | No | PK | Identificador del permiso |
| code | VARCHAR | 30 | No | UNIQUE (uk_permission_code) | Código único |
| name | VARCHAR | 50 | No | — | Nombre del permiso |
| description | VARCHAR | 100 | Sí | — | Descripción |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### user_system_role

*Relaciona usuarios con roles del sistema.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-011-create-user-system-role

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| user_id | VARCHAR | 10 | No | PK; FK → user.id_user | Usuario relacionado |
| system_role_id | VARCHAR | 10 | No | PK; FK → system_role.id_system_role | Rol asignado |
| assigned_at | TIMESTAMP | — | No | — | Fecha de asignación |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### system_role_permission

*Relaciona roles con permisos.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-012-create-system-role-permission

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| system_role_id | VARCHAR | 10 | No | PK; FK → system_role.id_system_role | Rol relacionado |
| permission_id | VARCHAR | 10 | No | PK; FK → permission.id_permission | Permiso asignado |
| assigned_at | TIMESTAMP | — | No | — | Fecha de asignación |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### audit_log

*Registra las acciones relevantes ejecutadas por los usuarios.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-013-create-audit-log

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_audit_log | VARCHAR | 10 | No | PK | Identificador del registro |
| user_id | VARCHAR | 10 | Sí | FK → user.id_user | Usuario que ejecutó la acción |
| action | ENUM | CREATE, UPDATE, DELETE, LOGIN, LOGIN_FAILED, LOGOUT | No | — | Acción auditada |
| description | VARCHAR | 255 | Sí | — | Descripción |
| ip_address | VARCHAR | 45 | Sí | — | Dirección IP |
| application | VARCHAR | 255 | Sí | — | Aplicación de origen |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### refresh_token

*Cada fila es una generación de refresh token. Solo se guarda el hash SHA-256 del secreto, nunca el secreto en claro. family_id agrupa todas las generaciones de un mismo login y parent_id encadena la genealogía.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-015-create-refresh-token

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_refresh_token | VARCHAR | 10 | No | PK | Identificador de la generación del token |
| id_user_session | VARCHAR | 10 | No | FK → user_session.id_user_session | Sesión a la que pertenece la generación |
| family_id | VARCHAR | 10 | No | — | Identificador de la raíz de la familia de generaciones |
| parent_id | VARCHAR | 10 | Sí | FK → refresh_token.id_refresh_token | Generación que esta reemplaza |
| token_hash | VARCHAR | 64 | No | UNIQUE (uk_refresh_token_hash) | Hash SHA-256 del secreto (nunca el secreto en claro) |
| status | ENUM | ACTIVE, ROTATED, REVOKED, EXPIRED | No | — | Estado del ciclo de vida de la generación |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| expires_at | TIMESTAMP | — | No | — | Fecha de expiración |
| rotated_at | TIMESTAMP | — | Sí | — | Fecha en que fue reemplazada |
| revoked_at | TIMESTAMP | — | Sí | — | Fecha de revocación |
| revoked_reason | ENUM | REFRESH_TOKEN_REUSE, PASSWORD_CHANGED, ADMINISTRATIVE | Sí | — | Motivo de la revocación |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** `ix_refresh_token_family_id` (family_id); `ix_refresh_token_user_session` (id_user_session)

### security_outbox

*Outbox transaccional del servicio: cada evento que el servicio publica en RabbitMQ se escribe aquí en la misma transacción que el cambio, y un relay lo publica cada segundo, del más antiguo al más reciente.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-026-create-security-outbox

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_message | VARCHAR | 10 | No | PK | Identificador del mensaje; viaja como message id en RabbitMQ y permite a los consumidores descartar duplicados |
| message_type | VARCHAR | 60 | No | — | Tipo de mensaje (routing key), por ejemplo `home.deleted` |
| payload | TEXT | — | Sí | — | Cuerpo del evento en JSON |
| occurred_at | DATETIME | 6 | No | — | Momento en que ocurrió el cambio (orden de publicación) |
| expires_at | DATETIME | 6 | Sí | — | Momento a partir del cual el mensaje ya no se publica (solo security_outbox, para los correos con código) |
| published_at | DATETIME | 6 | Sí | — | Momento en que el broker confirmó el mensaje; vacío mientras está pendiente |
| discarded_at | DATETIME | 6 | Sí | — | Momento en que el mensaje se descartó sin publicarse (vencido o escrito por las pruebas) |
| attempts | INT | — | No | DEFAULT 0 | Intentos de publicación realizados |
| last_error | VARCHAR | 255 | Sí | — | Último error de publicación (por ejemplo, ninguna cola recibió el mensaje) |

**Índices:** `idx_security_outbox_pending` (published_at, discarded_at, occurred_at)

### pending_registration

*Registros que esperan la verificación del correo. Ninguna cuenta (person ni user) existe hasta que se verifica el código; la solicitud vence a las 24 horas y un proceso horario la elimina.*

**Servicio dueño:** en-monitor-security-service · **Changesets:** security-028-create-pending-registration

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_user | VARCHAR | 10 | No | PK | Identificador reservado para la cuenta que se creará al verificar el correo |
| email | VARCHAR | 255 | No | UNIQUE (uq_pending_registration_email) | Correo de la solicitud (normalizado en minúsculas) |
| name | VARCHAR | 100 | No | — | Nombre ingresado en el registro |
| last_name | VARCHAR | 100 | Sí | — | Apellido ingresado en el registro (opcional) |
| password_hash | VARCHAR | 255 | No | — | Hash BCrypt de la contraseña elegida |
| profile_image | MEDIUMTEXT | — | Sí | — | Foto de perfil enviada en el registro (URL de datos en base64), opcional |
| ip_address | VARCHAR | 45 | Sí | — | Dirección IP desde la que se hizo el registro |
| requested_at | DATETIME | 6 | No | — | Momento de la solicitud; se usa como fecha de aceptación de los términos |
| expires_at | DATETIME | 6 | No | — | Vencimiento de la solicitud (24 horas) |

**Índices:** `idx_pending_registration_expires_at` (expires_at)

### appliance_type

*Catálogo de tipos de electrodomésticos.*

**Servicio dueño:** en-monitor-device-service · **Changesets:** device-001-create-appliance-type

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_appliance_type | VARCHAR | 10 | No | PK | Identificador del tipo |
| name | VARCHAR | 50 | No | UNIQUE (uk_appliance_type_name) | Nombre del tipo |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### device

*Registra los dispositivos de medición instalados.*

**Servicio dueño:** en-monitor-device-service · **Changesets:** device-002-create-device, device-007-add-api-key-hash, device-008-drop-clear-api-key

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_device | VARCHAR | 10 | No | PK | Identificador del dispositivo |
| name | VARCHAR | 50 | No | — | Nombre |
| appliance_type_id | VARCHAR | 10 | No | FK → appliance_type.id_appliance_type | Tipo de electrodoméstico |
| location | VARCHAR | 50 | Sí | — | Ubicación |
| description | VARCHAR | 200 | Sí | — | Descripción |
| installation_date | TIMESTAMP | — | No | — | Fecha de instalación |
| device_code | VARCHAR | 6 | No | UNIQUE (uk_device_device_code) | Código de 6 caracteres del módulo; es también su usuario MQTT |
| api_key_hash | VARCHAR | 64 | No | — | Hash SHA-256 de la clave de integración (la clave no se guarda en claro) |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### device_status_log

*Registra el estado y conectividad de los dispositivos.*

**Servicio dueño:** en-monitor-device-service · **Changesets:** device-003-create-device-status-log, device-005-status-log-last-seen-precision

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_device_status | VARCHAR | 10 | No | PK | Identificador del registro |
| device_id | VARCHAR | 10 | Sí | FK → device.id_device | Dispositivo relacionado |
| status | ENUM | ONLINE, OFFLINE | No | — | Estado del dispositivo |
| signal_strength | INT | — | Sí | — | Intensidad de señal |
| last_seen | TIMESTAMP | 6 | No | — | Última fecha de conexión |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### device_home

*Relaciona dispositivos con hogares.*

**Servicio dueño:** en-monitor-device-service · **Changesets:** device-004-create-device-home

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| device_id | VARCHAR | 10 | No | PK; FK → device.id_device | Dispositivo relacionado |
| home_id | VARCHAR | 10 | No | PK; home (referencia de aplicación, sin FK) | Hogar relacionado |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### device_outbox

*Outbox transaccional del servicio: cada evento que el servicio publica en RabbitMQ se escribe aquí en la misma transacción que el cambio, y un relay lo publica cada segundo, del más antiguo al más reciente.*

**Servicio dueño:** en-monitor-device-service · **Changesets:** device-009-create-device-outbox

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_message | VARCHAR | 10 | No | PK | Identificador del mensaje; viaja como message id en RabbitMQ y permite a los consumidores descartar duplicados |
| message_type | VARCHAR | 60 | No | — | Tipo de mensaje (routing key), por ejemplo `home.deleted` |
| payload | TEXT | — | No | — | Cuerpo del evento en JSON |
| occurred_at | DATETIME | 6 | No | — | Momento en que ocurrió el cambio (orden de publicación) |
| published_at | DATETIME | 6 | Sí | — | Momento en que el broker confirmó el mensaje; vacío mientras está pendiente |
| discarded_at | DATETIME | 6 | Sí | — | Momento en que el mensaje se descartó sin publicarse (vencido o escrito por las pruebas) |
| attempts | INT | — | No | DEFAULT 0 | Intentos de publicación realizados |
| last_error | VARCHAR | 255 | Sí | — | Último error de publicación (por ejemplo, ninguna cola recibió el mensaje) |

**Índices:** `idx_device_outbox_pending` (published_at, discarded_at, occurred_at)

### measurement

*Almacena las mediciones eléctricas registradas.*

**Servicio dueño:** en-monitor-measurement-service · **Changesets:** measurement-001-create-measurement

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_measurement | VARCHAR | 10 | No | PK | Identificador de la medición |
| device_id | VARCHAR | 10 | No | — | Dispositivo que genera la medición |
| date_time | TIMESTAMP | — | No | — | Fecha y hora de la medición |
| voltage | DOUBLE | — | No | — | Voltaje medido |
| current | DOUBLE | — | No | — | Corriente medida |
| active_power | DOUBLE | — | No | — | Potencia activa |
| stored_energy | DOUBLE | — | No | — | Energía acumulada |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### consumption_level

*Define niveles de riesgo y sus rangos de consumo.*

**Servicio dueño:** en-monitor-measurement-service · **Changesets:** measurement-002-create-consumption-level

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_consumption_level | VARCHAR | 10 | No | PK | Identificador del nivel |
| name | ENUM | LOW, MEDIUM, HIGH, CRITICAL | No | UNIQUE (uk_consumption_level_name) | Nivel de riesgo |
| description | VARCHAR | 200 | No | — | Descripción |
| min_limit | DOUBLE | — | No | — | Límite inferior |
| max_limit | DOUBLE | — | No | — | Límite superior |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### measurement_outbox

*Outbox transaccional del servicio: cada evento que el servicio publica en RabbitMQ se escribe aquí en la misma transacción que el cambio, y un relay lo publica cada segundo, del más antiguo al más reciente.*

**Servicio dueño:** en-monitor-measurement-service · **Changesets:** measurement-004-create-measurement-outbox

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_message | VARCHAR | 10 | No | PK | Identificador del mensaje; viaja como message id en RabbitMQ y permite a los consumidores descartar duplicados |
| message_type | VARCHAR | 60 | No | — | Tipo de mensaje (routing key), por ejemplo `home.deleted` |
| payload | TEXT | — | No | — | Cuerpo del evento en JSON |
| occurred_at | DATETIME | 6 | No | — | Momento en que ocurrió el cambio (orden de publicación) |
| published_at | DATETIME | 6 | Sí | — | Momento en que el broker confirmó el mensaje; vacío mientras está pendiente |
| discarded_at | DATETIME | 6 | Sí | — | Momento en que el mensaje se descartó sin publicarse (vencido o escrito por las pruebas) |
| attempts | INT | — | No | DEFAULT 0 | Intentos de publicación realizados |
| last_error | VARCHAR | 255 | Sí | — | Último error de publicación (por ejemplo, ninguna cola recibió el mensaje) |

**Índices:** `idx_measurement_outbox_pending` (published_at, discarded_at, occurred_at); `idx_measurement_outbox_discarded` (discarded_at)

### home_type

*Catálogo de tipos de hogar.*

**Servicio dueño:** en-monitor-home-service · **Changesets:** home-001-create-home-type

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_home_type | VARCHAR | 10 | No | PK | Identificador del tipo |
| name | VARCHAR | 50 | No | UNIQUE (uk_home_type_name) | Nombre del tipo de hogar |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### home

*Registra los hogares administrados por el sistema.*

**Servicio dueño:** en-monitor-home-service · **Changesets:** home-002-create-home

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_home | VARCHAR | 10 | No | PK | Código del hogar |
| name | VARCHAR | 50 | No | — | Nombre del hogar |
| home_type_id | VARCHAR | 10 | No | FK → home_type.id_home_type | Tipo de hogar |
| address | VARCHAR | 200 | No | — | Dirección |
| access_code | VARCHAR | 8 | No | UNIQUE (uk_home_access_code) | Código de acceso de 8 caracteres (letras mayúsculas y números) con el que otros usuarios se unen al hogar |
| description | VARCHAR | 200 | Sí | — | Descripción |
| creation_date | TIMESTAMP | — | No | — | Fecha de creación |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### home_thresholds

*Configura los límites de consumo de un hogar.*

**Servicio dueño:** en-monitor-home-service · **Changesets:** home-003-create-home-thresholds, home-006-add-threshold-limit-period

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_threshold | VARCHAR | 10 | No | PK | Identificador del umbral |
| home_id | VARCHAR | 10 | No | UNIQUE (uk_home_thresholds_home_id); FK → home.id_home | Hogar configurado |
| daily_limit | DOUBLE | — | No | — | Límite diario de consumo |
| monthly_limit | DOUBLE | — | No | — | Límite mensual de consumo |
| use_system_default | BOOLEAN | — | No | — | Usa límites predeterminados |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |
| limit_period | ENUM | DAILY, MONTHLY | No | DEFAULT 'DAILY' | Límite que fijó el propietario (DAILY o MONTHLY) |

**Índices:** Ninguno

### user_home

*Relaciona usuarios con hogares y define su rol.*

**Servicio dueño:** en-monitor-home-service · **Changesets:** home-004-create-user-home

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| user_id | VARCHAR | 10 | No | PK; user (referencia de aplicación, sin FK) | Usuario relacionado |
| home_id | VARCHAR | 10 | No | PK; FK → home.id_home | Hogar relacionado |
| role | ENUM | OWNER, MEMBER | No | — | Rol del usuario en el hogar |
| favorite | BOOLEAN | — | No | — | Indica si es el hogar favorito |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### home_outbox

*Outbox transaccional del servicio: cada evento que el servicio publica en RabbitMQ se escribe aquí en la misma transacción que el cambio, y un relay lo publica cada segundo, del más antiguo al más reciente.*

**Servicio dueño:** en-monitor-home-service · **Changesets:** home-007-create-home-outbox

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_message | VARCHAR | 10 | No | PK | Identificador del mensaje; viaja como message id en RabbitMQ y permite a los consumidores descartar duplicados |
| message_type | VARCHAR | 60 | No | — | Tipo de mensaje (routing key), por ejemplo `home.deleted` |
| payload | TEXT | — | No | — | Cuerpo del evento en JSON |
| occurred_at | DATETIME | 6 | No | — | Momento en que ocurrió el cambio (orden de publicación) |
| published_at | DATETIME | 6 | Sí | — | Momento en que el broker confirmó el mensaje; vacío mientras está pendiente |
| discarded_at | DATETIME | 6 | Sí | — | Momento en que el mensaje se descartó sin publicarse (vencido o escrito por las pruebas) |
| attempts | INT | — | No | DEFAULT 0 | Intentos de publicación realizados |
| last_error | VARCHAR | 255 | Sí | — | Último error de publicación (por ejemplo, ninguna cola recibió el mensaje) |

**Índices:** `idx_home_outbox_pending` (published_at, discarded_at, occurred_at)

### notification

*Registro de entrega de un mensaje al usuario. Alert y Recommendation son eventos; Notification registra si el mensaje fue entregado, por qué canal y su estado de lectura.*

**Servicio dueño:** en-monitor-notification-service · **Changesets:** notification-001-create-notification, notification-004-add-in-app-admin-notifications, notification-006-add-activity-sources, notification-007-add-message-params

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_notification | VARCHAR | 10 | No | PK | Identificador de la notificación |
| user_id | VARCHAR | 10 | No | user (referencia de aplicación, sin FK) | Usuario destinatario |
| home_id | VARCHAR | 10 | Sí | home (referencia de aplicación, sin FK) | Hogar relacionado |
| channel | ENUM | EMAIL, PUSH, IN_APP | No | — | Canal: EMAIL, PUSH o IN_APP (bandeja de la web y la app) |
| source_type | ENUM | ALERT, RECOMMENDATION, SECURITY, ADMINISTRATION, ACCOUNT, HOME, DEVICE | No | — | Origen: ALERT, RECOMMENDATION, SECURITY, ADMINISTRATION, ACCOUNT, HOME o DEVICE |
| source_id | VARCHAR | 10 | Sí | (source_type) (referencia de aplicación, sin FK) | Identificador del origen |
| message_key | VARCHAR | 100 | No | — | Clave del mensaje |
| delivery_status | ENUM | PENDING, SENT, FAILED | No | — | Estado de la entrega |
| sent_at | TIMESTAMP | — | Sí | — | Fecha de envío |
| read_at | TIMESTAMP | — | Sí | — | Fecha de lectura |
| failure_reason | VARCHAR | 200 | Sí | — | Motivo del fallo de entrega |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |
| message_params | TEXT | — | Sí | — | Parámetros del texto en JSON (nombres del hogar, del dispositivo o de las personas en el momento del evento), para traducirlo en el cliente |

**Índices:** `idx_notification_user_channel_created` (user_id, channel, created_at); `idx_notification_source` (source_type, source_id)

### notification_preference

*Preferencias de canal de notificación de cada usuario (correo y push).*

**Servicio dueño:** en-monitor-notification-service · **Changesets:** notification-002-create-notification-preference

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| user_id | VARCHAR | 10 | No | PK; user (referencia de aplicación, sin FK) | Usuario dueño de las preferencias (clave primaria) |
| email_enabled | BOOLEAN | — | No | DEFAULT TRUE | Entrega de alertas y recomendaciones por correo |
| push_enabled | BOOLEAN | — | No | DEFAULT TRUE | Entrega por notificación del navegador |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** Ninguno

### push_subscription

*Suscripciones de notificaciones push de los navegadores del usuario.*

**Servicio dueño:** en-monitor-notification-service · **Changesets:** notification-003-create-push-subscription

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_push_subscription | VARCHAR | 10 | No | PK | Identificador de la suscripción |
| user_id | VARCHAR | 10 | No | user (referencia de aplicación, sin FK) | Usuario suscrito |
| endpoint | VARCHAR | 500 | No | UNIQUE (uk_push_subscription_endpoint) | Dirección del servicio push del navegador (única) |
| p256dh | VARCHAR | 100 | No | — | Clave pública de cifrado de la suscripción |
| auth | VARCHAR | 50 | No | — | Secreto de autenticación de la suscripción |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** `idx_push_subscription_user_id` (user_id)

### push_device

*Instalaciones de la app móvil que reciben notificaciones push por el relay de Expo (una fila por instalación). No es otro tipo de PushSubscription: el token del relay no lleva endpoint ni claves de cifrado.*

**Servicio dueño:** en-monitor-notification-service · **Changesets:** notification-005-create-push-device

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_push_device | VARCHAR | 10 | No | PK | Identificador de la instalación |
| user_id | VARCHAR | 10 | No | user (referencia de aplicación, sin FK) | Usuario dueño de la instalación (con índice) |
| token | VARCHAR | 255 | No | UNIQUE (uk_push_device_token) | Token de push de la instalación; si se registra otra vez, cambia de dueño |
| platform | VARCHAR | 10 | No | — | Plataforma de la instalación (la app publicada es Android: ANDROID) |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** `idx_push_device_user_id` (user_id)

### alert

*Alertas de un hogar: lectura sobre el umbral de potencia (THRESHOLD, nivel HIGH o CRITICAL), límite diario o mensual alcanzado (LIMIT), dispositivo sin conexión (CONNECTIVITY) y primera lectura de un dispositivo recién vinculado (DEVICE).*

**Servicio dueño:** en-monitor-alert-service · **Changesets:** alert-001-create-alert, alert-002-add-device-alert-type, alert-003-add-limit-alert-type

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_alert | VARCHAR | 10 | No | PK | Identificador de la alerta |
| home_id | VARCHAR | 10 | No | home (referencia de aplicación, sin FK) | Hogar al que aplica |
| device_id | VARCHAR | 10 | Sí | device (referencia de aplicación, sin FK) | Dispositivo relacionado |
| type | ENUM | THRESHOLD, CONNECTIVITY, DEVICE, LIMIT | No | — | Tipo de alerta: THRESHOLD, CONNECTIVITY, DEVICE o LIMIT |
| message_key | VARCHAR | 100 | No | — | Clave del texto que traduce el cliente (alert.threshold.high, alert.threshold.critical, alert.limit.daily, alert.limit.monthly, alert.connectivity.offline, alert.device.linked) |
| date_time | TIMESTAMP | — | No | — | Fecha y hora |
| alert_status | ENUM | PENDING, RESOLVED | No | — | PENDING mientras sigue activa; RESOLVED cuando se resolvió o se marcó como leída |
| consumption_level_id | VARCHAR | 10 | Sí | consumption_level (referencia de aplicación, sin FK) | Nivel de consumo asociado |
| measurement_id | VARCHAR | 10 | Sí | measurement (referencia de aplicación, sin FK) | Medición asociada |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** `idx_alert_home_id` (home_id)

### alert_outbox

*Outbox transaccional del servicio: cada evento que el servicio publica en RabbitMQ se escribe aquí en la misma transacción que el cambio, y un relay lo publica cada segundo, del más antiguo al más reciente.*

**Servicio dueño:** en-monitor-alert-service · **Changesets:** alert-004-create-alert-outbox

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_message | VARCHAR | 10 | No | PK | Identificador del mensaje; viaja como message id en RabbitMQ y permite a los consumidores descartar duplicados |
| message_type | VARCHAR | 60 | No | — | Tipo de mensaje (routing key), por ejemplo `home.deleted` |
| payload | TEXT | — | No | — | Cuerpo del evento en JSON |
| occurred_at | DATETIME | 6 | No | — | Momento en que ocurrió el cambio (orden de publicación) |
| published_at | DATETIME | 6 | Sí | — | Momento en que el broker confirmó el mensaje; vacío mientras está pendiente |
| discarded_at | DATETIME | 6 | Sí | — | Momento en que el mensaje se descartó sin publicarse (vencido o escrito por las pruebas) |
| attempts | INT | — | No | DEFAULT 0 | Intentos de publicación realizados |
| last_error | VARCHAR | 255 | Sí | — | Último error de publicación (por ejemplo, ninguna cola recibió el mensaje) |

**Índices:** `idx_alert_outbox_pending` (published_at, discarded_at, occurred_at)

### recommendation

*Recomendaciones de ahorro que genera cada hora el servicio de recomendaciones a partir de la energía por hora del hogar; device_id vacío si aplica a todo el hogar.*

**Servicio dueño:** en-monitor-recommendation-service · **Changesets:** recommendation-001-create-recommendation

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_recommendation | VARCHAR | 10 | No | PK | Identificador de la recomendación |
| home_id | VARCHAR | 10 | No | home (referencia de aplicación, sin FK) | Hogar al que aplica |
| device_id | VARCHAR | 10 | Sí | device (referencia de aplicación, sin FK) | Dispositivo relacionado |
| type | ENUM | SAVING, PEAK_HOURS, HIGH_CONSUMPTION, HISTORICAL_COMPARISON, THRESHOLD | No | — | Regla que la generó: PEAK_HOURS, SAVING, HISTORICAL_COMPARISON, THRESHOLD o HIGH_CONSUMPTION |
| message_key | VARCHAR | 100 | No | — | Clave del texto (recommendation.peakHours, .standby, .aboveAverage, .limitProjection, .deviceIncrease) |
| date_time | TIMESTAMP | — | No | — | Fecha y hora |
| status | ENUM | READ, UNREAD | No | — | UNREAD o READ |
| created_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP | Fecha y hora de creación del registro (auditoría) |
| updated_at | TIMESTAMP | — | No | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Fecha y hora de la última modificación (auditoría) |
| deleted_at | TIMESTAMP | — | Sí | — | Fecha y hora del borrado lógico; vacío si el registro está vigente |

**Índices:** `idx_recommendation_home_id` (home_id)

### recommendation_outbox

*Outbox transaccional del servicio: cada evento que el servicio publica en RabbitMQ se escribe aquí en la misma transacción que el cambio, y un relay lo publica cada segundo, del más antiguo al más reciente.*

**Servicio dueño:** en-monitor-recommendation-service · **Changesets:** recommendation-002-create-recommendation-outbox

| **Campo** | **Tipo** | **Tamaño / valores** | **Nulo** | **Restricción** | **Descripción** |
|---|---|---|---|---|---|
| id_message | VARCHAR | 10 | No | PK | Identificador del mensaje; viaja como message id en RabbitMQ y permite a los consumidores descartar duplicados |
| message_type | VARCHAR | 60 | No | — | Tipo de mensaje (routing key), por ejemplo `home.deleted` |
| payload | TEXT | — | No | — | Cuerpo del evento en JSON |
| occurred_at | DATETIME | 6 | No | — | Momento en que ocurrió el cambio (orden de publicación) |
| published_at | DATETIME | 6 | Sí | — | Momento en que el broker confirmó el mensaje; vacío mientras está pendiente |
| discarded_at | DATETIME | 6 | Sí | — | Momento en que el mensaje se descartó sin publicarse (vencido o escrito por las pruebas) |
| attempts | INT | — | No | DEFAULT 0 | Intentos de publicación realizados |
| last_error | VARCHAR | 255 | Sí | — | Último error de publicación (por ejemplo, ninguna cola recibió el mensaje) |

**Índices:** `idx_recommendation_outbox_pending` (published_at, discarded_at, occurred_at)

## 5. Datos semilla

| **Tabla** | **Changeset** | **Contenido** |
|---|---|---|
| password_policy | security-019 | Política por defecto `pol0000001`: 8 a 64 caracteres, mayúsculas, números y símbolos obligatorios, vencimiento de 90 días (solo si la tabla está vacía). |
| system_role, permission, system_role_permission | security-025 | Rol global `ADMIN` (`rol0000001`) con los permisos `USER.READ` (`prm0000001`), `USER.MANAGE` (`prm0000002`) y `ROLE.MANAGE` (`prm0000003`). Ninguna cuenta recibe el rol: el primer administrador se asigna con `make-admin.sh` del repositorio energy-monitor-infra. |
| appliance_type | device-006 | refrigerator, air_conditioner, lighting, washing_machine, television, microwave, computer, water_heater y other (`appl000001` a `appl000009`). |
| consumption_level | measurement-003 | LOW [0, 500) W, MEDIUM [500, 1500) W, HIGH [1500, 3000) W y CRITICAL [3000, 1 000 000) W de potencia activa. |
| home_type | home-005 | house, apartment, studio, country_house, cabin y other. |

Los umbrales por defecto de un hogar (6 kWh diarios y 180 kWh mensuales) no son datos semilla: son configuración del servicio de hogares (`daily-limit` y `monthly-limit` en su `application.yaml`).
