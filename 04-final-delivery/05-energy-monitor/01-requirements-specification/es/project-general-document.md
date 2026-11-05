# Acta de constitución del proyecto EnergyMonitor

Juan Esteban Cortés Parra y Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Tecnólogo en Análisis y Desarrollo de Software (ADSO), ficha 3145556

16 de octubre de 2026 (actualizado el 28 de octubre de 2026)

## Control de versiones

| Versión | Fecha      | Descripción del cambio                                                                                          | Responsables                                  |
|---------|------------|-----------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| 1.0     | 16/10/2026 | Primera versión del acta de constitución: justificación, objetivos, alcance, presupuesto, cronograma y equipo.   | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.1     | 20/10/2026 | Alineación con el sistema desplegado: administración de cuentas dentro del alcance, presupuesto con el servidor en producción y el costo total estimado, puesta en producción en `https://energy-monitor.tech` con integración y despliegue continuos, y estado de objetivos e hitos al 20 de octubre. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.2     | 24/10/2026 | Componentes del alcance según la arquitectura en producción: usuarios, dispositivos y mediciones como servicios independientes detrás de un API gateway, y migración en curso de los módulos restantes. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.3     | 28/10/2026 | Migración completada: el API gateway y los siete servicios en producción, y el backend monolítico retirado. Registro con verificación previa del correo y aceptación de términos, reglas de las cuentas creadas con Google (sin cambio de contraseña y borrado con código por correo) y edición independiente del nombre y el apellido. Estado de hitos y plan de la fase final al 28 de octubre. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.4     | 28/10/2026 | Ayuda dentro de la aplicación (documentación según el rol, soporte para usuarios y novedades que se actualizan solas), fotos de perfil visibles en la página y en los hogares, alerta de consumo inusual de un dispositivo y explicación del push en Brave. | Cortés Parra, J. E.; García Artunduaga, M. A. |

## Resumen

Este documento es el acta de constitución del proyecto EnergyMonitor, una solución que permite a los hogares conocer el consumo eléctrico de cada electrodoméstico, identificar los periodos de mayor demanda, recibir alertas y obtener recomendaciones de ahorro. Define por qué se hace el proyecto y el valor que aporta, sus objetivos con indicadores medibles, el alcance incluido y excluido, el presupuesto, las fases, los hitos y el cronograma hasta la fecha de entrega, el 28 de octubre de 2026, y el equipo de trabajo con sus roles. La solución integra un dispositivo de medición que se conecta a cada electrodoméstico, una aplicación web y una aplicación móvil para Android. Al 28 de octubre de 2026 el sistema está desplegado en producción en `https://energy-monitor.tech`, repartido en un API gateway y siete servicios independientes, y se actualiza mediante integración y despliegue continuos. El detalle de las funciones se desarrolla en la especificación de requisitos.

*Palabras clave:* acta de constitución, consumo eléctrico, ahorro de energía, hogares, alertas

## Introducción y justificación

### Contexto del proyecto

EnergyMonitor permite que los hogares conozcan el consumo eléctrico de cada electrodoméstico, identifiquen los periodos de mayor demanda, reciban alertas y obtengan recomendaciones para ahorrar energía. La medición la realiza un dispositivo propio que se conecta al electrodoméstico y envía lecturas reales de su consumo. El usuario consulta los datos desde una aplicación web y una aplicación móvil para Android.

El proyecto es un proyecto formativo del programa Tecnólogo en Análisis y Desarrollo de Software (ADSO) del SENA, desarrollado por dos aprendices y con fecha de entrega el 28 de octubre de 2026.

### Problema que resuelve

El alto consumo de energía en los hogares colombianos se debe en buena parte a la falta de conciencia energética. Las personas desconocen qué electrodomésticos consumen más y cómo varía su consumo a lo largo del día, y por ello no pueden corregir hábitos como dejar un aparato encendido sin necesidad o mantener un equipo que consume más de lo debido. El resultado son facturas elevadas y un mayor impacto ambiental.

### Justificación

El proyecto parte de la premisa de que la mayoría de los hogares no cuenta con herramientas que muestren su consumo en tiempo real ni que identifiquen cuáles electrodomésticos pesan más en la factura. Un sistema que mida el consumo de cada aparato, avise cuando se superan límites razonables y proponga acciones de ahorro a partir de los datos medidos facilita decisiones informadas y fomenta hábitos de uso responsable y sostenible de la electricidad. Medir con un sensor real, y no con estimaciones, es lo que permite que las alertas y las recomendaciones se basen en el comportamiento efectivo de cada hogar.

### Beneficios esperados

