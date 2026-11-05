# 5.5 Interfaces gráficas

**Proyecto:** Monitor de Consumo Eléctrico — EnergyMonitor
**Ficha:** 3145556 — Análisis y Desarrollo de Software (ADSO), SENA CIES Regional Huila
**Versión:** 1.0

---

## 1. Propósito y relación con el punto 03

Esta sección documenta el **diseño de la interfaz de usuario**: el sistema de diseño que la sostiene, la paleta, la tipografía, la estructura de pantallas, los componentes reutilizables, los patrones de interacción y las decisiones de accesibilidad.

> **Los mockups de este proyecto están en el punto 03 — Análisis de software**, no aquí.
> Este documento **no** los sustituye: los describe, los clasifica y los relaciona con el código que los implementa. Cuando este documento y un mockup parezcan contradecirse, **prevalece el mockup**, porque es la fuente de verdad del diseño con la que se validó la implementación.

| **Artefacto** | **Ubicación** | **Qué aporta** |
|---|---|---|
| **Mockups** en HTML (web y móvil) | `03-analisis-software/mockup/layout/{web,mobile}/` | Aspecto visual aprobado de cada pantalla |
| **Mapas de navegación** web y móvil | `03-analisis-software/ES/navegacion/` | Flujo real de ejecución, tomado del código y no de los mockups |
| **Requisitos e historias de usuario** | `02-especificacion-requisitos/` | Requisitos funcionales que dan origen a las pantallas |

## 2. Principios de diseño de la interfaz

| **#** | **Principio** | **Aplicación concreta** |
|---|---|---|
| 1 | **Claridad ante complejidad** | El consumo se entiende con tres indicadores de alto nivel (potencia actual, consumo de hoy, dispositivos activos) y el detalle queda un nivel más abajo |
| 2 | **Información progresiva** | El Dashboard muestra tarjetas de hogar; el detalle se abre en seis pestañas, nunca todas a la vez |
| 3 | **Una pantalla, una tarea** | Las acciones destructivas (eliminar hogar, salir del hogar, desvincular dispositivo) se aislan en un diálogo de confirmación |
| 4 | **Realimentación inmediata** | Todo cambio en pantalla se confirma visualmente: insignia en el menú, título de la pestaña, aviso emergente y actualización del contador |
| 5 | **Consistencia entre plataformas** | Web y móvil comparten paleta, tipografía, espaciado y comportamiento; un usuario que usa ambos reconoce la aplicación |
| 6 | **Internacionalización real** | Cuatro idiomas en toda la interfaz, incluidos mensajes de error, nombres de días y formatos de número |
| 7 | **Estados explícitos** | Toda vista de datos declara sus cuatro estados: cargando, con error, vacía y con contenido |

## 3. Sistema de diseño

### 3.1 Decisión de arquitectura

**Ambos front-ends implementan un sistema de diseño propio. No se utiliza ninguna biblioteca de componentes de terceros.**

| **Tecnología** | **¿Se usa?** | **Comentario** |
|---|---|---|
| **CSS Modules** (web) | **Sí** | Tecnología principal de estilos, 40+ archivos |
| **Variables CSS** (web) | **Sí** | Tokens de diseño en `:root` |
| **`StyleSheet` + tokens** (móvil) | **Sí** | Replicación del sistema web |
| Tailwind CSS | No | Sin configuración, sin dependencia |
| shadcn/ui · Radix · Headless UI · MUI · Chakra | No | Sin dependencia |
| Iconos | `lucide-react` (web) · `Ionicons` y `MaterialCommunityIcons` (móvil) | Única librería visual de terceros |

**Consecuencia de la decisión.** El proyecto no hereda decisiones de diseño ajenas: cada control refleja una necesidad concreta de esta aplicación. El costo es mantener el sistema: cada componente propio necesita pruebas, documentación y atención a la accesibilidad.

### 3.2 Componentes del sistema

**Web — 7 componentes en `src/design/`**

| **Componente** | **Variantes** | **Función** |
|---|---|---|
| `Button` | `primary`, `secondary`, `ghost`, `danger`; tamaños `small`, `medium`, `large` | Botón base de toda la aplicación |
| `Input` | `default`, `otp`, `bare` | Campo de formulario con etiqueta, icono accionable y modo código |
| `Card` | — | Contenedor superficial con título opcional |
| `Header` | — | Cabecera de página interna con migas y zona de acciones |
| `BreadCrumb` | — | Migas de pan; el ítem con ruta es enlace, el actual es texto |
| `ActionMenu` | 4 posiciones | Menú desplegable de acciones |
| `Badge` | `filled`, `outline` | Insignia de estado |

**Móvil — 18 componentes en `src/components/`**

| **Componente** | **Variantes** | **Función** |
|---|---|---|
| `Button` | 4 variantes, 3 tamaños | Botón base (`Pressable`) |
| `Card` | `default`, `soft`, `selected`; 4 niveles de relleno | Superficie; se convierte en pulsable si recibe `onPress` |
| `Input` | `default`, `otp` | Campo con estado de foco interno |
| `Modal` | `default`, `danger` | Diálogo centrado con animación de aparición |
| `Alert` | `success`, `warning`, `danger`, `info` | Banner con icono, título y mensaje |
| `Header` / `Breadcrumb` | — | Cabecera y migas |
| `ActionMenu` | — | Botón con menú anclado por medición de posición |
| `OtpInput` | Longitud configurable | Casillas de código de un dígito |
| `Switch` | — | Interruptor con los colores del tema |
| `Chart` | `area`, `bar`, `pie` | Envoltura de gráficas |
| `Loader` | `small`, `large` | Indicador de carga con texto |
| `EmptyState` | `full`, `compact` | Estado vacío con imagen o icono |
| `ListItem` | — | Fila de lista reutilizable |
| `LanguageSwitcher` | `dark`, `light` | Selector de idioma con bandera |
| `Sidebar`, `BottomTabBar`, `AuthGate` | — | Componentes de disposición y navegación |

