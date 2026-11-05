# EduAirControl · Manual técnico

**María de los Ángeles Olaya García; Jhon Jairo Cortez Vargas**

Servicio Nacional de Aprendizaje (SENA), Programa ADSO-3145556

Instructora: Karol Daniela Correa

12 de octubre de 2026

---

# EduAirControl · Manual técnico

**SENA INDUSTRIA**

**EDUAIRCONTROL**

**Manual técnico**

*Arquitectura, componentes, datos, API y mantenimiento*

**Programa:** Tecnólogo en Análisis y Desarrollo de Software

Group (Ficha): 3145556

Team members: Jhon Jairo Cortez Vargas · María de los Ángeles Olaya García

Instructor: Karol Daniela Correa

Neiva, Huila · October 5, 2026

**Versión 1.1**

---

## Versión Control

| **Versión** | **Fecha** | **Descripción** | **Responsable** |
| --- | --- | --- | --- |
| 1.0 | October 5, 2026 | Initial creation of the technical manual. | Development team |
| 1.1 | October 7, 2026 | Alineación con el código actual: arquitectura de microservicios, API Gateway/OAuth2, roles RBAC, inicio de sesión social y Súper administrador. | Development team |

---

## Tabla de contenido

- Introducción
- Objetivo
- Alcance
- Technical description of the system
- Arquitectura
  - 5.1 Flujo de información ambiental
  - 5.2 Componentes y versiones
- Project structure
- Módulos técnicos del backend
- Base de datos
- API y endpoints
- Seguridad
- Frontend web y aplicación móvil
- Integración de hardware e IoT
- Mantenimiento
- Glosario
- Apéndices y asuntos pendientes de esta versión

---

## 1. Introducción

Este manual técnico documenta la construcción de EduAirControl, una plataforma para gestionar, monitorear y analizar entornos educativos mediante datos ambientales e IoT. Está dirigido a desarrolladores, personal de soporte, instructores y responsables del mantenimiento del sistema.

## 2. Objetivo

Describir la arquitectura, las tecnologías, la estructura del código, la persistencia, los servicios, la seguridad, la comunicación con dispositivos y las recomendaciones de mantenimiento de EduAirControl.

## 3. Alcance

El documento cubre el sistema web, el backend, la base de datos PostgreSQL, la aplicación móvil Expo/React Native, los servicios Docker y la integración conceptual con el ESP32. Los archivos ESP32 incluidos en la entrega corresponden a pruebas de sensores; la ingesta mediante el firmware definitivo debe confirmarse antes de pasar a producción.

## 4. Technical Descripción of the System

EduAirControl está organizado como una solución cliente-servidor. El frontend web y la aplicación móvil consumen el backend mediante HTTP y JSON. El backend gestiona la autenticación, los entornos, los sensores, las mediciones, los análisis, las alertas, las preferencias y las notificaciones. PostgreSQL almacena la información y Liquibase controla la evolución del esquema.

## 5. Arquitectura

![captura de pantalla](../images/image-6.png)

EduAirControl está implementado como una arquitectura de microservicios con Spring Boot. Cada servicio es un proyecto independiente con su propia base de datos (un esquema dedicado según ADR-003) y sigue una arquitectura hexagonal (ADR-009). Los servicios son: api-gateway (puerto 8080), ms-security (8081), ms-classroom-management (3002), ms-environment-monitoring (3003), ms-sensor-management (3004) y ms-user-management (3007). El api-gateway actúa como BFF (Backend for Frontend) con OAuth2/OIDC (ADR-017).

### 5.1 Environmental Information Flow

1. Los sensores capturan variables del entorno educativo.
2. El ESP32 procesa las lecturas y, en la integración final, las envía por la red al endpoint de ingesta.
3. El backend valida la autenticación del dispositivo, procesa la lectura y la almacena en PostgreSQL.
4. El frontend web y la aplicación móvil consultan la API y presentan mediciones, estados, análisis y alertas.

### 5.2 Components and Versions

