package ex1.multiindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public final class HashedUniqueIndex<K, E> extends Index<K, E> {
    private final HashMap<K, E> hashMap = new HashMap<>();
    private final KeyExtractor<K, E> keyExtractor;

    public HashedUniqueIndex(KeyExtractor<K, E> keyExtractor) {
        this.keyExtractor = keyExtractor;
    }

    @Override
    void add(E o) {
        hashMap.putIfAbsent(keyExtractor.key(o), o);
    }

    @Override
    void remove(E o) {
        hashMap.remove(keyExtractor.key(o), o);
    }

    @Override
    public List<E> find(K key) {
        List<E> result = new ArrayList<>();
        E v = hashMap.get(key);
        if (v != null) {
            result.add(v);
        }
        return result;
    }

    @Override
    boolean contains(E o) {
        return hashMap.containsKey(keyExtractor.key(o));
    }

    @Override
    boolean isUnique() {
        return true;
    }

    @Override
    public Stream<E> stream() {
        return hashMap.values().stream();
    }
}
