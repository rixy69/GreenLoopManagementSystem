package services.Impl;

import models.Property;
import services.DatabaseConnectionService;
import services.PropertyService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PropertyServiceImpl implements PropertyService {
    private DatabaseConnectionService connectionService;

    public PropertyServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addProperty(Property property) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Properties (property_key, value, type) VALUES (?, ?, ?)")) {
            statement.setString(1, property.getPropertyKey());
            statement.setString(2, property.getValue());
            statement.setString(3, property.getType());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Property getPropertyByKey(String propertyKey) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Properties WHERE property_key = ?")) {
            statement.setString(1, propertyKey);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getPropertyFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Properties");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                properties.add(getPropertyFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @Override
    public boolean updateProperty(Property property) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Properties SET value = ?, type = ? WHERE property_key = ?")) {
            statement.setString(1, property.getValue());
            statement.setString(2, property.getType());
            statement.setString(3, property.getPropertyKey());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProperty(String propertyKey) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Properties WHERE property_key = ?")) {
            statement.setString(1, propertyKey);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Property> getAllPropertiesByType(String type) {
        List<Property> properties = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Properties WHERE type = ?")) {
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                properties.add(getPropertyFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return properties;
    }

    @Override
    public Property getPropertyByKeyAndType(String propertyKey, String type) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Properties WHERE property_key = ? AND type = ?")) {
            statement.setString(1, propertyKey);
            statement.setString(2, type);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getPropertyFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addOrUpdateProperties(List<Property> properties) {
        try {
            for (Property property : properties) {
                if (getPropertyByKey(property.getPropertyKey()) != null) {
                    updateProperty(property);
                } else {
                    addProperty(property);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private Property getPropertyFromResultSet(ResultSet resultSet) throws SQLException {
        Property property = new Property();
        property.setPropertyKey(resultSet.getString("property_key"));
        property.setValue(resultSet.getString("value"));
        property.setType(resultSet.getString("type"));
        return property;
    }
}
