package ex1.multiindex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public Iterator<E> iterator() {
        return hashMap.values().iterator();
    }

    @Override
    public Iterator<E> find(K key) {
        return new Iterator<>() {
            E next = hashMap.get(key);

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                if (hasNext()) {
                    E result = next;
                    next = null;
                    return result;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @Override
    boolean contains(E o) {
        return hashMap.containsKey(keyExtractor.key(o));
    }

    @Override
    boolean isUnique() {
        return true;
    }
}
