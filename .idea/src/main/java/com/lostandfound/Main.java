package com.lostandfound;

// These imports are the "bridge" to your other folders
import com.lostandfound.model.*;
import com.lostandfound.service.*;
import com.lostandfound.repository.FileRepository;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Setup the basic pieces
        FileRepository repo = new FileRepository();
        MatchingService matcher = new MatchingService();

        // 2. Use Member A's User Roles
        User currentUser = new Student("salum_student", "password123");
        System.out.println("System Started. Logged in as: " + currentUser.getUsername());

        // 3. Create items using the Logic from your PDF
        Item lost = new Phone("P1", "Black Samsung", "Library", "Samsung");
        Item found = new Phone("P2", "Galaxy Phone", "Library", "Samsung");

        // 4. Run the Automatic Matching logic
        matcher.processMatch(lost, found);

        // 5. Save everything to the database (I/O Requirement)
        List<Item> items = new ArrayList<>();
        items.add(lost);
        items.add(found);
        repo.saveAllItems(items);
    }
}