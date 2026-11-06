# Software registration: Energy Monitor / Registro de software: Energy Monitor

Fill in every empty field. It follows Decreto 1360 de 1989.

Llene todos los campos vacíos. Sigue el Decreto 1360 de 1989.

## Institutional context / Marco institucional

| Field / Campo | Value / Valor |
|---|---|
| Purpose / Propósito | Request registration before the Dirección Nacional de Derecho de Autor, for the apprentices team and the instructors team that supported the project or are cross-cutting to it / Solicitar el registro ante la Dirección Nacional de Derecho de Autor, a nombre del equipo de aprendices y del equipo de instructores que apoyaron el proyecto o son transversales a su consecución |
| Process / Proceso | Completion of the taught stage of the technologist program / Finalización de la etapa lectiva del tecnólogo |
| Training project / Proyecto formativo | Desarrollo de software orientado a servicios |
| Research seedbed / Semillero de investigación | Developers & Geeks |

## 1. Applicant / Solicitante (Decreto 1360, art. 5, num. 1)

| Field / Campo | Value / Valor |
|---|---|
| Full name / Nombre | Juan Esteban Cortes Parra |
| ID document / Identificación | 1079605497 |
| Acts on own behalf or as representative / Actúa en nombre propio o como representante | ☑ Own behalf / Nombre propio ☐ Representative / Representante |
| Proof of representation / Prueba de representación, si aplica | |

## 2. Authors / Autores (art. 5, num. 2)

Authors are the instructors and the apprentices listed in `team-roster.md`. Each apprentice checks their data and writes their contribution. / Los autores son los instructores y aprendices de `team-roster.md`. Cada aprendiz revisa sus datos y escribe su aporte.

| Full name / Nombre completo | Doc. type / Tipo | Doc. number / Número | Ficha | Contribution / Aporte |
|---|---|---|---|---|
| JESUS ARIEL GONZALEZ BONILLA | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| CARLOS FABIAN MARTINEZ MORA | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| CARLOS JULIO CADENA SARASTY | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| JAVIER HUMBERTO PINTO DIAZ | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| KAROL DANIELA CORREA TRUJILLO | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| JOSE DE JESUS MOTTA VARGAS | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| FERNELLY CARVAJAL SILVA | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| EDDIER YAIR LIBERATO CUENCA | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| JHON WILLIAN CORREDOR ARAUJO | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| ANDRES MORENO COLLAZOS | | Kept privately / En custodia | | Instructor: see `team-roster.md` / ver `team-roster.md` |
| JUAN ESTEBAN CORTES PARRA | CC | 1079605497 | 3145556 | Backend of the gateway and the security, device and measurement services; web frontend; infrastructure; documentation / Backend del gateway y de los servicios de seguridad, dispositivos y mediciones; frontend web; infraestructura; documentación |
| MIGUEL ANGEL GARCIA ARTUNDUAGA | CC | 1075241131 | 3145556 | Backend of the home, notification, alert and recommendation services; mobile app; infrastructure; documentation / Backend de los servicios de hogares, notificaciones, alertas y recomendaciones; app móvil; infraestructura; documentación |

## 3. Producer / Productor (art. 5, num. 3)

| Field / Campo | Value / Valor |
|---|---|
| Producer name / Nombre del productor | Supporting and cross-cutting instructors / Instructores de apoyo y transversales: JESUS ARIEL GONZALEZ BONILLA, CARLOS FABIAN MARTINEZ MORA, CARLOS JULIO CADENA SARASTY, JAVIER HUMBERTO PINTO DIAZ, KAROL DANIELA CORREA TRUJILLO, JOSE DE JESUS MOTTA VARGAS, FERNELLY CARVAJAL SILVA, EDDIER YAIR LIBERATO CUENCA, JHON WILLIAN CORREDOR ARAUJO, ANDRES MORENO COLLAZOS. Project apprentices / Aprendices del equipo: JUAN ESTEBAN CORTES PARRA, MIGUEL ANGEL GARCIA ARTUNDUAGA. |
| ID document or NIT / Identificación o NIT | Apprentices: section 2. Instructors: kept privately by the lead instructor. / Aprendices: sección 2. Instructores: en custodia del instructor líder. |

