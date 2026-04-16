package com.example.customerservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String type;
    private String fromCustomer;
    private String toCustomer;

    // Constructors
    public TransactionDTO() {
    }

    public TransactionDTO(Long id, BigDecimal amount, LocalDateTime timestamp, String type, String fromCustomer, String toCustomer) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
        this.fromCustomer = fromCustomer;
        this.toCustomer = toCustomer;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromCustomer() {
        return fromCustomer;
    }

    public void setFromCustomer(String fromCustomer) {
        this.fromCustomer = fromCustomer;
    }

    public String getToCustomer() {
        return toCustomer;
    }

    public void setToCustomer(String toCustomer) {
        this.toCustomer = toCustomer;
    }
}