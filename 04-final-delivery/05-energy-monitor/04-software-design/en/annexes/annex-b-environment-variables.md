# Annex B. Production environment variables

Version 1.0 · October 28, 2026. Variables that `docker-compose.prod.yml` takes from `infra/.env.prod` and the service that uses them, read from the file on `main` of `energy-monitor-infra`. Only names and purpose are documented: no value appears here. Secrets are generated on the server (`openssl rand -hex 24`), live only in `.env.prod` (permissions 600) and go to no repository.

## Variables by purpose

| Variable | Type | Services | Purpose |
|---|---|---|---|
| `EXPO_ACCESS_TOKEN` | Secret | `notification` | Expo token for mobile push |
| `GOOGLE_CLIENT_SECRET` | Secret | `security` | Google OAuth client secret |
| `INTERNAL_API_TOKEN` | Secret | `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | `X-Internal-Token` header of the internal calls between services |
| `MAIL_PASSWORD` | Secret | `notification` | Application password of the email account |
| `MQTT_DEVICE_SERVICE_PASS` | Secret | `device` | MQTT password of the device service |
| `MQTT_MEASUREMENT_SERVICE_PASS` | Secret | `device`, `measurement` | MQTT password of the measurement service |
| `MYSQL_PASSWORD` | Secret | `mysql`, `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | Password of the MySQL `energy_app` user |
| `MYSQL_ROOT_PASSWORD` | Secret | `mysql` | MySQL `root` password |
| `NOTIFICATION_VAPID_PRIVATE_KEY` | Secret | `notification` | Web Push VAPID private key (single line) |
| `RABBITMQ_PASSWORD` | Secret | `rabbitmq`, `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | RabbitMQ password, the same in every service |
| `SECURITY_RESET_PEPPER` | Secret | `security` | HMAC pepper of the codes (verification, recovery and deletion); changing it invalidates the codes already sent |
| `ALERT_LIMIT_ZONE` | Configuration | `alert` | Time zone of the daily and monthly limits |
| `BACKEND_GID` | Configuration | `security` | Group the security service runs as |
| `BACKEND_UID` | Configuration | `security` | User the security service runs as, owner of the keys |
| `DEVICE_MQTT_PUBLIC_HOST` | Configuration | `device` | Broker domain the web app writes to the meter (`mqtt.energy-monitor.tech`) |
| `DEVICE_MQTT_PUBLIC_PORT` | Configuration | `device` | Broker TLS port (8883) |
| `GOOGLE_CLIENT_ID` | Configuration | `security` | Google OAuth client (public) |
| `MAIL_FROM` | Configuration | `notification` | Sender of the emails |
| `MAIL_HOST` | Configuration | `notification` | SMTP server |
| `MAIL_PORT` | Configuration | `notification` | SMTP port |
| `MAIL_USERNAME` | Configuration | `notification` | Sending email account |
| `MQTT_DEVICE_SERVICE_USER` | Configuration | `device` | MQTT user of the device service |
| `MQTT_MEASUREMENT_SERVICE_USER` | Configuration | `device`, `measurement` | MQTT user of the measurement service |
| `NOTIFICATION_VAPID_PUBLIC_KEY` | Configuration | `notification` | Web Push VAPID public key |
| `NOTIFICATION_WEB_URL` | Configuration | `notification` | Public address for the links in the emails |
| `RABBITMQ_USERNAME` | Configuration | `rabbitmq`, `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | RabbitMQ user, the same in every service |
| `RECOMMENDATION_INITIAL_DELAY` | Configuration | `recommendation` | Wait before the first evaluation |
| `RECOMMENDATION_ZONE` | Configuration | `recommendation` | Time zone of the recommendations |
| `SECURITY_CORS_ALLOWED_ORIGINS` | Configuration | `gateway` | Origins allowed by the gateway |
| `VITE_GOOGLE_CLIENT_ID` | Configuration | `frontend` | Google client of the web button; baked in when the image is built |
| `WEB_PORT` | Configuration | `frontend` | Local port Nginx points to (8081) |
| `JWT_KEYS_DIR` | Path | `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation`, `gateway` | Folder of `private.pem` (security only) and `public.pem` (the others) |
| `MQTT_CERT_DIR` | Path | `mosquitto` | Folder with the broker's Let's Encrypt certificate |
| `ACCOUNT_DELETED_CONSUMER_ENABLED` | Switch | `home` | Cleans the data of a deleted account; `true` by default |
| `ACTIVITY_CONSUMER_ENABLED` | Switch | `notification` | Consumes home and device activity; `true` by default |
| `ALERT_CONSUMERS_ENABLED` | Switch | `alert` | Consumes readings and device events to create alerts; `true` by default |
| `ALERT_EVENTS_CONSUMER_ENABLED` | Switch | `notification` | Consumes `alert.raised` and `recommendation.created`; `true` by default |
| `ALERT_SERVICE_OUTBOX_RELAY_ENABLED` | Switch | `alert` | Publishes `alert.raised` from the *outbox*; `true` by default |
| `HOME_OUTBOX_RELAY_ENABLED` | Switch | `home` | Publishes the home *outbox* events; `true` by default |
| `RECOMMENDATION_OUTBOX_RELAY_ENABLED` | Switch | `recommendation` | Publishes `recommendation.created` from the *outbox*; `true` by default |
| `RECOMMENDATION_SCHEDULER_ENABLED` | Switch | `recommendation` | Hourly evaluation of the rules; `true` by default |
| `SECURITY_ACCOUNT_DELETION_CODE_MAIL_ENABLED` | Switch | `security` | Email with the deletion code of Google accounts; turn on only with notifications deployed (off, asking for the code answers 503) |
| `SECURITY_EVENTS_CONSUMER_ENABLED` | Switch | `notification` | Consumes the account events and the mail; `true` by default |

## Clone paths

Each service is built from a clone next to `infra`. The variables `ALERT_CONTEXT`, `DEVICE_CONTEXT`, `FRONTEND_CONTEXT`, `GATEWAY_CONTEXT`, `HOME_CONTEXT`, `MEASUREMENT_CONTEXT`, `NOTIFICATION_CONTEXT`, `RECOMMENDATION_CONTEXT`, `SECURITY_CONTEXT` change that path; by default they point to `../backend/<repository>` and `../frontend/energy-monitor-web`, so they are only set if the clones are elsewhere.

## Alert service properties (not `.env.prod` variables)

| Property | Default | What it controls |
|---|---|---|
| `alert.anomaly.window` | `P7D` | The days of history from which the usual power of a device is computed for the unusual consumption alert |
| `alert.anomaly.baseline-ttl` | `PT10M` | How long that usual power is reused before asking the measurement service again |

The thresholds of the alert (2.5 times, 200 W, 24 hours of history and a 5 W minimum average) are constants of the service, not properties.

## Notes

- `.env.prod.example` keeps, by inheritance, the `BACKEND_CONTEXT` line of the retired backend and does not list `ALERT_CONTEXT` or `RECOMMENDATION_CONTEXT`: it has no effect, because the compose file defines their default, but the template should be aligned.
- `VITE_*` variables are baked in when the frontend image is built: changing them requires rebuilding `frontend`.
- A switch that was set to `false` because of the migration must be deleted from `.env.prod` (they are `true` by default).

Spanish version: [../ES/anexo-b-variables-de-entorno.md](annex-b-environment-variables.md)
