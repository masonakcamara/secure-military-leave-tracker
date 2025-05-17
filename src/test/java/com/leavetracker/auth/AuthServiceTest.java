package com.leavetracker.auth;

import com.leavetracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService auth;

    @BeforeEach
    void setUp() {
        auth = new AuthService();
    }

    @Test
    void registerNewUserSucceeds() {
        boolean first = auth.register("bob", "pass123", "USER");
        assertTrue(first, "First registration should succeed");
    }

    @Test
    void duplicateRegisterFails() {
        auth.register("bob", "pass123", "USER");
        boolean second = auth.register("bob", "pass123", "USER");
        assertFalse(second, "Registering same username twice should fail");
    }

    @Test
    void loginWithCorrectCredentialsReturnsUser() {
        auth.register("jane", "secret!", "ADMIN");
        User u = auth.login("jane", "secret!");
        assertNotNull(u, "Login should return a User object");
        assertEquals("jane", u.getUsername());
        assertEquals("ADMIN", u.getRole());
    }

    @Test
    void loginWithWrongPasswordReturnsNull() {
        auth.register("joe", "letmein", "USER");
        User u = auth.login("joe", "wrong");
        assertNull(u, "Login with wrong password should return null");
    }

    @Test
    void loginNonexistentUserReturnsNull() {
        User u = auth.login("ghost", "nopass");
        assertNull(u, "Login for non-existent user should return null");
    }
}