package views.panels;

import controllers.DashboardController;
import models.Order;
import models.OrderTableModel;
import views.DeleteSelectionDialog;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class OrderPanel extends DashboardModulePanel {
    private List<Order> orders;
    private final OrderTableModel model;
    private final JTable table;

    public OrderPanel(DashboardController dashboardController, List<Order> orders) {
        this.orders = orders;
        this.model = new OrderTableModel(orders);
        this.table = new JTable(model);

        addPageTitle("Manage Orders", "Create, review, and maintain order workflows with a shared layout.");

        TableRowSorter<OrderTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        DashboardTheme.styleTable(table);

        JPanel searchCard = createCard(30, 96, 860, 96);
        JLabel searchLabel = DashboardTheme.createSectionLabel("Order Search");
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
                    sorter.setRowFilter(new RowFilter<OrderTableModel, Object>() {
                        @Override
                        public boolean include(Entry<? extends OrderTableModel, ? extends Object> entry) {
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
        JLabel tableLabel = DashboardTheme.createSectionLabel("Orders");
        tableLabel.setBounds(24, 18, 220, 22);
        tableCard.add(tableLabel);
        tableCard.add(buildTableScrollPane(table, 24, 54, 812, 314));

        JPanel actionCard = createCard(30, 620, 400, 74);
        JButton addButton = buildPrimaryButton("Add", 24, 18, 110, 38);
        addButton.addActionListener(e -> dashboardController.showOrderView(new Order(), "Add Order", this));
        actionCard.add(addButton);

        JButton editButton = buildSecondaryButton("Edit", 145, 18, 110, 38);
        editButton.addActionListener(e -> editSelectedOrder(dashboardController));
        actionCard.add(editButton);

        JButton deleteButton = buildDangerButton("Delete", 266, 18, 110, 38);
        deleteButton.addActionListener(e -> showDeleteDialog(dashboardController));
        actionCard.add(deleteButton);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        addItem.addActionListener(e -> dashboardController.showOrderView(new Order(), "Add Order", this));
        editItem.addActionListener(e -> editSelectedOrder(dashboardController));
        deleteItem.addActionListener(e -> deleteSelectedOrder(dashboardController));
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

    private void editSelectedOrder(DashboardController dashboardController) {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Order order = orders.get(modelRow);
            dashboardController.showOrderView(order, "Edit Order", this);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedOrder(DashboardController dashboardController) {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Order order = orders.get(modelRow);
            boolean deleted = dashboardController.crudOrder(order, 'd');
            if (deleted) {
                orders.remove(modelRow);
                model.fireTableRowsDeleted(modelRow, modelRow);
                repaint();
                JOptionPane.showMessageDialog(this, "Order deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void refreshTable() {
        model.setOrders(orders);
        model.fireTableDataChanged();
    }

    private void showDeleteDialog(DashboardController dashboardController) {
        DeleteSelectionDialog<Order> dialog = new DeleteSelectionDialog<>(
                this,
                "Delete Order",
                orders,
                new String[]{"Order ID", "Customer ID", "Order Date", "Total Price"},
                order -> new Object[]{order.getOrderId(), order.getCustomerId(), order.getOrderDate(), order.getTotalPrice()},
                "Filter by Name / Customer ID",
                0,
                1,
                order -> dashboardController.crudOrder(order, 'd'),
                this::removeOrderFromTable
        );
        dialog.setVisible(true);
    }

    private void removeOrderFromTable(Order order) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId() == order.getOrderId()) {
                orders.remove(i);
                model.fireTableRowsDeleted(i, i);
                repaint();
                return;
            }
        }
    }
}
