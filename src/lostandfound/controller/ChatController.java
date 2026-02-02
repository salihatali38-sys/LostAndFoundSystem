package lostfound.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import lostfound.model.Message;
import lostfound.model.User;
import lostfound.service.MessageService;

import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatController {

    @FXML private Label chatTitleLabel;
    @FXML private Label withLabel;
    @FXML private ListView<String> messageListView;
    @FXML private TextArea messageInput;

    private User currentUser;
    private int receiverId;
    private String receiverUsername;
    private Integer itemId;
    private final MessageService messageService = new MessageService();
    private ObservableList<String> messages = FXCollections.observableArrayList();

    private Timer refreshTimer;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void startConversationWith(int receiverId, String receiverUsername, Integer itemId) {
        this.receiverId = receiverId;
        this.receiverUsername = receiverUsername;
        this.itemId = itemId;

        chatTitleLabel.setText("Chat");
        withLabel.setText("With: " + receiverUsername);

        messageListView.setItems(messages);

        loadConversation();

        // Auto-refresh every 5 seconds
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(ChatController.this::loadConversation);
            }
        }, 0, 5000);
    }

    private void loadConversation() {
        try {
            List<Message> conv = messageService.getConversation(currentUser.getId(), receiverId);
            messages.clear();
            for (Message msg : conv) {
                String sender = msg.getSenderId() == currentUser.getId() ? "You" : msg.getSenderUsername();
                messages.add(sender + " (" + msg.getSentAt() + "): " + msg.getMessageText());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendMessage() {
        String text = messageInput.getText().trim();
        if (text.isEmpty()) return;

        try {
            Message msg = new Message();
            msg.setSenderId(currentUser.getId());
            msg.setReceiverId(receiverId);
            msg.setItemId(itemId);
            msg.setMessageText(text);

            messageService.sendMessage(msg);

            messageInput.clear();
            loadConversation();
        } catch (SQLException e) {
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Failed to send message").showAndWait();
        }
    }

    // Called when window closes
    public void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
    }
}
