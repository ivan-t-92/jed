public final class SQLConstants {
    static final String CREATE_PERSON_TABLE =
            "CREATE TABLE personTable(" +
                    "id INT NOT NULL PRIMARY KEY," +
                    "age INT NOT NULL," +
                    "sex INT NOT NULL," +
                    "name VARCHAR(255) NOT NULL);";
    static final String CREATE_PET_TABLE =
            "CREATE TABLE petTable(" +
                    "id INT NOT NULL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "ownerId INT NOT NULL," +
                    "weight INT NOT NULL," +
                    "FOREIGN KEY(ownerId) REFERENCES personTable);";
    static final String INSERT_PERSON = "INSERT INTO personTable VALUES(?, ?, ?, ?);";
    static final String INSERT_PET = "INSERT INTO petTable VALUES(?, ?, ?, ?);";
    static final String DELETE_PET = "DELETE FROM petTable WHERE id = ?;";
    static final String SEARCH_PET_BY_ID = "SELECT * FROM petTable WHERE id = ?;";
    static final String SEARCH_PERSON_BY_ID = "SELECT * FROM personTable WHERE id = ?;";
    static final String SEARCH_PET_BY_NAME = "SELECT * FROM petTable WHERE name = ?;";
    static final String SORTED_PETS =
            "SELECT * FROM petTable " +
                    "JOIN personTable ON petTable.ownerId = personTable.id " +
                    "ORDER BY personTable.name, petTable.name, petTable.weight;";

    private SQLConstants() {}
}
