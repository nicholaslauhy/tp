package seedu.duke;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import seedu.duke.data.Expense;
import seedu.duke.category.Category;

/**
 * Unit tests for {@link Expense}, verifying that fields are stored
 * correctly and that {@link Expense#toString()} formats as expected.
 */
public class ExpenseTest {

    /**
     * Verifies that the constructor correctly stores name, amount, and category.
     */
    @Test
    void constructor_validInputs_fieldsStoredCorrectly() {
        Expense expense = new Expense("lunch", new BigDecimal("12.50"), Category.fromString("FOOD"), 1);

        assertEquals("lunch", expense.getName());
        assertEquals(new BigDecimal("12.50"), expense.getAmount());
        assertEquals("FOOD", expense.getCategory().getName());
    }

    /**
     * Verifies that {@code toString()} formats the expense correctly.
     */
    @Test
    void toString_validExpense_returnsFormattedString() {
        Expense expense = new Expense("lunch", new BigDecimal("12.50"), Category.fromString("FOOD"), 1);

        assertEquals("[FOOD] lunch $12.50", expense.toString());
    }

    /**
     * Verifies that a zero-value expense is stored and displayed correctly.
     */
    @Test
    void constructor_zeroAmount_storedAndDisplayedCorrectly() {
        Expense expense = new Expense("free item", new BigDecimal("0"), Category.fromString("OTHER"), 1);

        assertEquals(new BigDecimal("0"), expense.getAmount());
        assertEquals("[OTHER] free item $0", expense.toString());
    }

    @Test
    void constructor_validInsertionOrder_storedCorrectly() {
        Expense expense = new Expense("lunch", new BigDecimal("12.50"), Category.fromString("FOOD"), 3);

        assertEquals(3, expense.getInsertionOrder());
    }

    @Test
    void constructor_zeroInsertionOrder_storedCorrectly() {
        // first expense in the list
        Expense expense = new Expense("lunch", new BigDecimal("12.50"), Category.fromString("FOOD"), 0);

        assertEquals(0, expense.getInsertionOrder());
    }
}
