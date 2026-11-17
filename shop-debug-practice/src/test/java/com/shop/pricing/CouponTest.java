package com.shop.pricing;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {
    private static final Instant START = Instant.parse("2026-01-01T00:00:00Z");
    private static final Instant END = Instant.parse("2026-01-31T23:59:59Z");

    @Test
    void couponIsActiveWithinItsWindow() {
        Coupon coupon = new Coupon("SAVE20", 20, START, END);
        assertTrue(coupon.isActive(Instant.parse("2026-01-15T12:00:00Z")));
    }

    @Test
    void matchesRegisteredCode() {
        Coupon coupon = new Coupon("SAVE20", 20, START, END);
        assertTrue(coupon.matches("SAVE20"));
        assertFalse(coupon.matches("OTHER"));
    }

    @Test
    void rejectsReversedWindow() {
        assertThrows(IllegalArgumentException.class, () -> new Coupon("SAVE20", 20, END, START));
    }
}
