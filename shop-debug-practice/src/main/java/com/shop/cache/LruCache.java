package com.shop.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Spec: Thread-safe bounded least-recently-used cache with positive capacity and nonnull
 * keys/values. Successful get and put refresh recency. Overflow evicts the least recently
 * accessed entry; replacing an entry does not increase size. Missing get returns empty.
 */
public final class LruCache<K, V> {
    private final int capacity;
    private final Map<K, V> entries = new LinkedHashMap<>();

    public LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public synchronized Optional<V> get(K key) {
        return Optional.ofNullable(entries.get(Objects.requireNonNull(key, "key")));
    }

    public synchronized void put(K key, V value) {
        entries.put(Objects.requireNonNull(key, "key"), Objects.requireNonNull(value, "value"));
        if (entries.size() > capacity) {
            K oldest = entries.keySet().iterator().next();
            entries.remove(oldest);
        }
    }

    public synchronized int size() {
        return entries.size();
    }
}