- Para los hogares: saber en qué electrodomésticos se va la energía, reaccionar a tiempo ante un consumo excesivo y tomar decisiones de ahorro con datos propios, lo que ayuda a controlar el costo de la factura.
- Para el entorno: reducir el consumo innecesario de electricidad y, con ello, su impacto ambiental.
- Para el programa de formación: un sistema completo y demostrable, de la medición en un electrodoméstico real hasta el aviso en el teléfono del usuario.

## Objetivos del proyecto

### Objetivo general

Desarrollar un sistema de control del recurso energético que permita monitorear en tiempo real el consumo eléctrico de los hogares e identificar los electrodomésticos que generan mayor gasto, para promover un uso eficiente y responsable de la electricidad.

### Objetivos específicos

Cada objetivo específico tiene un indicador medible y una meta alcanzable dentro del plazo del proyecto. La Tabla 1 los presenta junto con su cobertura al 20 de octubre de 2026.

**Tabla 1**

*Objetivos específicos, indicadores y cobertura*

| Código | Objetivo | Indicador y meta | Cobertura al 20 de octubre de 2026 |
|---|---|---|---|
| OBJ-01 | Medir el consumo eléctrico de cada electrodoméstico del hogar | Una lectura cada 60 segundos de voltaje, corriente, potencia activa y energía acumulada por cada dispositivo de medición conectado | Cubierto. Dispositivo de medición operativo |
| OBJ-02 | Mostrar el consumo de forma clara en una aplicación web y una móvil | Resumen e historial de consumo por hogar en la web, y consulta de hogares, alertas y recomendaciones en la aplicación Android, en español, inglés, francés y portugués | Aplicación web desplegada en producción. Aplicación móvil en desarrollo, integrada con la API |
| OBJ-03 | Emitir alertas automáticas ante consumo inusual o excesivo | Cuatro tipos de alerta (dispositivo vinculado, desconexión, consumo alto o crítico, límite diario o mensual). Una desconexión se detecta en un máximo de 150 segundos | Cubierto |
| OBJ-04 | Fomentar hábitos responsables mediante recomendaciones basadas en los datos medidos | Cinco reglas de ahorro evaluadas cada hora. La misma recomendación no se repite en 7 días | Cubierto |
| OBJ-05 | Entregar el sistema funcionando de extremo a extremo | Demostración en vivo, desde la medición de un dispositivo físico hasta una alerta o recomendación en un teléfono, sin intervención manual, el 28 de octubre de 2026 | Sistema desplegado en producción. Demostración en vivo prevista para la entrega del 28 de octubre; la vinculación de un medidor en producción está pendiente de evidencia |

## Alcance general

### Dentro del alcance

La Tabla 2 enumera lo que el proyecto construye y el módulo del sistema que responde por cada elemento.

**Tabla 2**

*Elementos dentro del alcance*

| Elemento | Descripción | Módulo o componente |
|---|---|---|
| Usuarios y perfiles | Registro con verificación previa del correo y aceptación de los términos, inicio de sesión, recuperación de contraseña, inicio con Google, perfil (nombre, apellido y foto) y eliminación de cuenta | Módulo de Gestión de Usuarios y Perfiles |
| Administración | Consulta de cuentas, cambio de estado, asignación y retiro del rol de administrador, y avisos para los administradores | Módulo de Gestión de Usuarios y Perfiles |
| Hogares | Hogares, miembros, roles de propietario y miembro, favoritos, límites de consumo, eliminación del hogar y transferencia de propiedad | Módulo de Gestión de Hogares |
| Dispositivos | Registro, vinculación, edición y desvinculación de los dispositivos de medición | Módulo de Gestión de Dispositivos |
| Mediciones | Recepción de lecturas, estado de los dispositivos, historial y estadísticas de consumo | Módulo de Mediciones y Consumo |
| Alertas | Alertas de consumo, de conexión y de consumo inusual de los dispositivos | Módulo de Alertas |
| Recomendaciones | Sugerencias de ahorro basadas en el consumo medido | Módulo de Recomendaciones de Ahorro |
| Notificaciones | Avisos por correo y por el navegador, avisos dentro de la aplicación para administradores, preferencias y registro de entregas | Módulo de Notificaciones |
| Aplicación web | Cliente completo de gestión, historial y estadísticas, con ayuda en Ajustes (documentación, soporte y novedades) | Aplicación web |
| Aplicación móvil | Cliente Android de consulta y alertas | Aplicación móvil |
| Dispositivo de medición | Aparato físico que mide el consumo de cada electrodoméstico y envía las lecturas | Dispositivo de medición |
| Despliegue | Publicación del sistema en un servidor con HTTPS, y actualización con pruebas automáticas, aprobación y reversión | Infraestructura |

Desde el 28 de octubre de 2026, todos los módulos funcionan en producción como servicios independientes (seguridad, dispositivos, mediciones, hogares, notificaciones, alertas y recomendaciones), detrás de un punto de entrada único para la API. El servicio central que los reunía a todos se retiró. El detalle técnico está en la propuesta técnica.

