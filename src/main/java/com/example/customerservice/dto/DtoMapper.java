package com.example.customerservice.dto;

import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.Transaction;

import java.util.stream.Collectors;

public class DtoMapper {

    public static CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDTO(
                customer.getId(),
                customer.getCustomerName(),
                toAccountDTO(customer.getAccount())
        );
    }

    public static AccountDTO toAccountDTO(Account account) {
        if (account == null) {
            return null;
        }
        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getTransactions().stream().map(DtoMapper::toTransactionDTO).collect(Collectors.toList())
        );
    }

    public static TransactionDTO toTransactionDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTimestamp(),
                transaction.getType(),
                transaction.getFromCustomer().getCustomerName(),
                transaction.getToCustomer().getCustomerName()
        );
    }
}