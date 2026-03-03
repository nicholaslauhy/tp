package seedu.duke;

import seedu.duke.data.Expense;
import seedu.duke.ui.Ui;
import seedu.duke.util.InputUtil;
import seedu.duke.data.Profile;
import seedu.duke.data.ExpenseList;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.math.RoundingMode;
import java.math.BigDecimal;

/**
 * Main application controller for FinTrackPro.
 *
 * <p>Handles the interactive CLI workflow:
 * greets the user, collects initial BTO goal + deadline, then enters a command loop.
 * Commands are parsed and dispatched to handlers that update the user's {@link Profile}
 * and manage the {@link ExpenseList}.</p>
 *
 * <p>This class does not store persistent data by itself; it operates on in-memory
 * {@code Profile} and {@code ExpenseList} instances.</p>
 */
public class FinTrackPro {

    private final Ui ui;
    private final Profile profile = new Profile();
    private final ExpenseList expenseList = new ExpenseList();

    public FinTrackPro(Ui ui) {
        this.ui = ui;
    }

    /**
     * Starts the FinTrackPro CLI session.
     *
     * <p>This method:
     * <ul>
     *   <li>Displays the welcome screen and prompts for the user's name</li>
     *   <li>Prompts for the BTO target amount and computes additional legal fees</li>
     *   <li>Prompts for a future deadline date and shows time remaining</li>
     *   <li>Enters the main command loop until the user types {@code bye}</li>
     * </ul>
     * </p>
     */
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
        BigDecimal housePrice = InputUtil.readMoney(ui, in,
                "What is the total value that you and your partner have to pay for "
                        + "the house? (in dollars)");

        BigDecimal newRatio = InputUtil.readRatio(ui, in,
                "What is your share of the contribution? (e.g., 0.6 for 60%):");
        profile.setContributionRatio(newRatio);

        BigDecimal downPayment = housePrice.multiply(new BigDecimal("0.025"));
        BigDecimal legalFees = downPayment.multiply(BigDecimal.valueOf(1.1));
        BigDecimal totalDownpayment = downPayment.add(legalFees);
        BigDecimal yourShare = totalDownpayment.multiply(newRatio);

        ui.printLine("Total downpayment needed: " + InputUtil.formatMoney(totalDownpayment));
        ui.printLine("Based on a " + newRatio.multiply(new BigDecimal("100")) + "% share...");
        ui.printLine("Your personal contribution needed: " + InputUtil.formatMoney(yourShare));

        profile.setBtoGoal(yourShare);

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

