package views.panels;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class DashboardTheme {
    public static final Color SIDEBAR_GREEN = new Color(46, 125, 50);
    public static final Color SIDEBAR_GREEN_HOVER = new Color(67, 160, 71);
    public static final Color SIDEBAR_GREEN_ACTIVE = new Color(129, 199, 132);
    public static final Color PRIMARY_GREEN = new Color(46, 125, 50);
    public static final Color PRIMARY_GREEN_HOVER = new Color(56, 142, 60);
    public static final Color SOFT_TEXT = new Color(88, 110, 99);
    public static final Color CARD_BORDER = new Color(255, 255, 255, 150);
    public static final Color FIELD_BORDER = new Color(187, 222, 190);
    public static final Color DANGER = new Color(198, 40, 40);
    public static final Color DANGER_HOVER = new Color(183, 28, 28);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Border FIELD_PADDING = new EmptyBorder(8, 12, 8, 12);

    private DashboardTheme() {
    }

    public static JPanel createGlassCard() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 26, 26);
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 26, 26);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    public static JLabel createPageTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(new Color(27, 67, 50));
        return label;
    }

    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SECTION_FONT);
        label.setForeground(new Color(39, 83, 53));
        return label;
    }

    public static JLabel createMutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(SOFT_TEXT);
        return label;
    }

    public static JTextField styleTextField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setBackground(new Color(255, 255, 255, 235));
        field.setForeground(new Color(33, 33, 33));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 1, true),
                FIELD_PADDING
        ));
        field.setCaretColor(PRIMARY_GREEN);
        return field;
    }

    public static JPasswordField stylePasswordField(JPasswordField field) {
        field.setFont(BODY_FONT);
        field.setBackground(new Color(255, 255, 255, 235));
        field.setForeground(new Color(33, 33, 33));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 1, true),
                FIELD_PADDING
        ));
        field.setCaretColor(PRIMARY_GREEN);
        return field;
    }

    public static JCheckBox styleCheckBox(JCheckBox checkBox) {
        checkBox.setOpaque(false);
        checkBox.setFont(BODY_FONT);
        checkBox.setForeground(new Color(40, 60, 52));
        return checkBox;
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, PRIMARY_GREEN, PRIMARY_GREEN_HOVER, Color.WHITE);
        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, new Color(255, 255, 255, 215), new Color(241, 248, 233), new Color(39, 83, 53));
        button.setBorder(BorderFactory.createLineBorder(new Color(165, 214, 167), 1, true));
        return button;
    }

    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        styleButton(button, DANGER, DANGER_HOVER, Color.WHITE);
        return button;
    }

    public static void styleButton(JButton button, Color base, Color hover, Color text) {
        button.setBackground(base);
        button.setForeground(text);
        button.setFont(BUTTON_FONT);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(base);
            }
        });
    }

    public static void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(220, 234, 223));
        table.setSelectionBackground(new Color(200, 230, 201));
        table.setSelectionForeground(new Color(27, 67, 50));
        table.setBackground(new Color(255, 255, 255, 220));
        table.setForeground(new Color(33, 33, 33));
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(232, 245, 233));
        header.setForeground(new Color(27, 67, 50));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createEmptyBorder());
    }

    public static JScrollPane styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    public static void recursivelyStyle(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel && !(component instanceof SideBar) && !(component instanceof TopBar) && !(component instanceof BackgroundPanel)) {
                ((JPanel) component).setOpaque(false);
            }
            if (component instanceof JTable) {
                styleTable((JTable) component);
            } else if (component instanceof JScrollPane) {
                styleScrollPane((JScrollPane) component);
            } else if (component instanceof JTextField) {
                styleTextField((JTextField) component);
            } else if (component instanceof JPasswordField) {
                stylePasswordField((JPasswordField) component);
            } else if (component instanceof JCheckBox) {
                styleCheckBox((JCheckBox) component);
            }

            if (component instanceof Container) {
                recursivelyStyle((Container) component);
            }
        }
    }
}
