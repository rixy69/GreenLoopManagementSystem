package services;
import models.Jobs;
import java.util.List;


public interface JobsService {
    boolean addJob(Jobs job);
    boolean addJobByOrderIdAndJobTypeId(int orderId, int jobTypeId, String jobDescription);
    boolean findJobByOrderIdAndJobTypeId(int orderId, int jobTypeId);
    Jobs getJob(int jobId);
    boolean updateJob(Jobs job);
    boolean deleteJob(int jobId);
    boolean deleteJobByOrderIdAndJobTypeId(int orderId, int jobTypeId);
    List<Jobs> getAllJobs();
    List<Jobs> getAllJobsByEmployeeID(int employeeId);
}
