package services.Impl;

import models.Part;
import services.DatabaseConnectionService;
import services.PartService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartServiceImpl implements PartService {
    private DatabaseConnectionService connectionService;
    public PartServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addPart(Part part) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO Parts (name, description, price, supplier_id) " +
                             "VALUES (?, ?, ?, ?)")) {
            // Set parameters
            statement.setString(1, part.getName());
            statement.setString(2, part.getDescription());
            statement.setDouble(3, part.getPrice());
            statement.setInt(4, part.getSupplierId());

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Part getPartById(int partId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Parts WHERE part_id = ?")) {
            statement.setInt(1, partId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getPartFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Part> getAllParts() {
        List<Part> parts = new ArrayList<>();
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM Parts");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                parts.add(getPartFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parts;
    }

    @Override
    public boolean updatePart(Part part) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE Parts SET name = ?, description = ?, price = ?, supplier_id = ? " +
                             "WHERE part_id = ?")) {
            statement.setString(1, part.getName());
            statement.setString(2, part.getDescription());
            statement.setDouble(3, part.getPrice());
            statement.setInt(4, part.getSupplierId());
            statement.setInt(5, part.getPartId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deletePart(int partId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM Parts WHERE part_id = ?")) {
            statement.setInt(1, partId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Part getPartWithRemainingQuantityById(int partId) {
        try (Connection connection = connectionService.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select p.part_id     as part_id,\n" +
                             "       p.name        as name,\n" +
                             "       p.description as description,\n" +
                             "       p.price       as price,\n" +
                             "       p.supplier_id as supplier_id,\n" +
                             "       SUM(i.quantity) as remaining_quantity\n" +
                             "from parts p\n" +
                             "left outer join inventory i on p.part_id = i.part_id\n" +
                             "group by i.part_id,p.part_id having p.part_id=?")) {
            statement.setInt(1, partId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Part part = getPartFromResultSet(resultSet);
                part.setRemainingQuantity(resultSet.getInt("remaining_quantity"));
                return part;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Part getPartFromResultSet(ResultSet resultSet) throws SQLException {
        Part part = new Part();
        part.setPartId(resultSet.getInt("part_id"));
        part.setName(resultSet.getString("name"));
        part.setDescription(resultSet.getString("description"));
        part.setPrice(resultSet.getDouble("price"));
        part.setSupplierId(resultSet.getInt("supplier_id"));
        return part;
    }
}
