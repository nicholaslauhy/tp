package seedu.duke;

/**
 * Parses raw user input into command tokens.
 *
 * <p>This class is responsible only for extracting the command keyword
 * from a full user input line. It does not validate arguments or execute
 * commands.</p>
 *
 * <p>Example:
 * <ul>
 *   <li>"add 12.50" -> "add"</li>
 *   <li>"delete 3" -> "delete"</li>
 * </ul>
 * </p>
 */
public class Parser {
    public static final int MAX_SPLIT_LENGTH = 2;

    /**
     * Extracts the command keyword from a raw input string.
     *
     * <p>The input is split at the first space character.</p>
     *
     * <p>No validation is performed. The returned string may be empty
     * if the input string is empty.</p>
     *
     * @param input Raw user input line.
     * @return The command keyword (first token before the first space).
     */
    public static String parseCommand(String input) {
        assert input != null : "Input to parseCommand should not be null";

        if (input.trim().isEmpty()) {
            return "";
        }

        String[] tokens = input.trim().split("\\s+", MAX_SPLIT_LENGTH);
        assert tokens.length > 0 : "Split should produce at least one token";
        return tokens[0].toLowerCase();
    }

    public static int parseIndex(String indexString) {
        assert indexString != null : "Index string should not be null";
        if (!indexString.matches("\\d+")) {
            return -1;
        }
        return Integer.parseInt(indexString);
    }
}
