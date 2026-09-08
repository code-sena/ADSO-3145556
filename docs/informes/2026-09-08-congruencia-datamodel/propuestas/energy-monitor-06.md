# 06 - Data Model (counter-proposal)

> Project: **energy-monitor** - repository `en-monitor-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Plantilla intacta.

**Normalization level found:** Sin modelo

**02 -> 06 congruence:** 0% of the declared domain entities exist as tables.

El menos avanzado. Lo poco que hay esta bien pensado: los seis bounded contexts del mapa son correctos y sirven de base directa para el modelo. Falta bajar de contextos a entidades.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia critica** | Es el proyecto menos avanzado (9%). `02-domain/domain-map.md` define seis bounded contexts con criterio, pero `entities-and-rules.md` es la plantilla y no hay ninguna tabla. |
| **Origen del modelo** | Los seis dominios de esta propuesta son literalmente los seis bounded contexts que el equipo ya identifico en su mapa de dominio. No se agrega ni se quita alcance. |
| **1FN** | La medicion es una fila por dispositivo e instante, nunca columnas por electrodomestico. |
| **3FN** | La potencia nominal pertenece al modelo del electrodomestico, no a cada medicion; y la tarifa se referencia con vigencia en vez de copiarse. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Security` | Usuario, roles, permisos y sesiones. |
| `Home Management` | Vivienda, ambientes y miembros del hogar. |
| `Devices` | Dispositivo o electrodomestico monitoreado. |
| `Monitoring & Measurements` | Medicion de consumo en el tiempo. |
| `Alerts` | Alertas por consumo anomalo o umbral. |
| `Recommendations` | Recomendaciones de ahorro generadas y su seguimiento. |

## 4. Modeling standard

Applies to every table in this document.

**Language.** All schemas, tables, columns and catalog values are written in English. 
Documentation prose may stay in Spanish; the model may not.

**Naming.** `snake_case`; tables singular; primary key `<table>_id`; foreign key named after the referenced key.

**Normalization floor: 3NF.**

- **1NF** - atomic columns only. No comma separated lists, no repeating groups, no free-text fields 
holding structured data. A repeating group becomes a child table.
- **2NF** - on composite keys, every non-key attribute depends on the *whole* key. Attributes that 
depend on part of it move to the table that owns that part.
- **3NF** - no transitive dependencies. Descriptive attributes reachable through a foreign key are 
never copied. Statuses, types and categories live in catalog tables, never as free text.
- **Derived values are not stored**, with two documented exceptions: legally frozen amounts 
(an issued invoice, a purchased ticket) and aggregates materialized for performance, which are 
explicitly marked as recomputable.

**Audit block.** Every business table carries these columns:

```sql
created_at    TIMESTAMPTZ   NOT NULL DEFAULT now()   -- Alta del registro
created_by    BIGINT        NULL REFERENCES app_user(user_id)   -- Autor del alta
updated_at    TIMESTAMPTZ   NULL   -- Ultima modificacion
updated_by    BIGINT        NULL REFERENCES app_user(user_id)   -- Autor de la modificacion
deleted_at    TIMESTAMPTZ   NULL   -- Baja logica; NULL = vigente
deleted_by    BIGINT        NULL REFERENCES app_user(user_id)   -- Autor de la baja
row_version   INTEGER       NOT NULL DEFAULT 1   -- Bloqueo optimista
```

High-volume immutable tables (time series, logs, consent records) carry only `created_at` and 
`created_by`: they are never updated or soft-deleted. This is stated per table below.

A row-level audit block answers *when and who*. The `audit_log` table answers *what changed*. 
Both are required; neither replaces the other.

## 5. Shared core (identical across the ficha)

### `identity.app_user`

