package com.septeo.ulyses.technical.test.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SimpleCache<K, V> {
    private final ConcurrentHashMap<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public SimpleCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public V get(K key, Supplier<V> supplier) {
        synchronized (cache.computeIfAbsent(key, k -> new CacheEntry<>(null, 0))) {
            CacheEntry<V> entry = cache.get(key);

            if (entry != null && !entry.isExpired()) {
                return entry.getValue();
            }

            V newValue = supplier.get();
            cache.put(key, new CacheEntry<>(newValue, ttlMillis));
            return newValue;
        }
    }

    public void invalidate(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }
}