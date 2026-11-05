# Especificación de requisitos de software de EnergyMonitor

Juan Esteban Cortés Parra y Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Tecnólogo en Análisis y Desarrollo de Software (ADSO), ficha 3145556

Versión 2.3, 28 de octubre de 2026

16 de octubre de 2026 (actualizado el 28 de octubre de 2026)

## Control de versiones

- Versión 2.0 (10/10/2026): requisitos funcionales por módulo, requisitos no funcionales, 15 historias de usuario y 2 casos de uso.
- Versión 2.1 (20/10/2026): Módulo de Administración de la Plataforma (RF-ADM-01 a RF-ADM-10), requisito de seguridad de las funciones de administración (RNF-SEG-11), integración y despliegue continuos (RNF-MAN-05), notificaciones push en la aplicación móvil, historia de usuario HU-16 y renumeración de las tablas 12 a 23.
- Versión 2.2 (28/10/2026): registro con verificación previa del correo (la cuenta no existe hasta verificarlo) y rechazo de dominios con error de escritura (RF-SEC-01, RF-SEC-02), aceptación obligatoria de los términos (RF-SEC-13), reglas de las cuentas creadas con Google (RF-SEC-05, RF-SEC-07, RF-SEC-11), edición independiente del nombre y el apellido (RF-SEC-08), límite de intentos del código de eliminación (RNF-SEG-06) y criterios de aceptación CA-39 a CA-42.
- Versión 2.3 (28/10/2026): alerta de consumo inusual de un dispositivo (RF-ALE-07), fotos de los miembros de un hogar (RF-HOM-12), ayuda en Ajustes con documentación por rol, soporte solo para usuarios y novedades automáticas (RF-CLI-07), foto de perfil ampliable en la página (RF-CLI-08), historia de usuario HU-17 y criterios de aceptación CA-43 a CA-47.

## Resumen

Este documento especifica lo que debe hacer EnergyMonitor, un sistema que permite a los hogares conocer el consumo eléctrico de cada electrodoméstico, recibir alertas y obtener recomendaciones de ahorro. Se redacta desde la perspectiva del usuario y del negocio, sin definir cómo se construirá la solución. Presenta los requisitos funcionales, agrupados por módulo del sistema, los requisitos no funcionales (seguridad, rendimiento, disponibilidad, compatibilidad, mantenibilidad, internacionalización, escalabilidad, usabilidad e interoperabilidad) y 17 historias de usuario y 2 casos de uso con criterios de aceptación verificables. Los requisitos se identifican con códigos estables que son idénticos en las versiones en español e inglés.

*Palabras clave:* especificación de requisitos, consumo eléctrico, alertas, recomendaciones de ahorro, hogares

## Introducción

### Propósito

Este documento especifica los requisitos de EnergyMonitor: lo que el sistema debe hacer (requisitos funcionales), con qué calidad debe hacerlo (requisitos no funcionales) y cómo se verifica (historias de usuario y casos de uso con sus criterios de aceptación). La estructura sigue la práctica recomendada para la especificación de requisitos de software (Institute of Electrical and Electronics Engineers [IEEE], 1998). El documento describe necesidades del usuario y del negocio y no condiciona la tecnología con la que se construya el sistema.

### Alcance

EnergyMonitor mide el consumo eléctrico de cada electrodoméstico de un hogar con un dispositivo de medición que se conecta al aparato, guarda las lecturas y las presenta en una aplicación web y en una aplicación móvil para Android. Genera alertas, recomendaciones de ahorro y notificaciones. El alcance completo y lo excluido están en el acta de constitución del proyecto (sección Alcance general).

### Definiciones y siglas

La Tabla 1 define los términos y siglas empleados en el documento.

**Tabla 1**

*Definiciones y siglas*

| Término | Descripción |
|---|---|
| ADSO | Análisis y Desarrollo de Software |
| RF | Requisito funcional |
| RNF | Requisito no funcional |
| HU | Historia de usuario |
| CU | Caso de uso |
| CA | Criterio de aceptación |
| Hogar | Espacio (casa, apartamento u otro) que agrupa a sus miembros y a sus dispositivos de medición |
| Propietario | Persona que administra un hogar, sus dispositivos, sus miembros y sus límites de consumo |
| Miembro | Persona que pertenece a un hogar y consulta su consumo, sus alertas y sus recomendaciones |
| Dispositivo de medición | Dispositivo físico que se conecta a un electrodoméstico, mide su consumo eléctrico y envía las lecturas |
| Umbral | Límite de consumo diario o mensual que define el propietario del hogar |
| Alerta | Aviso que genera el sistema ante un consumo excesivo, un límite alcanzado o la desconexión de un dispositivo |
| Recomendación | Sugerencia de ahorro que el sistema elabora a partir del consumo medido del hogar |
| Rol global | Permiso sobre la plataforma completa, distinto de los roles de propietario y miembro de cada hogar. Hoy existe uno: administrador (ADMIN) |
| Aviso de administración | Notificación que el sistema muestra dentro de la aplicación web a los administradores; no se envía por correo ni por push |

### Convención de códigos

Cada requisito tiene un código estable que es idéntico en las versiones en español e inglés. La Tabla 2 explica los prefijos.

**Tabla 2**

*Prefijos de los códigos*

| Prefijo | Significado |
|---|---|
| `RF-SEC` | Requisitos funcionales del Módulo de Gestión de Usuarios y Perfiles |
| `RF-HOM` | Requisitos funcionales del Módulo de Gestión de Hogares |
| `RF-DEV` | Requisitos funcionales del Módulo de Gestión de Dispositivos |
| `RF-MED` | Requisitos funcionales del Módulo de Mediciones y Consumo |
| `RF-ALE` | Requisitos funcionales del Módulo de Alertas |
| `RF-REC` | Requisitos funcionales del Módulo de Recomendaciones de Ahorro |
| `RF-NOT` | Requisitos funcionales del Módulo de Notificaciones |
| `RF-CLI` | Requisitos funcionales de las aplicaciones web y móvil |
| `RF-ADM` | Requisitos funcionales del Módulo de Administración de la Plataforma |
| `RNF-SEG`, `RNF-REN`, `RNF-DIS`, `RNF-COM`, `RNF-MAN`, `RNF-I18`, `RNF-ESC`, `RNF-USA`, `RNF-INT` | Requisitos no funcionales: seguridad, rendimiento, disponibilidad, compatibilidad, mantenibilidad, internacionalización, escalabilidad, usabilidad e interoperabilidad |
| `HU` | Historias de usuario |
| `CU` | Casos de uso |
| `CA` | Criterios de aceptación |



### Tipos de usuario

La Tabla 3 describe los tipos de usuario del sistema. Una persona puede pertenecer a varios hogares con roles distintos.

**Tabla 3**

*Tipos de usuario*

