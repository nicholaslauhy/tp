package seedu.duke.data;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExpenseList {
    //List to save expenses
    private final ArrayList<BigDecimal> expenses = new ArrayList<>();
    //place to store total
    private BigDecimal total = BigDecimal.ZERO;

    public void add(BigDecimal amount){
        expenses.add(amount);
        total = total.add(amount);
    }

    public BigDecimal getTotal(){
        return total;
    }
}
