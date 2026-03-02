package seedu.duke;

import seedu.duke.data.Expense;
import seedu.duke.ui.Ui;
import seedu.duke.util.InputUtil;
import seedu.duke.data.Profile;
import seedu.duke.data.ExpenseList;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.math.BigDecimal;

public class FinTrackPro {

    private final Ui ui;
    private final Profile profile = new Profile();
    private final ExpenseList expenseList = new ExpenseList();

    public FinTrackPro(Ui ui) {
        this.ui = ui;
    }

    public void run() {
        ui.showWelcome();
        Scanner in = new Scanner(System.in);

        // Name handling
        String name = ui.readLine(in, "What is your name?");
        name = name.isEmpty() ? "friend" : name.trim();
        ui.greet(name);

        ui.printLine("");
        ui.printLine("Hang tight... I have a few questions for you.");

        // Initial goal setup
        BigDecimal goal = InputUtil.readMoney(ui, in,
                "What is the total value that you and your partner have to pay for "
                        + "the house? (in dollars)");

        BigDecimal legalFees = goal.multiply(new BigDecimal("1.1"));
        BigDecimal totalRequired = goal.add(legalFees);

        ui.printLine("Sweeeett. Including legal fees, you will need "
                + InputUtil.formatMoney(totalRequired) + " for the initial downpayment phase");

        // Deadline Handling
        LocalDate deadline = InputUtil.readFutureDate(
                ui,
                in,
                "When do you need to save this money by? "
                        + "(Enter in format YYYY-MM-DD)"
        );
        LocalDate today = LocalDate.now();
        Period period = Period.between(today, deadline);

        ui.printLine("You have " + period.getYears() + " years and "
                + period.getMonths() + " months remaining.");

        // Help Lines
        ui.printLine("");
        ui.printLine("Type 'help' to view my currently supported commands!");
        ui.printLine("Any non-command word would be echoed back to you you you");
        ui.printLine("");

        // Main Command Loop
        String userInput = ui.readLine(in, "");
        while (!userInput.equalsIgnoreCase("bye")) {
            handleCommand(userInput, in);
            userInput = ui.readLine(in, "");
        }
        ui.goodBye(name);
        in.close();
    }

    private void handleCommand(String userInput, Scanner in) {
        if (userInput.trim().isEmpty()) {
            ui.printLine("Cannot process empty description!");
            return;
        }
        String command = Parser.parseCommand(userInput);

        switch (command) {
        case "salary":
            handleSalary(in);
            break;
        case "savings":
            handleSavings(in);
            break;
        case "ratio":
            handleRatio(in);
            break;
        case "category":
            // addCategoryToExpense();
            break;
        case "help":
            ui.showHelpMessage();
            break;
        case "add":
            handleAdd(userInput);
            break;
        case "delete":
            handleDelete(userInput);
            break;
        case "list":
            printList();
            break;
        case "goal":
            handleGoal(userInput);
            break;
        case "clear":
            handleClear(in);
            break;
        default:
            ui.printLine("You said: " + userInput);
            break;
        }
    }

    private void handleAdd(String userInput){
        String rest = userInput.substring("add".length()).trim();
        //if there is no input after add
        if(rest.isEmpty()){
            ui.printLine("Format: add <value(to 2dp)> bro! where is the MONEHHHH");
            return;
        }

        BigDecimal amount;

        try{
            amount = new BigDecimal(rest);
        } catch (NumberFormatException e) {
            ui.printLine("Amount must be a valid number bro! What is this garbage!");
            return;
        }

        //Reject negative values
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            ui.printLine("Amount cannot be negative bro who you trying to scam?");
            return;
        }

        // Reject >2 decimal places
        if (amount.scale() > 2) {
            ui.printLine("Amount must not exceed 2 decimal places bro we dont want your measly cents!");
            return;
        }

        expenseList.add(amount);

