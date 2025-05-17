package com.leavetracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Sets up and provides connections to the H2 database.
 * <p>
 * On first load, it creates the `users` and `leave_requests` tables if they don't exist.
 * </p>
 */
public class DatabaseUtil {
    private static final String JDBC_URL = "jdbc:h2:./data/leaveTrackerDb;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "";

    static {
        // Initialize tables
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                  username VARCHAR(50) PRIMARY KEY,
                  password_hash VARCHAR(60) NOT NULL,
                  role VARCHAR(20) NOT NULL
                )
                """);
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS leave_requests (
                  id BIGINT PRIMARY KEY,
                  username VARCHAR(50) NOT NULL,
                  start_date DATE NOT NULL,
                  end_date DATE NOT NULL,
                  type VARCHAR(20) NOT NULL,
                  reason VARCHAR(255),
                  status VARCHAR(20) NOT NULL
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database tables", e);
        }
    }

    /**
     * Opens and returns a new Connection to the H2 database.
     *
     * @return a Connection object
     * @throws SQLException if the connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }
}