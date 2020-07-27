package ex3;

public class BubbleSortAlgorithm<T> implements SortAlgorithm<T> {
    @Override
    public void sort(T[] arr, Compare<T> c) {
        boolean sorted;
        do {
            sorted = true;
            for (int i = 1; i < arr.length; i++) {
                if (c.less(arr[i], arr[i-1])) {
                    T tmp = arr[i];
                    arr[i] = arr[i-1];
                    arr[i-1] = tmp;
                    sorted = false;
                }
            }
        } while (!sorted);
    }
}
