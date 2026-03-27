package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.category.Category;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Profile;
import seedu.duke.data.RecurringExpenseList;
import seedu.duke.data.Storage;
import seedu.duke.exception.InvalidAmountException;
import seedu.duke.exception.InvalidIndexException;
import seedu.duke.ui.Ui;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.Scanner;

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

    @Test
    void handleAllowance_validInput_updatesProfile() {
        Scanner in = new Scanner(new ByteArrayInputStream("1500.00\n".getBytes()));

        ch.handleAllowance(in);

        assertEquals(new BigDecimal("1500.00"), profile.getMonthlyAllowance());
    }

    @Test
    void handleRatio_validInput_updatesProfile() {
        Scanner in = new Scanner(new ByteArrayInputStream("0.7\n".getBytes()));

        ch.handleRatio(in);

        assertEquals(new BigDecimal("0.7"), profile.getContributionRatio());
    }

    @Test
    void handleSavings_validDeposit_incrementsTotalSavings() {
        profile.setCurrentSavings(new BigDecimal("1000.00"));
        Scanner in = new Scanner(new ByteArrayInputStream("500.00\n".getBytes()));

        ch.handleSavings(in);

        assertEquals(new BigDecimal("1500.00"), profile.getCurrentSavings());
    }

    @Test
    void handleReset_userConfirms_wipesAllData() {
        // Setup initial data
        profile.setName("Jairus");
        expenseList.add("Coffee", new BigDecimal("5.00"), Category.fromString("FOOD"));
        recurringExpenseList.add(new seedu.duke.data.RecurringExpense("Gym",
                new BigDecimal("80"), Category.fromString("OTHER")));

        Scanner in = new Scanner(new java.io.ByteArrayInputStream("y\n".getBytes()));

        ch.handleReset(in);

        // Verify state is reset to defaults
        assertEquals("friend", profile.getName());
        assertEquals(0, expenseList.size());

        // Ensure handleReset logic also clears the recurring list
        assertEquals(0, recurringExpenseList.size());
    }

    @Test
    void handleReset_userDeclines_preservesData() {
        profile.setName("Jairus");
        Scanner in = new Scanner(new java.io.ByteArrayInputStream("n\n".getBytes()));

        ch.handleReset(in);

        // Data should be untouched
        assertEquals("Jairus", profile.getName());
    }

    @Test
    void handleClear_userConfirms_wipesOneOffExpensesOnly() {
        expenseList.add("Lunch", new BigDecimal("10.00"), Category.fromString("FOOD"));
        recurringExpenseList.add(new seedu.duke.data.RecurringExpense("Rent",
                new BigDecimal("1200"), Category.fromString("OTHER")));

        Scanner in = new Scanner(new java.io.ByteArrayInputStream("y\n".getBytes()));

        ch.handleClear(in);

        // Clear only wipes one-off expenses, not recurring ones
        assertEquals(0, expenseList.size());
        assertEquals(1, recurringExpenseList.size());
    }

    @Test
    public void handleAdd_multiWordName_correctlyParsed() {
        // Test with spaces in the name
        ch.handleAdd("add Hainanese Chicken Rice 6.50 FOOD");

        assertEquals(1, expenseList.size());
        assertEquals("Hainanese Chicken Rice", expenseList.get(0).getName());
        assertEquals(new BigDecimal("6.50"), expenseList.get(0).getAmount());
    }

    @Test
    public void handleAdd_recurringExpense_addsToRecurringList() {
        int initialSize = recurringExpenseList.size();
        ch.handleAdd("add Netflix Subscription 15.00 ENTERTAINMENT recurring");

        assertEquals(initialSize + 1, recurringExpenseList.size());
        assertEquals("Netflix Subscription", recurringExpenseList.get(initialSize).getName());
    }

    @Test
    void handleSaveMonth_overspent_transfersZeroToSavings() {
        profile.setMonthlyAllowance(new BigDecimal("500"));
        profile.setCurrentSavings(new BigDecimal("1000"));

        // Spend $600 (Overspent by $100)
        expenseList.add("Shopping", new BigDecimal("600"), Category.fromString("OTHER"));

        ch.handleSaveMonth();

        // Savings should stay at 1000, not 900
        assertEquals(new BigDecimal("1000"), profile.getCurrentSavings());
        assertEquals(2, profile.getCurrentMonth());
    }

    @Test
    void handleAdd_caseInsensitivity_parsesCorrectly() {
        ch.handleAdd("add LUNCH 5.00 food"); // Mixed case name and category

        assertEquals(1, expenseList.size());
        assertEquals("LUNCH", expenseList.get(0).getName());
        assertEquals("FOOD", expenseList.get(0).getCategory().getName());
    }

    @Test
    void handleRatio_boundaryValues_updatesProfile() {
        // Test 0% share
        Scanner inZero = new Scanner(new java.io.ByteArrayInputStream("0.0\n".getBytes()));
        ch.handleRatio(inZero);
        assertEquals(new BigDecimal("0.0"), profile.getContributionRatio());

        // Test 100% share
        Scanner inFull = new Scanner(new java.io.ByteArrayInputStream("1.0\n".getBytes()));
        ch.handleRatio(inFull);
        assertEquals(new BigDecimal("1.0"), profile.getContributionRatio());
    }
}
