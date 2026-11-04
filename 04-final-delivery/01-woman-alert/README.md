# Alerta Mujer

Final delivery of the training project / Entrega final del proyecto formativo.

## General information / Información general

| Field / Campo | Value / Valor |
|---|---|
| Product name / Nombre del producto | Alerta Mujer |
| Ficha | 3145556 |
| Training program / Programa de formación | Análisis y Desarrollo de Software (228118) |
| Training project / Proyecto formativo | Desarrollo de software orientado a servicios |
| Training center / Centro de formación | Centro de la Industria, la Empresa y los Servicios (Regional Huila) |
| Process / Proceso | Completion of the technologist taught stage / Finalización de la etapa lectiva del tecnólogo |
| Research seedbed / Semillero de investigación | Developers & Geeks |
| Delivery version / Versión entregada | `final-delivery-2026` |
| Delivery date / Fecha de entrega | 2026-11-04 |
| Delivered by / Entregado por | MICHAEL STIVEN SANCHEZ CASTAÑEDA (`menticio`) |

## Summary / Resumen

- **Problem it solves / Problema que resuelve:** Women in Colombia who face risk or gender-based violence need help from their support network as fast as possible, even when they cannot use the phone openly. Alerta Mujer is a personal-safety system that shortens the time between danger and help: it raises an SOS alert in several discreet ways, warns her trusted contacts, shares her live location and records evidence.

  Las mujeres en Colombia que viven situaciones de riesgo o de violencia de género necesitan ayuda de su red de apoyo lo más rápido posible, incluso cuando no pueden usar el teléfono abiertamente. Alerta Mujer es un sistema de seguridad personal que acorta el tiempo entre el peligro y la ayuda: activa una alerta SOS de varias formas discretas, avisa a sus contactos de confianza, comparte su ubicación en vivo y graba evidencia.
- **Target users / Usuarios:** (1) Women at risk or facing gender-based violence, with Neiva, Huila, as the first scope: they use the Android app and the web portal. (2) Their trusted contacts (family and friends): they receive the alerts and follow her live location without an account. (3) Administrators and moderators: they manage users, alerts, risk zones, reports, evidence and the audit log from the web admin panel.

  (1) Mujeres en riesgo o que viven violencia de género, con Neiva, Huila, como alcance inicial: usan la app Android y el portal web. (2) Sus contactos de confianza (familia y amistades): reciben las alertas y siguen su ubicación en vivo sin necesidad de cuenta. (3) Administradores y moderadores: gestionan usuarios, alertas, zonas de riesgo, reportes, evidencias y auditoría desde el panel de administración web.
- **Main features / Funciones principales:** SOS alert with a cancellable countdown, raised from the on-screen button, the home-screen widget, shaking the phone or pressing the power button 4 times (a native Kotlin module keeps shake and power-button detection working in the background with the screen locked; after a reboot it re-registers the automatic-protection geofences, and shake/power-button protection resumes when she opens the app); trusted contacts warned by email and by push with a full-screen alarm (on a channel configured to bypass Do Not Disturb when the phone allows it), a public live-tracking link and a chat during the alert; automatic evidence (video with sound while the app is on screen, first 5 minutes, then audio; audio only in the background through a native microphone service) stored in the cloud and deleted after 30 days unless she chooses to keep that alert; safe trips ("Avísame si no llego") and automatic protection when she leaves her frequent places; cancel PIN, duress PIN and discreet mode; risk-zone map with citizen reports, saved places and routes, and emergency resources; web user portal and admin panel (users, alerts, zones, PDF/CSV reports, evidence, devices, audit log); Google Sign-In; interface in Spanish, English, Portuguese and French.

  Alerta SOS con cuenta regresiva cancelable, activada con el botón en pantalla, el widget de la pantalla de inicio, agitando el teléfono o pulsando 4 veces el botón de encendido (un módulo nativo en Kotlin mantiene activa la detección por agitado y por botón de encendido en segundo plano con la pantalla bloqueada; después de reiniciar vuelve a registrar las geocercas de la protección automática, y la detección por agitado y botón de encendido se reanuda cuando ella abre la app); aviso a los contactos de confianza por correo y por notificación push con alarma a pantalla completa (en un canal configurado para saltarse el modo No molestar cuando el teléfono lo permite), enlace público de seguimiento en vivo y chat durante la alerta; evidencia automática (video con sonido mientras la app está en pantalla, primeros 5 minutos, y luego audio; en segundo plano solo audio, con un servicio nativo de micrófono) guardada en la nube y borrada a los 30 días salvo las alertas que ella decide conservar; viajes seguros ("Avísame si no llego") y protección automática al salir de sus lugares frecuentes; PIN de cancelación, PIN de coacción y modo discreto; mapa de zonas de riesgo con reportes ciudadanos, lugares y rutas guardados, y recursos de emergencia; portal web de la usuaria y panel de administración (usuarios, alertas, zonas, reportes PDF/CSV, evidencias, dispositivos, auditoría); inicio de sesión con Google; interfaz en español, inglés, portugués y francés.
