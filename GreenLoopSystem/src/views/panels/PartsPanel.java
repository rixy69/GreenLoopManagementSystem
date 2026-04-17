package views.panels;

import controllers.DashboardController;
import models.Employee;
import models.Part;
import models.PartTableModel;
import models.Supplier;
import services.CS;
import views.ModifyPart;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PartsPanel extends DashboardModulePanel {
    private List<Part> parts;
    private PartTableModel model;
    private JTable table;
    private DashboardController dashboardController;

    public PartsPanel(DashboardController dashboardController, List<Part> parts) {
        this.parts = parts;
        this.dashboardController = dashboardController;
        this.createUIComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw a rectangle
        g.setColor(Color.decode("#BBBBBB"));
        g.drawRect(40, 80, 850, 110); // (x, y, width, height)
    }

    private void createUIComponents() {
        model = new PartTableModel(parts);
        table = new JTable(model);
        DashboardTheme.styleTable(table);

        TableRowSorter<PartTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        addPageTitle("Product Catalogue", "Search, update, and maintain packaging items using the shared dashboard layout.");


        JLabel filterLabel = DashboardTheme.createSectionLabel("Search");
        filterLabel.setBounds(50, 90, 80, 22);
        this.add(filterLabel);

        JTextField searchField = new JTextField();
        DashboardTheme.styleTextField(searchField);
        searchField.setBounds(50, 135, 250, 34);
        this.add(searchField);

        JButton searchButton = DashboardTheme.createPrimaryButton("Search");
        searchButton.setBounds(310, 135, 110, 34);
        searchButton.addActionListener(e -> {
            String text = searchField.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                // Create a filter that searches across all columns
                RowFilter<PartTableModel, Object> rf = new RowFilter<PartTableModel, Object>() {
                    public boolean include(Entry<? extends PartTableModel, ? extends Object> entry) {
                        String searchText = text.toLowerCase();
                        for (int i = 0; i < entry.getValueCount(); i++) {
                            String value = entry.getStringValue(i);
                            if (value != null && value.toLowerCase().contains(searchText)) {
                                return true;
                            }
                        }
                        return false;
                    }
                };
                sorter.setRowFilter(rf);
            }
        });
        // Add hover effect
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(DashboardTheme.PRIMARY_GREEN_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(DashboardTheme.PRIMARY_GREEN);
            }
        });
        this.add(searchButton);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");

        //Add Action
        addItem.addActionListener(e -> {
            ModifyPart modifyPart = new ModifyPart(PartsPanel.this, "Add New Part", new Part(), null);
            modifyPart.setVisible(true);
        });

        // Edit action
        editItem.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Part part = parts.get(modelRow);
                ModifyPart modifyPart = new ModifyPart(PartsPanel.this, "Edit Part", part, getSupplierById(part.getSupplierId()));
                modifyPart.setVisible(true);
            }
        });

        // Delete action
        deleteItem.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                Part part = parts.get(modelRow);
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this part?", "Delete Part", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = deletePart(part);
                    if (deleted) {
                        JOptionPane.showMessageDialog(null, "Part deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        updateTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete part.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        contextMenu.add(addItem);
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        // Mouse listener for showing context menu on right-click
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        contextMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });

        JScrollPane tableScrollPane = DashboardTheme.styleScrollPane(new JScrollPane(table));
        tableScrollPane.setBounds(40, 200, 850, 400);
        this.add(tableScrollPane);


        // Add button
        JButton addButton = DashboardTheme.createPrimaryButton("Add");
        addButton.setBounds(40, 620, 110, 36);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and display the ModifyPart frame
                ModifyPart modifyPart = new ModifyPart(PartsPanel.this, "Add New Part", new Part(), null);
                modifyPart.setVisible(true);
            }
        });
        this.add(addButton);

        // Edit button
        JButton editButton = DashboardTheme.createSecondaryButton("Edit");
        editButton.setBounds(160, 620, 110, 36);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    Part part = parts.get(modelRow);
                    ModifyPart modifyPart = new ModifyPart(PartsPanel.this, "Edit Part", part, getSupplierById(part.getSupplierId()));
                    modifyPart.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a part to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        this.add(editButton);

        // Delete button
        JButton deleteButton = DashboardTheme.createDangerButton("Delete");
        deleteButton.setBounds(280, 620, 110, 36);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int viewRow = table.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    Part part = parts.get(modelRow);
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this part?", "Delete Part", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean deleted = deletePart(part);
                        if (deleted) {
                            JOptionPane.showMessageDialog(null, "Part deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            updateTable();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete part.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a part to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        this.add(deleteButton);

    }


    public boolean createPart(Part part) {
        boolean created = dashboardController.crudPart(part, 'c');
        if (created) {
            List<Part> parts_ = dashboardController.getAllParts();
            Collections.sort(parts_, Comparator.comparingInt(Part::getPartId));
            parts.add(parts_.get(parts_.size()-1));

            int newRowIndex = parts.size() - 1;
            model.fireTableRowsInserted(newRowIndex, newRowIndex);
            this.getParent().repaint();
        }
        return created;
    }

    public boolean updatePart(Part updatedPart) {
        boolean updated = dashboardController.crudPart(updatedPart, 'u');
        if (updated) {
            int partIndex = -1;
            for (int i = 0; i < parts.size(); i++) {
                if (parts.get(i).getPartId() == updatedPart.getPartId()) {
                    partIndex = i;
                    break;
                }
            }

            // Update the part in the list
            if (partIndex != -1) {
                parts.set(partIndex, updatedPart);
                model.fireTableRowsUpdated(partIndex, partIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Part not found");
            }
        }
        return updated;
    }

    private boolean deletePart(Part part) {
        boolean deleted = dashboardController.crudPart(part, 'd');
        if (deleted) {
            parts.remove(part);
            model.fireTableDataChanged();
        }
        return deleted;
    }

    private void updateTable() {
        model.fireTableDataChanged();
    }

    public Supplier getSupplierById(int id) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(id);
        if(dashboardController.crudSupplier(supplier, 'r')){
            return supplier;
        }else {
            return null;
        }
    }
}