> **Paridad comprobada.** Siete de los componentes del sistema web tienen equivalente directo en el móvil, con las mismas variantes. Es la evidencia de que se trata de un único diseño, no de dos.

### 3.3 Paleta de colores

**Figura 8.** *Paleta de marca y colores semánticos del sistema de diseño.*

**Colores de marca y semánticos**

| **Token** | **Valor** | **Uso** |
|---|---|---|
| `--color-primary` | `#0078D7` | Botón primario, enlaces, indicador de foco |
| `--color-primary-hover` | `#3399FF` | Estado presionado, borde de campo enfocado |
| `--color-primary-dark` | `#0A2540` | Extremo del gradiente, fondo de barras de límite |
| `--color-primary-soft` | `#E8F3FF` | Fondo sutil de pestañas activas |
| `--color-secondary` | `#32CD32` | Verde de acento y de éxito |
| `--color-success` | `#2ECC71` | Confirmación |
| `--color-warning` | `#FFD700` | Advertencia |
| `--color-danger` | `#E63946` | Acción destructiva |
| `--color-info` | `#17A2B8` | Información |

**Superficies y texto**

| **Token** | **Valor** |
|---|---|
| `--color-background` | `#F8F9FA` |
| `--color-background-left` | `#3F6BAE` |
| `--color-surface` | `#FFFFFF` |
| `--color-text-primary` | `#212529` |
| `--color-text-secondary` | `#6C757D` |
| `--color-border` | `#E5E7EB` |

**Pares de estado** — cada estado tiene un fondo suave y un color de texto con contraste suficiente

| **Estado** | **Fondo** | **Texto** |
|---|---|---|
| Error / crítico | `#FCEBEB` | `#A32D2D` |
| Éxito / en línea | `#EAF3DE` | `#3B6D11` |
| Advertencia | `#FFF4D6` | `#854F0B` |
| Información | `#D1ECF1` | `#0C5460` |

> **El móvil replica los 32 tokens de color** en su contrato `ThemeColors`, con los mismos valores hexadecimales. La paridad está documentada en el propio código con comentarios que remiten al archivo de origen del lado web.

### 3.4 Los cuatro temas

El requisito RF-CLI-06 (configuración de colores) se implementa con **cuatro temas**, dos claros y dos oscuros. La preferencia se guarda en el dispositivo (`energymonitor_theme`) y en la base de datos (`user_configuration.color_theme`).

| **Tema** | **Modo** | **Barra lateral** | **Principal** | **Carácter** |
|---|---|---|---|---|
| **EnergyMonitor** *(predeterminado)* | Claro | `#3F6BAE` | `#0078D7` | Corporativo, sobrio |
| **Eco Hogar** | Claro | `#2E7D32` | `#32CD32` | Natural, orientado al ahorro |
| **EnergyMonitor Dark** | Oscuro | `#0F172A` | `#1E293B` | Oscuro de alto contraste |
| **Eco Dark** | Oscuro | `#102A1A` | `#183321` | Oscuro con matiz verde |

**Modo oscuro real, no invertido.** En los temas oscuros cada token se reasigna: no se aplica un filtro de inversión sobre el tema claro. Los colores suaves pasan de hexadecimales sólidos a valores translúcidos (`rgba(…, 0.15)`), y el color de peligro se aclara a `#FCA5A5` para mantener el contraste sobre fondos oscuros.

**Vista previa en la interfaz.** La pantalla de configuración dibuja una miniatura de cada tema —barra lateral, barra de progreso y puntos de color— construida con los mismos valores que se aplicarán. El usuario elige viendo, no leyendo.

### 3.5 Colores de gráficas

Las gráficas usan una paleta categórica fija, independiente del tema, con **variante para modo claro y oscuro**:

| **Electrodoméstico** | **Claro** | **Oscuro** |
|---|---|---|
| Nevera | `#2563EB` | `#60A5FA` |
| Lavadora | `#7C3AED` | `#A78BFA` |
| Televisor | `#DB2777` | `#F472B6` |
| Microondas | `#EA580C` | `#FB923C` |
| Aire acondicionado | `#0D9488` | `#2DD4BF` |
| Equipo de cómputo | `#CA8A04` | `#FACC15` |
| Calentador de agua | `#DC2626` | `#F87171` |
| Iluminación | `#65A30D` | `#A3E635` |
| Otros | `#64748B` | `#94A3B8` |

> **Por qué esta paleta.** Los colores no se eligen al azar: cada uno mantiene una relación de contraste perceptible con los demás, de modo que dos segmentos adyacentes de una gráfica circular nunca se confunden. Además, el mismo electrodoméstico tiene siempre el mismo color, lo que permite reconocerlo de una pantalla a otra sin consultar la leyenda.

### 3.6 Tipografía

**Familias**

| **Plataforma** | **Fuente primaria** | **Fuente secundaria** |
|---|---|---|
| Web | `Inter` | `Poppins` |
| Móvil | `System` (fuente del sistema operativo) | `System` |

> **Deuda técnica documentada.** La web declara `Inter` y `Poppins` en sus variables CSS, pero no carga ninguna de las dos mediante `@font-face` ni desde un CDN. El navegador cae en la fuente genérica del sistema. Para corregirlo basta con añadir el `@font-face` o el enlace correspondiente en `index.html`.

**Escala tipográfica (11 medidas, idéntica en ambas plataformas)**

| **Token** | **Valor** | **Uso típico** |
|---|---|---|
| `size11` | 11 px | Texto de gráficas y etiquetas de leyenda |
| `xs` | 12 px | Texto auxiliar |
| `size13` | 13 px | Etiquetas pequeñas |
| `sm` | 14 px | Texto de controles |
| `md` | 16 px | **Texto base del cuerpo** |
| `size18` | 18 px | Texto destacado |
| `lg` | 20 px | Subtítulos |
| `size22` | 22 px | Títulos de tarjeta |
| `xl` | 24 px | Títulos de sección |
| `xxl` | 32 px | Títulos de pantalla |
| `xxxl` | 60 px | Mensajes de portada |

