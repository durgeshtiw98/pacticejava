package com.shop.order;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReturnPolicyTest {
    private static final Instant DELIVERED = Instant.parse("2026-03-01T12:00:00Z");

    @Test
    void returnAfterExactDeadlineIsRejected() {
        ReturnPolicy policy = at(DELIVERED.plusSeconds(30L * 86400 + 1));
        assertFalse(policy.canReturn(Order.Status.SHIPPED, DELIVERED));
    }

    @Test
    void returnAtExactDeadlineIsAccepted() {
        assertTrue(at(DELIVERED.plusSeconds(30L * 86400))
                .canReturn(Order.Status.SHIPPED, DELIVERED));
    }

    @Test
    void returnWithinWindowIsAccepted() {
        assertTrue(at(DELIVERED.plusSeconds(86400)).canReturn(Order.Status.SHIPPED, DELIVERED));
    }

    @Test
    void futureDeliveryIsRejected() {
        assertFalse(at(DELIVERED.minusSeconds(1)).canReturn(Order.Status.SHIPPED, DELIVERED));
    }

    @Test
    void cancelledOrderIsIneligible() {
        assertFalse(at(DELIVERED).canReturn(Order.Status.CANCELLED, DELIVERED));
    }

    private static ReturnPolicy at(Instant now) {
        return new ReturnPolicy(30, Clock.fixed(now, ZoneOffset.UTC));
    }
}
