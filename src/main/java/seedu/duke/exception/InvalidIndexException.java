//@@ Yuanfeng
package seedu.duke.exception;

/**
 * Exception thrown when a list index fails validation in FinTrackPro.
 *
 * <p>This exception is raised by {@code CommandHandler#parseAmount} when the input:
 * <ul>
 *   <li>Is missing (empty string)</li>
 *   <li>Is non-numeric (e.g., "abc")</li>
 *   <li>Is out of bounds (e.g., index exceeds current expense list size)</li>
 *   <li>Is not a whole number (e.g., "7.009")</li>
 *   <li>Is zero or negative (e.g., "-1")</li>
 * </ul>
 * </p>
 */
public class InvalidIndexException extends Exception {

    /**
     * Constructs an {@code InvalidIndexException} with the specified error message.
     *
     * @param message A description of why the index was invalid.
     */
    public InvalidIndexException(String message) {
        super(message);
    }
}
