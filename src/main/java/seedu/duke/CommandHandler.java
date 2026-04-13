package seedu.duke;

import seedu.duke.category.Category;
import seedu.duke.data.Expense;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.MonthlyArchive;
import seedu.duke.data.Profile;
import seedu.duke.data.RecurringExpense;
import seedu.duke.data.RecurringExpenseList;
import seedu.duke.data.Storage;
import seedu.duke.data.SummaryReport;
import seedu.duke.exception.InvalidAmountException;
import seedu.duke.exception.InvalidCategoryException;
import seedu.duke.exception.InvalidIndexException;
import seedu.duke.ui.Ui;
import seedu.duke.util.InputUtil;
import seedu.duke.util.LoggerUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.logging.Logger;

public class CommandHandler {
    private static final String SAVE_CONFIRMATION_PROMPT =
            "Are you sure you want to save this month's expenditures? You will not be able to update once you press "
                    + "Y\n(press Y to confirm, any key to abort)";

    private static final String ADD_FORMAT_MESSAGE = "Invalid add format. Use: add NAME AMOUNT CATEGORY "
            + "[RECURRING]. Try again!\n";

    /**
     * Logger for recording command handler events.
     * Routes all output to the central {@code logs/fintrack.log} via {@link LoggerUtil}.
     */
    private static final Logger logger = LoggerUtil.getLogger(CommandHandler.class);

    private final Ui ui;
    private final Profile profile;
    private final ExpenseList expenseList;
    private final RecurringExpenseList recurringExpenseList;
    private final Storage storage;

    private static class AddArguments {
        private final String name;
        private final BigDecimal amount;
        private final Category category;
        private final boolean recurring;

        private AddArguments(String name, BigDecimal amount, Category category, boolean recurring) {
            this.name = name;
            this.amount = amount;
            this.category = category;
            this.recurring = recurring;
        }
    }

    public CommandHandler(Ui ui, Profile profile, ExpenseList expenseList, RecurringExpenseList recurringExpenseList,
                          Storage storage) {
        assert ui != null : "Ui should not be null";
        assert profile != null : "Profile should not be null";
        assert expenseList != null : "ExpenseList should not be null";
        assert recurringExpenseList != null : "RecurringExpenseList should not be null";
        assert storage != null : "Storage should not be null";

        this.ui = ui;
        this.profile = profile;
        this.expenseList = expenseList;
        this.recurringExpenseList = recurringExpenseList;
        this.storage = storage;
    }


