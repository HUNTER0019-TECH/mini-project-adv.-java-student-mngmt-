package com.student.ui;

import com.student.dao.CourseDAO;
import com.student.dao.StudentDAO;
import com.student.models.Student;
import com.student.ui.theme.ModernButton;
import com.student.ui.theme.RoundedPanel;
import com.student.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * OverviewPanel renders the main dashboard cards:
 * - Total Students
 * - Total Courses
 * - Total Departments
 * - SQLite Database Status
 * Also displays quick actions and recent student enrollments.
 */
public class OverviewPanel extends JPanel {

    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final DashboardFrame parentFrame;

    private JLabel lblTotalStudents;
    private JLabel lblTotalCourses;
    private JLabel lblTotalDepts;
    private DefaultTableModel recentStudentsModel;
    private DefaultTableModel courseDistModel;

    public OverviewPanel(DashboardFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();

        setBackground(UITheme.BG_CANVAS);
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initComponents();
        refreshMetrics();
    }

    private void initComponents() {
        // TOP: Stat Cards (4 Cards in a Grid)
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        statsPanel.setOpaque(false);

        lblTotalStudents = new JLabel("0");
        lblTotalCourses = new JLabel("0");
        lblTotalDepts = new JLabel("0");

        statsPanel.add(createStatCard("Total Students", lblTotalStudents, "🎓", UITheme.PRIMARY_BLUE));
        statsPanel.add(createStatCard("Total Courses", lblTotalCourses, "📚", new Color(16, 185, 129)));
        statsPanel.add(createStatCard("Departments", lblTotalDepts, "🏛️", new Color(245, 158, 11)));
        statsPanel.add(createStatCard("Database Engine", new JLabel("SQLite 3"), "🗄️", new Color(139, 92, 246)));

        add(statsPanel, BorderLayout.NORTH);

        // CENTER: Main Content split into Two Cards (Recent Students & Course Distribution)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);

        // Left Card: Recent Enrollments
        RoundedPanel recentCard = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        recentCard.setLayout(new BorderLayout(0, 12));
        recentCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel recentHeader = new JPanel(new BorderLayout());
        recentHeader.setOpaque(false);
        JLabel lblRecentTitle = new JLabel("Recent Student Registrations");
        lblRecentTitle.setFont(UITheme.FONT_SECTION_TITLE);
        lblRecentTitle.setForeground(UITheme.TEXT_PRIMARY);

        ModernButton btnGoToStudents = new ModernButton("View All →", UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_HOVER, Color.WHITE, 6);
        btnGoToStudents.setPreferredSize(new Dimension(95, 28));
        btnGoToStudents.setFont(UITheme.FONT_SMALL);
        btnGoToStudents.addActionListener(e -> parentFrame.showSection("STUDENTS"));

        recentHeader.add(lblRecentTitle, BorderLayout.WEST);
        recentHeader.add(btnGoToStudents, BorderLayout.EAST);
        recentCard.add(recentHeader, BorderLayout.NORTH);

        String[] recentCols = {"ID", "Name", "Course", "Phone"};
        recentStudentsModel = new DefaultTableModel(recentCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tblRecent = new JTable(recentStudentsModel);
        UITheme.styleTable(tblRecent);
        JScrollPane spRecent = new JScrollPane(tblRecent);
        UITheme.styleScrollPane(spRecent);
        recentCard.add(spRecent, BorderLayout.CENTER);

        centerPanel.add(recentCard);

        // Right Card: Course Breakdown & Quick Actions
        RoundedPanel rightCard = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        rightCard.setLayout(new BorderLayout(0, 12));
        rightCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel rightHeader = new JPanel(new BorderLayout());
        rightHeader.setOpaque(false);
        JLabel lblDistTitle = new JLabel("Course Enrollment Summary");
        lblDistTitle.setFont(UITheme.FONT_SECTION_TITLE);
        lblDistTitle.setForeground(UITheme.TEXT_PRIMARY);

        ModernButton btnGoToCourses = new ModernButton("Manage Courses →", new Color(16, 185, 129), new Color(5, 150, 105), Color.WHITE, 6);
        btnGoToCourses.setPreferredSize(new Dimension(135, 28));
        btnGoToCourses.setFont(UITheme.FONT_SMALL);
        btnGoToCourses.addActionListener(e -> parentFrame.showSection("COURSES"));

        rightHeader.add(lblDistTitle, BorderLayout.WEST);
        rightHeader.add(btnGoToCourses, BorderLayout.EAST);
        rightCard.add(rightHeader, BorderLayout.NORTH);

        String[] distCols = {"Course Code", "Students Enrolled"};
        courseDistModel = new DefaultTableModel(distCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable tblDist = new JTable(courseDistModel);
        UITheme.styleTable(tblDist);
        JScrollPane spDist = new JScrollPane(tblDist);
        UITheme.styleScrollPane(spDist);
        rightCard.add(spDist, BorderLayout.CENTER);

        centerPanel.add(rightCard);

        add(centerPanel, BorderLayout.CENTER);

        // BOTTOM: Quick Action Toolbar
        RoundedPanel actionPanel = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 12));

        JLabel lblQuick = new JLabel("Quick Actions:");
        lblQuick.setFont(UITheme.FONT_LABEL);
        lblQuick.setForeground(UITheme.TEXT_PRIMARY);
        actionPanel.add(lblQuick);

        ModernButton btnQuickAdd = new ModernButton("+ Register Student", UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_HOVER, Color.WHITE, 6);
        btnQuickAdd.setPreferredSize(new Dimension(145, 34));
        btnQuickAdd.addActionListener(e -> parentFrame.showSection("STUDENTS"));
        actionPanel.add(btnQuickAdd);

        ModernButton btnQuickCourse = new ModernButton("+ Add New Course", new Color(16, 185, 129), new Color(5, 150, 105), Color.WHITE, 6);
        btnQuickCourse.setPreferredSize(new Dimension(145, 34));
        btnQuickCourse.addActionListener(e -> parentFrame.showSection("COURSES"));
        actionPanel.add(btnQuickCourse);

        ModernButton btnRefresh = new ModernButton("🔄 Refresh Dashboard", new Color(71, 85, 105), new Color(51, 65, 85), Color.WHITE, 6);
        btnRefresh.setPreferredSize(new Dimension(160, 34));
        btnRefresh.addActionListener(e -> refreshMetrics());
        actionPanel.add(btnRefresh);

        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String icon, Color accentColor) {
        RoundedPanel card = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setPreferredSize(new Dimension(180, 100));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UITheme.FONT_SMALL);
        lblTitle.setForeground(UITheme.TEXT_SECONDARY);

