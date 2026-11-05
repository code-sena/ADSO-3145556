# Especificación de Requisitos de Software (SRS)

**María de los Ángeles Olaya García; Jhon Jairo Cortez Vargas**

Servicio Nacional de Aprendizaje (SENA), Programa ADSO-3145556

Instructora: Karol Daniela Correa

12 de octubre de 2026

---

# Especificación de Requisitos de Software (SRS)
## EduAirControl
**Versión:** 1.2 — revisión documental y alineación con el repositorio entregado  
**Fecha:** 9 de octubre de 2026  
**Proyecto:** EduAirControl  
**Elaborado para:** Servicio Nacional de Aprendizaje (SENA), Análisis y Desarrollo de Software — ficha 3145556  
**Integrantes:** María de los Ángeles Olaya García; Jhon Jairo Cortez Vargas  
**Instructor:** José de Jesús Motta Vargas

> **Convención de estado.** “Implementado”, “Parcial” y “Pendiente” reproducen el estado descrito en la especificación fuente y la documentación del repositorio. No equivalen por sí solos a una certificación independiente de funcionamiento. Los objetivos de calidad cuantitativos son criterios de aceptación propuestos y deben verificarse con evidencias de prueba.

## Control del documento
| Versión | Fecha | Descripción |
|---|---|---|
| 1.0 | 2025 | Versión inicial del SRS. |
| 1.1 | 08/10/2026 | Actualización contra la documentación de web, móvil y microservicios. |
| 1.2 | 09/10/2026 | Corrección de estructura, terminología, trazabilidad, estados y discrepancias documentales; se separan capacidades implementadas de objetivos aún por verificar. |

## 1. Introducción

### 1.1 Propósito
Este documento especifica los requisitos funcionales, no funcionales, interfaces, restricciones y criterios de aceptación de EduAirControl. Sirve como referencia común para desarrollo, pruebas, implementación, mantenimiento y evaluación del producto.

### 1.2 Problema
En los entornos educativos, la temperatura, la humedad, la concentración de dióxido de carbono (CO₂) y el ruido pueden afectar el confort y las condiciones de aprendizaje. Cuando estas variables no se observan de forma continua, el personal de la institución dispone de información limitada para detectar situaciones anómalas y decidir qué acciones tomar.

### 1.3 Propósito del producto
EduAirControl es una plataforma de monitoreo ambiental escolar basada en IoT. Recibe mediciones de sensores, las almacena, presenta valores actuales e históricos y genera alertas según umbrales configurados. Busca apoyar decisiones informadas sobre las condiciones de los espacios educativos; no sustituye instrumentos certificados ni constituye un sistema automático de control ambiental.

### 1.4 Objetivo general
Desarrollar un sistema de monitoreo ambiental escolar que registre y visualice temperatura, CO₂, humedad y ruido, para apoyar el bienestar de estudiantes y docentes y la toma de decisiones en instituciones educativas.

### 1.5 Objetivos específicos
- Recibir y consultar mediciones ambientales asociadas a espacios educativos.
- Presentar información actual e histórica mediante interfaces web y móvil.
- Informar sobre condiciones normales, de advertencia y críticas.
- Permitir administrar instituciones, sedes, ambientes, usuarios, sensores y preferencias según los permisos.
- Ofrecer opciones de accesibilidad y personalización visual.
- Mantener aislamiento de datos entre instituciones como requisito de seguridad.

## 2. Alcance

### 2.1 Incluido en el MVP documentado
- Autenticación y control de acceso por roles.
- Registro e inicio de sesión con correo y contraseña, y autenticación social donde esté configurada.
- Gestión de instituciones, sedes, usuarios y roles.
- Gestión de ambientes educativos.
- Registro/administración de sensores y asociaciones sensor-ambiente, con partes aún parciales.
- Ingesta y almacenamiento de mediciones ambientales.
- Consulta de valores actuales e históricos.
- Paneles, filtros, favoritos, perfil y preferencias.
- Alertas según umbrales de temperatura, humedad, CO₂ y ruido.
- Configuración de dispositivos mediante Bluetooth desde móvil, según compatibilidad del dispositivo.
- Aplicación web y aplicación móvil.

