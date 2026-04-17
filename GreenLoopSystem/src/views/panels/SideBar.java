package views.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class SideBar extends JPanel {
    private static final int SIDE_BAR_WIDTH = 220;
    private static final String HOVER_LISTENER_KEY = "sidebar.hover.listener";

    private final String roleName;
    private final ArrayList<JButton> buttons = new ArrayList<>();
    private final Map<String, JButton> buttonMap = new LinkedHashMap<>();
    private final JLabel timeLabel = new JLabel();
    private JButton activeButton;

    public SideBar(String roleName) {
        this.roleName = roleName == null ? "" : roleName;
        createUIComponents();
        startClock();
    }

    public ArrayList<JButton> getButtons() {
        return buttons;
    }

    public void setActiveItem(String label) {
        JButton nextActive = buttonMap.get(label);
        if (nextActive == null || nextActive == activeButton) {
            return;
        }
        if (activeButton != null) {
            styleMenuButton(activeButton, false);
        }
        activeButton = nextActive;
        styleMenuButton(activeButton, true);
    }

    private void createUIComponents() {
        setLayout(null);
        setBackground(DashboardTheme.SIDEBAR_GREEN);
        setOpaque(true);

        int x = 16;
        int y = 18;
        int width = SIDE_BAR_WIDTH - 32;

        JLabel brandLabel = new JLabel("GreenLoop");
        brandLabel.setBounds(x, y, width, 34);
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(brandLabel);

        JLabel roleBadge = new JLabel(roleName.toUpperCase(Locale.ENGLISH));
        roleBadge.setBounds(x, y + 40, width, 28);
        roleBadge.setOpaque(true);
        roleBadge.setBackground(new Color(255, 255, 255, 55));
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roleBadge.setBorder(new EmptyBorder(6, 10, 6, 10));
        add(roleBadge);

        JLabel dateLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.ENGLISH)));
        dateLabel.setBounds(x, y + 84, width, 20);
        dateLabel.setForeground(new Color(232, 245, 233));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setOpaque(false);
        add(dateLabel);

        timeLabel.setBounds(x, y + 104, width, 20);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeLabel.setOpaque(false);
        add(timeLabel);

        y += 142;

        addMenuButton("Home", x, y, width);
        y += 42;
        addMenuButton("My Assignments", x, y, width);
        y += 42;

        if (isAdminOrManager()) {
            addMenuButton("Manage Staff", x, y, width);
            y += 42;
            addMenuButton("Manage Roles", x, y, width);
            y += 42;
        }

        if (canManageBusinessModules()) {
            addMenuButton("Manage Clients", x, y, width);
            y += 42;
            addMenuButton("Manage Suppliers", x, y, width);
            y += 42;
            addMenuButton("Product Catalogue", x, y, width);
            y += 42;
            addMenuButton("Manage Inventory", x, y, width);
            y += 42;
            addMenuButton("Manage Orders", x, y, width);
            y += 42;
            addMenuButton("Delivery Assignments", x, y, width);
            y += 42;
            addMenuButton("Manage Notifications", x, y, width);
            y += 42;
            addMenuButton("Sales & Inventory Reports", x, y, width);
            y += 42;
            addMenuButton("Email Settings", x, y, width);
            y += 42;
        }

        setPreferredSize(new Dimension(SIDE_BAR_WIDTH, y + 18));
        setActiveItem("Home");
    }

    private void addMenuButton(String text, int x, int y, int width) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, 36);
        styleMenuButton(button, false);
        add(button);
        buttons.add(button);
        buttonMap.put(text, button);
    }

    private void styleMenuButton(JButton button, boolean active) {
        button.setBackground(active ? DashboardTheme.SIDEBAR_GREEN_ACTIVE : new Color(76, 175, 80));
        button.setForeground(active ? new Color(27, 67, 50) : Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(9, 14, 9, 14));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getDefaultCursor());
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);

        java.awt.event.MouseListener oldHoverListener = (java.awt.event.MouseListener) button.getClientProperty(HOVER_LISTENER_KEY);
        if (oldHoverListener != null) {
            button.removeMouseListener(oldHoverListener);
        }
        java.awt.event.MouseAdapter hoverListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(DashboardTheme.SIDEBAR_GREEN_HOVER);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(new Color(76, 175, 80));
                }
            }
        };
        button.addMouseListener(hoverListener);
        button.putClientProperty(HOVER_LISTENER_KEY, hoverListener);
    }

    private boolean isAdminOrManager() {
        String lowerRole = roleName.toLowerCase(Locale.ENGLISH);
        return lowerRole.contains("admin") || lowerRole.contains("manager");
    }

    private boolean canManageBusinessModules() {
        String lowerRole = roleName.toLowerCase(Locale.ENGLISH);
        return lowerRole.contains("admin") || lowerRole.contains("manager")
                || lowerRole.contains("clerk") || lowerRole.contains("officer");
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH))));
        timer.setInitialDelay(0);
        timer.start();
    }
}
