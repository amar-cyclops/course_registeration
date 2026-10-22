package com.university.registration.model;

public class RegistrationResult {
    private String courseCode;
    private String courseTitle;
    private boolean success;
    private String message;

    public RegistrationResult(String courseCode, String courseTitle, boolean success, String message) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.success = success;
        this.message = message;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
