# Annex A. Endpoint catalog per service

Version 1.0 · October 28, 2026. It lists the public API routes (`/api/v1`) each service exposes, extracted by an automatic reading of the controllers of each repository on `main` (the full contract, with bodies and responses, is in each service's Swagger in development mode). The API gateway sends each route to its service; any other route answers 404. Internal routes (`/internal/**`) are accepted only from the Docker network with `X-Internal-Token` and the gateway never forwards them, so here they are only counted.

## Security service (`en-monitor-security-service`, port 8082)

21 public routes and 3 internal ones.

| Method | Route | Controller |
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

## Device service (`en-monitor-device-service`, port 8084)

6 public routes and 4 internal ones.

| Method | Route | Controller |
|---|---|---|
| GET | `/api/v1/appliance-types` | ApplianceTypeController |
| GET | `/api/v1/homes/{homeId}/devices` | HomeDeviceController |
| POST | `/api/v1/homes/{homeId}/devices` | HomeDeviceController |
| DELETE | `/api/v1/homes/{homeId}/devices/{deviceId}` | HomeDeviceController |
| PUT | `/api/v1/homes/{homeId}/devices/{deviceId}` | HomeDeviceController |
| POST | `/api/v1/homes/{homeId}/devices/{deviceId}/credentials` | HomeDeviceController |

## Measurement service (`en-monitor-measurement-service`, port 8085)

6 public routes and 3 internal ones.

| Method | Route | Controller |
|---|---|---|
| GET | `/api/v1/consumption-levels` | ConsumptionLevelController |
| GET | `/api/v1/homes/{homeId}/consumption/history` | HomeConsumptionController |
| GET | `/api/v1/homes/{homeId}/consumption/summary` | HomeConsumptionController |
| GET | `/api/v1/measurements` | MeasurementController |
| GET | `/api/v1/measurements/latest` | MeasurementController |
| GET | `/api/v1/measurements/statistics` | MeasurementController |

## Home service (`en-monitor-home-service`, port 8086)

14 public routes and 4 internal ones.

| Method | Route | Controller |
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

## Notification service (`en-monitor-notification-service`, port 8087)

15 public routes and 0 internal ones.

| Method | Route | Controller |
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

## Alert service (`en-monitor-alert-service`, port 8088)

5 public routes and 0 internal ones.

| Method | Route | Controller |
|---|---|---|
| DELETE | `/api/v1/alerts` | AlertController |
| GET | `/api/v1/alerts` | AlertController |
| DELETE | `/api/v1/alerts/{idAlert}` | AlertController |
| GET | `/api/v1/alerts/{idAlert}` | AlertController |
| PUT | `/api/v1/alerts/{idAlert}/read` | AlertController |

## Recommendation service (`en-monitor-recommendation-service`, port 8089)

4 public routes and 0 internal ones.

| Method | Route | Controller |
|---|---|---|
| DELETE | `/api/v1/recommendations` | RecommendationController |
| GET | `/api/v1/recommendations` | RecommendationController |
| DELETE | `/api/v1/recommendations/{idRecommendation}` | RecommendationController |
| PUT | `/api/v1/recommendations/{idRecommendation}/read` | RecommendationController |

## Summary

The seven services expose 71 public routes and 14 internal ones. The gateway declares no routes of its own: it only distributes the services' routes, validates the token and rejects `/internal/**`.

Spanish version: [../ES/anexo-a-catalogo-de-endpoints.md](annex-a-endpoint-catalog.md)
