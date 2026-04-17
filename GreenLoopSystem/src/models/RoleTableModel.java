package models;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RoleTableModel extends AbstractTableModel {
    private final List<Role> roles;
    private final String[] columnNames = {"Role ID", "Role Name"};

    public RoleTableModel(List<Role> roles) {
        // Sort the roles by Role ID in ascending order
        Collections.sort(roles, Comparator.comparingInt(Role::getRoleId));
        this.roles = roles;
    }

    @Override
    public int getRowCount() {
        return roles.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Role role = roles.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return role.getRoleId();
            case 1:
                return role.getRoleName();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
