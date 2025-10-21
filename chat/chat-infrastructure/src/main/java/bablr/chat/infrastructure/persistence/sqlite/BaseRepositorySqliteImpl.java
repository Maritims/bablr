package bablr.chat.infrastructure.persistence.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseRepositorySqliteImpl {
    private final String connectionString;

    public BaseRepositorySqliteImpl(String connectionString, int queryTimeout) {
        if (connectionString == null || connectionString.isBlank()) {
            throw new IllegalArgumentException("connectionString must not be null or blank");
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