| Tipo | Descripción |
|---|---|
| Administrador del sistema | Cuenta con el rol global de administrador. Consulta las cuentas de la plataforma, cambia su estado, concede o revoca el rol de administrador y recibe avisos de administración desde una pantalla propia de la aplicación web. El rol no le da acceso a ningún hogar |
| Propietario del hogar | Gestiona el hogar, sus dispositivos, sus miembros y sus límites de consumo |
| Miembro del hogar | Consulta el consumo, las alertas y las recomendaciones del hogar |
| Dispositivo de medición | Actor técnico que envía las lecturas de consumo y su propio estado |

### Supuestos y dependencias

1. Los dispositivos de medición tienen Wi-Fi continuo en el lugar de instalación.
2. El inicio de sesión con Google y el envío de correos dependen de cuentas de esos servicios configuradas para el sistema.
3. Las notificaciones del navegador dependen de que el sistema tenga habilitado ese servicio.

## Requisitos funcionales

### Módulo de Gestión de Usuarios y Perfiles

La Tabla 4 enumera los requisitos funcionales del Módulo de Gestión de Usuarios y Perfiles.

**Tabla 4**

*Requisitos funcionales del Módulo de Gestión de Usuarios y Perfiles*

| Código | Requisito |
|---|---|
| RF-SEC-01 | El sistema debe permitir solicitar el registro de una cuenta con datos personales, correo y contraseña. La cuenta no existe hasta que el usuario verifica su correo: la solicitud queda pendiente 24 horas y, si no se verifica, se descarta. Debe rechazar el registro si el correo ya tiene una cuenta, si el dominio no existe o no recibe correo, o si el dominio parece un error de escritura de un proveedor conocido (por ejemplo `meil.com`), e informar al usuario. |
| RF-SEC-02 | El sistema debe enviar al correo indicado un código de verificación de seis dígitos y crear la cuenta, con el correo ya verificado, cuando el usuario lo ingrese. El usuario debe poder pedir que se le reenvíe el código, y registrarse de nuevo con el mismo correo reemplaza la solicitud pendiente. Sin verificar no se puede iniciar sesión. El código dura entre 15 y 30 minutos y cambiar el correo de una cuenta lo invalida. |
| RF-SEC-03 | El sistema debe permitir iniciar sesión de forma segura con correo y contraseña. |
| RF-SEC-04 | El sistema debe mantener la sesión del usuario sin pedirle sus credenciales continuamente, permitirle cerrar la sesión y cerrarla si detecta que se intenta reutilizar una credencial de sesión ya usada. |
| RF-SEC-05 | El sistema debe permitir registrarse e iniciar sesión con una cuenta de Google. La primera vez crea la cuenta con el nombre, el apellido (si existe), la foto y el correo de Google, el correo queda verificado y se exige aceptar los términos. Si el correo pertenece a una cuenta registrada con contraseña, debe rechazar el acceso e informar el conflicto. Si el servicio de Google no está disponible, debe avisar al usuario. |
| RF-SEC-06 | El sistema debe permitir recuperar la contraseña con un código de seis dígitos enviado al correo, válido 15 minutos y de un solo uso. Debe responder igual exista o no la cuenta, y pedir un código nuevo anula los anteriores. |
| RF-SEC-07 | El sistema debe permitir al usuario con sesión iniciada cambiar su contraseña. Una cuenta creada con Google no tiene contraseña propia y no puede cambiarla: el sistema debe rechazar la solicitud con un mensaje legible. |
| RF-SEC-08 | El sistema debe permitir consultar los datos de la cuenta y actualizar el perfil, editando el nombre y el apellido por separado, y la foto. |
| RF-SEC-09 | El sistema debe permitir conocer los permisos del usuario. Los permisos generales se evalúan en cada consulta y no quedan fijados en la sesión. |
| RF-SEC-10 | El sistema debe identificar al usuario únicamente a partir de su sesión y no debe aceptar identidades indicadas por la aplicación que realiza la solicitud. |
| RF-SEC-11 | El sistema debe permitir al usuario eliminar su cuenta confirmando su contraseña. Con una contraseña incorrecta debe rechazar la solicitud. Una cuenta creada con Google no tiene contraseña: la confirma con un código de seis dígitos que el sistema envía a su correo, y el código incorrecto o vencido rechaza la solicitud. La eliminación es lógica y se realiza completa o no se realiza: cierra las sesiones, marca como eliminados los datos personales, libera el correo y la identidad de Google para un registro nuevo y gestiona las membresías en los hogares. Un hogar del que la cuenta es el único miembro se elimina. Si la cuenta es el único propietario de un hogar con otros miembros, el sistema rechaza la eliminación con un mensaje legible hasta que se transfiera la propiedad o se retire a los miembros. El acceso ya iniciado antes de la eliminación puede seguir vigente 15 minutos como máximo y no puede renovarse. |
| RF-SEC-12 | El sistema debe aplicar una política de contraseñas (longitud mínima de 8 y máxima de 64 caracteres) y registrar los intentos fallidos de acceso y las acciones relevantes para la seguridad. Los intentos fallidos se cuentan y se registran, y un administrador puede bloquear una cuenta, pero no hay bloqueo automático tras intentos fallidos y la expiración de la contraseña a los 90 días no se aplica en esta versión. |
| RF-SEC-13 | El sistema no debe permitir el acceso a una cuenta que no haya aceptado los términos y condiciones, sea cual sea el método: el registro con contraseña, el primer inicio con Google y el inicio de sesión de una cuenta sin aceptación exigen la aceptación expresa del usuario, y sin ella el sistema responde pidiéndola y no crea ni modifica nada. El momento de la aceptación queda registrado. |

### Módulo de Gestión de Hogares

La Tabla 5 enumera los requisitos funcionales del Módulo de Gestión de Hogares.

**Tabla 5**

*Requisitos funcionales del Módulo de Gestión de Hogares*

| Código | Requisito |
|---|---|
| RF-HOM-01 | El sistema debe permitir crear un hogar eligiendo un tipo de hogar de un catálogo. Quien lo crea queda como propietario y el hogar recibe un código de acceso. |
| RF-HOM-02 | El sistema debe mostrar al usuario los hogares a los que pertenece. |
| RF-HOM-03 | El sistema debe permitir unirse a un hogar con su código de acceso, con el rol de miembro. |
| RF-HOM-04 | El sistema debe mostrar los miembros de un hogar. |
| RF-HOM-05 | El sistema debe distinguir los permisos generales de los usuarios de los roles de propietario y miembro dentro de cada hogar. Todo hogar tiene exactamente un propietario. Un usuario puede tener roles distintos en hogares distintos. |
| RF-HOM-06 | El propietario debe poder retirar a un miembro y cualquier miembro debe poder abandonar el hogar. |
| RF-HOM-07 | El usuario debe poder marcar un hogar como favorito. |
| RF-HOM-08 | El sistema debe permitir consultar, actualizar y restablecer a los valores del sistema los umbrales de un hogar. El propietario define el límite diario o el mensual. Los valores predeterminados son 6 kWh diarios y 180 kWh mensuales. |
| RF-HOM-09 | El propietario debe poder eliminar el hogar mediante eliminación lógica. Los dispositivos del hogar se desvinculan y quedan libres, se conserva el historial de mediciones, alertas y recomendaciones, y los miembros dejan de ver el hogar. Solo el propietario puede hacerlo. |
| RF-HOM-10 | El propietario debe poder transferir la propiedad del hogar a otro miembro. Los roles se intercambian en un solo paso y el hogar siempre tiene un único propietario. La transferencia no cambia los umbrales, los dispositivos ni las mediciones. |
| RF-HOM-11 | Pendiente: limitar el historial de un dispositivo al periodo de vinculación. |
| RF-HOM-12 | El sistema debe mostrar a los miembros de un hogar la foto de perfil de cada uno, cuando la tienen, y permitir ampliarla. |

