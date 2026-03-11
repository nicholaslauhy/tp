package seedu.duke;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import seedu.duke.data.Expense;
/**
 * Unit tests for {@link Expense}, verifying that the amount is stored
 * correctly and that {@link Expense#toString()} formats as expected.
 */

public class ExpenseTest {
    /**
     * Verifies that {@code getAmount()} returns the exact value passed to the constructor.
     */
    @Test
    void constructor_validAmount_getAmountReturnsCorrectValue() {
        Expense expense = new Expense(new BigDecimal("12.50"));

        assertEquals(new BigDecimal("12.50"), expense.getAmount());
    }
    /**
     * Verifies that {@code toString()} formats the expense with a leading dollar sign.
     */
    @Test
    void toString_validAmount_returnsFormattedString() {
        Expense expense = new Expense(new BigDecimal("12.50"));

        assertEquals("$12.50", expense.toString());
    }
}
