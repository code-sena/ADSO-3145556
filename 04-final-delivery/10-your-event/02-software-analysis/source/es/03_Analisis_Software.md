---
title: "Análisis del software"
lang: es
documents: [TE-0020, TE-0022, TE-0076, TE-0077, [TE-0034, TE-0035, TE-0036, TE-0037, TE-0038], [TE-0025, TE-0026, TE-0027, TE-0028, TE-0029], [TE-0030, TE-0031, TE-0032, TE-0033], [TE-0023, TE-0024], TE-0039, [TE-0040, TE-0041], [TE-0042, TE-0043], [TE-0044, TE-0045], [TE-0046, TE-0047], [TE-0048, TE-0049], [TE-0050, TE-0051], [TE-0052, TE-0053], [TE-0054, TE-0055], [TE-0056, TE-0057], [TE-0058, TE-0059], [TE-0060, TE-0061], [TE-0062, TE-0063], [TE-0064, TE-0065], [TE-0066, TE-0067], [TE-0068, TE-0069], [TE-0070, TE-0071], [TE-0072, TE-0073], [TE-0074, TE-0075], TE-0021, TE-0078]
status: borrador
---
# Análisis del software — Tu Evento

> Documento consolidado de Tu Evento. Reúne los documentos siguientes, cuyo contenido se conserva sin cambios; los metadatos de conversión de cada uno van en un comentario HTML al inicio de su sección.

**Contenido**

