package lostfound.repository;

import lostfound.model.User;
import java.sql.SQLException;

public interface UserRepository {
    // Changed from Exception to SQLException
    User findByUsername(String username) throws SQLException;
}