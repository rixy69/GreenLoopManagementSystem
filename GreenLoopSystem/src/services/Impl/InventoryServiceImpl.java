package services.Impl;

import models.Inventory;
import services.DatabaseConnectionService;
import services.InventoryService;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryServiceImpl implements InventoryService {
    private DatabaseConnectionService connectionService;

    public InventoryServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addInventory(Inventory inventory) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Inventory (part_id, quantity, location) VALUES (?, ?, ?)")) {
            statement.setInt(1, inventory.getPartId());
            statement.setInt(2, inventory.getQuantity());
            statement.setString(3, inventory.getLocation());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Inventory getInventoryById(int inventoryId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Inventory WHERE inventory_id = ?")) {
            statement.setInt(1, inventoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getInventoryFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Inventory getInventoryByPartId(int partID) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Inventory WHERE part_id = ?")) {
            statement.setInt(1, partID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getInventoryFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Inventory> getAllInventories() {
        List<Inventory> inventories = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Inventory");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                inventories.add(getInventoryFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventories;
    }

    @Override
    public boolean updateInventory(Inventory inventory) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Inventory SET part_id = ?, quantity = ?, location = ? WHERE inventory_id = ?")) {
            statement.setInt(1, inventory.getPartId());
            statement.setInt(2, inventory.getQuantity());
            statement.setString(3, inventory.getLocation());
            statement.setInt(4, inventory.getInventoryId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteInventory(int inventoryId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Inventory WHERE inventory_id = ?")) {
            statement.setInt(1, inventoryId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getQuantityByPartID(int partID) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT quantity FROM Inventory WHERE part_id = ?")) {
            statement.setInt(1, partID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Default return value if no result or error occurs
    }


    private Inventory getInventoryFromResultSet(ResultSet resultSet) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(resultSet.getInt("inventory_id"));
        inventory.setPartId(resultSet.getInt("part_id"));
        inventory.setQuantity(resultSet.getInt("quantity"));
        inventory.setLocation(resultSet.getString("location"));
        return inventory;
    }
}
