---
title: "Diccionario de datos"
lang: es
source: "06_Base_Datos/Script_BD_implementado.sql"
generated_from: "lozano2303/TuEvento origin/develop 2d259e7 (Liquibase 4.31.1, PostgreSQL 17.10)"
generated_on: 2026-10-24
status: borrador
reviewed_by: ""
---
# Diccionario de datos — Tu Evento

> **Borrador generado del esquema implementado.** Este diccionario se generó el 2026-10-24 a partir de [`Script_BD_implementado.sql`](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/06_Base_Datos/Script_BD_implementado.sql): los 171 changesets de Liquibase 4.31.1 de `lozano2303/TuEvento` (`origin/develop` / `2d259e7`) aplicados sobre un PostgreSQL 17.10 temporal. Los nombres, tipos, nulidad, claves y valores por defecto salen del esquema. Las descripciones salen de los comentarios de la base de datos y del código (Javadoc); donde no hay ninguna, la descripción se dedujo del código y lleva la marca *[D]*. El módulo de cada tabla sale de la entidad JPA que la mapea.
>
> Resumen: 75 tablas, 570 columnas; 254 descripciones de columna y 65 de tabla deducidas del código (*[D]*), pendientes de revisión del equipo.
>
> La versión anterior de este documento (diccionario de diseño convertido del XLSX `04_Diseno_Software/Diccionario de datos Tu-Evento.xlsx`) se conserva en el historial de Git: `git show 5e68501:es/06_Base_Datos/Diccionario_Datos.md`.

**Leyenda de la columna Clave:** PK = clave primaria · FK → `tabla.columna` = clave foránea · UQ = forma parte de una restricción UNIQUE.

**Descripciones generadas por regla.** Las descripciones marcadas con *[R1]*–*[R4]* no salen de comentarios: se deducen de la estructura del esquema sin interpretación. Las que no llevan marca salen de comentarios de la base de datos o del código. *[R1]* columna PK con `IDENTITY` → «Identificador único de la tabla (PK, IDENTITY).» · *[R2]* columnas `created_at`, `created_by`, `updated_at` y `updated_by` → descripción estándar de auditoría (en las entidades que heredan de `JpaAuditingEntity`, `created_by` y `updated_by` guardan el nombre del usuario autenticado o `system`, según `shared/infrastructure/config/JpaConfig.java`) · *[R3]* columna con clave foránea → «Referencia a tabla.columna.» · *[R4]* columna con restricción CHECK de lista → «Valores permitidos: …».

**Descripciones deducidas del código.** *[D]* indica una descripción redactada el 2026-10-24 a partir del nombre de la columna, la entidad JPA que la mapea, las validaciones y su uso en los casos de uso y repositorios de `2d259e7`. Es una interpretación, no un comentario del esquema: el equipo debe revisarla y, si es correcta, quitar la marca.

**Contenido**

