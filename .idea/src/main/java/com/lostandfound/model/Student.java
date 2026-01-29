package com.lostandfound.model;

public class Student extends User {
    public Student(String username, String password) {
        super(username, password, "STUDENT");
    }

    @Override
    public void displayPermissions() {
        System.out.println("Student Permissions: Report lost/found items, View matches.");
    }
}