- **Technology stack / Tecnologías:** Backend: Java 17, Spring Boot 3.2, 3 hexagonal microservices (auth, alert, evidence) with a shared kernel, PostgreSQL 16 and Liquibase. Web: Angular 21, TypeScript, Tailwind CSS, Bootstrap and Leaflet. Mobile: Expo SDK 54, React Native 0.81, TypeScript and a native Kotlin module (Android). Services: Firebase Cloud Messaging, Google Sign-In, Backblaze B2 (S3) and Resend (email). DevOps: Docker, Render, Vercel, GitHub Actions, Jenkins and GitHub Releases.

  Backend: Java 17, Spring Boot 3.2, 3 microservicios hexagonales (auth, alert, evidence) con un núcleo compartido, PostgreSQL 16 y Liquibase. Web: Angular 21, TypeScript, Tailwind CSS, Bootstrap y Leaflet. Móvil: Expo SDK 54, React Native 0.81, TypeScript y un módulo nativo en Kotlin (Android). Servicios: Firebase Cloud Messaging, inicio de sesión con Google, Backblaze B2 (S3) y Resend (correo). DevOps: Docker, Render, Vercel, GitHub Actions, Jenkins y GitHub Releases.
- **Production URL / URL en producción:**
  - Web (user portal, admin panel and public live tracking) / Web (portal de usuaria, panel de administración y seguimiento público): <https://alerta-mujer.vercel.app>
  - Auth API / API de autenticación: <https://alerta-mujer-auth.onrender.com> (health / estado: [`/actuator/health`](https://alerta-mujer-auth.onrender.com/actuator/health))
  - Alert API / API de alertas: <https://alerta-mujer-alert.onrender.com> (health / estado: [`/actuator/health`](https://alerta-mujer-alert.onrender.com/actuator/health))
  - Evidence API / API de evidencias: <https://alerta-mujer-evidence.onrender.com> (health / estado: [`/actuator/health`](https://alerta-mujer-evidence.onrender.com/actuator/health))
  - Android APK, latest release / APK Android, última versión: <https://github.com/JustinDanielaBahamon/AlertaMujer/releases/latest/download/AlertaMujer.apk> ([releases](https://github.com/JustinDanielaBahamon/AlertaMujer/releases))
  - The APIs run on Render's free plan: if a service is asleep, its first request can take one to two minutes. / Las API corren en el plan gratuito de Render: si un servicio está dormido, su primera petición puede tardar uno o dos minutos.
- **Demo credentials / Credenciales de prueba:** Test accounts are given to the instructor through the private channel; no credentials are written in this repository. / Las cuentas de prueba se entregan al instructor por el canal privado; no se escriben credenciales en este repositorio.

## Team members / Integrantes

> Names and document numbers were preloaded by the instructor from the Sofía Plus report. Each member checks their row, completes GitHub user and role, and marks the last column. Report any error, missing member or extra member to the instructor.
>
> Los nombres y documentos los precargó el instructor desde el reporte de Sofía Plus. Cada integrante revisa su fila, completa usuario de GitHub y rol, y marca la última columna. Reporte al instructor cualquier error, o si falta o sobra alguien.

