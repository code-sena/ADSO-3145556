# 06 - Data Model (counter-proposal)

> Project: **translates-sign-language** - repository `trans-sl-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Plantilla intacta.

**Normalization level found:** Sin modelo

**02 -> 06 congruence:** 0% of the declared domain entities exist as tables.

Riesgo doble: no hay modelo de datos y el dominio esta partido entre dos ramas que divergen. El trabajo existe y es bueno; el problema es que nadie puede verlo junto.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia critica** | 39 elementos declarados en el dominio (18 entidades reales mas eventos) y **cero tablas**. `06-data/models.md` es la plantilla intacta. |
| **Riesgo de ramas** | El dominio esta partido entre `docs` y `dev`, que divergen: `dev` tiene 890 lineas mas de dominio y `docs` tiene `03-product`. El modelo debe construirse sobre la union, no sobre una sola rama. |
| **1FN** | `SignLocalization` existe justamente porque una sena tiene varias glosas regionales: nunca un campo `regions` con valores separados por comas. |
| **Privacidad** | El consentimiento es dato sensible: `consent_grant` guarda otorgamiento y revocacion como filas separadas, sin sobreescribir, porque hay que poder probar que hubo consentimiento en el momento de la captura. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity & Access` | Usuario, roles, permisos, dispositivos y politica de contrasena. |
| `Sign Catalog` | Sena, categoria, localizacion por region y recurso multimedia. |
| `Translation` | Sesion de traduccion y sus traducciones individuales. |
| `Consent` | Otorgamiento y revocacion de consentimiento sobre datos biometricos. |
| `Training` | Muestras de entrenamiento aportadas y su curaduria. |
| `Model` | Versiones del modelo de reconocimiento y sus metricas. |
| `Notification` | Tipos de notificacion y avisos emitidos. |
| `Gamification` | Logros y progreso del usuario. |

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

### Schema `catalog`

#### `catalog.sign_category`

