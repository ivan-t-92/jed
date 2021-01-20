package ex1.multiindex;

import java.util.Comparator;
import java.util.TreeMap;

public final class OrderedNonUniqueIndex<K, E> extends NonUniqueIndex<K, E> {

    public OrderedNonUniqueIndex(KeyExtractor<K, E> keyExtractor, Comparator<K> comparator) {
        super(new TreeMap<>(comparator), keyExtractor);
    }
}
