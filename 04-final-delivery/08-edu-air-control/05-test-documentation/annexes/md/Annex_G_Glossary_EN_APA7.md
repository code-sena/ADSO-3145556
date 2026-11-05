# Annex G. Glossary

**María de los Ángeles Olaya García; Jhon Jairo Cortez Vargas**

Servicio Nacional de Aprendizaje (SENA), Programa ADSO-3145556

Instructora: Karol Daniela Correa

12 de octubre de 2026

---

# Annex G. Glossary

---

| Term | Definition |
|---|---|
| ADR | Architecture decision record: a short document explaining a design decision and its consequences |
| API | Interface that enables communication between applications |
| API Gateway | Single entry point that routes requests, validates the JWT and limits traffic |
| BFF | Backend-for-Frontend: a server layer tailored to the needs of one client |
| BLE | Bluetooth Low Energy; the mobile app uses it to provision the ESP32 |
| DHT11 | Digital sensor for air temperature and humidity |
| ESP32 | Microcontroller with Wi-Fi and Bluetooth |
| Hexagonal (architecture) | Style that separates the domain from inbound and outbound adapters through ports |
| IoT | Internet of Things: connected physical devices that send data |
| JWT | Signed token that authenticates a user's requests |
| JWKS | Set of public keys that services use to verify JWT signatures |
| KY-038 | Sound module with analog output for a relative noise level |
| Liquibase | Tool for versioning database changes |
| MQ-135 | Gas and air-quality sensor; requires calibration and does not measure CO₂ in ppm without it |
| MQTT | IoT messaging protocol considered in the architecture; not confirmed in the firmware |
| OAuth2 / OIDC | Authorization and identity standards used for sign-in |
| OpenAPI | Standard for describing REST API contracts |
| Outbox | Pattern that stores events in the database before publishing them so they are not lost |
| RBAC | Role-based access control (SUPER_ADMIN, ADMIN, USER, VIEWER) |
| Smoke test | Quick end-to-end test confirming that the basic system responds |
| Testcontainers | Library that starts Docker containers (for example PostgreSQL) during tests |
| X-API-Key | Key an IoT device uses to authenticate when sending measurements |
