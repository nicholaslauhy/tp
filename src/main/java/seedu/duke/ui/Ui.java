package seedu.duke.ui;

import seedu.duke.data.SummaryReport;
import seedu.duke.util.InputUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

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

    /**
     * Displays the welcome message and ASCII logo when the application starts.
     *
     * <p>This method is intended to be called once at application startup.</p>
     */
    public void showWelcome(){
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
        printLine("Nice to meet you, " + name + "!");
    }

    public void goodBye(String name){
        printLine("Goodbye " + name + ". Stay disciplined and get that house that you always wanted!");
    }

    public void printLine(String message){
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
    public String readLine(Scanner in, String prompt){
        if (prompt != null && !prompt.isEmpty()){
            printLine(prompt);
        }
        return in.nextLine();
    }

    /**
     * Displays a list of supported commands and their usage.
     *
     * <p>This method prints the currently supported CLI commands
     * and brief descriptions of their purpose.</p>
     */
    public void showHelpMessage() {
        // General Commands
        printLine("'help'    - view all current commands");
        printLine("'summary' - generate your BTO readiness report based on your goals");
        printLine("'bye'     - exit the program");
        printLine("");

        // Daily Transaction Commands
        printLine("'add'      <amount> - add a new expense (e.g., add 5.50)");
        printLine("'list'     - view all current expenses and your total spent");
        printLine("'delete'   <index> - remove a specific expense from your list");
        printLine("");

        // Profile & Goal Management
        printLine("'savings' - add a surplus amount to your existing savings");
        printLine("'clear'   - wipe all current expenses from the list");
        printLine("'reset'   - wipes all profile data and expenses to start fresh.");
    }

    /**
     * Prints a formatted BTO Readiness Report to the console.
     *
     * @param report a {@link SummaryReport} containing the user's precomputed financial snapshot.
     */
    public void showSummaryReport(SummaryReport report) {
            printLine("===== BTO Readiness Report =====");
            printLine("User: " + report.name);
            printLine("Dateline: " + report.deadline);
            printLine("BTO Goal: " + InputUtil.formatMoney(report.btoGoal) + " (your share + fees)");
            printLine("Monthly Salary: " + InputUtil.formatMoney(report.monthlySalary));
            printLine("Current Savings: " + InputUtil.formatMoney(report.currentSavings) + " (" + report.percentage + "% reached)");
            printLine("Distance to Goal: " + InputUtil.formatMoney(report.distance));
            printLine("Monthly Surplus: " + InputUtil.formatMoney(report.monthlySurplus));
            printLine("Estimated Goal Achievement: " + report.estimate);
    }

}
