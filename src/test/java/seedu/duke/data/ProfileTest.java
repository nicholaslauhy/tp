package seedu.duke.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProfileTest {

    private Profile profile;

    @BeforeEach
    public void setUp() {
        profile = new Profile();
    }

    @Test
    public void monthlyAllowance_setAndGet_correctValueStored() {
        BigDecimal testAllowance = new BigDecimal("5000.50");
        profile.setMonthlyAllowance(testAllowance);
        assertEquals(testAllowance, profile.getMonthlyAllowance());
    }

    @Test
    public void currentSavings_setAndGet_correctValueStored() {
        BigDecimal testSavings = new BigDecimal("15000.75");
        profile.setCurrentSavings(testSavings);
        assertEquals(testSavings, profile.getCurrentSavings());
    }

    @Test
    public void contributionRatio_setAndGet_correctValueStored() {
        BigDecimal testRatio = new BigDecimal("0.60");
        profile.setContributionRatio(testRatio);
        assertEquals(testRatio, profile.getContributionRatio());
    }

    @Test
    public void contributionRatio_invalidValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setContributionRatio(new BigDecimal("1.1")));
    }

    @Test
    public void monthlyAllowance_negativeValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setMonthlyAllowance(new BigDecimal("-100")));
    }

    @Test
    public void setHousePrice_validValue_updatesAttribute() {
        BigDecimal price = new BigDecimal("500000");
        profile.setHousePrice(price);
        assertEquals(price, profile.getHousePrice());
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

    @Test
    public void setHousePrice_negativeValue_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> profile.setHousePrice(new BigDecimal("-1")));
    }
}
