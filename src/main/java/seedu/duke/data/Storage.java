package seedu.duke.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import seedu.duke.category.Category;

/**
 * Handles the persistence of user profile data and expense lists to a local file.
 * <p>
 * This class ensures that the financial profile and all tracked expenses are saved
 * to a text file on disk and loaded back into the application at startup.
 * </p>
 */
public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private final String filePath;

    /**
     * Initializes a new Storage object with the specified file path.
     *
     * @param filePath The path to the text file used for data persistence.
     */
    public Storage(String filePath) {
        // Assertion: The file path should never be null or empty
        assert filePath != null && !filePath.trim().isEmpty() : "Storage file path is invalid!";
        this.filePath = filePath;
    }

    /**
     * Saves the current financial state to a text file.
     *
     * <p>Format for Profile: {@code P | name | allowance | savings | btoGoal | ratio | deadline}</p>
     *
     * <p>Format for Expenses: {@code E | name | amount | category}</p>
     *
     * @param profile The {@link Profile} containing the user's personal financial goals.
     * @param expenseList The {@link ExpenseList} containing all recorded transactions.
     * @throws IOException If there is an error writing to the file.
     */
    public void save(Profile profile, ExpenseList expenseList, RecurringExpenseList recurringExpenseList)
            throws IOException {
        // Assertion: Verify internal state before writing to disk
        assert profile != null : "Cannot save a null profile!";
        assert expenseList != null : "Cannot save a null expense list!";
        assert recurringExpenseList != null : "Cannot save a null recurring expense list!";

        logger.log(Level.INFO, "Saving financial data to " + filePath);

        try (FileWriter fw = new FileWriter(filePath)) {
            // Save Profile (P)
            fw.write(String.format("P | %s | %s | %s | %s | %s | %s | %d | %s%n",
                    profile.getName(),
                    profile.getMonthlyAllowance(),
                    profile.getCurrentSavings(),
                    profile.getBtoGoal(),
                    profile.getContributionRatio(),
                    profile.getDeadline(),
                    profile.getCurrentMonth(),
                    profile.getHousePrice() != null ? profile.getHousePrice() : "null"));

            // Save Expenses (E)
            for (int i = 0; i < expenseList.size(); i++) {
                Expense e = expenseList.get(i);
                // ASSERTION: Ensure no corrupted data exists in the list
                assert e.getName() != null : "Expense name at index " + i + " is null";
                assert e.getAmount() != null : "Expense amount at index " + i + " is null";
                assert e.getCategory() != null : "Expense category at index " + i + " is null";
                fw.write(String.format("E | %s | %s | %s | %s%n",
                        e.getName(),
                        e.getAmount(),
                        e.getCategory(),
                        e.getInsertionOrder()));
            }
            //save recurring expenses
            for (int i = 0; i < recurringExpenseList.size(); i++) {
                RecurringExpense recurringExpense = recurringExpenseList.get(i);
                assert recurringExpense.getName() != null : "Recurring expense name at index " + i + " is null";
                assert recurringExpense.getAmount() != null : "Recurring expense amount at index " + i + " is null";
                assert recurringExpense.getCategory() != null : "Recurring expense category at index " + i + " is null";

                fw.write(String.format("R | %s | %s | %s%n",
                        recurringExpense.getName(),
                        recurringExpense.getAmount(),
                        recurringExpense.getCategory()));
            }
            logger.log(Level.INFO, "Save successful.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not write to file: " + filePath, e);
            throw e;
        }
    }

    //@@author Jairusljr
    /**
     * Loads financial data from the save file into the provided profile and expense list.
     * <p>
     * If the save file does not exist, the method returns without modifying the data.
     * It parses each line based on the prefix ("P" for Profile, "E" for Expense) and
     * maps the data to the respective object fields.
     * </p>
     *
     * @param profile The {@link Profile} object to be populated with saved data.
     * @param expenseList The {@link ExpenseList} object to be populated with saved expenses.
     * @throws IOException If there is an error reading from the file.
     */
    //@@author Jairusljr
    public void load(Profile profile, ExpenseList expenseList, RecurringExpenseList recurringExpenseList)
            throws IOException {
        assert profile != null && expenseList != null && recurringExpenseList != null;

        File f = new File(filePath);
        if (!f.exists()) {
            logger.log(Level.INFO, "No save file found. Starting fresh.");
            return;
        }

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] parts = line.split(" \\| ");

                // 1. Skip lines that are obviously too short
                if (parts.length < 2) {
                    logger.log(Level.WARNING, "Skipping malformed line: " + line);
                    continue;
                }

                // 2. Wrap each line in its own try-catch so one bad line doesn't kill the loop
                try {
                    switch (parts[0]) {
                    case "P":
                        loadProfile(profile, parts);
                        break;
                    case "E":
                        loadExpense(expenseList, parts);
                        break;
                    case "R":
                        loadRecurring(recurringExpenseList, parts);
                        break;
                    default:
                        logger.log(Level.WARNING, "Unknown record type: " + parts[0]);
                    }
                } catch (NumberFormatException | DateTimeParseException | AssertionError e) {
                    // This catches bad numbers, bad dates, AND your Profile's "future date" assertions
                    logger.log(Level.WARNING, "Skipping corrupted/invalid line: " + line
                            + " | Reason: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fatal error reading file: " + filePath, e);
            throw e;
        }
    }

    // Helper methods to keep the code clean and readable
    private void loadProfile(Profile profile, String[] parts) {
        if (parts.length < 7) {
            return;
        }
        profile.setName(parts[1]);
        profile.setMonthlyAllowance(new BigDecimal(parts[2]));
        profile.setCurrentSavings(new BigDecimal(parts[3]));
        profile.setBtoGoal(new BigDecimal(parts[4]));
        profile.setContributionRatio(new BigDecimal(parts[5]));
        profile.setDeadline(java.time.LocalDate.parse(parts[6]));

        if (parts.length >= 8) {
            profile.setCurrentMonth(Integer.parseInt(parts[7]));
        }
        if (parts.length >= 9 && !"null".equals(parts[8].trim())) {
            profile.setHousePrice(new BigDecimal(parts[8].trim()));
        }
    }

    private void loadExpense(ExpenseList expenseList, String[] parts) {
        int len = parts.length;
        if (len == 2) { // Old format
            expenseList.add("Unnamed Expense", new BigDecimal(parts[1]), Category.fromString("OTHER"));
        } else if (len == 4) { // New format
            expenseList.add(parts[1], new BigDecimal(parts[2]), Category.fromString(parts[3]));
        } else if (len == 5) { // Format with insertion order
            expenseList.add(parts[1], new BigDecimal(parts[2]), Category.fromString(parts[3]),
                    Integer.parseInt(parts[4]));
        }
    }

    private void loadRecurring(RecurringExpenseList recurringList, String[] parts) {
        if (parts.length < 4) {
            return;
        }
        recurringList.add(new RecurringExpense(parts[1], new BigDecimal(parts[2]), Category.fromString(parts[3])));
    }
}
