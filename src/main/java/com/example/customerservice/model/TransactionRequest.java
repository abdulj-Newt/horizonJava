package com.example.customerservice.model;

import java.math.BigDecimal;

public class TransactionRequest {
    private Long fromCustomerId;
    private Long toCustomerId;
    private BigDecimal amount;

    // Getters and setters
    public Long getFromCustomerId() {
        return fromCustomerId;
    }

    public void setFromCustomerId(Long fromCustomerId) {
        this.fromCustomerId = fromCustomerId;
    }

    public Long getToCustomerId() {
        return toCustomerId;
    }

    public void setToCustomerId(Long toCustomerId) {
        this.toCustomerId = toCustomerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}