**Pesos:** `normal` (400), `medium` (500), `bold` (600).

> La escala es deliberadamente irregular: los nombres «redondos» (`xs`, `sm`, `md`, `lg`, `xl`, `xxl`) replican la escala de la web para conservar la paridad visual, y los «exactos» (`size11`, `size13`, `size18`, `size22`) cubren medidas que existen en el diseño pero no encajaban en la escala.

### 3.7 Espaciado y radios

**Espaciado** — escala de 4 puntos

| **Token** | **Valor** |
|---|---|
| `xs` | 4 px |
| `sm` | 8 px |
| `md` | 16 px |
| `lg` | 24 px |
| `xl` | 32 px |

**Radios de borde** — `sm` 6 px · `md` 10 px · `lg` 16 px · `999px` para píldoras y avatares.

**Sombras**

| **Token** | **Valor web** | **Elevación Android** |
|---|---|---|
| `shadow-sm` | `0 1px 2px rgba(0,0,0,0.05)` | 1 |
| `shadow-md` | `0 4px 6px rgba(0,0,0,0.1)` | 4 |
| `shadow-lg` | `0 10px 15px rgba(0,0,0,0.25)` | 10 |

> **Detalle móvil.** Cada sombra es un par: la cadena `boxShadow` para iOS y web, y el valor `elevation` para Android. Definir solo una de las dos produce sombras invisibles en una de las plataformas.

### 3.8 Puntos de quiebre (responsive)

| **Token** | **Valor** | **Dispositivo** |
|---|---|---|
| `--bp-mobile` | 480 px | Móvil pequeño |
| `--bp-tablet` | 768 px | Tablet y móvil |
| `--bp-laptop` | 1024 px | Computador portátil |

**Transformaciones responsive más relevantes**

| **Componente** | **Escritorio** | **≤ 1024 px** | **≤ 768 px** |
|---|---|---|---|
| Barra lateral | Fija, 240 px, plegable a 80 px | Igual | Capa deslizable fuera de pantalla, 85 % del ancho con máximo de 300 px |
| Navegación superior | Completa con enlaces | Tipografía reducida | Menú hamburguesa desplegable |
| Pestañas del detalle de hogar | Horizontales con texto | Barra fija en la parte inferior | Sólo iconos, con barra inferior |
| Migas de pan | Todos los niveles | Todos los niveles | Sólo primer y último nivel, con desplazamiento horizontal |
| Diálogos | Centrados | Centrados | **Hoja inferior** (*bottom sheet*) pegada al borde inferior |
| Panel de marca del acceso | 50 % izquierdo | Ajustes de relleno | Oculto; el formulario ocupa el ancho completo |
| Barras de progreso | Ancho completo | Ancho completo | Barras más altas para facilitar el toque |

> **Estrategia de la aplicación móvil.** La app está bloqueada en orientación vertical (`orientation: "portrait"` en `app.json`). No hay diseño horizontal que resolver.

## 4. Estructura de pantallas — Aplicación web

**Figura 9.** *Mapa de navegación de la aplicación web.*

### 4.1 Tabla de rutas

| **Ruta** | **Pantalla** | **Acceso** | **Disposición** |
|---|---|---|---|
| `/` | Redirección a `/home` | Público | — |
| `/home` | Página de bienvenida | Público | Navegación + portada + beneficios |
| `/about` | Sobre nosotros | Público | Navegación + contenido |
| `/support` | Soporte y preguntas frecuentes | Público | Navegación + buscador de preguntas |
| `/login` | Iniciar sesión | Público | Panel de marca + formulario |
| `/register` | Registrarse | Público | Panel de marca + formulario |
| `/VerifyAccount` | Verificación de correo | Público | Panel de marca + código de 6 dígitos |
| `/recover-password` | Recuperar contraseña (paso 1) | Público | Panel de marca + formulario |
| `/verifyRecoverPassword` | Verificación y nueva contraseña (paso 2) | Público | Panel de marca + formulario |
| `/dashboard` | **Panel de hogares** | **Protegido** | Menú lateral + tarjetas de hogar |
| `/homes/:homeId` | **Detalle del hogar** | **Protegido** | Menú lateral + seis pestañas |
| `/favorites` | Favoritos | **Protegido** | Menú lateral + tarjetas |
| `/notifications` | Notificaciones (ADMIN: avisos de administración) | **Protegido** | Menú lateral + cuatro pestañas y filtro por hogar |
| `/settings` | Configuración | **Protegido** | Menú lateral + cuatro secciones |
| `/account` | Mi cuenta | **Protegido** | Pantalla propia dentro de la zona protegida |
| `/admin/users` | **Administración: usuarios de la plataforma** | **Protegido + permiso `USER.READ`** | Menú lateral de administrador + buscador + tabla paginada |
| `*` | Página no encontrada | Público | Mensaje y enlace de regreso |

### 4.2 Niveles de protección

El enrutamiento aplica **cinco guardas** con responsabilidades distintas:

| **Guarda** | **Qué verifica** | **Si falla** |
|---|---|---|
| `ProtectedRoute` | Que exista una sesión con token de acceso | Redirige a `/login` |
| `HomeRoute` | Que el hogar de la URL exista y sea accesible | Muestra «hogar no encontrado» con botón de regreso |
| `GuestRoute` | Que no haya sesión al abrir `/login` o `/register` | Lleva al panel |
| `UserOnlyRoute` | Que la cuenta no sea administradora (pantallas de hogares) | Lleva a `/admin/users` |
| `RequirePermission` | Que la cuenta tenga el permiso global indicado (`USER.READ` en `/admin/users`) | Lleva a `/dashboard`; la API responde 403 de todos modos |

**Figura 10.** *Flujo de decisión del acceso a una ruta protegida.*

**Decisión de diseño — contexto de salida.** `HomeRoute` entrega el hogar y el indicador de propietario (`isOwner`) a la pantalla hija mediante el contexto de salida del enrutador. Esto evita pasar el hogar como propiedad por tres niveles de componentes y, sobre todo, evita que cada pantalla tenga que consultarlo otra vez.

