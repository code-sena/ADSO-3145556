# 5.2 Modelo de datos

**Proyecto:** Monitor de Consumo Eléctrico — EnergyMonitor
**Ficha:** 3145556 — Análisis y Desarrollo de Software (ADSO), SENA CIES Regional Huila
**Versión:** 1.0

---

> **Nota de actualización (27/10/2026).** Este documento describe el diseño de la versión 1.0, cuando el backend era el monolito modular `energy-monitor-backend` (Spring Modulith). Ese diseño evolucionó: los siete contextos acotados son hoy **siete microservicios** detrás del API gateway `en-monitor-api-gateway` (8080) — seguridad 8082, dispositivos 8084, mediciones 8085, hogares 8086, notificaciones 8087, alertas 8088 y recomendaciones 8089 —, comunicados por REST interno (`/internal/**`) y eventos de RabbitMQ con outbox transaccional, sobre el mismo esquema MySQL compartido (37 tablas). El backend modular quedó retirado. Las capas hexagonales, los contextos y los patrones descritos aquí se conservan dentro de cada servicio; la arquitectura vigente, las rutas, los eventos y la configuración están en el [manual técnico](../../08-manuals/es/technical-manual.md) (secciones 9 a 13) y el modelo de datos vigente en [`06-base-datos`](data-dictionary.md).


## 1. Propósito

Esta sección define el modelo de datos del sistema: cómo se estructuran las 29 tablas que lo componen, qué convenciones se aplican de manera uniforme, cómo se relacionan los contextos acotados (bounded contexts) y qué reglas de integridad garantizan la consistencia de la información.

El modelo de datos es el artefacto más estable del diseño: a diferencia de las pantallas o de los endpoints, las tablas y sus restricciones cambian con muy poca frecuencia, por lo que este documento sirve de contrato entre el backend, los dos front-ends y el proceso de calidad de datos.

## 2. Motor de persistencia y esquema

| **Aspecto** | **Definición** |
|---|---|
| Motor de base de datos | MySQL 8.4 |
| Esquema | `energy_monitor` |
| Collation | `utf8mb4_unicode_ci` |
| Charset | `utf8mb4` |
| Tipo de columnas de tiempo | `TIMESTAMP` |
| Formato de números reales | `DOUBLE` |
| Gestión del esquema | **Liquibase** — `src/main/resources/db/changelog` |
| Mapeo objeto-relacional | Spring Data JPA + Hibernate 7 |
| Comportamiento de Hibernate | `ddl-auto = validate` (valida, nunca crea) |
| Zona horaria de sesión JPA | UTC (`hibernate.jdbc.time_zone: UTC`) |
| Visibilidad de sesión | `open-in-view: false` |
| Tablas totales | **29** |

> **Decisión de diseño:** Hibernate opera en modo `validate`. Ninguna tabla se crea desde las entidades JPA; el esquema existe únicamente porque un *changeset* de Liquibase lo declaró. Esto garantiza que el modelo de datos documentado en este punto y el modelo de datos realmente desplegado sean siempre el mismo.

## 3. Convenciones de diseño de datos

Las siguientes convenciones se aplican de forma uniforme en las 29 tablas y fueron respetadas durante todo el modelado.

### 3.1 Convenciones estructurales

| **Convención** | **Regla aplicada** | **Justificación** |
|---|---|---|
| Nombre de tabla | `snake_case`, en singular o plural según la semántica del conjunto | Legibilidad y consistencia con las columnas |
| Nombre de columna | `snake_case` y descriptivo (`registration_date`, `daily_limit`, `home_id`) | Autodescriptiva, evita ambigüedad |
| Nombre de restricción | `pk_<tabla>` y `fk_<tabla>_<columna>` | Identificación inmediata en los cambiosets |
| Llave primaria | `VARCHAR(10)` en todas las entidades | Identificador corto, uniforme y legible |
| Llave primaria compuesta | `user_home`, `device_home`, `user_system_role`, `system_role_permission` | Tablas puente que modelan relaciones N:M |
| Llave foránea | Sólo dentro del mismo contexto acotado | Ver sección 4 |
| Nombres de índices | `idx_[tabla]_[columna(s)]` → `idx_alert_home_id`, `idx_recommendation_home_id` | Identificación inmediata del propósito |

### 3.2 Convenciones de auditoría

Todas las tablas, sin excepción, incluyen tres columnas de auditoría:

| **Columna** | **Tipo** | **Restricción** | **Comportamiento** |
|---|---|---|---|
| `created_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Fecha de creación del registro |
| `updated_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Se actualiza automáticamente en cada modificación |
| `deleted_at` | `TIMESTAMP` | Nullable | Marca la eliminación lógica del registro |

