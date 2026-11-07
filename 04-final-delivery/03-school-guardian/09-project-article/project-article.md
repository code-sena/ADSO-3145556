# Guardián Escolar: plataforma de monitoreo y trazabilidad del transporte escolar

**Artículo técnico**

*Diseño, implementación y validación de Guardián Escolar*

**Integrantes del proyecto:**

- Johan Smith Santamaria Fernández
- Sharik Dayanna Rojas Ibarra
- Juan Pablo Chala Ramírez

**Programa:** Tecnólogo en Análisis y Desarrollo de Software
**Ficha:** 3145556
**Centro de formación:** Sede La Industria, La Empresa y Los Servicios - SENA
**Lugar:** Neiva, Huila, Colombia
**Fecha:** 16 de octubre de 2026

---

# 1. Introducción

## 1.1. El Problema

En el transporte escolar de Neiva y del departamento del Huila, la operación diaria descansa sobre coordinación manual. El acudiente no sabe, en tiempo real, dónde va el bus de su hijo, si llegó retrasado, si se desvió o si el estudiante subió y bajó donde correspondía. El conductor registra el abordaje en papel, sin una fuente única de verdad, y la institución termina gestionando incidentes por llamadas y mensajes de mensajería instantánea. Los rastreadores GPS que ya existen en algunos vehículos muestran la posición del bus, pero no integran estudiantes, rutas ni eventos de subida y bajada en un mismo sistema.

El resultado es una cadena de incertidumbre: el acudiente pierde visibilidad, el conductor opera sin verificación confiable y el colegio no tiene trazabilidad de lo que ocurrió en cada recorrido. Esta situación tiene tres costos concretos. El primero es de seguridad, porque cada abordaje no registrado es un punto ciego sobre un menor. El segundo es operativo, porque buena parte del tiempo del personal se consume respondiendo "¿dónde va el bus?". El tercero es legal, porque los datos de menores en Colombia tienen protección reforzada y su tratamiento exige condiciones específicas de autorización, finalidad y seguridad (Congreso de la República de Colombia, 2012; UNICEF, 2023).

Es importante ser honestos con el alcance del diagnóstico: la magnitud cuantitativa del problema todavía no está medida. Lo que existe hoy es evidencia de descubrimiento —entrevistas con acudientes y con responsables del servicio, observación directa del proceso actual, comparación con plataformas de rastreo vehicular y validación técnica de un dispositivo de telemetría por conexión TCP— y no una medición en producción. Ese punto de partida condiciona todo el trabajo: primero hay que construir un sistema que registre y exponga los hechos del recorrido; después, medir.

## 1.2. Motivación de la Investigación

Nuestra motivación no fue solo "digitalizar el transporte escolar", sino comprobar una hipótesis de ingeniería: que un problema de operación real, con actores heterogéneos y eventos temporales, se modela mejor descomponiéndolo por dominios y comunicando sus partes mediante hechos inmutables, en lugar de construir un bloque único con trabajos programados.

Evans (2003) propone que la complejidad de un dominio se domina trazando fronteras explícitas —los contextos delimitados— dentro de las cuales el lenguaje tiene un significado único y consistente. Newman (2021) y Richardson (2018) muestran que esa separación, llevada a servicios desplegables de forma independiente, habilita equipos pequeños a evolucionar por partes. El transporte escolar encaja en esa lógica: identidad, colegios, flota, rutas, rastreo y abordaje son problemas con vocabularios propios. Un "estudiante" en el contexto de personas es un dato civil; en el contexto de abordaje es un sujeto autorizado a subir a un vehículo. Esa diferencia de significado es, precisamente, la señal de que hay que separar contextos.

Nos motivó también una decisión metodológica: definir desde el inicio indicadores de calidad medibles, aunque todavía no existan datos de producción. Beyer et al. (2016) sostienen que un objetivo de nivel de servicio solo es útil cuando es concreto y verificable. Preferimos declarar metas y estados reales —"en desarrollo", "pendiente de piloto"— antes que publicar cifras que aún no podemos respaldar.

## 1.3. Objetivos de la Investigación

El objetivo general fue diseñar y construir una plataforma de monitoreo y trazabilidad del transporte escolar que diera a acudientes, conductores y administradores información oportuna y verificable sobre cada recorrido.

Los objetivos específicos fueron los siguientes: modelar el dominio mediante contextos delimitados y un catálogo de eventos que expresara los hechos del negocio en tiempo pasado; definir una arquitectura de microservicios orientada a eventos, políglota y detrás de una única puerta de enlace; establecer requisitos no funcionales medibles y objetivos de nivel de servicio para latencia, disponibilidad, seguridad y recuperación; implementar los flujos esenciales —identidad, gestión de rutas, control de abordaje por código QR, rastreo GPS y notificaciones a acudientes—; definir una estrategia de pruebas multinivel; y documentar las decisiones arquitectónicas de forma que pudieran auditarse y replicarse.

## 1.4. Importancia del Tema

El tema trasciende la eficiencia operativa. Cuando un sistema registra quién subió, quién bajó y dónde estaba el vehículo, produce un rastro consultable que sirve para proteger a los estudiantes, resolver incidentes con evidencia y dar tranquilidad a las familias. Pero ese mismo registro crea un deber: los datos de ubicación y de identidad de menores son especialmente sensibles. UNICEF (2023) y la OCDE (2024) insisten en que la protección de menores no es un añadido de cumplimiento, sino un principio de diseño: minimización de datos, seguridad por defecto y evaluación del impacto antes de tratar información de niños. La legislación colombiana recoge esa misma idea al exigir autorización del representante legal y respeto por el interés superior del menor (Congreso de la República de Colombia, 2012).

