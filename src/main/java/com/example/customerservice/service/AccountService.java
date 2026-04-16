package com.example.customerservice.service;

import com.example.customerservice.model.Account;

import java.math.BigDecimal;

public interface AccountService {
    BigDecimal getBalance(long accountId);
    BigDecimal deposit(long accountId, BigDecimal amount);
    BigDecimal withdraw(long accountId, BigDecimal amount);
    void addMoney(Long customerId, BigDecimal amount);
    Account getAccountByUsername(String username);
    Account getAccountByCustomerId(Long customerId);
}