# 1. Developer Guide
## Table of Contents
* **[Acknowledgements](#2-acknowledgements)**
  * **[Frameworks and base code](#21-frameworks-and-base-code)**
  * **[Third-party libraries](#22-third-party-libraries)**
  * **[Educational resources](#23-educational-resources)**
* **[Design & Implementation](#3-design--implementation)**
  * **[Architecture Diagram](#31-architecture-diagram)
  * **[Category component](#32-category-component)**
* **[Product Scope](#4-product-scope)**
  * **[Target user profile](#41-target-user-profile)**
  * **[Value proposition](#42-value-proposition)**
  * **[User stories](#43-user-stories)**
* **[Non-Functional Requirements](#5-non-functional-requirements)**
  * **[Performance and scalability](#51-performance-and-scalability)**
  * **[Data integrity](#52-data-integrity)**
  * **[Security and privacy](#53-security-and-privacy)**
  * **[Usability](#54-usability)**
  * **[Environment](#55-environment)**
* **[Glossary](#6-glossary)**
* **[Instructions For Manual Testing](#7-instructions-for-manual-testing)**
  * **[Sorting expenses](#sorting-expenses)**
  * **[Category validation](#category-validation)**

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
![Architecture Diagram](diagram/Architecture-Diagram.png)

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


## 3.2 UML Diagrams
In this section, we will present the UML class diagrams, object diagrams and sequence diagrams to 
illustrate how each main component of FinTrackPro integrates with the rest of the codebase.

### Category component
![Class Diagram](diagram/Category-UML-Diagram.png)


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

---

### Sorting expenses

1. **Sorting by category**
    1. Prerequisites: Multiple expenses of different categories in the list.
    2. Test case: `sort category` Expected: Expenses reordered in category priority: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER.
    3. Test case: `sort recent` Expected: Expenses reordered back to insertion order.
    4. Test case: `sort foo` Expected: List order unchanged. Error shown for unrecognized argument.
    5. Other incorrect sort commands to try: `sort` (empty argument) Expected: Similar to previous.

---

### Managing profile
(to be added by Adam)
---

### Reset and clear
(to be added)
---

### Category validation

1. **Parsing a category from string**
    1. Test case: `FOOD`, `food`, `fOoD` Expected: All resolve to a `FoodCategory` instance (case-insensitive).
    2. Test case: `TRANSPORT`, `ENTERTAINMENT`, `UTILITIES`, `OTHER` Expected: Each resolves to its corresponding category class.
    3. Test case: `HELLO` Expected: `IllegalArgumentException` thrown.
    4. Test case: `Category.isValid("FOO")`, `Category.isValid("hello")` Expected: Returns `false`.

2. **Category sort ordering**
    1. Test case: Compare FOOD → TRANSPORT → ENTERTAINMENT → UTILITIES → OTHER. Expected: Each preceding category compares as less than the next, confirming sort priority.