    /**
     * Adds an expense entry into the {@link ExpenseList}.
     *
     * <p>Expected format: {@code add <name> <amount> <category>}</p>
     *
     * <p>The final token is treated as the category, the second-last token as the amount,
     * and all preceding tokens are treated as the expense name. This allows expense names
     * to contain multiple words.</p>
     *
     * <ul>
     *   <li>Rejects missing input</li>
     *   <li>Rejects blank expense names</li>
     *   <li>Rejects non-numeric amounts</li>
     *   <li>Rejects negative values</li>
     *   <li>Rejects values with more than 2 decimal places</li>
     *   <li>Rejects invalid categories</li>
     * </ul>
     *
     * <p>On success, prints the new expense and the updated running total.</p>
     *
     * @param userInput Full command line entered by the user, beginning with {@code add}.
     */
    public void handleAdd(String userInput) {
        assert userInput != null : "User input should not be null";
        String normalizedInput = userInput.trim();
        assert normalizedInput.startsWith("add") : "Input should start with 'add'";

        try {
            AddArguments args = parseAddArguments(normalizedInput);
            if (args.recurring) {
                int oldSize = recurringExpenseList.size();
                recurringExpenseList.add(new RecurringExpense(args.name, args.amount, args.category));

                assert recurringExpenseList.size() == oldSize + 1
                        : "Recurring expense list size should increase by one after add";

                logger.info("handleAdd succeeded | recurring expense | name: " + args.name
                        + " | amount: $" + args.amount
                        + " | category: " + args.category);

                ui.printLine("Added recurring expense: " + recurringExpenseList.get(recurringExpenseList.size() - 1));
                ui.printLine("Recurring Total: " + InputUtil.formatMoney(recurringExpenseList.getTotal()));
                ui.printLine("");
                return;
            }
            BigDecimal oldTotal = expenseList.getTotal();
            expenseList.add(args.name, args.amount, args.category);

            assert expenseList.getTotal().compareTo(oldTotal.add(args.amount)) == 0
                    : "Expense total should increase by added amount";

            logger.info("handleAdd succeeded | name: " + args.name
                    + " | amount: $" + args.amount
                    + " | category: " + args.category
                    + " | new total: $" + expenseList.getTotal());

            ui.printLine("Added expense: " + expenseList.get(expenseList.size() - 1));
            ui.printLine("Month " + profile.getCurrentMonth() + " Total: "
                    + InputUtil.formatMoney(expenseList.getTotal()));
            ui.printLine("");

        } catch (InvalidAmountException | InvalidCategoryException e) {
            ui.printLine(e.getMessage());
            ui.printLine("");
        }
    }
    /**
     * Parses the arguments of an {@code add} command into an expense name,
     * amount, and category.
     *
     * <p>The final token is treated as the category, the second-last token
     * as the amount, and all preceding tokens are treated as the expense name.
     * This allows expense names to contain multiple words.</p>
     *
     * @param userInput Full command line entered by the user, beginning with {@code add}.
     * @return A parsed {@code AddArguments} object containing the expense details.
     * @throws InvalidAmountException If the input is missing fields, contains a blank name,
     *                                or contains an invalid amount.
     */
    private AddArguments parseAddArguments(String userInput) throws InvalidAmountException, InvalidCategoryException {
        assert userInput != null : "User input should not be null";
        String normalizedInput = userInput.trim();
        assert normalizedInput.startsWith("add") : "Input should start with 'add'";

        String rest = normalizedInput.substring("add".length()).trim();

        if (rest.isEmpty()) {
            logger.warning("handleAdd rejected | reason: empty input");
            throw new InvalidAmountException("Format: add <NAME> <AMOUNT> <CATEGORY> [RECURRING]\n");
        }

        String[] parts = rest.split("\\s+");

        int recurringCount = 0;
        java.util.ArrayList<String> filteredParts = new java.util.ArrayList<>();

        for (String part : parts) {
            if (part.equalsIgnoreCase("recurring")) {
                recurringCount++;
            } else {
                filteredParts.add(part);
            }
        }

        boolean recurring = recurringCount > 0;

        if (recurringCount > 1) {
            logger.warning("handleAdd rejected | reason: multiple recurring flags");
            throw new InvalidAmountException("Format: add <NAME> <AMOUNT> <CATEGORY> [RECURRING]\n");
        }

        if ((!recurring && filteredParts.size() < 3) || (recurring && filteredParts.size() < 3)) {
            logger.warning("handleAdd rejected | reason: insufficient arguments");
            throw new InvalidAmountException("Format: add <NAME> <AMOUNT> <CATEGORY> [RECURRING]\n");
        }

        // Detect trailing junk such as:
        // add lunch 5 FOOD extra arguments
        // add lunch 5 FOOD recurring extra arguments
        if (filteredParts.size() >= 4) {
            String possibleAmount = filteredParts.get(filteredParts.size() - 2);
            String possibleCategory = filteredParts.get(filteredParts.size() - 1);

            boolean amountAtExpectedSpot =
                    possibleAmount.matches("-\\d+(\\.\\d+)?") || possibleAmount.matches("\\d+(\\.\\d+)?");
            boolean categoryAtExpectedSpot = Category.isValid(possibleCategory);

            if (!amountAtExpectedSpot || !categoryAtExpectedSpot) {
                boolean hasNumericEarlier = false;
                boolean hasValidCategoryEarlier = false;

                for (int i = 0; i < filteredParts.size() - 2; i++) {
                    String token = filteredParts.get(i);
                    if (token.matches("-\\d+(\\.\\d+)?") || token.matches("\\d+(\\.\\d+)?")) {
                        hasNumericEarlier = true;
                    }
                    if (Category.isValid(token)) {
                        hasValidCategoryEarlier = true;
                    }
                }

                if (hasNumericEarlier && hasValidCategoryEarlier) {
                    logger.warning("handleAdd rejected | reason: too many trailing arguments");
                    throw new InvalidAmountException("Format: add <NAME> <AMOUNT> <CATEGORY> [RECURRING]\n");
                }
            }
        }

        String categoryString = filteredParts.get(filteredParts.size() - 1);
        String amountString = filteredParts.get(filteredParts.size() - 2);
        int nameEndExclusive = filteredParts.size() - 2;

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < nameEndExclusive; i++) {
            if (i > 0) {
                nameBuilder.append(" ");
            }
            nameBuilder.append(filteredParts.get(i));
        }