### Módulo de Gestión de Dispositivos

La Tabla 6 enumera los requisitos funcionales del Módulo de Gestión de Dispositivos.

**Tabla 6**

*Requisitos funcionales del Módulo de Gestión de Dispositivos*

| Código | Requisito |
|---|---|
| RF-DEV-01 | El sistema debe ofrecer un catálogo de tipos de electrodoméstico. |
| RF-DEV-02 | El propietario debe poder vincular un dispositivo de medición a su hogar. El sistema registra el dispositivo, le entrega sus credenciales y la dirección del servicio al que debe enviar sus datos. Un dispositivo vinculado a otro hogar debe rechazarse. Vincular de nuevo el mismo dispositivo conserva su identidad y su historial y renueva sus credenciales. |
| RF-DEV-03 | Los miembros deben poder ver los dispositivos de un hogar. |
| RF-DEV-04 | El propietario debe poder editar el electrodoméstico, el nombre y la habitación de un dispositivo sin intervenir el aparato físico. |
| RF-DEV-05 | El propietario debe poder cambiar la red Wi-Fi de un dispositivo ya vinculado. El sistema renueva las credenciales e invalida las anteriores. No se considera una vinculación nueva y no genera el aviso de dispositivo vinculado. |
| RF-DEV-06 | El propietario debe poder desvincular un dispositivo. Al hacerlo, las credenciales dejan de ser válidas y el dispositivo recibe la orden de olvidarlas, conserva su red Wi-Fi y activa de inmediato el Bluetooth para vincularse de nuevo. Si estaba apagado, recibe la orden al reconectarse. Las credenciales anteriores pueden seguir siendo aceptadas hasta cinco minutos. Al vincularlo otra vez, la orden pendiente se borra. |
| RF-DEV-07 | El sistema solo debe aceptar datos de dispositivos vinculados, y cada dispositivo solo puede enviar sus propios datos. |
| RF-DEV-08 | La vinculación debe poder hacerse desde la aplicación web por Bluetooth: la aplicación muestra las redes Wi-Fi que ve el dispositivo, registra el dispositivo y le envía la configuración. Para navegadores sin Bluetooth, el dispositivo ofrece un portal de configuración propio, al que se accede conectándose a su red `EnergyMonitor-Setup`. |
| RF-DEV-09 | El dispositivo debe reconectarse sin intervención del usuario ante la pérdida de Wi-Fi o una falla temporal del sistema, conservar hasta 30 muestras mientras no tiene conexión y no enviar nunca lecturas en cero cuando el sensor no responde. |

### Módulo de Mediciones y Consumo

La Tabla 7 enumera los requisitos funcionales del Módulo de Mediciones y Consumo.

**Tabla 7**

*Requisitos funcionales del Módulo de Mediciones y Consumo*

| Código | Requisito |
|---|---|
| RF-MED-01 | El dispositivo debe enviar cada 60 segundos el voltaje (V), la corriente (A), la potencia activa (W) y la energía acumulada (kWh) del electrodoméstico. El sistema ignora cualquier dato adicional que no forme parte de esas magnitudes. |
| RF-MED-02 | El sistema debe guardar cada lectura con la fecha y hora en que se tomó. Las muestras acumuladas sin conexión llegan con su hora real. Si el dispositivo no tiene hora, el sistema usa la hora de recepción. |
| RF-MED-03 | El sistema debe descartar los mensajes que declaren pertenecer a un dispositivo distinto del que los envía. |
| RF-MED-04 | El dispositivo debe informar su estado cada cinco minutos y al conectarse: si está en línea o sin conexión, la intensidad de su señal, la versión de su software y el tiempo que lleva encendido. |
| RF-MED-05 | El sistema debe conservar los cambios de estado de cada dispositivo. Mientras siga en línea, cada informe solo actualiza el último estado registrado. |
| RF-MED-06 | El sistema debe marcar un dispositivo como sin conexión cuando el dispositivo avisa que se desconecta o cuando pasan 150 segundos sin recibir noticias de él. |
| RF-MED-07 | El sistema debe permitir consultar las mediciones, la última medición y estadísticas de consumo. |
| RF-MED-08 | El sistema debe ofrecer el resumen y el historial de consumo de un hogar, con selección de un rango de fechas. |
| RF-MED-09 | El sistema debe clasificar la potencia en los niveles LOW (0 a 500 W), MEDIUM (500 a 1500 W), HIGH (1500 a 3000 W) y CRITICAL (desde 3000 W), con rangos que incluyen el límite inferior y excluyen el superior, y permitir consultarlos. |

### Módulo de Alertas

La Tabla 8 enumera los requisitos funcionales del Módulo de Alertas.

**Tabla 8**

*Requisitos funcionales del Módulo de Alertas*

| Código | Requisito |
|---|---|
| RF-ALE-01 | El sistema debe generar un aviso de dispositivo vinculado con la primera lectura que envía un dispositivo tras ser vinculado, una vez por vinculación. El usuario lo cierra marcándolo como leído. |
| RF-ALE-02 | El sistema debe generar una alerta de desconexión cuando un dispositivo avisa que se desconecta o pasan 150 segundos sin datos. Se resuelve sola cuando el dispositivo vuelve a reportar. |
| RF-ALE-03 | El sistema debe generar una alerta de consumo alto o crítico cuando una lectura cae en el nivel HIGH o CRITICAL. Se resuelve sola con la primera lectura en nivel LOW o MEDIUM. |
| RF-ALE-04 | El sistema debe generar una alerta de límite diario o mensual cuando la energía del hogar desde el inicio del día o del mes alcanza el límite de sus umbrales. Día y mes se calculan en la zona horaria de Bogotá. Se resuelve sola al iniciar un nuevo periodo o al subir el límite por encima del consumo. |
| RF-ALE-05 | El sistema debe permitir consultar las alertas, ver el detalle de una, marcarla como leída y eliminarla. Solo se pueden eliminar las alertas resueltas o leídas. |
| RF-ALE-06 | El sistema debe generar las alertas de consumo a partir de cada nueva lectura y las de desconexión a partir de la pérdida de comunicación del dispositivo, y enviar cada alerta para su notificación. |
| RF-ALE-07 | El sistema debe generar una alerta de consumo inusual cuando una lectura de un dispositivo es de al menos 200 W y de al menos 2,5 veces su potencia habitual (el promedio horario de los 7 días anteriores, sin la última hora), siempre que haya al menos 24 horas de historial y un promedio de 5 W o más. Habrá una alerta pendiente por dispositivo y se resuelve sola cuando la lectura vuelve a lo normal. |

