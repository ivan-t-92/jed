package ex1.multiindex;

import java.util.*;
import java.util.stream.Stream;

class NonUniqueIndex<K, E> extends Index<K, E> {
    private final Map<K, HashSet<E>> multimap;
    private final KeyExtractor<K, E> keyExtractor;

    protected NonUniqueIndex(Map<K, HashSet<E>> multimap, KeyExtractor<K, E> keyExtractor) {
        this.multimap = multimap;
        this.keyExtractor = keyExtractor;
    }

    @Override
    void add(E o) {
        multimap.computeIfAbsent(keyExtractor.key(o), k -> new HashSet<>()).add(o);
    }

    @Override
    void remove(E o) {
        K key = keyExtractor.key(o);
        HashSet<E> l = multimap.get(key);
        if (l != null) {
            l.remove(o);
            if (l.isEmpty()) {
                multimap.remove(key);
            }
        }
    }

    @Override
    public List<E> find(K key) {
        return new ArrayList<>(multimap.get(key));
    }

    @Override
    boolean contains(E element) {
        return multimap.get(keyExtractor.key(element)) != null;
    }

    @Override
    void accept(IndexVisitor<E> visitor) {
        visitor.visitNonUnique(this);
    }

    @Override
    public Stream<E> stream() {
        return multimap.values().stream().flatMap(Collection::stream);
    }
}
