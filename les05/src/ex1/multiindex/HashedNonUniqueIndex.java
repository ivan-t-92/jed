package ex1.multiindex;

import java.util.HashMap;

public final class HashedNonUniqueIndex<K, E> extends NonUniqueIndex<K, E> {

    public HashedNonUniqueIndex(KeyExtractor<K, E> keyExtractor) {
        super(new HashMap<>(), keyExtractor);
    }
}
