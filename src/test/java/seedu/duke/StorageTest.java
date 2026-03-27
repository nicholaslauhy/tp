package seedu.duke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import seedu.duke.category.Category;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Profile;
import seedu.duke.data.RecurringExpense;
import seedu.duke.data.RecurringExpenseList;
import seedu.duke.data.Storage;

import java.io.IOException;
import java.nio.file.Path;
import java.math.BigDecimal;
import java.time.LocalDate;

class StorageTest {

    @TempDir
    Path tempDir;

    private Profile createValidProfile(String name) {
        Profile profile = new Profile();
        profile.setName(name);
        profile.setMonthlyAllowance(new BigDecimal("3000"));
        profile.setCurrentSavings(new BigDecimal("500"));
        profile.setBtoGoal(new BigDecimal("20000"));
        profile.setContributionRatio(new BigDecimal("0.5"));
        // Always in the future relative to the test run
        profile.setDeadline(LocalDate.now().plusYears(2));
        return profile;
    }

    @Test
    void saveAndLoad_validData_returnsCorrectData() throws IOException {
        Path tempFile = tempDir.resolve("test_data.txt");
        Storage storage = new Storage(tempFile.toString());

        Profile originalProfile = createValidProfile("John Doe");

        ExpenseList originalExpenses = new ExpenseList();
        originalExpenses.add("Lunch", new BigDecimal("50"), Category.fromString("FOOD"));

        RecurringExpenseList originalRecurringExpenses = new RecurringExpenseList();
        originalRecurringExpenses.add(
                new RecurringExpense("Netflix", new BigDecimal("30"), Category.fromString("ENTERTAINMENT"))
        );

        storage.save(originalProfile, originalExpenses, originalRecurringExpenses);

        Profile loadedProfile = new Profile();
        ExpenseList loadedExpenses = new ExpenseList();
        RecurringExpenseList loadedRecurringExpenses = new RecurringExpenseList();
        storage.load(loadedProfile, loadedExpenses, loadedRecurringExpenses);

        assertEquals("John Doe", loadedProfile.getName());
        assertEquals(1, loadedExpenses.size());
        assertEquals(new BigDecimal("30"), loadedRecurringExpenses.get(0).getAmount());
    }

    @Test
    void save_emptyLists_savesProfileOnly(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("empty_lists.txt");
        Storage storage = new Storage(tempFile.toString());

        Profile profile = createValidProfile("EmptyTester");

        storage.save(profile, new ExpenseList(), new RecurringExpenseList());

        Profile loadedProfile = new Profile();
        storage.load(loadedProfile, new ExpenseList(), new RecurringExpenseList());

        assertEquals("EmptyTester", loadedProfile.getName());
    }

    @Test
    void saveAndLoad_largeExpenseList_maintainsExactTotal(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("large_data.txt");
        Storage storage = new Storage(tempFile.toString());

        Profile profile = createValidProfile("LargeTester");
        ExpenseList expenses = new ExpenseList();

        BigDecimal totalSent = BigDecimal.ZERO;
        for (int i = 0; i < 100; i++) {
            BigDecimal amount = new BigDecimal("1.01");
            expenses.add("Item " + i, amount, Category.fromString("OTHER"));
            totalSent = totalSent.add(amount);
        }

        storage.save(profile, expenses, new RecurringExpenseList());

        ExpenseList loadedExpenses = new ExpenseList();
        storage.load(new Profile(), loadedExpenses, new RecurringExpenseList());

        assertEquals(100, loadedExpenses.size());
        assertEquals(totalSent, loadedExpenses.getTotal());
    }

    @Test
    void profile_setDeadlinePastOrToday_throwsAssertionError() {
        Profile profile = new Profile();

        // Testing Today
        assertThrows(AssertionError.class, () -> {
            profile.setDeadline(LocalDate.now());
        }, "Assertion should trigger for current date");

        // Testing Past
        assertThrows(AssertionError.class, () -> {
            profile.setDeadline(LocalDate.now().minusDays(1));
        }, "Assertion should trigger for past dates");
    }

    @Test
    void load_fileDoesNotExist_startsWithEmptyState() throws IOException {
        // Using a path that definitely won't exist
        Storage storage = new Storage(tempDir.resolve("ghost_file.txt").toString());

        Profile profile = new Profile();
        ExpenseList expenses = new ExpenseList();
        RecurringExpenseList recurring = new RecurringExpenseList();

        // Should return silently without modifying objects or throwing exceptions
        storage.load(profile, expenses, recurring);

        assertEquals("friend", profile.getName()); // Default name
        assertEquals(0, expenses.size());
        assertEquals(0, recurring.size());
    }

