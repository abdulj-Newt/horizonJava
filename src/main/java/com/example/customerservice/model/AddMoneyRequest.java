package com.example.customerservice.model;

import java.math.BigDecimal;

public class AddMoneyRequest {
    private Long customerId;
    private BigDecimal amount;

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}