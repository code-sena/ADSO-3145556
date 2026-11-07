# Assets de los mockups

Recursos que usan los mockups de `mockup/layout/web/`. Son **copias** de los archivos reales
del aplicativo; la fuente original es `C:\Sena\Monitor-de-consumo\Energy-Monitor-application`
y no se modifica nunca desde aquí.

## Estructura

```
assets/
├── icons/
│   └── favicon.svg                 (versión vector del logo del proyecto)
├── logos/
│   └── project_logo.png            ← src/assets/logo_proyecto.png
├── images/
│   └── mockup-assets/
│       ├── hero_img.jpeg           ← src/assets/hero_img.jpeg
│       ├── hero_img2.png           ← src/assets/img_lp2.png
│       ├── empty_img.png           ← src/assets/empty_image.png
│       ├── google_icon.png         ← src/assets/google_icon.png
│       └── perfil.png              ← src/assets/perfil.png
└── flags/
    ├── flag-co.svg
    ├── flag-us.svg
    ├── flag-fr.svg
    └── flag-br.svg                 (creados para los mockups, ver abajo)
```

## Origen de cada recurso

| Archivo aquí | Archivo en la app | Dónde lo usa la app | Dónde se usa en los mockups |
|---|---|---|---|
| `logos/project_logo.png` | `src/assets/logo_proyecto.png` | `Navbar.jsx`, `Sidebar.jsx`, `AuthLayout.jsx` | Navbar de `landing/` y logo del sidebar en `home-detail/` |
| `images/mockup-assets/hero_img.jpeg` | `src/assets/hero_img.jpeg` | `Hero.jsx` (imagen del hero) | `landing/index.html`, sección hero |
| `images/mockup-assets/hero_img2.png` | `src/assets/img_lp2.png` | `HowItWorks.jsx` | `landing/index.html`, "¿Cómo funciona?" |
| `images/mockup-assets/empty_img.png` | `src/assets/empty_image.png` | `dashboard/components/EmptyState` | `home-detail/*/devices-empty.html` |
| `images/mockup-assets/perfil.png` | `src/assets/perfil.png` | (recurso del proyecto, sin uso en el código actual) | Avatar del usuario en el pie del sidebar de `home-detail/` |
| `images/mockup-assets/google_icon.png` | `src/assets/google_icon.png` | `GoogleSignInButton.jsx` | Reservado para `mockup/layout/web/auth/` (aún sin HTML) |
| `icons/favicon.svg` | **no** es copia — ver abajo | `index.html` de la app (purple "Z" de plantilla) | `<link rel="icon">` de los 17 mockups |

Los nombres se aliasearon para que los mockups enganchen con lo que ya referenciaban
(`project_logo.png`, `hero_img2.png`, `empty_img.png`); el contenido es el del recurso original.

### `icons/favicon.svg`

`public/favicon.svg` de la app **no** es de EnergyMonitor: es el logo morado `#863bff` que dejó la
plantilla del proyecto (el mismo sobrante de plantilla que `public/icons.svg`), y ese morado ni
siquiera está en la paleta del aplicativo. Por eso `icons/favicon.svg` es una versión **vector** del
logo real del proyecto — el enchufe azul `#0078d7`, el arco del cable y las tres barras verdes
`#32cd32` de `logo_proyecto.png` — dibujada a 48×48 para que se lea también a 16 px. Si más
adelante la app define un favicon propio, basta con reemplazar este archivo.

## Banderas

En la app el selector de idioma usa emojis (`🇨🇴 🇺🇸 🇫🇷 🇧🇷`). Los emojis de bandera no se
renderizan igual en todos los sistemas —en Windows/Chrome se muestran como las letras
"CO", "US", "FR", "BR"— así que aquí se usan **SVG propios**, que se ven iguales en cualquier
navegador y respetan los colores oficiales de cada bandera.

| Idioma | Código i18n | Bandera | Archivo |
|---|---|---|---|
| Español | `es` | 🇨🇴 Colombia | `flags/flag-co.svg` |
| English | `en` | 🇺🇸 Estados Unidos | `flags/flag-us.svg` |
| Français | `fr` | 🇫🇷 Francia | `flags/flag-fr.svg` |
| Português | `pt` | 🇧🇷 Brasil | `flags/flag-br.svg` |

El orden y las etiquetas son los de la constante `LANGUAGES` de `Navbar.jsx`, y también los de
`LanguageSettings` (pantalla de Ajustes, todavía sin mockup).

> Si más adelante se decide replicar el comportamiento de la app, basta con sustituir el
> `<img class="flag">` por el emoji: el resto del marcado no cambia.

## Rutas relativas

| Desde | Prefijo |
|---|---|
| `mockup/layout/web/landing/*.html` | `../../../../assets/…` (4 niveles) |
| `mockup/layout/web/home-detail/{owner,member}/*.html` | `../../../../../assets/…` (5 niveles) |

## Tipografías

Los mockups cargan **Inter** (texto de la interfaz) y **Poppins** (titulares) desde Google
Fonts, que es lo que declara `src/styles/global.css`:

```css
--font-primary: "Inter", sans-serif;
--font-secondary: "Poppins", sans-serif;
```

Si el archivo se abre sin conexión a internet, cada hoja de mockup cae en su fallback
(`"Segoe UI", system-ui, sans-serif`) porque el token incluye una pila de fuentes local.