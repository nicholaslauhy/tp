package seedu.duke;

import seedu.duke.data.Category;
import seedu.duke.data.Expense;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Profile;
import seedu.duke.data.Storage;
import seedu.duke.data.SummaryReport;
import seedu.duke.exception.InvalidAmountException;
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
    private final Storage storage;

    public CommandHandler(Ui ui, Profile profile, ExpenseList expenseList, Storage storage) {
        assert ui != null : "Ui should not be null";
        assert profile != null : "Profile should not be null";
        assert expenseList != null : "ExpenseList should not be null";
        assert storage != null : "Storage should not be null";

        this.ui = ui;
        this.profile = profile;
        this.expenseList = expenseList;
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
            String rest = userInput.substring("add".length()).trim();

            if (rest.isEmpty()) {
                logger.warning("handleAdd rejected | reason: empty input");
                throw new InvalidAmountException("Format: add <name> <amount> <category>\n");
            }

            String[] parts = rest.split("\\s+");

            if (parts.length < 3) {
                logger.warning("handleAdd rejected | reason: insufficient arguments");
                throw new InvalidAmountException("Format: add <name> <amount> <category>\n");
            }

            String categoryString = parts[parts.length - 1];
            String amountString = parts[parts.length - 2];

            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 0; i < parts.length - 2; i++) {
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

            BigDecimal amount = parseAmount(amountString);

            // To be provided by category implementation
            Category category = Category.fromString(categoryString);

            BigDecimal oldTotal = expenseList.getTotal();
            expenseList.add(name, amount, category);

            assert expenseList.getTotal().compareTo(oldTotal.add(amount)) == 0
                    : "Expense total should increase by added amount";

            logger.info("handleAdd succeeded | name: " + name
                    + " | amount: $" + amount
                    + " | category: " + category
                    + " | new total: $" + expenseList.getTotal());

            ui.printLine("Added expense: " + new Expense(name, amount, category));
            ui.printLine("Current Total: $" + expenseList.getTotal());
            ui.printLine("");

        } catch (InvalidAmountException e) {
            ui.printLine(e.getMessage());
            ui.printLine("");
        }
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

        ui.printLine("WARNING: This will permanently delete ALL expenses. Are you sure? (Input Y to clear)");
        String response = in.nextLine().trim().toLowerCase();

        if (response.equals("y")) {
            expenseList.clear();

            // Log at INFO: clearing all expenses is a significant application event
            logger.info("handleClear executed | all expenses cleared by user confirmation");

            ui.printLine("Expense list has been wiped clean. Fresh start!");
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
     * Computes and displays a BTO Readiness Report based on the user's current financial profile.
     *
     * <p>Calculates the following metrics from {@link Profile} and {@link ExpenseList}:
     * <ul>
     *   <li>Distance to goal (BTO goal - current savings)</li>
     *   <li>Monthly surplus (monthly salary - total expenses)</li>
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
        ui.showSummaryReport(new SummaryReport(profile, expenseList));
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
                storage.save(profile, expenseList);

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

    public BigDecimal parseAmount(String rest) throws InvalidAmountException {
        assert rest != null : "Amount input should not be null";

        // If there is no input after add
        if (rest.isEmpty()) {
            logger.warning("parseAmount rejected | reason: empty input");
            throw new InvalidAmountException("Format: add <value(to 2dp)> bro! where is the MONEHHHH\n");
        }

        BigDecimal amount;

        try {
            amount = new BigDecimal(rest);
        } catch (NumberFormatException e) {
            // Reject non-numeric input
            logger.warning("parseAmount rejected | reason: non-numeric input '" + rest + "'");
            throw new InvalidAmountException("Amount must be a valid number bro! What is this garbage!\n");
        }

        //Reject negative values
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            logger.warning("parseAmount rejected | reason: negative value " + amount);
            throw new InvalidAmountException("Amount cannot be negative bro who you trying to scam?\n");
        }

        // Reject >2 decimal places
        if (amount.scale() > 2) {
            logger.warning("parseAmount rejected | reason: more than 2 decimal places, value: " + amount);
            throw new InvalidAmountException("Amount must not exceed 2 decimal places bro!\n");
        }

        assert amount.compareTo(BigDecimal.ZERO) >= 0 : "Amount should be non-negative";

        // Log at FINE: successful parse is a low-level detail, not a key app event
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
}
