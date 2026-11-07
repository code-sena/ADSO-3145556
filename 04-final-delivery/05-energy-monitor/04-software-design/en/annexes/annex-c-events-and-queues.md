# Annex C. RabbitMQ events and queues

Version 1.0 · October 28, 2026. The services notify each other through events on RabbitMQ 4.1 with a *transactional outbox*: the message row is written in the same transaction as the change, a relay publishes it every second with broker confirmation, delivery is at least once, the queues are durable and each has its `.dlq` (*dead-letter* after three attempts). An *outbox* stops at a message that no queue receives, which is why the consuming service is deployed before the producer.

## Events, producers and queues

| Event | Producer | Consuming queue | Consumer |
|---|---|---|---|
| `measurement.recorded` | measurements | `alert-service.measurement.recorded` | alerts |
| `device.connectivity-lost`, `device.connectivity-restored` | devices | `alert-service.device.events` | alerts |
| `device.linked`, `device.unlinked` | devices | `alert-service.device.events` and `notification-service.notification.activity` | alerts and notifications; measurements use them for their cache |
| `alert.raised` | alerts | `notification-service.notification.alert-raised` | notifications (also the unusual consumption alert, key `alert.anomaly.device`) |
| `recommendation.created` | recommendations | `notification-service.notification.recommendation-created` | notifications |
| `account.registered`, `account.administered` | security | `notification-service.notification.account-events` | notifications |
| `account.deleted` | security | `home-service.home.account-deleted` and `notification-service.notification.account-events` | homes and notifications (cleanup) |
| `mail.email-verification`, `mail.password-reset`, `mail.account-deletion` | security | `notification-service.notification.mail` | notifications (the only one that sends email) |
| `home.created`, `home.member-joined`, `home.member-left`, `home.member-removed`, `home.thresholds-updated`, `home.ownership-transferred` | homes | `notification-service.notification.activity` | notifications |
| `home.deleted` | homes | `notification-service.notification.activity` and the device service's queue | notifications and devices (unlinks the modules and sends `unlink`) |

## Switches of the consumers and relays

All are `true` by default and exist only to roll back during a migration: `HOME_OUTBOX_RELAY_ENABLED`, `ACCOUNT_DELETED_CONSUMER_ENABLED`, `SECURITY_EVENTS_CONSUMER_ENABLED`, `ALERT_EVENTS_CONSUMER_ENABLED`, `ACTIVITY_CONSUMER_ENABLED`, `ALERT_CONSUMERS_ENABLED`, `ALERT_SERVICE_OUTBOX_RELAY_ENABLED`, `RECOMMENDATION_SCHEDULER_ENABLED` and `RECOMMENDATION_OUTBOX_RELAY_ENABLED` (Annex B).

## Retired queues

With the backend retirement on October 28, 2026 the ten `energy-monitor-backend.*` queues (each with its `.dlq`) were deleted with `rabbitmqctl delete_queue`: they no longer had a consumer.

## Check

```bash
emc exec rabbitmq rabbitmqctl list_queues name messages     # the .dlq queues must stay at 0
```

Spanish version: [../ES/anexo-c-eventos-y-colas.md](annex-c-events-and-queues.md)
