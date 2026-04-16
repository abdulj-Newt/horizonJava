package com.example.customerservice.service;

import com.example.customerservice.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer getCustomerById(Long id);
    List<Customer> getAllCustomers();
}