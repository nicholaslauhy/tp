package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.category.Category;
import seedu.duke.data.RecurringExpense;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link RecurringExpense}.
 *
 * <p>Verifies that recurring expense objects correctly store and return
 * their name, amount, and category, and that their string representation
 * matches the expected output format.</p>
 */
public class RecurringExpenseTest {

    /**
     * Verifies that the constructor correctly stores the provided
     * name, amount, and category.
     */
    @Test
    void constructor_validInputs_fieldsStoredCorrectly() {
        RecurringExpense recurringExpense =
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"));

        assertEquals("Netflix", recurringExpense.getName());
        assertEquals(new BigDecimal("30.00"), recurringExpense.getAmount());
        assertEquals("ENTERTAINMENT", recurringExpense.getCategory().getName());
    }

    /**
     * Verifies that a zero-value recurring expense is stored correctly.
     */
    @Test
    void constructor_zeroAmount_storedCorrectly() {
        RecurringExpense recurringExpense =
                new RecurringExpense("Free Trial", new BigDecimal("0"), Category.fromString("OTHER"));

        assertEquals("Free Trial", recurringExpense.getName());
        assertEquals(new BigDecimal("0"), recurringExpense.getAmount());
        assertEquals("OTHER", recurringExpense.getCategory().getName());
    }

    /**
     * Verifies that {@code toString()} returns the expected formatted output.
     */
    @Test
    void toString_validRecurringExpense_returnsFormattedString() {
        RecurringExpense recurringExpense =
                new RecurringExpense("Netflix", new BigDecimal("30.00"), Category.fromString("ENTERTAINMENT"));

        assertEquals("[RECURRING][ENTERTAINMENT] Netflix $30.00", recurringExpense.toString());
    }
}
