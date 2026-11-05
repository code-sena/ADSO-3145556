# Annex E. Response codes and how to diagnose them

Version 1.0 · October 28, 2026. HTTP codes the API returns in situations the team had to diagnose, with their cause and the action. It is a quick guide during the demonstration and operation.

| Code | Where | When it happens | What to do |
|---|---|---|---|
| 202 | `POST /api/v1/auth/register` | The registration request was stored and the code was sent; it answers `{email, expiresAt}`. The account does not exist yet | Verify the email with `POST /api/v1/auth/email/verify` within 24 hours |
| 202 | `POST /api/v1/auth/account/delete/code` | The deletion code was sent to the email of a Google account | Enter the code at `POST /api/v1/auth/account/delete` |
| 400 | Registration and email change | Domain that looks like a typing error of a well-known provider (`meil.com`) or that does not exist or receive mail | Fix the address; the message names the probable provider |
| 400 | Verification and deletion with a code | Wrong or expired code | Ask for a new one (resending always answers 202) |
| 401 | Any protected route | No session or invalid token; also the wrong password when deleting the account | Sign in; it is not a service failure |
| 404 | Gateway | Route that no service handles | Check the route (Annex A) |
| 405 | `https://energy-monitor.tech/internal/...` | Nginx rejects `/internal/**`, which must not be reachable from the Internet | It is expected |
| 409 | Registration | The email already has an account | Sign in or recover the password |
| 409 | `POST /api/v1/auth/password/change` | Account created with Google: it has no password | It is expected; use Google |
| 409 | `POST /api/v1/auth/account/delete/code` | Account with a password: it confirms with it, not with a code | Use the password |
| 428 | Registration, first Google sign-in and sign-in | The account did not accept the terms (`acceptTerms` is missing or false) | Accept the terms; if it happens with the production web app, a version older than the terms is deployed |
| 429 | Gateway | More than 20 public account POSTs per minute from the same IP and route | Wait a minute; consecutive end-to-end tests can trigger it |
| 429 | Codes | Too many code attempts (10 in recovery, 5 in verification and in deletion, per address and 15 minutes) | Wait the time the response indicates |
| 503 | Gateway | The service of that route does not answer (starting or down) | `emc ps` and `emc logs --tail 100 <service>` |
| 503 | `POST /api/v1/auth/account/delete/code` | `SECURITY_ACCOUNT_DELETION_CODE_MAIL_ENABLED` is off | Turn it on when notifications is deployed (Annex B) |
| 504 | Gateway | The service took more than 30 s | Check the load and the service logs |

Spanish version: [../ES/anexo-e-codigos-de-respuesta.md](annex-e-response-codes.md)