### 2.2 Fuera del alcance
- Predicción ambiental avanzada o modelos predictivos.
- Control automático de ventilación, aire acondicionado u otros equipos.
- Integración con plataformas externas de automatización ambiental.
- Diseño y fabricación del hardware de sensores.
- Garantía de exactitud metrológica de sensores no certificados.
- Notificaciones push o correo externas como capacidad confirmada: se registran como pendientes.

### 2.3 Perspectiva del producto
El repositorio documenta una arquitectura distribuida con clientes web React/Vite y móvil Expo/React Native, servicios Java con Spring Boot, API Gateway, PostgreSQL, Redis y RabbitMQ, además de contenedores Docker y herramientas de integración. Se identifican servicios de seguridad, gestión de usuarios, aulas/ambientes, sensores, monitoreo ambiental y experiencia de usuario. La disponibilidad real de cada capacidad debe confirmarse en la versión desplegada.

**Nota de alineación:** la especificación fuente menciona HTTPS o MQTT para telemetría, mientras que la documentación arquitectónica describe ingesta HTTP y el servicio de monitoreo contiene un controlador HTTP; no se encontró evidencia suficiente en el repositorio revisado para declarar MQTT como canal operativo del MVP. Por tanto, HTTP/HTTPS es el canal base documentado y MQTT requiere confirmación antes de considerarse requisito implementado.

## 3. Usuarios y actores

| Actor | Responsabilidades / permisos esperados |
|---|---|
| Usuario (`USER`) | Consulta ambientes y variables autorizados, historial, alertas, favoritos, perfil y preferencias. |
| Administrador (`ADMIN`) | Administra ambientes, sensores y umbrales dentro del ámbito institucional autorizado. |
| Superadministrador (`SUPER_ADMIN`) | Gestiona instituciones y administración global de usuarios y roles. |
| Dispositivo IoT | Envía mediciones identificadas para su validación y almacenamiento. |
| Servicio externo de identidad | Google/Facebook, si las credenciales y callbacks están configurados. |
| Operador técnico | Despliega, configura, supervisa y recupera servicios; no es un rol funcional de usuario final. |

El aislamiento por institución y sede es obligatorio. La regla documentada indica que un administrador puede consultar la institución autorizada y que otros usuarios quedan limitados a su sede. La implementación completa de este aislamiento está marcada como parcial y requiere pruebas de autorización.

## 4. Suposiciones, dependencias y restricciones

### 4.1 Suposiciones
- Los espacios cuentan con alimentación eléctrica y conectividad Wi-Fi suficientes para los dispositivos.
- Los sensores se encuentran instalados y configurados correctamente.
- Las instituciones asignan responsables para revisar las alertas.
- Los usuarios reciben orientación básica para utilizar la plataforma.
- Las mediciones de los sensores tienen la precisión apropiada para el uso orientativo previsto.

### 4.2 Dependencias
- Proveedor de correo para recuperación de contraseña.
- Google y Facebook para autenticación social, si se habilitan.
- PostgreSQL para persistencia; Redis para funciones auxiliares documentadas; RabbitMQ para eventos entre servicios cuando aplique.
- Docker y la infraestructura de ejecución.
- GitHub/Jenkins para repositorio y automatización, cuando estén conectados al entorno de despliegue.
- Dispositivos ESP32 y sus sensores, así como Bluetooth/BLE para el aprovisionamiento móvil.

### 4.3 Restricciones
- Se requiere conectividad para consultar datos recientes y comunicarse con los servicios.
- Los datos deben segregarse por institución y sede de acuerdo con los permisos.
- Los secretos y contraseñas no deben almacenarse en código fuente ni registrarse en logs.
- La aplicación web debe funcionar en navegadores modernos; la aplicación móvil depende de las versiones admitidas por la configuración de Expo.
- Los valores de umbrales deben centralizarse y mantenerse consistentes entre backend, web y móvil.
- La especificación no afirma que todas las metas de rendimiento, disponibilidad, cobertura o recuperación ya hayan sido medidas.

