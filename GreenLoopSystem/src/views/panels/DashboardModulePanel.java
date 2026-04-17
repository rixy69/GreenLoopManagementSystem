package views.panels;

import javax.swing.*;
import java.awt.*;

public abstract class DashboardModulePanel extends JPanel {
    protected DashboardModulePanel() {
        setLayout(null);
        setOpaque(false);
    }

    protected JLabel addPageTitle(String title, String subtitle) {
        JLabel titleLabel = DashboardTheme.createPageTitle(title);
        titleLabel.setBounds(32, 20, 420, 36);
        add(titleLabel);

        JLabel subtitleLabel = DashboardTheme.createMutedLabel(subtitle);
        subtitleLabel.setBounds(34, 56, 560, 24);
        add(subtitleLabel);
        return titleLabel;
    }

    protected JPanel createCard(int x, int y, int width, int height) {
        JPanel card = DashboardTheme.createGlassCard();
        card.setBounds(x, y, width, height);
        card.setLayout(null);
        add(card);
        return card;
    }

    protected JScrollPane buildTableScrollPane(JTable table, int x, int y, int width, int height) {
        DashboardTheme.styleTable(table);
        JScrollPane scrollPane = DashboardTheme.styleScrollPane(new JScrollPane(table));
        scrollPane.setBounds(x, y, width, height);
        return scrollPane;
    }

    protected JButton buildPrimaryButton(String text, int x, int y, int width, int height) {
        JButton button = DashboardTheme.createPrimaryButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }

    protected JButton buildSecondaryButton(String text, int x, int y, int width, int height) {
        JButton button = DashboardTheme.createSecondaryButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }

    protected JButton buildDangerButton(String text, int x, int y, int width, int height) {
        JButton button = DashboardTheme.createDangerButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }

    protected JTextField buildTextField(int x, int y, int width, int height) {
        JTextField field = DashboardTheme.styleTextField(new JTextField());
        field.setBounds(x, y, width, height);
        return field;
    }
}
