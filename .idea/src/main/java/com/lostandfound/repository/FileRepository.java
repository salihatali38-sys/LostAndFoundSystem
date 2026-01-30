package com.lostfound.repository;

import com.lostandfound.model.item;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    private static final String FILE_NAME = "database.txt";

    /**
     * Saves all items to file using their toFileString() representation
     * @param items List of items (any subclass of Item)
     * @throws IOException if file I/O fails
     */
    public void saveAllItems(List<? extends Item> items) throws IOException {
        // Input validation
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Item item : items) {
                if (item != null) {
                    writer.write(item.toFileString());
                    writer.newLine();
                }
            }
            System.out.println("✓ Items saved successfully to " + FILE_NAME);
        } catch (FileNotFoundException e) {
            System.err.println("❌ File not found: " + FILE_NAME);
            throw new IOException("Cannot create file: " + FILE_NAME, e);
        } catch (IOException e) {
            System.err.println("❌ I/O error while saving items");
            throw e;
        }
    }

    /**
     * Loads all items from file (placeholder - will implement later)
     * @return empty list for now (to be implemented after basic system works)
     */
    public List<Item> loadItems() {
        // TODO: Implement proper deserialization in next step
        System.out.println("⚠ loadItems() not yet implemented - returning empty list");
        return new ArrayList<>();
    }
}