package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.category.Category;
import seedu.duke.data.RecurringExpense;
import seedu.duke.data.RecurringExpenseList;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link RecurringExpenseList}.
 *
 * <p>Verifies that recurring expenses can be added, retrieved, deleted,
 * searched, and totalled correctly. Also ensures that list state methods
 * such as {@code size()}, {@code isEmpty()}, and {@code isValidIndex()}
 * behave as expected.</p>
 */
public class RecurringExpenseListTest {

    private RecurringExpenseList recurringExpenseList;

    /**
     * Creates a fresh empty recurring expense list before each test.
     */
    @BeforeEach
    void setUp() {
        recurringExpenseList = new RecurringExpenseList();
    }

    /**
     * Verifies that adding one recurring expense increases the list size to 1.
     */
    @Test
    void add_singleRecurringExpense_sizeIsOne() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );

        assertEquals(1, recurringExpenseList.size());
    }

    /**
     * Verifies that the list transitions from empty to non-empty after the first add.
     */
    @Test
    void add_firstRecurringExpense_listBecomesNonEmpty() {
        assertTrue(recurringExpenseList.isEmpty());

        recurringExpenseList.add(
                new RecurringExpense("Spotify", new BigDecimal("12.00"), Category.fromString("ENTERTAINMENT"))
        );

        assertFalse(recurringExpenseList.isEmpty());
    }

    /**
     * Verifies that recurring expenses are stored in insertion order and
     * can be retrieved correctly by index.
     */
    @Test
    void add_multipleRecurringExpenses_storedInOrder() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );
        recurringExpenseList.add(
                new RecurringExpense("Phone Bill", new BigDecimal("20.00"), Category.fromString("UTILITIES"))
        );

        assertEquals("Netflix", recurringExpenseList.get(0).getName());
        assertEquals("Phone Bill", recurringExpenseList.get(1).getName());
    }

    /**
     * Verifies that {@code getTotal()} returns the correct sum of all recurring expenses.
     */
    @Test
    void getTotal_multipleRecurringExpenses_returnsCorrectTotal() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );
        recurringExpenseList.add(
                new RecurringExpense("Phone Bill", new BigDecimal("20.00"), Category.fromString("UTILITIES"))
        );
        recurringExpenseList.add(
                new RecurringExpense("Gym", new BigDecimal("50.00"), Category.fromString("OTHER"))
        );

        assertEquals(new BigDecimal("100.00"), recurringExpenseList.getTotal());
    }

    /**
     * Verifies that deleting a recurring expense removes it from the list
     * and returns the deleted object.
     */
    @Test
    void delete_validIndex_returnsRemovedRecurringExpense() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );

        RecurringExpense removed = recurringExpenseList.delete(1);

        assertEquals("Netflix", removed.getName());
        assertEquals(0, recurringExpenseList.size());
    }

    /**
     * Verifies that clearing the recurring expense list removes all entries.
     */
    @Test
    void clear_nonEmptyRecurringExpenseList_becomesEmpty() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );
        recurringExpenseList.add(
                new RecurringExpense("Phone Bill", new BigDecimal("20.00"), Category.fromString("UTILITIES"))
        );

        recurringExpenseList.clear();

        assertTrue(recurringExpenseList.isEmpty());
        assertEquals(0, recurringExpenseList.size());
        assertEquals(BigDecimal.ZERO, recurringExpenseList.getTotal());
    }

    /**
     * Verifies that {@code isValidIndex()} correctly accepts valid 1-based indices.
     */
    @Test
    void isValidIndex_validIndices_returnsTrue() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );
        recurringExpenseList.add(
                new RecurringExpense("Phone Bill", new BigDecimal("20.00"), Category.fromString("UTILITIES"))
        );

        assertTrue(recurringExpenseList.isValidIndex(1));
        assertTrue(recurringExpenseList.isValidIndex(2));
    }

    /**
     * Verifies that {@code isValidIndex()} correctly rejects invalid 1-based indices.
     */
    @Test
    void isValidIndex_invalidIndices_returnsFalse() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );

        assertFalse(recurringExpenseList.isValidIndex(0));
        assertFalse(recurringExpenseList.isValidIndex(2));
    }

    /**
     * Verifies that {@code findMatches()} returns recurring expenses whose
     * names contain the search keyword.
     */
    @Test
    void findMatches_matchingName_returnsCorrectRecurringExpenses() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );
        recurringExpenseList.add(
                new RecurringExpense("Phone Bill", new BigDecimal("20.00"), Category.fromString("UTILITIES"))
        );

        ArrayList<RecurringExpense> matches = recurringExpenseList.findMatches("net");

        assertEquals(1, matches.size());
        assertEquals("Netflix", matches.get(0).getName());
    }

    /**
     * Verifies that {@code findMatches()} returns recurring expenses whose
     * categories contain the search keyword.
     */
    @Test
    void findMatches_matchingCategory_returnsCorrectRecurringExpenses() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );
        recurringExpenseList.add(
                new RecurringExpense("Phone Bill", new BigDecimal("20.00"), Category.fromString("UTILITIES"))
        );

        ArrayList<RecurringExpense> matches = recurringExpenseList.findMatches("util");

        assertEquals(1, matches.size());
        assertEquals("Phone Bill", matches.get(0).getName());
    }

    /**
     * Verifies that {@code findMatches()} returns an empty list when there are no matches.
     */
    @Test
    void findMatches_noMatches_returnsEmptyList() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );

        ArrayList<RecurringExpense> matches = recurringExpenseList.findMatches("transport");

        assertTrue(matches.isEmpty());
    }

    /**
     * Verifies that {@code getTotal()} returns zero on an empty list.
     */
    @Test
    void getTotal_emptyList_returnsZero() {
        assertEquals(BigDecimal.ZERO, recurringExpenseList.getTotal());
    }

    /**
     * Verifies that deleting with index 0 throws AssertionError (1-based indexing).
     */
    @Test
    void delete_invalidIndexZero_throwsAssertionError() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );

        assertThrows(AssertionError.class, () -> recurringExpenseList.delete(0));
    }

    /**
     * Verifies that deleting with an index beyond the list size throws AssertionError.
     */
    @Test
    void delete_invalidIndexBeyondSize_throwsAssertionError() {
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"))
        );

        assertThrows(AssertionError.class, () -> recurringExpenseList.delete(2));
    }

    /**
     * Verifies that deleting from an empty list throws AssertionError.
     */
    @Test
    void delete_emptyList_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> recurringExpenseList.delete(1));
    }
}
