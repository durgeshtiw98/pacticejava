package com.shop.order;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Spec: A return is eligible only for a SHIPPED order and a delivery timestamp that
 * is not in the future. The return window begins at delivery and ends exactly the
 * configured number of 24-hour days later, including both endpoints. The window
 * must be positive. All time decisions use the injected clock.
 */
public final class ReturnPolicy {
    private final int windowDays;
    private final Clock clock;

    public ReturnPolicy(int windowDays, Clock clock) {
        if (windowDays <= 0) {
            throw new IllegalArgumentException("Return window must be positive");
        }
        this.windowDays = windowDays;
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public boolean canReturn(Order.Status status, Instant deliveredAt) {
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(deliveredAt, "deliveredAt");
        if (status == Order.Status.CANCELLED) {
            return false;
        }
        Instant now = clock.instant();
        if (now.isBefore(deliveredAt)) {
            return false;
        }
        return Duration.between(deliveredAt, now).toDays() <= windowDays;
    }
}
