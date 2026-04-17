package models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PartTableModel extends AbstractTableModel {
    private final List<Part> parts;
    private final String[] columnNames = {"Part ID", "Name", "Description", "Price", "Supplier ID"};

    public PartTableModel(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public int getRowCount() {
        return parts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Part part = parts.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return part.getPartId();
            case 1:
                return part.getName();
            case 2:
                return part.getDescription();
            case 3:
                return part.getPrice();
            case 4:
                return part.getSupplierId();
            default:
                return null;
        }
    }
}
