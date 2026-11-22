package com.student.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnection handles SQLite database connectivity and initialization.
 * Automatically creates the database file and executes table schemas on first run.
 * Seeds initial admin user, sample courses, and sample students.
 */
public class DatabaseConnection {

    // Database file name stored in the working directory
    private static final String DB_FILE = "student_management.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    static {
        try {
            // Load SQLite JDBC Driver class
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found! Ensure sqlite-jdbc jar is in the classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a new connection to the SQLite database.
     * @return active Connection instance
     * @throws SQLException on connection error
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initializes the database tables and seeds sample data if needed.
     * Invoked at application startup.
     */
    public static void initializeDatabase() {
        System.out.println("[DatabaseConnection] Initializing database at: " + new File(DB_FILE).getAbsolutePath());

        // 1. Create tables
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "role TEXT DEFAULT 'ADMIN'"
                + ");";

        String createCoursesTable = "CREATE TABLE IF NOT EXISTS courses ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "course_code TEXT UNIQUE NOT NULL, "
                + "course_name TEXT NOT NULL, "
                + "duration TEXT NOT NULL, "
                + "department TEXT"
                + ");";

        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "student_id TEXT UNIQUE NOT NULL, "
                + "full_name TEXT NOT NULL, "
                + "email TEXT NOT NULL, "
                + "phone TEXT NOT NULL, "
                + "course TEXT NOT NULL, "
                + "gender TEXT NOT NULL, "
                + "dob TEXT NOT NULL, "
                + "address TEXT NOT NULL, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsersTable);
            stmt.execute(createCoursesTable);
            stmt.execute(createStudentsTable);

            // 2. Seed Default Admin if users table is empty
            seedDefaultAdmin(conn);

            // 3. Seed Sample Courses if courses table is empty
            seedSampleCourses(conn);

            // 4. Seed Sample Students if students table is empty
            seedSampleStudents(conn);

            System.out.println("[DatabaseConnection] Database tables and sample data verified successfully.");

        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Error during database initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedDefaultAdmin(Connection conn) throws SQLException {
        String checkUserSql = "SELECT COUNT(*) FROM users;";
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery(checkUserSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertAdminSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?);";
                try (PreparedStatement pstmt = conn.prepareStatement(insertAdminSql)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin123");
                    pstmt.setString(3, "ADMIN");
                    pstmt.executeUpdate();
                    System.out.println("[DatabaseConnection] Seeded default admin user (admin / admin123).");
                }
            }
        }
    }

    private static void seedSampleCourses(Connection conn) throws SQLException {
        String checkCourseSql = "SELECT COUNT(*) FROM courses;";
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery(checkCourseSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertCourseSql = "INSERT INTO courses (course_code, course_name, duration, department) VALUES (?, ?, ?, ?);";
                try (PreparedStatement pstmt = conn.prepareStatement(insertCourseSql)) {
                    String[][] sampleCourses = {
                        {"BCA-101", "Bachelor of Computer Applications (BCA)", "3 Years", "Computer Applications"},
                        {"BTECH-201", "B.Tech Computer Science & Engineering", "4 Years", "Engineering & Tech"},
                        {"MCA-301", "Master of Computer Applications (MCA)", "2 Years", "Computer Applications"},
                        {"BBA-401", "Bachelor of Business Administration (BBA)", "3 Years", "Management"},
                        {"BSCIT-501", "B.Sc Information Technology (IT)", "3 Years", "Information Technology"},
                        {"MBA-601", "Master of Business Administration (MBA)", "2 Years", "Business School"}
                    };

                    for (String[] course : sampleCourses) {
                        pstmt.setString(1, course[0]);
                        pstmt.setString(2, course[1]);
                        pstmt.setString(3, course[2]);
                        pstmt.setString(4, course[3]);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                    System.out.println("[DatabaseConnection] Seeded 6 sample courses.");
                }
            }
        }
    }

    private static void seedSampleStudents(Connection conn) throws SQLException {
        String checkStudentSql = "SELECT COUNT(*) FROM students;";
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery(checkStudentSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertStudentSql = "INSERT INTO students (student_id, full_name, email, phone, course, gender, dob, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
                try (PreparedStatement pstmt = conn.prepareStatement(insertStudentSql)) {
                    String[][] sampleStudents = {
                        {"STU1001", "Aryan Sharma", "aryan.sharma@college.edu", "9876543210", "BCA-101", "Male", "2004-05-15", "12 Park Avenue, New Delhi"},
                        {"STU1002", "Priya Patel", "priya.patel@college.edu", "9823456789", "BTECH-201", "Female", "2003-11-20", "45 Gandhi Road, Mumbai"},
                        {"STU1003", "Rahul Verma", "rahul.verma@college.edu", "9712345678", "MCA-301", "Male", "2001-08-10", "88 Civil Lines, Jaipur"},
                        {"STU1004", "Sneha Reddy", "sneha.reddy@college.edu", "9945678123", "BBA-401", "Female", "2004-02-28", "77 MG Road, Bengaluru"},
                        {"STU1005", "Amit Kumar", "amit.kumar@college.edu", "9632587410", "BSCIT-501", "Male", "2003-09-05", "14 Station Road, Lucknow"}
                    };

                    for (String[] stu : sampleStudents) {
                        pstmt.setString(1, stu[0]);
                        pstmt.setString(2, stu[1]);
                        pstmt.setString(3, stu[2]);
                        pstmt.setString(4, stu[3]);
                        pstmt.setString(5, stu[4]);
                        pstmt.setString(6, stu[5]);
                        pstmt.setString(7, stu[6]);
                        pstmt.setString(8, stu[7]);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                    System.out.println("[DatabaseConnection] Seeded 5 sample students.");
                }
            }
        }
    }

    /**
     * Safely closes an AutoCloseable resource (Connection, Statement, ResultSet).
     * @param closeable resource to close
     */
    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // Ignore or log debug
            }
        }
    }
}
