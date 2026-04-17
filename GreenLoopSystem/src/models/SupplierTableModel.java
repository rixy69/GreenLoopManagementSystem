package models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SupplierTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Supplier ID", "Name", "Contact Name", "Contact Email", "Contact Phone", "Address"};
    private final List<Supplier> suppliers;

    public SupplierTableModel(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public int getRowCount() {
        return suppliers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Supplier supplier = suppliers.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return supplier.getSupplierId();
            case 1:
                return supplier.getName();
            case 2:
                return supplier.getContactName();
            case 3:
                return supplier.getContactEmail();
            case 4:
                return supplier.getContactPhone();
            case 5:
                return supplier.getAddress();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
