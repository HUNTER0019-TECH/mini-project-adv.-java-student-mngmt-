package com.student.ui;

import com.student.dao.CourseDAO;
import com.student.dao.StudentDAO;
import com.student.models.Student;
import com.student.ui.theme.ModernButton;
import com.student.ui.theme.ModernTextField;
import com.student.ui.theme.RoundedPanel;
import com.student.ui.theme.UITheme;
import com.student.utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * StudentPanel provides the complete CRUD interface for Student records:
 * - Search by Student ID, Full Name, or Course
 * - Table view with column sorting and row selection
 * - Form inputs for Student ID, Name, Email, Phone, Course, Gender, DOB, Address
 * - Input validation, duplicate ID prevention, and confirmation dialogs
 */
public class StudentPanel extends JPanel {

    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final DashboardFrame parentFrame;

    // Table Components
    private JTable tblStudents;
    private DefaultTableModel tableModel;
    private ModernTextField txtSearch;

    // Form Inputs
    private ModernTextField txtStudentId;
    private ModernTextField txtFullName;
    private ModernTextField txtEmail;
    private ModernTextField txtPhone;
    private JComboBox<String> cmbCourse;
    private JComboBox<String> cmbGender;
    private ModernTextField txtDob;
    private JTextArea txtAddress;

    // Buttons
    private ModernButton btnAdd;
    private ModernButton btnUpdate;
    private ModernButton btnDelete;
    private ModernButton btnClear;
    private ModernButton btnRefresh;

