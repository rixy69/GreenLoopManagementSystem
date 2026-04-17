package views.panels;

import controllers.DashboardController;
import models.Employee;
import models.EmployeeTableModel;
import models.Role;
import views.DeleteSelectionDialog;
import views.ModifyEmployee;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmployeesPanel extends DashboardModulePanel {
    private final List<Employee> employees;
    private final List<Role> roles;
    private final EmployeeTableModel model;
    private final JTable table;
    private final DashboardController dashboardController;

    public EmployeesPanel(DashboardController dashboardController, List<Employee> employees, List<Role> roles) {
        this.employees = employees;
        this.roles = roles;
        this.dashboardController = dashboardController;
        this.model = new EmployeeTableModel(employees, roles);
        this.table = new JTable(model);
        createUIComponents();
    }

    private void createUIComponents() {
        addPageTitle("Manage Staff", "Coordinate staff access, identity, and role assignments.");

        TableRowSorter<EmployeeTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        DashboardTheme.styleTable(table);

        JPanel searchCard = createCard(30, 96, 860, 96);
        JLabel searchLabel = DashboardTheme.createSectionLabel("Staff Search");
        searchLabel.setBounds(24, 18, 180, 22);
        searchCard.add(searchLabel);

        JTextField searchField = buildTextField(24, 48, 790, 38);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applySearch(sorter, searchField.getText());
            }
        });
        searchCard.add(searchField);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("View/Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");

        addItem.addActionListener(e -> new ModifyEmployee(this, "Add New Employee", new Employee(), roles).setVisible(true));
        editItem.addActionListener(e -> editSelectedEmployee());
        deleteItem.addActionListener(e -> deleteSelectedEmployee());

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

        JPanel tableCard = createCard(30, 210, 860, 392);
        JLabel tableLabel = DashboardTheme.createSectionLabel("Staff Directory");
        tableLabel.setBounds(24, 18, 220, 22);
        tableCard.add(tableLabel);
        tableCard.add(buildTableScrollPane(table, 24, 54, 812, 314));

        JPanel actionCard = createCard(30, 620, 400, 74);
        JButton addButton = buildPrimaryButton("Add", 24, 18, 110, 38);
        addButton.addActionListener(e -> new ModifyEmployee(this, "Add New Employee", new Employee(), roles).setVisible(true));
        actionCard.add(addButton);

        JButton editButton = buildSecondaryButton("Edit", 145, 18, 110, 38);
        editButton.addActionListener(e -> editSelectedEmployee());
        actionCard.add(editButton);

        JButton deleteButton = buildDangerButton("Delete", 266, 18, 110, 38);
        deleteButton.addActionListener(e -> showDeleteDialog());
        actionCard.add(deleteButton);
    }

    private void applySearch(TableRowSorter<EmployeeTableModel> sorter, String text) {
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        sorter.setRowFilter(new RowFilter<EmployeeTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends EmployeeTableModel, ? extends Object> entry) {
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

    private void editSelectedEmployee() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Employee employee = employees.get(modelRow);
            new ModifyEmployee(this, "Edit Employee", employee, roles).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedEmployee() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            boolean deleted = dashboardController.crudEmployee(employees.get(modelRow), 'd');
            if (deleted) {
                employees.remove(modelRow);
                model.fireTableRowsDeleted(modelRow, modelRow);
                repaint();
                JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete employee.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean updateEmployee(Employee updatedEmployee) {
        boolean updated = this.dashboardController.crudEmployee(updatedEmployee, 'u');
        if (updated) {
            int employeeIndex = -1;
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmployeeId() == updatedEmployee.getEmployeeId()) {
                    employeeIndex = i;
                    break;
                }
            }
            if (employeeIndex != -1) {
                employees.set(employeeIndex, updatedEmployee);
                model.fireTableRowsUpdated(employeeIndex, employeeIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found");
            }
        }
        return updated;
    }

    public boolean createEmployee(Employee newEmployee) {
        boolean created = dashboardController.crudEmployee(newEmployee, 'c');
        if (created) {
            List<Employee> employees_ = dashboardController.getAllEmployees();
            Collections.sort(employees_, Comparator.comparingInt(Employee::getEmployeeId));
            employees.add(employees_.get(employees_.size() - 1));
            int newRowIndex = employees.size() - 1;
            model.fireTableRowsInserted(newRowIndex, newRowIndex);
            repaint();
        }
        return created;
    }

    private void showDeleteDialog() {
        DeleteSelectionDialog<Employee> dialog = new DeleteSelectionDialog<>(
                this,
                "Delete Employee",
                employees,
                new String[]{"ID", "Title", "First Name", "Last Name", "Username"},
                employee -> new Object[]{employee.getEmployeeId(), employee.getTitle(), employee.getFirstName(), employee.getLastName(), employee.getUsername()},
                "Filter by Name",
                0,
                2,
                employee -> dashboardController.crudEmployee(employee, 'd'),
                this::removeEmployeeFromTable
        );
        dialog.setVisible(true);
    }

    private void removeEmployeeFromTable(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId() == employee.getEmployeeId()) {
                employees.remove(i);
                model.fireTableRowsDeleted(i, i);
                repaint();
                return;
            }
        }
    }
}
