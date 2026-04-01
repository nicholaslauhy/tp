package seedu.duke;

import seedu.duke.util.LoggerUtil;

import java.util.logging.Logger;

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
     * Logger for recording command handler events.
     * Routes all output to the central {@code logs/fintrack.log} via {@link LoggerUtil}.
     */
    private static final Logger logger = LoggerUtil.getLogger(Parser.class);

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
            // Log at WARNING: user provided empty string which is rejected
            logger.warning("Input rejected | reason: Input to parseCommand is null");
            return "";
        }

        String[] tokens = input.trim().split("\\s+", MAX_SPLIT_LENGTH);
        assert tokens.length > 0 : "Split should produce at least one token";

        // Log at FINE: parseCommand is a low-level detail, not a key app event
        logger.fine("parseCommand succeeded | token: " + tokens[0].toLowerCase());
        return tokens[0].toLowerCase();
    }

    public static int parseIndex(String indexString) {
        assert indexString != null : "Index string should not be null";
        if (!indexString.matches("\\d+")) {
            return -1;
        }
        try {
            // Log at FINE: readRatio is a low-level detail, not a key app event
            logger.fine("parseIndex succeeded | index string: " + indexString);
            return Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            // Log at WARNING: user provided invalid format string which is rejected
            logger.warning("Index string rejected | reason: Index string does not match integer format");
            return -1;
        }
    }
}
