package com.student.ui;

import com.student.ui.theme.ModernButton;
import com.student.ui.theme.RoundedPanel;
import com.student.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * DashboardFrame is the principal application window.
 * Features:
 * - Dark Navy sidebar navigation with live active button state
 * - CardLayout to seamlessly switch between:
 *   * Dashboard (OverviewPanel)
 *   * Students (StudentPanel)
 *   * Courses (CoursePanel)
 *   * Project Info (AboutPanel)
 * - Header bar displaying section title, current date, and admin session status
 * - Secure logout functionality returning to LoginFrame
 */
public class DashboardFrame extends JFrame {

    private final String currentUsername;

    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // View Panels
    private OverviewPanel overviewPanel;
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private AboutPanel aboutPanel;

    // Sidebar navigation buttons
    private final Map<String, ModernButton> navButtons = new HashMap<>();
    private JLabel lblPageTitle;
    private JLabel lblCurrentDate;

    public DashboardFrame(String username) {
        this.currentUsername = (username != null && !username.isEmpty()) ? username : "Administrator";
        initComponents();
        showSection("DASHBOARD");
    }

    private void initComponents() {
        setTitle("Student Management System - College Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 740);
        setMinimumSize(new Dimension(1000, 640));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_CANVAS);
        setLayout(new BorderLayout());

        // 1. WEST: Dark Navy Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. CENTER: Main View Area (Header + CardLayout Body)
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(UITheme.BG_CANVAS);

        // Top Header
        JPanel headerBar = createHeaderBar();
        centerContainer.add(headerBar, BorderLayout.NORTH);

        // Body Panel with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UITheme.BG_CANVAS);

        overviewPanel = new OverviewPanel(this);
        studentPanel = new StudentPanel(this);
        coursePanel = new CoursePanel(this);
        aboutPanel = new AboutPanel();

        mainContentPanel.add(overviewPanel, "DASHBOARD");
        mainContentPanel.add(studentPanel, "STUDENTS");
        mainContentPanel.add(coursePanel, "COURSES");
        mainContentPanel.add(aboutPanel, "ABOUT");

        centerContainer.add(mainContentPanel, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UITheme.NAVY_DARK);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new EmptyBorder(24, 16, 24, 16));

        // Top Brand & User Profile
        JPanel topBrand = new JPanel();
        topBrand.setLayout(new BoxLayout(topBrand, BoxLayout.Y_AXIS));
        topBrand.setOpaque(false);

        JLabel lblLogo = new JLabel("🎓  StudentHub");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRole = new JLabel("College Admin Portal");
        lblRole.setFont(UITheme.FONT_SMALL);
        lblRole.setForeground(UITheme.ACCENT_CYAN);
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        topBrand.add(lblLogo);
        topBrand.add(Box.createRigidArea(new Dimension(0, 4)));
        topBrand.add(lblRole);
        topBrand.add(Box.createRigidArea(new Dimension(0, 24)));

