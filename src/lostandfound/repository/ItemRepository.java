package lostfound.repository;

import lostfound.model.Item;
import lostfound.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    public void addItem(Item item, int userId) throws SQLException {
        String sql = "INSERT INTO items (name, type, description, posted_by) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getType());
            ps.setString(3, item.getDescription());
            ps.setInt(4, userId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
        }
    }

    public void updateItem(Item item, int currentUserId) throws SQLException {
        if (!isOwnerOrAdmin(item.getId(), currentUserId)) {
            throw new SQLException("You can only edit your own items.");
        }
        String sql = "UPDATE items SET name=?, type=?, description=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getName());
            ps.setString(2, item.getType());
            ps.setString(3, item.getDescription());
            ps.setInt(4, item.getId());
            ps.executeUpdate();
        }
    }

    public void deleteItem(int itemId, int currentUserId) throws SQLException {
        if (!isOwnerOrAdmin(itemId, currentUserId)) {
            throw new SQLException("You can only delete your own items.");
        }
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = """
            SELECT i.*, u.username AS posted_by_username 
            FROM items i 
            JOIN users u ON i.posted_by = u.id 
            ORDER BY i.posted_at DESC
            """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp postedAtTs = rs.getTimestamp("posted_at");
                LocalDateTime postedAt = postedAtTs != null ? postedAtTs.toLocalDateTime() : null;

                Item item = new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("posted_by"),
                        rs.getString("posted_by_username"),
                        postedAt
                );
                items.add(item);
            }
        }
        return items;
    }

    public boolean isOwnerOrAdmin(int itemId, int userId) throws SQLException {
        String sql = """
            SELECT posted_by, u.role 
            FROM items i 
            JOIN users u ON i.posted_by = u.id 
            WHERE i.id = ?
            """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int ownerId = rs.getInt("posted_by");
                    String role = rs.getString("role");
                    return ownerId == userId || "ADMIN".equals(role);
                }
                return false;
            }
        }
    }
}
