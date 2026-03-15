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
     * Creates a new expense with the specified amount, adds it to the list,
     * and updates the running total.
     *
     * @param amount The monetary value of the expense to add.
     */
    public void add(BigDecimal amount){
        //Added to capture pre mutation state for post mutation assertions
        int sizeBefore = expenses.size();
        BigDecimal totalBefore = total;

        Expense expense = new Expense(amount);
        expenses.add(expense);
        total = total.add(amount);
        //Post add invariant: List must have grown by exacrtly ony entry
        assert expenses.size() == sizeBefore + 1
                : "List size should have increaseed by 1 after add";
        // Post-add invariant: total must not have decreased
        assert total.compareTo(totalBefore) >= 0
                : "Total should not decrease after adding an expense.";

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
        int indexToDelete = indexInList - 1;
        Expense removed = expenses.remove(indexToDelete);
        total = total.subtract(removed.getAmount());
        return removed;
    }

    /**
     * Gets the cumulative total of all expenses currently in the list.
     *
     * @return The total expenditure as a {@code BigDecimal}.
     */
    public BigDecimal getTotal(){
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
        return expenses.get(index);
    }

    /**
     * Removes all expenses from the list and resets the running total to zero.
     */
    public void clear() {
        expenses.clear();
        total = BigDecimal.ZERO;
    }
}