### 4.3 Detalle de las pantallas principales

#### Pantalla de bienvenida

| **Aspecto** | **Detalle** |
|---|---|
| **Elementos** | Navegación superior con Inicio, Beneficios, Cómo funciona, Sobre nosotros y Soporte; selector de idioma; botón de tema; botones «Iniciar sesión» y «Registrarse»; mensaje de bienvenida; sección de beneficios en tarjetas; sección «Cómo funciona» |
| **Objetivo** | Explicar qué hace el sistema en menos de un minuto y ofrecer el acceso directo |
| **Diseño** | Composición en dos bloques: presentación y llamado a la acción |

#### Panel de hogares (Dashboard)

| **Aspecto** | **Detalle** |
|---|---|
| **Elementos** | Menú lateral con inicio, favoritos, notificaciones y ajustes; indicador de avatar en la parte inferior; tarjetas de los hogares del usuario; botón «+» de acceso rápido |
| **Estados** | **Cargando** — indicador con texto; **Con error** — mensaje traducido y botón «Reintentar», con distinción entre «sin conexión» y «error del servidor»; **Vacío** — invitación a crear el primer hogar con imagen y botón; **Con contenido** — rejilla de tarjetas |
| **Tarjeta de hogar** | Nombre, color de cabecera identificativo, descripción, botón de favorito (corazón) y acceso al detalle |
| **Acciones** | Crear hogar (nombre, tipo, dirección, descripción) · Unirse a un hogar (código de 8 caracteres, con contador y conversión a mayúsculas automática) |

#### Detalle del hogar — pestaña **Consumo** (pestaña por defecto)

| **Aspecto** | **Detalle** |
|---|---|
| **Indicadores** | Tres tarjetas: **potencia actual** (kW y nivel de riesgo), **consumo de hoy** (kWh frente al límite), **dispositivos activos** (activos sobre total) |
| **Gráficas** | **Área** — potencia activa media por hora en las últimas 24 horas · **Circular** — distribución porcentual del consumo por tipo de electrodoméstico · **Barras horizontales** — consumo actual por dispositivo |
| **Barras de límite** | Dos barras de progreso: avance del límite diario y del límite mensual |
| **Estados** | Gráfica sin datos → componente vacío con icono, **nunca** un gráfico en ceros |

#### Detalle del hogar — pestaña **Historial**

| **Aspecto** | **Detalle** |
|---|---|
| **Selectores** | Periodo: día, semana, mes o año. Dentro del mes: semana específica o «mostrar todo» |
| **Gráfica** | Barras con la distribución del consumo por dispositivo a lo largo del periodo |
| **Tabla** | Ranking de dispositivos con barra de porcentaje por fila |
| **Tarjetas** | Consumo total del periodo, promedio y electrodoméstico que más consume |
| **Comportamiento** | Si hay más de 8 puntos, la gráfica se desplaza horizontalmente en lugar de comprimirse |

#### Pestañas **Usuarios**, **Dispositivos**, **Umbrales** y **Hogar**

| **Pestaña** | **Propietario** | **Miembro** |
|---|---|---|
| **Usuarios** | Lista de miembros con rol; puede expulsar a un miembro y transferirle la propiedad («Hacer responsable a …», con confirmación) | Lista de miembros; sólo consulta |
| **Dispositivos** | Lista con estado y consumo; vincular, editar y desvincular | Lista con estado y consumo; sólo consulta |
| **Umbrales** | Interruptor de valores predeterminados y campos de límite diario y mensual; botón «Guardar cambios» | Consulta de los límites; campos en solo lectura |
| **Hogar** | Datos del hogar, datos del propietario, código de acceso para compartir y botón «Eliminar hogar» (se confirma escribiendo el nombre del hogar) | Datos del hogar y del propietario; botón «Salir del hogar» |

> **Regla de interfaz.** El botón de acceso rápido y los elementos del menú lateral se muestran desde el inicio: el usuario descubre las funciones disponibles. Sin embargo, **las acciones que el rol no permite sí se ocultan** (por ejemplo, el botón «Eliminar hogar» no aparece para un miembro).

#### Pantalla de notificaciones

| **Aspecto** | **Detalle** |
|---|---|
| **Pestañas** | Todas · Alertas · Recomendaciones · Actividad, cada una con contador de pendientes, y filtro por hogar o «Mi cuenta» |
| **Acción masiva** | «Marcar todas como leídas» y «Eliminar resueltas o leídas» (con confirmación) |
| **Tarjeta** | Icono según el tipo · título · descripción · hogar y dispositivo relacionados · fecha y hora · etiqueta de estado (Nueva, Activa o Resuelta) · acción (Resolver, Marcar leída o eliminar) |
| **Iconografía** | Conectividad → wifi apagado · Dispositivo → placa · Límite → medidor · Crítico → rayo · Advertencia → triángulo · Recomendación → bombilla |

#### Pantalla de configuración

| **Sección** | **Contenido** |
|---|---|
| **Apariencia** | Cuadrícula de los cuatro temas con miniatura de vista previa |
| **Idioma** | Selector de español, inglés, francés o portugués, con bandera |
| **Notificaciones** | Interruptores de correo, push y combinado; indicación de navegadores suscritos |
| **Ayuda** | Documentación, contacto y actualizaciones con icono de marca propio (marcados «Próximamente») |

Una cuenta administradora no ve la sección **Notificaciones** de los ajustes.

#### Pantallas de administración (solo web, rol ADMIN)

| **Aspecto** | **Detalle** |
|---|---|
| **Menú lateral** | Administración · Notificaciones · Ajustes (sin Inicio, Hogares ni Favoritos) |
| **Usuarios de la plataforma** (`/admin/users`) | Migas de pan Inicio › Administración; título y subtítulo; buscador por nombre o correo; tabla Usuario (con marca «Tú») · Estado · Roles globales · Registro · Último acceso; paginación de 20 |
| **Acciones por fila** | Selector de estado Activa / Inactiva / Bloqueada (permiso `USER.MANAGE`) y botón de escudo para dar o quitar ADMIN (`ROLE.MANAGE`); ocultas en la fila propia y sin el permiso |
| **Confirmaciones** | `ConfirmModal` al dejar Activa y siempre al dar o quitar el rol |
| **Notificaciones de administración** | Lista de avisos (cuenta registrada, estado cambiado, rol otorgado o retirado) con Nueva/Leída, marcar como leída, marcar todas y eliminar |
| **Mockups** | No existen todavía (ver `03-analisis-software/ES/navegacion/README.md`, sección 6.1) |

