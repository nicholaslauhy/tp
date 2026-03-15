# FinTrackPro User Guide

## Introduction

FinTrack Pro was created for individual students in a relationship who are planning to set aside finances for their share of a BTO downpayment.

## Quick Start
1. Ensure that you have Java 17 or above installed.
2. Down the latest ```.jar``` file of FinTrackPro from [here](https://github.com/AY2526S2-CS2113-T14-2/tP/releases).
3. Copy the file to the folder that you want to use as the home folder for your Task Manager.
4. Open Terminal(Mac) or Windows Powershell(Windows), ```cd``` into the folder you put the jar file in, and use the ```java -jar FinTrackPro.jar``` command to run the application. 
5. You should see something an introduction page asking for your name. 
6. Type the command in the command line and press Enter to execute it.
7. You can refer to the [Features](#features-) page for details of each command. Have fun!!

## Features 

* <b>View all commands:</b> [help](#viewing-help-help)
* <b>Add more savings:</b> [savings](#add-more-savings-savings)
* <b>Adding an expense:</b> [add](#adding-an-expense-add)
* <b>Listing all expenditures:</b> [list](#listing-all-entries-list)
* <b>Deleting an entry:</b> [delete](#deleting-an-entry-delete)
* <b>Viewing financial summary:</b> [summary](#viewing-financial-summary-summary)
* <b>Clearing all entries:</b> [clear](#clearing-all-entries-clear)
* <b>Exiting the program:</b> [exit](#exiting-the-program-exit)
* <b>Data Storage:</b> [storage](#data-storage)

## Additional Help:
* [FAQ](#faq)
* [Command Summary](#command-summary)
* [Enquiry](#enquiry)

### Viewing Help: ```help ```
Shows a message explaining how to access the help page, lists all commands. 

<b>Format:</b> help <br>
<b>Example of Usage:</b> <br>
<b>Expected Output:</b>
```
help
General Commands
'help'    - view all current commands
'summary' - generate your BTO readiness report based on your goals
'bye'     - exit the program

...
...
```


### Add more savings: ```savings```
Add more savings from the initial saving count

<b>Format:</b> ```savings``` <br>
<b>NOTE:</b> When entering `amount`, it represents the additional amount of cash you want to add towards the goal.<br>
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

### Adding an expense: ```add ```
Adds a regular expense to your monthly tracker.<br>
<b>Format:</b> ```add AMOUNT``` <br>
<b>Example of Usage:</b> <br>
```add 30 ```<br>
```add 15.90``` <br>
<b>Expected Output:</b>
```
add 30
Added expense: $30
Current Total: $30

add 15.90
Added expense: $15.90
Current Total: $45.90
```

### Listing all entries: ```list```
Shows a list of current month expenses and income entries.<br>
<b>Format:</b> ```list``` <br>
<b>Example of Usage:</b> ```list```<br>
<b>Expected Output:</b>
```
Here is your current expenditure list!
1. $30.00
2. $15.90
Total Expenditure: $45.90
```

### Deleting an entry: ```delete```
Deletes the specified entry from the tracker.<br>
<b>Format:</b> ```delete INDEX``` <br>
<b>Example of Usage:</b> ```delete 1```<br>
<b>Expected Output:</b>
```
delete 1
Deleted expense #1: $30
Current Total: $15.90
```

### Viewing financial summary: ```summary```
Displays a comprehensive overview of your financial status, including distance to goal.<br>
<b>Format:</b> ```summary``` <br>
<b>Example of Usage:</b> ```summary```<br>
<b>Expected Output:</b>
```
===== BTO Readiness Report =====
User: nicholas
BTO Goal: $12,285.00 (your share + fees)
Deadline: 2027-08-06 (17 months)

Current Savings: $1,000.00 (8% reached)
Distance to Goal: $11,285.00

Monthly Salary: $4,000.00
Total Expenditure: $30.00
Monthly Surplus: $3,970.00
Estimated Goal Achievement: 3 months
```

### Clearing all entries: ```clear```
Deletes all current data and resets the profile<br>
<b>Format:</b> ```clear``` <br>
<b>Example of Usage:</b> ```clear```<br>
<b>Expected Output:</b>
```
Are you sure you want to clear all current data? (Y/N):
(if Y) You have cleared all current data!
(else) Cancelling clearing data!
```

### Exiting the program: ```bye```
Exits the program<br>
<b>Format:</b> ```bye``` <br>
<b>Example of Usage:</b> ```bye``` <br>
<b>Expected Output:</b>
```
Goodbye nicholas. Stay disciplined and get that house that you always wanted!
```

### Data Storage
Stores the data in relative path as 'fintrack.txt'<br>
<b>NOTE:</b> The saving of data into storage will only be done after you type 'bye'<br>
<b>Expected Output:</b>
```
P | nicholas | 4000 | 1000 | 12285.00 | 0.6 | 2027-08-06
E | 30
```

**Below is a table for the interpretation of the output in** `fintrack.txt`

**Profile**

| Field        | Value              | Meaning                                               |
|--------------|--------------------|-------------------------------------------------------|
| `P`          | profile marker     | tells the viewer this line represents a **Profile**   |
| `nicholas`   | name               | user's name                                           |
| `4000`       | monthly salary     | user earns **$4000/month**                            |
| `1000`       | current savings    | currently saved **$1000**                             |
| `12285.00`   | BTO goal           | amount needed for the **downpayment goal**            |
| `0.6`        | contribution ratio | user is paying **60%** of the BTO cost                |
| `2027-08-06` | deadline           | goal date (ISO format `YYYY-MM-DD`)                   |

**Expenditure**

| Field        | Value              | Meaning                                               |
|--------------|--------------------|-------------------------------------------------------|
| `E`          | expenditure        | tells the viewer this line represents **Expenditure** |
| `30`         | amount             | user has spent **$30** on a single expenditure        |

## FAQ
<b>Updated as of 15 March 2026</b>

**Q**: How do I transfer my data to another computer? 

**A**: At this point you cannot, your data will be saved on your local device. Just run the code again and
you should not have to restart all your progress!

Watch this space for more updates!!

## Command Summary

| Action                 | Format, Examples               |
|------------------------|--------------------------------|
| Viewing Help           | `help`                         |
| Add more savings       | `savings` e.g. `savings`       |
| Add Expense            | `add AMOUNT` e.g. `add 450`    |
| List Entries           | `list`                         |
| Delete Entry           | `delete INDEX` e.g. `delete 2` |
| View Financial Summary | `summary`                      |
| Clear All Data         | `clear`                        |
| Exit Program           | `bye`                          |

### Enquiry
We hope that you found FinTrackPro useful and easy to use!

Meanwhile, if you have any enquires/bugs that you might have found please email us [here!](mailto:e1406324@u.nus.edu) 

