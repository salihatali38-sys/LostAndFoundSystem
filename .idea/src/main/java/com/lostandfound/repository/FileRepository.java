package com.lostandfound.repository;

import com.lostandfound.model.Item;
import java.io.*;
import java.util.List;

public class FileRepository {
    private final String FILE_NAME = "database.txt";

    // This is the method your Main class is currently missing!
    public void saveAllItems(List<Item> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Item item : items) {
                // We'll write a simple string representation for now
                writer.write(item.getDescription() + "," + item.getLocation());
                writer.newLine();
            }
            System.out.println("Successfully saved items to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }
}