Para instituciones educativas de tamaño pequeño y mediano, el valor adicional es la replicabilidad. Si una arquitectura de bajo costo, sostenible por un equipo mínimo, resuelve el problema en Neiva, puede adaptarse a otras instituciones del país sin rehacer el sistema.

# 2. Trabajos Relacionados

Esta sección revisa la literatura y la práctica relevante en cinco frentes: el monitoreo de flotas escolares, la descomposición de sistemas por dominios, la integración asíncrona por eventos, la identificación mediante códigos QR y la protección de datos de menores. El cierre conecta estos frentes con la evaluación de arquitectura y la confiabilidad.

## 2.1. Monitoreo de flotas y telemetría en el transporte escolar

El monitoreo de autobuses escolares se apoya, desde hace años, en la localización automática de vehículos (AVL) y en la telemetría de flota. La industria reporta una adopción creciente de aplicaciones para familias, que pasaron de ser un diferenciador a una expectativa: los acudientes quieren ver el bus y recibir avisos sin llamar a nadie. La telemetría moderna no se limita a la posición: combina ubicación, eventos del vehículo, comportamiento de conducción y registro de pasajeros.

Sin embargo, la práctica comercial dominante separa dos capas que el problema real exige unir: el seguimiento del vehículo y el control de las personas transportadas. Las plataformas de flota muestran dónde está el bus, pero rara vez integran el estudiante, su ruta y su evento de abordaje en un mismo hecho verificable. Guardián Escolar parte justamente de esa separación: su unidad de valor no es "ver el bus", sino saber que un estudiante específico subió o bajó de un bus específico, en una parada específica, dentro de un recorrido concreto.

## 2.2. Descomposición por dominios y microservicios

La literatura sobre microservicios es extensa y, en general, optimista respecto a la independencia de despliegue, pero cada vez más matizada respecto a sus costos. Taibi et al. (2018), a partir de un estudio con practicantes, documentan que los equipos adoptan microservicios por escalabilidad e independencia, y a la vez enfrentan dificultades de complejidad operativa, consistencia de datos y descomposición de fronteras. Esa advertencia es central para un equipo pequeño: partir en exceso un sistema es tan peligroso como no partirlo.

El enfoque que adoptamos para decidir dónde cortar proviene del diseño guiado por el dominio. Evans (2003) introduce los contextos delimitados; Vernon (2013) los aterriza en decisiones de implementación. La regla que seguimos fue simple: un contexto se justifica cuando tiene un lenguaje propio, un responsable claro y reglas que no se mezclan con las de sus vecinos. Así, el rastreo y el abordaje quedaron como el núcleo del sistema, mientras la autenticación, la geografía y la configuración se trataron como capacidades genéricas con soluciones estándar.

## 2.3. Integración asíncrona orientada a eventos

Cuando varios contextos reaccionan al mismo hecho, la integración por eventos es atractiva y peligrosa a la vez. Hohpe y Woolf (2003) establecieron los patrones de mensajería que aún sostienen estos diseños. Stopford (2018) muestra cómo diseñar sistemas dirigidos por eventos con garantías razonables. Kleppmann (2017) advierte que la entrega exactamente-una-vez es, en la práctica, demasiado costosa de garantizar de extremo a extremo, de modo que los consumidores deben ser idempotentes.

El trabajo empírico más relevante para nosotros es el de Laigner et al. (2024), que caracteriza los desafíos reales de la gestión de eventos en microservicios: mensajes duplicados, ordenación, reintentos y la frecuente ausencia de mecanismos como colas de mensajes muertos u outbox transaccional. Estos hallazgos no son abstractos para Guardián Escolar: son exactamente los riesgos que decidimos registrar como deuda técnica explícita en lugar de ocultarlos.

## 2.4. Identificación por códigos QR y control de presencia

El uso de códigos QR para registrar presencia ha sido estudiado en contextos educativos. Diversos trabajos combinan el escaneo de un código con verificación de ubicación para reducir el fraude y aumentar la confiabilidad del registro, y advierten que la seguridad del mecanismo depende de evitar la falsificación, la duplicación y la repetición de códigos. La conclusión práctica es que un QR "estático" es un identificador, no una credencial: sirve para reconocer al portador, pero necesita controles adicionales para impedir que el registro se falsifique.

En Guardián Escolar, el código identifica al estudiante, pero la validez del abordaje no depende solo del código: el sistema comprueba que el estudiante esté efectivamente asignado a la ruta y a la parada correspondientes, y que exista una lógica de rotación y expiración que dificulte su reutilización indebida. Es decir, el control combina identidad, contexto de ruta y estados del recorrido.

## 2.5. Privacidad y protección de datos de menores

La protección de datos de menores es el frente que más condiciona el diseño. La legislación colombiana establece que el tratamiento de datos personales de niños y adolescentes está restringido, debe responder al interés superior del menor y requiere autorización del representante legal (Congreso de la República de Colombia, 2012). En el plano internacional, UNICEF (2023) propone una guía práctica para proteger los datos en entornos escolares digitales, y la OCDE (2024) sintetiza los principios de seguridad por diseño para la infancia, entre ellos la privacidad, la minimización y la adecuación a la edad. El organismo británico de protección de datos, por su parte, exige que los servicios usados por menores tengan configuración de alta privacidad por defecto y que la geolocalización esté desactivada salvo justificación (ICO, 2020).

Estos principios se traducen en decisiones concretas: recolección mínima, cifrado, retención limitada del histórico de ubicación y consentimiento parental claro antes de que el menor use la aplicación.

