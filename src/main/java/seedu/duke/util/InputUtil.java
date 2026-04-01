package seedu.duke.util;

import seedu.duke.CommandHandler;
import seedu.duke.ui.Ui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Utility class for validated user input handling in FinTrackPro.
 *
 * <p>Provides helper methods to:
 * <ul>
 *   <li>Format monetary values consistently</li>
 *   <li>Read and validate monetary inputs</li>
 *   <li>Read and validate future dates</li>
 * </ul>
 * </p>
 *
 * <p>This class performs input validation loops and only returns values
 * once valid input has been received.</p>
 */
public class InputUtil {
    private static final Logger logger = LoggerUtil.getLogger(CommandHandler.class);
    private static final NumberFormat MONEY_FMT =
            NumberFormat.getCurrencyInstance(Locale.US);

    /**
     * Formats a monetary amount using US currency formatting.
     *
     * <p>Ensures:
     * <ul>
     *   <li>Two decimal places</li>
     *   <li>Comma thousands separator</li>
     *   <li>Currency symbol prefix</li>
     * </ul>
     * Example: 12250 -> $12,250.00</p>
     *
     * @param amount Monetary value to format.
     * @return Formatted currency string.
     */
    public static String formatMoney(BigDecimal amount) {
        assert amount != null : "formatMoney: amount must not be null";
        // Log at INFO: user provided valid string input which is accepted
        logger.info("formatMoney succeeded | amount: " + amount + ", formatted: " + MONEY_FMT.format(amount));
        return MONEY_FMT.format(amount);
    }

    /**
     * Prompts the user to enter a monetary value and validates the input.
     *
     * <p>Accepted formats:
     * <ul>
     *   <li>Whole numbers (e.g., 10250)</li>
     *   <li>Numbers with up to 2 decimal places (e.g., 10250.5, 10250.50)</li>
     * </ul>
     * </p>
     *
     * <p>Rejected inputs include:
     * <ul>
     *   <li>Negative numbers</li>
     *   <li>More than 2 decimal places</li>
     *   <li>Currency symbols or commas</li>
     *   <li>Malformed numeric strings</li>
     * </ul>
     * </p>
     *
     * <p>This method repeatedly prompts until valid input is received.</p>
     *
     * @param ui UI component used for displaying prompts and messages.
     * @param in Scanner used to read user input.
     * @param prompt Prompt message shown to the user.
     * @return 2.5% of the validated monetary amount.
     */
    public static BigDecimal readMoney(Ui ui, Scanner in, String prompt) {
        assert ui != null : "readMoney: ui must not be null";
        assert in != null : "readMoney: in must not be null";
        assert prompt != null : "readMoney: prompt must not be null";

        while (true) {
            String moneyString = ui.readLine(in, prompt).trim();

            if (!moneyString.matches("\\d+(\\.\\d{1,2})?")) {
                // Log at WARNING: user provided invalid format string which is rejected
                logger.warning("readMoney unsuccessful | reason: invalid formatting");
                ui.printLine("Bruh I need a valid amount like " +
                        "10250 or 10250.50 (numbers only, max 2 dp). Try again.");
                continue;
            }

            BigDecimal result = new BigDecimal(moneyString);
            assert result.compareTo(BigDecimal.ZERO) >= 0 : "readMoney: parsed amount must be non-negative";
            return result;

        }
    }

