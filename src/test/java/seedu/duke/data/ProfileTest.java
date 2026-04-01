package seedu.duke.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProfileTest {

    private Profile profile;

    @BeforeEach
    public void setUp() {
        profile = new Profile();
    }

    /**
     * Unit tests for Allowance.
     */
    @Test
    public void monthlyAllowance_setAndGet_correctValueStored() {
        BigDecimal testAllowance = new BigDecimal("5000.50");
        profile.setMonthlyAllowance(testAllowance);
        assertEquals(testAllowance, profile.getMonthlyAllowance());
    }

    @Test
    public void setMonthlyAllowance_null_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setMonthlyAllowance(null));
    }

    @Test
    public void setMonthlyAllowance_zero_isAccepted() {
        profile.setMonthlyAllowance(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, profile.getMonthlyAllowance());
    }

    @Test
    public void setMonthlyAllowance_largeValue_correctlyStored() {
        BigDecimal large = new BigDecimal("99999.99");
        profile.setMonthlyAllowance(large);
        assertEquals(large, profile.getMonthlyAllowance());
    }

    @Test
    public void monthlyAllowance_negativeValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setMonthlyAllowance(new BigDecimal("-100")));
    }

    /**
     * Unit tests for Savings.
     */

    @Test
    public void currentSavings_setAndGet_correctValueStored() {
        BigDecimal testSavings = new BigDecimal("15000.75");
        profile.setCurrentSavings(testSavings);
        assertEquals(testSavings, profile.getCurrentSavings());
    }

    @Test
    public void setCurrentSavings_null_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setCurrentSavings(null));
    }

    @Test
    public void setCurrentSavings_zero_isAccepted() {
        profile.setCurrentSavings(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, profile.getCurrentSavings());
    }

    @Test
    public void setCurrentSavings_largeValue_correctlyStored() {
        BigDecimal large = new BigDecimal("999999999.99");
        profile.setCurrentSavings(large);
        assertEquals(large, profile.getCurrentSavings());
    }

    @Test
    public void setCurrentSavings_negativeValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setCurrentSavings(new BigDecimal("-100")));
    }

    /**
     * Unit tests for contributionRatio.
     */

    @Test
    public void contributionRatio_setAndGet_correctValueStored() {
        BigDecimal testRatio = new BigDecimal("0.60");
        profile.setContributionRatio(testRatio);
        assertEquals(testRatio, profile.getContributionRatio());
    }

    @Test
    public void setContributionRatio_null_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setContributionRatio(null));
    }

    @Test
    public void setContributionRatio_negative_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setContributionRatio(new BigDecimal("-0.1")));
    }

    @Test
    public void setContributionRatio_zero_isAccepted() {
        profile.setContributionRatio(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, profile.getContributionRatio());
    }

    @Test
    public void setContributionRatio_one_isAccepted() {
        profile.setContributionRatio(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, profile.getContributionRatio());
    }

    @Test
    public void setContributionRatio_withoutHousePrice_doesNotRecalculateBtoGoal() {
        // housePrice is null on a fresh profile, so setContributionRatio must not touch btoGoal
        profile.setBtoGoal(new BigDecimal("50000"));
        profile.setContributionRatio(new BigDecimal("0.7"));
        assertEquals(new BigDecimal("50000"), profile.getBtoGoal());
    }

    @Test
    public void contributionRatio_invalidValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setContributionRatio(new BigDecimal("1.1")));
    }

    @Test
    public void setContributionRatio_afterSettingHousePrice_recalculatesBtoGoal() {
        BigDecimal housePrice = new BigDecimal("400000");
        profile.setHousePrice(housePrice);
        profile.setContributionRatio(new BigDecimal("0.5"));
        BigDecimal expectedGoal = new BigDecimal("10500.00");
        assertEquals(expectedGoal, profile.getBtoGoal());
        profile.setContributionRatio(new BigDecimal("1.0"));
        assertEquals(new BigDecimal("21000.00"), profile.getBtoGoal());
    }

    /**
     * Unit tests for housePrice.
     */

    @Test
    public void setHousePrice_validValue_updatesAttribute() {
        BigDecimal price = new BigDecimal("500000");
        profile.setHousePrice(price);
        assertEquals(price, profile.getHousePrice());
    }

    @Test
    public void setHousePrice_null_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setHousePrice(null));
    }

    @Test
    public void setHousePrice_zero_isAccepted() {
        profile.setHousePrice(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, profile.getHousePrice());
    }

    @Test
    public void setHousePrice_negativeValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setHousePrice(new BigDecimal("-1")));
    }

    @Test
    public void setHousePrice_withDefaultRatio_recalculatesBtoGoal() {
        profile.setHousePrice(new BigDecimal("400000"));
        assertEquals(new BigDecimal("10500.00"), profile.getBtoGoal());
    }

    @Test
    public void setHousePrice_afterChanging_recalculatesBtoGoal() {
        profile.setContributionRatio(new BigDecimal("0.5"));
        profile.setHousePrice(new BigDecimal("400000"));
        profile.setHousePrice(new BigDecimal("500000"));
        assertEquals(new BigDecimal("13125.00"), profile.getBtoGoal());
    }

    /**
     * Unit tests for btoGoal.
     */

    @Test
    public void setBtoGoal_null_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setBtoGoal(null));
    }

    @Test
    public void setBtoGoal_negativeValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setBtoGoal(new BigDecimal("-1")));
    }

    @Test
    public void setBtoGoal_validValue_correctlyStored() {
        BigDecimal goal = new BigDecimal("25000.00");
        profile.setBtoGoal(goal);
        assertEquals(goal, profile.getBtoGoal());
    }

    @Test
    public void getBtoGoal_freshProfile_isZero() {
        assertEquals(BigDecimal.ZERO, profile.getBtoGoal());
    }

    /**
     * Unit tests for name.
     */

    @Test
    public void setName_null_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setName(null));
    }

    @Test
    public void setName_validName_correctlyStored() {
        profile.setName("Adam");
        assertEquals("Adam", profile.getName());
    }

    @Test
    public void getName_freshProfile_returnsDefaultFriend() {
        assertEquals("friend", profile.getName());
    }

    /**
     * Unit tests for deadline.
     */

    @Test
    public void setDeadline_null_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setDeadline(null));
    }

    @Test
    public void setDeadline_pastDate_isAccepted() {
        LocalDate past = LocalDate.of(2020, 1, 1);
        profile.setDeadline(past);
        assertEquals(past, profile.getDeadline());
    }

    @Test
    public void setDeadline_today_isAccepted() {
        LocalDate today = LocalDate.now();
        profile.setDeadline(today);
        assertEquals(today, profile.getDeadline());
    }

    @Test
    public void setDeadline_futureDate_correctlyStored() {
        LocalDate future = LocalDate.now().plusYears(2);
        profile.setDeadline(future);
        assertEquals(future, profile.getDeadline());
    }

    /**
     * Unit tests for currentMonth and advanceMonth.
     */

    @Test
    public void getCurrentMonth_freshProfile_isOne() {
        assertEquals(1, profile.getCurrentMonth());
    }

    @Test
    public void advanceMonth_once_isTwo() {
        profile.advanceMonth();
        assertEquals(2, profile.getCurrentMonth());
    }

    @Test
    public void advanceMonth_multipleTimes_correctCount() {
        profile.advanceMonth();
        profile.advanceMonth();
        profile.advanceMonth();
        assertEquals(4, profile.getCurrentMonth());
    }

    /**
     * Unit tests for reset.
     */

    @Test
    public void reset_afterModifications_resetsAllFieldsToDefaults() {
        profile.setName("Adam");
        profile.setCurrentSavings(new BigDecimal("10000"));
        profile.setMonthlyAllowance(new BigDecimal("2000"));
        profile.setBtoGoal(new BigDecimal("50000"));
        profile.setContributionRatio(new BigDecimal("0.7"));
        profile.setHousePrice(new BigDecimal("400000"));
        profile.setDeadline(LocalDate.now().plusYears(2));
        profile.advanceMonth();

        profile.reset();

        assertEquals("friend", profile.getName());
        assertEquals(BigDecimal.ZERO, profile.getCurrentSavings());
        assertEquals(BigDecimal.ZERO, profile.getMonthlyAllowance());
        assertEquals(BigDecimal.ZERO, profile.getBtoGoal());
        assertEquals(new BigDecimal("0.5"), profile.getContributionRatio());
        assertEquals(1, profile.getCurrentMonth());
        assertNull(profile.getHousePrice());
        assertEquals(LocalDate.now(), profile.getDeadline());
    }
}
