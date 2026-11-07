# Anexo B. Variables de entorno de producción

Versión 1.0 · 28 de octubre de 2026. Variables que `docker-compose.prod.yml` toma de `infra/.env.prod` y servicio que las usa, leídas del archivo en `main` de `energy-monitor-infra`. Solo se documentan nombres y propósito: ningún valor aparece aquí. Los secretos se generan en el servidor (`openssl rand -hex 24`), viven solo en `.env.prod` (permisos 600) y no van a ningún repositorio.

## Variables por propósito

| Variable | Tipo | Servicios | Propósito |
|---|---|---|---|
| `EXPO_ACCESS_TOKEN` | Secreto | `notification` | Token de Expo para el push móvil |
| `GOOGLE_CLIENT_SECRET` | Secreto | `security` | Secreto del cliente OAuth de Google |
| `INTERNAL_API_TOKEN` | Secreto | `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | Encabezado `X-Internal-Token` de las llamadas internas entre servicios |
| `MAIL_PASSWORD` | Secreto | `notification` | Contraseña de aplicación de la cuenta de correo |
| `MQTT_DEVICE_SERVICE_PASS` | Secreto | `device` | Contraseña MQTT del servicio de dispositivos |
| `MQTT_MEASUREMENT_SERVICE_PASS` | Secreto | `device`, `measurement` | Contraseña MQTT del servicio de mediciones |
| `MYSQL_PASSWORD` | Secreto | `mysql`, `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | Contraseña del usuario `energy_app` de MySQL |
| `MYSQL_ROOT_PASSWORD` | Secreto | `mysql` | Contraseña de `root` de MySQL |
| `NOTIFICATION_VAPID_PRIVATE_KEY` | Secreto | `notification` | Clave privada VAPID de Web Push (una sola línea) |
| `RABBITMQ_PASSWORD` | Secreto | `rabbitmq`, `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | Contraseña de RabbitMQ, la misma en todos los servicios |
| `SECURITY_RESET_PEPPER` | Secreto | `security` | Pepper del HMAC de los códigos (verificación, recuperación y borrado); cambiarlo invalida los códigos ya enviados |
| `ALERT_LIMIT_ZONE` | Configuración | `alert` | Zona horaria de los límites diario y mensual |
| `BACKEND_GID` | Configuración | `security` | Grupo con el que corre el servicio de seguridad |
| `BACKEND_UID` | Configuración | `security` | Usuario con el que corre el servicio de seguridad, dueño de las claves |
| `DEVICE_MQTT_PUBLIC_HOST` | Configuración | `device` | Dominio del broker que la web escribe al medidor (`mqtt.energy-monitor.tech`) |
| `DEVICE_MQTT_PUBLIC_PORT` | Configuración | `device` | Puerto TLS del broker (8883) |
| `GOOGLE_CLIENT_ID` | Configuración | `security` | Cliente OAuth de Google (público) |
| `MAIL_FROM` | Configuración | `notification` | Remitente de los correos |
| `MAIL_HOST` | Configuración | `notification` | Servidor SMTP |
| `MAIL_PORT` | Configuración | `notification` | Puerto SMTP |
| `MAIL_USERNAME` | Configuración | `notification` | Cuenta de correo de envío |
| `MQTT_DEVICE_SERVICE_USER` | Configuración | `device` | Usuario MQTT del servicio de dispositivos |
| `MQTT_MEASUREMENT_SERVICE_USER` | Configuración | `device`, `measurement` | Usuario MQTT del servicio de mediciones |
| `NOTIFICATION_VAPID_PUBLIC_KEY` | Configuración | `notification` | Clave pública VAPID de Web Push |
| `NOTIFICATION_WEB_URL` | Configuración | `notification` | Dirección pública para los enlaces de los correos |
| `RABBITMQ_USERNAME` | Configuración | `rabbitmq`, `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation` | Usuario de RabbitMQ, el mismo en todos los servicios |
| `RECOMMENDATION_INITIAL_DELAY` | Configuración | `recommendation` | Espera antes de la primera evaluación |
| `RECOMMENDATION_ZONE` | Configuración | `recommendation` | Zona horaria de las recomendaciones |
| `SECURITY_CORS_ALLOWED_ORIGINS` | Configuración | `gateway` | Orígenes permitidos por el gateway |
| `VITE_GOOGLE_CLIENT_ID` | Configuración | `frontend` | Cliente de Google del botón de la web; se incorpora al compilar la imagen |
| `WEB_PORT` | Configuración | `frontend` | Puerto local al que apunta Nginx (8081) |
| `JWT_KEYS_DIR` | Ruta | `security`, `device`, `measurement`, `home`, `notification`, `alert`, `recommendation`, `gateway` | Carpeta de `private.pem` (solo seguridad) y `public.pem` (los demás) |
| `MQTT_CERT_DIR` | Ruta | `mosquitto` | Carpeta con el certificado Let's Encrypt del broker |
| `ACCOUNT_DELETED_CONSUMER_ENABLED` | Interruptor | `home` | Limpia los datos de una cuenta borrada; `true` por defecto |
| `ACTIVITY_CONSUMER_ENABLED` | Interruptor | `notification` | Consume la actividad de hogares y dispositivos; `true` por defecto |
| `ALERT_CONSUMERS_ENABLED` | Interruptor | `alert` | Consume lecturas y eventos de dispositivos para crear alertas; `true` por defecto |
| `ALERT_EVENTS_CONSUMER_ENABLED` | Interruptor | `notification` | Consume `alert.raised` y `recommendation.created`; `true` por defecto |
| `ALERT_SERVICE_OUTBOX_RELAY_ENABLED` | Interruptor | `alert` | Publica `alert.raised` desde el *outbox*; `true` por defecto |
| `HOME_OUTBOX_RELAY_ENABLED` | Interruptor | `home` | Publica los eventos del *outbox* de hogares; `true` por defecto |
| `RECOMMENDATION_OUTBOX_RELAY_ENABLED` | Interruptor | `recommendation` | Publica `recommendation.created` desde el *outbox*; `true` por defecto |
| `RECOMMENDATION_SCHEDULER_ENABLED` | Interruptor | `recommendation` | Evaluación horaria de las reglas; `true` por defecto |
| `SECURITY_ACCOUNT_DELETION_CODE_MAIL_ENABLED` | Interruptor | `security` | Correo con el código de borrado de cuentas de Google; encender solo con notificaciones desplegado (apagado, pedir el código responde 503) |
| `SECURITY_EVENTS_CONSUMER_ENABLED` | Interruptor | `notification` | Consume los eventos de cuentas y el correo; `true` por defecto |

## Rutas de los clones

Cada servicio se construye desde un clon vecino de `infra`. Las variables `ALERT_CONTEXT`, `DEVICE_CONTEXT`, `FRONTEND_CONTEXT`, `GATEWAY_CONTEXT`, `HOME_CONTEXT`, `MEASUREMENT_CONTEXT`, `NOTIFICATION_CONTEXT`, `RECOMMENDATION_CONTEXT`, `SECURITY_CONTEXT` cambian esa ruta; por defecto apuntan a `../backend/<repositorio>` y `../frontend/energy-monitor-web`, así que solo se definen si los clones están en otro lugar.

## Propiedades del servicio de alertas (no son variables de `.env.prod`)

| Propiedad | Por defecto | Qué controla |
|---|---|---|
| `alert.anomaly.window` | `P7D` | Los días de historial con los que se calcula la potencia habitual de un dispositivo para la alerta de consumo inusual |
| `alert.anomaly.baseline-ttl` | `PT10M` | Cuánto se reutiliza esa potencia habitual antes de pedirla de nuevo al servicio de mediciones |

Los umbrales de la alerta (2,5 veces, 200 W, 24 horas de historial y 5 W de promedio mínimo) son constantes del servicio, no propiedades.

## Notas

- `.env.prod.example` conserva por herencia la línea `BACKEND_CONTEXT` del backend retirado, y no lista `ALERT_CONTEXT` ni `RECOMMENDATION_CONTEXT`: no afecta, porque la compose define su valor por defecto, pero conviene alinear la plantilla.
- Las variables `VITE_*` se incorporan al compilar la imagen del frontend: cambiarlas exige reconstruir `frontend`.
- Un interruptor que estuviera en `false` por haber sido de la migración debe borrarse de `.env.prod` (valen `true` por defecto).

Versión en inglés: [../EN/annex-b-environment-variables.md](annex-b-environment-variables.md)
