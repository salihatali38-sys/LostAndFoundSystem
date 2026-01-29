package com.lostandfound.model;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, "ADMIN");
    }

    @Override
    public void displayPermissions() {
        System.out.println("Admin Permissions: Delete reports, Verify matches, Manage users.");
    }
}