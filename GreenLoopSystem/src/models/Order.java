package models;

import java.util.Date;

public class Order {
    private int orderId;
    private int customerId;
    private Date orderDate;
    private boolean isRepair;
    private double repairServiceFee;
    private boolean isRepaint;
    private double repaintServiceFee;
    private double partsCost;
    private double totalPrice;

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isRepair() {
        return isRepair;
    }

    public void setRepair(boolean isRepair) {
        this.isRepair = isRepair;
    }

    public double getRepairServiceFee() {
        return repairServiceFee;
    }

    public void setRepairServiceFee(double repairServiceFee) {
        this.repairServiceFee = repairServiceFee;
    }

    public boolean isRepaint() {
        return isRepaint;
    }

    public void setRepaint(boolean isRepaint) {
        this.isRepaint = isRepaint;
    }

    public double getRepaintServiceFee() {
        return repaintServiceFee;
    }

    public void setRepaintServiceFee(double repaintServiceFee) {
        this.repaintServiceFee = repaintServiceFee;
    }

    public double getPartsCost() {
        return partsCost;
    }

    public void setPartsCost(double partsCost) {
        this.partsCost = partsCost;
    }

}
