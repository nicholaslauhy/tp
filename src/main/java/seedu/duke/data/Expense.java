package seedu.duke.data;

import java.math.BigDecimal;

/**
 * Represents an individual financial expense within the FinTrackPro system.
 * * <p>Each expense maintains a monetary amount.</p>
 */
public class Expense {

    private final BigDecimal amount;

    /**
     * Constructs a new {@code Expense} with the specified amount.
     * The default category is set to "OTHER".
     *
     * @param amount The monetary value of the expense.
     */
    public Expense(BigDecimal amount) {
        //Invariant: Amount added must never be null
        assert amount != null : "Expense amount should not be null";
        //Invariant: Amount input must never be negative
        assert amount.compareTo(BigDecimal.ZERO) >= 0 : "Expense amount must be non-negative";
        this.amount = amount;
    }

    /**
     * Gets the monetary amount of this expense.
     *
     * @return The expense amount as a {@code BigDecimal}.
     */
    public BigDecimal getAmount() {
        //Invariant: Amount must never be null after successful construction
        assert amount != null : "Expense should never be null after construction.";
        return amount;
    }

    /**
     * Returns a string representation of the expense amount, prefixed with a dollar sign.
     *
     * @return a {@code String} in the format {@code "$<amount>"}, e.g. {@code "$42.50"}
     */
    @Override
    public String toString() {
        return "$" + amount ;
    }

}
