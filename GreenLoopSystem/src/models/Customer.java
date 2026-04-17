package models;

public class Customer {
    private int customerId;
    private String name;
    private String address;
    private String mobile;
    private String email;

    public Customer(int customerId, String name, String address, String mobile, String email) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
    }

    public Customer() {}

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
