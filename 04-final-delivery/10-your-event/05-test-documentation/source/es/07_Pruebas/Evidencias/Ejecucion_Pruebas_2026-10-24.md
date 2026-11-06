---
title: "Ejecución de pruebas del 2026-10-24"
lang: es
generated_on: 2026-10-24
status: borrador
reviewed_by: ""
---
# Ejecución de pruebas — Tu Evento

| Campo | Valor |
|---|---|
| Proyecto | Tu Evento — CapySoft |
| Repositorio verificado | lozano2303/TuEvento |
| Rama / commit verificado | `develop` / `2d259e7` |
| Fecha de ejecución | 2026-10-24 |
| Casos ejecutados | [Casos_de_Prueba_TuEvento.md](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/07_Pruebas/Evidencias/Casos_de_Prueba_TuEvento.md) (151 casos) |
| Leyenda de estados | Aprobado · Fallido · Bloqueado (no se pudo ejecutar) |

## 1. Alcance

Este documento registra dos ejecuciones hechas el 2026-10-24 contra el commit `2d259e7` de la rama `develop`:

1. Las pruebas automáticas del backend (`./mvnw verify`), con su reporte de cobertura de JaCoCo.
2. Los 151 casos de [Casos_de_Prueba_TuEvento.md](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/07_Pruebas/Evidencias/Casos_de_Prueba_TuEvento.md), ejecutados contra el sistema levantado en local. Los casos de API se ejecutaron con scripts HTTP. Los de interfaz web se ejecutaron en Chrome sin interfaz gráfica (headless), con Puppeteer.

Los resultados de cada caso están en las columnas K–O de [Casos_de_Prueba_TuEvento.md](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/07_Pruebas/Evidencias/Casos_de_Prueba_TuEvento.md). Aquí se describen el entorno, el resumen, los defectos y las limitaciones.

No es una prueba del despliegue en la nube: todo se ejecutó en un entorno local aislado, con datos de prueba creados para la ocasión.

## 2. Entorno de ejecución

| Componente | Versión o configuración |
|---|---|
| Backend | `tu-evento-0.0.1-SNAPSHOT.jar` compilado desde `TuEventoBackend/tu-evento` (`2d259e7`), perfil `dev`, puerto 18080 |
| Java / Maven | OpenJDK 21.0.11 / Apache Maven 3.9.14 (wrapper del proyecto) |
| Base de datos | PostgreSQL 17.10 (`postgres:17-alpine`), esquema creado por los changesets de Liquibase al arrancar |
| Caché | Redis 7.4.10 (`redis:7-alpine`) |
| Almacenamiento | MinIO `RELEASE.2025-09-07T16-13-09Z`, bucket `tuevento` |
| Pasarela de pago | `fake-payment-gateway` 1.0.0 del mismo repositorio, con su propia base PostgreSQL; webhooks firmados con HMAC-SHA256 |
| Correo | Mailpit v1.27 como servidor SMTP local: los correos se capturan, no se envían a buzones reales |
| Frontend web | `TuEventoFrontend/web` con el servidor de desarrollo de Vite 5.4.21, apuntando al backend de pruebas |
| Navegador | Google Chrome 151 en modo headless, controlado con `puppeteer-core` 24.10.0 |

**Aislamiento.** Todos los servicios se levantaron en puertos distintos a los del `docker-compose.yml` del proyecto, con credenciales generadas solo para esta ejecución. No se usaron el archivo `.env` del equipo, ni sus cuentas de correo, ni las claves de Google, Sightengine o la base de datos de desarrollo. El repositorio de código no se modificó: el backend, la pasarela y la web se copiaron a una carpeta temporal antes de compilarlos.

**Servicios no levantados.** No se levantaron el clasificador NSFW (`nsfw-service`), Sightengine, LibreTranslate, el inicio de sesión real con Google ni la app móvil. Los casos que dependen de ellos quedaron como Bloqueado (sección 4).

**Técnicas usadas.** Para comprobar vencimientos sin esperar días (códigos de activación, refresh tokens, reservas de sillas), se modificó la fecha de vencimiento directamente en la base de datos de pruebas. Los códigos que llegan por correo se leyeron de Mailpit o de la base de datos de pruebas. Cada caso donde se hizo esto lo indica en su columna Observaciones.

## 3. Pruebas automáticas del backend

