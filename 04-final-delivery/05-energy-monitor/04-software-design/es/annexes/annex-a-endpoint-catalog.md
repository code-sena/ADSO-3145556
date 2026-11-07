# Anexo A. Catálogo de endpoints por servicio

Versión 1.0 · 28 de octubre de 2026. Lista las rutas públicas de la API (`/api/v1`) que expone cada servicio, extraídas con una lectura automática de los controladores de cada repositorio en `main` (el contrato completo, con cuerpos y respuestas, está en el Swagger de cada servicio en modo desarrollo). El API gateway reparte cada ruta a su servicio; cualquier otra ruta responde 404. Las rutas internas (`/internal/**`) solo se aceptan desde la red de Docker con `X-Internal-Token` y el gateway nunca las reenvía, por lo que aquí solo se cuentan.

## Servicio de seguridad (`en-monitor-security-service`, puerto 8082)

21 rutas públicas y 3 internas.

| Método | Ruta | Controlador |
|---|---|---|
| GET | `/api/v1/admin/roles` | AdminController |
| GET | `/api/v1/admin/users` | AdminController |
| DELETE | `/api/v1/admin/users/{idUser}/roles/{roleName}` | AdminController |
| PUT | `/api/v1/admin/users/{idUser}/roles/{roleName}` | AdminController |
| PUT | `/api/v1/admin/users/{idUser}/status` | AdminController |
| GET | `/api/v1/auth/account` | AuthController |
| POST | `/api/v1/auth/account/delete` | AuthController |
| POST | `/api/v1/auth/account/delete/code` | AuthController |
| POST | `/api/v1/auth/email/verification/resend` | AuthController |
| POST | `/api/v1/auth/email/verify` | AuthController |
| POST | `/api/v1/auth/google` | AuthController |
| POST | `/api/v1/auth/login` | AuthController |
| POST | `/api/v1/auth/logout` | AuthController |
| POST | `/api/v1/auth/password/change` | AuthController |
| POST | `/api/v1/auth/password/forgot` | AuthController |
| POST | `/api/v1/auth/password/reset` | AuthController |
| GET | `/api/v1/auth/permissions` | AuthController |
| PUT | `/api/v1/auth/profile` | AuthController |
| POST | `/api/v1/auth/refresh` | AuthController |
| POST | `/api/v1/auth/register` | AuthController |
| GET | `/api/v1/support/contacts` | SupportController |

## Servicio de dispositivos (`en-monitor-device-service`, puerto 8084)

6 rutas públicas y 4 internas.

| Método | Ruta | Controlador |
|---|---|---|
| GET | `/api/v1/appliance-types` | ApplianceTypeController |
| GET | `/api/v1/homes/{homeId}/devices` | HomeDeviceController |
| POST | `/api/v1/homes/{homeId}/devices` | HomeDeviceController |
| DELETE | `/api/v1/homes/{homeId}/devices/{deviceId}` | HomeDeviceController |
| PUT | `/api/v1/homes/{homeId}/devices/{deviceId}` | HomeDeviceController |
| POST | `/api/v1/homes/{homeId}/devices/{deviceId}/credentials` | HomeDeviceController |

## Servicio de mediciones (`en-monitor-measurement-service`, puerto 8085)

6 rutas públicas y 3 internas.

| Método | Ruta | Controlador |
|---|---|---|
| GET | `/api/v1/consumption-levels` | ConsumptionLevelController |
| GET | `/api/v1/homes/{homeId}/consumption/history` | HomeConsumptionController |
| GET | `/api/v1/homes/{homeId}/consumption/summary` | HomeConsumptionController |
| GET | `/api/v1/measurements` | MeasurementController |
| GET | `/api/v1/measurements/latest` | MeasurementController |
| GET | `/api/v1/measurements/statistics` | MeasurementController |

## Servicio de hogares (`en-monitor-home-service`, puerto 8086)

14 rutas públicas y 4 internas.

