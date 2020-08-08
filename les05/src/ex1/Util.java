package ex1;

import java.util.Comparator;
import java.util.List;

public class Util {
    public static <T> int lowerBound(List<T> l, T key, Comparator<T> c) {
        int low = 0;
        int high = l.size()-1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (c.compare(l.get(mid), key) < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return low;
    }
}
