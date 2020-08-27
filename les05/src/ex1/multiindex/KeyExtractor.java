package ex1.multiindex;

@FunctionalInterface
public interface KeyExtractor<K, E> {
    K key(E o);
}
