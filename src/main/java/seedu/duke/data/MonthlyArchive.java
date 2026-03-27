//@@author nicholaslauhy
package seedu.duke.data;

import seedu.duke.util.LoggerUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the archival of monthly expenses into dedicated files.
 *
 * <p>This class handles creating and maintaining a directory structure for storing
 * monthly financial records. Each month's expenses are saved to a file named
 * MonthN where N is the month number.</p>
 */
public class MonthlyArchive {
    private static final Logger logger = LoggerUtil.getLogger(MonthlyArchive.class);
    private static final String ARCHIVE_DIR = "monthly_archives";
    private final String archiveDirectoryPath;

    /**
     * Initializes the MonthlyArchive with a base directory path.
     *
     * @param baseDir The base directory path where the archive folder will be created.
     */
    public MonthlyArchive(String baseDir) {
        assert baseDir != null && !baseDir.trim().isEmpty() : "Base directory cannot be null or empty";
        this.archiveDirectoryPath = baseDir + File.separator + ARCHIVE_DIR;
        ensureArchiveDirectoryExists();
    }

    /**
     * Creates the archive directory if it does not exist.
     */
    private void ensureArchiveDirectoryExists() {
        File archiveDir = new File(archiveDirectoryPath);
        if (!archiveDir.exists()) {
            boolean created = archiveDir.mkdirs();
            if (created) {
                logger.log(Level.INFO, "Archive directory created at: " + archiveDirectoryPath);
            } else if (!archiveDir.isDirectory()) {
                logger.log(Level.WARNING, "Failed to create archive directory: " + archiveDirectoryPath);
            }
        }
    }

    /**
     * Saves the expenses for a given month to a dedicated file.
     *
     * <p>File format: One expense per line as "name | amount | category"</p>
     *
     * @param monthNumber The month number (1-indexed).
     * @param expenseList The list of expenses to archive.
     * @throws IOException If there is an error writing to the file.
     */
    public void saveMonthlyExpenses(int monthNumber, ExpenseList expenseList) throws IOException {
        assert monthNumber > 0 : "Month number must be positive";
        assert expenseList != null : "Expense list cannot be null";

        File monthFile = getMonthFile(monthNumber);
        logger.log(Level.INFO, "Archiving Month " + monthNumber + " expenses to: " + monthFile.getAbsolutePath());

        try (FileWriter fw = new FileWriter(monthFile)) {
            for (int i = 0; i < expenseList.size(); i++) {
                Expense e = expenseList.get(i);
                fw.write(String.format("%s | %s | %s%n",
                        e.getName(),
                        e.getAmount(),
                        e.getCategory()));
            }
            logger.log(Level.INFO, "Month " + monthNumber + " archived successfully with "
                    + expenseList.size() + " expenses");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to archive Month " + monthNumber, e);
            throw e;
        }
    }

    /**
     * Loads archived one-off expenses for a given month.
     *
     * <p>Lines are expected to be in the format: {@code name | amount | category}.</p>
     *
     * @param monthNumber The month number (1-indexed).
     * @return Parsed archived entries; empty list if no archive file exists.
     * @throws IOException If there is an error reading the file.
     */
    public List<ArchivedExpense> loadMonthlyExpenses(int monthNumber) throws IOException {
        assert monthNumber > 0 : "Month number must be positive";

        File monthFile = getMonthFile(monthNumber);
        List<ArchivedExpense> archivedExpenses = new ArrayList<>();

        if (!monthFile.exists()) {
            return archivedExpenses;
        }

        try (Scanner scanner = new Scanner(monthFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length < 3) {
                    logger.log(Level.WARNING, "Skipping malformed archived expense line: " + line);
                    continue;
                }

                archivedExpenses.add(new ArchivedExpense(parts[0], parts[1], parts[2]));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load archived expenses for Month " + monthNumber, e);
            throw e;
        }

        return archivedExpenses;
    }

    /**
     * Retrieves the file object for a given month number.
     *
     * @param monthNumber The month number (1-indexed).
     * @return A File object pointing to the month's archive file.
     */
    private File getMonthFile(int monthNumber) {
        assert monthNumber > 0 : "Month number must be positive";
        String fileName = "Month" + monthNumber;
        return new File(archiveDirectoryPath, fileName);
    }

    /**
     * Checks if a monthly archive file exists for the given month number.
     *
     * @param monthNumber The month number (1-indexed).
     * @return {@code true} if the file exists; {@code false} otherwise.
     */
    public boolean monthlyFileExists(int monthNumber) {
        assert monthNumber > 0 : "Month number must be positive";
        return getMonthFile(monthNumber).exists();
    }

    /**
     * Returns the path to the archive directory.
     *
     * @return The path to the monthly_archives directory.
     */
    public String getArchiveDirectoryPath() {
        return archiveDirectoryPath;
    }
}