### Módulo de Recomendaciones de Ahorro

La Tabla 9 enumera los requisitos funcionales del Módulo de Recomendaciones de Ahorro.

**Tabla 9**

*Requisitos funcionales del Módulo de Recomendaciones de Ahorro*

| Código | Requisito |
|---|---|
| RF-REC-01 | El sistema debe evaluar cada hora la energía por hora de los hogares cuyos dispositivos reportaron en la última semana, y guardar una recomendación cuando se cumple una regla. La primera evaluación se hace 2 minutos después de iniciar el sistema. |
| RF-REC-02 | Regla de hora punta: al menos el 40 % de la energía de los últimos 7 días (mínimo 2 kWh) se consumió entre las 18:00 y las 22:00. |
| RF-REC-03 | Regla de consumo en reposo: un dispositivo que no sea un refrigerador consumió en promedio 15 W o más entre la 01:00 y las 05:00 en cada una de las últimas 3 noches. |
| RF-REC-04 | Regla de comparación semanal: los últimos 7 días superan en 25 % o más a los 7 anteriores (mínimo 1 kWh). |
| RF-REC-05 | Regla de proyección de límite: al ritmo actual, el hogar superará su límite diario o mensual, el que haya definido el propietario. |
| RF-REC-06 | Regla de aumento por dispositivo: la potencia media de un dispositivo en los últimos 7 días es 30 % o más superior a la de los 21 días anteriores. |
| RF-REC-07 | La misma recomendación (hogar, tipo y dispositivo) no debe repetirse dentro de 7 días, aunque se haya eliminado. Las horas se interpretan en la zona horaria de Bogotá. |
| RF-REC-08 | El sistema debe permitir consultar las recomendaciones de un hogar, marcarlas como leídas y eliminar las leídas, una por una o todas a la vez. Quien no es miembro del hogar no puede ver sus recomendaciones. |
| RF-REC-09 | Las reglas requieren historial: la hora punta y el consumo en reposo funcionan tras unos días de lecturas, la comparación semanal tras dos semanas y la comparación por dispositivo tras cuatro. |

### Módulo de Notificaciones

La Tabla 10 enumera los requisitos funcionales del Módulo de Notificaciones.

**Tabla 10**

*Requisitos funcionales del Módulo de Notificaciones*

| Código | Requisito |
|---|---|
| RF-NOT-01 | El sistema debe entregar cada alerta a todos los miembros del hogar por correo y por notificación push (en el navegador o en la aplicación móvil Android donde el usuario la haya activado), según las preferencias de cada usuario. Ambos canales están activos por defecto. |
| RF-NOT-02 | El sistema debe entregar cada recomendación nueva por los mismos canales. |
| RF-NOT-03 | El usuario debe poder consultar y modificar sus preferencias de notificación. |
| RF-NOT-04 | El usuario debe poder activar y desactivar las notificaciones push del navegador y de la aplicación móvil. Si el servicio no está habilitado en el sistema, el correo sigue funcionando. |
| RF-NOT-05 | Cada intento de entrega debe quedar registrado como enviado o fallido, sin guardar el contenido del mensaje. Los intentos que sigan sin cerrarse tras 30 minutos se marcan como fallidos. |
| RF-NOT-06 | El sistema debe enviar por correo el código de verificación y el de recuperación de contraseña. Un fallo del correo no debe cambiar lo que el usuario ve en pantalla. |
| RF-NOT-07 | Cada correo de alerta debe incluir un enlace a la aplicación web. |

### Aplicaciones web y móvil

La Tabla 11 recoge los requisitos funcionales de las aplicaciones web y móvil.

**Tabla 11**

*Requisitos funcionales de las aplicaciones web y móvil*

| Código | Requisito |
|---|---|
| RF-CLI-01 | La aplicación web debe ofrecer registro, inicio de sesión (incluido Google), recuperación de contraseña, cuenta, hogares, favoritos, dispositivos, umbrales, consumo, historial y notificaciones. A las cuentas administradoras les ofrece, en su lugar, la administración de la plataforma (RF-ADM-06). |
| RF-CLI-02 | La aplicación web debe mostrar una insignia con las alertas pendientes, un contador en el título de la pestaña y un aviso emergente cuando llega una alerta nueva, y actualizarse cada 30 segundos. |
| RF-CLI-03 | La aplicación web debe vincular dispositivos por Bluetooth y permitir notificaciones del navegador, que llegan aunque la pestaña esté cerrada. |
| RF-CLI-04 | La aplicación móvil debe estar disponible solo para Android y permitir el acceso a la cuenta, los hogares, las alertas, las recomendaciones y las preferencias de notificación, y recibir notificaciones push en el teléfono. La eliminación de hogares y la transferencia de propiedad aún no están disponibles en la aplicación móvil. |
| RF-CLI-05 | Ambas aplicaciones deben permitir elegir el idioma entre español, inglés, francés y portugués. |
| RF-CLI-06 | Las aplicaciones deben permitir elegir una paleta de colores con modo oscuro. |
| RF-CLI-07 | La aplicación web debe ofrecer en Ajustes la ayuda en ventanas modales: la documentación según el rol (usuario o administrador), el soporte técnico —solo para usuarios, que generan el ticket como un correo a un administrador— y las novedades, que se actualizan solas con cada versión.  |
| RF-CLI-08 | La aplicación web debe permitir ver la foto de perfil ampliada dentro de la propia página, sin abrir otra pestaña. |

### Módulo de Administración de la Plataforma

La Tabla 12 enumera los requisitos funcionales del Módulo de Administración de la Plataforma.

**Tabla 12**

*Requisitos funcionales del Módulo de Administración de la Plataforma*