**Figura 10b.** *Pantalla «Usuarios de la plataforma» con la confirmación de cambio de estado* — imagen pendiente.

## 5. Estructura de pantallas — Aplicación móvil

**Figura 11.** *Mapa de navegación de la aplicación móvil.*

### 5.1 Tabla de rutas

| **Ruta** | **Pantalla** | **Acceso** |
|---|---|---|
| `/` | Iniciar sesión | Público |
| `/register` | Registrarse | Público |
| `/recover-password` | Recuperar contraseña | Público |
| `/verify-account` | Verificación de correo | Público |
| `/verify-recover-password` | Verificación de recuperación | Público |
| `/new-password` | Nueva contraseña | Público |
| `/dashboard` | **Panel de hogares** | **Protegido** |
| `/homes` | Redirige a `/dashboard` | **Protegido** |
| `/home/[id]` | **Detalle del hogar** | **Protegido** |
| `/favorites` | Favoritos | **Protegido** |
| `/notifications` | Notificaciones | **Protegido** |
| `/settings` | Configuración | **Protegido** |
| `/account` | Cuenta | **Protegido** |

**13 rutas · 12 pantallas funcionales.** La app no tiene landing page ni pantallas de administración; el registro abre los términos y la política de privacidad en una ventana, y Ajustes y Mi cuenta tienen título de página.

### 5.2 Arquitectura de navegación

| **Elemento** | **Implementación** | **Decisión de diseño** |
|---|---|---|
| **Pila** | Un único `Stack` de Expo Router, sin cabecera | Estructura simple y predecible |
| **Menú lateral** | Componente propio con `Animated` y 300 ms de transición | Permite un acordeón con la lista de hogares sincronizado con el contexto global |
| **Barra de pestañas** | Componente propio en el detalle del hogar | El botón «atrás» sale del hogar y no de la sección |
| **Menú contextual** | `ActionMenu` con medición de posición (`measureInWindow`) | El desplegable se ancla correctamente aunque cambie el diseño |

**Figura 12.** *Estructura de navegación móvil: pila única, menú lateral propio y barra de pestañas propia.*

**Guarda de navegación.** `AuthGate` envuelve la pila completa. Define el conjunto de rutas públicas y, al detectar que la sesión no es válida, redirige a la pantalla inicial. Mientras restaura la sesión del almacenamiento no renderiza nada, para evitar un parpadeo de pantalla de carga.

### 5.3 Detalle del hogar — barra inferior de seis secciones

| **Sección** | **Icono** | **Contenido** |
|---|---|---|
| **Consumo** | Pulso | Tres indicadores, gráfica de área de 24 h, circular de distribución y barras por dispositivo, barras de límite diario y mensual |
| **Historial** | Reloj | Selector de periodo, gráfica de barras y tabla de ranking con barras de porcentaje |
| **Usuarios** | Personas | Lista de miembros con rol; el propietario puede expulsar |
| **Dispositivos** | Inalámbrico | Lista con estado de conexión y consumo; el propietario puede vincular, editar y desvincular |
| **Umbrales** | Deslizadores | Interruptor de predeterminados y campos de límite con selector de periodo |
| **Hogar** | Casa | Datos del hogar, datos del propietario y código de acceso |

### 5.4 Adaptación de pantallas a diferentes tamaños

| **Técnica** | **Aplicación** |
|---|---|
| Áreas seguras | Las 13 pantallas aplican `paddingTop: max(insets.top, espaciado.md)` y respetan el margen inferior del dispositivo |
| Anchos derivados | Los diálogos fijan un ancho máximo de 440 px, evitando diálogos gigantes en tableta |
| Menú lateral | Ancho de `min(85 % de la pantalla, 300 px)` |
| Alturas derivadas | Las listas largas de los diálogos ocupan como máximo el 55 %, 62 % o 45 % de la altura según su contenido |
| Desplazamiento en gráfica | Si la serie tiene más de 8 puntos, la gráfica se desplaza horizontalmente con ancho de `puntos × 56 px` |
| Teclado | Nueve pantallas usan el contenedor que evita el teclado, con comportamiento distinto en iOS y Android |
| Rejillas fluidas | Los selectores de tema, idioma y hogar usan envoltura con ancho base del 45 % |

## 6. Patrones de interacción

### 6.1 Formularios y validación

| **Aspecto** | **Web** | **Móvil** |
|---|---|---|
| **Motor** | `react-hook-form` + `zod` | `react-hook-form` + `zod` |
| **Esquemas** | 6 esquemas de autenticación y cuenta | 10 esquemas, incluidos hogar y umbrales |
| **Mensajes** | Función que recibe el traductor: el mensaje se escribe en el idioma activo | Igual, mediante un ayudante central |
| **Contraseña** | Ojos de visibilidad, reglas de complejidad, confirmación coincidente | Igual |
| **Código de 6 dígitos** | 6 casillas con avance automático y retroceso con la tecla de borrar | Componente `OtpInput` con el mismo comportamiento |

> **Validación en dos capas.** El esquema Zod actúa en el cliente para dar respuesta inmediata; el backend vuelve a validar todo con Bean Validation. La validación del cliente nunca sustituye a la del servidor: es una ayuda de usabilidad, no una garantía de integridad.

### 6.2 Estados de una vista de datos

Toda pantalla que consume información declara los mismos cuatro estados. Este patrón es la columna vertebral de la consistencia visual.

**Figura 13.** *Máquina de estados de una vista de datos.*

