# TuEvento

Final delivery of the training project / Entrega final del proyecto formativo.

## General information / Información general

| Field / Campo | Value / Valor |
|---|---|
| Product name / Nombre del producto | TuEvento |
| Ficha | 3145556 |
| Training program / Programa de formación | Análisis y Desarrollo de Software (228118) |
| Training project / Proyecto formativo | Desarrollo de software orientado a servicios |
| Training center / Centro de formación | Centro de la Industria, la Empresa y los Servicios (Regional Huila) |
| Process / Proceso | Completion of the technologist taught stage / Finalización de la etapa lectiva del tecnólogo |
| Research seedbed / Semillero de investigación | Developers & Geeks |
| Delivery version / Versión entregada | `final-delivery-2026` |
| Delivery date / Fecha de entrega | 2026-11-05 |
| Delivered by / Entregado por | CRISTOFER DAVID LOZANO CONTRERAS ([lozano2303](https://github.com/lozano2303)) |
| Team name / Nombre del equipo | CapySoft |

## Summary / Resumen

TuEvento is a web and mobile platform where anyone can create, publish and sell their own events: an emerging artist who wants to be known, or a company that runs small and medium events. To get the event-design tools, a user only uploads a document and waits for an administrator to approve them as an organizer.

TuEvento es una plataforma web y móvil en la que cualquier persona puede crear, publicar y vender sus propios eventos: un artista que busca darse a conocer o una empresa que organiza eventos pequeños y medianos. Para acceder a las herramientas de diseño de eventos, el usuario solo carga un documento y espera a que un administrador lo apruebe como organizador.

- **Problem it solves / Problema que resuelve:** small organizers and independent artists depend on large ticketing platforms that are hard to access, or sell tickets by hand with no seat control, no payment record and no entry validation. / Los organizadores pequeños y los artistas independientes dependen de grandes ticketeras de difícil acceso, o venden boletas a mano, sin control de asientos, sin registro de pagos y sin validación del ingreso.
- **Target users / Usuarios:** attendees who discover events and buy tickets; organizers (artists, companies) who design and sell their events; administrators who approve organizers and moderate content. / Asistentes que descubren eventos y compran boletas; organizadores (artistas, empresas) que diseñan y venden sus eventos; administradores que aprueban organizadores y moderan el contenido.
- **Main features / Funciones principales:**
  - Registration, login with email or Google, account activation and password recovery. / Registro, ingreso con correo o Google, activación de cuenta y recuperación de contraseña.
  - Organizer request with a supporting document, reviewed by an administrator. / Solicitud para ser organizador con documento de soporte, revisada por un administrador.
  - Event creation with images, categories and location on a map. / Creación de eventos con imágenes, categorías y ubicación en mapa.
  - Visual venue layout editor: sections, seats and prices. / Editor visual del plano del lugar: secciones, asientos y precios.
  - Real-time seat selection and reservation (WebSocket). / Selección y reserva de asientos en tiempo real (WebSocket).
  - Ticket purchase through a payment gateway (sandbox), digital wallet and refunds when an event is cancelled. / Compra de boletas con pasarela de pago (sandbox), billetera digital y reembolsos cuando se cancela un evento.
  - QR tickets, validated by the organizer at the entrance with the mobile app. / Boletas con QR que el organizador valida en la entrada con la app móvil.
  - Notifications, ratings and comments, automatic moderation of uploaded images, themes and two languages (es/en). / Notificaciones, calificaciones y comentarios, moderación automática de imágenes subidas, temas y dos idiomas (es/en).
- **Technology stack / Tecnologías:** Java 17 and Spring Boot 3.5 microservices (Spring Cloud Gateway, identity, catalog, commerce, notification, storage, geolocation), PostgreSQL, Redis, Kafka, S3-compatible storage (MinIO/RustFS), Liquibase; React 18 + Vite 5 web app; React Native 0.81 + Expo 54 mobile app; Python FastAPI image moderation service; Docker Compose, GitHub Actions and Nginx. / Microservicios en Java 17 y Spring Boot 3.5 (Spring Cloud Gateway, identity, catalog, commerce, notification, storage, geolocation), PostgreSQL, Redis, Kafka, almacenamiento compatible con S3 (MinIO/RustFS), Liquibase; aplicación web en React 18 + Vite 5; aplicación móvil en React Native 0.81 + Expo 54; servicio de moderación de imágenes en Python FastAPI; Docker Compose, GitHub Actions y Nginx.
- **Production URL / URL en producción:** <https://tuevento-capysoft.tech>
- **Demo credentials / Credenciales de prueba:** not published; the instructor can request a test account from the team. / No se publican; el instructor puede solicitar una cuenta de prueba al equipo.
- **Repositories / Repositorios:** see / ver [`code-tag/README.md`](code-tag/README.md).

## Team members / Integrantes

> Names and document numbers were preloaded by the instructor from the Sofía Plus report. Each member checks their row, completes GitHub user and role, and marks the last column. Report any error, missing member or extra member to the instructor.
>
> Los nombres y documentos los precargó el instructor desde el reporte de Sofía Plus. Cada integrante revisa su fila, completa usuario de GitHub y rol, y marca la última columna. Reporte al instructor cualquier error, o si falta o sobra alguien.

| Full name / Nombre completo | Doc. type / Tipo | Doc. number / Número | Ficha | GitHub user / Usuario | Role / Rol | Verified and authorized / Verificado y autorizado |
|---|---|---|---|---|---|---|
| CRISTOFER DAVID LOZANO CONTRERAS | CC | 1075322976 | 3145556 | lozano2303 | Project lead, backend, mobile, database / Líder, backend, móvil, base de datos | ☑ |
| JHAMPIER SANTOS ORTIZ | CC | 1077852139 | 3145556 | TheJhampierBoy | Web frontend, CI/CD and deployment / Frontend web, CI/CD y despliegue | ☑ |

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
| [`code-tag/`](code-tag/) | Repository links, tags and ZIP files / Enlaces, tags y ZIP | ☑ |
| [`team-roster.md`](team-roster.md) | Team and roles / Equipo y roles | ☑ |
| [`software-registration.md`](software-registration.md) | Software registration data / Datos para registro de software | ☑ |

## Large files on Google Drive / Archivos pesados en Google Drive

Only files too heavy for GitHub go here, such as demo videos. Everything else stays in this folder.

Aquí solo van archivos demasiado pesados para GitHub, como videos de demostración. Todo lo demás vive en esta carpeta.

| File / Archivo | Size / Tamaño | Link / Enlace |
|---|---|---|
| `tuevento-usage-tutorial.mp4` — usage tutorial / tutorial de uso | 20.2 MB | [Google Drive](https://drive.google.com/file/d/1JQwpBJMLQlUPd2DGHQo5i_C7UzpmvF-B/view?usp=sharing) |
| `tuevento-advertising-pitch.mp4` — advertising pitch / pitch publicitario | 25.6 MB | [Google Drive](https://drive.google.com/file/d/1muZgLYzA7VaPwu_vAFrtiOllNIjjoIlN/view?usp=sharing) |
