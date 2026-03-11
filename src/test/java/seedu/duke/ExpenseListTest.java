package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void setUp(){
        expenseList = new ExpenseList();
    }

    /**
     * Verfies that addinhg a single expense increments the list size to 1
     */
    @Test
    void add_singleExpense_sizeIsOne(){
        expenseList.add(new BigDecimal("12.50"));

        assertEquals(1, expenseList.size());
    }
    /*
    *Verifies whether adding multiple expenses produces the correct total
     */
    @Test
    void add_multipleExpenses_totalIsCorrectSum(){
        expenseList.add(new BigDecimal("10.00"));
        expenseList.add(new BigDecimal("5.50"));
        expenseList.add(new BigDecimal("2.25"));

        assertEquals(new BigDecimal("17.75"), expenseList.getTotal());
    }
    /*
    *Verifies whether insertiongs are in order and retrievable by their own index
     */
    @Test
    void add_multipleExpenses_storedInOrder() {
        expenseList.add(new BigDecimal("10.00"));
        expenseList.add(new BigDecimal("20.00"));

        assertEquals(new BigDecimal("10.00"), expenseList.get(0).getAmount());
        assertEquals(new BigDecimal("20.00"), expenseList.get(1).getAmount());
    }
    /*
    *Verify that List transitons from empty to non-empty after the first add
     */
    @Test
    void add_firstExpense_listWasEmptyBefore() {
        assertTrue(expenseList.isEmpty());

        expenseList.add(new BigDecimal("5.00"));

        assertFalse(expenseList.isEmpty());
    }
    /**
     * Verifies that deleting the only expense leaves the list empty.
     */
    @Test
    void delete_onlyExpense_listBecomesEmpty() {
        expenseList.add(new BigDecimal("10.00"));

        expenseList.delete(1);

        assertTrue(expenseList.isEmpty());
    }
    /**
     * Verifies that deleting the only expense resets the running total to zero.
     */
    @Test
    void delete_onlyExpense_totalBecomesZero() {
        expenseList.add(new BigDecimal("10.00"));

        expenseList.delete(1);

        assertEquals(new BigDecimal("0.00"), expenseList.getTotal());
    }
    /**
     * Verifies that {@code delete} returns the expense that was removed.
     */
    @Test
    void delete_returnsCorrectExpense() {
        expenseList.add(new BigDecimal("42.00"));

        Expense removed = expenseList.delete(1);

        assertEquals(new BigDecimal("42.00"), removed.getAmount());
    }
    /**
     * Verifies that the remaining expenses preserve their original relative order
     * after a middle item is deleted.
     */
    @Test
    void delete_middleExpense_remainingOrderIsCorrect() {
        expenseList.add(new BigDecimal("10.00"));
        expenseList.add(new BigDecimal("20.00"));
        expenseList.add(new BigDecimal("30.00"));

        expenseList.delete(2); // remove $20.00

        assertEquals(new BigDecimal("10.00"), expenseList.get(0).getAmount());
        assertEquals(new BigDecimal("30.00"), expenseList.get(1).getAmount());
    }
    /**
     * Verifies that passing index {@code 0} throws {@link IndexOutOfBoundsException}
     * since the list is 1-based.
     */
    @Test
    void delete_invalidIndexZero_throwsException() {
        expenseList.add(new BigDecimal("10.00"));

        assertThrows(IndexOutOfBoundsException.class, () -> expenseList.delete(0));
    }
    /**
     * Verifies that passing an index greater than the list size throws
     * {@link IndexOutOfBoundsException}.
     */
    @Test
    void delete_invalidIndexBeyondSize_throwsException() {
        expenseList.add(new BigDecimal("10.00"));

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