| Código | Requisito |
|---|---|
| RF-ADM-01 | El sistema debe contar con un rol global de administrador con tres permisos: consultar las cuentas, cambiar el estado de las cuentas y gestionar los roles globales. Ninguna cuenta es administradora al instalar el sistema: el primer administrador se designa desde el servidor sobre una cuenta ya registrada, y los siguientes desde la aplicación web. |
| RF-ADM-02 | El administrador debe poder consultar el directorio de cuentas activas, de la más reciente a la más antigua, buscar por correo, nombre o apellido y recorrerlo por páginas de hasta 50 cuentas. De cada cuenta se muestran el correo, el nombre, el estado, si el correo está verificado, la fecha de registro, el último acceso y los roles globales. |
| RF-ADM-03 | El administrador debe poder cambiar el estado de otra cuenta a activa, inactiva o bloqueada. Una cuenta que deja de estar activa no puede iniciar sesión y sus sesiones abiertas se cierran. La aplicación web pide confirmación antes de aplicar el cambio. |
| RF-ADM-04 | El administrador debe poder conceder y revocar el rol de administrador a otra cuenta. Repetir una concesión o una revocación no cambia nada. La aplicación web pide confirmación antes de revocar el rol. |
| RF-ADM-05 | El sistema debe impedir que un administrador cambie el estado o los roles de su propia cuenta, y rechazar la solicitud con un mensaje legible. |
| RF-ADM-06 | El rol de administrador no debe dar acceso a ningún hogar. En la aplicación web, una cuenta administradora entra directamente a la administración y su menú solo muestra Administración, Notificaciones y Ajustes; las pantallas de hogares y favoritos no están disponibles para ella y Ajustes no muestra las preferencias de notificación de los hogares. |
| RF-ADM-07 | El sistema debe avisar dentro de la aplicación web a cada administrador activo cuando se registra una cuenta nueva (también con el primer acceso con Google) y cuando otro administrador cambia el estado o el rol de una cuenta. Quien hizo el cambio no recibe el aviso y una acción que no cambia nada no genera avisos. |
| RF-ADM-08 | El administrador debe poder consultar sus 50 avisos más recientes, ver cuáles no ha leído, marcarlos como leídos uno por uno o todos a la vez y eliminarlos. Solo puede ver y modificar sus propios avisos. La aplicación muestra el número de avisos sin leer. |
| RF-ADM-09 | Cada cambio de estado o de rol hecho por un administrador debe quedar en el registro de auditoría, con la dirección de origen. |
| RF-ADM-10 | Pendiente: gestión de solicitudes de soporte y edición de la documentación desde Ajustes, y entrega de los avisos de administración por correo o por notificación push. |

## Requisitos no funcionales

### Seguridad

La Tabla 13 establece los requisitos de seguridad.

**Tabla 13**

*Requisitos no funcionales de seguridad*

| Código | Requisito |
|---|---|
| RNF-SEG-01 | Cada acceso concedido a un usuario debe durar 15 minutos y renovarse automáticamente mientras la sesión siga vigente. Una sesión no debe superar los 7 días sin que el usuario vuelva a iniciar sesión. |
| RNF-SEG-02 | Si se intenta reutilizar una credencial de renovación ya usada, el sistema debe invalidar la sesión. |
| RNF-SEG-03 | El sistema solo debe atender las solicitudes de las aplicaciones web autorizadas. Las demás deben rechazarse. |
| RNF-SEG-04 | Los datos que envían los dispositivos de medición deben viajar cifrados. |
| RNF-SEG-05 | Los códigos de recuperación deben almacenarse de forma protegida, de modo que una filtración de los datos no permita usarlos. |
| RNF-SEG-06 | El canje de códigos de recuperación debe limitarse a 10 intentos por dirección y por ventana de 15 minutos, e indicar al usuario cuánto debe esperar. La verificación de correo y la confirmación de la eliminación de la cuenta con código deben limitarse además a 5 intentos por dirección y por ventana. |
| RNF-SEG-07 | Las funciones internas con las que se autentica a los dispositivos solo deben ser accesibles desde la red del propio sistema y nunca desde internet. |
| RNF-SEG-08 | El sistema debe proteger la información de los usuarios conforme a la Ley 1581 de 2012 (Congreso de la República de Colombia, 2012). |
| RNF-SEG-09 | Las credenciales de los dispositivos deben comprobarse de forma segura. Solo se conserva una huella protegida (hash) de cada clave. |
| RNF-SEG-10 | Las contraseñas y claves del sistema no deben quedar expuestas en los archivos compartidos del proyecto. |
| RNF-SEG-11 | Las funciones de administración deben exigir en cada solicitud el permiso global que corresponde a la acción. Sin sesión se rechazan como no autenticadas, sin el permiso como no autorizadas, y cualquier función de administración que no tenga un permiso asignado se rechaza siempre. |

### Rendimiento

La Tabla 14 establece los requisitos de rendimiento.

**Tabla 14**

*Requisitos no funcionales de rendimiento*

| Código | Requisito |
|---|---|
| RNF-REN-01 | Las consultas de datos y las gráficas deben responder en menos de 2 segundos. Este valor no se ha medido bajo carga. |
| RNF-REN-02 | El sistema debe procesar la información de cada dispositivo con una cadencia de una lectura cada 60 segundos. |
| RNF-REN-03 | El envío de correos no debe dejar esperando al usuario más de 5 segundos. |

### Disponibilidad y tolerancia a fallos

La Tabla 15 establece los requisitos de disponibilidad y tolerancia a fallos.

**Tabla 15**

*Requisitos no funcionales de disponibilidad y tolerancia a fallos*

| Código | Requisito |
|---|---|
| RNF-DIS-01 | No se fija un porcentaje de disponibilidad objetivo para esta versión. |
| RNF-DIS-02 | El sistema debe tolerar la pérdida ocasional de lecturas. Una lectura perdida más el retardo de la red no debe provocar el estado sin conexión: el umbral es de 150 segundos. |
| RNF-DIS-03 | Si el sistema no está disponible, el dispositivo debe seguir midiendo y conservar hasta 30 muestras (30 minutos). Si se llena, descarta la más antigua. |
| RNF-DIS-04 | El servicio que recibe los datos de los dispositivos debe reintentar conectarse con el sistema cada 10 segundos mientras este no esté disponible. |
| RNF-DIS-05 | Si no hay Wi-Fi durante 10 minutos, el dispositivo debe reiniciarse. Tras 5 minutos sin una red conocida, debe ofrecer la configuración por Bluetooth. |
| RNF-DIS-06 | El sistema debe permitir consultar su estado de salud. |

### Compatibilidad

La Tabla 16 establece los requisitos de compatibilidad.

**Tabla 16**

*Requisitos no funcionales de compatibilidad*

| Código | Requisito |
|---|---|
| RNF-COM-01 | La aplicación web debe funcionar en los navegadores actuales (Chrome, Firefox, Safari y Edge). |
| RNF-COM-02 | La vinculación por Bluetooth desde la web solo está disponible en Chrome y Edge de escritorio y en Chrome para Android, y solo en una conexión segura. En los demás navegadores se usa el portal de configuración del dispositivo. |
| RNF-COM-03 | La aplicación móvil debe ejecutarse en dispositivos Android. |
| RNF-COM-04 | El dispositivo de medición debe incluir un sensor de medición eléctrica dedicado, con un solo sensor por placa, y una placa con Wi-Fi y Bluetooth. |

### Mantenibilidad

La Tabla 17 establece los requisitos de mantenibilidad.

**Tabla 17**

*Requisitos no funcionales de mantenibilidad*

