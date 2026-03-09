package seedu.duke;

import seedu.duke.data.Expense;
import seedu.duke.data.Storage;
import seedu.duke.ui.Ui;
import seedu.duke.util.BtoCalculator;
import seedu.duke.util.InputUtil;
import seedu.duke.data.Profile;
import seedu.duke.data.ExpenseList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private final Profile profile;
    private final ExpenseList expenseList;
    private final Storage storage;
    private final CommandHandler handler;

    public FinTrackPro(Ui ui) {
        this.ui = new Ui();
        this.profile = new Profile();
        this.expenseList = new ExpenseList();
        this.storage = new Storage("fintrack.txt");
        this.handler = new CommandHandler(ui, profile, expenseList, storage);
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

        // Load existing data
        try {
            storage.load(profile, expenseList);
        } catch (IOException e) {
            ui.printLine("Warning: Could not load previous data. Starting fresh!");
        }

        Scanner in = new Scanner(System.in);
        String name;

        // Check if btoGoal is already set
        if (profile.getBtoGoal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            name = performInitialSetup(in);
            profile.setName(name);
        } else {
            name = profile.getName();
            ui.printLine("Welcome back " + name + "! Loading your existing profile...");
        }

        // Help Lines
        ui.printLine("");
        ui.printLine("Type 'help' to view my currently supported commands!");
        ui.printLine("Any non-command word would be echoed back to you you you");
        ui.printLine("Type 'bye' to exit!");
        ui.printLine("");

        // Main Command Loop
        String userInput = ui.readLine(in, "");
        while (!userInput.equalsIgnoreCase("bye")) {
            handleCommand(userInput, in);
            userInput = ui.readLine(in, "");
        }

        // Save everything before closing
        try {
            storage.save(profile, expenseList);
        } catch (IOException e) {
            ui.printLine("Critical Error: Your financial data could not be saved!");
        }

        ui.goodBye(name);
        in.close();
    }

    /**
     * Conducts the first-time onboarding sequence for new users.
     * * <p>This method sequentially prompts for and initializes:
     * <ul>
     * <li>User's name</li>
     * <li>Current liquid savings and monthly salary</li>
     * <li>Total BTO house price and the user's specific contribution ratio</li>
     * <li>Target deadline for the savings goal</li>
     * </ul>
     * </p>
     * * <p>It calculates the user's specific financial goal by applying the 2.5%
     * downpayment rate and legal fees to the house price, adjusted by their
     * contribution share.</p>
     *
     * @param in Scanner instance to read user inputs.
     * @return The validated or default name of the user.
     */
    private String performInitialSetup(Scanner in) {
        // 1. Name handling
        String name = ui.readLine(in, "What is your name?");
        name = name.isEmpty() ? "friend" : name.trim();
        ui.printLine("");
        ui.greet(name);

        ui.printLine("");
        ui.printLine("Hang tight... I have a few questions for you.");

        // Prompt for monthly salary, current savings, total value of BTO & individual contribution ratio
        BigDecimal savings = InputUtil.readMoney(ui, in, "How much do you currently have in savings?");
        ui.printLine("");
        profile.setCurrentSavings(savings);

        BigDecimal salary = InputUtil.readMoney(ui, in, "What is your monthly salary? (in dollars)");
        ui.printLine("");
        profile.setMonthlySalary(salary);

        BigDecimal housePrice = InputUtil.readMoney(ui, in,
                "What is the total value that you and your partner have to pay for "
                        + "the house? (in dollars)");
        ui.printLine("");

        BigDecimal newRatio = InputUtil.readRatio(ui, in,
                "What is your share of the contribution? (e.g., 0.6 for 60%):");
        profile.setContributionRatio(newRatio);
        ui.printLine("");

        // Calculate individual share of user's downpayment
        BtoCalculator result = new BtoCalculator(housePrice, newRatio);

        ui.printLine("Total downpayment needed: " + InputUtil.formatMoney(result.totalDownpayment));
        ui.printLine("Based on a " + newRatio.multiply(new BigDecimal("100")) + "% share...");
        ui.printLine("Your personal contribution needed: " + InputUtil.formatMoney(result.yourShare));
        ui.printLine("");

        profile.setBtoGoal(result.yourShare);

        // Deadline Handling
        LocalDate deadline = InputUtil.readFutureDate(ui, in, "When do you need to save this money by?" +
                " (e.g., 2028-10-24)");
        ui.printLine("");
        profile.setDeadline(deadline);

        LocalDate today = LocalDate.now();
        Period period = Period.between(today, deadline);

        // Calculate time remaining
        ui.printLine("You have " + period.getYears() + " years and "
                + period.getMonths() + " months and "
                + period.getDays() + " days remaining.");

        // Calculate how much more needs to be saved
        BigDecimal remainingToSave = result.yourShare.subtract(savings);

        if (remainingToSave.compareTo(BigDecimal.ZERO) <= 0) {
            ui.printLine("Nice! Based on your current savings, you have already met your goal.");
        } else {

            int monthsLeft = period.getYears() * 12 + period.getMonths();

            // round up if there are remaining days
            if (period.getDays() > 0) {
                monthsLeft++;
            }

            if (monthsLeft <= 0) {
                monthsLeft = 1;
            }

            BigDecimal monthlyNeeded = remainingToSave.divide(
                    BigDecimal.valueOf(monthsLeft), 2, RoundingMode.HALF_UP);

            ui.printLine("Rounding it to " + monthsLeft + " months, you would need to save "
                    + InputUtil.formatMoney(monthlyNeeded) + "/month.");
        }

        return name;
    }

    /**
     * Parses and dispatches a single line of user input.
     *
     * <p>If the input does not match a supported command, it is echoed back to the user.
     * Empty/whitespace-only input is rejected.</p>
     *
     * @param userInput Raw line entered by the user.
     * @param in Scanner used for follow-up prompts for commands that require more input
     */
    private void handleCommand(String userInput, Scanner in) {
        if (userInput.trim().isEmpty()) {
            ui.printLine("Cannot process empty description!");
            return;
        }
        String command = Parser.parseCommand(userInput);

        switch (command) {
        case "add":
            handler.handleAdd(userInput);
            break;
        case "delete":
            handler.handleDelete(userInput);
            break;
        case "list":
            printList();
            break;
        case "help":
            ui.showHelpMessage();
            break;
        case "savings":
            handler.handleSavings(in);
            break;
        case "clear":
            handler.handleClear(in);
            break;
        case "summary":
            handler.handleSummary();
            break;
        case "reset":
            handler.handleReset(in);
            break;
        default:
            ui.printLine("You said: " + userInput);
            ui.printLine("");
            break;
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
            ui.printLine("");
            return;
        }

        ui.printLine("Here is your current expenditure list!");

        for (int i = 0; i < expenseList.size(); i++) {
            Expense expense = expenseList.get(i);
            String formattedAmount = InputUtil.formatMoney(expense.getAmount());
            ui.printLine( (i + 1) +  ". " + formattedAmount);
        }

        BigDecimal totalSpent = expenseList.getTotal();
        ui.printLine("Total Expenditure: $" +  totalSpent);
        ui.printLine("");
    }

}
