# Modelo entidad-relación — ALERTA MUJER

Generado el 28/10/2026 desde una base PostgreSQL 16 temporal con todas las migraciones Liquibase de la rama main aplicadas (v1.0.0 a v1.6.0, changeset 25): 35 tablas, 42 relaciones, en cinco esquemas.

- Script de creación: [database-schema.sql](database-schema.sql)
- Diccionario de datos: [data-dictionary.xlsx](data-dictionary.xlsx)
- Imágenes (SVG y PNG): `er-model-complete` y una por esquema (`er-model-<esquema>`).
- En cada diagrama de esquema, las tablas de otros esquemas aparecen solo con su llave para mostrar la relación.
- notification.tracking_link.alert_id no tiene llave foránea a propósito: ese esquema no depende de otro módulo.

Notación: `||--o{` uno a muchos, `||--o|` uno a uno, `|o--o{` relación opcional (la llave foránea admite nulos).

## identity — personas, cuentas, roles, perfiles y sesiones

```mermaid
erDiagram
    identity_account {
        bigint id PK "obligatorio"
        bigint user_id FK "obligatorio"
        varchar_255 password_hash "obligatorio"
        varchar_20 status "obligatorio"
        timestamp last_access
        timestamp updated_at
    }
    identity_admin_profile {
        bigint id PK "obligatorio"
        bigint account_id FK "obligatorio"
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    identity_alert_activation_setting {
        bigint id PK "obligatorio"
        bigint device_id FK "obligatorio"
        varchar_30 activation_method "obligatorio"
        boolean is_enabled "obligatorio"
        timestamp updated_at "obligatorio"
    }
    identity_device {
        bigint id PK "obligatorio"
        bigint account_id FK "obligatorio"
        varchar_255 device_uuid "obligatorio"
        varchar_255 fcm_token
        varchar_50 brand
        varchar_100 model
        varchar_30 os_name "obligatorio"
        varchar_30 os_version
        varchar_30 app_version
        varchar_20 gps_status "obligatorio"
        varchar_20 status "obligatorio"
        timestamp last_access
        timestamp created_at "obligatorio"
        timestamp updated_at
        varchar_100 device_name
        varchar_400 permissions
        timestamp permissions_updated_at
    }
    identity_device_permission {
        bigint id PK "obligatorio"
        bigint device_id FK "obligatorio"
        varchar_30 permission_type "obligatorio"
        varchar_20 status "obligatorio"
        timestamp updated_at "obligatorio"
    }
    identity_device_session {
        bigint id PK "obligatorio"
        bigint account_id FK "obligatorio"
        bigint device_id FK "obligatorio"
        varchar_255 refresh_token_hash "obligatorio"
        varchar_20 status "obligatorio"
        timestamp expires_at "obligatorio"
        timestamp last_used_at
        timestamp revoked_at
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    identity_recovery_request {
        bigint id PK "obligatorio"
        bigint account_id FK "obligatorio"
        varchar_20 method "obligatorio"
        varchar_255 token_hash "obligatorio"
        timestamp expires_at "obligatorio"
        timestamp used_at
        varchar_20 status "obligatorio"
        bigint attempts "obligatorio"
        timestamp created_at
        varchar_45 requested_ip
        timestamp updated_at
    }
    identity_role {
        bigint id PK "obligatorio"
        varchar_30 name "obligatorio"
        varchar_255 description
        timestamp updated_at
    }
    identity_user_preference {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        varchar_10 language "obligatorio"
        varchar_20 theme "obligatorio"
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    identity_user_profile {
        bigint id PK "obligatorio"
        bigint user_id FK "obligatorio"
        text profile_photo_url
        boolean tutorial_completed "obligatorio"
        timestamp tutorial_seen_at
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    identity_users {
        bigint id PK "obligatorio"
        bigint role_id FK "obligatorio"
        varchar_100 first_name "obligatorio"
        varchar_100 last_name "obligatorio"
        varchar_20 telephone
        varchar_150 email "obligatorio"
        varchar_20 document_number
        varchar_20 document_type
        date birthdate
        timestamp created_at "obligatorio"
        timestamp updated_at
        timestamp terms_accepted_at
        varchar_20 terms_version
    }
    alert_alert {
        bigint id PK "obligatorio"
    }
    alert_emergency_contact {
        bigint id PK "obligatorio"
    }
    alert_frequent_location {
        bigint id PK "obligatorio"
    }
    alert_location_log {
        bigint id PK "obligatorio"
    }
    alert_notification {
        bigint id PK "obligatorio"
    }
    alert_safe_trip {
        bigint id PK "obligatorio"
    }
    alert_saved_route {
        bigint id PK "obligatorio"
    }
    admin_account_deletion_request {
        bigint id PK "obligatorio"
    }
    admin_alert_report {
        bigint id PK "obligatorio"
    }
    admin_audit_log {
        bigint id PK "obligatorio"
    }
    admin_user_report {
        bigint id PK "obligatorio"
    }
    resource_resource_call {
        bigint id PK "obligatorio"
    }
    resource_zone_report {
        bigint id PK "obligatorio"
    }
    identity_users |o--o{ admin_account_deletion_request : "decided_by"
    identity_users ||--o{ admin_account_deletion_request : "requested_by"
    identity_users ||--o{ admin_account_deletion_request : "user_id"
    identity_users |o--o{ admin_alert_report : "created_by_user_id"
    identity_account |o--o{ admin_audit_log : "account_id"
    identity_users ||--o{ admin_user_report : "reported_user_id"
    identity_users |o--o{ admin_user_report : "reporter_user_id"
    identity_device ||--o{ alert_alert : "device_id"
    identity_user_profile ||--o{ alert_alert : "user_profile_id"
    identity_user_profile ||--o{ alert_emergency_contact : "user_profile_id"
    identity_user_profile ||--o{ alert_frequent_location : "user_profile_id"
    identity_user_profile ||--o{ alert_location_log : "user_profile_id"
    identity_user_profile ||--o{ alert_notification : "user_profile_id"
    identity_user_profile ||--o{ alert_safe_trip : "user_profile_id"
    identity_user_profile ||--o{ alert_saved_route : "user_profile_id"
    identity_users ||--o| identity_account : "user_id"
    identity_account ||--o| identity_admin_profile : "account_id"
    identity_device ||--o{ identity_alert_activation_setting : "device_id"
    identity_account ||--o{ identity_device : "account_id"
    identity_device ||--o{ identity_device_permission : "device_id"
    identity_account ||--o{ identity_device_session : "account_id"
    identity_device ||--o{ identity_device_session : "device_id"
    identity_account ||--o{ identity_recovery_request : "account_id"
    identity_user_profile ||--o| identity_user_preference : "user_profile_id"
    identity_users ||--o| identity_user_profile : "user_id"
    identity_role ||--o{ identity_users : "role_id"
    identity_device |o--o{ resource_resource_call : "device_id"
    identity_user_profile ||--o{ resource_resource_call : "user_profile_id"
    identity_user_profile ||--o{ resource_zone_report : "user_profile_id"
```

