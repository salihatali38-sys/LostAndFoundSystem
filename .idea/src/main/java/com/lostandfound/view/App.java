package com.lostandfound;

import com.lostandfound.model.*;
import com.lostandfound.service.MatchingService;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        // Create reports
        Item lost = new Phone("P1", "Black Samsung", "Library", "Samsung");
        Item found = new Phone("P2", "Phone found", "Library", "Samsung");

        // Use the Matching Service (Business Logic)
        MatchingService service = new MatchingService();
        service.processMatch(lost, found);

        System.out.println("System initialized. OOP and SOLID rules applied.");
    }
}