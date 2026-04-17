package views;

import models.Inventory;
import models.Part;
import models.Supplier;
import services.CS;
import views.FooterPanel;
import views.panels.InventoryPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ModifyInventory extends JFrame {
    private Inventory inventory;
    private InventoryPanel parentView;

    // Instance variables for input fields
    private JTextField partIdField;
    private JTextField quantityField;
    private JTextField locationField;
    private Part part;




    public ModifyInventory(InventoryPanel parentView, String title, Inventory inventory, Part part) {
        super(title);
        this.parentView = parentView;
        this.inventory = inventory;
        this.part = part;

        this.setSize(440, 270);
        this.setSize(440, 525);
        this.setLayout(null);
        this.setLocation(100,100);

        this.createUIComponents();
        FooterPanel footerLabel = CS.paintFooter((JPanel) getContentPane());
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
        this.populateFieldsFromInventory();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.decode("#BBBBBB"));
        g.drawRect(45, 115, 350, 180); // (x, y, width, height)
    }


    private void createUIComponents() {
        int x = 40, y = 40, lW = 100, lH = 25, fW = 240, fH = 25, g = 10, d = 10;

        JLabel partIdLabel = new JLabel("Part ID:");
        partIdLabel.setBounds(x, y, lW, lH);
        add(partIdLabel);
        partIdField = new JTextField();
        partIdField.setBounds(x + lW + g, y, fW, fH);
        add(partIdField);

        y += lH + d;




        y += 35;



        JLabel partNameLabel = CS.paintLabels(this, "", "Part Name", "#7faab5", "#ffffff", 0, true);
        partNameLabel.setBounds(x+20, y, lW , lH);


        JLabel partNameValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        partNameValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;

        JLabel partDescriptionLabel = CS.paintLabels(this, "", "Part Description", "#7faab5", "#ffffff", 0, true);
        partDescriptionLabel.setBounds(x+20, y, lW , lH);


        JLabel partDescriptionValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        partDescriptionValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;

        JLabel partPriceLabel = CS.paintLabels(this, "", "Part Price", "#7faab5", "#ffffff", 0, true);
        partPriceLabel.setBounds(x+20, y, lW , lH);

        JLabel partPriceValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        partPriceValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;

        JLabel supplierNameLabel = CS.paintLabels(this, "", "Supplier Name", "#7faab5", "#ffffff", 0, true);
        supplierNameLabel.setBounds(x+20, y, lW , lH);


        JLabel supplierNameValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        supplierNameValueLabel.setBounds(x + 20 + lW + g, y, fW - 40 , fH);

        y += lH + d;


        if (part != null) {
            partNameValueLabel.setText(part.getName());
            partDescriptionValueLabel.setText(part.getDescription());
            partPriceValueLabel.setText(String.format("%.2f", part.getPrice()));
            if(part.getSupplier()!=null){
                supplierNameValueLabel.setText(part.getSupplier().getName());
            }
        }




        partIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = partIdField.getText().trim();
                if (!text.isEmpty()) {
                    int id = convertStringToInt(text);
                    System.out.println(id);
                    if (id > 0) {
                        part = parentView.getPartByID(id);
                        if (part != null) {
                            partNameValueLabel.setText(part.getName());
                            partDescriptionValueLabel.setText(part.getDescription());
                            partPriceValueLabel.setText(String.format("%.2f", part.getPrice()));
                            if(part.getSupplier()!=null){
                                supplierNameValueLabel.setText(part.getSupplier().getName());
                            }

                            return;
                        }
                    }
                }
                part = null;
                partNameValueLabel.setText("????");
                partDescriptionValueLabel.setText("????");
                partPriceValueLabel.setText("????");
                supplierNameValueLabel.setText("????");
            }
        });


        y+=35;


        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(x, y, lW, lH);
        add(quantityLabel);
        quantityField = new JTextField();
        quantityField.setBounds(x + lW + g, y, fW, fH);
        add(quantityField);

        y += lH + d;

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(x, y, lW, lH);
        add(locationLabel);
        locationField = new JTextField();
        locationField.setBounds(x + lW + g, y, fW, fH);
        add(locationField);

        y += lH + d;


        y += 20;

        // Create and add buttons
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(x + lW + g + 40, y, 90, fH);
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(x + lW + g + 150, y, 90, fH);
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);


        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInventoryFromFields();
                boolean isNewInventory = inventory.getInventoryId()==0 ;
                if (isNewInventory) {
                    boolean created = parentView.createInventory(inventory);
                    if (created) {
                        JOptionPane.showMessageDialog(null, "Inventory created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create inventory.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    boolean updated = parentView.updateInventory(inventory);
                    if (updated) {
                        JOptionPane.showMessageDialog(null, "Inventory updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update inventory.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });



    }

    private void populateFieldsFromInventory() {
        partIdField.setText(String.valueOf(inventory.getPartId()));
        quantityField.setText(String.valueOf(inventory.getQuantity()));
        locationField.setText(inventory.getLocation());
    }

    private void updateInventoryFromFields() {
        inventory.setPartId(Integer.parseInt(partIdField.getText()));
        inventory.setQuantity(Integer.parseInt(quantityField.getText()));
        inventory.setLocation(locationField.getText());
    }

    private void saveInventory(ActionEvent e) {
        updateInventoryFromFields();
        // Call the method to update the inventory
        // You need to implement this method in your controller
        // For example: parentView.updateInventory(inventory);
    }


    public int convertStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception ignored) {
        }
        return -1;
    }

}
