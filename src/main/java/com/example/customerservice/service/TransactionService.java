package com.example.customerservice.service;

import com.example.customerservice.model.Transaction;

import java.util.List;

import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;

import java.math.BigDecimal;

public interface TransactionService {
    Transaction transferMoney(Long fromCustomerId, Long toCustomerId, BigDecimal amount);
    Transaction transferMoneyByPLSQL(Long fromCustomerId, Long toCustomerId, BigDecimal amount);
    Transaction payBill(Long customerId, String billType, String accountNumber, BigDecimal amount);
    Transaction payBillByPLSQL(Long customerId, String billType, String accountNumber, BigDecimal amount);
    Transaction addMoney(Long customerId, BigDecimal amount);
    Transaction addMoneyByPLSQL(Long customerId, BigDecimal amount);
    List<Transaction> getTransactions(String customerName);
    List<Transaction> getTransactionsByCustomerId(Long customerId);
    void createTransaction(Account account, BigDecimal amount, String type);
}