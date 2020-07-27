package ex1;

public class HelloNPE {

    public static void main(String[] args) {
        HelloWorld helloWorld = null;
        if (args.length > 1) {
            helloWorld = new HelloWorld("hello world");
        }
        System.out.println(helloWorld.getMessage());
    }

    private static class HelloWorld {
        private final String message;

        HelloWorld(String message) {
            this.message = message;
        }

        String getMessage() {
            return message;
        }
    }
}