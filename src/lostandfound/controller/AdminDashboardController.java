package lostfound.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lostfound.app.MainApp;
import lostfound.model.Item;
import lostfound.model.User;
import lostfound.service.ItemService;

import java.io.IOException;
import java.sql.SQLException;

public class AdminDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Item> itemTable;
    @FXML private TableColumn<Item, Integer> colId;
    @FXML private TableColumn<Item, String> colName;
    @FXML private TableColumn<Item, String> colType;
    @FXML private TableColumn<Item, String> colDesc;
    @FXML private TableColumn<Item, String> colPostedBy;

    private ItemService itemService = new ItemService();
    private User currentUser;
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Admin Dashboard - " + user.getUsername());
        loadItems();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPostedBy.setCellValueFactory(new PropertyValueFactory<>("postedByUsername"));
    }

    @FXML
    public void loadItems() {
        try {
            itemTable.setItems(FXCollections.observableArrayList(itemService.getAllItems()));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot load items: " + e.getMessage()).showAndWait();
        }
    }

    @FXML private void handleAddItem() {
        openItemForm(null);
    }

    @FXML private void handleEditItem() {
        Item item = itemTable.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                if (itemService.canEditOrDelete(item, currentUser.getId())) {
                    openItemForm(item);
                } else {
                    new Alert(Alert.AlertType.WARNING, "You can only edit your own items.").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Permission check failed.").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Select item first").showAndWait();
        }
    }

    @FXML private void handleDeleteItem() {
        Item item = itemTable.getSelectionModel().getSelectedItem();
        if (item == null) {
            new Alert(Alert.AlertType.WARNING, "Select item first").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + item.getName() + " ?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                itemService.deleteItem(item.getId(), currentUser.getId());  // fixed
                loadItems();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Delete failed: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML private void handleLogout() {
        mainApp.showLogin();
    }

    private void openItemForm(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lostfound/fxml/item_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(item == null ? "Add Item" : "Edit Item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(getOwnerWindow());

            ItemFormController ctrl = loader.getController();
            ctrl.setItem(item);
            ctrl.setDashboardController(this);
            ctrl.setCurrentUserId(currentUser.getId());  // pass user ID

            stage.showAndWait();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot open form: " + e.getMessage()).showAndWait();
        }
    }

    public Window getOwnerWindow() {
        return itemTable.getScene().getWindow();
    }
}