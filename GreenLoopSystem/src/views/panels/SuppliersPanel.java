package views.panels;

import controllers.DashboardController;
import models.Supplier;
import models.SupplierTableModel;
import views.DeleteSelectionDialog;
import views.ModifySupplier;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SuppliersPanel extends DashboardModulePanel {
    private final List<Supplier> suppliers;
    private final SupplierTableModel model;
    private final JTable table;
    private final DashboardController dashboardController;

    public SuppliersPanel(DashboardController dashboardController, List<Supplier> suppliers) {
        this.suppliers = suppliers;
        this.dashboardController = dashboardController;
        this.model = new SupplierTableModel(suppliers);
        this.table = new JTable(model);
        createUIComponents();
    }

    private void createUIComponents() {
        addPageTitle("Manage Suppliers", "Track supplier relationships, contacts, and procurement readiness.");

        TableRowSorter<SupplierTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        DashboardTheme.styleTable(table);

        JPanel filterCard = createCard(30, 96, 860, 124);
        JLabel title = DashboardTheme.createSectionLabel("Supplier Filters");
        title.setBounds(24, 18, 180, 22);
        filterCard.add(title);

        addFilterField(filterCard, "Supplier ID", 24, 52, text -> applyColumnFilter(sorter, text, 0));
        addFilterField(filterCard, "Name", 224, 52, text -> applyColumnFilter(sorter, text, 1));
        addFilterField(filterCard, "Contact Name", 424, 52, text -> applyColumnFilter(sorter, text, 2));
        addFilterField(filterCard, "Contact Email", 624, 52, text -> applyColumnFilter(sorter, text, 3));

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("View/Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        addItem.addActionListener(e -> new ModifySupplier(this, "Add New Supplier", new Supplier()).setVisible(true));
        editItem.addActionListener(e -> editSelectedSupplier());
        deleteItem.addActionListener(e -> deleteSelectedSupplier());
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

        JPanel tableCard = createCard(30, 238, 860, 364);
        JLabel tableLabel = DashboardTheme.createSectionLabel("Supplier Directory");
        tableLabel.setBounds(24, 18, 220, 22);
        tableCard.add(tableLabel);
        tableCard.add(buildTableScrollPane(table, 24, 54, 812, 286));

        JPanel actionCard = createCard(30, 620, 400, 74);
        JButton addButton = buildPrimaryButton("Add", 24, 18, 110, 38);
        addButton.addActionListener(e -> new ModifySupplier(this, "Add New Supplier", new Supplier()).setVisible(true));
        actionCard.add(addButton);

        JButton editButton = buildSecondaryButton("Edit", 145, 18, 110, 38);
        editButton.addActionListener(e -> editSelectedSupplier());
        actionCard.add(editButton);

        JButton deleteButton = buildDangerButton("Delete", 266, 18, 110, 38);
        deleteButton.addActionListener(e -> showDeleteDialog());
        actionCard.add(deleteButton);
    }

    private void addFilterField(JPanel parent, String labelText, int x, int y, java.util.function.Consumer<String> onChange) {
        JLabel label = DashboardTheme.createMutedLabel(labelText);
        label.setBounds(x, y, 160, 18);
        parent.add(label);

        JTextField field = buildTextField(x, y + 22, 176, 34);
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onChange.accept(field.getText());
            }
        });
        parent.add(field);
    }

    private void applyColumnFilter(TableRowSorter<SupplierTableModel> sorter, String text, int column) {
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, column));
        }
    }

    private void editSelectedSupplier() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Supplier supplier = suppliers.get(modelRow);
            new ModifySupplier(this, "Edit Supplier", supplier).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a supplier to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedSupplier() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            boolean deleted = dashboardController.crudSupplier(suppliers.get(modelRow), 'd');
            if (deleted) {
                suppliers.remove(modelRow);
                model.fireTableRowsDeleted(modelRow, modelRow);
                repaint();
                JOptionPane.showMessageDialog(this, "Supplier deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean createSupplier(Supplier supplier) {
        boolean created = dashboardController.crudSupplier(supplier, 'c');
        if (created) {
            List<Supplier> suppliers_ = dashboardController.getAllSuppliers();
            suppliers_.sort((o1, o2) -> o1.getSupplierId() - o2.getSupplierId());
            suppliers.add(suppliers_.get(suppliers_.size() - 1));
            int newRowIndex = suppliers.size() - 1;
            model.fireTableRowsInserted(newRowIndex, newRowIndex);
            repaint();
        }
        return created;
    }

    public boolean updateSupplier(Supplier updatedSupplier) {
        boolean updated = this.dashboardController.crudSupplier(updatedSupplier, 'u');
        if (updated) {
            int supplierIndex = -1;
            for (int i = 0; i < suppliers.size(); i++) {
                if (suppliers.get(i).getSupplierId() == updatedSupplier.getSupplierId()) {
                    supplierIndex = i;
                    break;
                }
            }
            if (supplierIndex != -1) {
                suppliers.set(supplierIndex, updatedSupplier);
                model.fireTableRowsUpdated(supplierIndex, supplierIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Supplier not found");
            }
        }
        return updated;
    }

    private void showDeleteDialog() {
        DeleteSelectionDialog<Supplier> dialog = new DeleteSelectionDialog<>(
                this,
                "Delete Supplier",
                suppliers,
                new String[]{"Supplier ID", "Name", "Contact Name", "Contact Email"},
                supplier -> new Object[]{supplier.getSupplierId(), supplier.getName(), supplier.getContactName(), supplier.getContactEmail()},
                "Filter by Name",
                0,
                1,
                supplier -> dashboardController.crudSupplier(supplier, 'd'),
                this::removeSupplierFromTable
        );
        dialog.setVisible(true);
    }

    private void removeSupplierFromTable(Supplier supplier) {
        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i).getSupplierId() == supplier.getSupplierId()) {
                suppliers.remove(i);
                model.fireTableRowsDeleted(i, i);
                repaint();
                return;
            }
        }
    }
}
