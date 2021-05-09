import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static Connection connection;

    public static void main(String[] args) {
        initDatabase();
        try {
            testPetCollection();
        } finally {
            closeDatabase();
        }
    }

    private static void testPetCollection() {
        PetCollection c = new PetCollection();

        int ericId, sarahId, johnId;
        try {
            int personId = 0;
            c.add(new Person(ericId = personId++, 50, Person.Sex.MALE, "Eric"));
            c.add(new Person(sarahId = personId++, 30, Person.Sex.FEMALE, "Sarah"));
            c.add(new Person(johnId = personId++, 15, Person.Sex.MALE, "John"));

            int petId = 0;
            c.add(new PetData(petId++, "Rex", ericId, 40));
            c.add(new PetData(petId++, "Rex", ericId, 50));
            c.add(new PetData(petId++, "Hunter", ericId, 20));
            c.add(new PetData(petId++, "Jasper", sarahId, 5));
            c.add(new PetData(petId++, "Jack", sarahId, 10));
            c.add(new PetData(petId++, "Jack", johnId, 20));
        } catch (AlreadyExistsException ex) {
            ex.printStackTrace();
            return;
        }

        System.out.println("-- Sorted collection --");
        printSorted(c);
        System.out.println();

        String name = "Jack";
        System.out.println("-- Search for pets named " + name + " --");
        c.searchByName(name).forEach(System.out::println);
        System.out.println();

        PetData newData = new PetData(5, "Lucy", johnId, 10);
        System.out.println("-- Change " + newData + " --");
        c.replaceById(newData);
        printSorted(c);
        System.out.println();

        System.out.println("-- Adding duplicate id, expecting exception --");
        try {
            c.add(new PetData(0, "Rex", ericId, 40));
        } catch (AlreadyExistsException ex) {
            ex.printStackTrace();
        }
        System.out.println();
    }

    static void printSorted(PetCollection c) {
        c.sortedStream().forEachOrdered(System.out::println);
    }

    static void initDatabase() {
        try {
            connection = DriverManager.getConnection(SQLConstants.DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can't initialize database");
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQLConstants.CREATE_PERSON_TABLE);
            statement.execute(SQLConstants.CREATE_PET_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can't create tables in database");
        }
    }

    static void closeDatabase() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