        // User Badge Card
        RoundedPanel userCard = new RoundedPanel(10, UITheme.NAVY_SURFACE, null, 0);
        userCard.setLayout(new BorderLayout(10, 0));
        userCard.setBorder(new EmptyBorder(10, 12, 10, 12));
        userCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        userCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUserIcon = new JLabel("👤");
        lblUserIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));

        JPanel userText = new JPanel();
        userText.setLayout(new BoxLayout(userText, BoxLayout.Y_AXIS));
        userText.setOpaque(false);

        JLabel lblUserName = new JLabel(currentUsername);
        lblUserName.setFont(UITheme.FONT_LABEL);
        lblUserName.setForeground(Color.WHITE);

        JLabel lblUserStatus = new JLabel("● Online (Admin)");
        lblUserStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblUserStatus.setForeground(UITheme.COLOR_SUCCESS);

        userText.add(lblUserName);
        userText.add(lblUserStatus);

        userCard.add(lblUserIcon, BorderLayout.WEST);
        userCard.add(userText, BorderLayout.CENTER);

        topBrand.add(userCard);
        topBrand.add(Box.createRigidArea(new Dimension(0, 28)));

        // Navigation Items
        JPanel navMenu = new JPanel();
        navMenu.setLayout(new BoxLayout(navMenu, BoxLayout.Y_AXIS));
        navMenu.setOpaque(false);

        addNavButton(navMenu, "📊  Dashboard", "DASHBOARD");
        addNavButton(navMenu, "🎓  Students", "STUDENTS");
        addNavButton(navMenu, "📚  Courses", "COURSES");
        addNavButton(navMenu, "ℹ️  Project Info", "ABOUT");

        topBrand.add(navMenu);
        sidebar.add(topBrand, BorderLayout.NORTH);

        // Bottom Logout Button
        ModernButton btnLogout = new ModernButton("🚪  Sign Out", new Color(239, 68, 68, 40), UITheme.COLOR_DANGER, Color.WHITE, 8);
        btnLogout.setPreferredSize(new Dimension(200, 40));
        btnLogout.setFont(UITheme.FONT_BUTTON);
        btnLogout.addActionListener(e -> handleLogout());

        sidebar.add(btnLogout, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addNavButton(JPanel container, String text, String sectionKey) {
        ModernButton btn = new ModernButton(text, UITheme.NAVY_DARK, UITheme.NAVY_SURFACE, new Color(203, 213, 225), 8);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(UITheme.FONT_NAV_ITEM);
        btn.setBorder(new EmptyBorder(0, 14, 0, 14));

        btn.addActionListener(e -> showSection(sectionKey));

        navButtons.put(sectionKey, btn);
        container.add(btn);
        container.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private JPanel createHeaderBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
            new EmptyBorder(14, 30, 14, 30)
        ));

        lblPageTitle = new JLabel("Dashboard Overview");
        lblPageTitle.setFont(UITheme.FONT_HEADER_TITLE);
        lblPageTitle.setForeground(UITheme.TEXT_PRIMARY);

        header.add(lblPageTitle, BorderLayout.WEST);

        // Date Display
        String dateText = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"));
        lblCurrentDate = new JLabel("📅  " + dateText);
        lblCurrentDate.setFont(UITheme.FONT_REGULAR);
        lblCurrentDate.setForeground(UITheme.TEXT_SECONDARY);

        header.add(lblCurrentDate, BorderLayout.EAST);

        return header;
    }

    public void showSection(String sectionKey) {
        cardLayout.show(mainContentPanel, sectionKey);

        // Highlight selected nav button
        for (Map.Entry<String, ModernButton> entry : navButtons.entrySet()) {
            ModernButton btn = entry.getValue();
            if (entry.getKey().equals(sectionKey)) {
                btn.setNormalColor(UITheme.PRIMARY_BLUE);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setNormalColor(UITheme.NAVY_DARK);
                btn.setForeground(new Color(203, 213, 225));
            }
        }

        // Update Header Title
        switch (sectionKey) {
            case "DASHBOARD":
                lblPageTitle.setText("Dashboard Overview");
                overviewPanel.refreshMetrics();
                break;
            case "STUDENTS":
                lblPageTitle.setText("Student Record Management");
                studentPanel.loadCoursesIntoDropdown();
                studentPanel.loadStudentsToTable();
                break;
            case "COURSES":
                lblPageTitle.setText("Academic Course Management");
                coursePanel.loadCoursesToTable();
                break;
            case "ABOUT":
                lblPageTitle.setText("About System & Project Architecture");
                break;
        }
    }

    public void refreshDashboardMetrics() {
        if (overviewPanel != null) {
            overviewPanel.refreshMetrics();
        }
    }

    public void syncCourseDropdownInStudents() {
        if (studentPanel != null) {
            studentPanel.loadCoursesIntoDropdown();
        }
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to sign out?",
            "Confirm Sign Out",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
