package ex1;

public class Main {
    public static void main(String[] args) {
        PetCollection c = new PetCollection();
        Person eric = new Person(50, Person.Sex.MALE, "Eric");
        Person sarah = new Person(30, Person.Sex.FEMALE, "Sarah");
        Person john = new Person(15, Person.Sex.MALE, "John");

        int id = 0;
        try {
            c.add(new PetData(id++, "Rex", eric, 40));
            c.add(new PetData(id++, "Rex", eric, 50));
            c.add(new PetData(id++, "Hunter", eric, 20));
            c.add(new PetData(id++, "Jasper", sarah, 5));
            c.add(new PetData(id++, "Jack", sarah, 10));
            c.add(new PetData(id++, "Jack", john, 20));
        } catch (AlreadyExistsException ex) {
            ex.printStackTrace();
        }

        System.out.println("-- Sorted collection --");
        printSorted(c);
        System.out.println();

        String name = "Jack";
        System.out.println("-- Search for pets named " + name + " --");
        c.searchByName(name).stream().forEach(System.out::println);
        System.out.println();

        PetData newData = new PetData(5, "Lucy", john, 10);
        System.out.println("-- Change " + newData + " --");
        c.replaceById(newData);
        printSorted(c);
        System.out.println();

        System.out.println("-- Adding duplicate id, expecting exception --");
        try {
            c.add(new PetData(0, "Rex", eric, 40));
        } catch (AlreadyExistsException ex) {
            ex.printStackTrace();
        }
        System.out.println();
    }

    static void printSorted(PetCollection c) {
        c.sortedStream().forEachOrdered(System.out::println);
    }
}
