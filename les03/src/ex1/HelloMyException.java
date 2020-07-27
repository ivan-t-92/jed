package ex1;

public class HelloMyException {
    public static void main(String[] args) throws MyException {
        throw new MyException("hello world");
    }

    private static class MyException extends Exception {
        MyException(String message) {
            super(message);
        }
    }
}
