package com.example.customerservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountDTO {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private List<TransactionDTO> transactions;

    // Constructors
    public AccountDTO() {
    }

    public AccountDTO(Long id, String accountNumber, BigDecimal balance, List<TransactionDTO> transactions) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactions = transactions;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}