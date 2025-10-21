package bablr.chat.infrastructure.persistence.sqlite;

import bablr.chat.infrastructure.config.BablrConfig;

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

    public BaseRepositorySqliteImpl() {
        this(BablrConfig.get("sqlite.connectionString"), BablrConfig.getInt("sqlite.queryTimeout"));
    }

    protected Connection connection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }
}
