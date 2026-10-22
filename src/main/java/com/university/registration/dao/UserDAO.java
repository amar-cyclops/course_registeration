package com.university.registration.dao;

import com.university.registration.model.User;
import com.university.registration.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking email", e);
        }
    }

    public boolean studentIdExists(String studentId) {
        String sql = "SELECT 1 FROM users WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking student ID", e);
        }
    }

    public void createStudent(String name, String email, String plainPassword, String studentId) {
        String sql = "INSERT INTO users (name, email, password, role, student_id) VALUES (?, ?, ?, 'STUDENT', ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.hash(plainPassword));
            ps.setString(4, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating student account", e);
        }
    }

    public User authenticate(String email, String plainPassword) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (storedHash.equals(PasswordUtil.hash(plainPassword))) {
                        return mapRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error authenticating user", e);
        }
        return null;
    }

    public User getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user with id " + id, e);
        }
        return null;
    }

    public void updateProfile(int id, String name, String email, String newPasswordOrNull) {
        String sql;
        boolean changingPassword = newPasswordOrNull != null && !newPasswordOrNull.trim().isEmpty();

        if (changingPassword) {
            sql = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
        } else {
            sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            if (changingPassword) {
                ps.setString(3, PasswordUtil.hash(newPasswordOrNull));
                ps.setInt(4, id);
            } else {
                ps.setInt(3, id);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile for user " + id, e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getString("student_id")
        );
    }
}
