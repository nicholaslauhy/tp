package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import seedu.duke.category.Category;
import seedu.duke.data.ExpenseList;
import seedu.duke.data.Profile;
import seedu.duke.data.SummaryReport;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SummaryReportTest {
    @Test
    public void summaryReport_correctValues() {

        Profile profile = new Profile();
        profile.setName("Nick");
        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("2000"));
        profile.setMonthlyAllowance(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.of(2027,8,6));

        ExpenseList expenses = new ExpenseList();
        expenses.add("Groceries", new BigDecimal("500"), Category.fromString("FOOD"));

        SummaryReport report = new SummaryReport(profile, expenses);

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

        SummaryReport report = new SummaryReport(profile, expenses);

        assertEquals(new BigDecimal("-2000"), report.distance);
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

        SummaryReport report = new SummaryReport(profile, expenses);

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

        SummaryReport report = new SummaryReport(profile, expenses);

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

        SummaryReport report = new SummaryReport(profile, expenses);

        assertEquals(0, report.percentage);
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

        SummaryReport report = new SummaryReport(profile, expenses);

        assertEquals(new BigDecimal("3000"), report.monthlySurplus);
        assertEquals("3 months", report.estimate);
    }

    @Test
    void computeReadinessLevel_thresholdTests() {
        Profile profile = new Profile();
        ExpenseList expenseList = new ExpenseList();

        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setDeadline(LocalDate.now().plusYears(1));

        // Test 100% threshold
        profile.setCurrentSavings(new BigDecimal("10000"));
        assertEquals("Ready - Time to sign that BTO!",
                new SummaryReport(profile, expenseList).readinessLevel);

        // Test 70% threshold
        profile.setCurrentSavings(new BigDecimal("7500"));
        assertEquals("Secure - Keep it up, you're almost there!",
                new SummaryReport(profile, expenseList).readinessLevel);

        // Test 50% threshold
        profile.setCurrentSavings(new BigDecimal("5500"));
        assertEquals("On Track - Let's go! You're more than halfway there",
                new SummaryReport(profile, expenseList).readinessLevel);

        // Test 10% threshold
        profile.setCurrentSavings(new BigDecimal("1500"));
        assertEquals("Making Progress - Jiayou! You're making good progress",
                new SummaryReport(profile, expenseList).readinessLevel);

        // Test <10% threshold
        profile.setCurrentSavings(new BigDecimal("500"));
        assertEquals("Barely Started - Do start saving soon!!",
                new SummaryReport(profile, expenseList).readinessLevel);
    }

    @Test
    void constructor_calculatesCorrectMonthlyRequired() {
        Profile profile = new Profile();
        ExpenseList expenseList = new ExpenseList();

        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("4000"));
        profile.setDeadline(LocalDate.now().plusMonths(12));

        SummaryReport report = new SummaryReport(profile, expenseList);

        assertEquals(new BigDecimal("500.00"), report.monthlyRequired);
    }

    @Test
    void monthlyRequired_goalAlreadyReached_returnsZero() {
        Profile profile = new Profile();
        ExpenseList expenseList = new ExpenseList();

        profile.setBtoGoal(new BigDecimal("10000"));
        profile.setCurrentSavings(new BigDecimal("12000"));
        profile.setDeadline(LocalDate.now().plusMonths(5));

        SummaryReport report = new SummaryReport(profile, expenseList);

        assertEquals(BigDecimal.ZERO, report.monthlyRequired);
        assertEquals("Ready - Time to sign that BTO!", report.readinessLevel);
    }
}
