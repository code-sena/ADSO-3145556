# Energy Monitor

Final delivery of the training project / Entrega final del proyecto formativo.

## General information / Información general

| Field / Campo | Value / Valor |
|---|---|
| Product name / Nombre del producto | Energy Monitor |
| Ficha | 3145556 |
| Training program / Programa de formación | Análisis y Desarrollo de Software (228118) |
| Training project / Proyecto formativo | Desarrollo de software orientado a servicios |
| Training center / Centro de formación | Centro de la Industria, la Empresa y los Servicios (Regional Huila) |
| Process / Proceso | Completion of the technologist taught stage / Finalización de la etapa lectiva del tecnólogo |
| Research seedbed / Semillero de investigación | Developers & Geeks |
| Delivery version / Versión entregada | `final-delivery-2026` |
| Delivery date / Fecha de entrega | |
| Delivered by / Entregado por | |

## Summary / Resumen

- **Problem it solves / Problema que resuelve:**
- **Target users / Usuarios:**
- **Main features / Funciones principales:**
- **Technology stack / Tecnologías:**
- **Production URL / URL en producción:**
- **Demo credentials / Credenciales de prueba:** do not write real passwords here / no escriba contraseñas reales aquí

## Team members / Integrantes

> Names and document numbers were preloaded by the instructor from the Sofía Plus report. Each member checks their row, completes GitHub user and role, and marks the last column. Report any error, missing member or extra member to the instructor.
>
> Los nombres y documentos los precargó el instructor desde el reporte de Sofía Plus. Cada integrante revisa su fila, completa usuario de GitHub y rol, y marca la última columna. Reporte al instructor cualquier error, o si falta o sobra alguien.

| Full name / Nombre completo | Doc. type / Tipo | Doc. number / Número | Ficha | GitHub user / Usuario | Role / Rol | Verified and authorized / Verificado y autorizado |
|---|---|---|---|---|---|---|
| JUAN ESTEBAN CORTES PARRA | CC | 1079605497 | 3145556 | Juan-Cortes2045 | Project leader (Project Manager), Scrum Master, system data owner and developer / Líder del proyecto (Project Manager), Scrum Master, responsable de los datos del sistema y desarrollador | ✅ |
| MIGUEL ANGEL GARCIA ARTUNDUAGA | CC | 1075241131 | 3145556 | angelgarcia0 | Analyst, QA and developer / Analista, QA y desarrollador | ✅ |

Data use authorization: by checking the box, the member confirms their data and authorizes publishing their name, document number and ficha in this public repository, for the software registration process (Ley 1581 de 2012). Anyone who does not authorize it tells the instructor before delivering.

Autorización de datos: al marcar la casilla, el integrante confirma sus datos y autoriza publicar su nombre, número de documento y ficha en este repositorio público, para el trámite de registro de software (Ley 1581 de 2012). Quien no lo autorice se lo informa al instructor antes de entregar.

## Initial reflection / Reflexión inicial

Answers of the team to the four questions of the delivery guide (activity 3.1). Data checked on 2026-11-04.

Respuestas del equipo a las cuatro preguntas de la guía de entrega (actividad 3.1). Datos verificados el 2026-11-04.

**1. Which repository and branch would an outside person use to install the software from scratch? / ¿Qué repositorio y rama usaría una persona externa para instalar desde cero?**

