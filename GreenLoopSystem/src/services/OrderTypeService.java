package services;

import models.OrderType;
import java.util.List;

public interface OrderTypeService {
    OrderType getOrderTypeById(int orderTypeId);
    List<OrderType> getAllOrderTypes();
}