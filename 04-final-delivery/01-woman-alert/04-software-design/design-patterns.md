# Design patterns and where they are used / Patrones de diseño y dónde se usan

Paths are relative to each repository at tag `final-delivery-2026`: Backend-ALERTA-MUJER `107e63eb` (folder `microservicios/`), AlertaMujer `1f7cf16f` (folder `Frontend-movil/`) and Frond-end-Web `377d7867` (folder `alerta-mujer-web/`). The class diagram `design-patterns-class-diagram.png` shows the conceptual model of these patterns.

Las rutas son relativas a cada repositorio en el tag `final-delivery-2026`. El diagrama de clases `design-patterns-class-diagram.png` muestra el modelo conceptual de estos patrones.

## Backend (Java, Spring Boot)

| Pattern / Patrón | Where it is used / Dónde se usa | What it solves / Qué resuelve |
|---|---|---|
| Hexagonal architecture (ports and adapters) / Arquitectura hexagonal (puertos y adaptadores) | Every module of the 3 services: `<service>/src/main/java/com/alertamujer/<module>/{domain,application,adapter}`, with ports in `application/port/in` and `application/port/out` (30 port interfaces). Enforced by `HexagonalArchitectureTest` in auth-service, alert-service and evidence-service | The domain does not depend on Spring, the database or external services / El dominio no depende de Spring, la base de datos ni servicios externos |
| Adapter / Adaptador | `adapter/in/web/*Controller` (HTTP in), `adapter/out/persistence/*` (JPA out), `adapter/out/storage/S3EvidenceStorage`, `notifications/adapter/out/email/EmailsAdapter` | Translates between the ports and each technology / Traduce entre los puertos y cada tecnología |
| Strategy / Estrategia | Evidence storage: port `evidence-service/.../evidence/application/port/out/EvidenceStorage.java` with `S3EvidenceStorage` (Backblaze B2) and `DiskEvidenceStorage`, chosen by `evidence.storage`. Email: `alert-service/.../notifications/adapter/out/email/EmailSender.java` with `ResendEmailSender`, `BrevoEmailSender` and `LogEmailSender`, chosen in `EmailSenderConfig` by `notifications.email.provider` | Changes the provider by configuration, without touching the use cases / Cambia el proveedor por configuración, sin tocar los casos de uso |
| Facade (module API) / Fachada (API del módulo) | `shared-kernel/src/main/java/com/alertamujer/*/api/*Api.java`: `AlertsApi`, `DevicesApi`, `EvidenceApi`, `IdentityApi`, `IdentityAdminApi`, `SettingsApi`, `ZonesApi` | A module only talks to another through its public API / Un módulo solo habla con otro por su API pública |
| Observer (domain events) / Observador (eventos de dominio) | Events in `shared-kernel/.../*/api/*Event.java` (for example `AlertActivatedEvent`, `AlertEndedEvent`); publishers `identity/adapter/out/events/IdentityEventsAdapter`, `devices/adapter/out/events/DeviceEventsAdapter`; listeners `notifications/adapter/in/events/NotificationEventsListener`, `evidence/adapter/in/events/EvidenceAccountDeletedListener`, `admin/adapter/in/events/AdminSessionListener`; between services `auth-service/.../microservice/EventsToAlertService` and `shared-kernel/.../shared/rpc/ReceivedEventsController` | When an alert starts or an account is deleted, the other modules react without being called directly / Cuando empieza una alerta o se borra una cuenta, los demás módulos reaccionan sin llamarse directamente |
| Repository / Repositorio | Output ports such as `admin/application/port/out/DeletionRequestRepository`, implemented with Spring Data in `adapter/out/persistence/*JpaRepository` | Hides SQL and JPA from the use cases / Oculta SQL y JPA de los casos de uso |
| Chain of responsibility (filters) / Cadena de responsabilidad (filtros) | `shared-kernel/.../shared/security/JwtAuthenticationFilter.java` and `shared-kernel/.../shared/rpc/InternalKeyFilter.java` in the Spring Security filter chain | Each filter checks one thing (user token, internal key) and passes the request on / Cada filtro revisa una cosa y pasa la petición |
| Composition (shared email layout) / Composición (diseño de correo compartido) | `notifications/adapter/out/email/EmailLayout.java`, reused by `AlertEmailTemplate` and `RecoveryCodeTemplate` | All emails share one layout and only fill their content / Todos los correos comparten un diseño y solo llenan su contenido |
| Dependency injection and singleton (Spring beans) / Inyección de dependencias y singleton | Constructor injection in every service and adapter; Spring keeps one instance of each bean | Decoupled classes that are easy to test with Mockito / Clases desacopladas y fáciles de probar con Mockito |

## Mobile app (React Native) / App móvil

| Pattern / Patrón | Where it is used / Dónde se usa | What it solves / Qué resuelve |
|---|---|---|
| MVVM (Model-View-ViewModel) | `src/features/<feature>/view/*` and `src/features/<feature>/viewModel/use*ViewModel.ts`, for example `src/features/home/viewModel/useHomeViewModel.ts` | The screen only draws; the logic lives in the view-model hook / La pantalla solo dibuja; la lógica vive en el hook |
| Provider (React Context) | `src/contexts/AuthContext.tsx`, `ContactsContext.tsx`, `EvidenceContext.tsx`, `LocaleContext.tsx`, `ThemeContext.tsx`, `NotificationsContext.tsx` | Shared state (session, contacts, language, theme) without passing props through every screen / Estado compartido sin pasar props por cada pantalla |
| Observer / Observador | `src/notifications/inbox.ts` (listeners refreshed when a notification arrives) | The bell and the inbox update by themselves / La campana y la bandeja se actualizan solas |
| Bridge to native code / Puente a código nativo | `modules/shake-sensor` (Kotlin: shake, power button, volume, geofences, background services) exposed to JavaScript as an Expo module | Hardware features JavaScript cannot reach / Funciones de hardware que JavaScript no alcanza |

## Web portal (Angular) / Portal web

| Pattern / Patrón | Where it is used / Dónde se usa | What it solves / Qué resuelve |
|---|---|---|
| Interceptor (chain of responsibility) | `src/app/core/interceptors/jwt.interceptor.ts` and `service-routes.interceptor.ts` | Adds the token and sends each request to the right microservice / Agrega el token y envía cada petición al microservicio correcto |
| Guard | `src/app/core/guards/role.guard.ts` | Only users with the right role open each section / Solo quien tiene el rol abre cada sección |
| Service layer and dependency injection / Capa de servicios e inyección | `src/app/core/services/*` injected into the components | Components do not call HTTP directly / Los componentes no llaman HTTP directamente |
| Pipe (decorator of values) | `src/app/core/i18n/i18n.ts` (`TPipe`) | Translates the interface into 4 languages from the template / Traduce la interfaz a 4 idiomas desde la plantilla |
