package com.example.customerservice.config;

import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;
import com.example.customerservice.repository.AccountRepository;
import com.example.customerservice.repository.CustomerRepository;
import com.example.customerservice.repository.TransactionRepository;
import com.example.customerservice.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.customerservice.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(CustomerRepository customerRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionService transactionService, JdbcTemplate jdbcTemplate) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {

        // Delete existing data
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        // Reset sequences (works for Oracle, adjust for your DB)
        try {
            jdbcTemplate.execute("ALTER SEQUENCE ACCOUNT_SEQ RESTART START WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE CUSTOMER_SEQ RESTART START WITH 1");
            jdbcTemplate.execute("ALTER SEQUENCE transaction_seq RESTART START WITH 1");
            
            jdbcTemplate.execute("CREATE OR REPLACE PROCEDURE TRANSFER_MONEY (\n"
            + "    p_from_customer_id IN NUMBER,\n"
            + "    p_to_customer_id IN NUMBER,\n"
            + "    p_amount IN NUMBER\n"
            + ")\n"
            + "AS\n"
            + "    v_from_account_id NUMBER;\n"
            + "    v_to_account_id NUMBER;\n"
            + "    v_from_balance NUMBER;\n"
            + "    v_transaction_id NUMBER;\n"
            + "BEGIN\n"
            + "    -- Get account IDs for the customers\n"
            + "    SELECT id INTO v_from_account_id FROM accounts WHERE customer_id = p_from_customer_id;\n"
            + "    SELECT id INTO v_to_account_id FROM accounts WHERE customer_id = p_to_customer_id;\n"
            + "\n"
            + "    -- Check sender's balance\n"
            + "    SELECT balance INTO v_from_balance FROM accounts WHERE id = v_from_account_id;\n"
            + "\n"
            + "    IF v_from_balance >= p_amount THEN\n"
            + "        -- Debit sender's account\n"
            + "        UPDATE accounts SET balance = balance - p_amount WHERE id = v_from_account_id;\n"
            + "\n"
            + "        -- Credit receiver's account\n"
            + "        UPDATE accounts SET balance = balance + p_amount WHERE id = v_to_account_id;\n"
            + "\n"
            + "        -- Record the debit transaction for the sender\n"
            + "        INSERT INTO transactions (id, account_id, type, amount, timestamp, from_customer, to_customer)\n"
            + "        VALUES (TRANSACTION_SEQ.NEXTVAL, v_from_account_id, 'DEBIT', -p_amount, SYSDATE, p_from_customer_id, p_to_customer_id);\n"
            + "\n"
            + "        -- Record the credit transaction for the receiver\n"
            + "        INSERT INTO transactions (id, account_id, type, amount, timestamp, from_customer, to_customer)\n"
            + "        VALUES (TRANSACTION_SEQ.NEXTVAL, v_to_account_id, 'CREDIT', p_amount, SYSDATE, p_from_customer_id, p_to_customer_id);\n"
            + "\n"
            + "        COMMIT;\n"
            + "    ELSE\n"
            + "        -- Raise an error if insufficient funds\n"
            + "        RAISE_APPLICATION_ERROR(-20001, 'Insufficient funds');\n"
            + "    END IF;\n"
            + "EXCEPTION\n"
            + "    WHEN NO_DATA_FOUND THEN\n"
            + "        RAISE_APPLICATION_ERROR(-20002, 'Customer or account not found');\n"
            + "    WHEN OTHERS THEN\n"
            + "        ROLLBACK;\n"
            + "        RAISE;\n"
            + "END;\n"
            + "");
            System.out.println("ACCOUNT_SEQ, CUSTOMER_SEQ, and transaction_seq reset to 1.");
        } catch (Exception seqEx) {
            System.out.println("Could not reset sequences: " + seqEx.getMessage());
        }


        // Insert customers
        Customer johnDoe = new Customer();
        johnDoe.setCustomerName("John Doe");
        customerRepository.save(johnDoe);

        Customer janeSmith = new Customer();
        janeSmith.setCustomerName("Jane Smith");
        customerRepository.save(janeSmith);

        Customer peterJones = new Customer();
        peterJones.setCustomerName("Peter Jones");
        customerRepository.save(peterJones);

        Customer charlie = new Customer();
        charlie.setCustomerName("Charlie");
        customerRepository.save(charlie);

        // Insert accounts
        Account johnsAccount = new Account();
        johnsAccount.setAccountNumber("1001");
        johnsAccount.setBalance(new BigDecimal("100000.00"));
        johnsAccount.setCustomer(johnDoe);
        accountRepository.save(johnsAccount);

        Account janesAccount = new Account();
        janesAccount.setAccountNumber("1002");
        janesAccount.setBalance(new BigDecimal("100000.00"));
        janesAccount.setCustomer(janeSmith);
        accountRepository.save(janesAccount);

        Account petersAccount = new Account();
        petersAccount.setAccountNumber("1003");
        petersAccount.setBalance(new BigDecimal("100000.00"));
        petersAccount.setCustomer(peterJones);
        accountRepository.save(petersAccount);

        Account charliesAccount = new Account();
        charliesAccount.setAccountNumber("1004");
        charliesAccount.setBalance(new BigDecimal("100000.00"));
        charliesAccount.setCustomer(charlie);
        accountRepository.save(charliesAccount);

        // Insert transactions
        // Transaction transaction1 = new Transaction();
        
        // System.out.println("Transaction 1 Account: " + johnsAccount);
        // System.out.println("Transaction 1 Amount: " + new BigDecimal("100.00"));
        // System.out.println("Transaction 1 From Customer: " + johnDoe);
        // System.out.println("Transaction 1 To Customer: " + janeSmith);
        // System.out.println("Transaction 1 Timestamp: " + LocalDateTime.now());
        // System.out.println("Transaction 1 Type: " + "TRANSFER");

        // transaction1.setAccount(johnsAccount);
        // transaction1.setAmount(new BigDecimal("100.00"));
        // transaction1.setFromCustomer(johnDoe);
        // transaction1.setToCustomer(janeSmith);
        // transaction1.setTimestamp(LocalDateTime.now());
        // transaction1.setType("TRANSFER");
        // transactionRepository.save(transaction1);


        // Transaction transaction2 = new Transaction();
        // transaction2.setAccount(janesAccount);
        // transaction2.setAmount(new BigDecimal("50.00"));
        // transaction2.setFromCustomer(janesAccount.getCustomer());
        // transaction2.setToCustomer(petersAccount.getCustomer());
        // transaction2.setTimestamp(LocalDateTime.now());
        // transaction2.setType("TRANSFER");
        // transactionRepository.save(transaction2);

        // // Retrieve and print transactions for each customer
        // List<Customer> customers = customerRepository.findAll();
        // for (Customer customer : customers) {
        //     System.out.println("Transactions for customer: " + customer.getCustomerName());
        //     List<Transaction> transactions = transactionService.getTransactionsByCustomerId(customer.getId());
        //     for (Transaction transaction : transactions) {
        //         System.out.print("  Transaction ID: " + transaction.getId());
        //         System.out.print("  Amount: " + transaction.getAmount());
        //         System.out.print("  Type: " + transaction.getType());
        //         System.out.print("  Timestamp: " + transaction.getTimestamp());
        //         System.out.println();
        //     }
            
        // }
    }
}