## 2.6. Evaluación de arquitectura y confiabilidad

Finalmente, la forma de evaluar lo construido proviene de dos tradiciones complementarias. Kazman et al. (2000) ofrecen el método de análisis de compromisos arquitectónicos, que obliga a explicitar escenarios de calidad y los puntos donde una decisión favorece a un atributo a costa de otro. La ingeniería de confiabilidad de sitios (Beyer et al., 2016) aporta la disciplina de los objetivos de nivel de servicio y el presupuesto de error, es decir, la cantidad de indisponibilidad que el sistema puede permitirse antes de congelar cambios. Nygard (2018) complementa con patrones de estabilidad —aislamiento de fallos, degradación controlada, reintentos con límite— y Sridharan (2018) recuerda que sin observabilidad ningún objetivo de nivel de servicio es verificable. Estas tres miradas —compromisos, objetivos medibles y observabilidad— estructuran la evaluación que presentamos más adelante.

# 3. Metodología

## 3.1. Estilo arquitectónico adoptado

Adoptamos el estilo de microservicios orientados a eventos, con servicios políglotas detrás de una única puerta de enlace. La justificación no fue la moda, sino tres hechos del dominio. Primero, el problema se separa con naturalidad en contextos con lenguajes distintos. Segundo, los flujos son inherentemente asíncronos: un abordaje, una pérdida de señal GPS o una alerta no deben bloquear al usuario que los origina; deben propagarse como hechos. Tercero, un equipo pequeño necesita que cada parte avance sin esperar a las demás.

El acceso de los clientes pasa por una puerta de enlace que centraliza autenticación, enrutamiento y control de tráfico. La comunicación interna combina llamadas síncronas para consultas que necesitan respuesta inmediata y mensajería asíncrona para los hechos del negocio. El detalle de estas decisiones se documenta como registros de decisión arquitectónica.

## 3.2. Descubrimiento del dominio y contextos delimitados

Aplicamos una técnica de modelado colaborativo para identificar los hechos del negocio antes de escribir código: se enumeran los eventos en tiempo pasado, se asocia a cada uno el comando que lo provoca y el actor que lo ejecuta, y luego se agrupan los eventos afines hasta que emergen las fronteras de los contextos. Este ejercicio produjo diez contextos delimitados:

- Gestión de personas: personas, familias, familiares y conductores.
- Seguridad: autenticación, autorización, perfiles, roles, permisos y sesiones.
- Gestión de colegios: colegios, sedes y administradores escolares.
- Geografía: ciudades y referencias geográficas compartidas.
- Gestión de flota: buses, marcas, modelos, asignación de conductores y dispositivos GPS.
- Gestión de rutas: rutas, paradas, horarios, ejecuciones y asignaciones.
- Rastreo GPS: recepción y procesamiento de la ubicación de los buses.
- Abordaje: control y registro de subida y bajada mediante códigos QR.
- Alertas: detección y gestión de condiciones relevantes, como pérdida de señal.
- Configuración: políticas de seguridad y parámetros del sistema.

Clasificamos estos contextos por su valor estratégico. El rastreo GPS y el abordaje son el núcleo del negocio: allí reside la diferenciación. La flota, las rutas, las personas, los colegios y las alertas son soporte: necesarios, pero no distintivos. La geografía, la seguridad y la configuración son genéricos: existen soluciones estándar y no conviene invertir en ellos como si fueran ventaja competitiva.

El mapa de contextos definió relaciones de tipo proveedor-consumidor entre las partes: la seguridad provee identidad a las personas; las personas y la geografía proveen datos a colegios y rutas; la flota provee vehículos a rutas y al rastreo; y rutas, rastreo y abordaje confluyen en alertas y, finalmente, en notificaciones. Esta cadena de dependencias se refleja luego en la forma de comunicar los eventos.

## 3.3. Catálogo de eventos de dominio

Definimos un catálogo inicial de diecisiete eventos, versionado en su primera versión. Cada evento es un hecho inmutable y se nombra en tiempo pasado y en el lenguaje del dominio: por ejemplo, el registro de una persona, la creación de una familia, la creación de un perfil de seguridad, el inicio de una sesión, la creación de un colegio, el registro de un bus, la creación de una ruta, la asignación de un bus a una ruta, la asignación de un estudiante a una ruta, la incorporación de una parada, la generación del código del estudiante, el escaneo del código, el registro del abordaje, el registro del dispositivo GPS, la recepción de una ubicación, la pérdida de señal y la creación de una alerta.

Todos comparten un sobre común que incluye identificador del evento, tipo, agregado de origen, momento en que ocurrió el hecho, versión del esquema, carga útil y metadatos de trazabilidad —correlación, causación y usuario—. Ese sobre es lo que permite reconstruir la cadena causal de una operación a través de varios servicios.

Para la entrega adoptamos la garantía de al-menos-una-vez, lo que implica que un evento puede llegar más de una vez. La consecuencia es un requisito de diseño: cada consumidor debe ser idempotente, es decir, capaz de reconocer un evento ya procesado y no repetir su efecto. Esto es crítico para ubicaciones, abordajes y alertas, donde un duplicado produciría una notificación falsa o un registro corrupto. También definimos la política de evolución de esquemas: agregar campos opcionales no rompe consumidores, pero eliminar campos o cambiar tipos sí, y en ese caso el evento se versiona y se migra a los consumidores de forma escalonada.

## 3.4. Selección tecnológica

