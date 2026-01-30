package com.lostandfound.model;

public class Student extends User {
    public Student(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public void displayPermissions() {
        System.out.println("Student Permissions:");
        System.out.println("- Report lost items");
        System.out.println("- Report found items");
        System.out.println("- View own items");
        System.out.println("- Receive match notifications");
    }

    @Override
    public String toString() {
        return "Student{" +
                "username='" + getUsername() + '\'' +
                '}';
    }
}