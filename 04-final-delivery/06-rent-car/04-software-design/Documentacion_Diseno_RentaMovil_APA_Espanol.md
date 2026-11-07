# Documentación de diseño del software RentaMovil

Nicole Dayana Ramirez Vargas · Thiago Fabian Rojas Guevara · David Santiago Erez Rodriguez  
Servicio Nacional de Aprendizaje (SENA)  
Programa de formación: Análisis y Desarrollo de Software  
Asignatura o ficha: 3145556  
Fecha de entrega: 15 de octubre de 2026 · Fecha de elaboración: 15 de octubre de 2026

## Resumen

Este documento describe el diseño de RentaMovil y relaciona sus interfaces, requisitos, arquitectura de microservicios, patrones documentados y modelo relacional en SQL Server. Presenta el proceso de alquiler, el pago vigente por transferencia bancaria con comprobante y aprobación administrativa, las decisiones de seguridad y el diccionario de datos. Se dirige a docentes, evaluadores y al equipo de desarrollo.

Palabras clave: RentaMovil, diseño de software, microservicios, arquitectura hexagonal, interfaces, SQL Server, modelo de datos.

## Tabla de contenido

- [Resumen](#resumen)
- [1. Introducción](#1-introducción)
  - [1.1 Propósito, audiencia y alcance](#11-propósito-audiencia-y-alcance)
  - [1.2 Objetivos](#12-objetivos)
  - [1.3 Marco conceptual](#13-marco-conceptual)
  - [1.4 Metodología de elaboración](#14-metodología-de-elaboración)
- [2. Actores y requisitos](#2-actores-y-requisitos)
  - [2.1 Requisitos y reglas de negocio](#21-requisitos-y-reglas-de-negocio)
  - [2.2 Requisitos no funcionales](#22-requisitos-no-funcionales)
- [3. Interfaces gráficas](#3-interfaces-gráficas)
- [4. Modelo arquitectónico](#4-modelo-arquitectónico)
  - [4.1 Tecnologías](#41-tecnologías)
  - [4.2 Estado de implementación](#42-estado-de-implementación)
- [5. Patrones de diseño](#5-patrones-de-diseño)
- [6. Modelo de datos](#6-modelo-de-datos)
  - [6.1 Protección de datos](#61-protección-de-datos)
- [7. Seguridad, calidad y operación](#7-seguridad-calidad-y-operación)
- [8. Conclusiones](#8-conclusiones)
- [Referencias](#referencias)
- [Apéndice A. Diccionario de datos](#apéndice-a-diccionario-de-datos)
- [Apéndice B. Interfaces gráficas](#apéndice-b-interfaces-gráficas)
- [Apéndice C. Inventario documental](#apéndice-c-inventario-documental)
- [Apéndice D. Diagramas de apoyo](#apéndice-d-diagramas-de-apoyo)

## 1. Introducción

RentaMovil digitaliza el ciclo de alquiler: búsqueda de disponibilidad, reserva, transferencia y revisión, administración de flota, alquiler físico, facturación, notificaciones y GPS. El documento consolida artefactos fuente y no los reemplaza (RentaMovil, 2026, 01-context/overview.md; 03-product/vision.md).

### 1.1 Propósito, audiencia y alcance

Dirigido a docentes, evaluadores y al equipo de desarrollo. Cubre la web y móvil del cliente y la web administrativa: autenticación, catálogo, reservas, seguro, transferencia, facturas, flota, sucursales, mantenimiento, ejecución del alquiler, GPS, notificaciones y roles. Pasarela automática y aplicación administrativa móvil constan como evolución futura (RentaMovil, 2026, 01-context/scope.md; 03-product/vision.md).

### 1.2 Objetivos

Objetivo general: documentar el diseño de RentaMovil con trazabilidad entre interfaces, arquitectura, patrones y modelo de datos. Objetivos específicos: describir actores y requisitos; explicar aplicaciones y servicios; resumir entidades SQL Server; registrar decisiones relevantes para evaluación.

### 1.3 Marco conceptual

El diseño utiliza dominios delimitados, microservicios desplegables, arquitectura hexagonal, APIs REST/eventos y modelo relacional normalizado. La interfaz aplica navegación por rol y un sistema visual compartido (RentaMovil, 2026, 02-domain/domain-map.md; 05-architecture/hexagonal-architecture.md; 06-data/normalization-assessment.md).

### 1.4 Metodología de elaboración

Se revisaron contexto, alcance, requisitos, arquitectura, ADR, modelos, catálogo y artefactos UX; se contrastaron con decisiones del equipo y se sintetizaron con citas internas. Las correcciones del equipo sobre SQL Server, transferencias y fecha inmutable prevalecen; se señalan estados contradictorios. No se auditó código ejecutable ni se midió producción.

## 2. Actores y requisitos

| Actor | Responsabilidades |
| --- | --- |
| Cliente | Busca disponibilidad, reserva, paga por transferencia, consulta reservas/facturas/notificaciones. |
| Administrador | Administra flota, sucursales y mantenimiento; revisa pagos; registra recogida/devolución; consulta GPS autorizado. |
| Superadministrador | Hereda permisos administrativos; gestiona usuarios, roles y cuentas bancarias. |

### 2.1 Requisitos y reglas de negocio

El backlog contiene 31 historias y siete épicas: identidad, flota, reserva, ejecución del alquiler, pago/facturación, GPS y notificaciones (RentaMovil, 2026, 04-requirements/user-stories.md).

| Regla | Descripción |
| --- | --- |
| Reserva | Una reserva vincula cliente, vehículo, fechas, sucursales y como máximo un seguro opcional. |
| Devolución | Por instrucción del equipo, la fecha de devolución no puede cambiar tras crear la reserva. Esto prevalece sobre versiones anteriores del diseño, que permitían extensión. |
| Transferencia | Cliente selecciona cuenta, registra monto/referencia y carga comprobante; administrador aprueba/rechaza, fecha y motivo. |
| Confirmación | La reserva se confirma tras aprobación del pago. |
| Privacidad | No incluir valores reales de contraseñas, identificaciones, tokens o datos bancarios. |

### 2.2 Requisitos no funcionales

Las fuentes cubren rendimiento, disponibilidad, escalabilidad, seguridad, observabilidad, mantenibilidad, portabilidad y recuperación ante desastres. Las metas numéricas deben venir de la matriz NFR; no se declaran mediciones verificadas. Incluye JWT, permisos, auditoría, protección de secretos, transporte seguro e idempotencia (RentaMovil, 2026, 04-requirements/non-functional.md; 00-governance/security-policy.md).

## 3. Interfaces gráficas

El manual visual aprobado define marca, paleta, Poppins/Inter, jerarquía, espaciado, componentes, estados, iconos, formularios y carga. Las interfaces están agrupadas por público, cliente, administrador y superadministrador. La navegación contiene 21 rutas documentadas; hay vistas compartidas por rol (Equipo RentaMovil, 2026, Identidad_visual.pdf; RentaMovil, 2026, 12-ux-ui/navigation-map.md; screen-descriptions.md).

| Grupo | Pantallas |
| --- | --- |
| Públicas | Inicio; catálogo /vehicles; detalle del vehículo /vehicles/:id; inicio de sesión, registro y recuperación de cuenta /auth/*. |
| Cliente | Perfil, reservas, asistente de reserva, pago, detalle de reserva y bandeja de notificaciones. |
| Administración | Flota, creación y edición de vehículos, sucursales, reservas, seguros y seguimiento. |
| Superadministración | Usuarios y roles, y cuentas bancarias. |

El catálogo requiere fecha o sucursal; las acciones del detalle dependen de permisos. El asistente reúne selección de vehículo/fechas, seguro, términos y proceso de transferencia. Los estados de carga, error, vacío y confirmación siguen la identidad visual.

## 4. Modelo arquitectónico

Arquitectura cliente-servidor: React/Vite web y Expo/React Native móvil consumen REST por API Gateway. Backend Java/Spring Boot organizado en microservicios desplegables; REST síncrono y eventos asíncronos. Siete contextos lógicos se agrupan en cinco servicios físicos más gateway; booking-reservation incluye ejecución del alquiler y notificaciones. Una base SQL Server compartida usa esquemas y propiedad de escritura por servicio (RentaMovil, 2026, 05-architecture/overview.md; 09-microservices/service-catalog.md; 06-data/README.md).

| Servicio | Responsabilidad |
| --- | --- |
| api-gateway | Enrutamiento, autenticación, limitación de tráfico. |
| iam | Registro, JWT, roles, permisos, sesiones. |
| fleet-maintenance | Vehículos, sucursales, disponibilidad y mantenimiento. |
| booking-reservation | Reservas, alquiler físico y notificaciones. |
| payment-billing | Cuentas, transferencias, revisión y facturas. |
| telemetry-gps | Dispositivos GPS y recorrido. |

**Figura 24. Arquitectura de microservicios de RentaMovil**

![Figura 24. Arquitectura de microservicios de RentaMovil](imagenes/figura_24.png)

*Fuente: elaboración del equipo RentaMovil, 2026, a partir de los repositorios del proyecto. Las líneas punteadas indican comunicación entre servicios; el servicio telemetry-gps aparece como planificado.*

**Figura 25. Diagrama de despliegue con Docker (contenedores, puertos y red)**

![Figura 25. Diagrama de despliegue con Docker (contenedores, puertos y red)](imagenes/figura_25.png)

*Fuente: elaboración del equipo RentaMovil, 2026, a partir del código y los archivos docker-compose de los repositorios del proyecto. Las líneas punteadas entre servicios corresponden a las variables IAM_URL, FLEET_URL y BOOKING_URL; el contenedor mock-api simula funcionalidades sin servicio real.*

### 4.1 Tecnologías

| Capa | Tecnología |
| --- | --- |
| Web | React, Vite y JavaScript (JSX), con algunos módulos en TypeScript; módulos por funcionalidad, rutas y componentes. |
| Móvil | Expo, React Native, TypeScript, Expo Router, Zustand. |
| Backend | Java, Spring Boot, arquitectura hexagonal. |
| Datos | Microsoft SQL Server, esquemas/migraciones por servicio. |
| Infraestructura | Docker y entornos descritos por DevOps. |

### 4.2 Estado de implementación

A 15 de octubre de 2026, los repositorios del equipo contienen código y pruebas automatizadas para cinco de los seis servicios del catálogo: api-gateway, iam, fleet-maintenance, booking-reservation y payment-billing. Cada servicio con base de datos cuenta con su repositorio de migraciones (iam-db, fleet-maintenance-db, booking-db y payment-billing-db). Los clientes web y móvil están en el repositorio rtm-frontend. El servicio telemetry-gps aún no tiene repositorio y se mantiene como planificado.

Esta descripción se basa en la estructura de los repositorios y no constituye una auditoría funcional del código. Las fuentes de planeación (catálogo de servicios y backlog) se elaboraron antes y su estado no coincide con el actual.

En la aplicación web, algunas funcionalidades (cuentas bancarias, sucursales, seguros y reservas en ciertas pantallas) aún consumen datos simulados del contenedor mock-api, que aparece en la Figura 25; no deben entenderse como integradas con el backend real.

## 5. Patrones de diseño

| Patrón | Aplicación | Estado |
| --- | --- | --- |
| Hexagonal | Dominio aislado mediante puertos/adaptadores. | Adoptado en backend. |
| Feature-based | Módulos de cliente por funcionalidad/dominio. | Documentado. |
| Component-driven | Componentes de UI reutilizables. | Documentado para móvil. |
| API Gateway | Entrada externa de servicios. | Gateway listado activo. |
| Schema per service | Propiedad de tablas por esquema en base compartida. | ADR-009. |
| Eventos de dominio | Cambios entre dominios e idempotencia. | Eventos definidos; transporte sujeto a configuración. |
| Saga/Outbox/Circuit breaker | Consistencia distribuida y resiliencia. | Evaluar, no afirmar implementación. |

**Figura 26. Arquitectura hexagonal del servicio iam (dominio, puertos y adaptadores)**

![Figura 26. Arquitectura hexagonal del servicio iam (dominio, puertos y adaptadores)](imagenes/figura_26.png)

*Fuente: elaboración del equipo RentaMovil, 2026, a partir de la estructura de paquetes del repositorio rtm-iam. Los demás servicios siguen la misma organización (domain, application, infrastructure).*

**Figura 27. Diagrama de clases del dominio por servicio**

![Figura 27. Diagrama de clases del dominio por servicio](imagenes/figura_27.png)

*Fuente: elaboración del equipo RentaMovil, 2026, a partir de las clases de domain/entity y domain/enums de cada servicio. Las líneas rojas punteadas son referencias por identificador entre servicios, no llaves foráneas.*

No se atribuyen Factory, Strategy, Observer o CQRS porque las fuentes no confirman su uso. Un patrón propuesto no equivale a implementado (RentaMovil, 2026, 05-architecture/overview.md; hexagonal-architecture.md).

## 6. Modelo de datos

El modelo físico contiene 26 tablas de dominio en siete contextos más audit transversal. Convenciones: snake_case, PK entero IDENTITY, FK relacional y CHECK para estados enumerados. Esquemas: iam, fleet, booking, payment, telemetry y shared. El apéndice A transcribe el diccionario fuente (RentaMovil, 2026, 06-data/models.md; data-dictionary.md; normalization-assessment.md).

Las migraciones versionadas en los repositorios de base de datos implementan 24 tablas (iam 7, fleet 9, booking 4, payment 3 y shared 1); las dos tablas restantes (gps y location) pertenecen al esquema telemetry, que aún no tiene migraciones.

| Contexto | Modelo conceptual |
| --- | --- |
| iam | persona-usuario 1:1; usuario-rol N:1; rol-permiso N:M; sesiones y códigos de verificación 1:N por usuario. |
| fleet | sucursal-horarios; marca-modelo-vehículo; vehículo-mantenimiento. |
| booking | la reserva relaciona persona, vehículo, sucursales y seguro opcional; reserva-alquiler 1:1. |
| payment | cuenta bancaria-pago 1:N; reserva-intentos de pago 1:N; factura-reserva 1:1. |
| telemetry | GPS-ubicación 1:N; el alquiler asigna un GPS. |
| shared | auditoría: registra la trazabilidad de las acciones. |

**Figura 28. Modelo de datos físico implementado (esquemas iam, fleet, booking, payment y shared)**

![Figura 28. Modelo de datos físico implementado](imagenes/figura_28.png)

*Fuente: elaboración del equipo RentaMovil, 2026, generada a partir de las migraciones Liquibase de los repositorios rtm-iam-db, rtm-fleet-maintenance-db, rtm-booking-db y rtm-payment-billing-db. Solo se dibujan las llaves foráneas declaradas dentro de cada base; las relaciones entre esquemas (por ejemplo, reserva-vehículo o reserva-sucursal) se resuelven en la capa de servicios.*

### 6.1 Protección de datos

password_hash es un hash bcrypt; nunca se muestra una contraseña. Solo aparecen nombres de campos técnicos; no valores reales de IDs, secretos, personas o bancos. Comprobantes y facturas son referencias de almacenamiento, no adjuntos (RentaMovil, 2026, 06-data/models.md; 00-governance/security-rules.md).

## 7. Seguridad, calidad y operación

La documentación describe JWT RS256, RBAC, permisos, validación, auditoría, secretos seguros y transporte protegido. Calidad aborda pruebas por capas y trazabilidad; DevOps, contenedores y entornos; operaciones, observabilidad e incidentes. Sin resultados ejecutables, se reporta diseño, no métricas ni certificaciones (RentaMovil, 2026, security-policy.md; 11-quality/testing-strategy.md; 10-devops/README.md; 13-operations/observability.md).

## 8. Conclusiones

El diseño integra interfaces aprobadas, microservicios, flujo de transferencia revisable y SQL Server compartido. Se adoptan las decisiones del equipo sobre base, aprobación manual y fecha inmutable. El servicio telemetry-gps queda como trabajo futuro, y las fuentes de planeación deben actualizarse con la fecha de devolución definida.

## Referencias

RentaMovil. (2026). 01-context/overview.md y scope.md [Documentación interna]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 02-domain/domain-map.md y entities-and-rules.md [Modelo de dominio]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 03-product/vision.md [Visión del producto]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 04-requirements/user-stories.md y non-functional.md [Requisitos]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 05-architecture/overview.md y hexagonal-architecture.md [Arquitectura]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). ADR-009-schema-per-service-and-migration-repos.md [Registro de decisión arquitectónica]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 06-data/models.md y data-dictionary.md [Modelo y diccionario de datos]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 09-microservices/service-catalog.md [Catálogo de servicios]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 12-ux-ui/navigation-map.md y screen-descriptions.md [Interfaces]. Repositorio de documentación RentaMovil.

Equipo RentaMovil. (2026). Identidad_visual.pdf [Manual de identidad visual aprobado]. Carpeta Documento de diseño.

RentaMovil. (2026). 00-governance/security-policy.md y security-rules.md [Políticas de seguridad]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 10-devops/README.md; 11-quality/testing-strategy.md; 13-operations/observability.md [Operación y calidad]. Repositorio de documentación RentaMovil.

RentaMovil. (2026). 12-ux-ui/design-system.md [Sistema visual]. Repositorio de documentación RentaMovil.

## Apéndice A. Diccionario de datos

Campos y restricciones transcritos del modelo de datos y actualizados con las migraciones de los repositorios de base de datos; no se incluyen datos de registros.

#### person — Identidad y acceso

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| person_id | int | No | Identificador único | PK, IDENTITY |
| first_name | varchar(100) | No |  |  |
| last_name | varchar(100) | No |  |  |
| email | varchar(150) | No |  | Unique (`uq_person_email`): lo garantiza la base de datos y no solo la aplicación; dos registros simultáneos con el mismo correo no pueden tener éxito |
| phone | varchar(20) | Yes |  |  |
| image_url | varchar(500) | Yes | URL de la foto de perfil; la imagen se guarda en Cloudinary y aquí solo el enlace |  |

#### user — Identidad y acceso

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| user_id | int | No |  | PK, IDENTITY |
| person_id | int | No | Relación 1:1 con `person` | FK → `person.person_id`, Unique |
| role_id | int | No | Clave foránea directa; cada usuario tiene exactamente un rol (no N:M) | FK → `role.role_id` |
| username | varchar(50) | No |  | Unique |
| password_hash | varchar(255) | No | Hash bcrypt, costo ≥ 12 |  |
| status | varchar(20) | No | Estado de cuenta: `ACTIVE`, `INACTIVE` o `BLOCKED` | `CHECK` |
| last_login | datetime | Yes |  |  |

#### role — Identidad y acceso

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| role_id | int | No |  | PK, IDENTITY |
| name | varchar(50) | No | `CLIENT`, `ADMIN` o `SUPER_ADMIN` | Unique |
| description | varchar(255) | Yes |  |  |

#### permission — Identidad y acceso

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| permission_id | int | No |  | PK, IDENTITY |
| name | varchar(50) | No | Por ejemplo, `reservations:create` | Unique |
| description | varchar(255) | Yes |  |  |

#### role_permission — Identidad y acceso

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| role_id | int | No |  | FK → `role.role_id`, parte de la PK compuesta |
| permission_id | int | No |  | FK → `permission.permission_id`, parte de la PK compuesta |

#### verification_code — Identidad y acceso

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| verification_code_id | int | No |  | PK, IDENTITY |
| user_id | int | No |  | FK → `user.user_id` |
| code | varchar(10) | No |  |  |
| type | varchar(30) | No | Por ejemplo, `EMAIL_VERIFICATION` y `PASSWORD_RESET` |  |
| created_at | datetime | No |  |  |
| expires_at | datetime | No |  |  |
| used | boolean | No |  |  |

#### session — Identidad y acceso

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| session_id | int | No |  | PK, IDENTITY |
| user_id | int | No |  | FK → `user.user_id` |
| refresh_token | varchar(255) | No | Consulte `00-governance/security-rules.md` para la política de rotación |  |
| created_at | datetime | No |  |  |
| expires_at | datetime | No | 7 días desde su creación |  |
| ip_address | varchar(45) | Yes |  |  |
| user_agent | varchar(255) | Yes |  |  |
| revoked | boolean | No |  |  |

#### branch — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| branch_id | int | No |  | PK, IDENTITY |
| name | varchar(100) | No |  |  |
| address | varchar(200) | No |  |  |
| city | varchar(100) | No |  |  |
| phone | varchar(20) | Yes |  |  |
| latitude | decimal(9,6) | Yes | Opcional; se define junto con `longitude` (ambas o ninguna) | `CHECK` entre -90 y 90 |
| longitude | decimal(9,6) | Yes | Opcional; se define junto con `latitude` | `CHECK` entre -180 y 180 |

#### branch_operating_hour — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| branch_operating_hour_id | int | No |  | PK, IDENTITY |
| branch_id | int | No |  | FK → `branch.branch_id` |
| day_of_week | smallint | No | 1–7 (lunes a domingo) | `CHECK (day_of_week BETWEEN 1 AND 7)` |
| opens_at | time | No |  |  |
| closes_at | time | No |  |  |

#### brand — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| brand_id | int | No |  | PK, IDENTITY |
| name | varchar(50) | No |  | Unique |

#### vehicle_model — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| model_id | int | No |  | PK, IDENTITY |
| brand_id | int | No |  | FK → `brand.brand_id` |
| name | varchar(50) | No |  |  |
| category_id | int | No |  | FK → `category.category_id` |
| engine_type_id | int | No |  | FK → `engine_type.engine_type_id` |

#### category — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| category_id | int | No | Por ejemplo, económico, SUV o lujo | PK, IDENTITY |
| name | varchar(50) | No |  |  |
| description | varchar(255) | Yes |  |  |

#### engine_type — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| engine_type_id | int | No | Por ejemplo, gasolina, diésel, eléctrico o híbrido | PK, IDENTITY |
| name | varchar(50) | No |  |  |

#### vehicle — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| vehicle_id | int | No |  | PK, IDENTITY |
| plate | varchar(10) | No |  | Unique |
| model_id | int | No | La categoría y el tipo de motor se consultan mediante el modelo; no se almacenan aquí | FK → `vehicle_model.model_id` |
| capacity | int | Yes | Número de pasajeros |  |
| year | int | Yes |  |  |
| image_url | varchar(255) | Yes |  |  |
| status | varchar(30) | No | `AVAILABLE`, `RENTED`, `MAINTENANCE`, o `RETIRED` | `CHECK` |
| mileage | decimal(10,2) | No |  |  |
| daily_price | decimal(10,2) | No |  |  |
| branch_id | int | No | Sucursal actual del vehículo | FK → `branch.branch_id` |

#### vehicle_maintenance — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| maintenance_id | int | No |  | PK, IDENTITY |
| vehicle_id | int | No |  | FK → `vehicle.vehicle_id` |
| maintenance_type_id | int | No |  | FK → `maintenance_type.maintenance_type_id` |
| start_date | date | No |  |  |
| end_date | date | Yes | Nulo mientras está en curso |  |
| cost | decimal(10,2) | Yes |  |  |
| observations | varchar(500) | Yes | Observaciones del mantenimiento |  |
| image_url | varchar(255) | Yes | URL de la imagen asociada al mantenimiento |  |
| status | varchar(30) | No | `SCHEDULED`, `IN_PROGRESS`, `COMPLETED` o `CANCELLED` (cancelar reemplaza la eliminación del registro) | `CHECK` |

#### maintenance_type — Flota y mantenimiento

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| maintenance_type_id | int | No | Por ejemplo, cambio de aceite, llantas o inspección | PK, IDENTITY |
| name | varchar(50) | No |  |  |
| description | varchar(255) | Yes |  |  |

#### insurance_type — Reservas

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| insurance_type_id | int | No |  | PK, IDENTITY |
| name | varchar(100) | No |  |  |
| coverage_details | text | Yes |  |  |
| daily_cost | decimal(10,2) | No |  |  |

#### reservation — Reservas

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| reservation_id | int | No |  | PK, IDENTITY |
| client_id | int | No |  | FK → `person.person_id` |
| vehicle_id | int | No |  | FK → `vehicle.vehicle_id` |
| insurance_type_id | int | Yes | El seguro es opcional | FK → `insurance_type.insurance_type_id` |
| pickup_branch_id | int | No |  | FK → `branch.branch_id` |
| return_branch_id | int | No | La sucursal de devolución puede cambiarse hasta 3 días antes de `end_date` (INV-013); no modifica la fecha de devolución | FK → `branch.branch_id` |
| reservation_date | datetime | No | Fecha de creación de la reserva |  |
| start_date | datetime | No |  |  |
| end_date | datetime | No |  |  |
| vehicle_subtotal | decimal(10,2) | No |  |  |
| insurance_subtotal | decimal(10,2) | No | 0 si no se selecciona seguro |  |
| total_amount | decimal(10,2) | No |  |  |
| status | varchar(30) | No | `PENDING_PAYMENT`, `PENDING_REVIEW`, `CONFIRMED`, `CANCELLED`, o `COMPLETED` | `CHECK` |

#### rental — Ejecución del alquiler

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| rental_id | int | No |  | PK, IDENTITY |
| reservation_id | int | No | Relación 1:1 con `reservation`; se crea cuando un administrador registra la recogida (RF4.1) | FK → `reservation.reservation_id`, Unique |
| gps_id | int | No |  | FK → `gps.gps_id` |
| actual_start_date | datetime | No |  |  |
| actual_end_date | datetime | Yes | Nulo mientras `status = IN_PROGRESS` |  |
| initial_mileage | decimal(10,2) | No |  |  |
| final_mileage | decimal(10,2) | Yes | Se establece cuando el administrador registra la devolución (RF4.2) |  |
| status | varchar(30) | No | `IN_PROGRESS` o `COMPLETED` | `CHECK` |

#### bank_account — Pagos y facturación

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| bank_account_id | int | No |  | PK, IDENTITY |
| bank_name | varchar(100) | No | Por ejemplo, Bancolombia o Nequi |  |
| account_holder | varchar(150) | No | Nombre del titular de la cuenta (la empresa) |  |
| qr_image_url | varchar(255) | No | Imagen QR mostrada al cliente durante el pago; almacenamiento S3/MinIO |  |
| is_active | boolean | No | Las cuentas inactivas se ocultan durante el pago, pero conservan su historial |  |

#### payment — Pagos y facturación

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| payment_id | int | No |  | PK, IDENTITY |
| reservation_id | int | No |  | FK → `reservation.reservation_id` |
| bank_account_id | int | No | Cuenta bancaria a la que transfirió el cliente | FK → `bank_account.bank_account_id` |
| payment_date | datetime | No | Fecha en que el cliente registró la transferencia |  |
| amount | decimal(10,2) | No | Debe ser exactamente igual a `reservation.total_amount`; consulte INV-001 |  |
| reference_number | varchar(100) | Yes | Referencia de transacción informada por el cliente |  |
| receipt_file_url | varchar(255) | No | Archivo comprobante cargado por el cliente; almacenamiento S3/MinIO |  |
| status | varchar(30) | No | `PENDING_REVIEW`, `APPROVED`, o `REJECTED` | `CHECK` |
| reviewed_by | int | Yes | Administrador que lo revisó; nulo mientras `status = PENDING_REVIEW` | FK → `user.user_id` |
| reviewed_at | datetime | Yes | Nulo mientras `status = PENDING_REVIEW` |  |
| rejection_reason | varchar(255) | Yes | Nota del administrador; se establece solo cuando `status = REJECTED` |  |

#### invoice — Pagos y facturación

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| invoice_id | int | No |  | PK, IDENTITY |
| reservation_id | int | No | Relación 1:1 con `reservation` | FK → `reservation.reservation_id`, Unique |
| invoice_number | varchar(50) | No |  | Unique |
| generated_date | datetime | No |  |  |
| invoice_pdf_url | varchar(255) | Yes | PDF generado una sola vez al crear la factura y guardado en almacenamiento de archivos (S3/MinIO); no se regenera en cada consulta |  |

#### gps — Telemetría y GPS

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| gps_id | int | No | Dispositivo físico instalado por vehículo al iniciar el alquiler | PK, IDENTITY |
| serial | varchar(100) | No |  | Unique |
| model | varchar(50) | Yes |  |  |
| is_active | boolean | No |  |  |

#### location — Telemetría y GPS

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| location_id | int | No |  | PK, IDENTITY |
| gps_id | int | No |  | FK → `gps.gps_id` |
| latitude | decimal(10,8) | No |  |  |
| longitude | decimal(11,8) | No |  |  |
| timestamp | datetime | No |  |  |

#### notification — Notificaciones

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| notification_id | int | No |  | PK, IDENTITY |
| person_id | int | No | Destinatario | FK → `person.person_id` |
| notification_type | varchar(50) | No | Por ejemplo, `RESERVATION_CREATED`, `PAYMENT_APPROVED` y `PAYMENT_REJECTED`; uno por cada evento de dominio que genera una notificación | `CHECK` |
| message | text | No |  |  |
| sent_date | datetime | No |  |  |
| is_read | boolean | No |  |  |
| reservation_id | int | Yes | Se establece cuando la notificación corresponde a una reserva | FK → `reservation.reservation_id` |
| invoice_id | int | Yes | Se establece cuando la notificación corresponde a una factura | FK → `invoice.invoice_id` |
| payment_id | int | Yes | Se establece cuando la notificación corresponde a un pago | FK → `payment.payment_id` |
| maintenance_id | int | Yes | Se establece cuando la notificación corresponde a un mantenimiento | FK → `vehicle_maintenance.maintenance_id` |

#### audit — Transversal (auditoría)

| Campo | Tipo | Nulo | Descripción | Restricciones |
| --- | --- | --- | --- | --- |
| audit_id | int | No |  | PK, IDENTITY |
| user_id | int | Yes | Usuario que realizó la acción | FK → `user.user_id` |
| action | varchar(50) | No | Por ejemplo, `CREATE`, `UPDATE`, `DELETE`, `LOGIN` |  |
| affected_table | varchar(50) | No | Nombre de la tabla afectada |  |
| affected_record_id | int | Yes | Clave primaria del registro afectado en `affected_table` |  |
| date | datetime | No |  |  |
| detail | nvarchar(max) | Yes | JSON adaptable; su estructura depende de `action` y `affected_table`, no de un conjunto fijo de columnas | Validar con `ISJSON()` |

## Apéndice B. Interfaces gráficas

Se incluyen las 23 imágenes de interfaz disponibles y las 21 rutas documentadas, con algunas vistas compartidas.

El inventario complementario identifica archivos PDF de diseño disponibles en el paquete fuente.

**Figura 1. Administración de sucursales**

![Figura 1. Administración de sucursales](imagenes/figura_01.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 2. Edición de vehículo**

![Figura 2. Edición de vehículo](imagenes/figura_02.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 3. Panel de flota**

![Figura 3. Panel de flota](imagenes/figura_03.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 4. Tipos de seguro**

![Figura 4. Tipos de seguro](imagenes/figura_04.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 5. Registro de vehículo**

![Figura 5. Registro de vehículo](imagenes/figura_05.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 6. Detalle de reserva**

![Figura 6. Detalle de reserva](imagenes/figura_06.jpeg)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 7. Panel de reservas**

![Figura 7. Panel de reservas](imagenes/figura_07.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 8. Seguimiento del vehículo**

![Figura 8. Seguimiento del vehículo](imagenes/figura_08.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 9. Notificaciones del cliente**

![Figura 9. Notificaciones del cliente](imagenes/figura_09.jpeg)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 10. Carga de pago**

![Figura 10. Carga de pago](imagenes/figura_10.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 11. Comprobante de transferencia**

![Figura 11. Comprobante de transferencia](imagenes/figura_11.jpeg)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 12. Detalle y factura de reserva**

![Figura 12. Detalle y factura de reserva](imagenes/figura_12.jpeg)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 13. Mis reservas**

![Figura 13. Mis reservas](imagenes/figura_13.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 14. Asistente de reserva**

![Figura 14. Asistente de reserva](imagenes/figura_14.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 15. Inicio de sesión**

![Figura 15. Inicio de sesión](imagenes/figura_15.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 16. Registro de usuario**

![Figura 16. Registro de usuario](imagenes/figura_16.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 17. Catálogo de vehículos**

![Figura 17. Catálogo de vehículos](imagenes/figura_17.jpeg)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 18. Recuperación de contraseña**

![Figura 18. Recuperación de contraseña](imagenes/figura_18.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 19. Inicio**

![Figura 19. Inicio](imagenes/figura_19.jpeg)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 20. Perfil**

![Figura 20. Perfil](imagenes/figura_20.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 21. Detalle del vehículo**

![Figura 21. Detalle del vehículo](imagenes/figura_21.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 22. Cuentas bancarias**

![Figura 22. Cuentas bancarias](imagenes/figura_22.png)

*Fuente: prototipo visual de RentaMovil, 2026.*

**Figura 23. Administración de usuarios y roles**

![Figura 23. Administración de usuarios y roles](imagenes/figura_23.jpeg)

*Fuente: prototipo visual de RentaMovil, 2026.*

## Apéndice C. Inventario documental

Archivos PDF de diseño contenidos en el paquete fuente RentaMovil.zip al elaborar el informe. El paquete original se conserva fuera de este repositorio.

| Archivo | Tipo |
| --- | --- |
| Panel Admin.pdf | Diseño o pantalla PDF |
| Footer.pdf | Diseño o pantalla PDF |
| Nav Bar.pdf | Diseño o pantalla PDF |
| checkStatus.pdf | Diseño o pantalla PDF |
| addVehicle.pdf | Diseño o pantalla PDF |
| inventary.pdf | Diseño o pantalla PDF |
| registerMaintenance.pdf | Diseño o pantalla PDF |
| historyMaintenance.pdf | Diseño o pantalla PDF |
| location.pdf | Diseño o pantalla PDF |
| checkUSer.pdf | Diseño o pantalla PDF |
| notifications.pdf | Diseño o pantalla PDF |
| contractHistory.pdf | Diseño o pantalla PDF |
| count.pdf | Diseño o pantalla PDF |
| page.pdf | Diseño o pantalla PDF |
| Overlay.pdf | Diseño o pantalla PDF |
| reservation.pdf | Diseño o pantalla PDF |
| historyLocation.pdf | Diseño o pantalla PDF |
| NewContract.pdf | Diseño o pantalla PDF |
| P.A V.pdf | Diseño o pantalla PDF |
| P.A U.pdf | Diseño o pantalla PDF |
| P.A M.pdf | Diseño o pantalla PDF |
| P.A C.pdf | Diseño o pantalla PDF |
| Home Admin V.pdf | Diseño o pantalla PDF |
| Overlay-1.pdf | Diseño o pantalla PDF |
| Overlay-2.pdf | Diseño o pantalla PDF |
| Overlay-3.pdf | Diseño o pantalla PDF |

---

## Apéndice D. Diagramas de apoyo

Estos diagramas se elaboraron únicamente con información ya documentada en los repositorios del proyecto; no se añadieron datos que no estuvieran respaldados por una fuente. Fuentes: el documento de análisis `02-documentacion-analisis-software` (diagramas de secuencia SEQ y de estados EST), `rtm-docs/02-domain/domain-events.md`, `rtm-docs/02-domain/entities-and-rules.md`, `rtm-docs/09-microservices/service-catalog.md`, `rtm-docs/07-api/contracts/openapi/` y `rtm-docs/12-ux-ui/navigation-map.md`.

### D.1 Diagrama de secuencia — reserva con pago por transferencia bancaria

Muestra la comunicación entre el API Gateway, `iam`, `booking-reservation`, `fleet-maintenance` y `payment-billing` para crear una reserva y pagarla por transferencia bancaria (carga del comprobante y revisión por parte de un administrador). Las flechas continuas representan llamadas REST síncronas y las discontinuas con punta abierta, eventos de dominio asíncronos.

**Figura 29. Diagrama de secuencia de la reserva con pago por transferencia bancaria.**

![Figura 29. Diagrama de secuencia de la reserva con pago por transferencia bancaria](imagenes/figura_29.png)

*Corresponde a SEQ-03 «Crear una reserva» y SEQ-04 «Pagar una reserva (revisión del comprobante)» del documento de análisis. Los endpoints y los eventos de dominio se tomaron de los contratos OpenAPI y de `domain-events.md`.*

### D.2 Mapa de navegación — 21 rutas

Estructura de rutas del cliente web/móvil y de la interfaz administrativa, agrupada por nivel de acceso. Se listan las 21 pantallas descritas en el mapa de navegación.

**Figura 30. Mapa de navegación de las 21 rutas del sistema.**

![Figura 30. Mapa de navegación de las 21 rutas del sistema](imagenes/figura_30.png)

*Fuente: `rtm-docs/12-ux-ui/navigation-map.md`. Las rutas `/admin/*` requieren el rol ADMIN; `/admin/users` y `/admin/bank-accounts` requieren SUPER_ADMIN. La reserva exige sesión: si no la hay, el flujo redirige a `/auth/login`.*

### D.3 Diagrama de estados — reserva y pago

Ciclos de vida de `Reservation` y `Payment`.

**Figura 31. Diagrama de estados de la reserva (`Reservation.status`).**

![Figura 31. Diagrama de estados de la reserva (`Reservation.status`)](imagenes/figura_31.png)

**Figura 32. Diagrama de estados del pago (`Payment.status`).**

![Figura 32. Diagrama de estados del pago (`Payment.status`)](imagenes/figura_32.png)

*Fuente: `rtm-docs/02-domain/entities-and-rules.md` (INV-004, INV-015, INV-016) y `rtm-docs/02-domain/domain-events.md`. Corresponde a EST-01 «Reservation» y EST-02 «Payment» del documento de análisis.*