| **Componente** | **Technology / version** | **Función** |
| --- | --- | --- |
| Web frontend | React 19.2.0, Vite 8.0.1 | Interfaz web y navegación |
| Backend | Java 17, Spring Boot 4.0.5 | API, lógica de negocio y seguridad |
| Persistence | PostgreSQL 15 + Spring Data JPA | Information storage |
| Migrations | Liquibase | Creación y versionado del esquema |
| Authentication | JWT 0.11.5 | Sesiones y autorización |
| API | Springdoc OpenAPI 2.5.0 | Swagger documentation |
| Mobile | Expo 54.0.37, React Native 0.81.5 | Android/iOS application |
| Containers | Docker Compose | Local orchestration |
| Local mail | Mailpit | Pruebas de recuperación de contraseña |
| Testing | JUnit/Spring Boot Testcontainers | Automated validation |
| API Gateway | Spring Cloud Gateway | Enrutamiento, autenticación y limitación de solicitudes |
| Auth Server | Spring Authorization Server 7 | OAuth2/OIDC y emisión de JWT RS256 |
| Social Login | Google OAuth2, Facebook Login | Inicio de sesión con redes sociales |
| Roles | SUPER_ADMIN, ADMIN, USER, VIEWER | Control de acceso RBAC |

## 6. Project Structure

```
EduAirControl/
├── Web/
│   ├── Front-End/             # React + Vite
│   ├── backend/               # Java + Spring Boot
│   ├── docker-compose.yml     # local services
│   └── docker-compose-jenkins.yml
├── MOBILE/                    # Expo + React Native
├── database/                  # Liquibase changelogs and models
├── iot/esp32/                 # ESP32 sensor tests
├── ms-monitoreo/              # analysis microservice under construction
├── jenkins/                   # auxiliary Jenkins image
└── Docs/                      # project documentation
```

| **Path** | **Responsibility** |
| --- | --- |
| Web/Front-End/src | Screens, components, services, viewmodels, routes and internationalization |
| Web/backend/src/main/java | Identity, classrooms, sensors, monitoring, analysis and UX modules |
| Web/backend/src/main/resources | Properties and packaged Liquibase changelogs |
| database/ | Data model separated by domain and migration files |
| MOBILE/.../src | Navigation, screens, services, BLE, storage and mobile styles |
| iot/esp32/ | Diagnostic programs and DHT11/KY-038 tests |

## 7. Backend Technical Modules

| **Module** | **Responsibilities** | **Highlighted elements** |
| --- | --- | --- |
| identity | Registration, login, JWT, recovery and profile | AuthController, ProfileController, Usuario, Rol |
| classrooms | Campuses, types and educational environments | CampusController, EnvironmentTypeController, EducationalEnvironmentController |
| sensors | Devices, sensors, variables and ingestion | DeviceController, SensorController, IngestController |
| monitoring | Measurements, dashboard, thresholds, alerts and notifications | DashboardController, NotificationController, Measurement services |
| analysis | Environmental analysis by period | AnalysisController, EnvironmentalAnalysis |
| ux | Favorites, preferences and notification states | FavoritesController, PreferencesController |

## 8. Base de datos

La base de datos utiliza PostgreSQL 15 y cuatro dominios principales. Hibernate está configurado con `ddl-auto=validate`; por tanto, Hibernate valida el esquema y Liquibase es la fuente de verdad para crearlo y evolucionarlo.

| **Schema** | **Contenido** |
| --- | --- |
| segurity | user, credential, role, permission, sessions, recovery, 2FA and audit |
| classrooms | campuses, environment_types, educational_environment |
| sensors | sensor, sensor_installation, variable, sensor_variable and related catalogs |
| monitoring | severity, variable_threshold, environment_measurement, environmental_analysis, analysis_result, environment_alert |
| ux | user_preferences, favorites, classroom_ratings and searches |

Los archivos de cambios se encuentran en `Web/backend/src/main/resources/db/changelog` y también en `database/`. El contexto `dev` puede cargar entornos, sensores, mediciones y alertas de ejemplo; un contexto vacío deja únicamente el esquema y los datos semilla de referencia.

## 9. API and Endpoints

La API principal está disponible en `http://localhost:8080`. Las operaciones de usuario utilizan JWT; la ingesta de dispositivos utiliza `X-API-Key`. Los errores se devuelven en JSON con los campos `status` y `message`.

