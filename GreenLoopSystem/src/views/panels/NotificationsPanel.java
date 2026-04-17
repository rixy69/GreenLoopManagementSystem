package views.panels;

import controllers.DashboardController;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import models.Notification;
import models.NotificationTableModel;
import models.Part;
import models.Property;
import services.CS;
import views.DeleteSelectionDialog;
import views.ModifyNotification;

public class NotificationsPanel extends DashboardModulePanel {
    private List<Notification> notifications;
    private List<Property> properties;
    private NotificationTableModel model;
    private JTable table;
    private DashboardController dashboardController;
    private Graphics g;

    private JTextField usernameField;
    private JTextField passwordField;

    public NotificationsPanel(DashboardController dashboardController, List<Notification> notifications, List<Property> properties) {
        this.dashboardController = dashboardController;
        this.notifications = notifications;
        this.properties = properties;
        createUIComponents();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw a rectangle
        CS.drawShaedBorder(g, new Rectangle(40, 80, 850, 110));
        CS.drawVerticalDoubleLine(g, 390,390,81,189);
        CS.drawShaedBorder(g, new Rectangle(40, 500, 850, 170));
        CS.drawShaedBorder(g, new Rectangle(50, 510, 330, 150));
        CS.drawShaedBorder(g, new Rectangle(390, 510, 490, 150));
        //50 515, 380 595
    }


