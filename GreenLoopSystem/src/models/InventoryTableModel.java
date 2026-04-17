package models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class InventoryTableModel extends AbstractTableModel {
    private final List<Inventory> inventory;
    private final String[] columnNames = {"ID", "Part ID", "Quantity", "Location"};

    public InventoryTableModel(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getRowCount() {
        return inventory.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Inventory item = inventory.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getInventoryId();
            case 1:
                return item.getPartId();
            case 2:
                return item.getQuantity();
            case 3:
                return item.getLocation();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
