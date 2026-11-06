# TraduceSeñas

Final delivery of the training project / Entrega final del proyecto formativo.

## General information / Información general

| Field / Campo | Value / Valor |
|---|---|
| Product name / Nombre del producto | TraduceSeñas |
| Ficha | 3145556 |
| Training program / Programa de formación | Análisis y Desarrollo de Software (228118) |
| Training project / Proyecto formativo | Desarrollo de software orientado a servicios |
| Training center / Centro de formación | Centro de la Industria, la Empresa y los Servicios (Regional Huila) |
| Process / Proceso | Completion of the technologist taught stage / Finalización de la etapa lectiva del tecnólogo |
| Research seedbed / Semillero de investigación | Developers & Geeks |
| Delivery version / Versión entregada | `final-delivery-2026` |
| Delivery date / Fecha de entrega | |
| Delivered by / Entregado por | JUAN MAURICIO SUAZA SOLORZANO |

## Summary / Resumen

- **Problem it solves / Problema que resuelve:** Deaf people depend on certified Colombian Sign Language (LSC) interpreters, far fewer than the demand, especially in rural areas, public institutions and unplanned everyday situations. That costs them autonomy and participation in education, work and health. / Las personas sordas dependen de intérpretes certificados de Lengua de Señas Colombiana (LSC), muy por debajo de la demanda, sobre todo en zonas rurales, entidades públicas y situaciones cotidianas no planificadas. Eso les resta autonomía y participación en la educación, el trabajo y la salud.
- **Target users / Usuarios:** Deaf or hard-of-hearing people who sign in LSC; hearing people who do not know LSC, such as teachers, students, shop assistants and public servants; and platform administrators. / Personas sordas o con hipoacusia que se comunican en LSC; personas oyentes que no conocen la LSC, como docentes, estudiantes, personal de tiendas y servidores públicos; y administradores de la plataforma.
- **Main features / Funciones principales:** camera-based LSC recognition on the device, shown as text and read aloud (about 54 words and the alphabet; on Android, word mode runs in a WebView and alphabet recognition is web only); translation history; alphabet with 3D models; accounts with email verification and Google or Facebook sign-in; roles and permissions; profile, theme and language; daily challenges, medals and achievements; in-app and email notifications; usage statistics and sign templates for administrators. It does not translate text or voice into signs, and it does not replace a certified interpreter. / reconocimiento de LSC con la cámara en el propio dispositivo, mostrado como texto y leído en voz alta (unas 54 palabras y el abecedario; en Android el modo palabras corre en una WebView y el reconocimiento del abecedario es solo web); historial de traducciones; alfabeto con modelos 3D; cuentas con verificación de correo e inicio de sesión con Google o Facebook; roles y permisos; perfil, tema e idioma; retos diarios, medallas y logros; notificaciones en la app y por correo; estadísticas de uso y plantillas de señas para administradores. No traduce texto o voz a señas ni reemplaza a un intérprete certificado.
- **Technology stack / Tecnologías:** Expo 57, React Native 0.86, react-native-web and TypeScript; MediaPipe Tasks Vision and TensorFlow.js; Node.js 20, Express 5 and TypeScript in five microservices behind an API Gateway; PostgreSQL 16 (one per service) and MongoDB 7; RabbitMQ; JWT; Docker Compose and Caddy. / Expo 57, React Native 0.86, react-native-web y TypeScript; MediaPipe Tasks Vision y TensorFlow.js; Node.js 20, Express 5 y TypeScript en cinco microservicios detrás de un API Gateway; PostgreSQL 16 (una por servicio) y MongoDB 7; RabbitMQ; JWT; Docker Compose y Caddy.
- **Production URL / URL en producción:** https://traducesenas.tech
- **Demo credentials / Credenciales de prueba:** no shared account: the evaluator registers at the production URL and confirms their email / sin cuenta compartida: el evaluador se registra en la URL de producción y confirma su correo

## Team members / Integrantes

> Names and document numbers were preloaded by the instructor from the Sofía Plus report. Each member checks their row, completes GitHub user and role, and marks the last column. Report any error, missing member or extra member to the instructor.
>
> Los nombres y documentos los precargó el instructor desde el reporte de Sofía Plus. Cada integrante revisa su fila, completa usuario de GitHub y rol, y marca la última columna. Reporte al instructor cualquier error, o si falta o sobra alguien.

| Full name / Nombre completo | Doc. type / Tipo | Doc. number / Número | Ficha | GitHub user / Usuario | Role / Rol | Verified and authorized / Verificado y autorizado |
|---|---|---|---|---|---|---|
| JUAN MAURICIO SUAZA SOLORZANO | CC | 1076907287 | 3145556 | mauro-1508 | Tech lead, Scrum Master and analyst-developer (lexicon and recognition backend, coordination) / Líder técnico, Scrum Master y analista-desarrollador (backend lexicon y recognition, coordinación) | ☐ |
| LUIS ALEJANDRO DUARTE ALDANA | CC | 1069402330 | 3145556 | tadeo77789 | Analyst-developer (interfaces and programming) / Analista-desarrollador (interfaces y programación) | ☐ |
| JUAN CAMILO FIERRO ESQUIVEL | CC | 1080292691 | 3145556 | fierroesquiveljuancamilo-svg | Analyst-developer (product documentation and programming) / Analista-desarrollador (documentación de producto y programación) | ☐ |

Data use authorization: by checking the box, the member confirms their data and authorizes publishing their name, document number and ficha in this public repository, for the software registration process (Ley 1581 de 2012). Anyone who does not authorize it tells the instructor before delivering.

Autorización de datos: al marcar la casilla, el integrante confirma sus datos y autoriza publicar su nombre, número de documento y ficha en este repositorio público, para el trámite de registro de software (Ley 1581 de 2012). Quien no lo autorice se lo informa al instructor antes de entregar.

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
| `Traduce_señas_pitch.mp4` | 17.0 MB | [Google Drive](https://drive.google.com/file/d/1d0ICdnzrIn2V2BSZ0yNUOGjvSDuRgLZ1/view?usp=sharing) |
| `Traduce_señas_Tutorial.mp4` | 26.9 MB | [Google Drive](https://drive.google.com/file/d/1rKvoF2k2tP0xoC65_oN2lKgVH2vJKK7J/view?usp=sharing) |
