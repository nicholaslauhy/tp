# Kynaston Wee Yu Qi - Project Portfolio Page

## Overview

---
We created FinTrackPro, a CLI-based individual BTO budget planner for university students planning to
apply for BTO.

It turns messy finances into a clear downpayment plan - showing how much the individual
needs to save and whether additional financing is required.

## Summary of Contributions

**Code Contributed for tP**: [link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=wKynaston&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

---

### Enhancements Implemented

**1. Expense and ExpenseList (`Expense.java`, `ExpenseList.java`)**
- **What it does**: `Expense` models a one-off expense (`name`, `amount`, `category`), while `ExpenseList` manages a 
  collection with operations such as `add`, `delete`, `retrieve`, `sort`, and `getTotal()`.
- **Justification**: Serves as the core abstraction for one-off expenses, encapsulating data and behaviour to avoid direct 
  manipulation of raw collections.
- **Highlights**: Centralizes total calculation and index validation; later extended (e.g., sorting, `insertionOrder`) 
  without refactoring, demonstrating extensibility.

**2. RecurringExpense and RecurringExpenseList (`RecurringExpense.java`, `RecurringExpenseList.java`)**
- **What it does**: `RecurringExpense` represents persistent expenses, while `RecurringExpenseList` manages them with 
  `add`, `delete`, and `getTotal()`.
- **Justification**: Recurring expenses have a different lifecycle (persist across months), so separating them avoids 
  mixing temporary and persistent data.
- **Highlights**: Mirrors `ExpenseList` but is not cleared during monthly rollover, reinforcing lifecycle distinction.

**3. `handleAdd()` and argument parsing (`CommandHandler.java`)**
- **What it does**: Parses user input, validates arguments, and inserts into either `ExpenseList` or `RecurringExpenseList`.
- **Justification**: Separating parsing from execution improves clarity and maintainability for this core command.
- **Highlights**: Supports both standard and `recurring` formats, handles multi-word names, and routes correctly based on the flag.

**4. `parseAmount()` (`CommandHandler.java`)**
- **What it does**: Converts and validates input into `BigDecimal`.
- **Justification**: Financial data requires precision and strict validation.
- **Highlights**: Enforces non-negative values and up to two decimal places, preventing invalid calculations.

**5. `handleDelete()`, `handleDeleteRecurring()`, and `parseDeleteIndex()` (`CommandHandler.java`)**
- **What it does**: Handles deletion of one-off and recurring expenses with index validation.
- **Justification**: Ensures safe, consistent, and unambiguous deletion across both expense types.
- **Highlights**: Validates integer input and bounds; retrieves totals before and after deletion for correctness and user feedback.

**6. Listing and display logic (`FinTrackPro.java`, `Ui.java`)**
- **What it does**: Displays recurring and one-off expenses in separate sections via `list`.
- **Justification**: Improves readability and reflects their different roles in budgeting.
- **Highlights**: Clearly distinguishes recurring commitments from monthly expenses, aligning UI with internal data separation.

**7. Monthly rollover and archiving (`handleSaveMonth()`, `MonthlyArchive`)**
- **What it does**: Archives expenses, clears one-off entries, and advances the month while preserving recurring expenses.
- **Justification**: Models a realistic monthly budgeting cycle.
- **Highlights**: Core logic by teammate; extended to support recurring expenses; uses `E` (one-off) and `R` (recurring) 
  prefixes for consistent storage.

---

### Contributions to the UG

Wrote/updated the following sections:
- `add` — enhanced command to include optional `recurring` flag with updated examples
- `deleterecurring` — added new command documentation including format and expected behaviour
- `list` — improved clarity of output by distinguishing recurring and monthly expenses
- `save` — updated to reflect accurate monthly rollover logic (recurring retained, one-off cleared)
- `Data Storage` — refined documentation to include `E` and `R` prefixes, aligning storage format with system behaviour 

---

### Contributions to the DG

Wrote the following sections:
- **Managing Expenses** — overall class design describing the interaction between `FinTrackPro`, `CommandHandler`, `Ui`, 
  `ExpenseList`, and `RecurringExpenseList`, including the separation of one-off and recurring expenses
- **Add Expenses** — sequence diagram and explanation for handling both one-off and recurring expenses via a unified `add` 
  command flow, including argument parsing and conditional routing
- **Delete One-off Expenses** — sequence diagram and explanation for validation, deletion, and total update within `ExpenseList`
- **Delete Recurring Expenses** — sequence diagram and explanation for validation, deletion, and total update within `RecurringExpenseList`

UML diagrams created:
- Expense Management Class Diagram
- Add Expense Sequence Diagram
- Delete One-off Expense Sequence Diagram
- Delete Recurring Expense Sequence Diagram

Updated and refined explanations to ensure alignment between UML diagrams and actual implementation, particularly for:
- Separation of one-off vs recurring expense handling
- Command routing through `FinTrackPro` and `CommandHandler`
- Consistent use of `Ui` for user-facing output  
### Contributions to Team-Based Tasks

- Coordinated task allocation to ensure even contribution across the team, and kept everyone aligned on deadlines throughout 
  the project lifecycle
- Regularly reviewed and gave feedback for PRs
- Implemented core expense management through `Expense` and `ExpenseList`, forming the foundation for handling one-off expenses
- Designed and integrated recurring expense support via `RecurringExpense` and `RecurringExpenseList`, enabling separation 
  of persistent and one-off expenses
- Extended `CommandHandler` to support full expense workflows, including `handleAdd`, `handleDelete`, `handleDeleteRecurring`, 
  and `handleSaveMonth`, along with parsing logic (`parseAddArguments`, `parseAmount`, index validation)
- Enhanced the `add` command to support `add <name> <amount> <category> [recurring]`, including multi-word input parsing 
  and optional recurring flag handling
- Updated multiple components (`FinTrackPro`, `Ui`, `ExpenseList`, `RecurringExpenseList`, `MonthlyArchive`, storage classes)
  to ensure consistent integration of recurring expenses across command execution, display, and persistence
- Implemented `deleterecurring` for independent management of recurring expenses
- Updated monthly rollover (`handleSaveMonth`, `MonthlyArchive`) to preserve recurring expenses while clearing one-off expenses
- Contributed to testing by refining unit tests and command-based test inputs to cover parsing, expense handling, and edge cases
- Updated UML diagrams and documentation (UG/DG) to reflect the final system design and ensure alignment between implementation and documentation

---

### Contributions to Team-Based Tasks

- Coordinated task allocation to ensure even contribution across the team, and kept
  everyone aligned on deadlines throughout the project lifecycle
- Regularly reviewed and gave feedback for PRs

---

### Review/Mentoring Contributions

PRs reviewed (from most recent to least recent):

- [PR #121](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/121)
- [PR #114](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/114)
- [PR #113](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/113)
- [PR #109](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/109)
- [PR #84](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/84)
- [PR #78](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/78)
- [PR #72](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/72)
- [PR #64](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/64)
- [PR #16](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/16)
- [PR #9](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/9)
- [PR #7](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/7)
- [PR #4](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/4)

---

### Contributions Beyond the Project Team

NIL