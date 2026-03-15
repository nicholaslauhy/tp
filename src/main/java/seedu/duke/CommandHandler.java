package seedu.duke;

import seedu.duke.data.Profile;
import seedu.duke.data.Expense;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Storage;
import seedu.duke.data.SummaryReport;
import seedu.duke.exception.InvalidAmountException;
import seedu.duke.exception.InvalidIndexException;
import seedu.duke.ui.Ui;
import seedu.duke.util.InputUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class CommandHandler {
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
    public void handleAdd(String userInput) {
        assert userInput != null : "User input should not be null";
        assert userInput.startsWith("add") : "Input should start with 'add'";

        try {
            String rest = userInput.substring("add".length()).trim();
            BigDecimal amount = parseAmount(rest);

            BigDecimal oldTotal = expenseList.getTotal();
            expenseList.add(amount);

            assert expenseList.getTotal().compareTo(oldTotal.add(amount)) == 0
                    : "Expense total should increase by added amount";

            ui.printLine("Added expense: $" + amount);
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

            ui.printLine("Deleted expense #" + index + ": $" + removed.getAmount());
            ui.printLine("Current Total: $" + expenseList.getTotal());
            ui.printLine("");
        } catch (InvalidIndexException e) {
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
            ui.printLine("Expense list has been wiped clean. Fresh start!");
            ui.printLine("");
        } else {
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
                ui.printLine("System reset successful. Please restart or type 'bye' to exit.");
                ui.printLine("");
            } catch (IOException e) {
                ui.printLine("Error: Could not reset the save file on disk.");
                ui.printLine("");
            }
        } else {
            ui.printLine("Reset aborted. Your data is safe!");
            ui.printLine("");
        }
    }

    public BigDecimal parseAmount(String rest) throws InvalidAmountException {
        assert rest != null : "Amount input should not be null";

        // If there is no input after add
        if (rest.isEmpty()) {
            throw new InvalidAmountException("Format: add <value(to 2dp)> bro! where is the MONEHHHH");
        }

        BigDecimal amount;

        try {
            amount = new BigDecimal(rest);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Amount must be a valid number bro! What is this garbage!");
        }

        //Reject negative values
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Amount cannot be negative bro who you trying to scam?");
        }

        // Reject >2 decimal places
        if (amount.scale() > 2) {
            throw new InvalidAmountException("Amount must not exceed 2 decimal places bro!");
        }

        assert amount.compareTo(BigDecimal.ZERO) >= 0 : "Amount should be non-negative";

        return amount;
    }

    public int parseDeleteIndex(String rest) throws InvalidIndexException {
        assert rest != null : "Delete index input should not be null";

        // If there is no input after delete
        if (rest.isEmpty()) {
            throw new InvalidIndexException("Format: delete <index> bro! where is the INDEXXX");
        }

        int index = Parser.parseIndex(rest);

        if (!expenseList.isValidIndex(index)) {
            throw new InvalidIndexException("Invalid index bro! do you even know how much you've spent?");
        }
        
        return index;
    }
}
