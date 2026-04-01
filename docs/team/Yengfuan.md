# Tan Feng Yuan - Project Portfolio Page

## Project: FinTrackPro

FinTrackPro is a CLI-based individual BTO budget planner for university students planning to
apply for BTO. It turns messy finances into a clear downpayment plan, showing how much the
individual needs to save and whether additional financing is required.

---

## Summary of Contributions

**Code Contributed for tP:
** [View on tP Code Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=yengfuan&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Yengfuan&tabRepo=AY2526S2-CS2113-T14-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Enhancements Implemented

**1. Category System (`Category.java`, `FoodCategory.java`, `TransportCategory.java`, `EntertainmentCategory.java`,
`UtilitiesCategory.java`, `OtherCategory.java`)**

- **What it does:** Defines a hierarchy of expense categories using an abstract base class, where each concrete subclass
  declares its display name and a sort priority used for ordered list display.
- **Justification:** Using an abstract class instead of an enum allows new categories to be added without modifying any
  existing code, and cleanly supports the `Comparable<Category>` interface needed for `sort category` — something enums
  handle poorly when sort order must be decoupled from declaration order.
- **Highlights:** The `isValid` / `fromString` pairing gives callers a safe two-step validation flow: check first, then
  convert — preventing `IllegalArgumentException` from propagating into command handling. The `compareTo` implementation
  delegates entirely to `getSortOrder()`, keeping sort logic encapsulated within each category subclass.

**2. Summary Report Extraction (`SummaryReport.java`)**

- **What it does:** Encapsulates all BTO readiness metrics — distance to goal, monthly surplus, percentage progress,
  estimated months, and readiness level — computing and storing them all as `public final` fields at construction time
  from a `Profile`, `ExpenseList`, and `RecurringExpenseList` snapshot.
- **Justification:** The report aggregates a large number of derived values across multiple data sources. Computing
  these inline inside `CommandHandler` would bloat the handler with business logic, and passing them individually into
  `Ui` would require an unwieldy method signature. Extracting into its own class keeps each layer focused:
  `SummaryReport` owns the computation, `Ui` owns the display, and `CommandHandler` owns neither.
- **Highlights:** Because all fields are computed once at construction and stored as immutable finals, the report is a
  clean snapshot that can be passed freely to `Ui` without risk of mutation mid-render. The self-contained design also
  made it straightforward for teammates to extend the report with new fields like `monthlyRequired` without touching
  display or command-handling code.

**3. Parser (`Parser.java`)**

- **What it does:** Extracts the command keyword from raw user input via `parseCommand`, and safely converts an index
  string to an integer via `parseIndex`, returning `-1` for any non-integer input rather than throwing.
- **Justification:** Without a dedicated Parser class, every command handler would need its own string-splitting logic,
  leading to duplicated code scattered across the codebase. Pulling this into one place means any fix or change to how
  input is parsed only needs to happen once.
- **Highlights:** `parseIndex` deliberately returns a sentinel value of `-1` on invalid input rather than throwing,
  allowing callers to route the failure through their own domain-specific exception (`InvalidIndexException`) with a
  user-facing message appropriate to their context.

**4. help Command (`Ui.java — showHelpMessage`)**

- **What it does**: Prints a formatted list of all supported commands and their usage, grouped into General Commands, Daily
Transaction Commands, and Profile & Goal Management sections.
- **Justification**: Without a help command, users would have no way to discover what the app can do after launching it,
making the CLI inaccessible to anyone unfamiliar with the codebase.
- **Highlights**: Keeping showHelpMessage inside Ui rather than CommandHandler ensures that display formatting stays in the
presentation layer. As teammates added new commands, the grouped structure made it easy to slot new entries into the
right section without restructuring the whole output.

**5. `sort category`, `sort recent`, and `handleSort` (`ExpenseList.java`, `CommandHandler.java`)**

- **What it does:** `sort category` reorders the expense list by each category's defined sort priority; `sort recent`
  restores the original insertion order. `handleSort` dispatches between these and `sort name` based on the argument
  token.
- **Justification:** Users need multiple views of their expense list depending on context — reviewing by type, reviewing
  chronologically, or scanning alphabetically. Without these commands, the list order is fixed at insertion and provides
  no analytical flexibility.
- **Highlights:** `sortByCategory` uses `Comparator.comparing(Expense::getCategory)`, which delegates to
  `Category.compareTo` — meaning sort order is fully controlled by the category hierarchy rather than hardcoded in the
  list class. `sortByRecent` restores order using an `insertionOrder` field stamped on each `Expense` at creation time,
  ensuring the original sequence survives any number of intermediate sorts. `handleSort` applies `arg.toLowerCase()`
  before the switch so the command is case-insensitive.

**6. Tests (`CategoryTest.java`, `CommandHandlerTest.java`, `SummaryReportTest.java`)**

- **What it does:** `CategoryTest` verifies `fromString` for all five categories including case-insensitive matching,
  `isValid` for valid and invalid inputs, `compareTo` sort ordering, and that an unknown string throws
  `IllegalArgumentException`. The `CommandHandlerTest` cases for `parseAmount` cover empty input, non-numeric strings,
  negative values, and values exceeding two decimal places; the `parseDeleteIndex` cases cover empty input, decimal
  indices, out-of-bounds indices, zero, and non-numeric strings. `SummaryReportTest` verifies correctness of computed
  metrics across edge cases including a zero BTO goal, non-positive surplus, and a goal that has already been reached.
- **Justification:** These are the most critical parsing and computation paths in the application — every expense add
  and delete flows through the parsers, and every `summary` call flows through `SummaryReport`. Comprehensive tests here
  catch regressions early and document the exact boundary conditions each component is expected to enforce.
- **Highlights:** The `parseDeleteIndex` tests call `handleAdd` as setup rather than directly manipulating the list,
  keeping the happy-path test realistic end-to-end while isolating each individual rejection case. The
  `SummaryReportTest` constructs `Profile` and `ExpenseList` objects directly, confirming that `SummaryReport` can be
  instantiated and verified entirely in isolation from the command loop.

---

### Contributions to the UG

Wrote the following sections:
- `sort` — command description, format, and example
- `help` — command description, format, and example

---

### Contributions to the DG

Wrote the following sections:
- **Category Component** — full description of the abstract class hierarchy, the
  `isValid` / `fromString` two-step validation design, and the `compareTo` sort
  order delegation decision
- **Sort Commands** — description of `sortByName`, `sortByCategory`, and `sortByRecent`
  responsibilities, the `insertionOrder` stamping design decision for `sortByRecent`,
  and how `handleSort` dispatches between all three via case-insensitive argument matching

UML diagrams created:
- Category Class Diagram
- Sort Command Sequence Diagram

Updated the Table of Contents to reflect all teammates' sections, and resolved formatting
inconsistencies across the DG (heading levels, anchor links, placeholder text removal).

---

### Contributions to Team-Based Tasks

- Maintained and iteratively corrected the Developer Guide across multiple review cycles
- Coordinated task allocation to ensure even contribution across the team, and kept
  everyone aligned on deadlines throughout the project lifecycle
- Designed and implemented the `Category` abstract class hierarchy to support
  extensible expense classification, and shared the `isValid` / `fromString`
  pattern with teammates as the standard approach for validated user input parsing
- Extracted `SummaryReport` into a self-contained class early on to keep business
  logic out of `Ui` and `CommandHandler`, making it easier for teammates to add
  new metrics without touching display or command-handling code
- Wrote JUnit tests for `CategoryTest`, `CommandHandlerTest` (parseAmount and
  parseDeleteIndex cases), and `SummaryReportTest`, and implemented `handleSort`
  with `sortByName`, `sortByCategory`, and `sortByRecent` to give users full
  control over how their expense list is displayed
- Regularly reviewed and gave feedback for PRs

---

### Review/Mentoring Contributions

PRs reviewed (from most recent to least recent):

- [PR #148](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/148)
- [PR #137](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/137)
- [PR #133](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/133)
- [PR #130](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/130)
- [PR #129](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/129)
- [PR #118](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/118)
- [PR #105](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/105)
- [PR #104](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/104)
- [PR #102](https://github.com/AY2526S2-CS2113-T14-2/tp/pull/102)

---

### Contributions Beyond the Project Team

NIL
