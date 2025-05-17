package com.leavetracker.ui.fx;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.service.LeaveService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDate;

public class NewRequestController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private TextArea reasonArea;
    @FXML private Label errorLabel;

    private final LeaveService leaveService = new LeaveService();
    private DashboardController parentController;

    /** Called by DashboardController to set itself as callback. */
    public void setParent(DashboardController parent) {
        this.parentController = parent;
    }

    @FXML
    private void onSubmit() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end   = endDatePicker.getValue();
        String type     = typeChoiceBox.getValue();
        String reason   = reasonArea.getText().trim();

        if (start == null || end == null || type == null || reason.isEmpty()) {
            errorLabel.setText("All fields are required");
            return;
        }
        if (end.isBefore(start)) {
            errorLabel.setText("End date must be on or after start date");
            return;
        }

        try {
            LeaveRequest req = leaveService.createRequest(
                    parentController.getCurrentUser().getUsername(),
                    start, end, type, reason
            );
            parentController.refreshTable();
            closeWindow();

        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Request Error",
                    "Could not save leave request",
                    e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) startDatePicker.getScene().getWindow();
        stage.close();
    }

    /** Utility to pop up an Alert dialog. */
    private void showAlert(Alert.AlertType type,
                           String title,
                           String header,
                           String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}