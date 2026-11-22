package com.student.ui;

import com.student.dao.CourseDAO;
import com.student.models.Course;
import com.student.ui.theme.ModernButton;
import com.student.ui.theme.ModernTextField;
import com.student.ui.theme.RoundedPanel;
import com.student.ui.theme.UITheme;
import com.student.utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CoursePanel provides course administration:
 * - Add new course (Course Name, Course Code, Duration, Department)
 * - Display all courses in JTable
 * - Delete course with confirmation
 * - Synchronize course dropdown in student management
 */
public class CoursePanel extends JPanel {

    private final CourseDAO courseDAO;
    private final DashboardFrame parentFrame;

    private JTable tblCourses;
    private DefaultTableModel tableModel;

    private ModernTextField txtCourseCode;
    private ModernTextField txtCourseName;
    private ModernTextField txtDuration;
    private ModernTextField txtDepartment;

    private ModernButton btnAddCourse;
    private ModernButton btnDeleteCourse;
    private ModernButton btnClearCourse;
    private ModernButton btnRefreshCourses;

    public CoursePanel(DashboardFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.courseDAO = new CourseDAO();

        setBackground(UITheme.BG_CANVAS);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initComponents();
        loadCoursesToTable();
    }

    private void initComponents() {
        // TOP: Toolbar
        JPanel topBar = createTopToolbar();
        add(topBar, BorderLayout.NORTH);

        // CENTER: Two Columns (Left: Table, Right: Add Course Form)
        JPanel contentPanel = new JPanel(new BorderLayout(18, 0));
        contentPanel.setOpaque(false);

        // Left Table Card
        RoundedPanel tableCard = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        String[] columns = {"ID", "Course Code", "Course Name", "Duration", "Department"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblCourses = new JTable(tableModel);
        UITheme.styleTable(tblCourses);

        // Hide internal DB ID
        tblCourses.removeColumn(tblCourses.getColumnModel().getColumn(0));

        tblCourses.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblCourses.getSelectedRow() != -1) {
                populateFormFromSelection();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblCourses);
        UITheme.styleScrollPane(scrollPane);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tableCard, BorderLayout.CENTER);

        // Right Form Card
        JPanel formCard = createFormCard();
        formCard.setPreferredSize(new Dimension(340, 0));
        contentPanel.add(formCard, BorderLayout.EAST);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTopToolbar() {
        RoundedPanel bar = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel lblTitle = new JLabel("Academic Course Offerings");
        lblTitle.setFont(UITheme.FONT_SECTION_TITLE);
        lblTitle.setForeground(UITheme.TEXT_PRIMARY);
        bar.add(lblTitle, BorderLayout.WEST);

        btnRefreshCourses = new ModernButton("🔄 Refresh Courses", new Color(241, 245, 249), new Color(226, 232, 240), UITheme.TEXT_PRIMARY, 6);
        btnRefreshCourses.setPreferredSize(new Dimension(150, 36));
        btnRefreshCourses.addActionListener(e -> loadCoursesToTable());
        bar.add(btnRefreshCourses, BorderLayout.EAST);

        return bar;
    }

