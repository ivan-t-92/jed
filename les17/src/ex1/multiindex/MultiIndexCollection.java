package ex1.multiindex;

import java.util.ArrayList;

public class MultiIndexCollection<E> {
    private final ArrayList<Index<?, E>> uniqueIndices;
    private final ArrayList<Index<?, E>> nonUniqueIndices;

    MultiIndexCollection(Builder<E> builder) {
        this.uniqueIndices = builder.uniqueIndices;
        this.nonUniqueIndices = builder.nonUniqueIndices;
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

    public static final class Builder<E> {
        private final ArrayList<Index<?, E>> uniqueIndices = new ArrayList<>();
        private final ArrayList<Index<?, E>> nonUniqueIndices = new ArrayList<>();

        public Builder<E> withIndex(Index<?, E> index) {
            if (index.isUnique()) {
                uniqueIndices.add(index);
            } else {
                nonUniqueIndices.add(index);
            }
            return this;
        }

        public MultiIndexCollection<E> build() {
            return new MultiIndexCollection<E>(this);
        }
    }
}
