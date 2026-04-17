package services;
import models.Order;

import java.util.List;

public interface OrderService {
    boolean addOrder(Order order);
    Order getOrder(int orderId);
    boolean updateOrder(Order order);
    boolean deleteOrder(int orderId);
    List<Order> getAllOrders();
}