### Fuera del alcance

La Tabla 3 lista de forma explícita lo que el proyecto no construye y el motivo.

**Tabla 3**

*Elementos fuera del alcance*

| Elemento | Motivo |
|---|---|
| Aplicación para iPhone (iOS) | La aplicación móvil solo se desarrolla para Android. |
| Registro manual del consumo desde la factura | El dispositivo de medición registra el consumo de forma automática, por lo que no se contempla la captura manual. |
| Gestión de soporte y de contenidos desde la administración | La administración actual cubre cuentas, estados, roles y avisos; lo demás queda para una versión posterior. |
| Estimación del costo en moneda local | No existe acuerdo con un operador de energía que provea tarifas. El sistema reporta energía en kWh. |
| Actualización remota del software del dispositivo de medición | El dispositivo se actualiza conectándolo por cable. |
| Servicio para varias organizaciones a la vez | Cada instalación atiende a una sola organización. |

### Supuestos y restricciones

La Tabla 4 recoge los supuestos y restricciones que condicionan el proyecto.

**Tabla 4**

*Supuestos y restricciones del proyecto*

| Tipo | Descripción |
|---|---|
| Tiempo | Entrega el 28 de octubre de 2026, fecha fijada para la evaluación de la ficha 3145556 en el programa ADSO, según el calendario del programa ADSO para la ficha 3145556. |
| Presupuesto | Aproximadamente COP 180.000 invertidos en los componentes del dispositivo de medición y COP 48.000 en un mes de servidor. El dominio se obtuvo sin costo. |
| Tecnología | Se usan las herramientas definidas para el proyecto formativo, que se detallan en la propuesta técnica. |
| Equipo | Dos aprendices del programa ADSO. |
| Normativa | Protección de la información de los usuarios, con referencia a la Ley 1581 de 2012 (Congreso de la República de Colombia, 2012). |
| Conectividad | Se asume Wi-Fi continuo donde se instalan los dispositivos de medición. |

### Presupuesto

La Tabla 5 resume el presupuesto del proyecto.

**Tabla 5**

*Presupuesto del proyecto*

| Concepto | Valor |
|---|---|
| Componentes del dispositivo de medición | Aproximadamente COP 180.000 (COP 173.997 según el desglose de la propuesta técnica) |
| Alojamiento | COP 48.000: VPS Hostinger KVM 2 contratado por un mes (COP 27.900 mensuales con contrato anual) |
| Dominio y certificados | Sin costo: dominio `energy-monitor.tech` gratuito y certificados Let's Encrypt |
| Costo total estimado del proyecto | COP 57.021.997, incluidos los equipos del equipo de desarrollo y el recurso humano estimado (detalle en la propuesta técnica) |

## Cronograma e hitos

### Fases principales

El proyecto inició el 1 de mayo de 2026 y se planificó en cuatro fases con avances de una semana. La Tabla 6 describe cada fase con su periodo, objetivo y criterio de salida.

**Tabla 6**

*Fases del proyecto*

| Fase | Periodo | Objetivo | Criterio de salida |
|---|---|---|---|
| 1. Fundación y flujo central | 1 de mayo a 30 de junio | Un usuario se registra, crea o se une a un hogar, registra un dispositivo y ve llegar sus mediciones. | Un usuario registrado inicia sesión, crea un hogar, vincula un dispositivo y ve al menos una medición real registrada. |
| 2. Visibilidad del consumo y alertas | 1 de julio a 31 de agosto | El consumo es visible y el sistema avisa cuando algo es anormal. | Un hogar que supera su límite genera una alerta visible en web y móvil y se entrega una notificación. |
| 3. Recomendaciones e internacionalización | 1 a 30 de septiembre | Cerrar el ciclo del producto con orientación proactiva y terminar el trabajo transversal de la interfaz. | Un hogar con una semana de historial recibe al menos una recomendación basada en su propio consumo, y la interfaz se verifica en más de un idioma. |
| 4. Consolidación y entrega | 1 a 28 de octubre | Dejar el sistema listo para demostración y defensa. | El sistema se demuestra en vivo, desde la medición de un dispositivo físico hasta una alerta o recomendación en un teléfono. |

**Estado al 20 de octubre de 2026:** las funciones de las fases 1 a 3 están construidas y, durante la fase 4, el sistema se puso en producción en `https://energy-monitor.tech`, con integración y despliegue continuos. El 24 de octubre de 2026 se puso en producción la separación de los módulos de usuarios, dispositivos y mediciones en servicios independientes, y el 28 de octubre de 2026 se completó la separación con los de hogares, notificaciones, alertas y recomendaciones y el retiro del servicio central, sin cambios para los usuarios.