## 5. Interfaces externas

### 5.1 Interfaz de usuario
- Web y móvil con tarjetas, listas, formularios, indicadores ambientales y vistas históricas.
- Modo claro y oscuro.
- Opciones de visualización para protanopia, deuteranopia y tritanopia, y tamaños de fuente ajustables, según los controles descritos en la SRS.
- Idiomas previstos: español, inglés, francés y portugués.
- Colores base documentados para el sistema visual: claro `#f0fafa` y oscuro `#0a0f1e`; estos valores describen el fondo base, no imágenes de fondo.
- La paridad completa de funciones entre web y móvil no se da por garantizada: debe verificarse por pantalla y flujo.

### 5.2 Hardware
- Computadores y dispositivos móviles conectados a Internet.
- Dispositivos de medición basados en ESP32 y sensores de temperatura, humedad, CO₂ y ruido.
- Bluetooth Low Energy (BLE) en el móvil para aprovisionamiento de dispositivos compatibles.

### 5.3 Software y comunicación
- Cliente web: React/Vite, de acuerdo con el repositorio.
- Cliente móvil: Expo/React Native; la versión exacta de SDK debe confirmarse con los manifiestos del proyecto.
- Backend: Java/Spring Boot en microservicios.
- Persistencia: PostgreSQL; Redis y RabbitMQ según configuración.
- APIs HTTP/REST detrás de un API Gateway. La ingesta de sensores por HTTP/HTTPS es la base documentada. MQTT no se considera confirmado hasta que exista configuración y evidencia ejecutable.
- Autenticación social mediante OAuth2/OIDC, solo si proveedores, credenciales y redirecciones están configurados.

## 6. Requisitos funcionales

**Prioridades:** Alta = crítica para el flujo principal o seguridad; Media = importante para el MVP; Pendiente = no debe anunciarse como capacidad disponible. Los estados siguientes proceden de la especificación fuente y documentación revisada.

### RF1. Autenticación y acceso
| ID | Requisito | Estado |
|---|---|---|
| RF1.1 | Permitir iniciar sesión con correo y contraseña y emitir tokens de acceso y renovación según la configuración. La fuente especifica 1 hora para acceso y 7 días para renovación. | Implementado en la fuente |
| RF1.2 | Ofrecer segundo factor de autenticación. | Pendiente |
| RF1.3 | Permitir registro con nombre de usuario, correo, contraseña, código de institución y sede opcional; rechazar correos duplicados. | Implementado en la fuente |
| RF1.4 | Permitir autenticación con Google/Facebook mediante OAuth2 cuando esté configurada; vincular cuentas existentes o iniciar el proceso de alta para nuevas cuentas. | Implementado en la fuente; validar configuración |
| RF1.5 | Exigir aceptación de términos y condiciones durante el registro. | Implementado en la fuente |
| RF1.6 | Permitir recuperar contraseña mediante código enviado por correo, validarlo y definir nueva contraseña; permitir reenvío. | Implementado en la fuente |
| RF1.7 | Permitir cambiar contraseña y solicitar eliminación de cuenta con confirmación. | Implementado en la fuente |
| RF1.8 | Restringir rutas y operaciones por roles `USER`, `ADMIN` y `SUPER_ADMIN`; responder 401 sin autenticación y 403 sin permiso. | Implementado en la fuente |
| RF1.9 | Permitir cierre de sesión y renovación de token; la revocación debe invalidar el token según la estrategia configurada. | Implementado en la fuente |