    public StudentPanel(DashboardFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.studentDAO = new StudentDAO();
        this.courseDAO = new CourseDAO();

        setBackground(UITheme.BG_CANVAS);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 24, 20, 24));

        initComponents();
        loadCoursesIntoDropdown();
        loadStudentsToTable();
    }

    private void initComponents() {
        // TOP: Toolbar with Search and Actions
        JPanel topBar = createTopToolbar();
        add(topBar, BorderLayout.NORTH);

        // CENTER: Split Layout (Left: Table, Right: Form)
        JPanel contentPanel = new JPanel(new BorderLayout(18, 0));
        contentPanel.setOpaque(false);

        // Left Panel: Students JTable inside Rounded Card
        RoundedPanel tableCard = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(12, 14, 12, 14));

        String[] columns = {"ID", "Student ID", "Full Name", "Email", "Phone", "Course", "Gender", "DOB", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tblStudents = new JTable(tableModel);
        UITheme.styleTable(tblStudents);

        // Hide internal DB ID column from user view, but keep in model for reference
        tblStudents.removeColumn(tblStudents.getColumnModel().getColumn(0));

        // When a user selects a table row, populate form fields
        tblStudents.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblStudents.getSelectedRow() != -1) {
                populateFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblStudents);
        UITheme.styleScrollPane(scrollPane);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tableCard, BorderLayout.CENTER);

        // Right Panel: Form Card
        JPanel formCard = createFormCard();
        formCard.setPreferredSize(new Dimension(360, 0));
        contentPanel.add(formCard, BorderLayout.EAST);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTopToolbar() {
        RoundedPanel bar = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        bar.setLayout(new BorderLayout(14, 0));
        bar.setBorder(new EmptyBorder(10, 16, 10, 16));

        // Left: Search Bar
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBox.setOpaque(false);

        JLabel lblSearchIcon = new JLabel("🔍");
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));

        txtSearch = new ModernTextField("Search by Student ID, Name, or Course...");
        txtSearch.setPreferredSize(new Dimension(320, 36));

        // Real-time live search listener
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                executeSearch();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                executeSearch();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                executeSearch();
            }
        });

        ModernButton btnClearSearch = new ModernButton("✕ Clear", new Color(226, 232, 240), new Color(203, 213, 225), UITheme.TEXT_PRIMARY, 6);
        btnClearSearch.setPreferredSize(new Dimension(80, 36));
        btnClearSearch.setFont(UITheme.FONT_SMALL);
        btnClearSearch.addActionListener(e -> {
            txtSearch.setText("");
            loadStudentsToTable();
        });

        searchBox.add(lblSearchIcon);
        searchBox.add(txtSearch);
        searchBox.add(btnClearSearch);

        bar.add(searchBox, BorderLayout.WEST);

        // Right: Refresh & Count indicator
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightActions.setOpaque(false);

        btnRefresh = new ModernButton("🔄 Refresh Table", new Color(241, 245, 249), new Color(226, 232, 240), UITheme.TEXT_PRIMARY, 6);
        btnRefresh.setPreferredSize(new Dimension(140, 36));
        btnRefresh.setFont(UITheme.FONT_BUTTON);
        btnRefresh.addActionListener(e -> {
            loadCoursesIntoDropdown();
            loadStudentsToTable();
        });

        rightActions.add(btnRefresh);
        bar.add(rightActions, BorderLayout.EAST);

        return bar;
    }

    private JPanel createFormCard() {
        RoundedPanel card = new RoundedPanel(12, Color.WHITE, UITheme.BORDER_COLOR, 1);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        // Form Title
        JLabel lblTitle = new JLabel("Student Information");
        lblTitle.setFont(UITheme.FONT_SECTION_TITLE);
        lblTitle.setForeground(UITheme.TEXT_PRIMARY);
        card.add(lblTitle, BorderLayout.NORTH);

        // Form Body Scroll
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(12, 0, 12, 0));

        // 1. Student ID
        txtStudentId = new ModernTextField("e.g. STU1001");
        addFormField(body, "Student ID *", txtStudentId);

        // 2. Full Name
        txtFullName = new ModernTextField("e.g. Aryan Sharma");
        addFormField(body, "Full Name *", txtFullName);

        // 3. Email
        txtEmail = new ModernTextField("e.g. aryan@college.edu");
        addFormField(body, "Email Address *", txtEmail);

        // 4. Phone Number
        txtPhone = new ModernTextField("e.g. 9876543210 (10 digits)");
        addFormField(body, "Phone Number *", txtPhone);

        // 5. Course Dropdown
        cmbCourse = new JComboBox<>();
        cmbCourse.setFont(UITheme.FONT_INPUT);
        cmbCourse.setBackground(Color.WHITE);
        addFormField(body, "Course *", cmbCourse);

        // 6. Gender & DOB side by side
        JPanel rowGenderDob = new JPanel(new GridLayout(1, 2, 10, 0));
        rowGenderDob.setOpaque(false);

        JPanel genderCol = new JPanel();
        genderCol.setLayout(new BoxLayout(genderCol, BoxLayout.Y_AXIS));
        genderCol.setOpaque(false);
        JLabel lblGen = new JLabel("Gender *");
        lblGen.setFont(UITheme.FONT_LABEL);
        lblGen.setForeground(UITheme.TEXT_PRIMARY);
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        cmbGender.setFont(UITheme.FONT_INPUT);
        cmbGender.setBackground(Color.WHITE);
        genderCol.add(lblGen);
        genderCol.add(Box.createRigidArea(new Dimension(0, 4)));
        genderCol.add(cmbGender);

        JPanel dobCol = new JPanel();
        dobCol.setLayout(new BoxLayout(dobCol, BoxLayout.Y_AXIS));
        dobCol.setOpaque(false);
        JLabel lblDob = new JLabel("DOB (YYYY-MM-DD) *");
        lblDob.setFont(UITheme.FONT_LABEL);
        lblDob.setForeground(UITheme.TEXT_PRIMARY);
        txtDob = new ModernTextField("2004-05-15");
        dobCol.add(lblDob);
        dobCol.add(Box.createRigidArea(new Dimension(0, 4)));
        dobCol.add(txtDob);

        rowGenderDob.add(genderCol);
        rowGenderDob.add(dobCol);

        body.add(rowGenderDob);
        body.add(Box.createRigidArea(new Dimension(0, 10)));

        // 7. Address
        JLabel lblAddr = new JLabel("Residential Address *");
        lblAddr.setFont(UITheme.FONT_LABEL);
        lblAddr.setForeground(UITheme.TEXT_PRIMARY);
        body.add(lblAddr);
        body.add(Box.createRigidArea(new Dimension(0, 4)));

        txtAddress = new JTextArea(3, 20);
        txtAddress.setFont(UITheme.FONT_INPUT);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane spAddress = new JScrollPane(txtAddress);
        spAddress.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        spAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        body.add(spAddress);

        JScrollPane formScroll = new JScrollPane(body);
        formScroll.setBorder(null);
        formScroll.getViewport().setBackground(Color.WHITE);
        card.add(formScroll, BorderLayout.CENTER);

        // Buttons Grid at Bottom of Form Card
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        btnAdd = new ModernButton("+ Add Student", UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_HOVER, Color.WHITE, 8);
        btnAdd.addActionListener(e -> handleAddStudent());

        btnUpdate = new ModernButton("✎ Update", new Color(245, 158, 11), new Color(217, 119, 6), Color.WHITE, 8);
        btnUpdate.addActionListener(e -> handleUpdateStudent());

        btnDelete = new ModernButton("🗑 Delete", UITheme.COLOR_DANGER, UITheme.COLOR_DANGER_HOVER, Color.WHITE, 8);
        btnDelete.addActionListener(e -> handleDeleteStudent());

        btnClear = new ModernButton("Reset Form", new Color(100, 116, 139), new Color(71, 85, 105), Color.WHITE, 8);
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

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
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    public void loadCoursesIntoDropdown() {
        cmbCourse.removeAllItems();
        List<String> codes = courseDAO.getAllCourseCodes();
        if (codes.isEmpty()) {
            cmbCourse.addItem("BCA-101");
        } else {
            for (String code : codes) {
                cmbCourse.addItem(code);
            }
        }
    }

    public void loadStudentsToTable() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAll();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getStudentId(),
                s.getName(),
                s.getEmail(),
                s.getPhone(),
                s.getCourse(),
                s.getGender(),
                s.getDob(),
                s.getAddress()
            });
        }
    }

    private void executeSearch() {
        String query = txtSearch.getText().trim();
        List<Student> results = studentDAO.searchStudents(query);
        tableModel.setRowCount(0);
        for (Student s : results) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getStudentId(),
                s.getName(),
                s.getEmail(),
                s.getPhone(),
                s.getCourse(),
                s.getGender(),
                s.getDob(),
                s.getAddress()
            });
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = tblStudents.getSelectedRow();
        if (selectedRow == -1) return;

        txtStudentId.setText(tblStudents.getValueAt(selectedRow, 0).toString());
        txtFullName.setText(tblStudents.getValueAt(selectedRow, 1).toString());
        txtEmail.setText(tblStudents.getValueAt(selectedRow, 2).toString());
        txtPhone.setText(tblStudents.getValueAt(selectedRow, 3).toString());

        String course = tblStudents.getValueAt(selectedRow, 4).toString();
        cmbCourse.setSelectedItem(course);

        String gender = tblStudents.getValueAt(selectedRow, 5).toString();
        cmbGender.setSelectedItem(gender);

        txtDob.setText(tblStudents.getValueAt(selectedRow, 6).toString());
        txtAddress.setText(tblStudents.getValueAt(selectedRow, 7).toString());

        // Make Student ID read-only during edit mode to prevent changing primary unique key accidentally
        txtStudentId.setEditable(false);
    }

    private void clearForm() {
        txtStudentId.setText("");
        txtStudentId.setEditable(true);
        txtFullName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        if (cmbCourse.getItemCount() > 0) cmbCourse.setSelectedIndex(0);
        cmbGender.setSelectedIndex(0);
        txtDob.setText("");
        txtAddress.setText("");
        tblStudents.clearSelection();
    }

    private void handleAddStudent() {
        String studentId = txtStudentId.getText().trim();
        String name = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String course = (String) cmbCourse.getSelectedItem();
        String gender = (String) cmbGender.getSelectedItem();
        String dob = txtDob.getText().trim();
        String address = txtAddress.getText().trim();

        // Validations
        if (ValidationUtils.isEmpty(studentId) || ValidationUtils.isEmpty(name) ||
            ValidationUtils.isEmpty(email) || ValidationUtils.isEmpty(phone) ||
            ValidationUtils.isEmpty(dob) || ValidationUtils.isEmpty(address)) {
            JOptionPane.showMessageDialog(this, "All fields marked with * are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidStudentId(studentId)) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID! Must be 3-20 alphanumeric characters (e.g. STU1001).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtStudentId.requestFocus();
            return;
        }

        if (studentDAO.isStudentIdExists(studentId)) {
            JOptionPane.showMessageDialog(this, "A student with ID '" + studentId + "' already exists! Please use a unique ID.", "Duplicate Student ID", JOptionPane.ERROR_MESSAGE);
            txtStudentId.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address (e.g. student@college.edu).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit mobile number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPhone.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidPastDate(dob)) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth! Format must be YYYY-MM-DD (e.g. 2004-05-15) and date must be in the past.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtDob.requestFocus();
            return;
        }

        Student student = new Student(studentId, name, email, phone, course, gender, dob, address);
        boolean success = studentDAO.add(student);

        if (success) {
            JOptionPane.showMessageDialog(this, "Student registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadStudentsToTable();
            parentFrame.refreshDashboardMetrics();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register student. Please check database logs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateStudent() {
        String studentId = txtStudentId.getText().trim();
        if (ValidationUtils.isEmpty(studentId)) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String course = (String) cmbCourse.getSelectedItem();
        String gender = (String) cmbGender.getSelectedItem();
        String dob = txtDob.getText().trim();
        String address = txtAddress.getText().trim();

        if (ValidationUtils.isEmpty(name) || ValidationUtils.isEmpty(email) ||
            ValidationUtils.isEmpty(phone) || ValidationUtils.isEmpty(dob) ||
            ValidationUtils.isEmpty(address)) {
            JOptionPane.showMessageDialog(this, "All fields marked with * are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit mobile number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ValidationUtils.isValidPastDate(dob)) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth! Format must be YYYY-MM-DD.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(studentId, name, email, phone, course, gender, dob, address);
        boolean success = studentDAO.update(student);

        if (success) {
            JOptionPane.showMessageDialog(this, "Student record updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadStudentsToTable();
            parentFrame.refreshDashboardMetrics();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteStudent() {
        String studentId = txtStudentId.getText().trim();
        if (ValidationUtils.isEmpty(studentId)) {
            JOptionPane.showMessageDialog(this, "Please select a student record to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to permanently delete student record [" + studentId + "]?\nThis action cannot be undone.",
            "Confirm Delete Student",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = studentDAO.delete(studentId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student record deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadStudentsToTable();
                parentFrame.refreshDashboardMetrics();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