### Hitos

La Tabla 7 reúne los hitos del proyecto con su fecha estimada.

**Tabla 7**

*Hitos del proyecto*

| Código | Hito | Fecha estimada | Estado al 20 de octubre de 2026 |
|---|---|---|---|
| H-01 | Inicio del proyecto | 1 de mayo de 2026 | Cumplido |
| H-02 | Fin de la fase 1: un usuario registrado vincula un dispositivo y ve una medición real | 30 de junio de 2026 | Cumplido |
| H-03 | Fin de la fase 2: una alerta por superar un límite, visible en web y móvil con su notificación | 31 de agosto de 2026 | Cumplido |
| H-04 | Fin de la fase 3: recomendación basada en el consumo propio e interfaz en más de un idioma | 30 de septiembre de 2026 | Cumplido |
| H-05 | Documentación bilingüe completa (acta de constitución y especificación de requisitos) | 12 de octubre de 2026 | En curso |
| H-06 | Pruebas de extremo a extremo y manuales de usuario, técnico y de instalación | 14 de octubre de 2026 | Cumplido, salvo las pruebas de extremo a extremo en producción, que están pendientes de evidencia |
| H-07 | Informe técnico y de costos, y ensayo de la demostración | 15 de octubre de 2026 | Cumplido |
| H-08 | Entrega final y demostración en vivo | 28 de octubre de 2026 | Pendiente |
| H-09 | Puesta en producción en `https://energy-monitor.tech` con integración y despliegue continuos | 19 de octubre de 2026 | Cumplido |

### Plan de la fase final

La Tabla 8 detalla las actividades del periodo comprendido entre el 10 y el 28 de octubre de 2026, que corresponde a la fase 4, con la puesta en producción y la entrega.

**Tabla 8**

*Plan de actividades de la fase 4*

| Fechas | Actividades | Entregable |
|---|---|---|
| 10 a 12 de octubre | Confirmar y registrar los cambios pendientes. Completar el acta de constitución y la especificación de requisitos (ES y EN) | Documentos bilingües en formato APA 7 |
| 13 y 14 de octubre | Pruebas de extremo a extremo. Manuales de usuario (web y móvil), manual técnico y de instalación | Plan y evidencias de pruebas. Manuales |
| 15 de octubre | Informe técnico y de costos. Ensayo de la demostración en vivo | Informe técnico y de costos |
| 16 a 20 de octubre | Despliegue en el servidor de producción, pipelines de GitHub Actions y manual de implementación y despliegue | Sistema en producción y manual de implementación y despliegue |
| Hasta el 24 de octubre | Separación de los módulos de usuarios, dispositivos y mediciones en servicios independientes, con un punto de entrada único para la API | Servicios en producción y documentación actualizada |
| 25 a 28 de octubre | Separación de hogares, notificaciones, alertas y recomendaciones y retiro del servicio central. Registro con verificación del correo y términos, reglas de las cuentas de Google, pruebas de punta a punta y despliegue en producción del gateway y los siete servicios | El API gateway y los siete servicios en producción, y documentación final actualizada |
| 28 de octubre | Entrega final y demostración en vivo | Sistema funcionando y documentación completa |

La mejora de seguridad de las credenciales de los dispositivos ya se aplicó: el sistema solo guarda el hash de la clave de cada dispositivo. Queda sin fecha, y se ejecuta solo si el tiempo lo permite, la mejora de confiabilidad en el envío de lecturas. El único pendiente de la gestión de hogares y dispositivos, que es la gestión del historial del dispositivo según el periodo de vinculación, tampoco tiene fecha asignada. La separación en servicios de los módulos de Hogares, Alertas, Recomendaciones y Notificaciones, a cargo de Miguel Ángel García Artunduaga, se completó y se desplegó el 28 de octubre de 2026.

## Equipo de trabajo y roles

La Tabla 9 presenta quién hace qué en el proyecto. El equipo se compone de dos aprendices del programa ADSO.

**Tabla 9**

*Equipo de trabajo y roles*

| Integrante | Rol | Responsabilidades |
|---|---|---|
| Juan Esteban Cortés Parra | Líder del proyecto (Project Manager), Scrum Master, responsable de los datos del sistema y desarrollador | Planificación y seguimiento del proyecto, facilitación de la metodología, diseño y administración de los datos del sistema, análisis de la información, diseño y programación |
| Miguel Ángel García Artunduaga | Analista, QA y desarrollador | Aseguramiento de la calidad y pruebas, análisis de la información, diseño y programación |

## Referencias

Congreso de la República de Colombia. (2012, 17 de octubre). *Ley 1581 de 2012: Por la cual se dictan disposiciones generales para la protección de datos personales*. Diario Oficial n.º 48.587.
