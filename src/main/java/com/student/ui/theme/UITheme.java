package com.student.ui.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * UITheme defines the color palette, fonts, and component styling rules.
 * Uses a White and Dark Navy theme with subtle blue accents.
 */
public class UITheme {

    // Color Palette
    public static final Color NAVY_DARK = new Color(15, 23, 42);         // #0F172A - Deep Navy Background for Sidebar
    public static final Color NAVY_SURFACE = new Color(30, 41, 59);      // #1E293B - Card/Nav Item Hover
    public static final Color NAVY_LIGHT = new Color(51, 65, 85);        // #334155 - Subtle Dividers

    public static final Color PRIMARY_BLUE = new Color(37, 99, 235);     // #2563EB - Royal Blue Accent
    public static final Color PRIMARY_BLUE_HOVER = new Color(29, 78, 216);// #1D4ED8
    public static final Color ACCENT_CYAN = new Color(14, 165, 233);     // #0EA5E9

    public static final Color BG_CANVAS = new Color(248, 250, 252);      // #F8FAFC - Main Body Canvas Background
    public static final Color CARD_BG = Color.WHITE;                     // Pure White Card Canvas
    public static final Color BORDER_COLOR = new Color(226, 232, 240);    // #E2E8F0 - Light Border

    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);      // #0F172A - High Contrast Text
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // #64748B - Muted Subtitles
    public static final Color TEXT_LIGHT = new Color(241, 245, 249);     // #F1F5F9 - Text on Navy Sidebar

    public static final Color COLOR_DANGER = new Color(239, 68, 68);     // #EF4444 - Delete / Error Red
    public static final Color COLOR_DANGER_HOVER = new Color(220, 38, 38);
    public static final Color COLOR_SUCCESS = new Color(16, 185, 129);   // #10B981 - Success Green
    public static final Color COLOR_WARNING = new Color(245, 158, 11);   // #F59E0B - Warning Amber

    // Fonts
    public static final Font FONT_HEADER_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_CARD_NUMBER = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_SECTION_TITLE = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_NAV_ITEM = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Styles a JTable to match the modern corporate aesthetic.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));
        table.setSelectionBackground(new Color(238, 242, 255));
        table.setSelectionForeground(NAVY_DARK);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_LABEL);
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(NAVY_DARK);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        // Default cell padding and alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));

                if (isSelected) {
                    c.setBackground(new Color(224, 231, 255));
                    c.setForeground(PRIMARY_BLUE);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        });
    }

    /**
     * Applies styling to a JScrollPane containing tables.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
    }
}
