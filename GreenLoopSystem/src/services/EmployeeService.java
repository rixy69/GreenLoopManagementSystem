package services;

import models.Employee;

import java.util.List;

public interface EmployeeService {
    boolean addEmployee(Employee employee);
    Employee getEmployeeById(int employeeId);
    List<Employee> getAllEmployees();
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(int employeeId);
    boolean authenticate(String username, String password);
    Employee authenticate(Employee employee);
}
