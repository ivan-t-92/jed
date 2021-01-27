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
        private final CollectorVisitor<E> collectorVisitor = new CollectorVisitor<>(uniqueIndices, nonUniqueIndices);

        public Builder<E> withIndex(Index<?, E> index) {
            index.accept(collectorVisitor);
            return this;
        }

        public MultiIndexCollection<E> build() {
            return new MultiIndexCollection<E>(this);
        }

        private static final class CollectorVisitor<E> implements IndexVisitor<E> {
            private final ArrayList<Index<?, E>> uniqueIndices;
            private final ArrayList<Index<?, E>> nonUniqueIndices;

            CollectorVisitor(ArrayList<Index<?, E>> uniqueIndices, ArrayList<Index<?, E>> nonUniqueIndices) {
                this.uniqueIndices = uniqueIndices;
                this.nonUniqueIndices = nonUniqueIndices;
            }

            @Override
            public void visitUnique(Index<?, E> index) {
                uniqueIndices.add(index);
            }

            @Override
            public void visitNonUnique(Index<?, E> index) {
                nonUniqueIndices.add(index);
            }
        }
    }
}
