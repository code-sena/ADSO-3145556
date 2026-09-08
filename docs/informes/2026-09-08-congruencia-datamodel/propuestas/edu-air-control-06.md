# 06 - Data Model (counter-proposal)

> Project: **edu-air-control** - repository `ea-control-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Real pero desviado: 66 KB en dos servicios (IAM y sensores). `models.md` sigue siendo la plantilla.

**Normalization level found:** 3FN parcial

**02 -> 06 congruence:** 25% of the declared domain entities exist as tables.

La incongruencia mas grave de la ficha. Modelaron a fondo la identidad y el catalogo de sensores, y **no existe tabla alguna para mediciones ni para analisis ambiental**, que es el proposito del proyecto. Tienen 16 tablas y ninguna guarda un dato de aire.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia critica** | El dominio declara `EducationalEnvironment`, `EnvironmentData` y `EnvironmentalDataAnalysis`; el modelo solo tiene IAM y el catalogo de sensores. **Las tres entidades del nucleo del proyecto no existen como tablas.** Es la brecha 02 a 06 mas grave de la ficha. |
| **Cobertura** | `06-data/models.md` sigue siendo la plantilla intacta; el trabajo real esta en `06-data/domain/ms-iam.md` y `ms-sensor-management.md`. Falta el servicio que da sentido al proyecto. |
| **3FN** | El umbral de alerta pertenece a la configuracion de la variable, no a cada medicion. Se referencia por FK con vigencia. |
| **1FN** | Una medicion no puede guardar varias variables en una fila: una fila por sensor, variable e instante. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity & Access` | Usuario, roles, permisos, sesiones y politica de contrasena. Ya modelado. |
| `Environment` | Ambiente educativo monitoreado: sede, aula, capacidad. |
| `Sensor` | Sensor fisico, variables que mide y su configuracion. |
| `Measurement` | Lectura de variable ambiental en el tiempo. |
| `Analysis` | Analisis de datos ambientales por periodo, con su resultado. |
| `Alert` | Alerta por umbral superado. |
| `Audit` | Bitacora de acciones y errores. |

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

### Schema `environment`

#### `environment.campus`

Sede de la institucion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `campus_id` | `SERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(150)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |

*Plus the full audit block from section 4.*

#### `environment.educational_environment`

Ambiente educativo monitoreado. Entidad nuclear ausente del modelo actual.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `educational_environment_id` | `SERIAL` | PK |  |
| `campus_id` | `INTEGER` | NOT NULL REFERENCES campus(campus_id) |  |
| `code` | `VARCHAR(30)` | NOT NULL |  |
| `name` | `VARCHAR(120)` | NOT NULL |  |
| `environment_type_id` | `SMALLINT` | NOT NULL REFERENCES environment_type(environment_type_id) |  |
| `floor_area_m2` | `NUMERIC(8,2)` | NULL |  |
| `occupancy_capacity` | `SMALLINT` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Congruencia 02 a 06: `EducationalEnvironment` pasa por fin a existir como tabla.

### Schema `sensor`

#### `sensor.sensor`

Sensor fisico instalado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `sensor_id` | `BIGSERIAL` | PK |  |
| `serial_number` | `VARCHAR(60)` | NOT NULL UNIQUE |  |
| `sensor_model_id` | `SMALLINT` | NOT NULL REFERENCES sensor_model(sensor_model_id) |  |
| `sensor_status_id` | `SMALLINT` | NOT NULL REFERENCES sensor_status(sensor_status_id) |  |
| `last_seen_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: fabricante y rango de medicion pertenecen al modelo, no a la unidad.

#### `sensor.sensor_installation`

Instalacion de un sensor en un ambiente, con vigencia.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `sensor_installation_id` | `BIGSERIAL` | PK |  |
| `sensor_id` | `BIGINT` | NOT NULL REFERENCES sensor(sensor_id) |  |
| `educational_environment_id` | `INTEGER` | NOT NULL REFERENCES educational_environment(educational_environment_id) |  |
| `installed_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `removed_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Historico: un sensor puede rotar entre ambientes sin perder la trazabilidad de sus mediciones.

#### `sensor.variable`

