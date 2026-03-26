package seedu.duke.data;

import java.math.BigDecimal;
import seedu.duke.category.Category;

/**
 * Represents an individual financial expense within the FinTrackPro system.
 * * <p>Each expense maintains a monetary amount.</p>
 */
public class Expense {
    private final String name;
    private final BigDecimal amount;
    private final Category category;
    private final int insertionOrder;

    /**
     * Constructs a new {@code Expense} with the specified name, amount, category and insertion order
     *
     * @param name Name or description of the expense.
     * @param amount Monetary value of the expense.
     * @param category Category assigned to the expense.
     * @param insertionOrder Insertion order assigned to the expense, used for sorting.
     */
    public Expense(String name, BigDecimal amount, Category category, int insertionOrder) {
        //Invariant: Name added must never be null
        assert name != null && !name.isBlank() : "Expense name should not be null or blank";
        //Invariant: Amount added must never be null
        assert amount != null : "Expense amount should not be null";
        //Invariant: Amount added must never be negative
        assert amount.compareTo(BigDecimal.ZERO) >= 0 : "Expense amount must be non-negative";
        //Invariant: Category Added should not be null
        assert category != null : "Expense category should not be null";
        //Invariant: Insertion order should be more than 0
        assert insertionOrder >= 0 : "Insertion order must be non-negative";

        this.name = name;
        this.amount = amount;
        this.category = category;
        this.insertionOrder = insertionOrder;
    }

    /**
     * Returns the name of this expense.
     *
     * @return Expense name or description.
     */
    public String getName() {
        assert name != null : "Expense name should never be null";
        return name;
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
     * Returns the category of this expense.
     *
     * @return Expense category.
     */
    public Category getCategory() {
        assert category != null : "Expense category should never be null";
        return category;
    }
    /**
     * Returns a formatted string representation of this expense.
     *
     * <p>The output includes the category, name, and amount of the expense
     * in a user-friendly format.</p>
     *
     * @return a {@code String} in the format {@code "[CATEGORY] name $amount"},
     *         e.g. {@code "[FOOD] lunch $12.50"}
     */
    @Override
    public String toString() {
        return "[" + category + "] " + name + " $" + amount;
    }

    public int getInsertionOrder() {
        return insertionOrder;
    }
}
