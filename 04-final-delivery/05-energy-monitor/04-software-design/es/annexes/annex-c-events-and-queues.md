# Anexo C. Eventos y colas de RabbitMQ

Versión 1.0 · 28 de octubre de 2026. Los servicios se avisan por eventos en RabbitMQ 4.1 con *transactional outbox*: la fila del mensaje se escribe en la misma transacción que el cambio, un relay la publica cada segundo con confirmación del broker, la entrega es al menos una vez, las colas son durables y cada una tiene su `.dlq` (*dead-letter* tras tres intentos). Un *outbox* se detiene ante un mensaje que ninguna cola recibe, por eso el servicio consumidor se despliega antes que el productor.

## Eventos, productores y colas

| Evento | Productor | Cola consumidora | Consumidor |
|---|---|---|---|
| `measurement.recorded` | mediciones | `alert-service.measurement.recorded` | alertas |
| `device.connectivity-lost`, `device.connectivity-restored` | dispositivos | `alert-service.device.events` | alertas |
| `device.linked`, `device.unlinked` | dispositivos | `alert-service.device.events` y `notification-service.notification.activity` | alertas y notificaciones; mediciones los usa para su caché |
| `alert.raised` | alertas | `notification-service.notification.alert-raised` | notificaciones (también la alerta de consumo inusual, clave `alert.anomaly.device`) |
| `recommendation.created` | recomendaciones | `notification-service.notification.recommendation-created` | notificaciones |
| `account.registered`, `account.administered` | seguridad | `notification-service.notification.account-events` | notificaciones |
| `account.deleted` | seguridad | `home-service.home.account-deleted` y `notification-service.notification.account-events` | hogares y notificaciones (limpieza) |
| `mail.email-verification`, `mail.password-reset`, `mail.account-deletion` | seguridad | `notification-service.notification.mail` | notificaciones (único que envía correo) |
| `home.created`, `home.member-joined`, `home.member-left`, `home.member-removed`, `home.thresholds-updated`, `home.ownership-transferred` | hogares | `notification-service.notification.activity` | notificaciones |
| `home.deleted` | hogares | `notification-service.notification.activity` y la cola del servicio de dispositivos | notificaciones y dispositivos (desvincula los módulos y envía `unlink`) |

## Interruptores de los consumidores y los relays

Todos valen `true` por defecto y existen solo para volver atrás durante una migración: `HOME_OUTBOX_RELAY_ENABLED`, `ACCOUNT_DELETED_CONSUMER_ENABLED`, `SECURITY_EVENTS_CONSUMER_ENABLED`, `ALERT_EVENTS_CONSUMER_ENABLED`, `ACTIVITY_CONSUMER_ENABLED`, `ALERT_CONSUMERS_ENABLED`, `ALERT_SERVICE_OUTBOX_RELAY_ENABLED`, `RECOMMENDATION_SCHEDULER_ENABLED` y `RECOMMENDATION_OUTBOX_RELAY_ENABLED` (Anexo B).

## Colas retiradas

Con el retiro del backend el 28 de octubre de 2026 se borraron las diez colas `energy-monitor-backend.*` (cada una con su `.dlq`) con `rabbitmqctl delete_queue`: ya no tenían consumidor.

## Comprobación

```bash
emc exec rabbitmq rabbitmqctl list_queues name messages     # las colas .dlq deben quedar en 0
```

Versión en inglés: [../EN/annex-c-events-and-queues.md](annex-c-events-and-queues.md)
