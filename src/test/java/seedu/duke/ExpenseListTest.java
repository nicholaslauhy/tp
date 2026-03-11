package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}