    /**
     * Prompts the user to enter a future date in ISO format (YYYY-MM-DD).
     *
     * <p>Validation rules:
     * <ul>
     *   <li>Input must follow the format YYYY-MM-DD</li>
     *   <li>Date must be strictly after today's date</li>
     * </ul>
     * </p>
     *
     * <p>If validation fails, the user is re-prompted until valid input is provided.</p>
     *
     * @param ui UI component used for displaying prompts and messages.
     * @param in Scanner used to read user input.
     * @param prompt Prompt message shown to the user.
     * @return A {@link LocalDate} that is strictly after today.
     */
    public static LocalDate readFutureDate(Ui ui, Scanner in, String prompt) {
        assert ui != null : "readFutureDate: ui must not be null";
        assert in != null : "readFutureDate: in must not be null";
        assert prompt != null : "readFutureDate: prompt must not be null";
        while (true) {
            String s = ui.readLine(in, prompt).trim();

            try {
                LocalDate date = LocalDate.parse(s);
                LocalDate today = LocalDate.now();

                if (!date.isAfter(today)) {
                    // Log at WARNING: user provided non-future date which is rejected
                    logger.warning("readFutureDate unsuccessful | reason: date input is not a future date");
                    ui.printLine("Deadline must be a future date. Try again.");
                    continue;
                }

                assert date.isAfter(LocalDate.now()) : "readFutureDate: returned date must be strictly in the future";
                // Log at FINE: readFutureDate is a low-level detail, not a key app event
                logger.fine("readFutureDate successful | date: " + date);
                return date;

            } catch (DateTimeParseException e) {
                // Log at WARNING: user provided invalid formatted string which is rejected
                logger.warning("readFutureDate unsuccessful | reason: invalid formatting");
                ui.printLine("Date format needs to be YYYY-MM-DD (e.g., 2026-12-31). Try again.");
            }
        }
    }

    /**
     * Prompts the user to enter a contribution ratio and validates the input range.
     *
     * <p>Accepted formats:
     * <ul>
     * <li>Decimals between 0.0 and 1.0 inclusive (e.g., 0, 1, 0.5, 0.75)</li>
     * <li>Max 2 decimal places</li>
     * </ul>
     * </p>
     *
     * <p>Rejected inputs include:
     * <ul>
     * <li>Negative numbers (e.g., -0.6)</li>
     * <li>Values greater than 1 (e.g., 1.1)</li>
     * <li>More than 2 decimal places (e.g., 0.555)</li>
     * <li>Non-numeric strings</li>
     * </ul>
     * </p>
     *
     * <p>This method repeatedly prompts until a valid ratio is received.</p>
     *
     * @param ui UI component used for displaying prompts and messages.
     * @param in Scanner used to read user input.
     * @param prompt Prompt message shown to the user.
     * @return A validated {@link BigDecimal} between 0 and 1.
     */
    public static BigDecimal readRatio(Ui ui, Scanner in, String prompt) {
        assert ui != null : "readRatio: ui must not be null";
        assert in != null : "readRatio: in must not be null";
        assert prompt != null : "readRatio: prompt must not be null";

        while (true) {
            String input = ui.readLine(in, prompt).trim();

            // Regex: Digits followed by a dot and 1-2 digits.
            // This prevents negative signs (-), symbols, and excessive decimals.
            if (!input.matches("\\d+(\\.\\d{1,2})?")) {
                // Log at WARNING: user provided invalid formatted string which is rejected
                logger.warning("readRatio unsuccessful | reason: invalid formatting");
                ui.printLine("EH WRONG FORMAT! Enter a decimal between 0 and 1 (e.g., 0.5).");
                continue;
            }
            try {
                BigDecimal ratio = new BigDecimal(input);

                // Check if between 0.0 & 1.0
                if (ratio.compareTo(BigDecimal.ZERO) < 0 || ratio.compareTo(BigDecimal.ONE) > 0) {
                    // Log at WARNING: user provided out of bounds input which is rejected
                    logger.warning("readRatio unsuccessful | reason: out of specified bounds");
                    ui.printLine("Brother...Ratio must be between 0 and 1. Try again!");
                    continue;
                }

                assert ratio.compareTo(BigDecimal.ZERO) >= 0 && ratio.compareTo(BigDecimal.ONE) <= 0
                        : "readRatio: returned ratio must be between 0.0 and 1.0 inclusive";
                // Log at FINE: readRatio is a low-level detail, not a key app event
                logger.fine("readRatio successful | ratio: " + ratio);
                return ratio;
            } catch (NumberFormatException e) {
                // Log at WARNING: user provided non-numeric string which is rejected
                logger.warning("readRatio unsuccessful | reason: input is not a number");
                ui.printLine("Pleaseee input a number. Try again!!!");
            }
        }
    }
}
