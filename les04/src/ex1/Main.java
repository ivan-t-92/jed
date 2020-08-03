package ex1;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();

        Number[] numbers = new Number[10];
        for (int i = 0; i < 5; i++) {
            numbers[i] = random.nextDouble();
        }
        int intBound = 100;
        for (int i = 5; i < 9; i++) {
            numbers[i] = random.nextInt(intBound);
        }
        numbers[9] = (double)intBound;

        MathBox mathBox = new MathBox(numbers);
        System.out.println(mathBox);
        mathBox.remove(intBound);
        System.out.println("removed " + intBound + ": " + mathBox);
        System.out.println("sum = " + mathBox.summator());
        double d = 10.0;
        mathBox.splitter(d);
        System.out.println("div by " + d + ": " + mathBox);
        System.out.println("hash code: " + mathBox.hashCode());
    }
}
