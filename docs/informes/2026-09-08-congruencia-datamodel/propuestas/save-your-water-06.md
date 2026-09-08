# 06 - Data Model (counter-proposal)

> Project: **save-your-water** - repository `sy-water-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Real: 29 KB, 18 tablas en 6 esquemas, con principios de modelado declarados.

**Normalization level found:** 3FN

**02 -> 06 congruence:** 100% of the declared domain entities exist as tables.

El modelo mejor estructurado despues de rent-car, y el unico que declara sus principios de modelado por escrito. Su unica falla es de auditoria: 40 tablas con `deleted_at` y ninguna con `created_by`.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Auditoria** | El modelo tiene `created_at`, `updated_at` y `deleted_at` en 40 tablas, pero CERO `created_by` / `updated_by`: se sabe cuando cambio algo y no quien. Se completan las seis columnas. |
| **3FN** | `place` guarda ciudad y estrato como texto; salen a los catalogos `city` y `socioeconomic_stratum`. |
| **1FN** | Se separa la lectura cruda del agregado: `device_reading` es serie temporal y `consumption_record` es el consolidado por periodo. Nunca la misma tabla. |
| **2FN** | La tarifa se referencia por FK con vigencia en vez de copiar el precio unitario en cada factura. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity` | Cuentas, sesiones e intentos de acceso. Ya modelado por el equipo. |
| `Place` | Vivienda o predio monitoreado y sus miembros. |
| `Device` | Medidor instalado y sus lecturas crudas. |
| `Consumption` | Consumo agregado, metas, tarifas y facturas comparadas. |
| `Valve` | Valvula, acciones de corte y reglas automaticas. |
| `Notification` | Alertas de fuga y preferencias de aviso. |

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

### Schema `place`

#### `place.place`

Predio monitoreado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `place_id` | `BIGSERIAL` | PK |  |
| `owner_user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `alias` | `VARCHAR(80)` | NOT NULL |  |
| `address_line` | `VARCHAR(200)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |
| `stratum_id` | `SMALLINT` | NULL REFERENCES socioeconomic_stratum(stratum_id) |  |
| `resident_count` | `SMALLINT` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: ciudad y estrato catalogados; antes eran VARCHAR repetidos en cada predio.

#### `place.place_member`

