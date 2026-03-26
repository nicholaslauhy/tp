package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Profile;
import seedu.duke.data.RecurringExpenseList;
import seedu.duke.data.Storage;
import seedu.duke.exception.InvalidAmountException;
import seedu.duke.exception.InvalidIndexException;
import seedu.duke.ui.Ui;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandHandlerTest {
    Ui ui = new Ui();
    Profile profile = new Profile();
    ExpenseList expenseList = new ExpenseList();
    Storage storage = new Storage("fintrack.txt");
    RecurringExpenseList recurringExpenseList = new RecurringExpenseList();
    CommandHandler ch = new CommandHandler(ui, profile, expenseList, recurringExpenseList , storage);

    @Test
    public void parseAmount_validInput_returnsValidAmount() throws InvalidAmountException {
        assertEquals(new BigDecimal("10.50"), ch.parseAmount("10.50"));
    }

    @Test
    public void parseAmount_zeroInput_returnsZero() throws InvalidAmountException {
        assertEquals(new BigDecimal("0"), ch.parseAmount("0"));
    }

    @Test
    public void parseAmount_missingInput_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class, ()->ch.parseAmount(""));
    }

    @Test
    public void parseAmount_invalidType_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class, ()->ch.parseAmount("hello"));
    }

    @Test
    public void parseAmount_negativeInput_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class, ()->ch.parseAmount("-1"));
    }

    @Test
    public void parseDeleteIndex_validInput_returnsValidIndex() throws InvalidIndexException {
        ch.handleAdd("add lunch 10.50 food");
        assertEquals(1, ch.parseDeleteIndex("1"));
    }

    @Test
    public void parseAmount_invalidDecimalFormat_throwsInvalidAmountException() {
        assertThrows(InvalidAmountException.class, ()->ch.parseAmount("7.009"));
    }

    @Test
    public void parseDeleteIndex_missingInput_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, () -> ch.parseDeleteIndex(""));
    }

    @Test
    public void parseDeleteIndex_invalidDecimalFormat_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, ()->ch.parseDeleteIndex("7.009"));
    }

    @Test
    public void parseDeleteIndex_invalidType_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, ()->ch.parseDeleteIndex("hello"));
    }

    @Test
    public void parseDeleteIndex_outOfBoundsIndex_throwsInvalidIndexException() {
        ch.handleAdd("add 10");
        assertThrows(InvalidIndexException.class, ()->ch.parseDeleteIndex("2"));
    }

    @Test
    public void parseDeleteIndex_zeroIndex_throwsInvalidIndexException() {
        assertThrows(InvalidIndexException.class, ()->ch.parseDeleteIndex("0"));
    }

    @Test
    public void parseAmount_validAllowance_returnsBigDecimal() throws InvalidAmountException {
        assertEquals(new BigDecimal("1200.00"), ch.parseAmount("1200.00"));
    }

    @Test
    public void parseAmount_allowanceWithTooManyDecimals_throwsException() {
        assertThrows(InvalidAmountException.class, () -> ch.parseAmount("1200.555"));
    }

    @Test
    public void handleSort_categoryArgument_sortsByCategory() {
        ch.handleAdd("add transport fare 5.00 TRANSPORT");
        ch.handleAdd("add caifan lunch 4.00 FOOD");
        ch.handleAdd("add movie 10.00 ENTERTAINMENT");
        ch.handleAdd("add popcorn 6.00 FOOD");

        ch.handleSort("sort category");

        // sorted in this order: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER
        assertEquals("FOOD", expenseList.get(0).getCategory().getName());
        assertEquals("FOOD", expenseList.get(1).getCategory().getName());
        assertEquals("TRANSPORT", expenseList.get(2).getCategory().getName());
        assertEquals("ENTERTAINMENT", expenseList.get(3).getCategory().getName());
    }

    @Test
    public void handleSort_recentArgument_sortsByRecent() {
        ch.handleAdd("add transport fare 5.00 TRANSPORT");
        ch.handleAdd("add caifan lunch 4.00 FOOD");
        ch.handleAdd("add movie 10.00 ENTERTAINMENT");
        ch.handleAdd("add popcorn 6.00 FOOD");

        // sort to category first
        ch.handleSort("sort category");

        // sort to recent after to verify
        ch.handleSort("sort recent");

        assertEquals("TRANSPORT", expenseList.get(0).getCategory().getName());
        assertEquals("FOOD", expenseList.get(1).getCategory().getName());
        assertEquals("ENTERTAINMENT", expenseList.get(2).getCategory().getName());
        assertEquals("FOOD", expenseList.get(3).getCategory().getName());
    }

    @Test
    public void handleSort_invalidArgument_listUnchanged() {
        ch.handleAdd("add transport fare 5.00 TRANSPORT");
        ch.handleAdd("add caifan lunch 4.00 FOOD");
        ch.handleAdd("add movie 10.00 ENTERTAINMENT");
        ch.handleAdd("add popcorn 6.00 FOOD");


        ch.handleSort("sort foo");

        // list should be still sorted in the same way
        assertEquals("TRANSPORT", expenseList.get(0).getCategory().getName());
        assertEquals("FOOD", expenseList.get(1).getCategory().getName());
        assertEquals("ENTERTAINMENT", expenseList.get(2).getCategory().getName());
        assertEquals("FOOD", expenseList.get(3).getCategory().getName());
    }

    @Test
    public void handleSort_emptyArgument_listUnchanged() {
        ch.handleAdd("add transport fare 5.00 TRANSPORT");
        ch.handleAdd("add caifan lunch 4.00 FOOD");
        ch.handleAdd("add movie 10.00 ENTERTAINMENT");
        ch.handleAdd("add popcorn 6.00 FOOD");


        ch.handleSort("sort");

        // list should be still sorted in the same way
        assertEquals("TRANSPORT", expenseList.get(0).getCategory().getName());
        assertEquals("FOOD", expenseList.get(1).getCategory().getName());
        assertEquals("ENTERTAINMENT", expenseList.get(2).getCategory().getName());
        assertEquals("FOOD", expenseList.get(3).getCategory().getName());
    }

    @Test
    public void handleAdd_invalidCategory_expenseNotAdded() {
        ch.handleAdd("add hello 5.00 HELLO");
        assertEquals(0, expenseList.size());
    }

    @Test
    public void handleAdd_validCategory_addsExpense() {
        ch.handleAdd("add lunch 3.00 FOOD");
        ch.handleAdd("add bus fare 2.00 TRANSPORT");
        ch.handleAdd("add movie 10.00 ENTERTAINMENT");
        ch.handleAdd("add electricity 50.00 UTILITIES");
        ch.handleAdd("add misc 1.00 OTHER");

        assertEquals(5, expenseList.size());
        assertEquals("FOOD", expenseList.get(0).getCategory().getName());
        assertEquals("TRANSPORT", expenseList.get(1).getCategory().getName());
        assertEquals("ENTERTAINMENT", expenseList.get(2).getCategory().getName());
        assertEquals("UTILITIES", expenseList.get(3).getCategory().getName());
        assertEquals("OTHER", expenseList.get(4).getCategory().getName());
    }
}
