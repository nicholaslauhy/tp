//@@author Yengfuan
package seedu.duke.exception;

/**
 * Exception thrown when a category string fails validation in FinTrackPro.
 *
 * <p>This exception is raised by {@code CommandHandler#parseAddArguments} when
 * the category token does not match any recognised category name.</p>
 *
 * <p>Recognised category values are: {@code FOOD}, {@code TRANSPORT},
 * {@code ENTERTAINMENT}, {@code UTILITIES}, {@code OTHER}.</p>
 */
public class InvalidCategoryException extends Exception {
    /**
     * Constructs an {@code InvalidCategoryException} with the specified error message.
     *
     * @param message A description of why the category was invalid.
     */
    public InvalidCategoryException(String message) {
        super(message);
    }
}
