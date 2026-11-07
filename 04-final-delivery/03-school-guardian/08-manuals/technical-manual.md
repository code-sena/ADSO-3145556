# Manual Técnico — Guardián Escolar (School Guardian)

**Programa:** Tecnólogo en Análisis y Desarrollo de Software (ADSO)
**Ficha:** 3145556
**Centro de formación:** Sede La Industria, La Empresa y Los Servicios — SENA (Neiva, Huila, Colombia)
**Autores:** Johan Smith Santamaria Fernández; Sharik Dayanna Rojas Ibarra; Juan Pablo Chala Ramírez
**Versión del documento:** 1.1
**Fecha de revisión:** 27 de octubre de 2026 (versión inicial: 10 de octubre)
**Estado del software documentado:** en desarrollo (arquitectura implementada; funcionalidad parcial)

> **Nota metodológica.** Este manual se elaboró a partir de la lectura directa del código y los artefactos reales de los repositorios independientes del proyecto: `sg-ms-*`, `sg-ms-api-gateway`, `broker-kafka`, `sg-db` y `GuardianEscolar`. Se documenta aquí **lo que el código hace realmente** y se señalan de forma explícita los hallazgos de consistencia detectados. Cuando un dato no pudo verificarse en el repositorio, se indica como **no verificable** en lugar de suponerse. Las rutas de archivo citadas actúan como evidencia.

---

## Control de versiones

| Versión | Fecha | Descripción del cambio | Autor(es) |
|---|---|---|---|
| 1.0 | 10-oct-2026 | Versión inicial del manual técnico (Parte A). Elaborada a partir del análisis directo del código fuente del proyecto. | Equipo del proyecto |
| 1.1 | 27-oct-2026 | Corrección documental acotada de inventario, JWT/autorización, Kong, Kafka y gRPC contra repositorios locales; no certifica despliegue ni auditoría integral. | Revisión técnica |

> **Corte de evidencia (27-oct-2026).** Se contrastaron los repositorios reales `sg-ms-*`, `broker-kafka`, `sg-db` y `GuardianEscolar`, no copias locales divergentes. Las rutas de código que se indican a continuación usan el nombre del repositorio remoto correspondiente. Se cuentan **ocho backend (seis .NET y dos Java) más Kong**; Kafka y SQL Server no son servicios de negocio. Versiones de manifiesto y código/configuración no prueban imágenes activas, tablas instaladas ni entrega de mensajes. No se leyeron secretos ni se consultó producción.

---

## Tabla de contenido

