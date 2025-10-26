package bablr.chat.adapters.out.persistence.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteBaseRepository {
    private final String connectionString;

    public SqliteBaseRepository(String connectionString, Integer queryTimeout) {
        if (connectionString == null) {
            throw new IllegalArgumentException("connectionString must not be null");
        }
        if(connectionString.isBlank()) {
            throw new IllegalArgumentException("connectionString must not be blank");
        }
        if (queryTimeout == null) {
            throw new IllegalArgumentException("queryTimeout must not be null");
        }
        if (queryTimeout < 0) {
            throw new IllegalArgumentException("queryTimeout must not be negative");
        }

        this.connectionString = connectionString;
    }

    protected Connection connection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }
}
