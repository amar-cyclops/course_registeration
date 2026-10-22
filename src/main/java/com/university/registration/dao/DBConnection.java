package com.university.registration.dao;

import com.university.registration.util.PasswordUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:h2:./course_registration_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
            initDatabase();
            seedData();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void initDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(150) NOT NULL, " +
                "email VARCHAR(150) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "role VARCHAR(20) NOT NULL, " +
                "student_id VARCHAR(50) UNIQUE" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS courses (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "course_code VARCHAR(20) NOT NULL UNIQUE, " +
                "title VARCHAR(200) NOT NULL, " +
                "description VARCHAR(1000), " +
                "credit_hours INT NOT NULL, " +
                "instructor VARCHAR(150), " +
                "total_seats INT NOT NULL, " +
                "available_seats INT NOT NULL, " +
                "prerequisites VARCHAR(255), " +
                "status VARCHAR(20) NOT NULL DEFAULT 'Active'" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS registrations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "student_user_id INT NOT NULL, " +
                "course_id INT NOT NULL, " +
                "status VARCHAR(20) NOT NULL, " +
                "reason VARCHAR(255), " +
                "registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (student_user_id) REFERENCES users(id), " +
                "FOREIGN KEY (course_id) REFERENCES courses(id)" +
                ")"
            );
        }
    }

    private static void seedData() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM users");
            rs.next();
            if (rs.getInt("total") == 0) {
                stmt.execute(
                    "INSERT INTO users (name, email, password, role, student_id) VALUES (" +
                    "'System Admin', 'admin@university.edu', '" + PasswordUtil.hash("admin123") + "', 'ADMIN', NULL)"
                );
            }

            rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM courses");
            rs.next();
            if (rs.getInt("total") == 0) {
                stmt.execute("INSERT INTO courses (course_code, title, description, credit_hours, instructor, total_seats, available_seats, prerequisites, status) VALUES " +
                    "('CS101', 'Introduction to Programming', 'Fundamentals of programming using Java.', 3, 'Dr. A. Reyes', 30, 30, NULL, 'Active')");
                stmt.execute("INSERT INTO courses (course_code, title, description, credit_hours, instructor, total_seats, available_seats, prerequisites, status) VALUES " +
                    "('CS201', 'Data Structures', 'Arrays, lists, trees, graphs, and algorithm analysis.', 4, 'Dr. B. Chen', 25, 25, 'CS101', 'Active')");
                stmt.execute("INSERT INTO courses (course_code, title, description, credit_hours, instructor, total_seats, available_seats, prerequisites, status) VALUES " +
                    "('CS301', 'Database Systems', 'Relational databases, SQL, and schema design.', 3, 'Dr. C. Osei', 2, 2, 'CS201', 'Active')");
                stmt.execute("INSERT INTO courses (course_code, title, description, credit_hours, instructor, total_seats, available_seats, prerequisites, status) VALUES " +
                    "('MATH150', 'Calculus I', 'Limits, derivatives, and integrals.', 4, 'Dr. D. Lindqvist', 40, 40, NULL, 'Active')");
                stmt.execute("INSERT INTO courses (course_code, title, description, credit_hours, instructor, total_seats, available_seats, prerequisites, status) VALUES " +
                    "('ENG105', 'Academic Writing', 'Composition and research writing skills.', 2, 'Prof. E. Whitfield', 0, 0, NULL, 'Active')");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
