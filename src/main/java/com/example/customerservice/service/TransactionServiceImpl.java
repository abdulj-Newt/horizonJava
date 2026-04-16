package com.example.customerservice.service;

import com.example.customerservice.exception.AccountNotFoundException;
import com.example.customerservice.exception.InsufficientFundsException;
import com.example.customerservice.exception.InvalidAmountException;
import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.Transaction;
import com.example.customerservice.repository.AccountRepository;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Transaction transferMoney(Long fromCustomerId, Long toCustomerId, BigDecimal amount) {
        Customer fromCustomer = customerRepository.findById(fromCustomerId)
                .orElseThrow(() -> new AccountNotFoundException("From account not found"));
        Customer toCustomer = customerRepository.findById(toCustomerId)
                .orElseThrow(() -> new AccountNotFoundException("To account not found"));

        Account fromAccount = fromCustomer.getAccount();
        Account toAccount = toCustomer.getAccount();
        if (amount == null || amount.signum() <= 0) {
             throw new InvalidAmountException("Amount must be positive");
        }
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setFromCustomer(fromCustomer);
        debitTransaction.setToCustomer(toCustomer);
        debitTransaction.setAmount(amount.negate());
        debitTransaction.setTimestamp(LocalDateTime.now());
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setType("DEBIT");
        transactionRepository.save(debitTransaction);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setFromCustomer(fromCustomer);
        creditTransaction.setToCustomer(toCustomer);
        creditTransaction.setAmount(amount);
        creditTransaction.setTimestamp(LocalDateTime.now());
        creditTransaction.setAccount(toAccount);
        creditTransaction.setType("CREDIT");
        return transactionRepository.save(creditTransaction);
    }

    @Override
    public Transaction transferMoneyByPLSQL(Long fromCustomerId, Long toCustomerId, BigDecimal amount) {
        logger.info("Using stored procedure to transfer money from customer {} to customer {}", fromCustomerId, toCustomerId);
        transactionRepository.transferMoneyByPLSQL(fromCustomerId, toCustomerId, amount);
        Customer fromCustomer = customerRepository.findById(fromCustomerId)
                .orElseThrow(() -> new AccountNotFoundException("From account not found"));
        return transactionRepository.findFirstByAccount_IdOrderByTimestampDesc(fromCustomer.getAccount().getId());
    }

    @Override
    public Transaction payBill(Long customerId, String billType, String accountNumber, BigDecimal amount) {
        logger.info("Processing bill payment for customer {} - Bill Type: {}, Account: {}, Amount: {}", 
                   customerId, billType, accountNumber, amount);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Customer account not found"));
        
        Account account = customer.getAccount();
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for bill payment");
        }
        
        // Deduct amount from customer's account
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        
        // Create transaction record for bill payment
        Transaction billPaymentTransaction = new Transaction();
        billPaymentTransaction.setFromCustomer(customer);
        billPaymentTransaction.setToCustomer(customer); // Bill payments are to external entities
        billPaymentTransaction.setAmount(amount.negate()); // Negative amount for debit
        billPaymentTransaction.setTimestamp(LocalDateTime.now());
        billPaymentTransaction.setAccount(account);
        billPaymentTransaction.setType("BILL_PAY_" + billType.toUpperCase());
        
        return transactionRepository.save(billPaymentTransaction);
    }

    @Override
    public Transaction payBillByPLSQL(Long customerId, String billType, String accountNumber, BigDecimal amount) {
        logger.info("Using stored procedure to pay bill for customer {} - Bill Type: {}, Account: {}, Amount: {}", 
                   customerId, billType, accountNumber, amount);
        
        transactionRepository.payBillByPLSQL(customerId, billType, accountNumber, amount);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Customer account not found"));
        
        return transactionRepository.findFirstByAccount_IdOrderByTimestampDesc(customer.getAccount().getId());
    }

    @Override
    public Transaction addMoney(Long customerId, BigDecimal amount) {
        logger.info("Adding money for customer {} - Amount: {}", customerId, amount);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Customer account not found"));
        
        Account account = customer.getAccount();
        
        // Add amount to customer's account
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        
        // Create transaction record for money addition
        Transaction addMoneyTransaction = new Transaction();
        addMoneyTransaction.setFromCustomer(customer);
        addMoneyTransaction.setToCustomer(customer);
        addMoneyTransaction.setAmount(amount);
        addMoneyTransaction.setTimestamp(LocalDateTime.now());
        addMoneyTransaction.setAccount(account);
        addMoneyTransaction.setType("ADD_MONEY");
        
        return transactionRepository.save(addMoneyTransaction);
    }

    @Override
    public Transaction addMoneyByPLSQL(Long customerId, BigDecimal amount) {
        logger.info("Using stored procedure to add money for customer {} - Amount: {}", customerId, amount);
        
        transactionRepository.addMoneyByPLSQL(customerId, amount);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Customer account not found"));
        
        return transactionRepository.findFirstByAccount_IdOrderByTimestampDesc(customer.getAccount().getId());
    }

    @Override
    public List<Transaction> getTransactions(String customerName) {
        Customer customer = customerRepository.findByCustomerName(customerName)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for customer: " + customerName));
        return transactionRepository.findByAccount_Customer(customer);
    }

    @Override
    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for customer ID: " + customerId));
        return transactionRepository.findByCustomerId(customerId);
    }

    @Override
    public void createTransaction(Account account, java.math.BigDecimal amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(type);
        transaction.setFromCustomer(account.getCustomer());
        transaction.setToCustomer(account.getCustomer());
        transactionRepository.save(transaction);
    }

}