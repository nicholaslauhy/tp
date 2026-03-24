package seedu.duke.data;

import java.math.BigDecimal;
import seedu.duke.category.Category;
/**
 * Represents a recurring expense template.
 */
public class RecurringExpense {

    private final String name;
    private final BigDecimal amount;
    private final Category category;
    /**
     * Constructs a recurring expense template with the specified name,
     * amount, and category.
     *
     * @param name Name of the recurring expense.
     * @param amount Amount charged each month.
     * @param category Category of the recurring expense.
     */
    public RecurringExpense(String name, BigDecimal amount, Category category){
        assert name != null && !name.isBlank() : "Recurring expense name must not be null or blank";
        assert amount != null : "Recurring expense amount must not be null";
        assert amount.compareTo(BigDecimal.ZERO) >= 0 : "Recurring expense amount must be non-negative";
        assert category != null : "Recurring expense category must not be null";

        this.name = name;
        this.amount = amount;
        this.category = category;
    }
    /**
     * Returns the name of this recurring expense.
     *
     * @return recurring expense name
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the amount of this recurring expense.
     *
     * @return recurring expense amount
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * Returns the category of this recurring expense.
     *
     * @return recurring expense category
     */
    public Category getCategory() {
        return category;
    }
    /**
     * Returns a formatted string representation of this recurring expense.
     *
     * @return a string in the format {@code "[RECURRING] name $amount [CATEGORY]"}
     */
    @Override
    public String toString() {
        return "[RECURRING]" + "[" + category + "] " + name + " $" + amount;
    }
}
