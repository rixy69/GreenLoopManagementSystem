package models;

public class Supplier {
    private int supplierId;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String address;

    public Supplier() {
    }

    public Supplier(int supplierId, String name, String contactName, String contactEmail, String contactPhone, String address) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.address = address;
    }

    // Getters and setters

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void clone(Supplier supplier) {
        this.supplierId = supplier.supplierId;
        this.name = supplier.name;
        this.contactName = supplier.contactName;
        this.contactEmail = supplier.contactEmail;
        this.contactPhone = supplier.contactPhone;
        this.address = supplier.address;
    }
}