### RF2. Instituciones, sedes y usuarios
| ID | Requisito | Estado |
|---|---|---|
| RF2.1 | Permitir al superadministrador crear, consultar y editar instituciones con código único. | Implementado en la fuente |
| RF2.2 | Permitir registrar, consultar, editar y eliminar sedes asociadas a una institución. | Parcial |
| RF2.3 | Aislar los datos por institución y sede en tokens, gateway y consultas de servicios. Ningún usuario debe acceder a datos de otra institución. | Parcial; requiere pruebas de seguridad |
| RF2.4 | Permitir al superadministrador consultar usuarios y asignar roles desde las funciones de administración. | Implementado en la fuente |

### RF3. Panel, variables y análisis
| ID | Requisito | Estado |
|---|---|---|
| RF3.1 | Mostrar ambientes autorizados con su estado ambiental y acceso al detalle. | Implementado en la fuente |
| RF3.2 | Mostrar temperatura, humedad, CO₂ y ruido con valor, unidad y estado. | Implementado en la fuente |
| RF3.3 | Calcular puntaje de 0 a 100 y ranking de ambientes según cumplimiento de rangos. | Parcial / fuera del MVP inicial; confirmar alcance |
| RF3.4 | Consultar datos actuales e históricos por día, semana, mes y año, seleccionando período y variable. | Parcial |
| RF3.5 | Mostrar gráficos o indicadores que permitan comparar la evolución de las mediciones. | Objetivo asociado al análisis; verificar por cliente |

### RF4. Búsqueda y filtrado
| ID | Requisito | Estado |
|---|---|---|
| RF4.1 | Buscar ambientes por nombre y permitir consultar, registrar o borrar historial de búsqueda si el servicio está disponible. | Parcial |
| RF4.2 | Filtrar por bloque, piso, favoritos y estado ambiental. La búsqueda por variable queda pendiente en la fuente. | Parcial |

### RF5. Detalle y valoración de ambientes
| ID | Requisito | Estado |
|---|---|---|
| RF5.1 | Mostrar ubicación, capacidad, tipo, calidad ambiental, métricas actuales, rangos de referencia, explicación de variables y recomendaciones básicas. | Implementado en la fuente |
| RF5.2 | Permitir calificar un ambiente de 1 a 5 y escribir un comentario de hasta 1.000 caracteres. | Parcial: la fuente indica que falta la pantalla y moderación |
| RF5.3 | Permitir consultar el detalle de una variable, su valor actual, estado e histórico. | Parcial |

### RF6. Alertas y notificaciones
| ID | Requisito | Estado |
|---|---|---|
| RF6.1 | Comparar cada medición con los umbrales activos y clasificar desviaciones como `WARNING` o `CRITICAL`. | Implementado en la fuente |
| RF6.2 | Listar alertas y permitir reconocerlas (`acknowledge`). | Implementado en la fuente |
| RF6.3 | Mostrar notificaciones ordenadas por fecha, permitir abrir el elemento relacionado y marcarlo como leído por usuario. La fuente advierte que parte de la interfaz calcula avisos con valores fijos en cliente y debe consumir alertas del backend. | Parcial |
| RF6.4 | Permitir activar/desactivar alertas, advertencias, resumen diario y sonido, según preferencias disponibles. | Implementado en la fuente |
| RF6.5 | Enviar alertas fuera de la aplicación mediante notificación push o correo a responsables. | Pendiente |

### RF7. Perfil, favoritos y preferencias
| ID | Requisito | Estado |
|---|---|---|
| RF7.1 | Permitir editar nombre, cargo, teléfono, ubicación y foto de perfil. | Implementado en la fuente |
| RF7.2 | Permitir marcar/desmarcar ambientes favoritos y consultar la lista propia. Favoritos de variables no incluidos. | Implementado en la fuente |
| RF7.3 | Ofrecer modo oscuro, tres opciones para daltonismo y tres tamaños de fuente. | Implementado en la fuente |
| RF7.4 | Ofrecer español, inglés, francés y portugués. | Implementado en la fuente |
| RF7.5 | Guardar preferencias de formato de fecha, zona horaria y recordatorios; permitir almacenamiento local cuando no haya conexión, donde esté implementado. | Implementado en la fuente |