The system is made of 14 public repositories (listed in [`code-tag/README.md`](code-tag/README.md)). The starting point is [`energy-monitor-infra`](https://github.com/Juan-Cortes2045/energy-monitor-infra), branch `main`: its Docker Compose builds the whole system. The gateway, the seven services and the web app are cloned from their own repository, also branch `main`. The mobile app is built from [`Energy-Monitor-Mobile`](https://github.com/angelgarcia0/Energy-Monitor-Mobile), branch `main`. The installation steps are in `11-manual-instalacion` of `energy-monitor-docs-final`.

El sistema son 14 repositorios públicos (en [`code-tag/README.md`](code-tag/README.md)). El punto de partida es [`energy-monitor-infra`](https://github.com/Juan-Cortes2045/energy-monitor-infra), rama `main`: su Docker Compose construye todo el sistema. El gateway, los siete servicios y la web se clonan de su propio repositorio, también en `main`. La app móvil se construye desde [`Energy-Monitor-Mobile`](https://github.com/angelgarcia0/Energy-Monitor-Mobile), rama `main`. Los pasos de instalación están en `11-manual-instalacion` de `energy-monitor-docs-final`.

**2. Is all finished work in `main`, or does part stay in `dev`? / ¿Todo el trabajo terminado está en `main` o parte sigue en `dev`?**

All of it is in `main`. The team's integration branch is called `dev` (not `develop`); on 2026-11-04 `dev` was fully contained in `main` in the 14 repositories, with no pending commits. The documentation is delivered in `en-monitor-docs` (course repository) and `energy-monitor-docs-final` (final version).

Todo está en `main`. La rama de integración del equipo se llama `dev` (no `develop`); el 2026-11-04 `dev` estaba contenida por completo en `main` en los 14 repositorios, sin commits pendientes. La documentación se entrega en `en-monitor-docs` (repositorio del curso) y `energy-monitor-docs-final` (versión final).

**3. Who really created the software and how is it shown? / ¿Quiénes crearon realmente el software y cómo se demuestra?**

The two apprentices, Juan Esteban Cortés Parra and Miguel Ángel García Artunduaga. It is shown by the commit history of each repository (kept complete in the `full-export` ZIP files) and by the pull requests. Commits in `main`, with merges left out:

Los dos aprendices, Juan Esteban Cortés Parra y Miguel Ángel García Artunduaga. Se demuestra con el historial de commits de cada repositorio (completo en los ZIP `full-export`) y con los pull requests. Commits en `main`, sin contar merges:

| Repository / Repositorio | Juan | Miguel | Total |
|---|---|---|---|
| Web frontend | 244 | 47 | 291 |
| Mobile app | 55 | 149 | 204 |
| API gateway | 51 | 11 | 62 |
| Security service | 66 | 10 | 76 |
| Device service | 94 | 6 | 100 |
| Measurement service | 69 | 4 | 73 |
| Home service | 3 | 56 | 59 |
| Notification service | 5 | 48 | 53 |
| Alert service | 4 | 45 | 49 |
| Recommendation service | 0 | 39 | 39 |
| Former monolith / Monolito retirado | 60 | 13 | 73 |
| Infrastructure / Infraestructura | 56 | 28 | 84 |
| Documentation, final version / Documentación, versión final | 53 | 40 | 93 |
| Documentation, course repository / Documentación, repositorio del curso | 97 | 36 | 134 |
| **Total** | **857 (62 %)** | **532 (38 %)** | **1,389** |

Each apprentice committed with more than one Git identity (name and GitHub user); the counts above add them up per person. The course repository also has 1 commit from the instructor (the initial scaffold), not counted in the totals.

Cada aprendiz hizo commits con más de una identidad de Git (nombre y usuario de GitHub); los conteos suman las identidades por persona. El repositorio del curso también tiene 1 commit del instructor (la estructura inicial), que no se cuenta en los totales.

**4. What code, libraries, models or images were not created by the team? / ¿Qué código, librerías, modelos o imágenes no fueron creados por el equipo?**

Open-source libraries and Docker images, the ESP32 firmware libraries, the Google sign-in, the Google logo, and two assistants: Claude (Anthropic) supported the code, tests and documentation, and ChatGPT (OpenAI) generated the landing and empty-state images. The project logo is the team's own design. The full list, with author and license, is in section 8 of [`software-registration.md`](software-registration.md).

Librerías de código abierto e imágenes de Docker, las librerías del firmware del ESP32, el inicio de sesión de Google, el logo de Google y dos asistentes: Claude (Anthropic) apoyó el código, las pruebas y la documentación, y ChatGPT (OpenAI) generó las imágenes de la portada y de los estados vacíos. El logo del proyecto es diseño propio. La lista completa, con autor y licencia, está en la sección 8 de [`software-registration.md`](software-registration.md).

## Delivery contents / Contenido de la entrega

| Folder / Carpeta | Content / Contenido | Done / Listo |
|---|---|---|
| [`01-requirements-specification/`](01-requirements-specification/) | Requirements specification / Especificación de requisitos | ☑ |
| [`02-software-analysis/`](02-software-analysis/) | Software analysis / Análisis del software | ☑ |
| [`03-technical-proposal/`](03-technical-proposal/) | Technical proposal / Propuesta técnica | ☑ |
| [`04-software-design/`](04-software-design/) | Software design / Diseño del software | ☑ |
| [`05-test-documentation/`](05-test-documentation/) | Test documentation / Documentación de pruebas | ☑ |
| [`06-implementation-procedure/`](06-implementation-procedure/) | Implementation procedure / Procedimiento de implementación | ☑ |
| [`07-improvement-actions/`](07-improvement-actions/) | Improvement actions / Acciones de mejoramiento | ☑ |
| [`08-manuals/`](08-manuals/) | Manuals / Manuales | ☑ |
| [`09-project-article/`](09-project-article/) | Project article / Artículo del proyecto | ☑ |
| [`10-videos/`](10-videos/) | Videos / Videos | ☑ |
| [`code-tag/`](code-tag/) | Repository links, tags and ZIP files / Enlaces, tags y ZIP | ☐ |
| [`team-roster.md`](team-roster.md) | Team and roles / Equipo y roles | ☐ |
| [`software-registration.md`](software-registration.md) | Software registration data / Datos para registro de software | ☐ |

## Large files on Google Drive / Archivos pesados en Google Drive

Only files too heavy for GitHub go here, such as demo videos. Everything else stays in this folder.

Aquí solo van archivos demasiado pesados para GitHub, como videos de demostración. Todo lo demás vive en esta carpeta.

| File / Archivo | Size / Tamaño | Link / Enlace |
|---|---|---|
| | | |