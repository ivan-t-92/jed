package ex1.multiindex;

import java.util.List;
import java.util.stream.Stream;

public abstract class Index<K, E> {
    abstract void add(E o);
    abstract void remove(E o);
    abstract boolean contains(E o);
    abstract boolean isUnique();

    public abstract Stream<E> stream();
    public abstract List<E> find(K key);
}
