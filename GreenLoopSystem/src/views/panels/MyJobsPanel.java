package views.panels;

import controllers.DashboardController;
import models.Employee;
import models.Jobs;
import models.JobsTableModel;
import views.ModifyMyJob;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MyJobsPanel extends DashboardModulePanel {
    private final List<Jobs> jobs;
    private final JobsTableModel model;
    private final JTable table;
    private final DashboardController dashboardController;

    public MyJobsPanel(DashboardController dashboardController, List<Jobs> jobs) {
        this.jobs = jobs;
        this.dashboardController = dashboardController;
        this.model = new JobsTableModel(jobs);
        this.table = new JTable(model);
        createUIComponents();
    }

    private void createUIComponents() {
        addPageTitle("My Assignments", "A focused workspace for delivery and assignment updates.");

        TableRowSorter<JobsTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        DashboardTheme.styleTable(table);

        JPanel searchCard = createCard(30, 96, 860, 96);
        JLabel searchLabel = DashboardTheme.createSectionLabel("Search My Assignments");
        searchLabel.setBounds(24, 18, 180, 22);
        searchCard.add(searchLabel);

        JTextField searchField = buildTextField(24, 48, 790, 38);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
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
            }
        });
        searchCard.add(searchField);

        JPanel tableCard = createCard(30, 210, 860, 420);
        JLabel tableLabel = DashboardTheme.createSectionLabel("Assigned Deliveries");
        tableLabel.setBounds(24, 18, 220, 22);
        tableCard.add(tableLabel);
        tableCard.add(buildTableScrollPane(table, 24, 54, 812, 342));

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("View/Edit");
        editItem.addActionListener(e -> editSelectedJob());
        contextMenu.add(editItem);

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

    private void editSelectedJob() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            Jobs job = jobs.get(modelRow);
            new ModifyMyJob(this, getEmployeeById(job.getEmployeeId()), "Edit My Job", job).setVisible(true);
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
}
