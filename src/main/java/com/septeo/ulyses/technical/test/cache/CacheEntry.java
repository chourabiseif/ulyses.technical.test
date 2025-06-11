package com.septeo.ulyses.technical.test.cache;

import java.time.Instant;

public class CacheEntry<T> {
    private final T value;
    private final Instant expiration;

    public CacheEntry(T value, long ttlMillis) {
        this.value = value;
        this.expiration = Instant.now().plusMillis(ttlMillis);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiration);
    }

    public T getValue() {
        return value;
    }
}