**Implementación en el código.** Cada módulo define su propia superclase `BaseAuditEntity` (`@MappedSuperclass`) con los *callbacks* `@PrePersist` y `@PreUpdate`. Las columnas se sellan en Java (`updatable = false`) para que la aplicación sea la única fuente de la fecha de creación.

> **Divergencia documentada.** Los módulos `security`, `home` y `alert` sellan `created_at` con `@PrePersist`; `device` usa `@CreationTimestamp`/`@UpdateTimestamp` de Hibernate; `notification` usa además `LocalDateTime` en lugar de `Instant`. Son decisiones locales válidas, pero deben unificarse si la evidencia de auditoría pasa a ser un requisito de calidad (ver sección 10).

### 3.3 Eliminación lógica (soft delete)

**No se ejecutan `DELETE` físicos por defecto en ninguna tabla.** Un registro se considera eliminado cuando `deleted_at IS NOT NULL`.

| **Beneficio** | **Descripción** |
|---|---|
| Trazabilidad | Se conserva el histórico de mediciones, alertas y auditorías |
| Recuperación | Un registro eliminado por error puede restaurarse |
| Cumplimiento normativo | Facilita el tratamiento de datos personales bajo la Ley 1581 de 2012 (Habeas Data) |

> **Consecuencia en el diseño:** las consultas de lectura filtran por `deleted_at IS NULL`. Se aplica de manera explícita en los repositorios, por ejemplo `RefreshTokenRepository.findByTokenHashAndDeletedAtIsNull(...)`. Las tablas puente con llave primaria compuesta (`user_system_role`, `system_role_permission`) **no** filtran por `deleted_at` en su consulta de existencia, precisamente para poder reactivar una fila previamente eliminada al reasignar un rol.

### 3.4 Tipos de dominio

| **Tipo** | **Regla** | **Ejemplo** |
|---|---|---|
| Booleano | `TINYINT(1)`, mapeado con la anotación marcadora `@TinyIntBoolean` (`@JdbcTypeCode(SqlTypes.TINYINT)`) | `user.email_verified` |
| Enumeración | `ENUM` de MySQL, mapeada con `@Enumerated(STRING)` | `user.status`, `alert.type` |
| Identificador | `VARCHAR(10)` generado por un `IdentifierGeneratorPort` por módulo | `SecureIdentifierGeneratorAdapter` |
| Booleano nullable | `Boolean` (objeto) en lugar de `boolean` primitivo, cuando el valor puede ser nulo | `home_thresholds.use_system_default` |

> **Detalle de diseño:** `@TinyIntBoolean` existe porque Hibernate 7 mapearía `boolean` a `bit`, que no coincide con `tinyint(1)` del esquema; con `ddl-auto = validate` esa discrepancia impediría el arranque. La anotación fuerza la correspondencia correcta.

## 4. Fronteras entre contextos: referencias de aplicación

Este es uno de los decisiones centrales del modelo de datos y explica la separación entre contextos en el diagrama entidad-relación.

### 4.1 Regla

> Las referencias **dentro** de un contexto acotado se implementan con `FOREIGN KEY` de SQL.
> Las referencias **entre** contextos acotados se almacenan como identificadores a nivel de aplicación y **no generan `FOREIGN KEY` en SQL**.

### 4.2 Referencias de aplicación entre contextos

| **Origen** | **Columna** | **Destino** | **Contexto de destino** |
|---|---|---|---|
| `User` | `id_user` | `UserHome.user_id` | Home Management |
| `DeviceHome` | `home_id` | `Home.id_home` | Home Management |
| `Measurement` | `device_id` | `Device.id_device` | Devices |
| `Alert` | `home_id` | `Home.id_home` | Home Management |
| `Alert` | `device_id` | `Device.id_device` | Devices |
| `Alert` | `measurement_id` | `Measurement.id_measurement` | Monitoring |
| `Alert` | `consumption_level_id` | `ConsumptionLevel.id_consumption_level` | Monitoring |
| `Recommendation` | `home_id` | `Home.id_home` | Home Management |
| `Recommendation` | `device_id` | `Device.id_device` | Devices |
| `Notification` | `user_id` | `User.id_user` | Security |
| `Notification` | `home_id` | `Home.id_home` | Home Management |
| `Notification` | `source_type` + `source_id` | `Alert`, `Recommendation` o evento de seguridad | Alerts / Recommendations / Security |
| `PushSubscription` | `user_id` | `User.id_user` | Security |

### 4.3 Justificación

| **Ventaja** | **Descripción** |
|---|---|
| Aislamiento de contextos | Un cambio en el esquema de un contexto no obliga a modificar los demás |
| Validación en la aplicación | Las reglas de integridad entre contextos se aplican en la capa de dominio, donde el mensaje de error puede ser comprensible |
| Escalabilidad | Evita bloqueos y joins costosos entre tablas de contextos distintos |
| Legibilidad del modelo | El diagrama muestra explícitamente qué relaciones son fuertes (línea continua) y cuáles lógicas (línea punteada) |

