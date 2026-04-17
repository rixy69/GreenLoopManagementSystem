package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderPart {
    private int orderPartID;
    private Date salesDate;
    private Integer partId;
    private String partDescription;
    private Integer supplierId;
    private Integer quantity;
    private Integer orderTypeId;
    private Double price;
    private Integer orderId;





    public OrderPart(BillItem billItem, Order order) {
        this.orderId = billItem.getOrderPartID();
        this.orderId = order.getOrderId();
        this.salesDate = order.getOrderDate();
        this.partId = billItem.getPartId();
        this.partDescription = billItem.getPartName();
        this.supplierId = billItem.getSupplierID();
        this.quantity = billItem.getQuantity();
        this.orderTypeId = billItem.getOrderTypeId();
        this.price = billItem.getTotalPrice();
    }

    public OrderPart() {
    }


    public static List<OrderPart> convertToOrderPart(List<BillItem> billItems, Order order){
        List<OrderPart> orderParts = new ArrayList<>();

        billItems.forEach(billItem -> {
            orderParts.add(new OrderPart(billItem, order));
        });

        return orderParts;
    }
    // Getters and Setters
    public int getOrderPartID() {
        return orderPartID;
    }

    public void setOrderPartID(int orderPartID) {
        this.orderPartID = orderPartID;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(Integer orderTypeId) {
        this.orderTypeId = orderTypeId;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderPart{" +
                "orderPartID=" + orderPartID +
                ", salesDate=" + salesDate +
                ", partId=" + partId +
                ", partDescription='" + partDescription + '\'' +
                ", supplierId=" + supplierId +
                ", quantity=" + quantity +
                ", orderTypeId=" + orderTypeId +
                ", price=" + price +
                ", orderId=" + orderId +
                '}';
    }


}
