package models;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
    private final List<Employee> employees;
    private final List<Role> roles;
    private final String[] columnNames = {"ID", "Title", "First Name", "Last Name", "Username", "Address", "Mobile", "Email", "Schedule", "Role"};

    public EmployeeTableModel(List<Employee> employees, List<Role> roles) {
        this.employees = employees;
        this.roles = roles;
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return employee.getEmployeeId();
            case 1:
                return employee.getTitle();
            case 2:
                return employee.getFirstName();
            case 3:
                return employee.getLastName();
            case 4:
                return employee.getUsername();
            case 5:
                return employee.getAddress();
            case 6:
                return employee.getMobile();
            case 7:
                return employee.getEmail();
            case 8:
                return employee.getSchedule();
            case 9:
                return getRoleName(employee.getRoleId());
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public String getRoleName(int roleId){
        return roles.stream()
                .filter(role -> role.getRoleId() == roleId)
                .findFirst()
                .orElse(null)
                .getRoleName();
    }

}
