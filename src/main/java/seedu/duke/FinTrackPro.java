package seedu.duke;

import seedu.duke.data.Expense;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.ArchivedExpense;
import seedu.duke.data.MonthlyArchive;
import seedu.duke.data.Profile;
import seedu.duke.data.RecurringExpense;
import seedu.duke.data.RecurringExpenseList;
import seedu.duke.data.Storage;
import seedu.duke.ui.Ui;
import seedu.duke.util.BtoCalculator;
import seedu.duke.util.InputUtil;

import java.util.logging.Logger;
import java.util.logging.Level;

import seedu.duke.util.LoggerUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

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
    private static final Logger logger = LoggerUtil.getLogger(FinTrackPro.class);
    private final Ui ui;
    private final Profile profile;
    private final ExpenseList expenseList;
    private final RecurringExpenseList recurringExpenseList;
    private final Storage storage;
    private final CommandHandler handler;

    public FinTrackPro(Ui ui) {
        assert ui != null : "Ui must not be null";
        this.ui = new Ui();
        this.profile = new Profile();
        this.expenseList = new ExpenseList();
        this.recurringExpenseList = new RecurringExpenseList();
        this.storage = new Storage("fintrack.txt");
        this.handler = new CommandHandler(ui, profile, expenseList, recurringExpenseList, storage);

        logState("startup", "run() entry", "storagePath=fintrack.txt");
    }

    private void logState(String state, String expectedNext, String values) {
        String safeValues = Objects.requireNonNull(values);
        logger.info("state=" + state + " | expected=" + expectedNext + " | " + safeValues);
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
        logState("run.start", "show welcome and load persisted data", "profileName=" + profile.getName());
        ui.showWelcome();

        // Load existing data
        try {
            storage.load(profile, expenseList, recurringExpenseList);
            logState("run.load.success", "check if initial setup is required",
                    "profileName=" + profile.getName()
                            + ", btoGoal=" + profile.getBtoGoal()
                            + ", oneOffCount=" + expenseList.size()
                            + ", recurringCount=" + recurringExpenseList.size());
        } catch (IOException e) {
            logger.log(Level.WARNING,
                    "state=run.load.failed | expected=continue with fresh in-memory profile | reason="
                            + e.getMessage(),
                    e);
            ui.printLine("Warning: Could not load previous data. Starting fresh!");
        }

        Scanner in = new Scanner(System.in);
        String name;

        // Check if btoGoal is already set
        if (profile.getBtoGoal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            logState("run.profile.new", "perform initial setup workflow",
                    "btoGoal=" + profile.getBtoGoal());
            name = performInitialSetup(in);
            profile.setName(name);
            logState("run.profile.initialized", "enter command loop",
                    "profileName=" + name
                            + ", btoGoal=" + profile.getBtoGoal()
                            + ", deadline=" + profile.getDeadline());
        } else {
            name = profile.getName();
            logState("run.profile.existing", "enter command loop",
                    "profileName=" + name + ", btoGoal=" + profile.getBtoGoal());
            ui.printLine("Welcome back " + name + "! Loading your existing profile...");
        }

        ui.printLine("");
        ui.printLine("Type 'help' to view my currently supported commands!");
        ui.printLine("Any non-command word would be echoed back to you you you");
        ui.printLine("Type 'bye' to exit!");
        ui.printLine("");

        logState("run.command-loop", "read command until bye",
                "profileName=" + name
                        + ", oneOffCount=" + expenseList.size()
                        + ", recurringCount=" + recurringExpenseList.size());
        String userInput = ui.readLine(in, "");
        while (!userInput.equalsIgnoreCase("bye")) {
            handleCommand(userInput, in);

            try {
                storage.save(profile, expenseList, recurringExpenseList);
                logger.fine("Auto-save successful after command: " + userInput);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Auto-save failed!", e);
                ui.printLine("Warning: Data could not be saved to disk.");
            }

            userInput = ui.readLine(in, "");
        }
        logState("run.command-loop.exit", "persist in-memory data", "exitCommand=bye");

        // Save everything before closing
        try {
            storage.save(profile, expenseList, recurringExpenseList);
            logState("run.save.success", "shutdown ui and scanner",
                    "profileName=" + profile.getName()
                            + ", oneOffCount=" + expenseList.size()
                            + ", recurringCount=" + recurringExpenseList.size());
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                    "state=run.save.failed | expected=shutdown still proceeds | reason=" + e.getMessage(), e);
            ui.printLine("Critical Error: Your financial data could not be saved!");
        }

        logState("run.end", "process exits", "profileName=" + name);
        ui.goodBye(name);
        in.close();
    }

    /**
     * Conducts the first-time onboarding sequence for new users.
     * * <p>This method sequentially prompts for and initializes:
     * <ul>
     * <li>User's name</li>
     * <li>Current liquid savings and monthly allowance</li>
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
        logState("setup.start", "collect name, finances and deadline", "scannerReady=true");

        // 1. Name handling
        String name;
        while (true) {
            name = ui.readLine(in, "What is your name?").trim();
            if (name.contains("|")) {
                ui.printLine("Name cannot contain the '|' character. Try again.");
                continue;
            }
            if (name.isEmpty()) {
                name = "friend";
            }
            break;
        }
        logState("setup.name.captured", "collect current savings", "name=" + name);

        ui.printLine("");
        ui.greet(name);

        ui.printLine("");
        ui.printLine("Hang tight... I have a few questions for you.");

        // Prompt for monthly allowance, current savings, total value of BTO & individual contribution ratio
        BigDecimal savings = InputUtil.readMoney(ui, in, "How much do you currently have in savings?");
        ui.printLine("");
        profile.setCurrentSavings(savings);
        logState("setup.savings.captured", "collect monthly allowance", "currentSavings=" + savings);

        BigDecimal allowance = InputUtil.readMoney(ui, in, "What is your monthly allowance? (in dollars)");
        ui.printLine("");
        profile.setMonthlyAllowance(allowance);
        logState("setup.allowance.captured", "collect house price", "monthlyAllowance=" + allowance);

        BigDecimal housePrice = InputUtil.readMoney(ui, in,
                "What is the total value that you and your partner have to pay for "
                        + "the house? (in dollars)");
        profile.setHousePrice(housePrice);
        logState("setup.house-price.captured", "collect contribution ratio", "housePrice=" + housePrice);
        ui.printLine("");

        BigDecimal newRatio = InputUtil.readRatio(ui, in,
                "What is your share of the contribution? (e.g., 0.6 for 60%):");
        profile.setContributionRatio(newRatio);
        logState("setup.ratio.captured", "calculate user BTO goal", "contributionRatio=" + newRatio);

        ui.printLine("");

        // Calculate individual share of user's downpayment
        BtoCalculator result = new BtoCalculator(housePrice, newRatio);
        logState("setup.bto-calculated", "persist goal and collect deadline",
                "totalDownpayment=" + result.totalDownpayment + ", userShare=" + result.yourShare);

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
        logState("setup.deadline.captured", "compute timeline and monthly requirement",
                "deadline=" + deadline + ", today=" + LocalDate.now());

        LocalDate today = LocalDate.now();
        Period period = Period.between(today, deadline);

        // Calculate time remaining
        ui.printLine("You have " + period.getYears() + " years and "
                + period.getMonths() + " months and "
                + period.getDays() + " days remaining.");

        // Calculate how much more needs to be saved
        BigDecimal remainingToSave = result.yourShare.subtract(savings);

        if (remainingToSave.compareTo(BigDecimal.ZERO) <= 0) {
            logState("setup.goal.already-met", "enter command loop", "remainingToSave=" + remainingToSave);
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

            logState("setup.goal.in-progress", "enter command loop",
                    "remainingToSave=" + remainingToSave
                            + ", monthsLeft=" + monthsLeft
                            + ", monthlyNeeded=" + monthlyNeeded);

            ui.printLine("Rounding it to " + monthsLeft + " months, you would need to save "
                    + InputUtil.formatMoney(monthlyNeeded) + "/month.");
        }
        logState("setup.end", "caller stores name in profile", "name=" + name + ", btoGoal=" + profile.getBtoGoal());
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
        assert userInput != null : "userInput should not be null";
        assert in != null : "Scanner should not be null";

        logState("command.received", "parse command token", "rawInput='" + userInput + "'");

        if (userInput.trim().isEmpty()) {
            logger.warning("state=command.invalid.empty | expected=prompt for non-empty command | rawInput=''");
            ui.printLine("Cannot process empty description!");
            return;
        }
        String command = Parser.parseCommand(userInput);
        logState("command.parsed", "dispatch to command handler",
                "command=" + command + ", rawInput='" + userInput + "'");

        switch (command) {
        case "add":
            logState("command.dispatch", "handler.handleAdd", "command=add");
            handler.handleAdd(userInput);
            break;
        case "delete":
            logState("command.dispatch", "handler.handleDelete", "command=delete");
            handler.handleDelete(userInput);
            break;
        case "deleterecurring":
            logState("command.dispatch", "handler.handleDeleteRecurring", "command=deleterecurring");
            handler.handleDeleteRecurring(userInput);
            break;
        case "list":
            logState("command.dispatch", "printList", "command=list");
            printList();
            break;
        case "help":
            logState("command.dispatch", "ui.showHelpMessage", "command=help");
            ui.showHelpMessage();
            break;
        case "savings":
            logState("command.dispatch", "handler.handleSavings", "command=savings");
            handler.handleSavings(in);
            break;
        case "allowance":
            logState("command.dispatch", "handler.handleAllowance", "command=allowance");
            handler.handleAllowance(in);
            break;
        case "ratio":
            logState("command.dispatch", "handler.handleRatio", "command=ratio");
            handler.handleRatio(in);
            break;
        case "clear":
            logger.warning(
                    "state=command.dispatch.destructive | expected="
                            + "handler.handleClear with confirmation | command=clear");
            handler.handleClear(in);
            break;
        case "summary":
            logState("command.dispatch", "handler.handleSummary", "command=summary");
            handler.handleSummary();
            break;
        case "reset":
            logger.warning(
                    "state=command.dispatch.destructive | expected="
                            + "handler.handleReset with confirmation | command=reset");
            handler.handleReset(in);
            break;
        case "sort":
            logState("command.dispatch", "handler.handleSort", "command=sort, rawInput='" + userInput + "'");
            handler.handleSort(userInput);
            break;
        case "save":
            logger.info("state=command.dispatch | expected=handler.handleSaveMonth | command=save");
            handler.handleSaveMonth();
            break;
        default:
            logger.warning("state=command.unknown | expected=echo input back to user | rawInput='" + userInput + "'");
            ui.printLine("You said: " + userInput);
            ui.printLine("");
            break;
        }
    }

    /**
     * Prints the current expense list including expense name, cost and category assigned
     * and the total expenditure so far.
     *
     * <p>If the list is empty, prints a message indicating no expenses exist.</p>
     *
     * <p>Also checks the user's spending goal from {@link Profile}.
     * If total expenditure exceeds the goal, prints an alert with the exceeded amount.</p>
     */
    private void printList(){
        logState("list.start", "render recurring and one-off expenses", "oneOffCount=" + expenseList.size()
                + ", recurringCount=" + recurringExpenseList.size());

        MonthlyArchive archive = new MonthlyArchive(".");
        int currentMonth = profile.getCurrentMonth();
        BigDecimal totalAllMonths = BigDecimal.ZERO;

        // Display recurring expenses (they're not month-specific)
        if (!recurringExpenseList.isEmpty()) {
            ui.printLine("Here are your recurring monthly commitments!");
            for (int i = 0; i < recurringExpenseList.size(); i++) {
                RecurringExpense recurringExpense = recurringExpenseList.get(i);
                ui.printLine((i + 1) + ". "
                        + recurringExpense.getName() + " "
                        + InputUtil.formatMoney(recurringExpense.getAmount()) + " "
                        + "[" + recurringExpense.getCategory() + "]");
            }
            ui.printLine("");
        }

        // Display archived expenses from previous months
        boolean hasArchivedExpenses = false;
        for (int month = 1; month < currentMonth; month++) {
            try {
                java.util.List<ArchivedExpense> archivedExpenses = 
                    archive.loadMonthlyExpenses(month);
                
                if (!archivedExpenses.isEmpty()) {
                    hasArchivedExpenses = true;
                    ui.printLine("*** MONTH " + month + " EXPENSES");
                    BigDecimal monthTotal = BigDecimal.ZERO;
                    
                    for (int i = 0; i < archivedExpenses.size(); i++) {
                        ArchivedExpense expense = archivedExpenses.get(i);
                        ui.printLine((i + 1) + ". "
                                + expense.getName() + " "
                                + InputUtil.formatMoney(new BigDecimal(expense.getAmount())) + " "
                                + "[" + expense.getCategory() + "]");
                        monthTotal = monthTotal.add(new BigDecimal(expense.getAmount()));
                    }
                    ui.printLine("Month " + month + " Total: " 
                            + InputUtil.formatMoney(monthTotal));
                    ui.printLine("");
                    totalAllMonths = totalAllMonths.add(monthTotal);
                }
            } catch (IOException e) {
                logger.log(java.util.logging.Level.WARNING,
                    "Failed to load archived expenses for Month " + month, e);
            } catch (NumberFormatException e) {
                logger.log(java.util.logging.Level.WARNING,
                    "Corrupted amount in archive for Month " + month + ": " + e.getMessage(), e);
            }
        }

        // Display current month's expenses
        if (!expenseList.isEmpty()) {
            ui.printLine("*** MONTH " + currentMonth + " EXPENSES");
            for (int i = 0; i < expenseList.size(); i++) {
                Expense expense = expenseList.get(i);
                ui.printLine((i + 1) + ". "
                        + expense.getName() + " "
                        + InputUtil.formatMoney(expense.getAmount()) + " "
                        + "[" + expense.getCategory() + "]");
            }
            ui.printLine("Month " + currentMonth + " Total: " 
                    + InputUtil.formatMoney(expenseList.getTotal()));
            ui.printLine("");
        } else if (currentMonth > 1 && hasArchivedExpenses) {
            // Current month has no expenses but previous months do
            ui.printLine("*** MONTH " + currentMonth + " EXPENSES");
            ui.printLine("No expenses recorded yet.");
            ui.printLine("");
        }

        // If there are no expenses at all
        if (!hasArchivedExpenses && expenseList.isEmpty()) {
            logState("list.empty", "return to command loop", "oneOffCount=0, recurringCount=0");
            ui.printLine("Your one time off expense list is as empty as my wallet. Go spend some money!");
            ui.printLine("");
            return;
        }

        BigDecimal combinedTotal = expenseList.getTotal().add(recurringExpenseList.getTotal())
                .add(totalAllMonths);
        logState("list.total.computed", "render total and return to command loop",
                "totalAllMonths=" + totalAllMonths
                        + ", oneOffTotal=" + expenseList.getTotal()
                        + ", recurringTotal=" + recurringExpenseList.getTotal()
                        + ", combinedTotal=" + combinedTotal);
        ui.printLine("Total Expenditure (All Months + Recurring): " 
                + InputUtil.formatMoney(combinedTotal));
        ui.printLine("");
    }
}
