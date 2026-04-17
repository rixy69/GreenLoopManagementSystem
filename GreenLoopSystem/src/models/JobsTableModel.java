package models;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JobsTableModel extends AbstractTableModel {
    private final List<Jobs> jobs;
    private final String[] columnNames = {
            "Job ID", "Order ID", "Employee ID", "Job Description",
            "Start Date", "End Date", "Status", "Assigned Date", "Job Type ID"
    };

    public JobsTableModel(List<Jobs> jobs) {
        // Sort the jobs by Job ID in ascending order
        Collections.sort(jobs, Comparator.comparingInt(Jobs::getJobId));
        this.jobs = jobs;
    }

    @Override
    public int getRowCount() {
        return jobs.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Jobs job = jobs.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return job.getJobId();
            case 1:
                return job.getOrderId();
            case 2:
                return job.getEmployeeId();
            case 3:
                return job.getJobDescription();
            case 4:
                return job.getStartDate();
            case 5:
                return job.getEndDate();
            case 6:
                return job.getStatus();
            case 7:
                return job.getAssignedDate();
            case 8:
                return job.getJobTypeId();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