## alert — alertas SOS, contactos, ubicaciones, evidencias, chat, trayectos y protección automática

```mermaid
erDiagram
    alert_alert {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        bigint device_id FK "obligatorio"
        varchar_20 alert_type "obligatorio"
        varchar_30 activation_method "obligatorio"
        varchar_20 status "obligatorio"
        varchar_500 message
        numeric_9_6 latitude
        numeric_9_6 longitude
        timestamp started_at "obligatorio"
        timestamp ended_at
        timestamp cancelled_at
        timestamp created_at "obligatorio"
        timestamp updated_at
        boolean keep_evidence "obligatorio"
        timestamp escalated_at
        timestamp extended_until
        timestamp unanswered_at
        timestamp duress_at
        timestamp last_signal_at
        timestamp signal_lost_at
    }
    alert_alert_contact {
        bigint id PK "obligatorio"
        bigint alert_id FK "obligatorio"
        bigint emergency_contact_id FK
        varchar_20 channel "obligatorio"
        varchar_150 destination "obligatorio"
        varchar_20 status "obligatorio"
        timestamp sent_at
        timestamp delivered_at
        bigint attempts "obligatorio"
        varchar_500 error_message
        timestamp created_at "obligatorio"
        timestamp updated_at
        timestamp seen_at
    }
    alert_alert_message {
        bigint id PK "obligatorio"
        bigint alert_id FK "obligatorio"
        varchar_10 sender_type "obligatorio"
        varchar_60 sender_name "obligatorio"
        varchar_500 body "obligatorio"
        timestamp created_at "obligatorio"
    }
    alert_alert_reminder {
        bigint id PK "obligatorio"
        bigint alert_id FK "obligatorio"
        varchar_20 reminder_type "obligatorio"
        integer sequence_number "obligatorio"
        timestamp scheduled_at "obligatorio"
        timestamp sent_at
        varchar_20 status "obligatorio"
        varchar_500 error_message
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    alert_emergency_contact {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        varchar_100 contact_name "obligatorio"
        varchar_20 telephone "obligatorio"
        varchar_150 email
        varchar_30 relationship
        timestamp created_at "obligatorio"
        timestamp updated_at
        text photo
    }
    alert_evidence {
        bigint id PK "obligatorio"
        bigint alert_id FK "obligatorio"
        varchar_20 media_type "obligatorio"
        varchar_255 file_url "obligatorio"
        timestamp created_at "obligatorio"
        timestamp updated_at
        varchar_255 file_name
        varchar_100 content_type
        bigint size_bytes
        char_64 sha256
        varchar_100 storage_key
        varchar_20 review_status "obligatorio"
    }
    alert_frequent_location {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        varchar_100 name "obligatorio"
        varchar_255 address
        varchar_100 city
        numeric_9_6 latitude "obligatorio"
        numeric_9_6 longitude "obligatorio"
        varchar_255 notes
        varchar_20 risk_level "obligatorio"
        boolean is_active "obligatorio"
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    alert_location_log {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        integer alert_id FK
        numeric_9_6 latitude "obligatorio"
        numeric_9_6 longitude "obligatorio"
        numeric_6_2 accuracy
        timestamp recorded_at "obligatorio"
    }
    alert_notification {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        integer alert_id FK
        varchar_30 type "obligatorio"
        varchar_20 priority "obligatorio"
        varchar_100 title "obligatorio"
        varchar_500 message "obligatorio"
        boolean is_read "obligatorio"
        boolean is_persistent "obligatorio"
        timestamp expires_at
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    alert_protection_device {
        bigint id PK "obligatorio"
        bigint user_profile_id "obligatorio"
        bigint user_id "obligatorio"
        bigint device_id "obligatorio"
        varchar_64 key_hash "obligatorio"
        timestamp created_at "obligatorio"
    }
    alert_safe_trip {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        bigint user_id "obligatorio"
        bigint device_id "obligatorio"
        varchar_120 destination "obligatorio"
        numeric_9_6 destination_latitude
        numeric_9_6 destination_longitude
        timestamp started_at "obligatorio"
        timestamp expected_arrival "obligatorio"
        varchar_12 status "obligatorio"
        boolean reminder_sent "obligatorio"
        bigint alert_id FK
        timestamp ended_at
        timestamp created_at "obligatorio"
        timestamp last_seen_at
        numeric_9_6 last_latitude
        numeric_9_6 last_longitude
        varchar_20 alert_reason
        varchar_10 mode "obligatorio"
        timestamp low_battery_at
    }
    alert_saved_route {
        bigint id PK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        varchar_150 name "obligatorio"
        varchar_150 start_label
        varchar_150 end_label
        varchar_100 city
        varchar_100 department
        varchar_100 country
        numeric_8_2 distance_km
        integer estimated_minutes
        jsonb points "obligatorio"
        boolean is_starred "obligatorio"
        boolean is_manual "obligatorio"
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    identity_device {
        bigint id PK "obligatorio"
    }
    identity_user_profile {
        bigint id PK "obligatorio"
    }
    resource_resource_call {
        bigint id PK "obligatorio"
    }
    notification_tracking_view {
        bigint alert_id PK, FK "obligatorio"
        varchar_64 visitor PK "obligatorio"
    }
    identity_device ||--o{ alert_alert : "device_id"
    identity_user_profile ||--o{ alert_alert : "user_profile_id"
    alert_alert ||--o{ alert_alert_contact : "alert_id"
    alert_emergency_contact |o--o{ alert_alert_contact : "emergency_contact_id"
    alert_alert ||--o{ alert_alert_message : "alert_id"
    alert_alert ||--o{ alert_alert_reminder : "alert_id"
    identity_user_profile ||--o{ alert_emergency_contact : "user_profile_id"
    alert_alert ||--o{ alert_evidence : "alert_id"
    identity_user_profile ||--o{ alert_frequent_location : "user_profile_id"
    alert_alert |o--o{ alert_location_log : "alert_id"
    identity_user_profile ||--o{ alert_location_log : "user_profile_id"
    alert_alert |o--o{ alert_notification : "alert_id"
    identity_user_profile ||--o{ alert_notification : "user_profile_id"
    alert_alert |o--o{ alert_safe_trip : "alert_id"
    identity_user_profile ||--o{ alert_safe_trip : "user_profile_id"
    identity_user_profile ||--o{ alert_saved_route : "user_profile_id"
    alert_alert ||--o{ notification_tracking_view : "alert_id"
    alert_alert |o--o{ resource_resource_call : "alert_id"
```

