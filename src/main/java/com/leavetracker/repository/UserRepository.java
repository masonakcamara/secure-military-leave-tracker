package com.leavetracker.repository;

import com.leavetracker.model.User;
import com.leavetracker.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Persists and retrieves User records from the database.
 */
public class UserRepository {

    private static final String INSERT_SQL =
            "INSERT INTO users(username, password_hash, role) VALUES (?, ?, ?)";

    private static final String SELECT_SQL =
            "SELECT username, password_hash, role FROM users WHERE username = ?";

    /**
     * Save a new user. Fails silently if the username already exists.
     *
     * @param user the User to insert
     * @return true if insertion succeeded; false on duplicate or error
     */
    public boolean save(User user) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            // duplicate key or other error
            return false;
        }
    }

    /**
     * Find a user by username.
     *
     * @param username lookup key
     * @return Optional containing the User if found; otherwise empty
     */
    public Optional<User> findByUsername(String username) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SQL)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String user = rs.getString("username");
                    String hash = rs.getString("password_hash");
                    String role = rs.getString("role");
                    return Optional.of(new User(user, hash, role));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying user", e);
        }
        return Optional.empty();
    }
}