Comando: `./mvnw -B verify` sobre una copia de `TuEventoBackend/tu-evento`.

| Resultado | Valor |
|---|---|
| Clases de prueba | 27 |
| Pruebas | 222 |
| Fallos y errores | 0 |
| Omitidas | 0 |
| Tiempo total de las pruebas | 43,4 s |
| Resultado de Maven | `BUILD SUCCESS` (código de salida 0) |

**Cobertura (JaCoCo, `target/site/jacoco/jacoco.csv`):**

| Métrica | Cubierto | Total | Cobertura |
|---|---|---|---|
| Líneas | 1768 | 11755 | 15,0 % |
| Ramas | 381 | 2898 | 13,1 % |
| Instrucciones | 7650 | 48759 | 15,7 % |
| Métodos | 383 | 2070 | 18,5 % |

**Pruebas por clase:**

| Clase de prueba | Pruebas | Fallos | Tiempo (s) |
|---|---|---|---|
| `TuEventoApplicationTests` | 1 | 0 | 36.0 |
| `modules.event.application.usecase.AddEventRatingServiceTest` | 28 | 0 | 0.2 |
| `modules.event.application.usecase.AdminChangeEventStatusUseCaseTest` | 12 | 0 | 0.5 |
| `modules.event.application.usecase.ChangeEventStatusServiceTest` | 14 | 0 | 0.1 |
| `modules.event.application.usecase.DeleteEventRatingServiceTest` | 7 | 0 | 0.0 |
| `modules.event.application.usecase.EditEventRatingServiceTest` | 10 | 0 | 0.4 |
| `modules.event.application.validator.EventDateValidatorTest` | 14 | 0 | 0.0 |
| `modules.event.infrastructure.EventRatingTranslatableContentHandlerTest` | 2 | 0 | 0.4 |
| `modules.event.infrastructure.websocket.EventCommentWebSocketListenerTest` | 5 | 0 | 0.4 |
| `modules.event.infrastructure.websocket.EventRatingDeletedWebSocketListenerTest` | 2 | 0 | 0.0 |
| `modules.language.application.service.TextMaskingServiceTest` | 17 | 0 | 0.0 |
| `modules.language.application.service.TranslationProcessorTest` | 4 | 0 | 0.5 |
| `modules.language.application.service.TranslationServiceTest` | 5 | 0 | 0.1 |
| `modules.language.domain.model.TranslationJobTest` | 7 | 0 | 0.0 |
| `modules.notification.application.NotificationMessageFactoryTest` | 21 | 0 | 0.2 |
| `modules.notification.application.SendNotificationUseCaseIdempotencyTest` | 2 | 0 | 0.6 |
| `modules.notification.infrastructure.messaging.EventStatusChangedListenerTest` | 4 | 0 | 0.0 |
| `modules.notification.infrastructure.messaging.OrganizerRoleRevokedListenerTest` | 4 | 0 | 0.1 |
| `modules.profile.infrastructure.ProfileTranslatableContentHandlerTest` | 1 | 0 | 0.3 |
| `modules.security.application.ActivateAccountUseCaseTest` | 3 | 0 | 0.4 |
| `modules.security.application.usecase.ApproveOrganizerRequestUseCaseTest` | 9 | 0 | 0.3 |
| `modules.security.application.usecase.GetOrganizersUseCaseTest` | 8 | 0 | 0.4 |
| `modules.security.application.usecase.OauthLoginUseCaseOnboardingTest` | 6 | 0 | 0.6 |
| `modules.security.application.usecase.RevokeOrganizerRoleUseCaseTest` | 6 | 0 | 0.0 |
| `modules.security.infrastructure.JavaMailEmailNotificationTest` | 4 | 0 | 0.3 |
| `modules.security.interfaces.rest.AdminControllerTest` | 7 | 0 | 1.3 |
| `shared.domain.valueobject.ValidationUtilsTest` | 19 | 0 | 0.3 |

Los nombres de clase omiten el prefijo `com.capysoft.tuevento.`. Las pruebas cubren sobre todo eventos, calificaciones, traducción, notificaciones y seguridad. No hay pruebas automáticas para pagos, tickets, sillas, wallet ni almacenamiento, que concentran la mayoría de los defectos de la sección 5.

## 4. Resumen de los casos de prueba

