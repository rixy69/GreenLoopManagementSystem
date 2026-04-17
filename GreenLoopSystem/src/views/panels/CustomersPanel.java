package views.panels;

import controllers.DashboardController;
import models.Customer;
import models.CustomerTableModel;
import views.DeleteSelectionDialog;
import views.ModifyCustomer;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomersPanel extends DashboardModulePanel {
    private final List<Customer> customers;
    private final CustomerTableModel model;
    private final JTable table;
    private final DashboardController dashboardController;

    public CustomersPanel(DashboardController dashboardController, List<Customer> customers) {
        this.customers = customers;
        this.dashboardController = dashboardController;
        this.model = new CustomerTableModel(customers);
        this.table = new JTable(model);
        createUIComponents();
    }

    private void createUIComponents() {
        addPageTitle("Manage Clients", "Search, maintain, and update client records in one clean workspace.");

        TableRowSorter<CustomerTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        DashboardTheme.styleTable(table);

        JPanel searchCard = createCard(30, 96, 860, 96);
        JLabel searchLabel = DashboardTheme.createSectionLabel("Client Search");
        searchLabel.setBounds(24, 18, 180, 22);
        searchCard.add(searchLabel);

        JTextField searchField = buildTextField(24, 48, 610, 38);
        searchCard.add(searchField);

        JButton searchButton = buildPrimaryButton("Search", 654, 48, 160, 38);
        searchButton.addActionListener(e -> applySearch(sorter, searchField.getText()));
        searchCard.add(searchButton);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("View/Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");

        addItem.addActionListener(e -> new ModifyCustomer(this, "Add New Customer", new Customer()).setVisible(true));
        editItem.addActionListener(e -> editSelectedCustomer());
        deleteItem.addActionListener(e -> deleteSelectedCustomer());
        contextMenu.add(addItem);
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            private void showContextMenu(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        JPanel tableCard = createCard(30, 210, 860, 392);
        JLabel tableLabel = DashboardTheme.createSectionLabel("Client Directory");
        tableLabel.setBounds(24, 18, 220, 22);
        tableCard.add(tableLabel);

        JScrollPane tableScrollPane = buildTableScrollPane(table, 24, 54, 812, 314);
        tableCard.add(tableScrollPane);

        JPanel actionCard = createCard(30, 620, 400, 74);
        JButton addButton = buildPrimaryButton("Add", 24, 18, 110, 38);
        addButton.addActionListener(e -> new ModifyCustomer(this, "Add New Customer", new Customer()).setVisible(true));
        actionCard.add(addButton);

        JButton editButton = buildSecondaryButton("Edit", 145, 18, 110, 38);
        editButton.addActionListener(e -> editSelectedCustomer());
        actionCard.add(editButton);

        JButton deleteButton = buildDangerButton("Delete", 266, 18, 110, 38);
        deleteButton.addActionListener(e -> showDeleteDialog());
        actionCard.add(deleteButton);
    }

    private void applySearch(TableRowSorter<CustomerTableModel> sorter, String text) {
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        sorter.setRowFilter(new RowFilter<CustomerTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends CustomerTableModel, ? extends Object> entry) {
                String searchText = text.toLowerCase();
                for (int i = 0; i < entry.getValueCount(); i++) {
                    String value = entry.getStringValue(i);
                    if (value != null && value.toLowerCase().contains(searchText)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void editSelectedCustomer() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Customer customer = customers.get(modelRow);
            new ModifyCustomer(this, "Edit Customer", customer).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedCustomer() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            boolean deleted = dashboardController.crudCustomer(customers.get(modelRow), 'd');
            if (deleted) {
                customers.remove(modelRow);
                model.fireTableRowsDeleted(modelRow, modelRow);
                repaint();
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean createCustomer(Customer newCustomer) {
        boolean created = dashboardController.crudCustomer(newCustomer, 'c');
        if (created) {
            List<Customer> customers_ = dashboardController.getAllCustomers();
            customers_.sort((o1, o2) -> o1.getCustomerId());
            customers.add(customers_.get(customers_.size() - 1));
            int newRowIndex = customers.size() - 1;
            model.fireTableRowsInserted(newRowIndex, newRowIndex);
            repaint();
        }
        return created;
    }

    public boolean updateCustomer(Customer updatedCustomer) {
        boolean updated = dashboardController.crudCustomer(updatedCustomer, 'u');
        if (updated) {
            int customerIndex = -1;
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getCustomerId() == updatedCustomer.getCustomerId()) {
                    customerIndex = i;
                    break;
                }
            }
            if (customerIndex != -1) {
                customers.set(customerIndex, updatedCustomer);
                model.fireTableRowsUpdated(customerIndex, customerIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found");
            }
        }
        return updated;
    }

    private void showDeleteDialog() {
        DeleteSelectionDialog<Customer> dialog = new DeleteSelectionDialog<>(
                this,
                "Delete Customer",
                customers,
                new String[]{"Customer ID", "Name", "Mobile", "Email"},
                customer -> new Object[]{customer.getCustomerId(), customer.getName(), customer.getMobile(), customer.getEmail()},
                "Filter by Name",
                0,
                1,
                customer -> dashboardController.crudCustomer(customer, 'd'),
                this::removeCustomerFromTable
        );
        dialog.setVisible(true);
    }

    private void removeCustomerFromTable(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId() == customer.getCustomerId()) {
                customers.remove(i);
                model.fireTableRowsDeleted(i, i);
                repaint();
                return;
            }
        }
    }
}
