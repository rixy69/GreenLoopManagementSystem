package models;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderTableModel extends AbstractTableModel {
    private List<Order> orders;
    private final String[] columnNames = {"Order ID", "Customer ID", "Order Date", "Total Price", "Repair", "Repaint"};

    public OrderTableModel(List<Order> orders) {
        this.setOrders(orders);
    }

    @Override
    public int getRowCount() {
        return getOrders().size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = getOrders().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return order.getOrderId();
            case 1:
                return order.getCustomerId();
            case 2:
                return formatDate(order.getOrderDate());
            case 3:
                return order.getTotalPrice();
            case 4:
                return order.isRepair();
            case 5:
                return order.isRepaint();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
