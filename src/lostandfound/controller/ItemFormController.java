package lostfound.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lostfound.model.Item;
import lostfound.service.ItemService;

import java.sql.SQLException;

public class ItemFormController {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextArea descField;

    private Object dashboardController;
    private final ItemService itemService = new ItemService();
    private Item currentItem;
    private int currentUserId;

    @FXML
    private void initialize() {
        typeCombo.getItems().addAll("LOST", "FOUND");
        typeCombo.setValue("LOST");
    }

    public void setDashboardController(Object controller) {
        this.dashboardController = controller;
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public void setItem(Item item) {
        this.currentItem = item;
        if (item != null) {
            nameField.setText(item.getName());
            typeCombo.setValue(item.getType());
            descField.setText(item.getDescription());
        } else {
            nameField.clear();
            descField.clear();
            typeCombo.setValue("LOST");
        }
    }

    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();
        String type = typeCombo.getValue();
        String description = descField.getText().trim();

        if (name.isEmpty() || type == null || description.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "All fields are required!");
            return;
        }

        try {
            if (currentItem == null) {
                Item newItem = new Item(name, type, description, currentUserId);
                itemService.addItem(newItem, currentUserId);
                showAlert(Alert.AlertType.INFORMATION, "Item added successfully!");
            } else {
                currentItem.setName(name);
                currentItem.setType(type);
                currentItem.setDescription(description);
                itemService.updateItem(currentItem, currentUserId);
                showAlert(Alert.AlertType.INFORMATION, "Item updated successfully!");
            }

            refreshParentDashboard();
            closeWindow();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void refreshParentDashboard() {
        if (dashboardController instanceof DashboardController dc) {
            dc.loadItems();
        } else if (dashboardController instanceof AdminDashboardController adc) {
            adc.loadItems();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}