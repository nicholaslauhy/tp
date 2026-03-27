//@@author Yengfuan
package seedu.duke.exception;

/**
 * Exception thrown when a monetary amount fails validation in FinTrackPro.
 *
 * <p>This exception is raised by {@code CommandHandler#parseAmount} when the input:
 * <ul>
 *   <li>Is missing (empty string)</li>
 *   <li>Is non-numeric (e.g., "abc")</li>
 *   <li>Is negative (e.g., "-1")</li>
 *   <li>Exceeds 2 decimal places (e.g., "7.009")</li>
 * </ul>
 * </p>
 */
public class InvalidAmountException extends Exception {

    /**
     * Constructs an {@code InvalidAmountException} with the specified error message.
     *
     * @param message A description of why the amount was invalid.
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}
