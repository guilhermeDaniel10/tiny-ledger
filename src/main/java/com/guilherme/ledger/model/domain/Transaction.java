package com.guilherme.ledger.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(BigDecimal amount, TransactionType type, LocalDateTime date) {
}
