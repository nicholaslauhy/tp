package seedu.duke.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * <p>
     * Format for Profile: {@code P | name | salary | savings | btoGoal | ratio}
     * <p>
     * Format for Expenses: {@code E | amount | category}
     *
     * @param profile The {@link Profile} containing the user's personal financial goals.
     * @param expenseList The {@link ExpenseList} containing all recorded transactions.
     * @throws IOException If there is an error writing to the file.
     */
    public void save(Profile profile, ExpenseList expenseList) throws IOException {
        // Assertion: Verify internal state before writing to disk
        assert profile != null : "Cannot save a null profile!";
        assert expenseList != null : "Cannot save a null expense list!";

        logger.log(Level.INFO, "Saving financial data to " + filePath);

        try (FileWriter fw = new FileWriter(filePath)) {
            // Save Profile (P)
            fw.write(String.format("P | %s | %s | %s | %s | %s | %s%n",
                    profile.getName(),
                    profile.getMonthlySalary(),
                    profile.getCurrentSavings(),
                    profile.getBtoGoal(),
                    profile.getContributionRatio(),
                    profile.getDeadline()));

            // Save Expenses (E)
            for (int i = 0; i < expenseList.size(); i++) {
                Expense e = expenseList.get(i);
                // ASSERTION: Ensure no corrupted data exists in the list
                assert e.getAmount() != null : "Expense amount at index " + i + " is null";
                fw.write(String.format("E | %s%n", e.getAmount()));
            }
            logger.log(Level.INFO, "Save successful.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not write to file: " + filePath, e);
            throw e;
        }
    }

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
    public void load(Profile profile, ExpenseList expenseList) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            logger.log(Level.WARNING, "No save file found. Starting fresh.");
            return; // No save file yet, start with fresh data
        }

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] parts = line.split(" \\| ");

                if (parts.length < 2) {
                    logger.log(Level.WARNING, "Skipping malformed line: " + line);
                    continue;
                }

                if (parts[0].equals("P")) {
                    profile.setName(parts[1]);
                    profile.setMonthlySalary(new BigDecimal(parts[2]));
                    profile.setCurrentSavings(new BigDecimal(parts[3]));
                    profile.setBtoGoal(new BigDecimal(parts[4]));
                    profile.setContributionRatio(new BigDecimal(parts[5]));
                    profile.setDeadline(java.time.LocalDate.parse(parts[6]));
                } else if (parts[0].equals("E")) {
                    expenseList.add(new BigDecimal(parts[1]));
                }
            }
        } catch (IOException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.SEVERE, "Failed to parse save file accurately.", e);
            throw new IOException("The save file is corrupted and cannot be loaded.", e);
        }
    }
}