### RF8. Administración de ambientes, sensores y umbrales
| ID | Requisito | Estado |
|---|---|---|
| RF8.1 | Permitir a administradores crear, editar y eliminar ambientes con nombre, ubicación, piso, capacidad y tipo. | Implementado en la fuente |
| RF8.2 | Registrar, editar, activar/desactivar y retirar sensores; asociar variables e instalar o retirar sensores de ambientes; validar campos obligatorios. | Parcial; la fuente indica que el panel web de sensores usa datos de ejemplo |
| RF8.3 | Configurar umbrales normales, de advertencia y críticos por variable y tipo de ambiente, con vigencia temporal; cerrar la vigencia en vez de borrar el historial. | Parcial |

### RF9. IoT y mediciones
| ID | Requisito | Estado |
|---|---|---|
| RF9.1 | Recibir, validar y persistir mediciones; evitar duplicados ante reintentos. La vía base documentada es HTTP/HTTPS. | Implementado en la fuente |
| RF9.2 | Conservar mediciones para consulta histórica por día, semana, mes y año. | Implementado en la fuente |
| RF9.3 | Consultar el valor más reciente de cada variable por ambiente. | Implementado en la fuente |
| RF9.4 | Permitir al administrador configurar un dispositivo ESP32 compatible desde la aplicación móvil por BLE, solicitando permisos necesarios y enviando configuración de red. | Implementado en la fuente; validar hardware y cliente |
| RF9.5 | Registrar dispositivos con MAC, nombre, tipo, firmware, red y estado, y asignarlos a ambientes. | Parcial; la fuente no encontró un servicio backend confirmado para `/api/v1/devices` |

### Resumen de estado de la fuente
| Estado | Cantidad reportada | Observación |
|---|---:|---|
| Implementado | 26 | Recuento original; no es una auditoría independiente |
| Parcial | 12 | Requiere cerrar brechas y aportar pruebas |
| Pendiente | 2 | Segundo factor y notificación externa |
| **Total reportado** | **40** | La tabla original resume subrequisitos; las filas de esta versión reorganizan y explicitan algunos requisitos compuestos. No usar el recuento como métrica de aceptación sin normalizar la matriz. |

## 7. Variables ambientales y umbrales

Los rangos siguientes son los valores documentados en la SRS fuente para cada tipo de ambiente. Deben centralizarse en una única configuración y verificarse con el responsable institucional y los sensores reales. No deben interpretarse como asesoría médica ni como estándares regulatorios universales.

### 7.1 Rangos válidos de entrada
| Variable | Código | Unidad | Rango válido |
|---|---|---|---|
| Temperatura | `TEMP` | °C | -10 a 50 |
| Humedad relativa | `HUM` | % | 0 a 100 |
| Dióxido de carbono | `CO2` | ppm | 0 a 5.000 |
| Nivel de ruido | `NOISE` | dB | 0 a 140 |

### 7.2 Aula (`CLASSROOM`)
| Variable | Normal | Advertencia | Crítico |
|---|---|---|---|
| Temperatura | 20–26 °C | 18–20 °C o 26–28 °C | <18 °C o >28 °C |
| Humedad | 40–60 % | 30–40 % o 60–70 % | <30 % o >70 % |
| CO₂ | 0–800 ppm | 800–1.000 ppm | >1.000 ppm |
| Ruido | 0–45 dB | 45–55 dB | >55 dB |

### 7.3 Laboratorio (`LABORATORY`)
| Variable | Normal | Advertencia | Crítico |
|---|---|---|---|
| Temperatura | 18–24 °C | 16–18 °C o 24–26 °C | <16 °C o >26 °C |
| Humedad | 30–60 % | 25–30 % o 60–75 % | <25 % o >75 % |
| CO₂ | 0–600 ppm | 600–800 ppm | >800 ppm |
| Ruido | 0–50 dB | 50–60 dB | >60 dB |

