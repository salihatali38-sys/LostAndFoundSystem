package lostfound.service;

import lostfound.model.Message;
import lostfound.repository.MessageRepository;

import java.sql.SQLException;
import java.util.List;

public class MessageService {

    private final MessageRepository repo = new MessageRepository();

    public void sendMessage(Message msg) throws SQLException {
        repo.sendMessage(msg);
    }

    public List<Message> getConversation(int userId1, int userId2) throws SQLException {
        return repo.getConversation(userId1, userId2);
    }

    public List<String> getConversationPartners(int userId) throws SQLException {
        return repo.getConversationPartners(userId);
    }
}