---
id: TE-0164
title: "Matriz_de_Trazabilidad_TuEvento"
lang: es
source: 11_Anexos/Matriz_de_Trazabilidad_TuEvento.pdf
source_sha256: f9851d363790ea9e04932d42629615411bd47825b5125cba4c97c11ff800ebcf
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–6: «Matriz de Trazabilidad - Tu Evento Pagina {n}»."
  - "PDF generado sin LaTeX: los guiones al final de línea son guiones reales y se conservan al unir las líneas (no son guiones de corte silábico)."
  - "10 viñetas de ReportLab sin correspondencia Unicode (código 127 de Helvetica, «(cid:127)» en la extracción) escritas como «•», que es como se ven en el original."
  - "Portada: líneas destacadas que no son el título se escriben como párrafos: «Propósito»; «Referencias de repositorios»; «Estructura del código fuente»."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
---
# Matriz de Trazabilidad — Proyecto Formativo Tu Evento

| **Campo** | **Detalle** |
|---|---|
| **Proyecto** | Tu Evento |
| **Programa / Ficha** | Tecnólogo en Análisis y Desarrollo de Software — Ficha 314556 (2026) |
| **Integrantes** | Jhampier Santos Ortiz · Cristofer David Lozano Contreras |
| **Fecha de actualización** | Octubre 2026 |

**Propósito**

Este documento relaciona cada requisito/historia de usuario con los módulos implementados en el código fuente (backend, frontend web y aplicación móvil) y con las pruebas que los validan, permitiendo verificar que todo lo especificado fue diseñado, construido y probado.

**Referencias de repositorios**

| **Repositorio** | **URL** |
|---|---|
| **Documentación** | https://github.com/TheJhampierBoy/TuEvento-Docs |
| **Código fuente (Backend + Frontend Web + Mobile)** | https://github.com/lozano2303/tu-evento |
| **Historias de usuario** | Historias_Usuario_TuEvento.docx |
| **SRS** | Manual SRS Tu Evento.docx |
| **Casos de prueba** | Casos_de_Prueba_TuEvento.xlsx |

**Estructura del código fuente**

