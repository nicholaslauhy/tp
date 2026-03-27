//@@author wKynaston
package seedu.duke.data;

import java.util.ArrayList;
import java.math.BigDecimal;

public class RecurringExpenseList {

    private final ArrayList<RecurringExpense> recurringExpenses = new ArrayList<>();
    /**
     * Adds a recurring expense template to the list.
     *
     * @param recurringExpense recurring expense template to add
     */
    public void add(RecurringExpense recurringExpense) {
        assert recurringExpense != null : "Recurring expense must not be null";
        recurringExpenses.add(recurringExpense);
    }
    /**
     * Deletes and returns the recurring expense at the specified 1-based index.
     *
     * @param indexInList 1-based index of the recurring expense
     * @return removed recurring expense
     */
    public RecurringExpense delete(int indexInList) {
        int indexToDelete = indexInList - 1;
        return recurringExpenses.remove(indexToDelete);
    }
    /**
     * Returns the recurring expense at the specified 0-based index.
     *
     * @param index 0-based index
     * @return recurring expense at the specified index
     */
    public RecurringExpense get(int index) {
        return recurringExpenses.get(index);
    }
    /**
     * Returns the number of recurring expense templates stored.
     *
     * @return size of recurring expense list
     */
    public int size() {
        return recurringExpenses.size();
    }
    /**
     * Returns whether the recurring expense list is empty.
     *
     * @return {@code true} if empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return recurringExpenses.isEmpty();
    }
    /**
     * Returns whether the given 1-based index is valid.
     *
     * @param inputIndex index to validate
     * @return {@code true} if valid, {@code false} otherwise
     */
    public boolean isValidIndex(int inputIndex) {
        return inputIndex >= 1 && inputIndex <= recurringExpenses.size();
    }
    /**
     * Returns the total amount of all recurring expenses currently stored.
     *
     * @return cumulative total of recurring expenses
     */
    public BigDecimal getTotal() {
        BigDecimal total = java.math.BigDecimal.ZERO;

        for (RecurringExpense recurringExpense : recurringExpenses) {
            assert recurringExpense != null : "Recurring expense in list must not be null";
            total = total.add(recurringExpense.getAmount());
        }

        assert total.compareTo(java.math.BigDecimal.ZERO) >= 0
                : "Recurring expense total must never be negative";
        return total;
    }
    /**
     * Removes all recurring expense templates from the list.
     */
    public void clear() {
        recurringExpenses.clear();
    }
    /**
     * Returns all recurring expenses whose name or category contains the given keyword.
     *
     * @param keyword keyword to search for
     * @return matching recurring expenses
     */
    public ArrayList<RecurringExpense> findMatches(String keyword) {
        assert keyword != null : "Find keyword must not be null";

        ArrayList<RecurringExpense> matches = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase();

        for (RecurringExpense recurringExpense : recurringExpenses) {
            assert recurringExpense != null : "Recurring expense in list must not be null";

            if (recurringExpense.getName().toLowerCase().contains(normalizedKeyword)
                    || recurringExpense.getCategory().getName().toLowerCase().contains(normalizedKeyword)) {
                matches.add(recurringExpense);
            }
        }

        return matches;
    }
}
