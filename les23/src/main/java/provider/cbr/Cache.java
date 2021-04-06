package provider.cbr;

import java.util.*;

class Cache<K, V> {
    private final int cacheSize;
    private final Map<K, V> cache = new HashMap<>();
    private final Queue<K> cacheHistory = new LinkedList<>();

    Cache(int cacheSize) {
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("illegal cache size: " + cacheSize);
        }
        this.cacheSize = cacheSize;
    }

    void put(K key, V value) {
        if (value == null) {
            throw new NullPointerException("value can't be null");
        }
        if (cache.put(key, value) == null) {
            cacheHistory.offer(key);
            if (cache.size() > cacheSize) {
                cache.remove(cacheHistory.poll());
            }
        } else {
            cacheHistory.remove(key);
            cacheHistory.offer(key);
        }
    }

    Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    void clear() {
        cache.clear();
        cacheHistory.clear();
    }
}
