package com.example.customerservice.repository;

import com.example.customerservice.model.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = "account")
    Optional<Customer> findByCustomerName(String customerName);

    @Override
    @EntityGraph(attributePaths = "account")
    Optional<Customer> findById(Long id);

}