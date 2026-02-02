package lostfound.service;

import lostfound.model.User;
import lostfound.util.DatabaseUtil;

import java.sql.*;

public class AuthService {

    public String register(String username, String password, String role) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "All fields are required.";
        }

        if (userExists(username)) {
            return "Username already exists.";
        }

        // Block any attempt to register as ADMIN
        if ("ADMIN".equals(role)) {
            return "Cannot register as ADMIN. Only one admin exists (pre-created).";
        }

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.trim());
            stmt.setString(2, password);
            stmt.setString(3, role);  // will always be "USER" from controller
            stmt.executeUpdate();
            return "SUCCESS";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Registration failed: " + e.getMessage();
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.trim());
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}