| Módulo | Total | Aprobados | Fallidos | Bloqueados | % Aprobado |
|---|---|---|---|---|---|
| Autenticación | 32 | 25 | 5 | 2 | 78,1 % |
| Usuarios | 9 | 9 | 0 | 0 | 100,0 % |
| Administración | 6 | 5 | 1 | 0 | 83,3 % |
| Perfil | 5 | 5 | 0 | 0 | 100,0 % |
| Eventos | 21 | 19 | 2 | 0 | 90,5 % |
| Media y layout | 7 | 5 | 1 | 1 | 71,4 % |
| Ratings y comentarios | 6 | 5 | 1 | 0 | 83,3 % |
| Categorías y sitios | 7 | 7 | 0 | 0 | 100,0 % |
| Secciones y sillas | 9 | 8 | 1 | 0 | 88,9 % |
| Órdenes y tickets | 12 | 9 | 3 | 0 | 75,0 % |
| Pagos | 16 | 15 | 1 | 0 | 93,8 % |
| Wallet | 5 | 5 | 0 | 0 | 100,0 % |
| Notificaciones | 4 | 4 | 0 | 0 | 100,0 % |
| Temas e idiomas | 6 | 5 | 0 | 1 | 83,3 % |
| Seguridad y almacenamiento | 6 | 2 | 3 | 1 | 33,3 % |
| **Total** | **151** | **128** | **18** | **5** | **84,8 %** |

**Casos bloqueados (5):**

| Caso | Motivo |
|---|---|
| CP-AUT-27 | Requiere una cuenta de Google real y credenciales OAuth válidas. |
| CP-AUT-30 | Depende del inicio de sesión real con Google. |
| CP-MED-02 | No se configuraron el servicio NSFW ni Sightengine, y no se usaron imágenes inapropiadas. Según `NsfwClientAdapter` y `SightengineAdapter`, sin configuración la moderación deja pasar cualquier imagen. |
| CP-TEM-05 | LibreTranslate no estaba levantado: el backend aceptó la solicitud, pero la traducción no puede completarse. |
| CP-SEG-06 | Requiere ejecutar la app móvil (Expo) en un dispositivo o emulador. |

**Casos ejecutados por API en lugar de la pantalla.** CP-USR-05, CP-MED-07 y CP-EVT-13 describen pasos en la web, pero se ejecutaron llamando a la misma API que usa la pantalla. Los casos web que sí se recorrieron en el navegador son CP-AUT-21, CP-AUT-22, CP-PAG-16 y CP-SEG-05.

## 5. Defectos encontrados

La severidad sigue la tabla de la sección 11 del [plan de pruebas](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/07_Pruebas/Plan_Pruebas.md).