    private void createUIComponents() {
        model = new NotificationTableModel(notifications);
        table = new JTable(model);
        DashboardTheme.styleTable(table);

        TableRowSorter<NotificationTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        addPageTitle("Manage Notifications", "Track low-stock alerts and automation settings with clearer controls.");


        JLabel filterLabel = DashboardTheme.createSectionLabel("Find");
        filterLabel.setBounds(50, 90, 80, 22);
        this.add(filterLabel);




        //1{"ID", "Part ID", "Part Name", "Remaining Quantity", "Minimum Quantity", "Notify"};
        JLabel partIdLabel = DashboardTheme.createMutedLabel("Part ID");
        partIdLabel.setBounds(50, 120, 150, 22);
        this.add(partIdLabel);

        JTextField partIdFilterField = new JTextField();
        DashboardTheme.styleTextField(partIdFilterField);
        partIdFilterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = partIdFilterField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1)); // Column index for part id is 1
                }
            }
        });
        partIdFilterField.setBounds(50, 145, 150, 34);
        this.add(partIdFilterField);


        //2{"ID", "Part ID", "Part Name", "Remaining Quantity", "Minimum Quantity", "Notify"};
        JLabel partNameLabel = DashboardTheme.createMutedLabel("Part Name");
        partNameLabel.setBounds(220, 120, 150, 22);
        this.add(partNameLabel);


        JTextField partNameFilterField = new JTextField();
        DashboardTheme.styleTextField(partNameFilterField);
        partNameFilterField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = partNameFilterField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2)); // Column index for Part Name is 2
                }
            }
        });
        partNameFilterField.setBounds(220, 145, 150, 34);
        this.add(partNameFilterField);



        JCheckBox autoSupplier = new JCheckBox("Automatically request email orders from suppliers on low stock.");
        autoSupplier.setBounds(420, 98, 430, 24);
        DashboardTheme.styleCheckBox(autoSupplier);
        this.add(autoSupplier);

        JCheckBox autoCustomer = new JCheckBox("Automatically send email to customer after job is completed.");
        autoCustomer.setBounds(420, 126, 430, 24);
        DashboardTheme.styleCheckBox(autoCustomer);
        this.add(autoCustomer);

        JCheckBox autoEmployee = new JCheckBox("Automatically send email to employee on new job allocation.");
        autoEmployee.setBounds(420, 154, 430, 24);
        DashboardTheme.styleCheckBox(autoEmployee);
        this.add(autoEmployee);











        JScrollPane tableScrollPane = DashboardTheme.styleScrollPane(new JScrollPane(table));
        tableScrollPane.setBounds(40, 200, 850, 220);
        this.add(tableScrollPane);



        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("View/Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");

        addItem.addActionListener(e -> showModifyNotificationDialog(new Notification(), "Add New Notification"));
        editItem.addActionListener(e -> showModifyNotificationDialog(getSelectedNotification(), "Edit Notification"));
        deleteItem.addActionListener(e -> deleteSelectedNotification());

        contextMenu.add(addItem);
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showContextMenu(e);
            }
            private void showContextMenu(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        JButton addButton = DashboardTheme.createPrimaryButton("Add");
        addButton.setBounds(40, 440, 110, 36);
        addButton.addActionListener(e -> showModifyNotificationDialog(new Notification(), "Add New Notification"));
        this.add(addButton);

        JButton editButton = DashboardTheme.createSecondaryButton("Edit");
        editButton.setBounds(160, 440, 110, 36);
        editButton.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Notification notification = notifications.get(modelRow);
                showModifyNotificationDialog(notification, "Edit Notification");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a notification to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        this.add(editButton);

        JButton deleteButton = DashboardTheme.createDangerButton("Delete");
        deleteButton.setBounds(280, 440, 110, 36);
        deleteButton.addActionListener(e -> showDeleteDialog());
        this.add(deleteButton);

        JLabel emailSettingsTitleLabel = DashboardTheme.createSectionLabel("Email Settings");
        emailSettingsTitleLabel.setBounds(60, 520, 180, 22);
        this.add(emailSettingsTitleLabel);

        JLabel usernameLabel = DashboardTheme.createMutedLabel("Username");
        usernameLabel.setBounds(60, 555, 150, 22);
        this.add(usernameLabel);

        usernameField = new JTextField();
        DashboardTheme.styleTextField(usernameField);
        usernameField.setBounds(220, 549, 150, 34);
        this.add(usernameField);

        JLabel passwordLabel = DashboardTheme.createMutedLabel("Password");
        passwordLabel.setBounds(60, 595, 150, 22);
        this.add(passwordLabel);


        passwordField = new JTextField();
        DashboardTheme.styleTextField(passwordField);
        passwordField.setBounds(220, 589, 150, 34);
        this.add(passwordField);

        JButton saveCredentials = DashboardTheme.createPrimaryButton("Save");
        saveCredentials.setBounds(220, 630, 84, 34);
        this.add(saveCredentials);

        JButton resetCredentials = DashboardTheme.createSecondaryButton("Reset");
        resetCredentials.setBounds(314, 630, 84, 34);
        this.add(resetCredentials);




    }

    private void showModifyNotificationDialog(Notification notification, String title) {
        ModifyNotification modifyNotification = new ModifyNotification(this, title, notification);
        modifyNotification.setVisible(true);
    }

    private Notification getSelectedNotification() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            return notifications.get(modelRow);
        }
        return null;
    }

    private void deleteSelectedNotification() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Notification notification = notifications.get(modelRow);
            boolean deleted = dashboardController.crudNotification(notification, 'd');
            if (deleted) {
                notifications.remove(modelRow);
                model.fireTableRowsDeleted(modelRow, modelRow);
                JOptionPane.showMessageDialog(this, "Notification deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete notification.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshTable() {
        model.fireTableDataChanged();
    }


    public boolean saveNotification(Notification notification) {
        boolean created = this.dashboardController.crudNotification(notification, 'c');
        if (created) {
            List<Notification> notifications_ = dashboardController.getAllNotifications();
            Collections.sort(notifications_, Comparator.comparingInt(Notification::getNotificationId));
            notifications.add(notifications_.get(notifications_.size()-1));

            int newRowIndex = notifications.size() - 1;
            model.fireTableRowsInserted(newRowIndex, newRowIndex);
            this.getParent().repaint();
        }
        return created;
    }

    public boolean updateNotification(Notification updatedNotification) {
        boolean updated = dashboardController.crudNotification(updatedNotification, 'u');
        if (updated) {
            int notificationIndex = -1;
            for (int i = 0; i < notifications.size(); i++) {
                if (notifications.get(i).getPartId() == updatedNotification.getPartId()) {
                    notificationIndex = i;
                    break;
                }
            }

            // Update the notification in the list
            if (notificationIndex != -1) {
                notifications.set(notificationIndex, updatedNotification);
                model.fireTableRowsUpdated(notificationIndex, notificationIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Notification not found");
            }
        }
        return updated;
    }

    public Part getPartByID(int id) {
        Part part = new Part();
        part.setPartId(id);
        if(dashboardController.crudPart(part, 'r')){
            return part;
        }else {
            return null;
        }
    }

    public void sendOrderRequestEmail(int partId) {
        dashboardController.sendOrderRequestEmail(partId);
    }

    private void showDeleteDialog() {
        DeleteSelectionDialog<Notification> dialog = new DeleteSelectionDialog<>(
                this,
                "Delete Notification",
                notifications,
                new String[]{"Notification ID", "Part ID", "Part Name", "Minimum Quantity"},
                notification -> new Object[]{notification.getNotificationId(), notification.getPartId(), notification.getPartName(), notification.getMinQuantity()},
                "Filter by Name",
                0,
                2,
                notification -> dashboardController.crudNotification(notification, 'd'),
                this::removeNotificationFromTable
        );
        dialog.setVisible(true);
    }

    private void removeNotificationFromTable(Notification notification) {
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getNotificationId() == notification.getNotificationId()) {
                notifications.remove(i);
                model.fireTableRowsDeleted(i, i);
                repaint();
                return;
            }
        }
    }
}