| **Estado** | **Presentación** | **Acción ofrecida** |
|---|---|---|
| **Cargando** | Indicador con texto y rol `status` | Ninguna; se espera |
| **Con error de red** | Mensaje traducido + icono de wifi apagado | «Reintentar» |
| **Con error del servidor** | Mensaje traducido por código de estado + icono de alerta | «Reintentar» |
| **Vacío** | Imagen o icono + título explicativo + acción principal | «Crear hogar», «Vincular dispositivo» |
| **Con contenido** | Los datos | Las propias de la pantalla |

> **Regla dura.** El mensaje de error que ve el usuario **nunca** es el que devuelve el servidor. El servidor responde con un código HTTP y el cliente lo traduce mediante un catálogo que combina la operación realizada con el código recibido. Esto impide filtrar detalles internos y garantiza que todos los mensajes estén traducidos.

### 6.3 Diálogos modales

| **Tipo** | **Uso** | **Comportamiento** |
|---|---|---|
| **Confirmación simple** | Acciones reversibles o moderadas | Título, mensaje, «Cancelar» y «Aceptar» |
| **Confirmación destructiva** | Eliminar hogar, transferir propiedad, salir del hogar, expulsar miembro, desvincular dispositivo, eliminar cuenta | Mismo diseño, pero con rol `alertdialog` y color de peligro |
| **Formulario** | Crear hogar, unirse a hogar, vincular dispositivo, editar perfil, cambiar contraseña | Campos con validación en línea |
| **Asistente por pasos** | Vincular un dispositivo | Cinco pasos con indicadores de avance y barras de señal |

**Comportamiento común**

| **Regla** | **Detalle** |
|---|---|
| Cierre | Botón de cierre, Cancelar o toque sobre el fondo oscurecido |
| Foco | Al abrir, el foco queda dentro del diálogo |
| Web — hoja inferior | En pantallas pequeñas el diálogo se convierte en hoja pegada al borde inferior |
| Móvil — teclado | El teclado no tapa los campos: el diálogo se desplaza |
| Confirmación doble | Ninguna acción destructiva se ejecuta sin una confirmación explícita |

### 6.4 Sistema de notificaciones multicanal

Un mismo evento se comunica por varios canales, según el contexto del usuario.

**Figura 14.** *Flujo multicanal de una alerta: de la medición al aviso en el dispositivo.*

| **Canal** | **Cuándo** | **Presentación** |
|---|---|---|
| **Insignia en el menú** | Sempre | Contador en la entrada del menú lateral, con tope visual en `99+` |
| **Título de la pestaña** | En el navegador | El título pasa a `(3) EnergyMonitor` |
| **Aviso emergente** | En pantalla | Panel que entra deslizando, máximo 3 a la vez, 8 segundos cada uno |
| **Notificación del navegador** | Con la aplicación abierta o cerrada | Notificación del sistema, al hacer clic se enfoca la aplicación |
| **Correo electrónico** | Según la preferencia del usuario | Mensaje enviado por SMTP |
| **Audio y vibración** | Móvil | Reservado para la versión con notificaciones nativas |

**Reglas del sistema**

| **Regla** | **Justificación** |
|---|---|
| Cada tipo tiene su icono y su color de severidad | Distinción por encima del color |
| El borde izquierdo indica la severidad | Rojo para crítico, ámbar para advertencia, azul para informativo |
| Los avisos ya vistos no se repiten al recargar | Se guardan los identificadores ya mostrados |
| Al tocar un aviso se navega a la bandeja | El aviso siempre lleva a su origen |
| Un fallo de red no vacía la bandeja | El usuario conserva lo que ya tenía |

### 6.5 Gráficas

| **Aspecto** | **Web** | **Móvil** |
|---|---|---|
| **Biblioteca** | `recharts` | `react-native-chart-kit` sobre `react-native-svg` |
| **Área** | Curva de 24 horas con degradado | Curva de 24 horas con sombra degradada |
| **Circular** | Dona con centro libre y separación entre segmentos | Sector con leyenda |
| **Barras** | Horizontales por dispositivo | Verticales por dispositivo |
| **Barras de límite** | Indicador construido con CSS | Indicador construido con vistas nativas |
| **Colores** | Paleta categórica con variante por tema | La misma paleta |
| **Escala** | Automática | Automática |
| **Interacción** | Tooltips con el estilo del sistema | Tooltips del componente |
| **Vacío** | Componente `EmptyChart` con icono y explicación | Mensaje «sin datos» — **nunca** una gráfica en cero |

> **Decisión de diseño — «sin datos» no es «cero».** En ambos front-ends se conserva la distinción entre «no hay mediciones» y «el consumo fue cero». Una gráfica que muestra una línea en cero afirma algo que el sistema no sabe; una gráfica vacía con explicación es honesta.

### 6.6 Datos en tiempo real

El requisito RF-MED-01 (registro automático de consumo) se refleja en la interfaz mediante sondeo en tres momentos distintos.

| **Dato** | **Frecuencia** | **Condición** |
|---|---|---|
| Dispositivos y su estado | 5 segundos | Sólo con la aplicación en primer plano (móvil) |
| Resumen de consumo | 30 segundos | Sólo con la aplicación en primer plano (móvil) |
| Alertas y recomendaciones de todos los hogares | 30 segundos | Sólo con la aplicación en primer plano (móvil) |

**Estados de conexión derivados de la ausencia de datos**

| **Estado** | **Criterio** | **Presentación** |
|---|---|---|
| En línea | Datos recientes | Indicador verde + intensidad de señal en porcentaje |
| Verificando | Más de 75 s sin datos | Indicador ámbar «comprobando» |
| Desconectado | Más de 150 s sin datos | Indicador gris + «Desconectado» |

> **Decisión de diseño.** Un estado intermedio de «verificando» evita el falso positivo: si el dispositivo no ha reportado en 75 segundos, todavía no se declara desconectado. Sin este estado, una caída breve de la red aparecería como una desconexión del dispositivo.

### 6.7 Navegación y realimentación

