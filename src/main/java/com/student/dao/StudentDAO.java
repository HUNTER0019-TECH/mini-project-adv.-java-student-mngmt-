package com.student.dao;

import com.student.database.DatabaseConnection;
import com.student.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * StudentDAO handles all persistence operations for Student records.
 * Implements BaseDAO<Student> and provides search, counting, and aggregation queries.
 */
public class StudentDAO implements BaseDAO<Student> {

    @Override
    public boolean add(Student student) {
        String sql = "INSERT INTO students (student_id, full_name, email, phone, course, gender, dob, address) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId().trim().toUpperCase());
            pstmt.setString(2, student.getName().trim());
            pstmt.setString(3, student.getEmail().trim().toLowerCase());
            pstmt.setString(4, student.getPhone().trim());
            pstmt.setString(5, student.getCourse().trim());
            pstmt.setString(6, student.getGender().trim());
            pstmt.setString(7, student.getDob().trim());
            pstmt.setString(8, student.getAddress().trim());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error adding student: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Student student) {
        String sql = "UPDATE students SET full_name = ?, email = ?, phone = ?, course = ?, gender = ?, dob = ?, address = ? "
                   + "WHERE student_id = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName().trim());
            pstmt.setString(2, student.getEmail().trim().toLowerCase());
            pstmt.setString(3, student.getPhone().trim());
            pstmt.setString(4, student.getCourse().trim());
            pstmt.setString(5, student.getGender().trim());
            pstmt.setString(6, student.getDob().trim());
            pstmt.setString(7, student.getAddress().trim());
            pstmt.setString(8, student.getStudentId().trim().toUpperCase());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error updating student: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Object key) {
        String sql = "DELETE FROM students WHERE UPPER(student_id) = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, key.toString().trim().toUpperCase());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error deleting student: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Student getById(Object key) {
        String sql = "SELECT id, student_id, full_name, email, phone, course, gender, dob, address, created_at "
                   + "FROM students WHERE UPPER(student_id) = ?;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, key.toString().trim().toUpperCase());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStudent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error getting student by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Student> getAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, student_id, full_name, email, phone, course, gender, dob, address, created_at "
                   + "FROM students ORDER BY id DESC;";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error getting all students: " + e.getMessage());
        }
        return list;
    }

    /**
     * Searches students by ID, Name, or Course keyword.
     * @param keyword search phrase
     * @return list of matching students
     */
    public List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
        String sql = "SELECT id, student_id, full_name, email, phone, course, gender, dob, address, created_at "
                   + "FROM students WHERE LOWER(student_id) LIKE ? OR LOWER(full_name) LIKE ? OR LOWER(course) LIKE ? "
                   + "ORDER BY id DESC;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToStudent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error searching students: " + e.getMessage());
        }
        return list;
    }

    /**
     * Checks if a student with the given student ID already exists.
     * @param studentId unique student ID
     * @return true if already exists
     */
    public boolean isStudentIdExists(String studentId) {
        String sql = "SELECT COUNT(*) FROM students WHERE UPPER(student_id) = ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId.trim().toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error checking studentId: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns total count of students registered.
     * @return count
     */
    public int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM students;";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error getting student count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Returns count of students grouped by course for dashboard metrics.
     * @return Map of CourseCode -> StudentCount
     */
    public Map<String, Integer> getCourseDistribution() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT course, COUNT(*) as cnt FROM students GROUP BY course ORDER BY cnt DESC;";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                map.put(rs.getString("course"), rs.getInt("cnt"));
            }
        } catch (SQLException e) {
            System.err.println("[StudentDAO] Error getting course distribution: " + e.getMessage());
        }
        return map;
    }

    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("id"),
            rs.getString("student_id"),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("course"),
            rs.getString("gender"),
            rs.getString("dob"),
            rs.getString("address"),
            rs.getString("created_at")
        );
    }
}