    /**
     * Parses and dispatches a single line of user input.
     *
     * <p>If the input does not match a supported command, it is echoed back to the user.
     * Empty/whitespace-only input is rejected.</p>
     *
     * @param userInput Raw line entered by the user.
     * @param in Scanner used for follow-up prompts for commands that require more input
     *           (e.g., salary, savings, ratio, clear).
     */
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
            handleGoal(in);
            break;
        case "clear":
            handleClear(in);
            break;
        case "summary":
            handleSummary();
            break;
        default:
            ui.printLine("You said: " + userInput);
            break;
        }
    }

    /**
     * Adds an expense entry into the {@link ExpenseList}.
     *
     * <p>Expected format: {@code add <amount>}</p>
     * <ul>
     *   <li>Rejects missing amount</li>
     *   <li>Rejects non-numeric amount</li>
     *   <li>Rejects negative values</li>
     *   <li>Rejects values with more than 2 decimal places</li>
     * </ul>
     *
     * <p>On success, prints the new expense and the updated running total.</p>
     *
     * @param userInput Full command line entered by the user (starting with {@code add}).
     */
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

    /**
     * Deletes an expense entry from the {@link ExpenseList} by 1-based index.
     *
     * <p>Expected format: {@code delete <index>}</p>
     * <ul>
     *   <li>Rejects non-integer indices</li>
     *   <li>Rejects out-of-range indices</li>
     * </ul>
     *
     * <p>On success, prints the removed entry and the updated running total.</p>
     *
     * @param userInput Full command line entered by the user (starting with {@code delete}).
     */
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

    /**
     * Clears all expenses from the {@link ExpenseList} after user confirmation.
     *
     * <p>This method prompts the user with a confirmation question.
     * Only a response of {@code "y"} (case-insensitive after trimming) will proceed.</p>
     *
     * <p>Side effect: Mutates the {@link ExpenseList} by removing all entries if confirmed.</p>
     *
     * @param in Scanner used to read the user's confirmation response.
     */
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

    /**
     * Prints the current expense list and total expenditure.
     *
     * <p>If the list is empty, prints a message indicating no expenses exist.</p>
     *
     * <p>Also checks the user's spending goal from {@link Profile}.
     * If total expenditure exceeds the goal, prints an alert with the exceeded amount.</p>
     */
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

    /**
     * Displays the current monthly spending goal and prompts for an updated value.
     *
     * <p>Usage: {@code goal}</p>
     *
     * <p>The method performs the following:
     * <ul>
     * <li>Displays the existing goal retrieved from the user's {@link Profile}</li>
     * <li>Prompts the user for a new goal amount using {@link InputUtil#readMoney}</li>
     * <li>Updates the profile with the validated amount</li>
     * <li>Checks if current total expenditure from {@link ExpenseList} already exceeds this goal</li>
     * </ul></p>
     *
     * <p>Validation is handled by {@code InputUtil}, which rejects non-numeric and negative values.</p>
     *
     * @param in Scanner used to read the user's new goal amount.
     */
    private void handleGoal(Scanner in) {
        BigDecimal currentGoal = profile.getSpendingGoal();
        ui.printLine("Current monthly spending goal: " + InputUtil.formatMoney(currentGoal));

        BigDecimal newGoal = InputUtil.readMoney(ui, in, "Enter your new monthly spending goal:");

        profile.setSpendingGoal(newGoal);
        ui.printLine("Spending goal successfully updated to: " + InputUtil.formatMoney(newGoal));

        BigDecimal totalSpent = expenseList.getTotal();
        if (totalSpent.compareTo(newGoal) > 0) {
            ui.printLine("Alert: You've already exceeded this new goal by "
                    + InputUtil.formatMoney(totalSpent.subtract(newGoal)) + "!");
        }
    }

    /**
     * Displays the user's current monthly salary and prompts for an updated value.
     *
     * <p>Uses {@link InputUtil#readMoney(Ui, Scanner, String)} for input parsing and validation.</p>
     *
     * @param in Scanner used to read the user's salary input.
     */
    private void handleSalary(Scanner in) {
        // Show previous input
        BigDecimal current = profile.getMonthlySalary();
        ui.printLine("Current monthly salary: " + InputUtil.formatMoney(current));

        // Prompt for update
        BigDecimal newAmount = InputUtil.readMoney(ui, in, "Enter your new monthly salary:");
        profile.setMonthlySalary(newAmount);

        ui.printLine("Salary successfully updated to: " + InputUtil.formatMoney(newAmount));
    }

    /**
     * Displays the user's current savings and prompts for an updated value.
     *
     * <p>Uses {@link InputUtil#readMoney(Ui, Scanner, String)} for input parsing and validation.</p>
     *
     * @param in Scanner used to read the user's savings input.
     */
    private void handleSavings(Scanner in) {
        // Show previous input
        BigDecimal current = profile.getCurrentSavings();
        ui.printLine("Current total savings: " + InputUtil.formatMoney(current));

        // Prompt for update
        BigDecimal newAmount = InputUtil.readMoney(ui, in, "Enter your new total savings:");
        profile.setCurrentSavings(newAmount);

        ui.printLine("Savings successfully updated to: " + InputUtil.formatMoney(newAmount));
    }

    /**
     * Displays and updates the user's BTO contribution ratio (share of payment).
     *
     * <p>Prompts the user for a decimal value (e.g., 0.60 for 60%). If parsing fails,
     * the ratio remains unchanged and a message is printed.</p>
     *
     *
     * @param in Scanner used to read the user's ratio input.
     */
    private void handleRatio(Scanner in) {
        BigDecimal current = profile.getContributionRatio();

        BigDecimal displayPercentage = current.multiply(new BigDecimal("100"));
        ui.printLine("Current BTO contribution share: " + displayPercentage + "%");

        BigDecimal newRatio = InputUtil.readRatio(ui, in, "Enter your new share (e.g., 0.60 for 60%):");

        profile.setContributionRatio(newRatio);
        ui.printLine("Contribution ratio updated!");
    }

    /**
     * Prints a summary "BTO Readiness Report" based on the user's financial profile.
     *
     * <p>Computes:
     * <ul>
     *   <li>Distance to the BTO goal (goal - current savings)</li>
     *   <li>Monthly surplus (monthly salary - total spent)</li>
     *   <li>Percentage progress towards goal</li>
     *   <li>Estimated time to reach goal in months (ceiling division)</li>
     * </ul></p>
     *
     * <p>If the goal is already reached, prints a success message.
     * If monthly surplus is zero/negative, prints that the estimate is effectively infinite.</p>
     */
    private void handleSummary() {
        BigDecimal btoGoal = profile.getBtoGoal();
        BigDecimal currentSavings = profile.getCurrentSavings();
        BigDecimal monthlySalary = profile.getMonthlySalary();
        BigDecimal totalSpent = expenseList.getTotal();

        BigDecimal distance = btoGoal.subtract(currentSavings);
        BigDecimal monthlySurplus = monthlySalary.subtract(totalSpent);

        int percentage = 0;
        if (btoGoal.compareTo(BigDecimal.ZERO) > 0) {
            percentage = currentSavings.multiply(BigDecimal.valueOf(100))
                    .divide(btoGoal, 0, RoundingMode.HALF_UP)
                    .intValue();
        }

        String estimate;
        if (distance.compareTo(BigDecimal.ZERO) <= 0) {
            estimate = "Reached! Go get that BTO!";
        } else if (monthlySurplus.compareTo(BigDecimal.ZERO) <= 0) {
            estimate = "Infinite (Surplus is $0 or negative!)";
        } else {
            int months = distance.divide(monthlySurplus, 0, RoundingMode.CEILING).intValue();
            estimate = months + " months";
        }

        ui.printLine("======= BTO Readiness Report ====");
        ui.printLine("Current Goal: " + InputUtil.formatMoney(btoGoal) + " (your share + fees)");
        ui.printLine("Current Savings: " + InputUtil.formatMoney(currentSavings) + " (" + percentage + "% reached)");
        ui.printLine("Distance to Goal: " + InputUtil.formatMoney(distance));
        ui.printLine("Monthly Surplus: " + InputUtil.formatMoney(monthlySurplus));
        ui.printLine("Estimated Goal Achievement: " + estimate);
    }
}