| Código | Requisito |
|---|---|
| RNF-MAN-01 | El sistema debe organizarse en módulos funcionales (Gestión de Usuarios y Perfiles, Gestión de Hogares, Gestión de Dispositivos, Mediciones y Consumo, Alertas, Recomendaciones de Ahorro, Notificaciones y Administración de la Plataforma), de modo que cada uno pueda modificarse sin afectar a los demás. |
| RNF-MAN-02 | Todo cambio en la estructura de los datos debe poder revertirse. |
| RNF-MAN-03 | Los datos eliminados deben conservarse marcados como eliminados y no borrarse físicamente. |
| RNF-MAN-04 | El sistema debe contar con pruebas automáticas que verifiquen sus funciones principales. |
| RNF-MAN-05 | Todo cambio debe pasar las pruebas automáticas antes de integrarse, y su publicación en producción debe requerir una aprobación, comprobar el estado de salud del sistema y volver a la versión anterior si la comprobación falla. |

### Internacionalización

La Tabla 18 establece los requisitos de internacionalización.

**Tabla 18**

*Requisitos no funcionales de internacionalización*

| Código | Requisito |
|---|---|
| RNF-I18-01 | La aplicación web debe estar disponible en español, inglés, francés y portugués. |
| RNF-I18-02 | La aplicación móvil debe estar disponible en español, inglés, francés y portugués. |
| RNF-I18-03 | Las alertas y las recomendaciones deben mostrarse en el idioma que haya elegido cada usuario. |

### Escalabilidad

La Tabla 19 establece los requisitos de escalabilidad.

**Tabla 19**

*Requisitos no funcionales de escalabilidad*

| Código | Requisito |
|---|---|
| RNF-ESC-01 | El sistema debe poder crecer en número de usuarios y de dispositivos sin afectar la experiencia del usuario. |
| RNF-ESC-02 | No se fijan cantidades objetivo de usuarios y dispositivos concurrentes para esta versión. |

### Usabilidad

La Tabla 20 establece los requisitos de usabilidad.

**Tabla 20**

*Requisitos no funcionales de usabilidad*

| Código | Requisito |
|---|---|
| RNF-USA-01 | La interfaz debe ser clara, adaptable a distintos tamaños de pantalla y coherente, con retroalimentación inmediata. No se declara un nivel formal de conformidad con las pautas WCAG. |

### Interoperabilidad

La Tabla 21 establece los requisitos de interoperabilidad con servicios externos y con el dispositivo de medición.

**Tabla 21**

*Requisitos no funcionales de interoperabilidad*

| Código | Requisito |
|---|---|
| RNF-INT-01 | Los dispositivos de medición deben comunicarse con el sistema mediante conexiones iniciadas desde el hogar, de modo que funcionen detrás de cualquier router doméstico sin cambiar la configuración del router. |
| RNF-INT-02 | El sistema debe enviar correos mediante una cuenta de correo configurada para ese fin. |
| RNF-INT-03 | El sistema debe permitir el acceso con cuentas de Google solicitando únicamente el nombre, el correo y la foto del perfil. |
| RNF-INT-04 | Las notificaciones del navegador solo deben funcionar en una conexión segura y requieren que el sistema tenga habilitado ese servicio. |
| RNF-INT-05 | La configuración del dispositivo por Bluetooth debe estar disponible hasta 10 minutos, no debe interrumpir la medición y no debe exigir emparejamiento previo. |

## Historias de usuario y casos de uso

Las historias de usuario describen las interacciones desde la perspectiva de quien las usa. Cada una indica los requisitos que cubre y sus criterios de aceptación en formato Dado, Cuando, Entonces. Los casos de uso detallan los dos flujos más críticos del sistema.

### Historias de usuario

#### HU-01 Registrar una cuenta

Como persona que quiere monitorear su consumo, quiero registrarme y verificar mi correo, para acceder al sistema.

Requisitos relacionados: RF-SEC-01, RF-SEC-02, RF-SEC-13.

- CA-01. Dado un correo ya registrado, cuando intento registrarme con él, entonces el sistema muestra un error y no crea una segunda cuenta.
- CA-02. Dado un registro exitoso, cuando ingreso el código de seis dígitos que llegó a mi correo dentro de su ventana de validez, entonces se crea mi cuenta con el correo ya verificado. Si no lo verifico en 24 horas, la solicitud se descarta y no queda ninguna cuenta.
- CA-39. Dado un registro sin aceptar los términos y condiciones, cuando lo envío, entonces el sistema lo rechaza pidiendo la aceptación y no guarda nada. Lo mismo ocurre con el primer inicio con Google.
- CA-40. Dado un correo con un dominio que parece un error de escritura de un proveedor conocido (`meil.com`) o que no existe, cuando intento registrarme, entonces el sistema lo rechaza y, si corresponde, indica el proveedor que probablemente quise escribir.

#### HU-02 Iniciar y mantener la sesión

Como usuario registrado, quiero iniciar sesión y mantenerla de forma segura, para consultar mis datos sin repetir mis credenciales a cada rato.

Requisitos relacionados: RF-SEC-03, RF-SEC-04, RF-SEC-10, RNF-SEG-01, RNF-SEG-02.

- CA-03. Dado un inicio de sesión correcto, cuando el sistema concede el acceso, entonces este dura 15 minutos y se renueva mientras la sesión siga vigente.
- CA-04. Dada una credencial de renovación ya usada, cuando intento usarla por segunda vez, entonces el sistema invalida la sesión.
- CA-05. Dada una solicitud que indica la identidad de otro usuario, cuando el sistema la atiende, entonces usa la identidad de mi sesión.

#### HU-03 Recuperar la contraseña

Como usuario que olvidó su contraseña, quiero recuperarla con un código enviado a mi correo, para volver a entrar a mi cuenta.

Requisitos relacionados: RF-SEC-06, RF-NOT-06, RNF-SEG-05, RNF-SEG-06.

- CA-06. Dado un correo que no existe, cuando solicito el código, entonces el sistema responde igual que si existiera. El intento número 11 en 15 minutos desde la misma dirección se rechaza e indica cuánto esperar.
- CA-07. Dado que el envío de correo no está configurado, cuando solicito el código, entonces el sistema responde igual y la entrega queda registrada como fallida.

#### HU-04 Eliminar mi cuenta

Como usuario, quiero eliminar mi cuenta, para dejar de usar el servicio y liberar mi correo.

Requisitos relacionados: RF-SEC-11.

- CA-08. Dada una contraseña incorrecta, cuando solicito eliminar la cuenta, entonces el sistema la rechaza. Con la contraseña correcta la elimina, el correo puede registrarse de nuevo y las sesiones quedan cerradas.
- CA-09. Dado que soy el único propietario de un hogar con otros miembros, cuando solicito eliminar la cuenta, entonces el sistema la rechaza con un mensaje legible y no modifica ningún dato.
- CA-41. Dada una cuenta creada con Google, cuando solicito eliminarla, entonces el sistema me envía un código a mi correo y solo la elimina con el código correcto; con uno equivocado o vencido la rechaza, y tras cinco intentos en 15 minutos me pide esperar. Al eliminarla, puedo volver a registrarme con la misma cuenta de Google.
- CA-42. Dada una cuenta creada con Google, cuando intento cambiar la contraseña, entonces el sistema lo rechaza con un mensaje legible.

