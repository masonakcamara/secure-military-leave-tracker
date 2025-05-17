package com.leavetracker.ui.fx;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.model.User;
import com.leavetracker.service.LeaveService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private TableView<LeaveRequest> requestsTable;
    @FXML private TableColumn<LeaveRequest, Long> colId;
    @FXML private TableColumn<LeaveRequest, LocalDate> colStart;
    @FXML private TableColumn<LeaveRequest, LocalDate> colEnd;
    @FXML private TableColumn<LeaveRequest, String> colType;
    @FXML private TableColumn<LeaveRequest, String> colReason;
    @FXML private TableColumn<LeaveRequest, String> colStatus;
    @FXML private TableColumn<LeaveRequest, Void> colAction;
    @FXML private Label messageLabel;

    private final LeaveService leaveService = new LeaveService();
    private User currentUser;

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colAction.setCellFactory(col -> new TableCell<>() {
            private final HBox buttons = new HBox(5);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                buttons.getChildren().clear();

                if (empty) {
                    setGraphic(null);
                    return;
                }

                LeaveRequest req = getTableView().getItems().get(getIndex());
                String status = req.getStatus();

                if ("PENDING".equals(status)) {
                    if ("ADMIN".equals(currentUser.getRole())) {
                        Button approveBtn = new Button("Approve");
                        approveBtn.setOnAction(e -> {
                            leaveService.approveRequest(req.getId());
                            messageLabel.setText("Approved request #" + req.getId());
                            refreshTable();
                        });

                        Button denyBtn = new Button("Deny");
                        denyBtn.setOnAction(e -> {
                            leaveService.denyRequest(req.getId());
                            messageLabel.setText("Denied request #" + req.getId());
                            refreshTable();
                        });

                        buttons.getChildren().addAll(approveBtn, denyBtn);
                    } else {
                        Button cancelBtn = new Button("Cancel");
                        cancelBtn.setOnAction(e -> {
                            leaveService.cancelRequest(req.getId());
                            messageLabel.setText("Cancelled request #" + req.getId());
                            refreshTable();
                        });
                        buttons.getChildren().add(cancelBtn);
                    }
                }

                setGraphic(buttons);
            }
        });
    }

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Hello, " + user.getUsername());
        refreshTable();
    }

    void refreshTable() {
        List<LeaveRequest> list = leaveService.getRequestsForUser(currentUser.getUsername());
        requestsTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    private void onNewRequest() {
        // existing NewRequest dialog logic...
    }

    @FXML
    private void onLogout() {
        try {
            MainApp main = new MainApp();
            main.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage current = (Stage) welcomeLabel.getScene().getWindow();
        current.close();
    }
}
