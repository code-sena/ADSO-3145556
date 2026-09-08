# 06 - Data Model (counter-proposal)

> Project: **woman-alert** - repository `wal-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Plantilla intacta.

**Normalization level found:** Sin modelo

**02 -> 06 congruence:** 0% of the declared domain entities exist as tables.

Segundo dominio mejor estructurado de la ficha, con separacion limpia por contexto, y cero traduccion a datos. Es tambien el proyecto con la informacion mas sensible, lo que hace la ausencia de modelo mas costosa.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia critica** | 26 entidades declaradas en 7 bounded contexts, con 25 KB de documentacion, y **cero tablas**. Es el dominio mejor estructurado de la ficha sin ninguna traduccion a datos. |
| **Privacidad** | Es el proyecto con el dato mas sensible de la ficha. El rastro de ubicacion se separa de la alerta y recibe politica de retencion explicita mediante `retention_expires_at`. |
| **1FN** | `AlertContact` existe porque una alerta notifica a varios contactos: nunca una lista dentro de `alert`. |
| **3FN** | El estado de la alerta sale a catalogo; el nombre y telefono del contacto NO se copian en `alert_contact`, se alcanzan por FK. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity & Accounts` | Usuario, cuenta, perfil, recuperacion, dispositivos y preferencias. |
| `Emergency Contacts` | Red de contactos de confianza. |
| `Alert Management` | Alerta activa, contactos notificados, rastro de ubicacion y recordatorios. |
| `Evidence` | Audio, foto o video capturado durante la alerta. |
| `Notification` | Avisos a contactos y autoridades. |
| `Zones & Resources` | Zonas de riesgo reportadas y recursos de ayuda. |
| `Administration & Moderation` | Reportes de usuario, acciones de moderacion y parametros. |

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

### Schema `identity`

#### `identity.user_profile`

Perfil extendido de la usuaria.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_profile_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES app_user(user_id) |  |
| `display_name` | `VARCHAR(80)` | NOT NULL |  |
| `blood_type_id` | `SMALLINT` | NULL REFERENCES blood_type(blood_type_id) |  |
| `medical_notes` | `TEXT` | NULL |  |

*Plus the full audit block from section 4.*

#### `identity.user_device`

Dispositivo desde el que se puede disparar la alerta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_device_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `device_fingerprint` | `VARCHAR(120)` | NOT NULL |  |
| `platform_id` | `SMALLINT` | NOT NULL REFERENCES platform(platform_id) |  |
| `push_token` | `VARCHAR(255)` | NULL |  |
| `is_trusted` | `BOOLEAN` | NOT NULL DEFAULT false |  |

*Plus the full audit block from section 4.*

#### `identity.alert_activation_setting`

Como se activa la alerta en cada dispositivo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `alert_activation_setting_id` | `BIGSERIAL` | PK |  |
| `user_device_id` | `BIGINT` | NOT NULL REFERENCES user_device(user_device_id) |  |
| `activation_method_id` | `SMALLINT` | NOT NULL REFERENCES activation_method(activation_method_id) | SHAKE / BUTTON / VOICE |
| `is_enabled` | `BOOLEAN` | NOT NULL DEFAULT true |  |
| `sensitivity` | `SMALLINT` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un metodo por fila; varios metodos activos conviven sin campos multivaluados.

### Schema `contacts`

#### `contacts.emergency_contact`

Contacto de confianza.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `emergency_contact_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `full_name` | `VARCHAR(120)` | NOT NULL | Contacto externo, sin cuenta en el sistema |
| `phone` | `VARCHAR(25)` | NOT NULL |  |
| `email` | `VARCHAR(150)` | NULL |  |
| `relationship_id` | `SMALLINT` | NOT NULL REFERENCES relationship_type(relationship_id) |  |
| `priority_order` | `SMALLINT` | NOT NULL |  |
| `is_verified` | `BOOLEAN` | NOT NULL DEFAULT false |  |

*Plus the full audit block from section 4.*

> **Normalization:** Excepcion justificada: aqui `full_name` es aceptable porque el contacto es una persona externa sin registro propio; no hay `person` de la cual derivarlo.

### Schema `alert`

#### `alert.alert`

Alerta disparada. Raiz de agregado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `alert_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `user_device_id` | `BIGINT` | NULL REFERENCES user_device(user_device_id) |  |
| `activation_method_id` | `SMALLINT` | NOT NULL REFERENCES activation_method(activation_method_id) |  |
| `alert_status_id` | `SMALLINT` | NOT NULL REFERENCES alert_status(alert_status_id) |  |
| `triggered_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `resolved_at` | `TIMESTAMPTZ` | NULL |  |
| `resolution_id` | `SMALLINT` | NULL REFERENCES alert_resolution(resolution_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: el estado y la resolucion salen a catalogo, no como texto libre.

#### `alert.alert_contact`

Contacto notificado en una alerta concreta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `alert_contact_id` | `BIGSERIAL` | PK |  |
| `alert_id` | `BIGINT` | NOT NULL REFERENCES alert(alert_id) |  |
| `emergency_contact_id` | `BIGINT` | NOT NULL REFERENCES emergency_contact(emergency_contact_id) |  |
| `notified_at` | `TIMESTAMPTZ` | NULL |  |
| `acknowledged_at` | `TIMESTAMPTZ` | NULL |  |
| `delivery_status_id` | `SMALLINT` | NOT NULL REFERENCES delivery_status(delivery_status_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: una fila por contacto notificado. 3FN: no copia nombre ni telefono; los alcanza por FK.

#### `alert.location_log`

Rastro de ubicacion durante la alerta. Dato altamente sensible.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `location_log_id` | `BIGSERIAL` | PK |  |
| `alert_id` | `BIGINT` | NOT NULL REFERENCES alert(alert_id) |  |
| `latitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `longitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `accuracy_m` | `NUMERIC(7,2)` | NULL |  |
| `recorded_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `retention_expires_at` | `TIMESTAMPTZ` | NOT NULL | Purga automatica |

