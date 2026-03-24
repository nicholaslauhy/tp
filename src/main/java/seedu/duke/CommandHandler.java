package seedu.duke;

import seedu.duke.category.Category;
import seedu.duke.data.Expense;
import seedu.duke.data.ExpenseList;
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
        assert userInput.startsWith("add") : "Input should start with 'add'";

        try {
            AddArguments args = parseAddArguments(userInput);
            if (args.recurring) {
                int oldSize = recurringExpenseList.size();
                recurringExpenseList.add(new RecurringExpense(args.name, args.amount, args.category));

                assert recurringExpenseList.size() == oldSize + 1
                        : "Recurring expense list size should increase by one after add";

                logger.info("handleAdd succeeded | recurring expense | name: " + args.name
                        + " | amount: $" + args.amount
                        + " | category: " + args.category);

                ui.printLine("Added recurring expense: " + recurringExpenseList.get(recurringExpenseList.size() - 1));
                ui.printLine("Recurring Total: $" + recurringExpenseList.getTotal());
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
            ui.printLine("Current Total: $" + expenseList.getTotal());
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
        assert userInput.startsWith("add") : "Input should start with 'add'";

        String rest = userInput.substring("add".length()).trim();

        if (rest.isEmpty()) {
            logger.warning("handleAdd rejected | reason: empty input");
            throw new InvalidAmountException("Format: add <name> <amount> <category>\n");
        }

        String[] parts = rest.split("\\s+");
        boolean recurring = parts[parts.length - 1].equalsIgnoreCase("recurring");

        if (!recurring && parts.length < 3) {
            logger.warning("handleAdd rejected | reason: insufficient arguments");
            throw new InvalidAmountException("Format: add <name> <amount> <category> [recurring]\n");
        }

        if (recurring && parts.length < 4) {
            logger.warning("handleAdd rejected | reason: insufficient arguments for recurring expense");
            throw new InvalidAmountException("Format: add <name> <amount> <category> [recurring]\n");
        }

        String categoryString;
        String amountString;
        int nameEndExclusive;

        if (recurring) {
            categoryString = parts[parts.length - 2];
            amountString = parts[parts.length - 3];
            nameEndExclusive = parts.length - 3;
        } else {
            categoryString = parts[parts.length - 1];
            amountString = parts[parts.length - 2];
            nameEndExclusive = parts.length - 2;
        }

        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < nameEndExclusive; i++) {
            if (i > 0) {
                nameBuilder.append(" ");
            }
            nameBuilder.append(parts[i]);
        }

        String name = nameBuilder.toString();

        if (name.isBlank()) {
            logger.warning("handleAdd rejected | reason: blank expense name");
            throw new InvalidAmountException("Expense name cannot be empty.\n");
        }

        if (!Category.isValid(categoryString)) {
            logger.warning("handleAdd rejected | reason: invalid category " + categoryString);
            throw new InvalidCategoryException("Invalid category! Valid categories: " +
                    "FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER\n");
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
        assert userInput.startsWith("delete") : "Input should start with 'delete'";

        try {
            String rest = userInput.substring("delete".length()).trim();
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

            ui.printLine("Deleted expense #" + index + ": $" + removed.getAmount());
            ui.printLine("Current Total: $" + expenseList.getTotal());
            ui.printLine("");
        } catch (InvalidIndexException e) {
            // Log at WARNING: user provided an invalid index that was rejected
            logger.warning("handleDelete rejected | reason: " + e.getMessage().trim());
            ui.printLine(e.getMessage());
        }
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
        assert userInput.startsWith("deleterecurring") : "Input should start with 'deleterecurring'";

        try {
            String rest = userInput.substring("deleterecurring".length()).trim();

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
        String response = in.nextLine().trim().toLowerCase();

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
     *
     * @param in Scanner used to read the user's input.
     */
    public void handleRatio(Scanner in) {
        assert in != null : "Scanner should not be null";

        BigDecimal current = profile.getContributionRatio();
        assert current != null : "Current ratio in profile should not be null";

        // Show current state
        ui.promptForRatio(current);
        BigDecimal newRatio = InputUtil.readRatio(ui, in, "Enter new ratio (0.0 to 1.0):");
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

    /**
     * Completely resets the user profile and expense list after confirmation.
     * @param in Scanner used for user confirmation.
     */
    public void handleReset(Scanner in) {
        assert in != null : "Scanner should not be null";

        ui.printLine("WARNING: This will wipe your profile and ALL expenses. Type 'Y' to continue: ");
        String response = in.nextLine().trim().toLowerCase();

        if (response.equals("y")) {
            // 1. Reset in-memory objects
            profile.reset();
            expenseList.clear();

            assert expenseList.getTotal().compareTo(BigDecimal.ZERO) == 0
                    : "Expense total should be zero after reset";

            // 2. Overwrite the save file with the empty data
            try {
                storage.save(profile, expenseList, recurringExpenseList);

                // Log at INFO: full system reset is the most significant application event
                logger.info("handleReset executed | profile and expenses cleared, save file overwritten");

                ui.printLine("System reset successful. Please restart or type 'bye' to exit.");
                ui.printLine("");
            } catch (IOException e) {
                // Log at WARNING: in-memory reset succeeded but disk write failed
                logger.warning("handleReset | in-memory reset succeeded but save file write failed: "
                        + e.getMessage());
                ui.printLine("Error: Could not reset the save file on disk.");
                ui.printLine("");
            }
        } else {
            // Log at INFO: user chose not to reset — still worth recording the decision
            logger.info("handleReset cancelled | user did not confirm");
            ui.printLine("Reset aborted. Your data is safe!");
            ui.printLine("");
        }
    }

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
}