1. [Análisis del software](#doc-te-0020)
2. [Requisitos de software](#doc-te-0022)
3. [Requisitos de hardware](#doc-te-0076)
4. [Matriz de riesgos](#doc-te-0077)
5. [Casos de uso — módulo de seguridad](#doc-te-0034-te-0035-te-0036-te-0037-te-0038)
6. [Casos de uso — módulo de eventos](#doc-te-0025-te-0026-te-0027-te-0028-te-0029)
7. [Casos de uso — módulo de pagos](#doc-te-0030-te-0031-te-0032-te-0033)
8. [Casos de uso — módulo de cartera](#doc-te-0023-te-0024)
9. [Caso de uso — diagrama draw.io](#doc-te-0039)
10. [Diagrama de clases — config](#doc-te-0040-te-0041)
11. [Diagrama de clases — controller](#doc-te-0042-te-0043)
12. [Diagrama de clases — controller/email](#doc-te-0044-te-0045)
13. [Diagrama de clases — data](#doc-te-0046-te-0047)
14. [Diagrama de clases — dto](#doc-te-0048-te-0049)
15. [Diagrama de clases — dto/email](#doc-te-0050-te-0051)
16. [Diagrama de clases — dto/requests](#doc-te-0052-te-0053)
17. [Diagrama de clases — dto/responses](#doc-te-0054-te-0055)
18. [Diagrama de clases — jwt](#doc-te-0056-te-0057)
19. [Diagrama de clases — model](#doc-te-0058-te-0059)
20. [Diagrama de clases — repository](#doc-te-0060-te-0061)
21. [Diagrama de clases — service](#doc-te-0062-te-0063)
22. [Diagrama de clases — service/aws](#doc-te-0064-te-0065)
23. [Diagrama de clases — service/email](#doc-te-0066-te-0067)
24. [Diagrama de clases — service/impl](#doc-te-0068-te-0069)
25. [Diagrama de clases — service/jwt](#doc-te-0070-te-0071)
26. [Diagrama de clases — service/notification](#doc-te-0072-te-0073)
27. [Diagrama de clases — service/oauth](#doc-te-0074-te-0075)
28. [Diagramas de estados](#doc-te-0021)
29. [Diagramas de secuencia](#doc-te-0078)
30. [Diagramas de secuencia de la implementación](#secuencias-implementacion)

> **Diagramas de actividades.** Los diagramas de actividades por módulo, que completan las vistas dinámicas de este análisis, están en [05_Diseno_Software.md — Diagramas de actividades por módulo](https://github.com/TheJhampierBoy/TuEvento-Docs/blob/main/es/05_Diseno_Software.md#doc-te-0082-te-0083-te-0084-te-0085-te-0086-te-0087-te-0088-te-0089-te-0090-te-0091-te-0092-te-0093).

---

<a id="doc-te-0020"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Analisis_del_Software_Tu_evento.md
id: TE-0020
title: "Análisis del Software-Tu evento"
lang: es
source: 02_Analisis_Software/Análisis del Software-Tu evento.docx
source_sha256: 10dd0932cfa76d7a16ecceceff675320340da563fdcd86b30bf8f7b0acc9e733
conversion: "pandoc 3.9 (-t gfm --wrap=none --extract-media) + extracción de encabezados/pies; sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Encabezados y pies de página extraídos aparte (pandoc no los convierte); en el original se repiten en cada página. Los campos se muestran como {PAGE}, {NUMPAGES}."
  - "Las tablas con celdas combinadas o con listas/varios párrafos en las celdas se escriben en HTML (pandoc), porque GFM no las admite."
status: borrador
reviewed_by: ""
-->

# Análisis del software

**Análisis del Software**

Lozano Contreras Cristofer David

Jhampier Santos Ortiz

Análisis y Desarrollo de Software

\(3145556\)

Servicio Nacional de Aprendizaje

Instructores:

Jhon Corredor

Carlos Julio Cadena

Karol Correa

Agosto 21, 2025

**Introducción**

El desarrollo de software es un proceso que requiere planificación, análisis y control para garantizar que el sistema cumpla con los objetivos funcionales y no funcionales establecidos. En este contexto, el análisis de software juega un papel fundamental dentro del ciclo de vida del sistema, ya que permite identificar necesidades reales, definir el alcance, entender los riesgos y asegurar que las funcionalidades propuestas se integren de manera eficiente y segura.

En el caso del sistema de gestión de eventos descrito en este documento, el análisis adquiere una importancia especial debido a la complejidad del proyecto. El sistema integra múltiples módulos críticos como autenticación avanzada, creación y maquetación de eventos, gestión de reservas, generación de códigos QR, integración con una pasarela de pagos sandbox para pruebas del flujo de pago, compatibilidad multiplataforma (web, Android e iOS), notificaciones push con FCM, arquitectura orientada a microservicios y un backend modular basado en N-capas con DDD.

Un análisis adecuado permite detectar desde el inicio los posibles retos técnicos, riesgos de seguridad, problemas de interoperabilidad y dependencias clave entre las funcionalidades. Gracias a esto, el software puede construirse garantizando escalabilidad, mantenibilidad, disponibilidad y una experiencia de usuario óptima tanto en la web como en las aplicaciones móviles.

Este documento tiene como propósito presentar el análisis del sistema “Tu Evento”, detallando su funcionamiento, alcance, restricciones, requerimientos funcionales y no funcionales, así como las decisiones arquitectónicas que permiten sustentar el desarrollo de una plataforma moderna, segura y preparada para la operación real en entornos de alto tráfico y múltiples dispositivos.

**Objetivos del Proyecto** 

**Objetivo General ** 

Desarrollar e implementar una plataforma integral de gestión de eventos que permita a los organizadores crear, maquetar y administrar sus eventos mediante herramientas avanzadas de diseño web, integrando funciones como reserva de asientos, pagos en línea a través de una pasarela de pagos sandbox, validación de acceso con códigos QR y notificaciones push. Al mismo tiempo, ofrecer a los asistentes una experiencia fluida, segura e intuitiva tanto en la versión web como en la aplicación móvil.

**Objetivos Específicos** 

***<u>Diseñar y desarrollar la arquitectura de la plataforma</u>*** Definir la estructura del backend y de la base de datos para soportar la gestión de eventos, reservas y usuarios.\
La base de datos será modelada y normalizada hasta Tercera Forma Normal (3FN) para garantizar integridad, consistencia y evitar redundancias.\
Además, se implementarán roles de usuario (organizadores y asistentes), junto con un sistema de autenticación seguro.

***<u>Implementar un sistema seguro de autenticación</u>***, incluyendo registro, inicio de sesión, redes sociales, recuperación de contraseña y manejo de tokens.

***<u>Desarrollar el módulo de creación y maquetación de eventos</u>***, exclusivamente disponible en la versión web para los organizadores.

***<u>Construir el sistema de reservas y compra de boletas</u>***, incluyendo la integración de la pasarela de pagos sandbox y generación de códigos QR.

***<u>Optimizar la experiencia del usuario</u>***, incorporando dashboards por rol, soporte multilenguaje y sistema de temas personalizables.

***<u>Garantizar compatibilidad multiplataforma</u>***, asegurando funcionamiento completo en web, Android e iOS.

***<u>Implementar notificaciones push</u>*** para recordatorios, cambios de estado, validaciones de pago y alertas de eventos.

***<u>Implementar la experiencia interactiva para los asistentes: </u>*** 

Desarrollar una interfaz intuitiva para la selección de asientos en tiempo real con bloqueo temporal y un sistema de colores para mostrar disponibilidad de asientos (disponible, reservado) 

 

***<u>Integrar Funcionalidades de Comunicación y Notificaciones:</u>*** 

Implementar un sistema de notificaciones que notifiqué cambios en los horarios de los eventos y envié recordatorios también conexión con redes sociales para compartir los eventos 

**Audiencia** 

El aplicativo de gestión de eventos está dirigido a distintos tipos de usuarios que interactuarán con el sistema para organizar, administrar y asistir a eventos. La audiencia se clasifica en los dos grupos: 

**1.Organizadores de Eventos:** 

**           <u>¿Quiénes son?</u>** 

Los organizadores de eventos son individuos o empresas responsables de la planificación, administración y ejecución de eventos en diferentes tipos de espacios. Este grupo puede ser como las empresas de producción de eventos, auditorios y centros de convenciones, organizadores independientes o agencias, empresas de entretenimiento y espectáculos, universidades e instituciones educativas. 

         **<u>¿Cuál es su necesidad?</u>** 

Requieren herramientas para crear, configurar y gestionar eventos de manera eficiente, personalizar espacios, optimizar logística, controlar reservas y validar accesos.

**2.Asistentes:** 

Los asistentes son las personas interesadas en adquirir boletos para distintos tipos de eventos. Este grupo incluye como el público en general, estudiantes, profesionales, familias y grupos de amigos. 

 

 

         **<u>¿Cuál es su necesidad?</u>** 

Necesitan una interfaz sencilla para explorar eventos, seleccionar asientos, obtener tickets con QR y recibir notificaciones importantes. 

 

 

 

**Descripción General del Software** 

El software de gestión de eventos **"Tu Evento"** es una plataforma digital diseñada para facilitar y optimizar la creación, administración y operación de eventos como conciertos, conferencias, obras teatrales, espectáculos y actividades institucionales.

La versión web permite a los organizadores maquetar completamente el espacio del evento, configurando secciones, distribuyendo asientos, ajustando tamaños y formas, y administrando todos los elementos visuales del plano. Los organizadores también podrán gestionar reservas, validar pagos a través de la pasarela de pagos sandbox y controlar el ingreso de asistentes mediante lectura de códigos QR

Por su parte, los asistentes pueden explorar eventos disponibles, visualizar el plano interactivo, seleccionar asientos, comprar boletas desde web o móvil, recibir notificaciones push y acceder a sus tickets electrónicos con código QR

El sistema incorpora monitoreo en tiempo real para visualizar reservas, ocupación del evento y estado de validaciones de entrada

La plataforma está diseñada bajo una arquitectura escalable y segura, garantizando funcionamiento fluido, accesibilidad y compatibilidad multiplataforma. Su propósito es ofrecer una solución moderna, completa y eficiente tanto para organizadores como para asistentes, permitiendo una gestión de eventos automatizada, flexible y confiable.

 

 

 

 

 

**Requerimientos** 

Para garantizar el correcto funcionamiento del software de gestión de eventos "Tu Evento", es necesario definir los requerimientos que abarcan tanto las funcionalidades esenciales como los aspectos técnicos y de calidad. Estos requerimientos establecen las bases del sistema, asegurando que cumpla con las expectativas de los usuarios y brinde una experiencia eficiente y segura. Los requerimientos del sisstema se dividen en tres categorías principales: 

***<u>1.Requerimientos funcionales:</u>*** Describen las características y acciones que debe realizar el software. 

***<u>2.Requerimientos no funcionales</u>***: Establecen criterios de rendimiento, seguridad, usabilidad y compatibilidad. 

***<u>3.Requerimientos de hardware y software:</u>*** Determinan las especificaciones técnicas necesarias para la implementación y ejecución del sistema. 

A continuación, se detallan los requerimientos específicos que regirán el desarrollo del aplicativo. 

# Requerimientos Funcionales

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF1</p>
</blockquote></th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Registro de usuario</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
<p>Necesario</p>
</blockquote></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Interfaz de usuario</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<blockquote>
<p>Alta</p>
</blockquote></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Página de inicio, interfaz de usuario.</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
</blockquote>
<ul>
<li><p>Credenciales del usuario</p></li>
</ul>
<blockquote>
<p>(Nombre, correo electrónico y contrasen a)</p>
</blockquote></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema muestra que la creación de la cuenta del usuario al sistema haya sido exitosa, donde lo redirecciona a la página de iniciar sesión de usuario.</p></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
<p>Este requerimiento define la funcionalidad de creación de cuentas de usuario</p>
</blockquote>
<ul>
<li><p>El usuario ingresa sus credenciales (nombre, correo y contraseña).</p></li>
<li><p>El sistema genera un código de verificación de 6 dígitos y lo envía al correo electrónico proporcionado.</p></li>
<li><p>El usuario ingresa el código en el sistema para validar su cuenta.</p></li>
</ul>
<blockquote>
<p><strong>Si la verificación es exitosa:</strong></p>
</blockquote>
<ul>
<li><p>El sistema registra la cuenta.</p></li>
<li><p>El usuario es redirigido a la página principal (login).</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Si la verificación falla:</strong></p>
</blockquote>
<ul>
<li><p>El sistema notifica que el correo ya existe o que el código es incorrecto/expirado.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>El sistema debe estar en ejecución y accesible para el usuario.</p></li>
<li><p>El usuario no debe tener una cuenta registrada previamente con el mismo correo electrónico.</p></li>
<li><p>El servicio de enví o de correos electrónicos debe estar disponible para enviar el código de verificación</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si el correo ya esta registrado, el sistema mostrara un mensaje de error.</p></li>
<li><p>Si el usuario ingresa un código incorrecto o expirado, el sistema mostrara un mensaje de invalidación.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<blockquote>
<p>Si los datos son correctos, el sistema debe crear la cuenta y redirigir al usuario a la pantalla principal.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<blockquote>
<p>Si la verificación es exitosa:</p>
</blockquote>
<ul>
<li><p>El usuario queda registrado en la base de datos.</p></li>
<li><p>Se genera una sesión o se redirige al inicio de sesión.</p></li>
</ul>
<blockquote>
<p>Si la verificación falla:</p>
</blockquote>
<ul>
<li><p>No se registra la cuenta.</p></li>
<li><p>El sistema notifica el error al usuario (correo ya existente o código inválido/expirado).</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF2</p>
</blockquote></th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Autenticación con redes sociales (OAuth)</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
<p>Opcional.</p>
</blockquote></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<blockquote>
<p>Interfaz de usuario.</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<p>No</p></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Media.</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Página de inicio, interfaz de usuario.</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
</blockquote>
<p>El usuario selecciona la opción de vincular su cuenta con una red social e inicia sesión en la plataforma elegida para autorizar la vinculación. El sistema solicita permisos de acceso según el proveedor</p>
<p><strong>(Facebook/Gmail)</strong></p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema asocia la cuenta del usuario con la red social seleccionada.</p></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
<p>Este requerimiento define la funcionalidad de autenticación mediante redes sociales.</p>
</blockquote>
<ul>
<li><p>El usuario podra vincular su cuenta del sistema con un perfil externo <strong>(Facebook o Gmail)</strong></p></li>
</ul>
<blockquote>
<p>utilizando el protocolo <strong>OAuth 2.0</strong></p>
</blockquote>
<ul>
<li><p>Una vez autorizada la vinculación, el sistema almacenara la relación de la cuenta para permitir inicios de sesión futuros sin necesidad de credenciales adicionales.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>El sistema debe estar en funcionamiento y accesible.</p></li>
<li><p>El usuario debe tener una cuenta activa en la red social seleccionada (Facebook o Gmail).</p></li>
<li><p>La red social debe estar disponible y operativa para responder a la solicitu de autenticación.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><ul>
<li><p>El usuario debe estar conectado a internet.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote>
<ul>
<li><p>Si el usuario cancela la autorización en la red social, la vinculación no se completa y se muestra un mensaje de notificación.</p></li>
<li><p>Si la red social no responde o hay un error en la autenticación, el sistema debe mostrar un mensaje de error con la opción de reintentar.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<blockquote>
<p>En caso de e xito, la cuenta del usuario debe quedar asociada y disponible para futuros inicios de sesión.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Postcondición:</strong></p>
<p>Si la autenticación es exitosa:</p>
</blockquote>
<ul>
<li><p>La cuenta del usuario en el sistema queda asociada correctamente con la red social seleccionada.</p></li>
<li><p>El usuario puede iniciar sesión en futuros accesos utilizando esa red social sin necesidad de credenciales adicionales.</p></li>
</ul>
<blockquote>
<p>Si la autenticación falla o es cancelada:</p>
</blockquote>
<ul>
<li><p>No se realiza la vinculación.</p></li>
<li><p>El sistema muestra un mensaje de notificación o error.</p></li>
<li><p>El usuario permanece sin cambios en su cuenta actual.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF3</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Inicio Sesión de usuario</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><strong>Requerimiento que lo utiliza o especializa:</strong> Interfaz de usuario</td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>página de inicio, interfaz de usuario.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<ul>
<li><p>Correo electrónico y contrasen a del usuario previamente registrado.</p></li>
</ul></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<ul>
<li><p>El sistema redirecciona a la página principal del sistema.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><p><strong>Descripción:</strong></p>
<ul>
<li><p>El usuario accede a la página de inicio de sesión.</p></li>
<li><p>El sistema muestra los campos para ingresar correo electrónico y contraseña.</p></li>
<li><p>El usuario introduce sus credenciales y el sistema debe validar que los campos no estén vacíos.</p></li>
<li><p>El sistema consulta la base de datos para validar que el correo electrónico se encuentre</p></li>
</ul>
<blockquote>
<p>registrado y que la contraseña proporcionada coincida con la almacenada para dicho usuario.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El usuario debe estar previamente registrado en el sistema.</p></li>
<li><p>El sistema debe estar disponible y en línea.</p></li>
<li><p>El usuario debe tener acceso a internet.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><p><strong>Manejo de situaciones anormales</strong></p>
<p>El usuario introduce su correo electrónico o una contrasen a incorrecta. El sistema debe mostrar un mensaje de error que indique que el nombre de usuario o la contrasen a son incorrectos (ej. "Usuario o contrasen a incorrectos") y que pueda permitir al usuario reintentar el inicio de sesión.</p></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<p>Si las credenciales ingresadas son válidas y autenticadas correctamente, el sistema deberá:</p>
<ul>
<li><p>Establecer una sesión activa.</p></li>
<li><p>Garantizar la persistencia de la sesión mientras dure la interacción</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>La sesión del usuario queda activa en el sistema hasta que este cierre sesión manualmente o la sesión expire.</p></li>
<li><p>El usuario tiene acceso a todas las funcionalidades y permisos asociados a su cuenta.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF4</p>
</blockquote></th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Gestión de perfil de usuario (creación y personalización)</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
</blockquote>
<p>Necesario.</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Interfaz de usuario, gestión de usuario.</p></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si.</p></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta.</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Interfaz de registro y perfil de usuario</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image5.png" /></p>
<p>nombre, apellido, correo (ya verificado), contraseña.</p>
</blockquote></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema confirma la creación/actualización del perfil y muestra un mensaje de e xito. La información queda guardada en la base de datos y disponible en la sección de perfil del usuario</p></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
<p>Este requerimiento permite al usuario configurar y gestionar su información personal dentro del</p>
<p>sistema. Incluye la creación inicial del perfil tras el registro, la posibilidad de editar datos (nombre,</p>
<p>apellido, fecha de nacimiento y dirección) y mantener actualizada su información. El objetivo es ofrecer una experiencia personalizada y consistente para cada usuario.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>El usuario debe haber completado el registro de cuenta en el sistema (ya sea mediante correo o red social vinculada).</p></li>
<li><p>El sistema debe estar disponible y en línea.</p></li>
<li><p>El usuario debe tener acceso a internet para completar la creación o modificación de su perfil.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><ul>
<li><p>Si se trata de un registro normal (no por redes sociales), el correo electrónico del usuario debe estar previamente verificado.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>El usuario ha cerrado sesión y quiere gestionar su información, este no encontrara su información mas hasta que inicie sesión.</p></li>
<li><p>Si el usuario no tiene acceso a internet, no podra gestionar su información.</p></li>
<li><p>Si el usuario desea modificar su información y se demora mucho procesar se pedira que lo intente de nuevo o un mensaje gene rico.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El usuario debe poder editar su información en cualquier momento después de la creación inicial.</p></li>
<li><p>La información debe guardarse de forma persistente en la base de datos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>El perfil queda creado o actualizado correctamente.</p></li>
<li><p>La información queda disponible para futuras sesiones.</p></li>
<li><p>Si se vinculó una red social, la asociación queda guardada para futuros inicios de sesión.</p></li>
<li><p>En caso de fallo, no se altera la información previa y se muestra un mensaje al usuario.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF5</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Recuperar contrasen a</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Interfaz de usuario</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Página de recuperar contrasen a, interfaz de usuario.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<ul>
<li><p>Credenciales del usuario (Correo electrónico)</p></li>
</ul></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>Si el correo electrónico ingresado es válido y existe en el sistema, se enví a un código de verificación al correo para que el usuario pueda renovar su</p>
<p>contrasen a.</p></td>
</tr>
<tr>
<td colspan="5"><p><strong>Descripción:</strong></p>
<ul>
<li><p>El usuario selecciona la opción "¿Olvidaste tu contraseña?" en la página de inicio de sesión.</p></li>
<li><p>El sistema muestra un campo para que el usuario introduzca su correo electrónico registrado.</p></li>
<li><p>El usuario ingresa su correo electrónico.</p></li>
<li><p>El sistema valida que el campo no esté vacío y que tenga formato válido de correo electrónico.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><ul>
<li><p>El sistema busca el correo en la base de datos:</p></li>
</ul>
<ul>
<li><p><strong>Si el correo está registrado:</strong></p>
<ul>
<li><p>Genera un código de verificación único y lo envía al correo del usuario.</p></li>
<li><p>Muestra un mensaje confirmando que se envió el código.</p></li>
</ul></li>
<li><p><strong>Si el correo NO está registrado:</strong></p>
<ul>
<li><p>Muestra un mensaje de error indicando que el correo no existe en el sistema.</p></li>
</ul></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El usuario debe tener una cuenta previamente registrada en el sistema.</p></li>
<li><p>El correo electrónico debe estar verificado en el sistema.</p></li>
<li><p>El sistema debe estar en lí nea y tener acceso al servicio de enví o de correos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si el usuario ingresa un correo electrónico no registrado, el sistema muestra un mensaje de error.</p></li>
<li><p>Si el código de verificación expira o es incorrecto, el sistema pedira al usuario solicitar un nuevo código.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El sistema debe validar que el correo electrónico tenga formato válido.</p></li>
<li><p>El sistema debe enviar correctamente el código al correo del usuario si existe en la base de datos.</p></li>
<li><p>El sistema debe garantizar que la nueva contraseña cumpla los requisitos de seguridad (mínimo 8 caracteres, una mayúscula, un número y un carácter especial).</p></li>
<li><p>El usuario debe recibir confirmación de que su contraseña ha sido actualizada.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>El usuario cuenta con una nueva contraseña registrada en el sistema.</p></li>
<li><p>El código de verificación utilizado queda invalidado para evitar reutilización.</p></li>
<li><p>La cuenta del usuario queda habilitada para iniciar sesión nuevamente con la nueva contraseña.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF6</p>
</blockquote></th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Cerrar sesión</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
<p>Necesario</p>
</blockquote></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<blockquote>
<p>Interfaz de usuario</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si</p></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Interfaz de usuario</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
</blockquote>
<p>El usuario hace clic en el botón de salir en la página principal del sistema.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema invalida la sesión activa del usuario e impide el acceso a las funciones protegidas hasta que se inicie sesión nuevamente.</p></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
</blockquote>
<p>Este requerimiento define la funcionalidad de cierre de sesión en el sistema.</p>
<ul>
<li><p>Al seleccionar la opción <strong>“Cerrar sesión”</strong>, el sistema debe invalidar el token o sesión activa asociada al usuario.</p></li>
<li><p>Una vez cerrada la sesión, el usuario pierde acceso inmediato a todas las áreas protegidas del sistema.</p></li>
<li><p>El sistema debe redirigir al usuario a la página de inicio o a la pantalla de inicio de sesión.</p></li>
<li><p>Esta acción garantiza la seguridad y privacidad de la información del usuario, evitando accesos no autorizados en dispositivos compartidos o públicos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>El usuario debe tener una sesión activa iniciada en el sistema.</p></li>
<li><p>El sistema debe estar en línea y con disponibilidad del servicio de autenticación.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><ul>
<li><p>Si ocurre una pérdida de conexión al servidor durante el cierre de sesión, el sistema debe mostrar un mensaje de error: <em>“No se pudo cerrar la sesión por problemas de conexión, inténtelo nuevamente”</em>.</p></li>
<li><p>Si la sesión ya había expirado antes de intentar cerrarla, el sistema redirige al usuario automáticamente a la pantalla de inicio de sesión.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><blockquote>
<p><strong>Criterios:</strong></p>
<p>Tras cerrar sesión, el usuario debe ser redirigido automa ticamente a la pantalla de inicio de sesión.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Postcondición:</strong></p>
</blockquote>
<ul>
<li><p>La sesión del usuario queda invalidada en el sistema.</p></li>
<li><p>El usuario se encuentra en la pantalla de inicio de sesión.</p></li>
<li><p>El sistema garantiza que no se pueda acceder a datos ni funcionalidades privadas hasta que se vuelva a iniciar sesión.</p></li>
</ul></td>
</tr>
</tbody>
</table>

> <img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image3.png" style="width:0.13889in;height:0.14583in" />Documento: Tue evento SRS.docx CapySoft, 2024 Página 22 de 55

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF7</p>
</blockquote></th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Eliminación permanente de cuenta de usuario</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
<p>Opcional</p>
</blockquote></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<blockquote>
<p>Interfaz de usuario</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si</p></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Página de configuración de la cuenta, interfaz de usuario</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
</blockquote>
<ul>
<li><p>El usuario selecciona la opción <strong>“Desactivar cuenta”</strong> dentro de la configuración.</p></li>
<li><p>El usuario confirma la acción en la ventana emergente de validación.</p></li>
</ul></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<ul>
<li><p>El sistema elimina de forma <strong>permanente e irreversible</strong> la cuenta y todos los datos asociados.</p></li>
<li><p>El sistema redirige al usuario a la pantalla de inicio con un mensaje de confirmación.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
</blockquote>
<p>La funcionalidad de eliminación de cuenta permite al usuario borrar permanentemente su perfil y toda la información asociada en el sistema.</p>
<ul>
<li><p>El sistema debe solicitar confirmación explícita antes de proceder (ej. “¿Está seguro de que desea eliminar su cuenta? Esta acción no se puede deshacer”).</p></li>
<li><p>Tras la eliminación, el usuario no podrá acceder nuevamente con esas credenciales ni recuperar la información borrada.</p></li>
<li><p>Finalmente, el sistema redirige al usuario a la página de inicio mostrando un mensaje de confirmación del proceso.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>El usuario debe tener una sesión activa en el sistema.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><ul>
<li><p>El usuario debe haber confirmado su identidad (ej. ingresar contraseña nuevamente o un código de verificación).</p></li>
<li><p>El sistema debe estar en línea y con acceso a la base de dato</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote>
<ul>
<li><p>Si la conexión a internet se interrumpe antes de completar la eliminación, el sistema muestra un mensaje de error: <em>“La operación no pudo completarse, inténtelo nuevamente”</em>.</p></li>
<li><p>Si el usuario intenta acceder a su cuenta después de la eliminación, el sistema debe indicar que las credenciales no existen.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Criterios:</strong></p>
</blockquote>
<ul>
<li><p>El sistema debe solicitar confirmación previa a la eliminación.</p></li>
<li><p>El sistema debe garantizar la eliminación <strong>definitiva e irreversible</strong> de los datos del usuario.</p></li>
<li><p>El usuario debe ser redirigido automáticamente a la pantalla de inicio después de la eliminación.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Postcondición:</strong></p>
</blockquote>
<ul>
<li><p>La cuenta del usuario y todos sus datos asociados ya no existen en el sistema.</p></li>
<li><p>El usuario queda sin acceso a cualquier servicio o funcionalidad del sistema.</p></li>
<li><p>El sistema asegura que la eliminación es irreversible y que no quedan datos recuperables vinculados al usuario.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF8</p>
</blockquote></th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Mo dulo de administración del sistema</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
<p>Necesario.</p>
</blockquote></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Interfaz de administrador,</p>
<p>validación y aceptación de solicitudes de organizador</p></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si.</p></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta.</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Interfaz de administración, mo dulo de validación de organizadores</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
</blockquote>
<p>El administrador inicia sesión en el sistema ingresando sus credenciales válidas.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<ul>
<li><p>El administrador accede a un apartado exclusivo para su rol.</p></li>
<li><p>Puede visualizar las solicitudes de organizadores, revisar sus documentos y aceptar/rechazar las peticiones para habilitar la creación de eventos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
</blockquote>
<ul>
<li><p>El administrador del sistema dispone de un módulo especial al iniciar sesión.</p></li>
<li><p>Una vez autenticado, el sistema identifica el rol de administrador y lo redirige al apartado correspondiente.</p></li>
<li><p>En este módulo, el administrador puede revisar los documentos enviados por los usuarios que solicitan ser organizadores.</p></li>
<li><p>El administrador tiene la capacidad de <strong>aceptar o rechazar</strong> estas solicitudes.</p></li>
<li><p>Una vez aceptada la solicitud, el usuario adquiere permisos para crear eventos en la plataforma.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><ul>
<li><p>El administrador también puede ejecutar todas las funciones comunes de cualquier cuenta (inicio/cierre de sesión, recuperación de contraseña, etc.).</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>El administrador debe tener un usuario registrado con rol de administrador.</p></li>
<li><p>El sistema debe estar en línea y con acceso a la base de datos de solicitudes.</p></li>
<li><p>El solicitante debe haber enviado previamente sus documentos para revisión.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote>
<ul>
<li><p>Si el administrador ingresa credenciales incorrectas, el sistema muestra un mensaje de error: <em>“Usuario o contraseña incorrectos”</em>.</p></li>
<li><p>Si la conexión a internet es inestable, el sistema puede mostrar un mensaje: <em>“Problemas de conexión, inténtelo de nuevo”</em>.</p></li>
<li><p>Si el administrador pierde conexión al aceptar/rechazar una solicitud, la acción no se completa y el sistema muestra un mensaje de error: <em>“La operación no se procesó</em></p></li>
</ul>
<blockquote>
<p><em>correctamente”</em>.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Criterios:</strong></p>
</blockquote>
<ul>
<li><p>El administrador debe ser redirigido automáticamente a la interfaz de administración tras iniciar sesión con credenciales válidas.</p></li>
<li><p>El sistema debe mostrar la lista de solicitudes de organizadores con sus documentos asociados.</p></li>
<li><p>El sistema debe registrar si la solicitud fue aceptada o rechazada.</p></li>
<li><p>Una vez aceptada, el organizador debe poder acceder al módulo de creación de eventos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Postcondición:</strong></p>
</blockquote>
<ul>
<li><p>El administrador queda autenticado dentro de su módulo de gestión.</p></li>
<li><p>Los organizadores aceptados pueden crear eventos en el sistema.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 15%" />
<col style="width: 7%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF9</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Edición de la estructura.</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario.</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Interfaz de desarrollo evento.</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si.</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta.</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Página de desarrollo para crear evento.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario con rol de organizador selecciona la opción de crear un plano del sitio del evento dentro de la interfaz de desarrollo.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>Este requerimiento permite al usuario organizador crear un plano digital del sitio donde se llevara a cabo el evento.</p></td>
</tr>
<tr>
<td colspan="5"><p><strong>Descripción:</strong></p>
<p>Entrara a una interfaz gra fica con opciones predeterminadas para que el usuario cree un plano que se asemeje al sitio donde se realizara el evento, teniendo la opción de poner un plano ya generado o desarrollar desde cero. Una vez que el organizador finaliza el diseño y lo confirma, el sistema lo</p>
<p>publica y lo asocia al evento, habilitando a los usuarios para seleccionar sus asientos desde la interfaz de reservas. Además, el organizador podra <strong>guardar avances de su maquetación</strong> sin necesidad de publicarla y, en caso de que lo desee, también tendra la opción de <strong>eliminar un plano existente</strong> antes de que sea publicado o incluso después.</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El usuario debe haber iniciado sesión en el sistema.</p></li>
<li><p>El usuario debe tener asignado el rol de organizador.</p></li>
<li><p>Debe existir un evento previamente creado al cual se asociará el plano.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<p>Si la conexión a internet se interrumpe durante el proceso de creación, el sistema debe notificar al usuario que el plano no se guardo ni se publico correctamente.</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El sistema debe solicitar confirmación al organizador antes de publicar el plano y hacerlo visible para reservas.</p></li>
<li><p>Solo los usuarios con rol de organizador pueden crear y publicar estructuras de eventos.</p></li>
<li><p>El sistema debe permitir al organizador <strong>guardar borradores</strong> de maquetación sin publicarlos.</p></li>
<li><p>El sistema debe permitir al organizador <strong>eliminar planos creados</strong>, siempre que no tengan reservas activas asociadas.</p></li>
<li><p>El sistema debe garantizar que el plano publicado se muestre correctamente en la interfaz de selección de asientos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>El plano queda almacenado en el sistema y vinculado al evento correspondiente.</p></li>
<li><p>Los usuarios pueden visualizar el plano desde la sección de reservas y seleccionar sus asientos disponibles.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF10</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Reservación de asientos.</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<blockquote>
<p>Interfaz de agendamiento</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Página de agendamiento</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario asistente accede al sistema, selecciona el evento de su interés y,</p>
<p>posteriormente, elige un asiento disponible dentro del lugar correspondiente.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema registra la reserva del asiento elegido y la coloca en estado <strong>“ocupado”</strong> hasta que el organizador valide el pago.</p></td>
</tr>
<tr>
<td colspan="5"><p><strong>Descripción:</strong></p>
<p>El sistema permitira al usuario asistente visualizar los eventos disponibles y seleccionar aquel al que desea asistir. Una vez elegido el evento, el asistente podra reservar un asiento entre los disponibles. La reserva quedara registrada en el sistema con estado <strong>“ocupado”</strong>, garantizando que el asiento quede bloqueado temporalmente (stock reservado) y no pueda ser seleccionado por otro usuario.</p>
<p>Cuando el organizador valide el pago de forma fí sica, el sistema actualizara la reserva al estado</p>
<p><strong>“confirmada”</strong>.</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El usuario debe haber iniciado sesión en el sistema.</p></li>
<li><p>Debe existir un evento creado y habilitado para reservas.</p></li>
<li><p>Debe existir un plano de asientos publicado y disponible para el evento.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si el asiento seleccionado ya fue reservado por otro usuario (en estado pendiente o confirmada), el sistema debe notificarlo y solicitar la elección de otro asiento.</p></li>
<li><p>Si la conexión a internet se interrumpe durante el proceso de reserva, el sistema debe indicar que la operación no se completo .</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El asiento seleccionado debe quedar bloqueado en el sistema desde el momento en que el usuario lo elige.</p></li>
<li><p>La reserva debe registrarse con estado <strong>“pendiente de confirmación”</strong> hasta que el organizador valide el pago.</p></li>
<li><p>El asiento confirmado debe quedar marcado como <strong>ocupado</strong> en el plano del evento.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>La reserva queda almacenada en la base de datos y vinculada al evento correspondiente.</p></li>
<li><p>Si la reserva es confirmada, el asiento queda bloqueado permanentemente como <strong>ocupado</strong>.</p></li>
<li><p>Si la reserva no es confirmada en el tiempo definido por el sistema (ej. lí mite de stock de confirmación), el asiento debe liberarse automa ticamente y volver a estar disponible para otros usuarios.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF11</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Generación y validación de código QR por reserva</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Reservas de asientos (RF10)</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Gestión de eventos.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario ha realizado una reserva de uno o varios asientos.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema genera un código QR único asociado a dicha reserva y lo enví a al usuario.</p></td>
</tr>
<tr>
<td colspan="5"><p><strong>Descripción:</strong> El sistema debe generar un código QR único por reserva en el momento en que el usuario selecciona los asientos y confirma su solicitud.</p>
<p>El QR contendra la información esencial del evento (nombre del evento, fecha, número y ubicación de asientos). Este código QR sera enviado al correo del usuario</p>
<p>El organizador del evento sera el encargado de validar el QR mediante un lector, una vez que el pago fí sico haya sido confirmado. El QR servira como comprobante de reserva y control de acceso al evento.</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El usuario debe haber seleccionado uno o varios asientos.</p></li>
<li><p>El pago físico debe ser confirmado por el organizador para validar la entrada.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si el pago no ha sido confirmado, el QR se genera, pero se marcará con estado "pendiente de validación", impidiendo su uso para ingresar al evento.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El código QR debe ser <strong>único</strong> por reserva, sin importar la cantidad de asientos.</p></li>
<li><p>El QR debe incluir toda la información necesaria para su validación.</p></li>
<li><p>El QR debe actualizar su estado a <strong>“válido”</strong> solo cuando el organizador confirme el pago.</p></li>
<li><p>El organizador debe poder escanear el QR desde un <strong>lector o aplicación integrada</strong> para validar la asistencia.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>La reserva queda confirmada y validada con un código QR funcional que permite el acceso al evento.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 22%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="2"><strong>Identificador:</strong> RF12</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>IOS Funcional</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="2"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Compatibilidad móvil</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="3"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Versión móvil del aplicativo</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 33%" />
<col style="width: 16%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="3"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="2">Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th colspan="2">Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario accede a la aplicación desde un dispositivo con sistema operativo iOS.</p></td>
<td colspan="2"><p><strong>Salida:</strong></p>
<p>El sistema responde mostrando las funcionalidades ba sicas disponibles en dicha plataforma.</p></td>
</tr>
<tr>
<td colspan="4"><p><strong>Descripción:</strong></p>
<p>Este requerimiento garantiza que el sistema pueda ejecutarse de manera ba sica y funcional en dispositivos con sistema operativo iOS.</p>
<p>La aplicación debe permitir la visualización de eventos, acceso al perfil del usuario, y lectura de códigos QR, asegurando la correcta comunicación con el servidor y las APIs principales.</p>
<p>No se garantiza el acceso completo a todas las funcionalidades disponibles en Android o la versión</p>
<p>web, pero debe realizar una prueba de compatibilidad que confirme el funcionamiento general del sistema.</p></td>
</tr>
<tr>
<td colspan="4"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El dispositivo debe contar con sistema operativo iOS actualizado.</p></li>
<li><p>El usuario debe tener acceso a internet.</p></li>
<li><p>El sistema debe estar en línea y con servicios operativos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="4"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si el sistema no logra ejecutar correctamente alguna funcionalidad, se mostrara un mensaje de error indicando incompatibilidad parcial.</p></li>
<li><p>Si la conexión con el servidor falla, el sistema notificara al usuario que verifique su conexión a internet.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="4"><p><strong>Criterios:</strong></p>
<ul>
<li><p>La aplicación debe ser capaz de abrir, cargar datos y ejecutar las funciones ba sicas en iOS.</p></li>
<li><p>Debe realizarse al menos una prueba de compatibilidad para verificar el funcionamiento general.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="4"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>Se confirma que el sistema es parcialmente funcional en iOS, garantizando las funciones esenciales (visualización de eventos, perfil y escaneo QR).</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 16%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="6"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="5">Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th colspan="5">Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="4"><strong>Identificador:</strong> RF13</td>
<td colspan="3"><p><strong>Nombre:</strong></p>
<p>Soporte multilenguaje (ES/EN/PT/JA)</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Interfaz de usuario</p></td>
<td colspan="2"><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td colspan="2"><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="5"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Disen o de la interfaz multilenguaje</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Entrada:</strong></p>
<p>El usuario accede a la aplicación y selecciona un idioma entre:</p>
<ul>
<li><p>Espan ol</p></li>
<li><p>Inglés</p></li>
<li><p>Portugués</p></li>
<li><p>Japonés</p></li>
</ul></td>
<td colspan="4"><p><strong>Salida:</strong></p>
<p>El sistema responde mostrando las funcionalidades ba sicas disponibles en dicha plataforma.</p></td>
</tr>
<tr>
<td colspan="7"><p><strong>Descripción:</strong></p>
<p>Este requerimiento garantiza que el sistema permita cambiar y mostrar la interfaz en cuatro idiomas: espan ol, inglés, portugués y japonés.</p>
<p>El sistema debe cargar dina micamente el contenido traducido, aplicarlo en la interfaz y mantener la coherencia visual y funcional sin necesidad de recargar completamente la aplicación.</p>
<blockquote>
<p>El usuario podra cambiar el idioma en cualquier momento desde el menu de configuración, y todos</p>
</blockquote>
<p>los elementos deben actualizarse inmediatamente.</p></td>
</tr>
<tr>
<td colspan="7"><p><strong>Precondición:</strong></p>
<ul>
<li><p>La aplicación debe contar con archivos, módulos o recursos de traducción para los 4 idiomas.</p></li>
<li><p>El dispositivo debe tener acceso a internet si las traducciones se cargan desde el servidor.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="7"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si no se encuentra una traducción disponible, el sistema mostrara el texto en el idioma por defecto (espan ol).</p></li>
<li><p>Si ocurre un error al cargar el paquete de idiomas, se enviará una notificación al usuario indicando que no fue posible cambiar el idioma.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El sistema debe permitir cambiar entre los 4 idiomas sin afectar el funcionamiento de la aplicación.</p></li>
<li><p>Todos los textos visibles deben mostrarse correctamente en el idioma seleccionado.</p></li>
<li><p>Deben existir pruebas que validen las traducciones completas para cada idioma.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<blockquote>
<p>La aplicación queda funcionando en el idioma elegido por el usuario, manteniendo coherencia en todos los textos y elementos de la interfaz</p>
</blockquote></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF14</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Dashboard</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Interfaz de usuario, mo dulo de administración, gestión de eventos</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Interfaz del Dashboard (usuario y administrador)</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario inicia sesión en el sistema con un rol válido (asistente, organizador o administrador).</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema responde mostrando las funcionalidades ba sicas disponibles en dicha plataforma.</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Descripción:</strong></p>
<p>Este requerimiento define la funcionalidad del Dashboard (panel de control), el cual se despliega inmediatamente después del inicio de sesión.</p>
<blockquote>
<p>El Dashboard debe ser dina mico y mostrar información relevante según el tipo de usuario:</p>
</blockquote>
<p><strong>Para usuarios asistentes:</strong></p>
<ul>
<li><p>Eventos disponibles</p></li>
<li><p>Eventos reservados</p></li>
<li><p>Estado de sus reservas (pendiente, confirmada)</p></li>
<li><p>Notificaciones importantes</p></li>
</ul>
<p><strong>Para organizadores:</strong></p>
<ul>
<li><p>Eventos creados</p></li>
<li><p>Nu mero de reservas por evento</p></li>
<li><p>Estado de validación de QR</p></li>
<li><p>Acceso directo al mo dulo de maquetación y creación de eventos</p></li>
</ul>
<p><strong>Para administradores:</strong></p>
<ul>
<li><p>Solicitudes pendientes de organizadores</p></li>
<li><p>Eventos generales del sistema</p></li>
<li><p>Usuarios activos</p></li>
<li><p>Acceso al mo dulo de validación y aprobación</p></li>
</ul>
<p>El Dashboard debe solicitar la información al backend y renderizarla en tiempo real, garantizando una vista clara, ra pida y actualizada.</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El usuario debe haber iniciado sesión.</p></li>
<li><p>El rol del usuario debe estar correctamente identificado por el sistema.</p></li>
<li><p>El servidor debe estar disponible para entregar la información solicitada.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si ocurre un error al cargar los datos del Dashboard, el sistema mostrara un mensaje: “No se pudo cargar la información del panel, intente más tarde.”</p></li>
<li><p>Si el rol del usuario no esta definido, se mostrara un mensaje de error y se redirigira a la pantalla de inicio de sesión.</p></li>
<li><p>En caso de falla de conexión, se mostrara una vista ba sica sin datos con opción a reintentar.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El sistema debe mostrar el Dashboard inmediatamente después del inicio de sesión.</p></li>
<li><p>La información debe ser precisa, actualizada y coherente con el rol del usuario.</p></li>
<li><p>El Dashboard debe cargarse en menos de 3 segundos bajo condiciones de red normales.</p></li>
<li><p>Debe permitir navegación directa a las funciones principales del sistema.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>El usuario permanece en su Dashboard con acceso directo a las funciones correspondientes a su rol.</p></li>
<li><p>La interfaz queda lista para ejecutar acciones como crear eventos, ver reservas, validar QR o gestionar solicitudes.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 22%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="2"><strong>Identificador:</strong> RF15</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Sistema de Refresh Token</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="2"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Autenticación persistente, seguridad de sesión, experiencia de usuario continua</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="3"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Flujo de renovación del token, diagrama de autenticación JWT, arquitectura de seguridad, endpoints de refresh token.</p></td>
</tr>
</tbody>
</table>

<img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image3.png" style="width:0.13889in;height:0.14583in" />

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 33%" />
<col style="width: 16%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="3"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="2">Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th colspan="2">Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario posee un Access Token expirado y un Refresh Token válido emitido previamente por el sistema.</p></td>
<td colspan="2"><p><strong>Salida:</strong></p>
<p>El sistema genera un nuevo Access Token (y opcionalmente un nuevo Refresh Token),</p>
<p>permitiendo continuar con la sesión sin requerir un nuevo inicio de sesión.</p></td>
</tr>
<tr>
<td colspan="4"><p><strong>Descripción:</strong></p>
<p>El sistema debe implementar un mecanismo de Refresh Token para mantener sesiones de usuario activas de forma segura. Cuando un Access Token expire, el cliente enviara automa ticamente el Refresh Token al endpoint /auth/refresh para obtener un nuevo Access Token. El Refresh Token</p>
<p>tendra una vida u til más larga (7-30 dí as) y permitira renovar Access Tokens sin requerir credenciales de usuario. El sistema validara la integridad del Refresh Token, verificara que no haya sido revocado, y generara nuevos tokens con claims actualizados.</p></td>
</tr>
<tr>
<td colspan="4"><p><strong>Precondición:</strong></p>
<ul>
<li><p>Usuario autenticado con tokens JWT válidos inicialmente.</p></li>
<li><p>Base de datos con tabla de Refresh Tokens revocados.</p></li>
<li><p>Endpoint /auth/refresh implementado en el backend.</p></li>
<li><p>Cliente (web/móvil) con lógica de renovación automática.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="4"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Refresh Token inválido o manipulado:</p></li>
</ul>
<blockquote>
<p>El sistema rechazará la solicitud y mostrará un mensaje de autenticación fallida.</p>
</blockquote>
<ul>
<li><p>Refresh Token expirado:</p></li>
</ul>
<blockquote>
<p>El usuario será redirigido a la pantalla de inicio de sesión.</p>
</blockquote>
<ul>
<li><p>Refresh Token revocado o no registrado:</p></li>
</ul>
<blockquote>
<p>El sistema cerrará la sesión y solicitará al usuario volver a autenticarse.</p>
</blockquote>
<ul>
<li><p>Falla de conexión o servidor:</p></li>
</ul>
<blockquote>
<p>Se mostrará un mensaje indicando que no fue posible renovar el token.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="4"><p><strong>Criterios:</strong></p>
<ul>
<li><p>El sistema debe generar un nuevo Access Token de manera automática y segura.</p></li>
<li><p>La sesión no debe interrumpirse mientras el Refresh Token siga siendo válido.</p></li>
<li><p>La renovación debe tardar menos de 2 segundos.</p></li>
<li><p>El usuario no debe notar la expiración del Access Token durante la navegación.</p></li>
<li><p>Debe existir un registro del uso de Refresh Tokens para su posible revocación.</p></li>
<li><p>Los Refresh Tokens deben poder revocarse individualmente o masivamente.</p></li>
<li><p>Implementación de rate limiting en endpoint de refresh.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><ul>
<li><p>Auditoría completa de renovaciones de token.</p></li>
</ul></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>El usuario mantiene la sesión activa sin necesidad de volver a ingresar sus credenciales.</p></li>
<li><p>El Access Token queda actualizado y listo para usar.</p></li>
<li><p>El sistema continu a garantizando seguridad y continuidad en la autenticación.</p></li>
<li><p>Se registra la renovación para auditorí a de seguridad.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 16%" />
<col style="width: 16%" />
<col style="width: 16%" />
<col style="width: 20%" />
<col style="width: 20%" />
</colgroup>
<tbody>
<tr>
<td colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF16</p>
</blockquote></td>
<td colspan="2"><blockquote>
<p><strong>Nombre:</strong></p>
</blockquote>
<p>Sistema de Notificaciones Push y Recordatorios Automáticos</p></td>
</tr>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
<p>Necesario</p>
</blockquote></td>
<td colspan="3"><blockquote>
<p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Compromiso de usuarios, recordatorios de eventos, notificaciones en tiempo real, confirmaciones transaccionales</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<p>No</p></td>
</tr>
<tr>
<td><blockquote>
<p><strong>Prioridad de desarrollo:</strong></p>
<p>Media</p>
</blockquote></td>
<td colspan="4"><blockquote>
<p><strong>Documentos de visualización asociados:</strong></p>
</blockquote>
<p>Arquitectura de Firebase Cloud Messaging, diagramas de flujo de notificaciones, mockups de notificaciones en móvil, configuración de tokens de dispositivo, reglas de recordatorios programados.</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
<p>Evento del sistema requiere notificar al usuario (pago confirmado, evento próximo, cambio en reserva, recordatorio automático).</p>
</blockquote></td>
<td colspan="3"><blockquote>
<p><strong>Salida:</strong></p>
<p>Notificación push llega al dispositivo del usuario con información relevante y recordatorios automáticos enviados en momentos programados.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
<p>El sistema debe implementar notificaciones push nativas usando Firebase Cloud Messaging (FCM) para mantener engagement continuo con los usuarios, además de un sistema de recordatorios automáticos programados. El backend registrará tokens de dispositivo de usuarios móviles y navegadores web, permitiendo envío de notificaciones personalizadas basadas en eventos del sistema. Las notificaciones incluirán título, mensaje, ícono, sonido, y datos adicionales para deep linking a secciones específicas de la app. Los usuarios podrán configurar preferencias de notificación (activar/desactivar tipos específicos). El sistema incluirá recordatorios automáticos enviados 48 horas y 2 horas antes de eventos reservados, con integración completa entre notificaciones push y sistema de recordatorios.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>Proyecto Firebase configurado con FCM habilitado.</p></li>
<li><p>Aplicación móvil con expo-notifications instalado.</p></li>
<li><p>Aplicación web con Firebase SDK configurado.</p></li>
<li><p>Backend con modelo DeviceToken implementado.</p></li>
<li><p>Usuarios con permisos de notificaciones concedidos.</p></li>
<li><p>Sistema de eventos con fechas y horas programadas.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote>
<ul>
<li><p>Token de dispositivo inválido o expirado:<br />
Se elimina automáticamente del registro y se solicita nuevo token al usuario.</p></li>
<li><p>FCM service unavailable temporalmente:<br />
Notificaciones se encolan y reenvían con exponential backoff.</p></li>
<li><p>Usuario sin permisos de notificación:</p></li>
</ul>
<blockquote>
<p>Sistema envía notificación por email como fallback automático.</p>
</blockquote>
<ul>
<li><p>Dispositivo offline:<br />
FCM entrega la notificación cuando el dispositivo se conecte.</p></li>
<li><p>Recordatorio no enviado por error de programación:<br />
Sistema registra el fallo y reintenta en siguiente ciclo.</p></li>
<li><p>Usuario canceló reserva después de programar recordatorio:<br />
Sistema automáticamente cancela recordatorios pendientes.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Criterios:</strong></p>
</blockquote>
<ul>
<li><p>Registro automático de device tokens al iniciar sesión en la app.</p></li>
<li><p>Notificaciones de confirmación de pago procesadas en menos de 5 segundos.</p></li>
<li><p>Recordatorios de eventos enviados 48 horas antes del evento.</p></li>
<li><p>Alertas de cambios en reservas (cancelaciones, modificaciones) inmediatas.</p></li>
<li><p>Notificaciones administrativas para comunicaciones importantes.</p></li>
<li><p>Deep linking funcional a secciones específicas de la app.</p></li>
<li><p>Configuración de preferencias de notificación por usuario.</p></li>
<li><p>Tasa de entrega &gt;95% para notificaciones push activas.</p></li>
<li><p>Fallback automático a email cuando push no esté disponible.</p></li>
<li><p>Métricas de entrega, apertura y conversión registradas.</p></li>
<li><p>Auditoría de notificaciones enviadas por seguridad.</p></li>
<li><p>Sistema de recordatorios programados y automáticos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Postcondición:</strong></p>
</blockquote>
<ul>
<li><p>Usuarios reciben notificaciones en tiempo real en sus dispositivos.</p></li>
<li><p>Recordatorios automáticos aseguran asistencia a eventos.</p></li>
<li><p>Mejora significativa del engagement y reducción de no-shows en eventos.</p></li>
<li><p>Sistema de comunicación bidireccional establecido con los usuarios.</p></li>
<li><p>Usuarios informados oportunamente sobre sus eventos reservados.</p></li>
<li><p>Métricas de notificaciones disponibles para análisis y optimización.</p></li>
<li><p>Experiencia de usuario mejorada con notificaciones contextuales y recordatorios automáticos.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:89%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF17</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Pasarela de pagos sandbox</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Reservas, generación de</p>
<p>entradas, validación de pagos, QR</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Flujo de pago, endpoints de la pasarela sandbox, confirmación de transaccion, webhook de la pasarela</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario selecciona pagar un evento con la pasarela de pagos sandbox.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>Pago procesado y reserva confirmada con QR activo.</p></td>
</tr>
<tr>
<td colspan="5"><p><strong>Descripción:</strong></p>
<p>El sistema debe permitir realizar pagos mediante la pasarela de pagos sandbox, conectada al backend, sin cobros reales.</p>
<p>El backend crea la transacción, la pasarela sandbox procesa el pago y enví a un webhook que confirma si fue aprobado o rechazado.</p>
<blockquote>
<p>Si el pago se aprueba, la reserva se activa y se genera el QR.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><p><strong>Precondición:</strong></p>
<ul>
<li><p>Usuario autenticado.</p></li>
<li><p>Evento con cupos.</p></li>
<li><p>Pasarela de pagos sandbox configurada y en línea.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Pago rechazado = no se crea reserva.</p></li>
<li><p>Pago pendiente = estado “Pendiente”.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><ul>
<li><p>Webhook fallido = reintento.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios:</strong></p>
<ul>
<li><p>Pago debe confirmarse vía webhook.</p></li>
<li><p>QR solo se genera si el estado es “APPROVED”.</p></li>
<li><p>Registro del pago con ID y estado.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<blockquote>
<p>Reserva activa + QR disponible para ingreso.</p>
</blockquote></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF18</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Comprar boletas desde la aplicación móvil</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Mo dulo móvil, mo dulo de compra de boletas, vista del plano del evento</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Vista móvil del plano, selección de sillas y pantalla de compra.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El usuario abre un evento desde la aplicación móvil.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>Se muestra el plano del evento, sillas disponibles y la opción de comprar boletas sin herramientas de maquetación.</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Descripción:</strong></p>
<p>Este requerimiento define que en la versión móvil el usuario únicamente podra visualizar la maquetación del evento sin herramientas de edición. Desde la aplicación móvil el usuario debe poder ver el plano del evento, identificar las sillas disponibles, seleccionar las que desee comprar y finalizar la adquisición de boletas. La generación del ticket o código QR estara disponible luego de la compra. La creación, edición o maquetación del evento no estara disponible en la aplicación móvil, ya que estas funcionalidades son exclusivas de la versión web.</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>Evento ya creado y maquetado en la versión web.</p></li>
<li><p>Usuario autenticado (si el flujo de compra lo requiere).</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si el plano no carga, el sistema mostrará un mensaje de error.</p></li>
<li><p>Si una silla ya está ocupada, el sistema actualizará la disponibilidad antes de continuar con la compra.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios</strong></p>
<ul>
<li><p>El usuario debe poder visualizar correctamente el plano del evento en la aplicación móvil.</p></li>
<li><p>El usuario debe poder seleccionar sillas disponibles.</p></li>
<li><p>El usuario debe poder completar la compra de boletas sin opciones de edición o maquetación.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>La boleta queda comprada.</p></li>
<li><p>El ticket o código QR queda disponible para el usuario.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 49%" />
<col style="width: 40%" />
</colgroup>
<thead>
<tr>
<th><strong>Identificador:</strong> RF19</th>
<th><p><strong>Nombre:</strong></p>
<p>Maquetación y gestión de eventos desde la versión web</p></th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 16%" />
<col style="width: 17%" />
<col style="width: 11%" />
<col style="width: 5%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="5"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="4">Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th colspan="4">Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="2"><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="2"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Panel del organizador, editor de plano, creación de eventos</p></td>
<td colspan="2"><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td colspan="2"><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Vista móvil del plano del evento, pantalla de selección de sillas y pantalla de compra en la aplicación móvil.</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Entrada:</strong></p>
<p>El organizador accede a la plataforma desde la versión web.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>El sistema muestra todas las herramientas de maquetación y gestión del evento disponibles en la interfaz web.</p></td>
</tr>
<tr>
<td colspan="6"><p><strong>Descripción:</strong></p>
<p>Este requerimiento define que la creación del evento, la maquetación del plano y la edición completa de la estructura del escenario estarán disponibles únicamente en la versión web. Desde la plataforma web, el organizador podra crear eventos, diseñar el plano del escenario, agregar secciones, distribuir sillas, asignar áreas, modificar el diseño del evento y gestionar todos los elementos relacionados con su estructura. Estas funcionalidades no estarán presentes en la versión móvil, ya que esta solo</p>
<p>permite la visualización del plano y la compra de boletas, manteniendo la maquetación</p>
<p>exclusivamente en la web para asegurar una experiencia más completa y óptima para el organizador.</p></td>
</tr>
<tr>
<td colspan="6"><p><strong>Precondición:</strong></p>
<ul>
<li><p>Organizador autenticado en la versión web.</p></li>
<li><p>Permisos activos de creación y edición de eventos.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="6"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si alguna herramienta de maquetación falla, se mostrará un mensaje de error.</p></li>
<li><p>Si el evento no puede guardarse, el sistema notificará al usuario que intente nuevamente.</p></li>
<li><p>Si no se cargan los elementos del editor, el sistema deberá recargar la vista o indicar fallo de carga.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Criterios</strong></p>
<ul>
<li><p>El organizador debe poder crear, editar y guardar eventos completamente desde la versión web.</p></li>
<li><p>El editor de plano debe funcionar sin restricciones para añadir, mover o eliminar elementos.</p></li>
<li><p>La versión móvil no debe permitir el acceso a herramientas de creación o edición.</p></li>
<li><p>Los cambios guardados deben reflejarse correctamente al ser visualizados en la aplicación móvil.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>El evento queda creado o actualizado correctamente.</p></li>
<li><p>El plano maquetado queda disponible para ser visualizado desde la versión móvil.</p></li>
<li><p>La información del evento queda almacenada para su consulta en cualquier plataforma.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF20</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Orientado a microservicios</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Necesario</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Gestión de usuarios, eventos, pagos, notificaciones y administración</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>Si</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Diagrama de arquitectura de microservicios, contratos de API, flujos de comunicación entre servicios, esquemas de base de datos por servicio</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>El sistema recibe una solicitud de operación que requiere coordinación entre múltiples dominios (usuario + evento + pago).</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>Los servicios independientes procesan la solicitud y responden de forma coordinada mediante comunicación así ncrona.</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Descripción:</strong></p>
<p>Este requerimiento define que el sistema debe migrar a una arquitectura de microservicios,</p>
<p>separando las funcionalidades en servicios independientes: User Service, Event Service, Payment Service, Notification Service y Admin Service. Desde el API Gateway, los servicios podrán comunicarse mediante message queues y APIs REST, gestionar bases de datos separadas, implementar tolerancia a fallos con circuit breakers, y mantener consistencia eventual mediante el patrón Saga. Estas</p>
<p>funcionalidades estarán disponibles tanto en web como móvil, manteniendo la separación de responsabilidades exclusivamente en la arquitectura backend para asegurar escalabilidad, mantenibilidad y tolerancia a fallos del sistema.</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>Arquitectura monolítica modular implementada.</p></li>
<li><p>Equipo con experiencia en microservicios.</p></li>
<li><p>Infraestructura cloud disponible (Docker/Kubernetes).</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Si un servicio falla, se activará circuit breaker y fallback response.</p></li>
<li><p>Si la comunicación entre servicios se interrumpe, el sistema usará colas de mensajes con reintento.</p></li>
<li><p>Si hay inconsistencia de datos, se aplicará compensación mediante patrón Saga.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios</strong></p>
<ul>
<li><p>Los servicios deben desplegarse y escalarse de forma independiente.</p></li>
<li><p>La comunicación entre servicios debe ser mediante APIs contractadas y eventos.</p></li>
<li><p>Cada servicio debe tener su propia base de datos con consistencia eventual.</p></li>
<li><p>El API Gateway debe manejar autenticación, rate limiting y enrutamiento.</p></li>
<li><p>Los cambios en un servicio no deben afectar la operación de los demás.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>Los servicios operan de forma desacoplada con comunicación asíncrona.</p></li>
<li><p>El sistema es escalable horizontalmente por dominio funcional.</p></li>
<li><p>Los fallos en un servicio no impactan la disponibilidad de los demás.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 16%" />
<col style="width: 17%" />
<col style="width: 11%" />
<col style="width: 5%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="5"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="4">Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th colspan="4">Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<tbody>
<tr>
<td colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF21</p>
</blockquote></td>
<td colspan="2"><blockquote>
<p><strong>Nombre:</strong></p>
<p>Migración Backend a Arquitectura N-Capas con DDD</p>
</blockquote></td>
</tr>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
<p>Necesario</p>
</blockquote></td>
<td colspan="3"><blockquote>
<p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Backend Java, modularización del dominio, organización por capas, preparación para futura separación en microservicios.</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si</p></td>
</tr>
<tr>
<td><blockquote>
<p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p>
</blockquote></td>
<td colspan="4"><blockquote>
<p><strong>Documentos de visualización asociados:</strong></p>
<p>Estructura de paquetes por módulo, diagramas de dominio, contratos de interfaces, diseño de capas (Domain, Application, Infrastructure, Presentation).</p>
</blockquote></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
<p>Backend Spring Boot monolítico estructurado sin separación clara por dominios.</p>
</blockquote></td>
<td colspan="3"><blockquote>
<p><strong>Salida:</strong></p>
<p>Backend modular organizado por dominios y capas siguiendo principios DDD simplificados.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong></p>
<p>Este requerimiento establece que el backend debe reorganizarse en una arquitectura en n-capas con enfoque modular inspirado en Domain-Driven Design. El sistema se dividirá en módulos por dominio (Usuarios, Eventos, Pagos, Notificaciones, Administración), donde cada módulo mantendrá una estructura interna por capas: Domain (entidades, lógica central), Application (servicios de aplicación y casos de uso), Infrastructure (repositorios, acceso a datos) y Presentation (controladores). Esta organización permitirá una separación lógica clara, un código más mantenible y una base estable para una futura migración a microservicios sin comprometer la entrega actual del proyecto.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>Backend actual funcionando.</p></li>
<li><p>Equipo con manejo básico de Spring Boot.</p></li>
<li><p>Base de datos configurada.</p></li>
<li><p>Docker disponible para empaquetar el backend.</p></li>
<li><p>Diseño modular aprobado.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote>
<ul>
<li><p>Si ocurre un error en un módulo, debe aislarse sin afectar los demás.</p></li>
<li><p>Fallos de infraestructura se deben manejar mediante excepciones controladas.</p></li>
<li><p>Pérdida de acceso a datos debe generar respuestas de error claras y seguras.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Criterios</strong></p>
</blockquote>
<ul>
<li><p>Los módulos deben organizarse por dominio.</p></li>
<li><p>Las capas deben estar separadas evitando dependencias incorrectas.</p></li>
<li><p>La lógica de negocio debe concentrarse en la capa Domain.</p></li>
<li><p>La capa Application debe contener los casos de uso sin lógica duplicada.</p></li>
<li><p>La capa Infrastructure debe implementar los repositorios y adaptadores externos.</p></li>
<li><p>La capa Presentation debe exponer controladores usando DTOs.</p></li>
<li><p>El código debe ser mantenible, legible y preparado para migrar a microservicios.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Postcondición:</strong></p>
</blockquote>
<ul>
<li><p>El backend queda modularizado por dominios y capas.</p></li>
<li><p>Se mejora la mantenibilidad del sistema.</p></li>
<li><p>El proyecto queda preparado para futura separación en microservicios.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 26%" />
<col style="width: 17%" />
<col style="width: 5%" />
<col style="width: 5%" />
<col style="width: 35%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><strong>Identificador:</strong> RF22</th>
<th colspan="2"><p><strong>Nombre:</strong></p>
<p>Sistema de Temas de Interfaz</p></th>
</tr>
</thead>
<tbody>
<tr>
<td><p><strong>Tipo:</strong></p>
<p>Mejora</p></td>
<td colspan="3"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<p>Personalización de UI,</p>
<p>accesibilidad, experiencia de usuario</p></td>
<td><p><strong>¿Crítico?</strong></p>
<blockquote>
<p>No</p>
</blockquote></td>
</tr>
<tr>
<td><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta</p></td>
<td colspan="4"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Paleta de colores por tema, mockups de cada tema aplicado, guí a de implementación de temas</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Entrada:</strong></p>
<p>Usuario selecciona un tema desde la configuración de la aplicación.</p></td>
<td colspan="3"><p><strong>Salida:</strong></p>
<p>La interfaz completa cambia al tema seleccionado manteniendo consistencia visual.</p></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 29%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="3"><p><strong>Descripción:</strong></p>
<p>El sistema debe implementar un sistema completo de temas para personalizar la apariencia de la</p>
<p>aplicación. Se proporcionarán 4 temas predefinidos: Tema Claro (blanco), Tema Oscuro (negro), Tema Morado (purple), y Tema Azul (blue). Cada tema definira colores para backgrounds, textos, botones,</p>
<p>í conos, y componentes especí ficos manteniendo jerarquí a visual y contraste adecuado. El tema</p>
<p>seleccionado se guardara en las preferencias del usuario y se aplicara automa ticamente al iniciar la aplicación. Los temas serán consistentes entre la versión web y móvil, con capacidad de detectar preferencia del sistema operativo (dark/light mode).</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Precondición:</strong></p>
<ul>
<li><p>Sistema de diseño establecido con variables CSS/Tailwind.</p></li>
<li><p>Configuración de usuario implementada (localStorage/AsyncStorage).</p></li>
<li><p>Componentes UI preparados para theming dina mico.</p></li>
<li><p>Paletas de colores definidas y testeadas para accesibilidad.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Manejo de situaciones anormales:</strong></p>
<ul>
<li><p>Tema no soportado en dispositivo:</p></li>
</ul>
<blockquote>
<p>Se aplica tema por defecto (claro) con notificación al usuario.</p>
</blockquote>
<ul>
<li><p>Configuración corrupta:</p></li>
</ul>
<blockquote>
<p>Se resetea a tema claro y se guarda configuración válida.</p>
</blockquote>
<ul>
<li><p>Cambio de tema falla:</p></li>
</ul>
<blockquote>
<p>Se mantiene tema anterior y se muestra mensaje de error temporal.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="3"><p><strong>Criterios</strong></p>
<ul>
<li><p>4 temas predefinidos implementados: Claro, Oscuro, Morado, Azul.</p></li>
<li><p>Cambio de tema instantáneo sin recarga de aplicación.</p></li>
<li><p>Persistencia de selección de tema entre sesiones.</p></li>
<li><p>Detección automática de preferencia del sistema (OS dark/light mode).</p></li>
<li><p>Temas consistentes entre plataformas web y móvil.</p></li>
<li><p>Animaciones suaves en transición entre temas.</p></li>
<li><p>Configuración de tema accesible desde menú de usuario.</p></li>
<li><p>Temas aplicados a todos los componentes: botones, modales, formularios, navegación.</p></li>
<li><p>Rendimiento no afectado por cambio de temas.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="3"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>Usuario puede personalizar la apariencia de la aplicación según preferencia.</p></li>
<li><p>Mejora la accesibilidad para usuarios con sensibilidad visual.</p></li>
<li><p>Interfaz se adapta al branding y estado de ánimo del usuario.</p></li>
<li><p>Experiencia de usuario más personalizada y agradable.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:89%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 6%" />
<col style="width: 16%" />
<col style="width: 16%" />
<col style="width: 11%" />
<col style="width: 8%" />
<col style="width: 20%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"></th>
<th colspan="6"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="4"><blockquote>
<p>Documento: Especificación de Requerimientos</p>
</blockquote></th>
<th colspan="2"><blockquote>
<p>Versión: <strong>1.0</strong></p>
</blockquote></th>
</tr>
<tr>
<th colspan="4"><blockquote>
<p>Clave: MTS-07</p>
</blockquote></th>
<th colspan="2"><blockquote>
<p>Fecha: 26/02/2025</p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="4"><strong>Identificador:</strong> RF23</td>
<td colspan="3"><p><strong>Nombre:</strong></p>
<p>Filtro de eventos.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Tipo:</strong></p>
<p>Necesario.</p></td>
<td colspan="4"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<blockquote>
<p>Interfaz de agendamiento</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Prioridad de desarrollo:</strong></p>
<p>Media</p></td>
<td colspan="5"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Página agendamiento</p></td>
</tr>
<tr>
<td colspan="3"><p><strong>Entrada:</strong></p>
<p>El usuario que estara en la interfaz gra fica tendra que poner su</p>
<p>ubicación y tendra la opción de poner cosas como nombre del evento, ciudad, disponibilidad, presupuesto, el tipo de evento.</p></td>
<td colspan="4"><p><strong>Salida:</strong></p>
<p>Los resultados le saldrán con las cualidades que haya puesto el usuario.</p></td>
</tr>
<tr>
<td colspan="7"><p><strong>Descripción:</strong></p>
<p>El sistema debe permitir a los usuarios buscar los eventos que deseen, esto les permitira una mayor visualización a los organizadores y al usuario comu n un mayor ana lisis de la disponibilidad.</p></td>
</tr>
<tr>
<td colspan="7"><p><strong>Precondición:</strong></p>
<ul>
<li><p>El usuario debe tener una red estable para poder tener una buena experiencia a la hora de buscar.</p></li>
<li><p>El usuario debe ingresar algu n valor para poder hacer una bu squeda por filtros, sea nombre, fecha y dí a.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="7"><p><strong>Manejo de situaciones anormales:</strong></p>
<p>Hay problemas de conexión entre el cliente y el servidor, lo que impide que la acción de bu squeda se complete. Donde el sistema debe mostrar un mensaje de error y permitir al usuario reintentar la</p>
<p>acción cuando la conexión se restablezca.</p></td>
</tr>
<tr>
<td colspan="7"><p><strong>Criterios:</strong></p>
<p>Los usuarios tienen que estar en la interfaz encargada de mostrar los eventos y podra investigar pudiendo hacerlo con condiciones especí ficas que desee e l usuario.</p></td>
</tr>
<tr>
<td colspan="7"><p><strong>Postcondición:</strong></p>
<ul>
<li><p>El programa responde mostrando los eventos filtrados por los te rminos que el usuario haya ejecutado.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:90%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 6%" />
<col style="width: 16%" />
<col style="width: 16%" />
<col style="width: 10%" />
<col style="width: 9%" />
<col style="width: 19%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><blockquote>
<p><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></p>
</blockquote></th>
<th colspan="6"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="4"><blockquote>
<p>Documento: Especificación de Requerimientos</p>
</blockquote></th>
<th colspan="2"><blockquote>
<p>Versión: <strong>1.0</strong></p>
</blockquote></th>
</tr>
<tr>
<th colspan="4"><blockquote>
<p>Clave: MTS-07</p>
</blockquote></th>
<th colspan="2"><blockquote>
<p>Fecha: 26/02/2025</p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="4"><blockquote>
<p><strong>Identificador:</strong> RF24</p>
</blockquote></td>
<td colspan="3"><p><strong>Nombre:</strong></p>
<p>Lector de QR para validación de asistencia</p></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Tipo:</strong></p>
</blockquote>
<p>Necesario.</p></td>
<td colspan="4"><p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
<blockquote>
<p>Página de validación de QR.</p>
</blockquote></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si.</p></td>
</tr>
<tr>
<td colspan="2"><p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta.</p></td>
<td colspan="5"><p><strong>Documentos de visualización asociados:</strong></p>
<p>Interfaz de detalles del evento del organizador.</p></td>
</tr>
<tr>
<td colspan="3"><blockquote>
<p><strong>Entrada:</strong></p>
<p>El usuario (asistente) presenta su código QR generado en la reserva confirmada y el organizador o personal de control lo escanea con la ca mara del dispositivo desde la</p>
<p>aplicación móvil.</p>
</blockquote></td>
<td colspan="4"><p><strong>Salida:</strong></p>
<p>El sistema valida el QR en tiempo real y, si es correcto, registra la asistencia y autoriza el acceso al evento.</p></td>
</tr>
<tr>
<td colspan="7"><blockquote>
<p><strong>Descripción:</strong></p>
<p>El lector de QR del sistema permitira verificar la autenticidad de los códigos generados para</p>
<p>reservas confirmadas. Una vez escaneado, el sistema validara que el QR sea válido, no este duplicado, pertenezca al evento correspondiente.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="7"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>Debe existir un código QR generado previamente (RF12) asociado a una reserva confirmada.</p></li>
<li><p>El dispositivo debe tener acceso a la ca mara y conexión estable al sistema.</p></li>
<li><p>El evento debe estar activo y en estado de validación de entradas.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="7"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote>
<ul>
<li><p>Si el QR es inválido, caducado o ya utilizado, el sistema debe notificar al validador y denegar el acceso.</p></li>
<li><p>​</p></li>
</ul></td>
</tr>
<tr>
<td colspan="7"><blockquote>
<p><strong>Criterios:</strong></p>
</blockquote>
<ul>
<li><p>El lector QR debe funcionar en tiempo real desde dispositivos móviles.</p></li>
<li><p>La validación debe garantizar seguridad y unicidad, evitando duplicaciones.</p></li>
<li><p>El sistema debe mostrar mensajes claros y diferenciados: acceso autorizado, QR inválido, QR caducado, QR ya utilizado.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="7"><blockquote>
<p><strong>Postcondición:</strong></p>
</blockquote>
<ul>
<li><p>La asistencia del usuario queda registrada en el sistema y vinculada al evento</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:89%;">
<colgroup>
<col style="width: 16%" />
<col style="width: 16%" />
<col style="width: 16%" />
<col style="width: 19%" />
<col style="width: 19%" />
</colgroup>
<thead>
<tr>
<th colspan="3"><blockquote>
<p><strong>Identificador:</strong> RF25</p>
</blockquote></th>
<th colspan="2"><blockquote>
<p><strong>Nombre:</strong></p>
<p>Cartera</p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Tipo:</strong></p>
</blockquote>
<p>Necesario.</p></td>
<td colspan="3"><blockquote>
<p><strong>Requerimiento que lo utiliza o especializa:</strong></p>
</blockquote>
<p>Pagos (RF17), Reservas (RF10), Compra de boletas (RF18), Dashboard (RF14)</p></td>
<td><p><strong>¿Crítico?</strong></p>
<p>Si.</p></td>
</tr>
<tr>
<td><blockquote>
<p><strong>Prioridad de desarrollo:</strong></p>
<p>Alta.</p>
</blockquote></td>
<td colspan="4"><blockquote>
<p><strong>Documentos de visualización asociados:</strong></p>
<p>Dashboard de usuario, módulo de pagos, historial de transacciones, confirmación de compra</p>
</blockquote></td>
</tr>
<tr>
<td colspan="2"><blockquote>
<p><strong>Entrada:</strong></p>
</blockquote>
<ul>
<li><p>Usuario autenticado</p></li>
<li><p>Monto a recargar en la cartera</p></li>
<li><p>Método de pago habilitado (pasarela de pagos sandbox)</p></li>
<li><p>Solicitud de uso de saldo para compra de boletas o reembolso</p></li>
</ul></td>
<td colspan="3"><blockquote>
<p><strong>Salida:</strong></p>
</blockquote>
<ul>
<li><p>Saldo actualizado en la cartera del usuario</p></li>
<li><p>Confirmación visual de recarga, uso o reembolso</p></li>
<li><p>Registro de la transacción en el historial del usuario</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Descripción:</strong> Este requerimiento define la funcionalidad de Cartera Digital interna del sistema “Tu Evento”, la cual permitirá a los usuarios manejar dinero real prepagado dentro de la plataforma para realizar la compra de boletas, recibir reembolsos y consultar su saldo disponible. El sistema permitirá al usuario recargar saldo en su cartera mediante la pasarela de pagos sandbox, utilizar el saldo disponible para efectuar compras dentro del aplicativo, recibir reembolsos aprobados directamente en la cartera y consultar el historial de movimientos correspondientes a recargas, compras y reembolsos. La cartera funcionará como un sistema cerrado, en el cual el saldo solo podrá ser utilizado dentro de la plataforma “Tu Evento” y no podrá ser retirado a cuentas bancarias externas.</p>
</blockquote></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Precondición:</strong></p>
</blockquote>
<ul>
<li><p>El usuario debe estar autenticado.</p></li>
<li><p>La cuenta del usuario debe estar activa.</p></li>
<li><p>El sistema debe estar en línea.</p></li>
<li><p>La pasarela de pagos sandbox debe estar operativa para recargas.</p></li>
<li><p>El usuario debe contar con un perfil válido.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Manejo de situaciones anormales:</strong></p>
</blockquote>
<ul>
<li><p>Si el pago por la pasarela sandbox falla, el saldo no se actualiza.</p></li>
<li><p>Si el saldo es insuficiente, el sistema bloquea la compra y notifica al usuario.</p></li>
<li><p>Si ocurre una interrupción de conexión durante la recarga, el sistema valida el estado con la pasarela sandbox antes de reflejar el saldo.</p></li>
<li><p>Si un reembolso no cumple las políticas definidas, el sistema notifica el rechazo.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Criterios:</strong></p>
</blockquote>
<ul>
<li><p>El saldo debe reflejarse correctamente tras una recarga aprobada.</p></li>
<li><p>El uso de saldo debe descontarse en tiempo real.</p></li>
<li><p>Cada movimiento debe registrarse en el historial del usuario.</p></li>
<li><p>Los reembolsos deben devolverse exclusivamente a la cartera.</p></li>
<li><p>El sistema debe impedir cualquier intento de retiro externo del saldo.</p></li>
<li><p>El saldo debe mostrarse actualizado en el Dashboard.</p></li>
</ul></td>
</tr>
<tr>
<td colspan="5"><blockquote>
<p><strong>Postcondición:</strong></p>
</blockquote>
<ul>
<li><p>El usuario cuenta con un saldo actualizado en su cartera.</p></li>
<li><p>Las transacciones quedan registradas de forma segura.</p></li>
<li><p>El usuario puede continuar comprando boletas sin repetir el proceso de pago externo.</p></li>
<li><p>El sistema mantiene trazabilidad financiera de cada movimiento.</p></li>
</ul></td>
</tr>
</tbody>
</table>

<table style="width:89%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 28%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><blockquote>
<p><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></p>
</blockquote></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th><blockquote>
<p>Documento: Especificación de Requerimientos</p>
</blockquote></th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th><blockquote>
<p>Clave: MTS-07</p>
</blockquote></th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
</tbody>
</table>

1.  **Requerimientos No Funcionales**

<table style="width:66%;">
<colgroup>
<col style="width: 21%" />
<col style="width: 44%" />
</colgroup>
<thead>
<tr>
<th><blockquote>
<p><strong>Identificación del requerimiento:</strong></p>
</blockquote></th>
<th>RNF1</th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Nombre del requerimiento:</strong></p>
</blockquote></td>
<td>Mensajes de error</td>
</tr>
<tr>
<td><blockquote>
<p><strong>Características:</strong></p>
</blockquote></td>
<td>Se mostrara en pantalla un mensaje de error cuando el usuario haya ingresado o realizado incorrectamente un procedimiento.</td>
</tr>
<tr>
<td><blockquote>
<p><strong>Descripción del requerimiento:</strong></p>
</blockquote></td>
<td><p>Los mensajes de error deben seguir un formato uniforme en todo el sistema para que el usuario</p>
<p>reconozca inmediatamente cuando ocurre un error.</p></td>
</tr>
<tr>
<td colspan="2"><strong>Prioridad del requerimiento:</strong> Medio</td>
</tr>
</tbody>
</table>

<table style="width:66%;">
<colgroup>
<col style="width: 21%" />
<col style="width: 44%" />
</colgroup>
<thead>
<tr>
<th><blockquote>
<p><strong>Identificación del requerimiento:</strong></p>
</blockquote></th>
<th><blockquote>
<p>RNF2</p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Nombre del requerimiento:</strong></p>
</blockquote></td>
<td><blockquote>
<p>Disen o responsive</p>
</blockquote></td>
</tr>
<tr>
<td><blockquote>
<p><strong>Características:</strong></p>
</blockquote></td>
<td>Permite que el diseño se visualice desde un computador o cualquier dispositivo.</td>
</tr>
<tr>
<td><blockquote>
<p><strong>Descripción del requerimiento:</strong></p>
</blockquote></td>
<td><p>El diseño del sistema se desarrollara de manera que</p>
<p>pueda visualizarse correctamente desde cualquier dispositivo, ya sea un computador o tele fono.</p></td>
</tr>
<tr>
<td colspan="2"><strong>Prioridad del requerimiento: Alto</strong></td>
</tr>
</tbody>
</table>

<table style="width:89%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 50%" />
<col style="width: 28%" />
</colgroup>
<thead>
<tr>
<th rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="2"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th>Documento: Especificación de Requerimientos</th>
<th style="text-align: right;">Versión: <strong>1.0</strong></th>
</tr>
<tr>
<th>Clave: MTS-07</th>
<th style="text-align: right;">Fecha: 26/02/2025</th>
</tr>
</thead>
<tbody>
</tbody>
</table>

<table style="width:66%;">
<colgroup>
<col style="width: 21%" />
<col style="width: 44%" />
</colgroup>
<thead>
<tr>
<th><blockquote>
<p><strong>Identificación del requerimiento:</strong></p>
</blockquote></th>
<th>RNF3</th>
</tr>
</thead>
<tbody>
<tr>
<td><blockquote>
<p><strong>Nombre del requerimiento:</strong></p>
</blockquote></td>
<td>Seguridad</td>
</tr>
<tr>
<td><blockquote>
<p><strong>Características:</strong></p>
</blockquote></td>
<td><p>Los usuarios deben ser correctamente identificados mediante mecanismos de autenticación seguros, como sus propias contrasen as y la autenticación de dos pasos (Con inicio de redes sociales no se aplicara dos pasos). Asegurando que solo las personas autorizadas</p>
<p>accedan al sistema.</p></td>
</tr>
<tr>
<td><blockquote>
<p><strong>Descripción del requerimiento:</strong></p>
</blockquote></td>
<td>Los usuarios deberán identificarse en el sistema mediante su correo electrónico y una contrasen a.</td>
</tr>
<tr>
<td colspan="2"><strong>Prioridad del requerimiento: Alto</strong></td>
</tr>
</tbody>
</table>

<table style="width:66%;">
<colgroup>
<col style="width: 21%" />
<col style="width: 44%" />
</colgroup>
<thead>
<tr>
<th style="text-align: center;"><blockquote>
<p><strong>Identificación del</strong></p>
<p><strong>requerimiento:</strong></p>
</blockquote></th>
<th><blockquote>
<p>RNF4</p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align: center;"><p><strong>Nombre del</strong></p>
<p><strong>requerimiento:</strong></p></td>
<td><blockquote>
<p>Usabilidad.</p>
</blockquote></td>
</tr>
<tr>
<td style="text-align: center;"><blockquote>
<p><strong>Características:</strong></p>
</blockquote></td>
<td><p>El sistema debe ser fa cil de usar, con una interfaz intuitiva que permita a los usuarios realizar tareas comunes sin necesidad de asistencia</p>
<p>te cnica.</p></td>
</tr>
<tr>
<td style="text-align: center;"><blockquote>
<p><strong>Descripción del requerimiento:</strong></p>
</blockquote></td>
<td><p>El sistema debe proporcionar una <strong>interfaz de usuario amigable e intuitiva</strong>. Los usuarios deben ser capaces de realizar tareas ba sicas</p>
<p>como la creación de un evento y la visualización</p></td>
</tr>
</tbody>
</table>

> <img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image3.png" style="width:0.13889in;height:0.14583in" />Documento: Tue evento SRS.docx CapySoft, 2024 Página 54 de 55

<table style="width:89%;">
<colgroup>
<col style="width: 9%" />
<col style="width: 0%" />
<col style="width: 21%" />
<col style="width: 29%" />
<col style="width: 15%" />
<col style="width: 13%" />
</colgroup>
<thead>
<tr>
<th colspan="2" rowspan="3"><img src="https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/media/image1.jpeg" style="width:0.60889in;height:0.505in" /></th>
<th colspan="4"><blockquote>
<p>Tu evento</p>
</blockquote></th>
</tr>
<tr>
<th colspan="2">Documento: Especificación de Requerimientos</th>
<th colspan="2"><blockquote>
<p>Versión: <strong>1.0</strong></p>
</blockquote></th>
</tr>
<tr>
<th colspan="2">Clave: MTS-07</th>
<th colspan="2"><blockquote>
<p>Fecha: 26/02/2025</p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr>
<td rowspan="2"></td>
<td colspan="2"></td>
<td colspan="2"><p>de detalles del evento sin una curva de</p>
<p>aprendizaje pronunciada.</p></td>
<td rowspan="2"></td>
</tr>
<tr>
<td colspan="4"><strong>Prioridad del requerimiento: Alto</strong></td>
</tr>
</tbody>
</table>

<table style="width:66%;">
<colgroup>
<col style="width: 21%" />
<col style="width: 44%" />
</colgroup>
<thead>
<tr>
<th style="text-align: center;"><blockquote>
<p><strong>Identificación del</strong></p>
<p><strong>requerimiento:</strong></p>
</blockquote></th>
<th><blockquote>
<p>RNF5</p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr>
<td style="text-align: center;"><p><strong>Nombre del</strong></p>
<p><strong>requerimiento:</strong></p></td>
<td><blockquote>
<p>Compatibilidad.</p>
</blockquote></td>
</tr>
<tr>
<td style="text-align: center;"><blockquote>
<p><strong>Características:</strong></p>
</blockquote></td>
<td><blockquote>
<p>El sistema debe ser compatible con los principales navegadores y dispositivos móviles para asegurar una experiencia de usuario</p>
<p>consistente y tv en sistema web.</p>
</blockquote></td>
</tr>
<tr>
<td style="text-align: center;"><blockquote>
<p><strong>Descripción del requerimiento:</strong></p>
</blockquote></td>
<td><blockquote>
<p>El sistema debe ser compatible con los principales navegadores web, como <strong>Google Chrome. Además, compatible con</strong></p>
<p><strong>dispositivos Android.</strong></p>
</blockquote></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Prioridad del requerimiento: Alto</strong></td>
</tr>
</tbody>
</table>

***Requerimientos del Hardware.*** 

<table style="width:87%;">
<colgroup>
<col style="width: 15%" />
<col style="width: 13%" />
<col style="width: 9%" />
<col style="width: 14%" />
<col style="width: 12%" />
<col style="width: 12%" />
<col style="width: 10%" />
</colgroup>
<thead>
<tr>
<th colspan="7" style="text-align: center;"><strong>Requisitos de hardware</strong> </th>
</tr>
</thead>
<tbody>
<tr>
<td><strong>CANTIDAD</strong> </td>
<td><strong>COMPONENTE</strong> </td>
<td colspan="5" style="text-align: center;"><strong>ESPECIFICACIONES</strong> </td>
</tr>
<tr>
<td rowspan="16" style="text-align: center;">4 </td>
<td rowspan="16" style="text-align: center;">Computadora portátil o escritorio </td>
<td style="text-align: center;">Marca </td>
<td style="text-align: center;">Acer </td>
<td style="text-align: center;">Acer </td>
<td style="text-align: center;">Corporativo HP </td>
<td style="text-align: center;">Corporativo HP </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">Aspire 515-52 </td>
<td style="text-align: center;">Nitro AN515-55 </td>
<td style="text-align: center;">Elitedesk 800 G1 </td>
<td style="text-align: center;">Elitedesk 600 G2 </td>
</tr>
<tr>
<td style="text-align: center;">Color </td>
<td style="text-align: center;">Negro </td>
<td style="text-align: center;">Negro </td>
<td style="text-align: center;">Negro </td>
<td style="text-align: center;">Negro </td>
</tr>
<tr>
<td colspan="5" style="text-align: center;"><strong>Componentes de rendimiento</strong> </td>
</tr>
<tr>
<td style="text-align: center;">Tarjeta Gráfica </td>
<td style="text-align: center;">Integrada </td>
<td style="text-align: center;">GeForce GTX 1650 </td>
<td style="text-align: center;">Integrada </td>
<td style="text-align: center;">Integrada </td>
</tr>
<tr>
<td style="text-align: center;">Procesador  </td>
<td style="text-align: center;">Intel Core </td>
<td style="text-align: center;">Intel Core </td>
<td style="text-align: center;">Intel Core </td>
<td style="text-align: center;">Intel Core </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">i5 </td>
<td style="text-align: center;">i5 </td>
<td style="text-align: center;">i5 </td>
<td style="text-align: center;">i5 </td>
</tr>
<tr>
<td colspan="5" style="text-align: center;"><strong>Memoria RAM</strong> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">8GB </td>
<td style="text-align: center;">8GB </td>
<td style="text-align: center;">12GB </td>
<td style="text-align: center;">8GB </td>
</tr>
<tr>
<td colspan="5" style="text-align: center;"><strong>Almacenamiento</strong> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">475GB </td>
<td style="text-align: center;">475GB </td>
<td style="text-align: center;">475GB </td>
<td style="text-align: center;">1TB </td>
</tr>
<tr>
<td colspan="5" style="text-align: center;"><strong>Pantalla y sistema operativo</strong> </td>
</tr>
<tr>
<td style="text-align: center;">Tamaño </td>
<td style="text-align: center;">15.6" </td>
<td style="text-align: center;">15.8" </td>
<td style="text-align: center;">15.6" </td>
<td style="text-align: center;">15.6" </td>
</tr>
<tr>
<td style="text-align: center;">Resolución </td>
<td style="text-align: center;">1366 x 768 60,06Hz </td>
<td style="text-align: center;">1920 x 1080, 144Hz </td>
<td style="text-align: center;">1921 x 1080, 60,06Hz </td>
<td style="text-align: center;">1922 x 1080, 60,06Hz </td>
</tr>
<tr>
<td style="text-align: center;">Entrada táctil </td>
<td style="text-align: center;">No </td>
<td style="text-align: center;">No </td>
<td style="text-align: center;">No </td>
<td style="text-align: center;">No </td>
</tr>
<tr>
<td style="text-align: center;">Sistema Operativo </td>
<td style="text-align: center;">Windows 10 </td>
<td style="text-align: center;">Windows 11 </td>
<td style="text-align: center;">Windows 10 </td>
<td style="text-align: center;">Windows 10 </td>
</tr>
<tr>
<td rowspan="15" style="text-align: center;">3 </td>
<td rowspan="15" style="text-align: center;">Android </td>
<td style="text-align: center;">Marca </td>
<td style="text-align: center;">Honor </td>
<td style="text-align: center;">Xiaomi </td>
<td style="text-align: center;">Xiaomi </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">Honor x6a plus </td>
<td style="text-align: center;">Redmi Note 10 </td>
<td style="text-align: center;">Redmi Note 11 </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Número de serie </td>
<td style="text-align: center;">WDY-LX3 </td>
<td style="text-align: center;">21061119AL </td>
<td style="text-align: center;">2201117TL </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="4" style="text-align: center;"><strong>Componentes de rendimiento</strong> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Procesador  </td>
<td style="text-align: center;">mediatek </td>
<td style="text-align: center;">Qualcomm®  </td>
<td style="text-align: center;">Qualcomm®  </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">Helio  </td>
<td style="text-align: center;"> Snapdragon™ </td>
<td style="text-align: center;">Snapdragon™ </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Serie </td>
<td style="text-align: center;">G36 </td>
<td style="text-align: center;">678 </td>
<td style="text-align: center;">680 </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="4" style="text-align: center;"><strong>Memoria RAM</strong> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">6GB + HONOR RAM Turbo </td>
<td style="text-align: center;">4GB </td>
<td style="text-align: center;">6GB </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="4" style="text-align: center;"><strong>Almacenamiento</strong> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">256GB </td>
<td style="text-align: center;">128GB </td>
<td style="text-align: center;">128GB </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="4" style="text-align: center;"><strong>Pantalla</strong> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Tamaño </td>
<td style="text-align: center;">LCD de 6,56 pulgadas </td>
<td style="text-align: center;">6,5″ pulgadas </td>
<td style="text-align: center;">6,43" </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Resolución </td>
<td style="text-align: center;">1612 x 720  </td>
<td style="text-align: center;">1080 x 2040  </td>
<td style="text-align: center;">1080 x 2400  </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Versión de Android </td>
<td style="text-align: center;">14 </td>
<td style="text-align: center;">13 </td>
<td style="text-align: center;">14 </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td rowspan="15" style="text-align: center;">1 </td>
<td rowspan="15" style="text-align: center;">iOS </td>
<td style="text-align: center;">Marca </td>
<td style="text-align: center;">iPhone  </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">iPhone 11  </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Número de serie </td>
<td style="text-align: center;">FFWLLRTRN73D </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Componentes de rendimiento</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Procesador  </td>
<td style="text-align: center;"> Bionic </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">A </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Serie </td>
<td style="text-align: center;">13 </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Memoria RAM</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">4GB LPDDR4X </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Almacenamiento</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">128GB </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Pantalla</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Tamaño </td>
<td style="text-align: center;">6.06" pulgadas </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Resolución </td>
<td style="text-align: center;">1792 x 828 </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Versión de iOS </td>
<td style="text-align: center;">18 </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td rowspan="15" style="text-align: center;">3 </td>
<td rowspan="15" style="text-align: center;">tv </td>
<td style="text-align: center;">Marca </td>
<td style="text-align: center;">LG </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">32 </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Número de serie </td>
<td style="text-align: center;">FFWLLRTRN73D </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Componentes de rendimiento</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Procesador  </td>
<td style="text-align: center;"> Alpha </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Modelo </td>
<td style="text-align: center;">A </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Serie </td>
<td style="text-align: center;">14 </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Memoria RAM</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">500 MB </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Almacenamiento</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Capacidad </td>
<td style="text-align: center;">200GB </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Pantalla</strong> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Tamaño </td>
<td style="text-align: center;">32 pulgadas </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Resolución </td>
<td style="text-align: center;">1,366 x 768 </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
<tr>
<td style="text-align: center;">Versión de WebOS </td>
<td style="text-align: center;">22 </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"> </td>
</tr>
</tbody>
</table>

 

 

 

 

 

 

 

***Requerimientos del Software.*** 

| **VS Code o IDE**  | **Versión**  |
|:--:|:--:|
| Visual Studio Code  | 1.x (actualizaciones regulares)  |
| Visual Studio  | 2022 (última versión estable)  |
| **Base de Datos**  | **Versión**  |
| PostgreSQL  | 15.x (última versión estable)  |
| **Lenguajes de Programacion**  | **Versión**  |
| JavaScript  | ES2023 (última versión)  |
| Java   | 21 (última versión)  |
| **Frameworks**  | **Versión**  |
| React Native  | Última versión estable (0.70.x o superior)  |
| Fabric.js  | Última versión  |
|  Spring Boot  | 3.2.x (última versión estable)  |
| **API**  | **Versión**  |
| Google Sign-In  | Última versión estable  |
| Facebook Login  | Última versión estable  |
| Twitter Login  | Última versión estable  |
| Instagram Graph  | Última versión estable  |

 

<table style="width:100%;">
<colgroup>
<col style="width: 10%" />
<col style="width: 27%" />
<col style="width: 59%" />
<col style="width: 1%" />
</colgroup>
<thead>
<tr>
<th colspan="2" style="text-align: center;"><strong>Sistema Operativo</strong> </th>
<th colspan="2" style="text-align: center;"><strong>Licencia</strong> </th>
</tr>
</thead>
<tbody>
<tr>
<td colspan="2" style="text-align: center;">Windows </td>
<td colspan="2" style="text-align: center;">Licencia gratuita </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Windows </td>
<td colspan="2" style="text-align: center;">Comunidad (gratis) </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Sistema Operativo</strong> </td>
<td colspan="2" style="text-align: center;"><strong>Licencia</strong> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Windows </td>
<td colspan="2" style="text-align: center;">Licencia abierta </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Sistema Operativo</strong> </td>
<td colspan="2" style="text-align: center;"><strong>Licencia</strong> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">Libre (Open Source) </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">MIT License </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Sistema Operativo</strong> </td>
<td colspan="2" style="text-align: center;"><strong>Licencia</strong> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Windows </td>
<td colspan="2" style="text-align: center;">MIT License </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">MIT License </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">Apache License 2.0 </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;"><strong>Sistema Operativo</strong> </td>
<td colspan="2" style="text-align: center;"><strong>Licencia</strong> </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">Gratis (Google Cloud Platform) </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">Gratis (Facebook Developer Portal) </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">Gratis (Twitter Developer Portal) </td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Multiplataforma </td>
<td colspan="2" style="text-align: center;">Gratis (Facebook Developer Portal) </td>
</tr>
<tr>
<td colspan="3" style="text-align: center;"><strong>Requerimientos Mínimos</strong> </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;">Windows: Windows 7, 8, 10, 11 (64 bits) </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;">Windows 10 versión 1909 o superior (64 bits), 8 GB de RAM recomendados </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;"><strong>Requerimientos Mínimos</strong> </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;">Windows 10 (64-bit), 2 GB de RAM, 200 MB de espacio libre en disco. </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;"><strong>Requerimientos Mínimos</strong> </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;">Depende del navegador, pero en general: Procesador 1 GHz, 2 GB de RAM, espacio libre de disco  </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;">Windows: Windows 10 o superior, 2 GB de RAM, 2 GB de espacio libre en disco. </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;"><strong>Requerimientos Mínimos</strong> </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Windows 10 o superior, 8 GB de RAM (mínimo recomendado), 10 GB de espacio libre en disco. </td>
<td style="text-align: center;">  </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">No tiene requisitos específicos, solo un navegador moderno </td>
<td style="text-align: center;">  </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">JDK 17 o superior, 2 GB de RAM, 2 GB de espacio libre en disco, conexión a internet para gestionar dependencias con Maven o Gradle. </td>
<td style="text-align: center;">  </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="3" style="text-align: center;"><strong>Requerimientos Mínimos</strong> </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Cuenta de Google, registrar app en Google Cloud Console, configurar OAuth 2.0. </td>
<td style="text-align: center;">  </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Cuenta de Facebook, crear aplicación en Facebook Developer Portal, configurar OAuth. </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Cuenta de Twitter, registrar app en Twitter Developer Portal, usar OAuth. </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"></td>
</tr>
<tr>
<td colspan="2" style="text-align: center;">Cuenta de Instagram (Business), configurar la app en Facebook Developer Portal. </td>
<td style="text-align: center;"> </td>
<td style="text-align: center;"></td>
</tr>
</tbody>
</table>

 

 

 

 

 

**Análisis de Viabilidad ** 

El análisis de viabilidad tiene como objetivo evaluar si el aplicativo es factible de implementar, considerando aspectos técnicos, económicos, operativos y de tiempo. Este análisis busca determinar si la plataforma propuesta, que facilita la organización y asistencia a eventos de manera dinámica, intuitiva y segura, puede desarrollarse con los recursos disponibles y si generará el valor esperado para los organizadores y asistentes. 

1.  ***<u>Viabilidad Técnica: </u>*** 

> Para garantizar la viabilidad técnica del aplicativo, se evaluaron los siguientes aspectos: 

- **Tecnologías disponibles: **El desarrollo del aplicativo utilizará tecnologías modernas y ampliamente adoptadas como:  

1.  **Frontend: **React Native, React.js, para desarrollar la interfaz de usuario del aplicativo. 

<!-- -->

2.  **Backend: **El lenguaje de programación que se va incorporar para la lógica sería Java.** ** 

<!-- -->

3.  **Base de datos: **PostgreSQL para almacenar información de eventos, usuarios y transacciones. 

<!-- -->

4.  **Integración de APIs: **Los usuarios podrán registrarse e iniciar sesión en el aplicativo utilizando sus cuentas de redes sociales o Google, sin necesidad de crear una nueva cuenta manualmente. 

- **Capacidad del equipo: **El equipo de desarrollo recientemente cuenta con poca experiencia en el uso de estas tecnologías, a pesar de este desafío, se ha identificado que el equipo tiene una base sólida en conceptos de programación y desarrollo de software, lo que facilita una curva de aprendizaje más rápida. 

<!-- -->

- **Compatibilidad: **El aplicativo está diseñado para ser compatible con dispositivos móviles, televisores y navegadores webs. 

**Conclusión:** El aplicativo es técnicamente viable, ya que las tecnologías necesarias están disponibles, el equipo tiene las capacidades requeridas, asegurando un desarrollo y funcionamiento eficiente. 

 

 

 

2.  ***<u>Viabilidad económica: </u>*** 

La viabilidad económica del aplicativo se centra en evaluar si el proyecto es financieramente viable, es decir, si los beneficios esperados justifican los costos de desarrollo e implementación. A continuación, se detallan los aspectos clave de este análisis: 

- **Costos de desarrollo: **No se incurrirá en costos de desarrollo, ya que el equipo de trabajo ya está conformado y cuenta con los conocimientos necesarios para llevar a cabo la implementación del aplicativo. Además, las herramientas y tecnologías requeridas (frameworks, librerías, entornos de desarrollo, etc.) son de código abierto o ya están disponibles para su uso sin costos adicionales. Las APIs serían públicas que no requieren licencias costosas. De esta manera, el desarrollo del aplicativo se realizará sin necesidad de inversión económica en esta fase. 

>  

3.  ***<u>Viabilidad operativa: </u>*** 

La viabilidad operativa evalúa si el aplicativo puede integrarse y funcionar de manera efectiva en el entorno actual, considerando los procesos, recursos y necesidades de los usuarios. Este análisis asegura que el aplicativo no solo sea técnicamente viable, sino que también pueda ser adoptado y utilizado sin problemas por organizadores y asistentes. Se evaluará los siguientes aspectos:  

- **Adaptación para los procesos actuales:** Los organizadores podrán migrar fácilmente desde métodos tradicionales (lápiz y papel, hojas de cálculo) a la plataforma digital, gracias a una interfaz intuitiva y funcionalidades personalizables. 

**Conclusión:** El aplicativo de gestión de eventos es operativamente viable, ya que se integra sin problemas con los procesos actuales, ofrece una experiencia intuitiva para los usuarios y cuenta con un soporte de utilidad para facilitar su adopción. Garantizando una operación eficiente y sin complicaciones. 

 

4.  ***<u>Viabilidad de tiempo: </u>*** 

La viabilidad de tiempo evalúa si el desarrollo del aplicativo puede completarse dentro del cronograma establecido, considerando la carga de trabajo, la asignación de tareas y los plazos de entrega. Este análisis permite determinar si el equipo cuenta con el tiempo suficiente para cumplir con los objetivos del proyecto sin comprometer la calidad del producto. 

 


---

## Encabezados y pies de página del original

- **Pie de página** (`word/footer1.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer10.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 19 de 55 Página 19 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer11.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer12.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 21 de 55 Página 21 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer14.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 23 de 55 Página 23 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer15.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 26 de 55 Página 26 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer16.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 25 de 55 Página 25 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer17.xml`): sin texto<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer18.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 27 de 55 Página 27 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer19.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 30 de 55 Página 30 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer2.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer20.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 29 de 55 Página 29 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer21.xml`): sin texto<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer22.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 31 de 55 Página 31 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer23.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 34 de 55 Página 34 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer24.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 33 de 55 Página 33 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer25.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 36 de 55 Página 36 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer26.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer27.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 38 de 55 Página 38 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer29.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer3.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 12 de 55 Página 12 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer30.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 39 de 55 Página 39 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer31.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer32.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 41 de 55 Página 41 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer33.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 44 de 55 Página 44 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer34.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 43 de 55 Página 43 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer35.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 46 de 55 Página 46 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer36.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 45 de 55 Página 45 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer37.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 48 de 55 Página 48 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer38.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 47 de 55 Página 47 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer39.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 50 de 55 Página 50 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer4.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 13 de 55 Página 13 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer40.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 49 de 55 Página 49 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer41.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer42.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 51 de 55 Página 51 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer43.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer44.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 53 de 55 Página 53 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer45.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 55 de 55 Página 55 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer5.xml`): texto «C o n f i d e n c i a l C o n f i d e n c i a l»
- **Pie de página** (`word/footer6.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 15 de 55 Página 15 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer7.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 18 de 55 Página 18 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Pie de página** (`word/footer8.xml`): texto «Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Documento: Tue evento SRS.docx CapySoft , 2024 C o n f i d e n c i a l Página 17 de 55 Página 17 de 55»<br>![Pie de página del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image2.png)
- **Encabezado** (`word/header1.xml`): texto «Tu evento Documento: Análisis del Software Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Análisis del Software Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header10.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header12.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header14.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header16.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header18.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header2.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header20.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header22.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header24.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header3.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header4.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header6.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)
- **Encabezado** (`word/header8.xml`): texto «Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025 Tu evento Documento: Especificación de Requerimientos Versión: 1.0 Clave: MTS- 07 Fecha: 26/02/2025»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Analisis_del_Software_Tu_evento/header_footer/image1.jpeg)

---

<a id="doc-te-0022"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Software_Requirements.md
id: TE-0022
title: "Software Requirements"
lang: es
source: 02_Analisis_Software/Software Requirements.xlsx
source_sha256: e3d14c3d2d42d91e1c4d1bf2541b8e4ab2b9f561f9cec663572b2edc2862525d
conversion: "openpyxl 3.1.5 (data_only + fórmulas); sin revisión manual"
converted_on: 2026-10-23
fidelity: parcial
losses:
  - "Formato visual (colores, bordes, anchos, fuentes y estilos del formato condicional) no se conserva."
  - "Celdas combinadas listadas pero no reproducidas como celda única."
  - "Los valores de las fórmulas son los guardados en caché por Excel; no se recalcularon."
status: borrador
reviewed_by: ""
-->

# Software Requirements

> Conversión de `02_Analisis_Software/Software Requirements.xlsx`: 1 hojas (R.Software), 0 fórmulas. Las columnas se nombran con la letra de Excel y cada fila con su número original.

## Hoja: R.Software

Rango usado: `A1:H17` · 17 filas × 8 columnas · celdas combinadas: 17

| Fila | A | B | C | D | E | F | G | H |
|---|---|---|---|---|---|---|---|---|
| 1 | Requisitos de software |  |  |  |  |  |  |  |
| 2 | VS Code o IDE | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 3 | Visual Studio Code | 1.x (actualizaciones regulares) | Windows | Licencia gratuita | Windows: Windows 7, 8, 10, 11 (64 bits) |  |  |  |
| 4 | Visual Studio | 2022 (última versión estable) | Windows | Comunidad (gratis) | Windows 10 versión 1909 o superior (64 bits), 8 GB de RAM recomendados |  |  |  |
| 5 | Base de Datos | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 6 | PostgreSQL | 15.x (última versión estable) | Windows | Licencia abierta | Windows 10 (64-bit), 2 GB de RAM, 200 MB de espacio libre en disco. |  |  |  |
| 7 | Lenguajes de Programacion | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 8 | JavaScript | ES2023 (última versión) | Multiplataforma | Libre (Open Source) | Depende del navegador, pero en general: Procesador 1 GHz, 2 GB de RAM, espacio libre de disco  |  |  |  |
| 9 | Java  | 11.0 (última versión) | Multiplataforma | MIT License | Windows: Windows 10 o superior, 2 GB de RAM, 2 GB de espacio libre en disco. |  |  |  |
| 10 | Frameworks | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 11 | React Native | Última versión estable (0.70.x o superior) | Windows | MIT License | Windows 10 o superior, 8 GB de RAM (mínimo recomendado), 10 GB de espacio libre en disco. |  |  |  |
| 12 | Fabric.js | Última versión | Multiplataforma | MIT License | No tiene requisitos específicos, solo un navegador moderno |  |  |  |
| 13 |  Spring Boot | 3.2.x (última versión estable) | Multiplataforma | Apache License 2.0 | JDK 17 o superior, 2 GB de RAM, 2 GB de espacio libre en disco, conexión a internet para gestionar dependencias con Maven o Gradle. |  |  |  |
| 14 | API | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 15 | Google Sign-In | Última versión estable | Multiplataforma | Gratis (Google Cloud Platform) | Cuenta de Google, registrar app en Google Cloud Console, configurar OAuth 2.0. |  |  |  |
| 16 | Facebook Login | Última versión estable | Multiplataforma | Gratis (Facebook Developer Portal) | Cuenta de Facebook, crear aplicación en Facebook Developer Portal, configurar OAuth. |  |  |  |
| 17 | Hosting /Dominio | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |

**Celdas combinadas** (el valor aparece solo en la celda superior izquierda): `A1:H1`, `E10:H10`, `E11:G11`, `E12:G12`, `E13:G13`, `E14:H14`, `E15:G15`, `E16:G16`, `E17:H17`, `E2:H2`, `E3:H3`, `E4:H4`, `E5:H5`, `E6:H6`, `E7:H7`, `E8:H8`, `E9:H9`

---

<a id="doc-te-0076"></a>

<!--
ruta_anterior: es/02_Analisis_Software/hardware_requirements.md
id: TE-0076
title: "hardware requirements"
lang: es
source: 02_Analisis_Software/hardware requirements.xlsx
source_sha256: 5cdd4fe32696aa57a42f140d337d96c99de0ee3477f5ad92fe6c005c19404bc6
conversion: "openpyxl 3.1.5 (data_only + fórmulas); sin revisión manual"
converted_on: 2026-10-23
fidelity: parcial
losses:
  - "Formato visual (colores, bordes, anchos, fuentes y estilos del formato condicional) no se conserva."
  - "Celdas combinadas listadas pero no reproducidas como celda única."
  - "Los valores de las fórmulas son los guardados en caché por Excel; no se recalcularon."
status: borrador
reviewed_by: ""
-->

# hardware requirements

> Conversión de `02_Analisis_Software/hardware requirements.xlsx`: 1 hojas (Table 1), 0 fórmulas. Las columnas se nombran con la letra de Excel y cada fila con su número original.

## Hoja: Table 1

Rango usado: `A1:I63` · 63 filas × 9 columnas · celdas combinadas: 26

| Fila | A | B | C | D | E | F | G | H | I |
|---|---|---|---|---|---|---|---|---|---|
| 1 | Requisitos de hardware |  |  |  |  |  |  |  |  |
| 2 | CANTIDAD | COMPONENTE | ESPECIFICACIONES |  |  |  |  |  |  |
| 3 | 4 | Computadora portatil o escritorio | Marca | Acer | Acer | Corporativo HP | Corporativo HP |  |  |
| 4 |  |  | Modelo | Aspire 515-52 | Nitro AN515-55 | Elitedesk 800 G1 | Elitedesk 600 G2 |  |  |
| 5 |  |  | Color | Negro | Negro | Negro | Negro |  |  |
| 6 |  |  | Componentes de rendimiento |  |  |  |  |  |  |
| 7 |  |  | Tarjeta Gráfica | Integrada | GeForce GTX 1650 | Integrada | Integrada |  |  |
| 8 |  |  | Procesador  | Intel core | Intel core | Intel core | Intel core |  |  |
| 9 |  |  | Modelo | i5 | i5 | i5 | i5 |  |  |
| 10 |  |  | Memoria RAM |  |  |  |  |  |  |
| 11 |  |  | Capacidad | 8GB | 8GB | 12GB | 8GB |  |  |
| 12 |  |  | Almacenamiento |  |  |  |  |  |  |
| 13 |  |  | Capacidad | 475GB | 475GB | 475GB | 1TB |  |  |
| 14 |  |  | Pantalla y sistema operativo |  |  |  |  |  |  |
| 15 |  |  | Tamaño | 15.6" | 15.8" | 15.6" | 15.6" |  |  |
| 16 |  |  | Resolución | 1366 x 768 60,06Hz | 1920 x 1080, 144Hz | 1921 x 1080, 60,06Hz | 1922 x 1080, 60,06Hz |  |  |
| 17 |  |  | Entrada táctil | No | No | No | No |  |  |
| 18 |  |  | Sistema Operativo | Windows 10 | Windows 11 | Windows 10 | Windows 10 |  |  |
| 19 | 3 | Android | Marca | Honor | Xiaomi | Xiaomi |  |  |  |
| 20 |  |  | Modelo | Honor x6a plus | RedmI Note 10 | Redmi Note 11 |  |  |  |
| 21 |  |  | Numero de serie | WDY-LX3 | 21061119AL | 2201117TL |  |  |  |
| 22 |  |  | Componentes de rendimiento |  |  |  |  |  |  |
| 23 |  |  | Procesador  | mediatek | Qualcomm®  | Qualcomm®  |  |  |  |
| 24 |  |  | Modelo | Helio  |  Snapdragon™ |  Snapdragon™ |  |  |  |
| 25 |  |  | Serie | G36 | 678 | 680 |  |  |  |
| 26 |  |  | Memoria RAM |  |  |  |  |  |  |
| 27 |  |  | Capacidad | 6GB + HONOR RAM Turbo | 4GB | 6GB |  |  |  |
| 28 |  |  | Almacenamiento |  |  |  |  |  |  |
| 29 |  |  | Capacidad | 256GB | 128GB | 128GB |  |  |  |
| 30 |  |  | Pantalla |  |  |  |  |  |  |
| 31 |  |  | Tamaño | LCD de 6,56 pulgadas | 6,5″ pulgadas | 6,43" |  |  |  |
| 32 |  |  | Resolución | 1612 x 720  | 1080 X 2040  | 	1080 x 2400  |  |  |  |
| 33 |  |  | Version de android | 14 | 13 | 14 |  |  |  |
| 34 | 1 | iOS | Marca | iphone  |  |  |  |  |  |
| 35 |  |  | Modelo | iphone 11  |  |  |  |  |  |
| 36 |  |  | Numero de serie | FFWLLRTRN73D |  |  |  |  |  |
| 37 |  |  | Componentes de rendimiento |  |  |  |  |  |  |
| 38 |  |  | Procesador  |  Bionic |  |  |  |  |  |
| 39 |  |  | Modelo | A |  |  |  |  |  |
| 40 |  |  | Serie | 13 |  |  |  |  |  |
| 41 |  |  | Memoria RAM |  |  |  |  |  |  |
| 42 |  |  | Capacidad | 4GB LPDDR4X |  |  |  |  |  |
| 43 |  |  | Almacenamiento |  |  |  |  |  |  |
| 44 |  |  | Capacidad | 128GB |  |  |  |  |  |
| 45 |  |  | Pantalla |  |  |  |  |  |  |
| 46 |  |  | Tamaño | 6.06" pulgadas |  |  |  |  |  |
| 47 |  |  | Resolución | 1792 x 828 |  |  |  |  |  |
| 48 |  |  | Version de iOS | 18 |  |  |  |  |  |
| 49 | 3 | tv | Marca | LG |  |  |  |  |  |
| 50 |  |  | Modelo | 32 |  |  |  |  |  |
| 51 |  |  | Numero de serie | FFWLLRTRN73D |  |  |  |  |  |
| 52 |  |  | Componentes de rendimiento |  |  |  |  |  |  |
| 53 |  |  | Procesador  |  Bionic |  |  |  |  |  |
| 54 |  |  | Modelo | A |  |  |  |  |  |
| 55 |  |  | Serie | 14 |  |  |  |  |  |
| 56 |  |  | Memoria RAM |  |  |  |  |  |  |
| 57 |  |  | Capacidad | 500 MB |  |  |  |  |  |
| 58 |  |  | Almacenamiento |  |  |  |  |  |  |
| 59 |  |  | Capacidad | 200GB |  |  |  |  |  |
| 60 |  |  | Pantalla |  |  |  |  |  |  |
| 61 |  |  | Tamaño | 32 Pulgadas |  |  |  |  |  |
| 62 |  |  | Resolución | 1,366 x 768 |  |  |  |  |  |
| 63 |  |  | Version de webOs | 22 |  |  |  |  |  |

**Celdas combinadas** (el valor aparece solo en la celda superior izquierda): `A19:A33`, `A1:G1`, `A34:A48`, `A3:A18`, `A49:A63`, `B19:B33`, `B34:B48`, `B3:B18`, `B49:B63`, `C10:G10`, `C12:G12`, `C14:G14`, `C22:F22`, `C26:F26`, `C28:F28`, `C2:G2`, `C30:F30`, `C37:D37`, `C41:D41`, `C43:D43`, `C45:D45`, `C52:D52`, `C56:D56`, `C58:D58`, `C60:D60`, `C6:G6`

---

<a id="doc-te-0077"></a>

<!--
ruta_anterior: es/02_Analisis_Software/matriz_de_riesgos_Tu_evento.md
id: TE-0077
title: "matriz de riesgos-Tu evento"
lang: es
source: 02_Analisis_Software/matriz de riesgos-Tu evento.xlsx
source_sha256: ccce9c6c004d753a70f35fbeddf8da76fd520001ee026f861a8c7cd004035bed
conversion: "openpyxl 3.1.5 (data_only + fórmulas); sin revisión manual"
converted_on: 2026-10-23
fidelity: parcial
losses:
  - "Las validaciones de datos con extensión x14 no las lee openpyxl; se extrajeron del XML de la hoja y se listan a mano."
  - "Formato visual (colores, bordes, anchos, fuentes y estilos del formato condicional) no se conserva."
  - "Celdas combinadas listadas pero no reproducidas como celda única."
  - "Los valores de las fórmulas son los guardados en caché por Excel; no se recalcularon."
status: borrador
reviewed_by: ""
-->

# matriz de riesgos-Tu evento

> Conversión de `02_Analisis_Software/matriz de riesgos-Tu evento.xlsx`: 2 hojas (TABLA RIESGOS, DATOS), 0 fórmulas. Las columnas se nombran con la letra de Excel y cada fila con su número original.

## Hoja: TABLA RIESGOS

Rango usado: `C3:H17` · 17 filas × 8 columnas · celdas combinadas: 0

| Fila | A | B | C | D | E | F | G | H |
|---|---|---|---|---|---|---|---|---|
| 3 |  |  |  | cual? | nivel |  | analisis  | como? |
| 4 |  |  | ITEM | DESCRIPCION DEL RIESGO | PRIORIDA | TIPO | EVALUACION | ENTRATEGIA DE MITIGACION |
| 5 |  |  | 1 | Conectividad a internet | Medio | Rec. Financieros | El lugar de trabajo no cuenta con conexiona internet | Comprar  un plan de datos para el uso del proyecto  |
| 6 |  |  | 2 | Falta de tiempo del equipo | Medio | Rec. Humanos | Miembros del equipo tienen otras responsabilidades que reducen su disponibilidad. | Ajustar los tiempos y redistribuir tareas entre los miembros disponibles. |
| 7 |  |  | 3 | Problemas con la calidad del código | Alto | Software | Código desorganizado podría causar errores graves en etapas avanzadas. | Implementar revisiones de código regulares y usar herramientas de control de calidad. |
| 8 |  |  | 4 | Falta de claridad en los objetivos del proyecto | Medio | General |  La falta de objetivos claros podría generar malentendidos y trabajos innecesarios. | Realizar reuniones para definir y documentar los objetivos del proyecto. |
| 9 |  |  | 5 | Problemas con el diseño visual del producto | Medio | Software |  Un diseño poco intuitivo podría frustrar a los usuarios finales. | Involucrar a un diseñador desde etapas tempranas del proyecto. |
| 10 |  |  | 6 | Falta de documentación adecuada | Medio | General | Asegurar la creación de documentación detallada y accesible durante el desarrollo del proyecto. | se debe realizar una evaluación periódica del estado de la documentación, identificando áreas de mejora y asegurando que sea fácil de actualizar y consultar. |
| 11 |  |  | 7 | Falta de pruebas de seguridad | Alto | Software | Si no se realizan pruebas exhaustivas de seguridad, el producto podría ser vulnerable a ataques o brechas de datos, afectando la confianza del usuario. | Realizar auditorías de seguridad periódicas, pruebas de penetración y aplicar parches de seguridad regularmente. |
| 12 |  |  | 8 | Sobrecarga de trabajo en el equipo | Alto | Rec. Humanos | Si el equipo asume más trabajo del que puede manejar, esto podría llevar al agotamiento, errores y una disminución en la calidad del trabajo. | Establecer cargas de trabajo realistas, hacer seguimiento del bienestar del equipo y delegar tareas de manera eficiente. |
| 13 |  |  | 9 | Problemas con el licenciamiento de Windows | Bajo | Rec. Financieros | El uso de licencias de Windows usuarios. | Asegurarse de que todas las licencias de Windows utilizadas sean legítimas y estén correctamente registradas. |
| 14 |  |  | 10 | Cambios frecuentes en los requisitos del cliente | Medio | General | Los cambios constantes en los requisitos del cliente pueden generar retrasos, desalineación del producto con los objetivos iniciales y aumentar la carga de trabajo. | Establecer un proceso claro para la gestión de cambios, incluyendo la revisión y aprobación formal de nuevos requisitos, y evaluar el impacto de cada cambio en el cronograma y el presupuesto. |
| 15 |  |  | 11 | Conflictos dentro del equipo de trabajo | Alto | Rec. Humanos |  Los conflictos internos pueden afectar la moral del equipo y reducir la productividad, generando retrasos en el proyecto. | Fomentar una cultura de respeto y comunicación abierta, mediando en los conflictos y ofreciendo soluciones que mejoren la dinámica del equipo. |
| 16 |  |  | 12 | Inyección SQL | Alto | Seguridad | Un atacante accede a la base de datos mediante entradas mal filtradas. | Usar consultas preparadas y sanitizar entradas de usuario. |

**Configuración de la hoja** (leída de `xl/worksheets/sheet1.xml`, extensión `x14`, que openpyxl no carga):

- Validación de datos (list) en `E5:E15`: `DATOS!$B$4:$B$6`.
- Validación de datos (list) en `F5:F15`: `DATOS!$D$4:$D$8`.

## Hoja: DATOS

Rango usado: `B3:D8` · 8 filas × 4 columnas · celdas combinadas: 0

| Fila | A | B | C | D |
|---|---|---|---|---|
| 3 |  | NIVEL |  | TIPO |
| 4 |  | Bajo |  | General |
| 5 |  | Medio |  | Software |
| 6 |  | Alto |  | Hardware |
| 7 |  |  |  | Rec. Humanos |
| 8 |  |  |  | Rec. Financieros |

---

<a id="doc-te-0034-te-0035-te-0036-te-0037-te-0038"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Use_case/Modulo_de_seguridad/Indice_de_imagenes.md
id: [TE-0034, TE-0035, TE-0036, TE-0037, TE-0038]
title: "Índice de imágenes — 02_Analisis_Software/Use case/Modulo de seguridad"
lang: es
source:
  - "02_Analisis_Software/Use case/Modulo de seguridad/login.png"
  - "02_Analisis_Software/Use case/Modulo de seguridad/logout y manejo de sesiones.png"
  - "02_Analisis_Software/Use case/Modulo de seguridad/olvide contraseña.png"
  - "02_Analisis_Software/Use case/Modulo de seguridad/peticion de organizador.png"
  - "02_Analisis_Software/Use case/Modulo de seguridad/registro.png"
source_sha256:
  - 2c03e1c59f5f7b451a112265c184fdd791f6ad1e1fbc6fbeeef4845a636ae5f0
  - 7c4d9df7aa5d5fdc410e1811723c49a54530dfa6305aa35af043f8df2ce81d9a
  - 5e3e9d2e4e3f55370eeceac6f3f95df2d7da83e58f85f75d5bd1b35cda44f164
  - 180e59d0a9b9dccf66154e99fb63a904ce03070a74007abdc3ac0098d293f47b
  - 210cbc303cb6e1f0aba616b2d6bd358ee5f9deaf0874ea11e223539a6ac48a8f
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

# Índice de imágenes — 02_Analisis_Software/Use case/Modulo de seguridad

> Índice de las 5 imágenes de `02_Analisis_Software/Use case/Modulo de seguridad/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | login.png | TE-0034 | PNG | 1464 × 1101 px |
| 2 | logout y manejo de sesiones.png | TE-0035 | PNG | 1293 × 873 px |
| 3 | olvide contraseña.png | TE-0036 | PNG | 1323 × 903 px |
| 4 | peticion de organizador.png | TE-0037 | PNG | 1683 × 963 px |
| 5 | registro.png | TE-0038 | PNG | 1404 × 1101 px |

## 1. login.png

| Campo | Valor |
|---|---|
| id | TE-0034 |
| Original | `02_Analisis_Software/Use case/Modulo de seguridad/login.png` |
| Formato y dimensiones | PNG · 1464 × 1101 px |
| Tamaño | 225188 bytes |
| SHA-256 | `2c03e1c59f5f7b451a112265c184fdd791f6ad1e1fbc6fbeeef4845a636ae5f0` |
| Transcripción | No transcrita |

![login](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20seguridad/login.png)

## 2. logout y manejo de sesiones.png

| Campo | Valor |
|---|---|
| id | TE-0035 |
| Original | `02_Analisis_Software/Use case/Modulo de seguridad/logout y manejo de sesiones.png` |
| Formato y dimensiones | PNG · 1293 × 873 px |
| Tamaño | 159185 bytes |
| SHA-256 | `7c4d9df7aa5d5fdc410e1811723c49a54530dfa6305aa35af043f8df2ce81d9a` |
| Transcripción | No transcrita |

![logout y manejo de sesiones](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20seguridad/logout%20y%20manejo%20de%20sesiones.png)

## 3. olvide contraseña.png

| Campo | Valor |
|---|---|
| id | TE-0036 |
| Original | `02_Analisis_Software/Use case/Modulo de seguridad/olvide contraseña.png` |
| Formato y dimensiones | PNG · 1323 × 903 px |
| Tamaño | 158982 bytes |
| SHA-256 | `5e3e9d2e4e3f55370eeceac6f3f95df2d7da83e58f85f75d5bd1b35cda44f164` |
| Transcripción | No transcrita |

![olvide contraseña](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20seguridad/olvide%20contrase%C3%B1a.png)

## 4. peticion de organizador.png

| Campo | Valor |
|---|---|
| id | TE-0037 |
| Original | `02_Analisis_Software/Use case/Modulo de seguridad/peticion de organizador.png` |
| Formato y dimensiones | PNG · 1683 × 963 px |
| Tamaño | 187265 bytes |
| SHA-256 | `180e59d0a9b9dccf66154e99fb63a904ce03070a74007abdc3ac0098d293f47b` |
| Transcripción | No transcrita |

![peticion de organizador](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20seguridad/peticion%20de%20organizador.png)

## 5. registro.png

| Campo | Valor |
|---|---|
| id | TE-0038 |
| Original | `02_Analisis_Software/Use case/Modulo de seguridad/registro.png` |
| Formato y dimensiones | PNG · 1404 × 1101 px |
| Tamaño | 187876 bytes |
| SHA-256 | `210cbc303cb6e1f0aba616b2d6bd358ee5f9deaf0874ea11e223539a6ac48a8f` |
| Transcripción | No transcrita |

![registro](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20seguridad/registro.png)

---

<a id="doc-te-0025-te-0026-te-0027-te-0028-te-0029"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Use_case/Modulo_de_eventos/Indice_de_imagenes.md
id: [TE-0025, TE-0026, TE-0027, TE-0028, TE-0029]
title: "Índice de imágenes — 02_Analisis_Software/Use case/Modulo de eventos"
lang: es
source:
  - "02_Analisis_Software/Use case/Modulo de eventos/Filtrar eventos.png"
  - "02_Analisis_Software/Use case/Modulo de eventos/comentar eventos.png"
  - "02_Analisis_Software/Use case/Modulo de eventos/crear evento.png"
  - "02_Analisis_Software/Use case/Modulo de eventos/modificar layout.png"
  - "02_Analisis_Software/Use case/Modulo de eventos/publicar evento.png"
source_sha256:
  - 7440bd1c497bd4d86a7bfffd744d539a8d1becbdae0d9a19fc322b1d28cb8eef
  - 89006311db301e742fb9e4f6993a4872393ed30b75fee5acc9fbca2cf6959ceb
  - bc2f8e78fdf9cb17ec6bb82ac48472497d0a5a2ce12c42eea7636ed1651305e2
  - 2152a9bc6bc17a507ef4426b4e6c6d726ce581fd23bd06922a16ad8733c7abd4
  - dd392b09eeb9f12476a466c51c349d84479bf27b5b074622ceba96c4c27e68a3
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

# Índice de imágenes — 02_Analisis_Software/Use case/Modulo de eventos

> Índice de las 5 imágenes de `02_Analisis_Software/Use case/Modulo de eventos/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Filtrar eventos.png | TE-0025 | PNG | 1683 × 1116 px |
| 2 | comentar eventos.png | TE-0026 | PNG | 1383 × 783 px |
| 3 | crear evento.png | TE-0027 | PNG | 1521 × 1173 px |
| 4 | modificar layout.png | TE-0028 | PNG | 1611 × 1143 px |
| 5 | publicar evento.png | TE-0029 | PNG | 1653 × 1143 px |

## 1. Filtrar eventos.png

| Campo | Valor |
|---|---|
| id | TE-0025 |
| Original | `02_Analisis_Software/Use case/Modulo de eventos/Filtrar eventos.png` |
| Formato y dimensiones | PNG · 1683 × 1116 px |
| Tamaño | 212210 bytes |
| SHA-256 | `7440bd1c497bd4d86a7bfffd744d539a8d1becbdae0d9a19fc322b1d28cb8eef` |
| Transcripción | No transcrita |

![Filtrar eventos](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20eventos/Filtrar%20eventos.png)

## 2. comentar eventos.png

| Campo | Valor |
|---|---|
| id | TE-0026 |
| Original | `02_Analisis_Software/Use case/Modulo de eventos/comentar eventos.png` |
| Formato y dimensiones | PNG · 1383 × 783 px |
| Tamaño | 140894 bytes |
| SHA-256 | `89006311db301e742fb9e4f6993a4872393ed30b75fee5acc9fbca2cf6959ceb` |
| Transcripción | No transcrita |

![comentar eventos](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20eventos/comentar%20eventos.png)

## 3. crear evento.png

| Campo | Valor |
|---|---|
| id | TE-0027 |
| Original | `02_Analisis_Software/Use case/Modulo de eventos/crear evento.png` |
| Formato y dimensiones | PNG · 1521 × 1173 px |
| Tamaño | 205982 bytes |
| SHA-256 | `bc2f8e78fdf9cb17ec6bb82ac48472497d0a5a2ce12c42eea7636ed1651305e2` |
| Transcripción | No transcrita |

![crear evento](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20eventos/crear%20evento.png)

## 4. modificar layout.png

| Campo | Valor |
|---|---|
| id | TE-0028 |
| Original | `02_Analisis_Software/Use case/Modulo de eventos/modificar layout.png` |
| Formato y dimensiones | PNG · 1611 × 1143 px |
| Tamaño | 232689 bytes |
| SHA-256 | `2152a9bc6bc17a507ef4426b4e6c6d726ce581fd23bd06922a16ad8733c7abd4` |
| Transcripción | No transcrita |

![modificar layout](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20eventos/modificar%20layout.png)

## 5. publicar evento.png

| Campo | Valor |
|---|---|
| id | TE-0029 |
| Original | `02_Analisis_Software/Use case/Modulo de eventos/publicar evento.png` |
| Formato y dimensiones | PNG · 1653 × 1143 px |
| Tamaño | 224885 bytes |
| SHA-256 | `dd392b09eeb9f12476a466c51c349d84479bf27b5b074622ceba96c4c27e68a3` |
| Transcripción | No transcrita |

![publicar evento](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20eventos/publicar%20evento.png)

---

<a id="doc-te-0030-te-0031-te-0032-te-0033"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Use_case/Modulo_de_pagos/Indice_de_imagenes.md
id: [TE-0030, TE-0031, TE-0032, TE-0033]
title: "Índice de imágenes — 02_Analisis_Software/Use case/Modulo de pagos"
lang: es
source:
  - "02_Analisis_Software/Use case/Modulo de pagos/Retirar saldo de la billetera.png"
  - "02_Analisis_Software/Use case/Modulo de pagos/comprar ticket.png"
  - "02_Analisis_Software/Use case/Modulo de pagos/pasarela de pago.png"
  - "02_Analisis_Software/Use case/Modulo de pagos/validar asistencia del ticket.png"
source_sha256:
  - 6e2a9ebc40aedb375ed0d1d6286faec3028a001bbe9971c7797588376ea701f1
  - 1748132c8509270c7e1691c0fe342711fa9cade93f074d08c84702e922759dc3
  - a8d7e065c7232139ace9d82c0606a5052b5ffeb548d0a9b15609ea5f3d679b07
  - b9f4411c6e6ed642534178ef134b5fd9c208b4b3294fe208db52b832f9db7c95
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

# Índice de imágenes — 02_Analisis_Software/Use case/Modulo de pagos

> Índice de las 4 imágenes de `02_Analisis_Software/Use case/Modulo de pagos/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Retirar saldo de la billetera.png | TE-0030 | PNG | 1770 × 1083 px |
| 2 | comprar ticket.png | TE-0031 | PNG | 2010 × 963 px |
| 3 | pasarela de pago.png | TE-0032 | PNG | 1530 × 933 px |
| 4 | validar asistencia del ticket.png | TE-0033 | PNG | 1350 × 933 px |

## 1. Retirar saldo de la billetera.png

| Campo | Valor |
|---|---|
| id | TE-0030 |
| Original | `02_Analisis_Software/Use case/Modulo de pagos/Retirar saldo de la billetera.png` |
| Formato y dimensiones | PNG · 1770 × 1083 px |
| Tamaño | 215844 bytes |
| SHA-256 | `6e2a9ebc40aedb375ed0d1d6286faec3028a001bbe9971c7797588376ea701f1` |
| Transcripción | No transcrita |

![Retirar saldo de la billetera](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20pagos/Retirar%20saldo%20de%20la%20billetera.png)

## 2. comprar ticket.png

| Campo | Valor |
|---|---|
| id | TE-0031 |
| Original | `02_Analisis_Software/Use case/Modulo de pagos/comprar ticket.png` |
| Formato y dimensiones | PNG · 2010 × 963 px |
| Tamaño | 204911 bytes |
| SHA-256 | `1748132c8509270c7e1691c0fe342711fa9cade93f074d08c84702e922759dc3` |
| Transcripción | No transcrita |

![comprar ticket](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20pagos/comprar%20ticket.png)

## 3. pasarela de pago.png

| Campo | Valor |
|---|---|
| id | TE-0032 |
| Original | `02_Analisis_Software/Use case/Modulo de pagos/pasarela de pago.png` |
| Formato y dimensiones | PNG · 1530 × 933 px |
| Tamaño | 164018 bytes |
| SHA-256 | `a8d7e065c7232139ace9d82c0606a5052b5ffeb548d0a9b15609ea5f3d679b07` |
| Transcripción | No transcrita |

![pasarela de pago](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20pagos/pasarela%20de%20pago.png)

## 4. validar asistencia del ticket.png

| Campo | Valor |
|---|---|
| id | TE-0033 |
| Original | `02_Analisis_Software/Use case/Modulo de pagos/validar asistencia del ticket.png` |
| Formato y dimensiones | PNG · 1350 × 933 px |
| Tamaño | 173110 bytes |
| SHA-256 | `b9f4411c6e6ed642534178ef134b5fd9c208b4b3294fe208db52b832f9db7c95` |
| Transcripción | No transcrita |

![validar asistencia del ticket](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20pagos/validar%20asistencia%20del%20ticket.png)

---

<a id="doc-te-0023-te-0024"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Use_case/Modulo_de_cartera/Indice_de_imagenes.md
id: [TE-0023, TE-0024]
title: "Índice de imágenes — 02_Analisis_Software/Use case/Modulo de cartera"
lang: es
source:
  - "02_Analisis_Software/Use case/Modulo de cartera/cancelacion de event.png"
  - "02_Analisis_Software/Use case/Modulo de cartera/pagar con cartera.png"
source_sha256:
  - 7739048d121a5bc9f95964d6137fdeaa984f1a626a308935eeaf1b3b899babb5
  - 645ffc8956d1b7d1e5551321568b1898481ad9a815db04459e0074f6415a073a
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

# Índice de imágenes — 02_Analisis_Software/Use case/Modulo de cartera

> Índice de las 2 imágenes de `02_Analisis_Software/Use case/Modulo de cartera/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | cancelacion de event.png | TE-0023 | PNG | 2268 × 1764 px |
| 2 | pagar con cartera.png | TE-0024 | PNG | 1383 × 993 px |

## 1. cancelacion de event.png

| Campo | Valor |
|---|---|
| id | TE-0023 |
| Original | `02_Analisis_Software/Use case/Modulo de cartera/cancelacion de event.png` |
| Formato y dimensiones | PNG · 2268 × 1764 px |
| Tamaño | 423864 bytes |
| SHA-256 | `7739048d121a5bc9f95964d6137fdeaa984f1a626a308935eeaf1b3b899babb5` |
| Transcripción | No transcrita |

![cancelacion de event](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20cartera/cancelacion%20de%20event.png)

## 2. pagar con cartera.png

| Campo | Valor |
|---|---|
| id | TE-0024 |
| Original | `02_Analisis_Software/Use case/Modulo de cartera/pagar con cartera.png` |
| Formato y dimensiones | PNG · 1383 × 993 px |
| Tamaño | 169192 bytes |
| SHA-256 | `645ffc8956d1b7d1e5551321568b1898481ad9a815db04459e0074f6415a073a` |
| Transcripción | No transcrita |

![pagar con cartera](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20cartera/pagar%20con%20cartera.png)

---

<a id="doc-te-0039"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Use_case/validar_asistencia_del_ticket.md
id: TE-0039
title: "validar asistencia del ticket"
lang: es
source: "02_Analisis_Software/Use case/validar asistencia del ticket"
source_sha256: 08607f237c385014091baaaefb1c6964c0ef0f2176d5e3945767306581f0b97c
conversion: "Python 3.12.3 xml.etree (elementos del mxfile) + copia literal del XML; sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "La representación visual (posiciones, colores y trazado de las líneas) no se dibuja; está en el XML copiado literalmente al final."
status: borrador
reviewed_by: ""
-->

# validar asistencia del ticket

> Representación en Markdown del archivo de draw.io `02_Analisis_Software/Use case/validar asistencia del ticket` (XML `mxfile`, sin extensión). Las tablas se obtienen leyendo el XML; solo se listan los elementos y relaciones que figuran en él. Al final se incluye el XML original completo.

Archivo original: [validar asistencia del ticket](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/validar%20asistencia%20del%20ticket)

## Datos del archivo

| Campo | Valor |
|---|---|
| Aplicación | `app.diagrams.net` (versión `29.5.5`) |
| Páginas | 1: «Página-1» |
| Celdas | 19 (incluidas las 2 celdas raíz sin contenido `0` y `1`) |

## Actores

| Actor | id de la celda | Geometría |
|---|---|---|
| User | `GZoZC9Dq-UAgP0DbEzWI-2` | x=40, y=440, width=40, height=80 |
| Payment Gateway | `GZoZC9Dq-UAgP0DbEzWI-11` | x=560, y=440, width=40, height=80 |

## Casos de uso

| Caso de uso | id de la celda | Geometría |
|---|---|---|
| Withdraw Wallet Balance | `GZoZC9Dq-UAgP0DbEzWI-5` | x=160, y=455, width=110, height=50 |
| Update Wallet Balance | `GZoZC9Dq-UAgP0DbEzWI-6` | x=240, y=570, width=110, height=50 |
| Update Wallet Transaction | `GZoZC9Dq-UAgP0DbEzWI-7` | x=350, y=520, width=110, height=50 |
| Create Withdrawal Request | `GZoZC9Dq-UAgP0DbEzWI-8` | x=330, y=370, width=130, height=50 |
| Validate Wallet Balance | `GZoZC9Dq-UAgP0DbEzWI-9` | x=220, y=320, width=110, height=50 |
| Request Transfer to Gateway | `GZoZC9Dq-UAgP0DbEzWI-10` | x=360, y=455, width=130, height=50 |

## Otros elementos

| Elemento | Texto | id de la celda | Geometría |
|---|---|---|---|
| Rectángulo | (sin texto) | `GZoZC9Dq-UAgP0DbEzWI-1` | x=120, y=280, width=400, height=360 |
| Cuadro de texto | Tu evento | `GZoZC9Dq-UAgP0DbEzWI-21` | x=120, y=280, width=60, height=30 |

## Relaciones

| Origen | Relación | Destino | id de la celda |
|---|---|---|---|
| Withdraw Wallet Balance | `<<include>>` (línea discontinua con flecha abierta) | Validate Wallet Balance | `GZoZC9Dq-UAgP0DbEzWI-12` |
| Withdraw Wallet Balance | `<<include>>` (línea discontinua con flecha abierta) | Request Transfer to Gateway | `GZoZC9Dq-UAgP0DbEzWI-13` |
| Withdraw Wallet Balance | `<<include>>` (línea discontinua con flecha abierta) | Create Withdrawal Request | `GZoZC9Dq-UAgP0DbEzWI-14` |
| Withdraw Wallet Balance | `<<include>>` (línea discontinua con flecha abierta) | Update Wallet Transaction | `GZoZC9Dq-UAgP0DbEzWI-15` |
| Withdraw Wallet Balance | `<<include>>` (línea discontinua con flecha abierta) | Update Wallet Balance | `GZoZC9Dq-UAgP0DbEzWI-16` |
| Payment Gateway | asociación (línea continua sin flecha) | Request Transfer to Gateway | `GZoZC9Dq-UAgP0DbEzWI-17` |
| User | asociación (línea continua sin flecha) | Withdraw Wallet Balance | `GZoZC9Dq-UAgP0DbEzWI-18` |

## Observaciones

- El nombre del archivo («validar asistencia del ticket») no coincide con su contenido: el diagrama trata de la retirada de saldo de la billetera (*Withdraw Wallet Balance*).
- Comparación visual: los 6 casos de uso y las 5 relaciones `<<include>>` coinciden con la imagen [`02_Analisis_Software/Use case/Modulo de pagos/Retirar saldo de la billetera.png`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20pagos/Retirar%20saldo%20de%20la%20billetera.png). La imagen [`02_Analisis_Software/Use case/Modulo de pagos/validar asistencia del ticket.png`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/Use%20case/Modulo%20de%20pagos/validar%20asistencia%20del%20ticket.png) muestra otro diagrama (*QR Check-in*).
- En la imagen «Retirar saldo de la billetera.png» los nombres de los actores no se ven; en el XML son «User» y «Payment Gateway».
- No se transcribe la posición exacta de las líneas ni los estilos más allá de lo indicado en las tablas; esos datos están en el XML de abajo.

## XML original

```xml
<mxfile host="app.diagrams.net" agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36" version="29.5.5">
  <diagram name="Página-1" id="VKdI3L7W4giT9jDppxJR">
    <mxGraphModel grid="1" page="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" pageScale="1" pageWidth="827" pageHeight="1169" math="0" shadow="0">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-1" parent="1" style="rounded=0;whiteSpace=wrap;html=1;" value="" vertex="1">
          <mxGeometry height="360" width="400" x="120" y="280" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-2" parent="1" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;" value="User" vertex="1">
          <mxGeometry height="80" width="40" x="40" y="440" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-5" parent="1" style="ellipse;whiteSpace=wrap;html=1;" value="Withdraw Wallet Balance" vertex="1">
          <mxGeometry height="50" width="110" x="160" y="455" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-6" parent="1" style="ellipse;whiteSpace=wrap;html=1;" value="Update Wallet Balance" vertex="1">
          <mxGeometry height="50" width="110" x="240" y="570" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-7" parent="1" style="ellipse;whiteSpace=wrap;html=1;" value="Update Wallet Transaction" vertex="1">
          <mxGeometry height="50" width="110" x="350" y="520" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-8" parent="1" style="ellipse;whiteSpace=wrap;html=1;" value="Create Withdrawal Request" vertex="1">
          <mxGeometry height="50" width="130" x="330" y="370" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-9" parent="1" style="ellipse;whiteSpace=wrap;html=1;" value="Validate Wallet Balance" vertex="1">
          <mxGeometry height="50" width="110" x="220" y="320" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-10" parent="1" style="ellipse;whiteSpace=wrap;html=1;" value="Request Transfer to Gateway" vertex="1">
          <mxGeometry height="50" width="130" x="360" y="455" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-11" parent="1" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;" value="Payment Gateway" vertex="1">
          <mxGeometry height="80" width="40" x="560" y="440" as="geometry" />
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-12" edge="1" parent="1" source="GZoZC9Dq-UAgP0DbEzWI-5" style="html=1;verticalAlign=bottom;endArrow=open;dashed=1;endSize=8;curved=0;rounded=0;entryX=0;entryY=1;entryDx=0;entryDy=0;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" target="GZoZC9Dq-UAgP0DbEzWI-9" value="&amp;lt;&amp;lt;include&amp;gt;&amp;gt;">
          <mxGeometry relative="1" as="geometry">
            <mxPoint x="80" y="350" as="sourcePoint" />
            <mxPoint x="126" y="310" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-13" edge="1" parent="1" source="GZoZC9Dq-UAgP0DbEzWI-5" style="html=1;verticalAlign=bottom;endArrow=open;dashed=1;endSize=8;curved=0;rounded=0;entryX=0;entryY=0.5;entryDx=0;entryDy=0;exitX=1;exitY=0.5;exitDx=0;exitDy=0;" target="GZoZC9Dq-UAgP0DbEzWI-10" value="&amp;lt;&amp;lt;include&amp;gt;&amp;gt;">
          <mxGeometry relative="1" as="geometry">
            <mxPoint x="580" y="390" as="sourcePoint" />
            <mxPoint x="146" y="260" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-14" edge="1" parent="1" source="GZoZC9Dq-UAgP0DbEzWI-5" style="html=1;verticalAlign=bottom;endArrow=open;dashed=1;endSize=8;curved=0;rounded=0;exitX=1;exitY=0;exitDx=0;exitDy=0;entryX=0;entryY=1;entryDx=0;entryDy=0;" target="GZoZC9Dq-UAgP0DbEzWI-8" value="&amp;lt;&amp;lt;include&amp;gt;&amp;gt;">
          <mxGeometry relative="1" as="geometry">
            <mxPoint x="50" y="352" as="sourcePoint" />
            <mxPoint x="156" y="340" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-15" edge="1" parent="1" source="GZoZC9Dq-UAgP0DbEzWI-5" style="html=1;verticalAlign=bottom;endArrow=open;dashed=1;endSize=8;curved=0;rounded=0;exitX=1;exitY=1;exitDx=0;exitDy=0;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" target="GZoZC9Dq-UAgP0DbEzWI-7" value="&amp;lt;&amp;lt;include&amp;gt;&amp;gt;">
          <mxGeometry relative="1" as="geometry">
            <mxPoint x="30" y="362" as="sourcePoint" />
            <mxPoint x="136" y="350" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-16" edge="1" parent="1" source="GZoZC9Dq-UAgP0DbEzWI-5" style="html=1;verticalAlign=bottom;endArrow=open;dashed=1;endSize=8;curved=0;rounded=0;entryX=0;entryY=0;entryDx=0;entryDy=0;exitX=0.5;exitY=1;exitDx=0;exitDy=0;" target="GZoZC9Dq-UAgP0DbEzWI-6" value="&amp;lt;&amp;lt;include&amp;gt;&amp;gt;">
          <mxGeometry relative="1" as="geometry">
            <mxPoint x="320" y="660" as="sourcePoint" />
            <mxPoint x="136" y="380" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-17" edge="1" parent="1" source="GZoZC9Dq-UAgP0DbEzWI-11" style="endArrow=none;html=1;rounded=0;entryX=1;entryY=0.5;entryDx=0;entryDy=0;" target="GZoZC9Dq-UAgP0DbEzWI-10" value="">
          <mxGeometry relative="1" as="geometry">
            <mxPoint x="250" y="700" as="sourcePoint" />
            <mxPoint x="530" y="710" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-18" edge="1" parent="1" source="GZoZC9Dq-UAgP0DbEzWI-2" style="endArrow=none;html=1;rounded=0;entryX=0;entryY=0.5;entryDx=0;entryDy=0;" target="GZoZC9Dq-UAgP0DbEzWI-5" value="">
          <mxGeometry relative="1" as="geometry">
            <mxPoint y="590" as="sourcePoint" />
            <mxPoint x="160" y="590" as="targetPoint" />
          </mxGeometry>
        </mxCell>
        <mxCell id="GZoZC9Dq-UAgP0DbEzWI-21" parent="1" style="text;html=1;whiteSpace=wrap;strokeColor=none;fillColor=none;align=center;verticalAlign=middle;rounded=0;" value="Tu evento" vertex="1">
          <mxGeometry height="30" width="60" x="120" y="280" as="geometry" />
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
```

---

<a id="doc-te-0040-te-0041"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/config/Indice_de_imagenes.md
id: [TE-0040, TE-0041]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/config"
lang: es
source:
  - "02_Analisis_Software/class diagram/config/Complete Config Class Diagram.png"
  - "02_Analisis_Software/class diagram/config/Simple Config Class Diagram.png"
source_sha256:
  - a1cb4c72abe375957c2f00ee6e4493a4770d5e41a20c06a15bbfc97af2076f71
  - 5ac759d5acb7e84b360ece4e94e770355dd109ab7c14c321fa6921893ff9749e
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/config

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/config/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Config Class Diagram.png | TE-0040 | PNG | 1940 × 507 px |
| 2 | Simple Config Class Diagram.png | TE-0041 | PNG | 1168 × 313 px |

## 1. Complete Config Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0040 |
| Original | `02_Analisis_Software/class diagram/config/Complete Config Class Diagram.png` |
| Formato y dimensiones | PNG · 1940 × 507 px |
| Tamaño | 107243 bytes |
| SHA-256 | `a1cb4c72abe375957c2f00ee6e4493a4770d5e41a20c06a15bbfc97af2076f71` |
| Transcripción | No transcrita |

![Complete Config Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/config/Complete%20Config%20Class%20Diagram.png)

## 2. Simple Config Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0041 |
| Original | `02_Analisis_Software/class diagram/config/Simple Config Class Diagram.png` |
| Formato y dimensiones | PNG · 1168 × 313 px |
| Tamaño | 28050 bytes |
| SHA-256 | `5ac759d5acb7e84b360ece4e94e770355dd109ab7c14c321fa6921893ff9749e` |
| Transcripción | No transcrita |

![Simple Config Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/config/Simple%20Config%20Class%20Diagram.png)

---

<a id="doc-te-0042-te-0043"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/controller/Indice_de_imagenes.md
id: [TE-0042, TE-0043]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/controller"
lang: es
source:
  - "02_Analisis_Software/class diagram/controller/Complete Controller Class Diagram.png"
  - "02_Analisis_Software/class diagram/controller/Simple Controller Class Diagram.png"
source_sha256:
  - 49750d7b9f51a49c001f7205c254bd56502e067be9a3566b31eb9f9a29d52aa3
  - 0f204190709d98435d88929398e279576566aefacd96f0a9e6f86ba7dd94e3f0
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/controller

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/controller/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Controller Class Diagram.png | TE-0042 | PNG | 2909 × 1231 px |
| 2 | Simple Controller Class Diagram.png | TE-0043 | PNG | 952 × 493 px |

## 1. Complete Controller Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0042 |
| Original | `02_Analisis_Software/class diagram/controller/Complete Controller Class Diagram.png` |
| Formato y dimensiones | PNG · 2909 × 1231 px |
| Tamaño | 381516 bytes |
| SHA-256 | `49750d7b9f51a49c001f7205c254bd56502e067be9a3566b31eb9f9a29d52aa3` |
| Transcripción | No transcrita |

![Complete Controller Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/controller/Complete%20Controller%20Class%20Diagram.png)

## 2. Simple Controller Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0043 |
| Original | `02_Analisis_Software/class diagram/controller/Simple Controller Class Diagram.png` |
| Formato y dimensiones | PNG · 952 × 493 px |
| Tamaño | 30254 bytes |
| SHA-256 | `0f204190709d98435d88929398e279576566aefacd96f0a9e6f86ba7dd94e3f0` |
| Transcripción | No transcrita |

![Simple Controller Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/controller/Simple%20Controller%20Class%20Diagram.png)

---

<a id="doc-te-0044-te-0045"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/controller/email/Indice_de_imagenes.md
id: [TE-0044, TE-0045]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/controller/email"
lang: es
source:
  - "02_Analisis_Software/class diagram/controller/email/Complete Controller Email Class Diagram.png"
  - "02_Analisis_Software/class diagram/controller/email/Simple Controller Email Class Diagram.png"
source_sha256:
  - d6dfcbb6cda4a4e862c7e05cecf143ff65d953b4ef4659b278bde3071eaadb84
  - 0433b121f756cfef607b410e843202692f45c2d8906b691a09c791722580ea50
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/controller/email

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/controller/email/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Controller Email Class Diagram.png | TE-0044 | PNG | 474 × 240 px |
| 2 | Simple Controller Email Class Diagram.png | TE-0045 | PNG | 251 × 187 px |

## 1. Complete Controller Email Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0044 |
| Original | `02_Analisis_Software/class diagram/controller/email/Complete Controller Email Class Diagram.png` |
| Formato y dimensiones | PNG · 474 × 240 px |
| Tamaño | 15470 bytes |
| SHA-256 | `d6dfcbb6cda4a4e862c7e05cecf143ff65d953b4ef4659b278bde3071eaadb84` |
| Transcripción | No transcrita |

![Complete Controller Email Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/controller/email/Complete%20Controller%20Email%20Class%20Diagram.png)

## 2. Simple Controller Email Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0045 |
| Original | `02_Analisis_Software/class diagram/controller/email/Simple Controller Email Class Diagram.png` |
| Formato y dimensiones | PNG · 251 × 187 px |
| Tamaño | 7677 bytes |
| SHA-256 | `0433b121f756cfef607b410e843202692f45c2d8906b691a09c791722580ea50` |
| Transcripción | No transcrita |

![Simple Controller Email Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/controller/email/Simple%20Controller%20Email%20Class%20Diagram.png)

---

<a id="doc-te-0046-te-0047"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/data/Indice_de_imagenes.md
id: [TE-0046, TE-0047]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/data"
lang: es
source:
  - "02_Analisis_Software/class diagram/data/Complete Data Class Diagram.png"
  - "02_Analisis_Software/class diagram/data/Simple Data Class Diagram.png"
source_sha256:
  - 404d041f1e76c90efe4b856fb9e8e0629ce178ed0900fc9d59606497614c0876
  - cca861802fc1b72217c29f7385d4f38bcabcc61144b6a33755ed268dddb8ac51
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/data

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/data/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Data Class Diagram.png | TE-0046 | PNG | 1723 × 275 px |
| 2 | Simple Data Class Diagram.png | TE-0047 | PNG | 1514 × 187 px |

## 1. Complete Data Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0046 |
| Original | `02_Analisis_Software/class diagram/data/Complete Data Class Diagram.png` |
| Formato y dimensiones | PNG · 1723 × 275 px |
| Tamaño | 63057 bytes |
| SHA-256 | `404d041f1e76c90efe4b856fb9e8e0629ce178ed0900fc9d59606497614c0876` |
| Transcripción | No transcrita |

![Complete Data Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/data/Complete%20Data%20Class%20Diagram.png)

## 2. Simple Data Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0047 |
| Original | `02_Analisis_Software/class diagram/data/Simple Data Class Diagram.png` |
| Formato y dimensiones | PNG · 1514 × 187 px |
| Tamaño | 27747 bytes |
| SHA-256 | `cca861802fc1b72217c29f7385d4f38bcabcc61144b6a33755ed268dddb8ac51` |
| Transcripción | No transcrita |

![Simple Data Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/data/Simple%20Data%20Class%20Diagram.png)

---

<a id="doc-te-0048-te-0049"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/dto/Indice_de_imagenes.md
id: [TE-0048, TE-0049]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/dto"
lang: es
source:
  - "02_Analisis_Software/class diagram/dto/Complete DTO Class Diagram.png"
  - "02_Analisis_Software/class diagram/dto/Simple DTO Class Diagram.png"
source_sha256:
  - 50166e7f78032affd5e8d3faca7cfd0335d7994c1d6879bb3176a889baf4a421
  - 2eec240bdbb54215888e0f0bea25bb8ae607806d720adb3a52d1d0a858a6a2ce
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/dto

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/dto/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete DTO Class Diagram.png | TE-0048 | PNG | 3269 × 2116 px |
| 2 | Simple DTO Class Diagram.png | TE-0049 | PNG | 1661 × 514 px |

## 1. Complete DTO Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0048 |
| Original | `02_Analisis_Software/class diagram/dto/Complete DTO Class Diagram.png` |
| Formato y dimensiones | PNG · 3269 × 2116 px |
| Tamaño | 642358 bytes |
| SHA-256 | `50166e7f78032affd5e8d3faca7cfd0335d7994c1d6879bb3176a889baf4a421` |
| Transcripción | No transcrita |

![Complete DTO Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/Complete%20DTO%20Class%20Diagram.png)

## 2. Simple DTO Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0049 |
| Original | `02_Analisis_Software/class diagram/dto/Simple DTO Class Diagram.png` |
| Formato y dimensiones | PNG · 1661 × 514 px |
| Tamaño | 50445 bytes |
| SHA-256 | `2eec240bdbb54215888e0f0bea25bb8ae607806d720adb3a52d1d0a858a6a2ce` |
| Transcripción | No transcrita |

![Simple DTO Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/Simple%20DTO%20Class%20Diagram.png)

---

<a id="doc-te-0050-te-0051"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/dto/email/Indice_de_imagenes.md
id: [TE-0050, TE-0051]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/dto/email"
lang: es
source:
  - "02_Analisis_Software/class diagram/dto/email/Complete DTO Email Class Diagram.png"
  - "02_Analisis_Software/class diagram/dto/email/Simple DTO Email Class Diagram.png"
source_sha256:
  - be166276bd2d7b1f6186e27e9cb30109745d4ff3c53a6b23f43f34e23ae9365c
  - 23d9bba7b87ad7a2ef22ab2790b8d7e0ff8e87ff12116dac187288266789ef4f
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/dto/email

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/dto/email/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete DTO Email Class Diagram.png | TE-0050 | PNG | 331 × 261 px |
| 2 | Simple DTO Email Class Diagram.png | TE-0051 | PNG | 202 × 68 px |

## 1. Complete DTO Email Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0050 |
| Original | `02_Analisis_Software/class diagram/dto/email/Complete DTO Email Class Diagram.png` |
| Formato y dimensiones | PNG · 331 × 261 px |
| Tamaño | 14412 bytes |
| SHA-256 | `be166276bd2d7b1f6186e27e9cb30109745d4ff3c53a6b23f43f34e23ae9365c` |
| Transcripción | No transcrita |

![Complete DTO Email Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/email/Complete%20DTO%20Email%20Class%20Diagram.png)

## 2. Simple DTO Email Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0051 |
| Original | `02_Analisis_Software/class diagram/dto/email/Simple DTO Email Class Diagram.png` |
| Formato y dimensiones | PNG · 202 × 68 px |
| Tamaño | 3215 bytes |
| SHA-256 | `23d9bba7b87ad7a2ef22ab2790b8d7e0ff8e87ff12116dac187288266789ef4f` |
| Transcripción | No transcrita |

![Simple DTO Email Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/email/Simple%20DTO%20Email%20Class%20Diagram.png)

---

<a id="doc-te-0052-te-0053"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/dto/requests/Indice_de_imagenes.md
id: [TE-0052, TE-0053]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/dto/requests"
lang: es
source:
  - "02_Analisis_Software/class diagram/dto/requests/Complete DTO Requests Class Diagram.png"
  - "02_Analisis_Software/class diagram/dto/requests/Simple DTO Requests Class Diagram.png"
source_sha256:
  - 71c508c68cd82bc75db407cc380457a0056b28c0c22d92d6cfa9ef2a5fc23ed3
  - 4c87842c1bb96b3c5bab9f539b57520ab78e5d3a880853c5982755bdbe4dc65f
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/dto/requests

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/dto/requests/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete DTO Requests Class Diagram.png | TE-0052 | PNG | 805 × 574 px |
| 2 | Simple DTO Requests Class Diagram.png | TE-0053 | PNG | 550 × 187 px |

## 1. Complete DTO Requests Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0052 |
| Original | `02_Analisis_Software/class diagram/dto/requests/Complete DTO Requests Class Diagram.png` |
| Formato y dimensiones | PNG · 805 × 574 px |
| Tamaño | 74063 bytes |
| SHA-256 | `71c508c68cd82bc75db407cc380457a0056b28c0c22d92d6cfa9ef2a5fc23ed3` |
| Transcripción | No transcrita |

![Complete DTO Requests Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/requests/Complete%20DTO%20Requests%20Class%20Diagram.png)

## 2. Simple DTO Requests Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0053 |
| Original | `02_Analisis_Software/class diagram/dto/requests/Simple DTO Requests Class Diagram.png` |
| Formato y dimensiones | PNG · 550 × 187 px |
| Tamaño | 11081 bytes |
| SHA-256 | `4c87842c1bb96b3c5bab9f539b57520ab78e5d3a880853c5982755bdbe4dc65f` |
| Transcripción | No transcrita |

![Simple DTO Requests Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/requests/Simple%20DTO%20Requests%20Class%20Diagram.png)

---

<a id="doc-te-0054-te-0055"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/dto/responses/Indice_de_imagenes.md
id: [TE-0054, TE-0055]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/dto/responses"
lang: es
source:
  - "02_Analisis_Software/class diagram/dto/responses/Complete DTO Responses Class Diagram.png"
  - "02_Analisis_Software/class diagram/dto/responses/Simple DTO Responses Class Diagram.png"
source_sha256:
  - a9139c1c216f04aee08cc9ffa8a8e5d41f374867591635ef4e7f232cca6664ad
  - 0c165f9c0c69a9e93216cfd31f2be436a2c63bdccc801895b854ba7fdf8828b0
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/dto/responses

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/dto/responses/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete DTO Responses Class Diagram.png | TE-0054 | PNG | 691 × 717 px |
| 2 | Simple DTO Responses Class Diagram.png | TE-0055 | PNG | 352 × 172 px |

## 1. Complete DTO Responses Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0054 |
| Original | `02_Analisis_Software/class diagram/dto/responses/Complete DTO Responses Class Diagram.png` |
| Formato y dimensiones | PNG · 691 × 717 px |
| Tamaño | 77746 bytes |
| SHA-256 | `a9139c1c216f04aee08cc9ffa8a8e5d41f374867591635ef4e7f232cca6664ad` |
| Transcripción | No transcrita |

![Complete DTO Responses Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/responses/Complete%20DTO%20Responses%20Class%20Diagram.png)

## 2. Simple DTO Responses Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0055 |
| Original | `02_Analisis_Software/class diagram/dto/responses/Simple DTO Responses Class Diagram.png` |
| Formato y dimensiones | PNG · 352 × 172 px |
| Tamaño | 7871 bytes |
| SHA-256 | `0c165f9c0c69a9e93216cfd31f2be436a2c63bdccc801895b854ba7fdf8828b0` |
| Transcripción | No transcrita |

![Simple DTO Responses Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/dto/responses/Simple%20DTO%20Responses%20Class%20Diagram.png)

---

<a id="doc-te-0056-te-0057"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/jwt/Indice_de_imagenes.md
id: [TE-0056, TE-0057]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/jwt"
lang: es
source:
  - "02_Analisis_Software/class diagram/jwt/Complete JWT Class Diagram.png"
  - "02_Analisis_Software/class diagram/jwt/Simple JWT Class Diagram.png"
source_sha256:
  - aed49b5fe0367a5543af793ebcb1ba0431162895ec3e1a60f1d5826da060d986
  - f39f884911baa80ab4014a2aff195ca2e974813f894cbb955cf914f114bead69
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/jwt

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/jwt/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete JWT Class Diagram.png | TE-0056 | PNG | 508 × 521 px |
| 2 | Simple JWT Class Diagram.png | TE-0057 | PNG | 181 × 187 px |

## 1. Complete JWT Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0056 |
| Original | `02_Analisis_Software/class diagram/jwt/Complete JWT Class Diagram.png` |
| Formato y dimensiones | PNG · 508 × 521 px |
| Tamaño | 36645 bytes |
| SHA-256 | `aed49b5fe0367a5543af793ebcb1ba0431162895ec3e1a60f1d5826da060d986` |
| Transcripción | No transcrita |

![Complete JWT Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/jwt/Complete%20JWT%20Class%20Diagram.png)

## 2. Simple JWT Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0057 |
| Original | `02_Analisis_Software/class diagram/jwt/Simple JWT Class Diagram.png` |
| Formato y dimensiones | PNG · 181 × 187 px |
| Tamaño | 6513 bytes |
| SHA-256 | `f39f884911baa80ab4014a2aff195ca2e974813f894cbb955cf914f114bead69` |
| Transcripción | No transcrita |

![Simple JWT Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/jwt/Simple%20JWT%20Class%20Diagram.png)

---

<a id="doc-te-0058-te-0059"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/model/Indice_de_imagenes.md
id: [TE-0058, TE-0059]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/model"
lang: es
source:
  - "02_Analisis_Software/class diagram/model/Complete Class Diagram.png"
  - "02_Analisis_Software/class diagram/model/Simple Class Diagram.png"
source_sha256:
  - 708af37867dec53bce9f89f55025a51f8ca36d3fd2694e6e6b7d8da56d53a933
  - 7455e03b5e7b9185b161b0b11f8a37f099b9526e1b268b15b14d70cfb29b67b8
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/model

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/model/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Class Diagram.png | TE-0058 | PNG | 3633 × 3497 px |
| 2 | Simple Class Diagram.png | TE-0059 | PNG | 1562 × 978 px |

## 1. Complete Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0058 |
| Original | `02_Analisis_Software/class diagram/model/Complete Class Diagram.png` |
| Formato y dimensiones | PNG · 3633 × 3497 px |
| Tamaño | 784198 bytes |
| SHA-256 | `708af37867dec53bce9f89f55025a51f8ca36d3fd2694e6e6b7d8da56d53a933` |
| Transcripción | No transcrita |

![Complete Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/model/Complete%20Class%20Diagram.png)

## 2. Simple Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0059 |
| Original | `02_Analisis_Software/class diagram/model/Simple Class Diagram.png` |
| Formato y dimensiones | PNG · 1562 × 978 px |
| Tamaño | 110065 bytes |
| SHA-256 | `7455e03b5e7b9185b161b0b11f8a37f099b9526e1b268b15b14d70cfb29b67b8` |
| Transcripción | No transcrita |

![Simple Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/model/Simple%20Class%20Diagram.png)

---

<a id="doc-te-0060-te-0061"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/repository/Indice_de_imagenes.md
id: [TE-0060, TE-0061]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/repository"
lang: es
source:
  - "02_Analisis_Software/class diagram/repository/Complete Repository Class Diagram.png"
  - "02_Analisis_Software/class diagram/repository/Simple Repository Class Diagram.png"
source_sha256:
  - 260caa378e2e6d0e3faf2b1ed73c2f4a3817c1d2f2f693c8f7cefb630fbc1f88
  - a444c11b05d57bc1ad41bef9829c72a0fc72377e93a6a90b2981163066dd732e
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/repository

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/repository/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Repository Class Diagram.png | TE-0060 | PNG | 1777 × 845 px |
| 2 | Simple Repository Class Diagram.png | TE-0061 | PNG | 1111 × 493 px |

## 1. Complete Repository Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0060 |
| Original | `02_Analisis_Software/class diagram/repository/Complete Repository Class Diagram.png` |
| Formato y dimensiones | PNG · 1777 × 845 px |
| Tamaño | 156725 bytes |
| SHA-256 | `260caa378e2e6d0e3faf2b1ed73c2f4a3817c1d2f2f693c8f7cefb630fbc1f88` |
| Transcripción | No transcrita |

![Complete Repository Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/repository/Complete%20Repository%20Class%20Diagram.png)

## 2. Simple Repository Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0061 |
| Original | `02_Analisis_Software/class diagram/repository/Simple Repository Class Diagram.png` |
| Formato y dimensiones | PNG · 1111 × 493 px |
| Tamaño | 32898 bytes |
| SHA-256 | `a444c11b05d57bc1ad41bef9829c72a0fc72377e93a6a90b2981163066dd732e` |
| Transcripción | No transcrita |

![Simple Repository Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/repository/Simple%20Repository%20Class%20Diagram.png)

---

<a id="doc-te-0062-te-0063"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/service/Indice_de_imagenes.md
id: [TE-0062, TE-0063]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/service"
lang: es
source:
  - "02_Analisis_Software/class diagram/service/Complete Service Class Diagram.png"
  - "02_Analisis_Software/class diagram/service/Simple Service Class Diagram.png"
source_sha256:
  - 44131b6778299b1df60bf1c8e31a711f39fc344cb3eb4298510271869623f5c8
  - 705594273e8b24eb909c90e9f23aaf453057feffe6c3132ae82b58e6a73b8d8b
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/service

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/service/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Service Class Diagram.png | TE-0062 | PNG | 1692 × 4096 px |
| 2 | Simple Service Class Diagram.png | TE-0063 | PNG | 762 × 4095 px |

## 1. Complete Service Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0062 |
| Original | `02_Analisis_Software/class diagram/service/Complete Service Class Diagram.png` |
| Formato y dimensiones | PNG · 1692 × 4096 px |
| Tamaño | 427791 bytes |
| SHA-256 | `44131b6778299b1df60bf1c8e31a711f39fc344cb3eb4298510271869623f5c8` |
| Transcripción | No transcrita |

![Complete Service Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/Complete%20Service%20Class%20Diagram.png)

## 2. Simple Service Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0063 |
| Original | `02_Analisis_Software/class diagram/service/Simple Service Class Diagram.png` |
| Formato y dimensiones | PNG · 762 × 4095 px |
| Tamaño | 184802 bytes |
| SHA-256 | `705594273e8b24eb909c90e9f23aaf453057feffe6c3132ae82b58e6a73b8d8b` |
| Transcripción | No transcrita |

![Simple Service Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/Simple%20Service%20Class%20Diagram.png)

---

<a id="doc-te-0064-te-0065"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/service/aws/Indice_de_imagenes.md
id: [TE-0064, TE-0065]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/service/aws"
lang: es
source:
  - "02_Analisis_Software/class diagram/service/aws/Complete Service AWS Class Diagram.png"
  - "02_Analisis_Software/class diagram/service/aws/Simple Service AWS Class Diagram.png"
source_sha256:
  - 76ec36035e4ba183bc5f9fa9ea34a2e06eecb271da7f4b146346bcecbb8d6639
  - df72e87046dded30f50c15b98692628849415c956a97be3d020886cbc21bbbc0
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/service/aws

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/service/aws/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Service AWS Class Diagram.png | TE-0064 | PNG | 313 × 293 px |
| 2 | Simple Service AWS Class Diagram.png | TE-0065 | PNG | 140 × 187 px |

## 1. Complete Service AWS Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0064 |
| Original | `02_Analisis_Software/class diagram/service/aws/Complete Service AWS Class Diagram.png` |
| Formato y dimensiones | PNG · 313 × 293 px |
| Tamaño | 14286 bytes |
| SHA-256 | `76ec36035e4ba183bc5f9fa9ea34a2e06eecb271da7f4b146346bcecbb8d6639` |
| Transcripción | No transcrita |

![Complete Service AWS Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/aws/Complete%20Service%20AWS%20Class%20Diagram.png)

## 2. Simple Service AWS Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0065 |
| Original | `02_Analisis_Software/class diagram/service/aws/Simple Service AWS Class Diagram.png` |
| Formato y dimensiones | PNG · 140 × 187 px |
| Tamaño | 5792 bytes |
| SHA-256 | `df72e87046dded30f50c15b98692628849415c956a97be3d020886cbc21bbbc0` |
| Transcripción | No transcrita |

![Simple Service AWS Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/aws/Simple%20Service%20AWS%20Class%20Diagram.png)

---

<a id="doc-te-0066-te-0067"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/service/email/Indice_de_imagenes.md
id: [TE-0066, TE-0067]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/service/email"
lang: es
source:
  - "02_Analisis_Software/class diagram/service/email/Complete Service Email Class Diagram.png"
  - "02_Analisis_Software/class diagram/service/email/Simple Service Email Class Diagram.png"
source_sha256:
  - 59eaab9ec34a79610ab64891b1b0b70f85d198521d3d50248cf5349a1232ab98
  - e9e724559576809ddb660ebf01c4c7bec54f84e2b963be4a0b08bbd00a828c0a
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/service/email

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/service/email/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Service Email Class Diagram.png | TE-0066 | PNG | 1488 × 221 px |
| 2 | Simple Service Email Class Diagram.png | TE-0067 | PNG | 975 × 169 px |

## 1. Complete Service Email Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0066 |
| Original | `02_Analisis_Software/class diagram/service/email/Complete Service Email Class Diagram.png` |
| Formato y dimensiones | PNG · 1488 × 221 px |
| Tamaño | 34968 bytes |
| SHA-256 | `59eaab9ec34a79610ab64891b1b0b70f85d198521d3d50248cf5349a1232ab98` |
| Transcripción | No transcrita |

![Complete Service Email Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/email/Complete%20Service%20Email%20Class%20Diagram.png)

## 2. Simple Service Email Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0067 |
| Original | `02_Analisis_Software/class diagram/service/email/Simple Service Email Class Diagram.png` |
| Formato y dimensiones | PNG · 975 × 169 px |
| Tamaño | 21074 bytes |
| SHA-256 | `e9e724559576809ddb660ebf01c4c7bec54f84e2b963be4a0b08bbd00a828c0a` |
| Transcripción | No transcrita |

![Simple Service Email Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/email/Simple%20Service%20Email%20Class%20Diagram.png)

---

<a id="doc-te-0068-te-0069"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/service/impl/Indice_de_imagenes.md
id: [TE-0068, TE-0069]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/service/impl"
lang: es
source:
  - "02_Analisis_Software/class diagram/service/impl/Complete Service Implementation Class Diagram.png"
  - "02_Analisis_Software/class diagram/service/impl/Simple Service Implementation Class Diagram.png"
source_sha256:
  - cffdc8b5aa7a2c714e6c57862972915c46d84daeafe99ab00b84306dfc4e2314
  - 73a983e1316d5c0a1a503400aadc35e65a9c136e1f07b2adf832a7bf12b3c74c
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/service/impl

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/service/impl/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Service Implementation Class Diagram.png | TE-0068 | PNG | 4096 × 446 px |
| 2 | Simple Service Implementation Class Diagram.png | TE-0069 | PNG | 4096 × 288 px |

## 1. Complete Service Implementation Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0068 |
| Original | `02_Analisis_Software/class diagram/service/impl/Complete Service Implementation Class Diagram.png` |
| Formato y dimensiones | PNG · 4096 × 446 px |
| Tamaño | 124930 bytes |
| SHA-256 | `cffdc8b5aa7a2c714e6c57862972915c46d84daeafe99ab00b84306dfc4e2314` |
| Transcripción | No transcrita |

![Complete Service Implementation Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/impl/Complete%20Service%20Implementation%20Class%20Diagram.png)

## 2. Simple Service Implementation Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0069 |
| Original | `02_Analisis_Software/class diagram/service/impl/Simple Service Implementation Class Diagram.png` |
| Formato y dimensiones | PNG · 4096 × 288 px |
| Tamaño | 51674 bytes |
| SHA-256 | `73a983e1316d5c0a1a503400aadc35e65a9c136e1f07b2adf832a7bf12b3c74c` |
| Transcripción | No transcrita |

![Simple Service Implementation Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/impl/Simple%20Service%20Implementation%20Class%20Diagram.png)

---

<a id="doc-te-0070-te-0071"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/service/jwt/Indice_de_imagenes.md
id: [TE-0070, TE-0071]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/service/jwt"
lang: es
source:
  - "02_Analisis_Software/class diagram/service/jwt/Complete Service JWT Class Diagram.png"
  - "02_Analisis_Software/class diagram/service/jwt/Simple Service JWT Class Diagram.png"
source_sha256:
  - e0a2942e7d35a808753e48f192b44b7a6f34095032319f2424c780d6fc0dd8e7
  - 92f138cf32ad73d654dea75cf3baec724a8c3d0babe04e3f78c5dc098742fb41
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/service/jwt

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/service/jwt/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Service JWT Class Diagram.png | TE-0070 | PNG | 398 × 332 px |
| 2 | Simple Service JWT Class Diagram.png | TE-0071 | PNG | 115 × 68 px |

## 1. Complete Service JWT Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0070 |
| Original | `02_Analisis_Software/class diagram/service/jwt/Complete Service JWT Class Diagram.png` |
| Formato y dimensiones | PNG · 398 × 332 px |
| Tamaño | 21911 bytes |
| SHA-256 | `e0a2942e7d35a808753e48f192b44b7a6f34095032319f2424c780d6fc0dd8e7` |
| Transcripción | No transcrita |

![Complete Service JWT Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/jwt/Complete%20Service%20JWT%20Class%20Diagram.png)

## 2. Simple Service JWT Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0071 |
| Original | `02_Analisis_Software/class diagram/service/jwt/Simple Service JWT Class Diagram.png` |
| Formato y dimensiones | PNG · 115 × 68 px |
| Tamaño | 2429 bytes |
| SHA-256 | `92f138cf32ad73d654dea75cf3baec724a8c3d0babe04e3f78c5dc098742fb41` |
| Transcripción | No transcrita |

![Simple Service JWT Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/jwt/Simple%20Service%20JWT%20Class%20Diagram.png)

---

<a id="doc-te-0072-te-0073"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/service/notification/Indice_de_imagenes.md
id: [TE-0072, TE-0073]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/service/notification"
lang: es
source:
  - "02_Analisis_Software/class diagram/service/notification/Complete Service Notification Class Diagram.png"
  - "02_Analisis_Software/class diagram/service/notification/Simple Service Notification Class Diagram.png"
source_sha256:
  - 69279bf697a1cd1a6ecb2302ba88737c1c2cdf69fee8d91aa0225adc18f7603f
  - 6085583c06cfacc20911416ae556be3eca6f2fdf34b7565752d65de0e2c5bb10
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/service/notification

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/service/notification/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Service Notification Class Diagram.png | TE-0072 | PNG | 582 × 310 px |
| 2 | Simple Service Notification Class Diagram.png | TE-0073 | PNG | 153 × 68 px |

## 1. Complete Service Notification Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0072 |
| Original | `02_Analisis_Software/class diagram/service/notification/Complete Service Notification Class Diagram.png` |
| Formato y dimensiones | PNG · 582 × 310 px |
| Tamaño | 24235 bytes |
| SHA-256 | `69279bf697a1cd1a6ecb2302ba88737c1c2cdf69fee8d91aa0225adc18f7603f` |
| Transcripción | No transcrita |

![Complete Service Notification Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/notification/Complete%20Service%20Notification%20Class%20Diagram.png)

## 2. Simple Service Notification Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0073 |
| Original | `02_Analisis_Software/class diagram/service/notification/Simple Service Notification Class Diagram.png` |
| Formato y dimensiones | PNG · 153 × 68 px |
| Tamaño | 2762 bytes |
| SHA-256 | `6085583c06cfacc20911416ae556be3eca6f2fdf34b7565752d65de0e2c5bb10` |
| Transcripción | No transcrita |

![Simple Service Notification Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/notification/Simple%20Service%20Notification%20Class%20Diagram.png)

---

<a id="doc-te-0074-te-0075"></a>

<!--
ruta_anterior: es/02_Analisis_Software/class_diagram/service/oauth/Indice_de_imagenes.md
id: [TE-0074, TE-0075]
title: "Índice de imágenes — 02_Analisis_Software/class diagram/service/oauth"
lang: es
source:
  - "02_Analisis_Software/class diagram/service/oauth/Complete Service OAuth Class Diagram.png"
  - "02_Analisis_Software/class diagram/service/oauth/Simple Service OAuth Class Diagram.png"
source_sha256:
  - a2d3be7aa15320151cf3a89b155ae05acb975c10bcd2c743c30207bfe0936714
  - d3a73b91e3d9f7518377b2d1e947697863f030155627951a37aef0d35e1f499f
conversion: "Python 3.12.3 + Pillow 13.0.0 (índice por carpeta; imagen enlazada al original, sin transcripción)"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "El texto y los elementos dibujados en las imágenes no se transcriben; solo se muestra la imagen original y sus datos de archivo."
status: borrador
reviewed_by: ""
-->

> **Documento histórico.** Este diagrama de clases describe el backend del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025), cuyo código se organizaba en estos paquetes. El backend actual (`lozano2303/TuEvento`) es un monolito modular con módulos en `modules/<módulo>/{domain,application,infrastructure,interfaces}`; su estructura se describe en `10_Manual_Tecnico.md`. Se conserva sin cambios. Ver C13 en `Registro_de_Contradicciones.md`.

# Índice de imágenes — 02_Analisis_Software/class diagram/service/oauth

> Índice de las 2 imágenes de `02_Analisis_Software/class diagram/service/oauth/`. Cada imagen se muestra desde su archivo original (no se copia). **Las imágenes no están transcritas**: el texto y los elementos que contienen no se han pasado a Markdown.

| # | Imagen | id | Formato | Dimensiones |
|---|---|---|---|---|
| 1 | Complete Service OAuth Class Diagram.png | TE-0074 | PNG | 761 × 293 px |
| 2 | Simple Service OAuth Class Diagram.png | TE-0075 | PNG | 219 × 68 px |

## 1. Complete Service OAuth Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0074 |
| Original | `02_Analisis_Software/class diagram/service/oauth/Complete Service OAuth Class Diagram.png` |
| Formato y dimensiones | PNG · 761 × 293 px |
| Tamaño | 31055 bytes |
| SHA-256 | `a2d3be7aa15320151cf3a89b155ae05acb975c10bcd2c743c30207bfe0936714` |
| Transcripción | No transcrita |

![Complete Service OAuth Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/oauth/Complete%20Service%20OAuth%20Class%20Diagram.png)

## 2. Simple Service OAuth Class Diagram.png

| Campo | Valor |
|---|---|
| id | TE-0075 |
| Original | `02_Analisis_Software/class diagram/service/oauth/Simple Service OAuth Class Diagram.png` |
| Formato y dimensiones | PNG · 219 × 68 px |
| Tamaño | 3455 bytes |
| SHA-256 | `d3a73b91e3d9f7518377b2d1e947697863f030155627951a37aef0d35e1f499f` |
| Transcripción | No transcrita |

![Simple Service OAuth Class Diagram](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/originales/02_Analisis_Software/class%20diagram/service/oauth/Simple%20Service%20OAuth%20Class%20Diagram.png)

---

<a id="doc-te-0021"></a>

<!--
ruta_anterior: es/02_Analisis_Software/Diagramas_de_Estados_TuEvento.md
id: TE-0021
title: "Diagramas_de_Estados_TuEvento"
lang: es
source: 02_Analisis_Software/Diagramas_de_Estados_TuEvento.pdf
source_sha256: ab1c00535ef166e2f5f1fda99d02273ee41085c094d0f353c5e0f367685f7830
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: parcial
losses:
  - "Cabecera o pie de página omitido en las páginas 1–25: «{n}»."
  - "Página 4: figura incluida como imagen (pagina-04-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 6: figura incluida como imagen (pagina-06-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 8: figura incluida como imagen (pagina-08-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 10: figura incluida como imagen (pagina-10-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 12: figura incluida como imagen (pagina-12-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 14: figura incluida como imagen (pagina-14-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 15: figura incluida como imagen (pagina-15-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 17: figura incluida como imagen (pagina-17-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 18: figura incluida como imagen (pagina-18-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 20: figura incluida como imagen (pagina-20-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "Página 21: figura incluida como imagen (pagina-21-figura-1.png); su texto (0 palabras) va en un comentario HTML no visible, agrupado por caja ([...]) de arriba abajo y de izquierda a derecha; flechas y relaciones solo están en la imagen."
  - "PDF generado sin LaTeX: los guiones al final de línea son guiones reales y se conservan al unir las líneas (no son guiones de corte silábico)."
  - "Portada: líneas destacadas que no son el título se escriben como párrafos: «Proyecto TuEvento: análisis del software, vistas dinámicas»."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
-->

# Diagramas de estados del sistema

**Proyecto TuEvento: análisis del software, vistas dinámicas**

Jhampier Santos Ortiz y Cristofer David Lozano Contreras

Servicio Nacional de Aprendizaje (SENA)

Análisis y Desarrollo de Software (ADSO), Ficha 3145556

Instructora: Carlos Julio Cadena

Octubre de 2026

Versión 1.0

## Introducción y alcance

Este documento describe el comportamiento dinámico de las entidades principales de TuEvento mediante diagramas de estados del Lenguaje Unificado de Modelado (UML) (Object Management Group [OMG], 2017). Cada diagrama y cada tabla de transiciones se construyó a partir de la lectura del código fuente del backend, desarrollado con Spring Boot bajo arquitectura hexagonal (Cockburn, 2005), y del servicio auxiliar fake-payment-gateway. Solo se documentan las transiciones que existen en el código; no se agregaron transiciones lógicas que no estén implementadas.

Cada sección presenta el enum o clase de estado, el estado inicial y los finales, el diagrama de estados, la tabla de transiciones con su disparador, las condiciones de guarda (guards) y la clase y el método donde ocurre, y las observaciones sobre inconsistencias detectadas. Al final se documentan las relaciones entre entidades, los procesos automáticos, las diferencias con la documentación existente y los puntos abiertos.

En los diagramas, el círculo inicial indica el estado inicial y el círculo final indica que la entidad no tiene más transiciones; las notas señalan particularidades del código. Las etiquetas de cada flecha indican el evento que dispara la transición. Salvo donde se indique lo contrario, todos los valores de cada enum se usan en el código. Los hallazgos se contrastaron con el repositorio del proyecto en la rama develop y en las 12 ramas remotas existentes.

## Resumen por entidad

La Tabla 1 resume las diez máquinas de estados analizadas y el hallazgo principal de cada una.

**Tabla 1**<br>
*Resumen de las máquinas de estados del sistema*

| **Entidad** | **N.º de** **estados** | **Estado inicial** | **Estados finales** | **Hallazgo principal** |
|---|---|---|---|---|
| Evento | 6 | DRAFT | CANCELLED, COMPLETED | Flujo de revisión admin completo; las reglas viven en dos switch, sin allowedTransitions() |
| Orden | 6 | DRAFT | CANCELLED, REFUNDED, USED | USED nunca se asigna; la orden queda en PAID tras el check-in |
| Ticket | 6 | PENDING | REFUNDED, USED, CANCELLED | Sin guardas de transición: asigna el estado directamente |
| Asiento | 4 | AVAILABLE | SOLD | COURTESY sin uso; updateSeatStatus() acepta cualquier combinación |
| Pago (backend) | 5 | PENDING o APPROVED | REJECTED, ERROR, REFUNDED | Dos estados iniciales; CANCELLED del gateway se mapea a ERROR |
| Pago (gateway) | 7 | PENDING | REFUNDED, DECLINED, FAILED, CANCELLED | PROCESSING es transitorio; el backend ignora su webhook |
| Billetera (transacción) | 3 | PENDING o COMPLETED | COMPLETED, FAILED | Dos estados iniciales según el tipo de transacción |
| Notificación (entrega) | 4 | PENDING, DELIVERED o FAILED | DELIVERED, SENT, FAILED | SENT → DELIVERED no se implementa; leer no cambia el estado de entrega |
| Cuenta de usuario | 5 | PENDING | (ninguno real) | DELETED no se asigna en ningún caso de uso; reactivación sin documentar |
| Reembolso | 4 | REQUESTED | PROCESSED, FAILED | Solo REQUESTED → PROCESSED está implementada |

## Diagramas de estados por entidad

## 1. Evento

El estado del evento se define en el enum EventStatus (modules/event/domain/model/EventStatus.java) y toma los valores DRAFT, PENDING_REVIEW, PUBLISHED, REJECTED, CANCELLED y COMPLETED. El estado inicial es DRAFT, asignado en CreateEventService.execute(). CANCELLED y COMPLETED son terminales de hecho, porque ningún caso de uso permite salir de ellos. La Figura 1 muestra el ciclo de vida completo, incluido el flujo de revisión del administrador; la flecha PUBLISHED → COMPLETED agrupa la finalización manual y la automática.

**Figura 1**<br>
*Diagrama de estados de la entidad Evento*

![Figura de la página 4](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-04-figura-1.png)

La Tabla 2 detalla cada transición con sus guardas y la clase responsable.

**Tabla 2**<br>
*Transiciones de la entidad Evento*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | DRAFT | Organizador crea el evento | Fechas válidas (no pasadas, no muy lejanas, fin ≥ inicio); el sitio existe; availableSeats ≤ capacidad del sitio; nombre + fecha + sitio únicos; categoría activa | CreateEventService.execute() |
| DRAFT | PENDING_REVIE W | Organizador envía a revisión | startDate ≥ hoy; entre 3 y 9 imágenes; al menos 1 sección configurada; el userId del JWT es el dueño | ChangeEventStatusService.execute() |
| PENDING_REVIE W | DRAFT | Organizador retira la solicitud | El userId del JWT es el dueño; se limpia rejectionReason | ChangeEventStatusService.execute() |
| REJECTED | DRAFT | Organizador corrige y retira para editar | El userId del JWT es el dueño; se limpia rejectionReason | ChangeEventStatusService.execute() |
| PENDING_REVIE W | PUBLISHED | Admin aprueba | startDate ≥ hoy; entre 3 y 9 imágenes; al menos 1 sección; sin verificación de ownership | AdminChangeEventStatusUseCase.exe cute() |
| PENDING_REVIE W | REJECTED | Admin rechaza | reason no nulo ni vacío; se persiste en event.rejectionReason | AdminChangeEventStatusUseCase.exe cute() |
| PUBLISHED | CANCELLED | Cancelación manual | Sin validaciones adicionales | ChangeEventStatusService.execute() (organizador); AdminChangeEventStat usUseCase.execute() (admin) |
| PUBLISHED | COMPLETED | Finalización manual | LocalDate.now().isAfter(event.getFinish Date()): solo después del día de fin | ChangeEventStatusService.execute(); AdminChangeEventStatusUseCase.exe cute() |
| PUBLISHED | COMPLETED | Finalización automática | finishDate &lt; hoy; cron 0 5 0 \* \* \* y ejecución de recuperación en ApplicationReadyEvent; changedBy = null | EventAutoCompletionScheduler.compl eteExpiredEvents() |

Se verificó que no existe la transición DRAFT → CANCELLED (un borrador no se puede cancelar directamente) ni REJECTED → PENDING_REVIEW (el organizador debe pasar primero por DRAFT). Además, el enum no define allowedTransitions(): las reglas están en el método validateTransition() de los dos casos de uso, por lo que agregar un tercero sin actualizar ambos dejaría la máquina de estados inconsistente.

## 2. Orden

La orden usa el enum OrderStatus (modules/ticket/domain/model/OrderStatus.java), que sí implementa allowedTransitions(), canTransitionTo() e isTerminal(). Sus valores son DRAFT, PAYMENT_PENDING, PAID, CANCELLED, REFUNDED y USED. El estado inicial es DRAFT, asignado en CreateOrderWithTicketsUseCaseImpl.execute(), y los finales declarados son CANCELLED, REFUNDED y USED. La Figura 2 incluye la transición PAID → USED porque el enum la declara, aunque ningún caso de uso la ejecuta.

**Figura 2**<br>
*Diagrama de estados de la entidad Orden*

![Figura de la página 6](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-06-figura-1.png)

**Tabla 3**<br>
*Transiciones de la entidad Orden*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | DRAFT | Usuario crea la orden en el checkout | Todas las sillas en RESERVED por ese userId; reservas no expiradas (reservedUntil &gt; now) | CreateOrderWithTicketsUseCaseImpl.e xecute() |
| DRAFT | PAYMENT_PENDI NG | Usuario inicia el flujo de pago | canTransitionTo(); invocado por el orquestador de pagos | InitiateOrderPaymentUseCaseImpl.initi ateOrderPayment() → order.markAsPaymentPending() |
| DRAFT | CANCELLED | Usuario cancela la orden | canTransitionTo(); las sillas se liberan a AVAILABLE | CancelOrderUseCaseImpl.execute() → order.cancel() |
| PAYMENT_PENDI NG | PAID | Pago aprobado (webhook APPROVED) | canTransitionTo() | ConfirmOrderPaymentUseCaseImpl.con firmOrderPayment() → order.markAsPaid() |
| PAYMENT_PENDI NG | CANCELLED | Pago fallido, rechazado o cancelado (webhooks DECLINED, FAILED, CANCELLED) | canTransitionTo(); las sillas se liberan a AVAILABLE | FailOrderPaymentUseCaseImpl.failOrde rPayment() → order.cancel() |
| PAID | REFUNDED | Reembolso confirmado (webhook REFUNDED o pago solo con wallet) | canTransitionTo() | ConfirmRefundUseCaseImpl.confirmRef und() → order.refund() |

El estado USED nunca se usa: el método Order.markAsUsed() existe (Order.java, línea 70), pero no tiene ninguna llamada en el proyecto. El check-in solo cambia el Ticket a USED (CheckinTicketUseCaseImpl, línea 55); la orden permanece en PAID indefinidamente. Por otra parte, el webhook CANCELLED del gateway marca el Payment como ERROR (markAsError()), pero cancela la orden mediante failOrderPayment() → order.cancel().

## 3. Ticket

El ticket usa el enum simple TicketStatus (modules/ticket/domain/model/TicketStatus.java), sin allowedTransitions(). Sus valores son PENDING, PROCESSING, PAID, REFUNDED, USED y CANCELLED. El estado inicial es PENDING, asignado en CreateOrderWithTicketsUseCaseImpl.execute(); los finales son REFUNDED, USED y CANCELLED, porque ningún caso de uso los lee como origen. La Figura 3 muestra su ciclo de vida.

**Figura 3**<br>
*Diagrama de estados de la entidad Ticket*

![Figura de la página 8](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-08-figura-1.png)

**Tabla 4**<br>
*Transiciones de la entidad Ticket*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | PENDING | Se crea la orden con tickets | Sillas RESERVED por el mismo usuario y reservas vigentes | CreateOrderWithTicketsUseCaseImpl.e xecute() |
| PENDING | PROCESSING | Inicio del flujo de pago | Sin guardia de estado origen (se asigna vía builder) | InitiateOrderPaymentUseCaseImpl.initi ateOrderPayment() |
| PROCESSING | PAID | Pago aprobado (webhook APPROVED) | Sin guardia; ticket.markAsPaid() asigna directo | ConfirmOrderPaymentUseCaseImpl.con firmOrderPayment() → ticket.markAsPaid() |
| PROCESSING | CANCELLED | Pago fallido, rechazado o cancelado | Sin guardia; ticket.cancel() asigna directo | FailOrderPaymentUseCaseImpl.failOrde rPayment() → ticket.cancel() |
| PAID | USED | Check-in completado | ticket.canCheckin(): status == PAID, expirationDate != null y now &lt; expirationDate; no debe existir un check-in previo en TicketCheckinRepository | CheckinTicketUseCaseImpl.execute() → ticket.markAsUsed() |
| PAID | REFUNDED | Reembolso confirmado (webhook REFUNDED o pago solo con wallet) | Sin guardia de estado; ticket.markAsRefunded() asigna directo | ConfirmRefundUseCaseImpl.confirmRef und() → ticket.markAsRefunded() |
| PENDING | CANCELLED | El usuario cancela la orden directamente | Sin guardia; ticket.cancel() asigna directo; las sillas se liberan | CancelOrderUseCaseImpl.execute() → ticket.cancel() |

A diferencia de la orden y el pago, el ticket no tiene guardas de transición: los métodos markAsPaid(), markAsUsed(), markAsRefunded() y cancel() asignan this.status directamente sin verificar el estado anterior (Ticket.java), por lo que una llamada accidental podría producir CANCELLED → PAID sin que el código la rechace. Asimismo, PENDING → CANCELLED ocurre en CancelOrderUseCaseImpl aunque el flujo natural sería PROCESSING → CANCELLED.

## 4. Asiento

El asiento usa el enum simple SeatStatus (modules/seat/domain/model/SeatStatus.java), sin allowedTransitions(), con los valores AVAILABLE, RESERVED, SOLD y COURTESY. El estado inicial es AVAILABLE, asignado en SeatService.createSeat(). SOLD es el único estado final de hecho: las sillas vendidas no se liberan tras un reembolso, decisión de negocio documentada en ConfirmRefundUseCaseImpl; sin embargo, un administrador sí puede cambiar manualmente cualquier estado mediante SeatService.updateSeatStatus(). La Figura 4 dibuja cada causa de liberación por separado y no incluye el cambio manual del administrador.

**Figura 4**<br>
*Diagrama de estados de la entidad Asiento*

![Figura de la página 10](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-10-figura-1.png)

**Tabla 5**<br>
*Transiciones de la entidad Asiento*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | AVAILABLE | Creación de la silla en la sección | Sin validación extra; implícito | SeatService.createSeat() |
| AVAILABLE | RESERVED | Usuario reserva la silla (TTL de 10 min) | seat.status == AVAILABLE; reservedUntil = now + 10 min; lanza SEAT_NOT_AVAILABLE si no está AVAILABLE | SeatReservationService.reserveSeat() |
| RESERVED | AVAILABLE | Usuario libera manualmente la reserva | seat.status == RESERVED; el userId debe coincidir con reservedBy, salvo que isAdmin = true | SeatReservationService.releaseSeat() |
| RESERVED | AVAILABLE | Expiración automática del TTL (cada 10 s) | seat.status == RESERVED y reservedUntil &lt; now; cron \*/10 \* \* \* \* \* y ejecución en ApplicationReadyEvent | SeatReservationExpirationScheduler.re leaseExpiredReservations() |
| RESERVED | SOLD | Pago aprobado (webhook APPROVED) | Sin guardia de estado origen; solo valida que el nuevo estado sea distinto al actual (SEAT_STATUS_UNCHANGED) | ConfirmOrderPaymentUseCaseImpl.con firmOrderPayment() → seatUseCase.updateSeatStatus(seatId, SOLD, null) |
| RESERVED | AVAILABLE | Pago fallido, rechazado o cancelado | Sin guardia de estado origen; solo valida que el nuevo estado sea distinto | FailOrderPaymentUseCaseImpl.failOrde rPayment() → seatUseCase.updateSeatStatus(seatId, AVAILABLE, reason) |
| RESERVED | AVAILABLE | Cancelación de la orden por el usuario | Sin guardia de estado origen | CancelOrderUseCaseImpl.execute() → seatUseCase.updateSeatStatus(seatId, AVAILABLE, "Order cancelled") |
| Cualquiera | Cualquiera | Cambio manual por un admin | Solo valida que el nuevo estado sea distinto al actual | SeatService.updateSeatStatus() (SeatController) |

COURTESY está en el enum SeatStatus, pero ningún caso de uso lo asigna ni lo verifica (un administrador podría enviarlo por REST). No debe confundirse con SeatType.COURTESY, un enum distinto (REGULAR, COURTESY) que clasifica el tipo de silla y no su estado. Por otra parte, updateSeatStatus() no valida el estado origen y permite, por ejemplo, SOLD → AVAILABLE si un administrador lo solicita; y el scheduler libera reservas cada 10 s sin generar SeatLog, mientras que SeatService.updateSeatStatus() sí lo genera.

## 5. Pago (backend)

El pago del backend usa el enum PaymentStatus<br>
(modules/payment/domain/model/PaymentStatus.java), que implementa<br>
allowedTransitions(), canTransitionTo() e isTerminal(). Sus valores son PENDING,<br>
APPROVED, REJECTED, ERROR y REFUNDED. Tiene dos estados iniciales: PENDING,<br>
asignado en ChargeOrderViaGatewayUseCase.execute() (línea 77) para pagos con pasarela,<br>
totales o mixtos; y APPROVED, asignado en CreateWalletOnlyPaymentUseCase.execute()<br>
(línea 60) para pagos pagados íntegramente con wallet, sin pasar por PENDING. REJECTED,<br>
ERROR y REFUNDED están declarados como terminales. La Figura 5 muestra ambos<br>
orígenes.<br>
**Figura 5**<br>
*Diagrama de estados de la entidad Pago (backend)*

![Figura de la página 12](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-12-figura-1.png)

**Tabla 6**<br>
*Transiciones de la entidad Pago (backend)*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | PENDING | Creación de pago con pasarela (total o mixto) | Orden en PAYMENT_PENDING; invocado por CreatePaymentUseCaseImpl | ChargeOrderViaGatewayUseCase.exec ute() |
| (nuevo) | APPROVED | Creación de pago 100 % wallet | Saldo disponible ≥ total de la orden; sin pasarela | CreateWalletOnlyPaymentUseCase.exe cute() |
| PENDING | APPROVED | Webhook APPROVED del gateway | Firma HMAC válida; idempotencia por gatewayEventId; canTransitionTo(APPROVED) | ProcessWebhookUseCaseImpl.execute( ) → payment.approve() |
| PENDING | REJECTED | Webhook DECLINED | Mismas verificaciones de firma e idempotencia; canTransitionTo(REJECTED) | ProcessWebhookUseCaseImpl.execute( ) → payment.reject() |
| PENDING | ERROR | Webhook FAILED | Mismas verificaciones; canTransitionTo(ERROR) | ProcessWebhookUseCaseImpl.execute( ) → payment.markAsError() |
| PENDING | ERROR | Webhook CANCELLED | Mismas verificaciones; el CANCELLED del gateway se mapea a ERROR (mismo markAsError()) | ProcessWebhookUseCaseImpl.execute( ) → payment.markAsError() |
| APPROVED | REFUNDED | Webhook REFUNDED | Firma HMAC válida; canTransitionTo(REFUNDED) | ProcessWebhookUseCaseImpl.execute( ) → payment.refund() |
| APPROVED | REFUNDED | Reembolso de un pago solo con wallet | payment.status == APPROVED (si no, lanza IllegalStateException); canTransitionTo(REFUNDED) | RequestRefundUseCaseImpl.closeRefu ndLocally() → payment.refund() |

El webhook CANCELLED de la pasarela lleva el Payment a ERROR, porque este enum no tiene el valor CANCELLED. Es intencional, pero puede confundir en el registro de auditoría. Además, CreateWalletOnlyPaymentUseCase registra en PaymentLog un cambio PENDING → APPROVED que nunca existió en la base de datos; es un registro conceptual para trazabilidad.

## 6. Pago (pasarela sandbox, fake-payment-gateway)

El servicio auxiliar fake-payment-gateway simula la pasarela de pagos. Su enum PaymentStatus (fake-payment-gateway/.../domain/model/PaymentStatus.java) implementa allowedTransitions(), canTransitionTo() e isTerminal(), y define PENDING, PROCESSING, APPROVED, REFUNDED, DECLINED, FAILED y CANCELLED. El estado inicial es PENDING, asignado en CreatePaymentUseCase.execute() → Payment.create(); los terminales son REFUNDED, DECLINED, FAILED y CANCELLED. Tras cada transición, WebhookNotifierPort.notify(PaymentStatusChanged) envía un evento HTTP al backend. La Figura 6 muestra el flujo.

**Figura 6**<br>
*Diagrama de estados del Pago en la pasarela sandbox*

![Figura de la página 14](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-14-figura-1.png)

**Tabla 7**<br>
*Transiciones del Pago en la pasarela sandbox*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | PENDING | Solicitud de creación desde el backend | Sin validaciones adicionales | CreatePaymentUseCase.execute() → Payment.create() |
| PENDING | PROCESSING | Paso intermedio interno al aprobar, declinar o fallar | payment.canTransitionTo(PROCESSING) | ApprovePaymentUseCase, DeclinePaymentUseCase, FailPaymentUseCase → payment.markAsProcessing() |
| PENDING | CANCELLED | El admin cancela antes de procesar | payment.canTransitionTo(CANCELLED) | CancelPaymentUseCase.execute() → payment.cancel() |
| PROCESSING | APPROVED | El admin aprueba | payment.canTransitionTo(APPROVED) | ApprovePaymentUseCase.execute() → payment.approve() |
| PROCESSING | DECLINED | El admin rechaza | payment.canTransitionTo(DECLINED) | DeclinePaymentUseCase.execute() → payment.decline() |
| PROCESSING | FAILED | El admin marca error técnico | payment.canTransitionTo(FAILED) | FailPaymentUseCase.execute() → payment.fail() |
| APPROVED | REFUNDED | El backend solicita el reembolso | payment.canTransitionTo(REFUNDED) | RefundPaymentUseCase.execute() → payment.refund() |

PROCESSING es un estado transitorio interno: el gateway pasa de PENDING a PROCESSING y luego a APPROVED, DECLINED o FAILED dentro de la misma llamada a execute(), enviando dos webhooks. ProcessWebhookUseCaseImpl (backend) no tiene un case para PROCESSING; ese valor cae en el default y solo registra un log.warn. Se verificó que esto es inofensivo: el gateway siempre envía PROCESSING antes que APPROVED, y si llegara un evento repetido la idempotencia por gatewayEventId lo descartaría.

## 7. Billetera (transacción de wallet)

Este diagrama representa el estado de cada transacción de la billetera<br>
(WalletTransaction), no el saldo de la billetera. Usa el enum simple WalletTransactionStatus<br>
(modules/wallet/domain/model/WalletTransactionStatus.java), con los valores PENDING,<br>
COMPLETED y FAILED. El estado inicial depende del tipo de transacción<br>
(WalletTransactionType): PENDING para PAYMENT, asignado en<br>
ReserveWalletPaymentUseCaseImpl.execute(), y COMPLETED para CREDIT, REVERSAL y<br>
ADJUSTMENT, que se crean directamente. COMPLETED y FAILED son terminales. La<br>
Figura 7 muestra los dos orígenes.<br>
**Figura 7**<br>
*Diagrama de estados de la transacción de wallet*

![Figura de la página 15](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-15-figura-1.png)

**Tabla 8**<br>
*Transiciones de la transacción de wallet*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | PENDING | Reserva de saldo para un pago (checkout) | saldoDisponible = balance − sumPendingPayments ≥ amount; bloqueo optimista @Version en Wallet | ReserveWalletPaymentUseCaseImpl.ex ecute() |
| (nuevo) | COMPLETED | Crédito directo (cancelación de evento, ajuste admin) | Idempotencia por idempotencyKey; sin otra validación de estado origen | CreditWalletUseCaseImpl.execute() |
| (nuevo) | COMPLETED | Reversión de un PAYMENT (reembolso con wallet) | Idempotencia por reversal-{originalTransactionId}; la transacción original debe estar en COMPLETED | ReverseWalletPaymentUseCaseImpl.ex ecute() |
| (nuevo) | COMPLETED | Ajuste manual de balance (admin) | amount != 0; reason no vacío; balance resultante ≥ 0 | AdjustWalletUseCaseImpl.execute() |
| PENDING | COMPLETED | Pago aprobado (webhook APPROVED o pago solo con wallet) | Idempotencia: si ya está COMPLETED no hace nada; descuenta el balance del Wallet | ConfirmWalletPaymentUseCaseImpl.ex ecute() → tx.complete(newBalance) |
| PENDING | FAILED | Pago fallido, rechazado o cancelado (webhooks DECLINED, FAILED, CANCELLED) | Idempotencia: si ya está FAILED no hace nada; no toca el balance | ReleaseWalletPaymentUseCaseImpl.ex ecute() → tx.fail() |

Las transacciones CREDIT, REVERSAL y ADJUSTMENT nacen directamente en COMPLETED sin pasar por PENDING; por eso el diagrama tiene dos estados iniciales. COMPLETED y FAILED son terminales: no existe ninguna transición de salida.

## 8. Notificación (entrega a cada destinatario)

El enum DeliveredStatus (modules/notification/domain/model/DeliveredStatus.java) aplica a la entidad NotificationUser, que relaciona una Notification con cada destinatario; la Notification principal no tiene estado propio. Sus valores son PENDING, SENT, DELIVERED y FAILED. El estado inicial depende del canal: IN_APP nace en DELIVERED, EMAIL nace en PENDING, y si hay un error antes de crear el registro (correo nulo o excepción al guardar) nace directamente en FAILED. DELIVERED, SENT y FAILED son finales, porque ningún caso de uso los lee como origen. La Figura 8 muestra los tres orígenes posibles.

**Figura 8**<br>
*Diagrama de estados de la entrega de notificaciones*

![Figura de la página 17](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-17-figura-1.png)

**Tabla 9**<br>
*Transiciones de la entrega de notificaciones*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | DELIVERED | Canal IN_APP: guardar en BD equivale a entregar | Intento adicional de push por WebSocket (si falla, no bloquea ni cambia el estado) | InAppChannelAdapter.deliver() → deliveredStatus(DELIVERED) |
| (nuevo) | FAILED | Canal IN_APP: excepción al guardar en BD | Bloque catch externo del for; intenta guardar con FAILED | InAppChannelAdapter.deliver() → catch |
| (nuevo) | FAILED | Canal EMAIL: usuario sin correo o correo en blanco | loginCredentialsRepository.findByUserId( userId) vacío o correo vacío | EmailChannelAdapter.deliver() (guarda FAILED sin crear PENDING previo) |
| (nuevo) | PENDING | Canal EMAIL: registro creado antes de enviar | Correo del usuario encontrado y no vacío | EmailChannelAdapter.deliver() → deliveredStatus(PENDING) |
| PENDING | SENT | Correo enviado por SMTP | mailSender.send() sin excepción | EmailChannelAdapter.deliver() (actualiza a SENT) |
| PENDING | FAILED | Error de SMTP o MessagingException | Bloque catch; actualiza el registro PENDING existente | EmailChannelAdapter.deliver() → catch (actualiza a FAILED) |

No existe la transición SENT → DELIVERED (el enum la sugiere implícitamente, pero ningún código la implementa) ni PENDING → DELIVERED directa. DELIVERED (IN_APP) y SENT (EMAIL) son ambos terminales en la práctica, aunque semánticamente distintos. Además, NotificationUser tiene el campo readAt y un endpoint para marcar como leída, pero esa acción solo actualiza readAt (InAppNotificationQueryUseCase) y no cambia deliveredStatus: leer y entregar son conceptos independientes en este código.

## 9. Cuenta de usuario

El estado de la cuenta no es un enum Java: UserStatus<br>
(modules/security/domain/model/UserStatus.java) es una entidad de la tabla user_status,<br>
manipulada como String con el campo code, cuyos valores se insertan en la migración<br>
Liquibase 017. Los valores son PENDING, ACTIVE, BLOCKED, INACTIVE y DELETED. El<br>
estado inicial es PENDING, asignado en RegisterUserUseCase.register()<br>
(DEFAULT_STATUS_CODE). En teoría DELETED sería el final, pero ningún caso de uso lo<br>
asigna, así que ningún estado es realmente terminal. La Figura 9 muestra el ciclo de vida.<br>
**Figura 9**<br>
*Diagrama de estados de la Cuenta de usuario*

![Figura de la página 18](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-18-figura-1.png)

**Tabla 10**<br>
*Transiciones de la Cuenta de usuario*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | PENDING | Registro de nuevo usuario | Email único y no registrado; @gmail.com obligatorio; contraseña fuerte; fullName válido | RegisterUserUseCase.register() |
| PENDING | ACTIVE | Verifica su correo con el código de activación | Código correcto, no expirado (expiresAt &gt; now), no usado y validado contra el userId | ActivateAccountUseCase.activate() → user.setUserStatus(ACTIVE) |
| ACTIVE | BLOCKED | 5 intentos fallidos de login en una ventana de 15 min | failedAttempts ≥ MAX_FAILED_ATTEMPTS (5); lockedUntil = now + LOCKOUT_MINUTES; transacción independiente (REQUIRES_NEW) | AccountLockoutService.saveLockout() → blockUser() |
| BLOCKED | ACTIVE | Expiración del bloqueo por tiempo (cron cada minuto) | lockedUntil &lt; now; transacción independiente (REQUIRES_NEW) | SecurityDataCleanupTask.unlockExpire dLockouts() → deleteLockout() → unblockUser() |
| BLOCKED | ACTIVE | Login después de que el bloqueo expiró | lockedUntil &lt; now detectado durante el login; se elimina el registro AccountLockout | LoginUseCase.checkLockout() → deleteLockout() → unblockUser() |
| ACTIVE | INACTIVE | El propio usuario desactiva su cuenta | Contraseña verificada; cuenta no INACTIVE ni DELETED; se revocan sesiones y refresh tokens activos | DeactivateAccountUseCase.deactivate( ) → user.setUserStatus(INACTIVE) |
| INACTIVE | ACTIVE | Confirma la reactivación con el token recibido por correo | Token válido, no usado y no expirado (30 min); el token se invalida tras su uso | ConfirmReactivationUseCase.confirm() → user.setUserStatus(ACTIVE) |

Se verificó con git grep en las 12 ramas remotas que DELETED solo aparece como guarda en LoginUseCase y DeactivateAccountUseCase; ningún caso de uso lo asigna, por lo que la eliminación de cuenta del RF7 del SRS no está implementada. El flujo de reactivación (RequestReactivationUseCase y ConfirmReactivationUseCase) no está documentado en el glosario ni en el manual técnico anterior. El bloqueo usa además la entidad AccountLockout (contador de intentos y expiración); el estado vuelve a ACTIVE al eliminar ese registro, como efecto de deleteLockout() y no como una transición declarada en un enum. No existe BLOCKED → INACTIVE ni BLOCKED → DELETED: el usuario bloqueado se desbloquea solo y vuelve a ACTIVE.

## 10. Reembolso

El reembolso usa el enum RefundStatus<br>
(modules/payment/domain/model/RefundStatus.java), con los valores REQUESTED,<br>
APPROVED, PROCESSED y FAILED. El propio enum indica que es una estructura lista para<br>
una fase futura. El estado inicial es REQUESTED, asignado en RequestRefundUseCaseImpl<br>
(línea 70), y los finales son PROCESSED y FAILED. La entidad Refund es un objeto<br>
inmutable construido con builder y no tiene métodos de transición: cada cambio de estado<br>
crea un objeto nuevo. La Figura 10 muestra que solo la transición REQUESTED →<br>
PROCESSED está implementada.<br>
**Figura 10**<br>
*Diagrama de estados de la entidad Reembolso*

![Figura de la página 20](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-20-figura-1.png)

**Tabla 11**<br>
*Transiciones de la entidad Reembolso*

| **Origen** | **Destino** | **Disparador** | **Condiciones (guards)** | **Clase y método** |
|---|---|---|---|---|
| (nuevo) | REQUESTED | Se solicita el reembolso (usuario o cancelación del evento) | Sin método de transición en la entidad | RequestRefundUseCaseImpl (línea 70) |
| REQUESTED | PROCESSED | Webhook REFUNDED del gateway | Se construye un nuevo Refund con status PROCESSED y processedAt = now() | ProcessWebhookUseCaseImpl (línea 190) |
| REQUESTED | PROCESSED | Cierre local del reembolso (pago solo con wallet) | Se asume que corresponde a closeRefundLocally(); pendiente de confirmar | RequestRefundUseCaseImpl (línea 113) |

RefundStatus.APPROVED y RefundStatus.FAILED no tienen ningún uso en el proyecto: están declarados en el enum, pero ningún caso de uso los asigna.

### Relaciones entre entidades (causa y efecto)

Los cambios de estado de una entidad disparan cambios en otras. La Figura 11 resume las cadenas principales y la Tabla 12 indica la clase donde ocurre cada una.

**Figura 11**<br>
*Cadenas de causa y efecto entre las entidades del sistema*

![Figura de la página 21](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/Diagramas_de_Estados_TuEvento/pagina-21-figura-1.png)

**Tabla 12**<br>
*Detalle de las relaciones de causa y efecto*

| **Causa** | **Efecto** | **Clase donde ocurre** |
|---|---|---|
| Pago (backend): PENDING → APPROVED (webhook APPROVED) | Orden: PAYMENT_PENDING → PAID; Tickets: PROCESSING → PAID; Sillas: RESERVED → SOLD | ProcessWebhookUseCaseImpl.execute() → ConfirmO rderPaymentUseCaseImpl.confirmOrderPayment() + SeatUseCase.updateSeatStatus() |
| Pago (backend): APPROVED creado directamente (solo wallet) | WalletTx: PENDING → COMPLETED; Orden: PAYMENT_PENDING → PAID; Tickets: PROCESSING → PAID; Sillas: RESERVED → SOLD | CreateWalletOnlyPaymentUseCase.execute() → ConfirmWalletPaymentUseCaseImpl + ConfirmOrderPaymentUseCaseImpl |
| Pago (backend): PENDING → REJECTED / ERROR (webhooks DECLINED, FAILED, CANCELLED) | WalletTx (si existía): PENDING → FAILED; Orden: PAYMENT_PENDING → CANCELLED; Tickets: PROCESSING → CANCELLED; Sillas: RESERVED → AVAILABLE | ProcessWebhookUseCaseImpl.execute() → ReleaseWalletPaymentUseCaseImpl + FailOrderPaymentUseCaseImpl.failOrderPayment() + SeatUseCase.updateSeatStatus() |
| Pago (backend): APPROVED → REFUNDED (webhook o pago solo con wallet) | Refund: REQUESTED → PROCESSED; Orden: PAID → REFUNDED; Tickets: PAID → REFUNDED; WalletTx: nueva transacción REVERSAL en COMPLETED (si había crédito wallet) | ProcessWebhookUseCaseImpl / RequestRefundUseCaseImpl.closeRefundLocally() → ConfirmRefundUseCaseImpl.confirmRefund() + ReverseWalletPaymentUseCaseImpl.execute() |
| Evento: PUBLISHED → CANCELLED | Reembolsos automáticos para todos los pagos APPROVED del evento (cada uno dispara la cadena de reembolso) | EventCancelledListener.handleEventStatusChanged( ) → RequestRefundUseCaseImpl.execute() por cada pago / for each payment |
| Evento: PENDING_REVIEW → PUBLISHED | Notificación al organizador (IN_APP y EMAIL): evento aprobado | EventStatusChangedListener (notification) → SendNotificationUseCase.execute() |
| Evento: PENDING_REVIEW → REJECTED | Notificación al organizador con el motivo del rechazo | EventStatusChangedListener → SendNotificationUseCase.execute() |
| Ticket: PAID → USED (check-in) | Se crea el registro TicketCheckin, sin cambio de estado en Orden ni Asiento | CheckinTicketUseCaseImpl.execute() |
| Usuario: ACTIVE → BLOCKED | Todos los intentos de login quedan bloqueados hasta que expire lockedUntil | AccountLockoutService.saveLockout() → blockUser() |
| Pago aprobado (cualquier tipo) | Se publica PaymentApprovedEvent para notificaciones | ProcessWebhookUseCaseImpl / CreateWalletOnlyPaymentUseCase → eventPublisher.publishEvent() |
| Wallet acreditada | Se publica WalletCreditedEvent para notificaciones | CreditWalletUseCaseImpl.execute() → eventPublisher.publishEvent() |

### Procesos automáticos

La Tabla 13 lista los schedulers, listeners y webhooks que cambian estados sin intervención directa del usuario.

**Tabla 13**<br>
*Procesos automáticos que cambian estados*

| **Proceso** | **Frecuencia o disparo** | **Entidad afectada** | **Transición** |
|---|---|---|---|
| EventAutoCompletionScheduler.comp leteExpiredEvents() | Diaria a las 00:05 (0 5 0 \* \* \*) y una vez al arrancar (ApplicationReadyEvent) | Evento | PUBLISHED → COMPLETED para todos los eventos con finishDate &lt; hoy |
| SeatReservationExpirationScheduler.r eleaseExpiredReservations() | Cada 10 segundos (\*/10 \* \* \* \* \*) y una vez al arrancar | Asiento | RESERVED → AVAILABLE para todos con reservedUntil &lt; now |
| SecurityDataCleanupTask.unlockExpir edLockouts() | Cada minuto (0 \* \* \* \* \*) | Usuario | BLOCKED → ACTIVE (limpia los AccountLockout expirados) |
| SecurityDataCleanupTask.cleanExpire dData() | Diaria a las 02:00 (0 0 2 \* \* \*) | Sesiones y tokens (no son estados de negocio) | Elimina AuthSession y RefreshToken expirados o revocados; AccountActivation y RecoverPassword expirados o usados |
| ProcessWebhookUseCaseImpl.execut e() | Invocado por el webhook HTTP del gateway | Pago, Orden, Tickets, Sillas, Wallet y Reembolso | Ver Tabla 12 |
| EventCancelledListener.handleEventS tatusChanged() | @EventListener sobre EventStatusChangedEvent (asíncrono, @Async) | Pago, Orden, Tickets, Wallet y Reembolso | Reembolso automático en cascada |

### Diferencias con la documentación existente

La Tabla 14 recoge las discrepancias entre lo que dicen los documentos del proyecto (Equipo CapySoft, s. f.-a, s. f.-b) y lo que hace el código (Equipo CapySoft, 2026).

**Tabla 14**<br>
*Discrepancias entre la documentación existente y el código*

| **Discrepancia** | **Qué dice la documentación** | **Qué dice el código** |
|---|---|---|
| Rol ATTENDEE en glosario y diagramas de clase | ATTENDEE como nombre del rol de usuario estándar | El código usa USER como DEFAULT_ROLE_CODE en RegisterUserUseCase y en los controladores |
| Estado IN_PROGRESS en el glosario del SRS | Lista IN_PROGRESS como posible estado de evento | EventStatus.java no contiene IN_PROGRESS; los 6 estados son DRAFT, PENDING_REVIEW, PUBLISHED, REJECTED, CANCELLED y COMPLETED |
| Manual técnico v1: 4 estados de evento | Documentaba DRAFT, PUBLISHED, CANCELLED y COMPLETED | El código tiene 6 estados desde que se añadió el flujo de revisión admin (changelog 079-add-event-review-flow.yaml) |
| Flujo de reactivación de cuenta | No documentado en el manual técnico ni en las historias de usuario del SRS | RequestReactivationUseCase y ConfirmReactivationUseCase están implementados y funcionales |
| Tiempo de bloqueo de cuenta | Manual técnico y manual de administrador documentan 30 minutos | LoginUseCase tiene LOCKOUT_MINUTES = 1 (static final, con un TODO para restaurar 30 en producción). Ningún archivo .java, .yml ni .properties define o lee app.security.lockout-minutes; el perfil application-k8s.yml no incluye esa propiedad |
| Transición Orden PAID → USED | El enum la declara como transición válida | Ningún caso de uso llama a order.markAsUsed(); la orden permanece en PAID después del check-in |
| Reembolso | El enum RefundStatus define 4 estados | Solo REQUESTED y PROCESSED se asignan; APPROVED y FAILED no tienen uso |

### Verificación y puntos abiertos

Los hallazgos de este documento se contrastaron con el repositorio mediante búsquedas en el código (grep y git grep) en la rama develop y en las 12 ramas remotas. A continuación se indica qué quedó verificado y qué sigue siendo una suposición.

### Verificado

\1. LOCKOUT_MINUTES vale 1 de forma fija en LoginUseCase y no se lee desde la configuración: no existe ningún @Value ni propiedad lockout-minutes en el proyecto. 2. El estado DELETED de la cuenta no es asignado por ningún caso de uso en ninguna de las 12 ramas remotas; solo aparece como guarda en LoginUseCase y DeactivateAccountUseCase.

\3. Un webhook PROCESSING de la pasarela sandbox se ignora con un log.warn y no provoca una<br>
transición inválida; además, la idempotencia por gatewayEventId descarta eventos<br>
repetidos.<br>
4. Order.markAsUsed() no tiene ninguna llamada en el proyecto.

\5. Los dos estados iniciales de Payment (PENDING y APPROVED) corresponden a las dos rutas de creación del pago.

### Suposiciones pendientes

\1. SeatService.updateSeatStatus() no valida el estado origen. Se asume que la validación se delega a los casos de uso de mayor nivel; no hay pruebas del módulo seat que confirmen el comportamiento esperado.

\2. CancelOrderUseCaseImpl puede cancelar tickets en PENDING porque ticket.cancel() no valida el estado origen. Se asume que es intencional para la cancelación temprana desde una orden en DRAFT.

\3. La asignación de RefundStatus.PROCESSED en RequestRefundUseCaseImpl (línea 113) se asume que corresponde al cierre local de reembolsos de pagos solo con wallet (closeRefundLocally()); falta confirmarlo.

### Referencias

Cockburn, A. (2005). *Hexagonal architecture.*

https://alistair.cockburn.us/hexagonal-architecture/

Equipo CapySoft. (2026). *TuEvento* [Código fuente]. Repositorio del proyecto.

Equipo CapySoft. (s. f.-a). *Especificación de requisitos de software (SRS) de TuEvento*

[Documento inédito]. Servicio Nacional de Aprendizaje (SENA).

Equipo CapySoft. (s. f.-b). *Manual técnico de TuEvento* [Documento inédito]. Servicio

Nacional de Aprendizaje (SENA).

Object Management Group. (2017). *OMG Unified Modeling Language (OMG UML)* (Versión

2.5.1). https://www.omg.org/spec/UML/2.5.1

---

<a id="doc-te-0078"></a>

<!--
ruta_anterior: es/02_Analisis_Software/secuencie_Diagrams.md
id: TE-0078
title: "secuencie Diagrams"
lang: es
source: 02_Analisis_Software/secuencie Diagrams.pdf
source_sha256: b7d6ba0d80c5ff40f29a31594877c0fd7a42fb9303a5d86899f309933886757e
conversion: "pdftoppm 300 ppp + tesseract 5.5.3 (tessdata_best 4.1.0, spa+eng, PSM 11); sin revisión manual"
converted_on: 2026-10-23
fidelity: solo_imagen
losses:
  - "PDF sin capa de texto: el texto proviene de OCR sin revisar (tesseract spa+eng)."
  - "Contenido diagramático: el OCR no conserva la disposición, flechas ni relaciones; la imagen de página es la referencia fiel."
  - "Página 1: confianza media 68%, 55 de 158 palabras < 60%."
  - "Página 2: confianza media 67%, 95 de 236 palabras < 60%."
  - "Página 3: confianza media 70%, 60 de 188 palabras < 60%."
  - "Página 4: confianza media 71%, 39 de 119 palabras < 60%."
  - "Página 5: en blanco."
  - "Se descartan las líneas de OCR con menos de 2 letras (ruido de trazos del diagrama)."
  - "Medición en la página 1 contra transcripción manual: 30 de 38 etiquetas recuperadas exactas (79 %); faltan nombres de participantes, una condición alt y la pregunta final."
  - "El contenido de los diagramas está en inglés, no en español."
status: borrador
reviewed_by: ""
-->

# secuencie Diagrams

## Página 1

![Página 1 del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/secuencie_Diagrams/pagina-1.png)

<!-- OCR: revisar · confianza media 68% · 158 palabras · 55 con confianza < 60% -->

Texto reconocido por OCR (sin orden de lectura fiable; la imagen es la referencia):

```text
Authentication and Account Access
U as "User"
Service"
Access the system
Home interface
- Hho
elgg
Is the user authenticated?
[User NOT authenticated]]
Start session
Enter credentials
[at J (invalid credentials]
Show authentication error
Retum to home
[RR
pp
nnnnaaoaao anna [ PA
[RR
credentials]]
Authenticate user
Verify provided data
Data verified
Authentication OK
E fn ff
Access granted
[[Create a new accounti
Review provided data
Jah
Data r
“TA
Enter account email
Send email verification code
le
- mm mmm mmm mmm mem
| Verification code sent
TE Lo]
Enter verification code
[ ait J (Link Google/Facebook account]]
Enter Google/Facebook credentials
Link with Google/Facebook
Account linked
JE
JER
JER SN
Linked account ready
Access granted
le
A fing he
pp i SPN NNN [PIPPI NEP RN, Sr ———
[[User already authenticated]
Access to functionalities
---- ooo
lo you want to
```

## Página 2

![Página 2 del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/secuencie_Diagrams/pagina-2.png)

<!-- OCR: revisar · confianza media 67% · 236 palabras · 95 con confianza < 60% -->

Texto reconocido por OCR (sin orden de lectura fiable; la imagen es la referencia):

```text
Organizer — Event Management
O as "Organizer"
BD as "Database"
Eno
Access the system
FE de Ah SN
Event management interface
What operation do you want to perform?
Pd pg fh
Define event layout
Configure seating
Set prices
Set schedule
gn
Publish now?
[Publish now]
Validate event information
[Information complete]
Validation OK
FER cg
Publish event
JER a
Event published
Notify publication
Pul
notification
PT
PU
FR
JRA {SR
[lincompiete information]
Validation errors
FER eg hi
Show validation errors
-] ZE e
Edit configuration
Changes applied
JE.
TALA LALA ARA
[EP SRS Sp SRpRpRY Spt RRR
[[Save as draft]
Save as draft
raft saved
Project saved as draft
Jc gh hg
Open list of projects
o — Ee
Fetch project list
List of projects
JEP vn
Sl gs
Show projects
JE
JER eg he fg
What to do with the project?
| at J [Delete project]
Confirm deletion
Delete project
JER he tl
Project deleted
JD J. igi LEO WF |
Project removed
TEE
Te e o SELLLCEE
[Publish project]
Publish selected project
Validate information
Validation completed
FE
Publish event
ia
Event published
lotify publication
lg
| Publication confirmed
peg
JE
JER
End — Operation completed
```

## Página 3

![Página 3 del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/secuencie_Diagrams/pagina-3.png)

<!-- OCR: revisar · confianza media 70% · 188 palabras · 60 con confianza < 60% -->

Texto reconocido por OCR (sin orden de lectura fiable; la imagen es la referencia):

```text
User — Browse Events and Select Seat
S as "System"
N as "Notifications"
U as "User"
BD as "Database"
Eu
Authorized user enters
Home interface
Je Za
What action do you want to perform?
[Browse events]
Search available events
Query events
Events list
oi
rm mm mm mm mm EE mm ————————
Show general information
{El
Where to see more details?
[View details]
Select event
Get event details
ai
Event details
Show event details
-—]_ie—_————————]——]— ——T——— —]——— —]—]
[Seat selection]
Start seat selection
Load seat map
eiii of
Seat map
mm mmm mmm mmm mm mm fm —————————
Display seat map
Choose seat
| alt) [Seats available]
Verify seat availability
mm
Seat available
| alt J [[Seat remains available]
Proceed to payment per seat
gf pe
Create multi-seat reservation if needed
DB  E—
[pty
A AS bocenonananananananen.
[[Seat becomes unavailable]
Notify lack of availability
Redirect to browse/search
eme
Request event management support
.....e.ee.....
ATI IIA AAA ALA ALA AAA AA? AAA ARA ALE
m......... pe
[[No seats
ble]]
ea
Show alternative seats
Continue navigation
```

## Página 4

![Página 4 del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/02_Analisis_Software/secuencie_Diagrams/pagina-4.png)

<!-- OCR: revisar · confianza media 71% · 119 palabras · 39 con confianza < 60% -->

Texto reconocido por OCR (sin orden de lectura fiable; la imagen es la referencia):

```text
Payments — Method Validation and Processing
U as "User"
Open payment page
Ve
alidate selected payment method
[[Payment method VALID]]
Method valid
Validate payment (confirmation)
L Verify successful transaction ra
Transaction confirmed
| alt 7 [Successful transaction]]
Payment processed successfully
Generate confirmation
eo
Mark seat purchase as completed
eg hs
Seat purchase completed
[ppt A eg i eg J
End — Payment processed successfully
J ELL LT EL LT puppy
fe eeeeeeeeeeeeeeeeeeeeeeeeeas pp JR
[[Failed transaction]]
Failed transaction
ae
Payment failure notification
la
Retry payment
New payment attempt
ena wm eo TTL a a devennnnvnnnnnensnnnennnnnwnmnnnedunneommnneemmnnennsnnenssmadensesenn
[Payment method INVALID]
Validation error
ooo
aio
Show validation error
le
Correct payment method
Retry payment
```

## Página 5

*Página en blanco en el original (todos los píxeles son blancos); no se incluye imagen.*

---

<a id="secuencias-implementacion"></a>

# Diagramas de secuencia de la implementación

> Sección agregada el 2026-10-24. A diferencia de las secuencias conceptuales anteriores, estas usan las clases y los endpoints reales, con el orden de llamadas confirmado en el código (`lozano2303/TuEvento`, `origin/develop` / `2d259e7`). Ver C13 en `Registro_de_Contradicciones.md`.

## Compra de entradas

Desde la creación de la orden hasta la confirmación del pago por el webhook de la pasarela simulada, incluido el pago con saldo de la billetera.

![Secuencia de compra de entradas](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_compra_entradas.png)

Fuente: [`secuencia_compra_entradas.mmd`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_compra_entradas.mmd) · versión vectorial: [`secuencia_compra_entradas.svg`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_compra_entradas.svg)

## Reserva de asientos en tiempo real

Reserva y liberación de sillas con difusión por WebSocket a todos los clientes que ven el evento, y vencimiento automático de las reservas a los 10 minutos.

![Secuencia de reserva de asientos en tiempo real](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_reserva_asientos.png)

Fuente: [`secuencia_reserva_asientos.mmd`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_reserva_asientos.mmd) · versión vectorial: [`secuencia_reserva_asientos.svg`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_reserva_asientos.svg)

## Moderación de eventos

Envío del evento a revisión por el organizador, aprobación o rechazo por el administrador y notificación al organizador.

![Secuencia de moderación de eventos](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_moderacion_eventos.png)

Fuente: [`secuencia_moderacion_eventos.mmd`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_moderacion_eventos.mmd) · versión vectorial: [`secuencia_moderacion_eventos.svg`](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/diagramas/es/secuencia_moderacion_eventos.svg)
