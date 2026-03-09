package seedu.duke.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Represents a snapshot of the user's BTO savings readiness.
 *
 * <p>Computed from a {@link Profile} and {@link ExpenseList} at the time of construction.
 * All fields are immutable after instantiation.</p>
 */
public class SummaryReport {
    public final String name;
    public final LocalDate deadline;
    public final BigDecimal btoGoal;
    public final BigDecimal monthlySalary;
    public final BigDecimal currentSavings;
    public final int percentage;
    public final BigDecimal distance;
    public final BigDecimal monthlySurplus;
    public final String estimate;

    /**
     * Constructs a {@code SummaryReport} from the user's current profile and expense list.
     *
     * @param profile     the user's financial profile.
     * @param expenseList the user's current list of expenses.
     */
    public SummaryReport(Profile profile, ExpenseList expenseList) {
        this.name = profile.getName();
        this.deadline = profile.getDeadline();
        this.btoGoal = profile.getBtoGoal();
        this.monthlySalary = profile.getMonthlySalary();
        this.currentSavings = profile.getCurrentSavings();

        this.distance = btoGoal.subtract(currentSavings);
        this.monthlySurplus = monthlySalary.subtract(expenseList.getTotal());
        this.percentage = computePercentage();
        this.estimate = computeEstimate();
    }

    /**
     * Computes the percentage of the BTO goal reached.
     * Returns {@code 0} if the BTO goal is zero or negative.
     *
     * @return percentage of goal reached, rounded to the nearest whole number.
     */
    private int computePercentage() {
        int percentage = 0;
        if (btoGoal.compareTo(BigDecimal.ZERO) > 0) {
            return currentSavings.multiply(BigDecimal.valueOf(100))
                    .divide(btoGoal, 0, RoundingMode.HALF_UP)
                    .intValue();
        }
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
            return "Reached! Go get that BTO!";
        }
        if (monthlySurplus.compareTo(BigDecimal.ZERO) <= 0) {
            return "Infinite (Surplus is $0 or negative!)";
        }
        int months = distance.divide(monthlySurplus, 0, RoundingMode.CEILING).intValue();
        return months + " months";
    }
}