        valueLabel.setFont(UITheme.FONT_CARD_NUMBER);
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(valueLabel);

        card.add(textPanel, BorderLayout.WEST);

        // Icon badge
        RoundedPanel iconBadge = new RoundedPanel(20, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 25), null, 0);
        iconBadge.setPreferredSize(new Dimension(44, 44));
        iconBadge.setLayout(new GridBagLayout());

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconBadge.add(lblIcon);

        card.add(iconBadge, BorderLayout.EAST);

        return card;
    }

    public void refreshMetrics() {
        // Fetch fresh counts from SQLite DAO
        int totalStudents = studentDAO.getStudentCount();
        int totalCourses = courseDAO.getCourseCount();
        int totalDepts = courseDAO.getDepartmentCount();

        lblTotalStudents.setText(String.valueOf(totalStudents));
        lblTotalCourses.setText(String.valueOf(totalCourses));
        lblTotalDepts.setText(String.valueOf(totalDepts));

        // Populate Recent Students
        recentStudentsModel.setRowCount(0);
        List<Student> students = studentDAO.getAll();
        int limit = Math.min(6, students.size());
        for (int i = 0; i < limit; i++) {
            Student s = students.get(i);
            recentStudentsModel.addRow(new Object[]{
                s.getStudentId(),
                s.getName(),
                s.getCourse(),
                s.getPhone()
            });
        }

        // Populate Course Enrollment Summary
        courseDistModel.setRowCount(0);
        Map<String, Integer> dist = studentDAO.getCourseDistribution();
        for (Map.Entry<String, Integer> entry : dist.entrySet()) {
            courseDistModel.addRow(new Object[]{
                entry.getKey(),
                entry.getValue() + " Students"
            });
        }
    }
}
