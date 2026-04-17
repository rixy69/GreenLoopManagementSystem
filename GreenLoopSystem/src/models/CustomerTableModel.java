package models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CustomerTableModel extends AbstractTableModel {
    private final List<Customer> customers;
    private final String[] columnNames = {"Customer ID", "Name", "Address", "Mobile", "Email"};

    public CustomerTableModel(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer customer = customers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return customer.getCustomerId();
            case 1:
                return customer.getName();
            case 2:
                return customer.getAddress();
            case 3:
                return customer.getMobile();
            case 4:
                return customer.getEmail();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
