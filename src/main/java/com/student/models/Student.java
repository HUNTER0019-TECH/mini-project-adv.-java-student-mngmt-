package com.student.models;

/**
 * Student model class representing a student entity in the institution.
 * Inherits from Person to demonstrate OOP Inheritance and Polymorphism.
 */
public class Student extends Person {
    private static final long serialVersionUID = 1L;

    private String studentId;     // Unique College ID (e.g. STU1001)
    private String course;        // Enrolled Course code / name (e.g. BCA)
    private String dob;           // Date of Birth (YYYY-MM-DD)
    private String createdAt;     // Timestamp of record creation

    public Student() {
        super();
    }

    public Student(int id, String studentId, String name, String email, String phone,
                   String course, String gender, String dob, String address, String createdAt) {
        super(id, name, email, phone, gender, address);
        this.studentId = studentId;
        this.course = course;
        this.dob = dob;
        this.createdAt = createdAt;
    }

    public Student(String studentId, String name, String email, String phone,
                   String course, String gender, String dob, String address) {
        super(name, email, phone, gender, address);
        this.studentId = studentId;
        this.course = course;
        this.dob = dob;
    }

    // Overriding abstract method from Person (Polymorphism)
    @Override
    public String getRoleDescription() {
        return "Student enrolled in course: " + (course != null ? course : "Not Assigned");
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