*Plus the full audit block from section 4.*

> **Normalization:** Inmutable y con caducidad explicita. Solo `created_at` del bloque de auditoria.

### Schema `evidence`

#### `evidence.evidence`

Audio, foto o video capturado durante la alerta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `evidence_id` | `BIGSERIAL` | PK |  |
| `alert_id` | `BIGINT` | NOT NULL REFERENCES alert(alert_id) |  |
| `evidence_type_id` | `SMALLINT` | NOT NULL REFERENCES evidence_type(evidence_type_id) |  |
| `storage_uri` | `VARCHAR(500)` | NOT NULL |  |
| `mime_type` | `VARCHAR(80)` | NOT NULL |  |
| `size_bytes` | `BIGINT` | NOT NULL |  |
| `checksum_sha256` | `CHAR(64)` | NOT NULL | Integridad probatoria |
| `captured_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `retention_expires_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** El checksum permite sostener la evidencia como prueba no alterada.

### Schema `zones`

#### `zones.zone`

Zona geografica con nivel de riesgo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `zone_id` | `SERIAL` | PK |  |
| `name` | `VARCHAR(120)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |
| `risk_level_id` | `SMALLINT` | NOT NULL REFERENCES risk_level(risk_level_id) |  |
| `center_latitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `center_longitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `radius_m` | `INTEGER` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `zones.zone_report`

Reporte de incidente en una zona.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `zone_report_id` | `BIGSERIAL` | PK |  |
| `zone_id` | `INTEGER` | NOT NULL REFERENCES zone(zone_id) |  |
| `reported_by` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `incident_type_id` | `SMALLINT` | NOT NULL REFERENCES incident_type(incident_type_id) |  |
| `description` | `TEXT` | NULL |  |
| `occurred_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `moderation_status_id` | `SMALLINT` | NOT NULL REFERENCES moderation_status(moderation_status_id) |  |

*Plus the full audit block from section 4.*

#### `zones.emergency_resource`

Recurso de ayuda: CAI, hospital, linea de atencion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `emergency_resource_id` | `SERIAL` | PK |  |
| `resource_type_id` | `SMALLINT` | NOT NULL REFERENCES emergency_resource_type(resource_type_id) |  |
| `name` | `VARCHAR(150)` | NOT NULL |  |
| `phone` | `VARCHAR(25)` | NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |
| `latitude` | `NUMERIC(9,6)` | NULL |  |
| `longitude` | `NUMERIC(9,6)` | NULL |  |

*Plus the full audit block from section 4.*

### Schema `moderation`

#### `moderation.user_report`

Reporte de una usuaria sobre contenido o conducta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_report_id` | `BIGSERIAL` | PK |  |
| `reported_by` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `target_entity` | `VARCHAR(60)` | NOT NULL |  |
| `target_entity_id` | `VARCHAR(60)` | NOT NULL |  |
| `report_reason_id` | `SMALLINT` | NOT NULL REFERENCES report_reason(report_reason_id) |  |
| `moderation_status_id` | `SMALLINT` | NOT NULL REFERENCES moderation_status(moderation_status_id) |  |

*Plus the full audit block from section 4.*

#### `moderation.moderation_action`

Accion tomada por un moderador.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `moderation_action_id` | `BIGSERIAL` | PK |  |
| `user_report_id` | `BIGINT` | NOT NULL REFERENCES user_report(user_report_id) |  |
| `moderator_user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `action_type_id` | `SMALLINT` | NOT NULL REFERENCES moderation_action_type(action_type_id) |  |
| `notes` | `TEXT` | NULL |  |
| `taken_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |

*Plus the full audit block from section 4.*

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `blood_type`
- `platform`
- `activation_method`
- `relationship_type`
- `alert_status`
- `alert_resolution`
- `delivery_status`
- `evidence_type`
- `risk_level`
- `incident_type`
- `moderation_status`
- `emergency_resource_type`
- `report_reason`
- `moderation_action_type`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Alerta de emergencia para mujeres, con contactos de confianza, ubicacion y evidencia.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*