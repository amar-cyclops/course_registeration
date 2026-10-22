package com.university.registration.dao;

import com.university.registration.model.Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {

    public void insert(int studentUserId, int courseId, String status, String reason) {
        String sql = "INSERT INTO registrations (student_user_id, course_id, status, reason) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentUserId);
            ps.setInt(2, courseId);
            ps.setString(3, status);
            ps.setString(4, reason);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving registration", e);
        }
    }

    /**
     * Used for prerequisite checks: has this student successfully registered
     * for a course with the given course code?
     */
    public boolean hasActiveRegistrationForCourseCode(int studentUserId, String courseCode) {
        String sql = "SELECT 1 FROM registrations r JOIN courses c ON r.course_id = c.id " +
                     "WHERE r.student_user_id = ? AND c.course_code = ? AND r.status = 'REGISTERED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentUserId);
            ps.setString(2, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking prerequisite registration", e);
        }
    }

    public List<Registration> getRegisteredCoursesForStudent(int studentUserId) {
        List<Registration> results = new ArrayList<>();
        String sql = "SELECT r.*, c.course_code, c.title AS course_title, c.credit_hours, c.instructor " +
                     "FROM registrations r JOIN courses c ON r.course_id = c.id " +
                     "WHERE r.student_user_id = ? AND r.status = 'REGISTERED' " +
                     "ORDER BY r.registered_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Registration r = new Registration();
                    r.setId(rs.getInt("id"));
                    r.setStudentUserId(rs.getInt("student_user_id"));
                    r.setCourseId(rs.getInt("course_id"));
                    r.setStatus(rs.getString("status"));
                    r.setReason(rs.getString("reason"));
                    r.setRegisteredAt(rs.getString("registered_at"));
                    r.setCourseCode(rs.getString("course_code"));
                    r.setCourseTitle(rs.getString("course_title"));
                    r.setCreditHours(rs.getInt("credit_hours"));
                    r.setInstructor(rs.getString("instructor"));
                    results.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching registered courses", e);
        }
        return results;
    }
}
