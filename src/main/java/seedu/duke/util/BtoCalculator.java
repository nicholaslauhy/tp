package seedu.duke.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculates the BTO downpayment amounts for a given house price and contribution ratio.
 *
 * <p>The total downpayment is derived from a 2.5% base rate on the house price,
 * plus an additional 10% legal fee surcharge on that base amount.
 * The user's personal share is then computed by applying their contribution ratio
 * to the total downpayment.</p>
 *
 * <p>All monetary results are rounded to 2 decimal places using
 * {@link java.math.RoundingMode#HALF_UP}.</p>
 */
public class BtoCalculator {

    /** The total downpayment amount (base downpayment + legal fees), rounded to 2 decimal places. */
    public final BigDecimal totalDownpayment;

    /** The user's personal share of the total downpayment, rounded to 2 decimal places. */
    public final BigDecimal yourShare;

    /**
     * Constructs a {@code BtoCalculator} and computes the downpayment figures.
     *
     * <p>Calculation breakdown:
     * <ol>
     *   <li>Base downpayment = {@code housePrice × 0.025}</li>
     *   <li>Legal fees       = {@code baseDownpayment × 1.1}</li>
     *   <li>Total downpayment = {@code baseDownpayment + legalFees}</li>
     *   <li>Your share       = {@code totalDownpayment × ratio}</li>
     * </ol>
     * </p>
     *
     * @param housePrice the total purchase price of the BTO flat, shared between both partners.
     * @param ratio      the user's fractional contribution (e.g., {@code 0.6} for 60%).
     *                   Must be between {@code 0.0} and {@code 1.0} inclusive.
     */
    public BtoCalculator(BigDecimal housePrice, BigDecimal ratio) {
        BigDecimal downpayment = housePrice.multiply(new BigDecimal("0.025"));
        BigDecimal legalFees = downpayment.multiply(BigDecimal.valueOf(1.1));
        this.totalDownpayment = downpayment.add(legalFees).setScale(2, RoundingMode.HALF_UP);
        this.yourShare = totalDownpayment.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
    }
}
