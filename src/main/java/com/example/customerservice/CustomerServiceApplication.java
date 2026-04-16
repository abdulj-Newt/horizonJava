package com.example.customerservice;

import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.Transaction;
import com.example.customerservice.repository.AccountRepository;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository customerRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        return (args) -> {
            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : customerRepository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch all accounts
            log.info("Accounts found with findAll():");
            log.info("-------------------------------");
            for (Account account : accountRepository.findAll()) {
                log.info(account.toString());
            }
            log.info("");

            // fetch all transactions
            log.info("Transactions found with findAll():");
            log.info("-------------------------------");
            for (Transaction transaction : transactionRepository.findAll()) {
                log.info(transaction.toString());
            }
            log.info("");
        };
    }

}