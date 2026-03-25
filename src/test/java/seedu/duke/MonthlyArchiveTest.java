package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.duke.data.MonthlyArchive;
import seedu.duke.data.ArchivedExpense;
import seedu.duke.data.ExpenseList;
import seedu.duke.category.Category;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the MonthlyArchive class.
 */
public class MonthlyArchiveTest {

    @TempDir
    Path tempDir;

    private MonthlyArchive archive;
    private ExpenseList expenseList;

    @BeforeEach
    public void setUp() {
        archive = new MonthlyArchive(tempDir.toString());
        expenseList = new ExpenseList();
    }

    @Test
    public void constructor_validPath_archiveCreated() {
        assertNotNull(archive);
        assertTrue(archive.getArchiveDirectoryPath().contains("monthly_archives"));
    }

    @Test
    public void ensureArchiveDirectoryExists_directoryCreated() {
        String archivePath = archive.getArchiveDirectoryPath();
        File archiveDir = new File(archivePath);
        assertTrue(archiveDir.exists());
        assertTrue(archiveDir.isDirectory());
    }

    @Test
    public void saveMonthlyExpenses_emptyList_fileCreated() throws IOException {
        archive.saveMonthlyExpenses(1, expenseList);
        assertTrue(archive.monthlyFileExists(1));
    }

    @Test
    public void saveMonthlyExpenses_withExpenses_fileContainsData() throws IOException {
        expenseList.add("Lunch", new BigDecimal("15.50"), Category.fromString("FOOD"));
        expenseList.add("Movie", new BigDecimal("12.00"), Category.fromString("ENTERTAINMENT"));

        archive.saveMonthlyExpenses(1, expenseList);

        File monthFile = new File(archive.getArchiveDirectoryPath(), "Month1");
        assertTrue(monthFile.exists());

        String content = Files.readString(monthFile.toPath());
        assertTrue(content.contains("Lunch"));
        assertTrue(content.contains("15.50"));
        assertTrue(content.contains("FOOD"));
        assertTrue(content.contains("Movie"));
        assertTrue(content.contains("12.00"));
        assertTrue(content.contains("ENTERTAINMENT"));
    }

    @Test
    public void monthlyFileExists_fileDoesNotExist_returnsFalse() {
        assertFalse(archive.monthlyFileExists(1));
    }

    @Test
    public void monthlyFileExists_fileExists_returnsTrue() throws IOException {
        archive.saveMonthlyExpenses(1, expenseList);
        assertTrue(archive.monthlyFileExists(1));
    }

    @Test
    public void saveMonthlyExpenses_multipleMonths_separateFiles() throws IOException {
        expenseList.add("Expense1", new BigDecimal("10.00"), Category.fromString("FOOD"));
        archive.saveMonthlyExpenses(1, expenseList);

        expenseList.clear();
        expenseList.add("Expense2", new BigDecimal("20.00"), Category.fromString("UTILITIES"));
        archive.saveMonthlyExpenses(2, expenseList);

        assertTrue(archive.monthlyFileExists(1));
        assertTrue(archive.monthlyFileExists(2));

        File month1File = new File(archive.getArchiveDirectoryPath(), "Month1");
        File month2File = new File(archive.getArchiveDirectoryPath(), "Month2");

        String content1 = Files.readString(month1File.toPath());
        String content2 = Files.readString(month2File.toPath());

        assertTrue(content1.contains("Expense1"));
        assertTrue(content2.contains("Expense2"));
        assertFalse(content1.contains("Expense2"));
        assertFalse(content2.contains("Expense1"));
    }

    @Test
    public void saveMonthlyExpenses_negativeMonth_throwsAssertion() {
        assertThrows(AssertionError.class, () -> archive.saveMonthlyExpenses(-1, expenseList));
    }

    @Test
    public void saveMonthlyExpenses_zeroMonth_throwsAssertion() {
        assertThrows(AssertionError.class, () -> archive.saveMonthlyExpenses(0, expenseList));
    }

    @Test
    public void saveMonthlyExpenses_nullExpenseList_throwsAssertion() {
        assertThrows(AssertionError.class, () -> archive.saveMonthlyExpenses(1, null));
    }

    @Test
    public void getArchiveDirectoryPath_containsArchiveDir() {
        String path = archive.getArchiveDirectoryPath();
        assertTrue(path.contains("monthly_archives"));
    }

    @Test
    public void saveMonthlyExpenses_largeExpenseAmount_savedCorrectly() throws IOException {
        expenseList.add("Rent", new BigDecimal("2500.50"), Category.fromString("OTHER"));
        archive.saveMonthlyExpenses(1, expenseList);

        File monthFile = new File(archive.getArchiveDirectoryPath(), "Month1");
        String content = Files.readString(monthFile.toPath());
        assertTrue(content.contains("2500.50"));
    }

    @Test
    public void saveMonthlyExpenses_specialCharactersInName_savedCorrectly() throws IOException {
        expenseList.add("Coffee & Snacks", new BigDecimal("5.50"), Category.fromString("FOOD"));
        archive.saveMonthlyExpenses(1, expenseList);

        File monthFile = new File(archive.getArchiveDirectoryPath(), "Month1");
        String content = Files.readString(monthFile.toPath());
        assertTrue(content.contains("Coffee & Snacks"));
    }

    @Test
    public void loadMonthlyExpenses_existingArchive_returnsParsedRows() throws IOException {
        expenseList.add("Lunch", new BigDecimal("15.50"), Category.fromString("FOOD"));
        archive.saveMonthlyExpenses(1, expenseList);

        java.util.List<ArchivedExpense> loaded = archive.loadMonthlyExpenses(1);

        assertEquals(1, loaded.size());
        assertEquals("Lunch", loaded.get(0).getName());
        assertEquals("15.50", loaded.get(0).getAmount());
        assertEquals("FOOD", loaded.get(0).getCategory());
    }

    @Test
    public void loadMonthlyExpenses_missingArchive_returnsEmptyList() throws IOException {
        java.util.List<ArchivedExpense> loaded = archive.loadMonthlyExpenses(2);
        assertTrue(loaded.isEmpty());
    }
}





