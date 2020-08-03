package ex1;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class MathBox {
    private HashSet<Number> numberSet = new HashSet<>();

    public MathBox(Number[] numberArray) {
        Collections.addAll(numberSet, numberArray);
    }

    public double summator() {
        double sum = 0.0;
        for (Number n: numberSet) {
            sum += n.doubleValue();
        }
        return sum;
    }

    public void splitter(double d) {
        HashSet<Number> newNumberSet = new HashSet<>();
        for (Number n: numberSet) {
            newNumberSet.add(n.doubleValue() / d);
        }
        numberSet = newNumberSet;
    }

    public void remove(Integer i) {
        for (Number n: numberSet) {
            if (n.doubleValue() == i.doubleValue()) {
                numberSet.remove(n);
                break;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MathBox mathBox = (MathBox) o;
        return Objects.equals(numberSet, mathBox.numberSet);
    }

    @Override
    public int hashCode() {
        return numberSet.hashCode();
    }

    @Override
    public String toString() {
        return numberSet.toString();
    }
}
