package com.guilherme.ledger.model.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AmountRequest(
        @NotNull
        @Positive
        @Digits(integer = 19, fraction = 2)
        BigDecimal amount) {
}
