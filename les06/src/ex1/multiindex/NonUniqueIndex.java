package ex1.multiindex;

import java.util.*;

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
    public Iterator<E> iterator() {
        return new Iterator<>() {
            final Iterator<HashSet<E>> itr = multimap.values().iterator();
            Iterator<E> currentSetItr = nextCurrentSetItr();

            @Override
            public boolean hasNext() {
                return currentSetItr != null && currentSetItr.hasNext();
            }

            @Override
            public E next() {
                if (currentSetItr == null) {
                    throw new NoSuchElementException();
                }
                E next = currentSetItr.next();
                if (!currentSetItr.hasNext()) {
                    currentSetItr = nextCurrentSetItr();
                }
                return next;
            }

            private Iterator<E> nextCurrentSetItr() {
                return itr.hasNext() ? itr.next().iterator() : null;
            }
        };
    }

    @Override
    public Iterator<E> find(K key) {
        return multimap.get(key).iterator();
    }

    @Override
    boolean contains(E element) {
        return multimap.get(keyExtractor.key(element)) != null;
    }

    @Override
    boolean isUnique() {
        return false;
    }
}
