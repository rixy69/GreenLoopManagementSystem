package views;

import models.Supplier;
import services.CS;
import views.FooterPanel;
import views.panels.SuppliersPanel;

import javax.swing.*;
import java.awt.*;

public class ModifySupplier extends JFrame {
    private final JPanel contentPane;
    private final JTextField nameTextField;
    private final JTextField contactNameTextField;
    private final JTextField contactEmailTextField;
    private final JTextField contactPhoneTextField;
    private final JTextArea addressTextArea;
    private SuppliersPanel suppliersPanel;
    private Supplier supplier;

    public ModifySupplier(SuppliersPanel suppliersPanel, String title, Supplier supplier) {
        this.setTitle(title);
        this.supplier = supplier;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(450, 320); // Increased size

        contentPane = new JPanel(null);
        this.setContentPane(contentPane);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 20); // Increased width
        contentPane.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(120, 20, 300, 20); // Increased width
        contentPane.add(nameTextField);

        JLabel contactNameLabel = new JLabel("Contact Name:");
        contactNameLabel.setBounds(20, 50, 100, 20); // Increased width
        contentPane.add(contactNameLabel);

        contactNameTextField = new JTextField();
        contactNameTextField.setBounds(120, 50, 300, 20); // Increased width
        contentPane.add(contactNameTextField);

        JLabel contactEmailLabel = new JLabel("Contact Email:");
        contactEmailLabel.setBounds(20, 80, 100, 20); // Increased width
        contentPane.add(contactEmailLabel);

        contactEmailTextField = new JTextField();
        contactEmailTextField.setBounds(120, 80, 300, 20); // Increased width
        contentPane.add(contactEmailTextField);

        JLabel contactPhoneLabel = new JLabel("Contact Phone:");
        contactPhoneLabel.setBounds(20, 110, 100, 20); // Increased width
        contentPane.add(contactPhoneLabel);

        contactPhoneTextField = new JTextField();
        contactPhoneTextField.setBounds(120, 110, 300, 20); // Increased width
        contentPane.add(contactPhoneTextField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 140, 100, 20); // Increased width
        contentPane.add(addressLabel);

        addressTextArea = new JTextArea();
        JScrollPane addressScrollPane = new JScrollPane(addressTextArea);
        addressScrollPane.setBounds(120, 140, 300, 60); // Increased width
        contentPane.add(addressScrollPane);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(150, 220, 80, 30);
        saveButton.addActionListener(e -> {
            updateSupplierFromFields();
            boolean isNewSupplier = supplier.getSupplierId()==0 ;
            if (isNewSupplier) {
                boolean created = suppliersPanel.createSupplier(supplier);
                if (created) {
                    JOptionPane.showMessageDialog(null, "Supplier created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create supplier.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                boolean updated = suppliersPanel.updateSupplier(supplier);
                if (updated) {
                    JOptionPane.showMessageDialog(null, "Supplier updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update supplier.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(saveButton);

        JButton exitButton = new JButton("Exit"); // Added exit button
        exitButton.setBounds(250, 220, 80, 30); // Positioned next to save button
        exitButton.addActionListener(e -> {
            this.dispose(); // Close the window
        });
        contentPane.add(exitButton);

        FooterPanel footerLabel = CS.paintFooter(contentPane);
        footerLabel.setBounds(0, 300, 450, 20);

        populateFieldsFromSupplier();

    }


    private void populateFieldsFromSupplier(){
        if (supplier != null) {
            nameTextField.setText(supplier.getName());
            contactNameTextField.setText(supplier.getContactName());
            contactEmailTextField.setText(supplier.getContactEmail());
            contactPhoneTextField.setText(supplier.getContactPhone());
            addressTextArea.setText(supplier.getAddress());
        }
    }

    private void updateSupplierFromFields() {
        supplier.setName(nameTextField.getText());
        supplier.setContactName(contactNameTextField.getText());
        supplier.setContactEmail(contactEmailTextField.getText());
        supplier.setContactPhone(contactPhoneTextField.getText());
        supplier.setAddress(addressTextArea.getText());
    }

}
