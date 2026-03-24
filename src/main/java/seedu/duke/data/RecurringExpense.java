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
    public RecurringExpense(String name, BigDecimal amount, Category category){
        assert name != null && !name.isBlank() : "Recurring expense name must not be null or blank";
        assert amount != null : "Recurring expense amount must not be null";
        assert amount.compareTo(BigDecimal.ZERO) >= 0 : "Recurring expense amount must be non-negative";
        assert category != null : "Recurring expense category must not be null";

        this.name = name;
        this.amount = amount;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }
}
