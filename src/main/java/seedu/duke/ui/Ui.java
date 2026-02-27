package seedu.duke.ui;

import java.util.Scanner;

public class Ui {
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

    public String readLine(Scanner in, String prompt){
        if (prompt != null && !prompt.isEmpty()){
            printLine(prompt);
        }
        return in.nextLine();
    }

    public void showHelpMessage() {
        printLine("'bye' - exit the program");
        printLine("'help' - view all current commands");
    }
}
