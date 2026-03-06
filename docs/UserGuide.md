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
* <b>Setting monthly salary:</b> [salary](#setting-monthly-salary-salary)
* <b>Setting current savings:</b> [savings](#setting-current-savings-savings)
* <b>Setting downpayment goal:</b> [goal](#setting-downpayment-goal-goal)
* <b>Setting ratio:</b> [ratio](#setting-ratio-ratio)
* <b>Adding an expense:</b> [add](#adding-an-expense-add)
* <b>Listing all expenditures:</b> [list](#listing-all-entries-list)
* <b>Deleting an entry:</b> [delete](#deleting-an-entry-delete)
* <b>Setting a target date:</b> [deadline](#setting-a-target-date-deadline)
* <b>Viewing financial summary:</b> [summary](#viewing-financial-summary-summary)
* <b>Clearing all entries:</b> [clear](#clearing-all-entries-clear)
* <b>Exiting the program:</b> [exit](#exiting-the-program-exit)

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
This is your help page. Here are the commands: 
salary - helps track the salary
```

### Setting monthly salary: ```salary```
Sets or updates your current monthly income to help calculate spending proportions.

<b>Format:</b> ```salary amt/AMOUNT``` <br>
<b>Example of Usage:</b> ```salary amt/1200.50``` <br>
<b>Expected Output:</b>
```
You have set your salary as $1200.50/month.
```

### Setting current savings: ```savings```
Setting your current liquid assets dedicated to the BTO downpayment. 

<b>Format:</b> ```savings amt/AMOUNT``` <br>
<b>NOTE:</b> ```AMOUNT``` represents the total cash/CPF you currently have available for the goal.<br>
<b>Example of Usage:</b> ```salary amt/5000```<br>
<b>Expected Output:</b>
```
You have set your current savings as $5000.00/month.
```

### Setting downpayment goal: ```goal```
Defines the target BTO price goal <br>
<b>Format:</b> ```goal TOTAL_PRICE``` <br>
<b>NOTE:</b> ```TOTAL_PRICE``` is the FULL cost of the BTO flat. FinTrackPro will
automatically calculate the 2.5% downpayment requirement plus estimated legal fees.<br>
<b>Example of Usage:</b>```goal 400000`` <br>
<b>Expected Output:</b>
```
Your goal is to save $20000.00 (Downpayment plus legal fees)
```
 
### Setting ratio: ```ratio```
Defines the proportion of how much you are paying for the downpayment.<br>
<b>Format:</b> ```ratio RATIO``` <br>
<b>Example of Usage:</b> ```ratio 60```<br>
<b>Expected Output:</b>
```
You have set the ratio to be 60%. You need to fork out $2,500.00 at the end of your goal of December 2027!
```

### Adding an expense: ```add ```
Adds a regular expense to your monthly tracker.<br>
<b>Format:</b> ```add n/NAME amt/AMOUNT``` <br>
<b>Example of Usage:</b> <br>
```add n/Hall Rental amt/450 ```<br>
```add n/Netflix amt/15.90```
<b>Expected Output:</b>
```
Here is the list of your expenditures! 
1. Hall Rental 450.00
2. Netflix 15.90
```

### Listing all entries: ```list```
Shows a list of current month expenses and income entries.<br>
<b>Format:</b> ```list``` <br>
<b>Example of Usage:</b> ```list```<br>
<b>Expected Output:</b>
```
Here is your current expenditure list! 
Food: $100.00
Hall Rental: $2,000.00
Miscellaneous: $400.00
Total Spent: $2,500.00
Money Left for the Month based on goal: $2,500.00 / $5,000.00
```

### Deleting an entry: ```delete```
Deletes the specified entry from the tracker.<br>
<b>Format:</b> ```delete INDEX``` <br>
<b>Example of Usage:</b> ```delete 2```<br>
<b>Expected Output:</b>
```
You have deleted:
    2. Hall Rental: $2,000.00.
You currently have 2 expenses in your expenditure list!
```

### Setting a target date: ```deadline```
Sets a specific date by which you want to reach your downpayment goal.<br>
<b>Format:</b> ```deadline d/YYYY-MM``` <br>
<b>Example of Usage:</b> ```deadline d/2027-12``` <br>
<b>Expected Output:</b>
```
You have set December 2027 as your goal deadline! You currently have x years, and y months left to your deadline.
```

### Viewing financial summary: ```summary```
Displays a comprehensive overview of your financial status, including distance to goal.<br>
<b>Format:</b> ```summary``` <br>
<b>Example of Usage:</b> ```summary```<br>
<b>Expected Output:</b>
```
======= BTO Readiness Report ====
Current Goal: $6,200.00 (your share of 2.5% + fees)
Current Savings: $4,500.00 (72% reached)
Distance to Goal: $1,700.00
Monthly Surplus: $400.00
Estimated Goal Achievement: 4 months
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

### Exiting the program: ```exit```
Exits the program<br>
<b>Format:</b> ```exit``` <br>
<b>Example of Usage:</b> ```exit``` <br>
<b>Expected Output:</b>
```
Exiting! Have a nice day!
```


## FAQ
<b>Updated as of 6 March 2026</b>

**Q**: How do I transfer my data to another computer? 

**A**: Your data will be saved on your local computer. Just log in with the same name
on another device if you want to continue with your saved progress!

Watch this space for more updates!!

## Command Summary

| Action                 | Format, Examples                                         |
| ---------------------- | -------------------------------------------------------- |
| Viewing Help           | `help`                                                   |
| Set Monthly Salary     | `salary amt/AMOUNT` e.g. `salary amt/1200.50`            |
| Set Current Savings    | `savings amt/AMOUNT` e.g. `savings amt/5000`             |
| Set BTO Goal Price     | `goal TOTAL_PRICE` e.g. `goal 400000`                    |
| Set Contribution Ratio | `ratio RATIO` e.g. `ratio 60`                            |
| Add Expense            | `add n/NAME amt/AMOUNT` e.g. `add n/Hall Rental amt/450` |
| List Entries           | `list`                                                   |
| Delete Entry           | `delete INDEX` e.g. `delete 2`                           |
| Set Deadline           | `deadline d/YYYY-MM` e.g. `deadline d/2027-12`           |
| View Financial Summary | `summary`                                                |
| Clear All Data         | `clear`                                                  |
| Exit Program           | `exit`                                                   |

### Enquiry
We hope that you found FinTrackPro useful and easy to use!

Meanwhile, if you have any enquires/bugs that you might have found please email us [here!](mailto:e1406324@u.nus.edu) 