1. [Introducción](#1-introducción)
2. [Descripción general del sistema](#2-descripción-general-del-sistema)
3. [Arquitectura del sistema](#3-arquitectura-del-sistema)
4. [Modelo de datos](#4-modelo-de-datos)
5. [Estructura del código fuente](#5-estructura-del-código-fuente)
6. [Descripción de módulos y componentes](#6-descripción-de-módulos-y-componentes)
7. [Documentación de APIs e integraciones](#7-documentación-de-apis-e-integraciones)
8. [Configuración](#8-configuración)
9. [Seguridad](#9-seguridad)
10. [Referencias](#10-referencias)

---

## 1. Introducción

### 1.1 Propósito y alcance

Este documento es el **manual técnico** de *Guardián Escolar* (nombre en inglés: *School Guardian*), una plataforma de gestión del transporte escolar que integra rastreo GPS en tiempo real, control de abordaje/descenso mediante código QR, administración de rutas y flota, y notificaciones a los acudientes.

El manual describe **cómo está construido y cómo se opera** el sistema: su arquitectura, su modelo de datos, la estructura del código, los servicios que lo componen, las interfaces (API REST, gRPC y mensajería), la configuración y los mecanismos de seguridad. Está dirigido a quien debe **comprender, extender, desplegar o mantener** el software.

**Alcance incluido:**

- Arquitectura de software y justificación del estilo arquitectónico.
- Backend de microservicios (8 servicios + API Gateway; broker de mensajería aparte).
- Modelo de base de datos (motor, esquemas, tablas, relaciones y diccionario de datos).
- Frontend web (Angular), aplicación móvil (React Native/Expo) y servicio de ingesta GPS.
- Contratos de API, configuración por ambiente y controles de seguridad.
- Limitaciones y hallazgos verificados (deuda técnica y divergencias código/documentación).

**Alcance excluido:**

- Contenido de negocio comercial (precios, acuerdos con colegios), cubierto por otros documentos del proyecto.
- Código fuente completo, que se referencia por ruta pero no se reproduce íntegro.
- Procedimientos administrativos internos del SENA.

### 1.2 Audiencia

| Audiencia | Uso principal del documento |
|---|---|
| Desarrolladores de backend | Crear nuevos casos de uso, adaptadores y migraciones respetando la arquitectura hexagonal y los contratos. |
| Desarrolladores de frontend (web/móvil) | Consumir las APIs correctamente, conocer autenticación, códigos de error y modelos. |
| Arquitectos de software | Evaluar el estilo arquitectónico, los patrones, la integración entre servicios y las decisiones tomadas. |
| DevOps / soporte técnico | Desplegar con Docker/Compose, configurar variables de entorno, diagnosticar la red de servicios. |
| Administradores de plataforma | Comprender roles, permisos, seguridad y operación general. |
| Evaluadores académicos | Verificar coherencia entre requisitos, diseño, implementación y pruebas. |

### 1.3 Definiciones, acrónimos y abreviaturas

| Término | Definición |
|---|---|
| **ADSO** | Análisis y Desarrollo de Software (programa de formación del SENA). |
| **API Gateway** | Punto de entrada único que enruta el tráfico hacia los microservicios (aquí, Kong). |
| **ACPM** | Acciones Correctivas, Preventivas y de Mejora. |
| **Adaptador (Adapter)** | Componente de infraestructura que conecta el dominio con tecnología externa (BD, HTTP, gRPC, Kafka). |
| **BCrypt** | Algoritmo de hashing de contraseñas con factor de coste. |
| **DBML** | *Database Markup Language*; notación declarativa del modelo de datos. |
| **DTO** | *Data Transfer Object*; objeto para transportar datos entre capas o servicios. |
| **EF Core** | *Entity Framework Core*; ORM de .NET. |
| **gRPC** | Marco de llamada a procedimiento remoto sobre HTTP/2, con *Protocol Buffers*. |
| **Hexagonal (Ports & Adapters)** | Estilo arquitectónico que aísla el dominio del exterior mediante puertos y adaptadores. |
| **RS256 / JWKS** | Firma RSA-SHA-256 y publicación de claves públicas para validar JWT. |
| **JWT** | *JSON Web Token*; credencial firmada con *claims*. |
| **Kafka** | Plataforma de mensajería distribuida orientada a eventos. |
| **KRaft** | Modo de Kafka sin ZooKeeper (controlador integrado). |
| **Kong** | API Gateway *open source*. |
| **Liquibase** | Herramienta de migración de esquemas de base de datos. |
| **OTP** | *One-Time Password*; código de verificación temporal. |
| **PII** | *Personally Identifiable Information*; datos personales identificables. |
| **Puerto (Port)** | Interfaz que define cómo el dominio se comunica con el exterior (de entrada o de salida). |
| **RNF / NFR** | Requisito no funcional (*Non-Functional Requirement*). |
| **RF / FR** | Requisito funcional (*Functional Requirement*). |
| **RFC 7807** | Estándar de *Problem Details* para respuestas de error en JSON. |
| **SSR** | *Server-Side Rendering*; renderizado en servidor. |
| **Tenant** | Colegio (institución) como unidad de aislamiento lógico de datos. |
| **TDD** | *Test-Driven Development*. |

### 1.4 Referencias (código del proyecto)

Las referencias bibliográficas y normativas se listan en la sección 10. La fuente primaria de verificación de este manual es el código fuente y los artefactos reales del proyecto; no se apoya en documentos auxiliares:

- **Backend:** repositorios `sg-ms-*`, `sg-ms-api-gateway` y `broker-kafka` (microservicios .NET 10 y Java 21, gateway Kong y mensajería Kafka).
- **Base de datos:** `code-sena/sg-db` (modelo `db-school-guardian.dbml` y changelogs de migración Liquibase).
- **Frontend:** `school-guardian-project/GuardianEscolar` (aplicación web Angular y aplicativo móvil React Native/Expo).
- **GPS:** `school-guardian-project/sg-ms-gps` (servicio independiente de ingesta y consulta).

### 1.5 Control de versiones

El software usa **Conventional Commits** (por ejemplo, `feat:`, `fix:`, `chore:`) y ramas por propósito. El presente manual sigue versionado documental con paridad estricta entre `es/` y `en/`: ambos idiomas deben mantener el mismo número de secciones, tablas y componentes. El detalle de la estrategia de ramas se encuentra en la sección 5.1.

---

## 2. Descripción general del sistema

### 2.1 Objetivo y funcionalidades principales

Guardián Escolar centraliza la operación del transporte escolar. Su objetivo es que un colegio pueda **planificar rutas, ejecutarlas, rastrear el bus en vivo, registrar el abordaje y descenso de cada estudiante y notificar a los acudientes**, con trazabilidad de cada evento.

Funcionalidades principales (agrupadas por requisito funcional del SRS, con su estado de implementación verificado en el código):

| Grupo | Funcionalidad | Servicios implicados | Estado verificado |
|---|---|---|---|
| **RF-01** | Gestión de usuarios: crear, actualizar, activar/desactivar cuentas y vincular estudiantes a acudientes | ms-user-management, ms-iam | Implementado (REST + Kafka) |
| **RF-02** | Autenticación con JWT y cierre/renovación de sesión | ms-iam | Implementado (login, refresh, logout) |
| **RF-03** | Rutas, flota y asignaciones: rutas y paradas ordenadas, buses, conductor→bus, estudiantes→ruta | ms-route, ms-fleet, ms-user-management | Implementado |
| **RF-04** | Escaneo QR de subida/bajada | ms-notification (registro de escaneo) | Parcial (registro de escaneo implementado; generación/lectura de QR en clientes) |
| **RF-05** | Notificaciones de abordaje a acudientes (push Expo) | ms-notification | Implementado (consumidor `student.scanned` + push) |
| **RF-06** | Rastreo en tiempo real y mapa interactivo | sg-ms-gps (ingesta TCP), ms-route (gRPC), web/móvil (Leaflet/Map) | Parcial (ingesta y memoria implementadas; ruta Kong `/api/gps` declarada, sin verificar despliegue ni flujo completo) |
| **RF-07** | Ciclo de viaje (inicio/fin) | ms-route | Implementado (`api/trips/start`, `api/trips/end`, `api/trips/current`) |
| **RF-08** | Incidentes y alertas | ms-notification | Parcial (modelo `Alert`, `AlertType`, `AlertRecipient`; endpoints de alertas no expuestos) |
| **RF-09** | Consulta de rutas por acudiente y por conductor | ms-route | Implementado (`api/routes/student/{id}`, `api/routes/current`) |
| **RF-10** | Personalización: temas y multi-idioma | web/móvil (cliente) | Implementado (temas y 4 idiomas) |

> **Límite documentado:** cuando la numeración de requisitos resulta ambigua, en este manual se describe la **funcionalidad real del código** en lugar de fijar una numeración específica.

### 2.2 Contexto y relación con otros sistemas

Guardián Escolar es un sistema con dos clientes (web y móvil) que consumen un backend de microservicios a través de un API Gateway. Se integra con:

- **Dispositivos GPS** de los buses (rastreadores que envían tramas por **TCP** al servicio de ingesta GPS).
- **Servicio de notificaciones push de Expo** (`https://exp.host/--/api/v2/push/send`) para notificar a la app móvil.
- **Twilio Verify** (SMS) para verificación de número telefónico en los flujos de recuperación/cambio.
- **Servidor SMTP** para el envío de correos (códigos OTP).

```mermaid
flowchart LR
    subgraph Clientes
        W[Web Angular]
        M[Móvil React Native / Expo]
    end
    subgraph Edge
        K[API Gateway Kong :8000]
    end
    subgraph Backend
        IAM[ms-iam]
        UM[ms-user-management]
        SM[ms-school-management]
        FL[ms-fleet]
        RT[ms-route]
        NT[ms-notification]
        FI[ms-forgot-information]
        GPS[sg-ms-gps]
    end
    KFK[(Kafka)]
    DB[(SQL Server: school-guardian)]
    GPSDEV[[Rastreadores GPS]]
    EXPO[[Expo Push]]
    TW[[Twilio Verify]]
    SMTP[[SMTP]]

    W --> K
    M --> K
    K --> IAM & UM & SM & FL & RT & NT & FI & GPS
    UM --> SM
    FL --> IAM
    RT --> FL
    NT --> UM & RT & EXPO
    FI --> IAM & TW & SMTP
    IAM <--> KFK
    UM <--> KFK
    NT <--> KFK
    GPSDEV -->|TCP| GPS
    IAM & UM & SM & FL & RT & NT & FI --> DB
```

### 2.3 Tecnologías, lenguajes, frameworks y versiones

**Backend (microservicios).** El backend es **políglota**: la mayoría de servicios están en **.NET 10 (C#)** y dos en **Java 21 (Spring Boot)**.

| Componente | Lenguaje / Runtime | Framework y versiones clave | Evidencia |
|---|---|---|---|
| ms-fleet, ms-notification, ms-route, ms-school-management, ms-user-management, sg-ms-forgot-information | C# / .NET `net10.0` | ASP.NET Core; EF Core; Kafka/gRPC/JwtBearer según el proyecto (no todos usan todos los paquetes) | `sg-ms-*/src/*/*.csproj` |
| ms-iam | Java 21 | Spring Boot 4.1.1; Spring Data JPA; Spring Security; Spring Kafka; jjwt 0.12.5; springdoc-openapi 3.0.0; scalar-webmvc 0.6.63 | `sg-ms-iam/pom.xml` |
| sg-ms-gps | Java 21 | Spring Boot 4.1.1; Spring Web MVC; dependencias JPA/JDBC no prueban persistencia | `sg-ms-gps/pom.xml:8–23` |
| API Gateway | — | Kong 3.9 (DB-less, config declarativa) | `sg-ms-api-gateway/docker-compose.yml` |
| Mensajería | — | Apache Kafka 4.1.0 en modo KRaft | `broker-kafka/docker-compose.yml` |
| Base de datos | — | Microsoft SQL Server 2022 | `sg-db/docker-compose.yaml` |

**Frontend.**

| Componente | Tecnología | Versiones clave | Evidencia |
|---|---|---|---|
| Web | Angular (SPA + SSR) | Angular 21.2.9; Angular Material 21.2.3; Angular CDK 21.2.3; RxJS 7.8.2; TypeScript 5.9.x; NGX-Translate 17.0.0; Leaflet 1.9.4; Express 5.2.1 | `GuardianEscolar/dvlp-front/dvlp-web/package.json` |
| Móvil | React Native + Expo | Expo 57.0.27; React 19.2.3; React Native 0.86.3; React Navigation 7; react-native-maps 1.27.2; i18next 26 | `GuardianEscolar/dvlp-front/dvlp-movil/guardian-escolar/package.json` |
| Pruebas web | Vitest 4.1.0 sobre builder `@ngx-env/builder:unit-test` (jsdom) | — | `package.json`, `angular.json` |

**Contenedores e infraestructura.**

| Elemento | Imagen / versión | Evidencia |
|---|---|---|
| Runtime .NET | `mcr.microsoft.com/dotnet/aspnet:10.0` | `sg-ms-*/src/*/Dockerfile` |
| SDK .NET (build) | `mcr.microsoft.com/dotnet/sdk:10.0` | ídem |
| Runtime Java (build) | `maven:3.9-eclipse-temurin-21` → `eclipse-temurin:21-jre` | `sg-ms-iam/Dockerfile` |
| Node (build web) | `node:20-alpine`; producción `nginx:alpine` | `GuardianEscolar/dvlp-front/dvlp-web/{Dockerfile,dev.Dockerfile}` |
| Red Docker compartida | `sg-services-network` (externa) | composes de cada servicio |

### 2.4 Resumen de la arquitectura (diagrama general)

El sistema se organiza en **cuatro planos**: clientes, borde (Edge), dominio (microservicios) e infraestructura (mensajería y datos). Los clientes sólo conocen el **API Gateway** (Kong), que enruta por prefijo hacia cada servicio. Los servicios colaboran de forma **síncrona** (REST y gRPC) y **asíncrona** (eventos Kafka). Cada servicio posee su propio esquema dentro de una única base de datos SQL Server.

```mermaid
flowchart TB
    subgraph P1[Plano de clientes]
        WEB[Web Angular SPA/SSR]
        MOV[Móvil Expo/React Native]
    end
    subgraph P2[Plano de borde]
        KONG[Kong API Gateway :8000]
    end
    subgraph P3[Plano de dominio]
        IAM[ms-iam :8080]
        UMU[ms-user-management :8080]
        SCH[ms-school-management :8080 + gRPC :5001]
        FLT[ms-fleet :8080]
        ROU[ms-route :8080 + gRPC]
        NOT[ms-notification :8080]
        FOR[ms-forgot-information :8080]
        GPSB[sg-ms-gps :8080 / TCP :8842]
    end
    subgraph P4[Plano de infraestructura]
        KAF[Kafka :9092]
        SQL[(SQL Server 2022 :1433)]
    end
    WEB & MOV --> KONG
    KONG --> IAM & UMU & SCH & FLT & ROU & NOT & FOR & GPSB
    UMU --> SCH
    FLT --> IAM
    ROU --> FLT
    NOT --> UMU & ROU
    FOR --> IAM
    IAM & UMU & NOT <--> KAF
    IAM & UMU & SCH & FLT & ROU & NOT & FOR --> SQL
```

**Figura 2.1 — Diagrama general de la arquitectura.** La numeración de figuras es consecutiva; cada figura se cita en el texto.

---

## 3. Arquitectura del sistema

### 3.1 Estilo arquitectónico y justificación

El sistema combina tres decisiones estructurales:

1. **Microservicios con arquitectura hexagonal (Ports & Adapters).** Cada servicio separa estrictamente:
   - **Dominio** (`Domain`): modelos y reglas de negocio, sin dependencias de frameworks.
   - **Aplicación** (`Application`): casos de uso que orquestan el dominio.
   - **Infraestructura** (`Infrastructure`): adaptadores de entrada (controladores REST, gRPC, consumidores Kafka) y de salida (persistencia, clientes HTTP, productores Kafka).

   La estructura de carpetas lo evidencia en todos los servicios (por ejemplo, `sg-ms-user-management/src/ms-user-management.Api/{Domain,Application,Infrastructure}` y, en Java, `sg-ms-iam/src/main/java/com/school_guardian/ms_iam/{domain,application,infrastructure}`). La **justificación** es la testabilidad del núcleo de negocio, el desacoplamiento de la tecnología y la posibilidad de cambiar un adaptador (por ejemplo, de motor de base de datos o de broker) sin tocar el dominio.

2. **Arquitectura orientada a eventos (event-driven) para la integración asíncrona.** Los eventos de creación de personas y de abordaje viajan por **Kafka**, desacoplando productores y consumidores y permitiendo que las notificaciones no bloqueen la operación principal.

3. **API Gateway único (Kong).** Centraliza el enrutamiento, CORS y control de tasa, y oculta la topología interna de los servicios a los clientes.

El emisor ms-iam firma **RS256** y publica JWKS; cinco backend .NET de negocio validan el token y exigen autorización para sus controladores. Recuperación es una excepción local basada en OTP (sección 7.3). El *deny-list* de IAM sigue en memoria y notificaciones está en .NET. Son observaciones de código, no garantías de operación.

### 3.2 Diagrama de componentes y de despliegue

**Diagrama de componentes** (relaciones lógicas entre servicios, sus puertos y sus adaptadores externos):

```mermaid
flowchart LR
    subgraph Edge
        KONG[Kong 3.9 :8000]
    end
    subgraph Identidad
        IAM["ms-iam (Java/Spring) REST /api/v1/auth + JWKS gRPC Profile/InternalProfile · RS256"]
    end
    subgraph Personas
        UMU["ms-user-management (.NET) REST /api/{students,drivers,parents,admins,families} gRPC cliente"]
    end
    subgraph Escolar
        SCH["ms-school-management (.NET) REST /api/v1/schools Servidor gRPC :5001"]
    end
    subgraph Flota
        FLT["ms-fleet (.NET) REST /api/{buses,vehicle-types}"]
    end
    subgraph Rutas
        ROU["ms-route (.NET) REST /api/{routes,stops,trips,cities} Servidor gRPC"]
    end
    subgraph Notificaciones
        NOT["ms-notification (.NET) REST /api/v1/notifications Consumidor L3"]
    end
    subgraph Recuperacion
        FOR["ms-forgot-information (.NET) REST /api/v1/{password,email,phone}"]
    end
    subgraph Tracking
        GPS["sg-ms-gps (Java) TCP :8842 + REST /api/gps"]
    end
    KONG --> IAM & UMU & SCH & FLT & ROU & NOT & FOR & GPS
    UMU -->|gRPC| SCH
    FLT -->|gRPC + clave interna| IAM
    ROU -->|gRPC + clave interna| FLT
    ROU -->|gRPC| UMU
    NOT -->|gRPC| UMU & ROU
    FOR -->|gRPC + clave interna| IAM
```

**Figura 3.1 — Diagrama de componentes.**

**Diagrama de despliegue** (contenedores Docker sobre la red `sg-services-network`):

```mermaid
flowchart TB
    subgraph Host[Docker Host / WSL2]
        subgraph Net["Red externa sg-services-network"]
            KC[kong:3.9 8000/8443/8001]
            KI[ms-iam:8080]
            KU[ms-user-management:8080]
            KS[ms-school-management:8080/5001]
            KF[ms-fleet:8080]
            KR[ms-route:8080]
            KN[ms-notification:8080]
            KX[ms-forgot-information:8080]
            KG[ms-gps:8080 / TCP 8842]
            KK[kafka:4.1.0 9092]
            DB[(sqlserver:2022 1433)]
            LB[contenedor liquibase migraciones]
        end
        subgraph Volumes
            V1[(volumen SQL data)]
            V2[(volumen Kafka data)]
        end
    end
    LB --> DB
    DB --> V1
    KK --> V2
    KC --> KI & KU & KS & KF & KR & KN & KX & KG
```

**Figura 3.2 — Topología declarada, no despliegue comprobado.** Kong referencia nombres DNS y puertos internos, no puertos publicados del host.

### 3.3 Descripción de módulos, capas y servicios

| Servicio | Bounded context | Esquema BD | Puerto(s) | Rol |
|---|---|---|---|---|
| **ms-iam** | Identidad y acceso | `Iam` | 8080 | Autenticación, perfiles, roles, términos, emisor de JWT |
| **ms-user-management** | Personas y familias | `UserManagement` | 8080 | Person, Profile (datos personales), familias, licencias |
| **ms-school-management** | Institucional | `School` (+`Geographic`) | 8080 / gRPC 5001 | Colegios, sedes y vínculo admin↔colegio |
| **ms-fleet** | Flota | `Fleet` | 8080 | Buses, marcas/modelos, asignación de conductores |
| **ms-route** | Rutas y viajes | `Route` | 8080 (+gRPC) | Rutas, paradas, asignaciones, ejecuciones y viajes |
| **ms-notification** | Notificaciones | `Notification` | 8080 | Alertas de abordaje, destinatarios, tokens de push |
| **sg-ms-forgot-information** | Recuperación | `ForgotInformation` | 8080 | OTP por correo/SMS y cambios de correo/teléfono |
| **sg-ms-gps** | Tracking | `Gps` (modelo; servicio en memoria) | 8080 / TCP 8842 | Ingesta de tramas GPS y consulta de posiciones |
| **ms-api-gateway** | Borde | — | 8000 | Gateway Kong |

`ForgotInformationContext` configura explícitamente el esquema `ForgotInformation` y la tabla `VerificationRequest`; esto no comprueba su instalación.

**Capas internas por servicio .NET.** El layout estándar es:

```
<servicio>/src/<servicio>.Api/
├── Domain/
│   ├── Model/          # Entidades y value objects
│   └── Ports/          # Interfaces (In = casos de uso, Out = repositorios/servicios)
├── Application/
│   ├── UseCase/        # Implementación de casos de uso
│   ├── Mapper/         # Perfiles AutoMapper
│   └── Dto/            # Objetos de transferencia
├── Infrastructure/
│   ├── Controller/     # Adaptadores de entrada REST
│   ├── Persistence/    # DbContext EF Core y repositorios
│   ├── External/       # Clientes HTTP a otros servicios
│   ├── Messaging/      # Productores/consumidores Kafka
│   ├── Grpc/           # Servicios y clientes gRPC
│   ├── Security/       # Autenticación/JWT
│   ├── Configuration/  # Tenant provider, DI
│   └── DependencyInjection/   # ServiceCollectionExtensions
├── Protos/             # Contratos .proto (si aplica)
├── Program.cs          # Composición de la aplicación
└── Dockerfile
```

### 3.4 Flujo de datos entre componentes

**Flujo A — Autenticación y consumo autenticado.**

```mermaid
sequenceDiagram
    participant U as Usuario (Web/Móvil)
    participant K as Kong
    participant IAM as ms-iam
    participant SV as Servicio de dominio
    U->>K: POST /api/v1/auth/login (credenciales)
    K->>IAM: POST /api/v1/auth/login
    IAM-->>U: access RS256 + cookie refresh (vigencia configurable)
    U->>K: GET /fleet/api/buses (Authorization: Bearer)
    K->>SV: reenvía con el token
    SV-->>U: 200 + datos (filtrados por tenant/colegio)
```

**Flujo B — Creación de persona y sincronización de identidad (event-driven).**

```mermaid
sequenceDiagram
    participant UMU as ms-user-management
    participant DB as SQL Server
    participant KFK as Kafka
    participant IAM as ms-iam
    UMU->>DB: INSERT Person
    UMU->>KFK: publish student.created / driver.created / admin.created / parent.created
    KFK->>IAM: consume (grupo "iam")
    IAM->>DB: crea/actualiza Profile (esquema Iam)
    IAM->>KFK: publish profile.created
```

**Flujo C — Abordaje (escaneo) y notificación al acudiente.**

```mermaid
sequenceDiagram
    participant Cli as Cliente (QR)
    participant NOT as ms-notification
    participant KFK as Kafka
    participant UMU as ms-user-management
    participant EXPO as Expo Push
    Cli->>NOT: POST /api/notifications/scan (vía Kong /notification)
    NOT->>NOT: persiste Boarding + Alert
    NOT->>KFK: publish student.scanned
    KFK->>NOT: consume student.scanned (ms-notification-consumer)
    NOT->>UMU: gRPC GetFamilyMembersByStudent
    UMU-->>NOT: acudientes del estudiante
    NOT->>EXPO: intento POST /--/api/v2/push/send (sin recibo de entrega)
```

**Flujo D — Rastreo GPS en tiempo real.**

```mermaid
sequenceDiagram
    participant Dev as Rastreador GPS
    participant GPS as sg-ms-gps (TCP :8842)
    participant K as Kong (ruta declarada)
    participant Cli as Web/Móvil
    Dev->>GPS: trama binaria (login 0x01 / heartbeat 0x13 / ubicación 0x31)
    GPS->>GPS: decodifica y guarda en memoria (ConcurrentHashMap)
    GPS-->>Dev: ACK con CRC-16
    Cli->>K: GET /api/gps/devices/{imei}/location + Bearer
    K->>GPS: valida access token y reenvía sin quitar prefijo
    GPS-->>Cli: última posición conocida
```

### 3.5 Patrones de diseño aplicados

| Patrón | Dónde se aplica | Evidencia |
|---|---|---|
| **Ports & Adapters (Hexagonal)** | Todos los microservicios | Carpetas `Domain/Ports`, `Application/UseCase`, `Infrastructure/…` |
| **Dependency Injection** | .NET (`ServiceCollectionExtensions`) y Java (Spring) | `Infrastructure/DependencyInjection/ServiceCollectionExtensions.cs`; `@Service`, `@Repository` |
| **Repository** | Acceso a datos detrás de interfaces de puerto | `IBusRepository`, `IPersonRepository`, repositorios Spring Data |
| **DTO + Mapper (AutoMapper)** | Traducción dominio↔API | `Application/Mapper`, `AddAutoMapper` en `Program.cs` |
| **API Gateway** | Enrutamiento, CORS, rate limiting | `sg-ms-api-gateway/kong/kong.yml` |
| **Publish/Subscribe (eventos)** | Integración vía Kafka | `KafkaEventPublisher`, consumidores `*.Consumer` |
| **gRPC (RPC síncrono)** | Comunicación entre servicios internos | `SchoolManagementGrpcClient`, `RouteGrpcService`, `FamilyGrpcService` |
| **Strategy** | Estrategias de búsqueda | `PlateSearchStrategy`, `NameSearchStrategy` (ms-fleet/ms-user-management) |
| **Provider / Tenant resolver** | Filtrado multi-colegio | `ITenantProvider`, `HttpContextTenantProvider`, `JwtTenantProvider` |
| **Hosted Service (Background)** | Consumo Kafka en segundo plano | `StudentScannedConsumer` como `IHostedService` |
| **Problem Details (RFC 7807)** | Errores uniformes en API y cliente | Middleware de excepciones; `core/http/problem-detail.ts` |
| **Rate Limiting** | Protección de endpoints | Middleware ASP.NET Core (`writes`, `registration`, `otp-public`) y plugin Kong |

> **Corrección de hallazgos anteriores:** RS256/JWKS y servidores gRPC de IAM están implementados (`JwtTokenProvider`, `JwksController`, `GrpcServerLifecycle`). El *deny-list* sigue en memoria. No confundir un servidor gRPC implementado con un cliente o despliegue demostrado; véanse secciones 7.3–7.5.

---

## 4. Modelo de datos

### 4.1 Diagrama entidad–relación

El modelo se materializa en **una base de datos SQL Server** llamada `school-guardian`, organizada por **esquema-por-servicio** (patrón *schema-per-service*). El DBML canónico es `db-school-guardian.dbml` (34 tablas agrupadas en 9 esquemas). Por su extensión, el ER se presenta por dominios.

**Dominio de identidad y acceso (esquema `Iam`):**

```mermaid
erDiagram
    Role ||--o{ Profile : "tiene"
    Role ||--o{ PermissionsRole : "agrupa"
    Permissions ||--o{ PermissionsRole : "otorga"
    Profile ||--o{ SessionProfile : "abre"
    Profile ||--o{ TermsAcceptance : "acepta"
    Profile {
        uuid Id PK
        uuid PersonId FK
        uuid CampuseId FK
        varchar PasswordHash
        tinyint RoleId FK
        status Status
    }
    Role {
        tinyint ID PK
        varchar Name
        status Status
    }
    SessionProfile {
        int ID PK
        uuid ProfileID FK
        datetime2 DateStart
        datetime2 DateEnd
        statusSession StatusSession
    }
```

**Figura 4.1 — ER del dominio de identidad.**

**Dominio de personas y familias (esquema `UserManagement`):**

```mermaid
erDiagram
    Person ||--o{ Profile : "es"
    Family ||--o{ FamilyMember : "incluye"
    Profile ||--o{ FamilyMember : "pertenece"
    Profile ||--o{ DriverLicense : "posee"
    Person {
        uuid Id PK
        varchar Name
        varchar LastName
        varchar IdentificationNumber
        varchar Email
        bigint Phone
        date DateBirth
    }
    Family {
        uuid Id PK
        varchar Name
        status Status
    }
    FamilyMember {
        uuid Id PK
        uuid FamilyId FK
        uuid ProfileId FK
        RelationshipType RelationshipType
    }
    DriverLicense {
        uuid ID PK
        uuid ProfileId FK
        varchar LicenseNumber
        date LicenseExpirationDate
    }
```

**Figura 4.2 — ER de personas y familias.**

**Dominio institucional (`School` + `Geographic`):**

```mermaid
erDiagram
    City ||--o{ School : "ubica"
    City ||--o{ Stop : "ubica"
    School ||--o{ SchoolCampus : "tiene"
    School ||--o{ SchoolAdmin : "administra"
    SchoolCampus ||--o{ Profile : "adscribe"
    School {
        uuid Id PK
        uuid CityId FK
        varchar Name
        varbinary Logo
        status Status
    }
    SchoolCampus {
        uuid Id PK
        uuid SchoolId FK
        varchar Name
        varchar Address
    }
    SchoolAdmin {
        uuid Id PK
        uuid SchoolId FK
        uuid ProfileId FK
    }
    City {
        uuid Id PK
        varchar Name
        varchar Country
    }
```

**Figura 4.3 — ER institucional y geográfico.**

**Dominio de rutas, flota y seguimiento (`Route`, `Bus`, `Gps`, `Notification`):**

```mermaid
erDiagram
    SchoolCampus ||--o{ Bus : "asigna"
    SchoolCampus ||--o{ Route : "origina"
    Model ||--o{ Bus : "tipo"
    Brand ||--o{ Model : "marca"
    GpsDevice ||--o{ GpsLocation : "reporta"
    GpsDevice ||--|| Bus : "equipa"
    Bus ||--o{ DriverAssignemts : "conduce"
    Profile ||--o{ DriverAssignemts : "asignado"
    Route ||--o{ RouteStop : "contiene"
    Stop ||--o{ RouteStop : "incluida"
    Route ||--o{ RouteBusAssignments : "opera"
    Bus ||--o{ RouteBusAssignments : "opera"
    RouteBusAssignments ||--o{ RouteSchedule : "programa"
    Route ||--o{ RouteExecution : "ejecuta"
    Bus ||--o{ RouteExecution : "recorre"
    Profile ||--o{ RouteExecution : "conduce"
    RouteStop ||--o{ RouteStudentAssignments : "sube"
    Profile ||--o{ RouteStudentAssignments : "asignado"
    RouteExecution ||--o{ Boarding : "registra"
    RouteExecution ||--o{ Alert : "genera"
    AlertType ||--o{ Alert : "clasifica"
    Alert ||--o{ AlertRecipient : "notifica"
    Profile ||--o{ DeviceToken : "registra"
    Boarding {
        uuid Id PK
        uuid RouteExecutionId FK
        uuid ProfileId FK
        uuid RouteStopId FK
        datetime2 DateTime
        BoardingType BoardingType
    }
    GpsLocation {
        uuid ID PK
        uuid GpsDeviceID FK
        decimal Latitude
        decimal Longitude
        decimal Speed
        decimal Course
        datetime2 DateTime
    }
```

**Figura 4.4 — ER de rutas, flota, GPS y notificaciones.**

### 4.2 Diccionario de datos

A continuación se documentan las tablas principales con sus campos, tipos y restricciones. La fuente única es `db-school-guardian.dbml`.

**Esquema `Iam`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **Profile** | Id | uuid | PK, not null | Identificador del perfil (cuenta de acceso). |
| | PersonId | uuid | FK → Person.Id, not null | Persona asociada. |
| | CampuseId | uuid | FK → SchoolCampus.Id, null | Sede a la que se adscribe el perfil. |
| | PasswordHash | varchar(100) | not null | Hash de contraseña (BCrypt). |
| | RoleId | tinyint | FK → Role.Id, not null | Rol del perfil. |
| | Status | status | not null, default `Active` | Estado lógico. |
| **Role** | ID | tinyint | PK, not null | Identificador del rol (1=Admin, 2=Student, 3=Driver, 4=Parent, 5=SuperAdmin). |
| | Name | varchar(20) | not null, unique | Nombre del rol. |
| | Description | varchar(100) | not null | Descripción. |
| | Status | status | not null, default `Active` | Estado. |
| **SessionProfile** | ID | int | PK, increment | Identificador de sesión. |
| | ProfileID | uuid | FK → Profile.ID, not null | Perfil. |
| | DateStart / DateEnd | datetime2 | not null | Vigencia de la sesión. |
| | IPAddress | varchar(50) | not null | IP de origen. |
| | StatusSession | StatusSession | not null, default `Active` | Estado de la sesión. |
| **Permissions** | Id | tinyint | PK, increment | Permiso. |
| | TypePermissions | varchar(100) | not null | Tipo/nombre del permiso. |
| | Description | varchar(150) | not null | Descripción. |
| **PermissionsRole** | Id | tinyint | PK, increment | Relación permiso-rol. |
| | PermissionsId | tinyint | FK → Permissions.Id, not null | Permiso. |
| | RoleId | tinyint | FK → Role.Id, not null | Rol. |
| **TermsAcceptance** | Id | uuid | PK | Identificador. |
| | ProfileId | uuid | FK → Profile.Id, not null | Quien acepta. |
| | StudentProfileId | uuid | FK → Profile.Id, null | Estudiante en cuyo nombre se acepta. |
| | TermsVersion | varchar(20) | not null | Versión de términos. |
| | AcceptedAt | datetime2 | not null | Fecha/hora de aceptación. |
| | IpAddress | varchar(50) | null | IP. |
| | Channel | varchar(20) | not null, default `mobile` | Canal (mobile/web). |

**Esquema `UserManagement`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **Person** | Id | uuid | PK, not null | Identificador de la persona. |
| | Name / LastName | varchar(50) | not null | Nombres y apellidos. |
| | IdentificationType | Identificationtype | not null, default `TI` | Tipo de documento. |
| | IdentificationNumber | varchar(20) | not null, unique | Número de documento. |
| | Email | varchar(50) | not null, unique | Correo. |
| | Phone | bigint | not null | Teléfono. |
| | ResidenceAddress | varchar(50) | not null | Dirección. |
| | DateBirth | date | not null | Fecha de nacimiento. |
| **Family** | Id | uuid | PK | Identificador de familia. |
| | Name | varchar(50) | not null | Nombre. |
| | Observations | varchar(100) | not null | Observaciones. |
| | Status | status | not null, default `Active` | Estado. |
| **FamilyMember** | Id | uuid | PK | Identificador de miembro. |
| | FamilyId | uuid | FK → Family.Id, not null | Familia. |
| | ProfileId | uuid | FK → Profile.Id, not null | Perfil miembro. |
| | RelationshipType | RelationshipType | default `Parent` | Parent o Student. |
| **DriverLicense** | ID | uuid | PK | Identificador de licencia. |
| | ProfileId | uuid | FK → Profile.Id, not null | Conductor. |
| | LicenseNumber | varchar(20) | not null, unique | Número de licencia. |
| | LicenseExpirationDate | date | not null | Vencimiento. |

**Esquema `School`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **School** | Id | uuid | PK | Colegio. |
| | CityId | uuid | FK → City.Id, not null | Ciudad. |
| | Logo | varbinary(max) | not null | Logo. |
| | Name | varchar(30) | not null | Nombre. |
| | Address | varchar(30) | not null | Dirección. |
| | Phone | int | not null | Teléfono. |
| | Email | varchar(50) | not null | Correo. |
| | Website | varchar(100) | null | Sitio web. |
| | Theme | varchar(20) | null | Tema visual. |
| **SchoolCampus** | Id | uuid | PK | Sede. |
| | SchoolId | uuid | FK → School.Id, not null | Colegio. |
| | Name / Address | varchar(30) | not null | Nombre y dirección. |
| **SchoolAdmin** | Id | uuid | PK | Vínculo admin-colegio. |
| | SchoolId | uuid | FK → School.Id, not null | Colegio. |
| | ProfileId | uuid | FK → Profile.Id, not null, unique | Administrador. |

**Esquema `Geographic`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **City** | Id | uuid | PK | Ciudad. |
| | Name | varchar(40) | not null | Nombre. |
| | Country | varchar(30) | not null | País. |

**Esquema `Fleet` (grupo *Bus*).**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **Bus** | Id | uuid | PK, not null | Bus. |
| | CampuseId | uuid | FK → SchoolCampus.Id, not null | Sede. |
| | SoatValidity | date | not null | Vigencia SOAT. |
| | GpsDeviceId | uuid | FK → GpsDevice.Id, not null, unique | Dispositivo GPS. |
| | Capacity | tinyint | not null | Capacidad. |
| | Plate | varchar(15) | not null, unique | Placa. |
| | ModelId | tinyint | FK → Model.Id, not null | Modelo. |
| **Brand** | Id / Name | tinyint / varchar(30) | PK / — | Marca. |
| **Model** | Id / BrandId / Name | tinyint / tinyint / varchar(30) | PK / FK / — | Modelo. |
| **DriverAssignemts** | Id | uuid | PK | Asignación conductor-bus. |
| | ProfileId | uuid | FK → Profile.Id, not null | Conductor. |
| | BusId | uuid | FK → Bus.Id, not null | Bus. |
| | AssignedFrom / AssignedTo | date | not null / null | Vigencia. |

> Se conserva el nombre físico `DriverAssignemts` (tal como está en la base y el DBML), aunque contiene una errata ortográfica.

**Esquema `Route`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **Route** | Id | uuid | PK | Ruta. |
| | CampuseId | uuid | FK → SchoolCampus.Id, not null | Sede. |
| | Name / TargetSector | varchar(30) | not null | Nombre y sector objetivo. |
| | StartTime / EndTime | time | not null | Horario. |
| **Stop** | Id | uuid | PK | Parada. |
| | CityId / SchoolId | uuid | FK, not null | Ciudad y colegio. |
| | Name | varchar(30) | not null | Nombre. |
| | Address | varchar(100) | not null | Dirección. |
| | Longitude / Latitude | decimal(10,7) | not null | Coordenadas. |
| **RouteStop** | Id | uuid | PK | Parada dentro de la ruta. |
| | RouteId / StopId | uuid | FK, not null | Ruta y parada. |
| | OrderSequence | int | not null | Orden de la parada. |
| **RouteStudentAssignments** | Id | uuid | PK | Asignación estudiante→parada. |
| | ProfileId | uuid | FK → Profile.Id, not null | Estudiante. |
| | RouteStopId | uuid | FK → RouteStop.Id, not null | Parada. |
| **RouteBusAssignments** | Id | uuid | PK | Asignación bus→ruta. |
| | BusId / RouteId | uuid | FK, not null | Bus y ruta. |
| **RouteSchedule** | Id | uuid | PK | Programación. |
| | RouteBusAssignmentsId | uuid | FK, not null | Asignación bus-ruta. |
| | DayOfWeek | DayOfWeek | not null, default `Monday` | Día. |
| | Direction | varchar(10) | not null | Sentido. |
| | StartTime | time | not null | Hora. |
| **RouteExecution** | ID | uuid | PK | Ejecución/viaje. |
| | BusId / DriverId | uuid | FK, not null | Bus y conductor. |
| | RouteId | uuid | FK, null | Ruta. |
| | StartDateTime / EndDateTime | datetime2 | not null / null | Inicio y fin. |

**Esquema `Gps`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **GpsDevice** | ID | uuid | PK | Dispositivo GPS. |
| | Imei | varchar(20) | not null, unique | IMEI. |
| | LastConnection | datetime2 | not null | Última conexión. |
| | GpsStatus | bit | not null | Estado. |
| **GpsLocation** | ID | uuid | PK | Lectura de posición. |
| | GpsDeviceID | uuid | FK → GpsDevice.ID, not null | Dispositivo. |
| | Latitude / Longitude | decimal(10,7) | not null | Coordenadas. |
| | Speed / Course | decimal(8,2) | not null | Velocidad y rumbo. |
| | DateTime | datetime2 | not null | Marca temporal. |

**Esquema `Notification`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **Boarding** | Id | uuid | PK | Abordaje/descenso. |
| | RouteExecutionId / ProfileId / RouteStopId | uuid | FK, not null | Ejecución, estudiante y parada. |
| | DateTime | datetime2 | not null | Fecha/hora (automática). |
| | BoardingType | BoardingType | not null, default `ON_BOARD` | ON_BOARD / OFF_BOARD. |
| **Alert** | Id | uuid | PK | Alerta. |
| | RouteExecutionId / ProfileId | uuid | FK, not null | Contexto y origen. |
| | AlertTypeId | tinyint | FK → AlertType.Id, not null | Tipo. |
| | Description / AcknowledgedAt | varchar(100) / datetime2 | null | Detalle y acuse. |
| **AlertType** | Id | tinyint | PK, increment | Tipo de alerta. |
| | Name / Description | varchar / varchar | not null | Detalle. |
| | UrgencyLevel | tinyint | not null | Urgencia. |
| **AlertRecipient** | ID | uuid | PK | Destinatario de alerta. |
| | AlertId / ProfileId | uuid | FK, not null | Alerta y perfil. |
| | DateTimeRead | datetime2 | not null | Lectura. |
| **DeviceToken** | Id | uuid | PK | Token push. |
| | ProfileId | uuid | FK → Profile.Id, not null | Perfil. |
| | ExpoToken | varchar(300) | not null, unique | Token de Expo. |
| | UpdatedAt | datetime2 | not null | Actualización. |

**Esquema `Settings`.**

| Tabla | Campo | Tipo | Restricciones | Descripción |
|---|---|---|---|---|
| **SecuritySettings** | Id | tinyint | PK, increment | Ajuste. |
| | NameSettings | varchar(100) | not null, unique | Clave del ajuste. |
| | ValueSettings | varchar(100) | not null | Valor. |
| | Description | varchar(150) | not null | Descripción. |
| **PasswordPolicies** | Id | tinyint | PK, increment | Política. |
| | MinLongitude / MaxLongitude | tinyint | not null | Longitud mínima/máxima. |
| | RequiresCapitalLetters / RequiresNumbers / RequiresSymbols | tinyint | not null | Requisitos. |
| | ExpirationDays | tinyint | not null | Días de expiración. |

### 4.3 Índices, vistas, procedimientos almacenados y triggers

Se verificaron los *changelogs* de Liquibase de cada módulo (`sg-db/ms-*-db/`). La conclusión es explícita:

- **Vistas:** no existen. Los changelogs `05-view` de todos los módulos contienen `databaseChangeLog: []`.
- **Funciones:** no existen. Changelogs `06-functions` vacíos.
- **Procedimientos almacenados:** no existen. Changelogs `07-procedure` vacíos.
- **Triggers:** no existen. Changelogs `08-triggers` vacíos.
- **Tipos definidos por usuario:** no existen. Changelogs `02-types` vacíos.
- **Índices:** no se declaran índices explícitos adicionales a las claves primarias y restricciones `unique` definidas en el DDL de tablas. Las claves foráneas generan los índices implícitos propios del motor.

> **Hallazgos del modelo de datos:** (1) existe una carpeta `ms-audit-db` **vacía** (el servicio de auditoría fue eliminado del proyecto, ver commits `58ddf08` y `102fbb2`), por lo que **no hay esquema `Audit`**; (2) el diccionario se documenta a nivel de tipo y restricción del DBML; los detalles de colación, `NULL`/`NOT NULL` a nivel de columna y precisión se conservan en los changelogs Liquibase.

### 4.4 Estrategia de respaldo y recuperación

El repositorio **no incluye** scripts de respaldo ni política de retención documentada para la base de datos. Por transparencia, se documenta la limitación y se recomienda la siguiente estrategia basada en las capacidades del motor:

- **Respaldo completo diario** de la base `school-guardian` (SQL Server `BACKUP DATABASE`).
- **Respaldos diferenciales** cada 6 horas y **respaldos de log** cada 15 minutos en modo de recuperación completa (reduce el RPO a menos de 15 minutos).
- **Persistencia del volumen** de datos SQL Server mediante volumen Docker dedicado (el `docker-compose.yaml` de `sg-db/` usa un volumen para los datos).
- **Validación de restauración** periódica en un entorno aislado.
- **Recuperación ante desastres:** el SRS (RNF-08) fija objetivos de **RTO/RPO** por escenario (servicio < 2 min / 0; BD primaria < 5 min / < 1 s; zona de disponibilidad < 15 min / < 5 min; región < 4 h / < 1 h). *Nota:* estos objetivos provienen del requisito, no de una configuración verificada en el repositorio.

---

## 5. Estructura del código fuente

### 5.1 Repositorios y estrategia de ramas

El proyecto **no vive en un único repositorio**: el código se distribuye en repositorios Git independientes, entre ellos `sg-ms-*`, `sg-ms-api-gateway`, `broker-kafka`, `code-sena/sg-db` y `GuardianEscolar`. El proyecto usa **Conventional Commits** y ramas por propósito: `main` (estable), `dev` (integración), `feat/*`, `fix/*`, `chore/*`, `hotfix/*`.

| Área | Contenido | Rama observada |
|---|---|---|
| Documentación | Entregables bilingües y este manual | `main` |
| Base de datos | DBML, changelogs Liquibase y compose de SQL Server | trabajo sobre una única rama (`database`) |
| Backend | 8 microservicios, Kafka y gateway | ramas por microservicio/feature |
| Frontend | Web y móvil | ramas por feature (p. ej. `fix/legal-page-shared-imports`) |
| GPS | Servicio de ingesta y consulta de ubicaciones | repositorio independiente `sg-ms-gps` |

### 5.2 Árbol de carpetas y paquetes

**Repositorios de servicios backend:**

```
sg-ms-api-gateway/            # Kong (config declarativa kong/kong.yml, docker-compose.yml)
sg-ms-iam/                    # Java 21 + Spring Boot 4.1.1 (hexagonal)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/school_guardian/ms_iam/
│       ├── domain/{model,port/{in,out},event,exception}
│       ├── application/{dto,usecase}
│       └── infrastructure/{config,messaging,persistence,security,web}
sg-ms-user-management/        # .NET 10
sg-ms-school-management/      # .NET 10 (+ servidor gRPC)
sg-ms-fleet/                  # .NET 10
sg-ms-route/                  # .NET 10 (+ servidor gRPC)
sg-ms-notification/           # .NET 10 (consumidor Kafka + push Expo)
sg-ms-forgot-information/     # .NET 10 (OTP por correo/SMS)
sg-ms-gps/                    # Java 21 + Spring Boot (TCP + REST /api/gps)
broker-kafka/                 # Broker + init/create-topics.sh + topics.conf
```

Cada servicio .NET repite el layout estándar descrito en la sección 3.3.

**Base de datos (`code-sena/sg-db/`):**

```
sg-db/
├── db-school-guardian.dbml       # Modelo canónico (34 tablas, 9 esquemas)
├── docker-compose.yaml           # SQL Server 2022 + contenedores Liquibase
├── .env / .env.example
├── ms-iam-db/        {01-ddl,02-types,05-view,06-functions,07-procedure,08-triggers}/changelog.yaml
├── ms-user-management-db/
├── ms-school-db/
├── ms-geographic-db/
├── ms-fleet-db/
├── ms-route-db/
├── ms-gps-db/
├── ms-notification-db/
├── ms-settings-db/
└── ms-audit-db/                  # VACÍA (servicio eliminado)
```

**Frontend (`school-guardian-project/GuardianEscolar/`):**

```
GuardianEscolar/
├── dvlp-front/
│   ├── dvlp-web/                 # Angular 21 (src/app/{core,features,shared})
│   └── dvlp-movil/guardian-escolar/  # React Native + Expo
├── scripts/verify-api.sh         # Smoke test frontend→Kong→microservicios
├── docs/                         # Material de gestión/análisis
└── docker-compose.yml            # Solo frontend web
```

El servicio GPS está en el repositorio independiente `sg-ms-gps`.

**Frontend web (Angular), estructura de `src/app/`:**

```
src/app/
├── core/
│   ├── guards/role.guard.ts
│   ├── services/  (auth, buses, routes, stops, students, …, auth.interceptor.ts)
│   ├── http/problem-detail.ts
│   ├── models/    (bus, city, family, route, school, stop, student)
│   └── validators/form-validators.ts
├── features/
│   ├── public/    (home, contact, auth/login, auth/forgot-password, legal/terms, legal/privacy)
│   ├── admin/     (users/pages/{students,guardians,drivers,families}, routes-buses/pages/{buses,routes,stops})
│   ├── dashboard/ (dashboard-admin, dashboard-superadmin)
│   ├── profile/pages/ (information-admin, change-email, change-password, change-contact)
│   └── superadmin/ (admins/pages/admins, schools/pages/schools)
├── shared/        (components/{cards,modal,navbar,location-map,…}, directives, footer, main)
├── app.config.ts  (router, Http con interceptor, hydration, i18n)
└── app.routes.ts  (rutas con roleGuard)
```

### 5.3 Convenciones de codificación y nomenclatura

| Aspecto | Convención | Evidencia |
|---|---|---|
| API REST | Sustantivos en plural, versión en el prefijo (`/api/v1/…`) | controladores de todos los servicios |
| Rutas internas Kong | Prefijo por servicio (`/user`, `/fleet`, `/school-management`, `/route`, `/notification`) | `kong.yml` |
| Entidades/BD | PascalCase en tablas y columnas; esquemas por servicio | `db-school-guardian.dbml` |
| .NET | C# con `PascalCase` para tipos/métodos; puertos con prefijo `I` (`IBusRepository`) | `Domain/Ports` |
| Java | Paquete base `com.school_guardian.ms_iam`; `camelCase` en métodos; Spring stereotypes | `sg-ms-iam/src/main` |
| Angular | Componentes standalone; alias de rutas `@core/*`, `@features/*`, `@shared/*` | `tsconfig.json` |
| Commits | Conventional Commits (`feat:`, `fix:`, `chore:`) | historial de los repositorios |
| Documentación | `kebab-case.md`, IDs estables (`RF`, `RNF`, `HU`, `UC`…) | repositorios del proyecto |
| Frontend (móvil) | Componentes por tipo (`buttons`, `cards`, `inputs`, `modals`) y `features` | `guardian-escolar/src` |

### 5.4 Dependencias y librerías (versión y licencia)

**Backend .NET (común):** ASP.NET Core (`net10.0`), Entity Framework Core 10.0.12, AutoMapper 16.2.0, Confluent.Kafka 2.15.1, Grpc.AspNetCore 2.84.0, Microsoft.AspNetCore.Authentication.JwtBearer 10.0.12, Scalar.AspNetCore 2.17.10, Twilio 8.0.2 (solo forgot-information). Licencias: mayoría **MIT**; EF Core y ASP.NET Core **MIT**; AutoMapper **MIT**; Confluent.Kafka **Apache-2.0**; Grpc.AspNetCore **Apache-2.0**; Twilio **MIT**.

**Backend Java:** Spring Boot 4.1.1 (Apache-2.0), Spring Data JPA / Security / Kafka (Apache-2.0), jjwt 0.12.5 (Apache-2.0), springdoc-openapi 3.0.0 (Apache-2.0), scalar-webmvc 0.6.63 (MIT), Lombok (MIT), mssql-jdbc (MIT).

**Web (Angular):**

| Paquete | Versión | Licencia |
|---|---|---|
| @angular/core, common, router, forms, platform-browser, platform-server | 21.2.9 | MIT |
| @angular/material / @angular/cdk | 21.2.3 | MIT |
| @angular/ssr | 21.2.10 | MIT |
| @angular/cli / @angular/build | 21.2.7 | MIT |
| @ngx-translate/core / http-loader | 17.0.0 | MIT |
| rxjs | 7.8.2 | Apache-2.0 |
| tslib | 2.8.1 | 0BSD |
| zone.js | 0.16.3 | MIT |
| leaflet | 1.9.4 | BSD-2-Clause |
| @phosphor-icons/web | 2.1.2 | MIT |
| express | 5.2.1 | MIT |
| typescript | 5.9.3 | Apache-2.0 |
| vitest | 4.1.0 | MIT |
| prettier | 3.8.1 | MIT |
| jsdom | 28.1.0 | MIT |

**Móvil (React Native / Expo):**

| Paquete | Versión | Licencia |
|---|---|---|
| expo | 57.0.27 | MIT |
| react / react-dom | 19.2.3 | MIT |
| react-native | 0.86.3 | MIT |
| @react-navigation/native / stack / bottom-tabs | 7.x | MIT |
| react-native-maps | 1.27.2 | MIT |
| react-native-svg | 15.15.4 | MIT |
| react-native-qrcode-svg | 6.3.26 | MIT |
| i18next / react-i18next | 26.x / 17.x | MIT |
| @react-native-async-storage/async-storage | 2.2.0 | MIT |
| expo-location | 57.0.20 | MIT |

**GPS backend:** Spring Boot (Apache-2.0), mssql-jdbc (MIT), Lombok (MIT).

> **Nota de licencias:** la tabla anterior se verificó directamente en los `package.json`/`pom.xml`/`.csproj` del proyecto.

---

## 6. Descripción de módulos y componentes

Esta sección describe cada servicio: su responsabilidad, los casos de uso y clases principales, sus entradas/salidas y sus dependencias. Los endpoints concretos se detallan en la sección 7.

### 6.1 ms-iam (identidad y acceso)

- **Responsabilidad:** autenticación, emisión y validación de JWT, perfiles/roles, sesiones, términos y aceptaciones; expone endpoints internos de gestión de perfil.
- **Casos de uso y clases clave:** `AuthController`, `LoginService`, `RefreshTokenService`, `LogoutService`, `InMemoryTokenDenyList`, `CreateProfileService`, `PersonCreatedConsumer`, `PersonStatusChangedConsumer`, `JwtTokenProvider`, `JwksController`, `GrpcServerLifecycle`.
- **Entradas/salidas:** REST `/api/v1/auth/*` y JWKS; servidores gRPC `ProfileService` e `InternalProfileService`; eventos Kafka; persistencia en esquema `Iam`. Los antiguos endpoints REST internos `/api/profiles/**` no representan la integración actual.
- **Dependencias:** Spring Data JPA (SQL Server), Spring Security, Spring Kafka, jjwt.
- **Reglas de negocio:** BCrypt; access/refresh por defecto 60 minutos/7 días; gRPC protegido con metadata `x-internal-api-key` (sección 7.3).

### 6.2 ms-user-management (personas y familias)

- **Responsabilidad:** CRUD de personas por rol (estudiantes, conductores, acudientes, administradores), familias y licencias de conducción; es el **dueño del dato de la persona**.
- **Casos de uso/controladores:** `Admin/Driver/Parent/Student` controllers; `FamilyController`; `DriverLicenseController`; `ProfileLookupController` (`/api/profiles/{id}/exists`, `/name`). Publica eventos `student.created`, `driver.created`, `parent.created`, `admin.created` y `admin.school.updated`.
- **Entradas/salidas:** REST (`/api/{students,drivers,parents,admins,families,driver-licenses}`); cliente gRPC `SchoolManagementGrpcClient`; productor Kafka.
- **Dependencias:** EF Core (pool 60), AutoMapper, Confluent.Kafka, Grpc.Net.Client.
- **Reglas de negocio:** el colegio/sede se valida contra ms-school-management vía gRPC antes de crear; los datos se filtran por el colegio del token (tenant). Ejemplo de asignación: un administrador se vincula a un colegio (`LinkAdminSchool`).

### 6.3 ms-school-management (institucional)

- **Responsabilidad:** colegios y sedes; vínculo administrador↔colegio; **directorio escolar** consumido por otros servicios.
- **Casos de uso/controladores:** `SchoolController` (`/api/v1/schools`, incluido `with-campuses`), `CampusController`; servidor gRPC `SchoolManagementService` (RPC `GetAdminSchool`, `GetCampus`, `GetCampusByName`, `LinkAdminSchool`, `GetSchool`).
- **Entradas/salidas:** REST y **gRPC en `:5001` (h2c)**; salida a esquema `School`.
- **Dependencias:** EF Core (pool 64), AutoMapper, Grpc.AspNetCore.
- **Reglas de negocio:** una sede pertenece a un colegio; un administrador se vincula a un colegio mediante `SchoolAdmin`; el servicio es la **fuente autoritativa** de sedes/colegios para el resto del sistema.

### 6.4 ms-fleet (flota)

- **Responsabilidad:** parque automotor: buses, marcas/modelos y asignación de conductores a buses.
- **Casos de uso/controladores:** `BusController` (`/api/buses`), `VehicleTypesController`/`VehicleTypeController` (`/api/vehicle-types`).
- **Entradas/salidas:** REST; cliente `IamGrpcClient` a IAM para perfil/nombre y servidor `route.v1.RouteService` para rutas.
- **Dependencias:** EF Core, AutoMapper, cliente HTTP tipado.
- **Reglas de negocio:** placa única; un bus tiene un GPS único y una capacidad; la asignación de conductor tiene vigencia (`AssignedFrom`/`AssignedTo`).
- **Corrección del diagnóstico anterior:** `Grpc.AspNetCore` y `Protos/profile.proto` sí participan en la integración actual; no se sostiene el hallazgo de gRPC sin uso.

### 6.5 ms-route (rutas y viajes)

- **Responsabilidad:** rutas, paradas, asignaciones (bus, estudiantes), programación, ejecuciones y viajes.
- **Casos de uso/controladores:** `RouteController`, `RouteDetailController`, `RouteAssignmentController`, `StopController`, `TripController`, `CityController`; **servidor gRPC** `route.RouteService` (16 RPC).
- **Entradas/salidas:** REST y gRPC; clientes `FleetGrpcClient` y `FamilyGrpcClient` (sección 7.4).
- **Dependencias:** EF Core, AutoMapper, Grpc.AspNetCore, cliente HTTP tipado.
- **Reglas de negocio:** las paradas de una ruta tienen orden (`OrderSequence`); un viaje (`RouteExecution`) asocia bus y conductor; `GET /api/routes/current` devuelve la ruta vigente del usuario según el token.
- **Hallazgo:** `Confluent.Kafka` referenciado pero **no usado** en el código.

### 6.6 ms-notification (notificaciones)

- **Responsabilidad:** registro de escaneos de abordaje y notificación a acudientes (push Expo); almacén de tokens de dispositivos.
- **Casos de uso/controladores:** `ScanController`, `GuardianNotificationController`, `DeviceController`, `IncidentReportController`; `ProcessScanService`, `ReportIncidentService`; consumidores `StudentScannedConsumer` e `IncidentReportedConsumer`.
- **Entradas/salidas:** REST bajo `/api/notifications`; Kafka; clientes gRPC Family/Route y servidor Boarding; HTTPS a Expo.
- **Dependencias:** EF Core, Confluent.Kafka, HttpClient.
- **Reglas de negocio:** se persiste alerta antes de publicar; el consumidor resuelve destinatarios e intenta push. Ni persistencia ni procesamiento garantizan entrega (sección 7.5).
- **Hallazgo:** `MapOpenApi()`/`MapScalarApiReference()` se registran dos veces (duplicación).

### 6.7 sg-ms-forgot-information (recuperación y cambio de datos)

- **Responsabilidad:** recuperación de contraseña anónima y flujos de cambio de correo y de teléfono mediante OTP.
- **Casos de uso/controladores:** `PasswordController` (`/api/v1/password/{forgot,forgot/verify,reset}`), `EmailChangeController` (`/api/v1/email/change/*`), `PhoneChangeController` (`/api/v1/phone/change/*`), servicio `VerificationCodeService`.
- **Entradas/salidas:** REST con OTP sin JWT local (email/teléfono protegidos por Kong); `IdentityDirectoryGrpcClient` a IAM con metadata `x-internal-api-key`; SMTP y Twilio.
- **Dependencias:** EF Core, HttpClient, Twilio, SMTP.
- **Reglas de negocio:** los códigos OTP se validan con *pepper* (`OTP_HASH_PEPPER`); límite de 10 req/min por IP y un limitador interno por perfil; los flujos de teléfono responden `503` si Twilio no está configurado; validación *fail-fast* de SMTP/clave interna fuera de `Development`.

### 6.8 sg-ms-gps (ingesta GPS)

- **Responsabilidad:** recibir tramas binarias de rastreadores GPS por **TCP** y exponer la última posición por REST.
- **Componentes clave:** `GpsTcpServer` (puerto TCP **8842**), `GpsFrameDecoder`, `GpsPacketRouter`, handlers por protocolo (`0x01` login, `0x13` heartbeat, `0x31` ubicación, `0x32` alarma, `0x50` LBS, `0x80` comando, `79 79` legacy), `GpsLocationService`/`GpsDeviceService` en memoria, `GpsAckService` (ACK con CRC-16).
- **Entradas/salidas:** TCP y REST `GET /api/gps/devices/{imei}`, `/{imei}/connected`, `/{imei}/location`; no se encontró `history` en los controladores inspeccionados.
- **Dependencias:** Spring Web MVC (el DataSource/JPA están **desactivados** en `application.properties`).
- **Limitación:** la persistencia es **en memoria** (mapas concurrentes), no SQL Server; REST 8080 y TCP 8842; `sg-ms-gps/Dockerfile` sí existe (Java 21). Esto no acredita una imagen desplegada.

### 6.9 API Gateway (Kong) y Kafka

- **Kong:** declara rutas a los ocho backend, incluido GPS; `sg-jwt` global, CORS común por servicio y límites específicos de IAM/login (no un límite común a todos).
- **Kafka:** nueve tópicos declarados, incluidos `admin.school.updated`, `user.status.changed` e `incident.reported` (sección 7.5); broker activo no comprobado.

### 6.10 Frontend web y móvil

- **Web (Angular 21):** SPA con *roleGuard*; rutas públicas (home, contacto, login, recuperación, términos, privacidad), de administrador (usuarios, flota, rutas, perfil, cambios de datos) y de superadministrador (admins, colegios). Servicios REST por dominio contra Kong. SSR/pre-render configurable; i18n con 4 idiomas (es, en, fr, pt); interceptor que añade el token y renueva ante 401.
- **Móvil (React Native/Expo):** login, datos personales, familia, notificaciones, ruta, perfil, seguridad, recuperación, idioma y apariencia; consumo de API mediante `apiClient` con renovación de token; mapa (`react-native-maps`), cámara/QR (`expo-camera`, `react-native-qrcode-svg`) y ubicación (`expo-location`).
- **Hallazgos:** no hay *lazy loading* real en la web (todos los componentes se importan directamente); `core/interceptors/auth.interceptor.ts` está **vacío** (el interceptor real es `core/services/auth.interceptor.ts`).

---

## 7. Documentación de APIs e integraciones

### 7.1 Endpoints

La puerta de entrada pública es **Kong** en `http://<host>:8000`. Kong enruta por prefijo; los servicios reciben las peticiones en su puerto interno `8080`. La tabla siguiente resume los servicios expuestos y su prefijo.

| Servicio | Prefijo en Kong | Upstream |
|---|---|---|
| ms-iam | `/api/v1/auth` (strip_path = false) | `http://ms-iam:8080` |
| ms-user-management | `/user` | `http://ms-user-management:8080` |
| ms-fleet | `/fleet` | `http://ms-fleet:8080` |
| ms-school-management | `/school-management` | `http://ms-school-management:8080` |
| ms-notification | `/notification` (GET/POST/PUT/PATCH/DELETE/OPTIONS) | `http://ms-notification:8080` |
| ms-route | `/route`, `/trip` | `http://ms-route:8080` |
| ms-forgot-information | `/api/v1/password`, `/api/v1/email/change`, `/api/v1/phone/change` (strip_path = false) | `http://ms-forgot-information:8080` |
| ms-gps | `/api/gps` (strip_path = false) | `http://ms-gps:8080` |

**Borde verificado en configuración, no en producción.** `sg-ms-api-gateway/kong/kong.yml:13–24` enumera `/api/v1/auth/login`, `/refresh`, `/logout`, `/health` (cada uno bajo `/api/v1/auth`), `/api/v1/password` y `/.well-known/jwks.json` como públicos. Son entradas concretas, **no** todo `/api/v1/auth`; `handler.lua:79–85` las compara por **prefijo**, no por igualdad estricta. `/profile`, `/terms/*`, email/teléfono y GPS requieren access token en el borde. CORS reutiliza la lista explícita de orígenes, credenciales y métodos/headers de `kong.yml:66–103`; no equivale a autorización. El plugin JWT no contiene excepción explícita para OPTIONS: validar preflight y orden de plugins en entorno autorizado antes de concluir compatibilidad del navegador. La ruta JWKS GET usa `strip_path=false`.

**ms-iam.**

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/auth/profile` | Perfil del usuario autenticado. |
| POST | `/api/v1/auth/login` | Inicia sesión; devuelve access token y cookie de refresh. |
| POST | `/api/v1/auth/refresh` | Renueva el access token usando la cookie. |
| POST | `/api/v1/auth/logout` | Cierra sesión e invalida el token. |
| GET | `/api/v1/auth/terms/status` | Estado de aceptación de términos. |
| GET | `/api/v1/auth/terms/acceptances` | Historial de aceptaciones. |
| POST | `/api/v1/auth/terms/accept` | Acepta términos vigentes. |

**ms-user-management** (prefijo `/user`).

| Método | Ruta | Descripción |
|---|---|---|
| POST/GET | `/api/admins` | Crea / lista administradores. |
| GET/PUT/DELETE | `/api/admins/{id}` | Consulta / actualiza / elimina administrador. |
| GET | `/api/admins/search` | Búsqueda. |
| POST/GET | `/api/drivers` · `/api/parents` · `/api/students` | Mismos verbos que admins. |
| GET/PUT/DELETE | `/api/drivers/{id}` · `/api/parents/{id}` · `/api/students/{id}` | Detalle/actualización/eliminación. |
| POST/GET | `/api/driver-licenses` | Crea / lista licencias. |
| GET/PUT/DELETE | `/api/driver-licenses/{id}` | Detalle/actualización/eliminación. |
| POST/GET | `/api/families` | Crea / lista familias. |
| GET | `/api/families/search` · `/api/families/{id}` | Búsqueda / detalle. |
| GET | `/api/families/student/{studentProfileId}/members` | Miembros acudientes de un estudiante. |
| PUT/DELETE | `/api/families/{id}` | Actualiza / elimina familia. |
| GET | `/api/profiles/{id}/exists` · `/api/profiles/{id}/name` | (Interno) Existencia y nombre de perfil. |

**ms-school-management** (prefijo `/school-management`).

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/schools` · `/api/v1/schools/with-campuses` | Crea colegio (con o sin sedes). |
| GET | `/api/v1/schools` · `/api/v1/schools/search` · `/api/v1/schools/{id}` | Lista / búsqueda / detalle. |
| GET | `/api/v1/schools/{id}/campuses` · `/api/v1/schools/{schoolId}/campuses` | Sedes del colegio. |
| PUT | `/api/v1/schools/{id}` · `/with-campuses` | Actualiza colegio. |
| DELETE | `/api/v1/schools/{id}` | Elimina colegio. |

**ms-fleet** (prefijo `/fleet`).

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/vehicle-types/brands` · `/api/vehicle-types/models` | Marcas y modelos. |
| POST/GET | `/api/buses` | Crea / lista buses. |
| GET | `/api/buses/search` · `/api/buses/{id}` · `/api/buses/assigned/{profileId}` | Búsqueda / detalle / buses de un conductor. |
| PUT/PATCH/DELETE | `/api/buses/{id}` · `/api/buses/{id}/status` | Actualiza / cambia estado / elimina. |
| PUT/DELETE | `/api/buses/{busId}/driver` | Asigna / retira conductor. |

**ms-route** (prefijo `/route`).

| Método | Ruta | Descripción |
|---|---|---|
| POST/GET | `/api/routes` · `/api/routes/search` · `/api/routes/{id}` | Crea / lista / busca / detalle de rutas. |
| PUT/DELETE | `/api/routes/{id}` | Actualiza / elimina ruta. |
| GET | `/api/routes/current` · `/api/routes/today` · `/api/routes/student/{id}` | Ruta vigente / del día / de un estudiante. |
| POST | `/api/routes/{routeId}/stops` · `/api/routes/{routeId}/students` | Añade parada / estudiante. |
| PUT | `/api/routes/{routeId}/bus` | Asigna bus a la ruta. |
| GET | `/api/routes/{routeId}/students/{studentProfileId}` | Verifica asignación de estudiante. |
| POST/GET | `/api/stops` · `/api/stops/search` · `/api/stops/{id}` | CRUD de paradas. |
| POST | `/api/trips/start` · `/api/trips/end` | Inicia / finaliza viaje. |
| GET | `/api/trips/current` | Viaje en curso. |
| GET | `/api/cities` | Ciudades. |

**ms-notification** (prefijo `/notification`).

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/notifications/scan` | Registra un escaneo (abordaje) y dispara notificación. |
| GET | `/api/v1/notifications/guardian/{profileId}` | Notificaciones de un acudiente. |
| POST | `/api/v1/notifications/devices` | Registra token de dispositivo (Expo). |

**sg-ms-forgot-information** (prefijo `/api/v1/password` externo; internamente `/api/v1/{password,email/change,phone/change}`).

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/password/forgot` | Solicita OTP de recuperación. |
| POST | `/api/v1/password/forgot/verify` | Verifica OTP. |
| POST | `/api/v1/password/reset` | Restablece contraseña. |
| POST | `/api/v1/email/change/request` · `/verify` · `/new` · `/resend-new` · `/verify-new` · `/confirm` | Flujo de cambio de correo. |
| POST | `/api/v1/phone/change/request` · `/verify` · `/verification/request` · `/verification/resend` · `/verification/check` | Flujo de cambio de teléfono. |
| GET | `/health` | Salud del servicio. |

**sg-ms-gps.**

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/gps/devices/{imei}` | Estado del dispositivo. |
| GET | `/api/gps/devices/{imei}/connected` | ¿Está conectado? |
| GET | `/api/gps/devices/{imei}/location` | Última ubicación. |
| POST | `/api/gps/test/{imei}` | Inyección de prueba. |

**Ejemplo de petición y respuesta — inicio de sesión.**

Solicitud:

```json
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "admin@colegio.edu.co",
  "password": "Adm1n!Segura"
}
```

Respuesta `200 OK`:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3YzE...",
  "expiresIn": 3600,
  "roleId": 1,
  "personId": "3f2b8c10-...",
  "email": "admin@colegio.edu.co",
  "schoolId": "a1b2c3d4-..."
}
```

La respuesta establece la cookie `refresh_token` (`HttpOnly`, `Secure`, `SameSite=None`, ruta `/api/v1/auth/refresh`, Max-Age derivado de la expiración; `RefreshCookieSupport.java:40–45`). El ejemplo no es una captura de producción.

**Ejemplo de petición y respuesta — creación de un bus (a través de Kong).**

Solicitud:

```json
POST /fleet/api/buses
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "plate": "ABC123",
  "capacity": 40,
  "modelId": 3,
  "campuseId": "9d8c7b6a-...",
  "gpsDeviceId": "1a2b3c4d-...",
  "soatValidity": "2027-05-31"
}
```

Respuesta `201 Created`:

```json
{
  "id": "b7e6d5c4-...",
  "plate": "ABC123",
  "capacity": 40,
  "modelId": 3,
  "status": "Active"
}
```

**Ejemplo de respuesta de error (RFC 7807).**

```json
{
  "type": "https://httpstatuses.com/400",
  "title": "Solicitud inválida",
  "status": 400,
  "detail": "La placa 'ABC123' ya está registrada.",
  "instance": "/fleet/api/buses"
}
```

### 7.2 Códigos de estado y errores

El backend adopta un esquema REST convencional; los errores de aplicación se exponen como **Problem Details (RFC 7807)** y el cliente web los interpreta mediante `core/http/problem-detail.ts`.

| Código | Significado | Uso típico |
|---|---|---|
| 200 | OK | Consulta/actualización exitosa. |
| 201 | Created | Recurso creado (buses, rutas, estudiantes…). |
| 204 | No Content | Eliminación exitosa. |
| 400 | Bad Request | Validación de entrada fallida. |
| 401 | Unauthorized | Token ausente/expirado (el cliente intenta *refresh*). |
| 403 | Forbidden | Rol/permiso insuficiente o clave interna inválida. |
| 404 | Not Found | Recurso inexistente. |
| 409 | Conflict | Duplicado (placa, identificación, correo). |
| 429 | Too Many Requests | Límite de tasa excedido (Kong o middleware). |
| 500 | Internal Server Error | Error no controlado. |
| 503 | Service Unavailable | Dependencia externa no configurada (p. ej. Twilio). |

### 7.3 Autenticación y autorización

- **Emisor:** **ms-iam**, firma **RS256**, cabecera `kid`, claves públicas en `GET /.well-known/jwks.json`. `RsaKeyProvider` carga el par configurado o genera uno efímero si falta; reinicios y réplicas requieren gestión de claves estable.
- **Claims:** `iss`, `aud`, `sub` (ProfileId), `personId`, `roleId`, `role`, `type=access`, `jti`, `permissions` y, cuando corresponda, `email`, `documentNumber`, `campusId`, `schoolId`. Refresh incluye `type=refresh` y `jti`; no sirve como access token.
- **Vigencia:** constructor y `application.properties:55–56`: access **60 minutos**, refresh **7 días**; no comprobados en ejecución. Cookie `HttpOnly`, `Secure`, `SameSite=None` (`RefreshCookieSupport.java:40–45`); no atribuirle la mitigación CSRF de `Strict`.
- **Revocación:** lista de tokens denegados **en memoria** (`InMemoryTokenDenyList`); no se comparte entre réplicas (limitación de escalado).
- **Cinco servicios .NET de negocio:** flota, rutas, colegios, usuarios y notificaciones registran `JwtBearer`, `UseAuthentication`, `UseAuthorization` y `MapControllers().RequireAuthorization()`. Cada `JwtAuthenticationExtensions` exige firma RS256/JWKS, issuer, audience, expiración y `type=access`; no confía en `X-User-*` como identidad.
- **Permisos/roles:** filtros `RequirePermission` verifican `permissions` (p. ej. buses/estudiantes/rutas); `RequireRole` verifica `roleId` (p. ej. colegios y notificaciones); `RequireProfile` exige perfil válido. Devuelven 401 sin identidad y 403 por permiso/rol insuficiente. No se certifica cobertura exhaustiva ni aislamiento multitenant por la sola presencia del filtro.
- **Excepción de recuperación:** `sg-ms-forgot-information/Program.cs` no registra JWT ni autorización global. Sus controladores usan OTP y limitadores; Kong permite anónimamente el prefijo `/api/v1/password`, pero exige JWT en `/api/v1/email/change` y `/api/v1/phone/change`. Protección en el borde y protección local no son equivalentes.
- **Autorización interna gRPC:** IAM protege `ProfileService` e `InternalProfileService` mediante metadata `x-internal-api-key` y `InternalApiKeyInterceptor`; flota aplica interceptor al servidor gRPC. No se extiende automáticamente la autorización de controladores a `MapGrpcService`.
- **Roles del sistema:** `ADMIN=1`, `STUDENT=2`, `DRIVER=3`, `PARENT=4`, `SUPER_ADMIN=5` (semillas `001-insert-roles.sql` y `002-insert-rol-super-admin.sql`). El frontend aplica `roleGuard` para restringir rutas por rol.

### 7.4 Integraciones con sistemas externos

| Integración | Protocolo / Formato | Credenciales | Notas |
|---|---|---|---|
| Rastreadores GPS → sg-ms-gps | TCP binario (protocolo propietario, tramas con CRC-16) | — | Puertos `0x01` login, `0x13` heartbeat, `0x31` ubicación, `0x32` alarma, `0x50` LBS, `0x80` comando, `79 79` legacy. |
| Servicios ↔ Kafka | TCP Kafka (KRaft) | — | Tópicos de dominio (sección 7.5). |
| ms-user-management → ms-school-management | gRPC (HTTP/2) | — | `http://ms-school-management:5001`. |
| ms-fleet → ms-iam | gRPC | `x-internal-api-key` | `usermanagement.v1.ProfileService`: existencia y nombre. |
| ms-route → ms-fleet | gRPC | `x-internal-api-key` | `route.v1.RouteService`: bus asignado y detalles (2 RPC). |
| ms-route / ms-notification → ms-user-management | gRPC | No interceptor de clave verificado | `family.FamilyService`: miembros; servidor también registra familia (2 RPC). |
| ms-notification → ms-route | gRPC | No interceptor de clave verificado | `route.RouteService` (16 RPC en servidor; copia del cliente con 15, sin `GetTripStatus`). |
| sg-ms-forgot-information → ms-iam | gRPC | `x-internal-api-key` | `iam.v1.InternalProfileService`: búsqueda, teléfono y cambios (5 RPC). |
| Cliente no localizado → ms-notification | gRPC | Revisar exposición interna | `boarding.BoardingService.GetAlightedCountAtStop`, registrado como servidor. |
| ms-notification → Expo | HTTPS/JSON | — | `https://exp.host/--/api/v2/push/send`. |
| sg-ms-forgot-information → Twilio | HTTPS/JSON | `TWILIO_*` | Verificación por SMS; 503 si no está configurado. |
| sg-ms-forgot-information → SMTP | SMTP/TLS | `SMTP_*` | Envío de códigos OTP por correo. |
| Clientes → Edge | HTTPS (Kong :8443) | — | Kong expone el API Gateway y el servicio de contraseñas. |

**Gestión de credenciales:** las credenciales externas se inyectan por **variables de entorno** y **no** se exponen a los clientes. La sección 8.4 y la sección 9 detallan el estado de la gestión de secretos y los hallazgos asociados.

**Límite de arquitectura observado:** no toda lectura entre dominios usa gRPC. `sg-ms-notification/src/ms-notification.Api/Infrastructure/Persistence/Directory/{StudentDirectory,SchoolDirectory}.cs` consulta perfiles/personas y une perfiles, sedes y administradores; `NotificationContext.cs:49–64` mapea tablas `Iam`, `UserManagement` y `School`. En rutas, `TripBoardingQueryRepository.cs:41–54` intenta consultar EF y retorna cero al fallar, no llama al servidor Boarding. La existencia de ese servidor no prueba su uso ni éxito del conteo.

### 7.5 Mensajería (Kafka)

| Tópico | Productor | Consumidor | Propósito |
|---|---|---|---|
| `student.created` | ms-user-management | ms-iam | Sincronizar perfil del estudiante. |
| `driver.created` | ms-user-management | ms-iam | Sincronizar perfil del conductor. |
| `parent.created` | ms-user-management | ms-iam | Sincronizar perfil del acudiente. |
| `admin.created` | ms-user-management | ms-iam | Sincronizar perfil del administrador. |
| `admin.school.updated` | ms-user-management | ms-iam | Actualizar vínculo admin↔colegio. |
| `user.status.changed` | ms-user-management | ms-iam | Sincronizar estado de cuenta. |
| `profile.created` | ms-iam | *(ninguno en el repo)* | Notifica creación de perfil (sin consumidor). |
| `student.scanned` | ms-notification | ms-notification | Dispara la notificación de abordaje. |
| `incident.reported` | ms-notification | ms-notification | Intento de push de incidente a destinatarios resueltos. |

Los **nueve** tópicos están declarados en `broker-kafka/init/topics.conf:6–20`, con 1 partición y replicación 1; declaración no prueba creación en un broker activo. .NET serializa con `System.Text.Json` (PascalCase, enums numéricos por defecto); IAM asigna explícitamente nombres PascalCase con `JsonProperty`.

| Payload real | Campos |
|---|---|
| `*.created` de personas | `EventId`, `PersonId`, `Email`, `IdentificationNumber`, `SchoolId`, `CampusId` |
| `admin.school.updated` / `user.status.changed` | `EventId`, `PersonId`, `SchoolId` / `EventId`, `PersonId`, `Email`, `Status` (texto) |
| `profile.created` | `EventId`, `ProfileId`, `PersonId`, `RoleId` |
| `student.scanned` | `BoardingId`, `AlertId`, `StudentProfileId`, `RouteExecutionId`, `BoardingType`, `Timestamp`, `StopName`, `StopAddress`, `Latitude`, `Longitude` |
| `incident.reported` | `AlertId`, `RouteExecutionId`, `ReportedByProfileId`, `AlertTypeId`, `Description`, `StudentsOnBoard`, `Recipients`, `Timestamp` |

**Procesamiento y límites.** IAM usa grupo `iam`; notificaciones usa `ms-notification-consumer` y `ms-notification-incident-consumer`, ambos `Earliest`. Los publicadores .NET esperan `ProduceAsync` con clave GUID aleatoria, no clave por entidad; IAM invoca `KafkaTemplate.send` sin esperar su resultado asíncrono. No se observó outbox transaccional, DLQ o registro de deduplicación por `EventId` en estos adaptadores. IAM captura/loguea errores del caso de uso y retorna; notificaciones captura errores y no configura commit manual ligado al éxito. Por ello no se promete reintento fiable, exactamente una vez ni orden por entidad fuera de la configuración declarada.

`CreateProfileService` rechaza persona existente (no es un upsert). `ProcessScanService` evita repetir un tipo explícito cuando ya es el estado vigente; sin tipo explícito alterna según estado, por lo que no equivale a idempotencia por solicitud. El consumidor de escaneos evita recrear destinatarios existentes, **pero vuelve a intentar push** al reprocesar. El de incidentes no registra una deduplicación persistente.

**Estados distintos, sin garantía de entrega:** (1) `Boarding`/`Alert` persistidos antes de publicar; (2) evento procesado por el consumidor; (3) envío HTTP a Expo intentado si hay tokens; (4) recibo de entrega al dispositivo, **no implementado/verificado aquí**. `ExpoPushNotifier` envía lotes de 100 y registra errores, pero no procesa tickets/recibos. `AlertRecipient.DateTimeRead` se inicializa al crear la fila: no demuestra lectura humana ni entrega. Un fallo entre guardado y publicación puede dejar alerta sin evento; éxito HTTP de la API o de Expo tampoco demuestra recepción.

**Evidencia local (rutas relativas a `Proyecto`).** `sg-ms-iam/src/main/java/com/school_guardian/ms_iam/infrastructure/{security/JwtTokenProvider.java:37–107,web/JwksController.java:21–23,messaging/PersonCreatedConsumer.java:19–47,config/KafkaConsumerConfig.java:31–81,grpc/GrpcServerLifecycle.java:53–57}`; `sg-ms-{fleet,route,school-management,user-management,notification}/src/*/Program.cs` y sus `Infrastructure/Auth/JwtAuthenticationExtensions.cs` (usuarios: `Shared/Infrastructure/Auth`); `sg-ms-notification/src/ms-notification.Api/{Application/UseCase/ProcessScanService.cs:153–175,Application/UseCase/ReportIncidentService.cs:101–108,Infrastructure/Messaging/Kafka/Consumer,Infrastructure/Messaging/Push/ExpoPushNotifier.cs:49–81}`. Contratos `.proto` de cada servicio son evidencia de RPC/mensajes, no de conectividad ni autenticación en ejecución. La matriz consolidada del catálogo distingue ambos `RouteService`.

---

## 8. Configuración

### 8.1 Archivos de configuración y su propósito

| Archivo | Ámbito | Propósito |
|---|---|---|
| `appsettings.json` / `appsettings.Development.json` | Servicios .NET | Configuración (cadenas de conexión, JWT, URLs internas, Kestrel). |
| `.env` / `.env.example` | Cada servicio y la BD | Variables de entorno (se inyectan al contenedor). |
| `docker-compose.yml` / `docker-compose.yaml` | Cada servicio, Kafka y BD | Definición de contenedores, puertos, redes y variables. |
| `application.properties` | ms-iam y sg-ms-gps | Configuración Spring (datasource, JPA, JWT, Kafka). |
| `launchSettings.json` | Servicios .NET (algunos) | Perfiles de ejecución local y puertos de desarrollo. |
| `kong/kong.yml` | ms-api-gateway | Config declarativa DB-less de Kong (servicios, rutas, plugins). |
| `broker-kafka/init/topics.conf` + `create-topics.sh` | Kafka | Creación de tópicos al iniciar. |
| `.env`, `environment.ts`, `angular.json` | Web Angular | URL de API y configuración de build. |
| `.env`, `app.json`, `eas.json` | Móvil Expo | URL de API, permisos y perfiles EAS. |

### 8.2 Variables de entorno

| Variable | Servicio(s) | Descripción | Valor por defecto | ¿Obligatoria? |
|---|---|---|---|---|
| `ConnectionStrings__DefaultConnection` | Servicios .NET | Cadena de conexión SQL Server (`school-guardian`). | — | Sí |
| `Kafka__BootstrapServers` | Varios .NET | Dirección del broker Kafka. | `kafka:9092` (según compose) | Según servicio |
| `JWT_PRIVATE_KEY` / `JWT_PUBLIC_KEY` / `JWT_KEY_ID` | ms-iam | Par RSA y `kid` (no divulgar valores). | Par efímero si falta; `sg-iam` | Par estable necesario para reinicios/réplicas |
| `Jwt__JwksUrl` / `Jwt__Issuer` / `Jwt__Audience` | Cinco .NET de negocio | Validación local RS256. | JWKS de IAM; `school-guardian-iam`; `school-guardian-api` | Opciones validadas al inicio |
| `Grpc__Family__Address` / `Grpc__Route__Address` | ms-notification | Direcciones gRPC Family/Route. | — | Sí |
| `Iam__GrpcAddress` | ms-fleet | Servidor gRPC de perfiles IAM. | — | Sí |
| `Fleet__GrpcAddress` / `Family__GrpcAddress` | ms-route | Direcciones gRPC flota/familias. | — | Sí |
| `IdentityDirectory__GrpcAddress` | sg-ms-forgot-information | Directorio interno IAM por gRPC. | — | Sí |
| `SchoolManagement__GrpcAddress` | ms-user-management | Dirección gRPC de ms-school-management. | `http://ms-school-management:5001` | Sí |
| `APP_INTERNAL_API_KEY` / `INTERNAL_API_KEY` | ms-iam / forgot-information | Clave de API interna. | — | Sí (ms-iam la exige) |
| `OTP_HASH_PEPPER` | sg-ms-forgot-information | *Pepper* para hashing de OTP. | — | Sí |
| `SMTP__*` (host, port, user, password) | sg-ms-forgot-information | Servidor de correo. | — | Sí en prod |
| `TWILIO_*` (account Sid, api key, secret) | sg-ms-forgot-information | Verificación SMS. | — | No (503 si falta) |
| `CORS_ALLOWED_ORIGIN` | sg-ms-forgot-information | Origen CORS permitido. | `http://localhost:4200` | No |
| `MSSQL_DATABASE` / `MSSQL_USER` / `MSSQL_SA_PASSWORD` | ms-iam / BD | Parámetros de SQL Server. | — | Sí |
| `KAFKA_BOOTSTRAP_SERVERS` | ms-iam | Broker para Spring Kafka. | — | Sí |
| `APP_URL` / `ASPNETCORE_URLS` | Servicios .NET | URL/endpoint de escucha. | `:8080` | No |
| `API_URL` | Web Angular | URL del gateway (Kong). | `http://localhost:8000` | Sí |
| `EXPO_PUBLIC_API_URL` | Móvil | URL pública del backend (túnel). | — | Sí |
| `Kestrel__Endpoints__*` | ms-route / ms-school-management | Endpoints HTTP/1 y HTTP/2 (gRPC). | `8080` / `5001` | No |

### 8.3 Configuración por ambiente

| Ambiente | Red | Base de datos | Gateway | Observaciones |
|---|---|---|---|---|
| **Desarrollo** | Docker Compose + red `sg-services-network` | SQL Server 2022 en contenedor (`1433`), base `school-guardian` | Kong `:8000` | Topología declarada; claves RSA configuradas o efímeras, sin comprobar valores efectivos. |
| **Pruebas** | Igual que desarrollo; datos aislados | Instancia separada o esquema recreado por Liquibase | Kong | Se usa en la verificación posdespliegue. |
| **Producción** | Orquestación (Docker/Kubernetes; K8s es aspiracional) | SQL Server gestionado con respaldos; TLS | Kong con TLS | Requiere secretos reales, HTTPS/TLS 1.2+, escalado y observabilidad. |

La aplicación **se configura exclusivamente por variables de entorno** (RNF-07), lo que permite promover el mismo artefacto entre ambientes.

### 8.4 Gestión de secretos

El diseño prevé inyección de secretos por entorno. **Antecedente no revalidado:** la versión 1.0 registró credenciales en `.env`; esta revisión no leyó esos archivos ni certifica que sus valores sean reales, vigentes o comprometidos. Si se confirma exposición, rotar mediante el proceso autorizado y usar un gestor de secretos; no copiar valores a documentación ni informes.

---

## 9. Seguridad

### 9.1 Roles, permisos y control de acceso

- **Roles de negocio:** `SUPER_ADMIN` (operador de plataforma), `ADMIN` (administrador escolar), `DRIVER` (conductor), `PARENT` (acudiente) y `STUDENT` (estudiante). La semilla de la base fija sus identificadores 1–5.
- **Control de acceso en el cliente:** el `roleGuard` de la web restringe rutas por rol (`dashboard-admin` para ADMIN/SUPER_ADMIN, `dashboard-superadmin` y gestión de colegios/admins solo SUPER_ADMIN).
- **Control de acceso en el backend:** el JWT identifica al usuario y su colegio; los servicios filtran los datos por **tenant** (`ITenantProvider`). El aislamiento entre colegios depende de esta resolución del tenant a partir del token.
- **Autorización entre servicios:** clave interna (`X-Internal-Api-Key`) para los endpoints sensibles de ms-iam.
- **Controles y excepción:** los cinco backend .NET de negocio exigen autorización global y filtros de permisos/roles; recuperación no tiene JWT local (sección 7.3). Queda pendiente comprobar cobertura, aislamiento, red interna y configuración del despliegue.

### 9.2 Cifrado de datos y comunicaciones

- **Contraseñas:** se almacenan con **BCrypt** (`PasswordEncoderConfig` en ms-iam) y se validan contra una política (longitud 8–72 y combinación de mayúscula, minúscula, dígito y símbolo).
- **Tokens:** access RS256 y refresh en cookie `HttpOnly`/`Secure`/`SameSite=None`; validar controles CSRF y uso móvil por separado, sin atribuir protección `Strict`.
- **OTP:** los códigos se almacenan hasheados con *pepper* (`OTP_HASH_PEPPER`).
- **Transporte:** se prevé HTTPS/TLS 1.2+ (RNF-04); en desarrollo el tráfico interno es HTTP dentro de la red Docker y el acceso externo se realiza mediante Kong con TLS en el borde (:8443).
- **Datos de menores (PII):** el SRS incluye requisitos de protección de datos personales (Ley 1581 de 2012 y Decreto 1377 de 2013) y la aceptación de términos con consentimiento parental (`TermsAcceptance` con `StudentProfileId`).

### 9.3 Auditoría y registro de actividad

- El diseño original contemplaba un microservicio de **auditoría** (esquema `Audit` y carpeta `ms-audit-db`), pero **fue eliminado** del proyecto (la carpeta está vacía y no hay esquema `Audit`). Por tanto, **no existe un servicio de auditoría dedicado** en la versión actual.
- Persisten mecanismos parciales de trazabilidad: `SessionProfile` (sesiones con IP y vigencia), `TermsAcceptance` (aceptaciones con IP y canal), `AlertRecipient` (lectura de alertas) y los campos de fecha/hora en las entidades.
- Los servicios registran mediante el sistema de logging estándar (.NET `ILogger`/Serilog y Spring `Logback`); el **formato JSON con *correlation ID*** está previsto en el requisito de observabilidad (RNF-05) pero **no se verificó** su implementación en el código.

### 9.4 Buenas prácticas y vulnerabilidades mitigadas

**Buenas prácticas aplicadas.**

- Arquitectura hexagonal y separación de responsabilidades (superficie de ataque y de cambio reducida).
- Hashing de contraseñas con BCrypt y política de complejidad.
- Validación de clave interna en **tiempo constante** (`MessageDigest.isEqual`) para evitar *timing attacks*.
- Cookies de refresco `HttpOnly`+`Secure`+`SameSite=None` (no prueba de mitigación CSRF).
- **Rate limiting** según alcance: Kong IAM 300/min por cabecera Authorization, login 20/min y 200/h por IP; recuperación aplica `otp-public` 10/min por IP en el servicio. No se verificó un límite Kong de recuperación.
- Manejo uniforme de errores con Problem Details (evita filtración de *stack traces* al cliente).
- Validación de entrada (Spring Validation / DataAnnotations) y CORS restringido por origen.
- Contenedores multi-stage con imágenes base oficiales y mínimas (runtime sin SDK).

**Vulnerabilidades y riesgos identificados (deuda técnica).**

| # | Hallazgo | Riesgo | Recomendación |
|---|---|---|---|
| 1 | Antecedente 1.0 de secretos en `.env`, no revalidado | Exposición potencial, no confirmada aquí | Investigar sin divulgar valores y rotar si se confirma. |
| 2 | Claves RSA efímeras si falta el par configurado (`RsaKeyProvider`) | Tokens inválidos tras reinicio y claves divergentes entre réplicas | Gestionar par RSA y rotación estable; no usar la antigua recomendación `JWT_SECRET` de HS256. |
| 3 | *Deny-list* de tokens en memoria | Logout no efectivo con múltiples réplicas | Migrar a un almacén compartido (p. ej., base de datos o memoria distribuida). |
| 4 | Hallazgo anterior cerrado en código: autorización global en cinco backend .NET | Recuperación y gRPC tienen controles distintos; cobertura no certificada | Validar límites y permisos efectivos, sin volver a proponer autorización como ausente. |
| 5 | Hallazgo anterior cerrado en configuración: `/api/gps` protegido en Kong | Despliegue/exposición directa no comprobados | Validar imagen y red antes de afirmar protección operativa. |
| 6 | Hallazgo anterior cerrado en código: RS256/JWKS | Gestión de claves y rotación pendientes de validación operacional | Verificar configuración estable y renovación de JWKS. |
| 7 | Diagnóstico anterior de gRPC IAM ausente superado | Servidores y clientes no implican conectividad efectiva | Contrastar contratos y controles por enlace (sección 7.4). |
| 8 | Sin *outbox* transaccional en Kafka | Eventos perdidos si falla el broker tras el commit | Patrón outbox + `acks=all` + DLQ. |
| 9 | Hallazgo anterior cerrado: los nueve tópicos figuran en `topics.conf` | Broker activo no inspeccionado | Comprobar creación/configuración en entorno autorizado. |
| 10 | GPS con persistencia en memoria | Pérdida de datos al reiniciar | Persistir en SQL Server (JPA está desactivado). |

---

## 10. Referencias

Las referencias se presentan en orden alfabético.

Becker, C., & Beck, K. (s. f.). *Conventional Commits 1.0.0*. https://www.conventionalcommits.org/

Fielding, R., & Reschke, J. (2014). *Hypertext Transfer Protocol (HTTP/1.1): Semantics and content* (RFC 7231). Internet Engineering Task Force. https://doi.org/10.17487/RFC7231

Google. (s. f.). *API design guide*. https://cloud.google.com/apis/design

International Organization for Standardization. (2011). *Systems and software quality requirements and evaluation (SQuaRE) — System and software quality models* (ISO/IEC 25010:2011). https://www.iso.org/standard/35733.html

International Organization for Standardization. (2017). *Systems and software engineering — Software life cycle processes* (ISO/IEC/IEEE 12207:2017). https://www.iso.org/standard/63712.html

Internet Engineering Task Force. (2015). *JSON Web Token (JWT)* (RFC 7519). https://doi.org/10.17487/RFC7519

Internet Engineering Task Force. (2016). *Problem Details for HTTP APIs* (RFC 7807). https://doi.org/10.17487/RFC7807

Kong Inc. (s. f.). *Kong Gateway documentation*. https://docs.konghq.com/

Liquibase. (s. f.). *Liquibase documentation*. https://docs.liquibase.com/

Microsoft. (s. f.). *ASP.NET Core documentation*. https://learn.microsoft.com/aspnet/core

Microsoft. (s. f.). *Entity Framework Core documentation*. https://learn.microsoft.com/ef/core

OWASP Foundation. (2021). *OWASP Top 10:2021 — The ten most critical web application security risks*. https://owasp.org/Top10/

Pivotal Software. (s. f.). *Spring Boot reference documentation*. https://docs.spring.io/spring-boot/

Tolk, A. (s. f.). *gRPC documentation*. https://grpc.io/docs/

Apache Software Foundation. (s. f.). *Apache Kafka documentation*. https://kafka.apache.org/documentation/

Apache Software Foundation. (s. f.). *Apache License 2.0*. https://www.apache.org/licenses/LICENSE-2.0

OpenJS Foundation. (s. f.). *Node.js documentation*. https://nodejs.org/docs/

---

*Fin del Manual Técnico — Parte A.*