## 4. Rights holder / Titular de los derechos patrimoniales

| Field / Campo | Value / Valor |
|---|---|
| Rights holder / Titular | Supporting and cross-cutting instructors / Instructores de apoyo y transversales: JESUS ARIEL GONZALEZ BONILLA, CARLOS FABIAN MARTINEZ MORA, CARLOS JULIO CADENA SARASTY, JAVIER HUMBERTO PINTO DIAZ, KAROL DANIELA CORREA TRUJILLO, JOSE DE JESUS MOTTA VARGAS, FERNELLY CARVAJAL SILVA, EDDIER YAIR LIBERATO CUENCA, JHON WILLIAN CORREDOR ARAUJO, ANDRES MORENO COLLAZOS. Project apprentices / Aprendices del equipo: JUAN ESTEBAN CORTES PARRA, MIGUEL ANGEL GARCIA ARTUNDUAGA. |
| ID document or NIT / Identificación o NIT | Apprentices: section 2. Instructors: kept privately by the lead instructor. / Aprendices: sección 2. Instructores: en custodia del instructor líder. |
| How rights were acquired / Cómo adquirió los derechos, si no es el autor | Not applicable: the rights holders are the authors / No aplica: los titulares son los autores |
| Document that proves it / Documento que lo prueba | Not applicable / No aplica |

## 5. The work / La obra (art. 5, num. 4)

| Field / Campo | Value / Valor |
|---|---|
| Title / Título de la obra | Energy Monitor |
| Year of creation / Año de creación | 2026 |
| Country of origin / País de origen | Colombia |
| Version registered / Versión a registrar | `final-delivery-2026` |
| Short description of its functions / Breve descripción de sus funciones | **EN:** System that measures the electrical consumption of each appliance in a home. A device built with an ESP32 and a PZEM-004T sensor measures voltage, current, power and energy once a minute and sends the reading over MQTT with TLS. The server stores it and the web and Android apps show consumption per appliance, history and daily or monthly limits. It raises alerts (high consumption, limit reached, lost connection, unusual consumption, newly linked device), creates saving recommendations every hour and notifies by email and push. Homes can be shared, with owner and member roles, and an administrator manages accounts. The interface is in Spanish, English, French and Portuguese. **ES:** Sistema que mide el consumo eléctrico de cada electrodoméstico de un hogar. Un dispositivo con ESP32 y sensor PZEM-004T mide voltaje, corriente, potencia y energía una vez por minuto y envía la lectura por MQTT con TLS. El servidor la guarda y la aplicación web y la app Android muestran el consumo por aparato, el historial y los límites diario o mensual. Genera alertas (consumo alto, límite alcanzado, conexión perdida, consumo inusual, dispositivo vinculado), crea recomendaciones de ahorro cada hora y notifica por correo y push. Los hogares se comparten con roles de propietario y miembro, y un administrador gestiona las cuentas. La interfaz está en español, inglés, francés y portugués. |
| What sets it apart from similar software / Qué la diferencia de obras similares | **EN:** It measures with a real sensor instead of estimating, and does so per appliance. A module is linked from the web or the app over Bluetooth, and one firmware serves every device because its identity and key are written at linking time. Alerts and recommendations come from the consumption actually measured in each home. Homes are shared among members with roles. It is built only with free software, so the only running cost is the server. This is the team's own description of the work, not a comparison made against other products. **ES:** Mide con un sensor real y no con estimaciones, y lo hace por electrodoméstico. Un módulo se vincula desde la web o la app por Bluetooth, y un solo firmware sirve a todos los dispositivos porque su identidad y su clave se escriben al vincularlo. Las alertas y las recomendaciones salen del consumo realmente medido en cada hogar. Los hogares se comparten entre miembros con roles. Está construida solo con software libre, así que el único costo de operación es el servidor. Es la descripción que el equipo hace de su obra, no una comparación hecha contra otros productos. |
| Programming languages / Lenguajes | Java 21 (gateway and seven services / gateway y siete servicios), JavaScript with React (web / web), TypeScript with React Native (mobile app / app móvil), C++ for Arduino (ESP32 firmware / firmware del ESP32), SQL and XML with Liquibase (database migrations / migraciones de la base de datos), Bash (scripts), YAML (Docker, CI/CD and configuration / Docker, CI/CD y configuración) |
| Type: web, mobile, desktop, device / Tipo: web, móvil, escritorio, dispositivo | Web, mobile (Android) and device (ESP32 with firmware), backed by server-side services. It is not a desktop program. / Web, móvil (Android) y dispositivo (ESP32 con firmware), con servicios del lado del servidor. No es un programa de escritorio. |