        ui.printLine("Added expense: $" + amount);
        ui.printLine("Current Total: $" + expenseList.getTotal());

    }

    private void handleDelete(String userInput){
        String rest = userInput.substring("delete".length()).trim();

        if (!rest.matches("\\d+")) {
            ui.printLine("Format: delete <number-on-list> bruh its not that hard");
            return;
        }

        int index = Integer.parseInt(rest);

        if (!expenseList.isValidIndex(index)) {
            ui.printLine("Invalid index bro! do you even know how much you've spent?");
            return;
        }

        Expense removed = expenseList.delete(index);

        ui.printLine("Deleted expense #" + index + ": $" + removed.getAmount());
        ui.printLine("Current Total: $" + expenseList.getTotal());
    }

    private void handleClear(Scanner in) {
        ui.printLine("WARNING: This will permanently delete ALL expenses. Are you sure? (Y/N)");
        String response = in.nextLine().trim().toLowerCase();
        if (response.equals("y")) {
            expenseList.clear();
            ui.printLine("Expense list has been wiped clean. Fresh start!");
        } else {
            ui.printLine("Clear cancelled. Your data is still there, bro.");
        }
    }

    private void printList(){
        if (expenseList.isEmpty()) {
            ui.printLine("Your expense list is as empty as my wallet. Go spend some money!");
            return;
        }

        ui.printLine("Here is your current expenditure list!");

        for (int i = 0; i < expenseList.size(); i++) {
            Expense expense = expenseList.get(i);
            String formattedAmount = InputUtil.formatMoney(expense.getAmount());
            ui.printLine((i + 1) + ". " + formattedAmount);
        }

        BigDecimal totalSpent = expenseList.getTotal();
        ui.printLine("Total Expenditure: $" +  expenseList.getTotal());

        BigDecimal goal = profile.getSpendingGoal();
        if (totalSpent.compareTo(goal) > 0) {
            ui.printLine("Alert: You've already exceeded your goal by "
                    + InputUtil.formatMoney(totalSpent.subtract(goal)) + "!");
        }
    }

    private void handleGoal(String userInput) {
        String rest = userInput.substring("goal".length()).trim();

        if (rest.isEmpty()) {
            ui.printLine("Current spending goal: " + InputUtil.formatMoney(profile.getSpendingGoal()));
            ui.printLine("To update, use: goal <amount>");
            return;
        }

        BigDecimal newGoal;
        try {
            newGoal = new BigDecimal(rest);
        } catch (NumberFormatException e) {
            ui.printLine("Invalid amount! 'goal " + rest + "' is not a number, bro.");
            return;
        }

        if (newGoal.compareTo(BigDecimal.ZERO) < 0) {
            ui.printLine("Goal cannot be negative! You can't budget for negative money!!.");
            return;
        }

        profile.setSpendingGoal(newGoal);
        ui.printLine("Spending goal updated to: " + InputUtil.formatMoney(newGoal));

        BigDecimal totalSpent = expenseList.getTotal();
        if (totalSpent.compareTo(newGoal) > 0) {
            ui.printLine("Alert: You've already exceeded this goal by "
                    + InputUtil.formatMoney(totalSpent.subtract(newGoal)) + "!");
        }
    }

    private void handleSalary(Scanner in) {
        // Show previous input
        BigDecimal current = profile.getMonthlySalary();
        ui.printLine("Current monthly salary: " + InputUtil.formatMoney(current));

        // Prompt for update
        BigDecimal newAmount = InputUtil.readMoney(ui, in, "Enter your new monthly salary:");
        profile.setMonthlySalary(newAmount);

        ui.printLine("Salary successfully updated to: " + InputUtil.formatMoney(newAmount));
    }

    private void handleSavings(Scanner in) {
        // Show previous input
        BigDecimal current = profile.getCurrentSavings();
        ui.printLine("Current total savings: " + InputUtil.formatMoney(current));

        // Prompt for update
        BigDecimal newAmount = InputUtil.readMoney(ui, in, "Enter your new total savings:");
        profile.setCurrentSavings(newAmount);

        ui.printLine("Savings successfully updated to: " + InputUtil.formatMoney(newAmount));
    }

    private void handleRatio(Scanner in) {
        double current = profile.getContributionRatio();
        ui.printLine("Current BTO contribution share: " + (current * 100) + "%");

        ui.printLine("Enter your new share (e.g., 0.6 for 60%):");
        try {
            double newRatio = Double.parseDouble(in.nextLine());
            profile.setContributionRatio(newRatio);
            ui.printLine("Contribution ratio updated!");
        } catch (NumberFormatException e) {
            ui.printLine("Invalid input. Ratio remains at " + (current * 100) + "%.");
        }
    }
}
