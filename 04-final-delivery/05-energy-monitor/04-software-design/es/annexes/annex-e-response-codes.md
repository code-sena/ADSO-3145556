# Anexo E. Códigos de respuesta y cómo diagnosticarlos

Versión 1.0 · 28 de octubre de 2026. Códigos HTTP que devuelve la API en situaciones que el equipo tuvo que diagnosticar, con su causa y la acción. Sirve de guía rápida durante la demostración y la operación.

| Código | Dónde | Cuándo ocurre | Qué hacer |
|---|---|---|---|
| 202 | `POST /api/v1/auth/register` | La solicitud de registro se guardó y se envió el código; responde `{email, expiresAt}`. La cuenta aún no existe | Verificar el correo con `POST /api/v1/auth/email/verify` dentro de las 24 horas |
| 202 | `POST /api/v1/auth/account/delete/code` | Se envió el código de borrado al correo de una cuenta de Google | Ingresar el código en `POST /api/v1/auth/account/delete` |
| 400 | Registro y cambio de correo | Dominio que parece un error de escritura de un proveedor conocido (`meil.com`) o que no existe ni recibe correo | Corregir la dirección; el mensaje indica el proveedor probable |
| 400 | Verificación y borrado con código | Código equivocado o vencido | Pedir uno nuevo (el reenvío siempre responde 202) |
| 401 | Cualquier ruta protegida | Sin sesión o token inválido; también la contraseña incorrecta al borrar la cuenta | Iniciar sesión; no es una falla del servicio |
| 404 | Gateway | Ruta que ningún servicio atiende | Revisar la ruta (Anexo A) |
| 405 | `https://energy-monitor.tech/internal/...` | Nginx rechaza `/internal/**`, que no debe alcanzarse desde Internet | Es lo esperado |
| 409 | Registro | El correo ya tiene una cuenta | Iniciar sesión o recuperar la contraseña |
| 409 | `POST /api/v1/auth/password/change` | Cuenta creada con Google: no tiene contraseña | Es lo esperado; usar Google |
| 409 | `POST /api/v1/auth/account/delete/code` | Cuenta con contraseña: confirma con ella, no con código | Usar la contraseña |
| 428 | Registro, primer inicio con Google e inicio de sesión | La cuenta no aceptó los términos (`acceptTerms` falta o es falso) | Aceptar los términos; si ocurre con la web de producción, está desplegada una versión anterior a los términos |
| 429 | Gateway | Más de 20 POST públicos de cuenta por minuto desde la misma IP y ruta | Esperar un minuto; las pruebas de punta a punta consecutivas pueden provocarlo |
| 429 | Códigos | Demasiados intentos de código (10 en recuperación, 5 en verificación y en borrado, por dirección y 15 minutos) | Esperar el tiempo que indica la respuesta |
| 503 | Gateway | El servicio de esa ruta no responde (arrancando o caído) | `emc ps` y `emc logs --tail 100 <servicio>` |
| 503 | `POST /api/v1/auth/account/delete/code` | `SECURITY_ACCOUNT_DELETION_CODE_MAIL_ENABLED` está apagado | Encenderlo cuando notificaciones esté desplegado (Anexo B) |
| 504 | Gateway | El servicio tardó más de 30 s | Revisar la carga y los logs del servicio |

Versión en inglés: [../EN/annex-e-response-codes.md](annex-e-response-codes.md)
