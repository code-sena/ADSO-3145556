<!-- running-head: MANUAL DE USUARIO DEL CLIENTE: SISTEMA DE LAVADO VEHICULAR -->

# Manual de Usuario del Cliente

Sistema de Gestión de Lavado Vehicular (Vehicle Washing)

Cómo Registrarse, Reservar un Lavado, Pagar con Código QR o en Efectivo, Acumular Puntos y Calificar el Servicio, en la Web y en la Aplicación Móvil

Brayan Estiven Patiño Cabrera (líder de proyecto), Juan Camilo Guilombo Osorio (líder de frontend) y Merari Urbano (líder de backend)

Tecnólogo en Análisis y Desarrollo de Software (ADSO), Servicio Nacional de Aprendizaje (SENA), Centro de Formación: Industria

Ficha 3145556

Instructora: Karol Daniela Correa

27 de octubre de 2026

Tipo de manual: Manual de usuario del rol Cliente (aplicación web y aplicación móvil Android) · Versión 1.5

## Control de Versiones

La Tabla 1 registra el historial de cambios de este documento. Cada vez que el equipo de desarrollo modifique el manual, se debe agregar una fila con la versión, la fecha, la descripción del cambio y el responsable.

**Tabla 1**

*Historial de Versiones del Manual de Usuario del Cliente*

| Versión | Fecha | Descripción del cambio | Responsable |
|---|---|---|---|
| 1.0 | 2026-10-26 | Creación del manual exclusivo del rol Cliente, separado del Manual de Usuario (versión 4.9) para que cada público tenga su propio documento, según la guía de manuales del SENA. Revisado contra *develop* del 26 de octubre de 2026: pago al reservar (sección 10.3), pantalla de pago según la forma elegida (10.5), nueva sección del reporte de inspección del vehículo que el lavadero comparte por enlace (10.8), términos y condiciones actualizados con la cláusula del informe de inspección y sus fotografías (8.1) y aplicación móvil (10.11). Capturas nuevas de la reserva con el paso "¿Cómo vas a pagar?", de la confirmación en efectivo, de las pantallas de pago digital (con plazo) y en efectivo, y del historial con las insignias de pago. | Equipo de desarrollo |
| 1.1 | 2026-10-26 | Política de Datos con las fotografías de la inspección (8.1); aplicación móvil 1.1.1 con el APK *versionCode* 6 y la actualización por internet publicada (10.11). | Equipo de desarrollo |
| 1.2 | 2026-10-27 | Las horas alternativas de "Esa hora ya se ocupó" ya no incluyen las que se cruzan con otra reserva del mismo vehículo (10.3 y 13), con la Figura 16 nueva. | Equipo de desarrollo |
| 1.3 | 2026-10-27 | Capturas de la aplicación móvil (Figuras 26 a 32) en la sección 10.11; el cierre de sesión pasa a ser la Figura 33. | Equipo de desarrollo |
| 1.4 | 2026-10-27 | Términos y Condiciones: el apartado 6 con el plazo de 30 minutos del pago digital y el límite de una reserva activa en efectivo (8.1); actualización por internet de la app del 27 de octubre (*v1.1.2*) (10.11 y referencias). | Equipo de desarrollo |
| 1.5 | 2026-10-27 | Figura 29 tomada de nuevo con la app *v1.1.2*: la nota bajo **Reservar Ahora** ya no habla de lavado a domicilio (10.11). | Equipo de desarrollo |

*Nota.* Las fechas se escriben en formato ISO 8601 (año-mes-día). Elaborado por el equipo de desarrollo.

## Tabla de Contenido

<!-- toc -->

