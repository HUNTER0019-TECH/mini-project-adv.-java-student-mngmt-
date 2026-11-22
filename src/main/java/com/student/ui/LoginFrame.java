package com.student.ui;

import com.student.dao.UserDAO;
import com.student.ui.theme.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * LoginFrame provides an authentication screen for the administrator.
 * Features:
 * - Modern split design (Navy branding banner + White form panel)
 * - Show/Hide password toggle
 * - Input validation with friendly error messages
 * - Default credentials hint: admin / admin123
 */
public class LoginFrame extends JFrame {

    private ModernTextField txtUsername;
    private ModernPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private ModernButton btnLogin;
    private JLabel lblStatus;

    private final UserDAO userDAO;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Student Management System - Administrator Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 520);
        setMinimumSize(new Dimension(800, 480));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_CANVAS);
        setLayout(new BorderLayout());

        // Main Container Card
        RoundedPanel cardPanel = new RoundedPanel(16, Color.WHITE, UITheme.BORDER_COLOR, 1);
        cardPanel.setLayout(new GridLayout(1, 2, 0, 0));

        // LEFT PANEL: Navy Branding Panel
        JPanel leftPanel = createBrandPanel();

        // RIGHT PANEL: Login Form
        JPanel rightPanel = createLoginFormPanel();

        cardPanel.add(leftPanel);
        cardPanel.add(rightPanel);

        // Center card inside frame with margin
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setBackground(UITheme.BG_CANVAS);
        outerWrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        outerWrapper.add(cardPanel, gbc);

        add(outerWrapper, BorderLayout.CENTER);
    }

    private JPanel createBrandPanel() {
        RoundedPanel brandPanel = new RoundedPanel(16, UITheme.NAVY_DARK, null, 0);
        brandPanel.setLayout(new BorderLayout());
        brandPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel lblLogo = new JLabel("🎓");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("StudentHub");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Student Management System");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSubtitle.setForeground(new Color(148, 163, 184));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTag = new JLabel("College Edition • Advanced Java");
        lblTag.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTag.setForeground(UITheme.ACCENT_CYAN);
        lblTag.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(lblLogo);
        content.add(Box.createRigidArea(new Dimension(0, 16)));
        content.add(lblTitle);
        content.add(Box.createRigidArea(new Dimension(0, 4)));
        content.add(lblSubtitle);
        content.add(Box.createRigidArea(new Dimension(0, 8)));
        content.add(lblTag);

        content.add(Box.createRigidArea(new Dimension(0, 36)));

        // Feature list
        String[] features = {
            "✓ Complete CRUD Record Management",
            "✓ SQLite Persistent Database",
            "✓ Dynamic Course Allocation",
            "✓ Real-time Student Search & Metrics",
            "✓ MVC & DAO Architectural Pattern"
        };

        for (String feat : features) {
            JLabel lblFeat = new JLabel(feat);
            lblFeat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblFeat.setForeground(new Color(203, 213, 225));
            lblFeat.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(lblFeat);
            content.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        brandPanel.add(content, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel("Mini Project • BCA / B.Tech / MCA");
        lblFooter.setFont(UITheme.FONT_SMALL);
        lblFooter.setForeground(new Color(100, 116, 139));
        brandPanel.add(lblFooter, BorderLayout.SOUTH);

        return brandPanel;
    }

    private JPanel createLoginFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BorderLayout());
        formPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel formBody = new JPanel();
        formBody.setBackground(Color.WHITE);
        formBody.setLayout(new BoxLayout(formBody, BoxLayout.Y_AXIS));

        JLabel lblHeading = new JLabel("Sign In");
        lblHeading.setFont(UITheme.FONT_HEADER_TITLE);
        lblHeading.setForeground(UITheme.TEXT_PRIMARY);
        lblHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Enter your credentials to access the portal");
        lblSub.setFont(UITheme.FONT_REGULAR);
        lblSub.setForeground(UITheme.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        formBody.add(lblHeading);
        formBody.add(Box.createRigidArea(new Dimension(0, 4)));
        formBody.add(lblSub);
        formBody.add(Box.createRigidArea(new Dimension(0, 24)));

        // Username Field
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(UITheme.FONT_LABEL);
        lblUser.setForeground(UITheme.TEXT_PRIMARY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new ModernTextField("Enter username (admin)");
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        formBody.add(lblUser);
        formBody.add(Box.createRigidArea(new Dimension(0, 6)));
        formBody.add(txtUsername);
        formBody.add(Box.createRigidArea(new Dimension(0, 14)));

        // Password Field
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(UITheme.FONT_LABEL);
        lblPass.setForeground(UITheme.TEXT_PRIMARY);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new ModernPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        formBody.add(lblPass);
        formBody.add(Box.createRigidArea(new Dimension(0, 6)));
        formBody.add(txtPassword);
        formBody.add(Box.createRigidArea(new Dimension(0, 8)));

        // Show/Hide Password Checkbox
        chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setFont(UITheme.FONT_SMALL);
        chkShowPassword.setForeground(UITheme.TEXT_SECONDARY);
        chkShowPassword.setBackground(Color.WHITE);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkShowPassword.addActionListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar(txtPassword.getDefaultEchoChar());
            }
        });

        formBody.add(chkShowPassword);
        formBody.add(Box.createRigidArea(new Dimension(0, 12)));

        // Status / Error message label
        lblStatus = new JLabel(" ");
        lblStatus.setFont(UITheme.FONT_SMALL);
        lblStatus.setForeground(UITheme.COLOR_DANGER);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        formBody.add(lblStatus);
        formBody.add(Box.createRigidArea(new Dimension(0, 12)));

        // Login Button
        btnLogin = new ModernButton("Sign In to Portal", UITheme.PRIMARY_BLUE, UITheme.PRIMARY_BLUE_HOVER, Color.WHITE, 8);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.setPreferredSize(new Dimension(200, 42));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> performLogin());

        formBody.add(btnLogin);

        // Key Listeners for Enter key
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterListener);
        txtPassword.addKeyListener(enterListener);

        formPanel.add(formBody, BorderLayout.CENTER);

        // Demo Credentials Hint Box at bottom
        RoundedPanel hintPanel = new RoundedPanel(8, new Color(241, 245, 249), UITheme.BORDER_COLOR, 1);
        hintPanel.setLayout(new BorderLayout());
        hintPanel.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel lblHint = new JLabel("<html><b>Default Credentials:</b> User: <code>admin</code> | Pass: <code>admin123</code></html>");
        lblHint.setFont(UITheme.FONT_SMALL);
        lblHint.setForeground(UITheme.TEXT_SECONDARY);
        hintPanel.add(lblHint, BorderLayout.CENTER);

        formPanel.add(hintPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // 1. Validation
        if (username.isEmpty()) {
            lblStatus.setText("⚠ Please enter your username.");
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            lblStatus.setText("⚠ Please enter your password.");
            txtPassword.requestFocus();
            return;
        }

        lblStatus.setForeground(UITheme.PRIMARY_BLUE);
        lblStatus.setText("Authenticating credentials...");

        // 2. Perform authentication with SQLite DB
        SwingUtilities.invokeLater(() -> {
            boolean success = userDAO.authenticate(username, password);

            if (success) {
                lblStatus.setForeground(UITheme.COLOR_SUCCESS);
                lblStatus.setText("✓ Login successful! Opening dashboard...");

                // Open Dashboard and close LoginFrame
                Timer timer = new Timer(300, evt -> {
                    dispose();
                    new DashboardFrame(username).setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();

            } else {
                lblStatus.setForeground(UITheme.COLOR_DANGER);
                lblStatus.setText("❌ Invalid username or password.");
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        });
    }
}
