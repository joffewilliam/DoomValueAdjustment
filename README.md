# Doom Value Adjustment

Doom Value Adjustment is a RuneLite plugin that lets you **adjust specific item values** in the Doom end-of-level loot window so the total better matches how you want to value certain drops.

It currently supports:

- **Sun-kissed bones**: leave the value as-is, or **infer** a value as **77% of dragon bones (GE)**.
- **Spirit seeds**: leave the value as-is, set to **0 GP**, or set to **Seed pack average (80,000 GP)**.

---

## What it does

- **Detects** the Doom end-of-level UI (`DomEndLevelUi`).
- **Reads** the displayed loot total from `DomEndLevelUi.LOOT_VALUE`.
- **Scans** the loot items in `DomEndLevelUi.LOOT_CONTENTS` and totals quantities for:
  - Sun-kissed bones (`ItemID.SUNKISSED_BONES`)
  - Spirit seed (`ItemID.SPIRIT_SEED`)
- **Computes an adjustment** based on your settings:
  - Sun-kissed bones: replace the current price with \(0.77 \times\) dragon bones GE price when “Inferred” is selected.
  - Spirit seeds: replace the current price with either 0 GP or 80,000 GP when selected.
- **Rewrites** the value text (e.g. `Value: 977,427 GP`) to the adjusted amount.

If none of the configured items are present (or all are set to Default), the loot value is left untouched.

---

## Settings

- **Sun-kissed bones value**
  - **Default**: no change
  - **Inferred**: 77% of dragon bones (GE)
- **Spirit seed value**
  - **Default**: no change
  - **0 GP**: treat spirit seeds as 0
  - **Seed pack average (80,000 GP)**: treat each spirit seed as 80,000

