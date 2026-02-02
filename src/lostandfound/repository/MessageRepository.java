package lostfound.repository;

import lostfound.model.Message;
import lostfound.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository {

    public void sendMessage(Message msg) throws SQLException {
        String sql = """
            INSERT INTO messages (sender_id, receiver_id, item_id, message_text, sent_at, is_read)
            VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, 0)
            """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, msg.getSenderId());
            ps.setInt(2, msg.getReceiverId());
            if (msg.getItemId() != null) {
                ps.setInt(3, msg.getItemId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, msg.getMessageText());
            ps.executeUpdate();
        }
    }

    public List<Message> getConversation(int userId1, int userId2) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String sql = """
            SELECT m.*,
                   s.username AS sender_username,
                   r.username AS receiver_username
            FROM messages m
            JOIN users s ON m.sender_id = s.id
            JOIN users r ON m.receiver_id = r.id
            WHERE (m.sender_id = ? AND m.receiver_id = ?)
               OR (m.sender_id = ? AND m.receiver_id = ?)
            ORDER BY m.sent_at ASC
            """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message msg = new Message();
                    msg.setId(rs.getInt("id"));
                    msg.setSenderId(rs.getInt("sender_id"));
                    msg.setSenderUsername(rs.getString("sender_username"));
                    msg.setReceiverId(rs.getInt("receiver_id"));
                    msg.setReceiverUsername(rs.getString("receiver_username"));
                    msg.setItemId(rs.getInt("item_id") != 0 ? rs.getInt("item_id") : null);
                    msg.setMessageText(rs.getString("message_text"));
                    msg.setSentAt(rs.getString("sent_at"));
                    msg.setRead(rs.getBoolean("is_read"));
                    messages.add(msg);
                }
            }
        }
        return messages;
    }

    public List<String> getConversationPartners(int userId) throws SQLException {
        List<String> partners = new ArrayList<>();
        String sql = """
            SELECT DISTINCT 
                CASE WHEN m.sender_id = ? THEN m.receiver_id ELSE m.sender_id END AS partner_id,
                CASE WHEN m.sender_id = ? THEN r.username ELSE s.username END AS partner_username
            FROM messages m
            JOIN users s ON m.sender_id = s.id
            JOIN users r ON m.receiver_id = r.id
            WHERE m.sender_id = ? OR m.receiver_id = ?
            """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int partnerId = rs.getInt("partner_id");
                    String username = rs.getString("partner_username");
                    if (username != null && !username.trim().isEmpty()) {
                        partners.add(username + " (ID: " + partnerId + ")");
                    }
                }
            }
        }
        return partners;
    }
}