| **Elemento** | **Comportamiento** |
|---|---|
| **Volver** | En el móvil, el botón atrás sale del detalle del hogar hacia el panel; dentro de la aplicación web, las rutas del navegador funcionan con normalidad |
| **Favoritos** | Botón de corazón en cada tarjeta; el valor aplicado es el que devuelve el servidor, no una alternancia local, para que dos toques rápidos no dejen el corazón al revés |
| **Idioma** | El cambio se aplica de inmediato en toda la aplicación y se persiste |
| **Tema** | El cambio se aplica de inmediato sin recargar la página |
| **Cerrar sesión** | Disponible desde el menú de perfil, en ambas plataformas |

## 7. Accesibilidad

**Requisito RNF-USA-01:** La interfaz debe ser clara, adaptable a distintos tamaños de pantalla y coherente, con retroalimentación inmediata. No se declara un nivel formal de conformidad con las pautas WCAG.

### 7.1 Lo que está implementado

| **Categoría** | **Implementación** |
|---|---|
| **Roles y regiones** | Diálogos con rol `dialog` y `aria-modal`; confirmaciones destructivas con rol `alertdialog`; interruptores con rol `switch`; grupos de radio con rol `radiogroup`; estados de carga con rol `status`; errores con rol `alert` |
| **Etiquetas** | Todos los botones de solo icono tienen etiqueta; los iconos decorativos están marcados para ser omitidos por el lector de pantalla |
| **Estados** | Interruptores, pestañas y tarjetas selections exponen su estado; los acordeones exponen si están expandidos |
| **Navegación por teclado** | Los diálogos se cierran con `Escape`; el código numérico avanza y retrocede automáticamente; las tarjetas y campos pulsables responden a `Enter` |
| **Foco visible** | Contorno de 2 px con el color primario en botones, menús y campos |
| **Regiones vivas** | La pila de avisos usa región `política`; el estado del dispositivo se anuncia |
| **Contraste** | Cada estado tiene un par fondo-texto diseñado para cumplir contraste; los temas oscuros reajustan el color de peligro para mantenerlo |
| **Tamaño táctil** | Controles con zona táctil ampliada en listas densas; en el móvil se agrandaron los objetivos táctiles pequeños |
| **Móvil (octubre de 2026)** | Etiquetas de lector de pantalla en campos, botones de icono y dígitos del código (que acepta pegado); roles y estados en los controles; títulos de pantalla marcados como encabezados; menú lateral oculto al lector cuando está cerrado; iconos en lugar de emojis |
| **Idioma del documento** | El atributo `lang` del documento cambia con el idioma elegido |
| **Formato regional** | Fechas y números se formatean según el idioma y la zona horaria del navegador |

### 7.2 Brechas identificadas

| **#** | **Brecha** | **Impacto** | **Recomendación** |
|---|---|---|---|
| 1 | Sin cierre con `Escape` en la mayoría de diálogos (solo en uno de la web) | Un usuario de teclado debe buscar el botón de cierre | Implementar cierre con `Escape` en el componente base de diálogo |
| 2 | Sin retención del foco dentro de los diálogos | El foco puede salir del diálogo con `Tab` | Implementar trampa de foco y restaurar el foco al cerrar |
| 3 | Las pestañas no usan los roles `tablist`, `tab` y `tabpanel` | Un lector de pantalla no anuncia cuántas secciones hay ni cuál está activa | Adoptar el patrón de pestañas accesibles |
| 4 | Los gráficos no tienen descripción textual alternativa | La información de las gráficas no es accesible | Añadir una descripción y remitir a la tabla de datos, que ya existe en el historial |
| 5 | Los elementos con `role="button"` sobre `div` no responden a la barra espaciadora | Se omite una acción de teclado esperada | Usar el elemento `button` nativo |
| 6 | Sin enlace de salto al contenido principal | Un usuario de teclado debe recorrer toda la navegación en cada pantalla | Añadir enlace de salto |

### 7.3 Pruebas orientadas a accesibilidad

La suite de pruebas del front-end web usa consultas por rol (`getByRole`) de forma sistemática, lo que constituye una comprobación involuntaria de accesibilidad: **si un control pierde su rol, la prueba falla**. Es una práctica barata y eficaz que conviene mantener.

## 8. Internacionalización

| **Aspecto** | **Detalle** |
|---|---|
| **Idiomas** | Español (predeterminado), inglés, francés y portugués |
| **Espacios de nombres** | 31 en la web · 25 en el móvil |
| **Total de archivos de traducción** | 124 en la web · 100 en el móvil |
| **Qué se traduce** | Todo el texto visible: etiquetas, mensajes de error, ayuda de los campos, nombres de días, unidades y mensajes del sistema |
| **Qué no se traduce** | El nombre del producto y el marcador de la contraseña |
| **Formato regional** | Fechas y números con `Intl` según el idioma activo; la zona horaria del navegador se envía al backend para agrupar las mediciones por hora local |
| **Idioma inicial** | Preferencia guardada → idioma del dispositivo → español |
| **Anti-parpadeo** | En el móvil, el idioma se resuelve **antes** de montar el proveedor, evitando el destello de la interfaz en español |
| **Mensajes de validación** | Los mensajes de los esquemas de validación se resuelven con el idioma activo, nunca están escritos en el código |

**Figura 15.** *Idiomas disponibles en el selector, con bandera y nombre del idioma.*

## 9. Consistencia entre plataformas

### 9.1 Mapa de equivalencias

| **Web** | **Móvil** | **Equivalencia** |
|---|---|---|
| Menú lateral fijo o plegable | Menú lateral deslizable | Mismos destinos, misma posición de los elementos |
| Barra de pestañas horizontal superior | Barra inferior con iconos | Mismas seis secciones del detalle del hogar |
| Panel de marca 50/50 | Cabecera de marca con degradado | Mismo propósito: identidad del producto frente al formulario |
| Diálogo centrado | Diálogo centrado | Mismos componentes y variantes |
| Diálogo como hoja inferior en móvil | Diálogo centrado nativo | Adaptación coherente con la plataforma |
| Navegación superior con hamburguesa | Sin equivalente: el menú lateral cumple la función | Decisión propia de cada plataforma |
| Notificación del navegador (Web Push) | Notificación push nativa (Expo) | Mismas preferencias de correo y push |
| Web Bluetooth del navegador | Bluetooth nativo (BLE) | Mismo asistente de vinculación |
| Pantallas de administración | Sin equivalente | Decisión: la administración solo se hace en la web |

