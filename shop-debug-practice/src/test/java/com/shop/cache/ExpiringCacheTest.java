package com.shop.cache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpiringCacheTest {
    private static final Instant NOW = Instant.parse("2026-03-01T12:00:00Z");

    @Test
    void entryExpiresAtItsDeadline() {
        TestClock clock = new TestClock();
        ExpiringCache<String, String> cache = new ExpiringCache<>(Duration.ofMinutes(1), clock);
        cache.put("sku", "Keyboard");
        clock.advance(Duration.ofSeconds(60));
        assertTrue(cache.get("sku").isEmpty());
    }

    @Test
    void entryIsAvailableBeforeDeadline() {
        ExpiringCache<String, String> cache = new ExpiringCache<>(
                Duration.ofMinutes(1), Clock.fixed(NOW, ZoneOffset.UTC));
        cache.put("sku", "Keyboard");
        assertEquals("Keyboard", cache.get("sku").orElseThrow());
    }

    @Test
    void entryIsAbsentAfterDeadline() {
        TestClock clock = new TestClock();
        ExpiringCache<String, String> cache = new ExpiringCache<>(Duration.ofMinutes(1), clock);
        cache.put("sku", "Keyboard");
        clock.advance(Duration.ofSeconds(61));
        assertTrue(cache.get("sku").isEmpty());
    }

    @Test
    void missingKeyReturnsEmpty() {
        ExpiringCache<String, String> cache = new ExpiringCache<>(
                Duration.ofMinutes(1), Clock.fixed(NOW, ZoneOffset.UTC));
        assertTrue(cache.get("missing").isEmpty());
    }

    @Test
    void rejectsInvalidConfigurationAndNullValues() {
        Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
        assertThrows(IllegalArgumentException.class, () -> new ExpiringCache<>(Duration.ZERO, clock));
        assertThrows(IllegalArgumentException.class,
                () -> new ExpiringCache<>(Duration.ofSeconds(-1), clock));
        ExpiringCache<String, String> cache = new ExpiringCache<>(Duration.ofSeconds(1), clock);
        assertThrows(NullPointerException.class, () -> cache.put("sku", null));
    }

    private static final class TestClock extends Clock {
        private Instant now = NOW;

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return Clock.fixed(now, zone);
        }

        @Override
        public Instant instant() {
            return now;
        }
    }
}
