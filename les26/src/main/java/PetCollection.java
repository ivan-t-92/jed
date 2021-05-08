import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class PetCollection {

    private static final String DB_URL = "jdbc:h2:mem:";
    private static final String CREATE_PERSON_TABLE_SQL = """
            CREATE TABLE personTable(
                id INT NOT NULL PRIMARY KEY,
                age INT NOT NULL,
                sex INT NOT NULL,
                name VARCHAR(255) NOT NULL
            );""";
    private static final String CREATE_PET_TABLE_SQL = """
            CREATE TABLE petTable(
                id INT NOT NULL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                ownerId INT NOT NULL,
                weight INT NOT NULL,
                FOREIGN KEY(ownerId) REFERENCES personTable
            );""";
    private static final String INSERT_PERSON_SQL = "INSERT INTO personTable VALUES(?, ?, ?, ?);";
    private static final String INSERT_PET_SQL = "INSERT INTO petTable VALUES(?, ?, ?, ?);";
    private static final String DELETE_PET_SQL = "DELETE FROM petTable WHERE id = ?;";
    private static final String SEARCH_PET_BY_ID_SQL = "SELECT * FROM petTable WHERE id = ?;";
    private static final String SEARCH_PERSON_BY_ID_SQL = "SELECT * FROM personTable WHERE id = ?;";
    private static final String SEARCH_PET_BY_NAME_SQL = "SELECT * FROM petTable WHERE name = ?;";
    private static final String SORTED_PETS_SQL = """
            SELECT * FROM petTable
            JOIN personTable ON petTable.ownerId = personTable.id
            ORDER BY personTable.name, petTable.name, petTable.weight;
            """;

    private final Connection connection;

    public PetCollection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can't initialize database");
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_PERSON_TABLE_SQL);
            statement.execute(CREATE_PET_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Person person) throws AlreadyExistsException {
        if (getPerson(person.id).isPresent()) {
            throw new AlreadyExistsException("id " + person.id + " already exists");
        }
        addUnchecked(person);
    }

    private void addUnchecked(Person person) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PERSON_SQL)) {
            statement.setInt(1, person.id);
            statement.setInt(2, person.age);
            statement.setInt(3, person.sex.ordinal());
            statement.setString(4, person.name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(PetData newData) throws AlreadyExistsException {
        if (getPet(newData.id).isPresent()) {
            throw new AlreadyExistsException("id " + newData.id + " already exists");
        }
        addUnchecked(newData);
    }

    private void addUnchecked(PetData newData) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PET_SQL)) {
            statement.setInt(1, newData.id);
            statement.setString(2, newData.name);
            statement.setInt(3, newData.ownerId);
            statement.setInt(4, newData.weight);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Person> getPerson(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SEARCH_PERSON_BY_ID_SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Person(id,
                        resultSet.getInt(2),
                        Person.Sex.values()[resultSet.getInt(3)],
                        resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PetData> getPet(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SEARCH_PET_BY_ID_SQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new PetData(id,
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<PetData> searchByName(String name) {
        try (PreparedStatement statement = connection.prepareStatement(SEARCH_PET_BY_NAME_SQL)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            List<PetData> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(new PetData(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void delete(PetData petData) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PET_SQL)) {
            statement.setInt(1, petData.id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void replaceById(PetData newData) {
        if (getPet(newData.id).isPresent()) {
            delete(newData);
        }
        addUnchecked(newData);
    }

    public Stream<PetData> sortedStream() {
        try (PreparedStatement statement = connection.prepareStatement(SORTED_PETS_SQL)) {
            ResultSet resultSet = statement.executeQuery();

            List<PetData> resultList = new ArrayList<>();
            while (resultSet.next()) {
                resultList.add(new PetData(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getInt(4)));
            }
            return resultList.stream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
