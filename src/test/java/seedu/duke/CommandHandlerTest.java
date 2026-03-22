package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Profile;
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
    CommandHandler ch = new CommandHandler(ui, profile, expenseList, storage);

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
}
