# Continuous integration

- **Tested version:** `v1.7.0`, commit `07119ab7`, branch `main`.
- **Run date:** November 4, 2026.
- **Result:** all 13 jobs finished green.
- **Evidence:** [CI run on GitHub Actions](https://github.com/Jonas7891/ProyectoFaceAttendEDU/actions/runs/41912162243).

| Component | Technology | Total | Passed | Failed | Skipped |
|---|---|---|---|---|---|
| 01-ms-identity | Java / Maven | 442 | 428 | 0 | 14 |
| 02-ms-authorization | Java / Maven | 142 | 142 | 0 | 0 |
| 04-ms-scheduling | Java / Maven | 145 | 144 | 0 | 1 |
| 05-ms-attendance | Java / Maven | 193 | 192 | 0 | 1 |
| shared / auth | Java / Maven | 19 | 19 | 0 | 0 |
| 03-ms-academic | TypeScript / Jest | 126 | 126 | 0 | 0 |
| 07-ms-configuration | TypeScript / Jest | 70 | 70 | 0 | 0 |
| 08-ms-notification | TypeScript / Jest | 201 | 201 | 0 | 0 |
| 09-ms-quality | TypeScript / Jest | 159 | 159 | 0 | 0 |
| 06-ms-biometric | Python / pytest | 213 | 213 | 0 | 0 |
| Web | Expo / Jest | 998 | 909 | 0 | 89 |
| Mobile | Expo / Jest | 614 | 614 | 0 | 0 |
| **Total** | | **3322** | **3217** | **0** | **105** |

The `openapi / drift` job also finished green. It is not in the table because it compares contracts and does not report a test count.

The totals come from the logs of that run. For the Java services, the passed column subtracts the skipped tests from the total Maven reports.

# End-to-end flows

- **Workflow:** `E2E flujos MVP`.
- **Tested commit:** `b67a5933`, from November 2, 2026.
- **Result:** finished green.
- **Evidence:** [E2E run on GitHub Actions](https://github.com/Jonas7891/ProyectoFaceAttendEDU/actions/runs/41507144757).

This run predates `v1.7.0`. It is the most recent E2E run on `main` at the time of this delivery.

# Full functional review

Run locally on October 31, 2026, with `verificar-estado.mjs`.

| Reviewed item | Total | Passed | Failed | Warnings | Skipped |
|---|---|---|---|---|---|
| API endpoints | 108 | 86 | 0 | 0 | 22 |
| Web screens | 49 | 43 | 0 | 0 | 6 |
| Web buttons | 53 | 39 | 0 | 4 | 10 |
| Mobile screens | 27 | 27 | 0 | 0 | 0 |

The coherence check reviewed 44 points and 8,755 records in 24 lists, with no findings. The contract review of November 1, 2026 passed all 88 operations compared between the web and the database.

# Limitations

- These two local reviews predate `v1.7.0` and were not repeated for this delivery.
- Skipped tests provide no evidence for the cases they cover: 16 in Java services and 89 on the web.
- The four buttons with warnings remain as review items.