#### HU-05 Crear un hogar y definir sus umbrales

Como propietario, quiero crear un hogar, invitar a sus miembros y fijar sus límites de consumo, para que el sistema me avise cuando se superen.

Requisitos relacionados: RF-HOM-01 a RF-HOM-08.

- CA-10. Dado un hogar nuevo, cuando restablezco sus umbrales a los valores del sistema, entonces muestra 6 kWh diarios y 180 kWh mensuales.
- CA-11. Dado un usuario que pertenece a dos hogares, cuando consulta sus roles, entonces puede ser propietario en uno y miembro en el otro al mismo tiempo.

#### HU-06 Vincular un dispositivo de medición

Como propietario, quiero vincular un dispositivo de medición a mi hogar desde la web, para que empiece a enviar lecturas de un electrodoméstico.

Requisitos relacionados: RF-DEV-02, RF-DEV-07, RF-DEV-08, RNF-SEG-04.

- CA-12. Dado un dispositivo ya vinculado a otro hogar, cuando intento vincularlo, entonces el sistema lo rechaza. Si lo vinculo de nuevo al mismo hogar, conserva su historial y recibe credenciales nuevas.
- CA-13. Dado un dispositivo con credenciales válidas, cuando se conecta, entonces el sistema acepta sus datos. Con credenciales incorrectas los rechaza.
- CA-14. Dado un dispositivo vinculado, cuando intenta enviar datos como si fuera otro dispositivo, entonces el sistema lo rechaza.

#### HU-07 Desvincular un dispositivo

Como propietario, quiero eliminar un dispositivo de mi hogar, para que el dispositivo quede disponible de inmediato para vincularse de nuevo.

Requisitos relacionados: RF-DEV-06.

- CA-15. Dado un dispositivo conectado, cuando elimino el dispositivo, entonces el dispositivo olvida sus credenciales y ofrece el Bluetooth en pocos segundos.

#### HU-08 Consultar el consumo de mi hogar

Como miembro del hogar, quiero ver el consumo actual y el historial de cada electrodoméstico, para identificar los periodos de mayor demanda.

Requisitos relacionados: RF-MED-01 a RF-MED-09.

- CA-16. Dado un dispositivo en línea, cuando consulto su consumo, entonces aparece una lectura nueva cada 60 segundos con voltaje, corriente, potencia y energía.
- CA-17. Dado un mensaje que declara pertenecer a un dispositivo distinto del que lo envía, cuando el sistema lo recibe, entonces no lo guarda.
- CA-18. Dadas lecturas de 1499 W, 1500 W y 3000 W, cuando el sistema las clasifica, entonces son MEDIUM, HIGH y CRITICAL respectivamente.
- CA-19. Dado que el sistema estuvo no disponible, cuando se restablece la conexión, entonces el dispositivo envía hasta 30 muestras acumuladas con su hora real.

#### HU-09 Recibir alertas

Como miembro del hogar, quiero que el sistema me alerte cuando el consumo sea excesivo o un dispositivo se desconecte, para reaccionar a tiempo.

Requisitos relacionados: RF-ALE-01 a RF-ALE-06, RF-MED-06.

- CA-20. Dado un dispositivo en línea, cuando lo desconecto, entonces el sistema lo marca sin conexión en unos 22 segundos si el dispositivo avisa, o como máximo a los 150 segundos, y genera la alerta de desconexión. Al reconectarlo, la alerta se resuelve sola.
- CA-21. Dada una lectura en nivel HIGH, cuando el sistema la procesa, entonces genera una alerta de consumo alto. La primera lectura LOW o MEDIUM la resuelve.
- CA-22. Dada una energía del día que alcanza el límite diario, cuando el sistema la evalúa, entonces genera la alerta de límite diario. Si subo el límite por encima del consumo, la alerta se resuelve.

#### HU-10 Recibir recomendaciones de ahorro

Como miembro del hogar, quiero recibir recomendaciones basadas en mi propio consumo, para ahorrar energía.

Requisitos relacionados: RF-REC-01 a RF-REC-09.

- CA-23. Dado que el 40 % o más de la energía de los últimos 7 días (mínimo 2 kWh) se consumió entre las 18:00 y las 22:00, cuando el sistema evalúa las reglas, entonces crea una recomendación de hora punta.
- CA-24. Dada una recomendación que eliminé, cuando el sistema vuelve a evaluar las reglas, entonces la misma no reaparece antes de 7 días.
- CA-25. Dado un usuario que no es miembro del hogar, cuando intenta ver sus recomendaciones, entonces el sistema no se las muestra.

#### HU-11 Elegir cómo recibir las notificaciones

Como usuario, quiero elegir si recibo las alertas y recomendaciones por correo, por notificación del navegador o por ambos, para enterarme por el canal que prefiero.

Requisitos relacionados: RF-NOT-01 a RF-NOT-05.

- CA-26. Dados ambos canales activos, cuando se genera una alerta, entonces cada miembro recibe un correo y una notificación del navegador. Con un canal desactivado, recibe solo el otro.
- CA-27. Dado cualquier intento de entrega, cuando termina, entonces queda registrado como enviado o fallido, sin el contenido del mensaje.

#### HU-12 Usar el sistema en mi idioma

Como usuario, quiero cambiar el idioma de la aplicación, para entender la información.

Requisitos relacionados: RF-CLI-05, RNF-I18-01, RNF-I18-02.

- CA-28. Dado un idioma elegido entre español, inglés, francés y portugués, cuando lo cambio en la web o en la móvil, entonces todos los textos pasan a ese idioma.

#### HU-13 Proteger los accesos

Como administrador del sistema, quiero que el sistema rechace orígenes y redes no autorizados, para proteger los datos de los usuarios.

Requisitos relacionados: RNF-SEG-03, RNF-SEG-07.

- CA-29. Dada una solicitud de una aplicación web no autorizada, cuando el sistema responde, entonces la rechaza.
- CA-30. Dada una solicitud a las funciones internas de autenticación de dispositivos desde una red no permitida, cuando el sistema la recibe, entonces la rechaza.

#### HU-14 Evolucionar los datos sin perder información

Como desarrollador, quiero que cada cambio en la estructura de los datos se pueda revertir, para poder deshacer un cambio fallido.

Requisitos relacionados: RNF-MAN-02.

- CA-31. Dado cualquier cambio en la estructura de los datos, cuando se revisa, entonces existe una forma de revertirlo.

#### HU-15 Eliminar un hogar o transferir su propiedad

Como propietario, quiero eliminar mi hogar o ceder su propiedad a otro miembro, para dejar de administrarlo sin perder el control de sus datos.

