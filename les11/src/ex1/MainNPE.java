package ex1;

public class MainNPE {
    public static void main(String[] args) {
        Person person = new Person(null, 0);
        String regex = null;
        System.out.println(person.name().split(regex));
    }
}
