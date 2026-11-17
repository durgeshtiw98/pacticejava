package com.shop.pricing;

import com.shop.money.Money;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import static org.junit.jupiter.api.Assertions.*;

class DiscountServiceTest {
    private final DiscountService discounts = new DiscountService(
            Clock.fixed(Instant.parse("2026-01-15T12:00:00Z"), ZoneOffset.UTC));

    @Test
    void appliesPercentageDiscount() {
        Money result = discounts.applyPercentage(Money.of("80.00", "USD"), 25);
        assertEquals(0, new BigDecimal("60.00").compareTo(result.amount()));
    }

    @Test
    void minimumSpendIsInclusive() {
        assertTrue(discounts.qualifies(Money.of("50.00", "USD"), Money.of("50.00", "USD")));
    }

    @Test
    void rejectsCrossCurrencyMinimum() {
        assertThrows(IllegalArgumentException.class,
                () -> discounts.qualifies(Money.of("100", "USD"), Money.of("10", "EUR")));
    }

    @Test
    void ignoresUnmatchedCouponCode() {
        Coupon coupon = new Coupon("SAVE25", 25,
                Instant.parse("2026-01-01T00:00:00Z"), Instant.parse("2026-02-01T00:00:00Z"));
        Money subtotal = Money.of("80.00", "USD");
        assertSame(subtotal, discounts.quote(subtotal, Money.of("10", "USD"), coupon, "OTHER"));
    }

    @Test
    void rejectsInvalidDiscountPercentage() {
        assertThrows(IllegalArgumentException.class,
                () -> discounts.applyPercentage(Money.of("80", "USD"), 101));
    }
}