- [Control de Versiones](#control-de-versiones)
- [4. Introducción](#4-introducción)
  - [4.1 Fuentes y Convenciones de Este Manual](#41-fuentes-y-convenciones-de-este-manual)
- [5. Objetivo](#5-objetivo)
  - [5.1 Objetivo General](#51-objetivo-general)
  - [5.2 Objetivos Específicos](#52-objetivos-específicos)
- [6. Alcance](#6-alcance)
  - [6.1 Qué Cubre Este Manual](#61-qué-cubre-este-manual)
  - [6.2 Qué No Cubre Este Manual](#62-qué-no-cubre-este-manual)
  - [6.3 Sobre las Capturas de Pantalla](#63-sobre-las-capturas-de-pantalla)
- [7. Requisitos para Usar el Sistema](#7-requisitos-para-usar-el-sistema)
  - [7.1 Requisitos del Dispositivo y de la Cuenta](#71-requisitos-del-dispositivo-y-de-la-cuenta)
  - [7.2 Presentación del Sistema](#72-presentación-del-sistema)
- [8. Ingreso al Sistema](#8-ingreso-al-sistema)
  - [8.1 Registro de un Cliente Nuevo](#81-registro-de-un-cliente-nuevo)
  - [8.2 Inicio de Sesión](#82-inicio-de-sesión)
  - [8.3 Recuperación de la Contraseña](#83-recuperación-de-la-contraseña)
- [9. Menú Principal](#9-menú-principal)
- [10. Módulos del Sistema](#10-módulos-del-sistema)
  - [10.1 Panel del Cliente](#101-panel-del-cliente)
  - [10.2 Mis Vehículos](#102-mis-vehículos)
  - [10.3 Reservar un Lavado](#103-reservar-un-lavado)
  - [10.4 Consultar o Cancelar una Reserva](#104-consultar-o-cancelar-una-reserva)
  - [10.5 Pagar un Servicio](#105-pagar-un-servicio)
  - [10.6 Historial y Calificación](#106-historial-y-calificación)
  - [10.7 Notificaciones](#107-notificaciones)
  - [10.8 Reporte de Inspección del Vehículo](#108-reporte-de-inspección-del-vehículo)
  - [10.9 Perfil](#109-perfil)
  - [10.10 Configuración](#1010-configuración)
  - [10.11 Aplicación Móvil Android](#1011-aplicación-móvil-android)
- [11. Mensajes y Validaciones](#11-mensajes-y-validaciones)
- [12. Cierre de Sesión](#12-cierre-de-sesión)
- [13. Preguntas Frecuentes](#13-preguntas-frecuentes)
- [14. Glosario](#14-glosario)
- [Referencias](#referencias)
- [15. Anexos](#15-anexos)
  - [Anexo A. Origen de las Capturas](#anexo-a-origen-de-las-capturas)
  - [Anexo B. Lista de Verificación para la Entrega](#anexo-b-lista-de-verificación-para-la-entrega)

<!-- /toc -->

*Nota.* La numeración empieza en 4 porque las secciones 1 a 3 son la portada, el control de versiones y la tabla de contenido, que no llevan número en el título, como en la plantilla mínima de la guía de manuales del SENA.

## 4. Introducción

El Sistema de Gestión de Lavado Vehicular (Vehicle Washing) es una plataforma web y móvil que permite a los clientes de un lavadero reservar un turno desde el teléfono o el computador, pagar escaneando el código QR del negocio o en efectivo en el local, acumular puntos que desbloquean cupones de descuento y conservar el historial y la calificación de cada servicio.

Antes del sistema, los clientes tenían que llamar o desplazarse al local sin saber si había un turno libre, y los pagos en efectivo no dejaban registro. Con la plataforma, el cliente consulta la disponibilidad real del lavadero, reserva en pocos minutos y sigue el avance de su servicio desde cualquier dispositivo.

Este manual está dirigido exclusivamente al **cliente**, es decir, a la persona dueña del vehículo que usa el sistema para reservar y pagar sus lavados. Explica en lenguaje sencillo y paso a paso cómo crear la cuenta, ingresar, navegar y usar cada módulo. No se necesitan conocimientos de programación, de bases de datos ni de configuración técnica. Las funciones del operario se explican en el Manual de Usuario del Operario y las del administrador, en el Manual de Administrador.

El documento sigue la plantilla mínima del manual de usuario de la guía para elaborar manuales de software del programa Tecnólogo en Análisis y Desarrollo de Software (Servicio Nacional de Aprendizaje [SENA], 2026) y aplica las normas APA, séptima edición, en títulos, tablas, figuras, citas y referencias. Su contenido se verificó contra el código fuente de la aplicación web (Equipo de Desarrollo Vehicle Washing, 2026b), de la aplicación móvil (Equipo de Desarrollo Vehicle Washing, 2026c) y la documentación del proyecto (Equipo de Desarrollo Vehicle Washing, 2026a). En cuanto a los datos personales, el sistema se rige por la Ley Estatutaria 1581 de 2012 (Congreso de la República de Colombia, 2012).

### 4.1 Fuentes y Convenciones de Este Manual

Las pantallas, rutas, botones y mensajes descritos corresponden a la rama *develop* de la aplicación web y de la aplicación móvil al 26 de octubre de 2026, la misma versión que corre en el servidor público desde ese día. En la interfaz el sistema aparece con su nombre comercial, **LavaRápido**. Los nombres de botones, menús y mensajes se escriben en **negrita** o entre comillas tal como aparecen cuando el idioma de la interfaz es el español; la interfaz también está disponible en inglés, francés y portugués. Una ruta como */client/vehicles* es la parte de la dirección web que sigue al dominio del sistema (por ejemplo, *https://lavarapidovehicular.northcentralus.cloudapp.azure.com/client/vehicles*).

**Datos siempre al día.** No hace falta recargar la página para ver un cambio hecho por el lavadero: la web actualiza la pantalla al entrar, al volver a la pestaña o a la ventana y cada vez que llega una notificación nueva, y las pantallas que esperan una acción del lavadero (por ejemplo, **Pagar servicio** mientras el administrador revisa el pago) se actualizan cada 45 segundos mientras la pestaña está visible. La app móvil se actualiza al volver a la pantalla y al llegar una notificación push. Así, cuando el administrador aprueba un pago o asigna un operario, el cliente lo ve en menos de un minuto.

## 5. Objetivo

### 5.1 Objetivo General

Explicar paso a paso cómo el cliente debe registrarse, ingresar, navegar y usar las funciones del Sistema de Gestión de Lavado Vehicular, de modo que pueda reservar, pagar y calificar sus lavados sin ayuda del equipo de desarrollo ni del personal del lavadero.

### 5.2 Objetivos Específicos

- Guiar al cliente en el registro de la cuenta, el inicio de sesión y la recuperación de la contraseña.
- Describir cómo el cliente registra vehículos, reserva un lavado eligiendo la forma de pago, consulta y cancela una reserva, paga el servicio y canjea cupones.
- Explicar cómo consultar el historial, calificar un servicio, leer las notificaciones y abrir el reporte de inspección del vehículo.
- Indicar cómo personalizar el perfil, el idioma, el tema y las notificaciones, y cómo usar la aplicación móvil Android.
- Explicar el significado de los mensajes del sistema y la acción que debe tomar el cliente en cada caso, y responder las preguntas más frecuentes.

## 6. Alcance

### 6.1 Qué Cubre Este Manual

- La aplicación web del sistema, usada desde un navegador en un computador, una tableta o un teléfono.
- La aplicación móvil Android para el rol Cliente (sección 10.11).
- Todas las funciones del rol **Cliente**: registro, inicio de sesión, recuperación de la contraseña, panel, vehículos, reservas, pagos, puntos de fidelización y cupones, historial y calificaciones, notificaciones, reporte de inspección del vehículo, perfil, configuración y cierre de sesión.

### 6.2 Qué No Cubre Este Manual

- Las funciones del rol **Operario** (agenda, servicios asignados, inicio y finalización de servicios), que se explican en el Manual de Usuario del Operario.
- Las funciones del rol **Administrador** (revisión de pagos, asignación de operarios, horarios, bahías, usuarios, servicios, promociones y reportes), que se explican en el Manual de Administrador.
- La instalación, la configuración y la construcción interna del software, que se explican en el Manual de Instalación y en el Manual Técnico.
- El lavado a domicilio: en la versión actual el cliente lleva el vehículo al lavadero en la fecha y la hora reservadas.
- La facturación electrónica ante la DIAN y las pasarelas de pago con tarjeta: el sistema no cobra con tarjeta ni se conecta con los bancos.

### 6.3 Sobre las Capturas de Pantalla

Las figuras de este manual son capturas reales del sistema funcionando con cuentas y datos de prueba, ordenadas según el flujo de uso. Las pantallas de reserva, pago e historial (Figuras 13 a 15 y 18 a 21) se tomaron de nuevo el 26 de octubre de 2026 para mostrar el pago al reservar y la Figura 16, el 27 de octubre; las demás son del 19 de octubre de 2026 y no cambiaron desde entonces. Las Figuras 26 a 32 son de la aplicación móvil. Su origen se explica en el Anexo A.

## 7. Requisitos para Usar el Sistema

### 7.1 Requisitos del Dispositivo y de la Cuenta

El sistema funciona en las versiones actuales de Google Chrome, Mozilla Firefox y Microsoft Edge, y su interfaz se adapta a la pantalla de un teléfono, una tableta o un computador. La Tabla 2 lista lo que necesita el cliente.

**Tabla 2**

*Requisitos para Usar el Sistema como Cliente*

| Recurso | Requisito |
|---|---|
| Dispositivo | Computador, tableta o teléfono inteligente. Para la app móvil, un teléfono Android. |
| Navegador | Google Chrome, Mozilla Firefox o Microsoft Edge, en una versión actualizada. |
| Conexión | Acceso estable a internet durante todo el uso del sistema. |
| Correo | Una cuenta de correo terminada en @gmail.com para registrarse. |
| Documento de identidad | Un número de documento colombiano de 5 a 20 dígitos. |
| Teléfono | Un número celular colombiano de 10 dígitos que empiece por 3. |
| Pago digital | Nequi, Daviplata o una aplicación bancaria para escanear el código QR y hacer la transferencia. Si se prefiere, se puede pagar en efectivo en el lavadero cuando el negocio tiene activa esa opción. |

*Nota.* Elaborado por los autores a partir de las validaciones del formulario de registro y de los requisitos no funcionales del sistema.

### 7.2 Presentación del Sistema

**Pantalla de bienvenida.** Al abrir la dirección web del sistema aparece la pantalla de bienvenida. En la parte superior hay una barra de navegación con los enlaces **Inicio**, **Nosotros** y **Servicios**, un selector de idioma y los botones **Iniciar Sesión** y **Registrarse**. El bloque central muestra los botones **Comenzar ahora** y **Ya tengo cuenta**; más abajo aparecen, en este orden, la sección "Quiénes somos", "¿Por qué elegirnos?", los servicios de lavado con su descripción y duración, la descarga de la aplicación móvil, los datos de contacto del lavadero y la sección **Preguntas frecuentes**, que responde en un acordeón cómo reservar, los métodos de pago, la cancelación, la elección del operario, los puntos, los recordatorios y la aplicación para celular. El pie de página tiene los enlaces **Sobre nosotros**, los tres servicios, **Centro de ayuda**, **Preguntas frecuentes**, **Términos de servicio** y **Política de privacidad**.

**Uso en celulares y tabletas.** Todas las pantallas se adaptan al ancho del dispositivo desde 360 píxeles, sin desplazamiento lateral. En el celular, la barra superior muestra solo el selector de idioma e **Iniciar sesión** (el registro sigue disponible con **Comenzar ahora**), los botones de la portada ocupan todo el ancho y, con la sesión iniciada, el menú lateral se abre con el botón ☰ de la esquina superior izquierda.

**Figura 1**

*Pantalla de Bienvenida del Sistema*

![Pantalla de Bienvenida del Sistema](capturas/cliente-01-pantalla-de-bienvenida-del-sistema.jpg)

*Nota.* Captura en un navegador de escritorio con la ventana completa.

**Tipos de servicio de lavado.** El lavadero ofrece tres niveles de limpieza. Cada servicio tiene su propia duración, su precio y los puntos de fidelización que suma, y **el precio depende del tipo de vehículo**; por eso el valor final se muestra en el resumen de la reserva una vez se eligen el vehículo y el servicio (Tabla 3).

**Tabla 3**

*Niveles de Servicio de Lavado*

| Servicio | Qué incluye | Duración estimada |
|---|---|---|
| Básico | Lavado exterior completo, aspirado básico y limpieza de vidrios. | 45 minutos |
| Premium | Todo lo del Básico, más lavado de motor y cera líquida protectora. | 75 minutos |
| Completo | Todo lo del Premium, más encerado a mano y detallado interior. | 120 minutos |

*Nota.* Las duraciones corresponden a los datos iniciales del sistema. El administrador configura los precios, las duraciones y los puntos de cada tipo de vehículo, y pueden cambiar.

**Idiomas y temas.** Toda la interfaz está disponible en español, inglés, francés y portugués, y en cuatro temas de color (verde claro, verde oscuro, rosado y rosado oscuro). El idioma se puede cambiar desde la pantalla de bienvenida o, con la sesión iniciada, desde **Configuración** (sección 10.10).

## 8. Ingreso al Sistema

Para reservar se necesita una cuenta de cliente. El cliente la crea él mismo con el registro (sección 8.1) y desde entonces entra con su correo y su contraseña (sección 8.2).

### 8.1 Registro de un Cliente Nuevo

**Nombre del módulo:** Registro de clientes.

**Descripción:** Permite a una persona nueva crear una cuenta de cliente para reservar servicios de lavado.

**Rol que puede usarlo:** Persona sin cuenta (futuro cliente).

**Ruta o menú donde se encuentra:** Pantalla de bienvenida > botón **Registrarse** (ruta */auth/register*).

**Pasos para utilizarlo:**

1. Abrir el navegador e ingresar la dirección web del sistema.
2. Hacer clic en **Registrarse** en la barra superior (o en **Comenzar ahora**).
3. Escribir el **Número de Documento**: solo dígitos, entre 5 y 20.
4. Escribir los **Nombres** (mínimo 3 caracteres) y los **Apellidos** (mínimo 2 caracteres).
5. Escribir el **Correo Electrónico**, que debe terminar en @gmail.com (el registro solo acepta Gmail; el servidor también lo comprueba). El campo no admite espacios.
6. Escribir el **Teléfono**: un número celular colombiano de 10 dígitos que empiece por 3. El campo solo acepta dígitos.
7. Escribir la **Contraseña** y observar la lista de requisitos, que se marcan a medida que se cumplen: mínimo 8 caracteres, al menos una letra mayúscula, al menos un número y al menos un carácter especial. **Los cuatro requisitos son obligatorios**; no se admiten espacios.
8. Repetir la contraseña en **Confirmar Contraseña**. El ícono del ojo muestra u oculta lo escrito.
9. Abrir los **Términos y Condiciones** y la **Política de Tratamiento de Datos Personales** con sus enlaces, leerlos y aceptar ambos. Al hacer clic en **Aceptar y continuar** dentro de cada documento, la casilla correspondiente se marca automáticamente.
10. Hacer clic en **Registrarse**. Mientras se procesa la solicitud, el botón muestra "Registrando...".

**Resultado esperado:** El sistema muestra el mensaje "¡Registro exitoso!" y, al hacer clic en **Ir a iniciar sesión**, lleva a la persona a la pantalla de inicio de sesión. Si el lavadero tiene activado el correo, llega un mensaje de bienvenida.

**Observaciones o restricciones:**

- No es posible registrar dos cuentas con el mismo correo ni con el mismo número de documento: el sistema muestra "Ya existe una cuenta con este correo" o "Ya existe una cuenta con este número de documento" y no crea la cuenta.
- El servidor exige las dos casillas de aceptación: sin ellas responde "Debes aceptar el tratamiento de datos personales (Ley 1581) para registrarte."
- Los **Términos y Condiciones** tienen 15 apartados. Desde el 26 de octubre de 2026 están redactados en tercera persona ("el Cliente"). El apartado 6, **Reservas, cancelaciones y prestación del servicio**, dice que con pago digital el comprobante debe cargarse dentro de los 30 minutos siguientes a la reserva (o antes de la hora del servicio, si es más pronto), porque si no la reserva se cancela sola y el horario se libera, y que con pago en efectivo el cliente puede tener como máximo una reserva activa. Los términos incluyen además el apartado 8, **Informe de inspección y fotografías del vehículo**: en los servicios de tres horas o más, el lavadero puede registrar el estado del vehículo antes y después del servicio, por fases, con hasta cinco fotografías por hallazgo, y compartir ese informe con el cliente (sección 10.8). La **Política de Tratamiento de Datos Personales** incluye esas fotografías entre los datos que se recolectan (apartado 4), entre las finalidades (registrar el estado del vehículo y atender reclamaciones por daños, apartado 5) y en la conservación (se guardan el tiempo necesario para atender reclamaciones sobre el servicio, apartado 12).

**Figura 2**

*Formulario de Registro de Clientes*

![Formulario de Registro de Clientes](capturas/cliente-02-formulario-de-registro-de-clientes.jpg)

*Nota.* En la captura no aparece información personal real.

**Figura 3**

*Mensajes de Validación en el Registro*

![Mensajes de Validación en el Registro](capturas/cliente-03-mensajes-de-validacion-en-el-registro.jpg)

*Nota.* Apoya la Tabla 8 de la sección 11.

### 8.2 Inicio de Sesión

**Nombre del módulo:** Inicio de sesión.

**Descripción:** Permite al cliente entrar al sistema con su correo y su contraseña.

**Rol que puede usarlo:** Cliente (la misma pantalla la usan el operario y el administrador).

**Ruta o menú donde se encuentra:** Pantalla de bienvenida > botón **Iniciar Sesión** (ruta */auth*).

**Pasos para utilizarlo:**

1. Abrir el navegador e ingresar la dirección web del sistema.
2. Hacer clic en **Iniciar Sesión**.
3. Escribir el **Correo Electrónico** registrado. El inicio de sesión acepta un correo de cualquier dominio (solo se revisa que tenga formato de correo), para que las cuentas creadas antes con otro proveedor puedan entrar; el registro sigue pidiendo @gmail.com.
4. Escribir la **Contraseña**.
5. Hacer clic en **Iniciar Sesión**. Mientras se procesa la solicitud, el botón muestra "Ingresando...".

**Resultado esperado:** El sistema abre el panel del cliente (ruta */client*). El tema y el idioma guardados en la cuenta se aplican automáticamente.

**Observaciones o restricciones:**

- Si el correo o la contraseña son incorrectos, el sistema muestra "Correo o contraseña incorrectos" y no revela cuál de los dos falló.
- Después de cinco intentos fallidos en menos de 15 minutos, la cuenta queda bloqueada durante 15 minutos y el sistema muestra "Demasiados intentos fallidos. Tu cuenta quedó bloqueada 15 minutos; espera o usa "¿Olvidaste tu contraseña?" para entrar ya." Si el cliente restablece la contraseña, puede entrar de inmediato.
- La sesión dura una hora; al vencerse, el sistema vuelve a la pantalla de inicio de sesión con el mensaje "Tu sesión expiró. Inicia sesión de nuevo."
- Una cuenta desactivada no puede ingresar y recibe el mensaje "Tu cuenta está desactivada. Contacta al administrador."
- Si se hacen demasiados intentos seguidos desde el mismo equipo (más de 60 en un minuto entre inicio de sesión, registro y recuperación), el sistema responde "Hiciste demasiados intentos seguidos. Espera un momento y vuelve a intentarlo."

**Figura 4**

*Pantalla de Inicio de Sesión*

![Pantalla de Inicio de Sesión](capturas/cliente-04-pantalla-de-inicio-de-sesion.jpg)

*Nota.* Se usó un correo de prueba; no se muestran contraseñas reales.

### 8.3 Recuperación de la Contraseña

**Nombre del módulo:** Recuperación de la contraseña.

**Descripción:** Permite recuperar el acceso a la cuenta cuando se olvidó la contraseña, mediante un código de verificación de seis dígitos enviado por correo.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Pantalla de inicio de sesión > enlace **¿Olvidaste tu contraseña?** (ruta */auth/forgot-password*).

**Pasos para utilizarlo:**

1. En la pantalla de inicio de sesión, hacer clic en **¿Olvidaste tu contraseña?**.
2. **Paso 1 (Recuperar Contraseña):** escribir el correo de la cuenta y hacer clic en **Enviar Código**.
3. Abrir el correo recibido y copiar el código de seis dígitos. El código es válido durante 15 minutos.
4. **Paso 2 (Verificación):** escribir los seis dígitos, uno en cada casilla, y hacer clic en **Verificar Código**. Si el correo no llegó, hacer clic en **¿No recibiste el código? Reenviar**.
5. **Paso 3 (Nueva Contraseña):** escribir la nueva contraseña, que debe cumplir los cuatro requisitos, confirmarla y hacer clic en **Actualizar Contraseña**.
6. Cuando aparezca el mensaje "¡Contraseña actualizada!", hacer clic en **Ir a iniciar sesión** e ingresar con la nueva contraseña.

**Resultado esperado:** La contraseña se cambia, el código usado no puede volver a usarse y las sesiones abiertas en otros dispositivos se cierran.

**Observaciones o restricciones:** Por seguridad, el sistema muestra la misma pantalla esté o no registrado el correo, de modo que no revela qué cuentas existen. Cada código nuevo invalida el anterior ("Te enviamos un código nuevo. El anterior ya no sirve."). Después de cinco intentos incorrectos el código deja de funcionar y el sistema muestra "Demasiados intentos fallidos. Espera unos minutos o pide otro código para volver a intentarlo.": hay que solicitar uno nuevo con **Reenviar**. Un indicador de tres pasos en la parte superior muestra la etapa del proceso.

**Figura 5**

*Recuperación de la Contraseña, Paso 1: Correo Electrónico*

![Recuperación de la Contraseña, Paso 1: Correo Electrónico](capturas/cliente-05-recuperacion-de-la-contrasena-paso-1-correo-electronico.jpg)

**Figura 6**

*Recuperación de la Contraseña, Paso 2: Código de Verificación*

![Recuperación de la Contraseña, Paso 2: Código de Verificación](capturas/cliente-06-recuperacion-de-la-contrasena-paso-2-codigo-de-verificacion.jpg)

**Figura 7**

*Recuperación de la Contraseña, Paso 3: Nueva Contraseña*

![Recuperación de la Contraseña, Paso 3: Nueva Contraseña](capturas/cliente-07-recuperacion-de-la-contrasena-paso-3-nueva-contrasena.jpg)

**Figura 8**

*Confirmación del Cambio de Contraseña*

![Confirmación del Cambio de Contraseña](capturas/cliente-08-confirmacion-del-cambio-de-contrasena.jpg)

## 9. Menú Principal

Después de iniciar sesión, el sistema muestra un menú lateral con las opciones del cliente (Tabla 4). En la parte superior del menú aparecen las iniciales, el nombre y el correo del cliente; en la parte inferior está la opción **Cerrar Sesión**. En el teléfono, el menú se abre y se cierra con el ícono ☰. El cliente solo ve sus propias opciones: si intenta abrir una pantalla de otro rol, el sistema lo devuelve a su panel.

**Tabla 4**

*Opciones del Menú Principal del Cliente*

| Opción (ruta) | Para qué sirve | Sección |
|---|---|---|
| Dashboard (/client) | Muestra un resumen de reservas, vehículos, el próximo servicio, accesos rápidos, el progreso de fidelidad y los beneficios. | 10.1 |
| Perfil (/client/profile) | Permite ver y editar los datos personales, cambiar el correo y la contraseña, y desactivar la cuenta. | 10.9 |
| Mis vehículos (/client/vehicles) | Permite registrar, editar, retirar y ver los vehículos del cliente. | 10.2 |
| Reservar lavado (/client/reserve) | Permite programar un nuevo servicio de lavado y elegir la forma de pago. | 10.3 |
| Pagar servicio (/client/payment) | Permite pagar una reserva con código QR o en efectivo, canjear un cupón y cancelarla. | 10.5 |
| Notificaciones (/client/notifications) | Muestra los avisos de reservas, recordatorios, asignaciones, avance del servicio, pagos, puntos, cupones e informes de inspección. | 10.7 |
| Historial (/client/history) | Lista las reservas con el estado de su pago y permite pagarlas, cancelarlas y calificarlas. | 10.6 |
| Configuración (/client/configuration) | Permite elegir el tema, el idioma y las preferencias de notificación, y abrir el centro de ayuda y los documentos legales. | 10.10 |
| Cerrar Sesión | Termina la sesión de forma segura. | 12 |

*Nota.* Los nombres de las opciones son los del menú lateral cuando la interfaz está en español.

**Figura 9**

*Menú Lateral del Cliente*

![Menú Lateral del Cliente](capturas/cliente-09-menu-lateral-del-cliente.jpg)

## 10. Módulos del Sistema

Cada módulo se documenta con el formato de la guía de manuales: nombre, descripción, rol que puede usarlo, ruta o menú, pasos, captura de pantalla, resultado esperado y observaciones o restricciones.

### 10.1 Panel del Cliente

**Nombre del módulo:** Panel del cliente.

**Descripción:** Pantalla de inicio del cliente. Resume la actividad y ofrece atajos a las acciones más frecuentes.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Dashboard** (ruta */client*).

**Pasos para utilizarlo:**

1. Iniciar sesión; el sistema abre el panel automáticamente.
2. Revisar las tarjetas de resumen: **Reservas activas**, **Mis vehículos** y **Lavados realizados**.
3. Consultar el bloque **Tu próximo servicio**, que muestra el servicio, el vehículo, la fecha, la hora, el **Operario asignado** y el avance del servicio. El avance queda en 0 % mientras la reserva está programada o confirmada, porque el lavado aún no ha empezado; sube cuando el operario inicia el servicio y llega a 100 % al finalizar. Hacer clic en **Ver detalle** para ver toda la información.
4. Usar los **Accesos rápidos** (Agendar lavado, Pagar servicio, Mis vehículos y Notificaciones) para ir directamente a cada módulo.
5. Revisar el bloque **Tus beneficios y promociones**: la barra **Progreso de fidelidad** muestra los puntos acumulados ("N de M puntos") y, para cada promoción, indica "¡Ya la desbloqueaste! Cánjala al pagar." o cuántos puntos faltan para desbloquearla ("Te faltan N puntos para desbloquearla").

**Resultado esperado:** El cliente ve la información más importante y llega a cualquier módulo con un solo clic.

**Observaciones o restricciones:** Si no hay reservas, el bloque muestra "No tienes servicios programados" en lugar de una pantalla vacía. Los puntos se acreditan cuando el administrador aprueba el pago de una reserva (o registra un pago en el local), no cuando termina el servicio; hasta que se apruebe el primer pago, el bloque de beneficios muestra "Aún no tienes datos de fidelidad". Los puntos nunca se gastan: desbloquean cupones, y canjear un cupón no reduce el saldo.

**Figura 10**

*Panel del Cliente*

![Panel del Cliente](capturas/cliente-10-panel-del-cliente.jpg)

### 10.2 Mis Vehículos

**Nombre del módulo:** Mis vehículos.

**Descripción:** Permite registrar los vehículos del cliente y ver, para cada uno, el último lavado y el total de lavados. Se debe registrar un vehículo antes de poder reservar un servicio.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Mis vehículos** (ruta */client/vehicles*).

**Pasos para utilizarlo:**

1. Hacer clic en **Mis vehículos** en el menú lateral.
2. Revisar los indicadores de la parte superior: **Vehículos registrados**, **Lavados acumulados** y **Último lavado realizado**.
3. Para agregar un vehículo, hacer clic en **Registrar vehículo**.
4. En la ventana "Registrar nuevo vehículo", seleccionar el **Tipo de vehículo** (automóvil, sedán, camioneta SUV, pickup, camión o motocicleta) y escribir la **Marca**, el **Modelo**, la **Placa** y el **Color**.
5. Hacer clic en **Guardar vehículo** y verificar que el vehículo aparezca en la lista con el mensaje "¡Vehículo registrado!".
6. Para corregir los datos de un vehículo, usar la opción de editar de su tarjeta, cambiar los campos y hacer clic en **Guardar cambios**.
7. Para retirar un vehículo, usar la opción de eliminar de su tarjeta y confirmar en la ventana "¿Eliminar vehículo?".

**Resultado esperado:** El vehículo queda registrado y disponible para seleccionarlo al reservar un lavado.

**Observaciones o restricciones:**

- La placa debe ser una placa colombiana oficial: tres letras y tres números para automóviles y demás vehículos (por ejemplo, ABC123), o tres letras, dos números y una letra para motocicletas (por ejemplo, ABC12D). No se aceptan placas extranjeras ni promocionales.
- La marca es obligatoria (al menos dos caracteres) y el color solo admite letras. La misma placa no se puede registrar dos veces mientras el vehículo esté activo.
- El tipo de vehículo importa porque determina el precio de cada servicio.
- Si el vehículo tiene reservas activas o futuras, el sistema no lo retira y muestra "Este vehículo tiene reservas activas o futuras: cancélalas antes de retirarlo."

**Figura 11**

*Lista de Vehículos del Cliente*

![Lista de Vehículos del Cliente](capturas/cliente-11-lista-de-vehiculos-del-cliente.jpg)

**Figura 12**

*Formulario para Registrar un Vehículo*

![Formulario para Registrar un Vehículo](capturas/cliente-12-formulario-para-registrar-un-vehiculo.jpg)

### 10.3 Reservar un Lavado

**Nombre del módulo:** Reservar lavado.

**Descripción:** Permite programar un turno de lavado eligiendo el vehículo, el tipo de servicio, la fecha, la hora y la forma de pago. El sistema solo ofrece horas que están realmente libres.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Reservar lavado** (ruta */client/reserve*), o el acceso rápido **Agendar lavado** del panel.

**Pasos para utilizarlo:**

1. Hacer clic en **Reservar lavado**.
2. **Paso 1 (Elige tu vehículo):** seleccionar uno de los vehículos registrados. Si el vehículo no aparece, usar el enlace **Mis Vehículos** para registrarlo primero.
3. **Paso 2 (Tipo de servicio):** seleccionar Básico, Premium o Completo. Cada tarjeta muestra qué incluye, la duración, el precio para el tipo de vehículo elegido y los puntos de fidelización que suma ("Suma N puntos").
4. **Paso 3 (Fecha y hora):** elegir la fecha (el calendario no deja escoger días pasados) y luego una de las horas libres que aparecen en **Selecciona una hora**. Las horas se ofrecen cada 30 minutos, dentro del horario de atención, fuera del descanso y solo cuando hay una bahía de lavado libre durante toda la duración del servicio.
5. Si se desea, elegir en **Operario (opcional)** uno de los operarios libres a esa hora. Si se deja en "Sin preferencia: te asignamos uno libre", el sistema asigna uno automáticamente.
6. **Paso 4 (¿Cómo vas a pagar?):** elegir una de las dos opciones:
   - **Pago digital ahora:** Nequi, Daviplata o transferencia. Se escanea el QR y se sube el comprobante **dentro de los 30 minutos siguientes** (o antes de la hora del servicio, si es más pronto).
   - **Efectivo en el lavadero:** se paga al llegar y el administrador registra el pago, sin comprobante. Solo aparece si el lavadero tiene activa la cuenta de efectivo, y el cliente puede tener **como máximo una reserva activa en efectivo**.
7. Revisar el **Resumen de tu reserva**: vehículo, placa, servicio, duración estimada, fecha, hora, ubicación, total y **Puntos que ganas**. Mientras falte un dato, el botón de confirmar permanece deshabilitado y debajo aparece la lista de lo que falta.
8. Hacer clic en **Reservar Ahora**.

**Resultado esperado:** La reserva se crea de inmediato con el estado Confirmada y una bahía de lavado, y el cliente recibe la confirmación en sus notificaciones y por correo. Lo que sigue depende de la forma de pago:

- **Pago digital:** el sistema abre directamente la pantalla de pago de esa reserva con el aviso "Tienes un plazo para pagar: Sube el comprobante antes de las HH:mm o la reserva se libera." Si el comprobante no se sube a tiempo, la reserva se cancela sola con el motivo "No se subió el comprobante de pago a tiempo" y el horario queda libre para otros clientes.
- **Efectivo:** aparece "¡Reserva creada!" con el resumen y el mensaje "Tu lavado quedó programado. Pagas en efectivo en el lavadero."; el botón **Ver mis reservas** abre el historial. No hay nada que pagar en línea.

**Observaciones o restricciones:**

- Elegir el operario es opcional. Si el operario elegido ya no está libre al confirmar, el sistema asigna otro ("Si el operario que elegiste ya no está libre al confirmar, te asignamos otro."). El operario asignado se ve en el panel y en las notificaciones, y el administrador puede cambiarlo después.
- Las reservas se pueden hacer con hasta 60 días de anticipación.
- Si el día está cerrado, el sistema muestra "Ese día el lavadero está cerrado"; si no hay horas libres, "No hay horas libres ese día".
- Si otra persona toma la hora mientras el cliente está reservando, el sistema no guarda nada, muestra "Esa hora ya se ocupó: Te mostramos las horas libres más cercanas de ese día." y deja en **Selecciona una hora** solo las horas libres más cercanas de ese mismo día (hasta cinco) para que el cliente elija otra. Desde el 27 de octubre de 2026 esas horas también respetan las otras reservas del mismo vehículo: no se ofrece una hora que se cruce con otra reserva activa de ese vehículo, porque un vehículo no puede estar en dos bahías al mismo tiempo.
- Si la hora elegida no se puede reservar por otro motivo, el sistema explica cuál: "El lavadero no abre ese día. Elige otra fecha.", "A esa hora el servicio no cabe en el horario de atención. Elige otra hora." o "Esa hora ya pasó. Elige una hora más tarde."
- Un vehículo no puede tener dos reservas a la misma hora ("Ese vehículo ya tiene una reserva a esa hora.").
- El servicio se presta en el lavadero: el cliente debe llevar el vehículo en la fecha y la hora reservadas.
- Si el cliente ya tiene una reserva activa en efectivo (Programada, Confirmada o En progreso), no puede hacer otra en efectivo: el sistema muestra "Ya tienes una reserva activa con pago en efectivo. Págala o cancélala, o elige pago digital." El efectivo vuelve a estar disponible cuando esa reserva termina, se cancela o queda como No asistió (no basta con que el administrador registre el pago).
- Mientras la reserva con pago digital espera el comprobante, el Historial y el detalle muestran la insignia "Paga antes de las HH:mm". Una vez enviado el comprobante, la reserva ya no vence, aunque el administrador todavía no lo haya revisado.
- La forma de pago no cambia si el administrador reprograma la reserva. Las reservas hechas antes del 26 de octubre de 2026 no tienen forma de pago ni plazo.

**Figura 13**

*Reservar un Lavado, Pasos 1 y 2: Vehículo y Servicio*

![Reservar un Lavado, Pasos 1 y 2: Vehículo y Servicio](capturas/cliente-13-reservar-un-lavado-pasos-1-y-2-vehiculo-y-servicio.jpg)

**Figura 14**

*Reservar un Lavado, Pasos 3 y 4: Fecha, Hora y Forma de Pago*

![Reservar un Lavado, Pasos 3 y 4: Fecha, Hora y Forma de Pago](capturas/cliente-14-reservar-un-lavado-pasos-3-y-4-fecha-hora-y-forma-de-pago.jpg)

*Nota.* El paso "¿Cómo vas a pagar?" existe desde el 26 de octubre de 2026.

**Figura 15**

*Confirmación de una Reserva con Pago en Efectivo*

![Confirmación de una Reserva con Pago en Efectivo](capturas/cliente-15-confirmacion-de-una-reserva-con-pago-en-efectivo.jpg)

**Figura 16**

*Aviso "Esa Hora ya se Ocupó" con las Horas Alternativas*

![Aviso "Esa Hora ya se Ocupó" con las Horas Alternativas](capturas/cliente-15b-aviso-esa-hora-ya-se-ocupo-con-horas-alternativas.jpg)

*Nota.* Al cerrar el aviso, **Selecciona una hora** solo ofrece las alternativas. En la prueba del 27 de octubre de 2026 el vehículo ya tenía una reserva a las 10:00 y la hora pedida era las 09:30: para otro vehículo el sistema sugería 08:00, 08:30, 09:00, 10:30 y 11:00, y para este, 07:30, 08:00, 08:30, 09:00 y 11:00, porque las 10:30 se cruzan con su otra reserva.

### 10.4 Consultar o Cancelar una Reserva

**Nombre del módulo:** Seguimiento y cancelación de reservas.

**Descripción:** Permite consultar el estado de una reserva y cancelarla antes de que empiece el servicio.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Dashboard > **Tu próximo servicio** > **Ver detalle**; menú lateral > **Historial**; o menú lateral > **Pagar servicio**.

**Pasos para utilizarlo:**

1. Abrir el **Dashboard** o el **Historial** y ubicar la reserva.
2. En el Dashboard, hacer clic en **Ver detalle** para consultar el servicio, el vehículo, la fecha, la hora, el operario asignado y el estado. En el **Historial**, hacer clic en la tarjeta de la reserva para ver su detalle (sección 10.6).
3. Para cancelar, hacer clic en **Cancelar reserva** en la tarjeta de la reserva en **Historial** (o en la pantalla **Pagar servicio**).
4. Leer el mensaje "¿Cancelar reserva?" y hacer clic en **Sí, cancelar**; hacer clic en **Volver** para conservar la reserva.

**Resultado esperado:** La reserva cambia a Cancelada, aparece el mensaje "Reserva cancelada", el cliente recibe el aviso por correo y el turno queda libre para otros clientes.

**Observaciones o restricciones:** Una reserva solo se puede cancelar antes de su hora de inicio; después, el sistema muestra "Esta reserva ya no se puede cambiar." La cancelación no se puede deshacer. La aplicación no ofrece la opción de cambiar la fecha o la hora de una reserva: para moverla, el cliente la cancela y reserva de nuevo, o contacta al lavadero para que el administrador la reprograme. Si la reserva ya tenía un pago aprobado, el reembolso lo decide el administrador.

**Tabla 5**

*Estados de una Reserva*

| Estado | Significado | ¿El cliente puede cancelarla? |
|---|---|---|
| Programada | La reserva se registró y está pendiente de confirmación. | Sí, antes de la hora de inicio. |
| Confirmada | La reserva tiene turno y bahía de lavado. Las reservas que hace el cliente se crean con este estado. | Sí, antes de la hora de inicio. |
| En progreso | El operario inició el lavado. | No. |
| Completada | El servicio terminó; desde este estado se puede calificar. | No. |
| Cancelada | La reserva fue anulada por el cliente o por el lavadero, o el sistema la cerró porque pasó una hora de su fin sin confirmarse o porque no se subió el comprobante del pago digital a tiempo ("No se subió el comprobante de pago a tiempo"). | No aplica. |
| No asistió | El cliente no llegó al turno reservado y el lavadero lo registró así. Si la reserva confirmada nunca tuvo operario, el sistema la marca así una hora después de su hora de fin. | No aplica. |

*Nota.* Elaborado por los autores a partir de los estados de la reserva definidos en el sistema.

**Figura 17**

*Confirmación de la Cancelación de una Reserva*

![Confirmación de la Cancelación de una Reserva](capturas/cliente-16-confirmacion-de-la-cancelacion-de-una-reserva.jpg)

### 10.5 Pagar un Servicio

El pago se hace por transferencia a la cuenta del lavadero (escaneando su código QR o usando los datos de su cuenta) o en efectivo en el local. El sistema **no solicita ni guarda datos de tarjetas de crédito o débito** y no se conecta con los bancos: un administrador verifica manualmente cada pago digital (Tabla 6).

La forma de pago se elige al reservar (sección 10.3), y la pantalla **Pagar servicio** la respeta: en una reserva con **pago digital** no se ofrece el efectivo y se muestra el plazo ("Sube el comprobante antes de las HH:mm o la reserva se libera."); en una reserva **en efectivo** el método Efectivo aparece preseleccionado con el aviso "Pagas en efectivo en el lavadero: El administrador registra el pago cuando llegues; no necesitas subir comprobante.", y en el Historial esa reserva muestra "Pagas en efectivo en el lavadero" en lugar de **Pagar ahora**.

**Tabla 6**

*Métodos de Pago Disponibles*

| Método | Cómo se paga | ¿Requiere comprobante? |
|---|---|---|
| Nequi | Se escanea el código QR y se hace la transferencia desde la aplicación. | Sí |
| Daviplata | Se escanea el código QR y se hace la transferencia desde la aplicación. | Sí |
| Transferencia | Se escanea el código QR o se usan los datos de la cuenta que se muestran. | Sí |
| Efectivo | Se paga en el lavadero al recibir el servicio. No se muestra código QR. Solo aparece si el lavadero tiene activa la cuenta de efectivo. | No; el administrador registra el pago en el local. |

*Nota.* Los métodos que se muestran, incluido Efectivo, dependen de las cuentas de pago que el administrador mantenga activas.

**Nombre del módulo:** Pagar servicio.

**Descripción:** Permite pagar una reserva adjuntando el comprobante del pago digital, o confirmar que se pagará en efectivo, con la opción de canjear un cupón de descuento.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Pagar servicio** (ruta */client/payment*); se abre sola después de reservar con pago digital, y también con **Pagar ahora** en el Historial.

**Pasos para utilizarlo:**

1. Abrir **Pagar servicio** y revisar el **Resumen de la reserva**: servicio, vehículo, **Subtotal**, **Descuento aplicado** y **TOTAL A PAGAR** (IVA del 19 % incluido). En una reserva con pago digital, revisar también la hora límite del aviso de plazo.
2. Si se tiene un cupón desbloqueado, escribirlo en **Código del cupón** y hacer clic en **Aplicar**. Aparece el mensaje "Cupón aplicado: [nombre]" y el descuento se resta del total a pagar. Para quitarlo antes de reportar el pago, hacer clic en la **X** junto al cupón: el sistema anula el canje y el total vuelve a ser el de la reserva. El **TOTAL A PAGAR** lo calcula el servidor: si se recarga la página o se vuelve a entrar después de aplicar el cupón, la pantalla sigue mostrando el cupón, el descuento y la **X**.
3. En **Selecciona tu método de pago**, elegir Nequi, Daviplata o Transferencia (en una reserva en efectivo, Efectivo ya viene seleccionado).
4. Escanear el código QR con la aplicación bancaria o la billetera, o copiar el número que aparece en **Llave / Cuenta autorizada** con **Copiar número**, y transferir exactamente el **Monto a pagar**. Si el código es difícil de leer, **Ampliar QR** lo muestra más grande, con **Acercar** y **Alejar** (también con la rueda del mouse o con doble clic).
5. En **Confirma tu pago**, arrastrar la imagen del comprobante o hacer clic para buscarla (JPG o PNG de hasta 10 MB). La página la reduce a menos de 2 MB antes de enviarla; mientras tanto muestra "Preparando el comprobante...".
6. Escribir la **Referencia de la transacción** impresa en el comprobante (de 8 a 12 letras o números emitidos por el banco) y revisar el **Monto pagado**, que viene lleno con el total a pagar y debe coincidir con lo transferido (mayor que 0).
7. Hacer clic en **Ya realicé el pago**. Mientras falten datos, el botón permanece deshabilitado y la lista "Para confirmar el pago te falta:" indica lo pendiente (comprobante, referencia o monto).
8. En una reserva en efectivo, la pantalla muestra **Efectivo** seleccionado, el bloque **Pago en efectivo** ("Pagarás directamente en el local cuando recibas tu vehículo. Tu reserva queda apartada.") y el botón **Confirmar reserva**, que solo muestra el aviso "Pagas en el local: Paga en efectivo cuando llegues por tu vehículo. El administrador registrará el pago y te llegará la notificación de pago aprobado." No hay que subir comprobante: se lleva el dinero el día del servicio.

**Resultado esperado:** El sistema muestra "¡Pago enviado!" y el pago queda en revisión ("Pago en verificación") hasta que un administrador lo apruebe o lo rechace. El cliente recibe una notificación y un correo con el resultado.

**Observaciones o restricciones:**

- El monto a pagar siempre lo calcula el sistema (total de la reserva menos el cupón aplicado, si lo hay); el cliente no puede cambiarlo. El administrador compara el monto pagado que declara el cliente con el comprobante y con el banco. Si el sistema no logra consultar el total, muestra "—" y el aviso "No pudimos consultar el total a pagar. Revisa tu conexión y vuelve a entrar en unos segundos."; en ese caso no conviene pagar hasta que aparezca el monto.
- Solo se pueden pagar las reservas en estado Confirmada, En progreso o Completada.
- Mientras un pago está en revisión no se puede reportar otro para la misma reserva ("Ya hay un pago en revisión para esta reserva."); una reserva admite un solo pago aprobado. Una referencia que ya se usó en otro pago se rechaza ("Esa referencia ya se usó en otro pago. Revisa el número de tu comprobante.").
- Si la reserva cambia después de reportar el pago (por ejemplo, el administrador le cambia los servicios), el administrador rechaza el pago con el monto viejo y el cliente lo reporta de nuevo con el monto nuevo que muestra la pantalla. Si el administrador rechaza el pago por otro motivo, el cliente puede reportarlo de nuevo con un comprobante correcto.
- Una reserva con pago digital tiene plazo para subir el comprobante: 30 minutos desde que se hizo, o la hora del servicio si es más pronto. Si se vence, la reserva se cancela sola y ya no se puede pagar ("Esta reserva ya no se puede pagar (está cancelada, pagada o en revisión)."): hay que reservar de nuevo.
- Si la imagen no se puede reducir a menos de 2 MB, aparece "No pudimos reducir la imagen a menos de 2 MB. Prueba con una captura de pantalla del comprobante."
- Cuando el pago se aprueba, el cliente recibe el aviso de pago confirmado (bandeja y correo), los puntos de fidelización de la reserva ("Ganaste N puntos", solo en la bandeja) y, por cada cupón que esos puntos desbloquean, el aviso "¡Tienes un cupón canjeable!" con su código (bandeja y correo). Si el pago se rechaza o se reembolsa, el cliente recibe un aviso y un correo; un reembolso revierte los puntos ganados por esa reserva.
- Con el pago aprobado, la pantalla **Pagar servicio** de esa reserva solo muestra el resumen: desaparecen el método de pago, el código QR, la carga del comprobante y el botón **Ya realicé el pago**, y el cupón usado ya no se puede quitar.
- Un cupón se acepta solo si está activo, está dentro de sus fechas y el cliente alcanzó los puntos que exige; solo se acepta un cupón por reserva ("Esta reserva ya tiene un cupón. Quítalo si quieres usar otro."). Un cupón de porcentaje se recalcula si cambia el total de la reserva; uno de descuento fijo se muestra en pesos. El sistema responde "Ese cupón no existe.", "Ese cupón no está vigente o todavía no acumulas los puntos necesarios." o "Ya canjeaste este cupón en esta reserva." Un canje exitoso envía un aviso y un correo de confirmación.

**Tabla 7**

*Estados de un Pago*

| Estado | Significado | Qué debe hacer el cliente |
|---|---|---|
| Pendiente | La reserva todavía no tiene un pago aprobado. | Hacer el pago (o llevar el efectivo el día del servicio). |
| En revisión | Se envió el comprobante y espera la verificación del administrador. | Esperar la verificación. |
| Aprobado | El administrador verificó el pago. | Ninguna acción. |
| Rechazado | No se pudo verificar el pago; el administrador registra el motivo. | Reportar de nuevo el pago con un comprobante correcto. |
| Reembolsado | El valor del pago se devolvió al cliente. | Ninguna acción. |

*Nota.* En las tarjetas del Historial estos estados se muestran como Pendiente de verificación, En revisión, Pagado, Rechazado y Reembolsado.

**Figura 18**

*Pantalla de Pago con el Plazo para Subir el Comprobante*

![Pantalla de Pago con el Plazo para Subir el Comprobante](capturas/cliente-17-pantalla-de-pago-con-el-plazo-para-subir-el-comprobante.jpg)

*Nota.* Se abre sola después de reservar con pago digital.

**Figura 19**

*Pantalla de Pago de una Reserva en Efectivo*

![Pantalla de Pago de una Reserva en Efectivo](capturas/cliente-18-seleccion-del-metodo-de-pago-y-resumen-de-la-reserva.jpg)

**Figura 20**

*Método de Pago, Código QR y Carga del Comprobante*

![Método de Pago, Código QR y Carga del Comprobante](capturas/cliente-19-codigo-qr-y-carga-del-comprobante.jpg)

*Nota.* Se usó un código QR de prueba; no se muestran cuentas bancarias reales.

### 10.6 Historial y Calificación

**Nombre del módulo:** Historial.

**Descripción:** Lista las reservas del cliente con su estado y el estado de su pago, y permite pagarlas, cancelarlas y calificarlas.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Historial** (ruta */client/history*).

**Pasos para utilizarlo:**

1. Hacer clic en **Historial**. Las reservas aparecen de la más reciente a la más antigua.
2. Usar los filtros **Todas**, **Activas**, **Completadas** y **Canceladas**, y las fechas **Desde** y **Hasta**, para acotar la lista.
3. Usar **Buscar por servicio, vehículo o código** para encontrar una reserva.
4. En cada tarjeta, revisar el **Tipo de servicio**, el **Vehículo**, el **Estado** y el estado del **Pago**, y usar **Pagar ahora** o **Cancelar reserva** cuando estén disponibles. **Pagar ahora** aparece solo cuando la reserva se puede pagar (estado pagable, sin un pago aprobado y sin uno pendiente o en revisión); en una reserva en efectivo aparece "Pagas en efectivo en el lavadero" y, en una con pago digital pendiente, la insignia "Paga antes de las HH:mm".
5. Hacer clic en la tarjeta para abrir el **Detalle de la reserva** (servicios, fecha y hora, vehículo, total, estado del pago y calificación). El botón **Ir al pago** lleva a la pantalla de pago de esa reserva; no aparece en las reservas canceladas o en las que el cliente no asistió.
6. En un servicio completado, hacer clic en **Calificar Servicio**.
7. Elegir de una a cinco estrellas, escribir un comentario si se desea (máximo 500 caracteres) y hacer clic en **Enviar calificación**.

**Resultado esperado:** Aparece el mensaje "¡Gracias por tu calificación!", la tarjeta muestra **Calificado** y el operario que realizó el servicio puede ver la calificación.

**Observaciones o restricciones:** La calificación es opcional, se permite una sola vez por reserva y solo cuando la reserva está Completada; después no se puede modificar. Si no hay reservas para los filtros elegidos, el sistema muestra "No se encontraron servicios".

**Figura 21**

*Historial con Filtros*

![Historial con Filtros](capturas/cliente-20-historial-con-filtros.jpg)

**Figura 22**

*Calificación de un Servicio Completado*

![Calificación de un Servicio Completado](capturas/cliente-21-calificacion-de-un-servicio-completado.jpg)

### 10.7 Notificaciones

**Nombre del módulo:** Notificaciones.

**Descripción:** Reúne los avisos del sistema (reserva creada, confirmada o cancelada, recordatorios, operario asignado, servicio iniciado y completado, pago confirmado, rechazado o reembolsado, puntos ganados, cupón desbloqueado o canjeado, e informe de inspección del vehículo) e indica cuáles se han leído.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Notificaciones** (ruta */client/notifications*), o el acceso rápido del panel.

**Pasos para utilizarlo:**

1. Hacer clic en **Notificaciones**. El encabezado muestra cuántas notificaciones no se han leído.
2. Usar las pestañas **Todas**, **Recordatorios**, **Promociones**, **Confirmaciones** y **Otras** para filtrar por tipo.
3. Usar el filtro **Estado de lectura** y las fechas para acotar la lista.
4. Hacer clic en una notificación (o en **Ver detalle**) para leerla completa; queda marcada como leída. Si el aviso pertenece a un apartado, el detalle trae un botón para ir allí: **Ir al historial** (reservas, pagos, servicio e informe de inspección) o **Ir al inicio** (bienvenida, puntos y cupones).
5. Usar **Marcar leído** o **Marcar no leído** en cada notificación, o **Marcar todo como leído**.
6. Para eliminar una notificación, usar la opción de eliminar y confirmar "¿Eliminar esta notificación?".

**Resultado esperado:** El cliente se mantiene informado sin tener que revisar cada módulo constantemente.

**Observaciones o restricciones:** El sistema envía recordatorios 24 horas y 1 hora antes de cada reserva. Los avisos que cambian algo para el cliente también llegan por correo: bienvenida, reserva creada, confirmada o cancelada y recordatorios, pago confirmado, rechazado o reembolsado, cupón canjeado o desbloqueado, e informe de inspección. El avance del servicio (operario asignado, iniciado y completado) y los puntos ganados llegan solo a la bandeja y como notificación push. El código de recuperación de la contraseña siempre llega por correo. Los interruptores de **Configuración > Notificaciones** deciden solo los canales externos; la bandeja siempre recibe todos los avisos. Una notificación eliminada no se puede recuperar.

**Figura 23**

*Centro de Notificaciones del Cliente*

![Centro de Notificaciones del Cliente](capturas/cliente-22-centro-de-notificaciones-del-cliente.jpg)

### 10.8 Reporte de Inspección del Vehículo

**Nombre del módulo:** Reporte de tu vehículo (informe de inspección).

**Descripción:** Página de solo lectura en la que el cliente ve cómo recibió y cómo entregó el lavadero su vehículo, por fases (Recepción, Exterior, Interior, Motor y Entrega), con las notas y las fotografías de cada hallazgo.

**Rol que puede usarlo:** Cliente que recibió el enlace. No hace falta iniciar sesión: el enlace es personal y solo lo conoce quien lo recibe.

**Ruta o menú donde se encuentra:** Enlace del aviso "Informe de inspección" que llega a **Notificaciones** y al correo (ruta */report/* seguida de un código único).

**Pasos para utilizarlo:**

1. Abrir la notificación o el correo del informe de inspección y hacer clic en el enlace.
2. Leer el encabezado **Reporte de tu vehículo**, con el subtítulo "Así va el servicio de tu vehículo en el lavadero", el avance ("N de M fases terminadas") y la fecha de la última actualización ("Actualizado: ...").
3. Revisar cada fase con su estado (**Pendiente**, **En curso** o **Terminada**) y la hora de inicio y de fin. Una fase sin hallazgos muestra "Sin novedades en esta fase".
4. En cada hallazgo, leer la parte del vehículo, el tipo (**Observación**, **Daño leve** o **Daño importante**), la nota y las fotografías.
5. Volver a abrir el enlace más tarde para ver el avance: la página muestra los cambios que el lavadero publique.

**Resultado esperado:** El cliente conoce el estado de su vehículo antes y después del servicio, con evidencia fotográfica.

**Observaciones o restricciones:** El informe solo existe en los servicios de tres horas o más y solo si el lavadero decide elaborarlo y publicarlo. Si el enlace no es válido o el informe todavía no se publicó, la página muestra "Este reporte no está disponible: El enlace no es válido o el reporte todavía no se ha publicado. Comunícate con el lavadero si crees que es un error." Las fotos y notas las registra el lavadero; el cliente no puede modificarlas. Para cualquier inconformidad, el cliente debe comunicarse con el lavadero (Términos y Condiciones, apartado 8).

### 10.9 Perfil

**Nombre del módulo:** Perfil.

**Descripción:** Permite ver y actualizar los datos personales, cambiar el correo y la contraseña, y desactivar la cuenta.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Perfil** (ruta */client/profile*).

**Pasos para utilizarlo:**

1. Hacer clic en **Perfil**. La pantalla "Mi Perfil" se abre en modo de consulta con los nombres, apellidos, correo, teléfono, dirección y la fecha desde la cual la persona es cliente.
2. Para modificar los datos, hacer clic en **Editar perfil**, cambiar los campos y hacer clic en **Guardar**; aparece el mensaje "¡Perfil actualizado!".
3. Para cambiar el correo, que es el usuario de ingreso, escribir el nuevo y confirmar con la contraseña actual en la ventana "Confirma el cambio de correo"; desde ese momento se ingresa con el nuevo correo.
4. Para cambiar la contraseña, ir a **Contraseña** y hacer clic en **Cambiar**. Escribir la **Contraseña actual**, la **Nueva contraseña** y **Confirmar nueva contraseña**, y hacer clic en **Guardar**.
5. Para dejar de usar la cuenta, ir a **Cuenta**, hacer clic en **Desactivar mi cuenta**, leer el mensaje y confirmar con la contraseña.

**Resultado esperado:** Los datos quedan actualizados. Si se cambió la contraseña, la nueva se usa en el siguiente ingreso.

**Observaciones o restricciones:** Si la contraseña actual es incorrecta, el cambio se rechaza ("La contraseña actual es incorrecta"); la nueva contraseña debe ser distinta de la actual. La cuenta se desactiva, no se borra: el cliente ya no puede ingresar y sus sesiones se cierran, pero se conserva el historial de reservas y pagos. Para reactivarla, debe contactar al administrador. La **Dirección** se guarda en la cuenta, por lo que aparece igual en cualquier dispositivo.

**Figura 24**

*Perfil del Cliente*

![Perfil del Cliente](capturas/cliente-23-perfil-del-cliente.jpg)

### 10.10 Configuración

**Nombre del módulo:** Configuración.

**Descripción:** Permite personalizar la apariencia, el idioma y las preferencias de notificación, y abrir el centro de ayuda y los documentos legales.

**Rol que puede usarlo:** Cliente.

**Ruta o menú donde se encuentra:** Menú lateral > **Configuración** (ruta */client/configuration*).

**Pasos para utilizarlo:**

1. Hacer clic en **Configuración**.
2. En **Apariencia**, hacer clic en **Elegir tema** y seleccionar uno de los cuatro temas de color. El cambio se aplica de inmediato.
3. En **Idiomas**, seleccionar Español, English, Français o Português. Todos los textos de la interfaz cambian al idioma elegido.
4. En **Notificaciones**, activar o desactivar **Notificaciones push en el celular**, **Recordatorios por Email** (recordatorios de la reserva por correo, un día y una hora antes) y **Promociones y Ofertas** (avisos de los cupones que se desbloquean con los puntos, por correo y push).
5. En **Ayuda**, hacer clic en **Abrir** junto a **Centro de ayuda** para ver la línea de atención, el correo, el horario de atención y la dirección, tomados de los datos del lavadero registrados en el sistema.
6. En la misma tarjeta **Ayuda**, abrir **Ver Términos y Condiciones** o **Ver Política de Datos**.

**Resultado esperado:** El tema, el idioma y los interruptores de notificación se aplican de inmediato y se guardan en la cuenta, de modo que se conservan en el siguiente ingreso y en cualquier dispositivo.

**Observaciones o restricciones:** Desactivar **Notificaciones push en el celular** detiene los avisos al teléfono; desactivar **Recordatorios por Email** detiene solo los correos de recordatorio; desactivar **Promociones y Ofertas** detiene los avisos por correo y push de los cupones desbloqueados. Ningún interruptor afecta la bandeja. Si el cambio no se puede guardar, el sistema muestra "No se pudieron guardar tus preferencias de notificación. Inténtalo de nuevo." Las fechas se muestran en el formato del idioma seleccionado.

**Figura 25**

*Pantalla de Configuración del Cliente*

![Pantalla de Configuración del Cliente](capturas/cliente-24-pantalla-de-configuracion-del-cliente.jpg)

### 10.11 Aplicación Móvil Android

La aplicación **LavaRapidoVehicular** (versión 1.1.1, APK *versionCode* 6, publicada el 26 de octubre de 2026; el 27 de octubre recibió por internet la nota del paso de reservar corregida, *v1.1.2* del repositorio, sin APK nuevo) ofrece al cliente las mismas funciones que la web, con las mismas reglas y los mismos datos: una reserva hecha en la app aparece en la web y al revés. Esta sección explica cómo instalarla y en qué cambia su uso; las reglas de cada módulo son las de las secciones 10.1 a 10.10.

**Instalación.** La app no está en Google Play: se instala desde la web del lavadero en un teléfono Android con conexión a internet.

1. En la pantalla de bienvenida de la web, tocar el botón de descarga de la app o escanear el código QR (el archivo es */app/LavaRapidoVehicular.apk*).
2. Abrir el archivo descargado y tocar **Instalar**. La primera vez, Android pide permitir que el navegador instale aplicaciones.
3. Abrir **LavaRapidoVehicular**. Si había una versión anterior al cambio de paquete del 16 de octubre de 2026, desinstalarla una vez; las siguientes se instalan encima.

**Actualizaciones.** Si el lavadero publica un APK nuevo, al abrir la app aparece la pantalla **Actualiza la app para continuar** con el botón **Descargar**; se instala encima sin perder la cuenta ni los datos, y hasta instalarla no se puede seguir. Los cambios de pantallas llegan solos por internet: al abrir la app aparece **Actualización lista**; **Reiniciar ahora** los muestra de inmediato y, si no, se aplican la próxima vez que se abra.

**Ingreso y registro.** La pantalla de ingreso pide el correo y la contraseña con las mismas reglas de la sección 8. Desde ella se crea la cuenta de cliente (los mismos datos y la aceptación de los dos documentos legales de la sección 8.1) y se recupera la contraseña con el código de 6 dígitos (sección 8.3); si el código se escribe mal cinco veces, la app ofrece **Pedir otro código**. La sesión se guarda cifrada en el teléfono y dura una hora.

**Navegación.** Abajo hay una barra con **Inicio**, **Reservar**, **Historial** y **Avisos**; el botón **Más** abre **Mis vehículos**, **Perfil**, **Pagar servicio**, **Configuración** y **Cerrar Sesión**.

- **Inicio:** saludo, tarjetas de **Mis reservas activas**, **Mis vehículos**, **Lavados realizados** y **Notificaciones**, el bloque **Tu próximo servicio** (estado, operario y avance), los **Accesos rápidos** y **Tus beneficios y promociones** (sección 10.1).
- **Reservar:** los pasos en una sola pantalla: **Elige tu vehículo**, **Tipo de servicio** (con la duración estimada y los puntos que suma), **Fecha y hora** (los días cerrados y las horas ocupadas no se ofrecen), **Operario (opcional)**, con **Sin preferencia** por defecto, y **¿Cómo vas a pagar?** (pago digital ahora o efectivo en el lavadero). Con pago digital, **Reservar Ahora** lleva a pagar con el plazo; con efectivo, muestra el resumen y que se paga en el lavadero (sección 10.3).
- **Pagar servicio:** se elige la cuenta del lavadero (o **Pago en efectivo**, si la reserva es en efectivo), se amplía el QR con **Ampliar QR**, se aplica un cupón (y se quita con la **X**), se adjunta el comprobante, que la app reduce a menos de 2 MB, y se escribe la referencia de la transacción. Al enviar aparece **¡Pago enviado!**; cuando el administrador lo aprueba, la pantalla se actualiza sola con la notificación push. Desde esta pantalla también se cancela la reserva (sección 10.5).
- **Historial:** filtros **Todos**, **Activas**, **Completadas** y **Canceladas**, búsqueda por servicio, vehículo o código, y el detalle de cada reserva con **Pagar ahora**, **Cancelar reserva** y **Calificar Servicio** (sección 10.6).
- **Mis vehículos**, **Avisos**, **Perfil** y **Configuración** funcionan como en las secciones 10.2, 10.7, 10.9 y 10.10.

**Figura 26**

*Ingreso a la Aplicación Móvil*

![Ingreso a la Aplicación Móvil](capturas/cliente-26-app-movil-ingreso.jpg)

**Figura 27**

*Inicio del Cliente en la Aplicación Móvil*

![Inicio del Cliente en la Aplicación Móvil](capturas/cliente-27-app-movil-inicio.jpg)

**Figura 28**

*Reservar en la Aplicación Móvil: Vehículo y Servicio*

![Reservar en la Aplicación Móvil: Vehículo y Servicio](capturas/cliente-28-app-movil-reservar-vehiculo-y-servicio.jpg)

**Figura 29**

*Reservar en la Aplicación Móvil: Forma de Pago y Resumen*

![Reservar en la Aplicación Móvil: Forma de Pago y Resumen](capturas/cliente-29-app-movil-reservar-hora-y-forma-de-pago.jpg)

*Nota.* Pantalla desplazada hasta el paso 4, con **Pago digital ahora** elegido. Bajo **Reservar Ahora** se ve la nota de la app *v1.1.2*: "Te esperamos en nuestra sede. Llega unos minutos antes de tu hora...".

**Figura 30**

*Pago con el Plazo en la Aplicación Móvil*

![Pago con el Plazo en la Aplicación Móvil](capturas/cliente-30-app-movil-pagar-servicio-con-plazo.jpg)

*Nota.* Se abre sola después de **Reservar Ahora** con pago digital; en la app la pantalla se titula **Pasarela de Pagos**.

**Figura 31**

*Historial en la Aplicación Móvil*

![Historial en la Aplicación Móvil](capturas/cliente-31-app-movil-historial.jpg)

**Figura 32**

*Menú Más del Cliente en la Aplicación Móvil*

![Menú Más del Cliente en la Aplicación Móvil](capturas/cliente-32-app-movil-menu-mas.jpg)

**Notificaciones push.** Al ingresar, la app pide permiso para enviar notificaciones. Si se acepta, el teléfono recibe por push los mismos avisos de la bandeja (recordatorios 24 horas y 1 hora antes, operario asignado, servicio iniciado y terminado, pagos, puntos y cupones, entre otros), y tocar uno abre la pantalla relacionada. Android los agrupa en los canales **Recordatorios**, **Promociones y puntos**, **Confirmaciones**, **Cancelaciones**, **Mensajes del servicio** y **Sistema**, que se pueden silenciar desde los ajustes del teléfono. La app muestra el motivo exacto de los errores del servidor en los cuatro idiomas.

**Cierre de sesión en la app.** Abrir **Más** y tocar **Cerrar Sesión**, que pide confirmación; al cerrar, el teléfono deja de recibir las notificaciones de esa cuenta. Limitación conocida, registrada en el documento de Acciones Correctivas (ACO-39): si la sesión vence mientras la app está en segundo plano, el teléfono puede seguir recibiendo las notificaciones de esa cuenta hasta que se vuelva a iniciar sesión o se cierre la sesión desde **Más**.

## 11. Mensajes y Validaciones

El sistema valida la información mientras el cliente escribe. Cuando un dato no es correcto, resalta el campo con un borde rojo y muestra un mensaje debajo. Las Tablas 8 y 9 explican el significado de los mensajes más frecuentes y lo que debe hacer el cliente.

**Tabla 8**

*Mensajes de los Formularios de Registro, Ingreso y Recuperación*

| Mensaje del sistema | Significado | Acción del cliente |
|---|---|---|
| El correo es obligatorio | El campo de correo está vacío. | Escribir el correo. |
| Debe ingresar un correo válido | En el inicio de sesión o la recuperación, el correo no tiene formato de correo. | Corregir el correo. |
| Solo se permiten correos @gmail.com | En el registro se escribió un correo de otro proveedor. | Usar una cuenta de Gmail. |
| El documento es obligatorio / Debe tener entre 5 y 20 dígitos | El número de documento está vacío o tiene una longitud no válida. | Escribir el documento solo con dígitos. |
| El nombre es obligatorio / Debe tener mínimo 3 caracteres | Los nombres están vacíos o son demasiado cortos. | Escribir los nombres. |
| Los apellidos son obligatorios / Debe tener mínimo 2 caracteres | Los apellidos están vacíos o son demasiado cortos. | Escribir los apellidos. |
| El teléfono es obligatorio | El campo de teléfono está vacío. | Escribir el número celular. |
| Debe comenzar con 3 (número celular colombiano) | El número no empieza por 3. | Escribir un número celular colombiano. |
| El número debe tener 10 dígitos | El teléfono tiene menos de 10 dígitos. | Completar los 10 dígitos. |
| Las contraseñas no coinciden | La confirmación es diferente de la contraseña. | Escribir la misma contraseña en ambos campos. |
| La contraseña no cumple los requisitos de seguridad | Falta uno de los cuatro requisitos. | Seguir la lista de requisitos. |
| Debes aceptar los Términos y la Política de Datos para continuar | No se aceptó uno de los dos documentos legales. | Leer y aceptar ambos documentos. |
| Debes aceptar el tratamiento de datos personales (Ley 1581) para registrarte. | El servidor recibió el registro sin las dos aceptaciones. | Marcar las dos casillas y registrarse de nuevo. |
| Hiciste demasiados intentos seguidos. Espera un momento y vuelve a intentarlo. | Se hicieron más de 60 intentos en un minuto desde el mismo equipo. | Esperar un minuto e intentar de nuevo. |
| Demasiados intentos fallidos. Espera unos minutos o pide otro código para volver a intentarlo. | Se escribió mal cinco veces el código de recuperación o la contraseña actual. | Pedir otro código; si fue la contraseña actual, esperar 15 minutos. |
| Ya existe una cuenta con este correo | El correo ya está registrado. | Iniciar sesión o recuperar la contraseña. |
| Ya existe una cuenta con este número de documento | El número de documento ya está registrado. | Verificar los datos o contactar al lavadero. |
| Correo o contraseña incorrectos | Las credenciales no coinciden con ninguna cuenta. | Revisar los datos e intentar de nuevo, o recuperar la contraseña. |
| Demasiados intentos fallidos. Tu cuenta quedó bloqueada 15 minutos... | Se escribió mal la contraseña cinco veces en menos de 15 minutos. | Esperar 15 minutos o usar ¿Olvidaste tu contraseña?. |
| Tu cuenta está desactivada. Contacta al administrador. | La cuenta fue desactivada. | Pedir al administrador que la reactive. |
| El código es incorrecto, ya se usó o expiró. Solicita uno nuevo. | El código de recuperación no es válido. | Solicitar un código nuevo. |
| ¡Contraseña actualizada! | La contraseña se cambió correctamente. | Ingresar con la nueva contraseña. |

*Nota.* Textos tomados del archivo de traducción al español de la aplicación web.

**Tabla 9**

*Mensajes de Vehículos, Reservas, Pagos y Calificaciones*

| Mensaje o situación | Significado | Acción del cliente |
|---|---|---|
| Tu sesión expiró. Inicia sesión de nuevo. | Terminó la sesión de una hora, o la sesión se cerró (por ejemplo, porque el administrador desactivó la cuenta). | Iniciar sesión de nuevo. |
| No tienes permiso para realizar esta acción | La acción pertenece a otro rol o a datos de otra persona. | Usar solo las opciones del cliente. |
| Tu perfil de cliente aún no está listo. Intenta de nuevo en unos minutos. | El perfil de cliente se está creando después del registro. | Esperar un momento e intentar de nuevo. |
| Ya existe un vehículo registrado con esta placa | La placa pertenece a un vehículo activo. | Verificar la placa. |
| El formato de la placa no corresponde al tipo de vehículo | La placa no sigue el formato oficial del tipo elegido. | Corregir la placa o el tipo. |
| Este vehículo tiene reservas activas o futuras: cancélalas antes de retirarlo. | Se intentó retirar un vehículo con reservas pendientes. | Cancelar o esperar a que terminen esas reservas. |
| Esa hora ya se ocupó | El turno se tomó mientras se reservaba. | Elegir una de las horas sugeridas. |
| El lavadero no abre ese día. Elige otra fecha. | El día elegido está marcado como cerrado. | Elegir otra fecha. |
| A esa hora el servicio no cabe en el horario de atención. Elige otra hora. | El servicio terminaría después del cierre o cruza el descanso. | Elegir otra de las horas libres. |
| Esa hora ya pasó. Elige una hora más tarde. | La hora elegida ya pasó. | Elegir una hora más tarde. |
| Ese vehículo ya tiene una reserva a esa hora. | El vehículo ya está reservado en ese turno. | Elegir otra hora. |
| La fecha no es válida para reservar. | La fecha ya pasó o está fuera de la ventana de reservas. | Elegir una fecha válida. |
| Esta reserva ya no se puede cambiar. | La reserva ya empezó o se cerró. | Contactar al lavadero. |
| Elige cómo vas a pagar: pago digital o efectivo en el lavadero. | Se intentó reservar sin elegir la forma de pago. | Elegir una opción en **¿Cómo vas a pagar?**. |
| Ya tienes una reserva activa con pago en efectivo. Págala o cancélala, o elige pago digital. | El cliente ya tiene una reserva activa en efectivo (máximo una). | Elegir pago digital, o esperar a que esa reserva termine o cancelarla. |
| Sube el comprobante antes de las HH:mm o la reserva se libera. / Paga antes de las HH:mm | La reserva con pago digital tiene un plazo para subir el comprobante (30 minutos o la hora del servicio). | Pagar y subir el comprobante antes de esa hora. |
| Pagas en efectivo en el lavadero | La reserva se paga en efectivo al llegar; no hay que subir comprobante. | Llevar el dinero el día del servicio. |
| No se subió el comprobante de pago a tiempo | Motivo con el que el sistema canceló una reserva con pago digital cuyo plazo venció. | Reservar de nuevo. |
| Esta reserva ya no se puede pagar (está cancelada, pagada o en revisión). | Se intentó pagar una reserva cancelada o que ya tiene un pago. | Revisar el estado en el Historial. |
| Archivo no válido | El comprobante no es una imagen JPG o PNG de hasta 10 MB. | Adjuntar una imagen válida. |
| No pudimos reducir la imagen a menos de 2 MB. Prueba con una captura de pantalla del comprobante. | La foto del comprobante es demasiado pesada aun después de reducirla. | Adjuntar una captura de pantalla del comprobante. |
| Para confirmar el pago te falta: ... | Faltan datos del pago (comprobante, referencia o monto). | Completar los elementos listados bajo el botón. |
| Ese cupón no existe. | El código del cupón es incorrecto. | Revisar el código que aparece en el aviso del cupón. |
| Ese cupón no está vigente o todavía no acumulas los puntos necesarios. | El cupón está pausado, fuera de sus fechas o bloqueado. | Revisar los puntos en el panel. |
| Ya canjeaste este cupón en esta reserva. | El cupón ya se usó en esa reserva. | Pagar sin el cupón o usar otro. |
| Esta reserva ya tiene un cupón. Quítalo si quieres usar otro. | Solo se acepta un cupón por reserva. | Quitar el cupón con la **X** antes de aplicar otro. |
| No pudimos consultar el total a pagar. Revisa tu conexión y vuelve a entrar en unos segundos. | El sistema no logró calcular el monto. | Esperar y volver a abrir la pantalla; no pagar mientras el total aparezca como "—". |
| Ya hay un pago en revisión para esta reserva. / Esa referencia ya se usó en otro pago. Revisa el número de tu comprobante. | El pago ya se reportó o la referencia ya existe en otro pago. | Esperar la revisión o corregir la referencia. |
| Solo puedes calificar un servicio finalizado. / Este servicio ya fue calificado. | La calificación no está permitida. | Esperar a que el servicio termine; una reserva se califica una sola vez. |
| Este reporte no está disponible | El enlace del informe de inspección no es válido o el informe no se ha publicado. | Comunicarse con el lavadero. |
| Los datos chocan con un registro existente o alguien los cambió mientras los editabas. Vuelve a abrirlos e intenta de nuevo. | Otra persona cambió lo mismo al mismo tiempo. | Volver a abrir la pantalla y repetir el cambio. |
| El servicio de reservas no responde. / La base de datos no responde en este momento. | Una parte del sistema está detenida por un momento. | Intentar de nuevo en unos minutos. |
| No hay conexión con el servidor. Intenta de nuevo en un momento. | No hay conexión con el sistema. | Revisar la conexión a internet e intentar de nuevo. |

*Nota.* Textos tomados del archivo de traducción al español de la aplicación web.

## 12. Cierre de Sesión

Cerrar la sesión al terminar evita que otra persona use la cuenta, especialmente en computadores o teléfonos compartidos. Aunque la sesión vence automáticamente una hora después del ingreso, se recomienda cerrarla manualmente.

1. Ubicar la opción **Cerrar Sesión** en la parte inferior del menú lateral.
2. Hacer clic en **Cerrar Sesión**.
3. En la ventana "Cerrar sesión", hacer clic en **Sí, cerrar**.
4. Verificar que el sistema vuelva a la pantalla de inicio de sesión.

Al cerrar sesión, las demás pestañas del mismo navegador también vuelven a la pantalla de inicio de sesión, y si en una pestaña se entra con otra cuenta, las otras se recargan con esa cuenta. La sesión cerrada tampoco sirve en el servidor, aunque alguien hubiera copiado su acceso. En la app móvil, la sesión se cierra desde **Más** (sección 10.11).

**Figura 33**

*Confirmación del Cierre de Sesión*

![Confirmación del Cierre de Sesión](capturas/cliente-25-confirmacion-del-cierre-de-sesion.jpg)

## 13. Preguntas Frecuentes

##### ¿Qué hago si olvidé mi contraseña?

En la pantalla de inicio de sesión, hacer clic en "¿Olvidaste tu contraseña?" y seguir los tres pasos de la sección 8.3. El código llega al correo registrado y es válido durante 15 minutos.

##### ¿Por qué el sistema no me deja registrarme?

El registro solo acepta correos terminados en @gmail.com, y cada correo y cada número de documento pueden tener una sola cuenta. Además, se deben aceptar los dos documentos legales.

##### ¿Mi cuenta se puede bloquear por escribir mal la contraseña?

Sí. Después de cinco intentos fallidos en menos de 15 minutos, la cuenta queda bloqueada durante 15 minutos. Puede esperar ese tiempo o usar **¿Olvidaste tu contraseña?** para crear una contraseña nueva y entrar de inmediato.

##### ¿Cómo sé si mi reserva quedó confirmada?

Cuando la hora está libre, la reserva se confirma de inmediato: la reserva aparece en el panel y en el Historial con el estado Confirmada y llega la confirmación por correo. Con pago digital, el sistema lo lleva a la pantalla de pago; con efectivo, muestra "¡Reserva creada!".

##### ¿Qué hago si la hora que quiero ya está ocupada?

La pantalla solo ofrece horas libres. Si otra persona toma la hora mientras usted reserva, el sistema sugiere las horas libres más cercanas del mismo día que su vehículo puede tomar (no las que se cruzan con otra reserva suya); elija una y confirme.

##### ¿Puedo elegir quién lava mi vehículo?

Sí, es opcional. Al reservar, en **Operario (opcional)** aparecen los operarios libres a esa hora. Si no elige ninguno, el sistema asigna uno libre automáticamente. Puede ver el operario asignado en el panel y recibe una notificación cuando se asigna o cambia.

##### ¿Cómo cambio la fecha de una reserva?

El sistema no permite que el cliente cambie la fecha ni la hora. Cancele la reserva antes de su hora de inicio y reserve de nuevo, o contacte al lavadero para que el administrador la reprograme.

##### ¿Qué pasa si no llego a mi reserva?

El lavadero registra la reserva como No asistió. Si la reserva no tenía operario asignado, el sistema la marca así una hora después de la hora de fin. Si ya había pagado, contacte al lavadero: el reembolso lo decide el administrador.

##### ¿Cómo pago mi servicio?

La forma de pago se elige al reservar. Con **Pago digital ahora**, en **Pagar servicio** escanee el código QR, transfiera el monto exacto y adjunte el comprobante con la referencia de la transacción dentro del plazo. Con **Efectivo en el lavadero** (si el lavadero lo tiene activo), pague al llegar.

##### ¿Por qué se canceló sola mi reserva?

Si al reservar eligió **Pago digital ahora**, tenía 30 minutos (o hasta la hora del servicio, si era más pronto) para subir el comprobante. Si no lo subió a tiempo, el sistema canceló la reserva con el motivo "No se subió el comprobante de pago a tiempo" y liberó el horario. Puede reservar de nuevo y pagar dentro del plazo, o elegir **Efectivo en el lavadero** si no tiene otra reserva activa en efectivo.

##### ¿Por qué no me aparece la opción de efectivo?

Porque el lavadero no tiene activa la cuenta de efectivo o porque usted ya tiene una reserva activa en efectivo: solo se permite una a la vez.

##### ¿El sistema guarda los datos de mi tarjeta?

No. El sistema no solicita ni guarda datos de tarjetas.

##### ¿Por qué mi pago aparece "En revisión"?

Porque un administrador debe verificar que el dinero llegó a la cuenta del lavadero. Cuando lo apruebe o lo rechace, usted recibirá una notificación y un correo; también puede consultar el estado en el Historial.

##### ¿Puedo usar un código de promoción?

Sí. Cuando sus puntos desbloquean un cupón, usted recibe un aviso con su código. En Pagar servicio, escríbalo en Código del cupón y haga clic en Aplicar antes de confirmar el pago. Solo se acepta un cupón por reserva.

##### ¿Cómo gano puntos de fidelización?

Cada servicio suma los puntos que aparecen al reservar ("Suma N puntos"). Los puntos se acreditan cuando el administrador aprueba el pago de la reserva, y su progreso se ve en el panel. Los puntos no se gastan: desbloquean cupones.

##### ¿Cómo califico un servicio?

Cuando la reserva esté Completada, abra el Historial, haga clic en "Calificar Servicio", elija de una a cinco estrellas y, si lo desea, escriba un comentario. Cada reserva se puede calificar una sola vez.

##### ¿Qué es el reporte de mi vehículo que me llegó por correo?

Es el informe de inspección: el lavadero registra, por fases y con fotos, el estado de su vehículo al recibirlo y al entregarlo. Se abre con el enlace del aviso, sin iniciar sesión (sección 10.8).

##### ¿Puedo cambiar el idioma o los colores?

Sí. En Configuración puede elegir español, inglés, francés o portugués y uno de cuatro temas de color; la elección se guarda en su cuenta.

##### ¿Puedo eliminar o desactivar mi cuenta?

La opción **Desactivar mi cuenta** desactiva la cuenta, no la borra: no podrá ingresar, pero su historial se conserva. Para reactivarla, contacte al administrador.

## 14. Glosario

**Tabla 10**

*Glosario de Términos del Sistema para el Cliente*

| Término | Definición |
|---|---|
| Administrador | Persona del lavadero que verifica los pagos, asigna operarios, gestiona horarios y promociones. |
| Bahía (de lavado) | Puesto físico del lavadero donde se lava un vehículo. Dos reservas no pueden ocupar la misma bahía al mismo tiempo. |
| Calificación | Puntuación de una a cinco estrellas, con un comentario opcional, que el cliente da a un servicio completado. |
| Cliente | Persona dueña del vehículo que se registra, reserva turnos y paga los servicios. |
| Código QR | Código bidimensional que se escanea con el teléfono para pagar. |
| Comprobante | Imagen que demuestra que se hizo una transferencia. |
| Cupón | Código de descuento que el cliente desbloquea al acumular puntos de fidelización y canjea al pagar. |
| Dashboard (panel) | Pantalla de inicio que resume la información más importante del cliente. |
| Forma de pago | Elección que se hace al reservar: pago digital ahora (con plazo para subir el comprobante) o efectivo en el lavadero. |
| Informe de inspección | Registro por fases, con notas y fotografías, del estado del vehículo antes y después de un servicio largo. |
| Notificación | Aviso dentro del sistema sobre un hecho relevante. |
| Operario | Empleado del lavadero que lava los vehículos. |
| Plazo de pago | Tiempo para subir el comprobante de una reserva con pago digital: 30 minutos o hasta la hora del servicio, lo que ocurra primero. |
| Puntos de fidelización | Puntos que suma cada servicio cuando se aprueba su pago; desbloquean cupones y no se gastan. |
| Recordatorio | Aviso que el sistema envía 24 horas y 1 hora antes de una reserva. |
| Referencia de la transacción | Número que el banco imprime en el comprobante para identificar la transferencia. |
| Reserva | Bloque de tiempo apartado para lavar un vehículo. |
| Servicio de lavado | Tipo de lavado ofrecido (Básico, Premium o Completo), con su propia duración, precio y puntos. |
| Tema | Combinación de colores de la interfaz que cada persona puede elegir. |

*Nota.* Basado en el glosario de la documentación del proyecto.

## Referencias

Congreso de la República de Colombia. (2012). *Ley Estatutaria 1581 de 2012, por la cual se dictan disposiciones generales para la protección de datos personales*. http://www.secretariasenado.gov.co/senado/basedoc/ley_1581_2012.html

Equipo de Desarrollo Vehicle Washing. (2026a). *Vehicle washing management system: Project documentation* (vehicle-w-docs) [Documentación de proyecto no publicada]. GitHub. https://github.com/code-sena/vehicle-w-docs

Equipo de Desarrollo Vehicle Washing. (2026b). *Front-end-proyecto-web* (rama develop) [Software]. GitHub. https://github.com/BrayanCABRERA123/Front-end-proyecto-web

Equipo de Desarrollo Vehicle Washing. (2026c). *LavadoVehicular-Movil* (versión 1.1.2) [Software]. GitHub. https://github.com/merari0322/LavadoVehicular-Movil

Servicio Nacional de Aprendizaje. (2026). *Guía para elaborar manuales de software: Manual de usuario, manual técnico, manual de instalación y manual de administrador* [Material de formación no publicado]. Programa Tecnólogo en Análisis y Desarrollo de Software.

## 15. Anexos

### Anexo A. Origen de las Capturas

Las figuras de este manual son capturas reales del sistema completo corriendo en Docker con la web en *http://localhost:4200*, sobre la rama *develop*. Las Figuras 13 a 15 y 18 a 21 se tomaron el 26 de octubre de 2026 para mostrar el pago al reservar, con una cuenta de cliente y un código QR de prueba, y la Figura 16 el 27 de octubre de 2026, con reservas de prueba que ocupaban las bahías a esa hora; las demás, el 19 de octubre de 2026, y corresponden a pantallas que no cambiaron desde entonces. Se usaron cuentas, vehículos, reservas y pagos de prueba creados solo para las capturas, por lo que no aparecen correos, documentos, teléfonos, placas ni cuentas bancarias reales. Las capturas de la aplicación móvil se tomaron el 27 de octubre de 2026 con el código de la app (*develop*, versión 1.1.1) ejecutado en el modo web de Expo, con el tamaño y la densidad de pantalla de un celular (393 × 852 puntos, densidad 2,5), contra el mismo sistema local y con las mismas cuentas de prueba. Las pantallas son las de la app; lo único que cambia frente al teléfono es que no aparecen la barra de estado ni los permisos del sistema operativo. Si una pantalla cambia en una versión futura, la captura correspondiente se debe tomar de nuevo con el mismo nombre de archivo de la carpeta *capturas*.

### Anexo B. Lista de Verificación para la Entrega

**Tabla 11**

*Lista de Verificación del Manual de Usuario del Cliente*

| Criterio | Cumple |
|---|---|
| Tiene portada completa (ficha, programa, instructora, centro de formación, fecha) y versión | Sí |
| Incluye control de versiones y tabla de contenido con páginas actualizadas | Sí |
| Explica la introducción, el objetivo y el alcance | Sí |
| Describe los requisitos, el ingreso, el menú principal y cada módulo con el formato de la guía | Sí |
| El contenido coincide con el software de la versión final (*develop* del 26 de octubre de 2026) | Sí |
| Todas las figuras son capturas reales y están ordenadas según el flujo de uso | Sí |
| Incluye la tabla de mensajes y validaciones, el cierre de sesión, las preguntas frecuentes y el glosario | Sí |
| No contiene información sensible ni credenciales reales | Sí |
| Ortografía y formato APA revisados | Sí |
| Exportado a PDF y a Word, en español y en inglés | Sí |

*Nota.* Adaptado de la lista de verificación de la guía para elaborar manuales de software (SENA, 2026).
