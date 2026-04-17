package models;

import java.util.ArrayList;
import java.util.List;

public class BillItem {
    private int orderPartID;
    private int partId;
    private String partName;
    private int supplierID;
    private Double unitPrice;
    private int quantity;
    private Double totalPrice;
    private int orderTypeId;
    private String type;

    //When Order creating or updating this Order Part Object will be created
    //This  notSavedQuantity is quantity which was not subtracted from inventory
    //This variable help to maintain temporary record
    private Integer notSavedQuantity;

    public BillItem() {
    }

    public BillItem(OrderModel orderModel, int quantity, String type) {
        Part part = orderModel.getCurrentPart();
        this.partId = part.getPartId();
        this.partName = part.getName();
        this.supplierID = part.getSupplierId();
        this.unitPrice = part.getPrice();
        this.quantity = quantity;
        this.notSavedQuantity = quantity;
        this.totalPrice = quantity * part.getPrice();
        OrderType orderType = OrderType.findByTypeName(orderModel.getOrderTypes(), type);
        this.orderTypeId = orderType.getOrderTypeId();
        this.type = type;
    }

    public static List<BillItem> convertToBillItems(List<OrderPart> orderParts){
        List<BillItem> billItems = new ArrayList<>();
        orderParts.forEach(orderPart -> {

            BillItem billItem = new BillItem();

            billItem.orderPartID = orderPart.getOrderPartID();
            billItem.partId = orderPart.getPartId();
            billItem.partName = orderPart.getPartDescription();
            billItem.supplierID = orderPart.getSupplierId();
            billItem.unitPrice = 1.0;
            billItem.quantity = orderPart.getQuantity();
            billItem.totalPrice = orderPart.getPrice();
            billItem.orderTypeId = orderPart.getOrderTypeId();
            billItem.type = "Type";

            billItems.add(billItem);
        });
        return billItems;
    }


    public BillItem(OrderPart orderPart, Double unitPrice, String type){
        this.orderPartID = orderPart.getOrderPartID();
        this.partId = orderPart.getPartId();
        this.partName = orderPart.getPartDescription();
        this.supplierID = orderPart.getSupplierId();
        this.unitPrice = unitPrice;
        this.quantity = orderPart.getQuantity();
        this.totalPrice = orderPart.getPrice();
        this.orderTypeId = orderPart.getOrderTypeId();
        this.type = type;
        this.notSavedQuantity = 0;
    }


    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity2) {
        this.quantity = this.quantity + quantity2;
        this.totalPrice = this.quantity * this.unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.quantity * this.unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void calculateTotalPrice() {
        this.totalPrice = this.unitPrice * this.quantity;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderPartID() {
        return orderPartID;
    }

    public void setOrderPartID(int orderPartID) {
        this.orderPartID = orderPartID;
    }

    public Integer getNotSavedQuantity() {
        return notSavedQuantity;
    }

    public void setNotSavedQuantity(Integer notSavedQuantity) {
        this.notSavedQuantity = notSavedQuantity;
    }

}
