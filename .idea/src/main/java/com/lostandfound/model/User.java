package com.lostandfound.model;

public abstract class User {
    private String username;
    private String password;
    private String role; // To distinguish between Admin and Student

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Encapsulation: Accessors for private fields
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Polymorphism: Each user type might have a different home screen
    public abstract void displayPermissions();
}