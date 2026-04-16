package com.example.customerservice.delegate;

import com.example.customerservice.model.Customer;
import com.example.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerDelegate {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(String name, int age, double salary) {
        if (age < 0) {
            throw new IllegalArgumentException("Invalid age");
        }
        if (salary <= 0) {
            throw new IllegalArgumentException("Invalid salary");
        }
        Customer customer = new Customer();
        customer.setCustomerName(name);
        return customerRepository.save(customer);
    }
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            System.out.println("Customer ID: " + customer.getId() + ", Name: " + customer.getCustomerName());
        }
        return customers;
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    public Customer updateCustomer(Long id, String name, int age, double salary) {
        Customer customer = getCustomerById(id);
        customer.setCustomerName(name);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}