package com.lostandfound;

import com.lostandfound.model.Item.DocumentItem;
import com.lostandfound.model.Item.ElectronicItem;
import com.lostandfound.model.Item.Phone;
import com.lostandfound.model.User.Admin;
import com.lostandfound.model.User.Student;
import com.lostandfound.model.User.User;
import com.lostandfound.repository.FileRepository;
import com.lostandfound.service.MatchingService;
import com.lostandfound.service.NotificationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Setup
            FileRepository repo = new FileRepository();
            MatchingService matcher = new MatchingService();
            NotificationService notifier = new NotificationService();

            // Create users (FIXED: removed invalid named parameters)
            User currentUser = new Student("salum_student", "password123");
            User admin = new Admin("admin_user", "admin123");

            // Create items (FIXED: removed invalid named parameters)
            Phone lostPhone = new Phone("P1", "Black Samsung", "Library", "Samsung");
            Phone foundPhone = new Phone("P2", "Samsung black phone", "Library", "Samsung");

            ElectronicItem lostLaptop = new ElectronicItem("L1", "MacBook Pro 16GB", "Cafeteria", "Apple", "XYZ123");
            ElectronicItem foundLaptop = new ElectronicItem("L2", "Apple laptop", "Cafeteria", "Apple", "XYZ123");

            DocumentItem lostId = new DocumentItem("D1", "Student ID card", "Classroom", "Salum Mwita");
            DocumentItem foundId = new DocumentItem("D2", "ID card with name Salum", "Classroom", "Salum Mwita");

            // Store items
            List<Phone> phones = new ArrayList<>();
            phones.add(lostPhone);
            phones.add(foundPhone);

            List<ElectronicItem> electronics = new ArrayList<>();
            electronics.add(lostLaptop);
            electronics.add(foundLaptop);

            List<DocumentItem> documents = new ArrayList<>();
            documents.add(lostId);
            documents.add(foundId);

            // Run matching (Phone example)
            double phoneScore = matcher.calculateMatchScore(lostPhone, foundPhone);
            System.out.println("\n=== MATCHING RESULTS ===");
            System.out.println("Phone match score: " + phoneScore);
            if (phoneScore > 0.7) {
                System.out.println("[MATCH FOUND] High confidence match for phone!");
                notifier.notifyUser(currentUser, "Your lost phone might have been found!");
            } else {
                System.out.println("[NO MATCH] Low confidence for phone.");
            }

            // Save to file (with proper exception handling)
            List<Phone> allPhones = new ArrayList<>();
            allPhones.add(lostPhone);
            allPhones.add(foundPhone);

            repo.saveAllItems(allPhones);
            System.out.println("\n✓ Successfully saved items to database.txt");

        } catch (IOException e) {
            System.err.println("❌ I/O Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Application completed successfully ===");
    }
}