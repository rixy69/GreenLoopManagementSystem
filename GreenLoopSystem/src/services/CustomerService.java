package services;

import models.Customer;

import java.util.List;

public interface CustomerService {
    boolean addCustomer(Customer customer);
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(int customerId);
}

