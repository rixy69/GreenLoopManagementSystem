package services.Impl;

import models.OrderType;
import services.OrderTypeService;
import services.DatabaseConnectionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderTypeServiceImpl implements OrderTypeService {

    private DatabaseConnectionService connectionService;

    public OrderTypeServiceImpl(DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public OrderType getOrderTypeById(int orderTypeId) {
        OrderType orderType = null;
        String query = "SELECT * FROM ordertype WHERE order_type_id = ?";

        try (Connection connection = connectionService.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, orderTypeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                orderType = mapRowToOrderType(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderType;
    }

    @Override
    public List<OrderType> getAllOrderTypes() {
        List<OrderType> orderTypeList = new ArrayList<>();
        String query = "SELECT * FROM ordertype";

        try (Connection connection = connectionService.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                OrderType orderType = mapRowToOrderType(rs);
                orderTypeList.add(orderType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderTypeList;
    }

    private OrderType mapRowToOrderType(ResultSet rs) throws SQLException {
        OrderType orderType = new OrderType();
        orderType.setOrderTypeId(rs.getInt("order_type_id"));
        orderType.setTypeName(rs.getString("type_name"));
        return orderType;
    }
}