package com.leavetracker.ui.fx;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.service.LeaveService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Dialog controller for creating a new leave request.
 */
public class NewRequestController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private TextArea reasonArea;
    @FXML private Label errorLabel;

    private final LeaveService leaveService = new LeaveService();
    private DashboardController parentController;

    /** Populate the Type dropdown on load */
    @FXML
    public void initialize() {
        typeChoiceBox.getItems().addAll("VACATION", "EMERGENCY");
    }

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

        // Validation
        if (start == null || end == null || type == null || reason.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    "Validation Error",
                    null,
                    "All fields are required and a Type must be chosen.");
            return;
        }
        if (end.isBefore(start)) {
            showAlert(Alert.AlertType.WARNING,
                    "Validation Error",
                    null,
                    "End date must be on or after start date.");
            return;
        }

        try {
            LeaveRequest req = leaveService.createRequest(
                    parentController.getCurrentUser().getUsername(),
                    start, end, type, reason
            );

            showAlert(Alert.AlertType.INFORMATION,
                    "Request Created",
                    null,
                    "Leave request #" + req.getId() + " created successfully.");
            parentController.refreshTable();
            closeWindow();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Save Error",
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

    /** Helper to show a modal Alert dialog. */
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
