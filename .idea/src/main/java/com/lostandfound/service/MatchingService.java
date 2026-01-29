package com.lostandfound.service;

import com.lostandfound.model.Item;

public class MatchingService {
    public void processMatch(Item lost, Item found) {
        double score = lost.calculateMatchScore(found);

        if (score > 0.7) {
            System.out.println("[MATCH FOUND] High confidence match for Item ID: " + lost.getId());
        } else {
            System.out.println("[NO MATCH] No significant match found.");
        }
    }
}