| # | Caso | Severidad | Defecto | Evidencia en el código |
|---|---|---|---|---|
| D-01 | CP-SIL-05 | Crítica | Dos usuarios que reservan la misma silla al mismo tiempo reciben los dos «Silla reservada». En la BD queda solo el último como `reserved_by`. Se reprodujo en 5 de 5 intentos. | `modules/seat/application/usecase/SeatReservationService.java` (`reserveSeat` lee el estado y luego guarda, sin bloqueo de fila ni control de versión) |
| D-02 | CP-ORD-07 | Alta | Cualquier usuario autenticado puede cancelar la orden de otro. Hueco conocido, confirmado. | `modules/ticket/interfaces/rest/OrderController.java` (`/{orderId}/cancel`) |
| D-03 | CP-ORD-08 | Alta | `GET /tickets/user/{userId}` devuelve los tickets de cualquier usuario. Hueco conocido, confirmado. | `modules/ticket/interfaces/rest/TicketController.java` |
| D-04 | CP-ORD-12 | Alta | Cualquier USER puede hacer check-in de un ticket ajeno, que queda como USED. Hueco conocido, confirmado. | `TicketController.java`, `/{ticketId}/checkin` sin `@PreAuthorize` |
| D-05 | CP-SEG-02 | Alta | `POST /storage/upload` no exige token. Sin usuario falla al guardar, pero la respuesta 400 incluye la sentencia SQL interna. | `shared/infrastructure/security/SecurityConfig.java`, `modules/storage/interfaces/rest/StorageController.java` |
| D-06 | CP-SEG-03 | Alta | `GET /auth/dev/activate-code/{email}` devuelve el código de activación de cualquier cuenta sin autenticación. No está restringido a un perfil. | `modules/security/interfaces/rest/AuthController.java:290` |
| D-07 | CP-AUT-23 | Media | `POST /auth/recover-password` responde 404 «This email is not registered in the system» para correos no registrados, lo que permite enumerar usuarios. | Caso de uso de recuperación de contraseña |
| D-08 | CP-AUT-16 | Media | Al vencer el bloqueo por intentos fallidos, el primer login correcto responde `ACCOUNT_BLOCKED` aunque la BD ya quedó en ACTIVE. El segundo intento funciona. | `modules/security/application/usecase/LoginUseCase.java` (`checkLockout` y la relectura del usuario) |
| D-09 | CP-AUT-22 | Media | Con el token de acceso vencido, la web lanza dos `POST /auth/refresh` en paralelo. El segundo falla con el refresh token ya rotado y la sesión se cierra. | `services/httpClient.js`, `hooks/useTokenRefresh.js` y `context/NotificationContext.jsx` (tres implementaciones de refresh) |
| D-10 | CP-MED-03 | Media | Un archivo de 11 MB devuelve 500 «An unexpected error occurred»: `MaxUploadSizeExceededException` no tiene manejador. | `shared/infrastructure/advice/GlobalExceptionHandler.java` |
| D-11 | CP-PAG-13 | Media | Un motivo de reembolso de exactamente 500 caracteres pasa la validación, pero el INSERT en `payment_log.reason` (varchar 500) falla y responde 500. | Caso de uso de reembolso y columna `payment_log.reason` |
| D-12 | CP-SEG-04 | Media | Swagger UI (`/swagger-ui`) y la especificación OpenAPI (`/api-docs`) son públicos. En `application-prod.yml` también están habilitados («Cambiar a false cuando el proyecto pase a producción real»). Actuator solo expone `health`. | `application-dev.yml`, `application-prod.yml` |
| D-13 | CP-AUT-14, CP-AUT-19, CP-ADM-04 | Baja | Las respuestas 404 no traen el campo `code` que esperan los casos (`USER_NOT_FOUND`, `PETITION_NOT_FOUND`), y el refresh tras el logout responde 404 en lugar de `REFRESH_TOKEN_REVOKED`. El comportamiento es seguro; solo cambia el formato. | `GlobalExceptionHandler.java` (manejo de `NotFoundException`) |
| D-14 | CP-EVT-05, CP-RAT-05 | Baja | Las validaciones de Bean Validation (`@NotNull`, `@NotBlank`, `@Size`) responden antes que las reglas de negocio, así que no aparecen los códigos `EVENT_DATES_REQUIRED`, `COMMENT_BLANK` y `COMMENT_TOO_LONG`. Los límites sí se respetan. | DTO `CreateEventRequest`, `AddEventRatingRequest` |

**Caso con resultado esperado desactualizado.** CP-EVT-17 esperaba que los tickets no se revirtieran al cancelar un evento publicado. En el código actual, `EventCancelledListener` (módulo `payment`) reembolsa automáticamente todos los pagos APPROVED del evento. El ticket pagado pasó a REFUNDED y el saldo volvió a la wallet. Se registró como Fallido porque no coincide con lo esperado, pero lo que hay que corregir es el caso, no el sistema.

**Otros hallazgos fuera de los casos:**

| # | Severidad | Hallazgo |
|---|---|---|
| H-01 | Media | El backend acepta cualquier carácter especial en la contraseña (`ValidationUtils.PASSWORD_PATTERN`: `[^a-zA-Z0-9]`), pero los formularios web solo aceptan `@$!%*?&`. Una cuenta creada desde la API o la app móvil con, por ejemplo, `#` en la contraseña no puede iniciar sesión desde la web. |
| H-02 | Media | La API permite reservar sillas de eventos que todavía están en DRAFT (se comprobó en CP-MED-05). |
| H-03 | Baja | Si falta un parámetro obligatorio de la petición (`MissingServletRequestParameterException`), el backend responde 500 en lugar de 400. |
| H-04 | Baja | En la billetera web, un crédito hecho por el administrador con el motivo «Bono de bienvenida» aparece como «Crédito por cancelación», con «ID: N/A». |
| H-05 | Baja | En la pantalla de pago pendiente, el campo «Evento» aparece vacío. Al aprobarse el pago, la confirmación se muestra en la misma ruta `/checkout/pending/:id`, sin pasar por `/checkout/confirmed/:id`. |
| H-06 | Baja | La web cambia el idioma de la interfaz al idioma preferido del perfil unos segundos después de cargar, aunque el usuario haya elegido otro en el selector. |

