# Doom Value Adjustment

Doom Value Adjustment is a RuneLite plugin that fixes the end-of-level loot valuation at Doom by treating **Sun-kissed bones** as **0 GP** instead of the incorrect 8,000 GP each.

Jagex currently assigns Sun-kissed bones (item ID `29378`) a value of 8,000 GP, which heavily inflates the total loot value shown in the Doom loot window. This plugin corrects that by subtracting the bogus bone value from the final displayed total.

![Doom loot value without Sun-kissed bones](https://github.com/user-attachments/assets/5fbcb518-1e17-45df-9331-3a231d951981)

---

## What it does

- **Detects** the Doom end-of-level UI (`DomEndLevelUi`, group ID `919`).
- **Reads** the official Jagex loot total from `DomEndLevelUi.LOOT_VALUE` (child ID `20`).
- **Finds** all Sun-kissed bones in `DomEndLevelUi.LOOT_CONTENTS` (child ID `19`), using:
  - Item ID: `29378`
  - Quantity: whatever is shown in the loot window.
- **Treats bones as 0 GP** by subtracting:
  \[
  \text{correction} = \text{boneQuantity} \times 8{,}000 \text{ GP}
  \]
  from the Jagex total.
- **Rewrites** the value text (e.g. `Value: 977,427 GP`) to the corrected amount, while leaving all other loot unchanged.

If there are no Sun-kissed bones present, the loot value is left untouched.

