package services.Impl;
import models.Order;
import services.DatabaseConnectionService;
import services.OrderService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    private DatabaseConnectionService connectionService;

    public OrderServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public boolean addOrder(Order order) {
        String query = "INSERT INTO Orders (customer_id, order_date, total_price, is_repair, repair_service_fee, is_repaint, repaint_service_fee) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getCustomerId());
            stmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setDouble(3, order.getTotalPrice());
            stmt.setBoolean(4, order.isRepair());
            stmt.setDouble(5, order.getRepairServiceFee());
            stmt.setBoolean(6, order.isRepaint());
            stmt.setDouble(7, order.getRepaintServiceFee());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    System.out.println(id);
                    order.setOrderId(id); // Assuming setId method exists in Order class
                } else {
                    System.out.println("ELSE CRE");
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Order getOrder(int orderId) {
        String query = "SELECT * FROM Orders WHERE order_id = ?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setRepair(rs.getBoolean("is_repair"));
                order.setRepairServiceFee(rs.getDouble("repair_service_fee"));
                order.setRepaint(rs.getBoolean("is_repaint"));
                order.setRepaintServiceFee(rs.getDouble("repaint_service_fee"));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateOrder(Order order) {
        String query = "UPDATE Orders SET customer_id = ?, order_date = ?, total_price = ?, is_repair = ?, repair_service_fee = ?, is_repaint = ?, repaint_service_fee = ? WHERE order_id = ?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, order.getCustomerId());
            stmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setDouble(3, order.getTotalPrice());
            stmt.setBoolean(4, order.isRepair());
            stmt.setDouble(5, order.getRepairServiceFee());
            stmt.setBoolean(6, order.isRepaint());
            stmt.setDouble(7, order.getRepaintServiceFee());
            stmt.setInt(8, order.getOrderId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteOrder(int orderId) {
        String query = "DELETE FROM Orders WHERE order_id = ?";
        try (Connection conn = connectionService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";
        try (Connection conn = connectionService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setCustomerId(rs.getInt("customer_id"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setRepair(rs.getBoolean("is_repair"));
                order.setRepairServiceFee(rs.getDouble("repair_service_fee"));
                order.setRepaint(rs.getBoolean("is_repaint"));
                order.setRepaintServiceFee(rs.getDouble("repaint_service_fee"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