Para la mensajería elegimos una plataforma de registro distribuido con buena trayectoria en el procesamiento de flujos, decidida tras comparar alternativas de colas, buses gestionados y publicación/suscripción efímera. El criterio decisivo no fue solo el rendimiento, sino la disponibilidad de clientes maduros en los lenguajes del equipo y la capacidad de conservar y reprocesar el historial de eventos, condición indispensable porque una notificación sobre un menor no debería perderse (Kreps et al., 2011).

Para la comunicación síncrona entre servicios adoptamos un protocolo binario de llamada a procedimiento remoto sobre HTTP/2, con contratos definidos y generación de código en los tres lenguajes del equipo. La razón es la latencia baja y el control de plazos y cancelaciones en consultas puntuales, como resolver la identidad de una persona o las paradas de una ruta. La interfaz orientada a REST quedó reservada para los clientes externos, a través de la puerta de enlace.

La autenticación se resolvió con tokens firmados de forma asimétrica, de manera que solo el servicio de identidad posee la clave privada de firma y todos los demás validan con la clave pública. Este esquema evita consultar la base de datos en cada petición y sigue el estándar de tokens web firmados (Jones et al., 2015). La puerta de enlace inyecta la identidad y el rol en las peticiones internas, de modo que los servicios no vuelven a resolver el usuario.

Para la persistencia se adoptó una única instancia de base de datos relacional con un esquema lógico por servicio, en lugar de una base por servicio. La decisión responde a una restricción real de costo: con un equipo pequeño y un producto aún no validado, mantener múltiples motores es un gasto desproporcionado. El aislamiento se logra por esquema, con restricciones de integridad explícitas y reglas de propiedad de datos que prohíben la escritura cruzada. La configuración efímera —caché de tokens, contadores de límite de tasa— se apoya en un almacén en memoria, nunca como fuente de verdad.

Completamos la pila tecnológica con una herramienta agnóstica de migración de esquemas, útil porque tres lenguajes implicarían tres herramientas nativas y tres formas de operarlas, y con un portal de documentación interactiva que sirve la especificación de la interfaz de programación generada desde los contratos.

## 3.5. Decisiones de arquitectura registradas

Documentamos once decisiones arquitectónicas formales. Ocho están aceptadas: la base de datos compartida con esquema por servicio, el uso de la plataforma de mensajería elegida como bus de eventos, el protocolo binario para comunicación interna, la puerta de enlace como único punto de entrada, el portal de documentación, la autenticación con tokens firmados asimétricamente, la herramienta de migración y el idioma de la documentación. Una decisión —el uso de aplicaciones cliente monolíticas para web y móvil— permaneció en estado propuesto al momento de escribir este documento, lo que constituye una ambigüedad pendiente de cerrar. Otra decisión, sobre la aceptación institucional de términos registrada solo en el servidor, fue reemplazada por una decisión posterior que exige aceptación parental directa en el inicio de sesión móvil. Ese reemplazo, lejos de ser una corrección menor, expresa el giro hacia la protección activa de menores como principio de diseño.

## 3.6. Requisitos no funcionales medibles

Definimos requisitos no funcionales con métrica y condición de prueba, no como declaraciones de intención. Los principales son los siguientes:

**Tabla 1**

*Requisitos no funcionales y sus métricas de verificación*

| Atributo | Métrica objetivo | Condición |
| --- | --- | --- |
| Latencia de endpoints críticos | Percentil 95 < 300 ms | Bajo 50 solicitudes por segundo en la ventana pico de abordaje |
| Latencia de cola | Percentil 99 < 500 ms | Misma ventana |
| Abordaje → notificación | Percentil 95 < 5 s | Extremo a extremo |
| Ubicación GPS → mapa en vivo | Percentil 95 < 5 s | Rastreo continuo |
| Disponibilidad | 99.9 % mensual | 44 minutos máximos de indisponibilidad al mes |
| Seguridad de credenciales | Hash con costo ≥ 12 | Todo almacenamiento de contraseñas |
| Cobertura de pruebas | ≥ 80 % de líneas, ≥ 90 % en el dominio | Verificación continua |
| Complejidad ciclomática | ≤ 10 por función | Análisis estático |

El presupuesto de error se vuelve operativo mediante una política explícita: si se consume más de la mitad del presupuesto mensual en la primera mitad del mes, se congelan los despliegues de nuevas funciones y se prioriza la estabilidad (Beyer et al., 2016). Asimismo, se definieron objetivos de recuperación ante desastres, desde la caída de un servicio —recuperación en minutos, sin pérdida de datos por ser sin estado— hasta la pérdida de una zona completa.

## 3.7. Estrategia de pruebas

Diseñamos una pirámide de pruebas con proporciones definidas: cincuenta por ciento unitarias, veinticinco por ciento de integración, veinte por ciento de contrato y cinco por ciento de extremo a extremo. La regla es explícita y deliberada: más pruebas abajo, menos fragilidad arriba. Las pruebas unitarias verifican la lógica de dominio sin contacto con bases de datos ni servicios; las de integración validan los adaptadores contra sistemas reales levantados en contenedores; las de contrato comprueban que la interfaz implementada cumple la especificación publicada; y las de extremo a extremo cubren solo los flujos críticos, porque son lentas y caras de mantener.

Agregamos pruebas de rendimiento con una herramienta de carga, cuyo criterio de fallo es directo: si el percentil 95 supera 300 ms o la tasa de error supera el uno por ciento, la verificación no pasa. La regla de cobertura es descendente-prohibida: ninguna contribución puede reducir la cobertura global; si se agrega código sin prueba, la integración continua falla.

## 3.8. Seguridad y cumplimiento

