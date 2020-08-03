package ex3;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("--MathBox--");
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
        System.out.println();

        System.out.println("--ObjectBox--");
        ObjectBox<Object> objectBox = new ObjectBox<>();
        objectBox.addObject("string object");
        objectBox.addObject(42);
        // stack overflow
        //objectBox.addObject(objectBox);
        objectBox.addObject(System.out);

        System.out.println(objectBox.dump());
        objectBox.deleteObject(42);
        System.out.println(objectBox.dump());
    }
}
