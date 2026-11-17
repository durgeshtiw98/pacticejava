package com.shop.cache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Spec: Thread-safe cache with a positive fixed time-to-live and an injected clock.
 * Keys and values are nonnull. Each put sets an expiry at insertion time plus the TTL,
 * replacing any earlier entry. Entries are absent at or after that expiry instant.
 * Reads do not extend an entry's lifetime. Missing and expired entries return empty.
 */
public final class ExpiringCache<K, V> {
    private final Map<K, Entry<V>> entries = new HashMap<>();
    private final Duration timeToLive;
    private final Clock clock;

    /** Spec: Immutable stored value and its absolute expiration instant. */
    private record Entry<V>(V value, Instant expiresAt) { }

    public ExpiringCache(Duration timeToLive, Clock clock) {
        this.timeToLive = Objects.requireNonNull(timeToLive, "timeToLive");
        this.clock = Objects.requireNonNull(clock, "clock");
        if (timeToLive.isZero() || timeToLive.isNegative()) {
            throw new IllegalArgumentException("Time-to-live must be positive");
        }
    }

    public synchronized void put(K key, V value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        entries.put(key, new Entry<>(value, clock.instant().plus(timeToLive)));
    }

    public synchronized Optional<V> get(K key) {
        Entry<V> entry = entries.get(Objects.requireNonNull(key, "key"));
        if (entry == null) {
            return Optional.empty();
        }
        Instant now = clock.instant();
        if (now.isAfter(entry.expiresAt())) {
            entries.remove(key);
            return Optional.empty();
        }
        entries.put(key, new Entry<>(entry.value(), now.plus(timeToLive)));
        return Optional.of(entry.value());
    }
}
