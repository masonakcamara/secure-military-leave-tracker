package com.leavetracker.ui.fx;

import com.leavetracker.auth.AuthService;
import com.leavetracker.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the login/register screen.
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthService auth = new AuthService();

    @FXML
    private void onLogin() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();
        User u = auth.login(user, pass);
        if (u != null) {
            messageLabel.setText("Welcome, " + u.getUsername());
            // TODO: switch to main dashboard
        } else {
            messageLabel.setText("Login failed");
        }
    }

    @FXML
    private void onRegister() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();
        boolean ok = auth.register(user, pass, "USER");
        messageLabel.setText(ok ? "Registered! Please login." : "User exists");
    }
}