        String name = nameBuilder.toString();

        if (name.isBlank()) {
            logger.warning("handleAdd rejected | reason: blank expense name");
            throw new InvalidAmountException("Expense name cannot be empty.\n");
        }

        if (name.contains("|")) {
            logger.warning("handleAdd rejected | reason: expense name contains reserved character '|'");
            throw new InvalidAmountException("Expense name cannot contain the '|' character.\n");
        }

        if (!Category.isValid(categoryString)) {
            logger.warning("handleAdd rejected | reason: invalid category " + categoryString);
            throw new InvalidCategoryException("Invalid category! Valid categories: "
                    + "FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER\n");
        }

        if (amountString.matches("-\\d+(\\.\\d+)?")) {
            throw new InvalidAmountException("You cannot add a negative expenditure! Try again!\n");
        }

        if (!amountString.matches("\\d+(\\.\\d+)?")) {
            throw new InvalidAmountException(ADD_FORMAT_MESSAGE);
        }

        BigDecimal amount = parseAmount(amountString);
        Category category = Category.fromString(categoryString);

        return new AddArguments(name, amount, category, recurring);
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
    public void handleDelete(String userInput) {
        assert userInput != null : "User input should not be null";
        //trims whitespaces
        String normalizedInput = userInput.trim();
        assert normalizedInput.startsWith("delete") : "Input should start with 'delete'";

        try {
            String rest = getString(normalizedInput, "delete");

            int index = parseDeleteIndex(rest);

            BigDecimal oldTotal = expenseList.getTotal();
            Expense removed = expenseList.delete(index);

            assert removed != null : "Deleted expense should not be null";
            assert expenseList.getTotal().compareTo(oldTotal.subtract(removed.getAmount())) == 0
                    : "Expense total should decrease by removed amount";
            // Log at INFO: a successful delete is a key application state change
            logger.info("handleDelete succeeded | index: " + index
                            + " | removed: $" + removed.getAmount()
                            + " | new total: $" + expenseList.getTotal());

            ui.printLine("Deleted expense #" + index + ": " + removed);
            ui.printLine("Current Total: $" + expenseList.getTotal());
            ui.printLine("");
        } catch (InvalidIndexException e) {
            // Log at WARNING: user provided an invalid index that was rejected
            logger.warning("handleDelete rejected | reason: " + e.getMessage().trim());
            ui.printLine(e.getMessage());
        }
    }

    private static String getString(String userInput, String commandWord) throws InvalidIndexException {
        String rest = userInput.substring(commandWord.length()).trim();

        String[] tokens = rest.split("\\s+");
        if (rest.isEmpty() || tokens.length != 1) {
            throw new InvalidIndexException(
                    """
                            Wrong Format Bro!!!
                            For one-off expenses: delete <INDEX>
                            For recurring expenses: deleterecurring <INDEX>
                            """);
        }
        return rest;
    }

