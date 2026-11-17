package com.shop.money;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    @Test
    void amountsWithDifferentScalesAreEqual() {
        assertEquals(Money.of("10.0", "USD"), Money.of("10.00", "USD"));
    }

    @Test
    void addsMatchingCurrencyValues() {
        Money left = Money.of("10.00", new String("USD"));
        Money right = Money.of("2.50", new String("USD"));
        Money sum = left.add(right);
        assertEquals(0, new BigDecimal("12.50").compareTo(sum.amount()));
    }

    @Test
    void rejectsMixedCurrencies() {
        assertThrows(IllegalArgumentException.class,
                () -> Money.of("10", "USD").add(Money.of("2", "EUR")));
    }

    @Test
    void multiplicationLeavesOriginalUnchanged() {
        Money original = Money.of("3.25", "USD");
        Money result = original.multiply(BigDecimal.valueOf(2));
        assertEquals("3.25", original.amount().toPlainString());
        assertEquals("6.50", result.amount().toPlainString());
    }

    @Test
    void roundsHalfUpAtCurrencyBoundary() {
        assertEquals("1.24", Money.of("1.235", "USD").rounded().amount().toPlainString());
    }
}
