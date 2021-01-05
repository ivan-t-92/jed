package ex1.multiindex;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiIndexCollection<E> {
    private final ArrayList<Index<?, E>> uniqueIndices = new ArrayList<>();
    private final ArrayList<Index<?, E>> nonUniqueIndices = new ArrayList<>();

    public MultiIndexCollection(Index<?, E>... indices) {
        Arrays.stream(indices).forEach(index -> {
            if (index.isUnique()) {
                uniqueIndices.add(index);
            } else {
                nonUniqueIndices.add(index);
            }
        });
    }

    public boolean add(E o) {
        if (uniqueIndices.stream().anyMatch(index -> index.contains(o))) {
            return false;
        }
        uniqueIndices.stream().forEach(index -> index.add(o));
        nonUniqueIndices.stream().forEach(index -> index.add(o));
        return true;
    }

    public void remove(E o) {
        uniqueIndices.stream().forEach(index -> index.remove(o));
        nonUniqueIndices.stream().forEach(index -> index.remove(o));
    }
}
