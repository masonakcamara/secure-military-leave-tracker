package com.leavetracker.ui.fx;

import com.leavetracker.auth.AuthService;
import com.leavetracker.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/fx/DashboardView.fxml")
                );
                Parent root = loader.load();
                DashboardController ctrl = loader.getController();
                ctrl.setUser(u);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (IOException e) {
                messageLabel.setText("Failed to open dashboard");
                e.printStackTrace();
            }
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