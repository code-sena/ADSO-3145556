---
title: "Artículo del proyecto"
lang: es
documents: [TE-0002]
status: borrador
---
# Artículo del proyecto — Tu Evento

> Documento consolidado de Tu Evento. Reúne los documentos siguientes, cuyo contenido se conserva sin cambios; los metadatos de conversión de cada uno van en un comentario HTML al inicio de su sección.

**Contenido**

1. [Artículo del proyecto](#doc-te-0002)

---

<a id="doc-te-0002"></a>

<!--
ruta_anterior: es/00_Informes/Articulo_TuEvento.md
id: TE-0002
title: "Articulo_TuEvento"
lang: es
source: 00_Informes/Articulo_TuEvento.pdf
source_sha256: c1150ee006a6ef9e0e5a0c85d4669a6da163cc54a200d2c8fe54ba32739eb8a3
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–7: «{n}»."
  - "PDF generado sin LaTeX: los guiones al final de línea son guiones reales y se conservan al unir las líneas (no son guiones de corte silábico)."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
-->

# Artículo del proyecto

## TuEvento: plataforma web y móvil para la gestión de eventos y la venta de entradas con selección de asientos en tiempo real

Jhampier Santos Ortiz y Cristofer David Lozano Contreras

Servicio Nacional de Aprendizaje (SENA)

Análisis y Desarrollo de Software (ADSO), ficha 3145556

Instructora: Karol Daniela Correa

Octubre de 2026

## TuEvento: plataforma web y móvil para la gestión de eventos y la venta de entradas con selección de asientos en tiempo real

## Resumen

TuEvento es una plataforma web y móvil que busca facilitar la organización de eventos y la venta de entradas mediante herramientas para gestionar reservas y seleccionar asientos en tiempo real. El proyecto se desarrolló a partir de la identificación de necesidades de organizadores y asistentes, utilizando Scrum como metodología de trabajo. La solución integra tecnologías para la gestión de eventos, usuarios, pagos y notificaciones. Este artículo presenta el proceso de desarrollo, las características principales del sistema, los resultados obtenidos y algunos aspectos que todavía se encuentran pendientes.

Palabras clave: gestión de eventos, venta de entradas, selección de asientos, Scrum, arquitectura de software.

## TuEvento: plataforma web y móvil para la gestión de eventos y la venta de entradas con selección de asientos en tiempo real

## Introducción

La organización de eventos requiere gestionar diferentes procesos, como el registro de asistentes, la venta de entradas y el control de acceso. Cuando estas actividades se realizan manualmente o mediante herramientas separadas, pueden presentarse dificultades para administrar las reservas y mantener la información actualizada.

A partir de esta necesidad surge TuEvento, una plataforma que reúne herramientas para que los organizadores puedan administrar sus eventos y los asistentes consulten opciones, seleccionen asientos y gestionen sus entradas. El sistema también cuenta con funciones administrativas para controlar usuarios, roles y organizadores.

El objetivo del proyecto es centralizar estos procesos en una solución que pueda utilizarse desde la web y dispositivos móviles, facilitando la gestión de los eventos y la experiencia de los asistentes.

## Metodología y desarrollo

Para definir las funcionalidades se realizó una entrevista a un organizador de eventos y una encuesta a asistentes. También se analizaron plataformas existentes para identificar características que pudieran servir como referencia. Los resultados ayudaron a establecer las necesidades principales del sistema, aunque, debido al tamaño reducido de las muestras, se utilizaron como orientación para el diseño y no como evidencia estadística generalizable.

El desarrollo se organizó mediante Scrum, con ocho sprints entre marzo y octubre de 2026. Durante este periodo se trabajó en la documentación de requisitos, el diseño de las interfaces, la construcción del backend, el desarrollo de las aplicaciones y las pruebas del sistema. Este enfoque permitió distribuir el trabajo por etapas y revisar los avances durante el desarrollo (Schwaber & Sutherland, 2020).

En cuanto a las tecnologías, el backend utiliza Spring Boot y PostgreSQL, mientras que la aplicación web está desarrollada con React y la móvil con React Native. Para la comunicación entre los componentes se utilizan APIs REST y WebSocket, este último para actualizar información en tiempo real, como la disponibilidad de los asientos.

La arquitectura actual es un monolito modular organizado con Domain-Driven Design (DDD) y arquitectura hexagonal. Esta organización busca separar la lógica del negocio de los detalles técnicos y facilitar el mantenimiento del sistema (Cockburn, 2005; Evans, 2003). Como parte de la evolución contemplada para el proyecto, se considera una posible migración hacia microservicios, pero esta transición todavía no se ha realizado.

Entre las funcionalidades principales se encuentran la creación y gestión de eventos, las reservas, la selección de asientos, las notificaciones, los perfiles de usuario y la billetera digital. También se implementan mecanismos de autenticación y control de acceso según los roles definidos.

## Resultados y limitaciones

Como resultado del desarrollo, TuEvento cuenta con componentes para gestionar eventos, usuarios, reservas y entradas, además de interfaces web y móviles. De acuerdo con las métricas del repositorio, el proyecto supera las 71.000 líneas de código y dispone de 114 métodos de prueba unitaria en el backend.

Sin embargo, todavía existen funciones por completar. La pasarela de pagos es un sandbox para pruebas del flujo de pago y aún no se ha integrado un proveedor real. Además, la aplicación móvil no cuenta con la pantalla para escanear códigos QR durante el ingreso de los asistentes. También están pendientes las pruebas automatizadas de las aplicaciones web y móvil, junto con algunas funciones administrativas y validaciones relacionadas con la seguridad.

Estos aspectos deben tenerse en cuenta para continuar mejorando el sistema. Entre los siguientes pasos se encuentran integrar una pasarela de pago real, completar la validación de entradas, ampliar las pruebas y evaluar la evolución de la arquitectura según las necesidades futuras del proyecto.

## Conclusiones

El desarrollo de TuEvento permitió construir una plataforma que reúne diferentes procesos relacionados con la organización de eventos y la venta de entradas. La aplicación de Scrum y el uso de tecnologías como Spring Boot, PostgreSQL, React y React Native permitieron desarrollar las funcionalidades principales para las plataformas web y móvil.

Aunque el sistema cuenta con avances importantes, todavía requiere completar algunas funciones y fortalecer sus pruebas antes de considerarlo preparado para un entorno de producción. A futuro, la integración de pagos reales, la mejora de los mecanismos de validación y la posible migración a microservicios representan oportunidades para continuar su evolución.

# Referencias

Cockburn, A. (2005). Hexagonal architecture.

https://alistair.cockburn.us/hexagonal-architecture/

Evans, E. (2003). Domain-driven design: Tackling complexity in the heart

of software. Addison-Wesley.

Schwaber, K., & Sutherland, J. (2020). The Scrum Guide.

https://scrumguides.org/

SeatGeek. (n.d.). SeatGeek. https://seatgeek.com/

Ticketmaster. (n.d.). Ticketmaster. https://www.ticketmaster.com/ Time.ly. (n.d.). Nuevas tendencias en la industria de eventos que debes

seguir. https://time.ly/es/blog/nuevas-tendencias-en-la-industria-de-eventos-que-debes-seguir/
