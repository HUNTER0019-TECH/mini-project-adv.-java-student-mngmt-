package com.student.dao;

import com.student.database.DatabaseConnection;
import com.student.models.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * CourseDAO implements BaseDAO for Course entities.
 * Handles database queries for courses using JDBC PreparedStatements.
 */
public class CourseDAO implements BaseDAO<Course> {

    @Override
    public boolean add(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, duration, department) VALUES (?, ?, ?, ?);";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseCode().trim().toUpperCase());
            pstmt.setString(2, course.getCourseName().trim());
            pstmt.setString(3, course.getDuration().trim());
            pstmt.setString(4, course.getDepartment() != null ? course.getDepartment().trim() : "");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error adding course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Course course) {
        String sql = "UPDATE courses SET course_name = ?, duration = ?, department = ? WHERE course_code = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseName().trim());
            pstmt.setString(2, course.getDuration().trim());
            pstmt.setString(3, course.getDepartment() != null ? course.getDepartment().trim() : "");
            pstmt.setString(4, course.getCourseCode().trim().toUpperCase());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error updating course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Object key) {
        String sql;
        if (key instanceof Integer) {
            sql = "DELETE FROM courses WHERE id = ?;";
        } else {
            sql = "DELETE FROM courses WHERE course_code = ?;";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (key instanceof Integer) {
                pstmt.setInt(1, (Integer) key);
            } else {
                pstmt.setString(1, key.toString().trim().toUpperCase());
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error deleting course: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Course getById(Object key) {
        String sql = (key instanceof Integer) ?
                "SELECT id, course_code, course_name, duration, department FROM courses WHERE id = ?;" :
                "SELECT id, course_code, course_name, duration, department FROM courses WHERE course_code = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (key instanceof Integer) {
                pstmt.setInt(1, (Integer) key);
            } else {
                pstmt.setString(1, key.toString().trim().toUpperCase());
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                        rs.getInt("id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("duration"),
                        rs.getString("department")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error retrieving course: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Course> getAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT id, course_code, course_name, duration, department FROM courses ORDER BY course_code ASC;";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Course(
                    rs.getInt("id"),
                    rs.getString("course_code"),
                    rs.getString("course_name"),
                    rs.getString("duration"),
                    rs.getString("department")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error retrieving all courses: " + e.getMessage());
        }
        return list;
    }

    /**
     * Checks if a course with given code already exists.
     * @param courseCode course code (e.g. BCA-101)
     * @return true if exists
     */
    public boolean isCourseCodeExists(String courseCode) {
        String sql = "SELECT COUNT(*) FROM courses WHERE UPPER(course_code) = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode.trim().toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error checking course existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns total count of registered courses.
     * @return count
     */
    public int getCourseCount() {
        String sql = "SELECT COUNT(*) FROM courses;";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error getting course count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Retrieves unique department names count.
     */
    public int getDepartmentCount() {
        String sql = "SELECT COUNT(DISTINCT department) FROM courses WHERE department IS NOT NULL AND department != '';";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error getting department count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Retrieves all course codes for selection in dropdowns.
     * @return list of course codes
     */
    public List<String> getAllCourseCodes() {
        List<String> codes = new ArrayList<>();
        String sql = "SELECT course_code FROM courses ORDER BY course_code ASC;";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                codes.add(rs.getString("course_code"));
            }
        } catch (SQLException e) {
            System.err.println("[CourseDAO] Error getting course codes: " + e.getMessage());
        }
        return codes;
    }
}
