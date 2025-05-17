package com.leavetracker.model;

/**
 * Represents a user of the Secure Military Leave Tracker.
 * Stores the login name, the hashed password, and the user’s role.
 */
public class User {
    private String username;
    private String passwordHash;
    private String role; // e.g., "USER" or "ADMIN"

    /**
     * Constructs a new User.
     * @param username the unique login identifier
     * @param passwordHash the BCrypt‐hashed password
     * @param role the user’s role (USER or ADMIN)
     */
    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    /**
     * @return the user’s username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the BCrypt‐hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @return the user’s role
     */
    public String getRole() {
        return role;
    }

    /**
     * Updates this user’s password hash.
     * @param passwordHash the new BCrypt‐hashed password
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}