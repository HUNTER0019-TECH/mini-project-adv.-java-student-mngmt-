package com.student.dao;

import com.student.database.DatabaseConnection;
import com.student.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO handles authentication and user credential operations.
 */
public class UserDAO {

    /**
     * Authenticates user against credentials stored in SQLite.
     * Uses PreparedStatement to prevent SQL injection.
     *
     * @param username user login name
     * @param password user plain text password
     * @return true if authentication succeeds
     */
    public boolean authenticate(String username, String password) {
        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("[UserDAO] Authentication error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes password for an existing username.
     * @param username username
     * @param newPassword new password
     * @return true if updated
     */
    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UserDAO] Change password error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetches user details by username.
     * @param username username
     * @return User object or null
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[UserDAO] Error fetching user: " + e.getMessage());
        }
        return null;
    }
}
