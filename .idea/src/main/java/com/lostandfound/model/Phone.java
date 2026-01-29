package com.lostandfound.model;

public class Phone extends Item {
    private String brand;

    public Phone(String id, String description, String location, String brand) {
        super(id, description, location);
        this.brand = brand;
    }

    @Override
    public double calculateMatchScore(Item other) {
        if (!(other instanceof Phone)) return 0.0;
        Phone otherPhone = (Phone) other;
        double score = 0;
        // Logic from your "Untitled" document: match by brand and location
        if (this.brand.equalsIgnoreCase(otherPhone.brand)) score += 0.5;
        if (this.getLocation().equalsIgnoreCase(otherPhone.getLocation())) score += 0.5;
        return score;
    }

    @Override
    public String toFileString() {
        return String.format("PHONE,%s,%s,%s,%s", getId(), getDescription(), getLocation(), brand);
    }
}