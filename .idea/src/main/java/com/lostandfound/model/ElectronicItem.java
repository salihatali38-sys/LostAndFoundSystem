package com.lostandfound.model;

public class ElectronicItem extends Item {
    private String serialNumber;

    public ElectronicItem(String id, String description, String location, String serialNumber) {
        super(id, description, location);
        this.serialNumber = serialNumber;
    }

    @Override
    public double calculateMatchScore(Item other) {
        if (!(other instanceof ElectronicItem)) return 0.0;
        ElectronicItem otherElec = (ElectronicItem) other;

        // Strict matching by serial number
        if (this.serialNumber.equalsIgnoreCase(otherElec.serialNumber)) return 1.0;
        return 0.0;
    }

    @Override
    public String toFileString() {
        return String.format("ELECTRONIC,%s,%s,%s,%s", getId(), getDescription(), getLocation(), serialNumber);
    }
}