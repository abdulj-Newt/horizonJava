package com.example.customerservice.model;

import java.math.BigDecimal;

public class AmountRequest {
    private BigDecimal amount;

    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}