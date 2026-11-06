# EduAirControl · Manual de instalación

**María de los Ángeles Olaya García; Jhon Jairo Cortez Vargas**

Servicio Nacional de Aprendizaje (SENA), Programa ADSO-3145556

Instructora: Karol Daniela Correa

12 de octubre de 2026

---

# EduAirControl · Manual de instalación

**SENA INDUSTRIA**

**EDUAIRCONTROL**

**Manual de instalación**

*Preparación, despliegue local, aplicación móvil y prototipo IoT*

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
| 1.0 | October 5, 2026 | Initial creation of the installation manual. | Development team |
| 1.1 | October 7, 2026 | Se actualizaron los puertos para la arquitectura de microservicios y se añadieron variables de entorno de OAuth2. | Development team |

---

## Tabla de contenido

- Introducción
- Objetivo
- Alcance y advertencias
- Requisitos
- Obtención del proyecto
- Preparación del archivo de entorno
- Instalación y ejecución web con Docker
  - 7.1 Operation commands
  - 7.2 Ejecución local sin contenedores
- Base de datos y migraciones
- Verificación del sistema web
- Instalación y ejecución móvil
  - 10.1 Configuración de la API móvil
- Instalación física del prototipo IoT
  - 11.1 Conexiones de prueba verificadas
  - 11.2 Circuit tests
- Configuración BLE desde el móvil
- Errores comunes y soluciones
- Desinstalación y limpieza
- Glosario

---

## 1. Introducción

Este manual permite preparar un equipo con Windows para instalar y ejecutar EduAirControl en un entorno local de desarrollo y demostración. Incluye el despliegue web con Docker, la ejecución móvil con Expo y la preparación preliminar del prototipo ESP32.

## 2. Objetivo

Proporcionar instrucciones reproducibles para obtener el código, instalar las herramientas, iniciar los servicios, verificar la API, ejecutar la aplicación móvil y realizar las pruebas básicas del circuito IoT.

## 3. Alcance y advertencias

El procedimiento se basa en los archivos entregados. El despliegue web con Docker está definido. La instalación física se describe como un prototipo de prueba porque todavía no cuenta con firmware definitivo de ingesta, voltaje de batería confirmado ni diagrama eléctrico final.

## 4. Requisitos

| **Resource** | **Requirement** |
| --- | --- |
| Operating system | Windows; official minimum version pending definition |
| RAM | 8 GB or more recommended for Docker and simultaneous services |
| Storage | Enough space for Docker images, dependencies and PostgreSQL volumes |
| Internet | Required to clone/obtain the code and download images and dependencies |
| Herramientas | Git, Docker Desktop, Node.js/npm, Visual Studio Code |
| Mobile | Expo Go or Android/iOS device/emulator depending on the test |
| Hardware | ESP32, DHT11, KY-038, MQ-135, breadboard, jumpers and a safe power supply |

## 5. Obtención del proyecto

The project contains the `EduAirControl` folder. If you use the repository, run:

```bash
git clone https://github.com/Jhon-Cortez/EduAirControl.git
cd EduAirControl
```

The mobile application is delivered as a separate project. Copy or unzip the `MOBILE` folder into the workspace, without mixing its `package.json` with that of the web project.

## 6. Preparación del archivo de entorno

In `Web/`, create a `.env` file based on the values in the compose file. Real credentials must not be published. For development, you can use a local configuration equivalent to the following:

```env
BACKEND_PORT=8080
POSTGRES_DB=eduaircontrol
POSTGRES_USER=eduair_user
POSTGRES_PASSWORD=CHANGE_LOCALLY
JWT_SECRET=LOCAL_KEY_OF_AT_LEAST_32_CHARACTERS
ADMIN_EMAIL=admin@eduaircontrol.com
ADMIN_PASSWORD=CHANGE_LOCALLY
ADMIN_COMPANY_CODE=EDU-0001
DB_CHANGELOG_CONTEXTS=dev
VITE_API_URL=http://localhost:8080

# OAuth2 social login
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
FACEBOOK_CLIENT_ID=your-facebook-app-id
FACEBOOK_CLIENT_SECRET=your-facebook-app-secret
```

The PostgreSQL password and the administrator password must be defined by the person performing the installation. They must not be copied into the manual as production credentials.

## 7. Instalación y ejecución web con Docker

Docker Desktop debe estar en ejecución y utilizar el motor de Linux. Desde la carpeta `Web`:

