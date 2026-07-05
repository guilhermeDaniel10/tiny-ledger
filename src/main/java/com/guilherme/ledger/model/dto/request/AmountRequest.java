package com.guilherme.ledger.model.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AmountRequest(
        @NotNull(message = "amount is required")
        @Positive(message = "amount must be positive")
        @Digits(integer = 19, fraction = 2)
        BigDecimal amount) {
}
