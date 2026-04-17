package services.Impl;

import models.Customer;
import services.DatabaseConnectionService;
import services.CustomerService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private DatabaseConnectionService connectionService;
    public CustomerServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Customers (name, address, mobile, email) VALUES (?, ?, ?, ?)")) {
            // Set parameters
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getMobile());
            statement.setString(4, customer.getEmail());

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Customer getCustomerById(int customerId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Customers WHERE customer_id = ?")) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getCustomerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Customers");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                customers.add(getCustomerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Customers SET name = ?, address = ?, mobile = ?, email = ? WHERE customer_id = ?")) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getMobile());
            statement.setString(4, customer.getEmail());
            statement.setInt(5, customer.getCustomerId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Customers WHERE customer_id = ?")) {
            statement.setInt(1, customerId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Customer getCustomerFromResultSet(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setName(resultSet.getString("name"));
        customer.setAddress(resultSet.getString("address"));
        customer.setMobile(resultSet.getString("mobile"));
        customer.setEmail(resultSet.getString("email"));
        return customer;
    }
}