## 6. Evidencias

Evidencias de los casos de interfaz web (Chrome headless, 1440 × 900):

| Caso | Evidencia |
|---|---|
| CP-AUT-21 | Pestaña B redirigida a `/login` después del logout en la pestaña A: [`aut21_pestana_b.png`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/07_Pruebas/Ejecucion_2026-10-24/aut21_pestana_b.png) |
| CP-AUT-22 | Sesión cerrada tras el refresh duplicado: [`aut22_wallet.png`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/07_Pruebas/Ejecucion_2026-10-24/aut22_wallet.png) |
| CP-PAG-16 | Pago pendiente y pago aprobado: [`05_pago_pendiente.png`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/09_Manuales/Manual_de_usuario/Manual_web/capturas_2026-10-24/es/05_pago_pendiente.png) y [`06_pago_confirmado.png`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/09_Manuales/Manual_de_usuario/Manual_web/capturas_2026-10-24/es/06_pago_confirmado.png) |

El recorrido completo de compra en la web, con capturas en español y en inglés, está en el [manual de usuario](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/12_Manual_Usuario.md#compra-de-entradas).

Los registros de ejecución (salida de Maven, logs del backend y de la pasarela, y respuestas HTTP) quedaron en la carpeta temporal de la ejecución y no se versionan.

## 7. Contradicciones detectadas

| # | Afirmación | Fuente A | Fuente B | Qué es correcto según la evidencia | Acción sugerida |
|---|---|---|---|---|---|
| 1 | Al cancelar un evento, los tickets no se revierten solos | Caso CP-EVT-17 en [Casos_de_Prueba_TuEvento.md](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/07_Pruebas/Evidencias/Casos_de_Prueba_TuEvento.md) | `modules/payment/infrastructure/messaging/EventCancelledListener.java` | El sistema reembolsa automáticamente los pagos aprobados | Actualizar el resultado esperado de CP-EVT-17 |
| 2 | `validatedBy` del check-in es el nombre del validador | Datos de entrada de CP-ORD-09 | `CheckinTicketRequest.validatedBy` (`Long`) | Es el ID numérico del usuario validador | Corregir los datos de entrada del caso |
| 3 | Para crear un perfil se usa `POST /profiles` | CP-PRF-01 | Ejecución: el registro ya crea el perfil | El perfil se crea al registrarse; `POST /profiles` responde 409 | Ajustar la precondición de CP-PRF-01 |
| 4 | El código de activación del caso es «000000» | CP-AUT-08 | `ActivateAccountRequest` (8 caracteres) | El código tiene 8 caracteres | Usar un código de 8 caracteres en el caso |
| 5 | Caracteres especiales admitidos en la contraseña | `ValidationUtils.java` (cualquiera) | Formularios web (`@$!%*?&`) | Las dos reglas difieren (H-01) | Unificar la regla en backend, web y móvil |

## 8. Pendiente de verificar en el repositorio de código

| Punto | Cómo resolverlo | A quién preguntar |
|---|---|---|
| CP-AUT-27 y CP-AUT-30 (Google OAuth) | Repetirlos con credenciales reales de Google en un entorno con el dominio autorizado | Equipo (dueño de la consola de Google Cloud) |
| CP-MED-02 (moderación de imágenes) | Levantar `nsfw-service` y configurar Sightengine con claves de prueba | Equipo |
| CP-TEM-05 (traducción automática) | Levantar el servicio `libretranslate` del `docker-compose.yml` y repetir el caso | Equipo |
| CP-SEG-06 (flujo móvil) | Ejecutar la app de `TuEventoFrontend/movil` con Expo en un emulador | Equipo |
| Comportamiento en el despliegue real | Repetir los casos de seguridad (CP-SEG-02 a CP-SEG-04) contra la URL desplegada con el perfil `prod` | Equipo (URL de despliegue) |
| Resultado de CP-AUT-22 con el build de producción | Repetir con `npm run build` + `vite preview`, sin el modo desarrollo de React | Equipo |
