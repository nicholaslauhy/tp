package seedu.duke.categories;

public class Miscellaneous implements Expenses{
    public String getTypeIcon() {
        return "[Misc]";
    }

    public String toString() {
        return "This expense has been automatically assigned to " + getTypeIcon();
    }
}
