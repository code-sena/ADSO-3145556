-- =============================================================================
-- EnergyMonitor - Script de la base de datos (esquema compartido energy_monitor)
-- =============================================================================
-- Proyecto SENA ADSO 3145556 - Monitor de consumo electrico del hogar.
--
-- Generado a partir de los changelogs de Liquibase de los siete servicios con
-- base de datos (src/main/resources/db/changelog de cada repositorio), en el orden
-- de su db.changelog-master.xml, con el estado final de cada tabla despues de
-- aplicar todos los changesets (security-001..030, device-001..009,
-- measurement-001..004, home-001..007, notification-001..007, alert-001..004,
-- recommendation-001..002).
--
-- Fuente de verdad: Liquibase. En una instalacion real NO se ejecuta este script:
-- cada servicio crea y migra sus propias tablas al arrancar y registra lo aplicado
-- en DATABASECHANGELOG / DATABASECHANGELOGLOCK (tablas de Liquibase, no incluidas).
-- Este archivo sirve como referencia, para revisar el modelo o para crear una base
-- vacia de pruebas (MySQL 8.4, InnoDB, utf8mb4).
--
-- Reglas del modelo:
--   * Un solo esquema (energy_monitor); cada servicio es dueno de sus tablas y solo
--     el las escribe. Los demas servicios piden los datos por su API interna
--     (/internal/** con X-Internal-Token) o los reciben por eventos de RabbitMQ.
--   * Llaves foraneas SOLO dentro de un mismo servicio. Las referencias a tablas de
--     otro servicio (user_id, home_id, device_id, measurement_id...) son columnas
--     VARCHAR(10) sin FOREIGN KEY: se validan en la aplicacion.
--   * Identificadores VARCHAR(10) generados por la aplicacion.
--   * Borrado logico con deleted_at en las tablas de negocio.
--   * Cada servicio que publica eventos tiene su tabla *_outbox (outbox transaccional).
--   * La tabla backend_outbox del backend retirado (energy-monitor-backend) puede
--     seguir existiendo en bases migradas, sin uso; no se crea aqui.
-- =============================================================================

CREATE DATABASE IF NOT EXISTS energy_monitor
  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE energy_monitor;
SET FOREIGN_KEY_CHECKS = 0;


-- =============================================================================
-- Servicio: en-monitor-security-service (:8082)
-- =============================================================================

-- person: Personal data of a system user, kept apart from credentials.
-- Changesets: security-001-create-person, security-018-drop-person-contact-data, security-020-drop-person-cellphone
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id_person` VARCHAR(10) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `last_name` VARCHAR(100) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_person` PRIMARY KEY (`id_person`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- user: Credentials and account state. One user per person.
-- Changesets: security-002-create-user, security-017-add-user-profile-image, security-022-widen-user-profile-image, security-024-add-user-google-subject, security-029-add-user-terms-accepted-at
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id_user` VARCHAR(10) NOT NULL,
  `person_id` VARCHAR(10) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `email_verified` BOOLEAN NOT NULL,
  `registration_date` TIMESTAMP NOT NULL,
  `status` ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') NOT NULL,
  `failed_login_attempts` INT NOT NULL,
  `last_login_at` TIMESTAMP NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  `profile_image` MEDIUMTEXT NULL,
  `google_subject` VARCHAR(255) NULL,
  `terms_accepted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_user` PRIMARY KEY (`id_user`),
  CONSTRAINT `uk_user_person_id` UNIQUE (`person_id`),
  CONSTRAINT `uk_user_email` UNIQUE (`email`),
  CONSTRAINT `uq_user_google_subject` UNIQUE (`google_subject`),
  CONSTRAINT `fk_user_person_id` FOREIGN KEY (`person_id`) REFERENCES `person` (`id_person`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- user_configuration: Per-user notification and interface preferences.
-- Changesets: security-003-create-user-configuration
DROP TABLE IF EXISTS `user_configuration`;
CREATE TABLE `user_configuration` (
  `id_configuration` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NOT NULL,
  `notify_by_email` BOOLEAN NOT NULL,
  `notify_by_push` BOOLEAN NOT NULL,
  `color_theme` VARCHAR(50) NULL,
  `language` VARCHAR(50) NULL,
  `social_provider` VARCHAR(50) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_user_configuration` PRIMARY KEY (`id_configuration`),
  CONSTRAINT `uk_user_configuration_user_id` UNIQUE (`user_id`),
  CONSTRAINT `fk_user_configuration_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- password_reset_token: Single-use token issued for a password reset request. Stores only the
-- SHA-256 hash, never the secret.
-- Changesets: security-004-create-password-reset-token, security-021-add-password-reset-token-attempts
DROP TABLE IF EXISTS `password_reset_token`;
CREATE TABLE `password_reset_token` (
  `id_reset_token` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NOT NULL,
  `reset_token_hash` VARCHAR(64) NOT NULL,
  `used` BOOLEAN NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expiration_at` TIMESTAMP NOT NULL,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  `attempts` INT NOT NULL DEFAULT 0,
  CONSTRAINT `pk_password_reset_token` PRIMARY KEY (`id_reset_token`),
  CONSTRAINT `uk_password_reset_token_reset_token_hash` UNIQUE (`reset_token_hash`),
  CONSTRAINT `fk_password_reset_token_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- user_session: Active session backing a refresh token.
-- Changesets: security-005-create-user-session, security-014-add-session-revocation, security-016-drop-session-refresh-token
DROP TABLE IF EXISTS `user_session`;
CREATE TABLE `user_session` (
  `id_user_session` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NOT NULL,
  `revoked` BOOLEAN NOT NULL,
  `ip_address` VARCHAR(45) NULL,
  `user_agent` VARCHAR(150) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expiration_at` TIMESTAMP NOT NULL,
  `closed_at` TIMESTAMP NULL,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  `revoked_at` TIMESTAMP NULL,
  `revoked_reason` ENUM('REFRESH_TOKEN_REUSE', 'PASSWORD_CHANGED', 'ADMINISTRATIVE') NULL,
  CONSTRAINT `pk_user_session` PRIMARY KEY (`id_user_session`),
  CONSTRAINT `fk_user_session_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- security_configuration: Key-value security settings applied system wide.
-- Changesets: security-006-create-security-configuration
DROP TABLE IF EXISTS `security_configuration`;
CREATE TABLE `security_configuration` (
  `id_security_configuration` VARCHAR(10) NOT NULL,
  `config_name` VARCHAR(100) NOT NULL,
  `config_value` VARCHAR(100) NOT NULL,
  `description` VARCHAR(255) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_security_configuration` PRIMARY KEY (`id_security_configuration`),
  CONSTRAINT `uk_security_configuration_config_name` UNIQUE (`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- password_policy: Rules a password must satisfy.
-- Changesets: security-007-create-password-policy
DROP TABLE IF EXISTS `password_policy`;
CREATE TABLE `password_policy` (
  `id_password_policy` VARCHAR(10) NOT NULL,
  `min_length` INT NOT NULL,
  `max_length` INT NOT NULL,
  `require_uppercase` BOOLEAN NOT NULL,
  `require_numbers` BOOLEAN NOT NULL,
  `require_symbols` BOOLEAN NOT NULL,
  `expiration_days` INT NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_password_policy` PRIMARY KEY (`id_password_policy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- login_error_log: Failed login attempts. user_id is nullable: the account may not exist.
-- Changesets: security-008-create-login-error-log, security-027-login-error-email-not-verified
DROP TABLE IF EXISTS `login_error_log`;
CREATE TABLE `login_error_log` (
  `id_login_error` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NULL,
  `error_type` ENUM('INVALID_PASSWORD', 'USER_NOT_FOUND', 'ACCOUNT_BLOCKED', 'ACCOUNT_INACTIVE', 'EMAIL_NOT_VERIFIED') NOT NULL,
  `description` VARCHAR(500) NULL,
  `ip_address` VARCHAR(45) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_login_error_log` PRIMARY KEY (`id_login_error`),
  CONSTRAINT `fk_login_error_log_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- system_role: Global RBAC role, independent from home-scoped roles.
-- Changesets: security-009-create-system-role
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `id_system_role` VARCHAR(10) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(200) NULL,
  `enabled` BOOLEAN NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_system_role` PRIMARY KEY (`id_system_role`),
  CONSTRAINT `uk_system_role_name` UNIQUE (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- permission: Atomic action a role can be granted.
-- Changesets: security-010-create-permission
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id_permission` VARCHAR(10) NOT NULL,
  `code` VARCHAR(30) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(100) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_permission` PRIMARY KEY (`id_permission`),
  CONSTRAINT `uk_permission_code` UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- user_system_role: Assignment of a global role to a user.
-- Changesets: security-011-create-user-system-role
DROP TABLE IF EXISTS `user_system_role`;
CREATE TABLE `user_system_role` (
  `user_id` VARCHAR(10) NOT NULL,
  `system_role_id` VARCHAR(10) NOT NULL,
  `assigned_at` TIMESTAMP NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_user_system_role` PRIMARY KEY (`user_id`, `system_role_id`),
  CONSTRAINT `fk_user_system_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id_user`),
  CONSTRAINT `fk_user_system_role_system_role_id` FOREIGN KEY (`system_role_id`) REFERENCES `system_role` (`id_system_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- system_role_permission: Assignment of a permission to a global role.
-- Changesets: security-012-create-system-role-permission
DROP TABLE IF EXISTS `system_role_permission`;
CREATE TABLE `system_role_permission` (
  `system_role_id` VARCHAR(10) NOT NULL,
  `permission_id` VARCHAR(10) NOT NULL,
  `assigned_at` TIMESTAMP NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_system_role_permission` PRIMARY KEY (`system_role_id`, `permission_id`),
  CONSTRAINT `fk_system_role_permission_system_role_id` FOREIGN KEY (`system_role_id`) REFERENCES `system_role` (`id_system_role`),
  CONSTRAINT `fk_system_role_permission_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id_permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- audit_log: Audit trail of security-relevant actions. user_id is nullable: the actor may be
-- unknown.
-- Changesets: security-013-create-audit-log
DROP TABLE IF EXISTS `audit_log`;
CREATE TABLE `audit_log` (
  `id_audit_log` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NULL,
  `action` ENUM('CREATE', 'UPDATE', 'DELETE', 'LOGIN', 'LOGIN_FAILED', 'LOGOUT') NOT NULL,
  `description` VARCHAR(255) NULL,
  `ip_address` VARCHAR(45) NULL,
  `application` VARCHAR(255) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_audit_log` PRIMARY KEY (`id_audit_log`),
  CONSTRAINT `fk_audit_log_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- refresh_token: Refresh token generations with family lineage. Stores only the SHA-256 hash,
-- never the secret.
-- Changesets: security-015-create-refresh-token
DROP TABLE IF EXISTS `refresh_token`;
CREATE TABLE `refresh_token` (
  `id_refresh_token` VARCHAR(10) NOT NULL,
  `id_user_session` VARCHAR(10) NOT NULL,
  `family_id` VARCHAR(10) NOT NULL,
  `parent_id` VARCHAR(10) NULL,
  `token_hash` VARCHAR(64) NOT NULL,
  `status` ENUM('ACTIVE', 'ROTATED', 'REVOKED', 'EXPIRED') NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_at` TIMESTAMP NOT NULL,
  `rotated_at` TIMESTAMP NULL,
  `revoked_at` TIMESTAMP NULL,
  `revoked_reason` ENUM('REFRESH_TOKEN_REUSE', 'PASSWORD_CHANGED', 'ADMINISTRATIVE') NULL,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_refresh_token` PRIMARY KEY (`id_refresh_token`),
  CONSTRAINT `uk_refresh_token_hash` UNIQUE (`token_hash`),
  INDEX `ix_refresh_token_family_id` (`family_id`),
  INDEX `ix_refresh_token_user_session` (`id_user_session`),
  CONSTRAINT `fk_refresh_token_user_session` FOREIGN KEY (`id_user_session`) REFERENCES `user_session` (`id_user_session`),
  CONSTRAINT `fk_refresh_token_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `refresh_token` (`id_refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- security_outbox: Creates the security_outbox table of the transactional outbox.
-- Changesets: security-026-create-security-outbox
DROP TABLE IF EXISTS `security_outbox`;
CREATE TABLE `security_outbox` (
  `id_message` VARCHAR(10) NOT NULL,
  `message_type` VARCHAR(60) NOT NULL,
  `payload` TEXT NULL,
  `occurred_at` DATETIME(6) NOT NULL,
  `expires_at` DATETIME(6) NULL,
  `published_at` DATETIME(6) NULL,
  `discarded_at` DATETIME(6) NULL,
  `attempts` INT NOT NULL DEFAULT 0,
  `last_error` VARCHAR(255) NULL,
  CONSTRAINT `pk_security_outbox` PRIMARY KEY (`id_message`),
  INDEX `idx_security_outbox_pending` (`published_at`, `discarded_at`, `occurred_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- pending_registration: Creates the pending_registration table: registrations not verified
-- yet.
-- Changesets: security-028-create-pending-registration
DROP TABLE IF EXISTS `pending_registration`;
CREATE TABLE `pending_registration` (
  `id_user` VARCHAR(10) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `last_name` VARCHAR(100) NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `profile_image` MEDIUMTEXT NULL,
  `ip_address` VARCHAR(45) NULL,
  `requested_at` DATETIME(6) NOT NULL,
  `expires_at` DATETIME(6) NOT NULL,
  CONSTRAINT `pk_pending_registration` PRIMARY KEY (`id_user`),
  CONSTRAINT `uq_pending_registration_email` UNIQUE (`email`),
  INDEX `idx_pending_registration_expires_at` (`expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- Servicio: en-monitor-device-service (:8084)
-- =============================================================================

-- appliance_type: Catalog of appliance types (refrigerator, air conditioner, lighting).
-- Changesets: device-001-create-appliance-type
DROP TABLE IF EXISTS `appliance_type`;
CREATE TABLE `appliance_type` (
  `id_appliance_type` VARCHAR(10) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_appliance_type` PRIMARY KEY (`id_appliance_type`),
  CONSTRAINT `uk_appliance_type_name` UNIQUE (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- device: Physical measurement device. installation_date is the business date, created_at is
-- the audit timestamp.
-- Changesets: device-002-create-device, device-007-add-api-key-hash, device-008-drop-clear-api-key
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id_device` VARCHAR(10) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `appliance_type_id` VARCHAR(10) NOT NULL,
  `location` VARCHAR(50) NULL,
  `description` VARCHAR(200) NULL,
  `installation_date` TIMESTAMP NOT NULL,
  `device_code` VARCHAR(6) NOT NULL,
  `api_key_hash` VARCHAR(64) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_device` PRIMARY KEY (`id_device`),
  CONSTRAINT `uk_device_device_code` UNIQUE (`device_code`),
  CONSTRAINT `fk_device_appliance_type_id` FOREIGN KEY (`appliance_type_id`) REFERENCES `appliance_type` (`id_appliance_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- device_status_log: History of the device connection state. device_id is nullable following
-- the log-table pattern.
-- Changesets: device-003-create-device-status-log, device-005-status-log-last-seen-precision
DROP TABLE IF EXISTS `device_status_log`;
CREATE TABLE `device_status_log` (
  `id_device_status` VARCHAR(10) NOT NULL,
  `device_id` VARCHAR(10) NULL,
  `status` ENUM('ONLINE', 'OFFLINE') NOT NULL,
  `signal_strength` INT NULL,
  `last_seen` TIMESTAMP(6) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_device_status_log` PRIMARY KEY (`id_device_status`),
  CONSTRAINT `fk_device_status_log_device_id` FOREIGN KEY (`device_id`) REFERENCES `device` (`id_device`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- device_home: Assignment of a device to a home. home_id references home.home at application
-- level only, no SQL foreign key across bounded contexts.
-- Changesets: device-004-create-device-home
DROP TABLE IF EXISTS `device_home`;
CREATE TABLE `device_home` (
  `device_id` VARCHAR(10) NOT NULL,
  `home_id` VARCHAR(10) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_device_home` PRIMARY KEY (`device_id`, `home_id`),
  CONSTRAINT `fk_device_home_device_id` FOREIGN KEY (`device_id`) REFERENCES `device` (`id_device`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- device_outbox: Creates the device_outbox table of the transactional outbox.
-- Changesets: device-009-create-device-outbox
DROP TABLE IF EXISTS `device_outbox`;
CREATE TABLE `device_outbox` (
  `id_message` VARCHAR(10) NOT NULL,
  `message_type` VARCHAR(60) NOT NULL,
  `payload` TEXT NOT NULL,
  `occurred_at` DATETIME(6) NOT NULL,
  `published_at` DATETIME(6) NULL,
  `discarded_at` DATETIME(6) NULL,
  `attempts` INT NOT NULL DEFAULT 0,
  `last_error` VARCHAR(255) NULL,
  CONSTRAINT `pk_device_outbox` PRIMARY KEY (`id_message`),
  INDEX `idx_device_outbox_pending` (`published_at`, `discarded_at`, `occurred_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- Servicio: en-monitor-measurement-service (:8085)
-- =============================================================================

-- measurement: Single electrical reading produced by a device. device_id references
-- device.device at application level only, no SQL foreign key across bounded contexts.
-- date_time is the business timestamp of the reading, created_at is the audit timestamp.
-- Changesets: measurement-001-create-measurement
DROP TABLE IF EXISTS `measurement`;
CREATE TABLE `measurement` (
  `id_measurement` VARCHAR(10) NOT NULL,
  `device_id` VARCHAR(10) NOT NULL,
  `date_time` TIMESTAMP NOT NULL,
  `voltage` DOUBLE NOT NULL,
  `current` DOUBLE NOT NULL,
  `active_power` DOUBLE NOT NULL,
  `stored_energy` DOUBLE NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_measurement` PRIMARY KEY (`id_measurement`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- consumption_level: Numerical classification of a consumption value. Ranges must not overlap,
-- enforced at application level.
-- Changesets: measurement-002-create-consumption-level
DROP TABLE IF EXISTS `consumption_level`;
CREATE TABLE `consumption_level` (
  `id_consumption_level` VARCHAR(10) NOT NULL,
  `name` ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL,
  `description` VARCHAR(200) NOT NULL,
  `min_limit` DOUBLE NOT NULL,
  `max_limit` DOUBLE NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_consumption_level` PRIMARY KEY (`id_consumption_level`),
  CONSTRAINT `uk_consumption_level_name` UNIQUE (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- measurement_outbox: Creates the measurement_outbox table of the transactional outbox.
-- Changesets: measurement-004-create-measurement-outbox
DROP TABLE IF EXISTS `measurement_outbox`;
CREATE TABLE `measurement_outbox` (
  `id_message` VARCHAR(10) NOT NULL,
  `message_type` VARCHAR(60) NOT NULL,
  `payload` TEXT NOT NULL,
  `occurred_at` DATETIME(6) NOT NULL,
  `published_at` DATETIME(6) NULL,
  `discarded_at` DATETIME(6) NULL,
  `attempts` INT NOT NULL DEFAULT 0,
  `last_error` VARCHAR(255) NULL,
  CONSTRAINT `pk_measurement_outbox` PRIMARY KEY (`id_message`),
  INDEX `idx_measurement_outbox_pending` (`published_at`, `discarded_at`, `occurred_at`),
  INDEX `idx_measurement_outbox_discarded` (`discarded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- Servicio: en-monitor-home-service (:8086)
-- =============================================================================

-- home_type: Catalog of home types (house, apartment, studio, other).
-- Changesets: home-001-create-home-type
DROP TABLE IF EXISTS `home_type`;
CREATE TABLE `home_type` (
  `id_home_type` VARCHAR(10) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_home_type` PRIMARY KEY (`id_home_type`),
  CONSTRAINT `uk_home_type_name` UNIQUE (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- home: Aggregate root of the home context. creation_date is the business date, created_at is
-- the audit timestamp.
-- Changesets: home-002-create-home
DROP TABLE IF EXISTS `home`;
CREATE TABLE `home` (
  `id_home` VARCHAR(10) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `home_type_id` VARCHAR(10) NOT NULL,
  `address` VARCHAR(200) NOT NULL,
  `access_code` VARCHAR(8) NOT NULL,
  `description` VARCHAR(200) NULL,
  `creation_date` TIMESTAMP NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_home` PRIMARY KEY (`id_home`),
  CONSTRAINT `uk_home_access_code` UNIQUE (`access_code`),
  CONSTRAINT `fk_home_home_type_id` FOREIGN KEY (`home_type_id`) REFERENCES `home_type` (`id_home_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- home_thresholds: Consumption limits of a home, one row per home. Limits are in kWh.
-- Changesets: home-003-create-home-thresholds, home-006-add-threshold-limit-period
DROP TABLE IF EXISTS `home_thresholds`;
CREATE TABLE `home_thresholds` (
  `id_threshold` VARCHAR(10) NOT NULL,
  `home_id` VARCHAR(10) NOT NULL,
  `daily_limit` DOUBLE NOT NULL,
  `monthly_limit` DOUBLE NOT NULL,
  `use_system_default` BOOLEAN NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  `limit_period` ENUM('DAILY', 'MONTHLY') NOT NULL DEFAULT 'DAILY',
  CONSTRAINT `pk_home_thresholds` PRIMARY KEY (`id_threshold`),
  CONSTRAINT `uk_home_thresholds_home_id` UNIQUE (`home_id`),
  CONSTRAINT `fk_home_thresholds_home_id` FOREIGN KEY (`home_id`) REFERENCES `home` (`id_home`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- user_home: Membership of a user in a home. user_id references security.user at application
-- level only, no SQL foreign key across bounded contexts.
-- Changesets: home-004-create-user-home
DROP TABLE IF EXISTS `user_home`;
CREATE TABLE `user_home` (
  `user_id` VARCHAR(10) NOT NULL,
  `home_id` VARCHAR(10) NOT NULL,
  `role` ENUM('OWNER', 'MEMBER') NOT NULL,
  `favorite` BOOLEAN NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_user_home` PRIMARY KEY (`user_id`, `home_id`),
  CONSTRAINT `fk_user_home_home_id` FOREIGN KEY (`home_id`) REFERENCES `home` (`id_home`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- home_outbox: Creates the home_outbox table of the transactional outbox.
-- Changesets: home-007-create-home-outbox
DROP TABLE IF EXISTS `home_outbox`;
CREATE TABLE `home_outbox` (
  `id_message` VARCHAR(10) NOT NULL,
  `message_type` VARCHAR(60) NOT NULL,
  `payload` TEXT NOT NULL,
  `occurred_at` DATETIME(6) NOT NULL,
  `published_at` DATETIME(6) NULL,
  `discarded_at` DATETIME(6) NULL,
  `attempts` INT NOT NULL DEFAULT 0,
  `last_error` VARCHAR(255) NULL,
  CONSTRAINT `pk_home_outbox` PRIMARY KEY (`id_message`),
  INDEX `idx_home_outbox_pending` (`published_at`, `discarded_at`, `occurred_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- Servicio: en-monitor-notification-service (:8087)
-- =============================================================================

-- notification: Delivery log. References to other modules by identifier, never by foreign key.
-- Changesets: notification-001-create-notification, notification-004-add-in-app-admin-notifications, notification-006-add-activity-sources, notification-007-add-message-params
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id_notification` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NOT NULL,
  `home_id` VARCHAR(10) NULL,
  `channel` ENUM('EMAIL','PUSH','IN_APP') NOT NULL,
  `source_type` ENUM('ALERT','RECOMMENDATION','SECURITY','ADMINISTRATION','ACCOUNT','HOME','DEVICE') NOT NULL,
  `source_id` VARCHAR(10) NULL,
  `message_key` VARCHAR(100) NOT NULL,
  `delivery_status` ENUM('PENDING','SENT','FAILED') NOT NULL,
  `sent_at` TIMESTAMP NULL,
  `read_at` TIMESTAMP NULL,
  `failure_reason` VARCHAR(200) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  `message_params` TEXT NULL,
  CONSTRAINT `pk_notification` PRIMARY KEY (`id_notification`),
  INDEX `idx_notification_user_channel_created` (`user_id`, `channel`, `created_at`),
  INDEX `idx_notification_source` (`source_type`, `source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- notification_preference: Per-user notification channel preferences.
-- Changesets: notification-002-create-notification-preference
DROP TABLE IF EXISTS `notification_preference`;
CREATE TABLE `notification_preference` (
  `user_id` VARCHAR(10) NOT NULL,
  `email_enabled` BOOLEAN NOT NULL DEFAULT TRUE,
  `push_enabled` BOOLEAN NOT NULL DEFAULT TRUE,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_notification_preference` PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- push_subscription: Web Push subscriptions per user.
-- Changesets: notification-003-create-push-subscription
DROP TABLE IF EXISTS `push_subscription`;
CREATE TABLE `push_subscription` (
  `id_push_subscription` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NOT NULL,
  `endpoint` VARCHAR(500) NOT NULL,
  `p256dh` VARCHAR(100) NOT NULL,
  `auth` VARCHAR(50) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_push_subscription` PRIMARY KEY (`id_push_subscription`),
  CONSTRAINT `uk_push_subscription_endpoint` UNIQUE (`endpoint`),
  INDEX `idx_push_subscription_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- push_device: Push tokens of app installations, delivered through a relay.
-- Changesets: notification-005-create-push-device
DROP TABLE IF EXISTS `push_device`;
CREATE TABLE `push_device` (
  `id_push_device` VARCHAR(10) NOT NULL,
  `user_id` VARCHAR(10) NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  `platform` VARCHAR(10) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_push_device` PRIMARY KEY (`id_push_device`),
  CONSTRAINT `uk_push_device_token` UNIQUE (`token`),
  INDEX `idx_push_device_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- Servicio: en-monitor-alert-service (:8088)
-- =============================================================================

-- alert: Notification that something requires attention: an exceeded threshold or a device
-- that stopped reporting. home_id, device_id, consumption_level_id and measurement_id
-- reference other bounded contexts at application level only, no SQL foreign keys. A THRESHOLD
-- alert carries consumption_level_id and measurement_id; a CONNECTIVITY alert carries neither.
-- date_time is the business timestamp of the event, created_at is the audit timestamp.
-- Changesets: alert-001-create-alert, alert-002-add-device-alert-type, alert-003-add-limit-alert-type
DROP TABLE IF EXISTS `alert`;
CREATE TABLE `alert` (
  `id_alert` VARCHAR(10) NOT NULL,
  `home_id` VARCHAR(10) NOT NULL,
  `device_id` VARCHAR(10) NULL,
  `type` ENUM('THRESHOLD', 'CONNECTIVITY', 'DEVICE', 'LIMIT') NOT NULL,
  `message_key` VARCHAR(100) NOT NULL,
  `date_time` TIMESTAMP NOT NULL,
  `alert_status` ENUM('PENDING', 'RESOLVED') NOT NULL,
  `consumption_level_id` VARCHAR(10) NULL,
  `measurement_id` VARCHAR(10) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_alert` PRIMARY KEY (`id_alert`),
  INDEX `idx_alert_home_id` (`home_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- alert_outbox: Creates the alert_outbox table of the transactional outbox.
-- Changesets: alert-004-create-alert-outbox
DROP TABLE IF EXISTS `alert_outbox`;
CREATE TABLE `alert_outbox` (
  `id_message` VARCHAR(10) NOT NULL,
  `message_type` VARCHAR(60) NOT NULL,
  `payload` TEXT NOT NULL,
  `occurred_at` DATETIME(6) NOT NULL,
  `published_at` DATETIME(6) NULL,
  `discarded_at` DATETIME(6) NULL,
  `attempts` INT NOT NULL DEFAULT 0,
  `last_error` VARCHAR(255) NULL,
  CONSTRAINT `pk_alert_outbox` PRIMARY KEY (`id_message`),
  INDEX `idx_alert_outbox_pending` (`published_at`, `discarded_at`, `occurred_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================================================
-- Servicio: en-monitor-recommendation-service (:8089)
-- =============================================================================

-- recommendation: Suggestion generated from a home's observed consumption behavior. home_id
-- and device_id reference other bounded contexts at application level only, no SQL foreign
-- keys. Measurements are analyzed by the domain but never stored as direct references.
-- device_id is null when the recommendation targets the whole home. date_time is the business
-- timestamp of generation, created_at is the audit timestamp.
-- Changesets: recommendation-001-create-recommendation
DROP TABLE IF EXISTS `recommendation`;
CREATE TABLE `recommendation` (
  `id_recommendation` VARCHAR(10) NOT NULL,
  `home_id` VARCHAR(10) NOT NULL,
  `device_id` VARCHAR(10) NULL,
  `type` ENUM('SAVING', 'PEAK_HOURS', 'HIGH_CONSUMPTION', 'HISTORICAL_COMPARISON', 'THRESHOLD') NOT NULL,
  `message_key` VARCHAR(100) NOT NULL,
  `date_time` TIMESTAMP NOT NULL,
  `status` ENUM('READ', 'UNREAD') NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` TIMESTAMP NULL,
  CONSTRAINT `pk_recommendation` PRIMARY KEY (`id_recommendation`),
  INDEX `idx_recommendation_home_id` (`home_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- recommendation_outbox: Creates the recommendation_outbox table of the transactional outbox.
-- Changesets: recommendation-002-create-recommendation-outbox
DROP TABLE IF EXISTS `recommendation_outbox`;
CREATE TABLE `recommendation_outbox` (
  `id_message` VARCHAR(10) NOT NULL,
  `message_type` VARCHAR(60) NOT NULL,
  `payload` TEXT NOT NULL,
  `occurred_at` DATETIME(6) NOT NULL,
  `published_at` DATETIME(6) NULL,
  `discarded_at` DATETIME(6) NULL,
  `attempts` INT NOT NULL DEFAULT 0,
  `last_error` VARCHAR(255) NULL,
  CONSTRAINT `pk_recommendation_outbox` PRIMARY KEY (`id_message`),
  INDEX `idx_recommendation_outbox_pending` (`published_at`, `discarded_at`, `occurred_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- Datos semilla (los mismos que insertan los changesets indicados)
-- =============================================================================

-- security-019-seed-default-password-policy
INSERT INTO `password_policy` (`id_password_policy`, `min_length`, `max_length`, `require_uppercase`, `require_numbers`, `require_symbols`, `expiration_days`) VALUES ('pol0000001', 8, 64, TRUE, TRUE, TRUE, 90);

-- security-025-seed-admin-role
INSERT INTO `system_role` (`id_system_role`, `name`, `description`, `enabled`) VALUES ('rol0000001', 'ADMIN', 'Platform administrator: manages accounts and global roles.', TRUE);
INSERT INTO `permission` (`id_permission`, `code`, `name`, `description`) VALUES ('prm0000001', 'USER.READ', 'Read users', 'List accounts and the global roles they hold.');
INSERT INTO `permission` (`id_permission`, `code`, `name`, `description`) VALUES ('prm0000002', 'USER.MANAGE', 'Manage account status', 'Activate, deactivate or block an account.');
INSERT INTO `permission` (`id_permission`, `code`, `name`, `description`) VALUES ('prm0000003', 'ROLE.MANAGE', 'Manage global roles', 'Grant or revoke global roles.');
INSERT INTO `system_role_permission` (`system_role_id`, `permission_id`, `assigned_at`) VALUES ('rol0000001', 'prm0000001', CURRENT_TIMESTAMP);
INSERT INTO `system_role_permission` (`system_role_id`, `permission_id`, `assigned_at`) VALUES ('rol0000001', 'prm0000002', CURRENT_TIMESTAMP);
INSERT INTO `system_role_permission` (`system_role_id`, `permission_id`, `assigned_at`) VALUES ('rol0000001', 'prm0000003', CURRENT_TIMESTAMP);

-- device-006-seed-appliance-types
INSERT IGNORE INTO appliance_type (id_appliance_type, name) VALUES
  ('appl000001', 'refrigerator'),
  ('appl000002', 'air_conditioner'),
  ('appl000003', 'lighting'),
  ('appl000004', 'washing_machine'),
  ('appl000005', 'television'),
  ('appl000006', 'microwave'),
  ('appl000007', 'computer'),
  ('appl000008', 'water_heater'),
  ('appl000009', 'other');

-- measurement-003-seed-consumption-level
INSERT INTO `consumption_level` (`id_consumption_level`, `name`, `description`, `min_limit`, `max_limit`) VALUES ('low0000001', 'LOW', 'Efficient consumption, below the typical base load', 0, 500);
INSERT INTO `consumption_level` (`id_consumption_level`, `name`, `description`, `min_limit`, `max_limit`) VALUES ('medi000001', 'MEDIUM', 'Moderate consumption, typical of regular household use', 500, 1500);
INSERT INTO `consumption_level` (`id_consumption_level`, `name`, `description`, `min_limit`, `max_limit`) VALUES ('high000001', 'HIGH', 'High consumption, above the efficient range', 1500, 3000);
INSERT INTO `consumption_level` (`id_consumption_level`, `name`, `description`, `min_limit`, `max_limit`) VALUES ('crit000001', 'CRITICAL', 'Critical consumption, immediate attention recommended', 3000, 1000000);

-- home-005-seed-home-type
INSERT INTO `home_type` (`id_home_type`, `name`) VALUES ('hous000001', 'house');
INSERT INTO `home_type` (`id_home_type`, `name`) VALUES ('apar000001', 'apartment');
INSERT INTO `home_type` (`id_home_type`, `name`) VALUES ('stud000001', 'studio');
INSERT INTO `home_type` (`id_home_type`, `name`) VALUES ('coun000001', 'country_house');
INSERT INTO `home_type` (`id_home_type`, `name`) VALUES ('cabi000001', 'cabin');
INSERT INTO `home_type` (`id_home_type`, `name`) VALUES ('othe000001', 'other');

-- Ninguna cuenta es administradora al instalar: el primer ADMIN se otorga con
-- energy-monitor-infra/make-admin.sh <correo> (inserta la fila en user_system_role
-- con system_role_id = 'rol0000001'). Ver el manual del administrador.