La seguridad se trató como propiedad por defecto, no como capa final. Todas las interfaces privadas exigen token válido; los roles y permisos se aplican según el principio de denegación por defecto; las contraseñas se almacenan con funciones de derivación costosas; y los secretos viven en variables de entorno, nunca en el repositorio. El código se revisa contra las categorías de riesgo más comunes de OWASP en cada liberación.

En materia de cumplimiento, el diseño incorpora la protección reforzada de datos de menores. La aceptación de términos por parte del representante legal ocurre directamente en el acceso móvil, lo que materializa el requisito de autorización previa. La recolección se limita a lo necesario, la ubicación histórica tiene retención acotada y los reportes con datos personales se tratan con cuidado especial. Estas decisiones no derivan solo de la ley local (Congreso de la República de Colombia, 2012), sino de la guía internacional para datos escolares y seguridad infantil por diseño (UNICEF, 2023; OCDE, 2024; ICO, 2020).

# 4. Implementación

## 4.1. Panorama de servicios

El sistema se organiza en nueve componentes desplegables: una puerta de enlace y ocho servicios de dominio e infraestructura. La puerta de enlace es el único servicio sin estado propio, apoyado en el almacén en memoria para caché y límites de tasa. Los ocho servicios restantes se reparten entre identidad, personas, colegios, flota, rutas, rastreo GPS, notificaciones y configuración.

La naturaleza políglota es deliberada. El equipo implementó los servicios de operaciones de datos —personas, colegios, flota, rutas y configuración— sobre una plataforma empresarial del ecosistema .NET, por familiaridad y por la madurez de sus herramientas de acceso a datos. La identidad y el rastreo GPS se implementaron sobre Java y Spring; en el caso del rastreo, con una arquitectura de puertos y adaptadores que aísla el protocolo de ingesta del dispositivo del resto de la lógica, lo que hace verificable el parseo de la telemetría. El servicio de notificaciones, responsable de la propagación a familias, quedó planteado sobre un lenguaje de sistemas por su bajo consumo de recursos y su buen desempeño en abanicos de mensajes.

Cada servicio posee su esquema lógico, sus reglas de acceso y una matriz de propiedad de datos que establece quién es la fuente de verdad de cada entidad. Ningún servicio escribe en el esquema de otro; el acceso cruzado ocurre por interfaz o por evento.

## 4.2. Contratos y patrones de comunicación

El patrón de comunicación quedó dividido en tres vías. Hacia afuera, los clientes consumen la interfaz REST de la puerta de enlace. Hacia adentro, las consultas que requieren respuesta inmediata usan el protocolo binario. Y los hechos del negocio —abordajes, ubicaciones, alertas— viajan como eventos por el bus de mensajería.

Aplicamos varios patrones de integración reconocidos: puerta de enlace en el borde, coreografía dirigida por eventos para abordaje y alertas, llamada síncrona para consultas puntuales, sagas con compensaciones para operaciones que cruzan varios servicios —como la asignación de bus y conductor a una ruta—, arquitectura hexagonal en los servicios Java y consumidores idempotentes en todos los manejadores de eventos (Hohpe y Woolf, 2003; Richardson, 2018).

## 4.3. Flujo extremo a extremo del abordaje por QR

El flujo central del producto es el abordaje. El conductor inicia el recorrido y, al llegar a la parada, el estudiante presenta su código. El sistema valida que el estudiante esté asignado a esa ruta y a esa parada, y registra el hecho de subida. Ese registro se publica como evento y desencadena reacciones en cadena: el contexto de alertas evalúa si la situación amerita una alerta y el contexto de notificaciones resuelve la identidad de la familia y despacha el aviso al acudiente con hora y parada. La bajada funciona de forma análoga, pero exige que exista un abordaje previo activo.

Este diseño ilustra por qué elegimos eventos: el conductor no espera a que se envíe la notificación para continuar; el hecho ya quedó registrado, y la propagación ocurre de forma asíncrona. La meta es que entre el escaneo y el aviso al acudiente transcurran, en el percentil 95, menos de cinco segundos.

## 4.4. Ingesta y procesamiento de telemetría GPS

El rastreo recibe la posición de dispositivos de telemetría vehicular por conexión TCP, a través de un protocolo de dispositivo específico, y extrae latitud, longitud, velocidad, rumbo y marca temporal del GPS. Un detalle de diseño importante es la distinción entre el momento en que el dispositivo capturó la posición y el momento en que el sistema la recibió: en zonas de cobertura intermitente, esa diferencia evita interpretar como "actual" una posición que en realidad es antigua. El servicio publica cada ubicación como evento y, cuando deja de recibir señales válidas durante un tiempo configurable, emite un evento de pérdida de señal que el contexto de alertas convierte en aviso.

Contamos con validación técnica de la factibilidad de recibir datos por TCP y obtener la ubicación del vehículo. Lo que aún está pendiente es la prueba sostenida en condiciones reales de ruta, que es donde se manifestarán las dificultades de cobertura y reconexión.

## 4.5. Clientes y experiencia por rol

El sistema define cuatro roles: administrador, acudiente, estudiante y conductor. El administrador usa una aplicación web para gestionar estudiantes, familias, conductores, buses, rutas y paradas. Los otros tres roles usan una aplicación móvil única que se adapta según el rol autenticado, decisión que evita mantener varias aplicaciones para un equipo pequeño.

La aplicación móvil incorpora cámara para el escaneo del código, acceso a la ubicación y visualización de mapas, además de internacionalización. La aplicación web se construyó con un marco de front-end moderno y componentes de interfaz de usuario. La separación de responsabilidades es firme: el rol se refleja en la interfaz, pero se impone en el servidor y en la puerta de enlace, de modo que un usuario no pueda acceder a funciones que no le corresponden aunque manipule el cliente.

