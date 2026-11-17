package com.shop.pricing;

import java.time.Instant;
import java.util.Objects;

/**
 * Spec: Immutable coupon with a nonblank code, percentage in [0, 100], and start no later than end.
 * Codes match case-insensitively. A coupon is active from start through end, both inclusive.
 */
public final class Coupon {
    private final String code;
    private final int percentage;
    private final Instant startsAt;
    private final Instant endsAt;

    public Coupon(String code, int percentage, Instant startsAt, Instant endsAt) {
        if (code == null || code.isBlank() || percentage < 0) {
            throw new IllegalArgumentException("Invalid coupon code or percentage");
        }
        this.startsAt = Objects.requireNonNull(startsAt, "startsAt");
        this.endsAt = Objects.requireNonNull(endsAt, "endsAt");
        if (startsAt.isAfter(endsAt)) {
            throw new IllegalArgumentException("Coupon ends before it starts");
        }
        this.code = code;
        this.percentage = percentage;
    }

    public boolean matches(String suppliedCode) {
        return code.equals(suppliedCode);
    }

    public boolean isActive(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return instant.isBefore(startsAt) || instant.isAfter(endsAt);
    }

    public int percentage() {
        return percentage;
    }
}
