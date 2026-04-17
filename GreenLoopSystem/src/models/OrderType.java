package models;

import java.util.List;

public class OrderType {
    private int orderTypeId;
    private String typeName;

    // Getters and Setters
    public int getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static OrderType findByTypeName(List<OrderType> orderTypes, String typeName) {
        for (OrderType orderType : orderTypes) {
            if (orderType.getTypeName().equals(typeName)) {
                return orderType;
            }
        }
        return null; // Return null if typeName is not found
    }

}