```bash
cd EduAirControl/Web
docker compose up -d --build
```

The first startup may take a while because Docker downloads images, builds the backend and the frontend, starts PostgreSQL and runs Liquibase. The defined services are backend, frontend, postgres, mailpit and jenkins.

| **Servicio** | **Acceso / puerto** |
| --- | --- |
| Frontend | http://localhost:5173 (Vite dev) |
| API Gateway | http://localhost:8080 |
| ms-security | http://localhost:8081 |
| ms-classroom-management | http://localhost:3002 |
| ms-environment-monitoring | http://localhost:3003 |
| ms-sensor-management | http://localhost:3004 |
| ms-user-management | http://localhost:3007 |
| Swagger | http://localhost:8080/swagger-ui.html |
| Backend health | http://localhost:8080/health |
| PostgreSQL | localhost:5432 |
| Mailpit web | http://localhost:8025 |
| Jenkins | http://localhost:8081 |

### 7.1 Operation Commands

```bash
docker compose ps
docker compose logs
docker compose logs backend
docker compose down
docker compose up -d --build
```

### 7.2 Local Execution Without Containers

Este modo se recomienda únicamente para desarrollo. Puede iniciar PostgreSQL y Mailpit con Compose y ejecutar el backend/frontend por separado:

```bash
cd EduAirControl/Web
docker compose up -d postgres mailpit

cd backend
./mvnw spring-boot:run

cd ../Front-End
npm install
npm run dev
```

En Windows puede utilizar `mvnw.cmd spring-boot:run`. El frontend de Vite normalmente se abre en http://localhost:5173; el backend permanece en http://localhost:8080.

## 8. Base de datos y migraciones

PostgreSQL is created through the `postgres` container. Liquibase runs the changelogs when the backend starts. With `DB_CHANGELOG_CONTEXTS=dev`, development data is loaded; with an empty value, loading of demonstration data is avoided.

- Default database: `eduaircontrol`.
- Usuario and password: defined by `POSTGRES_USER` and `POSTGRES_PASSWORD`.
- Exposed port: 5432.
- Persistent volume: `postgres_data`.
- Do not delete the volume if you want to keep the data.

## 9. Verificación del sistema web

| **Test** | **Expected result** |
| --- | --- |
| Containers | `docker compose ps` shows active services |
| Frontend | http://localhost:3000 shows EduAirControl |
| Backend | http://localhost:8080/health responds without error |
| Swagger | The documentation interface opens at /swagger-ui.html |
| Login | You can authenticate with an administrator created in the local environment |
| Base de datos | The backend starts without connection errors and Liquibase finishes successfully |
| Code tests | `npm run lint`, `npm run build` and `mvn test` finish successfully |

## 10. Instalación y ejecución móvil

The mobile application is located in `MOBILE/EduAirControlMobile/Front-End` and is built with Expo 54.0.37, React Native 0.81.5 and React 19.1.0. Its target is Android/iOS; web export is not part of the scope.

```bash
cd MOBILE/EduAirControlMobile/Front-End
npm install
npm start
```

From the Expo menu you can open the application on a compatible device. For native Android, `npm run android` is available, which may require Android Studio and a native environment. To validate the Android bundle:

```bash
npm run check:android
```

### 10.1 Mobile API Configuration

En desarrollo, la aplicación detecta el host de Metro. Puede forzar una dirección accesible desde el teléfono mediante `EXPO_PUBLIC_API_URL`. En un dispositivo físico, `localhost` apunta al propio teléfono y no al computador; debe utilizar la IP de la red local del computador:

```bash
EXPO_PUBLIC_API_URL=http://192.168.1.X:8080 npx expo start
```

En el emulador de Android, `10.0.2.2` se utiliza como alias del computador anfitrión. La aplicación utiliza JWT para la autenticación y AsyncStorage para conservar el token y el usuario.

## 11. Instalación física del prototipo IoT

El montaje descrito es una demostración en una protoboard. Antes de energizarlo, verifique los voltajes y la continuidad. No conecte el MQ-135 al ADC del ESP32 sin adaptar el nivel si su salida puede superar el máximo permitido.

