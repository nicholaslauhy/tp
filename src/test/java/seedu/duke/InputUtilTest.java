package seedu.duke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.duke.util.InputUtil;
import seedu.duke.ui.Ui;

import java.time.LocalDate;
import java.util.Scanner;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InputUtilTest {
    private static class CapturingUi extends Ui {
        private final List<String> lines = new ArrayList<>();

        @Override
        public void printLine(String message) {
            lines.add(message);
        }

        public List<String> getLines() {
            return lines;
        }
    }

    @Test
    public void formatMoney_wholeNumber_correctCurrencyFormat() {
        String formatted = InputUtil.formatMoney(new BigDecimal("12250"));
        assertEquals("$12,250.00", formatted);
    }

    @Test
    public void formatMoney_decimal_correctCurrencyFormat() {
        String formatted = InputUtil.formatMoney(new BigDecimal("10250.5"));
        assertEquals("$10,250.50", formatted);
    }

    @Test
    public void formatMoney_zero_correctCurrencyFormat() {
        String formatted = InputUtil.formatMoney(BigDecimal.ZERO);
        assertEquals("$0.00", formatted);
    }

    @Test
    public void readMoney_validWholeNumber_returnsAmount() {
        Ui ui = new Ui();
        Scanner in = new Scanner("390000\n");

        BigDecimal result = InputUtil.readMoney(ui, in, "Enter amount:");

        assertEquals(0, new BigDecimal("390000").compareTo(result));
    }

    @Test
    public void readMoney_validDecimal_returnsAmount() {
        Ui ui = new Ui();
        Scanner in = new Scanner("10250.50\n");

        BigDecimal result = InputUtil.readMoney(ui, in, "Enter amount:");

        assertEquals(0, new BigDecimal("10250.50").compareTo(result));
    }

    @Test
    public void readMoney_invalidThenValid_returnsValidAmount() {
        Ui ui = new Ui();
        Scanner in = new Scanner("abc\n10250.50\n");

        BigDecimal result = InputUtil.readMoney(ui, in, "Enter amount:");

        assertEquals(0, new BigDecimal("10250.50").compareTo(result));
    }

    @Test
    public void readMoney_tooManyDecimalPlacesThenValid_returnsValidAmount() {
        Ui ui = new Ui();
        Scanner in = new Scanner("10.999\n100.25\n");

        BigDecimal result = InputUtil.readMoney(ui, in, "Enter amount:");

        assertEquals(0, new BigDecimal("100.25").compareTo(result));
    }

    @Test
    public void readMoney_negativeThenValid_showsNegativeMessageAndReturnsValidAmount() {
        CapturingUi ui = new CapturingUi();
        Scanner in = new Scanner("-100\n100.00\n");

        BigDecimal result = InputUtil.readMoney(ui, in, "Enter amount:");

        assertEquals(0, new BigDecimal("100.00").compareTo(result));
        assertTrue(ui.getLines().contains("Negative amounts are not accepted. Please enter 0 or more."));
    }

    @Test
    public void readMoney_invalidFormatThenValid_showsFormatMessageAndReturnsValidAmount() {
        CapturingUi ui = new CapturingUi();
        Scanner in = new Scanner("abc\n100.00\n");

        BigDecimal result = InputUtil.readMoney(ui, in, "Enter amount:");

        assertEquals(0, new BigDecimal("100.00").compareTo(result));
        assertTrue(ui.getLines().contains(
                "Bruh I need a valid amount like 10250 or 10250.50 (numbers only, max 2 dp). Try again."));
    }

    @Test
    public void readRatio_validHalf_returnsHalf() {
        Ui ui = new Ui();
        Scanner in = new Scanner("0.5\n");

        BigDecimal result = InputUtil.readRatio(ui, in, "Enter ratio:");

        assertEquals(0, new BigDecimal("0.5").compareTo(result));
    }

    @Test
    public void readRatio_minimumBoundary_returnsPointZeroOne() {
        Ui ui = new Ui();
        Scanner in = new Scanner("0.01\n");

        BigDecimal result = InputUtil.readRatio(ui, in, "Enter ratio:");

        assertEquals(0, new BigDecimal("0.01").compareTo(result));
    }

    @Test
    public void readRatio_zeroRejected_thenMinAccepted() {
        CapturingUi ui = new CapturingUi();
        Scanner in = new Scanner("0\n0.01\n");

        BigDecimal result = InputUtil.readRatio(ui, in, "Enter ratio:");

        assertEquals(0, new BigDecimal("0.01").compareTo(result));
        assertTrue(ui.getLines().contains("Brother...Ratio must be between 0.01 and 1. Try again!"));
    }

    @Test
    public void readRatio_one_returnsOne() {
        Ui ui = new Ui();
        Scanner in = new Scanner("1\n");

        BigDecimal result = InputUtil.readRatio(ui, in, "Enter ratio:");

        assertEquals(0, BigDecimal.ONE.compareTo(result));
    }

    @Test
    public void readRatio_invalidThenValid_returnsValidRatio() {
        Ui ui = new Ui();
        Scanner in = new Scanner("1.5\n0.75\n");

        BigDecimal result = InputUtil.readRatio(ui, in, "Enter ratio:");

        assertEquals(0, new BigDecimal("0.75").compareTo(result));
    }

    @Test
    public void readRatio_tooManyDecimalPlacesThenValid_returnsValidRatio() {
        CapturingUi ui = new CapturingUi();
        Scanner in = new Scanner("0.555\n0.55\n");

        BigDecimal result = InputUtil.readRatio(ui, in, "Enter ratio:");

        assertEquals(0, new BigDecimal("0.55").compareTo(result));
        assertTrue(ui.getLines().contains("Ratio can have at most 2 decimal places (e.g., 0.86). Try again."));
    }

    @Test
    public void readFutureDate_validFutureDate_returnsDate() {
        Ui ui = new Ui();
        LocalDate future = LocalDate.now().plusDays(1);
        Scanner in = new Scanner(future + "\n");

        LocalDate result = InputUtil.readFutureDate(ui, in, "Enter date:");

        assertEquals(future, result);
    }

    @Test
    public void readFutureDate_todayThenFutureDate_returnsFutureDate() {
        Ui ui = new Ui();
        LocalDate today = LocalDate.now();
        LocalDate future = LocalDate.now().plusDays(2);
        Scanner in = new Scanner(today + "\n" + future + "\n");

        LocalDate result = InputUtil.readFutureDate(ui, in, "Enter date:");

        assertEquals(future, result);
    }

    @Test
    public void readFutureDate_invalidFormatThenValid_returnsFutureDate() {
        Ui ui = new Ui();
        LocalDate future = LocalDate.now().plusDays(3);
        Scanner in = new Scanner("03-09-2026\n" + future + "\n");

        LocalDate result = InputUtil.readFutureDate(ui, in, "Enter date:");

        assertEquals(future, result);
    }
}