| `lozano2303/tu-evento` `\|-- TuEventoBackend/ -> API REST (Spring Boot / Java)` ``\| `-- src/main/java/TuEvento/Backend/`` `\| \|-- controller/ -> Controladores REST (endpoints)` `\| \|-- service/ -> Logica de negocio` `\| \|-- model/ -> Entidades / modelos de datos` `\| \|-- repository/ -> Acceso a datos (JPA)` `\| \|-- dto/ -> Objetos de transferencia` `\| \|-- jwt/ -> Seguridad / autenticacion JWT` ``\| `-- config/ -> Configuracion general`` `\|-- TuEventoFrontend (Web)/ -> Frontend web (React + Vite)` ```-- TuEventoFrontend (Mobile)/ -> App movil (React Native + Expo)`` ```-- src/`` `\|-- screens/auth/ -> Pantallas de autenticacion` `\|-- screens/home/ -> Pantallas principales` `\|-- api/ -> Llamadas a la API` `\|-- components/ -> Componentes reutilizables` `\|-- navigation/ -> Navegacion` ```-- constants/ -> Constantes globales`` |
|---|

## Matriz de Trazabilidad

#### Convenciones de la tabla

- **RF =** Requisito Funcional
- **HU =** Historia de Usuario
- **BE =** Backend (TuEventoBackend)
- **Web =** Frontend Web (TuEventoFrontend Web)
- **Mob =** App Móvil (TuEventoFrontend Mobile)
- **CP =** Caso de Prueba (evidencia en `06_Pruebas/Backend/)`
### Módulo 1 — Registro y Autenticación de Usuarios

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-01 / HU-01** | El usuario puede registrarse con correo y contraseña | `RegisterController.java ·` `RegisterService.java · User.java ·` `AccountActivation.java` | screens/auth/RegisterScreen.tsx | RegisterController.pdf |
| **RF-02 / HU-01** | Verificación de cuenta por correo electrónico | `AccountActivationController.java ·` `AccountActivationService.java` | screens/auth/CodeVerificationScreenR egister.tsx | AccountActivationController.pdf · AccountActivationEmailController.pdf |
| **RF-03 / HU-02** | El usuario puede iniciar sesión con JWT | `LoginController.java · LoginService.java ·` `jwt/ · Login.java` | screens/auth/LoginScreen.tsx | LoginController.pdf |
| **RF-04 / HU-02** | Recuperación de contraseña por correo | `RecoverPasswordService.java ·` `RecoverPassword.java · controller/email/` | screens/auth/ForgotPasswordScreen.ts x · CodeVerifcationForgotPassword.tsx · NewPasswordScreen.tsx | — |

### Módulo 2 — Gestión de Usuarios y Perfil

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-05 / HU-03** | Consultar y actualizar perfil de usuario | `UserController.java · UserService.java ·` `User.java` | screens/home/ProfileScreen.tsx · Frontend Web (perfil) | UserController.pdf |
| **RF-06 / HU-03** | Gestión de dirección del usuario | `AddressController.java ·` `AddressService.java · Address.java` | Frontend Web | AddressController.pdf |
| **RF-07 / HU-04** | Solicitud para convertirse en organizador | `OrganizerPetitionController.java ·` `OrganizerPetitionService.java ·` `OrganizerPetition.java` | Frontend Web | OrganizerPetitionController.pdf |

### Módulo 3 — Gestión de Eventos

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-08 / HU-05** | Crear, editar y eliminar eventos | `EventController.java · EventService.java ·` `Event.java` | Frontend Web (panel organizador) | EventController.pdf |
| **RF-09 / HU-05** | Gestión de imágenes del evento | `EventImgController.java ·` `EventImgService.java · EventImg.java ·` `StorageController.java` (AWS S3) | Frontend Web | EventImgController.pdf · StorageController.pdf |
| **RF-10 / HU-05** | Configurar layout (mapa de asientos) del evento | `EventLayoutController.java ·` `EventLayoutService.java · EventLayout.java` | Frontend Web | EventLayoutController.pdf |
| **RF-11 / HU-06** | Descubrir y filtrar eventos por categoría | `CategoryController.java ·` `CategoryService.java ·` `CategoryEventController.java ·` `Category.java · CategoryEvent.java` | screens/home/EvenListScreen.tsx · Frontend Web | CategoryController.pdf · CategoryEventController.pdf |
| **RF-12 / HU-06** | Calificar un evento | `EventRatingController.java ·` `EventRatingService.java · EventRating.java` | Frontend Web · App Móvil | EventRatingController.pdf |

### Módulo 4 — Ubicación y Geolocalización

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-13 / HU-07** | Asociar ubicación física a un evento | `LocationController.java ·` `LocationService.java · Location.java ·` `Address.java` | Frontend Web | LocationController.pdf |
| **RF-14 / HU-07** | Gestión de ciudades y departamentos | `CityController.java · CityService.java ·` `DepartmentController.java ·` `DepartmentService.java · City.java ·` `Department.java` | Frontend Web | CityController.pdf · DepartmentController.pdf |

### Módulo 5 — Asientos, Secciones y Boletas

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-15 / HU-08** | Configurar secciones del evento | `SectionController.java ·` `SectionService.java · Section.java ·` `SectionNameController.java ·` `SectionName.java` | Frontend Web | SectionController.pdf |
| **RF-16 / HU-08** | Gestionar asientos por sección | `SeatController.java · SeatService.java ·` `Seat.java · SeatTicket.java` | Frontend Web | SeatController.pdf |
| **RF-17 / HU-09** | Comprar boleta y reservar asiento | `TicketController.java · TicketService.java ·` `Ticket.java · SeatTicket.java` | Frontend Web · App Móvil | TicketController.pdf |

### Módulo 6 — Notificaciones

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-18 / HU-10** | Enviar y recibir notificaciones del sistema | `NotificationController.java ·` `NotificationService.java ·` `Notification.java · NotificationUser.java ·` `NotificationUserService.java` | Frontend Web · App Móvil | NotificationController.pdf |

### Módulo 7 — Check-in con QR

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-19 / HU-11** | Validar entrada al evento con código QR | `TicketController.java` (endpoint de validación) · `Ticket.java` | screens/home/readerQr.tsx | TicketController.pdf · mobile.pdf |

### Módulo 8 — Almacenamiento en la Nube (AWS)

| **ID RF / HU** | **Descripción del requisito** | **Componente Backend** | **Componente Frontend/Mobile** | **Prueba asociada** |
|---|---|---|---|---|
| **RF-20 / HU-12** | Subir y gestionar archivos e imágenes en AWS S3 | `StorageController.java · service/aws/ ·` `service/impl/` | Frontend Web (carga de imágenes) | StorageController.pdf |

## Resumen de cobertura

| **Módulo** | **Controladores implementados** | **Pruebas disponibles** | **Pantallas implementadas** |
|---|---|---|---|
| **Registro y autenticación** | 3 (Register, AccountActivation, Login) | 3 PDFs | 6 pantallas auth (mobile) |
| **Gestión de usuarios** | 3 (User, Address, OrganizerPetition) | 3 PDFs | ProfileScreen (mobile) + Web |
| **Gestión de eventos** | 5 (Event, EventImg, EventLayout, Category, EventRating) | 5 PDFs | EvenListScreen (mobile) + Web |
| **Ubicación y geolocalización** | 3 (Location, City, Department) | 3 PDFs | Web |
| **Asientos, secciones y boletas** | 4 (Section, SectionName, Seat, Ticket) | 4 PDFs | Web + Mobile |
| **Notificaciones** | 1 (Notification) | 1 PDF | Web + Mobile |
| **Check-in QR** | 1 (Ticket - validación) | readerQr.tsx + mobile.pdf | readerQr.tsx (mobile) |
| **Almacenamiento AWS** | 1 (Storage) | 1 PDF | Web |
| **Total** | **21 controladores** | **21 PDFs de prueba** | **3 plataformas** |

## Cadena de trazabilidad completa

| `SRS / Historias de Usuario` `\|` `v` `Analisis (diagramas de clases, casos de uso)` `\|` `v` `Diseno (arquitectura, DER, patrones, SQL)` `\|` `v` `Codigo fuente` `\|-- Backend: model -> repository -> service -> controller` `\|-- Web: TuEventoFrontend (Web)` ```-- Mobile: TuEventoFrontend (Mobile) / src/screens`` `\|` `v` `Pruebas (Plan, Casos de prueba, evidencias por controlador, mobile)` `\|` `v` `Acciones correctivas y mejora` |
|---|

## Observaciones

- Las historias de usuario con ID exacto (HU-XX) deben verificarse en Historias_Usuario_TuEvento.docx para cruzar el número correcto con cada RF.
- El Frontend Web no tiene carpeta de pantallas listada explícitamente en el repositorio (usa React + Vite); su cobertura se verifica a nivel de funcionalidad desplegada.
- Las pruebas de la app móvil están consolidadas en mobile.pdf.
- Cada PDF de prueba en `06_Pruebas/Backend/` corresponde exactamente a un controlador del backend, cubriendo los 21 controladores implementados.
*Documento actualizado en octubre de 2026. Actualizar cuando se agreguen nuevos módulos, ramas o versiones al repositorio.*