Requisitos relacionados: RF-HOM-05, RF-HOM-09, RF-HOM-10, RF-DEV-06.

- CA-32. Dado un hogar con dispositivos vinculados, cuando el propietario lo elimina, entonces los dispositivos quedan desvinculados y libres, y los miembros ya no ven el hogar.
- CA-33. Dado un hogar en el que soy miembro, cuando intento eliminarlo o transferir su propiedad, entonces el sistema lo rechaza por falta de permiso.
- CA-34. Dado un hogar con propietario y miembros, cuando el propietario transfiere la propiedad a un miembro, entonces el nuevo propietario asume el rol, el anterior queda como miembro y el hogar sigue teniendo un único propietario.

#### HU-16 Administrar las cuentas de la plataforma

Como administrador del sistema, quiero consultar las cuentas, cambiar su estado y gestionar el rol de administrador, para mantener la plataforma bajo control sin entrar a la base de datos.

Requisitos relacionados: RF-ADM-01 a RF-ADM-09, RNF-SEG-11.

- CA-35. Dada una cuenta sin el rol de administrador, cuando intenta abrir la administración, entonces el sistema la rechaza por falta de permiso.
- CA-36. Dada una cuenta activa con sesiones abiertas, cuando un administrador la bloquea, entonces la cuenta ya no puede iniciar sesión y sus sesiones se cierran.
- CA-37. Dado un administrador, cuando intenta cambiar el estado o los roles de su propia cuenta, entonces el sistema lo rechaza con un mensaje legible y no modifica nada.
- CA-38. Dados dos administradores, cuando uno de ellos concede el rol de administrador a otra cuenta, entonces el otro administrador recibe un aviso dentro de la aplicación y quien hizo el cambio no lo recibe.

#### HU-17 Consultar la ayuda desde Ajustes

Como persona que usa EnergyMonitor, quiero consultar la documentación, pedir soporte y ver las novedades sin salir de la aplicación, para resolver mis dudas rápido.

Requisitos relacionados: RF-CLI-07, RF-CLI-08, RF-HOM-12, RF-ALE-07.

- CA-43. Dada una lectura de 300 W de un aparato que suele consumir 100 W, cuando el sistema la procesa, entonces genera una alerta de consumo inusual; con una lectura de 240 W o con menos de 24 horas de historial no genera nada, y la alerta pendiente se resuelve sola al volver a lo normal.
- CA-44. Dado un usuario o un administrador, cuando abre la documentación en Ajustes, entonces ve solo la guía de su rol.
- CA-45. Dado un usuario, cuando abre el soporte técnico en Ajustes, entonces ve una explicación breve y puede crear el ticket como un correo a un administrador; un administrador no ve el soporte, porque es quien lo atiende.
- CA-46. Dada una versión nueva de la web desplegada, cuando abro las novedades, entonces veo los cambios de esa versión sin que nadie los haya escrito a mano.
- CA-47. Dada una cuenta con foto, cuando elijo «Ver foto», entonces se amplía dentro de la página, y en un hogar veo la foto de cada miembro que la tiene.

### Casos de uso

#### CU-01 Vincular un dispositivo de medición

La Tabla 22 resume el caso de uso de vinculación de un dispositivo.

**Tabla 22**

*Caso de uso CU-01 Vincular un dispositivo de medición*

| Campo | Detalle |
|---|---|
| Actores | Propietario del hogar, dispositivo de medición |
| Precondiciones | El usuario es propietario del hogar. El dispositivo está en modo de configuración (recién configurado, sin vincular, o con el botón del dispositivo pulsado 3 segundos). El navegador es Chrome o Edge en una conexión segura |
| Postcondiciones | El dispositivo queda registrado con sus credenciales, se conecta al sistema y sale del modo de configuración |
| Requisitos relacionados | RF-DEV-02, RF-DEV-07, RF-DEV-08, RNF-INT-05 |

Flujo normal:

1. El propietario abre Hogar, Dispositivos, Vincular dispositivo.
2. La aplicación busca el dispositivo por Bluetooth y el propietario elige `EnergyMonitor-<código>`.
3. El propietario elige el electrodoméstico, el nombre y la habitación.
4. La aplicación lista las redes Wi-Fi que ve el dispositivo y el propietario elige la de su hogar y escribe su contraseña.
5. La aplicación registra el dispositivo en el sistema, que le entrega sus credenciales.
6. La aplicación envía al dispositivo, por Bluetooth, la red, la contraseña, la dirección del servicio y las credenciales, y muestra el avance real del dispositivo.
7. El dispositivo se conecta al sistema, que verifica sus credenciales, y sale del modo de configuración.

Flujos alternos:

- A1. El navegador no tiene Bluetooth: el usuario se conecta a la red `EnergyMonitor-Setup`, abre la página de Energy Monitor y completa la configuración en el portal.
- A2. El dispositivo ya está vinculado a otro hogar: el sistema rechaza la vinculación.
- A3. La contraseña de la red Wi-Fi es incorrecta: el dispositivo lo informa y el propietario corrige la contraseña.

#### CU-02 Atender una alerta de desconexión

La Tabla 23 resume el caso de uso de la alerta por desconexión de un dispositivo.

**Tabla 23**

*Caso de uso CU-02 Atender una alerta de desconexión*

| Campo | Detalle |
|---|---|
| Actores | Dispositivo de medición, sistema, miembros del hogar |
| Precondiciones | El dispositivo está vinculado y en línea. Los miembros tienen activo al menos un canal de notificación |
| Postcondiciones | La alerta queda resuelta cuando el dispositivo vuelve a reportar. El estado del dispositivo queda registrado |
| Requisitos relacionados | RF-MED-04 a RF-MED-06, RF-ALE-02, RF-NOT-01 |

Flujo normal:

1. El dispositivo pierde la energía o la conexión.
2. El sistema recibe el aviso de desconexión del dispositivo unos 22 segundos después.
3. El sistema marca el dispositivo como sin conexión y genera la alerta de desconexión.
4. El sistema entrega la alerta a todos los miembros del hogar por correo y por notificación del navegador, según sus preferencias.
5. El dispositivo se reconecta de forma automática.
6. El sistema registra el estado en línea y resuelve la alerta.

Flujos alternos:

- A1. No llega el aviso de desconexión: el sistema marca sin conexión tras 150 segundos sin datos.
- A2. Los canales de un miembro están desactivados: ese miembro no recibe la notificación, pero la alerta sigue visible en la aplicación.

## Referencias

Congreso de la República de Colombia. (2012, 17 de octubre). *Ley 1581 de 2012: Por la cual se dictan disposiciones generales para la protección de datos personales*. Diario Oficial n.º 48.587.

Institute of Electrical and Electronics Engineers. (1998). *IEEE recommended practice for software requirements specifications* (IEEE Std 830-1998). https://doi.org/10.1109/IEEESTD.1998.88286