### 7.4 Biblioteca (`LIBRARY`)
| Variable | Normal | Advertencia | Crítico |
|---|---|---|---|
| Temperatura | 20–24 °C | 18–20 °C o 24–26 °C | <18 °C o >26 °C |
| Humedad | 40–55 % | 35–40 % o 55–65 % | <35 % o >65 % |
| CO₂ | 0–600 ppm | 600–800 ppm | >800 ppm |
| Ruido | 0–40 dB | 40–50 dB | >50 dB |

**Regla de interpretación:** los límites compartidos (por ejemplo, 800 ppm) deben tener una regla de precedencia única para evitar que un mismo valor sea clasificado en dos estados. La fuente no define expresamente si los intervalos son abiertos o cerrados en todos sus extremos; esta regla debe fijarse en ADR-014/código y cubrirse con pruebas de límites.

## 8. Requisitos no funcionales

Los siguientes son objetivos medibles documentados en la estrategia de calidad. Salvo que exista evidencia de prueba, deben tratarse como criterios pendientes de verificación, no como resultados obtenidos.

| ID | Categoría | Objetivo / criterio de aceptación |
|---|---|---|
| RNF1 | Rendimiento | P95 ≤ 500 ms para endpoints críticos bajo carga esperada; análisis histórico P95 ≤ 2 s; carga inicial del panel P95 ≤ 3 s; confirmación de ingesta P95 ≤ 1 s; tasa de error <1 % excluyendo validaciones de cliente. |
| RNF2 | Disponibilidad y resiliencia | Disponibilidad mensual ≥99,5 % en ventana acordada; endpoints de salud de vida y disponibilidad; errores controlados con correlación; reintentos idempotentes. |
| RNF3 | Escalabilidad | Escalado horizontal de servicios sin estado; objetivo inicial de hasta cinco instancias por servicio; aumento de capacidad dentro de dos minutos en el escenario de crecimiento súbito, sujeto a infraestructura. |
| RNF4 | Seguridad y privacidad | Autenticación en rutas privadas; autorización por rol y tenant; validación de entradas; TLS en tránsito en entornos desplegados; secretos fuera del código; no registrar contraseñas ni tokens completos. |
| RNF5 | Observabilidad | Logs estructurados con fecha, servicio, nivel y correlación; métricas y trazas; alertas operativas; no depender de la telemetría para mantener la ingesta. |
| RNF6 | Mantenibilidad y documentación | Cobertura objetivo ≥80 % del código medible y ≥90 % de lógica de negocio; complejidad ciclomática ≤10 por función cuando sea razonable; instalación reproducible en menos de una hora; contrato OpenAPI actualizado por API expuesta. |
| RNF7 | Portabilidad y despliegue | Servicios desplegables con Docker; configuración por variables de entorno; ausencia de rutas locales fijas y secretos embebidos; Compose como destino mínimo documentado. |
| RNF8 | Recuperación | RTO objetivo <5 min ante fallo de microservicio/gateway y <30 min ante fallo de base de datos; RPO objetivo ≤24 h con respaldo diario. Metas sujetas a confirmación operativa y prueba de restauración. |
| RNF9 | Usabilidad, accesibilidad e idiomas | Flujos principales comprensibles; español, inglés, francés y portugués; modo claro/oscuro y opciones para daltonismo; navegación por teclado y objetivo WCAG 2.1 AA en flujos principales; indicar antigüedad de datos cuando no haya conexión. |
| RNF10 | Compatibilidad y adaptación | Navegadores modernos (Chrome, Firefox, Edge y Safari); Android/iOS según Expo SDK del proyecto; diseño adaptable desde 360×640 px hasta 1280×720 px sin pérdida de funciones principales ni desbordamiento horizontal. |

### 8.1 Endpoints críticos de referencia
La estrategia de calidad enumera como críticos, entre otros, `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/users/{id}`, `GET /api/environments/{id}`, `GET /api/monitoring/current` y `GET /api/monitoring/history`. Las rutas reales deben verificarse contra los controladores y el API Gateway desplegados; los ejemplos no constituyen un contrato OpenAPI definitivo.

