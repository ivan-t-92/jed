package ex1.multiindex;

import java.util.Iterator;

public abstract class Index<K, E> {
    abstract void add(E o);
    abstract void remove(E o);
    abstract boolean contains(E o);
    abstract boolean isUnique();

    public abstract Iterator<E> iterator();
    public abstract Iterator<E> find(K key);
}