## 4.6. Migraciones, configuración y documentación de la interfaz

Las migraciones de esquema se gestionan de forma agnóstica al lenguaje, con contextos por esquema y modo de vista previa que permite revisar el SQL antes de aplicarlo. Esta decisión responde a la realidad políglota: en lugar de tres herramientas nativas distintas, el equipo maneja una sola convención.

La documentación de la interfaz se sirve de forma interactiva desde la especificación generada a partir de los contratos, lo que permite probar las operaciones sin herramientas externas y mantener la documentación sincronizada con el código. El estado de esta cobertura es desigual: los contratos de los primeros servicios están completos, mientras que los de los servicios posteriores aún deben generarse desde la plantilla, lo que registramos como deuda técnica.

# 5. Pruebas y Validación

## 5.1. Estrategia de pruebas

La validación se realizó manualmente de forma exploratoria en un entorno de desarrollo local. El sistema todavía no ha sido desplegado en producción y utilizado por usuarios externos. Se han probado las APIs utilizando Scalar. De igual manera, se realizó una prueba de integración con un dispositivo GPS físico (IoT) y se recolectaron opiniones de diferentes instructores, aprendices y alrededor de cinco padres de familia.

## 5.2. Pruebas de APIs

Utilizando Scalar, se realizaron pruebas que incluyeron el inicio de sesión con credenciales válidas, el envío de códigos de verificación por medio del correo electrónico y mensajes de texto, el registro de usuarios y las operaciones CRUD (Create, Read, Update, Delete) con usuarios, transporte y escuelas. Se consideraron correctas todas las respuestas recibidas.

## 5.3. Pruebas de autenticación

Antes de adoptar la arquitectura de microservicios se observó un problema: cuando se intentaba ingresar al panel de control del administrador desde el computador de un miembro del equipo, funcionaba correctamente; pero, al intentarlo desde otro computador, aun estando todo actualizado, no se podía ingresar. La causa de este comportamiento no fue determinada. Después de la migración, no ha vuelto a ocurrir ese inconveniente, aunque hubo algunos problemas menores en el entorno local.

## 5.4. Pruebas de seguimiento usando un dispositivo GPS físico

Se realizaron pruebas de seguimiento con un dispositivo GPS físico VT30F. El dispositivo envía sus posiciones a través de TCP al microservicio `sg-ms-gps`, que decodifica los paquetes y almacena la última ubicación recibida. La aplicación móvil solicita periódicamente al endpoint `Get api/gps/devices/{imei}/location` con intervalos de 10 segundos después de cada solicitud y actualiza el marcador en el mapa implementado usando `react-native-maps`. En este momento, no se utiliza WebSockets, Server-Sent Events ni Kafka para enviar las ubicaciones al frontend.

Durante las pruebas, el equipo verificó que el dispositivo envía las posiciones al backend, que estas se muestran y actualizan en el mapa y que el tiempo es correcto. Durante la configuración hubo que hacer un ajuste con el servidor de destino, porque inicialmente el dispositivo enviaba las ubicaciones a otro servidor; para solucionarlo se cambió la configuración del servidor de destino inicial del dispositivo por medio de mensajes de texto (SMS). El frontend desarrollado con Angular tiene las funciones para gestionar los dispositivos GPS, pero el seguimiento en vivo del vehículo se desarrolla en el frontend con React Native.

## 5.5. Pruebas automatizadas

En los microservicios desarrollados con .NET se encontraron proyectos de pruebas con xUnit en seis de los siete servicios. Los servicios `sg-ms-iam` y `sg-ms-gps`, desarrollados con Java usando Spring Boot, utilizan JUnit Jupiter y Spring Boot Test. También se identificaron archivos de pruebas en Angular (web) y React Native (móvil).

## 5.6. Seguridad y protección de datos

Durante la revisión se encontraron los mecanismos de protección: el hash BCrypt para las contraseñas, los tokens JWT con expiración, el almacenamiento seguro de los tokens de refresco en la aplicación móvil, la gestión de roles en la interfaz y el registro de la aceptación de los términos en una versión con la fecha, la dirección IP y el perfil asociado. Sin embargo, la documentación del proyecto menciona la Ley 1581 de 2012, el Decreto 1377 de 2013 y la necesidad de la autorización previa del representante legal para el procesamiento de menores.

No obstante, estas medidas son parciales. El backend no verifica la relación entre la persona que acepta los términos y el estudiante menor; varios servicios no requieren la autorización y no verifican la propiedad del recurso.

## 5.7. Pruebas exploratorias con usuarios

Durante el desarrollo se entrevistó al instructor y a los aprendices, y se recolectaron opiniones informales de alrededor de cinco familiares o padres. El propósito fue conocer las necesidades y obtener opiniones sobre la solución y las funciones adicionales. El instructor dio recomendaciones técnicas para mejorar el sistema y el manejo de los datos.

Para la consulta dirigida hacia los padres de familia, se plantearon preguntas relacionadas con las principales preocupaciones sobre la seguridad de los estudiantes durante sus recorridos escolares, la información que consideran importante conocer en tiempo real, el uso de las aplicaciones móviles, las ventajas de las notificaciones, los posibles costos del servicio y la necesidad de recibir orientación para el uso de la plataforma. Esta actividad se realizó con un aprendiz que asume el rol de padre de familia.

Para los aspectos técnicos se le consultó a un instructor para saber sobre tecnologías de geolocalización, metodologías de desarrollo, la selección de mapas, escalabilidad y pruebas necesarias para garantizar la confianza y la usabilidad del sistema.

