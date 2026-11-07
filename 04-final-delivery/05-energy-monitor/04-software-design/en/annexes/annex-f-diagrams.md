# Annex F. Diagrams

Version 1.0 · October 28, 2026. Diagrams in Mermaid, which GitHub draws directly in the browser. They show the system as it is in production.

## 1. Architecture

```mermaid
flowchart TB
    U["Browser and mobile app"] -->|HTTPS| N["Server Nginx"]
    N --> F["Frontend (nginx + SPA)"]
    F -->|"/api/"| G["API gateway :8080"]
    G --> SEC["Security :8082"]
    G --> DEV["Devices :8084"]
    G --> MEA["Measurements :8085"]
    G --> HOM["Homes :8086"]
    G --> NOT["Notifications :8087"]
    G --> ALE["Alerts :8088"]
    G --> REC["Recommendations :8089"]
    SEC & DEV & MEA & HOM & NOT & ALE & REC --> DB[("MySQL 8.4")]
    SEC & DEV & MEA & HOM & NOT & ALE & REC -.->|events| Q[("RabbitMQ")]
    M["ESP32 + PZEM-004T"] -->|"MQTT TLS 8883"| B["Mosquitto"]
    B -->|"authenticates each connection"| DEV
    B -->|telemetry| MEA
```

## 2. Journey of a reading

```mermaid
sequenceDiagram
    participant D as Meter
    participant B as Mosquitto
    participant M as Measurements
    participant Q as RabbitMQ
    participant A as Alerts
    participant N as Notifications
    participant W as Web and mobile
    D->>B: reading every 60 s (TLS)
    B->>M: telemetry
    M->>M: stores the reading
    M->>Q: measurement.recorded
    Q->>A: reading
    A->>A: level, home limit and unusual consumption
    A->>Q: alert.raised
    Q->>N: alert
    N-->>W: mail and push
    W->>M: consumption and history (through the gateway)
```

## 3. Unusual consumption rule

```mermaid
flowchart TD
    L["Reading of a device"] --> H{"Does it have a home?"}
    H -- no --> X["Nothing is done"]
    H -- yes --> P{"24 hours of history and average of 5 W or more?"}
    P -- no --> R["The pending alert is resolved, if any"]
    P -- yes --> C{"At least 200 W and 2.5 times the average?"}
    C -- no --> R
    C -- yes --> E{"Is one already pending?"}
    E -- yes --> X
    E -- no --> A["Unusual consumption alert"]
    A --> N["Mail and push"]
```

## 4. Continuous deployment

```mermaid
flowchart LR
    PR["Pull request to dev"] --> T["Tests in GitHub Actions"]
    T --> M["Merge to main"]
    M --> AP{"Approval of the production environment"}
    AP --> D["deploy.sh over SSH: backup, git pull and rebuild of the service"]
    D --> S{"Does the health check pass?"}
    S -- yes --> OK["In production"]
    S -- no --> RB["Goes back to the previous version"]
```

## 5. Main tables per service

```mermaid
erDiagram
    PERSON ||--o{ USER : "has accounts"
    USER ||--o{ USER_SESSION : opens
    USER }o--o{ SYSTEM_ROLE : "has roles"
    HOME ||--o{ USER_HOME : "has members"
    HOME ||--|| HOME_THRESHOLDS : "has limits"
    HOME_TYPE ||--o{ HOME : classifies
    DEVICE ||--o{ DEVICE_HOME : "is linked"
    DEVICE ||--o{ DEVICE_STATUS_LOG : "logs status"
    DEVICE ||--o{ MEASUREMENT : "sends readings"
    ALERT }o--|| HOME : "warns"
```

Each service owns its tables and there are no foreign keys between services: the relations of the diagram that cross services (for example, the reading and its device) are resolved by identifier and by the internal API.

Spanish version: [../ES/anexo-f-diagramas.md](annex-f-diagrams.md)
