# Software design / Diseño del software

**Training project deliverable / Producto del proyecto formativo:** Documentación de diseño del software (interfaces gráficas, modelo arquitectónico, patrones de diseño, modelo de datos).

## Required content / Contenido requerido

- [x] Graphical interfaces: mockups or prototype / Interfaces gráficas: mockups o prototipo
- [x] Architectural model / Modelo arquitectónico
- [x] Design patterns applied, with where they are used / Patrones de diseño aplicados, indicando dónde se usan
- [x] Data model: conceptual, logical and physical / Modelo de datos: conceptual, lógico y físico

## Evidence / Evidencia

- Conceptual / Conceptual: domain entities and rules / entidades y reglas del dominio, [`docs/02-domain/entities-and-rules.md`](https://github.com/mauro-1508/traduce-senas-evidencias/blob/final-delivery-2026/docs/02-domain/entities-and-rules.md) and [`docs/02-domain/domain-map.md`](https://github.com/mauro-1508/traduce-senas-evidencias/blob/final-delivery-2026/docs/02-domain/domain-map.md)
- Logical / Lógico: entity-relationship diagram and data dictionary in `software-design-es.pdf` (Figure 16, Tables 6 onwards) / diagrama entidad-relación y diccionario de datos en `software-design-es.pdf` (Figura 16, Tablas 6 en adelante); [`docs/06-data/models.md`](https://github.com/mauro-1508/traduce-senas-evidencias/blob/final-delivery-2026/docs/06-data/models.md) and [`traduce_senas.dbml`](https://github.com/mauro-1508/traduce-senas-evidencias/blob/final-delivery-2026/docs/06-data/traduce_senas.dbml)
- Physical / Físico: SQL scripts of each service / scripts SQL de cada servicio, `services/<service>/db/*.sql` in [backendTS](https://github.com/mauro-1508/backendTS/tree/final-delivery-2026/services)

## Format / Formato

- Final version in PDF, plus the editable source when it exists. / Versión final en PDF y, si existe, el archivo fuente editable.
- English file names, without spaces or accents: `requirements-specification.pdf`. / Nombres de archivo en inglés, sin espacios ni tildes.
- Files over 50 MB go to Drive and are listed in the project `README.md`. / Los archivos de más de 50 MB van a Drive y se relacionan en el `README.md` del proyecto.
- Do not add passwords, tokens, keys or real user data. / No incluya contraseñas, tokens, llaves ni datos reales de usuarios.
