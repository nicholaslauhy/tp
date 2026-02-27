package seedu.duke.categories;

public class Food implements Expenses{
    public String getTypeIcon() {
        return "[Food]";
    }

    public String toString() {
        return "You have assigned this expense to " + getTypeIcon();
    }
}
