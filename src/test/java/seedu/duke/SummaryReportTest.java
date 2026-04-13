package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.category.Category;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Profile;
import seedu.duke.data.RecurringExpense;
import seedu.duke.data.RecurringExpenseList;
import seedu.duke.data.SummaryReport;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SummaryReportTest {
    @Test
    public void summaryReport_correctValues() {
        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.of(2027, 8, 6));

        ExpenseList expenses = new ExpenseList();
        expenses.add("Groceries", new BigDecimal("500"), Category.fromString("FOOD"));

        RecurringExpenseList recurringExpenses = new RecurringExpenseList();

        SummaryReport report = new SummaryReport(profile, expenses, recurringExpenses);

        assertEquals(new BigDecimal("8000"), report.distance);
        assertEquals(new BigDecimal("3500"), report.monthlySurplus);
        assertEquals(20, report.percentage);
    }

    @Test
    public void summaryReport_goalAlreadyReached_correctEstimate() {
        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("12000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.of(2027, 8, 6));

        ExpenseList expenses = new ExpenseList();
        expenses.add("Groceries", new BigDecimal("500"), Category.fromString("FOOD"));

        RecurringExpenseList recurringExpenses = new RecurringExpenseList();

        SummaryReport report = new SummaryReport(profile, expenses, recurringExpenses);

        assertEquals(new BigDecimal("0"), report.distance);;
        assertEquals("Reached! Go get that BTO!", report.estimate);
    }

    @Test
    public void summaryReport_zeroMonthlySurplus_infiniteEstimate() {
        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.of(2027, 8, 6));

        ExpenseList expenses = new ExpenseList();
        expenses.add("Rent", new BigDecimal("4000"), Category.fromString("UTILITIES"));

        RecurringExpenseList recurringExpenses = new RecurringExpenseList();

        SummaryReport report = new SummaryReport(profile, expenses, recurringExpenses);

        assertEquals(new BigDecimal("0"), report.monthlySurplus);
        assertEquals("Infinite (Surplus is $0 or negative!)", report.estimate);
    }

    @Test
    public void summaryReport_negativeMonthlySurplus_infiniteEstimate() {
        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("3000"));
        profile.setDeadline(LocalDate.of(2027, 8, 6));

        ExpenseList expenses = new ExpenseList();
        expenses.add("Rent", new BigDecimal("3500"), Category.fromString("UTILITIES"));

        RecurringExpenseList recurringExpenses = new RecurringExpenseList();

        SummaryReport report = new SummaryReport(profile, expenses, recurringExpenses);

        assertEquals(new BigDecimal("-500"), report.monthlySurplus);
        assertEquals("Infinite (Surplus is $0 or negative!)", report.estimate);
    }

    @Test
    public void summaryReport_zeroBtoGoal_percentageZero() {
        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(BigDecimal.ZERO);
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.of(2027, 8, 6));

        ExpenseList expenses = new ExpenseList();
        expenses.add("Groceries", new BigDecimal("500"), Category.fromString("FOOD"));

        RecurringExpenseList recurringExpenses = new RecurringExpenseList();

        SummaryReport report = new SummaryReport(profile, expenses, recurringExpenses);

        assertEquals(100, report.percentage);
    }

    @Test
    public void summaryReport_fractionalMonths_roundsUpEstimate() {
        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.of(2027, 8, 6));

        ExpenseList expenses = new ExpenseList();
        expenses.add("Transport", new BigDecimal("1000"), Category.fromString("TRANSPORT"));

        RecurringExpenseList recurringExpenses = new RecurringExpenseList();

        SummaryReport report = new SummaryReport(profile, expenses, recurringExpenses);

        assertEquals(new BigDecimal("3000"), report.monthlySurplus);
        assertEquals("3 months", report.estimate);
    }

    @Test
    void computeReadinessLevel_thresholdTests() {
        Profile profile = new Profile();
        ExpenseList expenseList = new ExpenseList();
        RecurringExpenseList recurringExpenseList = new RecurringExpenseList();

        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.now().plusYears(1));

        profile.setCurrentSavings(new BigDecimal("10000"));
        assertEquals("Ready - Time to sign that BTO!",
                new SummaryReport(profile, expenseList, recurringExpenseList).readinessLevel);

        profile.setCurrentSavings(new BigDecimal("7500"));
        assertEquals("Secure - Keep it up, you're almost there!",
                new SummaryReport(profile, expenseList, recurringExpenseList).readinessLevel);

        profile.setCurrentSavings(new BigDecimal("5500"));
        assertEquals("On Track - Let's go! You're more than halfway there",
                new SummaryReport(profile, expenseList, recurringExpenseList).readinessLevel);

        profile.setCurrentSavings(new BigDecimal("1500"));
        assertEquals("Making Progress - Jiayou! You're making good progress",
                new SummaryReport(profile, expenseList, recurringExpenseList).readinessLevel);

        profile.setCurrentSavings(new BigDecimal("500"));
        assertEquals("Barely Started - Do start saving soon!!",
                new SummaryReport(profile, expenseList, recurringExpenseList).readinessLevel);
    }

    @Test
    void constructor_calculatesCorrectMonthlyRequired() {
        Profile profile = new Profile();
        ExpenseList expenseList = new ExpenseList();
        RecurringExpenseList recurringExpenseList = new RecurringExpenseList();

        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("4000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.now().plusMonths(12));

        SummaryReport report = new SummaryReport(profile, expenseList, recurringExpenseList);

        assertEquals(new BigDecimal("500.00"), report.monthlyRequired);
    }

    @Test
    void monthlyRequired_goalAlreadyReached_returnsZero() {
        Profile profile = new Profile();
        ExpenseList expenseList = new ExpenseList();
        RecurringExpenseList recurringExpenseList = new RecurringExpenseList();

        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("12000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.now().plusMonths(5));

        SummaryReport report = new SummaryReport(profile, expenseList, recurringExpenseList);

        assertEquals(BigDecimal.ZERO, report.monthlyRequired);
        assertEquals("Ready - Time to sign that BTO!", report.readinessLevel);
    }

    @Test
    void summaryReport_includesRecurringExpensesInTotalExpenditure() {
        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.now().plusMonths(12));

        ExpenseList expenseList = new ExpenseList();
        expenseList.add("Lunch", new BigDecimal("500"), Category.fromString("FOOD"));

        RecurringExpenseList recurringExpenseList = new RecurringExpenseList();
        recurringExpenseList.add(
                new RecurringExpense("Netflix", new BigDecimal("30"), Category.fromString("ENTERTAINMENT"))
        );

        SummaryReport report = new SummaryReport(profile, expenseList, recurringExpenseList);

        assertEquals(new BigDecimal("530"), report.totalExpenditure);
        assertEquals(new BigDecimal("3470"), report.monthlySurplus);
    }

    @Test
    void readinessLevel_boundaryCheck_lowerAndUpperLimits() {
        Profile profile = new Profile();
        profile.setBtoGoal(new BigDecimal("100"));
        profile.setMonthlyAllowance(new BigDecimal("1000"));
        profile.setDeadline(LocalDate.now().plusYears(1));
        ExpenseList expenses = new ExpenseList();
        RecurringExpenseList recurring = new RecurringExpenseList();

        // 49% - Should be "Making Progress"
        profile.setCurrentSavings(new BigDecimal("49"));
        assertEquals("Making Progress - Jiayou! You're making good progress",
                new SummaryReport(profile, expenses, recurring).readinessLevel);

        // 50% - Should jump to "On Track"
        profile.setCurrentSavings(new BigDecimal("50"));
        assertEquals("On Track - Let's go! You're more than halfway there",
                new SummaryReport(profile, expenses, recurring).readinessLevel);
    }

    @Test
    public void summaryReport_deadlineIsTomorrow_calculatesOneMonthRemaining() {
        Profile profile = new Profile();
        profile.setBtoGoal(new BigDecimal("1000"));
        profile.setCurrentSavings(BigDecimal.ZERO);
        profile.setMonthlyAllowance(new BigDecimal("2000"));

        profile.setDeadline(LocalDate.now().plusDays(1));

        SummaryReport report = new SummaryReport(profile, new ExpenseList(), new RecurringExpenseList());

        // monthsLeft: 0 months + 1 day will round up to 1 month
        // monthlyRequired = (1000 - 0) / 1 = 1000.00
        assertEquals(new BigDecimal("1000.00"), report.monthlyRequired);
    }

    @Test
    void profile_setDeadlinePastOrToday_isAccepted() {
        Profile profile = new Profile();

        // Past and today dates are accepted by the setter; validation is enforced at input via InputUtil
        LocalDate today = LocalDate.now();
        profile.setDeadline(today);
        assertEquals(today, profile.getDeadline());

        LocalDate past = LocalDate.now().minusDays(1);
        profile.setDeadline(past);
        assertEquals(past, profile.getDeadline());
    }

    @Test
    void summaryReport_pastDeadline_doesNotCrash() {
        Profile profile = new Profile();
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.now().minusMonths(3));

        // Should not throw AssertionError — monthsLeft clamped to 1 in SummaryReport
        SummaryReport report = new SummaryReport(profile, new ExpenseList(), new RecurringExpenseList());

        assertEquals(new BigDecimal("8000.00"), report.monthlyRequired);
    }

    @Test
    void summaryReport_zeroExpenditure_surplusEqualsAllowance() {
        Profile profile = new Profile();
        profile.setMonthlyAllowance(new BigDecimal("2500"));
        profile.setBtoGoal(new BigDecimal("5000"));
        profile.setCurrentSavings(BigDecimal.ZERO);
        profile.setDeadline(LocalDate.now().plusYears(1));

        // Both lists are empty
        SummaryReport report = new SummaryReport(profile, new ExpenseList(), new RecurringExpenseList());

        assertEquals(new BigDecimal("2500"), report.monthlySurplus);
        assertEquals(new BigDecimal("0"), report.totalExpenditure);
    }

    @Test
    void summaryReport_simulatedMonthAdvancement_recalculatesRequiredSavings() {
        Profile profile = new Profile();
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("4000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));

        // Set deadline to exactly 10 months from now
        profile.setDeadline(LocalDate.now().plusMonths(10));

        ExpenseList expenses = new ExpenseList();
        RecurringExpenseList recurring = new RecurringExpenseList();

        // 1. Test Initial Month (currentMonth = 1)
        // realMonthsRemaining = 10, simulatedElapsedMonths = 0
        // adjustedMonthsLeft = 10
        // monthlyRequired = 6000 / 10 = 600.00
        SummaryReport reportMonth1 = new SummaryReport(profile, expenses, recurring);
        assertEquals(10, reportMonth1.adjustedMonthsLeft);
        assertEquals(new BigDecimal("600.00"), reportMonth1.monthlyRequired);

        // 2. Simulate month advancement (simulate calling 'save' command)
        profile.setCurrentMonth(3);

        // 3. Test Advanced Month (currentMonth = 3)
        // realMonthsRemaining = 10, simulatedElapsedMonths = 2
        // adjustedMonthsLeft = 10 - 2 = 8
        // monthlyRequired = 6000 / 8 = 750.00
        SummaryReport reportMonth3 = new SummaryReport(profile, expenses, recurring);
        assertEquals(8, reportMonth3.adjustedMonthsLeft);
        assertEquals(new BigDecimal("750.00"), reportMonth3.monthlyRequired);

        // 4. Test Simulation Floor (simulate advancing past the deadline)
        profile.setCurrentMonth(15);

        // realMonthsRemaining = 10, simulatedElapsedMonths = 14
        // adjustedMonthsLeft = Max(1, 10 - 14) = 1
        // monthlyRequired = 6000 / 1 = 6000.00
        SummaryReport reportPastDeadline = new SummaryReport(profile, expenses, recurring);
        assertEquals(1, reportPastDeadline.adjustedMonthsLeft);
        assertEquals(new BigDecimal("6000.00"), reportPastDeadline.monthlyRequired);
    }
}
