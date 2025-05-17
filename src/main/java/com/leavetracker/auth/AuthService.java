package com.leavetracker.auth;

import com.leavetracker.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles user sign-up and sign-in using an in-memory store and BCrypt.
 */
public class AuthService {

    private final Map<String, User> users = new HashMap<>();

    /**
     * Add a new user with a hashed password.
     *
     * @param username unique login name
     * @param password plain-text password
     * @param role     the user’s role, e.g. "USER" or "ADMIN"
     * @return true if the user was added; false if the username is taken
     */
    public boolean register(String username, String password, String role) {
        if (users.containsKey(username)) {
            return false;
        }
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        users.put(username, new User(username, hash, role));
        return true;
    }

    /**
     * Check credentials and return the user if they match.
     *
     * @param username login name
     * @param password plain-text password
     * @return the User on success, or null if login fails
     */
    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
}