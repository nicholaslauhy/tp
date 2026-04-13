# FinTrackPro User Guide

## Introduction

Welcome to FinTrackPro!
FinTrackPro is a desktop app for university students planning to secure a Build-To-Order (BTO) flat.
FinTrackPro turns messy finances into a clear, actionable downpayment plan—showing exactly how much you need to save 
monthly and tracking your progress in real-time.
Let's get started!


## Quick Start
1. Ensure that you have Java 17 or above installed.
2. Download the latest ```.jar``` file of FinTrackPro from [here](https://github.com/AY2526S2-CS2113-T14-2/tP/releases).
3. Copy the file to the folder that you want to use as the home folder for FinTrackPro.
4. Open Terminal(Mac) or Windows Powershell(Windows), ```cd``` into the folder you put the jar file in, and use the ```java -jar FinTrackPro.jar``` command to run the application. 
5. You should see an introduction page asking for your name.
6. Type the command in the command line and press Enter to execute it.
7. You can refer to the [Features](#features) page for details of each command. Have fun!!

## Notes about the command format
- Words in `UPPER_CASE` are parameters to be supplied by the user. e.g. in `add <NAME> <AMOUNT> <CATEGORY>`, `NAME`, `AMOUNT` and `CATEGORY` are parameters.
- Items within angle brackets `< >` are compulsory. e.g. `<NAME>` means `NAME` must be provided.
- Items in square brackets `[ ]` are optional. e.g. `[RECURRING]` can be omitted.
- 
## Features

* <b>View all commands:</b> [help](#viewing-help-help)
* <b>Add more savings:</b> [savings](#add-more-savings-savings)
* <b>Update monthly allowance:</b> [allowance](#update-monthly-allowance-allowance)
* <b>Update contribution ratio:</b> [ratio](#update-contribution-ratio-ratio)
* <b>Adding an expense:</b> [add](#adding-an-expense-add)
* <b>Listing all expenditures:</b> [list](#listing-all-entries-list)
* <b>Sorting the expenditure list:</b> [sort](#sorting-the-expenditure-list-sort-keyword)
* <b>Deleting an entry:</b> [delete](#deleting-an-entry-delete)
* <b>Deleting a recurring expense:</b> [deleterecurring](#deleting-a-recurring-entry-deleterecurring)
* <b>View financial summary:</b> [summary](#view-financial-summary-summary)
* <b>Archive monthly expenditures:</b> [save](#archive-monthly-expenditures-save)
* <b>Clearing all entries:</b> [clear](#clearing-all-entries-clear)
* <b>Factory Reset:</b> [reset](#factory-reset-reset)
* <b>Exiting the program:</b> [bye](#exiting-the-program-bye)
* <b>Data Storage:</b> [storage](#data-storage)

## Additional Help:
* [FAQ](#faq)
* [Command Summary](#command-summary)
* [Enquiry](#enquiry)

### Viewing Help: ```help```
Shows a message explaining how to access the help page, lists all commands. 

<b>Format:</b> ```help``` <br>
<b>Example of Usage:</b> ```help``` <br>
<b>Expected Output:</b>
```
help
General Commands
'help'    - view all current commands
'summary' - generate your BTO readiness report based on your goals
'bye'     - exit the program

Daily Transaction Commands
'add'      <name> <amount> <category> <recurring> - add a new expense
(e.g., add lunch 5.50 FOOD for not recurring and add lunch 5.50 FOOD recurring for recurring)
..
..
```


### Add more savings: ```savings```
Add more savings from the initial saving count

<b>Format:</b> ```savings``` <br>
<b>NOTE:</b> When entering `amount`, it represents the additional amount of cash you want to add towards the goal.<br>
<b>WARNING:</b> Savings can only be increased. There is no command to reduce or undo a savings entry. Double-check your amount before confirming.<br>
<b>Example of Usage:</b> ```savings```<br>
<b>Expected Output:</b>
```
savings
Current total savings: $1,000.00
Enter amount to add to your savings:
1000

Transaction successful!
Added: $1,000.00
New total savings: $2,000.00
```

### Update monthly allowance: ```allowance```
Updates your monthly allowance to a new value.<br>
<b>Format:</b> ```allowance``` <br>
<b>Example of Usage:</b> ```allowance```<br>
<b>Expected Output:</b>
```
allowance
Current Monthly Allowance: $4,000.00
Enter new monthly allowance:
5000

Success! Your monthly allowance is now $5,000.00
```
<b>NOTE:</b>
- Amount must be a non-negative number with at most 2 decimal places.
- This replaces the existing allowance (it does not add to it).

### Update contribution ratio: ```ratio```
Updates your share of the BTO downpayment cost relative to your partner.<br>
<b>Format:</b> ```ratio``` <br>
<b>Example of Usage:</b> ```ratio```<br>
<b>Expected Output:</b>
```
ratio
Current Contribution Ratio: 60.0% (0.6)
Enter new ratio (0.0 to 1.0):
0.5

Success! Your contribution ratio is now 0.5
```
<b>NOTE:</b>
- Value must be between `0.01` (1%) and `1.0` (100%), with at most 2 decimal places.
- Inputs like `0.8666666` are rejected; use `0.86` instead.
- Updating the ratio automatically recalculates your BTO goal. Run `summary` to see the updated goal.

### Adding an expense: ```add```
Adds a regular expense to your monthly tracker, with optional field recurring.<br>
<b>Format:</b> ```add <NAME> <AMOUNT> <CATEGORY> [RECURRING]``` <br>
<b>Example of Usage:</b> <br>
```add netflix 30 entertainment recurring ```<br>
```add breakfast 25 food``` <br>
<b>Expected Output:</b>
```
Added recurring expense: [RECURRING][ENTERTAINMENT] netflix $30
Recurring Total: $30

Added expense: [FOOD] breakfast $25
Month 1 Total: $25
```
<b>NOTE:</b>
- The keyword `recurring` is optional.
- If omitted, the expense will be treated as a one-off expense.
- The command format is: `add <NAME> <AMOUNT> <CATEGORY> [RECURRING]` The system interprets the last numeric token before 
  the category as the amount. Any earlier tokens, including numbers, are treated as part of the expense name. To avoid ambiguity, 
  users should enter exactly one amount value.
- Expense name cannot contain the `|` character, as it is reserved as the file delimiter.
- Name should only contain standard English letters, numbers, and common punctuation (e.g., A–Z, a–z, 0–9, spaces, ., -, _)
- Special Unicode characters (e.g., emojis or non-English symbols) are not supported
  and may not be displayed or stored correctly

### Listing all entries: ```list```
Shows a consolidated view of all your recorded expenses, neatly categorized by recurring commitments, previous months' 
archives, and the current month's one-off expenses.<br>
<b>Format:</b> ```list``` <br>
<b>Example of Usage:</b> ```list```<br>
<b>Expected Output:</b>
```
Here are your recurring monthly commitments!
1. Netflix $30.00 [ENTERTAINMENT]

*** MONTH 1 EXPENSES
1. Chicken Rice $7.30 [FOOD]
2. Pizza $10.00 [FOOD]
3. Game $12.00 [ENTERTAINMENT]
4. Movie $9.00 [ENTERTAINMENT]
5. Project Materials $50.00 [OTHER]
Month 1 Total: $88.30

Total Expenditure (All Months + Recurring): $118.30
```

### Sorting the expenditure list: ```sort <keyword>```
Sorts the expenditure list by category, by recency or by alphabetical order. Valid keywords are `name`, `category` and `recent`.<br>
<b>Format:</b> ```sort category``` or ``` sort name ``` or ```sort recent```<br>
<b>Example of Usage:</b> ``` sort name ``` or ```sort category```<br>
<b>Expected Output:</b>
```
> sort name
Expenses sorted alphabetically by name.
> list
Here are your recurring monthly commitments!
1. Netflix $30.00 [ENTERTAINMENT]

*** MONTH 1 EXPENSES
1. Chicken Rice $7.30 [FOOD]
2. Game $12.00 [ENTERTAINMENT]
3. Movie $9.00 [ENTERTAINMENT]
4. Pizza $10.00 [FOOD]
5. Project Materials $50.00 [OTHER]
Month 1 Total: $88.30

Total Expenditure (All Months + Recurring): $118.30

> sort category
Expenses sorted by category.
> list
Here are your recurring monthly commitments!
1. Netflix $30.00 [ENTERTAINMENT]

*** MONTH 1 EXPENSES
1. Chicken Rice $7.30 [FOOD]
2. Pizza $10.00 [FOOD]
3. Game $12.00 [ENTERTAINMENT]
4. Movie $9.00 [ENTERTAINMENT]
5. Project Materials $50.00 [OTHER]
Month 1 Total: $88.30

Total Expenditure (All Months + Recurring): $118.30
```
<b>NOTE:</b> Sorting reorders the in-memory list immediately. Run `list` to see the updated order.
<b>NOTE:</b> Sort only reorders the current month expenses.

### Deleting an entry: ```delete```
Deletes the specified entry from the tracker.<br>
<b>Format:</b> ```delete <INDEX>``` <br>
<b>Example of Usage:</b> ```delete 1```<br>
<b>Expected Output:</b>
```
> delete 1
Deleted expense #1: [OTHER] water $10.00
Current Total: $0
```
<b>NOTE:</b>
- INDEX refers to the position in the current month’s expense list.

### Deleting a recurring entry: ```deleterecurring```
Deletes a recurring expense from the tracker.<br>
<b>Format:</b> ```deleterecurring <INDEX>```  
<b>Example of Usage:</b> ```deleterecurring 1```  
<b>Expected Output:</b>
```
Deleted recurring expense #1: [RECURRING][ENTERTAINMENT] netflix $30
Recurring Total: $0
```
<b>NOTE:</b>  
- INDEX refers to the position in the recurring expense list.  
- Use `list` to view recurring expenses and their indices.  

### View financial summary: ```summary```
Generates a comprehensive financial report based on your profile and current spending habits. Calculates your monthly
surplus, distance to your goal, and provides an estimate of how many months it will take to secure your downpayment.
<br>The Readiness Level reflects your percentage progress toward your BTO goal, ranging from `Barely Started - Do start saving soon` to 
`Ready - Time to sign that BTO!`.<br>
<b>Format:</b> ```summary``` <br>
<b>Example of Usage:</b> ```summary``` <br>
<b>Expected Output:</b>
```
===== BTO Readiness Report =====
User: Jairus
Readiness Level: Barely Started - Do start saving soon!
BTO Goal: $25,000.00 (your share + fees)
Deadline: 2028-10-24 (31 months)

Current Savings: $12,500.00 (50.00% reached)
Distance to Goal: $12,500.00
Adjusted Minimum Savings: $403.23 / month

Monthly Allowance: $1,000.00
Total Expenditure: $200.00
Monthly Surplus (Allowance - Expenditure): $800.00
Estimated Goal Achievement: 16 months
```

### Clearing all entries: ```clear```
Wipes all one-off expenses for the current month.<br>
<b>Format:</b> ```clear``` <br>
<b>Example of Usage:</b> ```clear```<br>
<b>Expected Output:</b>
```
WARNING: This will permanently delete ALL one-off expenses. Are you sure? (Input Y to clear)
> Y
Current month's one-off expenses have been wiped clean. Recurring expenses are kept.
```

### Factory Reset: ```reset```
Completely erases all data inside FinTrackPro, including your profile, all one-off expenses, all recurring expenses, and all monthly archives. <br>
<b>Format:</b> ```reset``` <br>
<b>Example of Usage:</b> ```reset``` <br>
<b>Expected Output:</b>
```
WARNING: This will wipe your profile and ALL expenses. Type 'Y' to continue: 
> Y
System reset successful. Please restart or type 'bye' to exit.
```

### Exiting the program: ```bye```
Exits the program<br>
<b>Format:</b> ```bye``` <br>
<b>Example of Usage:</b> ```bye``` <br>
<b>Expected Output:</b>
```
Goodbye Jairus. Stay disciplined and get that house that you always wanted!
```

### Archive monthly expenditures: ```save```
Saves the current month of expenditures into `monthly_archives/` as `MonthN`, and resets the expenditure to 0, simulating a new month.<br>
<b>Format:</b> ```save``` <br>
<b>Example of Usage:</b> ```save``` <br>
<b>Expected Output:</b>
```
Month 1 expenses archived to 'monthly_archives'
Transferred $3,975.00 of unspent allowance to savings
Advanced to Month 2
Current Savings: $4,975.00
Monthly Allowance: $4,000.00
```
<b>NOTE:</b>  
- Any unspent allowance (monthly allowance minus total expenses) is automatically transferred to your savings balance.
- If your total expenses exceed your monthly allowance, no transfer occurs and an overspend message is shown.

### Data Storage
FinTrackPro data is saved in the hard disk automatically after any command that changes your data (e.g., add, delete, allowance, ratio). There is no need to save manually.<br>
<b>Data File Location:</b><br> Your data is securely saved locally on your computer in a file named fintrack.txt located in the same folder as your application. <br>
<b>Warning:</b><br> Advanced users can modify fintrack.txt directly. However, if the format is corrupted, FinTrackPro will safely skip the corrupted lines to prevent the app from crashing. <br>
<b>Expected Output:</b>
```
P | Jairus | 1500 | 1000 | 18375.00 | 0.7 | 2028-10-10 | 1 | null
E | Chicken Rice | 7.3 | FOOD | 1
E | Pizza | 10 | FOOD | 0
E | Game | 12 | ENTERTAINMENT | 4
E | Movie | 9 | ENTERTAINMENT | 2
E | Project Materials | 50 | OTHER | 3
R | Netflix | 30 | ENTERTAINMENT
```

**Below is a table for the interpretation of the output in** `fintrack.txt`

**Profile (`P`)**

| Field        | Value              | Meaning                                               |
|--------------|--------------------|-------------------------------------------------------|
| `P`          | profile marker     | tells the viewer this line represents a **Profile** |
| `Jairus`     | name               | user's name                                           |
| `1500`       | monthly allowance  | user has a **$1500/month** allowance                  |
| `1000`       | current savings    | currently saved **$1000** |
| `18375.00`   | BTO goal           | amount needed for the **downpayment goal** |
| `0.7`        | contribution ratio | user is paying **70%** of the BTO cost                |
| `2028-10-10` | deadline           | goal date (ISO format `YYYY-MM-DD`)                   |
| `1`          | current month      | user is currently tracking month **1** of their data  |
| `null`       | house price        | total BTO flat price; `null` if not set               |

**Expenditure (`E`)**

| Field          | Value              | Meaning                                                       |
|----------------|--------------------|---------------------------------------------------------------|
| `E`            | expenditure marker | tells the viewer this line represents a one-off **Expenditure**|
| `Chicken Rice` | name               | description of the expense                                    |
| `7.3`          | amount             | user spent **$7.30** on this item                             |
| `FOOD`         | category           | category assigned to the expense                              |
| `1`            | insertion order    | internal sequence tracker used to remember chronological order|

**Recurring Expenditure (`R`)**

| Field          | Value              | Meaning                                                       |
|----------------|--------------------|---------------------------------------------------------------|
| `R`            | recurring marker   | tells the viewer this line represents a **Recurring** expense |
| `Netflix`      | name               | description of the recurring expense                          |
| `30`           | amount             | user spends **$30.00** every month on this item               |
| `ENTERTAINMENT`| category           | category assigned to the expense                              |

## FAQ
<b>Updated as of 1st April 2026</b>

**Q**: How do I transfer my data to another computer? 

**A**: Because FinTrackPro prioritizes your privacy, your data is not stored in the cloud. 
To transfer your data to a new computer, simply copy the `fintrack.txt` file from your current FinTrackPro folder and 
paste it into the FinTrackPro folder on your new computer. When you run the `.jar` file on the new computer, it will 
automatically load your existing progress!

Watch this space for more updates!!

## Command Summary

### General Commands

| Action                    | Format, Examples                                              |
|---------------------------|---------------------------------------------------------------|
| Viewing Help              | `help`                                                        |
| View Financial Summary    | `summary`                                                     |
| Exit Program              | `bye`                                                         |

### Daily Transaction Commands

| Action                    | Format, Examples                                 |
|---------------------------|--------------------------------------------------|
| Add Expense               | `add <NAME> <AMOUNT> <CATEGORY> [RECURRING]`     |
| List Entries              | `list`                                           |
| Delete Entry              | `delete <INDEX>` e.g. `delete 2`                 |
| Delete Recurring          | `deleterecurring <INDEX>` eg `deleterecurring 1` |

### Other Commands

| Action                    | Format, Examples                                                |
|---------------------------|-----------------------------------------------------------------|
| Sort Entries              | `sort <keyword>` e.g. `sort category` `sort recent` `sort name` |
| Add more savings          | `savings`                                                       |
| Update monthly allowance  | `allowance`                                                     |
| Update contribution ratio | `ratio`                                                         |
| Archive Month             | `save`                                                          |
| Clear Current Month       | `clear`                                                         |
| Factory Reset             | `reset`                                                         |


### Enquiry
We hope that you found FinTrackPro useful and easy to use!

Meanwhile, if you have any enquiries/bugs that you might have found please email us [here!](mailto:e1406324@u.nus.edu)
