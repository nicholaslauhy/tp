package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.category.Category;
import seedu.duke.data.Expense;
import seedu.duke.data.ExpenseList;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseListTest {

    private ExpenseList expenseList;

    /**
     * Initialises a new empty {@link ExpenseList} before each test.
     * This ensures full isolation between test cases.
     */
    @BeforeEach
    void setUp() {
        expenseList = new ExpenseList();
    }

    /**
     * Verifies that adding a single expense increments the list size to 1.
     */
    @Test
    void add_singleExpense_sizeIsOne() {
        expenseList.add("lunch", new BigDecimal("12.50"), Category.fromString("FOOD"));

        assertEquals(1, expenseList.size());
    }

    /**
     * Verifies that adding multiple expenses produces the correct total.
     */
    @Test
    void add_multipleExpenses_totalIsCorrectSum() {
        expenseList.add("breakfast", new BigDecimal("10.00"), Category.fromString("FOOD"));
        expenseList.add("bus fare", new BigDecimal("5.50"), Category.fromString("TRANSPORT"));
        expenseList.add("snack", new BigDecimal("2.25"), Category.fromString("FOOD"));

        assertEquals(new BigDecimal("17.75"), expenseList.getTotal());
    }

    /**
     * Verifies that insertions are in order and retrievable by index.
     */
    @Test
    void add_multipleExpenses_storedInOrder() {
        expenseList.add("breakfast", new BigDecimal("10.00"), Category.fromString("FOOD"));
        expenseList.add("movie", new BigDecimal("20.00"), Category.fromString("ENTERTAINMENT"));

        assertEquals("breakfast", expenseList.get(0).getName());
        assertEquals(new BigDecimal("10.00"), expenseList.get(0).getAmount());
        assertEquals("FOOD", expenseList.get(0).getCategory().getName());

        assertEquals("movie", expenseList.get(1).getName());
        assertEquals(new BigDecimal("20.00"), expenseList.get(1).getAmount());
        assertEquals("ENTERTAINMENT", expenseList.get(1).getCategory().getName());
    }

    /**
     * Verifies that the list transitions from empty to non-empty after the first add.
     */
    @Test
    void add_firstExpense_listWasEmptyBefore() {
        assertTrue(expenseList.isEmpty());

        expenseList.add("coffee", new BigDecimal("5.00"), Category.fromString("FOOD"));

        assertFalse(expenseList.isEmpty());
    }

    /**
     * Verifies that deleting the only expense leaves the list empty.
     */
    @Test
    void delete_onlyExpense_listBecomesEmpty() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.fromString("FOOD"));

        expenseList.delete(1);

        assertTrue(expenseList.isEmpty());
    }

    /**
     * Verifies that deleting the only expense resets the running total to zero.
     */
    @Test
    void delete_onlyExpense_totalBecomesZero() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.fromString("FOOD"));

        expenseList.delete(1);

        assertEquals(new BigDecimal("0.00"), expenseList.getTotal());
    }

    /**
     * Verifies that {@code delete} returns the expense that was removed.
     */
    @Test
    void delete_returnsCorrectExpense() {
        expenseList.add("concert", new BigDecimal("42.00"), Category.fromString("ENTERTAINMENT"));

        Expense removed = expenseList.delete(1);

        assertEquals("concert", removed.getName());
        assertEquals(new BigDecimal("42.00"), removed.getAmount());
        assertEquals("ENTERTAINMENT", removed.getCategory().getName());
    }

    /**
     * Verifies that the remaining expenses preserve their original relative order
     * after a middle item is deleted.
     */
    @Test
    void delete_middleExpense_remainingOrderIsCorrect() {
        expenseList.add("breakfast", new BigDecimal("10.00"), Category.fromString("FOOD"));
        expenseList.add("movie", new BigDecimal("20.00"), Category.fromString("ENTERTAINMENT"));
        expenseList.add("taxi", new BigDecimal("30.00"), Category.fromString("TRANSPORT"));

        expenseList.delete(2);

        assertEquals("breakfast", expenseList.get(0).getName());
        assertEquals(new BigDecimal("10.00"), expenseList.get(0).getAmount());
        assertEquals("FOOD", expenseList.get(0).getCategory().getName());

        assertEquals("taxi", expenseList.get(1).getName());
        assertEquals(new BigDecimal("30.00"), expenseList.get(1).getAmount());
        assertEquals("TRANSPORT", expenseList.get(1).getCategory().getName());
    }

    /**
     * Verifies that passing index {@code 0} throws {@link IndexOutOfBoundsException}
     * since the list is 1-based.
     */
    @Test
    void delete_invalidIndexZero_throwsException() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.fromString("FOOD"));

        assertThrows(IndexOutOfBoundsException.class, () -> expenseList.delete(0));
    }

    /**
     * Verifies that passing an index greater than the list size throws
     * {@link IndexOutOfBoundsException}.
     */
    @Test
    void delete_invalidIndexBeyondSize_throwsException() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.fromString("FOOD"));

        assertThrows(IndexOutOfBoundsException.class, () -> expenseList.delete(2));
    }

    /**
     * Verifies that calling {@code delete} on an empty list throws
     * {@link IndexOutOfBoundsException}.
     */
    @Test
    void delete_emptyList_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> expenseList.delete(1));
    }

    /**
     * Verifies that sorting by name is case-insensitive and alphabetical.
     */
    @Test
    void sortByName_mixedCaseNames_sortedAlphabetically() {
        expenseList.add("Zebra", new BigDecimal("10.00"), Category.fromString("OTHER"));
        expenseList.add("apple", new BigDecimal("5.00"), Category.fromString("FOOD"));
        expenseList.add("Banana", new BigDecimal("7.00"), Category.fromString("TRANSPORT"));

        expenseList.sortByName();

        // Should be: apple, Banana, Zebra
        assertEquals("apple", expenseList.get(0).getName());
        assertEquals("Banana", expenseList.get(1).getName());
        assertEquals("Zebra", expenseList.get(2).getName());
    }

    /**
     * Verifies that sorting by category follows the Enum definition order.
     */
    @Test
    void sortByCategory_multipleCategories_sortedByEnumOrder() {
        // Adding in reverse-ish order of typical Enum priority
        expenseList.add("Taxi", new BigDecimal("15.00"), Category.fromString("TRANSPORT"));
        expenseList.add("Lunch", new BigDecimal("10.00"), Category.fromString("FOOD"));
        expenseList.add("Movie", new BigDecimal("20.00"), Category.fromString("ENTERTAINMENT"));

        expenseList.sortByCategory();

        // Assuming Category order is FOOD, TRANSPORT, ENTERTAINMENT
        assertEquals("FOOD", expenseList.get(0).getCategory().getName());
        assertEquals("TRANSPORT", expenseList.get(1).getCategory().getName());
        assertEquals("ENTERTAINMENT", expenseList.get(2).getCategory().getName());
    }

    /**
     * Verifies that sorting by recent restores the original insertion order
     * even after the list has been rearranged by another sort.
     */
    @Test
    void sortByRecent_afterNameSort_restoresOriginalOrder() {
        expenseList.add("First", new BigDecimal("1.00"), Category.fromString("FOOD"));
        expenseList.add("Second", new BigDecimal("2.00"), Category.fromString("FOOD"));
        expenseList.add("Third", new BigDecimal("3.00"), Category.fromString("FOOD"));

        // Mess up the order
        expenseList.sortByName();

        // Restore order
        expenseList.sortByRecent();

        assertEquals("First", expenseList.get(0).getName());
        assertEquals("Second", expenseList.get(1).getName());
        assertEquals("Third", expenseList.get(2).getName());
    }

    /**
     * Verifies that sorting an empty list does not throw exceptions.
     */
    @Test
    void sorting_emptyList_doesNotThrowException() {
        // Should handle gracefully without NullPointerExceptions
        expenseList.sortByName();
        expenseList.sortByCategory();
        expenseList.sortByRecent();

        assertEquals(0, expenseList.size());
    }
}
