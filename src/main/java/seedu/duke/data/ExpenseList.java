package seedu.duke.data;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExpenseList {
    //List to save expenses
    private final ArrayList<Expense> expenses = new ArrayList<>();
    //place to store total
    private BigDecimal total = BigDecimal.ZERO;

    public void add(BigDecimal amount){
        Expense expense = new Expense(amount);
        expenses.add(expense);
        total = total.add(amount);
    }

    public Expense delete(int indexInList){
        int indexToDelete = indexInList - 1;
        Expense removed = expenses.remove(indexToDelete);
        total = total.subtract(removed.getAmount());
        return removed;
    }

    public BigDecimal getTotal(){
        return total;
    }

    public boolean isValidIndex(int inputIndex){
        //returns true if index input is writhin ranfge and false if not
        return inputIndex >= 1 && inputIndex <= expenses.size();
    }

    public int size(){
        //returns size of the List
        return expenses.size();
    }

    public boolean isEmpty() {
        return expenses.isEmpty();
    }

    public Expense get(int index) {
        return expenses.get(index);
    }

    public void clear() {
        expenses.clear();
        total = BigDecimal.ZERO;
    }
}
