package ex1;

import java.util.Arrays;

public class MySorter implements Sorter {
    @Override
    public void bubbleSort(int[] array) {
        boolean sorted;
        int n = array.length;
        do {
            sorted = true;
            for (int i = 1; i < n; i++) {
                if (array[i] < array[i-1]) {
                    int tmp = array[i];
                    array[i] = array[i-1];
                    array[i-1] = tmp;
                    sorted = false;
                }
            }
            n--;
        } while (!sorted);
    }

    @Override
    public void standardSort(int[] array) {
        Arrays.sort(array);
    }
}
