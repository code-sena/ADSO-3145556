# 06 - Data Model (counter-proposal)

> Project: **rent-car** - repository `rtm-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Real y extenso: 21 KB, 25 tablas repartidas en 5 contextos.

**Normalization level found:** 3FN con una fuga de 1FN

**02 -> 06 congruence:** 100% of the declared domain entities exist as tables.

El unico proyecto con la cadena 02-03-06 cerrada. Cada entidad del dominio tiene tabla y cada tabla responde a una entidad. La brecha no es de congruencia sino de auditoria: 8 menciones de campos temporales en 21 KB.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **1FN** | `branch.schedules VARCHAR(100)` guarda el horario como texto libre. Un horario es un grupo repetitivo por dia de la semana: se extrae a `branch_operating_hour`. |
| **3FN** | `reservation.total_amount` es un valor derivable de las lineas de cargo. Se conserva como total congelado del contrato pero se sustenta en `rental_charge`, de modo que el importe sea reconstruible y auditable. |
| **2FN** | `person.branch_id` solo aplica a personal interno, no a clientes. Se mueve a `employee_assignment`, que es donde esa dependencia es total. |
| **Auditoria** | El modelo actual solo tiene la tabla `audit` generica y 8 menciones de campos temporales en 21 KB. Se agrega el bloque de auditoria a las 25 tablas. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity & Access` | Persona, cuenta, roles, permisos, sesiones y verificacion. Ya modelado por el equipo. |
| `Fleet` | Sede, marca, modelo, categoria, tipo de motor y vehiculo. |
| `Maintenance` | Mantenimientos programados y ejecutados sobre cada vehiculo. |
| `Booking` | Reserva y contrato de alquiler, con su ciclo de estados. |
| `Billing` | Metodos de pago, pagos y facturas. |
| `Telemetry` | Dispositivo GPS y posiciones reportadas. |
| `Notification` | Avisos al cliente y al administrador. |

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

### Schema `fleet`

#### `fleet.branch`

Sede fisica.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `branch_id` | `SERIAL` | PK |  |
| `name` | `VARCHAR(100)` | NOT NULL |  |
| `address_line` | `VARCHAR(200)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) | Ciudad catalogada, no texto |
| `phone` | `VARCHAR(25)` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: la ciudad sale a catalogo; antes viajaba como VARCHAR duplicado en cada sede.

#### `fleet.branch_operating_hour`

Horario de atencion por dia. Reemplaza el campo de texto libre.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `branch_operating_hour_id` | `SERIAL` | PK |  |
| `branch_id` | `INTEGER` | NOT NULL REFERENCES branch(branch_id) |  |
| `day_of_week` | `SMALLINT` | NOT NULL CHECK (day_of_week BETWEEN 1 AND 7) |  |
| `opens_at` | `TIME` | NOT NULL |  |
| `closes_at` | `TIME` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: cada franja horaria es una fila, no un fragmento de cadena.

#### `fleet.brand`

Marca del vehiculo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `brand_id` | `SERIAL` | PK |  |
| `name` | `VARCHAR(60)` | NOT NULL UNIQUE |  |

*Plus the full audit block from section 4.*

#### `fleet.vehicle_model`

Modelo, dependiente de la marca.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `vehicle_model_id` | `SERIAL` | PK |  |
| `brand_id` | `INTEGER` | NOT NULL REFERENCES brand(brand_id) |  |
| `name` | `VARCHAR(60)` | NOT NULL |  |
| `category_id` | `SMALLINT` | NOT NULL REFERENCES category(category_id) |  |
| `engine_type_id` | `SMALLINT` | NOT NULL REFERENCES engine_type(engine_type_id) |  |
| `passenger_capacity` | `SMALLINT` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: categoria y tipo de motor son atributos del modelo, no del vehiculo individual. Guardarlos en `vehicle` los repetiria en cada unidad.

#### `fleet.vehicle`

Unidad fisica de la flota.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `vehicle_id` | `BIGSERIAL` | PK |  |
| `vehicle_model_id` | `INTEGER` | NOT NULL REFERENCES vehicle_model(vehicle_model_id) |  |
| `branch_id` | `INTEGER` | NOT NULL REFERENCES branch(branch_id) |  |
| `license_plate` | `VARCHAR(10)` | NOT NULL UNIQUE |  |
| `vin` | `VARCHAR(17)` | NOT NULL UNIQUE |  |
| `model_year` | `SMALLINT` | NOT NULL |  |
| `odometer_km` | `INTEGER` | NOT NULL DEFAULT 0 |  |
| `vehicle_status_id` | `SMALLINT` | NOT NULL REFERENCES vehicle_status(vehicle_status_id) |  |
| `daily_rate` | `NUMERIC(12,2)` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: marca y categoria NO se repiten aqui; se alcanzan por `vehicle_model`.

### Schema `maintenance`

#### `maintenance.maintenance_type`

Catalogo de tipos de mantenimiento.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `maintenance_type_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(80)` | NOT NULL |  |
| `interval_km` | `INTEGER` | NULL |  |

*Plus the full audit block from section 4.*

#### `maintenance.vehicle_maintenance`

Intervencion sobre un vehiculo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `vehicle_maintenance_id` | `BIGSERIAL` | PK |  |
| `vehicle_id` | `BIGINT` | NOT NULL REFERENCES vehicle(vehicle_id) |  |
| `maintenance_type_id` | `SMALLINT` | NOT NULL REFERENCES maintenance_type(maintenance_type_id) |  |
| `scheduled_for` | `DATE` | NOT NULL |  |
| `performed_at` | `TIMESTAMPTZ` | NULL |  |
| `odometer_km` | `INTEGER` | NULL |  |
| `cost` | `NUMERIC(12,2)` | NULL |  |
| `notes` | `TEXT` | NULL |  |

*Plus the full audit block from section 4.*

### Schema `booking`

#### `booking.reservation`

Reserva previa al contrato.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `reservation_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `vehicle_id` | `BIGINT` | NOT NULL REFERENCES vehicle(vehicle_id) |  |
| `pickup_branch_id` | `INTEGER` | NOT NULL REFERENCES branch(branch_id) |  |
| `return_branch_id` | `INTEGER` | NOT NULL REFERENCES branch(branch_id) |  |
| `starts_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `ends_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `reservation_status_id` | `SMALLINT` | NOT NULL REFERENCES reservation_status(reservation_status_id) |  |
| `quoted_total` | `NUMERIC(12,2)` | NOT NULL | Total congelado; reconstruible desde rental_charge |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: `quoted_total` se documenta explicitamente como total congelado del contrato, no como dato de verdad. La verdad esta en las lineas de cargo.