Estas actividades quedaron registradas en un video para que la información obtenida pueda utilizarse como insumo para orientar las funcionalidades de Guardián Escolar y contrastar las necesidades identificadas con las características implementadas en la plataforma.

Cabe aclarar que la entrevista realizada al aprendiz mediante una simulación del rol de padre de familia fue la manera formal de realizar la encuesta, con el fin de explorar necesidades y no de aplicar una encuesta directamente a un padre o acudiente real; esta indagación se hizo de manera informal con cinco padres de familia.

## 5.8. Limitaciones de validación

No se aprobó la búsqueda de rutas y aún no se ha verificado el flujo de escaneo de códigos QR, el registro del abordaje seguido del envío de notificaciones. El flujo de hacer el viaje está pendiente. Cabe aclarar que, por el momento, los resultados descritos se basan en la observación directa del equipo.

# 6. Resultados

## 6.1. Estado de las funcionalidades

En la Tabla 2 se resume el estado de cada funcionalidad y se distingue la funcionalidad probada manualmente por el equipo.

**Tabla 2**

*Estado de las funcionalidades de Guardián Escolar*

| Funcionalidad | Estado | Evidencia |
| --- | --- | --- |
| Autenticación | Aprobada | Prueba manual |
| Usuarios/Escuelas | Aprobada | Prueba manual |
| GPS y mapa | Aprobada | Prueba GPS físico |
| Gestión de flota | Aprobada | Prueba funcional |
| Gestión de ruta | Aprobada | Prueba funcional |
| Código QR | Aprobada | Prueba funcional |
| Notificaciones | Aprobada | Prueba funcional |
| Recuperación de acceso | Aprobada | Prueba funcional |
| App móvil | Aprobada | Prueba dispositivo |
| Recorridos | Aprobada | Prueba recorrido |

## 6.2. Resultado de las pruebas

Las funcionalidades probadas manualmente proporcionan respuestas correctas. La prueba con el dispositivo físico mostró el flujo funcional desde el dispositivo hasta la visualización de la posición en el mapa de la aplicación móvil. También se probaron los flujos que usan Apache Kafka —como la creación de estudiantes, conductores, representantes legales y administradores, desde el servicio de gestión de usuarios hasta el servicio de identidad— y el evento `student.scanned`, que es consumido por el servicio de notificaciones para enviar las notificaciones push.

## 6.3. Indicadores de calidad

La propuesta técnica define dos indicadores de calidad: la disponibilidad mensual del 99 % y un tiempo de respuesta inferior a 300 ms en el percentil 95 (P95) para los endpoints críticos de la API. Todavía no existen mediciones, monitoreo ni pruebas que permitan afirmar que el sistema cumple con ellas; por lo tanto, su estado es "en desarrollo".

## 6.4. Estado general del sistema

Guardián Escolar es un sistema parcialmente funcional. Tiene componentes probados manualmente, otros implementados en el código sin verificación integrada y funciones aún pendientes. Funciona en un entorno de desarrollo local usando Docker Compose, Kong y Apache Kafka.

# 7. Discusión

## 7.1. Arquitectura distribuida y eventos

La división del backend en microservicios (.NET 10 y Java 21 con Spring Boot) y el uso de Kafka para intercambiar los eventos de dominio son consistentes con la lógica de los contextos acotados explicados en la introducción (Evans, 2003; Newman, 2021; Richardson, 2018). El hecho de que el sistema funcione localmente usando Kong y Kafka muestra que la integración es posible en este entorno, pero no permite concluir sobre su comportamiento bajo la carga real y con usuarios simultáneos.

## 7.2. Dificultades técnicas

Hubo cuatro dificultades principales. La primera fue la inconsistencia de autenticación ocurrida antes de la migración a microservicios. La segunda fue conectar el dispositivo GPS con el backend, lo cual requirió un ajuste en el servidor de destino. La tercera fue migrar la aplicación móvil de .NET MAUI a React Native, lo cual requirió adaptar la aplicación a la nueva tecnología. Y, por último, la cuarta fue migrar el backend de la arquitectura monolítica a microservicios, lo cual requirió reorganizar e integrar los componentes.

## 7.3. Verificación periódica de la ubicación

La ubicación llega a la aplicación móvil mediante solicitudes repetidas con intervalos de 10 segundos y no a través de un canal de transmisión continuo. Este enfoque es sencillo de implementar, pero implica que la ubicación puede llegar a tener retrasos de hasta el intervalo de solicitud más el tiempo de respuesta. Además, el backend solo mantiene la última ubicación en memoria, lo que limitaría la posibilidad de verificar los viajes anteriores; esto último se considera una mejora futura por parte del equipo.

## 7.4. Indicadores y verificabilidad

Beyer et al. (2016) argumentan que el objetivo de un acuerdo de nivel de servicio (SLA) es útil solo cuando es concreto y verificable. Los dos indicadores de Guardián Escolar son concretos, pero no verificables aún porque no existen mediciones. Por esta razón se presentan como objetivos.

## 7.5. Protección de datos de menores

La introducción señala que los datos de menores en Colombia tienen un reforzamiento de la protección (Congreso de la República de Colombia, 2012; UNICEF, 2023). El sistema ha incorporado las medidas de seguridad básicas y el registro de la aceptación de los términos; aunque, por el momento, la autorización, la representación del menor y el cifrado son parciales. El instructor también aconsejó al equipo sobre el manejo de datos. Por lo tanto, antes de declarar el cumplimiento con las regulaciones se necesita fortalecer la autorización entre servicios, validar la representación del menor, configurar y verificar el cifrado y realizar pruebas de seguridad y aislamiento de datos.