| **Componente** | **Quantity** | **Función** |
| --- | --- | --- |
| ESP32 | 1 | Controller and Wi-Fi connectivity |
| Breadboard | 1 | Solderless assembly |
| Jumpers | Several | Connections |
| DHT11 | 1 | Air temperature and humidity |
| KY-038 | 1 | Relative sound level |
| MQ-135 | 1 | Gases/air quality; not a precision CO₂ meter |
| Battery | 1 | Portable power supply; voltage pending |
| 10 kΩ and 20 kΩ resistors | Por confirmar | Possible divider for the MQ-135; final use pending |

### 11.1 Verified Test Connections

| **Element** | **Connection / note** |
| --- | --- |
| DHT11 DATA | GPIO 5 (D5) in the test programs |
| KY-038 AO | GPIO 34 (D34) in the test programs |
| Serial monitor | 115200 baud |
| DHT11 | Use the DHT sensor library and Adafruit Unified Sensor libraries |
| MQ-135 | Final connection, power supply and divider pending confirmation |
| ESP32 → backend | Final firmware and ingestion protocol pending confirmation |

### 11.2 Circuit Tests

The test files are located in `iot/esp32`. For the DHT11 and KY-038, you can copy the content into the Arduino IDE, install the indicated libraries, select an ESP32 board and upload the program. Open the serial monitor at 115200 baud.

- **DHT11:** observe temperature and humidity without "SIN DATOS" ("NO DATA") messages.
- **KY-038:** make a sound near the microphone and check that the AO value changes.
- **MQ-135:** wait for the warm-up/stabilization defined by the sensor's procedure and verify analog variation; do not automatically interpret it as CO₂ ppm.

Si se pierde la conexión Wi-Fi, el comportamiento de reconexión debe verificarse en el firmware definitivo.

## 12. Configuración BLE desde el móvil

La aplicación móvil incluye configuración mediante Bluetooth de baja energía (BLE) utilizando `react-native-ble-plx`. Busca el dispositivo `ESP32_Config` mediante un UUID de servicio y característica definidos en el código. El teléfono debe conceder permisos de Bluetooth; Android 12 o superior requiere `BLUETOOTH_SCAN` y `BLUETOOTH_CONNECT`.

```
BLE service:     4fafc201-1fb5-459e-8fcc-c5c9c331914b
Characteristic:  beb5483e-36e1-4688-b7f5-ea07361b26a8
Expected name:   ESP32_Config
```

La configuración envía las credenciales Wi-Fi como JSON codificado en Base64. No registre ni comparta credenciales reales en capturas de pantalla o documentos.

## 13. Errores comunes y soluciones

| **Problem** | **Probable cause** | **Solution** |
| --- | --- | --- |
| Docker does not start | Docker Desktop or the Linux engine is off | Start Docker Desktop and check `docker compose ps` |
| Port in use | Another process uses 3000, 8080, 5432, 8025 or 8081 | Stop the process or change the port in .env/compose |
| Backend does not connect to PostgreSQL | Incorrect POSTGRES_* variables | Check .env and that postgres is healthy |
| Blank frontend | VITE_API_URL unreachable or backend stopped | Check the URL, CORS and the backend logs |
| Expo does not connect to the API | The phone uses localhost as its own host | Use EXPO_PUBLIC_API_URL with the LAN IP |
| DHT11 without data | Incorrect pin, power supply or library | Check DATA on GPIO 5, VCC/GND and libraries |
| Unsafe MQ-135 reading | Analog output above the ESP32 maximum | Use adaptation/divider and verify resistors |
| BLE does not find the ESP32 | Bluetooth off, permissions or missing firmware | Turn on Bluetooth, grant permissions and verify the name/UUID |

## 14. Desinstalación y limpieza

Para detener los contenedores sin eliminar los datos:

```bash
docker compose down
```

Para eliminar también el volumen de PostgreSQL —acción que destruye los datos locales—, utilice este comando solo si necesita restablecer el entorno de demostración:

```bash
docker compose down -v
```

## 15. Glosario

| **Término** | **Definición** |
| --- | --- |
| Docker Compose | Herramienta para definir y ejecutar varios servicios relacionados. |
| Expo | Plataforma de desarrollo para aplicaciones React Native. |
| Liquibase | Control de versiones y aplicación de cambios de la base de datos. |
| Mailpit | Servidor SMTP local para visualizar correos de prueba. |
| BLE | Bluetooth de baja energía, utilizado para configurar el ESP32. |
| GPIO | Pin de entrada/salida digital o analógica del microcontrolador. |
| ADC | Convertidor analógico-digital del ESP32. |

---

*SENA Industria · Ficha 3145556*
