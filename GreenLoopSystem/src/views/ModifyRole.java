package views;

import models.Role;
import services.CS;
import views.FooterPanel;
import views.panels.RolesPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ModifyRole extends JFrame {
    private final RolesPanel parentView;
    private final Role role;

    // Instance variables for input fields
    private JTextField roleIdField;
    private JTextField roleNameField;

    public ModifyRole(RolesPanel parentView, String title, Role role) {
        super(title);
        this.parentView = parentView;
        this.role = role;

        this.setSize(400, 250);
        this.setLocation(200, 100);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.createUIComponents();
        FooterPanel footerLabel = CS.paintFooter(this);
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
        this.populateFieldsFromRole();
    }

    private void createUIComponents() {
        int x = 40, y = 40, lW = 100, lH = 25, fW = 200, fH = 25, g = 10, d = 10;

        JLabel roleIdLabel = CS.paintLabels(this, "", "Role ID", null, null, 2, false);
        roleIdLabel.setBounds(x, y, lW, lH);
        roleIdField = new JTextField(String.valueOf(role.getRoleId()));
        roleIdField.setBounds(x + lW + g, y, fW, fH);
        roleIdField.setEditable(false);
        this.add(roleIdField);
        y += lH + d;

        JLabel roleNameLabel = CS.paintLabels(this, "", "Role Name", null, null, 2, false);
        roleNameLabel.setBounds(x, y, lW, lH);
        roleNameField = new JTextField();
        roleNameField.setBounds(x + lW + g, y, fW, fH);
        this.add(roleNameField);
        y += lH + d;

        y += 35;
        int bW = 80, bH = 25;
        int g_2 = 15; // gap between buttons
        int g_1 = fW - 2 * bW - g_2; // gap before buttons

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(x + lW + g + g_1, y, bW, bH);
        this.add(saveBtn);

        saveBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRoleFromFields();
                boolean isNewRole = role.getRoleId() == 0;
                if (isNewRole) {
                    boolean created = parentView.createRole(role);
                    if (created) {
                        JOptionPane.showMessageDialog(null, "Role created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create role.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    boolean updated = parentView.updateRole(role);
                    if (updated) {
                        JOptionPane.showMessageDialog(null, "Role updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update role.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton cancelBtn = new JButton("Exit");
        cancelBtn.setBounds(x + lW + g + g_1 + bW + g_2, y, bW, bH);
        this.add(cancelBtn);

        cancelBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    // Method to populate fields from the role object
    private void populateFieldsFromRole() {
        roleNameField.setText(role.getRoleName());
    }

    // Method to update the role object from the fields
    private void updateRoleFromFields() {
        role.setRoleName(roleNameField.getText());
    }
}