| **Method** | **Ruta** | **Purpose** |
| --- | --- | --- |
| POST | /auth/login | Authenticate user and return JWT |
| POST | /auth/register | Register user |
| POST | /auth/forgot-password | Request password recovery |
| GET/PUT | /api/v1/profile | Retrieve or update profile |
| GET/POST/PATCH/DELETE | /api/v1/environments | Retrieve and manage environments |
| GET/POST/PATCH/DELETE | /api/v1/sensors | Retrieve and manage sensors |
| GET/POST/PUT/DELETE | /api/v1/devices | Manage IoT devices |
| POST | /ingest/v1/measurements | Receive device readings using X-API-Key |
| GET | /api/v1/dashboard/summary | Dashboard summary |
| GET | /api/v1/dashboard/series | Historical series |
| GET | /api/v1/notifications | Retrieve notifications |
| GET/POST/DELETE | /api/v1/favorites | Manage favorites |
| GET | /health | Check backend status |

## 10. Seguridad

- JWT para autenticar usuarios y controlar la expiración.
- Roles y rutas protegidas para separar las operaciones de usuarios y administradores.
- Inicio de sesión social (Google, Facebook) mediante el flujo OAuth2 Authorization Code.
- Súper administrador: gestión de usuarios, roles e instituciones.
- Asignación del rol ADMIN a los administradores de las empresas.
- `X-API-Key` device key for IoT ingestion; it is shown only once when the device is created or rotated.
- Variables sensibles mediante el entorno: `JWT_SECRET`, `POSTGRES_PASSWORD`, `SMTP_PASSWORD` and administrator credentials.
- CORS configurable through `CORS_ALLOWED_ORIGINS`.
- No almacene contraseñas, tokens ni claves reales en el repositorio ni en los manuales.

## 11. Web Frontend and Mobile Aplicación

The web frontend uses React Router, HTTP services, forms, Zod/React Hook Form validations, i18next internationalization, Recharts charts and local preferences. The mobile application uses Expo, React Navigation, AsyncStorage, React Native BLE PLX, charts and the same authentication and query services.

| **Aplicación** | **Main modules** |
| --- | --- |
| Web | Landing page, authentication, dashboard, environments, management, favorites, profile, settings, notifications, accessibility and languages |
| Mobile | Authentication, dashboard, environments, favorites, notifications, profile, settings, device management and BLE provisioning |

## 12. Hardware and IoT Integration

![captura de pantalla](../images/image-7.png)

| **Componente** | **Documented function / connection** |
| --- | --- |
| ESP32 | Controller and Wi-Fi communication |
| DHT11 | Air temperature and humidity; tests with DATA on GPIO 5 |
| KY-038 | Relative sound level; AO output on GPIO 34 in tests |
| MQ-135 | Gases and air quality; requires voltage adaptation before the ADC |
| Breadboard and jumpers | Prototype assembly |
| Battery | Portable power supply, voltage not yet confirmed |

> **Technical warning:** the MQ-135 must not be presented as a precision CO₂ meter, and its analog output must not be connected to an ESP32 GPIO without verifying the voltage level. The delivered files contain DHT11 and KY-038 tests at 115200 baud; no final firmware that sends readings to the API was found.

## 13. Mantenimiento

- Mantenga sincronizados el frontend, el backend, la aplicación móvil, los archivos de cambios y la documentación.
- Actualice las dependencias tras revisarlas y probarlas; no modifique versiones críticas sin validar Docker y Testcontainers.
- Haga una copia de seguridad del volumen `postgres_data` antes de cambiar el esquema.
- Utilice Liquibase en lugar de modificar tablas manualmente.
- Rote `JWT_SECRET` y las claves de los dispositivos en entornos reales.
- Ejecute `npm run lint`, `npm run build` y `mvn test` antes de entregar una versión.
- Documente los cambios en el firmware, los pines, el protocolo y la frecuencia de envío de datos IoT.

## 14. Glosario

| **Término** | **Definición** |
| --- | --- |
| API | Interface that enables communication between applications. |
| ESP32 | Microcontroller with Wi-Fi/Bluetooth connectivity. |
| JWT | Signed token used to authenticate user requests. |
| Liquibase | Tool for versioning database changes. |
| MQ-135 | Gas and relative air-quality sensor; requires calibration. |
| MQTT | IoT messaging protocol considered in the architecture, not confirmed in the firmware. |
| X-API-Key | Authentication key used by IoT devices. |
| BLE | Bluetooth Low Energy, used by the mobile app to provision the ESP32. |

---

*SENA Industria · Ficha 3145556*