### 9.2 Elementos comunes verificados

| **Elemento** | **Coincidencia** |
|---|---|
| 32 tokens de color | Valores hexadecimales idénticos |
| 4 paletas de tema | Mismos identificadores, mismos colores de barra y de superficie |
| 11 medidas tipográficas | Idénticas, incluidos los valores «exactos» |
| Escala de espaciado | Idéntica (4, 8, 16, 24, 32) |
| Paleta de gráficas | Idéntica, con la misma variante clara y oscura |
| Siete componentes del sistema | Mismas variantes en `Button`, `Card`, `Input`, `Header`, `Breadcrumb`, `ActionMenu`, `Alert` |
| Reglas de validación | Mismo conjunto de reglas de contraseña y correo |
| Endpoints consumidos | El mismo conjunto de rutas de la API |
| Modelo de errores | Misma estructura: código de estado y traducir el mensaje |

> **Recomendación para la siguiente iteración.** La paridad se mantiene por replicación manual de valores, con comentarios que remiten al archivo de origen. Esto funciona, pero es frágil: un cambio en la paleta web puede no llegar al móvil. La solución sería un paquete compartido con los tokens, que ambos front-ends importen.

## 10. Brechas de la interfaz y trabajo pendiente

| **#** | **Brecha** | **Plataforma** | **Impacto** | **Trabajo pendiente** |
|---|---|---|---|---|
| 1 | Resuelta: alta de dispositivos por Bluetooth nativo | Móvil | — | Implementada con `react-native-ble-plx` |
| 2 | Resuelta: copiar el código de acceso | Móvil | — | Implementada con `expo-clipboard` |
| 3 | Resuelta: notificaciones nativas | Móvil | — | Push con `expo-notifications` |
| 4 | Resuelta: Google y enlaces legales | Móvil | — | Inicio nativo con Google y ventana legal en el registro |
| 5 | Resuelta: ruta de reserva | Web | — | Ruta `*` con `NotFound.jsx` |
| 6 | Resuelta: carga diferida de rutas | Web | — | Pantallas con `lazy` y `Suspense` |
| 6b | Sin pantallas de administración | Móvil | Un administrador debe usar la web | Decisión de alcance; mantener documentado |
| 7 | Sin carga diferida de imágenes | Web | Todas las imágenes se descargan al inicio | Añadir carga diferida |
| 8 | Sin prueba automatizada | Móvil | La verificación es manual | Incorporar un marco de pruebas |
| 9 | Componentes sin utilizar | Ambos | `Badge` (web), `ListItem` y `Breadcrumb` (móvil) | Integrarlos o retirarlos |
| 10 | Rutas huérfanas | Ambos | Archivos que no corresponden a ninguna ruta | Retirarlos del código |

## 11. Trazabilidad con los requisitos

| **Requisito (especificación de requisitos)** | **Pantalla o elemento de diseño** |
|---|---|
| RF-SEC-05 Acceso con redes sociales | Botón «Continuar con Google» en registro e inicio de sesión |
| RF-SEC-08 Actualización de información | Pantalla de cuenta: nombre, apellido y fotografía |
| RF-SEC-06 Recuperar contraseña | Pantallas de recuperación en dos pasos |
| RF-SEC-11 Eliminar cuenta | Diálogo de eliminación dentro de la pantalla de cuenta |
| RF-HOM-01 Registrar un hogar | Formulario de creación de hogar |
| RF-HOM-05 Gestión de permisos | Diferencia de acciones entre propietario y miembro |
| RF-HOM-05 Usuario responsable | Pestaña Hogar: datos del propietario y código de acceso |
| RF-HOM-07 Hogares favoritos | Botón de corazón en las tarjetas + pantalla de favoritos |
| RF-HOM-08 Configuración de umbrales | Pestaña de umbrales con interruptor de predeterminados |
| RF-CLI-06 Configuración de colores | Cuadrícula de cuatro temas con vista previa |
| RF-CLI-05 Configuración de idioma | Selector de idioma con bandera |
| RF-NOT-03 Configuración de alertas | Interruptores de notificación en configuración |
| RF-MED-08 Selección de rangos de fechas | Selector de periodo en la pestaña de historial |
| RF-MED-08 Comparación de consumo | Tabla de ranking con barra de porcentaje |
| RF-MED-08 Gráficos de tendencias | Gráficas de área, circular y de barras |
| RF-ALE-05 Visualización de alertas | Pantalla de notificaciones con tres pestañas |
| RF-REC-08 Visualización de recomendaciones | Pestaña de recomendaciones en notificaciones |
| RF-DEV-08 Vinculación del dispositivo | Asistente de vinculación por pasos |
| RF-MED-04 a RF-MED-06 Estado de conexión | Indicador por dispositivo con señal y estado derivado |
| RNF-USA-01 Usabilidad y accesibilidad | Sistema de diseño, cuatro temas, cuatro idiomas, estados declarativos |

## 12. Artefactos relacionados

| **Artefacto** | **Ubicación** |
|---|---|
| **Mockups** | `03-analisis-software/mockup/layout/{web,mobile}/` — fuente de verdad del diseño visual |
| **Mapas de navegación** | `03-analisis-software/ES/navegacion/` |
| Modelo arquitectónico de presentación | `05.1-modelo-arquitectonico.md`, sección 6 |
| Patrones de diseño de interfaz | `05.4-patrones-de-diseno.md`, sección 7 |
| Manual de usuario (web y móvil, documento único) | [`12-manual-usuario/ES/MD/Manual_usuario_EnergyMonitor.md`](../../08-manuals/es/user-manual.md) |