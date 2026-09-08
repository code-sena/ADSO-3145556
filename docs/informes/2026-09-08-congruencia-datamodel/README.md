# Ficha ADSO-3145556 — Congruencia 02·03·06 y contrapropuesta de modelo de datos

Revisión de los 10 repositorios `-docs` de la ficha, en la organización **code-sena**.
Corte: 8 de septiembre de 2026.

## Qué hay aquí

```
index.html                  Cuadro comparativo de los 10 proyectos y lectura de conjunto
proyectos/<proyecto>.html   Informe individual: estado de 02/03/06, congruencia,
                            normalización actual, dominios y modelo propuesto
propuestas/<proyecto>-06.md Contrapropuesta lista para copiar al repositorio del equipo
gen.py, spec*.py            Generadores; permiten regenerar todo tras una corrección
congruencia.py              Cálculo de cobertura entidades → tablas
```

Abra `index.html` en el navegador. Los informes individuales se enlazan desde ahí.

## Cómo se hizo la evaluación

Cada repositorio se comparó **contra su propio commit de scaffold**
(`feat: apply microservices-governance-framework scaffold`) usando la API de comparación
de GitHub, de modo que la plantilla no cuente como trabajo del equipo. Los archivos que
conservan exactamente el tamaño de la plantilla se detectan y se reportan como *sin iniciar*:

| Archivo de plantilla | Bytes |
|---|---|
| `02-domain/entities-and-rules.md` | 8 088 |
| `02-domain/domain-map.md` | 7 954 |
| `02-domain/domain-events.md` | 7 667 |
| `03-product/vision.md` | 2 878 |
| `03-product/problem-framing.md` | 3 578 |
| `06-data/models.md` | 7 369 |

Para los proyectos con el trabajo repartido en varias ramas se tomó la rama más completa
por carpeta, de modo que ningún equipo pierda crédito por trabajo hecho pero no integrado.

## Resultado en una línea

**4 de 10** proyectos tienen alguna tabla en `06-data`. Seis conservan el archivo de
plantilla intacto. Entre los cuatro que sí modelaron, la congruencia con su propio dominio
va del **25 %** (edu-air-control) al **100 %** (rent-car, save-your-water).

## Regla de oro

Ninguna contrapropuesta cambia el enfoque del proyecto. Todas las entidades salen de lo que
cada equipo ya escribió en `02-domain` y `03-product`. Donde un equipo no tenía dominio
(`vehicle-washing`, `energy-monitor`), las entidades se derivaron de su propio
`discovery-brief` y de su mapa de bounded contexts, y así queda dicho en su informe.

## Estándar aplicado a los 10 modelos

- **Normalización mínima 3FN.** Sin grupos repetitivos ni listas en campos de texto (1FN);
  sin dependencias parciales sobre claves compuestas (2FN); sin atributos transitivos —
  estados, tipos y unidades viven en catálogos (3FN).
- **Valores derivados no se almacenan**, salvo dos excepciones documentadas por tabla:
  importes congelados por obligación legal y agregados materializados por rendimiento,
  marcados como recalculables.
- **Nomenclatura íntegramente en inglés**: esquemas, tablas, columnas y valores de catálogo.
  `snake_case`, tabla en singular, PK `<tabla>_id`.
- **Bloque de auditoría en toda tabla de negocio**: `created_at`, `created_by`, `updated_at`,
  `updated_by`, `deleted_at`, `deleted_by`, `row_version`. Las tablas inmutables de alto
  volumen (series temporales, bitácoras, consentimientos) llevan solo `created_at` y
  `created_by`, y así se indica en cada una.
- **Núcleo compartido** de identidad y auditoría idéntico en los 10 proyectos, para que un
  mismo criterio de seguridad y trazabilidad aplique a toda la ficha.

## Alcance de la medición

Mide **volumen, cobertura y estructura**, no corrección semántica del contenido. Dice qué
entidades existen, dónde están y cómo se relacionan; no dice si las reglas de negocio
escritas son correctas. Eso requiere leer los documentos con el equipo.
