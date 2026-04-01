package seedu.duke.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.logging.Logger;
import seedu.duke.util.LoggerUtil;

/**
 * Represents a snapshot of the user's BTO savings readiness.
 *
 * <p>Computed from a {@link Profile} and {@link ExpenseList} at the time of construction.
 * All fields are immutable after instantiation.</p>
 */
public class SummaryReport {
    private static final Logger logger = LoggerUtil.getLogger(SummaryReport.class);

    public final String name;
    public final LocalDate deadline;
    public final BigDecimal btoGoal;
    public final BigDecimal monthlyAllowance;
    public final BigDecimal currentSavings;
    public final int percentage;
    public final BigDecimal distance;
    public final BigDecimal monthlySurplus;
    public final String estimate;
    public final BigDecimal totalExpenditure;
    public final BigDecimal monthlyRequired;
    public final String readinessLevel;

    /**
     * Constructs a {@code SummaryReport} from the user's current profile and expense list.
     *
     * @param profile     the user's financial profile.
     * @param expenseList the user's current list of expenses.
     */
    public SummaryReport(Profile profile, ExpenseList expenseList, RecurringExpenseList recurringExpenseList) {
        assert profile != null : "Profile cannot be null for report generation";
        assert expenseList != null : "ExpenseList cannot be null for report generation";
        assert recurringExpenseList != null : "RecurringExpenseList cannot be null for report generation";

        logger.info("Generating SummaryReport for user: " + profile.getName());

        this.name = profile.getName();
        this.deadline = profile.getDeadline();
        this.btoGoal = profile.getBtoGoal();
        this.monthlyAllowance = profile.getMonthlyAllowance();
        this.currentSavings = profile.getCurrentSavings();

        assert btoGoal.compareTo(BigDecimal.ZERO) >= 0 : "BTO Goal should not be negative";
        assert monthlyAllowance.compareTo(BigDecimal.ZERO) >= 0 : "Allowance should not be negative";

        this.totalExpenditure = expenseList.getTotal().add(recurringExpenseList.getTotal());
        this.distance = btoGoal.subtract(currentSavings);
        this.monthlySurplus = monthlyAllowance.subtract(totalExpenditure);

        this.percentage = computePercentage();
        this.readinessLevel = computeReadinessLevel(this.percentage);
        this.estimate = computeEstimate();

        // Calculate months remaining
        LocalDate today = LocalDate.now();
        Period period = Period.between(today, profile.getDeadline());
        int monthsLeft = period.getYears() * 12 + period.getMonths();
        if (period.getDays() > 0) {
            monthsLeft++; // Round up for partial months
        }
        if (monthsLeft <= 0) {
            monthsLeft = 1;
        }

        // Calculate Distance to Goal / Months Remaining
        BigDecimal distanceToGoal = profile.getBtoGoal().subtract(profile.getCurrentSavings());

        if (distanceToGoal.compareTo(BigDecimal.ZERO) <= 0) {
            this.monthlyRequired = BigDecimal.ZERO;
        } else {
            this.monthlyRequired = distanceToGoal.divide(
                    BigDecimal.valueOf(monthsLeft), 2, java.math.RoundingMode.HALF_UP);
        }

        logger.fine("Report values - Distance: " + distance + ", Surplus: " + monthlySurplus);
    }

    /**
     * Determines a qualitative financial readiness level based on the savings percentage.
     *
     * <p>Maps the current progress to an encouraging status message using specific
     * thresholds (100%, 70%, 50%, and 10%). This provides users with a quick
     * benchmark of their BTO downpayment progress.</p>
     *
     * @param percentage The current percentage of the BTO goal reached.
     * @return A descriptive string indicating the user's readiness level and
     *     corresponding advice or encouragement.
     */
    private String computeReadinessLevel(int percentage) {
        if (percentage >= 100) {
            return "Ready - Time to sign that BTO!";
        } else if (percentage >= 70) {
            return "Secure - Keep it up, you're almost there!";
        } else if (percentage >= 50) {
            return "On Track - Let's go! You're more than halfway there";
        } else if (percentage >= 10) {
            return "Making Progress - Jiayou! You're making good progress";
        } else {
            return "Barely Started - Do start saving soon!!";
        }
    }

    /**
     * Computes the percentage of the BTO goal reached.
     * Returns {@code 0} if the BTO goal is zero or negative.
     *
     * @return percentage of goal reached, rounded to the nearest whole number.
     */
    private int computePercentage() {
        if (btoGoal.compareTo(BigDecimal.ZERO) > 0) {
            int calcPercentage = currentSavings.multiply(BigDecimal.valueOf(100))
                    .divide(btoGoal, 0, RoundingMode.HALF_UP)
                    .intValue();

            assert calcPercentage >= 0 : "Percentage calculation resulted in negative value";
            return calcPercentage;
        }
        logger.warning("BTO goal is zero; percentage set to 0.");
        return 0;
    }

    /**
     * Computes a human-readable estimate of when the BTO goal will be reached.
     *
     * @return {@code "Reached! Go get that BTO!"} if the goal is met,
     *         {@code "Infinite (Surplus is $0 or negative!)"} if no progress can be made,
     *         or the estimated number of months as a {@code String} otherwise.
     */
    private String computeEstimate() {
        if (distance.compareTo(BigDecimal.ZERO) <= 0) {
            logger.info("Distance is zero or negative. Goal marked as reached.");
            return "Reached! Go get that BTO!";
        }

        if (monthlySurplus.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warning("Monthly surplus is non-positive; cannot estimate completion.");
            return "Infinite (Surplus is $0 or negative!)";
        }

        BigDecimal monthsBig = distance.divide(monthlySurplus, 0, RoundingMode.CEILING);
        if (monthsBig.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            return "A very long time (goal is too far away)";
        }
        int months = monthsBig.intValue();

        assert months >= 0 : "Estimated months should be a positive value";
        return months + " months";
    }
}
