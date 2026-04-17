package views;

import models.Notification;
import models.Part;
import services.CS;
import views.FooterPanel;
import views.panels.NotificationsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ModifyNotification extends JFrame {
    private JTextField txtPartId;
    private JTextField txtMinQuantity;
    private JCheckBox chkNotify;
    private JButton btnSave;
    private JButton btnCancel;
    private Notification notification;
    private Part part;
    private NotificationsPanel notificationsPanel;

    private JTextField notificationIdField;
    private JTextField partIdField;
    private JTextField minimumQtyFiled;
    private JCheckBox notifyCheckBox;


    public ModifyNotification(NotificationsPanel notificationsPanel, String title, Notification notification) {
        super(title);
        this.notificationsPanel = notificationsPanel;
        this.notification = notification;

        this.setSize(400, 580);
        this.setLocation(200, 100);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        FooterPanel footerLabel = CS.paintFooter((JPanel) getContentPane());
        footerLabel.setBounds(0, this.getHeight() - 20, this.getWidth(), 20);
        //populateFields();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        CS.drawShaedBorder(g, new Rectangle(45, 150, 310, 145)); // (x, y, width, height)
    }


    private void initComponents() {


        int x = 40, y = 40, lW = 100, lH = 25, fW = 200, fH = 25, g = 10, d = 10;

        JLabel notificationIDLabel = CS.paintLabels(this, "", "Notification ID", null, null, 2, false);
        notificationIDLabel.setBounds(x, y, lW, lH);
        notificationIdField = new JTextField();
        notificationIdField.setBounds(x + lW + g, y, fW, fH);
        notificationIdField.setEditable(false);
        this.add(notificationIdField);
        y += lH + d;

        JLabel partIdLabel = CS.paintLabels(this, "", "Part ID", null, null, 2, false);
        partIdLabel.setBounds(x, y, lW, lH);
        partIdField = new JTextField();
        partIdField.setBounds(x + lW + g, y, fW, fH);
        this.add(partIdField);
        y += lH + d;

        y+=35;

        JLabel partIdKeyLabel = CS.paintLabels(this, "", "Part ID", "#7faab5", "#ffffff", 0, true);
        partIdKeyLabel.setBounds(x+20, y, lW -20 , lH);

        JLabel partIdValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        partIdValueLabel.setBounds(x + lW + g, y, fW -25 , lH);

        y += lH + d;

        JLabel partNameKeyLabel = CS.paintLabels(this, "", "Name", "#7faab5", "#ffffff", 0, true);
        partNameKeyLabel.setBounds(x+20, y, lW-20, lH);

        JLabel partNameValeLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        partNameValeLabel.setBounds(x + lW + g, y, fW -25 , lH);

        y += lH + d;

        JLabel availableKeyLabel = CS.paintLabels(this, "", "Available", "#7faab5", "#ffffff", 0, true);
        availableKeyLabel.setBounds(x+20, y, lW-20, lH);

        JLabel availableValueLabel = CS.paintLabels(this, "", "????", "#555555", "#ffffff", 0, true);
        availableValueLabel.setBounds(x + lW + g, y, fW -25 , lH);

        y += lH + d;
        y+=40;


        JLabel minQtyLabel = CS.paintLabels(this, "", "Minimum Qty", null, null, 2, false);
        minQtyLabel.setBounds(x, y, lW, lH);
        minimumQtyFiled = new JTextField();
        minimumQtyFiled.setBounds(x + lW + g, y, fW, fH);
        this.add(minimumQtyFiled);
        y += lH + d;


        JLabel notifyLabel = CS.paintLabels(this, "", "Notify", null, null, 2, false);
        notifyLabel.setBounds(x, y, lW, lH);
        notifyCheckBox = new JCheckBox();
        notifyCheckBox.setBounds(x + lW + g, y, fW, fH);
        this.add(notifyCheckBox);
        y += lH + d;


        if(notification!=null && notification.getNotificationId()>0){
            notificationIdField.setText(Integer.toString(notification.getNotificationId()));
            partIdField.setText(Integer.toString(notification.getPartId()));
            partIdField.setEditable(false);
            partIdValueLabel.setText(Integer.toString(notification.getPartId()));
            partNameValeLabel.setText(notification.getPartName());
            availableValueLabel.setText(Integer.toString(notification.getRemainingQuantity()));
            minimumQtyFiled.setText(Integer.toString(notification.getMinQuantity()));
            notifyCheckBox.setSelected(notification.isNotify());
            this.part = new Part();
            part.setPartId(notification.getPartId());
            part.setName(notification.getPartName());
            part.setRemainingQuantity(notification.getRemainingQuantity());
        }

        partIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = partIdField.getText().trim();
                if (!text.isEmpty()) {
                    int id = convertStringToInt(text);
                    System.out.println(id);
                    if (id > 0) {
                        part = notificationsPanel.getPartByID(id);
                        if (part != null) {
                            partIdValueLabel.setText(Integer.toString(part.getPartId()));
                            partNameValeLabel.setText(part.getName());
                            availableValueLabel.setText(Integer.toString(part.getRemainingQuantity()));
                            return;
                        }
                    }
                }
                notification = null;
                partIdValueLabel.setText("????");
                partNameValeLabel.setText("????");
                availableValueLabel.setText("????");
            }
        });

        y += 50;
        int bW = 95, bH = 25;
        int g_2 = 10; // gap between buttons
        int g_1 = fW - 2 * bW - g_2; // gap before buttons

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(x + lW + g + g_1, y, bW, bH);
        this.add(saveBtn);

        saveBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(part==null){
                    JOptionPane.showMessageDialog(null, "Invalid Part Id.", "Error", JOptionPane.ERROR_MESSAGE);
                }



                notification.setPartId(part.getPartId());
                notification.setPartName(part.getName());
                notification.setMinQuantity(Integer.parseInt(minimumQtyFiled.getText()));
                notification.setNotify(notifyCheckBox.isSelected());



                boolean saved = false;

                try {
                    if(notification.getNotificationId()==0){
                        saved = saveNotification();
                    }else {
                        saved = updateNotification();
                    }
                }catch (Exception e2){
                }


                if (saved) {
                    JOptionPane.showMessageDialog(ModifyNotification.this, "Notification saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    notificationsPanel.refreshTable(); // Refresh the table in the NotificationsPanel

                    if(part.getRemainingQuantity()<notification.getMinQuantity()){
                        int response = JOptionPane.showConfirmDialog(
                                null,
                                "Available quantity is lesser than minimum Quantity. Do you want to request supplier for item?",
                                "Minimum Quantity",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );

                        // Check the user's response
                        if (response == JOptionPane.OK_OPTION) {
                            notificationsPanel.sendOrderRequestEmail(part.getPartId());
                        }
                    }

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(ModifyNotification.this, "Failed to save notification.", "Error", JOptionPane.ERROR_MESSAGE);
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


    private boolean saveNotification() {
        return notificationsPanel.saveNotification(notification);
    }

    private boolean updateNotification() {
        return notificationsPanel.updateNotification(notification);
    }

    private void populateFields() {
        txtPartId.setText(String.valueOf(notification.getPartId()));
        txtMinQuantity.setText(String.valueOf(notification.getMinQuantity()));
        chkNotify.setSelected(notification.isNotify());
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            notification.setPartId(Integer.parseInt(txtPartId.getText()));
            notification.setMinQuantity(Integer.parseInt(txtMinQuantity.getText()));
            notification.setNotify(chkNotify.isSelected());

            boolean saved = false;

            if(notification.getNotificationId()==0){
                saved = saveNotification();
            }else {
                saved = updateNotification();
            }

            if (saved) {
                JOptionPane.showMessageDialog(ModifyNotification.this, "Notification saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                notificationsPanel.refreshTable(); // Refresh the table in the NotificationsPanel
                dispose();
            } else {
                JOptionPane.showMessageDialog(ModifyNotification.this, "Failed to save notification.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean saveNotification() {
            return notificationsPanel.saveNotification(notification);
        }

        private boolean updateNotification() {
            return notificationsPanel.updateNotification(notification);
        }



    }

    public int convertStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return -1;
        }
    }
}
