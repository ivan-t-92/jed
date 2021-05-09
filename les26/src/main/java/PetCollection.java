import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class PetCollection {

    private static final String DB_URL = "jdbc:h2:mem:";
    private final Connection connection;

    public PetCollection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can't initialize database");
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQLConstants.CREATE_PERSON_TABLE);
            statement.execute(SQLConstants.CREATE_PET_TABLE);
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
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.INSERT_PERSON)) {
            statement.setInt(1, person.id);
            statement.setInt(2, person.age);
            statement.setString(3, person.sex.toString());
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
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.INSERT_PET)) {
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
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.SEARCH_PERSON_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Person(id,
                        resultSet.getInt(2),
                        Person.Sex.valueOf(resultSet.getString(3)),
                        resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PetData> getPet(int id) {
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.SEARCH_PET_BY_ID)) {
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
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.SEARCH_PET_BY_NAME)) {
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
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.DELETE_PET)) {
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
        try (PreparedStatement statement = connection.prepareStatement(SQLConstants.SORTED_PETS)) {
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
