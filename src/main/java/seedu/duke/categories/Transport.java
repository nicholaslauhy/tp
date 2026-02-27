package seedu.duke.categories;

public class Transport implements Expenses {
    public String getTypeIcon() {
        return "[Transport]";
    }

    public String toString() {
        return "You have assigned this expense to " + getTypeIcon();
    }
}
