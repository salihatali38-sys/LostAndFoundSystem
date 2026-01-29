package com.lostandfound.model;

    public abstract class Item {
        private String id;
        private String description;
        private String location;

        public Item(String id, String description, String location) {
            this.id = id;
            this.description = description;
            this.location = location;
        }

        // Getters for Encapsulation
        public String getId() { return id; }
        public String getDescription() { return description; }
        public String getLocation() { return location; }

        // Polymorphism: Each item type calculates its match differently
        public abstract double calculateMatchScore(Item other);

        // For File I/O requirement
        public abstract String toFileString();
    }