Cuenta de acceso. Separada de la persona para que un mismo individuo pueda tener o no credenciales.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_id` | `BIGSERIAL` | PK |  |
| `person_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES person(person_id) | 1:1 con la persona |
| `username` | `VARCHAR(50)` | NOT NULL UNIQUE |  |
| `password_hash` | `VARCHAR(255)` | NULL | NULL cuando el proveedor es externo |
| `auth_provider_id` | `SMALLINT` | NOT NULL REFERENCES auth_provider(auth_provider_id) | LOCAL / GOOGLE / ... |
| `external_subject` | `VARCHAR(255)` | NULL | Identificador en el proveedor externo |
| `user_status_id` | `SMALLINT` | NOT NULL REFERENCES user_status(user_status_id) | Estado catalogado, no texto libre |
| `last_login_at` | `TIMESTAMPTZ` | NULL |  |

> **Normalization:** 3FN: `user_status` y `auth_provider` salen a catalogo para eliminar la dependencia transitiva de la descripcion del estado respecto del usuario.

### `identity.person`

Datos naturales de la persona, independientes de si tiene cuenta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `person_id` | `BIGSERIAL` | PK |  |
| `document_type_id` | `SMALLINT` | NOT NULL REFERENCES document_type(document_type_id) |  |
| `document_number` | `VARCHAR(30)` | NOT NULL | UNIQUE junto a document_type_id |
| `first_name` | `VARCHAR(80)` | NOT NULL |  |
| `last_name` | `VARCHAR(80)` | NOT NULL |  |
| `email` | `VARCHAR(150)` | NOT NULL UNIQUE |  |
| `phone` | `VARCHAR(25)` | NULL |  |
| `birth_date` | `DATE` | NULL | La edad NO se almacena: es derivable |

> **Normalization:** 1FN: nunca un campo `full_name`; nombre y apellido separados. 3FN: la edad se calcula, no se guarda.

### `identity.role`

Rol funcional.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `role_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(40)` | NOT NULL UNIQUE | ADMIN, CLIENT, ... |
| `name` | `VARCHAR(80)` | NOT NULL |  |
| `description` | `VARCHAR(255)` | NULL |  |

### `identity.permission`

Permiso atomico.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `permission_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(60)` | NOT NULL UNIQUE | recurso:accion |
| `description` | `VARCHAR(255)` | NULL |  |

### `identity.role_permission`

Resuelve la N:M entre rol y permiso.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `role_id` | `SMALLINT` | NOT NULL REFERENCES role(role_id) | PK compuesta |
| `permission_id` | `SMALLINT` | NOT NULL REFERENCES permission(permission_id) | PK compuesta |

> **Normalization:** 1FN: sin esta tabla los permisos terminarian como lista separada por comas dentro de `role`.

### `identity.user_role`

Resuelve la N:M entre usuario y rol.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) | PK compuesta |
| `role_id` | `SMALLINT` | NOT NULL REFERENCES role(role_id) | PK compuesta |
| `granted_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |

### `identity.user_session`

Sesion emitida.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `session_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `refresh_token_hash` | `VARCHAR(255)` | NOT NULL | Nunca el token en claro |
| `issued_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `expires_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `revoked_at` | `TIMESTAMPTZ` | NULL |  |
| `ip_address` | `INET` | NULL |  |
| `user_agent` | `VARCHAR(255)` | NULL |  |

### `audit.audit_log`

Bitacora de cambios. Complementa, no reemplaza, las columnas de auditoria por tabla.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `audit_log_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NULL REFERENCES app_user(user_id) | NULL en acciones del sistema |
| `entity_name` | `VARCHAR(60)` | NOT NULL | Tabla afectada |
| `entity_id` | `VARCHAR(60)` | NOT NULL | PK afectada, como texto |
| `action_id` | `SMALLINT` | NOT NULL REFERENCES audit_action(action_id) | CREATE/UPDATE/DELETE/LOGIN |
| `occurred_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `old_value` | `JSONB` | NULL | Snapshot previo |
| `new_value` | `JSONB` | NULL | Snapshot posterior |
| `ip_address` | `INET` | NULL |  |

