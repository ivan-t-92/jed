package ex1;

public class HelloOutOfBounds {
    public static void main(String[] args) {
        char[] helloWorld = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        for (int i = 0; i <= helloWorld.length; i++) {
            System.out.print(helloWorld[i]);
        }
        System.out.println();
    }
}