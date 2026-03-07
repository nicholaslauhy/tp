package seedu.duke.data;

import java.math.BigDecimal;

/**
 * Manages the user's personal financial data, including income, savings,
 * and specific targets for BTO downpayment planning.
 */
public class Profile {
    private String name = "friend";
    private BigDecimal monthlySalary;
    private BigDecimal currentSavings;
    private BigDecimal spendingGoal;
    private BigDecimal btoGoal;
    private BigDecimal contributionRatio;

    /**
     * Initializes a profile with zero Salary/Savings and a default 50/50 split ratio.
     */
    public Profile() {
        this.monthlySalary = BigDecimal.ZERO;
        this.currentSavings = BigDecimal.ZERO;
        this.contributionRatio = new BigDecimal("0.5");
        this.spendingGoal = BigDecimal.ZERO;
        this.btoGoal = BigDecimal.ZERO;
    }

    /**
     * Updates the user's name.
     *
     * @param name The name to be associated with this profile.
     */
    public void setName(String name) {
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
     * Updates the user's monthly salary.
     * @param monthlySalary The new monthly income.
     */
    public void setMonthlySalary(BigDecimal monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    /**
     * Gets the user's monthly salary.
     * @return The monthly salary as a {@code BigDecimal}.
     */
    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    /**
     * Updates the user's current liquid savings.
     * @param currentSavings The new savings total.
     */
    public void setCurrentSavings(BigDecimal currentSavings) {
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
     */
    public void setContributionRatio(BigDecimal contributionRatio) {
        this.contributionRatio = contributionRatio;
    }

    /**
     * Gets the current BTO contribution share ratio.
     * @return The ratio as a {@code BigDecimal}.
     */
    public BigDecimal getContributionRatio() {
        return contributionRatio;
    }
}
