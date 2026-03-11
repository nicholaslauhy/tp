package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
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

}