| Método | Ruta | Controlador |
|---|---|---|
| GET | `/api/v1/home-types` | HomeTypeController |
| GET | `/api/v1/home-types/{idHomeType}` | HomeTypeController |
| GET | `/api/v1/homes` | HomeController |
| POST | `/api/v1/homes` | HomeController |
| POST | `/api/v1/homes/join` | HomeController |
| DELETE | `/api/v1/homes/{homeId}` | HomeController |
| PUT | `/api/v1/homes/{homeId}/favorite` | HomeController |
| GET | `/api/v1/homes/{homeId}/members` | HomeController |
| DELETE | `/api/v1/homes/{homeId}/members/me` | HomeController |
| DELETE | `/api/v1/homes/{homeId}/members/{userId}` | HomeController |
| PUT | `/api/v1/homes/{homeId}/owner` | HomeController |
| GET | `/api/v1/homes/{homeId}/thresholds` | HomeThresholdsController |
| PUT | `/api/v1/homes/{homeId}/thresholds` | HomeThresholdsController |
| POST | `/api/v1/homes/{homeId}/thresholds/defaults` | HomeThresholdsController |

## Servicio de notificaciones (`en-monitor-notification-service`, puerto 8087)

15 rutas públicas y 0 internas.

| Método | Ruta | Controlador |
|---|---|---|
| GET | `/api/v1/admin/notifications` | AdminNotificationsController |
| DELETE | `/api/v1/admin/notifications/{id}` | AdminNotificationsController |
| PUT | `/api/v1/admin/notifications/{id}/read` | AdminNotificationsController |
| GET | `/api/v1/notifications/activity` | ActivityNotificationsController |
| PUT | `/api/v1/notifications/activity/read` | ActivityNotificationsController |
| DELETE | `/api/v1/notifications/activity/{id}` | ActivityNotificationsController |
| PUT | `/api/v1/notifications/activity/{id}/read` | ActivityNotificationsController |
| GET | `/api/v1/notifications/preferences` | NotificationChannelsController |
| PUT | `/api/v1/notifications/preferences` | NotificationChannelsController |
| POST | `/api/v1/notifications/push/devices` | NotificationChannelsController |
| GET | `/api/v1/notifications/push/devices/available` | NotificationChannelsController |
| POST | `/api/v1/notifications/push/devices/remove` | NotificationChannelsController |
| GET | `/api/v1/notifications/push/public-key` | NotificationChannelsController |
| POST | `/api/v1/notifications/push/subscriptions` | NotificationChannelsController |
| POST | `/api/v1/notifications/push/subscriptions/remove` | NotificationChannelsController |

## Servicio de alertas (`en-monitor-alert-service`, puerto 8088)

5 rutas públicas y 0 internas.

| Método | Ruta | Controlador |
|---|---|---|
| DELETE | `/api/v1/alerts` | AlertController |
| GET | `/api/v1/alerts` | AlertController |
| DELETE | `/api/v1/alerts/{idAlert}` | AlertController |
| GET | `/api/v1/alerts/{idAlert}` | AlertController |
| PUT | `/api/v1/alerts/{idAlert}/read` | AlertController |

## Servicio de recomendaciones (`en-monitor-recommendation-service`, puerto 8089)

4 rutas públicas y 0 internas.

| Método | Ruta | Controlador |
|---|---|---|
| DELETE | `/api/v1/recommendations` | RecommendationController |
| GET | `/api/v1/recommendations` | RecommendationController |
| DELETE | `/api/v1/recommendations/{idRecommendation}` | RecommendationController |
| PUT | `/api/v1/recommendations/{idRecommendation}/read` | RecommendationController |

## Resumen

Los siete servicios exponen 71 rutas públicas y 14 internas. El gateway no declara rutas propias: solo reparte las de los servicios, valida el token y rechaza `/internal/**`.

Versión en inglés: [../EN/annex-a-endpoint-catalog.md](annex-a-endpoint-catalog.md)
