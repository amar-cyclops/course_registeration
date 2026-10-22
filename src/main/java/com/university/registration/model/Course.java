package com.university.registration.model;

public class Course {
    private int id;
    private String courseCode;
    private String title;
    private String description;
    private int creditHours;
    private String instructor;
    private int totalSeats;
    private int availableSeats;
    private String prerequisites; // comma-separated course codes, may be empty
    private String status;        // "Active" or "Inactive"

    public Course() {
    }

    public Course(int id, String courseCode, String title, String description, int creditHours,
                  String instructor, int totalSeats, int availableSeats, String prerequisites, String status) {
        this.id = id;
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.creditHours = creditHours;
        this.instructor = instructor;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.prerequisites = prerequisites;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
