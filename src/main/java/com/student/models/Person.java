package com.student.models;

import java.io.Serializable;

/**
 * Abstract Person class representing common attributes of individuals in the institution.
 * Demonstrates OOP principles:
 * - Abstraction (cannot be instantiated directly)
 * - Encapsulation (private fields accessed via getters and setters)
 * - Inheritance (extended by Student class)
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;             // Primary key in DB
    protected String name;        // Full Name
    protected String email;       // Email Address
    protected String phone;       // Contact Number
    protected String gender;      // Gender (Male, Female, Other)
    protected String address;     // Residential Address

    public Person() {
    }

    public Person(int id, String name, String email, String phone, String gender, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
    }

    public Person(String name, String email, String phone, String gender, String address) {
        this(0, name, email, phone, gender, address);
    }

    // Abstract method demonstrating Polymorphism in subclasses
    public abstract String getRoleDescription();

    // Getters and Setters (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
