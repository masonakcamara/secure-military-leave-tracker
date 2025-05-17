package com.leavetracker.auth;

import com.leavetracker.model.User;
import com.leavetracker.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

/**
 * Handles user sign-up and sign-in against the H2 database.
 */
public class AuthService {

    private final UserRepository userRepo = new UserRepository();

    /**
     * Register a new user by hashing their password and saving to the DB.
     *
     * @param username unique login name
     * @param password plain-text password
     * @param role     the user’s role, e.g. "USER" or "ADMIN"
     * @return true if saved; false if username already exists
     */
    public boolean register(String username, String password, String role) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User(username, hash, role);
        return userRepo.save(user);
    }

    /**
     * Authenticate a user by verifying their password hash.
     *
     * @param username login name
     * @param password plain-text password
     * @return the User on success; null if login fails
     */
    public User login(String username, String password) {
        Optional<User> opt = userRepo.findByUsername(username);
        if (opt.isPresent() && BCrypt.checkpw(password, opt.get().getPasswordHash())) {
            return opt.get();
        }
        return null;
    }
}