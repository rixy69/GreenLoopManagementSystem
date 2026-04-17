package services.Impl;

import models.Supplier;
import services.DatabaseConnectionService;
import services.SupplierService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierServiceImpl implements SupplierService {
    private DatabaseConnectionService connectionService;
    public SupplierServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addSupplier(Supplier supplier) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Suppliers (name, contact_name, contact_email, contact_phone, address) " +
                             "VALUES (?, ?, ?, ?, ?)")) {
            // Set parameters
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getContactName());
            statement.setString(3, supplier.getContactEmail());
            statement.setString(4, supplier.getContactPhone());
            statement.setString(5, supplier.getAddress());

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Supplier getSupplierById(int supplierId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Suppliers WHERE supplier_id = ?")) {
            statement.setInt(1, supplierId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getSupplierFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Suppliers");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                suppliers.add(getSupplierFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Suppliers SET name = ?, contact_name = ?, contact_email = ?, contact_phone = ?, address = ? " +
                             "WHERE supplier_id = ?")) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getContactName());
            statement.setString(3, supplier.getContactEmail());
            statement.setString(4, supplier.getContactPhone());
            statement.setString(5, supplier.getAddress());
            statement.setInt(6, supplier.getSupplierId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteSupplier(int supplierId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Suppliers WHERE supplier_id = ?")) {
            statement.setInt(1, supplierId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Supplier getSupplierFromResultSet(ResultSet resultSet) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(resultSet.getInt("supplier_id"));
        supplier.setName(resultSet.getString("name"));
        supplier.setContactName(resultSet.getString("contact_name"));
        supplier.setContactEmail(resultSet.getString("contact_email"));
        supplier.setContactPhone(resultSet.getString("contact_phone"));
        supplier.setAddress(resultSet.getString("address"));
        return supplier;
    }
}