Categoria de senas.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `sign_category_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(40)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(100)` | NOT NULL |  |
| `parent_category_id` | `SMALLINT` | NULL REFERENCES sign_category(sign_category_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Jerarquia auto-referenciada; evita duplicar niveles como columnas.

#### `catalog.sign`

Sena del catalogo. Sustituye a `AlphabetLetter`, como definio el equipo en v1.1.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `sign_id` | `BIGSERIAL` | PK |  |
| `code` | `VARCHAR(50)` | NOT NULL UNIQUE |  |
| `sign_category_id` | `SMALLINT` | NOT NULL REFERENCES sign_category(sign_category_id) |  |
| `difficulty_level_id` | `SMALLINT` | NOT NULL REFERENCES difficulty_level(difficulty_level_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: el nombre legible NO va aqui, porque cambia por region. Vive en `sign_localization`.

#### `catalog.sign_localization`

Glosa de una sena en una region y lengua.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `sign_localization_id` | `BIGSERIAL` | PK |  |
| `sign_id` | `BIGINT` | NOT NULL REFERENCES sign(sign_id) |  |
| `locale_id` | `SMALLINT` | NOT NULL REFERENCES locale(locale_id) |  |
| `gloss` | `VARCHAR(150)` | NOT NULL |  |
| `description` | `TEXT` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: una glosa por fila. UNIQUE(sign_id, locale_id).

#### `catalog.multimedia_resource`

Video o imagen asociado a una sena.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `multimedia_resource_id` | `BIGSERIAL` | PK |  |
| `sign_id` | `BIGINT` | NOT NULL REFERENCES sign(sign_id) |  |
| `resource_type_id` | `SMALLINT` | NOT NULL REFERENCES resource_type(resource_type_id) |  |
| `storage_uri` | `VARCHAR(500)` | NOT NULL |  |
| `mime_type` | `VARCHAR(80)` | NOT NULL |  |
| `duration_ms` | `INTEGER` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un recurso por fila; una sena puede tener varios.

### Schema `translation`

#### `translation.translation_session`

Sesion de traduccion. Raiz de agregado segun el dominio.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `translation_session_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `user_device_id` | `BIGINT` | NULL REFERENCES user_device(user_device_id) |  |
| `recognition_model_id` | `INTEGER` | NOT NULL REFERENCES recognition_model(recognition_model_id) | Version usada, congelada |
| `started_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `ended_at` | `TIMESTAMPTZ` | NULL |  |
| `session_status_id` | `SMALLINT` | NOT NULL REFERENCES session_status(session_status_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Trazabilidad: la sesion apunta a la version del modelo que la atendio, para poder explicar un resultado meses despues.

#### `translation.translation`

Traduccion individual dentro de la sesion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `translation_id` | `BIGSERIAL` | PK |  |
| `translation_session_id` | `BIGINT` | NOT NULL REFERENCES translation_session(translation_session_id) |  |
| `sign_id` | `BIGINT` | NULL REFERENCES sign(sign_id) | NULL si no hubo coincidencia |
| `recognized_text` | `VARCHAR(255)` | NOT NULL |  |
| `confidence_score` | `NUMERIC(5,4)` | NOT NULL |  |
| `sequence_number` | `INTEGER` | NOT NULL |  |
| `recognized_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: una sena reconocida por fila, en orden. Nunca una cadena con toda la frase.

### Schema `consent`

#### `consent.consent_grant`

Otorgamiento o revocacion de consentimiento. Nunca se sobreescribe.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `consent_grant_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `consent_type_id` | `SMALLINT` | NOT NULL REFERENCES consent_type(consent_type_id) |  |
| `consent_action_id` | `SMALLINT` | NOT NULL REFERENCES consent_action(consent_action_id) | GRANT / REVOKE |
| `policy_version` | `VARCHAR(20)` | NOT NULL |  |
| `occurred_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `ip_address` | `INET` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Inmutable por diseno legal: el estado vigente se calcula tomando la ultima fila por usuario y tipo.

### Schema `training`

#### `training.training_sample`

Muestra aportada para entrenar el modelo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `training_sample_id` | `BIGSERIAL` | PK |  |
| `sign_id` | `BIGINT` | NOT NULL REFERENCES sign(sign_id) |  |
| `contributed_by` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `consent_grant_id` | `BIGINT` | NOT NULL REFERENCES consent_grant(consent_grant_id) | Consentimiento bajo el que se capturo |
| `storage_uri` | `VARCHAR(500)` | NOT NULL |  |
| `review_status_id` | `SMALLINT` | NOT NULL REFERENCES review_status(review_status_id) |  |
| `reviewed_by` | `BIGINT` | NULL REFERENCES app_user(user_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Cada muestra queda amarrada al consentimiento concreto que la habilito.

### Schema `model`

#### `model.recognition_model`

Version del modelo de reconocimiento.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `recognition_model_id` | `SERIAL` | PK |  |
| `version_tag` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `trained_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `accuracy` | `NUMERIC(5,4)` | NOT NULL |  |
| `sample_count` | `INTEGER` | NOT NULL |  |
| `is_active` | `BOOLEAN` | NOT NULL DEFAULT false |  |

*Plus the full audit block from section 4.*

> **Normalization:** Solo una activa a la vez, garantizado por indice unico parcial.

### Schema `identity`

#### `identity.user_device`

Dispositivo registrado por el usuario.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_device_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `device_fingerprint` | `VARCHAR(120)` | NOT NULL |  |
| `platform_id` | `SMALLINT` | NOT NULL REFERENCES platform(platform_id) |  |
| `push_token` | `VARCHAR(255)` | NULL |  |
| `registered_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |

*Plus the full audit block from section 4.*

### Schema `notification`

#### `notification.notification`

Aviso emitido.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `notification_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `notification_type_id` | `SMALLINT` | NOT NULL REFERENCES notification_type(notification_type_id) |  |
| `title` | `VARCHAR(150)` | NOT NULL |  |
| `body` | `TEXT` | NOT NULL |  |
| `sent_at` | `TIMESTAMPTZ` | NULL |  |
| `read_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

### Schema `gamification`

#### `gamification.achievement`

Logro disponible.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `achievement_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(40)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(100)` | NOT NULL |  |
| `points` | `SMALLINT` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `gamification.user_achievement`

Logro obtenido por un usuario.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) | PK compuesta |
| `achievement_id` | `SMALLINT` | NOT NULL REFERENCES achievement(achievement_id) | PK compuesta |
| `earned_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: los puntos NO se copian aqui; se alcanzan por FK. Si cambia el valor del logro, no hay que reescribir el historico.

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `difficulty_level`
- `locale`
- `resource_type`
- `session_status`
- `consent_type`
- `consent_action`
- `review_status`
- `platform`
- `notification_type`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Traduccion de lengua de senas a texto, con catalogo de senas y entrenamiento del modelo.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*