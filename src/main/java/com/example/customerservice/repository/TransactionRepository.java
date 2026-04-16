package com.example.customerservice.repository;

import com.example.customerservice.model.Account;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

// @Repository
// public interface TransactionRepository extends JpaRepository<Transaction, Long> {
//     List<Transaction> findByAccount(Account account);
//     List<Transaction> findByAccount_Customer(com.example.customerservice.model.Customer customer);
//     List<Transaction> findByFromCustomerOrToCustomer(Customer fromCustomer, Customer toCustomer);
//     Transaction findFirstByAccount_IdOrderByTimestampDesc(Long accountId);
//     @Query("SELECT t FROM Transaction t WHERE t.account.customer.id = :customerId")
//     List<Transaction> findByCustomerId(@Param("customerId") Long customerId);

//     @Procedure(procedureName = "TRANSFER_MONEY")
//     void transferMoneyByPLSQL(@Param("p_from_customer_id") Long fromCustomerId, @Param("p_to_customer_id") Long toCustomerId, @Param("p_amount") BigDecimal amount);
// }

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);

    List<Transaction> findByAccount_Customer(com.example.customerservice.model.Customer customer);

    List<Transaction> findByFromCustomerOrToCustomer(Customer fromCustomer, Customer toCustomer);

    Transaction findFirstByAccount_IdOrderByTimestampDesc(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.customer.id = :customerId")
    List<Transaction> findByCustomerId(@Param("customerId") Long customerId);

    @Procedure(procedureName = "TRANSFER_MONEY")
    void transferMoneyByPLSQL(@Param("p_from_customer_id") Long fromCustomerId,
                               @Param("p_to_customer_id") Long toCustomerId,
                               @Param("p_amount") BigDecimal amount);

    @Procedure(procedureName = "PAY_BILL")
    void payBillByPLSQL(@Param("p_customer_id") Long customerId,
                        @Param("p_bill_type") String billType,
                        @Param("p_account_number") String accountNumber,
                        @Param("p_amount") BigDecimal amount);

    @Procedure(procedureName = "ADD_MONEY")
    void addMoneyByPLSQL(@Param("p_customer_id") Long customerId,
                         @Param("p_amount") BigDecimal amount);

    @Query(value = "SELECT * FROM transaction t WHERE NVL(t.amount, 0) > :minAmount", nativeQuery = true)
    List<Transaction> findWithNonNullAmountGreaterThan(@Param("minAmount") BigDecimal minAmount);

    @Query(value = "SELECT * FROM transaction t WHERE t.timestamp >= SYSDATE - :days", nativeQuery = true)
    List<Transaction> findTransactionsInLastNDays(@Param("days") int days);

    @Query(value = "SELECT * FROM transaction t WHERE INSTR(LOWER(t.description), LOWER(:keyword)) > 0", nativeQuery = true)
    List<Transaction> findByDescriptionContaining(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM transaction t WHERE NVL(t.amount, 0) > :minAmount AND t.timestamp >= SYSDATE - :days AND INSTR(LOWER(t.description), LOWER(:keyword)) > 0", nativeQuery = true)
    List<Transaction> searchTransactions(@Param("minAmount") BigDecimal minAmount,
                                         @Param("days") int days,
                                         @Param("keyword") String keyword);
}