    /**
     * Handles the deletion of a recurring expense based on user input.
     *
     * <p>Expected input format:
     * {@code deleterecurring <index>}</p>
     *
     * <p>This method parses the index provided by the user, validates it against the
     * {@link RecurringExpenseList}, and removes the corresponding recurring expense.</p>
     *
     * <p>Upon successful deletion:
     * <ul>
     *     <li>The recurring expense is removed from the list.</li>
     *     <li>The total recurring expenditure is updated accordingly.</li>
     *     <li>A confirmation message is displayed to the user.</li>
     * </ul>
     *
     * <p>If the input format is invalid or the index is out of bounds, an appropriate
     * error message is shown to the user.</p>
     *
     * @param userInput Full user input string starting with {@code deleterecurring}.
     *                  Must not be {@code null} and must start with {@code deleterecurring}.
     */
    public void handleDeleteRecurring(String userInput) {
        assert userInput != null : "User input should not be null";
        String normalizedInput = userInput.trim();
        assert normalizedInput.startsWith("deleterecurring") : "Input should start with 'deleterecurring'";

        try {
            String rest = getString(normalizedInput, "deleterecurring");

            if (rest.isEmpty()) {
                throw new InvalidIndexException("Format: deleterecurring <index>\n");
            }

            int index = Parser.parseIndex(rest);

            if (!recurringExpenseList.isValidIndex(index)) {
                throw new InvalidIndexException("Invalid recurring expense index!\n");
            }

            BigDecimal oldTotal = recurringExpenseList.getTotal();
            RecurringExpense removed = recurringExpenseList.delete(index);

            assert removed != null : "Deleted recurring expense should not be null";
            assert recurringExpenseList.getTotal().compareTo(oldTotal.subtract(removed.getAmount())) == 0
                    : "Recurring expense total should decrease by removed amount";

            ui.printLine("Deleted recurring expense #" + index + ": " + removed);
            ui.printLine("Recurring Total: $" + recurringExpenseList.getTotal());
            ui.printLine("");

        } catch (InvalidIndexException e) {
            ui.printLine(e.getMessage());
            ui.printLine("");
        }
    }

    //@@ jairusljr
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
    public void handleClear(Scanner in) {
        assert in != null : "Scanner should not be null";

        ui.printLine("WARNING: This will permanently delete ALL one-off expenses. Are you sure? (Input Y to clear)");
        String response = ui.readLine(in, "").trim().toLowerCase();

        if (response.equals("y")) {
            expenseList.clear();

            // Log at INFO: clearing all expenses is a significant application event
            logger.info("handleClear executed | all one-off expenses cleared by user confirmation");
            ui.printLine("Current month's one-off expenses have been wiped clean. Recurring expenses are kept.");
            ui.printLine("");
        } else {
            // Log at INFO: user chose not to clear — still worth recording the decision
            logger.info("handleClear cancelled | user did not confirm");

            ui.printLine("Clear cancelled. Your data is still there, bro.");
            ui.printLine("");
        }
    }

    //@@ ak2003x
    /**
     * Adds a specified amount to the user's current total savings.
     *
     * @param in Scanner used to read the user's deposit input.
     */
    public void handleSavings(Scanner in) {
        assert in != null : "Scanner should not be null";

        BigDecimal current = profile.getCurrentSavings();
        assert current != null : "Current savings should not be null";

        ui.printLine("Current total savings: " + InputUtil.formatMoney(current));

        // Prompt for the amount to add
        BigDecimal depositAmount = InputUtil.readMoney(ui, in, "Enter amount to add to your savings:");
        assert depositAmount.compareTo(BigDecimal.ZERO) >= 0 : "Deposit amount should not be negative";

        // Update profile by adding to the current balance
        BigDecimal updatedSavings = current.add(depositAmount);
        profile.setCurrentSavings(updatedSavings);
        assert profile.getCurrentSavings().compareTo(updatedSavings) == 0
                : "Profile savings should match updated savings";

        // Log at INFO: savings update is a key profile state change
        logger.info("handleSavings executed | deposited: $" + depositAmount
                + " | new savings total: $" + updatedSavings);

        ui.printLine("");
        ui.printLine("Transaction successful!");
        ui.printLine("Added: " + InputUtil.formatMoney(depositAmount));
        ui.printLine("New total savings: " + InputUtil.formatMoney(updatedSavings));
        ui.printLine("");
    }

