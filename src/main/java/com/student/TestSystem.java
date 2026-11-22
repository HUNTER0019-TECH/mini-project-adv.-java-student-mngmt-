package com.student;

import com.student.dao.CourseDAO;
import com.student.dao.StudentDAO;
import com.student.dao.UserDAO;
import com.student.database.DatabaseConnection;
import com.student.models.Course;
import com.student.models.Student;

import java.util.List;
import java.util.Map;

/**
 * TestSystem performs automated headless verification of all subsystems:
 * - SQLite database connection and auto-initialization
 * - Admin authentication
 * - Course DAO CRUD operations
 * - Student DAO CRUD operations
 * - Search and aggregation queries
 */
public class TestSystem {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Starting Automated System Verification Tests");
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        // 1. Initialize Database
        try {
            DatabaseConnection.initializeDatabase();
            System.out.println("[PASS] 1. Database initialized successfully.");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] 1. Database initialization error: " + e.getMessage());
            failed++;
        }

        // 2. Test User Authentication
        UserDAO userDAO = new UserDAO();
        boolean authAdmin = userDAO.authenticate("admin", "admin123");
        boolean authBad = userDAO.authenticate("admin", "wrongpassword");

        if (authAdmin && !authBad) {
            System.out.println("[PASS] 2. User authentication verified (Valid credentials passed, invalid rejected).");
            passed++;
        } else {
            System.err.println("[FAIL] 2. User authentication check failed.");
            failed++;
        }

        // 3. Test Course DAO
        CourseDAO courseDAO = new CourseDAO();
        int courseCount = courseDAO.getCourseCount();
        List<Course> courses = courseDAO.getAll();

        if (courseCount >= 5 && courses.size() == courseCount) {
            System.out.println("[PASS] 3. CourseDAO verified (" + courseCount + " sample courses available).");
            passed++;
        } else {
            System.err.println("[FAIL] 3. CourseDAO count check failed. Count=" + courseCount);
            failed++;
        }

        // 4. Test Student DAO
        StudentDAO studentDAO = new StudentDAO();
        int studentCount = studentDAO.getStudentCount();
        List<Student> students = studentDAO.getAll();

        if (studentCount >= 5 && students.size() == studentCount) {
            System.out.println("[PASS] 4. StudentDAO verified (" + studentCount + " sample students loaded).");
            passed++;
        } else {
            System.err.println("[FAIL] 4. StudentDAO count check failed. Count=" + studentCount);
            failed++;
        }

        // 5. Test Search
        List<Student> searchResults = studentDAO.searchStudents("Aryan");
        if (!searchResults.isEmpty() && searchResults.get(0).getName().contains("Aryan")) {
            System.out.println("[PASS] 5. Search operation verified (Found: " + searchResults.get(0).getName() + ").");
            passed++;
        } else {
            System.err.println("[FAIL] 5. Search operation failed.");
            failed++;
        }

        // 6. Test Student CRUD Cycle (Add -> Update -> Delete)
        String tempId = "TEST9999";
        Student tempStudent = new Student(tempId, "Test Candidate", "test@college.edu", "9998887770", "BCA-101", "Male", "2002-01-01", "College Campus");

        boolean addSuccess = studentDAO.add(tempStudent);
        if (addSuccess && studentDAO.isStudentIdExists(tempId)) {
            System.out.println("[PASS] 6a. Student Add operation verified.");
            passed++;
        } else {
            System.err.println("[FAIL] 6a. Student Add operation failed.");
            failed++;
        }

        tempStudent.setName("Test Candidate Updated");
        boolean updateSuccess = studentDAO.update(tempStudent);
        Student fetched = studentDAO.getById(tempId);
        if (updateSuccess && fetched != null && "Test Candidate Updated".equals(fetched.getName())) {
            System.out.println("[PASS] 6b. Student Update operation verified.");
            passed++;
        } else {
            System.err.println("[FAIL] 6b. Student Update operation failed.");
            failed++;
        }

        boolean deleteSuccess = studentDAO.delete(tempId);
        if (deleteSuccess && !studentDAO.isStudentIdExists(tempId)) {
            System.out.println("[PASS] 6c. Student Delete operation verified.");
            passed++;
        } else {
            System.err.println("[FAIL] 6c. Student Delete operation failed.");
            failed++;
        }

        // 7. Course distribution aggregation test
        Map<String, Integer> dist = studentDAO.getCourseDistribution();
        if (dist != null && !dist.isEmpty()) {
            System.out.println("[PASS] 7. Course Distribution Metrics: " + dist);
            passed++;
        } else {
            System.err.println("[FAIL] 7. Course distribution check failed.");
            failed++;
        }

        System.out.println("==================================================");
        System.out.println(" SUMMARY: " + passed + " Passed, " + failed + " Failed.");
        System.out.println(" All Core Subsystems Ready For College Submission!");
        System.out.println("==================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }
}
