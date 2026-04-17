package views;

import models.Part;
import models.Supplier;
import services.CS;
import views.FooterPanel;
import views.panels.PartsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ModifyPart extends JFrame {
    private PartsPanel partsPanel;
    private Part part;

    // Instance variables for input fields
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField supplierIdField;
    private Supplier supplier;

    public ModifyPart(PartsPanel partsPanel, String title, Part part, Supplier supplier) {
        super(title);
        this.partsPanel = partsPanel;
        this.part = part;
        this.supplier = supplier;
        this.setSize(440, 570);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null); // Setting null layout

        this.createUIComponents();
        FooterPanel footerLabel = CS.paintFooter((JPanel) getContentPane());
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
        this.populateFieldsFromPart();
    }

    private void createUIComponents() {
        int x = 40, y = 50, lW = 100, lH = 25, fW = 240, fH = 25, g = 10, d = 10;

        JLabel nameLabel = CS.paintLabels(this, "", "Name:", null, null, 2, false);
        nameLabel.setBounds(x, y, lW, lH);
        nameField = new JTextField();
        nameField.setBounds(x + lW + g, y, fW, fH);
        this.add(nameField);
        y += lH + d;

        JLabel descriptionLabel = CS.paintLabels(this, "", "Description:", null, null, 2, false);
        descriptionLabel.setBounds(x, y, lW, lH);
        descriptionField = new JTextField();
        descriptionField.setBounds(x + lW + g, y, fW, fH);
        this.add(descriptionField);
        y += lH + d;

        JLabel priceLabel = CS.paintLabels(this, "", "Price:", null, null, 2, false);
        priceLabel.setBounds(x, y, lW, lH);
        priceField = new JTextField();
        priceField.setBounds(x + lW + g, y, fW, fH);
        this.add(priceField);
        y += lH + d;

        JLabel supplierIdLabel = CS.paintLabels(this, "", "Supplier ID:", null, null, 2, false);
        supplierIdLabel.setBounds(x, y, lW, lH);
        supplierIdField = new JTextField();
        supplierIdField.setBounds(x + lW + g, y, fW, fH);
        this.add(supplierIdField);
        y += lH + d;



        y += 35;



        JLabel supplierNameLabel = CS.paintLabels(this, "", "Supplier Name", "#7faab5", "#ffffff", 0, true);
        supplierNameLabel.setBounds(x+20, y, lW , lH);


        JLabel supplierNameValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        supplierNameValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;

        JLabel supplierContactNameLabel = CS.paintLabels(this, "", "Contact Name", "#7faab5", "#ffffff", 0, true);
        supplierContactNameLabel.setBounds(x+20, y, lW , lH);


        JLabel supplierContactNameValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        supplierContactNameValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;

        JLabel supplierContactEmailLabel = CS.paintLabels(this, "", "Contact Email", "#7faab5", "#ffffff", 0, true);
        supplierContactEmailLabel.setBounds(x+20, y, lW , lH);

        JLabel supplierContactEmailValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        supplierContactEmailValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;

        JLabel supplierContactPhoneLabel = CS.paintLabels(this, "", "Contact Phone", "#7faab5", "#ffffff", 0, true);
        supplierContactPhoneLabel.setBounds(x+20, y, lW , lH);


        JLabel supplierContactPhoneValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        supplierContactPhoneValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;




        y += 50;

        int bW = 80, bH = 30;
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(x + lW + 70, y, bW, bH);
        this.add(saveButton);


        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(x + lW + 70 + 2*g + bW, y, bW, bH);
        this.add(cancelButton);

        if (supplier != null) {
            supplierNameValueLabel.setText(supplier.getName());
            supplierContactNameValueLabel.setText(supplier.getContactName());
            supplierContactEmailValueLabel.setText(supplier.getContactEmail());
            supplierContactPhoneValueLabel.setText(supplier.getContactPhone());
        }

        
        supplierIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = supplierIdField.getText().trim();
                if (!text.isEmpty()) {
                    int id = convertStringToInt(text);
                    System.out.println(id);
                    if (id > 0) {
                        supplier = partsPanel.getSupplierById(id);
                        if (supplier != null) {
                            supplierNameValueLabel.setText(supplier.getName());
                            supplierContactNameValueLabel.setText(supplier.getContactName());
                            supplierContactEmailValueLabel.setText(supplier.getContactEmail());
                            supplierContactPhoneValueLabel.setText(supplier.getContactPhone());
                            return;
                        }
                    }
                }
                supplier = null;
                supplierNameValueLabel.setText("????");
                supplierContactNameValueLabel.setText("????");
                supplierContactEmailValueLabel.setText("????");
                supplierContactPhoneValueLabel.setText("????");
            }
        });

        saveButton.addActionListener(e -> {
            updatePartFromFields();
            boolean isNewPart = part.getPartId() == 0;
            if (isNewPart) {
                boolean created = partsPanel.createPart(part);
                if (created) {
                    JOptionPane.showMessageDialog(null, "Part created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create part.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                boolean updated = partsPanel.updatePart(part);
                if (updated) {
                    JOptionPane.showMessageDialog(null, "Part updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update part.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> this.dispose());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.decode("#BBBBBB"));
        g.drawRect(45, 230, 350, 180); // (x, y, width, height)
    }


    // Method to populate fields from the part object
    private void populateFieldsFromPart() {
        nameField.setText(part.getName());
        descriptionField.setText(part.getDescription());
        priceField.setText(String.valueOf(part.getPrice()));
        supplierIdField.setText(String.valueOf(part.getSupplierId()));
    }

    // Method to update the part object from the fields
    private void updatePartFromFields() {
        part.setName(nameField.getText());
        part.setDescription(descriptionField.getText());
        part.setPrice(Double.parseDouble(priceField.getText()));
        part.setSupplierId(Integer.parseInt(supplierIdField.getText()));
    }

    public int convertStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception ignored) {
        }
        return -1;
    }
}
