package views.panels;

import controllers.DashboardController;
import models.Employee;
import models.Jobs;
import models.JobsTableModel;
import views.DeleteSelectionDialog;
import views.ModifyJob;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class JobsPanel extends DashboardModulePanel {
    private final List<Jobs> jobs;
    private final JobsTableModel model;
    private final JTable table;
    private final DashboardController dashboardController;

    public JobsPanel(DashboardController dashboardController, List<Jobs> jobs) {
        this.jobs = jobs;
        this.dashboardController = dashboardController;
        this.model = new JobsTableModel(jobs);
        this.table = new JTable(model);
        createUIComponents();
    }

    private void createUIComponents() {
        addPageTitle("Delivery Assignments", "Assign, update, and review delivery work with a consistent workflow.");

        TableRowSorter<JobsTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        DashboardTheme.styleTable(table);

        JPanel searchCard = createCard(30, 96, 860, 96);
        JLabel searchLabel = DashboardTheme.createSectionLabel("Assignment Search");
        searchLabel.setBounds(24, 18, 180, 22);
        searchCard.add(searchLabel);

        JTextField searchField = buildTextField(24, 48, 610, 38);
        searchCard.add(searchField);

        JButton searchButton = buildPrimaryButton("Search", 654, 48, 160, 38);
        searchButton.addActionListener(e -> applySearch(sorter, searchField.getText()));
        searchCard.add(searchButton);

        JPanel tableCard = createCard(30, 210, 860, 392);
        JLabel tableLabel = DashboardTheme.createSectionLabel("Assignment Queue");
        tableLabel.setBounds(24, 18, 220, 22);
        tableCard.add(tableLabel);
        tableCard.add(buildTableScrollPane(table, 24, 54, 812, 314));

        JPanel actionCard = createCard(30, 620, 400, 74);
        JButton addButton = buildPrimaryButton("Add", 24, 18, 110, 38);
        addButton.addActionListener(e -> new ModifyJob(this, null, "Add New Job", null).setVisible(true));
        actionCard.add(addButton);

        JButton editButton = buildSecondaryButton("Edit", 145, 18, 110, 38);
        editButton.addActionListener(e -> editSelectedJob());
        actionCard.add(editButton);

        JButton deleteButton = buildDangerButton("Delete", 266, 18, 110, 38);
        deleteButton.addActionListener(e -> showDeleteDialog());
        actionCard.add(deleteButton);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("View/Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
        editItem.addActionListener(e -> editSelectedJob());
        deleteItem.addActionListener(e -> deleteSelectedJob());
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
    }

    private void applySearch(TableRowSorter<JobsTableModel> sorter, String text) {
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        sorter.setRowFilter(new RowFilter<JobsTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends JobsTableModel, ? extends Object> entry) {
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

    private void editSelectedJob() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Jobs job = jobs.get(modelRow);
            new ModifyJob(this, getEmployeeById(job.getEmployeeId()), "Edit Job", job).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a job to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedJob() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            boolean deleted = dashboardController.crudJob(jobs.get(modelRow), 'd');
            if (deleted) {
                jobs.remove(modelRow);
                model.fireTableRowsDeleted(modelRow, modelRow);
                repaint();
                JOptionPane.showMessageDialog(this, "Job deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete job.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean updateJob(Jobs updatedJob) {
        boolean updated = this.dashboardController.crudJob(updatedJob, 'u');
        if (updated) {
            int jobIndex = -1;
            for (int i = 0; i < jobs.size(); i++) {
                if (jobs.get(i).getJobId() == updatedJob.getJobId()) {
                    jobIndex = i;
                    break;
                }
            }
            if (jobIndex != -1) {
                jobs.set(jobIndex, updatedJob);
                model.fireTableRowsUpdated(jobIndex, jobIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Job not found");
            }
        }
        return updated;
    }

    public boolean createJob(Jobs newJob) {
        boolean created = dashboardController.crudJob(newJob, 'c');
        if (created) {
            List<Jobs> jobs_ = dashboardController.getAllJobs();
            jobs_.sort((o1, o2) -> Integer.compare(o1.getJobId(), o2.getJobId()));
            jobs.add(jobs_.get(jobs.size() - 1));
            int newRowIndex = jobs.size() - 1;
            model.fireTableRowsInserted(newRowIndex, newRowIndex);
            repaint();
        }
        return created;
    }

    public Employee getEmployeeById(int id) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        return dashboardController.crudEmployee(employee, 'r') ? employee : null;
    }

    public void sendJobAssignmentEmail(int employeeId, int jobId) {
        dashboardController.sendJobAssignmentEmail(employeeId, jobId);
    }

    public void sendJobCompletionEmail(int orderId) {
        dashboardController.sendJobCompletionEmail(orderId);
    }

    private void showDeleteDialog() {
        DeleteSelectionDialog<Jobs> dialog = new DeleteSelectionDialog<>(
                this,
                "Delete Job",
                jobs,
                new String[]{"Job ID", "Order ID", "Employee ID", "Status"},
                job -> new Object[]{job.getJobId(), job.getOrderId(), job.getEmployeeId(), job.getStatus()},
                "Filter by Name / Status",
                0,
                3,
                job -> dashboardController.crudJob(job, 'd'),
                this::removeJobFromTable
        );
        dialog.setVisible(true);
    }

    private void removeJobFromTable(Jobs job) {
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getJobId() == job.getJobId()) {
                jobs.remove(i);
                model.fireTableRowsDeleted(i, i);
                repaint();
                return;
            }
        }
    }
}