## admin — configuración, auditoría, moderación, informes y eliminación de cuentas

```mermaid
erDiagram
    admin_account_deletion_request {
        bigint id PK "obligatorio"
        bigint user_id FK "obligatorio"
        bigint requested_by FK "obligatorio"
        varchar_500 reason "obligatorio"
        varchar_10 status "obligatorio"
        bigint decided_by FK
        varchar_500 decision_note
        timestamp created_at "obligatorio"
        timestamp decided_at
    }
    admin_alert_report {
        bigint id PK "obligatorio"
        varchar_200 name "obligatorio"
        varchar_100 city
        varchar_20 status "obligatorio"
        varchar_200 user_label
        varchar_200 event
        text description
        varchar_10 format "obligatorio"
        text alert_ids
        timestamp from_date
        timestamp to_date
        varchar_20 alert_status
        integer export_count "obligatorio"
        bigint created_by_user_id FK
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    admin_audit_log {
        bigint id PK "obligatorio"
        bigint account_id FK
        varchar_100 action "obligatorio"
        varchar_50 entity_type "obligatorio"
        integer entity_id
        varchar_500 description
        varchar_45 ip_address
        timestamp created_at "obligatorio"
        varchar_120 source
    }
    admin_moderation_action {
        bigint id PK "obligatorio"
        bigint user_report_id FK "obligatorio"
        integer admin_perfil_id "obligatorio"
        varchar_30 action_type "obligatorio"
        varchar_1000 notes
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    admin_system_configuration {
        bigint id PK "obligatorio"
        varchar_10 singleton_key "obligatorio"
        integer sos_max_duration_minutes "obligatorio"
        varchar_20 emergency_phone_number "obligatorio"
        varchar_150 admin_email "obligatorio"
        boolean notifications_enabled "obligatorio"
        varchar_20 default_notification_priority "obligatorio"
        varchar_20 default_theme "obligatorio"
        integer updated_by_admin_id
        timestamp created_at "obligatorio"
        timestamp updated_at
        integer max_emergency_contacts "obligatorio"
        varchar_20 min_app_version
    }
    admin_user_report {
        bigint id PK "obligatorio"
        bigint reported_user_id FK "obligatorio"
        integer reporter_user_id FK
        varchar_100 reason "obligatorio"
        varchar_1000 description "obligatorio"
        varchar_20 status "obligatorio"
        integer reviewed_by_admin_id
        timestamp reviewed_at
        varchar_1000 resolution_notes
        timestamp created_at "obligatorio"
        timestamp updated_at
        varchar_10 severity "obligatorio"
        text evidence
    }
    identity_account {
        bigint id PK "obligatorio"
    }
    identity_users {
        bigint id PK "obligatorio"
    }
    identity_users |o--o{ admin_account_deletion_request : "decided_by"
    identity_users ||--o{ admin_account_deletion_request : "requested_by"
    identity_users ||--o{ admin_account_deletion_request : "user_id"
    identity_users |o--o{ admin_alert_report : "created_by_user_id"
    identity_account |o--o{ admin_audit_log : "account_id"
    admin_user_report ||--o{ admin_moderation_action : "user_report_id"
    identity_users ||--o{ admin_user_report : "reported_user_id"
    identity_users |o--o{ admin_user_report : "reporter_user_id"
```

