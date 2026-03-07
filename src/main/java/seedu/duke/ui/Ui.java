package seedu.duke.ui;

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
        printLine("'bye' - exit the program");
        printLine("'help' - view all current commands");
        printLine("'add' <value(to 2dp)> - add expense for the month");
        printLine("'category' <index in list> <category> - add categories to expenses.");
        printLine("AVAILABLE CATEGORIES: FOOD, TRANSPORT, GROCERIES, OTHER, SUBSCRIPTION");
        printLine("'delete' <index in list> - delete the expense in the specified index");
        printLine("'salary' - view and update your monthly salary");
        printLine("'savings' - view and update your total current savings");
        printLine("'ratio' - view and update your individual BTO contribution share");
        printLine("'list' - view all current expenses");
        printLine("'clear' - wipe all current expenses from the list");
        printLine("'summary' - generate your BTO readiness report based on your goals");
        printLine("'reset' - Wipes all profile data and expenses to start fresh.");
    }
}
