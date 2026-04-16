package com.example.customerservice.exception;

public class AccountNotFoundException extends RuntimeException {
    private Long customerId;
    private String customerName;
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Long customerId, String customerName) {
        super(message);
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }
}