    /**
     * Displays the current monthly allowance and prompts the user to update it.
     *
     * @param in Scanner used to read the user's input.
     */
    public void handleAllowance(Scanner in) {
        assert in != null : "Scanner should not be null";
        BigDecimal current = profile.getMonthlyAllowance();
        assert current != null : "Current allowance in profile should not be null";

        ui.promptForAllowance(current);
        BigDecimal newAllowance = InputUtil.readMoney(ui, in, "Enter new monthly allowance:");
        ui.printLine("");

        profile.setMonthlyAllowance(newAllowance);
        assert profile.getMonthlyAllowance().equals(newAllowance) : "Profile failed to update allowance";

        logger.info("handleAllowance executed | old: " + current + " | new: " + newAllowance);

        ui.printLine("Success! Your monthly allowance is now " + InputUtil.formatMoney(newAllowance));
        ui.printLine("");
    }

    /**
     * Displays the current contribution ratio and prompts the user to update it.
     * The ratio must be between 0.01 (1%) and 1.0 (100%).
     *
     * @param in Scanner used to read the user's input.
     */
    public void handleRatio(Scanner in) {
        assert in != null : "Scanner should not be null";

        BigDecimal current = profile.getContributionRatio();
        assert current != null : "Current ratio in profile should not be null";

        // Show current state
        ui.promptForRatio(current);
        BigDecimal newRatio = InputUtil.readRatio(ui, in, "Enter new ratio (0.01 to 1.0):");
        ui.printLine("");

        profile.setContributionRatio(newRatio);
        assert profile.getContributionRatio().equals(newRatio) : "Profile failed to update ratio";

        logger.info("handleRatio executed | old: " + current + " | new: " + newRatio);

        ui.printLine("Success! Your contribution ratio is now " + newRatio);
        ui.printLine("");
    }

    /**
     * Computes and displays a BTO Readiness Report based on the user's current financial profile.
     *
     * <p>Calculates the following metrics from {@link Profile} and {@link ExpenseList}:
     * <ul>
     *   <li>Distance to goal (BTO goal - current savings)</li>
     *   <li>Monthly surplus (monthly allowance - total expenses)</li>
     *   <li>Percentage progress towards the BTO goal</li>
     *   <li>Estimated months to reach the goal, or a status message if already reached
     *       or surplus is non-positive</li>
     * </ul>
     * </p>
     *
     * <p>Results are passed to {@link Ui#showSummaryReport} for display.</p>
     */
    public void handleSummary() {

        // Log at INFO: summary generation is a deliberate user-initiated action
        logger.info("handleSummary executed | generating BTO readiness report");
        ui.showSummaryReport(new SummaryReport(profile, expenseList, recurringExpenseList));
    }

    //@@ jairusljr
    /**
     * Completely resets the user profile and all expense lists after confirmation,
     * then signals to the caller whether a re-setup sequence should be triggered.
     *
     * <p>If the user confirms with {@code "y"}, all in-memory data (profile, one-off expenses,
     * and recurring expenses) is wiped and the save file is overwritten with the empty state.
     * Returns {@code true} to indicate that {@link FinTrackPro} should immediately run
     * {@code performInitialSetup()} without requiring the user to restart.</p>
     *
     * <p>If the user does not confirm, no data is modified and {@code false} is returned.</p>
     *
     * @param in Scanner used for user confirmation.
     * @return {@code true} if the reset was confirmed and completed; {@code false} if cancelled.
     */
    public boolean handleReset(Scanner in) {
        assert in != null : "Scanner should not be null";

        ui.printLine("WARNING: This will wipe your profile and ALL expenses. Type 'Y' to continue: ");
        String response = ui.readLine(in, "").trim().toLowerCase();

        if (response.equals("y")) {
            profile.reset();
            expenseList.clear();
            recurringExpenseList.clear();

            assert expenseList.getTotal().compareTo(BigDecimal.ZERO) == 0
                    : "Expense total should be zero after reset";

            try {
                storage.save(profile, expenseList, recurringExpenseList);
                logger.info("handleReset executed | All data cleared and save file overwritten");
                ui.printLine("System reset successful. Starting fresh setup...");
                ui.printLine("");
            } catch (IOException e) {
                logger.warning("handleReset | Disk write failed: " + e.getMessage());
                ui.printLine("Error: Could not reset the save file on disk.");
            }
            return true; // signals that re-setup is needed

        } else {
            logger.info("handleReset cancelled | user did not confirm");
            ui.printLine("Reset aborted. Your data is safe!");
            ui.printLine("");
            return false;
        }
    }

