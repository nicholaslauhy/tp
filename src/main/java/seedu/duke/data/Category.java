package seedu.duke.data;

public enum Category {
    FOOD,
    TRANSPORT,
    ENTERTAINMENT,
    UTILITIES,
    OTHER;

    public static Category fromString(String input) {
        return Category.valueOf(input.toUpperCase());
    }
}
