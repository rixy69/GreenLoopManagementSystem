package services.Impl;

import models.OrderPart;
import services.OrderPartService;
import services.DatabaseConnectionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderPartServiceImpl implements OrderPartService {

    private DatabaseConnectionService connectionService;

    public OrderPartServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public OrderPart getOrderPartById(int orderPartID) {
        OrderPart orderPart = null;
        String query = "SELECT * FROM order_part WHERE order_part_id = ?";

        try (Connection connection = connectionService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderPartID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                orderPart = mapRowToSales(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderPart;
    }

    @Override
    public List<OrderPart> getAllOrderPartsByOrderId(int orderID){
        List<OrderPart> orderParts = new ArrayList<>();
        String query = "SELECT * FROM order_part WHERE order_id = ?";

        try (Connection connection = connectionService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ) {

            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderPart orderPart = mapRowToSales(rs);
                orderParts.add(orderPart);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderParts;
    }

    @Override
    public List<OrderPart> getAllOrderParts() {
        List<OrderPart> orderPartList = new ArrayList<>();
        String query = "SELECT * FROM order_part";

        try (Connection connection = connectionService.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                OrderPart orderPart = mapRowToSales(rs);
                orderPartList.add(orderPart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderPartList;
    }

    @Override
    public boolean createOrderPart(OrderPart orderPart) {
        String query = "INSERT INTO order_part (sales_date, part_id, part_description, supplier_id, quantity, order_type_id, price, order_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectionService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setSalesParameters(stmt, orderPart);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedOrderPartID = generatedKeys.getInt(1);
                        orderPart.setOrderPartID(generatedOrderPartID);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean updateOrderPart(OrderPart orderPart) {
        String query = "UPDATE order_part SET sales_date = ?, part_id = ?, part_description = ?, supplier_id = ?, quantity = ?, order_type_id = ?, price = ?, order_id = ? WHERE order_part_id = ?";

        try (Connection connection = connectionService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            setSalesParameters(stmt, orderPart);
            stmt.setInt(9, orderPart.getOrderPartID());
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected : " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteOrderPart(int orderPartId) {
        String query = "DELETE FROM order_part WHERE order_part_id = ?";

        try (Connection connection = connectionService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderPartId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private OrderPart mapRowToSales(ResultSet rs) throws SQLException {
        OrderPart orderPart = new OrderPart();
        orderPart.setOrderPartID(rs.getInt("order_part_id"));
        orderPart.setSalesDate(rs.getDate("sales_date"));
        orderPart.setPartId(rs.getInt("part_id"));
        orderPart.setPartDescription(rs.getString("part_description"));
        orderPart.setSupplierId(rs.getInt("supplier_id"));
        orderPart.setQuantity(rs.getInt("quantity"));
        orderPart.setOrderTypeId(rs.getInt("order_type_id"));
        orderPart.setPrice(rs.getDouble("price"));
        orderPart.setOrderId(rs.getInt("order_id"));

        return orderPart;
    }

    private void setSalesParameters(PreparedStatement stmt, OrderPart orderPart) throws SQLException {
        stmt.setDate(1, new java.sql.Date(orderPart.getSalesDate().getTime()));
        stmt.setInt(2, orderPart.getPartId());
        stmt.setString(3, orderPart.getPartDescription());
        stmt.setInt(4, orderPart.getSupplierId());
        stmt.setInt(5, orderPart.getQuantity());
        stmt.setInt(6, orderPart.getOrderTypeId());
        stmt.setDouble(7, orderPart.getPrice());
        stmt.setInt(8, orderPart.getOrderId());
    }
}