    //@@ author
    /**
     * Parses and validates an amount string.
     *
     * <p>The amount must be a valid non-negative number with at most
     * 2 decimal places.</p>
     *
     * @param amountString Raw amount string to parse.
     * @return Parsed amount as a {@link BigDecimal}.
     * @throws InvalidAmountException If the amount is empty, non-numeric,
     *                                negative, or has more than 2 decimal places.
     */
    public BigDecimal parseAmount(String amountString) throws InvalidAmountException {
        assert amountString != null : "Amount input should not be null";

        if (amountString.isBlank()) {
            logger.warning("parseAmount rejected | reason: empty input");
            throw new InvalidAmountException("Amount cannot be empty.\n");
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountString);
        } catch (NumberFormatException e) {
            logger.warning("parseAmount rejected | reason: non-numeric input '" + amountString + "'");
            throw new InvalidAmountException("Amount must be a valid number.\n");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            logger.warning("parseAmount rejected | reason: negative value " + amount);
            throw new InvalidAmountException("Amount cannot be negative bro who you trying to scam?\n");
        }

        if (amount.scale() > 2) {
            logger.warning("parseAmount rejected | reason: more than 2 decimal places, value: " + amount);
            throw new InvalidAmountException("Amount must not exceed 2 decimal places bro!\n");
        }

        assert amount.compareTo(BigDecimal.ZERO) >= 0 : "Amount should be non-negative";

