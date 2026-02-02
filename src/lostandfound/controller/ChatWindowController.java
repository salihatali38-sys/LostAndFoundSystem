package lostfound.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lostfound.model.Message;
import lostfound.model.User;
import lostfound.service.MessageService;

import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatWindowController {

    @FXML private ListView<String> conversationList;
    @FXML private ListView<Message> messageList;
    @FXML private TextArea messageInput;
    @FXML private Label chatHeader;
    @FXML private VBox chatPane;

    private User currentUser;
    private final MessageService messageService = new MessageService();
    private final ObservableList<String> conversations = FXCollections.observableArrayList();
    private int selectedReceiverId = -1;
    private Integer selectedItemId = null;

    private Timer refreshTimer;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        refreshAll();
    }

    @FXML
    private void initialize() {
        conversationList.setItems(conversations);
        conversationList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    String username = newVal.substring(0, newVal.lastIndexOf(" (ID:"));
                    String idStr = newVal.substring(newVal.lastIndexOf("ID: ") + 4, newVal.lastIndexOf(")"));
                    selectedReceiverId = Integer.parseInt(idStr.trim());
                    selectedItemId = null;
                    chatHeader.setText("Chat with " + username.trim());
                    loadMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                selectedReceiverId = -1;
                chatPane.setVisible(false);
                chatHeader.setText("Select a conversation");
            }
        });

        // Custom cell factory for message list (left/right alignment)
        messageList.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> listView) {
                return new ListCell<Message>() {
                    @Override
                    protected void updateItem(Message msg, boolean empty) {
                        super.updateItem(msg, empty);
                        if (empty || msg == null) {
                            setGraphic(null);
                            return;
                        }

                        boolean isMe = msg.getSenderId() == currentUser.getId();

                        Label textLabel = new Label(msg.getMessageText());
                        textLabel.setWrapText(true);
                        textLabel.setMaxWidth(300);
                        textLabel.setPadding(new Insets(8, 12, 8, 12));
                        textLabel.setStyle("-fx-background-color: " + (isMe ? "#DCF8C6" : "#FFFFFF") + "; " +
                                "-fx-background-radius: 10; -fx-border-radius: 10; " +
                                "-fx-border-color: #DDDDDD;");

                        HBox wrapper = new HBox(textLabel);
                        wrapper.setAlignment(isMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                        wrapper.setPadding(new Insets(5, 10, 5, 10));

                        setGraphic(wrapper);
                        setText(null);
                    }
                };
            }
        });
    }

    private void refreshAll() {
        loadConversations();
        if (selectedReceiverId != -1) {
            loadMessages();
        }
    }

    private void loadConversations() {
        try {
            List<String> partners = messageService.getConversationPartners(currentUser.getId());
            conversations.clear(); // No duplicates
            conversations.addAll(partners);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMessages() {
        if (selectedReceiverId == -1) {
            chatPane.setVisible(false);
            return;
        }
        chatPane.setVisible(true);

        try {
            List<Message> msgs = messageService.getConversation(currentUser.getId(), selectedReceiverId);
            messageList.setItems(FXCollections.observableArrayList(msgs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSend() {
        String text = messageInput.getText().trim();
        if (text.isEmpty() || selectedReceiverId == -1) return;

        try {
            Message msg = new Message();
            msg.setSenderId(currentUser.getId());
            msg.setReceiverId(selectedReceiverId);
            msg.setItemId(selectedItemId);
            msg.setMessageText(text);

            messageService.sendMessage(msg);
            messageInput.clear();
            loadMessages(); // Reload immediately
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to send: " + e.getMessage()).showAndWait();
        }
    }

    public void openChatWith(int receiverId, String receiverUsername, Integer itemId) {
        this.selectedReceiverId = receiverId;
        this.selectedItemId = itemId;
        conversationList.getSelectionModel().clearSelection();
        chatHeader.setText("Chat with " + receiverUsername);
        loadMessages();
        chatPane.setVisible(true);
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> refreshAll());
            }
        }, 3000, 3000);
    }

    public void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
    }
}