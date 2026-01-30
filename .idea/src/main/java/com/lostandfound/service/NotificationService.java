package com.lostandfound.service;

import com.lostandfound.model.User;
import com.lostandfound.model.Item;

public class NotificationService {

    // Method to notify a specific user about a match
    public void notifyUser(User user, Item lostItem, Item foundItem) {
        System.out.println("\n[NOTIFICATION SENT TO: " + user.getUsername() + "]");
        System.out.println("Alert: We found a potential match for your " + lostItem.getDescription() + "!");
        System.out.println("Matching Item Location: " + foundItem.getLocation());
    }

    // Method to log system-wide alerts (useful for Admins)
    public void logSystemAlert(String message) {
        System.out.println("[SYSTEM ALERT]: " + message);
    }
}