## 6. Declarations / Declaraciones (art. 4 y art. 5, num. 5 y 6)

| Declaration / Declaración | Option / Opción |
|---|---|
| Origin / Origen | Original |
| If derived, base work / Si es derivada, obra de base | Not applicable: the work is not an adaptation or translation of another work. The third-party libraries and the instructors' documentation scaffold are declared in section 8. / No aplica: la obra no es una adaptación ni una traducción de otra. Las librerías de terceros y la estructura de documentación de los instructores se declaran en la sección 8. |
| Type of work / Clase de obra | In collaboration / En colaboración |
| Publication / Publicación | Published / Publicada |
| If published, date and place / Si está publicada, fecha y lugar | 2026-10-20, Internet (Colombia): the system in production at https://energy-monitor.tech and the public repositories listed in `code-tag/README.md`. / 2026-10-20, Internet (Colombia): el sistema en producción en https://energy-monitor.tech y los repositorios públicos listados en `code-tag/README.md`. |

Software is treated as unpublished unless the rights holder states otherwise (art. 4). A public repository may count as publication: the team records it here, and the Centro decides the final declaration.

El software se considera inédito salvo manifestación del titular (art. 4). Un repositorio público puede contar como publicación: el equipo lo deja anotado aquí y el Centro define la declaración final.

## 7. Elements attached / Elementos que se adjuntan (art. 2, 3 y 6)

At least one is required. Attach all three. / Se exige al menos uno. Adjunte los tres.

| Element / Elemento | Where it is / Dónde está | Ready / Listo |
|---|---|---|
| Computer program / Programa de computador | ZIP files `final-delivery-2026` listed in `code-tag/README.md` / ZIP relacionados en `code-tag/README.md` | ✅ |
| Program description / Descripción de programa | `02-software-analysis/` and `04-software-design/` | ✅ |
| Auxiliary material / Material auxiliar | `08-manuals/` | ✅ |

## 8. Third-party components / Componentes de terceros

List code, libraries, models, images or data not created by the team. An unlisted copy puts the "original work" declaration at risk.

Relacione código, librerías, modelos, imágenes o datos que el equipo no creó. Una copia sin declarar pone en riesgo la declaración de obra original.