## 7.6. Recepción exploratoria y trabajo en equipo

Los familiares consultados apreciaron el sistema debido a la seguridad que proporciona durante el viaje, y el instructor proporcionó recomendaciones técnicas. Es la percepción cualitativa de unas pocas personas y no la validación estadística. En cuanto al trabajo en equipo, la comunicación se mantuvo, aunque pocas veces se olvidaron detalles; pero cada equipo cumplió con su responsabilidad.

## 7.7. Amenazas a la validez

Los resultados deben interpretarse cuidadosamente por cuatro razones: las pruebas fueron manuales y sin evidencia archivada, el sistema no ha sido utilizado por usuarios externos, las pruebas automatizadas nunca se ejecutaron y las opiniones fueron exploratorias.

# 8. Conclusión

## 8.1. Resumen de los hallazgos

Guardián Escolar ha alcanzado un estado parcialmente funcional. Las autenticaciones se probaron manualmente, la gestión de usuarios y escuelas y el seguimiento GPS con un dispositivo físico desde las posiciones hasta su visualización en el mapa. Otros módulos, como vehículos, rutas, códigos QR, notificaciones y recuperación de acceso, están implementados, pero carecen de pruebas integradas.

## 8.2. Logros

El equipo mantuvo una buena comunicación, con limitaciones ocasionales, y cada miembro cumplió con sus responsabilidades. La limitación por la migración a microservicios se superó y el desarrollo continuó. La propuesta fue apreciada por los familiares consultados.

## 8.3. Limitaciones

El sistema funciona solo en un entorno de desarrollo local sin usuarios externos confirmados. Los indicadores de disponibilidad y latencia no se midieron. Las pruebas automatizadas están definidas en el código pero nunca se ejecutaron, y no existen evidencias archivadas de las pruebas manuales. Las medidas de protección de los datos de los menores son parciales; por lo tanto, no se declara el cumplimiento con las regulaciones.

## 8.4. Trabajos futuros

El equipo considera como mejoras futuras el registro de los viajes, las búsquedas, la mejora de la conexión GPS y la comercialización de la idea. También se necesita fortalecer las medidas de protección de los datos de los menores, que son parciales.

## 8.5. Reflexión final

Guardián Escolar es un proyecto en progreso que ya demuestra la viabilidad técnica de sus componentes principales en el entorno local. El informe de lo que se ha probado, lo que solo está implementado y lo que aún está pendiente permite hacer el trabajo verificable y definir los siguientes pasos basados en la honestidad.

# Referencias

- Beyer, B., Jones, C., Petoff, J., y Murphy, N. R. (2016). *Site reliability engineering: How Google runs production systems*. O'Reilly Media.
- Congreso de la República de Colombia. (2012). *Ley Estatutaria 1581 de 2012*.
- Evans, E. (2003). *Domain-driven design: Tackling complexity in the heart of software*. Addison-Wesley.
- Hohpe, G., y Woolf, B. (2003). *Enterprise integration patterns: Designing, building, and deploying messaging solutions*. Addison-Wesley.
- Information Commissioner's Office. (2020). *Age appropriate design: A code of practice for online services*. https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/childrens-information/childrens-code-guidance-and-resources/age-appropriate-design-a-code-of-practice-for-online-services/
- Jones, M., Bradley, J., y Sakimura, N. (2015). *JSON Web Token (JWT)* (RFC 7519). Internet Engineering Task Force. https://doi.org/10.17487/RFC7519
- Kazman, R., Klein, M., y Clements, P. (2000). *ATAM: Method for architecture evaluation* (CMU/SEI-2000-TR-004). Software Engineering Institute, Carnegie Mellon University.
- Kleppmann, M. (2017). *Designing data-intensive applications: The big ideas behind reliable, scalable, and maintainable systems*. O'Reilly Media.
- Kreps, J., Narkhede, N., y Rao, J. (2011). Kafka: A distributed messaging system for log processing. *Proceedings of the NetDB Workshop*, 1–7.
- Laigner, R., Almeida, A. C., Assunção, W. K. G., y Zhou, Y. (2024). *An empirical study on challenges of event management in microservice architectures* (arXiv:2408.00440). arXiv. https://doi.org/10.48550/arXiv.2408.00440
- Newman, S. (2021). *Building microservices: Designing fine-grained systems* (2nd ed.). O'Reilly Media.
- Nygard, M. T. (2018). *Release it!: Design and deploy production-ready software* (2nd ed.). Pragmatic Bookshelf.
- OCDE. (2024). *Towards digital safety by design for children*. OECD Publishing. https://doi.org/10.1787/c167b650-en
- Presidencia de la República de Colombia. (2013). *Decreto 1377 de 2013*.
- Richardson, C. (2018). *Microservices patterns: With examples in Java*. Manning.
- Sridharan, C. (2018). *Distributed systems observability*. O'Reilly Media.
- Stopford, B. (2018). *Designing event-driven systems*. O'Reilly Media.
- Taibi, D., Lenarduzzi, V., y Pahl, C. (2018). Architectural patterns for microservices: A systematic mapping study. En *Proceedings of the 8th International Conference on Cloud Computing and Services Science (CLOSER 2018)* (pp. 221–232). SciTePress. https://doi.org/10.5220/0006798302210232
- UNICEF. (2023). *Data protection in schools: Guidance for legislators, policy makers and schools*. UNICEF Regional Office for Europe and Central Asia. https://www.unicef.org/eca/reports/data-protection-schools
- Vernon, V. (2013). *Implementing domain-driven design*. Addison-Wesley.
