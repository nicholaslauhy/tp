//@@author AK2003x
package seedu.duke.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import seedu.duke.util.BtoCalculator;

/**
 * Manages the user's personal financial data, including income, savings,
 * and specific targets for BTO downpayment planning.
 */
public class Profile {
    private String name = "friend";
    private BigDecimal monthlyAllowance;
    private BigDecimal currentSavings;
    private BigDecimal btoGoal;
    private BigDecimal contributionRatio;
    private LocalDate deadline = LocalDate.now();
    private BigDecimal housePrice;
    private int currentMonth = 1;

    /**
     * Initializes a profile with zero Allowance/Savings and a default 50/50 split ratio.
     */
    public Profile() {
        this.monthlyAllowance = BigDecimal.ZERO;
        this.currentSavings = BigDecimal.ZERO;
        this.contributionRatio = new BigDecimal("0.5");
        this.btoGoal = BigDecimal.ZERO;
    }

    /**
     * Sets the total purchase price of the BTO flat.
     * This value serves as the base for calculating the total downpayment
     * and the user's individual contribution goal.
     *
     * @param housePrice The total price of the HDB flat in dollars.
     */
    public void setHousePrice(BigDecimal housePrice) {
        assert housePrice != null && housePrice.compareTo(BigDecimal.ZERO) >= 0
                : "House price cannot be null or negative";
        this.housePrice = housePrice;
        BtoCalculator calc = new BtoCalculator(this.housePrice, this.contributionRatio);
        this.btoGoal = calc.yourShare;
    }

    /**
     * Returns the total purchase price of the BTO flat.
     * This value is used as the base for all downpayment and goal calculations.
     *
     * @return The total house price as a {@code BigDecimal}.
     */
    public BigDecimal getHousePrice() {
        return this.housePrice;
    }

    /**
     * Retrieves the target date by which the BTO savings goal should be met.
     *
     * @return The {@code LocalDate} representing the savings deadline.
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Updates the target savings deadline.
     *
     * <p>This date is used to calculate the remaining time in the BTO Readiness Report.</p>
     *
     * @param deadline The new {@code LocalDate} to set as the savings deadline.
     */
    public void setDeadline(LocalDate deadline) {
        assert deadline != null : "Deadline cannot be null";
        this.deadline = deadline;
    }

    /**
     * Updates the user's name.
     *
     * @param name The name to be associated with this profile.
     */
    public void setName(String name) {
        assert name != null : "Name cannot be null";
        this.name = name;
    }

    /**
     * Gets the user's name.
     *
     * @return The name stored in the profile, or "friend" if not set.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the individual share of the BTO target cost.
     * @param btoGoal The calculated BTO goal amount.
     */
    public void setBtoGoal(BigDecimal btoGoal) {
        assert btoGoal != null : "BTO goal cannot be null";
        assert btoGoal.compareTo(BigDecimal.ZERO) >= 0 : "BTO goal cannot be negative";
        this.btoGoal = btoGoal;
    }

    /**
     * Gets the individual's BTO target cost.
     * @return The BTO goal as a {@code BigDecimal}.
     */
    public BigDecimal getBtoGoal() {
        return btoGoal;
    }

    /**
     * Updates the user's monthly Allowance.
     * @param monthlyAllowance The new monthly income.
     */
    public void setMonthlyAllowance(BigDecimal monthlyAllowance) {
        assert monthlyAllowance != null : "Monthly allowance cannot be null";
        assert monthlyAllowance.compareTo(BigDecimal.ZERO) >= 0 : "Monthly allowance cannot be negative";
        this.monthlyAllowance = monthlyAllowance;
    }

    /**
     * Gets the user's monthly allowance.
     * @return The monthly allowance as a {@code BigDecimal}.
     */
    public BigDecimal getMonthlyAllowance() {
        return monthlyAllowance;
    }

    /**
     * Updates the user's current liquid savings.
     * @param currentSavings The new savings total.
     */
    public void setCurrentSavings(BigDecimal currentSavings) {
        assert currentSavings != null : "Current savings cannot be null";
        assert currentSavings.compareTo(BigDecimal.ZERO) >= 0 : "Current savings cannot be negative";
        this.currentSavings = currentSavings;
    }

    /**
     * Gets the user's current liquid savings.
     * @return The total savings as a {@code BigDecimal}.
     */
    public BigDecimal getCurrentSavings() {
        return currentSavings;
    }

    /**
     * Sets the share of the BTO cost relative to a partner.
     * @param contributionRatio A decimal representing the share (e.g., 0.5 for 50%).
     *                          Must be between 0.01 and 1.0 (minimum 1%).
     */
    public void setContributionRatio(BigDecimal contributionRatio) {
        assert contributionRatio != null : "Contribution ratio cannot be null";

        // Ensure the ratio is between 1% and 100% (0 is not allowed)
        assert contributionRatio.compareTo(new BigDecimal("0.01")) >= 0 &&
                contributionRatio.compareTo(BigDecimal.ONE) <= 0
                : "Contribution ratio must be between 0.01 and 1.0";
        this.contributionRatio = contributionRatio;

        // Set new btoGoal if ratio changed
        if (this.housePrice != null) {
            BtoCalculator calc = new BtoCalculator(this.housePrice, this.contributionRatio);
            this.btoGoal = calc.yourShare;
        }
    }

    /**
     * Gets the current BTO contribution share ratio.
     * @return The ratio as a {@code BigDecimal}.
     */
    public BigDecimal getContributionRatio() {
        return contributionRatio;
    }

    /**
     * Gets the current month number.
     * @return The current month as an integer (starting from 1).
     */
    public int getCurrentMonth() {

        return currentMonth;
    }

    /**
     * Sets the current month number.
     * This is primarily used by the Storage class to restore the user's progress.
     *
     * @param currentMonth The month number to set.
     */
    public void setCurrentMonth(int currentMonth) {
        assert currentMonth >= 1 : "Month must be at least 1";
        this.currentMonth = currentMonth;
    }

    /**
     * Advances to the next month.
     * Increments the current month counter by 1.
     */
    public void advanceMonth() {
        this.currentMonth++;
    }

    public void reset() {
        this.name = "friend";
        this.btoGoal = BigDecimal.ZERO;
        this.monthlyAllowance = BigDecimal.ZERO;
        this.currentSavings = BigDecimal.ZERO;
        this.contributionRatio = new BigDecimal("0.5");
        this.currentMonth = 1;
        this.housePrice = null;
        this.deadline = LocalDate.now();
    }
}
