# Annex D. Timeline of the migration to microservices

Version 1.0 · October 28, 2026. It summarizes how the system went from the monolithic backend to an API gateway and seven services, and what happened on the day of the complete deployment. The step-by-step procedure is in the implementation and deployment manual (4.12 and 4.13) and the incidents in its section 7.

## Before October

The system started as a modular monolith (`energy-monitor-backend`, Spring Modulith) with seven modules: security, home, device, measurement, alert, recommendation and notification.

## October 24, 2026

Security, devices and measurements leave the backend for services of their own behind an API gateway, with internal REST under `/internal/**` and events on RabbitMQ with a *transactional outbox*. The backend keeps home, alert, recommendation and notification, plus three proxy packages with no domain and no tables. The public API routes do not change.

## October 25 to 27, 2026 (until the code freeze)

Miguel Ángel García Artunduaga extracts home, notification, alert and recommendation, in that order, each with its own repository, container and event queue. Juan Esteban Cortés Parra adds to security the registration with prior email verification (the account is created on verification and the request expires after 24 hours), the rejection of domains with a typing error, the mandatory acceptance of the terms (answer 428 if missing), the rules for Google accounts (no password change and deletion with a code sent by email) and the independent editing of the first and last name. On October 27 the backend retirement is merged into `main` (PR no. 80 to `dev` and no. 81 to `main`). At the close the suites of the eight services and the five end-to-end tests were run on the local stack.

## October 28, 2026

| Approx. time | Event |
|---|---|
| Before 03:40 | MySQL backup, cloning of the four new repositories and review of `.env.prod` |
| 03:40 | First `deploy.sh infra`: it repeats `no such service: backend` because the script in memory was the old one; interrupted with `Ctrl+C` |
| 03:54 | Second `deploy.sh infra`: `OK` with infra at `ceb1543`; home, notification, alert and recommendation are built and the backend is removed |
| Afterwards | `deploy.sh` of `gateway`, `device`, `measurement`, `frontend` and `security`, each one `OK` |
| Afterwards | The ten `energy-monitor-backend.*` queues are deleted and verified: eight services `healthy`, `GET /api/v1/homes` → 401 |
| 04:00 | `SECURITY_ACCOUNT_DELETION_CODE_MAIL_ENABLED=true` and a third `deploy.sh infra` `OK` |

Later finding: the web deployment rebuilt the previous version (`e59bece`) because its PR to `main` was not merged; with the new security service, registration answered 428 until the new web app was published. The merge left `main` at `698d4c9`.

## October 28, 2026, after the migration

With the system already in production five changes were added, each in its own branch with its PR to `dev` and then to `main`:

- Security and homes: the profile photo travels in the internal profile and the members of a home show it.
- Alerts and notifications: unusual consumption alert of a device and its text in mail and push.
- Web: help modals in Settings (documentation by role, support for users and news assembled from the git history), photo viewer inside the page, homes list that closes on an outside click and an explanation of push in Brave.

## Continuous integration and secrets

The same day the CI of every repository was reviewed: nine already had `ci-cd.yml` with deployment, and the notification service, the only deployable without a pipeline, got its own written. The `production` environment and its four secrets were then created in the repositories that lacked them; two failures of the deploy job (`remote username contains invalid characters` and `hostname contains invalid characters`) were caused by a trailing newline or space in `DEPLOY_USER` and `DEPLOY_HOST`.

## Status at closing

- API gateway and seven services in production, backend retired and its queues deleted.
- Confirmed by the team: the new web app and every service in production, and CI/CD in every repository except the mobile app.
- Evidence pending: deletion of a Google account with the emailed code.
- Mobile app: 78 unit tests in 8 suites passed; still to rebuild the APK, which does not send `acceptTerms`, and to run its manual test protocol.

Spanish version: [../ES/anexo-d-cronologia-de-la-migracion.md](annex-d-migration-timeline.md)
