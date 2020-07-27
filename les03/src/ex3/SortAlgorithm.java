package ex3;

public interface SortAlgorithm<T> {
    void sort(T[] arr, Compare<T> c);
}
