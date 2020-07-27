package ex3;

public class InsertionSortAlgorithm<T> implements SortAlgorithm<T> {
    @Override
    public void sort(T[] arr, Compare<T> c) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (c.less(arr[j], arr[j-1])) {
                    T tmp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = tmp;
                } else {
                    break;
                }
            }
        }
    }
}
