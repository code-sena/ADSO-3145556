**SERVICIO NACIONAL DE APRENDIZAJE – SENA (National Learning Service)**

**CENTER FOR INDUSTRY, BUSINESS AND SERVICES (CIES)**

**HUILA REGIONAL**

Training Program: Technology in Software Analysis and Development (ADSO)

Group (Ficha): 3145556

**ADMINISTRATOR MANUAL**

**Project: Electrical Consumption Monitor**

*Account administration, operation, configuration, backups and security management of the platform*

| **Item**             | **Information**                                                                 |
|----------------------|---------------------------------------------------------------------------------|
| Team members         | Juan Esteban Cortés Parra, Miguel Ángel García Artunduaga                       |
| Training center      | Center for Industry, Business and Services (CIES) – SENA Huila Regional         |
| Program              | Technology in Software Analysis and Development (ADSO)                          |
| Group (Ficha)        | 3145556                                                                         |
| Document type        | Administrator manual                                                            |
| Version              | 1.5                                                                             |
| Date prepared        | October 7, 2026 (updated October 30, 2026)                                      |

Year 2026

# 2. Document version control

The change history of this manual is recorded in the following table. Any new administrative operation or configuration change supported by the system must be accompanied by an update to the manual.

| **Version** | **Date**   | **Description of change**                                                                                                                                                                                                       | **Responsible**      |
|-------------|------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------|
| 1.0         | 10/07/2026 | Initial creation of the administrator manual: environment, roles and permissions, password policy, audit log, backups, updates and incidents. Citations and references in APA format.                                          | Development team     |
| 1.1         | 10/20/2026 | Administration from the web application (user directory, statuses, administrator role and administration notices), designation of the first administrator with `make-admin.sh --prod`, production environment at `https://energy-monitor.tech`, backups and updates on the server, administration incidents and corrected SQL queries. | Development team     |
| 1.2         | 10/24/2026 | Operation with the services architecture: containers of the API gateway, the security, device and measurement services, the backend and RabbitMQ; health checks inside the containers, logs per service, `deploy.sh` components, new secrets (`RABBITMQ_PASSWORD`, `INTERNAL_API_TOKEN`, `MQTT_DEVICE_SERVICE_PASS`, `MQTT_MEASUREMENT_SERVICE_PASS`) and new incidents. | Development team     |
| 1.3         | 10/27/2026 | Backend retired: operation of the seven microservices (security, device, measurement, home, notification, alert and recommendation) and of the gateway; administration notices served by the notification service; queues and outboxes the administrator must watch; deployment order; `EMAIL_NOT_VERIFIED`; tables per service; updated commands and checklist. | Development team     |
| 1.4         | 10/28/2026 | Documents the unusual consumption alert of an appliance (what triggers it, when it resolves and what the user sees), the Settings help center with support handled by the administrators, and profile photos; the final version is unified at 1.4. | Development team |
| 1.5         | 10/30/2026 | The introduction now states that the administration screens and the administrator role are exclusive to the web application. | Development team |

# 3. Table of contents

