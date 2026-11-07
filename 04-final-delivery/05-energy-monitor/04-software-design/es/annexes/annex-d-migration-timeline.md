# Anexo D. Cronología de la migración a microservicios

Versión 1.0 · 28 de octubre de 2026. Resume cómo se pasó del backend monolítico a un API gateway y siete servicios, y qué ocurrió el día del despliegue completo. El procedimiento paso a paso está en el manual de implementación y despliegue (4.12 y 4.13) y los incidentes en su sección 7.

## Antes de octubre

El sistema nació como un monolito modular (`energy-monitor-backend`, Spring Modulith) con siete módulos: security, home, device, measurement, alert, recommendation y notification.

## 24 de octubre de 2026

Seguridad, dispositivos y mediciones salen del backend a servicios propios detrás de un API gateway, con REST interno bajo `/internal/**` y eventos en RabbitMQ con *transactional outbox*. El backend conserva home, alert, recommendation y notification, más tres paquetes intermediarios sin dominio ni tablas. Las rutas públicas de la API no cambian.

## 25 a 27 de octubre de 2026 (hasta el cierre del código)

Miguel Ángel García Artunduaga extrae home, notification, alert y recommendation, en ese orden, cada uno con su repositorio, su contenedor y su cola de eventos. Juan Esteban Cortés Parra incorpora en seguridad el registro con verificación previa del correo (la cuenta se crea al verificarlo y la solicitud caduca a las 24 horas), el rechazo de dominios con error de escritura, la aceptación obligatoria de los términos (respuesta 428 si falta), las reglas de las cuentas de Google (sin cambio de contraseña y borrado con código por correo) y la edición independiente del nombre y el apellido. El 27 de octubre se fusiona a `main` el retiro del backend (PR n.º 80 a `dev` y n.º 81 a `main`). Al cierre se ejecutaron las suites de los ocho servicios y las cinco pruebas de punta a punta sobre la pila local.

## 28 de octubre de 2026

| Hora aprox. | Hecho |
|---|---|
| Antes de las 03:40 | Respaldo de MySQL, clonado de los cuatro repositorios nuevos y revisión de `.env.prod` |
| 03:40 | Primer `deploy.sh infra`: repite `no such service: backend` porque el script en memoria era el anterior; se interrumpe con `Ctrl+C` |
| 03:54 | Segundo `deploy.sh infra`: `OK` con infra en `ceb1543`; se construyen home, notification, alert y recommendation y se retira el backend |
| Después | `deploy.sh` de `gateway`, `device`, `measurement`, `frontend` y `security`, cada uno en `OK` |
| Después | Borrado de las diez colas `energy-monitor-backend.*` y verificación: ocho servicios `healthy`, `GET /api/v1/homes` → 401 |
| 04:00 | `SECURITY_ACCOUNT_DELETION_CODE_MAIL_ENABLED=true` y tercer `deploy.sh infra` en `OK` |

Hallazgo posterior: el despliegue de la web reconstruyó la versión anterior (`e59bece`) porque su PR a `main` no estaba fusionado; con la seguridad nueva el registro respondía 428 hasta publicar la web nueva. La fusión dejó `main` en `698d4c9`.

## 28 de octubre de 2026, después de la migración

Con el sistema ya en producción se agregaron cinco cambios, cada uno en su rama y con su PR a `dev` y luego a `main`:

- Seguridad y hogares: la foto de perfil viaja en el perfil interno y los miembros de un hogar la muestran.
- Alertas y notificaciones: alerta de consumo inusual de un dispositivo y su texto en correo y push.
- Web: modales de ayuda en Ajustes (documentación por rol, soporte para usuarios y novedades armadas con el historial de git), visor de fotos dentro de la página, lista de hogares que se cierra al hacer clic fuera y explicación del push en Brave.

## Integración continua y secretos

El mismo día se revisó el CI de todos los repositorios: nueve ya tenían `ci-cd.yml` con despliegue, y al servicio de notificaciones, el único desplegable sin pipeline, se le escribió el suyo. Después se crearon el environment `production` y sus cuatro secretos en los repositorios que no los tenían; dos fallos del job de despliegue (`remote username contains invalid characters` y `hostname contains invalid characters`) se debieron a un salto de línea o un espacio al final de `DEPLOY_USER` y `DEPLOY_HOST`.

## Estado al cierre

- API gateway y siete servicios en producción, backend retirado y sus colas borradas.
- Confirmado por el equipo: la web nueva y todos los servicios en producción, y CI/CD en todos los repositorios salvo el móvil.
- Pendiente de evidencia: borrado de una cuenta de Google con el código por correo.
- Aplicación móvil: 78 pruebas unitarias en 8 suites aprobadas; pendiente reconstruir el APK, que no envía `acceptTerms`, y ejecutar su protocolo de pruebas manuales.

Versión en inglés: [../EN/annex-d-migration-timeline.md](annex-d-migration-timeline.md)
