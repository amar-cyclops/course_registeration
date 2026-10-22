package com.university.registration.dao;

import com.university.registration.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                courses.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses", e);
        }
        return courses;
    }

    /**
     * Courses a given student can currently register for:
     * Active status, at least one open seat, and not already registered.
     */
    public List<Course> getAvailableCoursesForStudent(int studentUserId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses c WHERE c.status = 'Active' AND c.available_seats > 0 " +
                     "AND c.id NOT IN (SELECT r.course_id FROM registrations r " +
                     "WHERE r.student_user_id = ? AND r.status = 'REGISTERED') " +
                     "ORDER BY c.course_code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching available courses", e);
        }
        return courses;
    }

    public Course getById(int id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course with id " + id, e);
        }
        return null;
    }

    public Course getByCode(String courseCode) {
        String sql = "SELECT * FROM courses WHERE course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course with code " + courseCode, e);
        }
        return null;
    }

    public void addCourse(Course course) {
        String sql = "INSERT INTO courses (course_code, title, description, credit_hours, instructor, " +
                     "total_seats, available_seats, prerequisites, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getCourseCode());
            ps.setString(2, course.getTitle());
            ps.setString(3, course.getDescription());
            ps.setInt(4, course.getCreditHours());
            ps.setString(5, course.getInstructor());
            ps.setInt(6, course.getTotalSeats());
            ps.setInt(7, course.getAvailableSeats());
            ps.setString(8, course.getPrerequisites());
            ps.setString(9, course.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding course", e);
        }
    }

    public void updateCourse(Course course) {
        String sql = "UPDATE courses SET course_code = ?, title = ?, description = ?, credit_hours = ?, " +
                     "instructor = ?, total_seats = ?, available_seats = ?, prerequisites = ?, status = ? " +
                     "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getCourseCode());
            ps.setString(2, course.getTitle());
            ps.setString(3, course.getDescription());
            ps.setInt(4, course.getCreditHours());
            ps.setString(5, course.getInstructor());
            ps.setInt(6, course.getTotalSeats());
            ps.setInt(7, course.getAvailableSeats());
            ps.setString(8, course.getPrerequisites());
            ps.setString(9, course.getStatus());
            ps.setInt(10, course.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating course with id " + course.getId(), e);
        }
    }

    public void deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course with id " + id, e);
        }
    }

    /**
     * Atomically claims one seat on a course. Returns true if a seat was available and claimed,
     * false if the course had no seats left (e.g. another student took the last one).
     */
    public boolean tryClaimSeat(int courseId) {
        String sql = "UPDATE courses SET available_seats = available_seats - 1 " +
                     "WHERE id = ? AND available_seats > 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error claiming seat for course " + courseId, e);
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        return new Course(
            rs.getInt("id"),
            rs.getString("course_code"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("credit_hours"),
            rs.getString("instructor"),
            rs.getInt("total_seats"),
            rs.getInt("available_seats"),
            rs.getString("prerequisites"),
            rs.getString("status")
        );
    }
}
