# Production environment

| Component | Platform | Address |
|---|---|---|
| Web application | Vercel | [https://faceattend-frontend.vercel.app](https://faceattend-frontend.vercel.app) |
| API, through Kong | AWS EC2 | `https://faceattend-edu.duckdns.org` |
| Relational database | Neon (PostgreSQL) | The connection is not published |

# Latest deployment

- *Version:* `v1.7.0`, commit `07119ab7`.
- *Date:* November 4, 2026. Times are in UTC.
- *Result:* all stages finished green.
- *Evidence:* [Deploy production run](https://github.com/Jonas7891/ProyectoFaceAttendEDU/actions/runs/41912162302).

| Stage | Jobs | Latest completion (UTC) | Result |
|---|---|---|---|
| CI | 13 | 21:09:59 | Success |
| database / liquibase / neon / production | 1 | 21:13:18 | Success |
| backend / ec2 / production | 1 | 21:18:49 | Success |
| frontend / vercel / production | 1 | 21:25:52 | Success |

# Production check

Check made on November 4, 2026, without signing in:

| Check | Response | Meaning |
|---|---|---|
| `GET https://faceattend-frontend.vercel.app/` | 200 | The web application is published |
| `GET https://faceattend-edu.duckdns.org/api/v1/auth/session` | 401 | The gateway responds and requires a session |

The following image shows the public screen of the web application in production.