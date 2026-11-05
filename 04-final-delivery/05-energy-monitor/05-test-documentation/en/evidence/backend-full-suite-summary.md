# Full Suite Summary (backend)

Version 1.1 of the test plan. No environment variables or credentials.

**Final result (October 20, 2026):** full `./mvnw test`, test profile, against MySQL 8.4: **1131 tests, 0 failures, 0 errors, 0 skipped**. It includes the 37 classes that were “To run (MySQL)” in version 1.0.

**Continuous integration:** the required `Tests` check of the `Backend CI/CD` workflow (GitHub Actions) runs the same suite against a `mysql:8.4` service container on every pull request and push to `dev` and `main`. Run on `main`, commit `90ae5ef` (October 19, 2026): `Tests` green in 23 min 9 s.

**Per-module distribution** of the most recent local Surefire report (`target/surefire-reports`, October 16, 2026). 7 tests were added between this report and the final run.

| Module | Classes | Run | Passed | Failed | Skipped | Time (s) |
|---|---|---|---|---|---|---|
| (root) | 8 | 25 | 25 | 0 | 0 | 305.0 |
| alert | 10 | 52 | 52 | 0 | 0 | 81.9 |
| device | 6 | 48 | 48 | 0 | 0 | 77.0 |
| home | 28 | 179 | 179 | 0 | 0 | 68.8 |
| measurement | 13 | 75 | 75 | 0 | 0 | 78.8 |
| notification | 13 | 93 | 93 | 0 | 0 | 199.0 |
| recommendation | 5 | 26 | 26 | 0 | 0 | 62.8 |
| security | 49 | 626 | 626 | 0 | 0 | 773.9 |
| **Total** | 132 | 1124 | 1124 | 0 | 0 | 1647.2 |

**Failures:** none. DEF-01 and DEF-02 are closed (see Table 35 of the test plan).

**New classes since version 1.0:** PushDeviceTest (5), ExpoPushSenderTest (9) and PushDevicePersistenceTest (7), in the notification module.

**Coverage:** JaCoCo is not in the `pom.xml`; there is still no coverage report.
