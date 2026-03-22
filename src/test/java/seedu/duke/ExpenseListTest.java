package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.data.Category;
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
        expenseList.add("lunch", new BigDecimal("12.50"), Category.FOOD);

        assertEquals(1, expenseList.size());
    }

    /**
     * Verifies that adding multiple expenses produces the correct total.
     */
    @Test
    void add_multipleExpenses_totalIsCorrectSum() {
        expenseList.add("breakfast", new BigDecimal("10.00"), Category.FOOD);
        expenseList.add("bus fare", new BigDecimal("5.50"), Category.TRANSPORT);
        expenseList.add("snack", new BigDecimal("2.25"), Category.FOOD);

        assertEquals(new BigDecimal("17.75"), expenseList.getTotal());
    }

    /**
     * Verifies that insertions are in order and retrievable by index.
     */
    @Test
    void add_multipleExpenses_storedInOrder() {
        expenseList.add("breakfast", new BigDecimal("10.00"), Category.FOOD);
        expenseList.add("movie", new BigDecimal("20.00"), Category.ENTERTAINMENT);

        assertEquals("breakfast", expenseList.get(0).getName());
        assertEquals(new BigDecimal("10.00"), expenseList.get(0).getAmount());
        assertEquals(Category.FOOD, expenseList.get(0).getCategory());

        assertEquals("movie", expenseList.get(1).getName());
        assertEquals(new BigDecimal("20.00"), expenseList.get(1).getAmount());
        assertEquals(Category.ENTERTAINMENT, expenseList.get(1).getCategory());
    }

    /**
     * Verifies that the list transitions from empty to non-empty after the first add.
     */
    @Test
    void add_firstExpense_listWasEmptyBefore() {
        assertTrue(expenseList.isEmpty());

        expenseList.add("coffee", new BigDecimal("5.00"), Category.FOOD);

        assertFalse(expenseList.isEmpty());
    }

    /**
     * Verifies that deleting the only expense leaves the list empty.
     */
    @Test
    void delete_onlyExpense_listBecomesEmpty() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.FOOD);

        expenseList.delete(1);

        assertTrue(expenseList.isEmpty());
    }

    /**
     * Verifies that deleting the only expense resets the running total to zero.
     */
    @Test
    void delete_onlyExpense_totalBecomesZero() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.FOOD);

        expenseList.delete(1);

        assertEquals(new BigDecimal("0.00"), expenseList.getTotal());
    }

    /**
     * Verifies that {@code delete} returns the expense that was removed.
     */
    @Test
    void delete_returnsCorrectExpense() {
        expenseList.add("concert", new BigDecimal("42.00"), Category.ENTERTAINMENT);

        Expense removed = expenseList.delete(1);

        assertEquals("concert", removed.getName());
        assertEquals(new BigDecimal("42.00"), removed.getAmount());
        assertEquals(Category.ENTERTAINMENT, removed.getCategory());
    }

    /**
     * Verifies that the remaining expenses preserve their original relative order
     * after a middle item is deleted.
     */
    @Test
    void delete_middleExpense_remainingOrderIsCorrect() {
        expenseList.add("breakfast", new BigDecimal("10.00"), Category.FOOD);
        expenseList.add("movie", new BigDecimal("20.00"), Category.ENTERTAINMENT);
        expenseList.add("taxi", new BigDecimal("30.00"), Category.TRANSPORT);

        expenseList.delete(2);

        assertEquals("breakfast", expenseList.get(0).getName());
        assertEquals(new BigDecimal("10.00"), expenseList.get(0).getAmount());
        assertEquals(Category.FOOD, expenseList.get(0).getCategory());

        assertEquals("taxi", expenseList.get(1).getName());
        assertEquals(new BigDecimal("30.00"), expenseList.get(1).getAmount());
        assertEquals(Category.TRANSPORT, expenseList.get(1).getCategory());
    }

    /**
     * Verifies that passing index {@code 0} throws {@link IndexOutOfBoundsException}
     * since the list is 1-based.
     */
    @Test
    void delete_invalidIndexZero_throwsException() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.FOOD);

        assertThrows(IndexOutOfBoundsException.class, () -> expenseList.delete(0));
    }

    /**
     * Verifies that passing an index greater than the list size throws
     * {@link IndexOutOfBoundsException}.
     */
    @Test
    void delete_invalidIndexBeyondSize_throwsException() {
        expenseList.add("lunch", new BigDecimal("10.00"), Category.FOOD);

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
}