        logger.fine("parseAmount succeeded | parsed value: $" + amount);
        return amount;
    }

    public int parseDeleteIndex(String rest) throws InvalidIndexException {
        assert rest != null : "Delete index input should not be null";

        // If there is no input after delete
        if (rest.isEmpty()) {
            logger.warning("parseDeleteIndex rejected | reason: empty input");
            throw new InvalidIndexException("Format: delete <index> bro! where is the INDEXXX\n");
        }

        int index = Parser.parseIndex(rest);

        if (!expenseList.isValidIndex(index)) {
            logger.warning("parseDeleteIndex rejected | reason: index " + index
                    + " out of range, list size: " + expenseList.size());
            throw new InvalidIndexException("Invalid index bro! do you even know how much you've spent?\n");
        }

        // Log at FINE: successful parse is a low-level detail, not a key app event
        logger.fine("parseDeleteIndex succeeded | parsed index: " + index);
        
        return index;
    }

    /**
     * Sorts the expense list by the specified sort argument.
     *
     * <p>Expected format: {@code sort <argument>}</p>
     *
     * <p>Recognised arguments:
     * <ul>
     *   <li>{@code category} — sorts expenses by category sort order</li>
     *   <li>{@code recent} — restores expenses to original insertion order</li>
     * </ul>
     * </p>
     *
     * <p>Any other argument is rejected with an error message.</p>
     *
     * @param userInput Full command line entered by the user, beginning with {@code sort}.
     */
    public void handleSort(String userInput) {
        assert userInput != null : "User input should not be null";
        assert userInput.startsWith("sort") : "Input should start with 'sort'!";

        String arg = userInput.substring("sort".length()).trim();

        switch (arg.toLowerCase()) {
        case "name":
            expenseList.sortByName();
            logger.info("handleSort executed | sort type: name");
            ui.printLine("Expenses sorted alphabetically by name.");
            ui.printLine("");
            break;
        case "category":
            expenseList.sortByCategory();
            logger.info("handleSort executed | sort type: category");
            ui.printLine("Expenses sorted by category.");
            ui.printLine("");
            break;
        case "recent":
            expenseList.sortByRecent();
            logger.info("handleSort executed | sort type: recent");
            ui.printLine("Expenses sorted by insertion order.");
            ui.printLine("");

            break;
        default:
            logger.warning("handleSort rejected | unknown argument: " + arg);
            ui.printLine("Wrong argument la bro! Use 'sort name', 'sort category' or 'sort recent' ONLY!");
            ui.printLine("");
            break;
        }
    }

    /**
     * Handles the 'save' command to archive current month's expenses and advance to next month.
     *
     * <p>This method:
     * <ul>
     *   <li>Archives the current month's expenses to a file (Month{N})</li>
     *   <li>Calculates unspent allowance: monthlyAllowance - currentMonthExpenses</li>
     *   <li>Transfers unspent amount to savings</li>
     *   <li>Clears the monthly expense list for the next month</li>
     *   <li>Increments the month counter</li>
     * </ul>
     * </p>
     */
    public void handleSaveMonth(Scanner in) {
        assert in != null : "Scanner should not be null";

        String response = ui.readLine(in, SAVE_CONFIRMATION_PROMPT).trim().toLowerCase();
        if (!response.equals("y")) {
            logger.info("handleSaveMonth cancelled | user did not confirm save");
            ui.printLine("Save cancelled. Your current month's data is still editable.");
            ui.printLine("");
            return;
        }

        int currentMonth = profile.getCurrentMonth();
        BigDecimal monthlyExpenses = expenseList.getTotal().add(recurringExpenseList.getTotal());
        BigDecimal monthlyAllowance = profile.getMonthlyAllowance();
        BigDecimal unspentAmount = monthlyAllowance.subtract(monthlyExpenses);

        logger.info("handleSaveMonth start | month=" + currentMonth
                + " | monthlyAllowance=" + monthlyAllowance
                + " | oneOffExpenses=" + expenseList.getTotal()
                + " | recurringExpenses=" + recurringExpenseList.getTotal()
                + " | monthlyExpenses=" + monthlyExpenses
                + " | unspentAmount=" + unspentAmount);

        // Archive current month's expenses
        try {
            MonthlyArchive archive = new MonthlyArchive(".");
            archive.saveMonthlyExpenses(currentMonth, expenseList, recurringExpenseList);
            logger.info("Month " + currentMonth + " expenses archived successfully");
            ui.printLine("Month " + currentMonth + " expenses archived to 'monthly_archives'");
        } catch (IOException e) {
            logger.log(java.util.logging.Level.SEVERE,
                    "Failed to archive Month " + currentMonth + " expenses", e);
            ui.printLine("Error: Could not archive expenses for Month " + currentMonth);
            ui.printLine("");
            return;
        }

        // Transfer unspent allowance to savings
        if (unspentAmount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newSavings = profile.getCurrentSavings().add(unspentAmount);
            profile.setCurrentSavings(newSavings);
            logger.info("Transferred unspent amount to savings | amount=" + unspentAmount
                    + " | newSavings=" + newSavings);
            ui.printLine("Transferred " + InputUtil.formatMoney(unspentAmount) + " of unspent "
                    + "allowance to savings");
        } else if (unspentAmount.compareTo(BigDecimal.ZERO) < 0) {
            logger.info("Overspent this month | deficit=" + unspentAmount.negate());
            ui.printLine("You overspent by " + InputUtil.formatMoney(unspentAmount.negate()));
        } else {
            logger.info("No unspent allowance this month");
            ui.printLine("You spent exactly your monthly allowance");
        }

        // Clear expense list for next month
        expenseList.clear();
        logger.info("Expense list cleared for next month");

        // Advance to next month
        profile.advanceMonth();
        int nextMonth = profile.getCurrentMonth();
        logger.info("Advanced to next month | currentMonth=" + nextMonth);

        ui.printLine("Advanced to Month " + nextMonth);
        ui.printLine("Current Savings: " + InputUtil.formatMoney(profile.getCurrentSavings()));
        ui.printLine("Monthly Allowance: " + InputUtil.formatMoney(monthlyAllowance));
        ui.printLine("");
    }
}
