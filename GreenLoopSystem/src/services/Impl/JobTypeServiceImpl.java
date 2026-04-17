package services.Impl;
import models.JobType;
import services.JobTypeService;
import services.DatabaseConnectionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobTypeServiceImpl implements JobTypeService {

    private DatabaseConnectionService connectionService;

    public JobTypeServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public JobType getJobTypeById(int jobTypeId) {
        JobType jobType = null;
        String query = "SELECT * FROM jobtype WHERE job_type_id = ?";

        try (Connection connection = connectionService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, jobTypeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                jobType = mapRowToJobType(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobType;
    }

    @Override
    public List<JobType> getAllJobTypes() {
        List<JobType> jobTypeList = new ArrayList<>();
        String query = "SELECT * FROM jobtype";

        try (Connection connection = connectionService.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JobType jobType = mapRowToJobType(rs);
                jobTypeList.add(jobType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobTypeList;
    }

    private JobType mapRowToJobType(ResultSet rs) throws SQLException {
        JobType jobType = new JobType();
        jobType.setJobTypeId(rs.getInt("job_type_id"));
        jobType.setTypeName(rs.getString("type_name"));
        return jobType;
    }
}