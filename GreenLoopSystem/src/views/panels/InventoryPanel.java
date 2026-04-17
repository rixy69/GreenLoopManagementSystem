package views.panels;

import controllers.DashboardController;
import models.Inventory;
import models.InventoryTableModel;
import models.Part;
import models.Supplier;
import views.DeleteSelectionDialog;
import views.ModifyInventory;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InventoryPanel extends DashboardModulePanel {
    private final List<Inventory> inventories;
    private final InventoryTableModel model;
    private final JTable table;
    private final DashboardController dashboardController;

    public InventoryPanel(DashboardController dashboardController, List<Inventory> inventories) {
        this.inventories = inventories;
        this.dashboardController = dashboardController;
        this.model = new InventoryTableModel(inventories);
        this.table = new JTable(model);
        createUIComponents();
    }

    private void createUIComponents() {
        addPageTitle("Manage Inventory", "Monitor stock levels and keep inventory actions readable and consistent.");

        TableRowSorter<InventoryTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        DashboardTheme.styleTable(table);

        JPanel searchCard = createCard(30, 96, 860, 96);
        JLabel searchLabel = DashboardTheme.createSectionLabel("Inventory Search");
        searchLabel.setBounds(24, 18, 180, 22);
        searchCard.add(searchLabel);

        JTextField searchField = buildTextField(24, 48, 790, 38);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(new RowFilter<InventoryTableModel, Object>() {
                        @Override
                        public boolean include(Entry<? extends InventoryTableModel, ? extends Object> entry) {
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
            }
        });
        searchCard.add(searchField);

        JPanel tableCard = createCard(30, 210, 860, 392);
        JLabel tableLabel = DashboardTheme.createSectionLabel("Inventory Table");
        tableLabel.setBounds(24, 18, 220, 22);
        tableCard.add(tableLabel);
        tableCard.add(buildTableScrollPane(table, 24, 54, 812, 314));

        JPanel actionCard = createCard(30, 620, 400, 74);
        JButton addButton = buildPrimaryButton("Add", 24, 18, 110, 38);
        addButton.addActionListener(e -> new ModifyInventory(this, "Add Inventory", new Inventory(), null).setVisible(true));
        actionCard.add(addButton);

        JButton editButton = buildSecondaryButton("Edit", 145, 18, 110, 38);
        editButton.addActionListener(e -> editSelectedInventory());
        actionCard.add(editButton);

        JButton deleteButton = buildDangerButton("Delete", 266, 18, 110, 38);
        deleteButton.addActionListener(e -> showDeleteDialog());
        actionCard.add(deleteButton);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        addItem.addActionListener(e -> new ModifyInventory(this, "Add Inventory", new Inventory(), null).setVisible(true));
        editItem.addActionListener(e -> editSelectedInventory());
        deleteItem.addActionListener(e -> deleteSelectedInventory());
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
    }

    private void editSelectedInventory() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Inventory inventory = inventories.get(modelRow);
            new ModifyInventory(this, "Edit Inventory", inventory, getPartByID(inventory.getPartId())).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an inventory item to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedInventory() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            boolean deleted = dashboardController.crudInventory(inventories.get(modelRow), 'd');
            if (deleted) {
                inventories.remove(modelRow);
                model.fireTableRowsDeleted(modelRow, modelRow);
                repaint();
                JOptionPane.showMessageDialog(this, "Inventory item deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete inventory item.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean createInventory(Inventory newInventory) {
        boolean created = dashboardController.crudInventory(newInventory, 'c');
        if (created) {
            List<Inventory> inventories_ = dashboardController.getAllInventories();
            Collections.sort(inventories_, Comparator.comparingInt(Inventory::getInventoryId));
            inventories.add(inventories_.get(inventories_.size() - 1));
            int newRowIndex = inventories.size() - 1;
            model.fireTableRowsInserted(newRowIndex, newRowIndex);
            repaint();
        }
        return created;
    }

    public boolean updateInventory(Inventory updatedInventory) {
        boolean updated = this.dashboardController.crudInventory(updatedInventory, 'u');
        if (updated) {
            int inventoryIndex = -1;
            for (int i = 0; i < inventories.size(); i++) {
                if (inventories.get(i).getInventoryId() == updatedInventory.getInventoryId()) {
                    inventoryIndex = i;
                    break;
                }
            }
            if (inventoryIndex != -1) {
                inventories.set(inventoryIndex, updatedInventory);
                model.fireTableRowsUpdated(inventoryIndex, inventoryIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Inventory not found");
            }
        }
        return updated;
    }

    public Part getPartByID(int id) {
        Part part = new Part();
        part.setPartId(id);
        if (dashboardController.crudPart(part, 'r')) {
            part.setSupplier(getSupplierById(part.getSupplierId()));
            return part;
        }
        return null;
    }

    public Supplier getSupplierById(int id) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(id);
        return dashboardController.crudSupplier(supplier, 'r') ? supplier : null;
    }

    private void showDeleteDialog() {
        DeleteSelectionDialog<Inventory> dialog = new DeleteSelectionDialog<>(
                this,
                "Delete Inventory",
                inventories,
                new String[]{"Inventory ID", "Part ID", "Quantity", "Location"},
                inventory -> new Object[]{inventory.getInventoryId(), inventory.getPartId(), inventory.getQuantity(), inventory.getLocation()},
                "Filter by Name / Location",
                0,
                3,
                inventory -> dashboardController.crudInventory(inventory, 'd'),
                this::removeInventoryFromTable
        );
        dialog.setVisible(true);
    }

    private void removeInventoryFromTable(Inventory inventory) {
        for (int i = 0; i < inventories.size(); i++) {
            if (inventories.get(i).getInventoryId() == inventory.getInventoryId()) {
                inventories.remove(i);
                model.fireTableRowsDeleted(i, i);
                repaint();
                return;
            }
        }
    }
}
