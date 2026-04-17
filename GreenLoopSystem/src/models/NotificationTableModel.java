package models;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class NotificationTableModel extends AbstractTableModel {
    private final List<Notification> notifications;
    private final String[] columnNames = {"ID", "Part ID", "Part Name", "Remaining Quantity", "Minimum Quantity", "Notify"};

    public NotificationTableModel(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public int getRowCount() {
        return notifications.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Notification notification = notifications.get(rowIndex);
        switch (columnIndex) {
            case 0: return notification.getNotificationId();
            case 1: return notification.getPartId();
            case 2: return notification.getPartName();
            case 3: return notification.getRemainingQuantity();
            case 4: return notification.getMinQuantity();
            case 5: return notification.isNotify();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
