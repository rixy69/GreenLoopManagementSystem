package models;

public class Part {
    private int partId;
    private String name;
    private String description;
    private double price;
    private int supplierId;
    private int remainingQuantity;
    private Supplier supplier;

    public Part() {
    }

    public Part(int partId, String name, String description, double price, int supplierId) {
        this.partId = partId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.supplierId = supplierId;
    }

    // Getters and setters
    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public void clone_(Part part) {
        this.partId = part.partId;
        this.name = part.name;
        this.description = part.description;
        this.price = part.price;
        this.supplierId = part.supplierId;
        this.remainingQuantity = part.remainingQuantity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