### 4.4 Consecuencia de diseño: mapeo relacional plano

**En todo el proyecto no existe una sola anotación de asociación JPA** (`@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`). Las llaves foráneas se modelan como atributos `String`/`Long` simples, y las tablas puente usan `@EmbeddedId` para la llave primaria compuesta.

La razón está documentada en el propio código (`UserEntity`):

> *"`person_id` is mapped as a plain identifier column rather than a `@ManyToOne`… The same reasoning applies to every other Security foreign key."*

| **Beneficio** | **Descripción** |
|---|---|
| Consistencia | El mapeo es idéntico dentro y fuera de los contextos |
| Control de consultas | Evita el problema de las *N+1* y el carga diferida accidental, ya que `open-in-view` está deshabilitado |
| Alineación con el MER | El modelo en código coincide exactamente con el modelo documentado |

### 4.5 Consecuencia de diseño: tabla puente polimórfica

`Notification` no puede apuntar a `Alert`, a `Recommendation` ni a un evento de seguridad con una llave foránea tradicional: son tablas de contextos diferentes y la combinación de varias `FOREIGN KEY` sería mutuamente excluyente. La solución adoptada es una **referencia polimórfica**:

```
source_type ENUM ('ALERT', 'RECOMMENDATION', 'SECURITY')  +  source_id VARCHAR(10)
```

Este par localiza la fila origen sin acoplar el contexto de Notifications al esquema de los otros. La tabla responde a una pregunta que `Alert` y `Recommendation` no pueden responder por sí solas: **si el mensaje llegó al usuario, por qué canal y si fue leído**.

## 5. Modelo entidad-relación por contextos

