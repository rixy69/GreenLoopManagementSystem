package services.Impl;

import models.Jobs;
import services.DatabaseConnectionService;
import services.JobsService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobsServiceImpl implements JobsService {

    private DatabaseConnectionService connectionService;

    public JobsServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addJob(Jobs job) {
        String query = "INSERT INTO Jobs (order_id, employee_id, job_description, start_date, end_date, status, assigned_date, job_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, job.getOrderId());
            stmt.setInt(2, job.getEmployeeId());
            stmt.setString(3, job.getJobDescription());
            stmt.setDate(4, new java.sql.Date(job.getStartDate().getTime()));
            stmt.setDate(5, new java.sql.Date(job.getEndDate().getTime()));
            stmt.setString(6, job.getStatus());
            stmt.setDate(7, new java.sql.Date(job.getAssignedDate().getTime()));
            stmt.setInt(8, job.getJobTypeId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addJobByOrderIdAndJobTypeId(int orderId, int jobTypeId, String jobDescription) {
        String query = "INSERT INTO Jobs (order_id, employee_id, job_description, start_date, end_date, status, assigned_date, job_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setNull(2, Types.NULL);
            stmt.setString(3, jobDescription);
            stmt.setNull(4, Types.NULL);
            stmt.setNull(5, Types.NULL);
            stmt.setString(6, "Job Created");
            stmt.setNull(7, Types.NULL);
            stmt.setInt(8, jobTypeId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean findJobByOrderIdAndJobTypeId(int orderId, int jobTypeId) {
        String query = "SELECT * FROM jobs WHERE order_id=? AND job_type_id=?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, jobTypeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true; // If there is at least one result, return true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Jobs getJob(int jobId) {
        String query = "SELECT * FROM Jobs WHERE job_id = ?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Jobs job = new Jobs();
                job.setJobId(rs.getInt("job_id"));
                job.setOrderId(rs.getInt("order_id"));
                job.setEmployeeId(rs.getInt("employee_id"));
                job.setJobDescription(rs.getString("job_description"));
                job.setStartDate(rs.getDate("start_date"));
                job.setEndDate(rs.getDate("end_date"));
                job.setStatus(rs.getString("status"));
                job.setAssignedDate(rs.getDate("assigned_date"));
                job.setJobTypeId(rs.getInt("job_type_id"));
                return job;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateJob(Jobs job) {

        StringBuilder queryBuilder = new StringBuilder("UPDATE Jobs SET order_id = ?, job_type_id = ?");

        if (job.getEmployeeId() != 0) { // Assuming 0 is the invalid/unset value for employee_id
            queryBuilder.append(", employee_id = ?");
        }
        if (job.getJobDescription() != null && !job.getJobDescription().isEmpty()) {
            queryBuilder.append(", job_description = ?");
        }
        if (job.getStatus() != null && !job.getStatus().isEmpty()) {
            queryBuilder.append(", status = ?");
        }
        if (job.getStartDate() != null) {
            queryBuilder.append(", start_date = ?");
        }
        if (job.getEndDate() != null) {
            queryBuilder.append(", end_date = ?");
        }
        if (job.getAssignedDate() != null) {
            queryBuilder.append(", assigned_date = ?");
        }
        queryBuilder.append(" WHERE job_id = ?");

        String query = queryBuilder.toString();

        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            stmt.setInt(paramIndex++, job.getOrderId());
            stmt.setInt(paramIndex++, job.getJobTypeId());

            if (job.getEmployeeId() != 0) {
                stmt.setInt(paramIndex++, job.getEmployeeId());
            }
            if (job.getJobDescription() != null && !job.getJobDescription().isEmpty()) {
                stmt.setString(paramIndex++, job.getJobDescription());
            }
            if (job.getStatus() != null && !job.getStatus().isEmpty()) {
                stmt.setString(paramIndex++, job.getStatus());
            }
            if (job.getStartDate() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(job.getStartDate().getTime()));
            }
            if (job.getEndDate() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(job.getEndDate().getTime()));
            }
            if (job.getAssignedDate() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(job.getAssignedDate().getTime()));
            }

            stmt.setInt(paramIndex, job.getJobId());

            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteJob(int jobId) {
        String query = "DELETE FROM Jobs WHERE job_id = ?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, jobId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteJobByOrderIdAndJobTypeId(int orderId, int jobTypeId) {
        String query = "DELETE FROM jobs WHERE order_id=? AND job_type_id=?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, jobTypeId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Jobs> getAllJobs() {
        List<Jobs> jobsList = new ArrayList<>();
        String query = "SELECT * FROM Jobs";
        try (Connection conn = connectionService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Jobs job = new Jobs();
                job.setJobId(rs.getInt("job_id"));
                job.setOrderId(rs.getInt("order_id"));
                job.setEmployeeId(rs.getInt("employee_id"));
                job.setJobDescription(rs.getString("job_description"));
                job.setStartDate(rs.getDate("start_date"));
                job.setEndDate(rs.getDate("end_date"));
                job.setStatus(rs.getString("status"));
                job.setAssignedDate(rs.getDate("assigned_date"));
                job.setJobTypeId(rs.getInt("job_type_id"));
                jobsList.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobsList;
    }

    @Override
    public List<Jobs> getAllJobsByEmployeeID(int employeeId) {
        List<Jobs> jobsList = new ArrayList<>();
        String query = "SELECT * FROM Jobs WHERE employee_id = ?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Jobs job = new Jobs();
                job.setJobId(rs.getInt("job_id"));
                job.setOrderId(rs.getInt("order_id"));
                job.setEmployeeId(rs.getInt("employee_id"));
                job.setJobDescription(rs.getString("job_description"));
                job.setStartDate(rs.getDate("start_date"));
                job.setEndDate(rs.getDate("end_date"));
                job.setStatus(rs.getString("status"));
                job.setAssignedDate(rs.getDate("assigned_date"));
                job.setJobTypeId(rs.getInt("job_type_id"));
                jobsList.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobsList;
    }
}
