package models;

public class Sale {
    private int partId;
    private String partName;
    private boolean isService;
    private int quantity;
    private double unitPrice;
    private double totalSales;
    private String serviceName;

    public Sale() {
    }

    public Sale(int partId, String partName, boolean isService, int quantity, double unitPrice, double totalPrice, String serviceName) {
        this.partId = partId;
        this.partName = partName;
        this.isService = isService;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalSales = totalPrice;
        this.serviceName = serviceName;
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

    public boolean isService() {
        return isService;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "partId=" + partId +
                ", partName='" + partName + '\'' +
                ", isService=" + isService +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalSales=" + totalSales +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