| Full name / Nombre completo | Doc. type / Tipo | Doc. number / Número | Ficha | GitHub user / Usuario | Role / Rol | Verified and authorized / Verificado y autorizado |
|---|---|---|---|---|---|---|
| MICHAEL STIVEN SANCHEZ CASTAÑEDA | CC | 1023375158 | 3145556 | menticio | Team lead and integrator: backend (hexagonal microservices), database, DevOps, mobile (native Kotlin module) and web / Líder e integrador: backend (microservicios hexagonales), base de datos, DevOps, móvil (módulo nativo Kotlin) y web | ☐ |
| JUSTIN DANIELA BAHAMON BALOCO | CC | 1015407712 | 3145556 | JustinDanielaBahamon | Mobile and web frontend, UI/UX and initial database design; repository owner / Frontend móvil y web, UI/UX y diseño inicial de la base de datos; dueña de los repositorios | ☐ |
| JOSE SEBASTIAN SUAREZ CUMACO | CC | 1077721136 | 3145556 | JoseSebastianSuarezCumaco | Web and mobile frontend (authentication, zones, active alert, map), mobile internationalization and documentation / Frontend web y móvil (autenticación, zonas, alerta activa, mapa), internacionalización móvil y documentación | ☐ |

Data use authorization: by checking the box, the member confirms their data and authorizes publishing their name, document number and ficha in this public repository, for the software registration process (Ley 1581 de 2012). Anyone who does not authorize it tells the instructor before delivering.

Autorización de datos: al marcar la casilla, el integrante confirma sus datos y autoriza publicar su nombre, número de documento y ficha en este repositorio público, para el trámite de registro de software (Ley 1581 de 2012). Quien no lo autorice se lo informa al instructor antes de entregar.

## Delivery contents / Contenido de la entrega

| Folder / Carpeta | Content / Contenido | Done / Listo |
|---|---|---|
| [`01-requirements-specification/`](01-requirements-specification/) | Requirements specification / Especificación de requisitos | ☐ |
| [`02-software-analysis/`](02-software-analysis/) | Software analysis / Análisis del software | ☐ |
| [`03-technical-proposal/`](03-technical-proposal/) | Technical proposal / Propuesta técnica | ☐ |
| [`04-software-design/`](04-software-design/) | Software design / Diseño del software | ☐ |
| [`05-test-documentation/`](05-test-documentation/) | Test documentation / Documentación de pruebas | ☐ |
| [`06-implementation-procedure/`](06-implementation-procedure/) | Implementation procedure / Procedimiento de implementación | ☐ |
| [`07-improvement-actions/`](07-improvement-actions/) | Improvement actions / Acciones de mejoramiento | ☐ |
| [`08-manuals/`](08-manuals/) | Manuals / Manuales | ☐ |
| [`09-project-article/`](09-project-article/) | Project article / Artículo del proyecto | ☐ |
| [`10-videos/`](10-videos/) | Videos / Videos | ☐ |
| [`code-tag/`](code-tag/) | Repository links, tags and ZIP files / Enlaces, tags y ZIP | ☐ |
| [`team-roster.md`](team-roster.md) | Team and roles / Equipo y roles | ☐ |
| [`software-registration.md`](software-registration.md) | Software registration data / Datos para registro de software | ☐ |

## Large files on Google Drive / Archivos pesados en Google Drive

Only files too heavy for GitHub go here, such as demo videos. Everything else stays in this folder.

Aquí solo van archivos demasiado pesados para GitHub, como videos de demostración. Todo lo demás vive en esta carpeta.

| File / Archivo | Size / Tamaño | Link / Enlace |
|---|---|---|
| Advertising pitch video / Video pitch publicitario | Pending / Pendiente | Pending link / Enlace pendiente |
| Short usage tutorial video / Video tutorial corto de uso | Pending / Pendiente | Pending link / Enlace pendiente |
