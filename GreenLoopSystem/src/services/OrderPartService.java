package services;

import models.OrderPart;

import java.util.List;

public interface OrderPartService {
    OrderPart getOrderPartById(int orderPartID);
    List<OrderPart> getAllOrderPartsByOrderId(int orderID);
    List<OrderPart> getAllOrderParts();
    boolean createOrderPart(OrderPart orderPart);
    boolean updateOrderPart(OrderPart orderPart);
    boolean deleteOrderPart(int salesId);
}