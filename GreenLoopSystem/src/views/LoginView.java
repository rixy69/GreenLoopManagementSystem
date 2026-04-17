package views;

import services.ResourceHelper;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginView extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameText;
    private JPasswordField passwordText;
    private JButton loginButton;
    private JButton cancelButton;

    public JTextField getUsernameText() {
        return usernameText;
    }

    public JPasswordField getPasswordText() {
        return passwordText;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public LoginView() {
        createUIComponents();
    }

    private void createUIComponents() {
        final Image backgroundImage = ResourceHelper.loadImage(
                getClass(),
                "/resources/images/ecofriendly_2.jpeg",
                "resources/images/ecofriendly_2.jpeg",
                "GreenLoopSystem/resources/images/ecofriendly_2.jpeg",
                "resources/images/ecofriendly 2.jpeg",
                "GreenLoopSystem/resources/images/ecofriendly 2.jpeg"
        );

        mainPanel = new BackgroundPanel(backgroundImage);
        mainPanel.setLayout(new BorderLayout());

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel loginCard = new RoundedCardPanel(30, new Color(255, 255, 255, 217));
        loginCard.setOpaque(false);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(new EmptyBorder(28, 30, 28, 30));
        loginCard.setPreferredSize(new Dimension(430, 470));

        JLabel titleLabel = new JLabel("GREENLOOP SYSTEM");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(27, 67, 50));

        JLabel subtitleLabel = new JLabel("Eco Packaging Management");
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(76, 96, 86));

        JLabel descriptionLabel = new JLabel("<html>Sign in to continue managing sustainable packaging, inventory, and customer operations.</html>");
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionLabel.setForeground(new Color(95, 110, 103));

        JLabel usernameLabel = createFieldLabel("Username");
        usernameText = createTextField();

        JLabel passwordLabel = createFieldLabel("Password");
        passwordText = createPasswordField();

        loginButton = new JButton("Login");
        styleButton(loginButton, new Color(46, 125, 50), new Color(27, 94, 32), Color.WHITE);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(236, 239, 241), new Color(207, 216, 220), new Color(55, 71, 79));
        cancelButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        buttonRow.add(loginButton);
        buttonRow.add(cancelButton);

        loginCard.add(titleLabel);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(subtitleLabel);
        loginCard.add(Box.createVerticalStrut(14));
        loginCard.add(descriptionLabel);
        loginCard.add(Box.createVerticalStrut(24));
        loginCard.add(usernameLabel);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(usernameText);
        loginCard.add(Box.createVerticalStrut(18));
        loginCard.add(passwordLabel);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(passwordText);
        loginCard.add(Box.createVerticalStrut(26));
        loginCard.add(buttonRow);

        centerWrapper.add(loginCard);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        mainPanel.add(new FooterPanel(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setTitle("GreenLoop System Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 680));
        setBounds(50, 50, 1200, 680);
        setLocationRelativeTo(null);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(39, 55, 48));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setPreferredSize(new Dimension(360, 44));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(248, 250, 249));
        field.setForeground(new Color(33, 33, 33));
        field.setCaretColor(new Color(46, 125, 50));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 220, 212), 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setPreferredSize(new Dimension(360, 44));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(248, 250, 249));
        field.setForeground(new Color(33, 33, 33));
        field.setCaretColor(new Color(46, 125, 50));
        field.setEchoChar('*');
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 220, 212), 1, true),
                new EmptyBorder(10, 14, 10, 14)
        ));
        return field;
    }

    private void styleButton(JButton button, Color baseColor, Color hoverColor, Color textColor) {
        button.setBackground(baseColor);
        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }

    private static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        private BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            if (backgroundImage != null) {
                g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                GradientPaint paint = new GradientPaint(0, 0, new Color(41, 84, 54), getWidth(), getHeight(), new Color(19, 44, 29));
                g2.setPaint(paint);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            g2.setColor(new Color(0, 0, 0, 128));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    private static class RoundedCardPanel extends JPanel {
        private final int arc;
        private final Color backgroundColor;

        private RoundedCardPanel(int arc, Color backgroundColor) {
            this.arc = arc;
            this.backgroundColor = backgroundColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 35));
            g2.fill(new RoundRectangle2D.Double(8, 10, getWidth() - 16, getHeight() - 12, arc, arc));
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arc, arc));
            g2.setColor(new Color(255, 255, 255, 140));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 2.0, getHeight() - 2.0, arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
