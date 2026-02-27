package seedu.duke.categories;

public class Groceries implements Expenses{
    public String getTypeIcon() {
        return "[Groceries]";
    }

    public String toString() {
        return "You have assigned this expense to " + getTypeIcon();
    }
}
