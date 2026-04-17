package services.Impl;

import models.Employee;
import services.DatabaseConnectionService;
import services.EmployeeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private DatabaseConnectionService connectionService;
    public EmployeeServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addEmployee(Employee employee) {

        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO employees (title, first_name, last_name, username, address, mobile, email, password, schedule, role_id) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            // Set parameters
            statement.setString(1, employee.getTitle());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getLastName());
            statement.setString(4, employee.getUsername());
            statement.setString(5, employee.getAddress());
            statement.setString(6, employee.getMobile());
            statement.setString(7, employee.getEmail());
            statement.setString(8, employee.getPassword());

            statement.setString(9, employee.getSchedule());
            statement.setInt(10, employee.getRoleId());

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public Employee getEmployeeById(int employeeId) {
        try (Connection connection = connectionService.getConnection()) {
            if (connection == null) {
                throw new SQLException("Unable to obtain database connection. Please check MySQL service, credentials, and the greenloop database.");
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM employees WHERE employee_id = ?")) {
                statement.setInt(1, employeeId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    Employee employee = getEmployeeFromResultSet(null, resultSet);
                    return employee;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM employees");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = getEmployeeFromResultSet(null, resultSet);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE employees SET title = ?, first_name = ?, last_name = ?, username = ?, address = ?, mobile = ?, email = ?, password = ?, schedule = ?, role_id = ? WHERE employee_id = ?")) {
            statement.setString(1, employee.getTitle());
            statement.setString(2, employee.getFirstName());
            statement.setString(3, employee.getLastName());
            statement.setString(4, employee.getUsername());
            statement.setString(5, employee.getAddress());
            statement.setString(6, employee.getMobile());
            statement.setString(7, employee.getEmail());
            statement.setString(8, employee.getPassword());
            statement.setString(9, employee.getSchedule());
            statement.setInt(10, employee.getRoleId());
            statement.setInt(11, employee.getEmployeeId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int employeeId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM employees WHERE employee_id = ?")) {
            statement.setInt(1, employeeId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    @Override
    public boolean authenticate(String username, String password) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM employees WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Employee authenticate(Employee employee) {
        try (Connection connection = connectionService.getConnection()) {
            if (connection == null) {
                throw new SQLException("Unable to obtain database connection. Please check MySQL service, credentials, and the greenloop database.");
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM employees WHERE username = ? AND password = ?")) {
                statement.setString(1, employee.getUsername());
                statement.setString(2, employee.getPassword());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return getEmployeeFromResultSet(employee, resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Employee getEmployeeFromResultSet(Employee employee, ResultSet resultSet) throws SQLException {
        if (employee == null) {
            employee = new Employee(resultSet.getString("username"), resultSet.getString("password"));
        }
        employee.setEmployeeId(resultSet.getInt("employee_id"));
        employee.setTitle(resultSet.getString("title"));
        employee.setFirstName(resultSet.getString("first_name"));
        employee.setLastName(resultSet.getString("last_name"));
        employee.setAddress(resultSet.getString("address"));
        employee.setMobile(resultSet.getString("mobile"));
        employee.setEmail(resultSet.getString("email"));
        employee.setPassword(resultSet.getString("password"));
        employee.setSchedule(resultSet.getString("schedule"));
        employee.setRoleId(resultSet.getInt("role_id"));
        employee.setAddress(resultSet.getString("address"));
        return employee;
    }

}
