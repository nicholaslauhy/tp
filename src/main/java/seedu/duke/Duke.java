package seedu.duke;

import seedu.duke.ui.Ui;
/**
 * Duke is running Fintrackpro, a CLI program that helps users
 * estimate savings required for their share of a BTO downpayment
 */
public class Duke {

    /**
     * Runs the Duke program which runs FinTrackPro
     */
    public static void main(String[] args) {
        // assert false : "Assertions are ENABLED!";
        Ui ui = new Ui();
        FinTrackPro app = new FinTrackPro(ui);
        app.run();
        assert false : "dummy assertion set to fail";
    }
}
