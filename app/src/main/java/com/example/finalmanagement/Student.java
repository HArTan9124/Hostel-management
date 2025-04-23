package com.example.finalmanagement;

public class Student {
    private String name;
    private boolean isPresent;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String name) {
        this.name = name;
        this.isPresent = false;
    }

    public String getName() {
        return name;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public void setName(String name) {
        this.name = name;
    }
}
