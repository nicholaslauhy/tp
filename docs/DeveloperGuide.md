# 1. Developer Guide
This Developer Guide will showcase the design and implementation of FinTrackPro, a CLI-based financial
planning application for BTO downpayment goal tracking with the use of UML diagrams and code snippets.

It will also include instructions for manual testing of the product, along with a comprehensive list of 
test cases to ensure the robustness and reliability of the application. 

The guide is structured to provide a clear understanding of the architecture, design decisions, and testing 
strategies employed in the development of FinTrackPro.

## Table of Contents
* **2. [Acknowledgements](#2-acknowledgements)**
  * **2.1 [Frameworks and base code](#21-frameworks-and-base-code)**
  * **2.2 [Third-party libraries](#22-third-party-libraries)**
  * **2.3 [Educational resources](#23-educational-resources)**
* **3. [Design & Implementation](#3-design--implementation)**
  * **3.1 [Architecture Diagram](#31-architecture-diagram)**
  * **3.2 [UML Diagrams](#32-uml-diagrams)**
    * **3.2.1 [Managing Profile](#managing-profile)**
* **4. [Product Scope](#4-product-scope)**
  * **4.1 [Target user profile](#41-target-user-profile)**
  * **4.2 [Value proposition](#42-value-proposition)**
  * **4.3 [User stories](#43-user-stories)**
* **5. [Non-Functional Requirements](#5-non-functional-requirements)**
  * **5.1 [Performance and scalability](#51-performance-and-scalability)**
  * **5.2 [Data integrity](#52-data-integrity)**
  * **5.3 [Security and privacy](#53-security-and-privacy)**
  * **5.4 [Usability](#54-usability)**
  * **5.5 [Environment](#55-environment)**
* **6. [Glossary](#6-glossary)**
* **7. [Instructions For Manual Testing](#7-instructions-for-manual-testing)**
  * **7.1 [Test Cases](#71-test-cases)**

# 2. Acknowledgements

## 2.1 Frameworks and base code
* **[AddressBook-Level 3 (AB3)](https://se-education.org/addressbook-level3/)**: FinTrack Pro's architectural patterns and initial codebase were adapted from the AddressBook-Level 3 project created by the **[SE-EDU initiative](https://se-education.org/)**.
* **[JavaFX](https://openjfx.io/)**: Used for the Graphical User Interface (GUI) components of the application.

## 2.2 Third-party libraries
* **[JUnit 5](https://junit.org/junit5/)**: Utilized for unit testing and ensuring code reliability.
* **[Jackson](https://github.com/FasterXML/jackson)**: Used for JSON parsing and data persistence in the storage component.
* **[PlantUML](https://plantuml.com/)**: Used for generating all UML diagrams (Sequence, Class, and Object diagrams) within this documentation.

## 2.3 Educational resources
* **[SE-EDU Guides](https://se-education.org/guides/)**: We referred to the various guides provided by the SE-EDU team for best practices in software documentation and testing.
* **[CS2113 Module Website](https://nus-cs2113-ay2526s2.github.io/website/)**: Special thanks to the professors and tutors of CS2113 for their guidance and feedback throughout the development lifecycle of v1.0 to v2.1.

# 3 Design & Implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}
## 3.1 Architecture Diagram

### Overview
![Architecture Diagram](diagram/Architecture-Diagram.jpg)

The architecture is organized into layered components that interact with each other to deliver the application's core functionality: expense tracking, savings planning, and BTO downpayment goal management.

### Main Components

The app consists of the following main components:

1. **Main (FinTrackPro)**: Responsible for app launch and shutdown.
    - Initializes all other components in the correct sequence at startup.
    - Connects components together and establishes their dependencies.
    - Invokes cleanup methods during shutdown.

2. **UI**: The user interface layer of the app.
    - Displays messages and prompts to the user.
    - Handles all command-line interaction and output formatting.

3. **Logic**: The command execution engine.
    - **CommandHandler**: Receives user commands, parses them, and executes the corresponding business logic.
    - **Parser**: Receives and validates user input to extract command type and arguments.
    - Coordinates between UI, Data, and Storage components to fulfill user requests.

4. **Data**: Holds the data of the app in memory.
    - **Profile**: Manages user financial information (name, salary, savings, BTO goal, deadline).
    - **ExpenseList**: Manages the current month's regular expenses.
    - **Expense**: Represents a single expense entry with amount, category, and timestamp.
    - **RecurringExpenseList**: Manages monthly recurring expenses (e.g., subscriptions).
    - **RecurringExpense**: Represents a recurring expense that occurs every month.
    - **MonthlyArchive**: Manages archived monthly data when advancing to a new month.
    - **ArchivedExpense**: Represents a historical expense record from a previous month.

5. **Storage**: Reads data from and writes data to the hard disk.
    - Persists the Profile, ExpenseList, RecurringExpenseList, and MonthlyArchive to a local file.
    - Loads previously saved data at app startup for data continuity.
    - Ensures no data loss through auto-save functionality after state-changing commands.

6. **Categories**: A polymorphic system for expense classification.
    - **Category**: Abstract base class defining the categorization interface.
    - **FoodCategory, TransportCategory, EntertainmentCategory, UtilitiesCategory, OtherCategory**: Concrete category implementations.
    - Enables type-safe expense categorization and sorting by priority.

7. **Utilities**: A collection of helper classes used by multiple components.
    - **BtoCalculator**: Performs financial calculations for BTO goal planning (savings needed, progress tracking).
    - **InputUtil**: Validates and parses user input (amounts, dates, categories).
    - **LoggerUtil**: Provides centralized logging functionality for debugging and monitoring.

### How the Architecture Components Interact with Each Other

The sequence of interactions in a typical use case follows this pattern:

1. **App Initialization**: FinTrackPro initializes all components (Ui, Storage, Profile, ExpenseList, etc.) and loads persisted data from disk via Storage.

2. **User Input**: The user enters a command via the CLI. Ui captures this input as a string.

3. **Command Parsing and Execution**:
    - CommandHandler receives the input string.
    - Parser receives and validates the input to identify the command type and arguments.
    - CommandHandler interprets the parsed command and determines which Data components to interact with.

4. **Logic Execution**:
    - Depending on the command, Logic may:
        - **Add/Delete/Edit**: Modifies Data (ExpenseList, Profile, RecurringExpenseList) in memory.
        - **Validate**: Uses InputUtil and Category to ensure data correctness.
        - **Calculate**: Uses BtoCalculator to compute financial metrics.
        - **Archive**: Moves ExpenseList data to MonthlyArchive and resets for the new month.

5. **Data Persistence**: After a state-changing command, Storage automatically saves the updated Data (Profile, ExpenseList, RecurringExpenseList, MonthlyArchive) to the hard disk.

6. **Display Results**: Ui formats and displays the results or status message to the user via the CLI.

7. **App Shutdown**: Upon user exit, FinTrackPro invokes cleanup methods and shuts down all components gracefully.

This layered architecture promotes separation of concerns, making the codebase maintainable, testable, and scalable. 
The sections below give more concrete details about each component.

## 3.2 UML Diagrams
In this section, we will present the UML class diagrams, object diagrams and sequence diagrams to 
illustrate how each main component of FinTrackPro integrates with the rest of the codebase.
### Managing Expenses 
#### Class Diagram
![Class Diagram](diagram/Expense-ClassDiagram.png)
Tha above class diagram illustrates the overall design of the expense management system and the classes that directly participate 
in handling user commands and managing expense data.

FinTrackPro serves as the main controller of the application. It coordinates bewteen the user interface(Ui), the command
processing component (CommandHandler), and the data storage structures (ExpenseList and RecurringExpenseList). It receives
user input and delegates command execution accordingly

CommandHandler is responsible for parsing and executing user commands such as adding, deleting, and sorting expenses. It 
interacts with both ExpenseList and RecurringExpenseList to modify or retrieve data, and communicates results back to the user through the Ui.

ExpenseList manages all one-off expenses. It stores multiple Expense objects and provides operations such as adding, deleting, 
sorting, and calculating the total expenditure. Each Expense represents a single transaction with attributes such as name, 
amount, category, and insertion order.

RecurringExpenseList manages recurring expenses separately from one-off expenses. It stores multiple RecurringExpense objects 
and provides similar operations such as adding, deleting, and computing total recurring expenditure.

Both ExpenseList and RecurringExpenseList act as data source classes. They encapsulate collections of their respective expense 
types and expose methods for manipulation and retrieval. The separation ensures that recurring and one-off expenses are 
handled independently while still allowing the system to aggregate and display them together when needed.

#### Sequence Diagram
![Sequence Diagram](diagram/AddExpense-SequenceDiagram.png)
The above sequence diagram illustrates how the system handles the add command for both one-off and recurring expenses.

The interaction begins when the User enters an add command. This input is received by FinTrackPro, which invokes handleCommand 
to determine the type of command entered. FinTrackPro then delegates the request to CommandHandler by calling handleAdd.

Within CommandHandler, the user input is parsed by parseAddArguments, which extracts the expense name, amount, category, 
and whether the recurring keyword is present.

If the expense is a one-off expense, CommandHandler interacts with ExpenseList. It first retrieves the old total, then 
adds the new expense into the list, retrieves the newly added Expense, and finally gets the updated total. The result is 
then displayed to the user through the Ui.

If the expense is marked as recurring, CommandHandler instead creates a RecurringExpense and adds it into RecurringExpenseList. 
It then retrieves the updated recurring total and displays the result through the Ui.

This sequence diagram shows that the system uses the same command entry point for both types of expenses, but routes them 
to different data structures depending on whether the recurring flag is present.

![Sequence Diagram](diagram/DeleteOneExpense-SequenceDiagram.png)
The above sequence diagram illustrates how the system handles the deletion of a one-off expense.

The interaction begins when the User enters the delete command. FinTrackPro receives the input and invokes handleCommand, 
which identifies the command and delegates execution to CommandHandler through handleDelete.

Within CommandHandler, the input index is first validated using parseDeleteIndex. Once the index is confirmed to be valid, 
CommandHandler retrieves the current total expenditure from ExpenseList, deletes the selected Expense, and then retrieves 
the updated total.

Finally, CommandHandler uses the Ui to display confirmation that the expense has been deleted together with the updated 
total expenditure.

![Sequence Diagram](diagram/DeleteRecurringExpense-SequenceDiagram.png)
The above sequence diagram illustrates how the system handles the deletion of a recurring expense.

The interaction begins when the User enters the deleterecurring command. FinTrackPro receives the input and invokes 
handleCommand, which dispatches the request to CommandHandler through handleDeleteRecurring.

CommandHandler first checks whether the given index is valid against RecurringExpenseList. If valid, it retrieves the old 
recurring total, removes the selected RecurringExpense, and then retrieves the updated recurring total.

Finally, the result is shown to the user through the Ui, confirming the deletion of the recurring expense and displaying 
the updated recurring total.
### Category component
![Class Diagram](diagram/Category-UML-Diagram.png)

### Archive Expenses 
#### Class Diagram
![Class Diagram](diagram/ArchiveExpense-ClassDiagram.jpg)
The above class diagram illustrates the design of the month archiving feature and the classes that directly participate
in it. 

CommandHandler triggers the archival through the `save` command, while FinTrackPro retrieves archived records during `list` display.
Both classes depend on MonthlyArchive as the archive service class. 

MonthlyArchive is responsible for persisting and loading monthly archive data, and it produces ArchivedExpense objects when archived
entries are read back from storage. 

For data sources, MonthlyArchive reads from both ExpenseList and RecurringExpenseList, accessing individual Expense and RecurringExpense
items. All archived entries are represented uniformly as ArchivedExpense, so listing logic can display historical records through a consistent
format.

#### Sequence Diagram
![Sequence Diagram](diagram/ArchiveExpense-SequenceDiagram.jpg)
The ArchiveExpense sequence diagram illustrates two runtime flows: saving a month and displaying archived history. 

In the save flow, the user enters the `save` command, and CommandHandler creates and uses MonthlyArchive, and invokes monthly save logic. MonthlyArchive
iterates through current one-time and recurring expense collections, then writes archive rows into the month's archive file (MonthN).

After successful persistence, control returns to CommandHandler, which continue month advancement and post-save updates. 

In the list flow, the user enters `list`, and FinTrackPro requests archive data from MonthlyArchive for previous months. MonthlyArchive
reads each MonthN file and reconstructs rows as List<ArchivedExpense>. FinTrackPro then iterates through these archived entries (name, amount, category)
to print month-grouped history and totals.

### Managing Profile

![Class Diagram](diagram/Profile-ClassDiagram.png)

The `Profile` class is the central data object for the user's financial identity. It stores the name,
monthly allowance, current savings, BTO goal, contribution ratio, and target deadline. All other
components — `CommandHandler`, `SummaryReport`, `BtoCalculator` — read from or write to it by reference.

#### BtoCalculator

`BtoCalculator` encodes the HDB downpayment rules as a reusable calculation. Given a house price
and contribution ratio, it computes two values at construction time:

| Field              | Formula                                                        |
|--------------------|----------------------------------------------------------------|
| `totalDownpayment` | `housePrice × 0.025` (2.5% HDB downpayment) + 10% legal fees |
| `yourShare`        | `totalDownpayment × contributionRatio`                         |

Both values are rounded to 2 decimal places (HALF_UP). `yourShare` is stored in `Profile` as the
user's `btoGoal` and is the target all savings progress is measured against. Calling
`setContributionRatio()` on a `Profile` that already has a `housePrice` will silently re-run this
calculation and update `btoGoal` in place.

#### Initial Setup Flow

When the application detects that no BTO goal has been set (i.e. the user is new), `FinTrackPro`
runs `performInitialSetup()` before entering the command loop. The steps are:

1. The user is prompted for their **name**.
2. The user provides their **current savings** and **monthly allowance** (validated by `InputUtil.readMoney()`).
3. The user provides the **total house price** and their **contribution ratio** (0.0–1.0, validated by `InputUtil.readRatio()`).
4. `BtoCalculator` computes the **total downpayment** (2.5% of house price + 10% legal fees) and the
   user's **personal share** (total downpayment × ratio). This value is stored in `Profile` as `btoGoal`.
5. The user provides a **future deadline** (ISO `YYYY-MM-DD`, validated by `InputUtil.readFutureDate()`).
6. The app displays the time remaining and the required monthly savings rate needed to meet the goal.

On subsequent launches, the saved profile is loaded from `fintrack.txt` and setup is skipped.

![Sequence Diagram](diagram/ProfileSetup-SequenceDiagram.png)

#### Runtime Profile Updates

Three commands allow the user to update their profile after initial setup:

| Command     | Handler             | Effect                                                                                          |
|-------------|---------------------|-------------------------------------------------------------------------------------------------|
| `allowance` | `handleAllowance()` | Replaces the stored monthly allowance with the new value entered by the user.                   |
| `savings`   | `handleSavings()`   | Adds the entered amount to the current savings total (deposit, not replacement).                |
| `ratio`     | `handleRatio()`     | Replaces the contribution ratio; automatically recalculates `btoGoal` if `housePrice` is set.  |

All three handlers use `InputUtil.readMoney()` or `InputUtil.readRatio()` to enforce valid input,
re-prompting the user on invalid entries rather than throwing an exception to the caller.

![Sequence Diagram](diagram/ProfileRatio-SequenceDiagram.png)

#### BTO Summary Report (`summary` command)

`handleSummary()` instantiates a `SummaryReport` from the current `Profile`, `ExpenseList`, and
`RecurringExpenseList`. The report derives the following metrics at construction time:

| Metric                   | Formula                                                                            |
|--------------------------|------------------------------------------------------------------------------------|
| Distance to goal         | `btoGoal − currentSavings`                                                         |
| Monthly surplus          | `monthlyAllowance − (oneOffExpenses + recurringExpenses)`                          |
| Percentage progress      | `(currentSavings / btoGoal) × 100`, capped at 100%                                |
| Estimated months         | `distanceToGoal ÷ monthlySurplus` (only shown when surplus > 0 and goal not met)  |
| Monthly required savings | `distanceToGoal ÷ monthsUntilDeadline`                                             |
| Readiness level          | Mapped from percentage: ≥100% → Ready, ≥75% → Secure, ≥50% → On Track, ≥25% → Making Progress, else → Barely Started |

The computed report is passed to `Ui.showSummaryReport()` for display. No data in `Profile` or
`ExpenseList` is mutated by this command.

# 4 Product Scope

## 4.1 Target user profile
FinTrack Pro was created for individual students in a relationship who are planning to set aside finances for their share of a BTO downpayment.

## 4.2 Value proposition
An individual BTO budget planner for university students planning to apply for BTO that turns messy finances into a clear downpayment plan - showing how much the individual needs to save and whether additional financing is required.


## 4.3 User Stories

| Version | As a ...       | I want to ...                        | So that I can ...                                                                 |
|---------|----------------|--------------------------------------|-----------------------------------------------------------------------------------|
| v1.0    | New User       | Add regular expenses                 | Track all costs that might slow down my progress toward my goals                  |
| v1.0    | New User       | Add my salary                        | Visualize my spending proportions relative to my total income                     |
| v1.0    | New User       | Add my savings                       | Track my current liquid assets compared to my target goal                         |
| v1.0    | New User       | Add ratio of downpayment             | Establish a concrete savings goal to pay my share of my future home               |
| v1.0    | New User       | Add downpayment price                | Adjust the downpayment to its actual value, since BTO flats are priced differently|
| v1.0    | New User       | Delete entries                       | Update my list to only show relevant expenditures                                 |
| v1.0    | New User       | Edit entries                         | Correct input errors or update my salary to reflect my current financial status   |
| v1.0    | New User       | View a financial summary             | Visualize my progress toward the downpayment goal in one glance                   |
| v2.0    | New User       | Categorize expenses                  | Identify which spending categories occupy the largest portion of my budget        |
| v1.0    | New User       | View "Distance to Goal" metrics      | Stay motivated by seeing exactly how close I am to reaching my downpayment target |
| v1.0    | New User       | View salary and savings              | Know how much I am saving relative to my salary                                   |
| v1.0    | New User       | Have a help command                  | Easily use the app's commands                                                     |
| v2.0    | Regular User   | Add recurring monthly expenses       | Never be blindsided by hidden or automated costs that occur every month           |
| v2.0    | Regular User   | Add comments to expenses             | Provide context for specific spending habits to better understand them later      |
| v1.0    | Regular User   | Set a specific target date           | Know the monthly savings rate needed to meet my deadline                          |
| v1.0    | Regular User   | Sort expenditure from highest to lowest | Know which expenditures are hindering me from reaching my downpayment goal     |
| v2.0    | Long Term User | Archive financial phases monthly     | Keep my dashboard uncluttered while preserving historical data                    |
| v2.0    | Long Term User | Assign a financial readiness level   | Know how ready I am to pay off my share of the downpayment                        |
| v1.0    | Long Term User | Have a local database                | View all past inputs and historical data                                          |


# 5 Non-Functional Requirements

##  5.1 Performance and scalability
* Response Time: Any command should return a result within 200 milliseconds under normal operating conditions.
* Capacity: The system should be able to handle up to 1,000 unique expenditure entries and 5 years of archived monthly data without any perceptible degradation in performance or lag in CLI responsiveness.

## 5.2 Data integrity
* Auto-save: To prevent loss of critical financial planning data, the application must automatically save the state of the budget and expenses to the local database after every valid state-changing command. 
* Fault Tolerance: The application should not crash or corrupt the local database if the user inputs malformed data. Instead it should provide a clear, non-technical error message and maintain the previous valid state.

## 5.3 Security and privacy
* Local Storage Only: Since the application handles sensitive information like salary, savings, and BTO targets, all data must be stored locally on the user's device. No financial data should be transmitted over a network or stored in the cloud.

## 5.4 Usability
* CLI Efficiency: The system is designed for "Power Users." An experienced user (typing at 50+ WPM) should be able to input a new expense and view their updated "Distance to Goal" faster than they could using a standard spreadsheet or mobile banking app.
* Documentation: A user who has never used a CLI should be able to perform basic tasks (adding salary/savings) within 5 minutes of reading the help command or the User Guide.

## 5.5 Environment
* Platform Independence: The application must be cross-platform, functioning identically on Windows, macOS, and Linux distributions, provided the system has Java 17 installed.
* Zero Installation: The product should be delivered as a single, executable JAR file that requires no complex installation process or external database setup. 

# 6 Glossary

* *BTO (Build-To-Order)* - a subsidized public housing option scheme in Singapore where new flats are constructed only after a sufficient number of units (typically 65-70%) have been pre-booked by applicants
* *Contribution ratio* - the user's fractional share of the downpayment (0.0 to 1.0)
* *Recurring expenses* - expenses that occur on a monthly basis following the same rates (eg Netflix subscription of $5.98/month), then you can run `addrecurring Netflix Subscription 5.98 entertainment recurring`
* *Adjusted Minimum Savings* - minimum amount you need to save per month given your distance to goal and number of months remaining till the deadline 
* *Estimated Goal Achievement* - number of months you need to take to achieve your goal, given the current month's savings and distance to goal

# 7 Instructions for Manual Testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}

## 7.1 Test cases

### Managing expenses
(to be added by Kynaston)
1. **Adding a one-off expense**
   1. Prerequisites: Application started with a valid profile setup.
   2. Test case: `add lunch 5 FOOD` Expected: Expense added to the one-off expense list. Total expenditure updated.
   3. Test case: `add grab home 20` TRANSPORT Expected: Multi-word expense name added successfully under TRANSPORT.
   4. Test case: `add` Expected: Error shown for missing arguments. No expense added.
   5. Test case: `add lunch -1 FOOD` Expected: Error shown for negative amount. No expense added.
   6. Test case: `add lunch abc FOOD` Expected: Error shown for invalid amount format. No expense added.
   7. Test case: `add lunch 5 HELLO` Expected: Error shown for invalid category. No expense added.
2. **Deleting a one-off expense**
   1. Prerequisites: At least one expense exists in the list.
   2. Test case: `delete 1` Expected: First expense removed. Total expenditure updated.
   3. Test case: `delete 0` Expected: Error shown for invalid index. List unchanged.
   4. Test case: `delete 99` Expected: Error shown for out-of-range index. List unchanged.
   5. Test case: `delete abc` Expected: Error shown for invalid index format. List unchanged.
3. **Adding a Recurring expense**
   1. Prerequisites: Application started with a valid profile setup.
   2. Test case: `add netflix 30 ENTERTAINMENT recurring` Expected: Recurring expense added to recurring expense list
   3. Test case: `add phone bill 20 UTILITIES RECURRING` Expected: Recurring expense added successfully (case-insensitive keyword).
   4. Test case: `add netflix -1 ENTERTAINMENT recurring` Expected: Error shown for invalid amount. No recurring expense added.
   5. Test case: `add netflix 30 HELLO recurring` Expected: Error shown for invalid category. No recurring expense added.
4. **Deleting a recurring Expense**
   1. Prerequisites: At least one recurring expense exists.
   2. Test case: `deleterecurring 1` Expected: First recurring expense removed.
   3. Test case: `deleterecurring 0` Expected: Error shown for invalid index. List unchanged.
   4. Test case: `deleterecurring 99` Expected: Error shown for out-of-range index. List unchanged.
   5. Test case: `deleterecurring abc` Expected: Error shown for invalid index format. List unchanged.
### Sorting expenses

1. **Sorting by category**
    1. Prerequisites: Multiple expenses of different categories in the list.
    2. Test case: `sort category` Expected: Expenses reordered in category priority: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER.
    3. Test case: `sort recent` Expected: Expenses reordered back to insertion order.
    4. Test case: `sort foo` Expected: List order unchanged. Error shown for unrecognized argument.
    5. Other incorrect sort commands to try: `sort` (empty argument) Expected: Similar to previous.

---

### Managing profile

1. **Updating monthly allowance**
   1. Prerequisites: Application started and profile initialised.
   2. Test case: Enter `allowance`, then enter `2000`. Expected: Allowance updated to $2,000.00 and confirmed.
   3. Test case: Enter `allowance`, then enter `0`. Expected: Allowance set to $0.00 with no error.
   4. Test case: Enter `allowance`, then enter `-100`. Expected: Error shown for negative value, user re-prompted.
   5. Test case: Enter `allowance`, then enter `abc`. Expected: Error shown for non-numeric input, user re-prompted.
   6. Test case: Enter `allowance`, then enter `100.123`. Expected: Error shown for more than 2 decimal places, user re-prompted.

2. **Adding to savings**
   1. Prerequisites: Application started and profile initialised.
   2. Test case: Enter `savings`, then enter `500`. Expected: $500.00 added to existing savings; new total displayed.
   3. Test case: Enter `savings`, then enter `0`. Expected: $0.00 added; savings total unchanged.
   4. Test case: Enter `savings`, then enter `-50`. Expected: Error shown for negative value, user re-prompted.
   5. Test case: Enter `savings`, then enter `abc`. Expected: Error shown for non-numeric input, user re-prompted.
   6. Test case: Enter `savings`, then enter `100.999`. Expected: Error shown for more than 2 decimal places, user re-prompted.

3. **Updating contribution ratio**
   1. Prerequisites: Application started and house price set during initial setup.
   2. Test case: Enter `ratio`, then enter `0.5`. Expected: Ratio set to 0.5; BTO goal recalculated and confirmed.
   3. Test case: Enter `ratio`, then enter `0.0` or `1.0`. Expected: Boundary values accepted; BTO goal recalculated.
   4. Test case: Enter `ratio`, then enter `1.5`. Expected: Error shown for value above 1.0, user re-prompted.
   5. Test case: Enter `ratio`, then enter `-0.1`. Expected: Error shown for negative value, user re-prompted.
   6. Test case: Enter `ratio`, then enter `abc`. Expected: Error shown for non-numeric input, user re-prompted.
   7. Test case: Enter `ratio`, then enter `0.123`. Expected: Error shown for more than 2 decimal places, user re-prompted.

4. **Viewing BTO Summary (`summary`)**
   1. Prerequisites: Profile initialised with allowance, savings, BTO goal, and deadline all set.
   2. Test case: Enter `summary` with current month expenses below monthly allowance. Expected: Summary shows positive monthly surplus and an estimated number of months to goal.
   3. Test case: Enter `summary` after savings meet or exceed BTO goal. Expected: Summary shows 100% progress and a "goal achieved" message; no months estimate shown.
   4. Test case: Enter `summary` when total expenses exceed monthly allowance. Expected: Summary shows negative surplus; a warning is shown instead of a months estimate.
   5. Test case: Enter `summary` with no expenses recorded yet. Expected: Summary shows full BTO goal as distance-to-goal and the full monthly allowance as surplus.

---

### Reset and clear
(to be added by Jairus)
---

### Category validation

1. **Parsing a category from string**
    1. Test case: `FOOD`, `food`, `fOoD` Expected: All resolve to a `FoodCategory` instance (case-insensitive).
    2. Test case: `TRANSPORT`, `ENTERTAINMENT`, `UTILITIES`, `OTHER` Expected: Each resolves to its corresponding category class.
    3. Test case: `HELLO` Expected: `IllegalArgumentException` thrown.
    4. Test case: `Category.isValid("FOO")`, `Category.isValid("hello")` Expected: Returns `false`.

2. **Category sort ordering**
    1. Test case: Compare FOOD → TRANSPORT → ENTERTAINMENT → UTILITIES → OTHER. Expected: Each preceding category compares as less than the next, confirming sort priority.

---

### Archive Expenses

1. **Archiving current month with `save`**
    1. Prerequisites: At least 1 regular expense exists in the current month (e.g., `add lunch 10 food`).
    2. Test case: `save` Expected: Current month's expenses are archived, month advances by 1, and monthly expense list is reset.
    3. Test case: Run `list` after `save` Expected: Archived month section is shown, and current month has no regular expenses yet.
    4. Test case: Run `save` immediately again (no new regular expenses added) Expected: New month is still created and advanced; archive for that month is empty.
    5. Test case: Add expenses that exceed allowance, then run `save` Expected: Month still archives and advances; no unspent allowance is transferred.

---

### Monthly Archive

1. **Viewing archived month history**
    1. Prerequisites: At least 1 month has been archived via `save`.
    2. Test case: `list` Expected: Output includes monthly sections in order (e.g., `*** MONTH 1 EXPENSES`, `*** MONTH 2 EXPENSES`) with expenses grouped under each month.
    3. Test case: In the newest month with no expenses, run `list` Expected: Earlier month sections still appear; current month shows no regular expenses recorded yet.
    4. Test case: Restart the app, then run `list` Expected: Archived month sections are reloaded from storage and remain visible.

2. **Incorrect commands related to archiving**
    1. Test case: `savee` Expected: Command is rejected as invalid; no month advancement or archive changes.
    2. Test case: `save month` Expected: Treated as invalid command format; archive data remains unchanged.