Miembros del hogar con acceso al predio.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `place_id` | `BIGINT` | NOT NULL REFERENCES place(place_id) | PK compuesta |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) | PK compuesta |
| `member_role_id` | `SMALLINT` | NOT NULL REFERENCES member_role(member_role_id) |  |
| `joined_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |

*Plus the full audit block from section 4.*

> **Normalization:** Resuelve la N:M usuario-predio.

### Schema `device`

#### `device.device`

Medidor instalado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `device_id` | `BIGSERIAL` | PK |  |
| `place_id` | `BIGINT` | NOT NULL REFERENCES place(place_id) |  |
| `serial_number` | `VARCHAR(60)` | NOT NULL UNIQUE |  |
| `device_model_id` | `SMALLINT` | NOT NULL REFERENCES device_model(device_model_id) |  |
| `installed_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `device_status_id` | `SMALLINT` | NOT NULL REFERENCES device_status(device_status_id) |  |
| `last_seen_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: fabricante y firmware pertenecen al modelo del dispositivo, no a la unidad instalada.

#### `device.device_reading`

Lectura cruda del medidor. Serie temporal.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `device_reading_id` | `BIGSERIAL` | PK |  |
| `device_id` | `BIGINT` | NOT NULL REFERENCES device(device_id) |  |
| `liters` | `NUMERIC(12,3)` | NOT NULL |  |
| `read_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `quality_flag_id` | `SMALLINT` | NOT NULL REFERENCES quality_flag(quality_flag_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Inmutable: solo `created_at`. Una lectura corregida genera fila nueva, nunca un UPDATE.

### Schema `consumption`

#### `consumption.consumption_record`

Consumo agregado por periodo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `consumption_record_id` | `BIGSERIAL` | PK |  |
| `place_id` | `BIGINT` | NOT NULL REFERENCES place(place_id) |  |
| `period_start` | `DATE` | NOT NULL |  |
| `period_end` | `DATE` | NOT NULL |  |
| `liters` | `NUMERIC(14,3)` | NOT NULL |  |
| `aggregation_level_id` | `SMALLINT` | NOT NULL REFERENCES aggregation_level(aggregation_level_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Derivado de `device_reading`: se materializa por rendimiento y queda documentado como recalculable.

#### `consumption.tariff`

Tarifa vigente por estrato y periodo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `tariff_id` | `SERIAL` | PK |  |
| `stratum_id` | `SMALLINT` | NOT NULL REFERENCES socioeconomic_stratum(stratum_id) |  |
| `price_per_liter` | `NUMERIC(12,6)` | NOT NULL |  |
| `valid_from` | `DATE` | NOT NULL |  |
| `valid_to` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: la factura referencia la tarifa por FK; no copia el precio unitario.

#### `consumption.goal`

Meta de ahorro fijada por el hogar.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `goal_id` | `BIGSERIAL` | PK |  |
| `place_id` | `BIGINT` | NOT NULL REFERENCES place(place_id) |  |
| `target_liters` | `NUMERIC(14,3)` | NOT NULL |  |
| `period_start` | `DATE` | NOT NULL |  |
| `period_end` | `DATE` | NOT NULL |  |

*Plus the full audit block from section 4.*

### Schema `valve`

#### `valve.valve`

Valvula asociada a un medidor.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `valve_id` | `BIGSERIAL` | PK |  |
| `device_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES device(device_id) |  |
| `valve_state_id` | `SMALLINT` | NOT NULL REFERENCES valve_state(valve_state_id) |  |
| `last_changed_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

#### `valve.valve_action`

Orden de apertura o cierre.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `valve_action_id` | `BIGSERIAL` | PK |  |
| `valve_id` | `BIGINT` | NOT NULL REFERENCES valve(valve_id) |  |
| `action_type_id` | `SMALLINT` | NOT NULL REFERENCES valve_action_type(action_type_id) |  |
| `requested_by` | `BIGINT` | NULL REFERENCES app_user(user_id) | NULL cuando la dispara una regla |
| `shutoff_rule_id` | `BIGINT` | NULL REFERENCES shutoff_rule(shutoff_rule_id) |  |
| `requested_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `executed_at` | `TIMESTAMPTZ` | NULL |  |
| `action_status_id` | `SMALLINT` | NOT NULL REFERENCES action_status(action_status_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Trazabilidad: toda accion tiene origen humano o regla. CHECK que impide ambos nulos.

#### `valve.shutoff_rule`

Regla de corte automatico.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `shutoff_rule_id` | `BIGSERIAL` | PK |  |
| `place_id` | `BIGINT` | NOT NULL REFERENCES place(place_id) |  |
| `threshold_liters_per_hour` | `NUMERIC(12,3)` | NOT NULL |  |
| `is_enabled` | `BOOLEAN` | NOT NULL DEFAULT true |  |

*Plus the full audit block from section 4.*

### Schema `notification`

#### `notification.alert`

Alerta por fuga o umbral superado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `alert_id` | `BIGSERIAL` | PK |  |
| `place_id` | `BIGINT` | NOT NULL REFERENCES place(place_id) |  |
| `alert_type_id` | `SMALLINT` | NOT NULL REFERENCES alert_type(alert_type_id) |  |
| `severity_id` | `SMALLINT` | NOT NULL REFERENCES severity(severity_id) |  |
| `raised_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `acknowledged_at` | `TIMESTAMPTZ` | NULL |  |
| `acknowledged_by` | `BIGINT` | NULL REFERENCES app_user(user_id) |  |

*Plus the full audit block from section 4.*

#### `notification.notification_preference`

Preferencia de canal por usuario y tipo de alerta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) | PK compuesta |
| `alert_type_id` | `SMALLINT` | NOT NULL REFERENCES alert_type(alert_type_id) | PK compuesta |
| `channel_id` | `SMALLINT` | NOT NULL REFERENCES notification_channel(channel_id) | PK compuesta |
| `is_enabled` | `BOOLEAN` | NOT NULL DEFAULT true |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un canal por fila; nunca un campo `channels` con valores separados por comas.

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `socioeconomic_stratum`
- `member_role`
- `device_model`
- `device_status`
- `quality_flag`
- `aggregation_level`
- `valve_state`
- `valve_action_type`
- `action_status`
- `alert_type`
- `severity`
- `notification_channel`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Monitoreo del consumo de agua en hogares, con deteccion de fugas y corte de valvula.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*