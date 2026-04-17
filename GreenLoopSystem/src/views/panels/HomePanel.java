package views.panels;

import models.Employee;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends DashboardModulePanel {
    private final Employee employee;

    public HomePanel(Employee employee) {
        this.employee = employee;
        createUI();
    }

    private void createUI() {
        addPageTitle("Home", "A clear overview of your GreenLoop account and role.");

        JPanel welcomeCard = createCard(30, 98, 860, 100);
        JLabel welcomeLabel = new JLabel("Welcome back, " + safe(employee.getFirstName()) + " " + safe(employee.getLastName()));
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(27, 67, 50));
        welcomeLabel.setBounds(24, 18, 720, 32);
        welcomeCard.add(welcomeLabel);

        JLabel subLabel = DashboardTheme.createMutedLabel("Your eco-friendly operations dashboard is ready.");
        subLabel.setBounds(24, 54, 540, 22);
        welcomeCard.add(subLabel);

        JPanel profileCard = createCard(30, 220, 410, 390);
        JLabel profileTitle = DashboardTheme.createSectionLabel("Profile Snapshot");
        profileTitle.setBounds(24, 20, 220, 24);
        profileCard.add(profileTitle);

        JPanel accessCard = createCard(480, 220, 410, 390);
        JLabel accessTitle = DashboardTheme.createSectionLabel("Access & Role");
        accessTitle.setBounds(24, 20, 220, 24);
        accessCard.add(accessTitle);

        addRow(profileCard, "Employee ID", String.valueOf(employee.getEmployeeId()), 60);
        addRow(profileCard, "Title", safe(employee.getTitle()), 110);
        addRow(profileCard, "First Name", safe(employee.getFirstName()), 160);
        addRow(profileCard, "Last Name", safe(employee.getLastName()), 210);
        addRow(profileCard, "Mobile", safe(employee.getMobile()), 260);
        addRow(profileCard, "Email", safe(employee.getEmail()), 310);

        addRow(accessCard, "Username", safe(employee.getUsername()), 60);
        addRow(accessCard, "Address", safe(employee.getAddress()), 110);
        addRow(accessCard, "Schedule", safe(employee.getSchedule()), 160);
        addRow(accessCard, "Role", employee.getRole() == null ? "" : safe(employee.getRole().getRoleName()), 210);

        JLabel noteTitle = DashboardTheme.createSectionLabel("GreenLoop Focus");
        noteTitle.setBounds(24, 272, 180, 22);
        accessCard.add(noteTitle);

        JTextArea note = new JTextArea("Keep operations moving with cleaner inventory, faster service, and a shared eco-friendly dashboard experience.");
        note.setEditable(false);
        note.setLineWrap(true);
        note.setWrapStyleWord(true);
        note.setOpaque(false);
        note.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        note.setForeground(new Color(77, 99, 88));
        note.setBounds(24, 304, 340, 60);
        accessCard.add(note);
    }

    private void addRow(JPanel card, String key, String value, int y) {
        JLabel keyLabel = DashboardTheme.createMutedLabel(key);
        keyLabel.setBounds(24, y, 140, 20);
        card.add(keyLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        valueLabel.setForeground(new Color(27, 67, 50));
        valueLabel.setBounds(24, y + 18, 330, 24);
        card.add(valueLabel);
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}
