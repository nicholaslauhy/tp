package seedu.duke.data;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Manages a collection of {@link Expense} objects and tracks the cumulative total.
 *
 * <p>This class provides methods to add, delete, and retrieve expenses while
 * automatically updating the running total using {@code BigDecimal} for precision.</p>
 */
public class ExpenseList {
    //List to save expenses
    private final ArrayList<Expense> expenses = new ArrayList<>();
    //place to store total
    private BigDecimal total = BigDecimal.ZERO;


    /**
     * Creates a new expense with the specified name, amount, and category,
     * adds it to the list, and updates the running total.
     *
     * @param name Name or description of the expense.
     * @param amount Monetary value of the expense.
     * @param category Category assigned to the expense.
     */
    public void add(String name, BigDecimal amount, Category category){
        assert name != null && !name.isBlank() : "Expense name should not be null or blank.";
        assert amount != null : "Expense amount should not be null.";
        assert category != null : "Expense category should not be null.";
        //Added to capture pre mutation state for post mutation assertions
        int sizeBeforeAdd = expenses.size();
        BigDecimal totalBeforeAdd = total;

        Expense expense = new Expense(name, amount, category);
        expenses.add(expense);
        total = total.add(amount);
        //Post add invariant: List must have grown by exacrtly ony entry
        assert expenses.size() == sizeBeforeAdd + 1
                : "List size should have increaseed by 1 after add";
        // Post-add invariant: total must have been increased only by the amount that was added
        assert total.compareTo(totalBeforeAdd.add(amount)) == 0
                : "Total should increase by exactly the added amount.";

        // Post-add invariant: total must never be negative
        assert total.compareTo(BigDecimal.ZERO) >= 0
                : "Total must never be negative.";

    }

    /**
     * Removes an expense from the list based on its 1-based index and subtracts
     * its value from the total.
     *
     * @param indexInList The 1-based index of the expense as displayed to the user.
     * @return The {@link Expense} object that was removed.
     */
    public Expense delete(int indexInList){
        //Capture pre-mutation size to use in post delete assertions
        int sizeBeforeDelete = expenses.size();
        BigDecimal totalBeforeDelete = total;
        int indexToDelete = indexInList - 1;
        Expense removed = expenses.remove(indexToDelete);
        total = total.subtract(removed.getAmount());
        // Post-delete invariant: list must have shrunk by exactly one entry
        assert expenses.size() == sizeBeforeDelete - 1
                : "List size should have decreased by 1 after delete.";
        //Post-delete invariant: List must have been excartly deleted by the amoutn that was removed
        assert total.compareTo(totalBeforeDelete.subtract(removed.getAmount())) == 0
                : "Total should decrease exactly by removed amount.";
        // Post-delete invariant: total must never be negative
        assert total.compareTo(BigDecimal.ZERO) >= 0
                : "Total must never be negative.";
        return removed;
    }

    /**
     * Gets the cumulative total of all expenses currently in the list.
     *
     * @return The total expenditure as a {@code BigDecimal}.
     */
    public BigDecimal getTotal(){
        // Invariant: total should never be null or negative at any point
        assert total != null : "Total should never be null.";
        assert total.compareTo(BigDecimal.ZERO) >= 0 : "Total must never be negative.";
        return total;
    }

    /**
     * Validates if a given 1-based index is within the bounds of the current list.
     *
     * @param inputIndex The index to check.
     * @return {@code true} if the index is valid; {@code false} otherwise.
     */
    public boolean isValidIndex(int inputIndex){
        //returns true if index input is within range and false if not
        return inputIndex >= 1 && inputIndex <= expenses.size();
    }

    /**
     * Returns the number of expenses currently in the list.
     *
     * @return The size of the expense list.
     */
    public int size(){
        //returns size of the List
        return expenses.size();
    }

    /**
     * Checks whether the expense list is empty.
     *
     * @return {@code true} if there are no expenses; {@code false} otherwise.
     */
    public boolean isEmpty() {
        return expenses.isEmpty();
    }

    /**
     * Retrieves an expense from the list at the specified 0-based index.
     *
     * @param index The 0-based position of the expense in the internal list.
     * @return The {@link Expense} at the specified position.
     */
    public Expense get(int index) {
        Expense expense = expenses.get(index);

        // Invariant: entries stored in the list must never be null
        assert expense != null : "Retrieved expense should never be null.";
        return expense;
    }

    /**
     * Removes all expenses from the list and resets the running total to zero.
     */
    public void clear() {
        expenses.clear();
        total = BigDecimal.ZERO;
        // Post-clear invariant: total must be reset to exactly zero
        assert total.compareTo(BigDecimal.ZERO) == 0 : "Total should be zero after clear.";
    }
}
