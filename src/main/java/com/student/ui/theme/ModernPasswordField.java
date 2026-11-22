package com.student.ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Modern styled password field with rounded corners, padding, and focus highlight.
 */
public class ModernPasswordField extends JPasswordField {
    private boolean isFocused = false;
    private int cornerRadius = 8;
    private char defaultEchoChar;

    public ModernPasswordField() {
        super();
        this.defaultEchoChar = getEchoChar();
        setFont(UITheme.FONT_INPUT);
        setForeground(UITheme.TEXT_PRIMARY);
        setBackground(Color.WHITE);
        setCaretColor(UITheme.PRIMARY_BLUE);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setOpaque(false);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    public char getDefaultEchoChar() {
        return defaultEchoChar;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Border
        if (isFocused) {
            g2.setColor(UITheme.PRIMARY_BLUE);
            g2.setStroke(new BasicStroke(1.5f));
        } else {
            g2.setColor(UITheme.BORDER_COLOR);
            g2.setStroke(new BasicStroke(1.0f));
        }
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        super.paintComponent(g);
        g2.dispose();
    }
}
