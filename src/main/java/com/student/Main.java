package com.student;

import com.student.database.DatabaseConnection;
import com.student.ui.LoginFrame;

import javax.swing.*;

/**
 * Main application entry point for the Student Management System.
 * - Initializes SQLite database schema and sample data.
 * - Sets the platform Look and Feel.
 * - Launches the LoginFrame on the Swing Event Dispatch Thread (EDT).
 */
public class Main {

    public static void main(String[] args) {
        // Set System Look and Feel for native OS aesthetics
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Using default Swing Look and Feel: " + e.getMessage());
        }

        // Initialize SQLite Database (creates tables and seeds sample data if needed)
        try {
            DatabaseConnection.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Fatal: Could not initialize database: " + e.getMessage());
            e.printStackTrace();
        }

        // Launch application on Swing Event Dispatch Thread (Thread safety)
        SwingUtilities.invokeLater(() -> {
            try {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                System.out.println("=================================================");
                System.out.println(" Student Management System is Running!");
                System.out.println(" Default Login: admin / admin123");
                System.out.println(" Database: student_management.db (SQLite)");
                System.out.println("=================================================");
            } catch (Exception e) {
                System.err.println("Failed to launch UI: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
