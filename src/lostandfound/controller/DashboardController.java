package lostfound.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lostfound.app.MainApp;
import lostfound.model.Item;
import lostfound.model.User;
import lostfound.service.ItemService;

import java.io.IOException;
import java.sql.SQLException;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Item> itemTable;
    @FXML private TableColumn<Item, Integer> colId;
    @FXML private TableColumn<Item, String> colName;
    @FXML private TableColumn<Item, String> colType;
    @FXML private TableColumn<Item, String> colDesc;
    @FXML private TableColumn<Item, String> colPostedBy;
    @FXML private TableColumn<Item, Void> colActions;

    private ItemService itemService = new ItemService();
    private User currentUser;
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        loadItems();
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPostedBy.setCellValueFactory(new PropertyValueFactory<>("postedByUsername"));

        // Actions column with per-row buttons
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final Button messageBtn = new Button("Message");

            private final HBox hbox = new HBox(10, editBtn, deleteBtn, messageBtn);

            {
                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                messageBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                editBtn.setOnAction(e -> handleEdit(getTableRow().getItem()));
                deleteBtn.setOnAction(e -> handleDelete(getTableRow().getItem()));
                messageBtn.setOnAction(e -> handleMessageOwner(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Item rowItem = getTableRow().getItem();
                boolean isOwner = rowItem.getPostedBy() == currentUser.getId();

                editBtn.setVisible(isOwner);
                deleteBtn.setVisible(isOwner);
                messageBtn.setVisible(!isOwner);

                // Show the hbox only if at least one button is visible
                setGraphic((isOwner || !isOwner) ? hbox : null);
            }
        });
    }

    @FXML
    public void loadItems() {
        try {
            itemTable.setItems(FXCollections.observableArrayList(itemService.getAllItems()));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load items: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddItem() {
        openItemForm(null);
    }

    private void handleEdit(Item item) {
        if (item != null) {
            openItemForm(item);
        }
    }

    private void handleDelete(Item item) {
        if (item == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete item: " + item.getName() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                itemService.deleteItem(item.getId(), currentUser.getId());
                loadItems();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Delete failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleMessageOwner(Item item) {
        if (item == null) return;

        if (item.getPostedBy() == currentUser.getId()) {
            showAlert(Alert.AlertType.INFORMATION, "This is your own item.");
            return;
        }

        openChatWindow(item.getPostedBy(), item.getPostedByUsername(), item.getId());
    }

    @FXML
    private void openMessages() {
        openChatWindow(-1, null, null); // open general chat window
    }

    @FXML
    private void handleLogout() {
        mainApp.showLogin();
    }

    private void openItemForm(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lostfound/fxml/item_form.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(item == null ? "Add Item" : "Edit Item");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(itemTable.getScene().getWindow());

            ItemFormController ctrl = loader.getController();
            ctrl.setItem(item);
            ctrl.setDashboardController(this);
            ctrl.setCurrentUserId(currentUser.getId());

            stage.showAndWait();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to open form: " + e.getMessage());
        }
    }

    private void openChatWindow(int receiverId, String receiverUsername, Integer itemId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lostfound/fxml/chat_window.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load(), 900, 600));
            stage.setTitle(receiverId == -1 ? "My Messages" : "Chat");

            ChatWindowController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            if (receiverId != -1) {
                controller.openChatWith(receiverId, receiverUsername, itemId);
            }

            stage.setOnCloseRequest(e -> controller.cleanup());
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to open chat: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}