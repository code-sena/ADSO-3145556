# Anexo F. Diagramas

Versión 1.0 · 28 de octubre de 2026. Diagramas en Mermaid, que GitHub dibuja directamente en el navegador. Muestran el sistema tal como está en producción.

## 1. Arquitectura

```mermaid
flowchart TB
    U["Navegador y app móvil"] -->|HTTPS| N["Nginx del servidor"]
    N --> F["Frontend (nginx + SPA)"]
    F -->|"/api/"| G["API gateway :8080"]
    G --> SEC["Seguridad :8082"]
    G --> DEV["Dispositivos :8084"]
    G --> MEA["Mediciones :8085"]
    G --> HOM["Hogares :8086"]
    G --> NOT["Notificaciones :8087"]
    G --> ALE["Alertas :8088"]
    G --> REC["Recomendaciones :8089"]
    SEC & DEV & MEA & HOM & NOT & ALE & REC --> DB[("MySQL 8.4")]
    SEC & DEV & MEA & HOM & NOT & ALE & REC -.->|eventos| Q[("RabbitMQ")]
    M["ESP32 + PZEM-004T"] -->|"MQTT TLS 8883"| B["Mosquitto"]
    B -->|"autentica cada conexión"| DEV
    B -->|telemetría| MEA
```

## 2. Recorrido de una lectura

```mermaid
sequenceDiagram
    participant D as Medidor
    participant B as Mosquitto
    participant M as Mediciones
    participant Q as RabbitMQ
    participant A as Alertas
    participant N as Notificaciones
    participant W as Web y móvil
    D->>B: lectura cada 60 s (TLS)
    B->>M: telemetría
    M->>M: guarda la lectura
    M->>Q: measurement.recorded
    Q->>A: lectura
    A->>A: nivel, límite del hogar y consumo inusual
    A->>Q: alert.raised
    Q->>N: alerta
    N-->>W: correo y push
    W->>M: consumo e historial (por el gateway)
```

## 3. Regla de consumo inusual

```mermaid
flowchart TD
    L["Lectura de un dispositivo"] --> H{"¿Tiene hogar?"}
    H -- no --> X["No se hace nada"]
    H -- sí --> P{"¿24 horas de historial y promedio de 5 W o más?"}
    P -- no --> R["Se resuelve la alerta pendiente, si hay"]
    P -- sí --> C{"¿Al menos 200 W y 2,5 veces el promedio?"}
    C -- no --> R
    C -- sí --> E{"¿Ya hay una pendiente?"}
    E -- sí --> X
    E -- no --> A["Alerta de consumo inusual"]
    A --> N["Correo y push"]
```

## 4. Despliegue continuo

```mermaid
flowchart LR
    PR["Pull request a dev"] --> T["Pruebas en GitHub Actions"]
    T --> M["Fusión a main"]
    M --> AP{"Aprobación del environment production"}
    AP --> D["deploy.sh por SSH: respaldo, git pull y reconstrucción del servicio"]
    D --> S{"¿Pasa la prueba de salud?"}
    S -- sí --> OK["En producción"]
    S -- no --> RB["Vuelve a la versión anterior"]
```

## 5. Tablas principales por servicio

```mermaid
erDiagram
    PERSON ||--o{ USER : "tiene cuentas"
    USER ||--o{ USER_SESSION : abre
    USER }o--o{ SYSTEM_ROLE : "tiene roles"
    HOME ||--o{ USER_HOME : "tiene miembros"
    HOME ||--|| HOME_THRESHOLDS : "tiene límites"
    HOME_TYPE ||--o{ HOME : clasifica
    DEVICE ||--o{ DEVICE_HOME : "se vincula"
    DEVICE ||--o{ DEVICE_STATUS_LOG : "registra estado"
    DEVICE ||--o{ MEASUREMENT : "envía lecturas"
    ALERT }o--|| HOME : "avisa a"
```

Cada servicio es dueño de sus tablas y no hay claves foráneas entre servicios: las relaciones del diagrama que cruzan servicios (por ejemplo, la lectura y su dispositivo) se resuelven por identificador y por la API interna.

Versión en inglés: [../EN/annex-f-diagrams.md](annex-f-diagrams.md)
