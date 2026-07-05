package com.guilherme.ledger.model.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * A single deposit or withdrawal and the balance left after it.
 */
public record Transaction(UUID id,
                          TransactionType type,
                          BigDecimal amount,
                          BigDecimal balanceAfter,
                          Instant timestamp) {

    /**
     * Creates a transaction with a generated id and the current timestamp.
     */
    public static Transaction create(TransactionType type, BigDecimal amount, BigDecimal balanceAfter) {
        return new Transaction(UUID.randomUUID(), type, amount, balanceAfter, Instant.now());
    }
}