    private JPanel createFormCard() {
        RoundedPanel card = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel lblTitle = new JLabel("Course Details");
        lblTitle.setFont(UITheme.FONT_SECTION_TITLE);
        lblTitle.setForeground(UITheme.TEXT_PRIMARY);
        card.add(lblTitle, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(16, 0, 16, 0));

        // 1. Course Code
        txtCourseCode = new ModernTextField("e.g. BCA-101");
        addFormField(body, "Course Code *", txtCourseCode);

        // 2. Course Name
        txtCourseName = new ModernTextField("e.g. Bachelor of Computer Applications");
        addFormField(body, "Course Name *", txtCourseName);

        // 3. Duration
        txtDuration = new ModernTextField("e.g. 3 Years");
        addFormField(body, "Duration *", txtDuration);

        // 4. Department
        txtDepartment = new ModernTextField("e.g. Computer Science");
        addFormField(body, "Department *", txtDepartment);

        card.add(body, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        btnPanel.setOpaque(false);

        btnAddCourse = new ModernButton("+ Add Course", new Color(16, 185, 129), new Color(5, 150, 105), Color.WHITE, 8);
        btnAddCourse.setPreferredSize(new Dimension(100, 36));
        btnAddCourse.addActionListener(e -> handleAddCourse());

        btnDeleteCourse = new ModernButton("🗑 Delete Selected Course", UITheme.COLOR_DANGER, UITheme.COLOR_DANGER_HOVER, Color.WHITE, 8);
        btnDeleteCourse.setPreferredSize(new Dimension(100, 36));
        btnDeleteCourse.addActionListener(e -> handleDeleteCourse());

        btnClearCourse = new ModernButton("Reset Fields", new Color(100, 116, 139), new Color(71, 85, 105), Color.WHITE, 8);
        btnClearCourse.setPreferredSize(new Dimension(100, 36));
        btnClearCourse.addActionListener(e -> clearForm());

        btnPanel.add(btnAddCourse);
        btnPanel.add(btnDeleteCourse);
        btnPanel.add(btnClearCourse);

        card.add(btnPanel, BorderLayout.SOUTH);

        return card;
    }

    private void addFormField(JPanel parent, String labelText, JComponent field) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        parent.add(lbl);
        parent.add(Box.createRigidArea(new Dimension(0, 4)));
        parent.add(field);
        parent.add(Box.createRigidArea(new Dimension(0, 12)));
    }

    public void loadCoursesToTable() {
        tableModel.setRowCount(0);
        List<Course> courses = courseDAO.getAll();
        for (Course c : courses) {
            tableModel.addRow(new Object[]{
                c.getId(),
                c.getCourseCode(),
                c.getCourseName(),
                c.getDuration(),
                c.getDepartment()
            });
        }
    }

    private void populateFormFromSelection() {
        int selected = tblCourses.getSelectedRow();
        if (selected == -1) return;

        txtCourseCode.setText(tblCourses.getValueAt(selected, 0).toString());
        txtCourseCode.setEditable(false);
        txtCourseName.setText(tblCourses.getValueAt(selected, 1).toString());
        txtDuration.setText(tblCourses.getValueAt(selected, 2).toString());
        txtDepartment.setText(tblCourses.getValueAt(selected, 3).toString());
    }

    private void clearForm() {
        txtCourseCode.setText("");
        txtCourseCode.setEditable(true);
        txtCourseName.setText("");
        txtDuration.setText("");
        txtDepartment.setText("");
        tblCourses.clearSelection();
    }

    private void handleAddCourse() {
        String code = txtCourseCode.getText().trim();
        String name = txtCourseName.getText().trim();
        String duration = txtDuration.getText().trim();
        String dept = txtDepartment.getText().trim();

        if (ValidationUtils.isEmpty(code) || ValidationUtils.isEmpty(name) ||
            ValidationUtils.isEmpty(duration) || ValidationUtils.isEmpty(dept)) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (courseDAO.isCourseCodeExists(code)) {
            JOptionPane.showMessageDialog(this, "Course code '" + code + "' already exists! Please use a unique code.", "Duplicate Course", JOptionPane.ERROR_MESSAGE);
            txtCourseCode.requestFocus();
            return;
        }

        Course course = new Course(code, name, duration, dept);
        boolean success = courseDAO.add(course);

        if (success) {
            JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadCoursesToTable();
            parentFrame.refreshDashboardMetrics();
            parentFrame.syncCourseDropdownInStudents();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteCourse() {
        String code = txtCourseCode.getText().trim();
        if (ValidationUtils.isEmpty(code)) {
            JOptionPane.showMessageDialog(this, "Please select a course from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete course [" + code + "]?\nStudents enrolled in this course will keep their existing course code record.",
            "Confirm Course Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = courseDAO.delete(code);
            if (success) {
                JOptionPane.showMessageDialog(this, "Course deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadCoursesToTable();
                parentFrame.refreshDashboardMetrics();
                parentFrame.syncCourseDropdownInStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