## 9. Casos de uso

| ID | Caso de uso | Actor principal | Requisitos relacionados |
|---|---|---|---|
| CU-01 | Registrarse | Usuario | RF1.3, RF1.5 |
| CU-02 | Iniciar sesión con correo y contraseña | Usuario, Administrador, Superadministrador | RF1.1, RF1.8 |
| CU-03 | Iniciar sesión con Google o Facebook | Usuario | RF1.4 |
| CU-04 | Recuperar o cambiar contraseña | Usuario | RF1.6, RF1.7 |
| CU-05 | Consultar panel y ranking de ambientes | Usuario | RF3.1–RF3.3 |
| CU-06 | Buscar y filtrar ambientes | Usuario | RF4.1, RF4.2 |
| CU-07 | Ver detalle del ambiente y sus variables | Usuario | RF5.1, RF5.3 |
| CU-08 | Consultar análisis histórico | Usuario | RF3.4, RF9.2 |
| CU-09 | Calificar un ambiente | Usuario | RF5.2 |
| CU-10 | Gestionar favoritos | Usuario | RF7.2 |
| CU-11 | Revisar alertas y notificaciones | Usuario | RF6.1–RF6.4 |
| CU-12 | Gestionar perfil y preferencias | Usuario | RF7.1, RF7.3–RF7.5 |
| CU-13 | Administrar ambientes | Administrador | RF8.1 |
| CU-14 | Administrar sensores y umbrales | Administrador | RF8.2, RF8.3 |
| CU-15 | Configurar un dispositivo por Bluetooth | Administrador | RF9.4, RF9.5 |
| CU-16 | Administrar instituciones, sedes y usuarios | Superadministrador | RF2.1–RF2.4 |
| CU-17 | Enviar mediciones | Dispositivo IoT | RF9.1 |

### 9.1 Flujo general de ingesta
1. El dispositivo obtiene una lectura y la asocia con sensor, variable y marca temporal.
2. Envía la medición por el canal configurado; HTTP/HTTPS es el canal base documentado.
3. El servicio valida estructura, rango, identidad del dispositivo y duplicidad, según las capacidades habilitadas.
4. La medición aceptada se almacena y queda disponible para consulta actual/histórica.
5. La lógica de monitoreo compara el valor con los umbrales activos y genera o actualiza una alerta cuando corresponde.
6. Los clientes consultan el valor y muestran estado, unidad y hora de actualización.

## 10. Reglas de negocio y validación
- Toda operación protegida requiere identidad autenticada y autorización acorde con el rol.
- Las consultas deben limitarse al ámbito institucional y de sede autorizado.
- Las mediciones deben incluir identificación suficiente para relacionarlas con sensor, variable y ambiente.
- Los datos fuera del rango válido de una variable deben rechazarse o marcarse como inválidos conforme a una política explícita; no deben mezclarse silenciosamente con mediciones válidas.
- Las mediciones reintentadas no deben producir duplicados.
- Los umbrales deben tener vigencia temporal y conservar historial de cambios.
- Una alerta `WARNING` corresponde a desviación del rango normal dentro de la banda de advertencia; `CRITICAL` corresponde a los límites críticos definidos para el tipo de ambiente.
- Los errores de autenticación y autorización deben diferenciarse (401 y 403).
- Las credenciales, secretos y tokens completos no deben aparecer en logs ni en mensajes de error.
- La fecha/hora y zona horaria de mediciones deben tratarse consistentemente para el análisis histórico.

