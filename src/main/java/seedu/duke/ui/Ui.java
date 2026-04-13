package seedu.duke.ui;

import seedu.duke.data.SummaryReport;
import seedu.duke.util.InputUtil;
import seedu.duke.util.LoggerUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Handles all user-facing input and output operations for FinTrackPro.
 *
 * <p>This class centralizes console interaction logic, ensuring that:
 * <ul>
 *   <li>All output formatting passes through a single interface</li>
 *   <li>User prompts are displayed consistently</li>
 *   <li>Business logic remains separated from presentation logic</li>
 * </ul>
 * </p>
 *
 * <p>This class does not perform validation or computation;
 * it is strictly responsible for communication with the user.</p>
 */
public class Ui {
    private static final Logger logger = LoggerUtil.getLogger(Ui.class);
    /**
     * Displays the welcome message and ASCII logo when the application starts.
     *
     * <p>This method is intended to be called once at application startup.</p>
     */
    public void showWelcome(){
        logger.info("Displaying welcome message and ASCII logo.");
        String logo = """
            +---------+
            |   HDB   |
            |  [ ][ ] |
            |_________|
      Secure the Keys, Secure the Dream
            """;
        printLine("Welcome to Fintrack Pro!\n" + logo);
        printLine("I am FinBro's brother, FinTrack Pro!");
    }

    public void greet(String name){
        assert name != null : "Name should not be null";
        logger.info("Greeting user: " + name);
        printLine("Nice to meet you, " + name + "!");
    }

    public void goodBye(String name){
        assert name != null : "Name should not be null";
        logger.info("Application exiting for user: " + name);
        printLine("Goodbye " + name + ". Stay disciplined and get that house that you always wanted!");
    }

    public void printLine(String message){
        assert message != null : "Message should not be null";
        System.out.println(message);
    }

    /**
     * Displays an optional prompt and reads a full line of input from the user.
     *
     * <p>If the prompt is null or empty, no prompt is displayed.</p>
     *
     * @param in Scanner used to read user input.
     * @param prompt Prompt message displayed before reading input.
     * @return The raw line entered by the user.
     */
    public String readLine(Scanner in, String prompt) {
        assert in != null : "Scanner should not be null";
        if (prompt != null && !prompt.isEmpty()) {
            logger.info("Displaying prompt to user: " + prompt);
            printLine(prompt);
        }

        if (!in.hasNextLine()) {
            logger.warning("Input stream closed (EOF). Exiting gracefully.");
            printLine("\nInput stream closed. Exiting FinTrackPro...");
            System.exit(0);
        }

        String input = in.nextLine();
        logger.fine("Raw user input received: [" + input + "]");
        assert input != null : "Input read should not be null";
        return input;
    }

    /**
     * Displays a list of supported commands and their usage.
     *
     * <p>This method prints the currently supported CLI commands
     * and brief descriptions of their purpose.</p>
     */
    public void showHelpMessage() {
        logger.info("Displaying help message commands.");

        printLine("General Commands");
        printLine("'help'    - view all current commands");
        printLine("'summary' - generate your BTO readiness report based on your goals");
        printLine("'bye'     - exit the program");
        printLine("");

        printLine("Daily Transaction Commands");
        printLine("'add'      NAME AMOUNT CATEGORY RECURRING - " +
                "add a new expense" +   "\n(e.g., add lunch 5.50 FOOD for not recurring and" +
                " add lunch 5.50 FOOD recurring for recurring)" +
                "\n(Valid categories to add: FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER)");
        printLine("'list'     - view all current expenses and your total spent");
        printLine("'delete'   INDEX - remove a specific expense from your list");
        printLine("'deleterecurring' INDEX - remove a recurring monthly expense");
        printLine("");

        printLine("Profile & Goal Management");
        printLine("'sort'   KEYWORD - sort the expenditure list by category, alphabetical order or recency " +
                "(e.g sort name, sort recent, sort category)");
        printLine("'savings'   - add a surplus amount to your existing savings");
        printLine("'allowance' - update your monthly allowance");
        printLine("'ratio'     - update your BTO contribution ratio (0.01 to 1.0, max 2 dp)");
        printLine("'save'      - archive current month's expenses and advance to next month");
        printLine("'clear'   - wipe all current expenses from the list");
        printLine("'reset'   - wipes all profile data and expenses to start fresh.");
        printLine("");
    }

    /**
     * Displays the current monthly allowance and prompts the user to provide a new value.
     * This provides context to the user before they perform an update to their profile.
     *
     * @param currentAllowance The existing monthly allowance stored in the User's profile.
     */
    public void promptForAllowance(BigDecimal currentAllowance) {
        assert currentAllowance != null : "Current allowance cannot be null for display";
        printLine("Current Monthly Allowance: " + InputUtil.formatMoney(currentAllowance));
    }

    /**
     * Displays the current contribution ratio in both percentage and decimal formats,
     * then prompts the user for a new decimal input.
     *
     * @param currentRatio The existing contribution ratio (0.01 to 1.0) from the User's profile.
     */
    public void promptForRatio(BigDecimal currentRatio) {
        assert currentRatio != null : "Current ratio cannot be null for display";
        // Convert 0.5 to 50 for the display string
        BigDecimal percentage = currentRatio.multiply(new BigDecimal("100"));
        printLine("Current Contribution Ratio: " + percentage.toPlainString() + "% (" + currentRatio + ")");
    }

    //@@author Jairusljr
    /**
     * Prints a formatted BTO Readiness Report to the console.
     *
     * @param report a {@link SummaryReport} containing the user's precomputed financial snapshot.
     */
    public void showSummaryReport(SummaryReport report) {
        assert report.name != null : "Report name should not be null";
        assert report.deadline != null : "Deadline should not be null";
        assert report.currentSavings != null : "Current savings should not be null";
        assert report.estimate != null : "Estimate should not be null";

        logger.info("Rendering SummaryReport for user: " + report.name);
        printLine("===== BTO Readiness Report =====");
        printLine("User: " + report.name);
        printLine("Readiness Level: " + report.readinessLevel);
        printLine("BTO Goal: " + InputUtil.formatMoney(report.btoGoal) + " (your share + fees)");

        LocalDate today = LocalDate.now();
        Period period = Period.between(today, report.deadline);

        int monthsLeft = period.getYears() * 12 + period.getMonths();
        if (period.getDays() > 0) {
            monthsLeft++;
        }
        if (monthsLeft < 0) {
            monthsLeft = 0;
        }

        assert monthsLeft >= 0 : "Months left should not be negative";

        System.out.println("Deadline: " + report.deadline + " (" + report.adjustedMonthsLeft + " months)");
        printLine("");
        String savingsLine = InputUtil.formatMoney(report.currentSavings) + " (" + report.percentage + "% reached)";
        printLine("Current Savings: " + savingsLine);
        printLine("Distance to Goal: " + InputUtil.formatMoney(report.distance));
        printLine("Adjusted Minimum Savings: " + InputUtil.formatMoney(report.monthlyRequired) + " / month");
        printLine("");
        printLine("Monthly Allowance: " + InputUtil.formatMoney(report.monthlyAllowance));
        printLine("Total Expenditure: " + InputUtil.formatMoney(report.totalExpenditure));
        printLine("Monthly Surplus (Allowance - Expenditure): " + InputUtil.formatMoney(report.monthlySurplus));
        printLine("Estimated Goal Achievement: " + report.estimate);
        printLine("");

        logger.info("SummaryReport display completed.");
    }

}
