package com.lostandfound.model;

public class DocumentItem extends Item {
    private String holderName;

    public DocumentItem(String id, String description, String location, String holderName) {
        super(id, description, location); // Must match the Item constructor exactly
        this.holderName = holderName;
    }

    @Override
    public double calculateMatchScore(Item other) {
        if (!(other instanceof DocumentItem)) return 0.0;
        DocumentItem otherDoc = (DocumentItem) other;

        // Logical matching: If names match, it's a very high confidence match
        if (this.holderName.equalsIgnoreCase(otherDoc.holderName)) return 1.0;
        return 0.0;
    }

    @Override
    public String toFileString() {
        return String.format("DOCUMENT,%s,%s,%s,%s", getId(), getDescription(), getLocation(), holderName);
    }
}