1. [`category`](#mod-category) — 2 tablas
2. [`event`](#mod-event) — 7 tablas
3. [`geolocation`](#mod-geolocation) — 3 tablas
4. [`language`](#mod-language) — 16 tablas
5. [`notification`](#mod-notification) — 4 tablas
6. [`payment`](#mod-payment) — 4 tablas
7. [`profile`](#mod-profile) — 2 tablas
8. [`seat`](#mod-seat) — 3 tablas
9. [`section`](#mod-section) — 2 tablas
10. [`sectiontype`](#mod-sectiontype) — 0 tablas
11. [`security`](#mod-security) — 16 tablas
12. [`storage`](#mod-storage) — 4 tablas
13. [`theme`](#mod-theme) — 4 tablas
14. [`ticket`](#mod-ticket) — 5 tablas
15. [`wallet`](#mod-wallet) — 3 tablas

<a id="mod-category"></a>

## 1. Módulo `category`

### 1.1 `category`

**Descripción:** Categorías de eventos (jerárquicas mediante `dad_id`). *[D]*

**Creada en el changeset:** `047-create-category.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `category_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `name` | `character varying(50)` | NO | UQ | — | Nombre único de la categoría. *[D]* |
| `description` | `character varying(100)` | NO | — | — | Descripción de la categoría. *[D]* |
| `active` | `boolean` | NO | — | `true` | Indica si la categoría está activa; al crear un evento se exige una categoría activa. *[D]* |
| `visible` | `boolean` | NO | — | `true` | Indica si la categoría se muestra en la interfaz. *[D]* |
| `dad_id` | `integer` | SÍ | FK → `category.category_id` | — | Categoría padre; nulo si es una categoría raíz. |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | NO | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | NO | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 1.2 `category_event`

**Descripción:** Relación muchos a muchos entre eventos y categorías. *[D]*

**Creada en el changeset:** `048-create-category-event.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `category_event_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `category_id` | `integer` | NO | UQ, FK → `category.category_id` | — | Categoría asociada al evento. |
| `event_id` | `integer` | NO | UQ, FK → `event.event_id` | — | Evento asociado a la categoría. |

<a id="mod-event"></a>

## 2. Módulo `event`

### 2.1 `event`

**Descripción:** Eventos creados por organizadores, con su sitio, fechas, aforo y estado del flujo DRAFT → PENDING_REVIEW → PUBLISHED. *[D]*

**Creada en el changeset:** `039-create-event.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `event_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `bigint` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `site_id` | `bigint` | NO | UQ, FK → `site.site_id` | — | Referencia a `site.site_id`. *[R3]* |
| `event_name` | `character varying(100)` | NO | UQ | — | Nombre del evento; junto con la fecha de inicio y el sitio no puede repetirse. *[D]* |
| `description` | `character varying(255)` | NO | — | — | Descripción del evento. *[D]* |
| `start_date` | `date` | NO | UQ | — | Fecha de inicio del evento. *[D]* Restricción CHECK: `CHECK ((finish_date > start_date))`. |
| `finish_date` | `date` | NO | — | — | Fecha de finalización del evento. *[D]* Restricción CHECK: `CHECK ((finish_date > start_date))`. |
| `status` | `character varying(20)` | NO | — | `'DRAFT'::character varying` | Valores permitidos: `DRAFT`, `PENDING_REVIEW`, `PUBLISHED`, `REJECTED`, `CANCELLED`, `COMPLETED`. *[R4]* |
| `is_public` | `boolean` | NO | — | `true` | Indica si el evento aparece en el catálogo público cuando está PUBLISHED. *[D]* |
| `available_seats` | `integer` | NO | — | — | Aforo del evento; no puede superar la capacidad del sitio. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |
| `rejection_reason` | `text` | SÍ | — | — | Motivo de rechazo; se llena cuando el estado es REJECTED y se borra al volver a DRAFT. |

### 2.2 `event_layout`

**Descripción:** Plano de sillas del evento guardado por el editor de layout. *[D]*

**Creada en el changeset:** `041-create-event-layout.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `event_layout_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `layout_data` | `jsonb` | NO | — | — | JSON del plano de secciones, bloques y sillas guardado por el editor de layout. *[D]* |

### 2.3 `event_log`

**Descripción:** Bitácora de cambios hechos sobre un evento. *[D]*

**Creada en el changeset:** `093-create-event-log-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `log_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `action` | `character varying(100)` | NO | — | — | Acción registrada sobre el evento. *[D]* |
| `old_value` | `text` | SÍ | — | — | Valor anterior al cambio. *[D]* |
| `new_value` | `text` | SÍ | — | — | Valor posterior al cambio. *[D]* |
| `occurred_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora del cambio. *[D]* |

### 2.4 `event_media`

**Descripción:** Imágenes asociadas a un evento. *[D]*

**Creada en el changeset:** `042-create-event-media.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `media_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `img_url` | `character varying(255)` | NO | — | — | URL de la imagen en el almacenamiento de objetos. *[D]* |

### 2.5 `event_media_log`

**Descripción:** Historial de versiones de las imágenes de un evento. *[D]*

**Creada en el changeset:** `043-create-event-media-log.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `media_log_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `media_id` | `bigint` | NO | FK → `event_media.media_id` | — | Referencia a `event_media.media_id`. *[R3]* |
| `img_url` | `character varying(255)` | NO | — | — | URL de la imagen en esa versión. *[D]* |
| `version` | `integer` | NO | — | — | Número de versión consecutivo de la imagen. *[D]* |
| `is_visible` | `boolean` | NO | — | `true` | Indica si esa versión de la imagen es visible. *[D]* |

### 2.6 `event_rating`

**Descripción:** Calificaciones (1–5) y comentarios de los usuarios sobre un evento. *[D]*

**Creada en el changeset:** `044-create-event-rating.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `rating_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `user_id` | `bigint` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `rating` | `integer` | SÍ | — | — | Calificación numérica de 1 a 5; nula cuando el registro es solo un comentario. *[D]* Restricción CHECK: `CHECK (((rating IS NULL) OR ((rating >= 1) AND (rating <= 5))))`. |
| `comment` | `character varying()` | NO | — | — | Texto del comentario (máximo 500 caracteres según la validación de la aplicación). *[D]* |
| `is_visible` | `boolean` | NO | — | `true` | Indica si el comentario o la calificación se muestra públicamente. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `parent_rating_id` | `bigint` | SÍ | FK → `event_rating.rating_id` | — | Comentario al que responde; nulo si es un comentario principal (borrado en cascada). |
| `edited_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última edición del comentario. |
| `reply_to_user_id` | `bigint` | SÍ | — | — | ID del usuario al que se responde directamente (para menciones). |
| `reply_to_user_name` | `character varying(100)` | SÍ | — | — | Nombre del usuario al que se responde (para mostrar @Nombre). |

### 2.7 `event_status_log`

**Descripción:** Historial de cambios de estado de un evento. *[D]*

**Creada en el changeset:** `040-create-event-status-log.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `status_log_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `old_status` | `character varying(20)` | SÍ | — | — | Estado anterior del evento. *[D]* |
| `new_status` | `character varying(20)` | NO | — | — | Estado nuevo del evento. *[D]* |
| `changed_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora del cambio de estado. *[D]* |
| `changed_by` | `bigint` | SÍ | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `reason` | `text` | SÍ | — | — | Motivo de rechazo del administrador cuando el nuevo estado es REJECTED; nulo en otro caso. |

<a id="mod-geolocation"></a>

## 3. Módulo `geolocation`

### 3.1 `city`

**Descripción:** Ciudades (municipios) cargadas desde `db/data/cities.csv`. *[D]*

**Creada en el changeset:** `026-create-city-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `city_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `department_id` | `integer` | NO | FK → `department.department_id` | — | Referencia a `department.department_id`. *[R3]* |
| `name` | `character varying(100)` | NO | — | — | Nombre de la ciudad. *[D]* |
| `code` | `character varying(10)` | NO | UQ | — | Código único de la ciudad (código DANE del municipio). *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 3.2 `department`

**Descripción:** Departamentos de Colombia cargados desde `db/data/departments.csv`. *[D]*

**Creada en el changeset:** `025-create-department-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `department_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `name` | `character varying(100)` | NO | — | — | Nombre del departamento. *[D]* |
| `code` | `character varying(10)` | NO | UQ | — | Código único del departamento (código DANE). *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 3.3 `site`

**Descripción:** Sitios o recintos donde se realizan los eventos, con capacidad y coordenadas. *[D]*

**Creada en el changeset:** `027-create-site-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `site_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `city_id` | `integer` | NO | FK → `city.city_id` | — | Referencia a `city.city_id`. *[R3]* |
| `name` | `character varying(150)` | NO | — | — | Nombre del sitio. *[D]* |
| `address` | `character varying(200)` | NO | — | — | Dirección del sitio. *[D]* |
| `capacity` | `integer` | NO | — | — | Capacidad máxima de personas del sitio. *[D]* |
| `latitude` | `numeric(9,6)` | NO | — | — | Latitud del sitio (−90 a 90). *[D]* |
| `longitude` | `numeric(9,6)` | NO | — | — | Longitud del sitio (−180 a 180). *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

<a id="mod-language"></a>

## 4. Módulo `language`

### 4.1 `action_translation`

**Descripción:** Traducciones de los nombres de acciones del sistema por idioma. *[D]*

**Creada en el changeset:** `091-create-final-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `action` | `character varying(100)` | NO | UQ | — | Código de la acción traducida. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_description` | `character varying(255)` | NO | — | — | Descripción traducida de la acción. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.2 `category_translation`

**Descripción:** Traducciones del nombre y la descripción de cada categoría por idioma. *[D]*

**Creada en el changeset:** `088-create-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `category_id` | `integer` | NO | UQ, FK → `category.category_id` | — | Referencia a `category.category_id`. *[R3]* |
| `language_id` | `bigint` | NO | UQ, FK → `language.language_id` | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | NO | — | — | Nombre traducido de la categoría. *[D]* |
| `translated_description` | `character varying(255)` | SÍ | — | — | Descripción traducida de la categoría. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'DRAFT'::character varying` | Valores permitidos: `DRAFT`, `PUBLISHED`, `PENDING_REVIEW`. *[R4]* |
| `created_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(255)` | NO | — | `'system'::character varying` | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(255)` | NO | — | `'system'::character varying` | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 4.3 `channel_translation`

**Descripción:** Traducciones de los canales de notificación por idioma. *[D]*

**Creada en el changeset:** `091-create-final-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `channel_id` | `integer` | NO | UQ | — | Canal traducido; único junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | SÍ | — | — | Nombre traducido del canal. *[D]* |
| `translated_description` | `character varying(255)` | NO | — | — | Descripción traducida del canal. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.4 `event_comment_reply_translation`

**Descripción:** Traducciones de las respuestas a comentarios de eventos. *[D]*

**Creada en el changeset:** `091-create-final-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `reply_id` | `integer` | NO | UQ | — | Respuesta traducida; única junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_reply_text` | `character varying(255)` | SÍ | — | — | Texto traducido de la respuesta. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.5 `event_rating_translation`

**Descripción:** Traducciones de los comentarios de las calificaciones de eventos. *[D]*

**Creada en el changeset:** `090-create-more-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `rating_id` | `integer` | NO | UQ | — | Calificación traducida; única junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_comment` | `character varying(255)` | SÍ | — | — | Comentario traducido. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.6 `event_translation`

**Descripción:** Traducciones del nombre y la descripción de cada evento por idioma. *[D]*

**Creada en el changeset:** `088-create-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | UQ, FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `language_id` | `bigint` | NO | UQ, FK → `language.language_id` | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | SÍ | — | — | Nombre traducido del evento. *[D]* |
| `translated_description` | `character varying(255)` | SÍ | — | — | Descripción traducida del evento. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'DRAFT'::character varying` | Valores permitidos: `DRAFT`, `PUBLISHED`, `PENDING_REVIEW`. *[R4]* |
| `created_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(255)` | NO | — | `'system'::character varying` | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(255)` | NO | — | `'system'::character varying` | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 4.7 `language`

**Descripción:** Idiomas disponibles en la plataforma. *[D]*

**Creada en el changeset:** `084-create-language-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `language_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `code` | `character varying(10)` | NO | UQ | — | Código único del idioma (por ejemplo, `es`, `en`). *[D]* |
| `name` | `character varying(50)` | NO | — | — | Nombre del idioma. *[D]* |
| `is_active` | `boolean` | NO | — | `true` | Indica si el idioma está activo; el público solo ve los activos. *[D]* |
| `is_default` | `boolean` | NO | — | `false` | Indica si es el idioma por defecto; este no se puede desactivar. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | NO | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | NO | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 4.8 `module_translation`

**Descripción:** Traducciones de los nombres de los módulos del sistema. *[D]*

**Creada en el changeset:** `091-create-final-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `module` | `character varying(50)` | NO | UQ | — | Código del módulo traducido. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | NO | — | — | Nombre traducido del módulo. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.9 `notification_translation`

**Descripción:** Traducción de notificaciones (Javadoc de `NotificationTranslation`).

**Creada en el changeset:** `089-create-additional-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `notification_id` | `integer` | NO | UQ | — | Notificación traducida; única junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_subject` | `text` | SÍ | — | — | Asunto traducido. *[D]* |
| `translated_body` | `text` | SÍ | — | — | Cuerpo traducido. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.10 `notification_type_translation`

**Descripción:** Traducciones de los tipos de notificación. *[D]*

**Creada en el changeset:** `091-create-final-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `notification_type_id` | `integer` | NO | UQ | — | Tipo de notificación traducido; único junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | NO | — | — | Nombre traducido del tipo. *[D]* |
| `translated_description` | `character varying(255)` | NO | — | — | Descripción traducida del tipo. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.11 `profile_translation`

**Descripción:** Traducciones de la biografía del perfil. *[D]*

**Creada en el changeset:** `089-create-additional-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `profile_id` | `integer` | NO | UQ | — | Perfil traducido; único junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | UQ, FK → `language.language_id` | — | Referencia a `language.language_id`. *[R3]* |
| `translated_bio` | `character varying(255)` | NO | — | — | Biografía traducida. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.12 `review_translation`

**Descripción:** Traducciones de reseñas. *[D]*

**Creada en el changeset:** `090-create-more-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `review_id` | `integer` | NO | UQ | — | Reseña traducida; única junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_comment` | `character varying(255)` | NO | — | — | Comentario traducido de la reseña. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.13 `seat_block_translation`

**Descripción:** Traducciones de los nombres de bloques de sillas. *[D]*

**Creada en el changeset:** `090-create-more-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `seat_block_id` | `integer` | NO | UQ | — | Bloque de sillas traducido; único junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | NO | — | — | Nombre traducido del bloque. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.14 `section_type_translation`

**Descripción:** Traducciones de los tipos de sección. *[D]*

**Creada en el changeset:** `091-create-final-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `section_type_id` | `integer` | NO | UQ | — | Tipo de sección traducido; único junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | NO | — | — | Nombre traducido del tipo de sección. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.15 `theme_translation`

**Descripción:** Traducciones del nombre y la descripción de los temas visuales. *[D]*

**Creada en el changeset:** `089-create-additional-translation-tables.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `translation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `theme_id` | `integer` | NO | UQ | — | Tema traducido; único junto con el idioma. *[D]* |
| `language_id` | `integer` | NO | FK → `language.language_id`, UQ | — | Referencia a `language.language_id`. *[R3]* |
| `translated_name` | `character varying(100)` | NO | — | — | Nombre traducido del tema. *[D]* |
| `translated_description` | `character varying(255)` | NO | — | — | Descripción traducida del tema. *[D]* |
| `source` | `character varying(50)` | NO | — | — | Origen de la traducción: `MANUAL` (humana) o `MACHINE` (automática), según `TranslationSource`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'draft'::character varying` | Estado de la traducción (`DRAFT`, `PUBLISHED` o `PENDING_REVIEW` en `TranslationStatus`); solo las publicadas se muestran. *[D]* |

### 4.16 `translation_job`

**Descripción:** Trabajos de traducción automática asíncrona (LibreTranslate) por entidad, con reintentos. *[D]*

**Creada en el changeset:** `087-translation-job-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `job_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `entity_type` | `character varying(50)` | NO | UQ | — | Tipo de entidad que se traduce (por ejemplo, `event`, `category`, `profile`). *[D]* |
| `entity_id` | `bigint` | NO | UQ | — | Identificador de la entidad que se traduce. *[D]* |
| `source_language_id` | `bigint` | NO | FK → `language.language_id` | — | Referencia a `language.language_id`. *[R3]* |
| `target_language_id` | `bigint` | NO | UQ, FK → `language.language_id` | — | Referencia a `language.language_id`. *[R3]* |
| `source_hash` | `character varying(64)` | NO | — | — | Hash de los textos de origen; permite detectar si cambiaron y hay que volver a traducir. *[D]* |
| `status` | `character varying(20)` | NO | — | — | Valores permitidos: `PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`. *[R4]* |
| `provider` | `character varying(20)` | NO | — | — | Proveedor de traducción usado. *[D]* |
| `attempts` | `integer` | NO | — | `0` | Número de intentos realizados. *[D]* |
| `last_error` | `character varying(1000)` | SÍ | — | — | Último error del proveedor. *[D]* |
| `next_retry_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora del siguiente reintento. *[D]* |
| `completed_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora en que terminó el trabajo. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(255)` | NO | — | `'system'::character varying` | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(255)` | NO | — | `'system'::character varying` | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

<a id="mod-notification"></a>

## 5. Módulo `notification`

### 5.1 `channel`

**Descripción:** Canales de envío de notificaciones (por ejemplo, correo o in-app). *[D]*

**Creada en el changeset:** `079-create-channel-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `channel_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `name` | `character varying(50)` | NO | UQ | — | Nombre único del canal. *[D]* |
| `description` | `character varying(100)` | NO | — | — | Descripción del canal. *[D]* |
| `active` | `boolean` | NO | — | `true` | Indica si el canal está activo. *[D]* |
| `config` | `jsonb` | SÍ | — | — | Configuración del canal en JSON. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 5.2 `notification`

**Descripción:** Notificaciones generadas por eventos de negocio. *[D]*

**Creada en el changeset:** `081-create-notification-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `notification_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `channel_id` | `bigint` | NO | FK → `channel.channel_id` | — | Referencia a `channel.channel_id`. *[R3]* |
| `notification_type_id` | `bigint` | NO | FK → `notification_type.notification_type_id` | — | Referencia a `notification_type.notification_type_id`. *[R3]* |
| `entity_type` | `character varying(30)` | NO | — | — | Tipo de entidad de negocio que originó la notificación. *[D]* |
| `entity_id` | `bigint` | NO | — | — | Identificador de la entidad que originó la notificación. *[D]* |
| `subject` | `character varying(150)` | NO | — | — | Asunto de la notificación. *[D]* |
| `body` | `text` | NO | — | — | Cuerpo de la notificación. *[D]* |
| `sent_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de envío. *[D]* |
| `idempotency_key` | `character varying(150)` | NO | UQ | — | Clave única que evita crear la misma notificación dos veces. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 5.3 `notification_type`

**Descripción:** Catálogo de tipos de notificación. *[D]*

**Creada en el changeset:** `080-create-notification-type-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `notification_type_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `name` | `character varying(50)` | NO | UQ | — | Nombre único del tipo de notificación. *[D]* |
| `description` | `character varying(100)` | NO | — | — | Descripción del tipo de notificación. *[D]* |
| `active` | `boolean` | NO | — | `true` | Indica si el tipo está activo. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 5.4 `notification_user`

**Descripción:** Entrega de una notificación a un usuario por un canal, con su estado de envío y lectura. *[D]*

**Creada en el changeset:** `082-create-notification-user-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `notification_user_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `notification_id` | `bigint` | NO | UQ, FK → `notification.notification_id` | — | Referencia a `notification.notification_id`. *[R3]* |
| `user_id` | `integer` | NO | UQ, FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `email_address` | `character varying(255)` | SÍ | — | — | Correo de destino usado en el envío. *[D]* |
| `delivered_status` | `character varying(20)` | NO | — | — | Valores permitidos: `PENDING`, `SENT`, `DELIVERED`, `FAILED`. *[R4]* |
| `error_message` | `text` | SÍ | — | — | Mensaje de error si el envío falló. *[D]* |
| `read_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora en que el usuario la marcó como leída; nula si no se ha leído. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

<a id="mod-payment"></a>

## 6. Módulo `payment`

### 6.1 `payment`

**Descripción:** Pagos de órdenes, por pasarela, por wallet o mixtos. *[D]*

**Creada en el changeset:** `069-create-payment-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `payment_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `order_id` | `bigint` | NO | FK → `order.order_id` | — | Referencia a `order.order_id`. *[R3]* |
| `gateway` | `character varying(20)` | NO | — | — | Valores permitidos: `FAKE`, `WOMPI`. *[R4]* |
| `gateway_transaction_id` | `character varying(100)` | NO | UQ | — | Identificador único de la transacción en la pasarela; los pagos solo con wallet usan el prefijo `wallet_only_`. *[D]* |
| `status` | `character varying(20)` | NO | — | `'PENDING'::character varying` | Valores permitidos: `PENDING`, `APPROVED`, `REJECTED`, `ERROR`, `REFUNDED`. *[R4]* |
| `amount` | `numeric(19,4)` | NO | — | — | Monto cobrado por la pasarela (0 si se pagó todo con wallet). *[D]* |
| `currency` | `character varying(3)` | NO | — | `'COP'::character varying` | Código ISO 4217 de la moneda. *[D]* |
| `payment_method` | `character varying(20)` | SÍ | — | — | Valores permitidos: `CARD`, `PSE`, `NEQUI`, `CASH`, `QR`. *[R4]* |
| `processed_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora en que se procesó el pago. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |
| `wallet_amount_applied` | `numeric(19,4)` | NO | — | `0` | Parte del total cubierta con saldo de la wallet. *[D]* Restricción CHECK: `CHECK ((wallet_amount_applied >= (0)::numeric))`. |
| `wallet_transaction_id` | `bigint` | SÍ | FK → `wallet_transaction.transaction_id` | — | Referencia a `wallet_transaction.transaction_id`. *[R3]* |

### 6.2 `payment_log`

**Descripción:** Auditoría de cambios de estado de los pagos (Javadoc de `PaymentLogEntity`).

**Creada en el changeset:** `070-create-payment-log-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `payment_log_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `payment_id` | `bigint` | NO | FK → `payment.payment_id` | — | Referencia a `payment.payment_id`. *[R3]* |
| `old_status` | `character varying(20)` | NO | — | — | Estado anterior del pago. *[D]* |
| `new_status` | `character varying(20)` | NO | — | — | Estado nuevo del pago. *[D]* |
| `changed_at` | `timestamp without time zone` | NO | — | — | Fecha y hora del cambio. *[D]* |
| `reason` | `character varying(500)` | SÍ | — | — | Motivo del cambio de estado. *[D]* |

### 6.3 `refund`

**Descripción:** Reembolsos solicitados sobre pagos aprobados. *[D]*

**Creada en el changeset:** `072-create-refund-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `refund_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `payment_id` | `bigint` | NO | FK → `payment.payment_id` | — | Referencia a `payment.payment_id`. *[R3]* |
| `status` | `character varying(20)` | NO | — | `'REQUESTED'::character varying` | Valores permitidos: `REQUESTED`, `APPROVED`, `PROCESSED`, `FAILED`. *[R4]* |
| `reason` | `character varying(500)` | SÍ | — | — | Motivo del reembolso (obligatorio, máximo 500 caracteres). *[D]* |
| `requested_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la solicitud. *[D]* |
| `processed_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora en que se procesó. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 6.4 `transaction_webhook`

**Descripción:** Auditoría de los webhooks recibidos (Javadoc de `TransactionWebhookEntity`).

**Creada en el changeset:** `071-create-transaction-webhook-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `webhook_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `payment_id` | `bigint` | NO | FK → `payment.payment_id` | — | Referencia a `payment.payment_id`. *[R3]* |
| `gateway_event_id` | `character varying(100)` | NO | UQ | — | Identificador único del evento de la pasarela; garantiza que un webhook repetido no se procese dos veces. *[D]* |
| `payload` | `text` | NO | — | — | Cuerpo recibido del webhook. *[D]* |
| `received_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de recepción. *[D]* |

<a id="mod-profile"></a>

## 7. Módulo `profile`

### 7.1 `profile`

**Descripción:** Perfil público del usuario (nombre, biografía y avatar). *[D]*

**Creada en el changeset:** `032-create-profile-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `profile_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id`, UQ | — | Referencia a `app_user.user_id`. *[R3]* |
| `city_id` | `integer` | SÍ | FK → `city.city_id` | — | Referencia a `city.city_id`. *[R3]* |
| `stored_file_id` | `integer` | SÍ | FK → `stored_file.stored_file_id` | — | Archivo de la foto de perfil (entero, sin relación JPA con el módulo storage). |
| `full_name` | `character varying(30)` | NO | — | — | Nombre completo del usuario (máximo 30 caracteres). *[D]* |
| `bio` | `character varying(255)` | SÍ | — | — | Biografía del usuario (máximo 255 caracteres; se filtra lenguaje inapropiado). *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |
| `name_changed_at` | `timestamp without time zone` | SÍ | — | — | Fecha del último cambio de nombre; se exigen 14 días entre cambios (`UpdateProfileUseCase`). *[D]* |
| `preferred_language_id` | `bigint` | SÍ | FK → `language.language_id` | — | Idioma preferido del usuario para mostrar el contenido. |

### 7.2 `profile_log`

**Descripción:** Bitácora de cambios del perfil. *[D]*

**Creada en el changeset:** `033-create-profile-log-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `log_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `profile_id` | `bigint` | NO | FK → `profile.profile_id` | — | Referencia a `profile.profile_id`. *[R3]* |
| `action` | `character varying(50)` | SÍ | — | — | Acción registrada (por ejemplo, `FULL_NAME_CHANGED`). *[D]* |
| `old_value` | `character varying(255)` | SÍ | — | — | Valor anterior. *[D]* |
| `new_value` | `character varying(255)` | SÍ | — | — | Valor nuevo. *[D]* |
| `occurred_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora del cambio. *[D]* |

<a id="mod-seat"></a>

## 8. Módulo `seat`

### 8.1 `seat`

**Descripción:** Sillas de una sección de evento, con su estado y reserva temporal. *[D]*

**Creada en el changeset:** `056-create-seat.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `seat_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `seat_block_id` | `integer` | NO | FK → `seat_block.seat_block_id` | — | Referencia a `seat_block.seat_block_id`. *[R3]* |
| `event_section_id` | `integer` | NO | UQ, FK → `event_section.event_section_id` | — | Referencia a `event_section.event_section_id`. *[R3]* |
| `code` | `character varying(20)` | NO | UQ | — | Código de la silla, único dentro de la sección. *[D]* |
| `row` | `integer` | NO | — | — | Fila de la silla en el plano. *[D]* |
| `position` | `integer` | NO | — | — | Posición de la silla dentro de la fila. *[D]* |
| `type` | `character varying(20)` | NO | — | — | Valores permitidos: `REGULAR`, `COURTESY`. *[R4]* |
| `status` | `character varying(20)` | NO | — | `'available'::character varying` | Valores permitidos: `AVAILABLE`, `RESERVED`, `SOLD`, `COURTESY`. *[R4]* |
| `reserved_by` | `integer` | SÍ | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `reserved_until` | `timestamp without time zone` | SÍ | — | — | Fecha y hora en que vence la reserva temporal (10 minutos). *[D]* |

### 8.2 `seat_block`

**Descripción:** Bloques que agrupan sillas dentro de una sección. *[D]*

**Creada en el changeset:** `055-create-seat-block.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `seat_block_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_section_id` | `integer` | NO | FK → `event_section.event_section_id` | — | Referencia a `event_section.event_section_id`. *[R3]* |
| `name` | `character varying(100)` | NO | — | — | Nombre del bloque. *[D]* |
| `capacity` | `integer` | NO | — | — | Capacidad de sillas del bloque. *[D]* Restricción CHECK: `CHECK ((capacity > 0))`. |

### 8.3 `seat_log`

**Descripción:** Historial de cambios de estado de las sillas. *[D]*

**Creada en el changeset:** `057-create-seat-log.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `seat_log_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `seat_id` | `integer` | NO | FK → `seat.seat_id` | — | Referencia a `seat.seat_id`. *[R3]* |
| `old_status` | `character varying(20)` | SÍ | — | — | Valores permitidos: `AVAILABLE`, `RESERVED`, `SOLD`, `COURTESY`. *[R4]* |
| `new_status` | `character varying(20)` | NO | — | — | Valores permitidos: `AVAILABLE`, `RESERVED`, `SOLD`, `COURTESY`. *[R4]* |
| `changed_at` | `timestamp without time zone` | NO | — | `now()` | Fecha y hora del cambio. *[D]* |
| `changed_by` | `integer` | SÍ | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `reason` | `character varying(255)` | SÍ | — | — | Motivo del cambio de estado. *[D]* |

<a id="mod-section"></a>

## 9. Módulo `section`

### 9.1 `event_section`

**Descripción:** Secciones de un evento, con capacidad, sillas disponibles y precio. *[D]*

**Creada en el changeset:** `053-create-event-section.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `event_section_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `integer` | NO | FK → `event.event_id`, UQ | — | Referencia a `event.event_id`. *[R3]* |
| `section_type_id` | `integer` | NO | UQ, FK → `section_type.section_type_id` | — | Referencia a `section_type.section_type_id`. *[R3]* |
| `capacity` | `integer` | NO | — | — | Capacidad total de la sección. *[D]* Restricción CHECK: `CHECK ((capacity > 0))`. Restricción CHECK: `CHECK ((available_seats <= capacity))`. |
| `available_seats` | `integer` | NO | — | — | Sillas aún disponibles en la sección. *[D]* Restricción CHECK: `CHECK ((available_seats <= capacity))`. |
| `price` | `numeric(10,2)` | NO | — | — | Precio por silla de la sección. *[D]* Restricción CHECK: `CHECK ((price >= (0)::numeric))`. |
| `is_active` | `boolean` | NO | — | `true` | Indica si la sección está activa. *[D]* |

### 9.2 `section_type`

**Descripción:** Catálogo de tipos de sección. *[D]*

**Creada en el changeset:** `052-create-section-type.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `section_type_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `name` | `character varying(100)` | NO | — | — | Nombre del tipo de sección. *[D]* |
| `active` | `boolean` | NO | — | `true` | Indica si el tipo está activo. *[D]* |

<a id="mod-sectiontype"></a>

## 10. Módulo `sectiontype`

El módulo `sectiontype` no tiene tablas propias: el tipo de sección se guarda en `section_type` (módulo `section`) y sus traducciones en `section_type_translation` (módulo `language`).

<a id="mod-security"></a>

## 11. Módulo `security`

### 11.1 `account_activation`

**Descripción:** Códigos de activación de cuenta enviados por correo. *[D]*

**Creada en el changeset:** `007-create-account-activation-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `account_activation_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `activation_code` | `character varying(8)` | NO | — | — | Código de activación enviado por correo. *[D]* |
| `expires_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de vencimiento del código. *[D]* |
| `activated` | `boolean` | NO | — | `false` | Indica si el código ya se usó para activar la cuenta. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |

### 11.2 `account_lockout`

**Descripción:** Control de intentos fallidos de inicio de sesión y bloqueo temporal. *[D]*

**Creada en el changeset:** `008-create-account-lockout-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `account_lockout_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `failed_attempts` | `integer` | NO | — | `0` | Intentos fallidos dentro de la ventana actual. *[D]* |
| `window_started_at` | `timestamp without time zone` | SÍ | — | — | Inicio de la ventana de conteo de intentos fallidos. *[D]* |
| `locked_until` | `timestamp without time zone` | SÍ | — | — | Fecha y hora hasta la que la cuenta está bloqueada. *[D]* |

### 11.3 `account_reactivation_token`

**Descripción:** Códigos para reactivar cuentas desactivadas. *[D]*

**Creada en el changeset:** `060-create-account-reactivation-token-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `reactivation_token_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `token` | `character varying(8)` | NO | — | — | Código de reactivación enviado por correo. *[D]* |
| `token_status` | `boolean` | NO | — | `false` | `true` cuando el código ya se usó o se invalidó; `false` mientras está vigente. *[D]* |
| `expires_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de vencimiento del código. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |

### 11.4 `app_user`

**Descripción:** Usuarios de la plataforma, con rol y estado. *[D]*

**Creada en el changeset:** `004-create-user-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `user_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_status_id` | `integer` | NO | FK → `user_status.user_status_id` | — | Referencia a `user_status.user_status_id`. *[R3]* |
| `role_id` | `integer` | NO | FK → `role.role_id` | — | Referencia a `role.role_id`. *[R3]* |
| `alias` | `character varying(50)` | NO | UQ | — | Alias único del usuario. *[D]* |
| `activated` | `boolean` | NO | — | `false` | Indica si el usuario activó su cuenta. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 11.5 `auth_session`

**Descripción:** Sesiones autenticadas (token de acceso emitido, IP y navegador). *[D]*

**Creada en el changeset:** `009-create-auth-session-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `auth_session_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `access_token` | `character varying(512)` | NO | — | — | Token de acceso (JWT) emitido en la sesión. *[D]* |
| `issued_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de emisión. *[D]* |
| `expires_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de vencimiento. *[D]* |
| `ip_address` | `character varying(45)` | SÍ | — | — | Dirección IP del cliente (IPv4 o IPv6). *[D]* |
| `user_agent` | `character varying(200)` | SÍ | — | — | Agente de usuario (navegador o aplicación) del cliente. *[D]* |
| `revoked` | `boolean` | NO | — | `false` | Indica si la sesión fue revocada (por ejemplo, al cerrar sesión). *[D]* |
| `revoked_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la revocación. *[D]* |

### 11.6 `login_credentials`

**Descripción:** Credenciales de inicio de sesión con correo y contraseña. *[D]*

**Creada en el changeset:** `006-create-login-credentials-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `login_credentials_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `password_hash` | `character varying(255)` | NO | — | — | Hash BCrypt (costo 12) de la contraseña. *[D]* |
| `email` | `character varying(255)` | NO | UQ | — | Correo de inicio de sesión, único. *[D]* |
| `last_login_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora del último inicio de sesión. *[D]* |

### 11.7 `oauth_account`

**Descripción:** Cuentas de proveedores OAuth (Google, Facebook) vinculadas a un usuario. *[D]*

**Creada en el changeset:** `013-create-oauth-account-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `oauth_account_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | UQ, FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `provider` | `character varying(30)` | NO | UQ | — | Proveedor OAuth (por ejemplo, `google`). *[D]* |
| `provider_user_id` | `character varying(100)` | NO | UQ | — | Identificador del usuario en el proveedor. *[D]* |
| `email` | `character varying(255)` | SÍ | — | — | Correo informado por el proveedor. *[D]* |
| `linked_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la vinculación. *[D]* |

### 11.8 `organizer_petition`

**Descripción:** Solicitudes de usuarios para obtener el rol de organizador. *[D]*

**Creada en el changeset:** `015-create-organizer-petition-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `organizer_petition_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `application_date` | `timestamp without time zone` | NO | — | — | Fecha y hora de la solicitud. *[D]* |
| `status` | `character varying(20)` | NO | — | — | Estado de la solicitud (pendiente, aprobada o rechazada). *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |
| `stored_file_id` | `integer` | SÍ | FK → `stored_file.stored_file_id` | — | Referencia a `stored_file.stored_file_id`. *[R3]* |
| `document_type` | `character varying(50)` | SÍ | — | — | Tipo del documento adjunto a la solicitud. *[D]* |

### 11.9 `password_history`

**Descripción:** Historial de contraseñas para impedir reutilizar las recientes. *[D]*

**Creada en el changeset:** `012-create-password-history-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `password_history_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `password_hash` | `character varying(255)` | NO | — | — | Hash de una contraseña usada antes. *[D]* |
| `changed_at` | `timestamp without time zone` | NO | — | — | Fecha y hora del cambio. *[D]* |

### 11.10 `permission`

**Descripción:** Catálogo de permisos. *[D]*

**Creada en el changeset:** `003-create-permission-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `permission_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `code` | `character varying(50)` | NO | UQ | — | Código único del permiso. *[D]* |
| `name` | `character varying(80)` | NO | — | — | Nombre del permiso. *[D]* |
| `description` | `character varying(150)` | NO | — | — | Descripción del permiso. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 11.11 `recover_password`

**Descripción:** Códigos de recuperación de contraseña enviados por correo. *[D]*

**Creada en el changeset:** `011-create-recover-password-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `recover_password_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `code` | `character varying(8)` | NO | — | — | Código de recuperación enviado por correo. *[D]* |
| `code_status` | `boolean` | NO | — | `false` | `true` cuando el código ya se usó o se invalidó; `false` mientras está vigente. *[D]* |
| `expires_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de vencimiento del código. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `last_password_hash` | `character varying(255)` | SÍ | — | — | Hash de la contraseña anterior al restablecimiento (la entidad lo mapea, pero no se encontró uso en los casos de uso). *[D]* |

### 11.12 `refresh_token`

**Descripción:** Tokens de renovación (refresh) emitidos a los usuarios. *[D]*

**Creada en el changeset:** `010-create-refresh-token-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `refresh_token_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `auth_session_id` | `integer` | NO | FK → `auth_session.auth_session_id` | — | Referencia a `auth_session.auth_session_id`. *[R3]* |
| `token` | `character varying(512)` | NO | — | — | Token de renovación emitido. *[D]* |
| `issued_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de emisión. *[D]* |
| `expires_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de vencimiento (7 días). *[D]* |
| `revoked` | `boolean` | NO | — | `false` | Indica si el token fue revocado (cierre de sesión o rotación). *[D]* |
| `revoked_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la revocación. *[D]* |

### 11.13 `role`

**Descripción:** Catálogo de roles (USER, ORGANIZER, ADMIN). *[D]*

**Creada en el changeset:** `002-create-role-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `role_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `code` | `character varying(50)` | NO | UQ | — | Código único del rol. *[D]* |
| `name` | `character varying(80)` | NO | — | — | Nombre del rol. *[D]* |
| `description` | `character varying(150)` | NO | — | — | Descripción del rol. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 11.14 `role_permission`

**Descripción:** Relación entre roles y permisos. *[D]*

**Creada en el changeset:** `005-create-role-permission-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `role_permission_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `role_id` | `integer` | NO | FK → `role.role_id` | — | Referencia a `role.role_id`. *[R3]* |
| `permission_id` | `integer` | NO | FK → `permission.permission_id` | — | Referencia a `permission.permission_id`. *[R3]* |

### 11.15 `user_status`

**Descripción:** Catálogo de estados de cuenta de usuario. *[D]*

**Creada en el changeset:** `001-create-user-status-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `user_status_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `code` | `character varying(30)` | NO | UQ | — | Código único del estado de cuenta. *[D]* |
| `name` | `character varying(80)` | NO | — | — | Nombre del estado. *[D]* |
| `description` | `character varying(150)` | NO | — | — | Descripción del estado. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 11.16 `user_status_history`

**Descripción:** Historial de cambios de estado de cuenta. *[D]*

**Creada en el changeset:** `014-create-user-status-history-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `user_status_history_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `user_status_id` | `integer` | NO | FK → `user_status.user_status_id` | — | Referencia a `user_status.user_status_id`. *[R3]* |
| `reason` | `character varying(150)` | NO | — | — | Motivo del cambio de estado. *[D]* |

<a id="mod-storage"></a>

## 12. Módulo `storage`

### 12.1 `file_category`

**Descripción:** Categorías de archivos con sus extensiones permitidas y tamaño máximo. *[D]*

**Creada en el changeset:** `019-create-file-category-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `file_category_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `storage_provider_id` | `integer` | NO | FK → `storage_provider.storage_provider_id` | — | Referencia a `storage_provider.storage_provider_id`. *[R3]* |
| `name` | `character varying(100)` | NO | — | — | Nombre de la categoría de archivo. *[D]* |
| `code` | `character varying(50)` | NO | UQ | — | Código único de la categoría de archivo. *[D]* |
| `is_public` | `boolean` | NO | — | `false` | Indica si los archivos de la categoría son de acceso público. *[D]* |
| `allowed_extensions` | `character varying(100)` | NO | — | — | Extensiones de archivo permitidas en la categoría. *[D]* |
| `max_size_mb` | `integer` | NO | — | — | Tamaño máximo por archivo, en MB. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 12.2 `storage_operation_log`

**Descripción:** Registro inmutable de operaciones de almacenamiento: solo se agregan filas, nunca se actualizan (Javadoc de `StorageOperationLogEntity`).

**Creada en el changeset:** `021-create-storage-operation-log-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `log_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `stored_file_id` | `integer` | NO | FK → `stored_file.stored_file_id` | — | Referencia a `stored_file.stored_file_id`. *[R3]* |
| `performed_by` | `integer` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `operation` | `character varying(20)` | NO | — | — | Operación realizada sobre el almacenamiento (por ejemplo, subida o borrado). *[D]* |
| `status` | `character varying(20)` | NO | — | — | Resultado de la operación. *[D]* |
| `error_message` | `character varying(500)` | SÍ | — | — | Mensaje de error si la operación falló. *[D]* |
| `occurred_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la operación. *[D]* |

### 12.3 `storage_provider`

**Descripción:** Proveedores de almacenamiento de objetos configurados (MinIO, RustFS, etc.). *[D]*

**Creada en el changeset:** `018-create-storage-provider-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `storage_provider_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `name` | `character varying(100)` | NO | — | — | Nombre del proveedor de almacenamiento. *[D]* |
| `code` | `character varying(20)` | NO | UQ | — | Código único del proveedor. *[D]* |
| `is_active` | `boolean` | NO | — | `false` | Indica si es el proveedor activo. *[D]* |
| `config` | `jsonb` | NO | — | — | Configuración del proveedor en JSON. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 12.4 `stored_file`

**Descripción:** Metadatos de los archivos subidos al almacenamiento de objetos. *[D]*

**Creada en el changeset:** `020-create-stored-file-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `stored_file_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `file_category_id` | `integer` | NO | FK → `file_category.file_category_id` | — | Referencia a `file_category.file_category_id`. *[R3]* |
| `uploaded_by` | `integer` | NO | FK → `app_user.user_id` | — | ID del usuario que subió el archivo. |
| `s3_key` | `character varying(512)` | NO | — | — | Clave del objeto en el bucket (API compatible con S3). *[D]* |
| `original_filename` | `character varying(255)` | NO | — | — | Nombre original del archivo subido. *[D]* |
| `content_type` | `character varying(100)` | NO | — | — | Tipo MIME del archivo. *[D]* |
| `size_bytes` | `bigint` | NO | — | — | Tamaño del archivo en bytes. *[D]* |
| `public_url` | `character varying(512)` | SÍ | — | — | URL pública del archivo, si la categoría es pública. *[D]* |
| `owner_entity_id` | `integer` | SÍ | — | — | Identificador de la entidad dueña del archivo. *[D]* |
| `owner_entity_type` | `character varying(50)` | SÍ | — | — | Tipo de la entidad dueña (por ejemplo, `EVENT`). *[D]* |
| `deleted` | `boolean` | NO | — | `false` | Borrado lógico del archivo. *[D]* |
| `uploaded_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la subida. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(50)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

<a id="mod-theme"></a>

## 13. Módulo `theme`

### 13.1 `theme`

**Descripción:** Temas visuales de la interfaz. *[D]*

**Creada en el changeset:** `034-create-theme-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `theme_id` | `integer` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `name` | `character varying(100)` | NO | UQ | — | Nombre único del tema. *[D]* |
| `description` | `character varying(255)` | NO | — | — | Descripción del tema. *[D]* |
| `default_palette` | `jsonb` | NO | — | — | Paleta de colores por defecto del tema en JSON. *[D]* |

### 13.2 `theme_customization`

**Descripción:** Personalizaciones de propiedades de un tema hechas por un usuario. *[D]*

**Creada en el changeset:** `036-create-theme-customization-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `customization_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_theme_id` | `bigint` | NO | UQ, FK → `user_theme.user_theme_id` | — | Tema de usuario personalizado. |
| `property` | `character varying(100)` | NO | UQ | — | Propiedad del tema personalizada (única por usuario y tema). *[D]* |
| `value` | `character varying(255)` | NO | — | — | Valor personalizado de la propiedad. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |

### 13.3 `theme_log`

**Descripción:** Registro de auditoría de temas: solo se agregan filas, nunca se actualizan (Javadoc de `ThemeLogEntity`).

**Creada en el changeset:** `037-create-theme-log-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `log_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_theme_id` | `bigint` | NO | FK → `user_theme.user_theme_id` | — | Referencia a `user_theme.user_theme_id`. *[R3]* |
| `action` | `character varying(50)` | NO | — | — | Acción registrada sobre el tema. *[D]* |
| `property` | `character varying(100)` | SÍ | — | — | Propiedad afectada. *[D]* |
| `old_value` | `character varying(255)` | SÍ | — | — | Valor anterior. *[D]* |
| `new_value` | `character varying(255)` | SÍ | — | — | Valor nuevo. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |

### 13.4 `user_theme`

**Descripción:** Temas activados por cada usuario. *[D]*

**Creada en el changeset:** `035-create-user-theme-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `user_theme_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `integer` | NO | UQ, FK → `app_user.user_id` | — | Usuario al que pertenece el tema. |
| `theme_id` | `integer` | NO | UQ, FK → `theme.theme_id` | — | Tema seleccionado. |
| `is_active` | `boolean` | NO | — | `true` | Indica si es el tema activo del usuario. *[D]* |
| `created_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | SÍ | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |

<a id="mod-ticket"></a>

## 14. Módulo `ticket`

### 14.1 `order`

**Descripción:** Órdenes de compra de tickets. *[D]*

**Creada en el changeset:** `064-create-order-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `order_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `bigint` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `total_amount` | `numeric(19,4)` | NO | — | — | Total de la orden (suma de los precios de sección de las sillas). *[D]* |
| `currency` | `character varying(3)` | NO | — | `'COP'::character varying` | Código ISO 4217 de la moneda. *[D]* |
| `status` | `character varying(20)` | NO | — | `'DRAFT'::character varying` | Valores permitidos: `DRAFT`, `PAYMENT_PENDING`, `PAID`, `CANCELLED`, `REFUNDED`, `USED`. *[R4]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 14.2 `seat_ticket`

**Descripción:** Relación N:M entre asiento y ticket, con el precio guardado al momento de la compra (Javadoc de `SeatTicketEntity`).

**Creada en el changeset:** `066-create-seat-ticket-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `seat_ticket_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `seat_id` | `integer` | NO | UQ, FK → `seat.seat_id` | — | Referencia a `seat.seat_id`. *[R3]* |
| `ticket_id` | `bigint` | NO | UQ, FK → `ticket.ticket_id` | — | Referencia a `ticket.ticket_id`. *[R3]* |
| `price` | `numeric(19,4)` | NO | — | — | Precio de la silla al momento de la compra. *[D]* |

### 14.3 `ticket`

**Descripción:** Tickets emitidos para una orden, con código y QR. *[D]*

**Creada en el changeset:** `065-create-ticket-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `ticket_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `event_id` | `bigint` | NO | FK → `event.event_id` | — | Referencia a `event.event_id`. *[R3]* |
| `user_id` | `bigint` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |
| `order_id` | `bigint` | NO | FK → `order.order_id` | — | Referencia a `order.order_id`. *[R3]* |
| `code` | `character varying(50)` | NO | UQ | — | Código único del ticket. *[D]* |
| `qr_code` | `character varying(500)` | NO | — | — | Contenido del código QR del ticket. *[D]* |
| `status` | `character varying(20)` | NO | — | `'PENDING'::character varying` | Valores permitidos: `PENDING`, `PROCESSING`, `PAID`, `REFUNDED`, `USED`, `CANCELLED`. *[R4]* |
| `expiration_date` | `timestamp without time zone` | SÍ | — | — | Vencimiento del ticket; hoy se calcula como la fecha de creación más 30 días (`CreateOrderWithTicketsUseCaseImpl`). *[D]* |
| `total_price` | `numeric(19,4)` | NO | — | — | Precio total del ticket. *[D]* |
| `currency` | `character varying(3)` | NO | — | `'COP'::character varying` | Código ISO 4217 de la moneda. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 14.4 `ticket_checkin`

**Descripción:** Registros de ingreso (check-in) de tickets al evento. *[D]*

**Creada en el changeset:** `067-create-ticket-checkin-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `checkin_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `ticket_id` | `bigint` | NO | FK → `ticket.ticket_id`, UQ | — | Referencia a `ticket.ticket_id`. *[R3]* |
| `checkin_time` | `timestamp without time zone` | NO | — | — | Fecha y hora del ingreso. *[D]* |
| `validated_by` | `bigint` | NO | FK → `app_user.user_id` | — | Referencia a `app_user.user_id`. *[R3]* |

### 14.5 `ticket_log`

**Descripción:** Auditoría inmutable de tickets (Javadoc de `TicketLogEntity`).

**Creada en el changeset:** `068-create-ticket-log-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `ticket_log_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `ticket_id` | `bigint` | NO | FK → `ticket.ticket_id` | — | Referencia a `ticket.ticket_id`. *[R3]* |
| `old_status` | `character varying(20)` | SÍ | — | — | Valores permitidos: `PENDING`, `PROCESSING`, `PAID`, `REFUNDED`, `USED`, `CANCELLED`. *[R4]* |
| `new_status` | `character varying(20)` | NO | — | — | Valores permitidos: `PENDING`, `PROCESSING`, `PAID`, `REFUNDED`, `USED`, `CANCELLED`. *[R4]* |
| `changed_at` | `timestamp without time zone` | NO | — | — | Fecha y hora del cambio. *[D]* |
| `changed_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que hizo el cambio. *[D]* |
| `reason` | `character varying(255)` | SÍ | — | — | Motivo del cambio. *[D]* |

<a id="mod-wallet"></a>

## 15. Módulo `wallet`

### 15.1 `wallet`

**Descripción:** Billetera. Usa bloqueo optimista (`@Version`): si dos transacciones modifican la misma billetera a la vez, la segunda falla (Javadoc de `WalletEntity`).

**Creada en el changeset:** `074-create-wallet-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `wallet_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `user_id` | `bigint` | NO | UQ | — | Usuario dueño de la wallet (una por usuario). *[D]* |
| `balance` | `numeric(19,4)` | NO | — | `0` | Saldo disponible; nunca negativo. *[D]* Restricción CHECK: `CHECK ((balance >= (0)::numeric))`. |
| `currency` | `character varying(3)` | NO | — | `'COP'::character varying` | Valores permitidos: `COP`. *[R4]* |
| `version` | `bigint` | NO | — | `0` | Versión para bloqueo optimista. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `updated_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de la última modificación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
| `updated_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que modificó el registro por última vez (auditoría). *[R2]* |

### 15.2 `wallet_reference`

**Descripción:** Referencia de movimientos de billetera. Inmutable: no tiene columnas de auditoría ni versión (Javadoc de `WalletReferenceEntity`).

**Creada en el changeset:** `076-create-wallet-reference-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `wallet_reference_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `transaction_id` | `bigint` | NO | FK → `wallet_transaction.transaction_id` | — | Referencia a `wallet_transaction.transaction_id`. *[R3]* |
| `entity_type` | `character varying(30)` | NO | — | — | Valores permitidos: `EVENT_CANCELLATION`, `ORDER`, `TICKET`, `ADMIN_ADJUSTMENT`. *[R4]* |
| `entity_id` | `bigint` | SÍ | — | — | Identificador de la entidad de negocio relacionada con el movimiento. *[D]* |

### 15.3 `wallet_transaction`

**Descripción:** Movimiento de billetera. Inmutable: solo tiene `created_at` y `created_by` (Javadoc de `WalletTransactionEntity`).

**Creada en el changeset:** `075-create-wallet-transaction-table.yaml`

| Columna | Tipo | Nulo | Clave | Valor por defecto | Descripción |
|---|---|---|---|---|---|
| `transaction_id` | `bigint` | NO | PK | `IDENTITY (BY DEFAULT)` | Identificador único de la tabla (PK, IDENTITY). *[R1]* |
| `wallet_id` | `bigint` | NO | FK → `wallet.wallet_id` | — | Referencia a `wallet.wallet_id`. *[R3]* |
| `type` | `character varying(20)` | NO | — | — | Valores permitidos: `CREDIT`, `PAYMENT`, `REVERSAL`, `ADJUSTMENT`. *[R4]* |
| `amount` | `numeric(19,4)` | NO | — | — | Monto del movimiento (siempre positivo). *[D]* Restricción CHECK: `CHECK ((amount > (0)::numeric))`. |
| `status` | `character varying(20)` | NO | — | `'PENDING'::character varying` | Valores permitidos: `PENDING`, `COMPLETED`, `FAILED`. *[R4]* |
| `balance_after` | `numeric(19,4)` | SÍ | — | — | Saldo resultante después del movimiento. *[D]* |
| `idempotency_key` | `character varying(100)` | NO | UQ | — | Clave única que evita aplicar el mismo movimiento dos veces. *[D]* |
| `created_at` | `timestamp without time zone` | NO | — | — | Fecha y hora de creación del registro (auditoría). *[R2]* |
| `created_by` | `character varying(100)` | SÍ | — | — | Usuario o proceso que creó el registro (auditoría). *[R2]* |