## 11. Criterios globales de aceptación
El producto podrá considerarse aceptado para una entrega solo cuando se disponga de evidencia verificable de:
1. Registro, autenticación, cierre de sesión y autorización por roles.
2. Aislamiento entre instituciones y sedes, incluyendo intentos de acceso no autorizado.
3. Administración de ambientes y consulta de valores ambientales.
4. Recepción, validación, persistencia y consulta de mediciones sin duplicados por reintento.
5. Cálculo de alertas con pruebas de valores normales, de advertencia, críticos y límites.
6. Consultas históricas y comportamiento correcto en rangos de fechas.
7. Interfaz web/móvil y preferencias declaradas, con diferencias funcionales documentadas.
8. Despliegue reproducible, configuración externa y recuperación desde respaldo.
9. Evidencia de rendimiento, seguridad, accesibilidad y compatibilidad para los objetivos RNF aplicables.
10. Trazabilidad de requisitos a casos de prueba, resultados, defectos y correcciones.

## 12. Trazabilidad y gestión de cambios
- Los requisitos funcionales usan identificadores `RFx.y`; los no funcionales, `RNFx`; los casos de uso, `CU-xx`.
- La matriz de trazabilidad debe vincular requisito, historia de usuario, servicio responsable, caso de prueba, evidencia y estado.
- Cualquier cambio de alcance, umbral, rol, API o flujo de autenticación debe actualizar este documento, la matriz de trazabilidad, los contratos de API y los manuales afectados.
- Los cambios de umbrales deben revisarse por su impacto en backend, web, móvil, pruebas y datos históricos.

## 13. Riesgos y puntos por confirmar
1. Completar y probar el aislamiento multiinstitución/sede (RF2.3).
2. Resolver la discrepancia entre la mención de MQTT en la SRS anterior y la ingesta HTTP documentada en arquitectura/código.
3. Conectar la interfaz de notificaciones a alertas reales del backend y definir notificación externa (RF6.3/RF6.5).
4. Completar la administración real de sensores y dispositivos (RF8.2/RF9.5), evitando datos de ejemplo.
5. Definir formalmente los límites inclusivos/exclusivos de los umbrales y probar valores fronterizos.
6. Aportar reportes para metas RNF: carga, cobertura, recuperación, seguridad, accesibilidad y compatibilidad.
7. Confirmar que las rutas de API descritas coincidan con los controladores y gateway de la versión que se desplegará.
8. Verificar configuración y credenciales de Google/Facebook y del servicio de correo antes de anunciar esos flujos como disponibles.

## 14. Glosario
- **API Gateway:** punto de entrada que enruta solicitudes a servicios.
- **BLE:** Bluetooth Low Energy; comunicación Bluetooth de bajo consumo.
- **CO₂:** dióxido de carbono.
- **IoT:** Internet de las Cosas.
- **MVP:** producto mínimo viable.
- **OAuth2/OIDC:** estándares/protocolos para autorización e identidad federada.
- **P95:** percentil 95 del tiempo de respuesta.
- **RPO:** punto objetivo de recuperación; pérdida de datos máxima objetivo.
- **RTO:** tiempo objetivo de recuperación.
- **SRS:** Software Requirements Specification / Especificación de Requisitos de Software.
- **Tenant:** ámbito lógico de una institución en una plataforma multiinstitución.
- **Umbral:** límite usado para clasificar una medición.

## 15. Referencias documentales
- SRS EduAirControl, versión fuente 1.1, entregada como `SRS_EduAirControl(1).docx`.
- Repositorio entregado `proyecto.zip`, especialmente `Docs-ea-control/04-requirements/`, `05-architecture/overview.md`, `08-uml/diagram-index.md`, `09-microservices/service-catalog.md`, `11-quality/testing-strategy.md` y `microservicios/ms-environment-monitoring/`.
- IEEE 830, como referencia histórica de la estructura del documento; para futuras revisiones, mantener una especificación verificable y trazable de requisitos.
- World Wide Web Consortium (W3C). Web Content Accessibility Guidelines (WCAG) 2.1, objetivo de accesibilidad referido en la fuente.
- Las referencias bibliográficas ambientales de la SRS original deben verificarse en cuanto a título, año, disponibilidad y relación exacta con los umbrales antes de usarse como respaldo normativo.
