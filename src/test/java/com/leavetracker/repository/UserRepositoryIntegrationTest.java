package com.leavetracker.repository;

import com.leavetracker.model.User;
import com.leavetracker.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryIntegrationTest {
    private UserRepository userRepo;

    @BeforeEach
    void setUp() throws Exception {
        // wipe users table
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM users");
        }
        userRepo = new UserRepository();
    }

    @Test
    void saveNewUserSucceeds() {
        User u = new User("alice", "hash123", "USER");
        assertTrue(userRepo.save(u), "Should insert new user");
    }

    @Test
    void duplicateSaveFails() {
        User u1 = new User("bob", "hash1", "USER");
        User u2 = new User("bob", "hash2", "ADMIN");
        assertTrue(userRepo.save(u1), "First insert should succeed");
        assertFalse(userRepo.save(u2), "Second insert with same username should fail");
    }

    @Test
    void findNonexistentReturnsEmpty() {
        Optional<User> opt = userRepo.findByUsername("ghost");
        assertTrue(opt.isEmpty(), "Lookup of missing user should be empty");
    }

    @Test
    void findExistingReturnsUser() {
        User u = new User("carol", "secretHash", "ADMIN");
        assertTrue(userRepo.save(u), "Insert should succeed");
        Optional<User> opt = userRepo.findByUsername("carol");
        assertTrue(opt.isPresent(), "Lookup should find the user");
        assertEquals("ADMIN", opt.get().getRole());
    }
}