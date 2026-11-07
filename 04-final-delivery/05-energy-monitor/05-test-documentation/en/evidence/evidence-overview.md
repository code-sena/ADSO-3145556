# Test Evidence

## Version 1.4 (current status)

Date: October 28, 2026. The repositories that received the latest features change; the rest stays as in version 1.3. `main` commits and totals:

| Repository | Branch | Commit | Note |
|---|---|---|---|
| en-monitor-security-service | main | `de52c95` | 774 tests |
| en-monitor-home-service | main | `6a4e396` | 260 tests |
| en-monitor-notification-service | main | `ecdbc0a` | 235 tests |
| en-monitor-alert-service | main | `0e65430` | 128 tests |
| energy-monitor-web | main | `27ad9f0` | 44 files and 253 tests |
| energy-monitor-mobile | dev | `8b5edc0` | 78 unit tests |

## Version 1.3

Date: October 28, 2026. With the complete migration the core service was retired and every service has its own repository and suite. `main` commits (the ones deployed to production that day) and the totals of each suite, all green:

| Repository | Branch | Commit | Note |
|---|---|---|---|
| en-monitor-api-gateway | main | `0b2c164` | API gateway; 69 tests |
| en-monitor-security-service | main | `f57c76b` | Security service; 773 tests |
| en-monitor-device-service | main | `06c1bdc` | Device service; 129 tests |
| en-monitor-measurement-service | main | `8a9b544` | Measurement service; 151 tests |
| en-monitor-home-service | main | `f424083` | Home service; 260 tests |
| en-monitor-notification-service | main | `7b10398` | Notification service; 234 tests |
| en-monitor-alert-service | main | `f050b56` | Alert service; 119 tests |
| en-monitor-recommendation-service | main | `aeeeec5` | Recommendation service; 71 tests |
| energy-monitor-web | main | `698d4c9` | Web application; 38 files and 228 tests |
| energy-monitor-mobile | dev | `1e4109b` | 78 unit tests in 8 suites, `tsc` and `expo lint` without errors ([mobile/static-verification.md](mobile-static-verification.md)) |
| energy-monitor-infra | main | `ceb1543` | End-to-end tests on the local stack: `device-flow` (24), `measurement-flow` (28), `alert-flow` (36), `activity-flow` (60) and `account-deletion-flow` (12) |

As in version 1.2, no Surefire reports or logs are attached: the totals are the ones each suite reported. The manual test protocol of the mobile application (MOV-01 to MOV-12) is in the test plan and is still to be run on a device.

## Version 1.2

Date: October 24, 2026. From this version on the system is no longer a single backend: the API gateway and the security, device and measurement services have their own repository and their own suite, and `energy-monitor-backend` keeps home, alert, recommendation and notification. `main` commits in production and the totals of each suite, all green:

| Repository | Branch | Commit | Note |
|---|---|---|---|
| en-monitor-api-gateway | main | `c070cec` | API gateway; 57 tests |
| en-monitor-security-service | main | `334691a` | Security service; 660 tests (run of October 22, 2026) |
| en-monitor-device-service | main | `f8b8526` | Device service; 130 tests |
| en-monitor-measurement-service | main | `ee2cb81` | Measurement service; 151 tests |
| energy-monitor-backend | main | `aa9a713` | Core service (home, alert, recommendation and notification) and firmware; 481 tests, `ModularityTests` green |
| energy-monitor-web | main | `e59bece` | Web application |
| energy-monitor-mobile | main | `894b65a` | No automated tests |
| energy-monitor-infra | main | `eaf961b` | End-to-end tests on the local stack: `e2e/device-flow.sh` (21/21) and `e2e/measurement-flow.sh` (25/25) |

No Surefire reports or logs are attached for this version: the totals are the ones each suite reported. The files under `backend/` and `web/` are historical evidence of versions 1.0 and 1.1, when `energy-monitor-backend` contained all seven modules.

## Version 1.1

Run date: October 20, 2026.

| Repository | Branch | Commit | Note |
|---|---|---|---|
| energy-monitor-backend | main | `90ae5ef` | Core service and firmware; full suite against MySQL (1131 tests, 0 failures), also in the `Tests` check of GitHub Actions |
| energy-monitor-web | main | `2b41cb3` | Web application; 141 tests passed, also in the `Tests and build` check |
| energy-monitor-mobile | dev | `4ebccfb` | No automated tests |
| energy-monitor-infra | main | `7324110` | Compose files, scripts and Nginx template validated in the `Validate compose and scripts` check |
| energy-monitor-docs-final | dev | `00806c6` | Final documentation (this document) |

- [backend/full-suite-summary.md](backend-full-suite-summary.md)
- [web/vitest-summary.md](web-vitest-summary.md) (section “Version 1.1 Run”)

## Version 1.0 (historical)

Run date: October 14, 2026.

| Repository | Branch | Commit | Note |
|---|---|---|---|
| energy-monitor-backend | dev | `d03bbd3` | Core service; the tree had 4 new uncommitted test classes |
| energy-monitor-web | dev | `e6edc12` | Web application |
| energy-monitor-mobile | dev | `a3f6875` | No automated tests |
| devices | dev | `57417a7` | Repository now retired: the firmware moved to the backend and the broker to `energy-monitor-infra` |
| energy-monitor-docs-final | dev | `2af09ab` | Final documentation (this document) |

- [backend/surefire-summary.md](backend-surefire-summary.md)
- [backend/log-run-without-db.md](backend-run-without-db-log.md)
- [backend/log-new-tests.md](backend-new-tests-log.md)
- [web/vitest-summary.md](web-vitest-summary.md)

There is no coverage evidence: JaCoCo is not in the backend `pom.xml` and the web defines no coverage script.

Spanish version: [../ES/README.md](evidence-overview.md)