    @Test
    void load_multipleProfileLines_lastLineTakesPrecedence(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("double_profile.txt");
        String futureDate = LocalDate.now().plusYears(1).toString();

        java.nio.file.Files.writeString(tempFile,
                "P | FirstUser | 1000 | 100 | 10000 | 0.5 | " + futureDate + "\n" +
                        "P | SecondUser | 2000 | 200 | 20000 | 0.6 | " + futureDate);

        Storage storage = new Storage(tempFile.toString());
        Profile profile = new Profile();
        storage.load(profile, new ExpenseList(), new RecurringExpenseList());

        assertEquals("SecondUser", profile.getName());
        assertEquals(new BigDecimal("2000"), profile.getMonthlyAllowance());
    }

    @Test
    void load_invalidNumericData_skipsLineGracefully(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("bad_numbers.txt");
        String futureDate = LocalDate.now().plusYears(1).toString();

        java.nio.file.Files.writeString(tempFile,
                "P | User | 3000 | 500 | 20000 | 0.5 | " + futureDate + "\n" +
                        "E | Coffee | NOT_A_NUMBER | FOOD | 1\n" + // Should skip this
                        "E | Rice | 5.50 | FOOD | 2");

        Storage storage = new Storage(tempFile.toString());
        ExpenseList expenses = new ExpenseList();
        storage.load(new Profile(), expenses, new RecurringExpenseList());

        // Should only have the 'Rice' expense
        assertEquals(1, expenses.size());
        assertEquals("Rice", expenses.get(0).getName());
    }

    @Test
    void saveAndLoad_insertionOrder_isPreserved(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("order_test.txt");
        Storage storage = new Storage(tempFile.toString());

        ExpenseList originalExpenses = new ExpenseList();

        // Add out of order to see if it preserves correctly
        originalExpenses.add("LaterItem", new BigDecimal("10"), Category.fromString("OTHER"), 5);
        originalExpenses.add("EarlierItem", new BigDecimal("5"), Category.fromString("OTHER"), 1);

        storage.save(createValidProfile("Tester"), originalExpenses, new RecurringExpenseList());

        ExpenseList loadedExpenses = new ExpenseList();
        storage.load(new Profile(), loadedExpenses, new RecurringExpenseList());

        assertEquals(5, loadedExpenses.get(0).getInsertionOrder());
        assertEquals(1, loadedExpenses.get(1).getInsertionOrder());
    }

    @Test
    void load_unknownRecordPrefix_skipsLine(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("unknown_prefix.txt");
        String futureDate = LocalDate.now().plusYears(1).toString();

        java.nio.file.Files.writeString(tempFile,
                "P | User | 3000 | 500 | 20000 | 0.5 | " + futureDate + "\n" +
                        "X | This is a junk line | 999\n" +
                        "E | Coffee | 5.00 | FOOD | 1");

        Storage storage = new Storage(tempFile.toString());
        ExpenseList expenses = new ExpenseList();
        storage.load(new Profile(), expenses, new RecurringExpenseList());

        // Should successfully load the 'Coffee' and ignore 'X'
        assertEquals(1, expenses.size());
        assertEquals("Coffee", expenses.get(0).getName());
    }

    @Test
    void load_missingParts_skipsGracefully(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("missing_parts.txt");
        java.nio.file.Files.writeString(tempFile,
                "E | OnlyName\n" +                        // Missing amount and category
                        "R | Rent | 1200.00\n" +                  // Missing category
                        "E | Lunch | 5.50 | FOOD | 1");           // Valid

        Storage storage = new Storage(tempFile.toString());
        ExpenseList expenses = new ExpenseList();
        RecurringExpenseList recurring = new RecurringExpenseList();
        storage.load(new Profile(), expenses, recurring);

        // Only the valid line should be loaded
        assertEquals(1, expenses.size());
        assertEquals(0, recurring.size());
    }

    @Test
    void load_expiredDeadline_handlesAssertionOrSkips(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve("expired.txt");

        String pastDate = LocalDate.now().minusDays(1).toString();

        java.nio.file.Files.writeString(tempFile,
                "P | OldUser | 3000 | 500 | 20000 | 0.5 | " + pastDate);

        Storage storage = new Storage(tempFile.toString());
        Profile profile = new Profile();

        storage.load(profile, new ExpenseList(), new RecurringExpenseList());
    }
}
