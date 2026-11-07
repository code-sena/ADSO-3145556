---
name: HydroMetric Clean
colors:
  surface: '#f9f9ff'
  surface-dim: '#d3daea'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f0f3ff'
  surface-container: '#e7eefe'
  surface-container-high: '#e2e8f8'
  surface-container-highest: '#dce2f3'
  on-surface: '#151c27'
  on-surface-variant: '#3e484f'
  inverse-surface: '#2a313d'
  inverse-on-surface: '#ebf1ff'
  outline: '#6e787f'
  outline-variant: '#bdc8d0'
  surface-tint: '#006688'
  primary: '#006385'
  on-primary: '#ffffff'
  primary-container: '#007ea7'
  on-primary-container: '#fbfcff'
  inverse-primary: '#77d1ff'
  secondary: '#006878'
  on-secondary: '#ffffff'
  secondary-container: '#69e5ff'
  on-secondary-container: '#006575'
  tertiary: '#2e5aa7'
  on-tertiary: '#ffffff'
  tertiary-container: '#4a73c1'
  on-tertiary-container: '#fefcff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#c2e8ff'
  primary-fixed-dim: '#77d1ff'
  on-primary-fixed: '#001e2c'
  on-primary-fixed-variant: '#004d68'
  secondary-fixed: '#a7edff'
  secondary-fixed-dim: '#58d6f1'
  on-secondary-fixed: '#001f25'
  on-secondary-fixed-variant: '#004e5b'
  tertiary-fixed: '#d8e2ff'
  tertiary-fixed-dim: '#aec6ff'
  on-tertiary-fixed: '#001a42'
  on-tertiary-fixed-variant: '#0f4490'
  background: '#f9f9ff'
  on-background: '#151c27'
  surface-variant: '#dce2f3'
typography:
  display:
    fontFamily: Manrope
    fontSize: 44px
    fontWeight: '800'
    lineHeight: 52px
    letterSpacing: -0.02em
  display-mobile:
    fontFamily: Manrope
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Manrope
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.015em
  headline-lg-mobile:
    fontFamily: Manrope
    fontSize: 24px
    fontWeight: '800'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Manrope
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 30px
  headline-sm:
    fontFamily: Manrope
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 26px
  body-lg:
    fontFamily: Manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-md:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
  data-metric-lg:
    fontFamily: JetBrains Mono
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.02em
  data-metric-md:
    fontFamily: JetBrains Mono
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 26px
  data-metric-sm:
    fontFamily: JetBrains Mono
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-desktop: 1.5rem
  margin: 1rem
  margin-tablet: 1.5rem
  margin-desktop: 2.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
  space-2xl: 3rem
---

## Brand & Style
The design system reflects an ecological, precise, and reassuring visual identity centered on water conservation and real-time consumption telemetry. The interface balances high-legibility data visualization with an organic, clean flow that instills confidence and environmental mindfulness.

### Visual Style
- **Ecological Precision:** Clean surfaces with high-clarity metrics, avoiding cluttered technical instrumentation while preserving analytical accuracy.
- **Fluid Modernism:** Crisp contrast against soft crystalline tints (`#F8FBFD`), structured cards, and tactile interactions tailored for telemetry tracking, alerts, and sustainable habits.
- **Audience & Tone:** Eco-conscious consumers, smart-home managers, and facility supervisors seeking calm, actionable clarity over complex telemetry.

## Colors
The palette utilizes water-inspired blue tones anchored by deep naval contrasts for text and structured neutral boundaries.

### Core Swatches
- **Primary (`#0096C7`):** Primary action buttons, active toggles, chart focus points.
  - Hover: `#0077A3`
  - Active: `#005F82`
- **Secondary (`#48CAE4`):** Secondary interactive accents, fill gradients for gauge volumes, visual highlights.
- **Background (`#F8FBFD`):** Light, ultra-soft aquamarine canvas that prevents eye fatigue.
- **Surface (`#FFFFFF`):** High-clarity elevated containers and operational dashboards.
- **Border (`#E5E7EB`):** Subtle dividers and low-contrast component borders.

### Typography Colors
- **Text Primary (`#023E8A`):** Headlines, main readings, heavy visual hierarchy anchors.
- **Text Secondary (`#0B5FA5`):** Subheadings, active labels, secondary telemetry markers.
- **Text Muted (`#6B7280`):** Supporting metadata, timestamps, and placeholder copy.

### Feedback & Status Tokens
- **Success (`#10853B`):** Optimal consumption, goal achieved, leak-free status.
- **Warning (`#C98107`):** Approaching threshold, seasonal anomaly, high baseline alert.
- **Error (`#A91B1B`):** Active leak detected, pipe pressure drop, billing ceiling exceeded.

## Typography
The system balances human-centered clarity with technical accuracy.

- **Primary Typeface (Manrope):** Geometric and approachable with high legibility across headlines and UI components. Used in weights 400 (Regular), 600 (Semi-Bold), and 800 (Extra-Bold).
- **Metric Typeface (Monospace):** Dedicated fixed-width numerals preserve tabular alignment across dashboards, dynamic water volume counters (L/min, m³), and timestamped telemetry feeds.
- **Metric Pairings:** When a data metric is shown alongside its volume unit (e.g., `124.5 L`), the numeric value uses monospace styling while the unit indicator transitions back to Manrope Semi-Bold for an integrated, polished display.