> **Normalization:** El JSONB aqui es deliberado y no viola 1FN: es una bitacora de snapshots, no una entidad consultable por atributo.

**Shared catalogs:** `auth_provider`, `user_status`, `document_type`, `audit_action`.
Every catalog has `(<name>_id, code, name, description, is_active)` plus the audit block.

## 6. Domain model

### Schema `home`

#### `home.home`

Vivienda monitoreada.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `home_id` | `BIGSERIAL` | PK |  |
| `owner_user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `alias` | `VARCHAR(80)` | NOT NULL |  |
| `address_line` | `VARCHAR(200)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |
| `stratum_id` | `SMALLINT` | NULL REFERENCES socioeconomic_stratum(stratum_id) |  |

*Plus the full audit block from section 4.*

#### `home.room`

Ambiente dentro de la vivienda.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `room_id` | `BIGSERIAL` | PK |  |
| `home_id` | `BIGINT` | NOT NULL REFERENCES home(home_id) |  |
| `name` | `VARCHAR(80)` | NOT NULL |  |
| `room_type_id` | `SMALLINT` | NOT NULL REFERENCES room_type(room_type_id) |  |

*Plus the full audit block from section 4.*

#### `home.home_member`

Miembro del hogar con acceso.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `home_id` | `BIGINT` | NOT NULL REFERENCES home(home_id) | PK compuesta |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) | PK compuesta |
| `member_role_id` | `SMALLINT` | NOT NULL REFERENCES member_role(member_role_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Resuelve la N:M usuario-vivienda.

### Schema `device`

#### `device.appliance_model`

Modelo de electrodomestico.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `appliance_model_id` | `SERIAL` | PK |  |
| `brand` | `VARCHAR(60)` | NOT NULL |  |
| `model_name` | `VARCHAR(100)` | NOT NULL |  |
| `appliance_category_id` | `SMALLINT` | NOT NULL REFERENCES appliance_category(appliance_category_id) |  |
| `rated_power_w` | `INTEGER` | NOT NULL |  |
| `energy_rating` | `VARCHAR(5)` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: la potencia nominal es del modelo. Guardarla en cada dispositivo la repetiria en toda la flota.

#### `device.device`

Dispositivo monitoreado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `device_id` | `BIGSERIAL` | PK |  |
| `room_id` | `BIGINT` | NOT NULL REFERENCES room(room_id) |  |
| `appliance_model_id` | `INTEGER` | NULL REFERENCES appliance_model(appliance_model_id) |  |
| `alias` | `VARCHAR(80)` | NOT NULL |  |
| `serial_number` | `VARCHAR(60)` | NULL UNIQUE |  |
| `device_status_id` | `SMALLINT` | NOT NULL REFERENCES device_status(device_status_id) |  |
| `installed_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

### Schema `monitoring`

#### `monitoring.energy_measurement`

Medicion de consumo. Serie temporal particionada por fecha.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `energy_measurement_id` | `BIGSERIAL` | PK |  |
| `device_id` | `BIGINT` | NOT NULL REFERENCES device(device_id) |  |
| `watt_hours` | `NUMERIC(12,3)` | NOT NULL |  |
| `measured_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `quality_flag_id` | `SMALLINT` | NOT NULL REFERENCES quality_flag(quality_flag_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: una fila por dispositivo e instante. Nunca una columna por electrodomestico, que obligaria a alterar la tabla al conectar uno nuevo. Inmutable: solo `created_at`.

#### `monitoring.consumption_summary`

Consumo agregado por vivienda y periodo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `consumption_summary_id` | `BIGSERIAL` | PK |  |
| `home_id` | `BIGINT` | NOT NULL REFERENCES home(home_id) |  |
| `period_start` | `DATE` | NOT NULL |  |
| `period_end` | `DATE` | NOT NULL |  |
| `total_watt_hours` | `NUMERIC(14,3)` | NOT NULL |  |
| `aggregation_level_id` | `SMALLINT` | NOT NULL REFERENCES aggregation_level(aggregation_level_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Derivado y recalculable; se materializa solo por rendimiento.

#### `monitoring.energy_tariff`

Tarifa vigente por estrato.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `energy_tariff_id` | `SERIAL` | PK |  |
| `stratum_id` | `SMALLINT` | NOT NULL REFERENCES socioeconomic_stratum(stratum_id) |  |
| `price_per_kwh` | `NUMERIC(12,6)` | NOT NULL |  |
| `valid_from` | `DATE` | NOT NULL |  |
| `valid_to` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: el costo se calcula uniendo consumo y tarifa vigente; no se copia el precio en cada resumen.

### Schema `alert`

#### `alert.consumption_threshold`

Umbral de consumo definido por el hogar.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `consumption_threshold_id` | `BIGSERIAL` | PK |  |
| `home_id` | `BIGINT` | NOT NULL REFERENCES home(home_id) |  |
| `device_id` | `BIGINT` | NULL REFERENCES device(device_id) | NULL = umbral de toda la vivienda |
| `threshold_watt_hours` | `NUMERIC(14,3)` | NOT NULL |  |
| `period_type_id` | `SMALLINT` | NOT NULL REFERENCES period_type(period_type_id) |  |
| `is_enabled` | `BOOLEAN` | NOT NULL DEFAULT true |  |

*Plus the full audit block from section 4.*

#### `alert.energy_alert`

Alerta por umbral superado o consumo anomalo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `energy_alert_id` | `BIGSERIAL` | PK |  |
| `home_id` | `BIGINT` | NOT NULL REFERENCES home(home_id) |  |
| `device_id` | `BIGINT` | NULL REFERENCES device(device_id) |  |
| `consumption_threshold_id` | `BIGINT` | NULL REFERENCES consumption_threshold(consumption_threshold_id) |  |
| `alert_type_id` | `SMALLINT` | NOT NULL REFERENCES alert_type(alert_type_id) |  |
| `severity_id` | `SMALLINT` | NOT NULL REFERENCES severity(severity_id) |  |
| `raised_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `acknowledged_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

### Schema `recommendation`

#### `recommendation.recommendation_rule`

Regla que genera una recomendacion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `recommendation_rule_id` | `SERIAL` | PK |  |
| `code` | `VARCHAR(40)` | NOT NULL UNIQUE |  |
| `title` | `VARCHAR(150)` | NOT NULL |  |
| `body_template` | `TEXT` | NOT NULL |  |
| `appliance_category_id` | `SMALLINT` | NULL REFERENCES appliance_category(appliance_category_id) |  |
| `is_active` | `BOOLEAN` | NOT NULL DEFAULT true |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: el texto de la recomendacion vive una sola vez en la regla, no copiado en cada emision.

#### `recommendation.recommendation`

Recomendacion emitida a un hogar.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `recommendation_id` | `BIGSERIAL` | PK |  |
| `home_id` | `BIGINT` | NOT NULL REFERENCES home(home_id) |  |
| `recommendation_rule_id` | `INTEGER` | NOT NULL REFERENCES recommendation_rule(recommendation_rule_id) |  |
| `device_id` | `BIGINT` | NULL REFERENCES device(device_id) |  |
| `estimated_saving_kwh` | `NUMERIC(10,3)` | NULL |  |
| `issued_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `recommendation_status_id` | `SMALLINT` | NOT NULL REFERENCES recommendation_status(recommendation_status_id) | ISSUED / APPLIED / DISMISSED |

*Plus the full audit block from section 4.*

> **Normalization:** Permite medir si la recomendacion sirvio, que es el objetivo del bounded context.

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `socioeconomic_stratum`
- `room_type`
- `member_role`
- `appliance_category`
- `device_status`
- `quality_flag`
- `aggregation_level`
- `period_type`
- `alert_type`
- `severity`
- `recommendation_status`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Monitoreo del consumo energetico del hogar, con alertas y recomendaciones de ahorro.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*