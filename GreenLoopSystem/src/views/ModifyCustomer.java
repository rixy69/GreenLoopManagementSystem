package views;

import models.Customer;
import services.CS;
import views.FooterPanel;
import views.panels.CustomersPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ModifyCustomer extends JFrame {
    private final CustomersPanel parentView;
    private final Customer customer;

    private JTextField nameField;
    private TextArea addressArea;
    private JTextField mobileField;
    private JTextField emailField;

    public ModifyCustomer(CustomersPanel parentView, String title, Customer customer) {
        super(title);
        this.parentView = parentView;
        this.customer = customer;

        this.setSize(400, 400);
        this.setLocation(200, 100);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.createUIComponents();
        FooterPanel footerLabel = CS.paintFooter((JPanel) getContentPane());
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
        this.populateFieldsFromCustomer();
    }

    private void createUIComponents() {
        int x = 40, y = 40, lW = 100, lH = 25, fW = 200, fH = 25, g = 10, d = 10;

        JLabel nameLabel = CS.paintLabels(this, "", "Name", null, null, 2, false);
        nameLabel.setBounds(x, y, lW, lH);
        nameField = new JTextField();
        nameField.setBounds(x + lW + g, y, fW, fH);
        this.add(nameField);
        y += lH + d;

        JLabel addressLabel = CS.paintLabels(this, "", "Address", null, null, 2, false);
        addressLabel.setBounds(x, y, lW, lH);
        addressArea = new TextArea();
        addressArea.setBounds(x + lW + g, y, fW, 3 * fH);
        this.add(addressArea);
        y += 3 * fH + d;

        JLabel mobileLabel = CS.paintLabels(this, "", "Mobile", null, null, 2, false);
        mobileLabel.setBounds(x, y, lW, lH);
        mobileField = new JTextField();
        mobileField.setBounds(x + lW + g, y, fW, fH);
        this.add(mobileField);
        y += lH + d;

        JLabel emailLabel = CS.paintLabels(this, "", "Email", null, null, 2, false);
        emailLabel.setBounds(x, y, lW, lH);
        emailField = new JTextField();
        emailField.setBounds(x + lW + g, y, fW, fH);
        this.add(emailField);
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
                updateCustomerFromFields();
                boolean isNewCustomer = customer.getCustomerId() == 0;
                if (isNewCustomer) {
                    boolean created = parentView.createCustomer(customer);
                    if (created) {
                        JOptionPane.showMessageDialog(null, "Customer created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create customer.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    boolean updated = parentView.updateCustomer(customer);
                    if (updated) {
                        JOptionPane.showMessageDialog(null, "Customer updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void populateFieldsFromCustomer() {
        nameField.setText(customer.getName());
        addressArea.setText(customer.getAddress());
        mobileField.setText(customer.getMobile());
        emailField.setText(customer.getEmail());
    }

    private void updateCustomerFromFields() {
        customer.setName(nameField.getText());
        customer.setAddress(addressArea.getText());
        customer.setMobile(mobileField.getText());
        customer.setEmail(emailField.getText());
    }
}