## resource — zonas y puntos de ayuda

```mermaid
erDiagram
    resource_emergency_resource {
        bigint id PK "obligatorio"
        varchar_150 name "obligatorio"
        varchar_40 resource_type "obligatorio"
        varchar_30 telephone
        varchar_30 secondary_telephone
        varchar_150 email
        varchar_255 address
        varchar_100 city "obligatorio"
        numeric_9_6 latitude
        numeric_9_6 longitude
        varchar_500 description
        boolean is_active "obligatorio"
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    resource_resource_call {
        bigint id PK "obligatorio"
        bigint emergency_resource_id FK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        integer alert_id FK
        integer device_id FK
        varchar_30 telephone_dialed "obligatorio"
        varchar_20 status "obligatorio"
        timestamp started_at "obligatorio"
        timestamp ended_at
        integer duration_seconds
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    resource_zone {
        bigint id PK "obligatorio"
        integer created_by_admin_id
        varchar_150 name "obligatorio"
        varchar_20 zone_type "obligatorio"
        varchar_20 risk_level "obligatorio"
        varchar_500 description
        varchar_255 address
        varchar_100 city "obligatorio"
        numeric_9_6 latitude "obligatorio"
        numeric_9_6 longitude "obligatorio"
        numeric_8_2 radius_meters "obligatorio"
        boolean is_active "obligatorio"
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    resource_zone_report {
        bigint id PK "obligatorio"
        bigint zone_id FK "obligatorio"
        bigint user_profile_id FK "obligatorio"
        varchar_20 classification "obligatorio"
        varchar_500 comment
        varchar_20 status "obligatorio"
        integer reviewed_by_admin_id
        timestamp reviewed_at
        timestamp created_at "obligatorio"
        timestamp updated_at
    }
    identity_device {
        bigint id PK "obligatorio"
    }
    identity_user_profile {
        bigint id PK "obligatorio"
    }
    alert_alert {
        bigint id PK "obligatorio"
    }
    alert_alert |o--o{ resource_resource_call : "alert_id"
    identity_device |o--o{ resource_resource_call : "device_id"
    resource_emergency_resource ||--o{ resource_resource_call : "emergency_resource_id"
    identity_user_profile ||--o{ resource_resource_call : "user_profile_id"
    identity_user_profile ||--o{ resource_zone_report : "user_profile_id"
    resource_zone ||--o{ resource_zone_report : "zone_id"
```

## notification — enlaces de seguimiento y sus visitas

```mermaid
erDiagram
    notification_tracking_link {
        bigint id PK "obligatorio"
        bigint alert_id "obligatorio"
        char_64 token_hash "obligatorio"
        timestamp created_at "obligatorio"
        timestamp revoked_at
    }
    notification_tracking_view {
        bigint alert_id PK, FK "obligatorio"
        varchar_64 visitor PK "obligatorio"
        timestamp first_seen "obligatorio"
        timestamp last_seen "obligatorio"
    }
    alert_alert {
        bigint id PK "obligatorio"
    }
    alert_alert ||--o{ notification_tracking_view : "alert_id"
```
