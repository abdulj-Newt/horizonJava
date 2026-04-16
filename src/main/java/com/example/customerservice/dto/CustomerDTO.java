package com.example.customerservice.dto;

public class CustomerDTO {

    private Long id;
    private String customerName;
    private AccountDTO account;

    // Constructors
    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String customerName, AccountDTO account) {
        this.id = id;
        this.customerName = customerName;
        this.account = account;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }
}