package com.student.ui;

import com.student.ui.theme.RoundedPanel;
import com.student.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * AboutPanel displays project credits, architecture overview,
 * and Advanced Java concepts implemented.
 */
public class AboutPanel extends JPanel {

    public AboutPanel() {
        setBackground(UITheme.BG_CANVAS);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initComponents();
    }

    private void initComponents() {
        RoundedPanel card = new RoundedPanel(16, Color.WHITE, UITheme.BORDER_COLOR, 1);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 35, 30, 35));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        // Header Title
        JLabel lblTitle = new JLabel("Student Management System (SMS)");
        lblTitle.setFont(UITheme.FONT_HEADER_TITLE);
        lblTitle.setForeground(UITheme.NAVY_DARK);
        content.add(lblTitle);

        JLabel lblSub = new JLabel("College Mini-Project Submission • Advanced Java Desktop Application");
        lblSub.setFont(UITheme.FONT_REGULAR);
        lblSub.setForeground(UITheme.TEXT_SECONDARY);
        content.add(lblSub);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // Specifications Grid
        JPanel infoGrid = new JPanel(new GridLayout(5, 2, 16, 12));
        infoGrid.setBackground(Color.WHITE);
        infoGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        addSpecItem(infoGrid, "Programming Language:", "Java (JDK 17+)");
        addSpecItem(infoGrid, "GUI Framework:", "Java Swing & AWT");
        addSpecItem(infoGrid, "Database Engine:", "SQLite 3 (Serverless, File-based)");
        addSpecItem(infoGrid, "Database Driver:", "org.xerial:sqlite-jdbc (3.45.2.0)");
        addSpecItem(infoGrid, "Architectural Pattern:", "MVC (Model-View-Controller) + DAO");
        addSpecItem(infoGrid, "Database Location:", new File("student_management.db").getAbsolutePath());
        addSpecItem(infoGrid, "Default Admin User:", "admin");
        addSpecItem(infoGrid, "Default Password:", "admin123");
        addSpecItem(infoGrid, "IDE Compatibility:", "IntelliJ IDEA, Eclipse, NetBeans, VS Code");
        addSpecItem(infoGrid, "Project Type:", "BCA / MCA / B.Tech Major Mini-Project");

        content.add(infoGrid);
        content.add(Box.createRigidArea(new Dimension(0, 24)));

        // Highlights Panel
        RoundedPanel conceptsCard = new RoundedPanel(12, new Color(248, 250, 252), UITheme.BORDER_COLOR, 1);
        conceptsCard.setLayout(new BorderLayout());
        conceptsCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel lblConcepts = new JLabel("<html><b>Core Advanced Java Concepts Demonstrated:</b><br/>"
                + "• <b>Object-Oriented Programming (OOP):</b> Person abstract base class extended by Student class.<br/>"
                + "• <b>Encapsulation:</b> Private data members accessed via getters/setters with data integrity rules.<br/>"
                + "• <b>Inheritance & Polymorphism:</b> Method overriding (<code>getRoleDescription()</code>) across Person/Student.<br/>"
                + "• <b>DAO & Generics Pattern:</b> <code>BaseDAO&lt;T&gt;</code> generic interface decoupled from SQLite implementation.<br/>"
                + "• <b>JDBC & SQL Security:</b> <code>PreparedStatement</code> parameterized queries preventing SQL injection.<br/>"
                + "• <b>Exception Handling:</b> Robust try-with-resources auto-closing DB connections, statements, and result sets.<br/>"
                + "• <b>Java Swing GUI:</b> Custom painted rounded panels, flat buttons, custom table renderers, and responsive layouts.<br/>"
                + "• <b>Event-Driven Architecture:</b> ActionListeners, DocumentListeners for live filtering, and KeyListeners."
                + "</html>");
        lblConcepts.setFont(UITheme.FONT_REGULAR);
        lblConcepts.setForeground(UITheme.TEXT_PRIMARY);

        conceptsCard.add(lblConcepts, BorderLayout.CENTER);
        content.add(conceptsCard);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);

        card.add(scroll, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void addSpecItem(JPanel grid, String label, String value) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        item.setOpaque(false);

        JLabel lbl = new JLabel(label + " ");
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.NAVY_DARK);

        JLabel val = new JLabel(value);
        val.setFont(UITheme.FONT_REGULAR);
        val.setForeground(UITheme.TEXT_SECONDARY);

        item.add(lbl);
        item.add(val);
        grid.add(item);
    }
}