Variable ambiental medible: CO2, temperatura, humedad, PM2.5.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `variable_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(80)` | NOT NULL |  |
| `unit_id` | `SMALLINT` | NOT NULL REFERENCES measurement_unit(unit_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: la unidad sale a catalogo; antes viajaba como texto junto a cada medicion.

#### `sensor.sensor_variable`

Variables que efectivamente mide un sensor.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `sensor_id` | `BIGINT` | NOT NULL REFERENCES sensor(sensor_id) | PK compuesta |
| `variable_id` | `SMALLINT` | NOT NULL REFERENCES variable(variable_id) | PK compuesta |

*Plus the full audit block from section 4.*

> **Normalization:** Resuelve la N:M sensor-variable.

#### `sensor.variable_threshold`

Umbral vigente por variable y tipo de ambiente.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `variable_threshold_id` | `SERIAL` | PK |  |
| `variable_id` | `SMALLINT` | NOT NULL REFERENCES variable(variable_id) |  |
| `environment_type_id` | `SMALLINT` | NOT NULL REFERENCES environment_type(environment_type_id) |  |
| `min_value` | `NUMERIC(12,4)` | NULL |  |
| `max_value` | `NUMERIC(12,4)` | NULL |  |
| `severity_id` | `SMALLINT` | NOT NULL REFERENCES severity(severity_id) |  |
| `valid_from` | `DATE` | NOT NULL |  |
| `valid_to` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: el umbral NO se copia en cada medicion. Se resuelve por FK con vigencia, de modo que un cambio de norma no reescribe el historico.

### Schema `measurement`

#### `measurement.environment_measurement`

Medicion puntual. Entidad nuclear ausente del modelo actual. Serie temporal particionada por fecha.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `environment_measurement_id` | `BIGSERIAL` | PK |  |
| `sensor_installation_id` | `BIGINT` | NOT NULL REFERENCES sensor_installation(sensor_installation_id) |  |
| `variable_id` | `SMALLINT` | NOT NULL REFERENCES variable(variable_id) |  |
| `measured_value` | `NUMERIC(12,4)` | NOT NULL |  |
| `measured_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `quality_flag_id` | `SMALLINT` | NOT NULL REFERENCES quality_flag(quality_flag_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: una fila por instalacion, variable e instante. Nunca columnas `co2`, `temp`, `humidity` en la misma fila, que obligarian a alterar la tabla al agregar una variable. Inmutable: solo `created_at`.

### Schema `analysis`

#### `analysis.environmental_analysis`

Analisis por periodo. Entidad nuclear ausente del modelo actual.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `environmental_analysis_id` | `BIGSERIAL` | PK |  |
| `educational_environment_id` | `INTEGER` | NOT NULL REFERENCES educational_environment(educational_environment_id) |  |
| `period_start` | `TIMESTAMPTZ` | NOT NULL |  |
| `period_end` | `TIMESTAMPTZ` | NOT NULL |  |
| `analysis_status_id` | `SMALLINT` | NOT NULL REFERENCES analysis_status(analysis_status_id) |  |
| `requested_by` | `BIGINT` | NULL REFERENCES app_user(user_id) |  |
| `computed_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Congruencia: materializa el value object `AnalysisPeriod` como rango explicito, no como texto.

#### `analysis.analysis_result`

Resultado por variable dentro de un analisis.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `analysis_result_id` | `BIGSERIAL` | PK |  |
| `environmental_analysis_id` | `BIGINT` | NOT NULL REFERENCES environmental_analysis(environmental_analysis_id) |  |
| `variable_id` | `SMALLINT` | NOT NULL REFERENCES variable(variable_id) |  |
| `min_value` | `NUMERIC(12,4)` | NOT NULL |  |
| `max_value` | `NUMERIC(12,4)` | NOT NULL |  |
| `avg_value` | `NUMERIC(12,4)` | NOT NULL |  |
| `sample_count` | `INTEGER` | NOT NULL |  |
| `exceedance_count` | `INTEGER` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: una fila por variable analizada; nunca un bloque de texto con todos los resultados.

### Schema `alert`

#### `alert.environment_alert`

Alerta por umbral superado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `environment_alert_id` | `BIGSERIAL` | PK |  |
| `educational_environment_id` | `INTEGER` | NOT NULL REFERENCES educational_environment(educational_environment_id) |  |
| `variable_id` | `SMALLINT` | NOT NULL REFERENCES variable(variable_id) |  |
| `variable_threshold_id` | `INTEGER` | NOT NULL REFERENCES variable_threshold(variable_threshold_id) | Umbral vigente al disparar |
| `triggering_measurement_id` | `BIGINT` | NOT NULL REFERENCES environment_measurement(environment_measurement_id) |  |
| `raised_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `acknowledged_at` | `TIMESTAMPTZ` | NULL |  |
| `acknowledged_by` | `BIGINT` | NULL REFERENCES app_user(user_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Trazabilidad completa: la alerta apunta a la medicion que la disparo y al umbral que regia en ese momento.

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `environment_type`
- `sensor_model`
- `sensor_status`
- `measurement_unit`
- `quality_flag`
- `severity`
- `analysis_status`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Monitoreo de calidad del aire en ambientes educativos, con analisis por periodo.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*