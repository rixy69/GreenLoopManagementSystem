package services;

import models.JobType;
import java.util.List;

public interface JobTypeService {
    JobType getJobTypeById(int jobTypeId);
    List<JobType> getAllJobTypes();
}