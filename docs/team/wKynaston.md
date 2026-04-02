# Kynaston Wee Yu Qi - Project Portfolio Page

## Overview

---
We created FinTrackPro, a CLI-based individual BTO budget planner for university students planning to
apply for BTO.

It turns messy finances into a clear downpayment plan - showing how much the individual
needs to save and whether additional financing is required.

## Summary of Contributions

---

### Code Contributed for tP: [link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=wKynaston&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

---

### Enhancements Implemented

**1.Expense and ExpenseList(`Expense.java`, `ExpenseList.java`)**
- **What it does**: `Expense` represents a one-off monthly expense with attributes such as `name`, `amount`, `category`, 
  while `ExpenseList` manages a collection of these expenses and provides operations such as `add`, `delete`, `retrieve`, `sort`, and `getTotal()`.
- **Justification** : I designed and implemented `Expense` and `ExpenseList` as the foundational abstraction for handling one-off 
  expenses. This encapsulates all expense-related data and operations within a dedicated structure, improving modularity 
  and preventing higher-level classes from interacting with raw data collections.
- **Highlights** : The design of `ExpenseList` centralizes key behaviors such as maintaining totals and validating indices, 
  ensuring consistent handling of expense data across commands like `add`, `delete`, and `list`.
  This foundational design was later extended by a teammate with additional features such as `insertionOrder` and sorting 
  functionality (e.g., `sortByCategory()`, `sortByRecent())`, which build on the original structure without requiring major 
  refactoring. This demonstrates the extensibility of the initial design.
**2. RecurringExpense and RecurringExpenseList (`RecurringExpense.java`, `RecurringExpenseList.java`)**
- **What it does** : `RecurringExpens`e represents persistent expenses such as subscriptions or utility bills, while
  `RecurringExpenseList` manages these items separately and provides operations such as `add`, `delete`, `retrieve`, and `getTotal()`.
- **Justification** : Recurring expenses have a different lifecycle compared to one-off expenses, as they persist across 
  months rather than being cleared after each cycle. Separating them improves clarity and prevents mixing of temporary and persistent data.
- **Highlights** : Although `RecurringExpense` shares similar fields with `Expense`, it is intentionally separated to reflect 
  its persistent nature. `RecurringExpenseList` mirrors operations of `ExpenseList` but is not cleared during save, which 
  reinforces the distinction in lifecycle.
**3. `handleAdd()` and argument parsing(`CommandHandler.java`)**
- **What it does**: `handleAdd()` processes the add command by parsing user input, validating arguments, and inserting the 
  expense into either `ExpenseList` or `RecurringExpenseList`.
- **Justification** : The add command is a core interaction point, and separating parsing from execution ensures cleaner 
  structure and easier maintenance.
- **Highlights** : The parsing logic supports both:
  - `add <name> <amount> <category>`
  - `add <name> <amount> <category> recurring`
  It also supports multi-word names while correctly extracting arguments. The routing logic determines whether to call 
  `ExpenseList.add()` or `RecurringExpenseList.add()`.
**4.`parseAmount()` (`CommandHandler.java`)**
- **What it does**: `parseAmount()` validates and converts user input into a `BigDecimal`.
- **Justification** : Financial data requires precision and strict validation. Using `BigDecimal` avoids floating-point inaccuracies.
- **Hightlights** :The method enforces:
  - non-negative values
  - maximum of two decimal places
  This ensures all stored values are safe for calculations such as totals and summaries
**5.`handleDelete()`, `handleDeleteRecurring()`, and `parseDeleteIndex()` (`CommandHandler.java`)**
- **What it does** : `handleDelete()` removes entries from `ExpenseList`, while `handleDeleteRecurring()` removes entries from 
  `RecurringExpenseList`. `parseDeleteIndex()` validates the index input.
- **Justification** : Deletion must be safe and error-resistant. Separating one-off and recurring deletion avoids ambiguity.
- **Highlights** : `parseDeleteIndex()` ensures:
  - valid integer input
  - correct bounds
  Each handler retrieves totals using `getTotal()` before and after deletion, ensuring correctness and improving feedback.
**6.Listing and display logic (`FinTrackPro.java`, `Ui.java`)**
- **What it does** : Displays recurring expenses and one-off expenses in separate sections using the `list` command
- **Justification** : Separating these views improves readability and aligns with their different roles in budgeting.
- **Highlights** : The output clearly distinguishes:
  - recurring commitments
  - monthly expenses
  This reinforces the system’s internal separation of `ExpenseList` and `RecurringExpenseList`.
**7.Monthly rollover and archiving (`handleSaveMonth()`, `MonthlyArchive`)**
- **What it does** : The save command archives current expenses, clears one-off expenses, and advances the month while 
  preserving recurring expenses.
- **Justification** : This models a realistic monthly budgeting cycle, where recurring financial commitments persist across 
  months while one-off expenses are reset.
- **Highlights** : 
  - The base monthly rollover and archiving functionality was implemented by a teammate
  - I enhanced this feature by extending it to support recurring expenses during the rollover process
  - Recurring expenses are preserved across months, while one-off expenses are archived and cleared
  - Archive files distinguish entries using prefixes:
    - E for one-off expenses
    - R for recurring expenses
  This enhancement ensures consistency between runtime behavior and stored data, allowing both expense types to be handled 
  correctly during monthly transitions.

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