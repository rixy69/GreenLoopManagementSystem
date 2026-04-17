package models;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class BillItemTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Type", "Supplier ID", "Part ID", "Part Name", "Unit Price", "Quantity", "Total Price"};



    private List<BillItem> billItems;

    public BillItemTableModel() {
        this.billItems = new ArrayList<>();
    }

    public BillItemTableModel(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    @Override
    public int getRowCount() {
        return billItems.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BillItem billItem = billItems.get(rowIndex);
        switch (columnIndex) {
            case 0: return billItem.getType();
            case 1: return billItem.getSupplierID();
            case 2: return billItem.getPartId();
            case 3: return billItem.getPartName();
            case 4: return billItem.getUnitPrice();
            case 5: return billItem.getQuantity();
            case 6: return billItem.getTotalPrice();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void addBillItem(BillItem newBillItem, int previousIndex) {

        if(previousIndex>=0){
            billItems.set(previousIndex, newBillItem);
        }else {
            billItems.add(newBillItem);
        }
        fireTableDataChanged();

    }

    public int getPreviousIndex(int partId) {
        int previousIndex=-1;
        for (int i = 0; i < billItems.size(); i++) {
            if(partId==billItems.get(i).getPartId()){
                previousIndex = i;
                break;
            }
        }
        return previousIndex;
    }

    public int getNotSavedQuantity(int index) {
        if(index>=0){
            return billItems.get(index).getNotSavedQuantity();
        }else {
            return 0;
        }
    }



    public void removeBillItem(int rowIndex) {
        billItems.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public BillItem getBillItem(int rowIndex) {
        return billItems.get(rowIndex);
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    public void removeRow(int selectedRow) {
        billItems.remove(selectedRow);
        fireTableRowsDeleted(selectedRow, selectedRow);
    }


}
