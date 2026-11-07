---
name: Hydro Cleanse
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
  background: '#f8f9ff'
  on-background: '#0b1c30'
  surface-variant: '#d3e4fe'
typography:
  headline-xl:
    fontFamily: Manrope
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Manrope
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Manrope
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
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
  label-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
  label-sm:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
  code-sm:
    fontFamily: JetBrains Mono
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
    letterSpacing: 0.02em
  code-xs:
    fontFamily: JetBrains Mono
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 2rem
  margin: 4rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2.5rem
---

## Brand & Style

This design system establishes an environment of crystal-clear purity, civic trust, and modern ecological stewardship for desktop authentication and account access. Designed with a clean, light-focused minimalist aesthetic, the UI minimizes cognitive overhead through disciplined restraint, purposeful typography, and generous negative space. 

The emotional tone balances absolute administrative reliability with an inviting, civic-minded clarity. Interactions should feel frictionless, precise, and reassuring, prioritizing clear visual hierarchy over decorative noise to make essential security and onboarding flows feel effortless.

## Colors

The palette draws directly from the dynamics of clean water: deep, authoritative depths paired with luminous, crystalline surface tones.

- **Primary (`#0096C7`)**: The active hydro-cyan core, deployed for primary call-to-action buttons, key interactive states, and active focal elements.
- **Secondary (`#48CAE4`)**: A vibrant sky-cyan used strictly for subtle interactive accents, focused halos, informative badges, and illustrative highlights.
- **Tertiary (`#023E8A`)**: Deep oceanic navy, establishing authority and grounding branding accents, critical milestones, and high-emphasis focal points.
- **Background (`#F8FBFD`)**: A pristine, high-clarity off-white with a subtle cool undertone to prevent eye fatigue while preserving absolute cleanliness.
- **Surfaces (`#FFFFFF`)**: Pure solid white reserved for structured auth cards, modal layers, and elevated interactive inputs.
- **Text Layers**: 
  - Primary (`#0F172A`): Deep slate for maximum contrast and legibility across all headings and body copy.
  - Secondary (`#64748B`): Neutral slate for secondary descriptions, hints, and helper text.
  - Muted (`#94A3B8`): Subdued tone for placeholders, disabled states, and technical meta-labels.
- **Structural Outlines (`#E2E8F0`)**: Low-contrast, hairline boundary lines providing structure without visual clutter.

## Typography

The type system pairs modern geometric clarity with structural precision.

- **Primary Voice (Manrope)**: Delivers a balanced, humanized, yet architectural aesthetic for all headers, informational copy, form labels, and button labels. Numeric data in Manrope is set to tabular lining figures (`tnum`) when handling financial or operational volumetric figures.
- **Technical Sub-Voice (JetBrains Mono / Monospaced)**: Deployed strictly for OTP numeric entry, verification tokens, security hashes, meter reference IDs, and system status indicators. This distinction sharpens the sense of data authenticity and digital hygiene.

## Layout & Spacing

Optimized for expansive desktop viewports (1280px and wider), the layout philosophy favors isolation and focus over density:

- **Centralized Focus Canvas**: Auth flows strictly employ a centered single-column card structure with an explicit fixed width of `400px` (or `440px` when dual-column credential inputs require breathing room). The card sits in the vertical and horizontal center of a `100vh` viewport, bordered by an unencumbered `#F8FBFD` field.
- **Vertical Flow Rhythm**: Forms observe a strict vertical rhythm. Inputs are spaced at `1.25rem` (`20px`) intervals, section headers detach by `2rem` (`32px`), and action button clusters detach from fields by `1.5rem` (`24px`).
- **Internal Component Breathing Room**: Modal card boundaries feature `space-xl` (`40px`) inner padding, ensuring elements never crowd container perimeters.

## Elevation & Depth

Visual hierarchy is maintained via ultra-low contrast borders paired with delicate, high-diffusion ambient shadows that evoke a soft surface resting on water:

- **Level 0 (Canvas)**: Baseline tone `#F8FBFD`. Completely flat, non-reflective.
- **Level 1 (Auth Card Surface)**: Crisp `#FFFFFF` surface enclosed by a 1px continuous border (`#E2E8F0`). Elevated by an ambient shadow: `0px 1px 3px rgba(15, 23, 42, 0.03), 0px 12px 32px -4px rgba(2, 62, 138, 0.04)`. The blue-tinted dispersion prevents dead gray tones and preserves brand clarity.
- **Level 2 (Interactive Elements & Dropdowns)**: Active menus, tooltips, and elevated floating state buttons utilize `0px 4px 16px -2px rgba(15, 23, 42, 0.06), 0px 1px 2px rgba(15, 23, 42, 0.04)`.
- **Focus Rings**: Strict zero-offset 3px rings in `#0096C7` at 20% opacity (`box-shadow: 0 0 0 3px rgba(0, 150, 199, 0.2)`), signaling interactive intent without obscuring component structure.

## Shapes

The geometric framework is anchored on a stable `8px` (`0.5rem`) corner radius across all interactive and structural components.

- **Inputs, Buttons & Cards**: Built uniformly with `rounded-lg` (8px). This creates structural harmony across stacked form components where the 48px input height matches the proportional curvature of the 48px action buttons.
- **Chips, Checkboxes & Micro-Badges**: Small tags and system chips take `rounded` (4px) to retain sharp definition at small scales, while full status tags may employ full-radius pill styling.

## Components

### Buttons
- **Primary**: Solid `#0096C7` fill with `#FFFFFF` text (Manrope SemiBold 14px). Exactly 48px height, 8px border radius, full-width inside the 400px container. Hover: `#0284A8`. Active: `#023E8A`.
- **Secondary / Outline**: 48px height, 1px solid `#E2E8F0` with `#0F172A` text. Hover: `#F8FBFD` background with `#CBD5E1` border.
- **Ghost / Tertiary**: Transparent background with `#0096C7` text. Hover: `#F0F9FF` background.

### Input Fields
- **Dimensions & Visuals**: Exactly 48px height, full width. Surface `#FFFFFF`, border 1px solid `#E2E8F0`, corner radius 8px. Text color `#0F172A`, placeholder `#94A3B8`.
- **States**: 
  - Hover: Border changes to `#CBD5E1`.
  - Focus: Border snaps to `#0096C7` with a 3px outer halo of `rgba(0, 150, 199, 0.2)`.
  - Error: Border snaps to `#EF4444` with a 3px outer halo of `rgba(239, 68, 68, 0.15)`.

### Cards & Auth Containers
- Centered 400px wide solid `#FFFFFF` module, 40px internal padding (`p-10`), 8px border radius, 1px border `#E2E8F0`, illuminated with tinted ambient elevation.

### Checkboxes & Radios
- Size: 18px by 18px with a 4px corner radius. Unchecked state features an `#E2E8F0` border on `#FFFFFF`. Checked state transitions to a solid `#0096C7` fill with a crisp white check icon. Focus state carries a 2px `#0096C7` focus ring.

### Verification Code Inputs (OTP)
- Clustered set of single-digit cells: 48px wide by 56px high each, utilizing `JetBrains Mono` at 20px. Centered text alignment, 8px border radius, transitioning from `#E2E8F0` to `#0096C7` with outer glow upon active cursor focus.

### Status Indicators & Micro Labels
- Monospaced badges utilizing `code-xs`, framed with 1px border `#E2E8F0`, `#F8FBFD` background, and text in `#64748B`. Used to display connection security, legal jurisdiction indicators, and tenant environment tags.