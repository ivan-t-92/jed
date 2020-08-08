package ex1;

import java.util.Comparator;
import java.util.Iterator;

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
        Iterator<PetData> itr = c.searchByName(name);
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
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
        Iterator<PetData> itr = c.sortedIterator(new Comparator<>() {
            @Override
            public int compare(PetData o1, PetData o2) {
                int comp = o1.owner.name.compareTo(o2.owner.name);
                if (comp != 0) {
                    return comp;
                }
                comp = o1.name.compareTo(o2.name);
                if (comp != 0) {
                    return comp;
                }
                return o2.weight - o1.weight;
            }
        });
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}
