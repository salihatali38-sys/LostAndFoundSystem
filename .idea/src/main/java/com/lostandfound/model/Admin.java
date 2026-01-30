package com.lostandfound.model;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public void displayPermissions() {
        System.out.println("Admin Permissions:");
        System.out.println("- View all lost/found items");
        System.out.println("- Approve matches");
        System.out.println("- Manage users");
        System.out.println("- Delete inappropriate items");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + getUsername() + '\'' +
                '}';
    }
}