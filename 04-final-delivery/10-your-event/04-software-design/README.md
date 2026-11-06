# Software design / Diseño del software

**Training project deliverable / Producto del proyecto formativo:** Documentación de diseño del software (interfaces gráficas, modelo arquitectónico, patrones de diseño, modelo de datos).

## Required content / Contenido requerido

- [x] Graphical interfaces: mockups or prototype / Interfaces gráficas: mockups o prototipo
- [x] Architectural model / Modelo arquitectónico
- [x] Design patterns applied, with where they are used / Patrones de diseño aplicados, indicando dónde se usan
- [x] Data model: conceptual, logical and physical / Modelo de datos: conceptual, lógico y físico

## Format / Formato

- Final version in PDF, plus the editable source when it exists. / Versión final en PDF y, si existe, el archivo fuente editable.
- English file names, without spaces or accents: `requirements-specification.pdf`. / Nombres de archivo en inglés, sin espacios ni tildes.
- Files over 50 MB go to Drive and are listed in the project `README.md`. / Los archivos de más de 50 MB van a Drive y se relacionan en el `README.md` del proyecto.
- Do not add passwords, tokens, keys or real user data. / No incluya contraseñas, tokens, llaves ni datos reales de usuarios.

## Files / Archivos

| File / Archivo | Content / Contenido |
|---|---|
| [`software-design.pdf`](software-design.pdf) | Design patterns (where they are used), architecture, package, process, flow and activity diagrams / Patrones de diseño (dónde se usan), arquitectura, diagramas de paquetes, procesos, flujo y actividades |
| [`user-interfaces-prototype.pdf`](user-interfaces-prototype.pdf) | Graphical interfaces: Figma prototype link and site map / Interfaces gráficas: enlace al prototipo en Figma y mapa del sitio |
| [`data-model.pdf`](data-model.pdf) | Data model: entity-relationship diagram and per-module models (conceptual and logical) and data dictionary / Modelo de datos: diagrama entidad-relación y modelos por módulo (conceptual y lógico) y diccionario de datos |
| [`database-script-design.sql`](database-script-design.sql) | Physical model as designed / Modelo físico diseñado |
| [`database-script-implemented.sql`](database-script-implemented.sql) | Physical model as implemented (Liquibase schema exported with pg_dump) / Modelo físico implementado (esquema de Liquibase exportado con pg_dump) |

Editable source: the Markdown files in `source/es/`, taken from the documentation repository [TheJhampierBoy/TuEvento-Docs](https://github.com/TheJhampierBoy/TuEvento-Docs) (`es/`; English version in `en/`). / Fuente editable: los archivos Markdown de `source/es/`, tomados del repositorio de documentación (`es/`; versión en inglés en `en/`).
