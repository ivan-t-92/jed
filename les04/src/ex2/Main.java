package ex2;

public class Main {
    public static void main(String[] args) {
        ObjectBox objectBox = new ObjectBox();
        objectBox.addObject("string object");
        objectBox.addObject(42);
        objectBox.addObject(objectBox);

        System.out.println(objectBox.dump());
        objectBox.deleteObject(42);
        System.out.println(objectBox.dump());
    }
}