El diagrama base se encuentra en `../diagrams/er-model.wsd` (sintaxis PlantUML, renderizable en <https://www.plantuml.com/plantuml/uml>).

**Figura 1.** *Modelo entidad-relación: 29 entidades agrupadas en siete contextos acotados.*

> **Nota de versión.** El diagrama adjunto (`MER.wsd`) fue elaborado en una fase anterior del proyecto y contiene **27** entidades. El esquema vigente, derivado de los *changesets* de Liquibase, tiene **29**. La sección 9 documenta con precisión la diferencia entre ambos.

### 5.1 Contexto Security — 14 tablas

Modela identidad, credenciales, sesiones, control de acceso y auditoría.

| **Tabla** | **Descripción** | **Llave primaria** | **Relaciones internas** |
|---|---|---|---|
| `person` | Datos personales asociados a un usuario | `id_person` | 1:1 `user` |
| `user` | Credenciales, estado y datos de acceso | `id_user` | 1:1 `person`; 1:1 `user_configuration`; 1:N `password_reset_token`; 1:N `user_session`; 1:N `user_system_role`; 1:N `audit_log`; 1:1 `login_error_log` (opcional) |
| `user_configuration` | Preferencias de configuración del usuario | `id_configuration` | 1:1 `user` |
| `password_reset_token` | Tokens de recuperación de contraseña | `id_reset_token` | N:1 `user` |
| `user_session` | Sesiones activas y renovables | `id_user_session` | N:1 `user`; 1:N `refresh_token` |
| `refresh_token` | Generaciones de refresh token (hash SHA-256) | `id_refresh_token` | N:1 `user_session`; 0..1:1 `refresh_token` (`parent_id`) |
| `security_configuration` | Parámetros configurables de seguridad | `id_security_configuration` | Sin relaciones |
| `password_policy` | Reglas de validación y expiración de contraseñas | `id_password_policy` | Sin relaciones |
| `login_error_log` | Errores producidos durante el inicio de sesión | `id_login_error` | N:1 `user` (opcional) |
| `system_role` | Roles disponibles en el sistema | `id_system_role` | 1:N `user_system_role`; 1:N `system_role_permission` |
| `user_system_role` | Relación usuario–rol | `user_id` + `system_role_id` | N:1 `user`; N:1 `system_role` |
| `permission` | Permisos funcionales (`METER.READ`, `ALERT.READ`…) | `id_permission` | 1:N `system_role_permission` |
| `system_role_permission` | Relación rol–permiso | `system_role_id` + `permission_id` | N:1 `system_role`; N:1 `permission` |
| `audit_log` | Acciones relevantes ejecutadas por los usuarios | `id_audit_log` | N:1 `user` (opcional) |

**Modelo de rotación de refresh tokens.** `refresh_token` es la tabla más elaborada del modelo de seguridad. Cada inicio de sesión crea una **familia** de tokens identificada por `family_id`. Cada renovación genera una fila nueva cuyo `parent_id` apunta a la fila que reemplaza. Cuando una fila ya rotada vuelve a usarse, se detecta la reutilización (*replay*) y se revoca la familia completa con `revoked_reason = 'REFRESH_TOKEN_REUSE'`.

```
  Generación 1 (parent_id = NULL)   status = ROTATED  ← reutilizada
  └─ Generación 2 (parent_id = 1)    status = ROTATED
     └─ Generación 3 (parent_id = 2) status = ACTIVE    ← vigente
  family_id = mismo valor en las tres filas
```

Solo se almacena el `token_hash` (SHA-256, 64 caracteres). **Nunca** se guarda el secreto en texto claro.

**Decisiones de seguridad incorporadas al esquema:**

| **Decisión** | **Implementación** | **Requisito** |
|---|---|---|
| Contador de intentos por token | `password_reset_token.attempts INT NOT NULL` | RNF-SEG-05, RNF-SEG-06 |
| Enlace con Google | `user.google_subject VARCHAR(255)` | RF-SEC-05 |
| Avatar como imagen completa | `user.profile_image` `MEDIUMTEXT` (data URL en base64) | RF-SEC-08 |

### 5.2 Contexto Home Management — 4 tablas

| **Tabla** | **Descripción** | **Llave primaria** | **Relaciones internas** |
|---|---|---|---|
| `home_type` | Catálogo de tipos de hogar | `id_home_type` | 1:N `home` |
| `home` | Hogares administrados por el sistema | `id_home` | N:1 `home_type`; 1:1 `home_thresholds`; 1:N `user_home`; 1:N `device_home`; 1:N `alert`; 1:N `recommendation` |
| `home_thresholds` | Límites de consumo de un hogar | `id_threshold` | 1:1 `home` (`home_id` es `UNIQUE`) |
| `user_home` | Relación usuario–hogar con rol y favorito | `user_id` + `home_id` | N:1 `user` *(ref. aplicación)*; N:1 `home` |

**Diseño destacado — `access_code`.** El campo `access_code VARCHAR(8) UNIQUE` es el mecanismo de vinculación de miembros: el propietario comparte este código y quien lo ingresa se une al hogar con rol `MEMBER`. No se envían invitaciones por correo, lo que simplifica el flujo de la interfaz gráfica y evita la necesidad de una tabla de invitaciones.

**Diseño destacado — umbrales por hogar y periodo activo.** `home_thresholds` implementa una relación 1:1 estricta con `home` (`home_id` es `UNIQUE` y `NOT NULL`). La columna `limit_period ENUM('DAILY','MONTHLY')` indica cuál de los dos límites está en edición en la interfaz. El valor por defecto de la columna es `'DAILY'`.

**Resolución del hueco `use_system_default`.** La bandera booleana `use_system_default` sí tiene respaldo: el módulo `home` declara `HomeProperties` (`home.defaults`) con `dailyLimit = 6.0` kWh y `monthlyLimit = 180.0` kWh, expuesto mediante el puerto `SystemDefaultsPort` y su adaptador `SystemDefaultsAdapter`. El endpoint `POST /api/v1/homes/{homeId}/thresholds/defaults` permite restaurar esos valores.

### 5.3 Contexto Devices — 4 tablas

| **Tabla** | **Descripción** | **Llave primaria** | **Relaciones internas** |
|---|---|---|---|
| `appliance_type` | Catálogo de tipos de electrodomésticos | `id_appliance_type` | 1:N `device` |
| `device` | Dispositivos de medición instalados | `id_device` | N:1 `appliance_type`; 1:N `device_status_log`; 1:N `device_home`; 1:N `measurement`; 1:N `alert`; 1:N `recommendation` |
| `device_status_log` | Estado y conectividad de los dispositivos | `id_device_status` | N:1 `device` |
| `device_home` | Relación dispositivo–hogar | `device_id` + `home_id` | N:1 `device`; N:1 `home` *(ref. aplicación)* |

**Diseño destacado — `device_code` y `api_key`.** `device_code VARCHAR(6) UNIQUE` es el identificador corto que el dispositivo (ESP32) declara al vincularse; `api_key VARCHAR(100)` es la credencial de integración. Ambos satisfacen el requisito RF-DEV-02 (vinculación segura del dispositivo).

**Diseño destacado — `device_status_log` con histórico.** En lugar de un único campo de estado en `device`, el estado y la conectividad se registran como unabitácora append-only. Esto permite determinar el último contacto (`last_seen`) y la intensidad de la señal en el momento de la lectura.

**Detalle de implementación.** `device_status_log.device_id` es **nullable** en el esquema vigente, lo que permite conservar el rastro histórico de un dispositivo desvinculado del hogar sin romper la integridad referencial.

### 5.4 Contexto Monitoring and Measurements — 2 tablas

| **Tabla** | **Descripción** | **Llave primaria** | **Relaciones internas** |
|---|---|---|---|
| `measurement` | Mediciones eléctricas registradas | `id_measurement` | N:1 `device` *(ref. aplicación)* |
| `consumption_level` | Niveles de riesgo y rangos de consumo | `id_consumption_level` | 1:N `alert` |

**Campos de `measurement`:**

| **Campo** | **Tipo** | **Significado físico** |
|---|---|---|
| `date_time` | `TIMESTAMP` | Instante de la lectura |
| `voltage` | `DOUBLE` | Voltaje (V) |
| `current` | `DOUBLE` | Corriente (A) |
| `active_power` | `DOUBLE` | Potencia activa (W) |
| `stored_energy` | `DOUBLE` | Energía acumulada (kWh) |

> **Decisión de diseño:** **no existe una entidad independiente de «historial de consumo»**. El historial es el conjunto de todas las filas de `measurement` filtradas por `device_id`, rango de fechas y zona horaria. Esto elimina la duplicación de datos y garantiza que las gráficas del historial y las estadísticas consuman exactamente la misma fuente de verdad.

**Diseño destacado — `consumption_level` como tabla de rangos.** En lugar de tener los umbrales codificados en el código, los cuatro niveles de riesgo (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`) se almacenan como filas con `min_limit` y `max_limit`. Esto permite ajustar la escala de riesgo mediante un *changeset* sin tocar código, y habilita que las recomendaciones se generen sobre rangos configurables. La tabla se siembra con un *changeset* dedicado (`003-seed-consumption-level`).

### 5.5 Contexto Alerts — 1 tabla

| **Tabla** | **Descripción** | **Llave primaria** | **Relaciones internas** |
|---|---|---|---|
| `alert` | Alertas de umbral, límite y conectividad | `id_alert` | N:1 `home`; N:1 `device`; N:1 `consumption_level`; N:1 `measurement` — todas *(ref. aplicación)* |

**Índice definido:** `idx_alert_home_id (home_id)`.

**Taxonomía de alertas.** El esquema vigente reconoce cuatro tipos:

| **Tipo** | **Origen** | **`consumption_level_id`** | **`measurement_id`** |
|---|---|---|---|
| `THRESHOLD` | La medición cae en un rango de riesgo | Obligatorio | Obligatorio |
| `LIMIT` | El consumo acumulado supera el umbral configurado | `NULL` | Obligatorio |
| `CONNECTIVITY` | El dispositivo deja de reportar | `NULL` | `NULL` |
| `DEVICE` | Vinculación o reemisión de credenciales de un dispositivo | `NULL` | `NULL` |

### 5.6 Contexto Recommendations — 1 tabla

| **Tabla** | **Descripción** | **Llave primaria** | **Relaciones internas** |
|---|---|---|---|
| `recommendation` | Recomendaciones para hogares y dispositivos | `id_recommendation` | N:1 `home`; N:1 `device` — ambas *(ref. aplicación)* |

**Reglas de integridad:**

- `device_id` es opcional: `NULL` cuando la recomendación aplica al hogar completo; `NOT NULL` cuando se refiere a un dispositivo específico.
- Las recomendaciones se generan con `status = 'UNREAD'`; el usuario alterna entre `READ` y `UNREAD`.
- Una recomendación puede originarse del análisis de **múltiples** mediciones, por lo que **no** almacena referencias directas a `measurement`. Esa decisión evita una tabla puente y mantiene el modelo flexible frente a los algoritmos de análisis.
- Índice definido: `idx_recommendation_home_id (home_id)`.

**Periodicidad de generación.** El módulo `recommendation` incorpora `RecommendationScheduler`, que evalúa todos los hogares cada hora (`recommendation.evaluate-interval: PT1H`, desfase inicial `PT2M`) con un periodo de enfriamiento de 7 días por recomendación (`recommendation.cool-down: P7D`) para no repetir el mismo consejo.

### 5.7 Contexto Notifications — 3 tablas

| **Tabla** | **Descripción** | **Llave primaria** |
|---|---|---|
| `notification` | Registro de entrega de un mensaje al usuario | `id_notification` |
| `notification_preference` | Preferencias de canal por usuario | `user_id` |
| `push_subscription` | Suscripciones push de navegadores | `id_push_subscription` |

**`notification_preference` — decisión de diseño.** El `user_id` es la **llave primaria** de la tabla. Esto garantiza por construcción que un usuario tenga como máximo una fila de preferencias: la unicidad «uno a uno» no depende de un índice único adicional, sino del modelo.

**`push_subscription` — decisión de diseño.** El `endpoint VARCHAR(500)` es `UNIQUE`, porque un mismo navegador genera un endpoint distinto por aplicación y por suscripción. Las claves `p256dh` y `auth` completan la suscripción VAPID.

**Ciclos de estado independientes.** La tabla `notification` separa dos conceptos que frecuentemente se confunden:

| **Ciclo** | **Columnas** | **Estados** |
|---|---|---|
| **Entrega** | `delivery_status` | `PENDING` → `SENT` \| `FAILED` |
| **Lectura** | `read_at` | `NULL` mientras no se ha leído |

> **Una alerta entregada no es una alerta resuelta.** `notification.read_at` es ortogonal a `alert.alert_status` (`PENDING`/`RESOLVED`) y a `delivery_status`. Esta separación de responsabilidades es una decisión de diseño deliberada y debe mantenerse en futuras iteraciones.

**Decisión de seguridad: el cuerpo del mensaje nunca se almacena.** La tabla no tiene ninguna columna con el texto del correo ni de la notificación. La razón está documentada en el propio módulo `notification`:

> *«A password recovery secret travels in that body, so a column holding it in clear text would undo the decision to keep only a hash elsewhere. Only the recipient, the type, the subject and the outcome are kept.»*

Es decir: si el código de recuperación de contraseña se almacenara en texto claro en la tabla de notificaciones, se perdería el beneficio de guardar únicamente su hash en `password_reset_token`. Solo se conservan el destinatario, el tipo, el asunto (`message_key`) y el resultado del envío.

**Selección de canal.** El canal se determina según las preferencias del destinatario en `notification_preference`:

| **Preferencia** | **Canal asignado** |
|---|---|
| `email_enabled = TRUE` | `EMAIL` |
| `push_enabled = TRUE` | `PUSH` |

**Barrido de entregas abandonadas.** `AbandonedDeliverySweeper` cierra las filas `PENDING` con más de `app.mail.pending-threshold` (30 minutos) y se ejecuta cada `app.mail.sweep-delay` (5 minutos) y también al iniciar la aplicación.

## 6. Resumen de las 29 tablas

| **#** | **Tabla** | **Contexto** | **Dominio funcional** |
|---|---|---|---|
| 1 | `person` | Security | Identidad |
| 2 | `user` | Security | Identidad |
| 3 | `user_configuration` | Security | Preferencias |
| 4 | `password_reset_token` | Security | Recuperación de contraseña |
| 5 | `user_session` | Security | Sesiones |
| 6 | `refresh_token` | Security | Sesiones |
| 7 | `security_configuration` | Security | Configuración |
| 8 | `password_policy` | Security | Contraseñas |
| 9 | `login_error_log` | Security | Auditoría |
| 10 | `system_role` | Security | Control de acceso |
| 11 | `user_system_role` | Security | Control de acceso |
| 12 | `permission` | Security | Control de acceso |
| 13 | `system_role_permission` | Security | Control de acceso |
| 14 | `audit_log` | Security | Auditoría |
| 15 | `home_type` | Home Management | Hogares |
| 16 | `home` | Home Management | Hogares |
| 17 | `home_thresholds` | Home Management | Umbrales |
| 18 | `user_home` | Home Management | Miembros |
| 19 | `appliance_type` | Devices | Dispositivos |
| 20 | `device` | Devices | Dispositivos |
| 21 | `device_status_log` | Devices | Conectividad |
| 22 | `device_home` | Devices | Dispositivos |
| 23 | `measurement` | Monitoring | Mediciones |
| 24 | `consumption_level` | Monitoring | Niveles de riesgo |
| 25 | `alert` | Alerts | Alertas |
| 26 | `recommendation` | Recommendations | Recomendaciones |
| 27 | `notification` | Notifications | Notificaciones |
| 28 | `notification_preference` | Notifications | Preferencias de canal |
| 29 | `push_subscription` | Notifications | Suscripciones push |

## 7. Gestión del esquema con Liquibase

El esquema se construye exclusivamente mediante *changesets* versionados. La cadena maestra es `db/changelog/db.changelog-master.xml`, que incluye siete archivos, uno por contexto, **en orden de dependencia**.

```
db.changelog-master.xml
├── security/security-changelog.xml               (24 changesets)
├── home/home-changelog.xml                       (6)
├── device/device-changelog.xml                   (6)
├── measurement/measurement-changelog.xml         (3)
├── alert/alert-changelog.xml                     (3)
├── recommendation/recommendation-changelog.xml   (1)
└── notification/notification-changelog.xml       (3)
```

**Convención de nombres:** `{módulo}-{NNN}-{descripción}.xml`, almacenados en la subcarpeta `changes/`.

**Cambiosets de Siemens (semilla de datos).** Tres *changesets* siembran catálogos y están declarados con `preConditions/tableIsEmpty` y bloque `<rollback>` para que sean reversibles si la tabla está vacía:

| **Cambioset** | **Siembra** |
|---|---|
| `security-019-seed-default-password-policy` | Política de contraseñas por defecto (mín. 8, máx. 64, mayúsculas, números, símbolos, 90 días) |
| `home-005-seed-home-type` | Tipos de hogar (casa, apartamento, oficina) |
| `device-006-seed-appliance-types` | Tipos de electrodoméstico (nevera, lavadora, televisor, microondas, aire acondicionado, equipo de cómputo, calentador de agua, iluminación, otros) |
| `measurement-003-seed-consumption-level` | Niveles de riesgo con sus rangos |

**Regla de mantenimiento:** los cambiosets ya aplicados **nunca se editan**. Toda modificación del esquema se realiza mediante un *changeset* nuevo con identificador secuencial.

## 8. Reglas de integridad

| **Categoría** | **Regla** | **Mecanismo de aplicación** |
|---|---|---|
| Unicidad de correo | `user.email` es `UNIQUE` | `409 Conflict` — `EmailAlreadyRegisteredException` |
| Unicidad de código de acceso | `home.access_code` es `UNIQUE` | `409 Conflict` — `HomeAccessCodeCollisionException` |
| Unicidad de código de dispositivo | `device.device_code` es `UNIQUE` | Validación en el módulo `device` |
| Unicidad de hash de token | `refresh_token.token_hash` es `UNIQUE` | Detección de reutilización |
| Unicidad de endpoint push | `push_subscription.endpoint` es `UNIQUE` | Re-suscripción idempotente |
| Exclusividad de preferencias | `notification_preference.user_id` **es** la PK | Garantía por modelo |
| Exclusividad de avatar | `user.profile_image` pertenece a la cuenta, no a la persona | `person` no almacena imagen |
| Exclusividad de dirección | `home.address` es la copia autorizada | `person` no almacena dirección |
| Umbral positivo | `daily_limit` y `monthly_limit` son `DOUBLE NOT NULL` | `@PositiveOrZero` en el DTO |
| Consistencia de umbrales | El límite del periodo activo debe ser positivo y coherente | Validación en `UpdateHomeThresholds` |
| Periodo de umbral | `limit_period ∈ {DAILY, MONTHLY}` | `ENUM` de MySQL |
| Token de un solo uso | `password_reset_token.used` | `markAsUsed()` en la entidad de dominio |
| Expiración de token | `expiration_at` | `isExpired()` en la entidad de dominio |
| Límite de intentos | `password_reset_token.attempts` y limitador en memoria (10 por IP / 15 min) | `429` con `Retry-After: 900` |
| Niveles sin traslape | Los rangos `[min_limit, max_limit]` de `consumption_level` no se traslapan | Validación en el módulo `measurement` |
| Pertenencia a un hogar | Un usuario sólo accede a los hogares donde figura en `user_home` | `HomeApi` + `CurrentUserResolver` |
| Autorización de lectura de un dispositivo | El usuario debe ser miembro del hogar al que pertenece el dispositivo | `HomeConsumptionUseCase.requireReadable(...)` |
| Último propietario | El último `OWNER` de un hogar no puede salir; antes debe transferir la propiedad o eliminar el hogar | `409 Conflict` — `LastOwnerCannotLeaveException` |
| Un único propietario | Un hogar siempre tiene exactamente un `OWNER`; la transferencia de propiedad intercambia los roles en un solo paso (`HOME-INV-001`, `HOME-INV-015`) con bloqueo de fila | `PUT /api/v1/homes/{homeId}/owner`: `409` si el destino ya es `OWNER` o es quien llama |
| Eliminación de hogar | Solo el `OWNER` puede eliminar el hogar; la eliminación lógica (`deleted_at`) alcanza al hogar, a sus filas de `user_home` y a `home_thresholds`, y desvincula los dispositivos | `DELETE /api/v1/homes/{homeId}`: `403` para un `MEMBER` |
| Apropiación de dispositivo | Sólo un `OWNER` puede vincular, editar, reemitir credenciales o desvincular | `NotHomeOwnerException` |
| Consulta en tiempo de lectura | La IP se toma de `getRemoteAddr()`; se ignora `X-Forwarded-For` | `clientIp(HttpServletRequest)` |

## 9. Diferencias entre el artefacto de diseño y el esquema vigente

El diagrama `../diagrams/er-model.wsd` entregado como artefacto de diseño del punto 03 fue elaborado en una fase anterior. La tabla siguiente registra la diferencia para que ambos documentos puedan coexistir sin contradicción.

| **#** | **Aspecto** | **MER del artefacto** | **Esquema vigente** | **Origen del cambio** |
|---|---|---|---|---|
| 1 | Número de tablas | 27 | 29 | `notification-002`, `notification-003` |
| 2 | `notification_preference` | No existe | Preferencias de canal, PK = `user_id` | Necesidad de separar preferencias de `user_configuration` |
| 3 | `push_subscription` | No existe | Suscripciones VAPID por navegador | Notificaciones push web (Web Push) |
| 4 | `alert.type` | `THRESHOLD`, `CONNECTIVITY` | + `DEVICE`, `LIMIT` | `alert-002`, `alert-003` |
| 5 | `notification.source_type` | `ALERT`, `RECOMMENDATION` | + `SECURITY` | Correos de verificación y recuperación |
| 6 | `home_thresholds` | 3 columnas de límite | + `limit_period ENUM('DAILY','MONTHLY')` | `home-006` — edición por periodo |
| 7 | `user` | — | + `google_subject` | `security-024` — acceso con Google |
| 8 | `password_reset_token` | `reset_token` | `reset_token_hash` + `attempts` | `security-021` — nunca almacenar el secreto en claro |
| 9 | `user.profile_image` | `VARCHAR(255)` | `MEDIUMTEXT` | `security-022` — avatar como data URL |
| 10 | `person.last_name` | `NOT NULL` | Opcional | `security-023` — usuarios con un solo nombre |
| 11 | `person` | Con `address` y `cellphone` | Ambos eliminados | `security-018`, `security-020` |
| 12 | Default de umbrales | Sin definir | `HomeProperties` (6,0 / 180,0) | Cierra el hueco reportado en la versión anterior |
| 13 | Ingesta de mediciones | Endpoint `POST /measurements` | MQTT (`energy-monitor/devices/+/telemetry`) | Sustituye la escritura por HTTP |

**Recomendación:** actualizar `MER.wsd` en el repositorio de documentación para incorporar las filas 1 a 13 antes de la entrega final.

## 10. Decisiones de diseño pendientes

| **#** | **Hallazgo** | **Impacto** | **Decisión propuesta** |
|---|---|---|---|
| 1 | `notification.source_id` no tiene llave foránea por diseño (referencia polimórfica). | Una alerta o recomendación eliminada lógicamente deja notificaciones huérfanas. | Aceptar el comportamiento: el borrado lógico de la fuente conserva la trazabilidad del aviso. |
| 2 | Una medición puede generar varias alertas del mismo tipo si cruza más de un umbral. | Alertas duplicadas en la bandeja. | Definir la regla de deduplicación por `(home_id, type, measurement_id, date_time)` en `RegisterThresholdAlertService`. |
| 3 | Los timestamps se sellan de tres maneras distintas (`@PrePersist`, `@CreationTimestamp`, `LocalDateTime`). | Ambigüedad al construir evidencia de auditoría. | Unificar en `Instant` + `@PrePersist` en los siete módulos. |
| 4 | `security_configuration` es un par genérico nombre/valor sin datos sembrados. | No hay parametrización efectiva de la seguridad desde la base de datos. | Sembrar un *changeset* con los parámetros vigentes (TTL del token, longitud de la clave de acceso, límite de intentos) o eliminar la tabla. |
| 5 | `person` no tiene medio de contacto distinto al correo. | Si la especificación de requisitos exigiera avisoSMS o teléfono, el modelo no lo soporta. | Confirmar con el equipo de requisitos antes de la entrega. |

## 11. Trazabilidad con los requisitos

| **Requisito (especificación de requisitos)** | **Tablas que lo soportan** | **Mecanismo de diseño** |
|---|---|---|
| RF-SEC-01 a RF-SEC-12 Gestión de usuarios y perfiles | `person`, `user`, `user_configuration`, `password_reset_token`, `user_session`, `refresh_token`, `audit_log` | Separación persona/cuenta; verificación por código; rotación de tokens; `google_subject` |
| RF-HOM-01 a RF-HOM-09 Gestión de hogares | `home`, `home_type`, `user_home`, `device`, `device_home` | `access_code` como mecanismo de vinculación; doble rol RBAC + rol por hogar |
| RF-HOM-08, RF-NOT-03, RF-CLI-05, RF-CLI-06 Configuración | `home_thresholds`, `user_configuration`, `notification_preference`, `security_configuration`, `password_policy` | Separación de preferencias de usuario frente a límites de negocio |
| RF-MED, RF-ALE, RF-REC, RF-NOT Consumo, alertas y recomendaciones | `measurement`, `consumption_level`, `alert`, `recommendation`, `notification` | Eventos separados por contexto; niveles de riesgo como datos |
| RF-DEV-01 a RF-DEV-09; RF-MED-04 a RF-MED-06 Dispositivo | `device`, `device_code`, `api_key`, `device_status_log` | Autenticación por `api_key`; bitácora de conectividad |

## 12. Artefactos relacionados

| **Artefacto** | **Ubicación** |
|---|---|
| MER base (PlantUML) | `../diagrams/er-model.wsd` |
| Diagrama de clases (PlantUML) | `../diagrams/class-diagram.wsd` — documentado en `05.3-modelo-de-clases.md` |
| Cambiosets de Liquibase | `energy-monitor-backend/src/main/resources/db/changelog/` |
| Diccionario de datos completo | `06-base-datos/diccionario-datos/` |
| Scripts de base de datos | `06-base-datos/script-DB/` |