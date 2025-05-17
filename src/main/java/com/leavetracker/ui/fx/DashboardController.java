package com.leavetracker.ui.fx;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.model.User;
import com.leavetracker.service.LeaveService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        addActionButtons();
    }

    public void setUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Hello, " + user.getUsername());
        refreshTable();
    }

    private void refreshTable() {
        List<LeaveRequest> list = leaveService.getRequestsForUser(currentUser.getUsername());
        requestsTable.setItems(FXCollections.observableArrayList(list));
    }

    private void addActionButtons() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Approve");

            {
                btn.setOnAction(e -> {
                    LeaveRequest req = getTableView().getItems().get(getIndex());
                    if ("PENDING".equals(req.getStatus())) {
                        if ("ADMIN".equals(currentUser.getRole())) {
                            leaveService.approveRequest(req.getId());
                            messageLabel.setText("Approved #" + req.getId());
                        } else {
                            messageLabel.setText("Not allowed");
                        }
                        refreshTable();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    @FXML
    private void onNewRequest() {
        // For brevity: show a simple popup to collect dates, type, reason
        // then call leaveService.createRequest and refreshTable()
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