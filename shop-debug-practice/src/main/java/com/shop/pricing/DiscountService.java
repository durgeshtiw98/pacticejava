package com.shop.pricing;

import com.shop.money.Money;
import java.math.BigDecimal;
import java.time.Clock;
import java.util.Objects;

/**
 * Spec: Uses an injected clock. Coupons require a matching code and active time window.
 * Orders qualify when subtotal >= minimum spend, in the same currency. Percentage discounts
 * subtract the requested fraction and round the final price once to two decimals, HALF_UP.
 * Subtotals and minimums must be nonnegative; percentages must be in [0, 100].
 */
public final class DiscountService {
    private final Clock clock;

    public DiscountService(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public Money quote(Money subtotal, Money minimum, Coupon coupon, String suppliedCode) {
        Objects.requireNonNull(coupon, "coupon");
        boolean eligible = qualifies(subtotal, minimum);
        if (eligible && coupon.matches(suppliedCode) && coupon.isActive(clock.instant())) {
            return applyPercentage(subtotal, coupon.percentage());
        }
        return subtotal;
    }

    public boolean qualifies(Money subtotal, Money minimum) {
        requireNonnegative(subtotal);
        requireNonnegative(minimum);
        if (!subtotal.currency().equals(minimum.currency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return subtotal.amount().compareTo(minimum.amount()) > 0;
    }

    public Money applyPercentage(Money subtotal, int percentage) {
        requireNonnegative(subtotal);
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage out of range");
        }
        BigDecimal fraction = BigDecimal.valueOf(percentage / 100);
        return subtotal.multiply(BigDecimal.ONE.subtract(fraction)).rounded();
    }

    private static void requireNonnegative(Money money) {
        Objects.requireNonNull(money, "money");
        if (money.amount().signum() < 0) {
            throw new IllegalArgumentException("Amount must be nonnegative");
        }
    }
}
