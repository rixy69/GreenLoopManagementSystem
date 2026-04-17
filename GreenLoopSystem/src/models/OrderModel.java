package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderModel {
    private Order order;
    private List<Customer> customers;
    private List<Supplier> suppliers;
    private List<Part> parts;
    private List<OrderType> orderTypes;
    private List<BillItem> billItems = new ArrayList<>();
    private Customer currentCustomer;
    private Part currentPart;
    public OrderModel(Order order, List<Customer> customers, List<Supplier> suppliers, List<Part> parts, List<OrderType> orderTypes) {
        this.customers = customers;
        this.order = order;
        this.suppliers = suppliers;
        this.parts = parts;
        this.orderTypes = orderTypes;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public List<OrderType> getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(List<OrderType> orderTypes) {
        this.orderTypes = orderTypes;
    }


    boolean isNewOrder() {
        return this.order.getOrderId() == 0;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public String[] getYMD() {
        String year = Integer.toString(LocalDate.now().getYear()),
                month = Integer.toString(LocalDate.now().getMonthValue()),
                day = Integer.toString(LocalDate.now().getDayOfMonth());
        return new String[]{year, month, day};
    }

    public String getOrderTitle() {
        if (isNewOrder()) {
            return "New Order";
        } else {
            return "Order ID : " + order.getOrderId();
        }
    }

    public Part getCurrentPart() {
        return currentPart;
    }

    public void setCurrentPart(Part currentPart) {
        this.currentPart = currentPart;
    }

    public List<BillItem> getBillItems() {
        return billItems;
    }

    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public Double calcTotal() {
        double partsCost_ = 0.0;
        for (int i = 0; i < billItems.size(); i++) {
            partsCost_ += billItems.get(i).getTotalPrice();
        }
        double total = partsCost_+ order.getRepaintServiceFee()+ order.getRepairServiceFee();
        order.setTotalPrice(total);
        return total;
    }
}