## Layout & Spacing
The layout follows a fluid 8pt/4pt mathematical spacing system designed to optimize sensor data density without visual overcrowding.

### Grid Framework
- **Mobile (< 768px):** 4-column layout with `16px` outer margins and `16px` column gutters. Single-column card stacking for metric cards and consumption reports.
- **Tablet (768px - 1024px):** 8-column layout with `24px` outer margins and `20px` gutters. Dual-card layouts for gauge widgets and chronological charts.
- **Desktop (> 1024px):** 12-column structured grid with max-width container capped at `1280px`, `40px` outer margins, and `24px` gutters. Enables multi-pane analytical dashboards with persistent sidebar navigation.

### Component Internal Padding
- Standard operational cards enforce uniform internal padding of `16px` (`space-md`), preserving rhythmic alignment across disparate modules.

## Elevation & Depth
Depth conveys urgency and hierarchical structure without heavy artificial shadow styling.

### Shadow System
- **Base Level (Cards & Containers):** Soft elevation using `0 2px 8px rgba(0, 0, 0, 0.08)`. Eliminates harsh boundary lines and harmonizes with `#F8FBFD` backgrounds.
- **Hover & Active States:** Enhanced depth using `0 4px 14px rgba(0, 150, 199, 0.12)`, imparting a localized water-tinted glow to interactive telemetry blocks.
- **Overlays, Floating Action Menus, and Drawers:** `0 8px 24px rgba(2, 62, 138, 0.12)`.

### Border Strategy
- All surfaces utilize a `1px` continuous perimeter border rendered in `#E5E7EB`.
- Critical alert cards (leaks, anomalies) combine the border with status-tinted background fills (e.g., `#A91B1B` at 4% opacity with a `1px` border of `#A91B1B`).

## Shapes
A foundational corner radius of `12px` (`0.75rem`) balances modern ergonomics with technical instrumentation structure.

### Radius Assignments
- **Cards, Panels & Data Containers:** Strict `12px` corner radius.
- **Inputs, Dropdowns & Modals:** `12px` corner radius to match cards.
- **Buttons & Action Chips:** `12px` for standard elements. Floating action buttons and pill badge counters can utilize fully rounded endpoints (`9999px`) when representing continuous states.
- **Progress Trackers & Tank Level Meters:** Outer containment tracks follow `12px`, with internal fluid fills matching outer curves precisely.

## Components

### Buttons
- **Primary:** Background `#0096C7`, text `#FFFFFF`, font Manrope 600 (`14px`), border-radius `12px`, padding `12px 20px`. Transition: `background-color 150ms ease`.
  - Hover: `#0077A3`.
  - Active: `#005F82`.
- **Secondary:** Background `transparent`, border `1.5px solid #0096C7`, text `#0096C7`.
  - Hover: Background `#0096C7` at 8% opacity.
- **Critical / Danger (Emergency Valve Shutoff):** Background `#A91B1B`, text `#FFFFFF`, hover `#8C1616`.

### Cards & Telemetry Containers
- Background `#FFFFFF`, border-radius `12px`, border `1px solid #E5E7EB`, padding `16px`, shadow `0 2px 8px rgba(0,0,0,0.08)`.
- Cards separate the category header (Manrope 600, `#0B5FA5`) from the primary volume metric (IBM Plex Mono 600, `#023E8A`).

### Chips & Filter Tags
- Height `32px`, border-radius `8px` or `9999px`, padding `0 12px`.
- Inactive: Background `#FFFFFF`, border `1px solid #E5E7EB`, text `#6B7280`.
- Active: Background `#48CAE4` at 15% opacity, border `1px solid #0096C7`, text `#023E8A`, font-weight 600.

### Input Fields & Selects
- Height `44px`, border-radius `12px`, border `1px solid #E5E7EB`, background `#FFFFFF`, padding `0 16px`, text `#023E8A`, font Manrope 400.
- Focus: Border `1.5px solid #0096C7`, outline none, shadow `0 0 0 3px rgba(0, 150, 199, 0.15)`.
- Label: Manrope 600 (`12px`), color `#0B5FA5`, margin-bottom `6px`.

### Selection Controls (Checkboxes & Radios)
- Dimensions `20px x 20px`, border-radius `4px` (checkbox) or `50%` (radio), border `1.5px solid #E5E7EB`.
- Checked: Background `#0096C7`, border-color `#0096C7`, icon color `#FFFFFF`.

### Water-Specific Domain Components
- **Fluid Level Meter:** Cylindrical or rounded track (`#F8FBFD`, border `1px solid #E5E7EB`, radius `12px`), dynamic fill level rendered in gradient from `#48CAE4` to `#0096C7`.
- **Telemetry Readout Row:** Key-value pair list. Key in Manrope 400 (`#6B7280`), value in monospace font (`#023E8A`), separated by subtle dotted rule (`#E5E7EB`).
- **Flow Alert Banner:** Background filled with `#A91B1B` at 6% opacity, border `1px solid #A91B1B`, text `#A91B1B`, featuring icon, real-time loss counter, and immediate shutoff button.