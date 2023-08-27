package com.assignment.Api;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private List<CustomerForm> customer = new ArrayList<>();
    public void addCustomer(CustomerForm customerForm) {
        customer.add(customerForm);
    }
}
