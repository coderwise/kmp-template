```markdown
# Design System Document: The Atmospheric Canvas

## 1. Overview & Creative North Star
**Creative North Star: "The Ethereal Editorial"**

This design system rejects the cluttered, widget-heavy aesthetic of traditional weather apps in favor of a high-end editorial experience. It treats meteorological data not as "stats," but as quiet, authoritative statements. By leaning into extreme whitespace and a monochromatic palette, we transform the interface into a "Canvas" where the weather is the only inhabitant.

To break the "template" look, this system utilizes **Intentional Asymmetry**. Instead of center-aligning every element, we use a modular, staggered layout where large-scale typography (Display-LG) creates a focal point, balanced by "floating" forecast modules. The goal is to make the user feel like they are reading a premium Swiss design magazine rather than an app.

---

## 2. Colors & Tonal Depth
The palette is rooted in Material 3's Light Mode logic but stripped of all "noise." We use a spectrum of greys and whites to create depth through light, not lines.

### The "No-Line" Rule
**Explicit Instruction:** Designers are prohibited from using 1px solid borders for sectioning. Boundaries must be defined solely through background color shifts. To separate a daily forecast from the current conditions, transition from `surface` (#f9f9f9) to `surface-container-low` (#f3f3f3). 

### Surface Hierarchy & Nesting
Treat the UI as a series of physical layers of fine paper. 
*   **Base Level:** `surface` (#f9f9f9) for the main background.
*   **Secondary Content:** `surface-container-low` (#f3f3f3) for subtle grouping.
*   **Primary Interaction/Cards:** `surface-container-lowest` (#ffffff) to provide a soft "pop" of brightness against the off-white background.

### The "Glass & Signature" Rule
For floating forecast cards or search bars, use **Glassmorphism**:
*   Apply `surface-container-lowest` with 80% opacity and a `24px` backdrop-blur. This ensures the layout feels integrated and airy.
*   **Signature Textures:** For high-priority elements like the "Current Temperature," use a subtle linear gradient from `primary` (#000000) to `primary-container` (#3c3b3b) at a 45-degree angle to give the numbers a metallic, premium weight.

---

## 3. Typography
We utilize **Inter** with an aggressive hierarchy scale to create "Visual Quiet."

| Level | Token | Weight | Size | Usage |
| :--- | :--- | :--- | :--- | :--- |
| **Hero Temp** | `display-lg` | 500 (Medium) | 3.5rem | The current temperature only. |
| **Condition** | `headline-md` | 400 (Regular) | 1.75rem | "Partly Cloudy" or "Clear Sky". |
| **Section Head** | `title-sm` | 600 (Semi-Bold) | 1rem | "3-Day Forecast" (All Caps, 0.05em tracking). |
| **Readings** | `body-lg` | 400 (Regular) | 1rem | High/Low temps, wind speeds. |
| **Metadata** | `label-sm` | 500 (Medium) | 0.6875rem | Humidity percentages, update times. |

**The Typography Philosophy:** Use `primary` (#000000) for the temperature to anchor the eye, and `on-surface-variant` (#474747) for secondary data. This 100% to 70% contrast ratio creates an effortless hierarchy.

---

## 4. Elevation & Depth
In this system, elevation is a whisper, not a shout.

*   **The Layering Principle:** Avoid shadows for static elements. Place a `surface-container-lowest` (#ffffff) card on a `surface-container-low` (#f3f3f3) background. The change in hex value provides enough "lift."
*   **Ambient Shadows:** Use only for floating action buttons or active search states.
    *   *Values:* `0px 8px 24px rgba(26, 28, 28, 0.06)`. The shadow color must be a tinted version of `on-surface`, never pure black.
*   **The "Ghost Border" Fallback:** If a container (like a search input) lacks contrast, use a **Ghost Border**: `outline-variant` (#c6c6c6) at **15% opacity**.
*   **Roundness:** Apply `ROUND_TWELVE` (0.75rem / 12px) to all containers to soften the "Brutalist" edges of the monochromatic scheme.

---

## 5. Components

### The "Atmospheric" Forecast Card
*   **Background:** `surface-container-lowest` (#ffffff).
*   **Border:** None.
*   **Layout:** Horizontal layout for the 3-day forecast. Use `display-sm` for the temp and `label-md` for the day.
*   **Spacing:** 24px (xl) internal padding to ensure the data has "room to breathe."

### Primary Buttons (Location/Settings)
*   **Shape:** Pill-shaped (Full Roundness).
*   **Color:** `primary` (#000000) with `on-primary` (#e5e2e1) text.
*   **Interaction:** On hover/tap, shift to `primary-fixed-dim` (#474646). No shadows.

### Minimalist Weather Icons
*   **Style:** Thin-stroke (1.5pt) vector paths.
*   **Color:** `primary` (#000000).
*   **Implementation:** Icons should never exceed 32x32px. Let the typography do the heavy lifting; icons are merely decorative punctuation.

### Input Fields
*   **Background:** `surface-container-low` (#f3f3f3).
*   **Text:** `on-surface` (#1a1c1c).
*   **State:** When focused, the background remains the same, but a `Ghost Border` (outline-variant at 20%) appears.

---

## 6. Do’s and Don'ts

### Do
*   **Use Massive Whitespace:** If you think there's enough space, add 16px more. Whitespace is a functional element that prevents the monochromatic UI from feeling "cramped."
*   **Embrace Asymmetry:** Place the "Current Temperature" in the top left, and the "Condition" in the bottom right of the hero area to create a sophisticated editorial flow.
*   **Use Tonal Shifts:** Rely on the difference between `#ffffff` and `#f3f3f3` to define areas.

### Don't
*   **No Dividers:** Never use a horizontal line to separate forecast days. Use a `1.5rem (xl)` vertical gap instead.
*   **No High-Contrast Shadows:** Avoid anything that looks like a "Material 2" drop shadow. It breaks the ethereal feel.
*   **No Color Outside Tokens:** Avoid using blues for "Cold" or reds for "Hot." This system communicates temperature through numerical scale and typographic weight, not color signals.