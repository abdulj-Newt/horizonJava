package com.example.customerservice.service;

import com.example.customerservice.exception.AccountNotFoundException;
import com.example.customerservice.exception.InsufficientFundsException;
import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;
import com.example.customerservice.repository.AccountRepository;
import com.example.customerservice.repository.CustomerRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    
    // Legacy: Using Vector instead of List
    private Vector<String> transactionHistory = new Vector<String>();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionService transactionService;

    @Override
    public BigDecimal getBalance(long accountId) {
        // Legacy: Manual validation instead of using validation annotations
        if (accountId <= 0) {
            throw new IllegalArgumentException("Account ID must be positive");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
        // Legacy: Using Vector operations
        transactionHistory.addElement("Balance checked for account: " + accountId + " at " + new Date());
        
        return account.getBalance();
    }

    @Override
    @Transactional
    public BigDecimal deposit(long accountId, BigDecimal amount) {
        // Legacy: Manual null and validation checks
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        Account account = null;
        try {
            account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        } catch (Exception e) {
            // Legacy: Catching generic Exception
            logger.error("Error finding account: " + e.getMessage());
            throw new RuntimeException("Database error occurred", e);
        }
        
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        transactionService.createTransaction(account, amount, "DEPOSIT");
        
        // Legacy: String concatenation in logging
        logger.info("Deposited " + amount + " to account " + accountId);
        transactionHistory.addElement("Deposit: $" + amount + " to account " + accountId);
        
        return account.getBalance();
    }

    @Override
    @Transactional
    public BigDecimal withdraw(long accountId, BigDecimal amount) {
        // Legacy: Verbose manual validation
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
                
        // Legacy: Manual balance check with verbose logging
        BigDecimal currentBalance = account.getBalance();
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        
        if (currentBalance.compareTo(amount) < 0) {
            String errorMsg = "Insufficient funds. Current: $" + currentBalance + ", Requested: $" + amount;
            logger.error(errorMsg);
            throw new InsufficientFundsException("Insufficient funds");
        }
        
        account.setBalance(currentBalance.subtract(amount));
        accountRepository.save(account);
        transactionService.createTransaction(account, amount, "WITHDRAWAL");
        
        // Legacy: Using Vector enumeration
        transactionHistory.addElement("Withdrawal: $" + amount + " from account " + accountId);
        
        return account.getBalance();
    }

    @Override
    @Transactional
    public void addMoney(Long customerId, BigDecimal amount) {
        // Legacy: Using StringUtils from commons-lang 2.x
        if (customerId == null || StringUtils.isEmpty(customerId.toString())) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        
        logger.info("Attempting to add money for customerId: {}", customerId);
        
        Account account = null;
        Customer customer = null;
        
        // Legacy: Separate try-catch blocks instead of unified exception handling
        try {
            account = accountRepository.findByCustomerId(customerId)
                    .orElseThrow(() -> {
                        logger.error("Account not found for customerId: {}", customerId);
                        return new AccountNotFoundException("Account not found");
                    });
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error finding account: " + e.getMessage());
            throw new RuntimeException("Database error", e);
        }
        
        try {
            customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new AccountNotFoundException("Customer not found for id: " + customerId));
        } catch (Exception e) {
            logger.error("Error finding customer: " + e.getMessage());
            throw new RuntimeException("Customer lookup failed", e);
        }
        
        logger.info("Found account {} for customer {}", account.getId(), customerId);
        account.setCustomer(customer);
        
        BigDecimal currentBalance = account.getBalance();
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        
        logger.info("Current balance is {}, adding {}", currentBalance, amount);
        account.setBalance(currentBalance.add(amount));
        accountRepository.save(account);
        transactionService.createTransaction(account, amount, "ADD_MONEY");
        
        // Legacy: Manual audit logging
        String auditEntry = "ADD_MONEY: $" + amount + " added to customer " + customerId + " account " + account.getId() + " at " + new Date();
        transactionHistory.addElement(auditEntry);
        
        logger.info("Successfully added money to account {}", account.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByUsername(String username) {
        // Legacy: Manual string validation using commons-lang 2.x
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        Account account = customerRepository.findByCustomerName(username)
                .map(Customer::getAccount)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for username: " + username));
                
        // Legacy: Direct Hibernate usage instead of JPA patterns
        Hibernate.initialize(account.getCustomer()); // Initialize the customer
        
        // Legacy: Manual audit trail
        transactionHistory.addElement("Account lookup by username: " + username + " at " + new Date());
        
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountByCustomerId(Long customerId) {
        // Legacy: Verbose null checking
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID: " + customerId);
        }
        
        Account account = accountRepository.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    Customer customer = customerRepository.findById(customerId).orElse(null);
                    String customerName = customer != null ? customer.getCustomerName() : "Unknown";
                    return new AccountNotFoundException("Account not found for customer id: " + customerId, customerId, customerName);
                });
                
        // Legacy: Direct Hibernate usage
        Hibernate.initialize(account.getCustomer()); // Initialize the customer
        
        return account;
    }
    
    // Legacy: Method to get transaction history using Vector enumeration
    public String getTransactionHistoryReport() {
        StringBuilder report = new StringBuilder();
        report.append("Transaction History Report\n");
        report.append("=========================\n");
        
        // Legacy: Using Enumeration instead of Iterator
        Enumeration<String> elements = transactionHistory.elements();
        int count = 1;
        while (elements.hasMoreElements()) {
            String transaction = elements.nextElement();
            report.append(count++).append(". ").append(transaction).append("\n");
        }
        
        return report.toString();
    }
}