- [4. Introduction](#4-introduction)
- [5. Objective](#5-objective)
- [6. Technical scope](#6-technical-scope)
- [7. Administration environment and credentials](#7-administration-environment-and-credentials)
- [8. Platform security model](#8-platform-security-model)
- [9. Roles and permissions (RBAC)](#9-roles-and-permissions-rbac)
- [10. Administration from the web application](#10-administration-from-the-web-application)
- [11. Password policy and security configuration](#11-password-policy-and-security-configuration)
- [12. Sessions, tokens and revocation](#12-sessions-tokens-and-revocation)
- [13. Backups and restore](#13-backups-and-restore)
- [14. Software update](#14-software-update)
- [15. Email verification](#15-email-verification)
- [16. Frequent incidents and how to diagnose them](#16-frequent-incidents-and-how-to-diagnose-them)
- [17. Quick administration commands](#17-quick-administration-commands)
- [18. Daily / weekly administrator checklist](#18-daily--weekly-administrator-checklist)
- [19. References (APA format, 7th edition)](#19-references-apa-format-7th-edition)

# 4. Introduction

This document is the administrator manual of the "Electrical Consumption Monitor" software (EnergyMonitor). It is aimed at whoever will be in charge of administering the platform: managing user accounts and administrators, monitoring the system's behavior, backups, software updates and incident handling (Cortés Parra & García Artunduaga, 2026a).

The system has two levels of administration:

- **Functional administration, from the web application.** An account with the global administrator role (`ADMIN`) has a dedicated screen at `https://energy-monitor.tech` to view the accounts, change their status, grant or revoke the administrator role and receive administration notices (section 10).
- **Technical administration, from the server.** Designating the first administrator, the password policy, the security parameters, backups and updates are done on the server, with the scripts of the infrastructure repository and queries to the MySQL 8.4 database (Spring, n.d.; Docker Inc., n.d.).

Functional administration and the administrator role are exclusive to the web application: the mobile app includes no administration screens and does not check this role.

The academic and technical references cited in the body of the document were formatted according to the 7th edition of the APA manual (American Psychological Association \[APA\], 2020).

# 5. Objective

To provide the team that administers the system with a clear, operational and verifiable guide to carry out, without risks, the tasks of administering the platform.

- Explain how the first administrator is designated and how accounts are administered from the web application.
- Describe the administration environment and where each credential or configuration file is located.
- Document the roles and permissions model and its restrictions.
- Explain how to review and rotate the encryption keys (recovery pepper and RSA pair).
- Record the procedure to run backups and restore them.
- Define the flow to update the software without losing data and to diagnose frequent incidents.

# 6. Technical scope

This manual covers administration in two environments:

- **Production:** a VPS with Ubuntu 26.04 LTS, published at `https://energy-monitor.tech`. The whole platform (MySQL 8.4, RabbitMQ 4.1, API gateway, the seven microservices, MQTT broker and frontend) runs with Docker Compose from `/opt/energy-monitor/infra` (`docker-compose.prod.yml`), behind Nginx with Let's Encrypt certificates.
- **Local development:** the full stack of the `energy-monitor-infra` repository (`docker-compose.yml`), or MySQL, RabbitMQ and the broker in Docker with the services run from the IDE or with `./mvnw spring-boot:run`.

Administered components:

- API gateway (`en-monitor-api-gateway`, Spring Cloud Gateway, port 8080): the single entry point of the API.
- Security service (`en-monitor-security-service`, port 8082): accounts, sessions, JWT RS256, RBAC and user administration.
- Device service (`en-monitor-device-service`, port 8084) and measurement service (`en-monitor-measurement-service`, port 8085).
- Home service (`en-monitor-home-service`, 8086), notification service (`en-monitor-notification-service`, 8087; email, push, activity and administration notices), alert service (`en-monitor-alert-service`, 8088) and recommendation service (`en-monitor-recommendation-service`, 8089).
- The former backend (`energy-monitor-backend`) is retired: it no longer runs and `deploy.sh` does not accept it. It only keeps the device firmware.
- The `energy_monitor` database, shared by all the services (each one with its own tables), and the RabbitMQ queue the services use to notify each other.
- Web application, including its administration screen.
- Verification, recovery, account deletion, alert, recommendation and activity email via SMTP (Gmail in production, Mailpit locally), sent only by the notification service.

It does not describe installing the server from scratch or configuring continuous deployment: both are in the *Implementation and deployment manual* (Cortés Parra & García Artunduaga, 2026b). Setting up the project from the code is in the *Technical manual*.

# 7. Administration environment and credentials

## 7.1 Components the administrator must monitor

On the production server the `emc` alias is used (defined in the implementation and deployment manual), equivalent to `docker compose -f /opt/energy-monitor/infra/docker-compose.prod.yml --env-file /opt/energy-monitor/infra/.env.prod --project-directory /opt/energy-monitor/infra`.

| **Component**         | **Production**                                                | **Evidence/verification**                                               |
|-----------------------|---------------------------------------------------------------|-------------------------------------------------------------------------|
| MySQL 8.4             | `mysql` service (`emc ps`)                                    | Status `Up (healthy)`                                                   |
| RabbitMQ 4.1          | `rabbitmq` service; console at `127.0.0.1:15672` (SSH tunnel) | Status `Up (healthy)`; `.dlq` queues empty                              |
| API gateway           | `gateway` service (port 8080 only on the Docker network)      | `emc exec gateway curl -fsS http://localhost:8080/actuator/health` → `{"status":"UP"...}` |
| Security service      | `security` service (8082, Docker network)                     | `emc ps` → `Up (healthy)`                                               |
| Device service        | `device` service (8084, Docker network)                       | `emc ps` → `Up (healthy)`                                               |
| Measurement service   | `measurement` service (8085, Docker network)                  | `emc ps` → `Up (healthy)`                                               |
| Home service          | `home` service (8086, Docker network)                         | `emc ps` → `Up (healthy)`                                               |
| Notification service  | `notification` service (8087, Docker network)                 | `emc ps` → `Up (healthy)`; `notification-service.*` queues not piling up |
| Alert service         | `alert` service (8088, Docker network)                        | `emc ps` → `Up (healthy)`; `alert-service.*` queues not piling up       |
| Recommendation service | `recommendation` service (8089, Docker network)              | `emc ps` → `Up (healthy)`                                               |
| MQTT broker           | `mosquitto` service, public port 8883 (TLS)                   | `emc logs --tail 50 mosquitto` without restarts; `em-device-service` and `em-measurement-service` connected |
| Web                   | `frontend` service behind Nginx                               | `https://energy-monitor.tech` opens the home page                       |
| Email                 | SMTP account configured in `.env.prod`                        | `notification` table without `FAILED` deliveries (section 15)           |

No Java service publishes ports on the host: their health is measured by each container's *healthcheck* (`/actuator/health`), which `emc ps` shows as `(healthy)`. In local development, the same services are checked with `docker compose ps` from `energy-monitor-infra`; Mailpit shows the emails at `http://localhost:8025`.

## 7.2 Where the platform secrets are

No secret is stored in the code or in Git. The administrator must keep them only in these places:

- **Production:**
  - `/opt/energy-monitor/infra/.env.prod` (mode 600): `MYSQL_PASSWORD`, `MYSQL_ROOT_PASSWORD`, `SECURITY_RESET_PEPPER`, `RABBITMQ_PASSWORD`, `INTERNAL_API_TOKEN`, `MQTT_DEVICE_SERVICE_PASS`, `MQTT_MEASUREMENT_SERVICE_PASS`, `MAIL_*` credentials, `GOOGLE_CLIENT_SECRET`, VAPID keys and the other secrets. `MQTT_BACKEND_USER/PASS`, `BACKEND_URL` and `BACKEND_INTERNAL_URL` are no longer used: the backend is retired.
  - `/opt/energy-monitor/keys/`: JWT `private.pem` / `public.pem` pair (owned by uid 10001). Only the security service reads `private.pem`; the gateway and the other services receive only `public.pem`.
  - `/opt/energy-monitor/mqtt-certs/`: broker certificate, copied from Let's Encrypt.
- **Local development:** `energy-monitor-infra/.env` and `energy-monitor-infra/keys/`; if the services run from the IDE, also the `.env` and the `src/main/resources/application-local.yaml` of each one (`backend/en-monitor-security-service`, `backend/en-monitor-device-service`, `backend/en-monitor-measurement-service`, `backend/en-monitor-home-service`, `backend/en-monitor-notification-service`, `backend/en-monitor-alert-service`, `backend/en-monitor-recommendation-service`, and the `.env` of `backend/en-monitor-api-gateway`).

> *Golden rule: no secret goes into commits, manual annexes, chats or shared folders. The `.env`, `.env.prod`, `application-local.yaml`, `*.pem` files and the certificates are in `.gitignore`.*

# 8. Platform security model

## 8.1 Tables that govern security

All the tables are in the same `energy_monitor` database. The security tables belong to the security service (`en-monitor-security-service`); `notification` belongs to the notification service. All the tables are detailed in the [data dictionary](../../04-software-design/en/data-dictionary.md).

| **Table**                                                         | **Purpose**                                                                                       |
|-------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| user, person, user_configuration, user_session                    | Identity, basic data, own configuration and sessions of each account. `user.status` is `ACTIVE`, `INACTIVE` or `BLOCKED`. |
| password_policy                                                   | Active password policy (by default min 8, max 64, 90 days, uppercase/numbers/symbols).            |
| password_reset_token                                              | Password recovery tokens (hashed with pepper).                                                    |
| security_configuration                                            | Name/value pairs of security parameters managed by the security service.                         |
| login_error_log                                                   | Failed access attempts with type, description, IP and date.                                      |
| system_role, permission, user_system_role, system_role_permission | RBAC model of the global roles (section 9).                                                      |
| audit_log                                                         | Audit of CREATE/UPDATE/DELETE/LOGIN/LOGIN_FAILED/LOGOUT with IP and application, including the status and role changes made by administrators. |
| notification                                                      | Log of email and push deliveries (SENT/FAILED status and reason), activity notifications and administration notices (`IN_APP` channel, `ADMINISTRATION` source). |
| pending_registration                                              | Registrations waiting for the email verification; they expire after 24 hours and an hourly job deletes them. |
| security_outbox                                                   | Events and mail requests waiting to be published to RabbitMQ (`last_error` explains a jam). |

## 8.2 Audit (`audit_log`)

Every relevant operation on accounts, sessions, credentials and roles leaves a trace in `audit_log`. The administrator can check the latest actions like this:

```sql
SELECT id_audit_log, user_id, action, description, ip_address, created_at
FROM audit_log
ORDER BY created_at DESC
LIMIT 50;
```

The `action` field accepts CREATE, UPDATE, DELETE, LOGIN, LOGIN_FAILED and LOGOUT (MySQL, 2026).

## 8.3 Failed access attempts (`login_error_log`)

Denied sign-ins are recorded with reason and source address. It is useful to detect brute force:

```sql
SELECT error_type, ip_address, COUNT(*) AS attempts, MAX(created_at) AS last
FROM login_error_log
GROUP BY error_type, ip_address
ORDER BY attempts DESC
LIMIT 20;
```

`error_type` can be `INVALID_PASSWORD`, `USER_NOT_FOUND`, `ACCOUNT_BLOCKED`, `ACCOUNT_INACTIVE` or `EMAIL_NOT_VERIFIED` (right password, but the email is not verified yet). The last two appear when an account blocked or deactivated by an administrator tries to sign in.

# 9. Roles and permissions (RBAC)

## 9.1 Model

The system distinguishes two kinds of role that do not mix:

- **Home roles** (`OWNER` / `MEMBER`): valid inside a single home and managed by the owner from the application.
- **Global roles** (RBAC): valid for the whole platform. Today there is one, `ADMIN`.

```
system_role (id_system_role, name, description, enabled)
permission (id_permission, code, name, description)
system_role_permission (system_role_id, permission_id)
user_system_role (user_id, system_role_id, assigned_at, deleted_at)
```

The Liquibase changeset `security-025` creates the `ADMIN` role with its three permissions:

| **Permission** | **What it allows**                                                   |
|----------------|----------------------------------------------------------------------|
| `USER.READ`    | View the account directory, the roles and one's own administration notices |
| `USER.MANAGE`  | Change the status of an account to active, inactive or blocked       |
| `ROLE.MANAGE`  | Grant or revoke global roles                                         |

Permissions are read from the database **on every request** and do not travel inside the session token. That is why granting or revoking the role, or blocking an account, takes effect on the next request to the security service. The administration notices are served by the notification service, which asks the security service for the `USER.READ` permission through its internal API. Revoking the role from the web does not delete the `user_system_role` row: it sets a date in `deleted_at`.

> *The `ADMIN` role gives no access to any home: an administrator who is not a member of a home cannot see its data.*

## 9.2 Designating the first administrator

No account receives the role when the system is installed. The first administrator is designated once from the server; the following ones are designated from the web application (section 10.4).

1. Register the account at `https://energy-monitor.tech` (sign-up with email or "Sign in with Google").
2. On the server, run:

   ```bash
   cd /opt/energy-monitor/infra
   ./make-admin.sh --prod admin-email@example.com
   ```

3. Log out of the web application and log back in: the menu shows **Administration**.

The script normalizes the email (no spaces, lowercase), rejects invalid addresses, fails if the account does not exist and is idempotent: if the account is already an administrator it duplicates nothing, and if the role had been revoked it reactivates it. At the end it shows the account's active roles. On the local stack it is used without `--prod` (`./make-admin.sh email@example.com`); with `--prod` it reads `.env.prod` and `docker-compose.prod.yml`.

# 10. Administration from the web application

## 10.1 Access and menu

An account with the `ADMIN` role that signs in at `https://energy-monitor.tech` goes straight to **Administration** (`/admin/users`). Its side menu only shows three options:

- **Administration:** user directory.
- **Notifications:** administration notices.
- **Settings:** language, theme and help center. The homes' notification preferences do not appear, because an administrator account has no homes.

The home screens (dashboard, home detail and favorites) are not available to an administrator account: if it tries to open them, the application sends it back to the administration. An account without the `USER.READ` permission that tries to open the administration goes back to its homes dashboard, and the API rejects its administration requests with `403`. The mobile app has no administration screens: administration is done only from the web.

## 10.2 User directory

The **Platform users** screen shows the active accounts, from newest to oldest, in pages of 20.

| **Column**        | **Content**                                                      |
|-------------------|------------------------------------------------------------------|
| User              | First name, last name and email. One's own account is marked "You". |
| Status            | Active, Inactive or Blocked                                      |
| Global roles      | Roles held by the account, or "No roles"                         |
| Registered        | Account creation date                                            |
| Last sign-in      | Last sign-in, or "Never"                                         |

- **Search:** type part of the first name, last name or email and press **Search**.
- **Paginate:** with more than 20 results the previous and next page buttons appear.

## 10.3 Changing the status of an account

Requires the `USER.MANAGE` permission.

1. In the account's row, choose the new status in the **Status** selector.
2. If the new status is not **Active**, the application asks for confirmation: the account will not be able to sign in and its open sessions will be closed.
3. Press **Confirm**.

| **Status** | **Effect**                                                                 |
|------------|----------------------------------------------------------------------------|
| Active     | The account can sign in normally.                                          |
| Inactive   | The account cannot sign in (`ACCOUNT_INACTIVE`); its sessions are closed.  |
| Blocked    | The account cannot sign in (`ACCOUNT_BLOCKED`); its sessions are closed.   |

Sessions are revoked with the `ADMINISTRATIVE` reason. The access token the account already had may remain valid for up to 15 minutes, but it cannot be renewed.

## 10.4 Granting or revoking the administrator role

Requires the `ROLE.MANAGE` permission.

- **Make administrator:** press the shield button in the account's row. The role applies immediately; the account must log out and back in to see the administration.
- **Remove the role:** press the same button on an administrator account and confirm. The account loses access to the administration.

Repeating a grant or a revocation changes nothing. Every change is recorded in `audit_log` with the source address.

## 10.5 Administration notices

The **Notifications** option shows the account's administration notices: the 50 most recent. The menu and the tab title show how many notices are unread.

| **Notice**                | **When it is produced**                                                 |
|---------------------------|-------------------------------------------------------------------------|
| New account registered    | Someone creates an account, with email or with their first Google sign-in |
| Account activated / deactivated / blocked | Another administrator changes the status of an account    |
| New administrator / Administrator removed | Another administrator grants or revokes the `ADMIN` role  |

- Whoever makes a change does not receive a notice about it, and an action that changes nothing produces no notices.
- Each notice can be **marked as read**; all of them can also be marked at once, and the ones no longer needed can be **deleted**.
- Each administrator only sees and changes their own notices.
- Administration notices are only shown inside the web application: they are not sent by email or push notification.

## 10.6 Restrictions

| **Restriction**                                              | **Behavior**                                                             |
|--------------------------------------------------------------|--------------------------------------------------------------------------|
| An administrator cannot administer their own account         | The web does not show the actions on one's own row; the security service answers `409` if attempted |
| An action without the matching permission                    | The web hides the action and the API answers `403`                       |
| No session                                                   | The API gateway answers `401`                                            |
| Any `/api/v1/admin/**` route without an assigned permission   | Always rejected (`403`)                                                  |
| The `ADMIN` role and the homes                                | The role gives no access to any home                                     |

## 10.7 Pending features

The following features do not exist in this version and are planned:

- Management of support requests from **Settings**.
- Editing of the help documentation from **Settings**.
- Delivery of administration notices by email or push notification.
- Global roles other than `ADMIN`.

## 10.8 Support and unusual consumption alert

**Technical support.** In Settings \> Help, the user finds the **Technical support** card: they choose one of the available administrators and **Create ticket** opens their email with the subject already written. That is why support is handled by the administrators, who answer by email, and the card does not appear for them. Keeping the emails of the administrator accounts up to date (section 10.2) is what guarantees there is someone to write to.

**Unusual consumption alert.** The alert service notifies the members of the home when an appliance consumes at least 200 W and at least 2.5 times its hourly average of the last 7 days, as long as it has 24 hours of history and an average of at least 5 W. The alert resolves by itself when consumption returns to normal and arrives by email and push according to each person's preferences. If a user reports not receiving the notice, check in this order: that the module has enough history, that the home has members with notifications turned on, and that the alert service is running (section 16). The thresholds are fixed in the code and cannot be changed from the application.

# 11. Password policy and security configuration

## 11.1 Active policy (`password_policy`)

The application takes the policy from the **first row** without `deleted_at`. For operational safety, the schema seeds a single default row (id `pol0000001`): min. 8, max. 64, 90-day expiration, uppercase, numbers and symbols required. To change it:

```sql
UPDATE password_policy SET min_length = 10, expiration_days = 60 WHERE id_password_policy = 'pol0000001';
```

Do not insert a second active row: the selection has no `ORDER BY` and the behavior would be non-deterministic.

## 11.2 Dynamic parameters (`security_configuration`)

The table stores `config_name / config_value` pairs that the administrator can adjust without recompiling. Edit existing values:

```sql
SELECT id_security_configuration, config_name, config_value, description FROM security_configuration;
UPDATE security_configuration SET config_value = 'value' WHERE config_name = 'parameter_name';
```

> *Changing values the code uses to sign/validate tokens may invalidate sessions: coordinate with the development team before touching those parameters.*

# 12. Sessions, tokens and revocation

Every session or refresh token revocation records its reason: `ADMINISTRATIVE`, `PASSWORD_CHANGED` or `REFRESH_TOKEN_REUSE` (MySQL, 2026). The recommended way to close an account's sessions is to change its status from the web (section 10.3), which revokes them with the `ADMINISTRATIVE` reason and leaves the change audited. If it has to be done by SQL during an incident:

```sql
UPDATE user_session SET revoked_at = NOW(), revoked_reason = 'ADMINISTRATIVE' WHERE user_id = 'usr0000001' AND revoked_at IS NULL;
```

After a revocation, the client's next `POST /api/v1/auth/refresh` answers `401` and forces it to authenticate again.

# 13. Backups and restore

## 13.1 Backup in production

The `infra/deploy/backup-mysql.sh` script produces `energy_monitor-YYYYMMDD-HHMMSS.sql.gz` in `/opt/energy-monitor/backups` (mode 600) and deletes those older than 14 days. It runs automatically before every deployment and must be scheduled daily:

```bash
sudo /opt/energy-monitor/infra/deploy/backup-mysql.sh                 # by hand
# cron (sudo crontab -e):
0 3 * * * /opt/energy-monitor/infra/deploy/backup-mysql.sh >> /var/log/energy-monitor-backup.log 2>&1
```

## 13.2 Restore

Restoring **overwrites** the database, which all the services share. First stop the services that write to it (`emc stop gateway security device measurement home notification alert recommendation`) and, from `/opt/energy-monitor/infra`:

```bash
gunzip -c /opt/energy-monitor/backups/<file>.sql.gz | \
  docker compose -f docker-compose.prod.yml --env-file .env.prod \
  exec -T -e MYSQL_PWD="<root password>" mysql mysql -uroot energy_monitor
emc start security device measurement home notification alert recommendation gateway
```

## 13.3 Data volume

MySQL's persistent data lives in the `mysql-data` Docker volume, and RabbitMQ's pending messages in `rabbitmq-data`. Running `docker compose down -v` deletes the volumes and, with them, the whole database; never do it in production (Docker Inc., n.d.).

> *Backups stay on the same server as the database: periodically copy the files to external storage with restricted access.*

# 14. Software update

In production, the software is updated with GitHub Actions: every change merged into `main` is deployed after the approval of the `production` environment, with a prior database backup, a health check and automatic rollback if something fails. Each repository deploys its own component, and the health check waits until every Java service is `healthy`, the web answers and `/api/v1/homes` answers 401 through the gateway. When a change adds a new event, the service that consumes it is deployed first and the one that produces it afterwards (for example, `notification` before `security` and `home`): an outbox stops at a message no queue takes. Liquibase applies each service's new changesets when that service starts. The full procedure is in the implementation and deployment manual (Cortés Parra & García Artunduaga, 2026b).

If GitHub is not available, the same procedure runs by hand on the server:

```bash
sudo /opt/energy-monitor/infra/deploy/deploy.sh security     # or device, measurement, home, notification, alert, recommendation, gateway, frontend, infra
```

> *Changing `SECURITY_RESET_PEPPER` invalidates all current recovery codes. Rotating it is a security decision, not a habit.*

# 15. Email verification

Only the notification service sends email: the verification, recovery and account deletion emails are requested by the security service through RabbitMQ (`mail.email-verification`, `mail.password-reset`, `mail.account-deletion`), and the alert, recommendation and activity emails come from the `alert.raised`, `recommendation.created` and activity events. Every email or push delivery attempt is recorded in the `notification` table with a status (`delivery_status`) of `SENT` or `FAILED` and, if it fails, the reason (`failure_reason`). To audit whether email is failing:

```sql
SELECT id_notification, channel, delivery_status, failure_reason, created_at
FROM notification
WHERE delivery_status = 'FAILED'
ORDER BY created_at DESC
LIMIT 20;
```

If almost all of them fail, it is usually due to wrong SMTP credentials, to Gmail requiring an "app password" instead of the account password, or to `MAIL_FROM` not matching `MAIL_USERNAME` (Google, n.d.). If a verification or recovery email does not even appear in `notification`, check in the RabbitMQ console that the `notification-service.notification.mail` queue is not piling up messages or sending them to its `.dlq`, and the `last_error` column of `security_outbox`. In development, check that Mailpit is running (`http://localhost:8025`).

# 16. Frequent incidents and how to diagnose them

| **Symptom**                                                                        | **Possible cause**                                              | **Action**                                                                                                    |
|------------------------------------------------------------------------------------|-----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|
| After `make-admin.sh` the menu does not show **Administration**                    | The session started before the role was granted                 | Log out and back in.                                                                                          |
| `make-admin.sh` answers "No encontré MYSQL_ROOT_PASSWORD"                          | It was run on the server without `--prod`                       | Run `./make-admin.sh --prod email`.                                                                           |
| `make-admin.sh` answers that no active account exists with the email               | The account is not registered yet or the email is spelled differently | Register the account in the web application and repeat.                                                 |
| The web rejects a change on one's own account (`409`)                              | An administrator cannot administer themselves                   | Ask another administrator for the change.                                                                     |
| The web reports a lack of permission (`403`) in the administration                 | The account lacks the permission the action requires            | Check its roles (section 17) or grant the role from another administrator account.                            |
| A user cannot sign in and `login_error_log` shows `ACCOUNT_BLOCKED` or `ACCOUNT_INACTIVE` | An administrator changed their status                    | Check `audit_log`; reactivate the account from the web if appropriate.                                        |
| The security service does not start and names keys or pepper                       | The JWT keys or `SECURITY_RESET_PEPPER` are missing.            | Check `/opt/energy-monitor/keys` and `.env.prod` (or `.env` locally).                                         |
| The web shows errors and the API answers `503`                                     | The gateway cannot reach the service of that route              | `emc ps` and `emc logs --tail 100 security device measurement home notification alert recommendation`.                                       |
| The API answers `504`                                                              | The service of that route took longer than 30 s                 | Check the logs of that service and of MySQL.                                                                  |
| A service stays `unhealthy` with RabbitMQ up                                       | `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` do not match the user RabbitMQ created the first time | Change the password inside RabbitMQ (`rabbitmqctl change_password`) or fix `.env.prod`.                      |
| A meter or a service is rejected by the broker (`rc=5`)                            | The device service does not recognize its credentials           | Check that `MQTT_DEVICE_SERVICE_*` and `MQTT_MEASUREMENT_SERVICE_*` match in `.env.prod`; for a meter, link it again. |
| Recovery emails do not arrive                                                      | SMTP misconfigured or Mailpit stopped.                          | Check `notification` (section 15) and `MAIL_*`.                                                               |
| No alerts or recommendations are created, or activity does not arrive              | Service stopped, switch set to `false` or outbox jammed         | `emc ps`; check that `.env.prod` has neither `ALERT_CONSUMERS_ENABLED=false` nor `RECOMMENDATION_SCHEDULER_ENABLED=false`; read `last_error` of the `*_outbox` tables. |
| `energy-monitor-backend.*` queues holding messages in RabbitMQ                     | Leftovers of the retired backend, without a consumer            | Delete them with their `.dlq` (`emc exec rabbitmq rabbitmqctl delete_queue <queue>`).                          |
| Frequent `INVALID_PASSWORD`, `USER_NOT_FOUND` errors in `login_error_log`          | Invalid access attempts or brute force.                         | Review repeated IPs and block them in the firewall if needed.                                                 |
| `docker compose ps` does not show the services on the server                       | The command was run without the production file                 | Use the `emc ps` alias.                                                                                       |

# 17. Quick administration commands

| **Task**                                    | **Command**                                                                                                                                                  |
|---------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Designate the first administrator           | `./make-admin.sh --prod email@example.com` (from `/opt/energy-monitor/infra`)                                                                                |
| Check the services                          | `emc ps` (production) or `docker compose ps` (locally, from `energy-monitor-infra`)                                                                         |
| Health of the services                      | `emc ps` (the gateway and the seven services `(healthy)`) or `emc exec gateway curl -fsS http://localhost:8080/actuator/health`                                         |
| Logs of a service                           | `emc logs --tail 100 gateway` (or `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation`, `mosquitto`, `rabbitmq`)                                                   |
| RabbitMQ queues                             | `emc exec rabbitmq rabbitmqctl list_queues name messages` (the `.dlq` queues must be at 0)                                                                   |
| Pending messages in the outboxes            | `SELECT message_type, attempts, last_error FROM home_outbox WHERE published_at IS NULL AND discarded_at IS NULL;` (same in `security_outbox`, `device_outbox`, `measurement_outbox`, `alert_outbox` and `recommendation_outbox`) |
| See the last 10 audited actions             | `SELECT * FROM audit_log ORDER BY created_at DESC LIMIT 10;`                                                                                                 |
| See the latest sign-in errors               | `SELECT * FROM login_error_log ORDER BY created_at DESC LIMIT 20;`                                                                                           |
| List the active administrators              | `SELECT u.email, r.name FROM user u JOIN user_system_role usr ON usr.user_id = u.id_user JOIN system_role r ON r.id_system_role = usr.system_role_id WHERE usr.deleted_at IS NULL AND u.deleted_at IS NULL;` |
| Force-close a user's sessions               | Change their status from the web, or `UPDATE user_session …` (section 12)                                                                                    |
| Database backup                             | `sudo /opt/energy-monitor/infra/deploy/backup-mysql.sh` (section 13.1)                                                                                       |
| Deploy by hand                              | `sudo /opt/energy-monitor/infra/deploy/deploy.sh <component>` with `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation`, `gateway`, `frontend` or `infra` (section 14) |

# 18. Daily / weekly administrator checklist

| **Check**                                                | **Frequency**  | **Expected state**                                    |
|----------------------------------------------------------|----------------|-------------------------------------------------------|
| Unread administration notices                            | Daily          | New accounts and other administrators' changes reviewed |
| Healthy services (`emc ps`) and `actuator/health`        | Daily          | All `Up`; gateway, security, device, measurement, home, notification, alert and recommendation `(healthy)` |
| Logs of the services and the broker                      | Daily          | No context errors or restarts                         |
| RabbitMQ queues and outboxes                             | Weekly         | `.dlq` queues empty; no `*_outbox` with old pending messages |
| `audit_log` and `login_error_log`                        | Daily          | No `LOGIN_FAILED` bursts or spikes per IP             |
| `FAILED` notifications                                   | Daily          | `FAILED` queue at zero or with a known reason         |
| List of active administrators                            | Weekly         | Only the accounts that should be                      |
| Recent backup in `/opt/energy-monitor/backups`           | Daily          | Today's file; weekly external copy                    |
| Check `.env` and keys are not committed                  | Weekly         | `pem`, `.env` and `.env.prod` ignored                 |

# 19. References (APA format, 7th edition)

In-text citations and the reference list follow the style of the seventh edition of the APA publication manual (American Psychological Association \[APA\], 2020).

American Psychological Association. (2020). *Publication manual of the American Psychological Association* (7th ed.). https://www.apa.org/

Cortés Parra, J. E., & García Artunduaga, M. A. (2026a). *EnergyMonitor software requirements specification* (version 2.1). SENA – CIES Huila Regional.

Cortés Parra, J. E., & García Artunduaga, M. A. (2026b). *EnergyMonitor implementation and deployment manual*. SENA – CIES Huila Regional.

Docker Inc. (n.d.). *Docker documentation*. https://docs.docker.com/

Google. (n.d.). *How to create an app password*. https://myaccount.google.com/apppasswords

MySQL. (2026). *MySQL 8.4 reference manual*. https://dev.mysql.com/doc/refman/8.4/en/

Spring. (n.d.). *Spring Boot reference documentation*. https://docs.spring.io/spring-boot/index.html
