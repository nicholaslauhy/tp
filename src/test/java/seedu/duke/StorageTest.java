package seedu.duke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void saveAndLoad_validData_returnsCorrectData() throws IOException {
        // Create a temporary file path
        Path tempFile = tempDir.resolve("test_data.txt");
        Storage storage = new Storage(tempFile.toString());

        Profile originalProfile = new Profile();
        originalProfile.setName("John Doe");
        originalProfile.setMonthlyAllowance(new BigDecimal("5000"));
        originalProfile.setCurrentSavings(new BigDecimal("1000"));
        originalProfile.setBtoGoal(new BigDecimal("50000"));
        originalProfile.setContributionRatio(new BigDecimal("0.2"));
        originalProfile.setDeadline(LocalDate.of(2030, 1, 1));

        ExpenseList originalExpenses = new ExpenseList();
        originalExpenses.add("Lunch", new BigDecimal("50"), Category.fromString("FOOD"));

        RecurringExpenseList originalRecurringExpenses = new RecurringExpenseList();
        originalRecurringExpenses.add(
                new RecurringExpense("Netflix", new BigDecimal("30"), Category.fromString("ENTERTAINMENT"))
        );
        // Save and then Load back into fresh objects
        storage.save(originalProfile, originalExpenses,originalRecurringExpenses );

        Profile loadedProfile = new Profile();
        ExpenseList loadedExpenses = new ExpenseList();
        RecurringExpenseList loadedRecurringExpenses = new RecurringExpenseList();
        storage.load(loadedProfile, loadedExpenses, loadedRecurringExpenses);

        // Compare original and loaded data
        assertEquals("John Doe", loadedProfile.getName());
        assertEquals(new BigDecimal("5000"), loadedProfile.getMonthlyAllowance());
        assertEquals(1, loadedExpenses.size());
        assertEquals(new BigDecimal("50"), loadedExpenses.get(0).getAmount());

        assertEquals(1, loadedRecurringExpenses.size());
        assertEquals("Netflix", loadedRecurringExpenses.get(0).getName());
        assertEquals(new BigDecimal("30"), loadedRecurringExpenses.get(0).getAmount());
        assertEquals("ENTERTAINMENT", loadedRecurringExpenses.get(0).getCategory().getName());
    }

    @Test
    void load_nonExistentFile_doesNotThrow() throws IOException {
        Storage storage = new Storage("non_existent_file.txt");
        Profile profile = new Profile();
        ExpenseList list = new ExpenseList();
        RecurringExpenseList recurringList = new RecurringExpenseList();
        // Should return silently without modifying objects
        storage.load(profile, list, recurringList);

        // Assert profile is still default/empty
        assertEquals("friend", profile.getName());
        assertEquals(0, list.size());
    }
}
