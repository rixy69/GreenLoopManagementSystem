package views.panels;

import models.Employee;
import services.ResourceHelper;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class TopBar extends JPanel {
    private final JLabel titleLabel;
    private final JLabel subtitleLabel;
    private final JLabel userLabel;
    private final JLabel userMetaLabel;
    private final JLabel avatarLabel;
    private final JButton logoutButton;
    private final JButton settingsButton;
    private final JPopupMenu settingsPopup;
    private final JLabel popupAvatarLabel;
    private final JTextField usernameField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField emailField;
    private final JTextField mobileField;
    private final JCheckBox notificationsCheckBox;
    private final JCheckBox compactHeaderCheckBox;
    private final JCheckBox roleBadgeCheckBox;
    private Employee currentEmployee;
    private Image profileImage;

    public TopBar(Employee employee) {
        this.currentEmployee = employee;
        setLayout(new BorderLayout(16, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(14, 22, 14, 22));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        titleLabel = DashboardTheme.createPageTitle("GreenLoop Dashboard");
        subtitleLabel = DashboardTheme.createMutedLabel("Eco-friendly operations dashboard");
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        actionsPanel.setOpaque(false);

        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(44, 44));
        avatarLabel.setMinimumSize(new Dimension(44, 44));
        avatarLabel.setMaximumSize(new Dimension(44, 44));

        JPanel userTextPanel = new JPanel();
        userTextPanel.setOpaque(false);
        userTextPanel.setLayout(new BoxLayout(userTextPanel, BoxLayout.Y_AXIS));

        userLabel = new JLabel();
        userLabel.setForeground(new Color(27, 67, 50));
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        userMetaLabel = new JLabel();
        userMetaLabel.setForeground(DashboardTheme.SOFT_TEXT);
        userMetaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        userTextPanel.add(userLabel);
        userTextPanel.add(Box.createVerticalStrut(2));
        userTextPanel.add(userMetaLabel);

        JPanel userCard = new RoundedPanel(new Color(255, 255, 255, 214), new Color(255, 255, 255, 120), 24);
        userCard.setLayout(new BoxLayout(userCard, BoxLayout.X_AXIS));
        userCard.setBorder(new EmptyBorder(8, 10, 8, 12));
        userCard.add(avatarLabel);
        userCard.add(Box.createHorizontalStrut(10));
        userCard.add(userTextPanel);

        settingsButton = createIconButton();
        logoutButton = DashboardTheme.createSecondaryButton("Logout");
        logoutButton.setPreferredSize(new Dimension(96, 40));

        settingsPopup = new JPopupMenu();
        settingsPopup.setOpaque(false);
        settingsPopup.setBorder(BorderFactory.createEmptyBorder());

        popupAvatarLabel = new JLabel();
        popupAvatarLabel.setPreferredSize(new Dimension(84, 84));
        popupAvatarLabel.setMinimumSize(new Dimension(84, 84));
        popupAvatarLabel.setMaximumSize(new Dimension(84, 84));

        usernameField = createPopupField();
        firstNameField = createPopupField();
        lastNameField = createPopupField();
        emailField = createPopupField();
        mobileField = createPopupField();
        notificationsCheckBox = createSettingsCheckBox("Email notifications");
        compactHeaderCheckBox = createSettingsCheckBox("Compact header mode");
        roleBadgeCheckBox = createSettingsCheckBox("Show role details");

        JPanel popupContent = buildSettingsPopup();
        settingsPopup.add(popupContent);

        settingsButton.addActionListener(e -> toggleSettingsPopup());

        actionsPanel.add(userCard);
        actionsPanel.add(settingsButton);
        actionsPanel.add(logoutButton);

        add(titlePanel, BorderLayout.WEST);
        add(actionsPanel, BorderLayout.EAST);

        refreshUserDisplay();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(255, 255, 255, 188));
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        g2.setColor(new Color(255, 255, 255, 140));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        g2.dispose();
        super.paintComponent(g);
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public void setPageTitle(String title) {
        subtitleLabel.setText(title.equals("Home") ? "Eco-friendly operations dashboard" : title);
    }

    public void setCurrentUser(Employee employee) {
        this.currentEmployee = employee;
        refreshUserDisplay();
    }

    private JPanel buildSettingsPopup() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel card = new ShadowPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel panelTitle = new JLabel("Profile Settings");
        panelTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelTitle.setForeground(new Color(27, 67, 50));
        card.add(panelTitle);
        card.add(Box.createVerticalStrut(14));

        JPanel profileRow = new JPanel();
        profileRow.setOpaque(false);
        profileRow.setLayout(new BoxLayout(profileRow, BoxLayout.X_AXIS));
        profileRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileRow.add(popupAvatarLabel);
        profileRow.add(Box.createHorizontalStrut(14));

        JPanel profileActions = new JPanel();
        profileActions.setOpaque(false);
        profileActions.setLayout(new BoxLayout(profileActions, BoxLayout.Y_AXIS));

        JLabel photoTitle = DashboardTheme.createSectionLabel("Profile Picture");
        photoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel photoSubtitle = DashboardTheme.createMutedLabel("Choose an image and preview it instantly.");
        photoSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton changePhotoButton = DashboardTheme.createPrimaryButton("Change Picture");
        changePhotoButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        changePhotoButton.setMaximumSize(new Dimension(150, 34));
        changePhotoButton.addActionListener(e -> chooseProfileImage());

        profileActions.add(photoTitle);
        profileActions.add(Box.createVerticalStrut(4));
        profileActions.add(photoSubtitle);
        profileActions.add(Box.createVerticalStrut(10));
        profileActions.add(changePhotoButton);

        profileRow.add(profileActions);
        card.add(profileRow);
        card.add(Box.createVerticalStrut(18));

        card.add(createFieldGroup("Username", usernameField));
        card.add(Box.createVerticalStrut(10));
        card.add(createFieldGroup("First Name", firstNameField));
        card.add(Box.createVerticalStrut(10));
        card.add(createFieldGroup("Last Name", lastNameField));
        card.add(Box.createVerticalStrut(10));
        card.add(createFieldGroup("Email", emailField));
        card.add(Box.createVerticalStrut(10));
        card.add(createFieldGroup("Mobile", mobileField));
        card.add(Box.createVerticalStrut(16));

        JLabel extraTitle = DashboardTheme.createSectionLabel("Additional Settings");
        extraTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(extraTitle);
        card.add(Box.createVerticalStrut(10));
        notificationsCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        compactHeaderCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleBadgeCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(notificationsCheckBox);
        card.add(Box.createVerticalStrut(6));
        card.add(compactHeaderCheckBox);
        card.add(Box.createVerticalStrut(6));
        card.add(roleBadgeCheckBox);
        card.add(Box.createVerticalStrut(16));

        JPanel footerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerActions.setOpaque(false);
        footerActions.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton closeButton = DashboardTheme.createSecondaryButton("Close");
        closeButton.addActionListener(e -> settingsPopup.setVisible(false));
        JButton saveButton = DashboardTheme.createPrimaryButton("Save Changes");
        saveButton.addActionListener(e -> saveProfileChanges());
        footerActions.add(closeButton);
        footerActions.add(saveButton);
        card.add(footerActions);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createFieldGroup(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = DashboardTheme.createMutedLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(290, 38));
        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(field);
        return panel;
    }

    private JTextField createPopupField() {
        JTextField field = new JTextField();
        DashboardTheme.styleTextField(field);
        return field;
    }

    private JCheckBox createSettingsCheckBox(String text) {
        return DashboardTheme.styleCheckBox(new JCheckBox(text));
    }

    private JButton createIconButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(241, 248, 233) : new Color(255, 255, 255, 214));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(165, 214, 167));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(new Color(39, 83, 53));
                g2.drawOval(cx - 7, cy - 7, 14, 14);
                for (int i = 0; i < 8; i++) {
                    double angle = i * (Math.PI / 4);
                    int x1 = (int) (cx + Math.cos(angle) * 11);
                    int y1 = (int) (cy + Math.sin(angle) * 11);
                    int x2 = (int) (cx + Math.cos(angle) * 15);
                    int y2 = (int) (cy + Math.sin(angle) * 15);
                    g2.drawLine(x1, y1, x2, y2);
                }
                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(40, 40));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void toggleSettingsPopup() {
        if (settingsPopup.isVisible()) {
            settingsPopup.setVisible(false);
            return;
        }
        refreshPopupFields();
        settingsPopup.show(settingsButton, -270, settingsButton.getHeight() + 6);
    }

    private void chooseProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(SwingUtilities.getWindowAncestor(this));
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Image selectedImage = new ImageIcon(file.getAbsolutePath()).getImage();
            if (selectedImage != null && selectedImage.getWidth(null) > 0) {
                profileImage = selectedImage;
                refreshAvatarLabels();
            }
        }
    }

    private void saveProfileChanges() {
        if (currentEmployee == null) {
            return;
        }
        currentEmployee.setUsername(usernameField.getText().trim());
        currentEmployee.setFirstName(firstNameField.getText().trim());
        currentEmployee.setLastName(lastNameField.getText().trim());
        currentEmployee.setEmail(emailField.getText().trim());
        currentEmployee.setMobile(mobileField.getText().trim());
        refreshUserDisplay();
        settingsPopup.setVisible(false);
    }

    private void refreshPopupFields() {
        if (currentEmployee == null) {
            usernameField.setText("");
            firstNameField.setText("");
            lastNameField.setText("");
            emailField.setText("");
            mobileField.setText("");
            return;
        }
        usernameField.setText(safe(currentEmployee.getUsername()));
        firstNameField.setText(safe(currentEmployee.getFirstName()));
        lastNameField.setText(safe(currentEmployee.getLastName()));
        emailField.setText(safe(currentEmployee.getEmail()));
        mobileField.setText(safe(currentEmployee.getMobile()));
        notificationsCheckBox.setSelected(true);
        compactHeaderCheckBox.setSelected(false);
        roleBadgeCheckBox.setSelected(true);
        refreshAvatarLabels();
    }

    private void refreshUserDisplay() {
        userLabel.setText(buildDisplayName(currentEmployee));
        userMetaLabel.setText(buildMetaText(currentEmployee));
        refreshPopupFields();
    }

    private void refreshAvatarLabels() {
        avatarLabel.setIcon(new ImageIcon(createAvatarImage(44)));
        popupAvatarLabel.setIcon(new ImageIcon(createAvatarImage(84)));
    }

    private Image createAvatarImage(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape clip = new Ellipse2D.Double(0, 0, size, size);
        g2.setClip(clip);

        Image source = resolveProfileImage();
        if (source != null) {
            g2.drawImage(source, 0, 0, size, size, null);
            g2.setColor(new Color(46, 125, 50, 40));
            g2.fillOval(0, 0, size, size);
        } else {
            g2.setColor(new Color(200, 230, 201));
            g2.fillOval(0, 0, size, size);
            g2.setColor(new Color(27, 67, 50));
            g2.setFont(new Font("Segoe UI", Font.BOLD, Math.max(14, size / 3)));
            String initials = buildInitials(currentEmployee);
            FontMetrics fm = g2.getFontMetrics();
            int textX = (size - fm.stringWidth(initials)) / 2;
            int textY = (size - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(initials, textX, textY);
        }

        g2.setClip(null);
        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(1, 1, size - 3, size - 3);
        g2.dispose();
        return image;
    }

    private Image resolveProfileImage() {
        if (profileImage != null) {
            return profileImage;
        }
        return ResourceHelper.loadImage(
                TopBar.class,
                "/images/logo.png",
                "GreenLoopSystem/resources/images/logo.png",
                "resources/images/logo.png"
        );
    }

    private String buildDisplayName(Employee employee) {
        if (employee == null) {
            return "GreenLoop User";
        }
        String fullName = (safe(employee.getFirstName()) + " " + safe(employee.getLastName())).trim();
        if (!fullName.isEmpty()) {
            return fullName;
        }
        if (!safe(employee.getUsername()).isEmpty()) {
            return employee.getUsername();
        }
        return "GreenLoop User";
    }

    private String buildMetaText(Employee employee) {
        if (employee == null || employee.getRole() == null) {
            return "Dashboard access";
        }
        return safe(employee.getUsername()) + " | " + employee.getRole().getRoleName();
    }

    private String buildInitials(Employee employee) {
        String first = employee == null ? "" : safe(employee.getFirstName());
        String last = employee == null ? "" : safe(employee.getLastName());
        String initials = "";
        if (!first.isEmpty()) {
            initials += Character.toUpperCase(first.charAt(0));
        }
        if (!last.isEmpty()) {
            initials += Character.toUpperCase(last.charAt(0));
        }
        return initials.isEmpty() ? "GL" : initials;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private static final class RoundedPanel extends JPanel {
        private final Color fillColor;
        private final Color borderColor;
        private final int arc;

        private RoundedPanel(Color fillColor, Color borderColor, int arc) {
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class ShadowPanel extends JPanel {
        private ShadowPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(320, 520));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(39, 83, 53, 28));
            g2.fillRoundRect(8, 10, getWidth() - 16, getHeight() - 18, 26, 26);
            g2.setColor(new Color(255, 255, 255, 245));
            g2.fillRoundRect(0, 0, getWidth() - 16, getHeight() - 18, 26, 26);
            g2.setColor(new Color(221, 237, 222));
            g2.drawRoundRect(0, 0, getWidth() - 16, getHeight() - 18, 26, 26);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
