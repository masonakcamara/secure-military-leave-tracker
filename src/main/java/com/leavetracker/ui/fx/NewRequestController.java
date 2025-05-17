package com.leavetracker.ui.fx;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.service.LeaveService;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
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

    /** Populate dropdown on load */
    @FXML
    public void initialize() {
        typeChoiceBox.getItems().addAll("VACATION", "EMERGENCY");
    }

    public void setParent(DashboardController parent) {
        this.parentController = parent;
    }

    @FXML
    private void onSubmit() {
        // ... existing validation and submit logic ...
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) startDatePicker.getScene().getWindow();
        stage.close();
    }

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