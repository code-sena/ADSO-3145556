---
name: Hydrological Clarity
colors:
  surface: '#f8f9ff'
  surface-dim: '#cbdbf5'
  surface-bright: '#f8f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#eff4ff'
  surface-container: '#e5eeff'
  surface-container-high: '#dce9ff'
  surface-container-highest: '#d3e4fe'
  on-surface: '#0b1c30'
  on-surface-variant: '#3e484f'
  inverse-surface: '#213145'
  inverse-on-surface: '#eaf1ff'
  outline: '#6e787f'
  outline-variant: '#bdc8d0'
  surface-tint: '#006688'
  primary: '#006385'
  on-primary: '#ffffff'
  primary-container: '#007ea7'
  on-primary-container: '#fbfcff'
  inverse-primary: '#77d1ff'
  secondary: '#315ca9'
  on-secondary: '#ffffff'
  secondary-container: '#86adff'
  on-secondary-container: '#033e8a'
  tertiary: '#006575'
  on-tertiary: '#ffffff'
  tertiary-container: '#008094'
  on-tertiary-container: '#f8fdff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c2e8ff'
  primary-fixed-dim: '#77d1ff'
  on-primary-fixed: '#001e2c'
  on-primary-fixed-variant: '#004d68'
  secondary-fixed: '#d8e2ff'
  secondary-fixed-dim: '#aec6ff'
  on-secondary-fixed: '#001a42'
  on-secondary-fixed-variant: '#0f4490'
  tertiary-fixed: '#a7edff'
  tertiary-fixed-dim: '#58d6f1'
  on-tertiary-fixed: '#001f25'
  on-tertiary-fixed-variant: '#004e5b'
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
  surface-canvas: '#F8FBFD'
  surface-card: '#FFFFFF'
  surface-subtle: '#F2F2F2'
  border-subtle: '#E2E8F0'
  status-success: '#10B981'
  status-warning: '#F59E0B'
  status-danger: '#EF4444'
typography:
  display-hero:
    fontFamily: Manrope
    fontSize: 48px
    fontWeight: '800'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Manrope
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Manrope
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Manrope
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  body-lg:
    fontFamily: Manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 26px
  body-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 22px
  body-bold:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 22px
  telemetry-lg:
    fontFamily: IBM Plex Mono
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.02em
  telemetry-md:
    fontFamily: IBM Plex Mono
    fontSize: 16px
    fontWeight: '500'
    lineHeight: 24px
  telemetry-sm:
    fontFamily: IBM Plex Mono
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 18px
    letterSpacing: 0.04em
  caption:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 2rem
  margin: 2.5rem
  space-xs: 0.5rem
  space-sm: 0.75rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 3rem
---

## Brand & Style

This design system delivers an airy, modern, precision-engineered environmental monitoring experience. Rooted in water conservation and resource stewardship, the interface communicates reliability, clinical accuracy, and ecological serenity.

The visual style blends modern corporate clarity with fluid minimalism. High-saturation cerulean accents float over pale, ultra-clean aquatic backgrounds (`#F8FBFD`). Generous negative space balances high-density utility: telemetry streams, flow rates, and sensor arrays feel unhurried yet immediately actionable. Generous breathing room across structural surfaces reinforces a sense of purity, calm oversight, and effortless operational command.

## Colors

The color architecture is built upon a 3-step aquatic spectrum anchored by clean structural neutrals and crisp feedback alerts:

- **Primary (`#0096C7`)**: The vibrant cerulean wave tone. Used for core interactive states, primary action triggers, visual progress fills, and active navigation indicators.
- **Secondary (`#023E8A`)**: Deep structural navy. Used for structural hierarchy, high-level navigation chrome, core section headers, and prominent icon contours.
- **Tertiary (`#48CAE4`)**: Soft highlight sky blue. Used for secondary telemetry accents, auxiliary progress meters, badges, and hovering wave states.
- **Neutral (`#64748B`)**: Balanced slate gray for legible secondary body copy, passive iconography, and non-critical metrics.
- **Functional Semantics**:
  - `surface-canvas` (`#F8FBFD`): Pristine, atmospheric canvas tint with an imperceptible cool undertone.
  - `surface-card` (`#FFFFFF`): High-contrast pure white backdrop for elevated panels and actionable components.
  - `border-subtle` (`#E2E8F0`): Clean perimeter boundary for structural definitions without visual clutter.
  - `status-success` (`#10B981`), `status-warning` (`#F59E0B`), `status-danger` (`#EF4444`): Unambiguous operational status signals for flow thresholds, leak detection, and infrastructure health.

## Typography

The typographic hierarchy implements an intentional dual-engine framework:

1. **Humanist Ergonomics (`Manrope`)**: Powers all interface copy, module headers, navigation, and long-form instructions. Its geometric curves mirror the flowing shapes of liquid management while retaining crisp legibility at all scales.
2. **Technical Telemetry (`IBM Plex Mono`)**: Dedicated entirely to objective data: hardware IDs, IP addresses, sensory timestamps, volumetric units (m³/h, L/min), and status indicators. It enforces strict columnar alignment across operational dashboards.

## Layout & Spacing

Designed specifically for desktop-first experiences (1280px+ viewports), the layout model employs generous negative space to guarantee operational calm and reduce cognitive fatigue:

