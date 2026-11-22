package com.student.models;

import java.io.Serializable;

/**
 * Course model class representing academic courses offered by the college.
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String courseCode;    // e.g. "BCA-101"
    private String courseName;    // e.g. "Bachelor of Computer Applications"
    private String duration;      // e.g. "3 Years"
    private String department;    // e.g. "Computer Applications"

    public Course() {
    }

    public Course(int id, String courseCode, String courseName, String duration, String department) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.duration = duration;
        this.department = department;
    }

    public Course(String courseCode, String courseName, String duration, String department) {
        this(0, courseCode, courseName, duration, department);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}
