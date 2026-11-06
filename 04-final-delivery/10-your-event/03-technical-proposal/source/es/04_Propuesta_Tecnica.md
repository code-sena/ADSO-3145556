---
title: "Propuesta técnica"
lang: es
documents: [TE-0080, TE-0079]
status: borrador
---
# Propuesta técnica — Tu Evento

> Documento consolidado de Tu Evento. Reúne los documentos siguientes, cuyo contenido se conserva sin cambios; los metadatos de conversión de cada uno van en un comentario HTML al inicio de su sección.

**Contenido**

1. [Propuesta técnica](#doc-te-0080)
2. [Ficha técnica](#doc-te-0079)

---

<a id="doc-te-0080"></a>

<!--
ruta_anterior: es/03_Propuesta_Tecnica/Propuesta_Tecnica_Tu_evento.md
id: TE-0080
title: "Propuesta Técnica-Tu evento"
lang: es
source: 03_Propuesta_Tecnica/Propuesta Técnica-Tu evento.docx
source_sha256: f3cf92828ddcc5f74a07c5a99edd437c583315f5631d1f1b3e73a03eebceca1c
conversion: "pandoc 3.9 (-t gfm --wrap=none --extract-media) + extracción de encabezados/pies; sin revisión manual"
converted_on: 2026-10-23
fidelity: parcial
losses:
  - "El original no usa estilos de título; los títulos visuales quedan como texto en negrita, sin jerarquía #. Pendiente de marcar a mano."
  - "Encabezados y pies de página extraídos aparte (pandoc no los convierte); en el original se repiten en cada página. Los campos se muestran como {PAGE}, {NUMPAGES}."
status: borrador
reviewed_by: ""
-->

# Propuesta técnica

**CapySoft**

**Propuesta técnica**

**Tu evento**

**Cristofer David Lozano Contreras**

**Jhampier Santos Ortiz**

**(3145556)**

**Servicio Nacional De Aprendizaje**

**Marzo 5 2025**

1.  **Resumen Ejecutivo**

El presente documento describe la propuesta técnica para el desarrollo del sistema “Tu Evento”, una plataforma innovadora orientada a la organización, gestión y personalización de eventos. El sistema busca solucionar las limitaciones actuales de las plataformas tradicionales de venta de entradas y gestión de espacios, las cuales ofrecen poca flexibilidad para eventos con configuraciones no estándar

“Tu Evento” permitirá a los organizadores diseñar mapas de asientos totalmente personalizados y a los asistentes interactuar con estos diseños de forma visual e intuitiva, seleccionando sus ubicaciones de manera sencilla. De esta forma, el sistema mejora la experiencia del usuario, optimiza la gestión del evento y ofrece una solución integral adaptable a distintos tipos de escenarios.

2.  **Objetivos del proyecto**

**2.1 Objetivos Específicos**

- Analizar las necesidades de organizadores y asistentes en la gestión de eventos.

- Diseñar la arquitectura del sistema (web, móvil y módulo administrativo).

- Desarrollar una interfaz gráfica intuitiva para la creación de mapas personalizados de asientos.

- Implementar un sistema de visualización interactiva para la selección de ubicaciones.

- Crear un módulo de administración para la gestión de eventos, usuarios y espacios.

- Optimizar la experiencia del usuario con usabilidad, accesibilidad y diseño responsivo.

- Ejecutar pruebas de validación y ajustes del sistema

3.  **Alcance del proyecto**

El sistema “Tu Evento” ofrecerá una plataforma web y móvil que permitirá:

A los organizadores:

- Crear eventos

- Diseñar mapas personalizados de espacios y asientos.

- Gestionar usuarios y configuraciones del evento.

> A los asistentes:

- Visualizar los mapas de asientos.

- Seleccionar su ubicación de forma interactiva.

- Consultar información del evento.

> A los administradores:

- Supervisar la operación del sistema.

- Gestionar usuarios, eventos y configuraciones generales

> **Alcance de plataformas.** Tu Evento se entrega como aplicación web (React) y aplicación móvil (React Native con Expo), ambas conectadas al mismo backend. No se desarrolla una aplicación de escritorio: la aplicación web funciona en los navegadores de escritorio y cubre a los usuarios que trabajan desde un computador, por lo que una aplicación de escritorio aparte duplicaría funciones sin aportar al alcance del proyecto.

4.  **Tecnologías a utilizar**

> **4.1 Backend**

- Lenguaje de programación: Java

- Framework: Spring Boot

- Base de datos: PostgreSQL

**4.2 Frontend**

- Aplicación web: React.js

- Aplicación móvil: React Native

**4.3 Librerias y Herramientas**\
En este apartado se describen las principales librerías, frameworks, APIs y herramientas que se utilizarán en el desarrollo del sistema, así como su propósito dentro de la arquitectura del proyecto.

- **Spring Boot:** Framework base para la construcción del backend.

- **Spring Security:** Control de autenticación y autorización de usuarios.

- **Hibernate / JPA:** Persistencia de datos.

- **PostgreSQL:** Sistema gestor de base de datos.

- **Pasarela de pagos sandbox:** Pruebas del flujo de pago electrónico, conectada al backend y sin cobros reales.

- **Swagger:** Documentación interactiva de la API.

5.  **Arquitectura del Sistema**

> El sistema implementará una arquitectura cliente-servidor, donde las aplicaciones web y móviles se comunicarán con un servidor central. El backend procesará la información de eventos, usuarios y espacios, mientras que el frontend mostrará de forma visual los mapas interactivos. La arquitectura está pensada para ser modular, escalable y adaptable a diferentes tipos de eventos.

6.  **Beneficios del Proyecto**

- Mayor flexibilidad en la personalización de eventos.

- Experiencia visual e intuitiva para los asistentes.

- Optimización en la gestión de espacios y usuarios.

- Plataforma adaptable a distintos tipos de escenarios y eventos.

- Mejora en la organización y control por parte de los administradores.

7.  **Conclusión**

> “Tu Evento” representa una solución innovadora frente a plataformas tradicionales como Ticketmaster o Eventbrite, ya que ofrece un alto nivel de personalización y una experiencia interactiva para organizadores y asistentes. Su implementación permitirá transformar la forma en que se diseñan y gestionan los eventos, brindando una herramienta moderna, flexible y eficiente


---

## Encabezados y pies de página del original

- **Encabezado** (`word/header1.xml`): texto «12»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/03_Propuesta_Tecnica/Propuesta_Tecnica_Tu_evento/header_footer/image7.jpeg)

---

<a id="doc-te-0079"></a>

<!--
ruta_anterior: es/03_Propuesta_Tecnica/Ficha_Tecnica.md
id: TE-0079
title: "Ficha Tecnica"
lang: es
source: 03_Propuesta_Tecnica/Ficha Tecnica.xlsx
source_sha256: 2c3561b6fb2112367daae462fe20047e44bc7a00934757d2fb1ba5d407e7e5d5
conversion: "openpyxl 3.1.5 (data_only + fórmulas); sin revisión manual"
converted_on: 2026-10-23
fidelity: parcial
losses:
  - "Formato visual (colores, bordes, anchos, fuentes y estilos del formato condicional) no se conserva."
  - "Celdas combinadas listadas pero no reproducidas como celda única."
  - "Los valores de las fórmulas son los guardados en caché por Excel; no se recalcularon."
status: borrador
reviewed_by: ""
-->

# Ficha Tecnica

> Conversión de `03_Propuesta_Tecnica/Ficha Tecnica.xlsx`: 2 hojas (R.Software, R.Hadware), 0 fórmulas. Las columnas se nombran con la letra de Excel y cada fila con su número original.

## Hoja: R.Software

Rango usado: `A1:H17` · 17 filas × 8 columnas · celdas combinadas: 17

| Fila | A | B | C | D | E | F | G | H |
|---|---|---|---|---|---|---|---|---|
| 1 | Requisitos de software |  |  |  |  |  |  |  |
| 2 | VS Code o IDE | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 3 | Visual Studio Code | 1.x (actualizaciones regulares) | Windows | Licencia gratuita | Windows: Windows 7, 8, 10, 11 (64 bits) |  |  |  |
| 4 | Visual Studio | 2022 (última versión estable) | Windows | Comunidad (gratis) | Windows 10 versión 1909 o superior (64 bits), 8 GB de RAM recomendados |  |  |  |
| 5 | Base de Datos | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 6 | PostgreSQL | 15.x (última versión estable) | Windows | Licencia abierta | Windows 10 (64-bit), 2 GB de RAM, 200 MB de espacio libre en disco. |  |  |  |
| 7 | Lenguajes de Programacion | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 8 | JavaScript | ES2023 (última versión) | Multiplataforma | Libre (Open Source) | Depende del navegador, pero en general: Procesador 1 GHz, 2 GB de RAM, espacio libre de disco  |  |  |  |
| 9 | Java  | 11.0 (última versión) |   Windows | MIT License | Windows: Windows 10 o superior, 2 GB de RAM, 2 GB de espacio libre en disco. |  |  |  |
| 10 | Frameworks | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 11 | React Native | Última versión estable (0.70.x o superior) | Windows | MIT License | Windows 10 o superior, 8 GB de RAM (mínimo recomendado), 10 GB de espacio libre en disco. |  |  |  |
| 12 | Fabric.js | Última versión | Multiplataforma | MIT License | No tiene requisitos específicos, solo un navegador moderno |  |  |  |
| 13 |  Spring Boot | 3.2.x (última versión estable) | Multiplataforma  | Apache License 2.0 | JDK 17 o superior, 2 GB de RAM, 2 GB de espacio libre en disco, conexión a internet para gestionar dependencias con Maven o Gradle. |  |  |  |
| 14 | API | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |
| 15 | Google Sign-In | Última versión estable | Multiplataforma | Gratis (Google Cloud Platform) | Cuenta de Google, registrar app en Google Cloud Console, configurar OAuth 2.0. |  |  |  |
| 16 | Facebook Login | Última versión estable | Multiplataforma | Gratis (Facebook Developer Portal) | Cuenta de Facebook, crear aplicación en Facebook Developer Portal, configurar OAuth. |  |  |  |
| 17 | Hosting /Dominio | Versión | Sistema Operativo | Licencia | Requerimientos Mínimos |  |  |  |

**Celdas combinadas** (el valor aparece solo en la celda superior izquierda): `A1:H1`, `E10:H10`, `E11:G11`, `E12:G12`, `E13:G13`, `E14:H14`, `E15:G15`, `E16:G16`, `E17:H17`, `E2:H2`, `E3:H3`, `E4:H4`, `E5:H5`, `E6:H6`, `E7:H7`, `E8:H8`, `E9:H9`


## Hoja: R.Hadware

Rango usado: `A1:G63` · 63 filas × 7 columnas · celdas combinadas: 26

| Fila | A | B | C | D | E | F | G |
|---|---|---|---|---|---|---|---|
| 1 | REQUISITOS DE HARDWARE |  |  |  |  |  |  |
| 2 | CANTIDAD | COMPONENTE | ESPECIFICACIONES |  |  |  |  |
| 3 | 4 | Computadora portatil o escritorio | Marca | Acer | Acer | Corporativo HP | Corporativo HP |
| 4 |  |  | Modelo | Aspire 515-52 | Nitro AN515-55 | Elitedesk 800 G1 | Elitedesk 600 G2 |
| 5 |  |  | Color | Negro | Negro | Negro | Negro |
| 6 |  |  | Componentes de rendimiento |  |  |  |  |
| 7 |  |  | Tarjeta Gráfica | Integrada | GeForce GTX 1650 | Integrada | Integrada |
| 8 |  |  | Procesador  | Intel core | Intel core | Intel core | Intel core |
| 9 |  |  | Modelo | i5 | i5 | i5 | i5 |
| 10 |  |  | Memoria RAM |  |  |  |  |
| 11 |  |  | Capacidad | 8GB | 8GB | 12GB | 8GB |
| 12 |  |  | Almacenamiento |  |  |  |  |
| 13 |  |  | Capacidad | 475GB | 475GB | 475GB | 1TB |
| 14 |  |  | Pantalla y sistema operativo |  |  |  |  |
| 15 |  |  | Tamaño | 15.6" | 15.8" | 15.6" | 15.6" |
| 16 |  |  | Resolución | 1366 x 768 60,06Hz | 1920 x 1080, 144Hz | 1921 x 1080, 60,06Hz | 1922 x 1080, 60,06Hz |
| 17 |  |  | Entrada táctil | No | No | No | No |
| 18 |  |  | Sistema Operativo | Windows 10 | Windows 11 | Windows 10 | Windows 10 |
| 19 | 3 | Android | Marca | Honor | Xiaomi | Xiaomi |  |
| 20 |  |  | Modelo | Honor x6a plus | RedmI Note 10 | Redmi Note 11 |  |
| 21 |  |  | Numero de serie | WDY-LX3 | 21061119AL | 2201117TL |  |
| 22 |  |  | Componentes de rendimiento |  |  |  |  |
| 23 |  |  | Procesador  | mediatek | Qualcomm®  | Qualcomm®  |  |
| 24 |  |  | Modelo | Helio  |  Snapdragon™ | Snapdragon™ |  |
| 25 |  |  | Serie | G36 | 678 | 680 |  |
| 26 |  |  | Memoria RAM |  |  |  |  |
| 27 |  |  | Capacidad | 6GB + HONOR RAM Turbo | 4GB | 6GB |  |
| 28 |  |  | Almacenamiento |  |  |  |  |
| 29 |  |  | Capacidad | 256GB | 128GB | 128GB |  |
| 30 |  |  | Pantalla |  |  |  |  |
| 31 |  |  | Tamaño | LCD de 6,56 pulgadas | 6,5″ pulgadas | 6,43" |  |
| 32 |  |  | Resolución | 1612 x 720  | 1080 X 2040  | 1080 x 2400  |  |
| 33 |  |  | Version de android | 14 | 13 | 14 |  |
| 34 | 1 | iOS | Marca | iphone  |  |  |  |
| 35 |  |  | Modelo | iphone 11  |  |  |  |
| 36 |  |  | Numero de serie | FFWLLRTRN73D |  |  |  |
| 37 |  |  | Componentes de rendimiento |  |  |  |  |
| 38 |  |  | Procesador  |  Bionic |  |  |  |
| 39 |  |  | Modelo | A |  |  |  |
| 40 |  |  | Serie | 13 |  |  |  |
| 41 |  |  | Memoria RAM |  |  |  |  |
| 42 |  |  | Capacidad | 4GB LPDDR4X |  |  |  |
| 43 |  |  | Almacenamiento |  |  |  |  |
| 44 |  |  | Capacidad | 128GB |  |  |  |
| 45 |  |  | Pantalla |  |  |  |  |
| 46 |  |  | Tamaño | 6.06" pulgadas |  |  |  |
| 47 |  |  | Resolución | 1792 x 828 |  |  |  |
| 48 |  |  | Version de iOS | 18 |  |  |  |
| 49 | 3 | tv | Marca | LG |  |  |  |
| 50 |  |  | Modelo | 32 |  |  |  |
| 51 |  |  | Numero de serie | FFWLLRTRN73D |  |  |  |
| 52 |  |  | Componentes de rendimiento |  |  |  |  |
| 53 |  |  | Procesador  |  Bionic |  |  |  |
| 54 |  |  | Modelo | A |  |  |  |
| 55 |  |  | Serie | 14 |  |  |  |
| 56 |  |  | Memoria RAM |  |  |  |  |
| 57 |  |  | Capacidad | 500 MB |  |  |  |
| 58 |  |  | Almacenamiento |  |  |  |  |
| 59 |  |  | Capacidad | 200GB |  |  |  |
| 60 |  |  | Pantalla |  |  |  |  |
| 61 |  |  | Tamaño | 32 Pulgadas |  |  |  |
| 62 |  |  | Resolución | 1,366 x 768 |  |  |  |
| 63 |  |  | Version de webOs | 22 |  |  |  |

**Celdas combinadas** (el valor aparece solo en la celda superior izquierda): `A19:A33`, `A1:G1`, `A34:A48`, `A3:A18`, `A49:A63`, `B19:B33`, `B34:B48`, `B3:B18`, `B49:B63`, `C10:G10`, `C12:G12`, `C14:G14`, `C22:F22`, `C26:F26`, `C28:F28`, `C2:G2`, `C30:F30`, `C37:D37`, `C41:D41`, `C43:D43`, `C45:D45`, `C52:D52`, `C56:D56`, `C58:D58`, `C60:D60`, `C6:G6`
