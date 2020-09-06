package ex1.multiindex;

import java.util.ArrayList;

public class MultiIndexCollection<E> {
    private final ArrayList<Index<?, E>> uniqueIndices = new ArrayList<>();
    private final ArrayList<Index<?, E>> nonUniqueIndices = new ArrayList<>();

    public MultiIndexCollection(Index<?, E>... indices) {
        for (Index<?, E> index: indices) {
            if (index.isUnique()) {
                uniqueIndices.add(index);
            } else {
                nonUniqueIndices.add(index);
            }
        }
    }

    public boolean add(E o) {
        for (Index<?, E> index: uniqueIndices) {
            if (index.contains(o)) {
                return false;
            }
        }
        for (Index<?, E> index: uniqueIndices) {
            index.add(o);
        }
        for (Index<?, E> index: nonUniqueIndices) {
            index.add(o);
        }
        return true;
    }

    public void remove(E o) {
        for (Index<?, E> index: uniqueIndices) {
            index.remove(o);
        }
        for (Index<?, E> index: nonUniqueIndices) {
            index.remove(o);
        }
    }
}