| Component / Componente | Author or source / Autor o fuente | License / Licencia | Where it is used / Dónde se usa |
|---|---|---|---|
| Spring Boot, Spring Security, Spring Data JPA, Spring AMQP, Spring Integration MQTT, Spring Cloud Gateway | VMware / Broadcom (Spring project) | Apache-2.0 | Gateway and the seven services |
| springdoc-openapi, Caffeine, Apache HttpClient 5 | springdoc contributors; Ben Manes; Apache Software Foundation | Apache-2.0 | Services and gateway |
| Eclipse Paho MQTT client | Eclipse Foundation | EPL-2.0 / EDL-1.0 | Device and measurement services |
| MySQL Connector/J | Oracle | GPL-2.0 with FOSS exception | All services with database |
| Liquibase | Liquibase Inc. | To verify: version 5.x / Por verificar: versión 5.x | Database migrations of each service |
| React, React DOM, React Router, Vite, Zustand, axios, zod, react-hook-form, i18next, Recharts, react-country-flag and the test tools (Vitest, Testing Library, ESLint) | Their respective maintainers on npm | MIT | Web frontend |
| lucide-react (icons) | Lucide contributors | ISC | Web frontend |
| Expo, React Native, Expo Router, react-native-ble-plx, react-native-chart-kit, react-native-svg, Reanimated, Gesture Handler, Google Sign-In for React Native and the rest of the Expo modules | Expo, Meta and community maintainers on npm | MIT | Mobile app |
| TypeScript | Microsoft | Apache-2.0 | Mobile app |
| PZEM004Tv30 | mandulaj | MIT | ESP32 firmware |
| PubSubClient | Nick O'Leary | MIT | ESP32 firmware |
| WiFiManager | tzapu | MIT | ESP32 firmware |
| NimBLE-Arduino | h2zero | Apache-2.0 | ESP32 firmware |
| ArduinoJson | Benoit Blanchon | MIT | ESP32 firmware |
| Arduino core for ESP32 | Espressif Systems | LGPL-2.1 | ESP32 firmware |
| Docker images: mysql:8.4, rabbitmq:4.1, mosquitto-go-auth, nginx, node, eclipse-temurin | Oracle; Broadcom (RabbitMQ); Eclipse Foundation and iegomez; F5; OpenJS Foundation; Eclipse Adoptium | GPL-2.0; MPL-2.0; EPL-2.0 and MIT; BSD-2-Clause; MIT; GPL-2.0 with Classpath exception | Infrastructure and Dockerfiles |
| Documentation scaffold of `en-monitor-docs` (microservices-governance-framework) | Jesús Ariel González Bonilla (instructor) | MIT | Folder structure and templates of the course documentation repository |
| Google Sign-In (Google Identity Services) | Google | Google APIs Terms of Service | Web and mobile sign-in |
| Google logo (`google_icon.webp`) | Google | Google brand guidelines | Web sign-in button |
| Expo push notification service | Expo | Expo Terms of Service | Notification service and mobile app |
| Images `hero_img.webp`, `img_lp2.webp`, `empty_image.webp` | Generated with ChatGPT (OpenAI) | Output of the tool, under OpenAI's terms of use | Web landing page and empty states |
| Claude (AI assistant) | Anthropic | Output of the tool, under Anthropic's terms of use | Support in code, tests and documentation of the repositories; reviewed and committed by the team |

Not third-party: the project logo (`logo_proyecto.webp`) is the team's own design, and all other code, tests and documentation were written by the team. Licenses of the Maven and npm libraries were taken from their packages and project files; the transitive dependencies are not listed one by one.

No es de terceros: el logo del proyecto (`logo_proyecto.webp`) es diseño propio del equipo, y el resto del código, pruebas y documentación lo escribió el equipo. Las licencias de las librerías Maven y npm se tomaron de sus paquetes y archivos del proyecto; las dependencias transitivas no se listan una a una.

## 9. Private data / Datos privados

**Do not write this data in this repository.** Registration also asks for address, nationality, phone and email. Each member gives them to the instructor through the private channel the instructor indicates.

**No escriba estos datos en este repositorio.** El registro también pide dirección, nacionalidad, teléfono y correo. Cada integrante se los entrega al instructor por el canal privado que se indique.

## 10. Team declaration / Declaración del equipo

- [x] The information is true and the names match the official ID documents. / La información es veraz y los nombres coinciden con los documentos de identidad.
- [x] The listed authors are the people who created the software. / Los autores relacionados son quienes crearon el software.
- [x] Third-party components are declared. / Los componentes de terceros están declarados.
- [x] No passwords, tokens or keys are included. / No se incluyen contraseñas, tokens ni llaves.