#### `booking.rental`

Contrato efectivo derivado de la reserva.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `rental_id` | `BIGSERIAL` | PK |  |
| `reservation_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES reservation(reservation_id) |  |
| `picked_up_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `returned_at` | `TIMESTAMPTZ` | NULL |  |
| `odometer_out` | `INTEGER` | NOT NULL |  |
| `odometer_in` | `INTEGER` | NULL |  |
| `rental_status_id` | `SMALLINT` | NOT NULL REFERENCES rental_status(rental_status_id) |  |

*Plus the full audit block from section 4.*

#### `booking.rental_charge`

Linea de cargo del contrato: tarifa base, dias extra, danos, combustible.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `rental_charge_id` | `BIGSERIAL` | PK |  |
| `rental_id` | `BIGINT` | NOT NULL REFERENCES rental(rental_id) |  |
| `charge_type_id` | `SMALLINT` | NOT NULL REFERENCES charge_type(charge_type_id) |  |
| `quantity` | `NUMERIC(10,2)` | NOT NULL DEFAULT 1 |  |
| `unit_price` | `NUMERIC(12,2)` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 2FN/3FN: el importe de linea es `quantity * unit_price`, calculado, nunca almacenado. Es lo que hace auditable el total.

### Schema `billing`

#### `billing.payment_method`

Medio de pago registrado por el cliente.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `payment_method_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `payment_method_type_id` | `SMALLINT` | NOT NULL REFERENCES payment_method_type(payment_method_type_id) |  |
| `masked_number` | `VARCHAR(25)` | NOT NULL | Solo los ultimos digitos |
| `expires_on` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Seguridad: nunca el numero completo ni el CVV.

#### `billing.payment`

Intento de cobro. Un intento fallido tambien deja fila.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `payment_id` | `BIGSERIAL` | PK |  |
| `rental_id` | `BIGINT` | NOT NULL REFERENCES rental(rental_id) |  |
| `payment_method_id` | `BIGINT` | NULL REFERENCES payment_method(payment_method_id) |  |
| `amount` | `NUMERIC(12,2)` | NOT NULL |  |
| `currency_code` | `CHAR(3)` | NOT NULL DEFAULT 'COP' |  |
| `payment_status_id` | `SMALLINT` | NOT NULL REFERENCES payment_status(payment_status_id) |  |
| `processed_at` | `TIMESTAMPTZ` | NULL |  |
| `gateway_reference` | `VARCHAR(100)` | NULL |  |

*Plus the full audit block from section 4.*

#### `billing.invoice`

Comprobante emitido.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `invoice_id` | `BIGSERIAL` | PK |  |
| `rental_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES rental(rental_id) |  |
| `invoice_number` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `issued_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `tax_rate` | `NUMERIC(5,4)` | NOT NULL | Tasa vigente al emitir; se congela por ley |

*Plus the full audit block from section 4.*

> **Normalization:** Excepcion documentada: la tasa se congela en la factura porque el valor legal es el del momento de emision.

### Schema `telemetry`

#### `telemetry.gps_device`

Dispositivo instalado en un vehiculo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `gps_device_id` | `BIGSERIAL` | PK |  |
| `vehicle_id` | `BIGINT` | NOT NULL REFERENCES vehicle(vehicle_id) |  |
| `serial_number` | `VARCHAR(60)` | NOT NULL UNIQUE |  |
| `installed_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `uninstalled_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

#### `telemetry.vehicle_location`

Posicion reportada. Tabla de alto volumen, particionada por fecha.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `vehicle_location_id` | `BIGSERIAL` | PK |  |
| `gps_device_id` | `BIGINT` | NOT NULL REFERENCES gps_device(gps_device_id) |  |
| `latitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `longitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `speed_kmh` | `NUMERIC(6,2)` | NULL |  |
| `recorded_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Serie temporal: solo `created_at` del bloque de auditoria; no admite update ni borrado logico.

### Schema `notification`

#### `notification.notification`

Aviso emitido al usuario.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `notification_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `notification_type_id` | `SMALLINT` | NOT NULL REFERENCES notification_type(notification_type_id) |  |
| `subject` | `VARCHAR(150)` | NOT NULL |  |
| `body` | `TEXT` | NOT NULL |  |
| `sent_at` | `TIMESTAMPTZ` | NULL |  |
| `read_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `vehicle_status`
- `reservation_status`
- `rental_status`
- `charge_type`
- `category`
- `engine_type`
- `payment_method_type`
- `payment_status`
- `notification_type`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Alquiler de vehiculos con reservas, mantenimiento de flota y facturacion.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*