- **Grid Architecture**: 12-column fluid grid system pinned to a maximum content container width of `1600px`. The grid uses `2rem` (32px) gutters for clean module separation.
- **Canvas Boundaries**: Enforces a strict minimum of `2.5rem` (40px) outer horizontal padding on the viewport periphery and a minimum of `3rem` (48px+) vertical padding between major structural bands.
- **Internal Spacing Cadence**:
  - `space-xs` (8px): Micro-spacing between paired label and value rows or icon-and-text locks.
  - `space-sm` (12px): Standard inner chip and button inline padding.
  - `space-md` (16px): Card internal grid spacing and form control stacks.
  - `space-lg` (24px): Standard internal card padding.
  - `space-xl` (48px): Section breaks and dashboard panel separation.

## Elevation & Depth

Visual hierarchy uses crisp boundary delineation paired with faint, diffused ambient lighting rather than heavy drop shadows:

- **Surface Stacking**: The foundation rests on `surface-canvas` (`#F8FBFD`). Modular cards and data surfaces are elevated to pure white (`#FFFFFF`) with a continuous border of `1px solid #E2E8F0`.
- **Shadow Profile**:
  - **Level 0 (Flat Container)**: `box-shadow: none; border: 1px solid #E2E8F0;`
  - **Level 1 (Card Default)**: `box-shadow: 0 1px 3px 0 rgba(2, 62, 138, 0.04), 0 1px 2px -1px rgba(2, 62, 138, 0.04);`
  - **Level 2 (Hover & Active Metric)**: `box-shadow: 0 10px 25px -5px rgba(2, 62, 138, 0.06), 0 8px 10px -6px rgba(2, 62, 138, 0.04); transform: translateY(-1px);`
  - **Level 3 (Modals & Overlays)**: `box-shadow: 0 20px 25px -5px rgba(2, 62, 138, 0.1), 0 8px 10px -6px rgba(2, 62, 138, 0.05);`
- **Atmospheric Tinting**: Shadows utilize an extremely dilute secondary brand tint (`rgba(2, 62, 138, ...)`) rather than true black, preserving chromatic harmony across light backgrounds.

## Shapes

The design system uses a controlled corner radius strategy that balances clinical instrumentation with water-inspired smoothness:

- **Interactive Controls (Inputs, Buttons, Tabs)**: Configured with `8px` (`0.5rem`) corner rounding, matching the standardized `48px` structural control height.
- **Containers & Surfaces (Cards, Modals, Panels)**: Scaled to `12px` (`0.75rem`) border radius, framing data with approachable modern softening.
- **Pills & Status Indicators**: Full rounded radii (`9999px`) reserved strictly for status chips, telemetry alerts, and notification badges.
- **Stroke Weights**: Standard structural line borders are fixed at `1px`. Emphasized graphical vectors, selection outlines, and custom SVG icons conform to a robust `2px` or `4px` visual stroke cadence.

## Components

### Buttons
- **Height & Sizing**: Standardized vertical height of `48px` across primary and secondary controls.
- **Primary Button**: Solid `#0096C7` fill with `#FFFFFF` Manrope semi-bold typography, 8px border radius, and horizontal padding of `24px`. Hover transition shifts fill to `#023E8A`.
- **Secondary Button**: Crisp `#FFFFFF` surface with `1.5px solid #E2E8F0` border, `#023E8A` typography, and soft hover highlight `#F8FBFD`.
- **Destructive Action**: `#FFFFFF` background with `1.5px solid #EF4444` and text in `#EF4444`, transitioning to filled red on confirmed states.

### Input Fields & Controls
- **Height & Frame**: Generous `48px` touch target with an inner horizontal inset of `16px`.
- **Default State**: Surface `#FFFFFF`, border `1px solid #E2E8F0`, corner radius `8px`. Body typography in Manrope 14px.
- **Active / Focus State**: Crisp ring using `border-color: #0096C7; box-shadow: 0 0 0 3px rgba(0, 150, 199, 0.15); outline: none;`.
- **Labels**: Rendered in Manrope 14px Semi-Bold positioned `8px` above the input frame. Helper and telemetry captions use `IBM Plex Mono` 12px.

### Cards & Telemetry Tiles
- **Padding & Structure**: Enforces generous internal padding of `24px` to `32px`.
- **Composition**: Distinct separation between the header (Manrope 16px bold with metadata chip) and the primary metric value (IBM Plex Mono 28px).
- **Background**: Solid `#FFFFFF` on `#F8FBFD` ground, bounded by `1px solid #E2E8F0` with subtle Level 1 ambient depth.

### Telemetry Chips & Badges
- **Dimensions**: `28px` height, full pill radius (`9999px`), horizontal padding of `12px`.
- **Typography**: IBM Plex Mono 12px Medium with uppercase letter tracking (`0.04em`).
- **Variants**:
  - *Normal/Active*: Background `rgba(0, 150, 199, 0.1)`, text `#0096C7`.
  - *Warning/High Consumption*: Background `rgba(245, 158, 11, 0.12)`, text `#D97706`.
  - *Critical/Leak Detected*: Background `rgba(239, 68, 68, 0.12)`, text `#DC2626`.

### Checkboxes & Radio Controls
- **Geometry**: Checkboxes utilize `8px` rounded squares (`size: 20px`); radio elements are 20px circles.
- **Checked State**: Solid `#0096C7` fill with crisp `#FFFFFF` internal checkmark.

### Telemetry Data Tables
- **Row Cadence**: Relaxed row height of `56px` with `16px` horizontal cell padding.
- **Headers**: IBM Plex Mono 12px uppercase, text color `#64748B`, with a bottom border of `1px solid #E2E8F0`.
- **Data Rows**: Alternating light striping optional; values rendered in IBM Plex Mono 14px aligned along fixed tab stops.