package lostfound.service;

import lostfound.model.Item;
import lostfound.repository.ItemRepository;

import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private final ItemRepository repo = new ItemRepository();

    public void addItem(Item item, int userId) throws SQLException {
        repo.addItem(item, userId);
    }

    public void updateItem(Item item, int currentUserId) throws SQLException {
        repo.updateItem(item, currentUserId);
    }

    public void deleteItem(int itemId, int currentUserId) throws SQLException {
        repo.deleteItem(itemId, currentUserId);
    }

    public List<Item> getAllItems() throws SQLException {
        return repo.getAllItems();
    }

    public boolean canEditOrDelete(Item item, int currentUserId) throws SQLException {
        return repo.isOwnerOrAdmin(item.getId(), currentUserId);
    }
}
