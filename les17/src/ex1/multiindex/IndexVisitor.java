package ex1.multiindex;

interface IndexVisitor<E> {
    void visitUnique(Index<?,E> index);
    void visitNonUnique(Index<?,E> index);
}
