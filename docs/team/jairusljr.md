# Jairus Leung Jie Rui - Project Portfolio Page

## Project: FinTrackPro

FinTrackPro is a CLI-based individual BTO budget planner for university students planning to
apply for BTO. It turns messy finances into a clear downpayment plan, showing how much the
individual needs to save and whether additional financing is required.

---

## Summary of Contributions

**Code Contributed for tP:** [View on tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=jairusljr&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

---

### Enhancements Implemented

**1. Storage Component (`Storage.java`)**
- **What it does:** Persists all user data to a pipe-delimited flat file (`fintrack.txt`)
  and reloads it on startup, with auto-save after every command cycle.
- **Justification:** Without persistence, all financial data would be lost on every exit,
  making the app unusable as a long-term planning tool.
- **Highlights:** Required defensive per-line `try-catch` for corrupted data, and a
  backward-compatibility check for older save files missing the `currentMonth` field.

**2. BTO Summary Report (`summary` command)**
- **What it does:** Generates a real-time BTO readiness report showing surplus, distance
  to goal, estimated months, and a readiness level (`BARELY STARTED` → `READY`).
- **Justification:** Without a summary, users have no way to gauge whether their spending
  habits allow them to meet their BTO deadline.
- **Highlights:** All metrics are computed at instantiation inside `SummaryReport`,
  keeping `Ui` entirely free of business logic.

**3. `clear` and `reset` Commands**
- **What it does:** `clear` wipes the current month's one-off expenses with confirmation,
  preserving recurring expenses. `reset` wipes all data and triggers fresh setup on next launch.
- **Justification:** Without confirmation prompts, accidental data loss would be
  unrecoverable given the auto-save mechanism.
- **Highlights:** `reset` writes the empty state to disk immediately so the wipe
  persists even if the app closes before the next auto-save cycle.

**4. `list` Command**
- **What it does:** Prints all current month expenses with their name, amount, and
  category, along with the running total expenditure.
- **Justification:** Users need a quick way to review what they have spent in the
  current month to make informed decisions about their remaining budget.
- **Highlights:** Laid the foundation for the list display which was later extended
  by a teammate to support multi-month archive viewing across previous months.

**5. `sort name`**
- **What it does:** Sorts the expense list alphabetically by name, case-insensitively.
- **Justification:** Complements `sort category` and `sort recent` to give users
  full control over how they view their expenses.
- **Highlights:** Uses a stable comparator on `getName().toLowerCase()` so expenses
  differing only in case retain their relative insertion order.

---

### Contributions to the UG

- Created the initial section split in v1.0 to divide UG responsibilities clearly among
  team members
- Wrote the following sections: `summary`, `storage`, `clear`, `reset`
- Reviewed the entire UG to ensure consistent flow and tone, correcting formatting and
  wording issues across all sections

---

### Contributions to the DG

Created the backbone of the DG, which included:
- Introduction and overview
- Acknowledgements section
- Non-Functional Requirements section
- Full Product Scope section including target user profile, value proposition, and all
  user stories

Wrote the following sections:
- **Storage Component** — class diagram, object diagram, load/save sequence diagrams,
  auto-save policy with code snippet, data persistence format table, and Design Considerations
- **BTO Summary Report** — calculation logic table, Readiness Level Classification table,
  and Interaction Flow

UML diagrams created:
- Storage Class Diagram
- Storage Load Sequence Diagram
- Storage Save Sequence Diagram
- Storage Object Diagram 

Updated the Table of Contents to reflect all teammates' sections, and resolved formatting
inconsistencies across the DG (heading levels, anchor links, placeholder text removal).

---

### Contributions to Team-Based Tasks
- Added `.log` and `fintrack.txt` to `.gitignore` to prevent merge conflicts caused by
  file locking and keep personal test data out of the shared repo
- Updated `runtest.sh`, `runtest.bat`, and `EXPECTED.TXT` across multiple cycles to
  keep the test pipeline passing
- Maintained and iteratively corrected the Developer Guide across multiple review cycles
- Set up the `Storage` class early on to ensure easy testing for all teammates, and
  shared a guide on how to add new fields to the data persistence format
- Coordinated task allocation to ensure even contribution across the team, and kept
  everyone aligned on deadlines throughout the project lifecycle
- Wrote JUnit tests for `CommandHandler`, `ExpenseList`, `Storage`, and `SummaryReport`,
  and enabled assertions in `build.gradle` for runtime invariant checking
---

### Review/Mentoring Contributions

PRs reviewed (from most recent to least recent):
- [PR #167](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/167)
- [PR #147](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/147)
- [PR #126](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/126)
- [PR #117](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/117)
- [PR #116](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/116)
- [PR #107](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/107)

---

### Contributions Beyond the Project Team
NIL

