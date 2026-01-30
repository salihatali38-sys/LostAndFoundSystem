package com.lostandfound.model;

public abstract class User {
    private String username;
    private String password;

    public User(String username, String password) {
        if (username == null || username.trim().length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Authentication method
    public boolean authenticate(String passwordInput) {
        return this.password.equals(passwordInput);
    }

    // Abstract method for role identification (replaces redundant 'role' field)
    public abstract String getRole();

    // Abstract method for permissions display
    public abstract void displayPermissions();

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}