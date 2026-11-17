package com.shop.cache;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LruCacheTest {
    @Test
    void accessKeepsEntryInCache() {
        LruCache<String, String> cache = new LruCache<>(2);
        cache.put("A", "alpha");
        cache.put("B", "beta");
        assertEquals("alpha", cache.get("A").orElseThrow());
        cache.put("C", "gamma");
        assertTrue(cache.get("A").isPresent());
        assertTrue(cache.get("B").isEmpty());
        assertEquals(2, cache.size());
    }

    @Test
    void returnsEmptyForMissingKey() {
        assertTrue(new LruCache<String, String>(2).get("missing").isEmpty());
    }

    @Test
    void replacementDoesNotIncreaseSize() {
        LruCache<String, String> cache = new LruCache<>(2);
        cache.put("A", "alpha");
        cache.put("A", "updated");
        assertEquals(1, cache.size());
        assertEquals("updated", cache.get("A").orElseThrow());
    }

    @Test
    void rejectsNonpositiveCapacityAndNullEntries() {
        assertThrows(IllegalArgumentException.class, () -> new LruCache<>(0));
        LruCache<String, String> cache = new LruCache<>(2);
        assertThrows(NullPointerException.class, () -> cache.put(null, "value"));
        assertThrows(NullPointerException.class, () -> cache.put("key", null));
    }
}
