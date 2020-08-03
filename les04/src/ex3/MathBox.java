package ex3;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class MathBox extends ObjectBox<Number> {

    public MathBox(Number[] numberArray) {
        Collections.addAll(objectSet, numberArray);
    }

    public double summator() {
        double sum = 0.0;
        for (Number n: objectSet) {
            sum += n.doubleValue();
        }
        return sum;
    }

    public void splitter(double d) {
        HashSet<Number> newNumberSet = new HashSet<>();
        for (Number n: objectSet) {
            newNumberSet.add(n.doubleValue() / d);
        }
        objectSet = newNumberSet;
    }

    public void remove(Integer i) {
        for (Number n: objectSet) {
            if (n.doubleValue() == i.doubleValue()) {
                objectSet.remove(n);
                break;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathBox mathBox = (MathBox) o;
        return Objects.equals(objectSet, mathBox.objectSet);
    }

}
